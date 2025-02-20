package com.example.easycadete

import Solicitud_Entrega.Solicitud_Envio
import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException
import kotlin.properties.Delegates

class ModificarDatos : AppCompatActivity() {


    private lateinit var textOrigen: TextView
    private lateinit var btnOrigen: Button
    private lateinit var card2: CardView
    private var Latitud by Delegates.notNull<Double>()
    private var Longitud by Delegates.notNull<Double>()
    private var CambioOrigen=false
    private val REQUEST_CODE_ORIGEN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modificar_datos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val Datos = intent.getParcelableExtra<ResultadoPersona>("result")
        val Nombre= findViewById<EditText>(R.id.editTextText2)
        val Apellido= findViewById<EditText>(R.id.editTextText4)
        val Edad= findViewById<EditText>(R.id.editTextText5)
        val DNI= findViewById<EditText>(R.id.editTextText7)
        val Email= findViewById<EditText>(R.id.editTextText6)
        val Locacion= findViewById<TextView>(R.id.textorigen)
        val Contraseña= findViewById<EditText>(R.id.editTextText3)
        val nivelFuncion = NivelFuncion()
        var NuevosDatos:ResultadoPersona



        val RegistrarBut= findViewById<Button>(R.id.button2)
        if (Datos != null) {
            Nombre.setText(Datos.Nombre)
            Apellido.setText(Datos.Apellido)
            Edad.setText(Datos.Edad)
            DNI.setText(Datos.DNI)
            Email.setText(Datos.Email)


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

            textOrigen = findViewById<TextView>(R.id.textorigen)
            btnOrigen = findViewById<Button>(R.id.btnorigen)
            card2 = findViewById(R.id.cardView)
            btnOrigen.setOnClickListener {
                CambioOrigen=true
                val intent = Intent(this, Solicitud_Envio::class.java)
                startActivityForResult(intent, REQUEST_CODE_ORIGEN)
            }
            RegistrarBut.setOnClickListener {
                if(CambioOrigen) {
                    nivelFuncion.ModificarUsuarios(
                        this,
                        Nombre.getText().toString(),
                        Contraseña.getText().toString(),
                        Apellido.getText().toString(),
                        Edad.getText().toString(),
                        Email.getText().toString(),
                        DNI.getText().toString(),

                        Latitud,
                        Longitud,
                        Datos.Email.toString()
                    )
                    NuevosDatos=ResultadoPersona(
                        Nombre.getText().toString(),
                        Contraseña.getText().toString(),
                        Datos.ID,
                        Apellido.getText().toString(),
                        Edad.getText().toString(),
                        Email.getText().toString(),
                        DNI.getText().toString(),
                        Datos.Telefono,
                        Datos.UsuarioOCadete,

                        Latitud.toString(),
                        Longitud.toString(),
                        Datos.IDCadUsu)

                }
                else{
                    nivelFuncion.ModificarUsuarios(
                        this,
                        Nombre.getText().toString(),
                        Contraseña.getText().toString(),
                        Apellido.getText().toString(),
                        Edad.getText().toString(),
                        Email.getText().toString(),
                        DNI.getText().toString(),

                        Datos.Latitud!!.toDouble(),
                        Datos.Longitud!!.toDouble(),
                        Datos.Email.toString()
                    )
                    NuevosDatos=ResultadoPersona(
                        Nombre.getText().toString(),
                        Contraseña.getText().toString(),
                        Datos.ID,
                        Apellido.getText().toString(),
                        Edad.getText().toString(),
                        Email.getText().toString(),
                        DNI.getText().toString(),
                        Datos.Telefono,
                        Datos.UsuarioOCadete,

                        Datos.Latitud!!.toString(),
                        Datos.Longitud!!.toString(),
                        Datos.IDCadUsu)
                }
                val intent = Intent(this, PantallaUsuario::class.java)
                intent.putExtra("result",NuevosDatos)
                startActivity(intent)


            }

    }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        println(data)
        if (resultCode == Activity.RESULT_OK && data != null) {

            val returnValue = data.getStringExtra("returnValue")
            val hardLocation=data.getDoubleArrayExtra("hardLocation")

            when (requestCode) {
                REQUEST_CODE_ORIGEN -> {
                    textOrigen.text = returnValue
                    if (!returnValue.isNullOrEmpty()) {
                        card2.visibility = View.VISIBLE
                    }
                }

            }
            hardLocation?.let {
                if (it.size >=2) {
                    Latitud = it[0]
                    Longitud = it[1]
                }
                else{

                }

            } ?: run {

            }

        }
    }
}