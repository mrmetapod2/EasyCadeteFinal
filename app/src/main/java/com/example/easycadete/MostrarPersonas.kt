package com.example.easycadete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MostrarPersonas(
    //se crea una variable para las personas y el boton para asignarlas al trabajo
    private val personas: List<ResultadoPersona>,
    private val onButtonClick: (ResultadoPersona) -> Unit
) : RecyclerView.Adapter<MostrarPersonas.PersonaViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonaViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_persona, parent, false)
            return PersonaViewHolder(view)
        }

        override fun onBindViewHolder(holder: PersonaViewHolder, position: Int) {
            val persona = personas[position]
            holder.bind(persona, onButtonClick)
        }
        //se observa cuantas personas hay
        override fun getItemCount(): Int = personas.size

        class PersonaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            //se asigna un nomrbe a para añadirlos a la textview
            private val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
            private val apellidoTextView: TextView = itemView.findViewById(R.id.apellidoTextView)
            private val edadTextView: TextView = itemView.findViewById(R.id.edadTextView)
            private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
            private val actionButton: Button = itemView.findViewById(R.id.actionButton)

            fun bind(persona: ResultadoPersona, onButtonClick: (ResultadoPersona) -> Unit) {
                //se les asigna un valor para cada persona
                nombreTextView.text = persona.Nombre
                apellidoTextView.text = persona.Apellido
                edadTextView.text = persona.Edad
                emailTextView.text = persona.Email
                actionButton.setOnClickListener {
                    //esta funcion va a ser cambiada por una que lo asigna de verdad
                    onButtonClick(persona)
                }
            }
        }

    }