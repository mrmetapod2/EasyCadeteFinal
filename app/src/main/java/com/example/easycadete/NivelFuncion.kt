package com.example.easycadete

class NivelFuncion {
    fun VerificacionUsuarios(Nombre :String, Contraseña: String){
        //Aqui se van a realizar todas las funciones logicas, se hara aca para mantener el codigo de
        // las actividades limpio
        val nivelDatabase = NivelDatabase()
        val result = nivelDatabase.EstaEnBDD(Nombre, Contraseña);
        if (result== "Usuario"){
            println(result)
            //Toast.makeText(this, "usuario", Toast.LENGTH_LONG).show()

        }
        else if (result== "Cadete"){
            println(result)
            //Toast.makeText(this, "Cadete", Toast.LENGTH_LONG).show()

        }
        else{
            println(result)
            //Toast.makeText(this, "nada", Toast.LENGTH_LONG).show()
        }

    }
}