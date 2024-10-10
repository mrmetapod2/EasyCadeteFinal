package Cancelar_Entrega

import Solicitud_Entrega.Solicitud_Envio
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easycadete.R

class Cancelar_Entrega : AppCompatActivity() {
    private lateinit var buttons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cancelar_entrega)

        buttons = listOf(
            findViewById(R.id.C1),
            findViewById(R.id.C2),
            findViewById(R.id.C3),
            findViewById(R.id.C4),
            findViewById(R.id.C5),
            findViewById(R.id.C6)
        )
        buttons.forEach { button ->
            button.setOnClickListener {
                val intent = Intent(this, Reembolso::class.java)
                startActivity(intent)
            }
        }

    }

}