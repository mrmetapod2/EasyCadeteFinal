package com.example.easycadete

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class DirectionMap : AppCompatActivity(),OnMapReadyCallback {

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction_map)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val solicitud = intent.getParcelableExtra<ResultadoSolicitud>("result")
         val location1 = LatLng(solicitud!!.LatitudIni!!.toDouble(), solicitud!!.LongitudIni!!.toDouble()) // Example coordinates for location 1
         val location2 = LatLng(solicitud!!.LatitudFin!!.toDouble(), solicitud!!.LongitudFin!!.toDouble()) // Example coordinates for location 2
        mMap = googleMap

        // Add markers for the two locations
        val marker1 = mMap.addMarker(
            MarkerOptions()
                .position(location1)
                .title("Inicio")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )
        val marker2 = mMap.addMarker(
            MarkerOptions()
                .position(location2)
                .title("Final")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )

        // Zoom to show both markers
        val builder = LatLngBounds.Builder()
        builder.include(location1)
        builder.include(location2)
        val bounds = builder.build()
        val padding = 100 // Padding around the markers in pixels
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.animateCamera(cu)
    }
}