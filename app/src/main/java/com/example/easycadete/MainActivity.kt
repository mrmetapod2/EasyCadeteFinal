package com.example.easycadete

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: BaseDeDatos
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //generando las variables de los elementos de la pantalla de login
        val Usuario= findViewById<EditText>(R.id.editTextText)
        val Contraseña= findViewById<EditText>(R.id.editTextTextPassword)
        val Ingresar= findViewById<Button>(R.id.button)
        val RegistrarBut= findViewById<Button>(R.id.button3)
        val ContraseñaBut= findViewById<Button>(R.id.button6)
        val nivelFuncion = NivelFuncion()
        dbHelper = BaseDeDatos(this)



        //reaccion al apretar un boton
        Ingresar.setOnClickListener {

            nivelFuncion.VerificacionUsuarios(this,Usuario.getText().toString(), Contraseña.getText().toString())
        }
        RegistrarBut.setOnClickListener {

            val i = Intent(this, Registrar::class.java)
            ContextCompat.startActivity(this, i, null)
        }
        ContraseñaBut.setOnClickListener {
            val i = Intent(this, RecuperarContrasenia::class.java)
            ContextCompat.startActivity(this, i, null)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

    }
    }