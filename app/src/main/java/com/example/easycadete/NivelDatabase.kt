package com.example.easycadete


import android.content.Context
import android.widget.Toast

import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody

import okhttp3.Request
import okhttp3.Response

import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt
import java.io.IOException
import java.util.concurrent.CountDownLatch



//uso esta clase para armar un esqueleto de una base de datos para las pruebas

class NivelDatabase(context: Context) {

    //conecto a la base de datos


    //CUANDO TRABAJES EN ESTO REEMPLAZA CON TU IP
    val IP="192.168.0.2"
    //url para la base de datos
    val url= String.format("http://%S/easycadete/server.php",IP)
    val client = okhttp3.OkHttpClient()
    val urlEmail=String.format("http://%S/easycadete/sendEmail.php",IP)
    val urlVerif=String.format("http://%S/easycadete/verify.php",IP)
    val urlPass=String.format("http://%S/easycadete/newPassword.php",IP)


    fun AsignarPedidos(context: Context) : List<ResultadoSolicitud> {
        //creo la lista de output
        val SolicitudesDisponibles = mutableListOf<ResultadoSolicitud>()
        //armo una variable que tendra los resultados de la query
        var responseText=""
        //genero la query de sql
        val query="SELECT s.ID AS ID, s.ID_Usuario AS IdUsuario, s.ID_Cadete AS IdCadete, s.ID_Paquete AS IdPaquete, p.Peso AS PaquetePeso, p.Largo AS PaqueteLargo, p.Ancho AS PaqueteAncho, p.Alto AS PaqueteAlto, e.Estado AS Estado, s.ID_Estado AS IdEstado, s.Fecha AS FechaCreacion, s.FechaFin AS FechaFinalizacion, s.ImporteTotal AS Importe, s.LatitudIni AS LatitudIni, s.LongitudIni AS LongitudIni, s.LatitudFin AS LatitudFin, s.LongitudFin AS LongitudFin FROM easycadete.solicitud s JOIN easycadete.paquete p ON s.ID_Paquete = p.ID JOIN easycadete.estado e ON s.ID_Estado = e.ID;"
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
                val resultadoSolicitud = ResultadoSolicitud(
                    ID = jsonObject.getString("ID"),

                    IdUsuario = jsonObject.getString("IdUsuario"),
                    IdCadete = jsonObject.getString("IdCadete"),
                    IdPaquete = jsonObject.getString("IdPaquete"),
                    PaquetePeso = jsonObject.getString("PaquetePeso"),
                    PaqueteLargo = jsonObject.getString("PaqueteLargo"),
                    PaqueteAncho = jsonObject.getString("PaqueteAncho"),
                    PaqueteAlto = jsonObject.getString("PaqueteAlto"),
                    Estado = jsonObject.getString("Estado"),
                    IdEstado = jsonObject.getString("IdEstado"),
                    FechaCreacion = jsonObject.getString("FechaCreacion"),
                    FechaFinalizacion = jsonObject.getString("FechaFinalizacion"),
                    Importe = jsonObject.getString("Importe"),
                    LatitudIni = jsonObject.getString("LatitudIni"),
                    LongitudIni = jsonObject.getString("LongitudIni"),
                    LatitudFin = jsonObject.getString("LatitudFin"),
                    LongitudFin = jsonObject.getString("LongitudFin")

                )
                SolicitudesDisponibles.add(resultadoSolicitud)
            }
        }
        return SolicitudesDisponibles
    }

    fun EnviarRecuperacion(context: Context, Email: String){
        //preparo la querry para la base de datos con los datos extraidos
        val query=String.format("SELECT * FROM `persona` WHERE `Email`='%s'", Email)
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
                    try { //se intenta crear un jsonobject a base de lo que hay en la base de datos
                        val jsonObject = JSONObject(responseData)
                        val formBody = FormBody.Builder()
                            .add("email", Email)
                            .add("subject", "Recuperacion de contraseña")
                            .add("link", urlPass)

                            .build()
                        //transfomo la form a una request
                        val request= Request.Builder()
                            .url(urlEmail)
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
                    catch (e: Exception) {
                        //si el crear el jsonobject falla se retorna que el Email puesto no esta en la BDD
                        e.printStackTrace()
                        countDownLatch.countDown()
                        Toast.makeText(context, "Email already in use", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        })
    }
    fun EstaEnBDD(context: Context,Nombre :String, Contraseña: String) : ResultadoPersona {
        //preparo la variable que va a retornar
        var resultadoPersona = ResultadoPersona()



        //preparo la querry para la base de datos con los datos extraidos
         val query=String.format("SELECT * FROM `persona` WHERE `Nombre`='%s' ", Nombre)
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
                        val jsonStrings = responseData.split("<br>")
                        //se va atravez de los resultados
                        for (jsonString in jsonStrings) {

                            if (jsonString.isNotEmpty()) {
                                //se crea un jsonobject con lo conseguido
                                val jsonObject = JSONObject(jsonString)
                                //se crea una variable de la contraseña hasheada
                                var contraseñaHasheada=jsonObject.optString("Contraseña")
                                //si inicia con el inicio del php se cambia al de android de esa manera ambos sistemas son compatibles
                                if (contraseñaHasheada.startsWith("$2y$")) {
                                     contraseñaHasheada =
                                        contraseñaHasheada.replace("$2y$", "$2a$")
                                }



                                //si el usuario esta verificado y si la contraseña dada es igual a la hasheada
                                if (jsonObject.optString("Verificacion") == "1" && BCrypt.checkpw(
                                        Contraseña,
                                        contraseñaHasheada
                                    )
                                ) {
                                    //se modifica resultado persona con los datos
                                    resultadoPersona = ResultadoPersona(
                                        ID = jsonObject.getString("ID"),
                                        Nombre = jsonObject.getString("Nombre"),
                                        Contraseña = jsonObject.getString("Contraseña"),
                                        Apellido = jsonObject.getString("Apellido"),
                                        Edad = jsonObject.getString("Edad"),
                                        DNI = jsonObject.getString("DNI"),
                                        Email = jsonObject.getString("Email"),


                                        Telefono = jsonObject.optString("Telefono"),
                                        Latitud = jsonObject.optString("Latitud"),
                                        Longitud = jsonObject.optString("Longitud")
                                    )
                                    //de aqui se realiza una query para ver si es cadete o Usuario
                                    println(resultadoPersona)
                                    val query = String.format(
                                        "SELECT * FROM `usuario` WHERE `ID_Persona`='%s'",
                                        resultadoPersona.ID
                                    )
                                    //genero la form con lo que estoy buscando
                                    val formBody = FormBody.Builder()
                                        .add("sql", query)

                                        .build()
                                    //transfomo la form a una request
                                    val request = Request.Builder()
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
                                                    resultadoPersona.IDCadUsu= jsonObject.getString("ID")
                                                    resultadoPersona.UsuarioOCadete = "Usuario"
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()

                                            }
                                            countDownLatch2.countDown();

                                        }
                                    })
                                    countDownLatch2.await()
                                    val query2 = String.format(
                                        "SELECT * FROM `cadete` WHERE `ID_Persona`='%s'",
                                        resultadoPersona.ID
                                    )
                                    //genero la form con lo que estoy buscando
                                    val formBody2 = FormBody.Builder()
                                        .add("sql", query2)

                                        .build()
                                    //transfomo la form a una request
                                    val request2 = Request.Builder()
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
                                                    resultadoPersona.IDCadUsu= jsonObject.getString("ID")
                                                    resultadoPersona.UsuarioOCadete = "Cadete"
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()

                                            }
                                            countDownLatch3.countDown();

                                        }

                                    })
                                    countDownLatch3.await()
                                }
                            }
                        }




                        countDownLatch.countDown()
                    }





                    catch (e: Exception) {
                        //si el crear el jsonobject falla se retorna un ResultadoPersona vacio
                        e.printStackTrace()
                        (context as MainActivity).runOnUiThread {
                            Toast.makeText(context, "invalido usuario o contraseña", Toast.LENGTH_SHORT)
                                .show()
                        }
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

    fun AñadirABDD(context: Context ,Nombre :String, Contraseña: String,Apellido:String,Edad:String,DNI:String,Email:String, EsUsuario: Boolean, Latitud:Double, Longitud:Double){
        //variable establecida para saber donde esta la nueva persona
        var insertedID=0;




        //genero la query de sql con la contraseña hasheada
        val query=String.format("INSERT INTO `persona`( `Nombre`, `Contraseña`, `Apellido`, `Edad`, `DNI`, `Email`, `Latitud`, `Longitud`) " +
                "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')", Nombre, BCrypt.hashpw(Contraseña,BCrypt.gensalt()), Apellido, Edad, DNI, Email,Latitud,Longitud)
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
                            val query= String.format( "INSERT INTO `usuario`( `ID_Persona`, `NombreUsuario`) VALUES ('%s','%s')", insertedID, Email)
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

                            val query= String.format("INSERT INTO `cadete`( `ID_Persona`, `NombreUsuario`) VALUES ('%s','%s')", insertedID, Nombre)
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
                        //despues que se añana a la BDD se mandara un mail de Verificacion
                        val formBody = FormBody.Builder()
                            .add("email", Email)
                            .add("subject", "Email de verificacion")
                            .add("link", urlVerif)

                            .build()
                        //transfomo la form a una request
                        val request= Request.Builder()
                            .url(urlEmail)
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
        val query="SELECT cadete.Disponibilidad, persona.ID, persona.Latitud,persona.Longitud, persona.Nombre, persona.Contraseña, persona.Apellido, persona.Edad, persona.DNI, persona.Email FROM persona, cadete WHERE cadete.ID_Persona = persona.ID AND persona.Verificacion=1 AND cadete.Disponibilidad=1;"
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


                    Telefono = jsonObject.optString("Telefono"),
                    Latitud = jsonObject.optString("Latitud"),
                    Longitud = jsonObject.optString("Longitud"),
                    //el resultado siempre va a ser cadetes porque sale de la tabla de cadetes
                    UsuarioOCadete = "Cadete"
                )
                CadetesDisponibles.add(resultadoPersona)
            }
        }











        //retorno lista de personas
        return CadetesDisponibles

    }
    fun ModificarEnBDD(context: Context ,Nombre :String, Contraseña: String,Apellido:String,Edad:String,DNI:String,Email:String, Latitud:Double, Longitud:Double,EmailViejo:String){
        //variable establecida para saber donde esta la nueva persona
        var insertedID=0;



        //genero la query de sql con la contraseña hasheada
        val query=String.format("UPDATE `persona` SET `Latitud`='%s'," +
                "`Longitud`='%s',`Nombre`='%s',`Contraseña`='%s',`Apellido`='%s'," +
                "`Edad`='%s',`DNI`='%s',`Email`='%s' " +
                "WHERE `Email`='%s'",Latitud,Longitud, Nombre, BCrypt.hashpw(Contraseña,BCrypt.gensalt()), Apellido, Edad, DNI, Email,EmailViejo)
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


                    }




                }

        })







    }

    fun crearPaquete(largo: String, ancho: String, alto: String, peso: String, context: Context):Int {
        var insertedID=0;
        val query=String.format("INSERT INTO `paquete`(  `Peso`, `Largo`, `Ancho`, `Alto`) VALUES ('%s','%s','%s','%s')", peso, largo, ancho, alto)
        //genero la form con lo que estoy buscando
        val formBody = FormBody.Builder()
            .add("sql", query)

            .build()
        //transfomo la form a una request
        val request= Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        val countDownLatch = CountDownLatch(1)
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
                        countDownLatch.countDown()
                    }
                    else{
                        Toast.makeText(context, "Paquete no fue ingresado", Toast.LENGTH_SHORT)
                        println("paquete no acceptado")
                        countDownLatch.countDown()
                    }
                }
            }
        })
        countDownLatch.await()
        return insertedID
    }

    fun crearSolicitud(idPaquete: Int, latitudIni: Double, longitudIni: Double, latitudFin: Double, longitudFin: Double, idUsuario: String?, context: Context): Int {
        var instertedID=0
        println(idUsuario)
        val query=String.format("INSERT INTO `solicitud`( `ID_Usuario`, `ID_Paquete`,  `ID_Estado`, `LatitudIni`, `LongitudIni`, `LatitudFin`, `LongitudFin`) VALUES ('%s','%s','%s','%s','%s','%s','%s')",
            idUsuario, idPaquete, 1, latitudIni, longitudIni, latitudFin, longitudFin)
        //genero la form con lo que estoy buscando
        val formBody = FormBody.Builder()
            .add("sql", query)

            .build()
        //transfomo la form a una request
        val request= Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        val countDownLatch = CountDownLatch(1)
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


                        instertedID= ResponseNumber.toInt()
                        countDownLatch.countDown()
                    }
                    else{

                        println("Solicitud no creada")
                        countDownLatch.countDown()
                    }
                }
            }
        })
        countDownLatch.await()


        return instertedID
    }

    fun ModificarSolicitud(
        solicitud: ResultadoSolicitud,
        context: Context,
        cadete: ResultadoPersona?
    ): Any {
        //genero la query de sql con la contraseña hasheada
        println(cadete!!.IDCadUsu)
        println("2")
        println(solicitud.ID)
        val query=String.format("UPDATE `solicitud` SET `ID_Cadete`='%s',`ID_Estado`='%s' WHERE `ID`=%s ",cadete!!.IDCadUsu,"2",solicitud.ID)
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
                    val responseText = response.body!!.string()
                    println(responseText)
                }
            }
        })
        return "aa"
    }

    fun CancelarSolicitud(solicitud: ResultadoSolicitud, context: Context, ): Any {
        //genero la query de sql con la contraseña hasheada
        val query=String.format("UPDATE `solicitud` SET `ID_Estado`='%s' WHERE `ID`=%s ","3",solicitud.ID)
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
                }
            }
        })
        return "aa"
    }

    fun CompletarSolicitud(solicitud: ResultadoSolicitud, context: Context): Any {
        val query=String.format("UPDATE `solicitud` SET `ID_Estado`='%s' WHERE `ID`=%s ","5",solicitud.ID)
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
                }
            }
        })
        return "aa"

    }

    fun TerminarSolicitud(solicitud: ResultadoSolicitud, context: Context): Any {
        val query=String.format("UPDATE `solicitud` SET `ID_Estado`='%s', `fechaFin`= current_timestamp()  WHERE `ID`=%s ","4",solicitud.ID)
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
                }
            }
        })
        return "aa"

    }


}
