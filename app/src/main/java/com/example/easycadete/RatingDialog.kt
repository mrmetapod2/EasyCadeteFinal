package com.example.easycadete

import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class RatingDialog(private val context: Context) {

    // Método para mostrar el diálogo
    fun show() {
        // Inflar el layout personalizado
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_rating, null)

        // Inicializar RatingBar y EditText
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val editTextComment = dialogView.findViewById<EditText>(R.id.editTextComment)


        AlertDialog.Builder(context)
            .setTitle("Califica nuestro servicio")
            .setView(dialogView)
            .setPositiveButton("Enviar") { _, _ ->

                // Obtener la calificación y el comentario
                val rating = ratingBar.rating
                val comment = editTextComment.text.toString()

                //AQUI VA LA SINTAXIS PARA ENVIAR EL COMENTARIO A DONDE DESEEMOS

                // Mostrar un Toast con la calificación y el comentario
                Toast.makeText(context, "Calificación: $rating\nComentario: $comment", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }
}
