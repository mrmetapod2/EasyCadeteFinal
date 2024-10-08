package Solicitud_Entrega

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easycadete.PantallaUsuario
import com.example.easycadete.R

class Main_Solicitud : AppCompatActivity() {
    private lateinit var textOrigen: TextView
    private lateinit var textDest: TextView
    private lateinit var textDestl: TextView
    private lateinit var btnOrigen: Button
    private lateinit var btnDest: Button
    private lateinit var btnpaquete: Button
    private lateinit var btnENVIO: Button
    private lateinit var card2: CardView

    private val REQUEST_CODE_ORIGEN = 1
    private val REQUEST_CODE_DEST = 2
    private val REQUEST_CODE_PAQUETE = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_solicitud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textOrigen = findViewById(R.id.textorigen)
        btnOrigen = findViewById(R.id.btnorigen)
        textDest = findViewById(R.id.textdest)
        btnDest = findViewById(R.id.btndest)
        btnpaquete = findViewById(R.id.btnpaquete)
        textDestl = findViewById(R.id.textdestl)
        card2 = findViewById(R.id.cardView2)

        btnENVIO = findViewById(R.id.btnenvio)

        btnENVIO.setOnClickListener {

            //Levantamos todos los datos y los enviamos a la pantalla de usuario
            val origen = textOrigen.text.toString()
            val destino = textDest.text.toString()
            val medidasPaquete = textDestl.text.toString()

            // Validar que los campos no estén vacíos
            if (origen.isNotEmpty() && destino.isNotEmpty() && medidasPaquete.isNotEmpty()) {
                // Crear el intent para enviar los datos a la otra actividad
                val intent = Intent(this, PantallaUsuario::class.java).apply {
                    putExtra("ORIGEN", origen)
                    putExtra("DESTINO", destino)
                    putExtra("PAQUETE", medidasPaquete)
                }
                startActivity(intent) // Iniciar la otra actividad con los datos
            } else {
                // Mostrar un mensaje de error si algún campo está vacío
                Toast.makeText(this, "Por favor, completa todos los campos antes de enviar.", Toast.LENGTH_SHORT).show()
            }
        }


        btnOrigen.setOnClickListener {
            val intent = Intent(this, Solicitud_Envio::class.java)
            startActivityForResult(intent, REQUEST_CODE_ORIGEN)
        }

        btnDest.setOnClickListener {
            val intent = Intent(this, Solicitud_Envio::class.java)
            startActivityForResult(intent, REQUEST_CODE_DEST)
        }
        btnpaquete.setOnClickListener {
            val intent = Intent(this, Paquete::class.java)
            startActivityForResult(intent, REQUEST_CODE_PAQUETE)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val returnValue = data.getStringExtra("returnValue")
            when (requestCode) {
                REQUEST_CODE_ORIGEN -> {
                    textOrigen.text = returnValue
                    if (!returnValue.isNullOrEmpty()) {
                        card2.visibility = View.VISIBLE
                    }
                }
                REQUEST_CODE_DEST -> textDest.text = returnValue


                REQUEST_CODE_PAQUETE ->{
                    val largo = data.getStringExtra("LARGO")
                    val ancho = data.getStringExtra("ANCHO")
                    val alto = data.getStringExtra("ALTO")
                    val peso = data.getStringExtra("PESO")

                    if (largo != null && ancho != null && alto != null && peso != null) {
                        textDestl.text = "Medidas del paquete:\nLargo: $largo cm\nAncho: $ancho cm\nAlto: $alto cm\nPeso: $peso kg"
                    }else {
                        textDestl.text = "No se recibieron todas las medidas del paquete."
                    }
                }
            }
        }
    }
}
