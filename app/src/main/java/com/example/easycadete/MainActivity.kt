package com.example.easycadete

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


    }

    fun onMatch(view: View){
        val Usuario= findViewById<EditText>(R.id.editTextText)
        val Contraseña= findViewById<EditText>(R.id.editTextTextPassword)
        val Boton= findViewById<Button>(R.id.button)
        Boton.setOnClickListener{
            Toast.makeText(
                this ,
                "aprete boton",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
fun Entrar(){

}