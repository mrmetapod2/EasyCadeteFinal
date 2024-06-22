package Solicitud_Entrega

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easycadete.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easycadete.MostrarPersonas
import com.example.easycadete.NivelFuncion
import com.example.easycadete.ResultadoPersona


class Solicitud_Cadete : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_solicitud_cadete)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nivelFuncion = NivelFuncion()
        val personas = nivelFuncion.AsignarCadetes(this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MostrarPersonas(personas) { persona ->
            onActionButtonClick(persona)
        }
        val PaqueteBut= findViewById<Button>(R.id.button5)
        recyclerView.adapter = adapter
        println("awoooga")
        PaqueteBut.setOnClickListener {
            val i = Intent(this, Paquete::class.java)
            ContextCompat.startActivity(this, i, null)
        }
    }
    private fun onActionButtonClick(persona: ResultadoPersona) {
        // Implement the action you want to perform when the button is clicked
        Toast.makeText(this, "Button clicked for ${persona.Nombre}", Toast.LENGTH_SHORT).show()
    }

}