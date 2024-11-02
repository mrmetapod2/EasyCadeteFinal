package com.example.easycadete

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException

class MostrarDatos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mostrar_datos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val Datos = intent.getParcelableExtra<ResultadoPersona>("result")
        val Nombre= findViewById<TextView>(R.id.textView19)
        val Apellido= findViewById<TextView>(R.id.textView20)
        val Edad= findViewById<TextView>(R.id.textView21)
        val DNI= findViewById<TextView>(R.id.textView22)
        val Email= findViewById<TextView>(R.id.textView25)
        val Locacion= findViewById<TextView>(R.id.textView23)
        if (Datos != null) {
            Nombre.text=Datos.Nombre
            Apellido.text=Datos.Apellido
            Edad.text=Datos.Edad
            DNI.text=Datos.DNI
            Email.text=Datos.Email
            //creo el geocoder
            val geocoder = Geocoder(this)
            try {

                val addresses: List<Address>? =
                    Datos.Latitud?.let { Datos.Longitud?.let { longitud -> geocoder.getFromLocation(it.toDouble(), longitud.toDouble(), 1) } }
                val address = addresses?.get(0)
                if (address != null) {
                    Locacion.text="${address.thoroughfare}, ${address.subThoroughfare}, ${address.locality}"
                }
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }

    }
}