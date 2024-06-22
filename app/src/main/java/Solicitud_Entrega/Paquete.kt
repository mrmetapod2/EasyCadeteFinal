package Solicitud_Entrega

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easycadete.MainActivity
import com.example.easycadete.R

class Paquete : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paquete)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val editLargo: EditText = findViewById(R.id.edit_largo)
        val editAncho: EditText = findViewById(R.id.edit_ancho)
        val editAlto: EditText = findViewById(R.id.editTextText10)
        val editPeso: EditText = findViewById(R.id.editTextText11)
        val btnEnviar: Button = findViewById(R.id.Enviar)

        btnEnviar.setOnClickListener {
            val largo = editLargo.text.toString()
            val ancho = editAncho.text.toString()
            val alto = editAlto.text.toString()
            val peso = editPeso.text.toString()

            val intent = Intent(this, Main_Solicitud::class.java).apply {
                putExtra("LARGO", largo)
                putExtra("ANCHO", ancho)
                putExtra("ALTO", alto)
                putExtra("PESO", peso)
            }
            startActivity(intent)
        }
    }
}