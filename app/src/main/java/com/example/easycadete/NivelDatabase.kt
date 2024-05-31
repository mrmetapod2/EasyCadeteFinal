package com.example.easycadete

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//uso esta clase para armar un esqueleto de una base de datos para las pruebas
class BaseDeDatos(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
    {
        companion object {
            //esto va a ser modificado para que sea exactamente la base de datos que usemos
         const val DATABASE_NAME = "example.db"
         const val DATABASE_VERSION = 5
         const val TABLE_NAME = "Persona"
         const val COLUMN_ID = "id"
         const val COLUMN_NAME = "name"
            const val COLUMN_CONT = "contraseña"
            const val CADETE_O_USUARIO= "usuarioOCadete"

        private const val PERSONA_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT NOT NULL," +
                    "$CADETE_O_USUARIO TEXT NOT NULL," +
                    "$COLUMN_CONT TEXT NOT NULL) "
            /*"CREATE TABLE Persona (" +
                    "ID INT PRIMARY KEY AUTOINCREMENT," +
                    "Nombre VARCHAR(100) NOT NULL," +
                    "Apellido VARCHAR(100) NOT NULL," +
                    "Contraseña VARCHAR(100) NOT NULL," +
                    "Edad INT NOT NULL," +
                    "DNI VARCHAR(20) NOT NULL," +
                    "Email VARCHAR(100) NOT NULL UNIQUE," +
                    "Telefono VARCHAR(20))" +*/
        private const val USUARIO_TABLE =
            "CREATE TABLE Usuario (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ID_Persona INTEGER NOT NULL," +
                    "FOREIGN KEY (ID_Persona) REFERENCES Persona(ID)" +
                    ")"
        private const val CADETE_TABLE =
            "CREATE TABLE Cadete (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ID_Persona INTEGER NOT NULL," +
                    "Disponibilidad BOOLEAN DEFAULT 0," +
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

    fun EstaEnBDD(context: Context,Nombre :String, Contraseña: String) : ResultadoPersona {
        baseDeDatos = BaseDeDatos(context)
        val db = baseDeDatos.readableDatabase
        val resultado= db.query(
            BaseDeDatos.TABLE_NAME,
            arrayOf(BaseDeDatos.COLUMN_ID, BaseDeDatos.COLUMN_NAME, BaseDeDatos.COLUMN_CONT, BaseDeDatos.CADETE_O_USUARIO),
            "${BaseDeDatos.COLUMN_NAME} = ? AND ${BaseDeDatos.COLUMN_CONT} = ?",
            arrayOf(Nombre, Contraseña),
            null,
            null,
            null
        )
        val cursor = resultado
        if (cursor != null && cursor.moveToFirst()){
            val dbNombre =cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val dbContraseña = cursor.getString(cursor.getColumnIndexOrThrow("contraseña"))
            val dbUsuarioOCadete = cursor.getString(cursor.getColumnIndexOrThrow("usuarioOCadete"))
            if (dbNombre == Nombre && dbContraseña == Contraseña) {
               return ResultadoPersona( dbNombre, dbContraseña, dbUsuarioOCadete)

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

    fun AñadirABDD(context: Context ,Nombre :String, Contraseña: String, EsUsuario: Boolean){
        baseDeDatos = BaseDeDatos(context)

        //se crea el objeto de añadir info a la BDD

        val db = baseDeDatos.writableDatabase
        //Se asigna una tabla con la info que va a la BDD
        var values = ContentValues().apply {
            put("name", Nombre)
            put("contraseña", Contraseña)
        }
        if (EsUsuario){
             values = ContentValues().apply {
                put("name", Nombre)
                put("contraseña", Contraseña)
                 put("usuarioOCadete", "usuario")
            }
        }
        else{
             values = ContentValues().apply {
                put("name", Nombre)
                put("contraseña", Contraseña)
                put("usuarioOCadete", "cadete")
            }
        }

        //se crea una nueva linea en la base de datos en la tabla asignada con los valores declarados
        val newRowId = db.insert("Persona", null, values)
        //se chequea si funciono
        if (newRowId != -1L) {
            println("result")
            // funciono bien
        } else {
            println("no")
            // no lo fue
        }
    }
}
