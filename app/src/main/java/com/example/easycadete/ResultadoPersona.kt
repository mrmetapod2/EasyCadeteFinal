package com.example.easycadete

import android.os.Parcel
import android.os.Parcelable

data class ResultadoPersona(
    val Nombre: String?=null,
    val Contraseña: String?=null,
    val ID: String?=null,
    val Apellido: String?=null,
    val Edad: String?=null,
    val DNI: String?=null,
    val Email: String?=null,
    val Telefono: String?=null,
    var UsuarioOCadete: String?=null,
    val Latitud : String?=null,
    val Longitud: String?=null
): Parcelable {
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Nombre)
        parcel.writeString(Contraseña)
        parcel.writeString(ID)
        parcel.writeString(Apellido)
        parcel.writeString(Edad)
        parcel.writeString(DNI)
        parcel.writeString(Email)
        parcel.writeString(Telefono)
        parcel.writeString(UsuarioOCadete)
        parcel.writeString(Latitud)
        parcel.writeString(Longitud)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultadoPersona> {
        override fun createFromParcel(parcel: Parcel): ResultadoPersona {
            return ResultadoPersona(parcel)
        }

        override fun newArray(size: Int): Array<ResultadoPersona?> {
            return arrayOfNulls(size)
        }
    }
}


