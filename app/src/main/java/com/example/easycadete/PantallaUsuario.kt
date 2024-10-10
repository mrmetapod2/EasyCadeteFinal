package com.example.easycadete

import Cancelar_Entrega.Reembolso
import Solicitud_Entrega.Main_Solicitud
import Solicitud_Entrega.Solicitud_Cadete
import Solicitud_Entrega.Solicitud_Envio
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//pantalla principal del usuario
class PantallaUsuario : AppCompatActivity() {
    private lateinit var calificacionbtn: Button
    private lateinit var NenvioBTN: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val PedidoBut= findViewById<Button>(R.id.button4)

        PedidoBut.setOnClickListener {
            val i = Intent(this, Solicitud_Cadete::class.java)
            ContextCompat.startActivity(this, i, null)
        }

        ///mostramos el cuadro de dialogo de la opinion del usuario
        calificacionbtn = findViewById(R.id.calificacionBTN)
        calificacionbtn.setOnClickListener {
            val ratingDialog = RatingDialog(this)
            ratingDialog.show()
        }

        NenvioBTN = findViewById(R.id.Nenviobtn)
        NenvioBTN.setOnClickListener {
            val intent = Intent(this, Main_Solicitud::class.java)
            startActivity(intent)

        }

    }
}