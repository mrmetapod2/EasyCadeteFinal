package com.example.easycadete


import android.R.attr.duration
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch


//uso esta clase para armar un esqueleto de una base de datos para las pruebas
class BaseDeDatos(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
    {
        companion object {
            //esto va a ser modificado para que sea exactamente la base de datos que usemos
         const val DATABASE_NAME = "example.db"
         const val DATABASE_VERSION = 5
         const val TABLE_NAME = "Persona"
            const val COLUMN_ID="ID"

            const val USUARIO_TABLE_NAME= "Usuario"
            const val CADETE_TABLE_NAME= "Cadete"


            //estas son las tablas actuales de la base de datos
        private const val PERSONA_TABLE =
            /*"CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT NOT NULL," +
                    "$CADETE_O_USUARIO TEXT NOT NULL," +
                    "$COLUMN_CONT TEXT NOT NULL) "*/
            "CREATE TABLE $TABLE_NAME (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Nombre VARCHAR(100) NOT NULL," +
                    "Apellido VARCHAR(100) NOT NULL," +
                    "Contraseña VARCHAR(100) NOT NULL," +
                    "Edad INTEGER NOT NULL," +
                    "DNI VARCHAR(20) NOT NULL," +
                    "Email VARCHAR(100) NOT NULL," +
                    "Telefono VARCHAR(20))"
        private const val USUARIO_TABLE =
            "CREATE TABLE $USUARIO_TABLE_NAME (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ID_Persona INTEGER NOT NULL," +
                    "FOREIGN KEY (ID_Persona) REFERENCES Persona(ID)" +
                    ")"
        private const val CADETE_TABLE =
            "CREATE TABLE $CADETE_TABLE_NAME (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ID_Persona INTEGER NOT NULL," +
                    "Disponibilidad BOOLEAN DEFAULT 1," +
                    "FOREIGN KEY (ID_Persona) REFERENCES Persona(ID)" +
                    ")"


    }
        //maneje de versiones
        override fun onCreate(db: SQLiteDatabase) {

            db.execSQL(PERSONA_TABLE)
            db.execSQL(USUARIO_TABLE)
            db.execSQL(CADETE_TABLE)

        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

}
class NivelDatabase(context: Context) {
    //conecto a la base de datos
    private lateinit  var baseDeDatos : BaseDeDatos

    //CUANDO TRABAJES EN ESTO REEMPLAZA CON TU IP
    val IP="192.168.0.2"
    //url para la base de datos
    val url= String.format("http://%S/easycadete/server.php",IP)
    val client = OkHttpClient()
    val urlEmail=String.format("http://%S/easycadete/sendEmail.php",IP)

    fun EstaEnBDD(context: Context,Nombre :String, Contraseña: String) : ResultadoPersona {
        //preparo la variable que va a retornar
        var resultadoPersona = ResultadoPersona()


        //preparo la querry para la base de datos con los datos extraidos
         val query=String.format("SELECT * FROM `persona` WHERE `Nombre`='%S' and `Contraseña`='%S'", Nombre, Contraseña)
        //genero un form con la query
        val formBody = FormBody.Builder()
            .add("sql", query)

            .build()
        //lo combierto en un request
        val request= Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        //creo una coundownlatch para que se espere hasta que termine el servidor
        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {




            override fun onFailure(call: Call, e: IOException) {

                e.printStackTrace()
                countDownLatch.countDown();
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.isSuccessful) {
                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    val responseData = response.body!!.string()
                    println(responseData)
                    try { //se intenta crear un jsonobject a base de lo que hay en la base de datos
                        val jsonObject = JSONObject(responseData)
                        println(jsonObject)
                        //si el usuario esta verificado
                        if (jsonObject.optString("Verificacion")=="1") {
                            //se modifica resultado persona con los datos
                            resultadoPersona = ResultadoPersona(
                                ID = jsonObject.getString("ID"),
                                Nombre = jsonObject.getString("Nombre"),
                                Contraseña = jsonObject.getString("Contraseña"),
                                Apellido = jsonObject.getString("Apellido"),
                                Edad = jsonObject.getString("Edad"),
                                DNI = jsonObject.getString("DNI"),
                                Email = jsonObject.getString("Email"),

                                Locacion = jsonObject.optString("Locacion"),
                                Telefono = jsonObject.optString("Telefono")
                            )
                            //de aqui se realiza una query para ver si es cadete o Usuario
                            val query=String.format("SELECT * FROM `usuario` WHERE `ID_Persona`='%S'", resultadoPersona.ID)
                            //genero la form con lo que estoy buscando
                            val formBody = FormBody.Builder()
                                .add("sql", query)

                                .build()
                            //transfomo la form a una request
                            val request= Request.Builder()
                                .url(url)
                                .post(formBody)
                                .build()
                            //realizo la request con manejo de errores
                            val countDownLatch2 = CountDownLatch(1)
                            client.newCall(request).enqueue(object : Callback {




                                override fun onFailure(call: Call, e: IOException) {

                                    e.printStackTrace()
                                    countDownLatch2.countDown();
                                }
                                override fun onResponse(call: Call, response: Response) {

                                    val responseData = response.body!!.string()
                                    println(responseData)
                                    try {
                                        val jsonObject = JSONObject(responseData)
                                        if (!jsonObject.optString("NombreUsuario")
                                                .isNullOrEmpty()
                                        ) {
                                            resultadoPersona.UsuarioOCadete = "Usuario"
                                        }
                                    }
                                    catch (e: Exception) {
                                        e.printStackTrace()

                                    }
                                    countDownLatch2.countDown();

                            }
                            })
                            countDownLatch2.await()
                            val query2=String.format("SELECT * FROM `cadete` WHERE `ID_Persona`='%S'", resultadoPersona.ID)
                            //genero la form con lo que estoy buscando
                            val formBody2 = FormBody.Builder()
                                .add("sql", query2)

                                .build()
                            //transfomo la form a una request
                            val request2= Request.Builder()
                                .url(url)
                                .post(formBody2)
                                .build()
                            val countDownLatch3 = CountDownLatch(1)
                            client.newCall(request2).enqueue(object : Callback {




                                override fun onFailure(call: Call, e: IOException) {

                                    e.printStackTrace()
                                    countDownLatch3.countDown();
                                }
                                override fun onResponse(call: Call, response: Response) {

                                    val responseData = response.body!!.string()
                                    println(responseData)
                                    try {
                                        val jsonObject = JSONObject(responseData)
                                        if (!jsonObject.optString("NombreUsuario")
                                                .isNullOrEmpty()
                                        ) {
                                            resultadoPersona.UsuarioOCadete = "Cadete"
                                        }
                                    }
                                    catch (e: Exception) {
                                        e.printStackTrace()

                                    }
                                    countDownLatch3.countDown();

                                }

                            })
                            countDownLatch3.await()

                        }
                        countDownLatch.countDown()
                    }




                    catch (e: Exception) {
                        //si el crear el jsonobject falla se retorna un ResultadoPersona vacio
                        e.printStackTrace()
                        countDownLatch.countDown()
                    }




                }
            }
        })

        println("antes del wait")
        countDownLatch.await()
        println(resultadoPersona.Nombre)




            //si coincide la data dada con lo puesto en Nombre y contraseña se retorna
            if (resultadoPersona.ID!=null){
                return resultadoPersona
            }

            return ResultadoPersona()




    }

    fun AñadirABDD(context: Context ,Nombre :String, Contraseña: String,Apellido:String,Edad:String,DNI:String,Email:String, EsUsuario: Boolean){
        //variable establecida para saber donde esta la nueva persona
        var insertedID=0;




        //genero la query de sql
        val query=String.format("INSERT INTO `persona`( `Nombre`, `Contraseña`, `Apellido`, `Edad`, `DNI`, `Email`) " +
                "VALUES ('%S','%S','%S','%S','%S','%S')", Nombre, Contraseña, Apellido, Edad, DNI, Email)
        //genero la form con lo que estoy buscando
        val formBody = FormBody.Builder()
            .add("sql", query)

            .build()
        //transfomo la form a una request
        val request= Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        //realizo la request con manejo de errores
        client.newCall(request).enqueue(object : Callback {
            //por si a caso algo falla
            override fun onFailure(call: Call, e: IOException) {

                e.printStackTrace()

            }

            override fun onResponse(call: Call, response: Response) {
                //response.use es necesario para usar lo que te da el codigo php
                response.use {
                    //si response es un error entonces lo tira
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    //sino continua mostrando los puntos individuales que sucedieron en la conexion
                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    //se crea una string con la respuesta del SQL siempre va a ser el ID del usuario puesto
                    val ResponseNumber=response.body!!.string()
                    //si se puede combertir en un numero se hace
                    if (ResponseNumber.toDoubleOrNull() != null){


                        insertedID= ResponseNumber.toInt()
                        println(insertedID)
                        //se realiza otro insert con insertedID como clave foranea para usuario o cadete dependiendo lo decidido por el usuario
                        if (EsUsuario== true){
                            val query= String.format( "INSERT INTO `usuario`( `ID_Persona`, `NombreUsuario`) VALUES ('%S','%S')", insertedID, Nombre)
                            val formBody = FormBody.Builder()
                                .add("sql", query)

                                .build()
                            //transfomo la form a una request
                            val request= Request.Builder()
                                .url(url)
                                .post(formBody)
                                .build()

                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    e.printStackTrace()
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    response.use {
                                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                                        for ((name, value) in response.headers) {
                                             println("$name: $value")
                                         }
                                        println(response.body!!.string())
                                    }
                                }
                            })

                        }

                        else{

                            val query= String.format("INSERT INTO `cadete`( `ID_Persona`, `NombreUsuario`) VALUES ('%S','%S')", insertedID, Nombre)
                            val formBody = FormBody.Builder()
                                .add("sql", query)

                                .build()
                            //transfomo la form a una request
                            val request= Request.Builder()
                                .url(url)
                                .post(formBody)
                                .build()
                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    e.printStackTrace()
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    response.use {
                                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                                        for ((name, value) in response.headers) {
                                            println("$name: $value")
                                        }
                                        println(response.body!!.string())
                                    }
                                }
                            })

                        }

                    }
                    else{
                        (context as Registrar).runOnUiThread {
                            Toast.makeText(context, "Email already in use", Toast.LENGTH_SHORT).show()


                        }
                    }


                }
            }
        })







    }
    fun AsignarCadetes(context: Context): MutableList<ResultadoPersona> {
        //creo la lista de output
        val CadetesDisponibles = mutableListOf<ResultadoPersona>()
        //armo una variable que tendra los resultados de la query
        var responseText=""
        //genero la query de sql
        val query="SELECT cadete.Disponibilidad, persona.ID, persona.Locacion, persona.Nombre, persona.Contraseña, persona.Apellido, persona.Edad, persona.DNI, persona.Email FROM persona, cadete WHERE cadete.ID_Persona = persona.ID AND persona.Verificacion=1 AND cadete.Disponibilidad=1;"
        //genero la form con lo que estoy buscando
        val formBody = FormBody.Builder()
            .add("sql", query)

            .build()
        //transfomo la form a una request
        val request= Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        //creo un countdown para esperar al server
        val countDownLatch = CountDownLatch(1)
        //realizo la request con manejo de errores
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                e.printStackTrace()

            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    responseText = response.body!!.string()
                    countDownLatch.countDown()
                }
            }
        })
        //espero hasta que toda la logica del servidor este completada
        countDownLatch.await()
        println("lalalan")
        println(responseText)


        // corto el input a una serie de strings con cada respuesta
        val jsonStrings = responseText.split("<br>")
        // creo la lista de json donde va a ir la info
        val jsonObjects = mutableListOf<JSONObject>()

        // voy por las strings y las convierto en jsonobjects
        for (jsonString in jsonStrings) {
            if (jsonString.isNotEmpty()) {
                val jsonObject = JSONObject(jsonString)
                // cuando ya son JSONobject lo añado a los resultados
                val resultadoPersona = ResultadoPersona(
                    ID = jsonObject.getString("ID"),
                    Nombre = jsonObject.getString("Nombre"),
                    Contraseña = jsonObject.getString("Contraseña"),
                    Apellido = jsonObject.getString("Apellido"),
                    Edad = jsonObject.getString("Edad"),
                    DNI = jsonObject.getString("DNI"),
                    Email = jsonObject.getString("Email"),

                    Locacion = jsonObject.optString("Locacion"),
                    Telefono = jsonObject.optString("Telefono"),
                    //el resultado siempre va a ser cadetes porque sale de la tabla de cadetes
                    UsuarioOCadete = "Cadete"
                )
                CadetesDisponibles.add(resultadoPersona)
            }
        }











        //retorno lista de personas
        return CadetesDisponibles

    }
}
