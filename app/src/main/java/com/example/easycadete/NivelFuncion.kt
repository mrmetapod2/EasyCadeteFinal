package com.example.easycadete

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

class NivelFuncion {
    fun VerificacionUsuarios(context: Context,Nombre :String, Contraseña: String){
        //Aqui se van a realizar todas las funciones logicas, se hara aca para mantener el codigo de
        // las actividades limpio
        val nivelDatabase = NivelDatabase(context)
        val result = nivelDatabase.EstaEnBDD(context,Nombre, Contraseña);
        if (result.UsuarioOCadete== "Usuario"){
            val i =Intent(context, PantallaUsuario::class.java)
            startActivity(context,i,null)
            println(result)
            //Toast.makeText(this, "usuario", Toast.LENGTH_LONG).show()

        }
        else if (result.UsuarioOCadete== "Cadete"){
            val i =Intent(context, PantallaCadete::class.java)
            startActivity(context,i,null)
            println(result)
            //Toast.makeText(this, "Cadete", Toast.LENGTH_LONG).show()

        }
        else{
            println(result)
            //Toast.makeText(this, "nada", Toast.LENGTH_LONG).show()
        }

    }
    fun RegistrarUsuarios(context: Context,Nombre :String, Contraseña: String,Apellido:String,Edad:String,DNI:String,Email:String,EsUsuario: Boolean){
        println(EsUsuario)
        val nivelDatabase = NivelDatabase(context)
        val result = nivelDatabase.AñadirABDD(context, Nombre, Contraseña,Apellido,Edad,Email,DNI,EsUsuario);
        println(result)
    }

    //esta funcion se va a encargar de la asignacion de cadetes porfa llamala cuando se tengan que asignar en el proceso de relizar un pedido
    fun AsignarCadetes(context: Context): MutableList<ResultadoPersona> {
        val nivelDatabase = NivelDatabase(context)
        val result = nivelDatabase.AsignarCadetes(context)
        return result
    }

}
