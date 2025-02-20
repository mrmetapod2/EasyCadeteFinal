package com.example.easycadete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AsignarSolicitudRecycle(
    //se crea una variable para las personas y el boton para asignarlas al trabajo
    private val solicitudes: List<ResultadoSolicitud>,
    private val onDirectionButtonClick: (ResultadoSolicitud) -> Unit,
    private val onAcceptButtonClick: (ResultadoSolicitud) -> Unit

) : RecyclerView.Adapter<AsignarSolicitudRecycle.SolicitudViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_solicitud, parent, false)
        return SolicitudViewHolder(view)
    }

    override fun onBindViewHolder(holder: SolicitudViewHolder, position: Int) {
        val solicitud = solicitudes[position]
        holder.bind(solicitud, onDirectionButtonClick, onAcceptButtonClick)
    }
    //se observa cuantas personas hay
    override fun getItemCount(): Int = solicitudes.size

    class SolicitudViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //se asigna un nomrbe a para añadirlos a la textview
        private val FechaPublicacion: TextView = itemView.findViewById(R.id.FechaPubBox)
        private val PaquetePeso: TextView = itemView.findViewById(R.id.PesoBox)
        private val PaqueteAlto: TextView = itemView.findViewById(R.id.AltoBox)
        private val PaqueteAncho: TextView = itemView.findViewById(R.id.AnchoBox)
        private val PaqueteLargo: TextView = itemView.findViewById(R.id.LargoBox)
        private val DirectionBut: Button = itemView.findViewById(R.id.DirectionBut)
        private val AcceptarBut: Button = itemView.findViewById(R.id.AcceptBut)


        fun bind(
            solicitud: ResultadoSolicitud,

            onDirectionButtonClick: (ResultadoSolicitud) -> Unit,
            onAcceptButtonClick: (ResultadoSolicitud) -> Unit
        ) {
            //se les asigna un valor para cada Solicitud
            FechaPublicacion.text = solicitud.FechaCreacion
            PaquetePeso.text = solicitud.PaquetePeso
            PaqueteAlto.text = solicitud.PaqueteAlto
            PaqueteAncho.text = solicitud.Estado
            PaqueteLargo.text = solicitud.PaqueteLargo
            DirectionBut.setOnClickListener {
                onDirectionButtonClick(solicitud)
            }
            AcceptarBut.setOnClickListener {
                onAcceptButtonClick(solicitud)
            }
        }
    }

}