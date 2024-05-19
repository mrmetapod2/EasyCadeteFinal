package com.example.easycadete

class NivelDatabase {
    fun EstaEnBDD(Nombre :String, Contraseña: String): String {
        //aqui se va a llamar la base de datos pidiendole si hay un usuario con ese nombre y contraseña
        // y si es un cadete o usuario
        if (Nombre == "Usuario" && Contraseña== "1"){
            return "Usuario"
        }
        else if (Nombre == "Cadete" && Contraseña== "1"){
            return "Cadete"
        }
        else{
            return "None"
        }
    }
}
