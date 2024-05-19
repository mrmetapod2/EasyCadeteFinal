package com.example.easycadete

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //generando las variables de los elementos de la pantalla de login
        val Usuario= findViewById<EditText>(R.id.editTextText)
        val Contraseña= findViewById<EditText>(R.id.editTextTextPassword)
        val Boton= findViewById<Button>(R.id.button)
        val nivelFuncion = NivelFuncion()


        //reaccion al apretar un boton
        Boton.setOnClickListener {
            nivelFuncion.VerificacionUsuarios(Usuario.getText().toString(), Contraseña.getText().toString())
        }


        //template de android estudio que no entiendo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

    }
    }