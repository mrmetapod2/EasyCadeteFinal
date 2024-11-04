package com.example.easycadete

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class NivelFuncion {
    inner class ModificarUsuarios(
        context: Context,
        Nombre: String,
        Contraseña: String,
        Apellido: String,
        Edad: String,
        Email: String,
        DNI: String,
        EsUsuario: Boolean,
        latitud: Double,
        longitud: Double
    ) {


    }

    fun EnviarEmail(context: Context, Email: String){
        val nivelDatabase= NivelDatabase(context)
        //esto chequea si lo que pusiste en el texto es un EMAIL
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            nivelDatabase.EnviarRecuperacion(context,Email)
        }
        else{
            Toast.makeText(context, "Texto ingresado no es un Email", Toast.LENGTH_SHORT).show()
        }
    }
    fun VerificacionUsuarios(context: Context,Nombre :String, Contraseña: String){
        //Aqui se van a realizar todas las funciones logicas, se hara aca para mantener el codigo de
        // las actividades limpio
        val nivelDatabase = NivelDatabase(context)
        val result = nivelDatabase.EstaEnBDD(context,Nombre, Contraseña);
        println(result)
        if (result.UsuarioOCadete== "Usuario"){
            val i =Intent(context, PantallaUsuario::class.java)
            //añado el resultado al futuro
            i.putExtra("result",result)
            startActivity(context,i,null)

            //println(result)
            //Toast.makeText(this, "usuario", Toast.LENGTH_LONG).show()

        }
        else if (result.UsuarioOCadete== "Cadete"){
            val i =Intent(context, PantallaCadete::class.java)
            //añado el resultado al futuro
            i.putExtra("result",result)
            startActivity(context,i,null)
            //println(result)
            //Toast.makeText(this, "Cadete", Toast.LENGTH_LONG).show()

        }
        else{
            //println(result)
            //Toast.makeText(this, "nada", Toast.LENGTH_LONG).show()
        }

    }
    fun RegistrarUsuarios(context: Context,Nombre :String, Contraseña: String,Apellido:String,Edad:String,Email: String,DNI:String,EsUsuario: Boolean, Latitud:Double, Longitud:Double){
        println(EsUsuario)
        //chequeo si contraseña es valida
        if (EsContraseñaValida(Contraseña)){
            //chequeo si edad es un numero
            if (Edad.toDoubleOrNull()!=null) {
                //chequeo si edad es un numero valido
                val numeroEdad= Edad.toInt()
                if(numeroEdad in 14..119){
                    //chequeo si dni es valido
                    println(DNI.length)
                    println(DNI)
                    if (DNI.length in 7..9 && DNI.toDoubleOrNull()!=null) {
                        //chequeo si el email es un email
                        if(android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                            if(Latitud!=null &&Longitud!=null){
                                val nivelDatabase = NivelDatabase(context)
                                val result = nivelDatabase.AñadirABDD(
                                    context,
                                    Nombre,
                                    Contraseña,
                                    Apellido,
                                    Edad,
                                    DNI,
                                    Email,
                                    EsUsuario,
                                    Latitud,
                                    Longitud
                                );
                                println(result)
                            }
                        }
                        else{
                            Toast.makeText(context, "Email no es valido", Toast.LENGTH_SHORT).show()
                        }


                    }
                    else{
                        Toast.makeText(context, "DNI no es valido", Toast.LENGTH_SHORT).show()
                    }
                }

                else{
                    Toast.makeText(context, "Edad no es un numero valido", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(context, "Edad no es un numero", Toast.LENGTH_SHORT).show()
            }

        }
        else{
            Toast.makeText(context, "Contraseña no cumple con los requerimientos", Toast.LENGTH_SHORT).show()
        }

    }
    //funcion para ver si una contraseña es valida
     fun EsContraseñaValida(password: String): Boolean {
         //chequea si tiene una letra en mayuscula
        val uppercasePattern = Regex(".*[A-Z].*")
        //una minuscula
        val lowercasePattern = Regex(".*[a-z].*")
        //algun numero
        val digitPattern = Regex(".*[0-9].*")
        //algun character especial
        val specialCharPattern = Regex(".*[!@#\$%^&*(),.?\":{}|<>].*")
        //y si es mas largo que 8
        val minLength = 8

        return password.length > minLength &&
                uppercasePattern.containsMatchIn(password) &&
                lowercasePattern.containsMatchIn(password) &&
                digitPattern.containsMatchIn(password) &&
                specialCharPattern.containsMatchIn(password)
    }

    //esta funcion se va a encargar de la asignacion de cadetes porfa llamala cuando se tengan que asignar en el proceso de relizar un pedido
    fun AsignarCadetes(context: Context): MutableList<ResultadoPersona> {
        val nivelDatabase = NivelDatabase(context)
        val result = nivelDatabase.AsignarCadetes(context)
        return result
    }

}
