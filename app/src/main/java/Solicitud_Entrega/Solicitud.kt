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
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class Solicitud : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var map:GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLocationButton: Button
    private lateinit var tvFrom: TextView
    private lateinit var guardar : Button
    private lateinit var btnFrom : Button
    private lateinit var mFromLatLng: LatLng

    // marker
    private  var mMarkerFrom: Marker? = null


    companion object{
        private val FROM_REQUEST_CODE = 1
        private val  TAG = "Mainactivity"
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_solicitud)

//////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////


        //traemos y guardo la api-key en la varible apikey
        val  apikey = getString(R.string.google_map_key)
        Places.initialize(applicationContext, apikey)

        //seteos
        tvFrom = findViewById(R.id.tvFrom)
        guardar = findViewById(R.id.guardar)
        currentLocationButton = findViewById(R.id.currentLocationButton)
        btnFrom = findViewById(R.id.btnFrom)


        //onclick
        btnFrom.setOnClickListener{
            startAutocomplete(FROM_REQUEST_CODE)
        }
        //envia la direccion seleccionada a la activity  main_solicitud
        guardar.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("returnValue", tvFrom.text.toString())
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        //Encontrar mi ubicacion (dispositivo)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        currentLocationButton.setOnClickListener {
        getCurrentLocation()
        }
        createFragment();


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

    //obtenemos direccion de disposivitvo(va a requerir permiso)
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

    //verifica si se obtuvo permiso del usuario o no para acceder a la ubicacion del usuario
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


    //autocomplete de busqueda de google layout
    private  fun  startAutocomplete(requestCode: Int){

        val  fields = listOf(Place.Field.ID,Place.Field.NAME, Place.Field.LAT_LNG,Place.Field.ADDRESS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fields)
            .build(this)
        startActivityForResult(intent,requestCode)
    }

    // obtiene address,LatLng y los guarda en el textview con nombre de direccion
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

    //proceso de autocomple de google
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

    //agregar pin colorado en google
    private fun addMarker(latLng: LatLng,title: String): Marker? {
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title(title)

        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        return map.addMarker(markerOptions)
    }
    //remove y evita que se llene de pines colorados
    private fun setMarkerFrom(latLng: LatLng){
        mMarkerFrom?.remove()
        mMarkerFrom = addMarker(latLng,getString(R.string.marker_title_from))
    }

    //inicio google maps










}