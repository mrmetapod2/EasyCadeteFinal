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
        //genero una variable con los datos del usuario
        val Datos = intent.getParcelableExtra<ResultadoPersona>("result")
        PedidoBut.setOnClickListener {
            val i = Intent(this, Solicitud_Cadete::class.java)
            //los mando a la proxima actividad por si a caso son utiles
            i.putExtra("result",Datos)
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
            intent.putExtra("result",Datos)
            startActivity(intent)

        }
        val DatosBut= findViewById<Button>(R.id.button7)
        DatosBut.setOnClickListener(){
            val intent = Intent(this, MostrarDatos::class.java)
            intent.putExtra("result",Datos)
            startActivity(intent)
        }

        val ModifBut=findViewById<Button>(R.id.button8)
        ModifBut.setOnClickListener(){
            val intent = Intent(this, ModificarDatos::class.java)
            intent.putExtra("result",Datos)
            startActivity(intent)
        }
        val SolBut=findViewById<Button>(R.id.button11)
        SolBut.setOnClickListener(){
            val intent = Intent(this, SolicitudesUsuario::class.java)
            intent.putExtra("result",Datos)
            startActivity(intent)
        }

    }
}