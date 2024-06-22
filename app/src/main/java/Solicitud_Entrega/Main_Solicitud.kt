package Solicitud_Entrega

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easycadete.R

class Main_Solicitud : AppCompatActivity() {
    private lateinit var textOrigen: TextView
    private lateinit var textDest: TextView
    private lateinit var btnOrigen: Button
    private lateinit var btnDest: Button
    private lateinit var card2: CardView

    private val REQUEST_CODE_ORIGEN = 1
    private val REQUEST_CODE_DEST = 2
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
        card2 = findViewById(R.id.cardView2)

        btnOrigen.setOnClickListener {
            val intent = Intent(this, Solicitud_Cadete::class.java)
            startActivityForResult(intent, REQUEST_CODE_ORIGEN)
        }

        btnDest.setOnClickListener {
            val intent = Intent(this, Solicitud_Cadete::class.java)
            startActivityForResult(intent, REQUEST_CODE_DEST)
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
            }
        }
    }
}
