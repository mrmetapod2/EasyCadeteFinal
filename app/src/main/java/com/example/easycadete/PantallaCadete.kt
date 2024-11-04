package com.example.easycadete

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//pantalla principal del cadete
class PantallaCadete : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_cadete)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val Datos = intent.getParcelableExtra<ResultadoPersona>("result")
        val DatosBut= findViewById<Button>(R.id.button7)
        DatosBut.setOnClickListener(){
            val intent = Intent(this, MostrarDatos::class.java)
            intent.putExtra("result",Datos)
            startActivity(intent)
        }
        val ModifBut=findViewById<Button>(R.id.button8)
        DatosBut.setOnClickListener(){
            val intent = Intent(this, ModificarDatos::class.java)
            intent.putExtra("result",Datos)
            startActivity(intent)
        }
    }
}