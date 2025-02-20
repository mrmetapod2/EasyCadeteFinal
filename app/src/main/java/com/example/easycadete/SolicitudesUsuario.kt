package com.example.easycadete

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SolicitudesUsuario : AppCompatActivity() {
    val nivelFuncion = NivelFuncion()
    private lateinit var Datos: ResultadoPersona
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_solicitudes_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Datos = intent.getParcelableExtra<ResultadoPersona>("result")!!
        val solicitudes = nivelFuncion.ObservarSolicitudes(this,Datos)
        val recyclerView: RecyclerView = findViewById(R.id.RecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = AsignarSolicitudRecycle(solicitudes, { solicitud ->
            onDirectionButtonClick(solicitud)
        }, { solicitud ->
            onAcceptButtonClick(solicitud)
        })

        recyclerView.adapter = adapter
        println("awoooga")

    }
    private fun onDirectionButtonClick(solicitud: ResultadoSolicitud) {

        val i = Intent(this, DirectionMap::class.java)
        i.putExtra("result",solicitud)
        ContextCompat.startActivity(this, i, null)

    }

    private fun onAcceptButtonClick(solicitud: ResultadoSolicitud) {
        // Implement the action for Accept button
        if (solicitud.IdEstado!!.toInt()==1) {
            nivelFuncion.CancelarSolicitud(solicitud, this)
        }
        else if (solicitud.IdEstado!!.toInt()==5){
            nivelFuncion.TerminarSolicitud(solicitud, this)
        }

    }
}