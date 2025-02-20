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
import com.example.easycadete.NivelFuncion
import com.example.easycadete.PantallaUsuario
import com.example.easycadete.R
import com.example.easycadete.ResultadoPersona
import kotlin.properties.Delegates

class Main_Solicitud : AppCompatActivity() {
    private lateinit var textOrigen: TextView
    private lateinit var textDest: TextView
    private lateinit var textDestl: TextView
    private lateinit var btnOrigen: Button
    private lateinit var btnDest: Button
    private lateinit var btnpaquete: Button
    private lateinit var btnENVIO: Button
    private lateinit var card2: CardView
    private var LatitudIni by Delegates.notNull<Double>()
    private var LongitudIni by Delegates.notNull<Double>()
    private var LatitudFin by Delegates.notNull<Double>()
    private var LongitudFin by Delegates.notNull<Double>()
    private lateinit var largo:String
    private lateinit var ancho:String
    private lateinit var alto:String
    private lateinit var peso:String

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
        val Datos = intent.getParcelableExtra<ResultadoPersona>("result")
        textOrigen = findViewById(R.id.textorigen)
        btnOrigen = findViewById(R.id.btnorigen)
        textDest = findViewById(R.id.textdest)
        btnDest = findViewById(R.id.btndest)
        btnpaquete = findViewById(R.id.btnpaquete)
        textDestl = findViewById(R.id.textdestl)
        card2 = findViewById(R.id.cardView2)
        val nivelFuncion = NivelFuncion()

        btnENVIO = findViewById(R.id.btnenvio)

        btnENVIO.setOnClickListener {

            //Levantamos todos los datos y los enviamos a la pantalla de usuario
            val origen = textOrigen.text.toString()
            val destino = textDest.text.toString()
            val medidasPaquete = textDestl.text.toString()

            // Validar que los campos no estén vacíos
            if (origen.isNotEmpty() && destino.isNotEmpty() && medidasPaquete.isNotEmpty()) {
                // Crear el intent para enviar los datos a la otra actividad
                if(Datos != null){

                    nivelFuncion.AniadirSolicitud(LatitudIni,
                        LongitudIni,
                        LatitudFin,
                        LongitudFin,
                        largo,
                        ancho,
                        alto,
                        peso,
                        Datos.IDCadUsu,
                        this)

                }


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
                    val hardLocation=data.getDoubleArrayExtra("hardLocation")

                    textOrigen.text = returnValue
                    hardLocation?.let {
                        if (it.size >=2) {
                            LatitudIni = it[0]
                            LongitudIni = it[1]
                        }
                        else{

                        }

                    } ?: run {

                    }
                    if (!returnValue.isNullOrEmpty()) {
                        card2.visibility = View.VISIBLE
                    }
                }
                REQUEST_CODE_DEST ->{
                    val hardLocation=data.getDoubleArrayExtra("hardLocation")

                    textOrigen.text = returnValue
                    hardLocation?.let {
                        if (it.size >=2) {
                            LatitudFin = it[0]
                            LongitudFin = it[1]
                        }
                        else{

                        }

                    } ?: run {

                    }
                    textDest.text = returnValue
                }


                REQUEST_CODE_PAQUETE ->{
                    largo = data.getStringExtra("LARGO")!!
                      ancho = data.getStringExtra("ANCHO")!!
                      alto = data.getStringExtra("ALTO")!!
                     peso = data.getStringExtra("PESO")!!


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
