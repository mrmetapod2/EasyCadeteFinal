package com.example.easycadete

import android.os.Parcel
import android.os.Parcelable

data class ResultadoSolicitud(

    val ID: String?=null,
    val IdUsuario: String?=null,
    val IdCadete: String?=null,
    val IdPaquete: String?=null,
    val PaquetePeso: String?=null,
    val PaqueteLargo: String?=null,
    var PaqueteAncho: String?=null,
    val PaqueteAlto : String?=null,
    val Estado: String?=null,
    val IdEstado: String?=null,
    val FechaCreacion: String?=null,
    var FechaFinalizacion: String?=null,
    var Importe: String?=null,
    val LatitudIni : String?=null,
    val LongitudIni: String?=null,
    val LatitudFin : String?=null,
    val LongitudFin: String?=null
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ID)
        parcel.writeString(IdUsuario)
        parcel.writeString(IdCadete)
        parcel.writeString(IdPaquete)
        parcel.writeString(PaquetePeso)
        parcel.writeString(PaqueteLargo)
        parcel.writeString(PaqueteAncho)
        parcel.writeString(PaqueteAlto)
        parcel.writeString(Estado)
        parcel.writeString(IdEstado)
        parcel.writeString(FechaCreacion)
        parcel.writeString(FechaFinalizacion)
        parcel.writeString(Importe)
        parcel.writeString(LatitudIni)
        parcel.writeString(LongitudIni)
        parcel.writeString(LatitudFin)
        parcel.writeString(LongitudFin)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultadoSolicitud> {
        override fun createFromParcel(parcel: Parcel): ResultadoSolicitud {
            return ResultadoSolicitud(parcel)
        }

        override fun newArray(size: Int): Array<ResultadoSolicitud?> {
            return arrayOfNulls(size)
        }
    }

}