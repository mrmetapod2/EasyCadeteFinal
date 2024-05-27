package com.example.easycadete

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//uso esta clase para armar un esqueleto de una base de datos para las pruebas
class BaseDeDatos(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
    {
        companion object {
            //esto va a ser modificado para que sea exactamente la base de datos que usemos
         const val DATABASE_NAME = "example.db"
         const val DATABASE_VERSION = 1
         const val TABLE_NAME = "example_table"
         const val COLUMN_ID = "id"
         const val COLUMN_NAME = "name"

        private const val TABLE_CREATE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT NOT NULL)"
    }
        //maneje de versiones
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(TABLE_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
}
class NivelDatabase(context: Context) {
    //conecto a la base de datos
     val baseDeDatos = BaseDeDatos(context)

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
    fun AñadirABDD(Nombre :String, Contraseña: String){
        //se crea el objeto de añadir info a la BDD
        val db = baseDeDatos.writableDatabase
        //Se asigna una tabla con la info que va a la BDD
        val values = ContentValues().apply {
            put("Nombre", Nombre)
            put("Contraseña", Contraseña)
        }
        //se crea una nueva linea en la base de datos en la tabla asignada con los valores declarados
        val newRowId = db.insert("Personas", null, values)
        //se chequea si funciono
        if (newRowId != -1L) {
            // funciono bien
        } else {
            // no lo fue
        }
    }
}
