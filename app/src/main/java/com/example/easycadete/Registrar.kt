package com.example.easycadete

import Solicitud_Entrega.Solicitud_Envio
import android.app.Activity
import android.content.Intent
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
import kotlin.properties.Delegates

class Registrar : AppCompatActivity() {

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
        setContentView(R.layout.activity_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        dbHelper = BaseDeDatos(this)
        val Usuario= findViewById<EditText>(R.id.editTextText2)
        val Contraseña= findViewById<EditText>(R.id.editTextText3)
        val RegistrarBut= findViewById<Button>(R.id.button2)
        val Apellido= findViewById<EditText>(R.id.editTextText4)
        val Edad= findViewById<EditText>(R.id.editTextText5)
        val Email= findViewById<EditText>(R.id.editTextText6)
        val DNI= findViewById<EditText>(R.id.editTextText7)
        val nivelFuncion = NivelFuncion()
        val EsUsuario=findViewById<RadioButton>(R.id.radioButton)
        val EsCadete=findViewById<RadioButton>(R.id.radioButton2)
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
                nivelFuncion.RegistrarUsuarios(this,Usuario.getText().toString(), Contraseña.getText().toString(),
                    Apellido.getText().toString(),Edad.getText().toString(),Email.getText().toString(),DNI.getText().toString(),true, Latitud, Longitud)
                val i = Intent(this, MainActivity::class.java)
                ContextCompat.startActivity(this, i, null)
            }
            else if (EsCadete.isChecked){
                println("cadete")
                nivelFuncion.RegistrarUsuarios(this,Usuario.getText().toString(), Contraseña.getText().toString(),
                    Apellido.getText().toString(),Edad.getText().toString(),Email.getText().toString(),DNI.getText().toString(),false,Latitud, Longitud)
                val i = Intent(this, MainActivity::class.java)
                ContextCompat.startActivity(this, i, null)

            }
            else{
                println("no se eligio usuario o cadete")
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