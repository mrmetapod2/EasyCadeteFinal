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
        if (result.UsuarioOCadete== "usuario"){
            val i =Intent(context, PantallaUsuario::class.java)
            startActivity(context,i,null)
            println(result)
            //Toast.makeText(this, "usuario", Toast.LENGTH_LONG).show()

        }
        else if (result.UsuarioOCadete== "cadete"){
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
    fun RegistrarUsuarios(context: Context,Nombre :String, Contraseña: String,EsUsuario: Boolean){

        val nivelDatabase = NivelDatabase(context)
        val result = nivelDatabase.AñadirABDD(context, Nombre, Contraseña,EsUsuario);
        println(result)
    }
}