package com.example.weatherapplication.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.media.browse.MediaBrowser.ConnectionCallback
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherapplication.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.weatherapplication.databinding.ActivityMapsBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.internal.ConnectionCallbacks
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import pub.devrel.easypermissions.EasyPermissions

private val REQUEST_CODE = 500

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,LocationListener
    , GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var mLastLocation:Location ? = null
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var mCurrLocationMarker:Marker
    private lateinit var mGoogleApiClient : GoogleApiClient
    private lateinit var mLastLocationRequest: LocationRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    private fun fetchLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
             return
        }
        val task = fusedLocation!!.lastLocation
        task.addOnSuccessListener {location ->
            if (location != null){
                mLastLocation = location
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latLng = LatLng(mLastLocation!!.latitude,mLastLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I Am Here!")
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        mMap.addMarker(markerOptions)
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            if(ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )==PackageManager.PERMISSION_GRANTED){
//                buildGoogleApiClient()
//                mMap!!.isMyLocationEnabled = true
//
//            }
//        }else{
//            buildGoogleApiClient()
//            mMap!!.isMyLocationEnabled = true
//
//        }
        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(REQUEST_CODE){
            REQUEST_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   fetchLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected fun buildGoogleApiClient(){
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }
    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if(mCurrLocationMarker != null){
            mCurrLocationMarker!!.remove()
        }
        val latLng = LatLng(location.latitude,location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)!!
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))
        if(mGoogleApiClient != null){
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnected(p0: Bundle?) {
        mLastLocationRequest = LocationRequest()
        mLastLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mLastLocationRequest.interval= 1000
        mLastLocationRequest.fastestInterval= 1000
        if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )==PackageManager.PERMISSION_GRANTED){
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    fun searchLocation(view: View){
        var location =  binding.searchTxt.text.toString().trim()

        var addressList : List<Address>? = null

        if(location == null || location == ""){
            Toast.makeText(this,"provide location",Toast.LENGTH_SHORT).show()
        }else{
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location,1)

            }catch (e : Exception){
                e.printStackTrace()

            }
            val address = addressList!![0]
            val latLng = LatLng(address.latitude,address.longitude)
            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }

    }

    fun saveLocation(view: View){

    }
}
