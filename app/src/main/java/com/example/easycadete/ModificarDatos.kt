package com.example.easycadete

import Solicitud_Entrega.Solicitud_Envio
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
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
    private lateinit var dbHelper: BaseDeDatos

    private lateinit var textOrigen: TextView
    private lateinit var btnOrigen: Button
    private lateinit var card2: CardView
    private var Latitud by Delegates.notNull<Double>()
    private var Longitud by Delegates.notNull<Double>()

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
        val Locacion= findViewById<TextView>(R.id.textView6)
        val Contraseña= findViewById<EditText>(R.id.editTextText3)
        val nivelFuncion = NivelFuncion()
        val EsUsuario=findViewById<RadioButton>(R.id.radioButton)
        val EsCadete=findViewById<RadioButton>(R.id.radioButton2)

        val RegistrarBut= findViewById<Button>(R.id.button2)
        if (Datos != null) {
            Nombre.setText(Datos.Nombre)
            Apellido.setText(Datos.Apellido)
            Edad.setText(Datos.Edad)
            DNI.setText(Datos.DNI)
            Email.setText(Datos.Email)
            if (Datos.UsuarioOCadete== "Usuario"){
                EsUsuario.isChecked=true
            }
            else if (Datos.UsuarioOCadete== "Cadete"){
                EsCadete.isChecked=true
            }

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
                val intent = Intent(this, Solicitud_Envio::class.java)
                startActivityForResult(intent, REQUEST_CODE_ORIGEN)
            }
            RegistrarBut.setOnClickListener {
                if (EsUsuario.isChecked){
                    println("usuario")
                    nivelFuncion.ModificarUsuarios(this,Nombre.getText().toString(), Contraseña.getText().toString(),
                        Apellido.getText().toString(),Edad.getText().toString(),Email.getText().toString(),DNI.getText().toString(),true, Latitud, Longitud)
                    val i = Intent(this, MainActivity::class.java)
                    ContextCompat.startActivity(this, i, null)
                }
                else if (EsCadete.isChecked){
                    println("cadete")
                    nivelFuncion.ModificarUsuarios(this,Nombre.getText().toString(), Contraseña.getText().toString(),
                        Apellido.getText().toString(),Edad.getText().toString(),Email.getText().toString(),DNI.getText().toString(),false,Latitud, Longitud)
                    val i = Intent(this, MainActivity::class.java)
                    ContextCompat.startActivity(this, i, null)

                }
                else{
                    println("no se eligio usuario o cadete")
                }

            }

    }
    }
}