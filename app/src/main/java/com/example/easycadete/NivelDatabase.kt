package com.example.easycadete

import android.content.ContentValues
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


import java.io.IOException

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

    fun EstaEnBDD(context: Context,Nombre :String, Contraseña: String) : ResultadoPersona {

        //convierto la base de datos a una variable
        baseDeDatos = BaseDeDatos(context)
        val db = baseDeDatos.readableDatabase
        //genero un resultado a base de lo buscado
         val query=String.format("SELECT * FROM `persona` WHERE `Nombre`=%S and `Contraseña`=%S", Nombre, Contraseña)
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




            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.isSuccessful) {
                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    val responseData = response.body!!.string()
                    println(responseData)
                        //Toast.makeText(context, responseData, Toast.LENGTH_SHORT)
                          //  .show()


                }
            }
        })




        val resultado= db.query(
            BaseDeDatos.TABLE_NAME,
            arrayOf(BaseDeDatos.COLUMN_ID,"Nombre", "Contraseña", "Apellido", "Edad", "DNI", "Email"),
            "Nombre = ? AND Contraseña = ?",
            arrayOf(Nombre, Contraseña),
            null,
            null,
            null
        )
        //se crea un cursor con todos los casos encontrados en la base de datos
        val cursor = resultado



        //si no esta vacio sacar el nombre contraseña y estado si es usuario o cadete
        if (cursor != null && cursor.moveToFirst()){
            println(cursor)
            //se chequea con db.query si la ID de la persona coincide con una ID_persona adentro de ellas
            val resUsuario= db.query(
                BaseDeDatos.USUARIO_TABLE_NAME,
                arrayOf(BaseDeDatos.COLUMN_ID , "ID_Persona"),
                "ID_Persona = ? ",
                arrayOf(cursor.getString(cursor.getColumnIndexOrThrow("ID"))),
                null,
                null,
                null
            )
            val resCadete= db.query(
                "Cadete",
                arrayOf(BaseDeDatos.COLUMN_ID, "ID_Persona"),
                "ID_Persona = ? ",
                arrayOf(cursor.getString(cursor.getColumnIndexOrThrow("ID"))),
                null,
                null,
                null
            )
            var UsuarioOCadete: String


            //si no esta en usuario o cadete esta null
            if (resUsuario!=null&&resUsuario.moveToFirst()){

                println(resUsuario.getString(resUsuario.getColumnIndexOrThrow("ID_Persona")))
                UsuarioOCadete="Usuario"
            }
            else if (resCadete!=null&&resCadete.moveToFirst()){
                println(resCadete.getString(resCadete.getColumnIndexOrThrow("ID_Persona")))
                UsuarioOCadete="Cadete"
            }
            else{ UsuarioOCadete="null"}
            //convierto los datos conseguidos en variables
            val dbNombre =cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
            val dbContraseña = cursor.getString(cursor.getColumnIndexOrThrow("Contraseña"))
            val dbApellido = cursor.getString(cursor.getColumnIndexOrThrow("Apellido"))
            val dbID =cursor.getString(cursor.getColumnIndexOrThrow("ID"))
            val dbEdad = cursor.getString(cursor.getColumnIndexOrThrow("Edad"))
            val dbDNI = cursor.getString(cursor.getColumnIndexOrThrow("DNI"))
            val dbEmail =cursor.getString(cursor.getColumnIndexOrThrow("Email"))

            //si coincide la data dada con lo puesto en Nombre y contraseña se retorna
            if (dbNombre == Nombre && dbContraseña == Contraseña) {
                cursor?.close()
               return ResultadoPersona( dbNombre, dbContraseña, dbID,dbApellido,dbEdad,dbDNI,dbEmail,null,UsuarioOCadete)

            }
            else{
                println("no se encontro usuario y contrasela de ese tipo")
                cursor?.close()
                return ResultadoPersona( )

            }

        }
        else{
            //en este el cursor no encontro nada
            cursor?.close()
            return ResultadoPersona( )
        }



    }

    fun AñadirABDD(context: Context ,Nombre :String, Contraseña: String,Apellido:String,Edad:String,DNI:String,Email:String, EsUsuario: Boolean){
        var insertedID=0;



        //genero un resultado a base de lo buscado
        val query=String.format("INSERT INTO `persona`( `Nombre`, `Contraseña`, `Apellido`, `Edad`, `DNI`, `Email`,  `esUsuario`) " +
                "VALUES ('%S','%S','%S','%S','%S','%S','%S')", Nombre, Contraseña, Apellido, Edad, DNI, Email, EsUsuario)
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
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    //se pone la id para que sea cadete o usuario
                     insertedID= response.body!!.string().toInt()
                    println(insertedID)
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
            }
        })







    }
    fun AsignarCadetes(context: Context): MutableList<ResultadoPersona> {
        //convierto la base de datos a una variable
        baseDeDatos = BaseDeDatos(context)
        val db = baseDeDatos.readableDatabase
        //creo una variable con todos los cadetes disponibles
        val CadetesDisponibles = mutableListOf<ResultadoPersona>()
        //genero un resultado a base de lo buscado
        val resultado= db.query(
            BaseDeDatos.CADETE_TABLE_NAME,
            arrayOf(BaseDeDatos.COLUMN_ID,"ID_Persona", "Disponibilidad"),
            "Disponibilidad = 1",
            null,
            null,
            null,
            null
        )

        //se crea un cursor con todos los casos encontrados en la base de datos
        val cursor = resultado



        //si no esta vacio sacar el nombre contraseña y estado si es usuario o cadete
        while (cursor != null && cursor.moveToNext()){
            println("pepe")
            val dbPersonaID=cursor.getString(cursor.getColumnIndexOrThrow("ID_Persona"))
            //estos van a ser los resultados de los cadetes buscados
            val compatibles =db.query(
                BaseDeDatos.TABLE_NAME,
                arrayOf(BaseDeDatos.COLUMN_ID,"Nombre", "Contraseña", "Apellido", "Edad", "DNI", "Email"),
                "ID = ?",
                arrayOf(dbPersonaID),
                null,
                null,
                null
            )
            while (compatibles != null && compatibles.moveToNext()) {
                println("lepeu")
                //convierto los datos conseguidos en variables
                val dbNombre = compatibles.getString(compatibles.getColumnIndexOrThrow("Nombre"))
                val dbContraseña = compatibles.getString(compatibles.getColumnIndexOrThrow("Contraseña"))
                val dbApellido = compatibles.getString(compatibles.getColumnIndexOrThrow("Apellido"))
                val dbID = compatibles.getString(compatibles.getColumnIndexOrThrow("ID"))
                val dbEdad = compatibles.getString(compatibles.getColumnIndexOrThrow("Edad"))
                val dbDNI = compatibles.getString(compatibles.getColumnIndexOrThrow("DNI"))
                val dbEmail = compatibles.getString(compatibles.getColumnIndexOrThrow("Email"))
                //creo una persona con esos datos
                val CadeteEncontrado = ResultadoPersona(
                    dbNombre,
                    dbContraseña,
                    dbID,
                    dbApellido,
                    dbEdad,
                    dbDNI,
                    dbEmail,
                    null,
                    "Cadete"
                )
                CadetesDisponibles.add(CadeteEncontrado)
            }
        }

        return CadetesDisponibles

    }
}
