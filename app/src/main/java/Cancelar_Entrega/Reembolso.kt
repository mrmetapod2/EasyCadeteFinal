package Cancelar_Entrega

import android.os.Bundle
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easycadete.R

class Reembolso : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reembolso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val radioGroup: RadioGroup = findViewById(R.id.radioGroupp)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // checkedId es el ID del RadioButton seleccionado
            when (checkedId) {
                R.id.RadioMPago -> {
                    // Acción cuando se selecciona Mercado Pago
                }
                R.id.RadioBco -> {
                    // Acción cuando se selecciona un Banco
                }
            }
        }

    }
}