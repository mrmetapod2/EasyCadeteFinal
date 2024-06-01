package com.example.easycadete

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Registrar : AppCompatActivity() {
    private lateinit var dbHelper: BaseDeDatos
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
        RegistrarBut.setOnClickListener {
            if (EsUsuario.isChecked){
                nivelFuncion.RegistrarUsuarios(this,Usuario.getText().toString(), Contraseña.getText().toString(),
                    Apellido.getText().toString(),Edad.getText().toString(),Email.getText().toString(),DNI.getText().toString(),true)
                val i = Intent(this, MainActivity::class.java)
                ContextCompat.startActivity(this, i, null)
            }
            else if (EsCadete.isChecked){
                nivelFuncion.RegistrarUsuarios(this,Usuario.getText().toString(), Contraseña.getText().toString(),
                    Apellido.getText().toString(),Edad.getText().toString(),Email.getText().toString(),DNI.getText().toString(),false)
                val i = Intent(this, MainActivity::class.java)
                ContextCompat.startActivity(this, i, null)
            }
            else{
                println("no se eligio usuario o cadete")
            }


        }
    }
}