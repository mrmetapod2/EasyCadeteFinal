package Solicitud_Entrega

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.easycadete.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Tile

import java.io.IOException
//////////////////////////////////////////////////////////////////////////////////////////////nuevo
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class Solicitud_Envio : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var map:GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var  guardar:Button
    private lateinit var currentLocationButton: Button
    private lateinit var tvFrom: TextView


    private lateinit var mFromLatLng: LatLng

    //origen y destino marker
    private  var mMarkerFrom: Marker? = null


    companion object{
        private val FROM_REQUEST_CODE = 1
        private val  TAG = "Mainactivity"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_solicitud_envio)


        currentLocationButton = findViewById(R.id.currentLocationButton)


        val  apikey = getString(R.string.google_map_key)
        Places.initialize(applicationContext, apikey)


        tvFrom = findViewById(R.id.tvFrom)
        guardar = findViewById(R.id.btnguardar)
        val btnFrom = findViewById<Button>(R.id.btnFrom)
        btnFrom.setOnClickListener{
            startAutocomplete(FROM_REQUEST_CODE)
        }
        guardar.setOnClickListener{
            // Dentro del método donde guardas los datos y cierras el segundo Activity
            val resultIntent = Intent()
            resultIntent.putExtra("returnValue", tvFrom.text.toString()) // Coloca aquí el valor que quieres enviar de vuelta
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }

        ////////////////////////////////////////////////////////////////////
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        currentLocationButton.setOnClickListener {
            getCurrentLocation()
        }
        createFragment();
    }





    private  fun  startAutocomplete(requestCode: Int){

        val fields = listOf(Place.Field.ID,Place.Field.NAME, Place.Field.LAT_LNG,Place.Field.ADDRESS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fields)
            .build(this)
        startActivityForResult(intent,requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == FROM_REQUEST_CODE){
            processAutocompleteResult(resultCode,data){ place ->
                tvFrom.text= place.address
                place.latLng?.let{
                    mFromLatLng = it
                }
                setMarkerFrom(mFromLatLng)
            }

            return
        }

        super.onActivityResult(requestCode, resultCode, data)


    }
    private fun processAutocompleteResult(resultCode: Int,data: Intent?,callback: (Place)->Unit){

        Log.d(TAG,"processAutocompleteResult(resultCode = $resultCode)")

        when(resultCode) {
            Activity.RESULT_OK -> {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    Log.i(TAG, "Place: $place")
                    callback(place)
                }
            }

            AutocompleteActivity.RESULT_ERROR -> {
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    status.statusMessage?.let { message ->
                        Log.i(TAG, message)
                    }
                }
            }
        }
    }

    private fun addMarker(latLng: LatLng,title: String): Marker? {
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title(title)

        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        return map.addMarker(markerOptions)
    }
    private fun setMarkerFrom(latLng: LatLng){
        mMarkerFrom?.remove()
        mMarkerFrom = addMarker(latLng,getString(R.string.marker_title_from))
    }








    private fun createFragment(){
        val mapFragment:SupportMapFragment  = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


        map.setOnMapClickListener { latLng ->
            map.clear()
            map.addMarker(MarkerOptions().position(latLng))
            getAddressFromLocation(latLng)
        }

        enableMyLocation()

    }
    private fun getAddressFromLocation(latLng: LatLng) {
        val geocoder = Geocoder(this)
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val addressText = "${address.thoroughfare}, ${address.subThoroughfare}, ${address.locality}"
                tvFrom.text = addressText
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    map.clear()
                    map.addMarker(MarkerOptions().position(currentLatLng))
                    getAddressFromLocation(currentLatLng)
                }
            }
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation()
        }
    }







}