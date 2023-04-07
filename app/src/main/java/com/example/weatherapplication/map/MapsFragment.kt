package com.example.weatherapplication.map

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.media.browse.MediaBrowser.ConnectionCallback
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putFloat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.alert.view.AddNewAlertFragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.weatherapplication.databinding.ActivityMapsBinding
import com.example.weatherapplication.databinding.FragmentHomeBinding
import com.example.weatherapplication.databinding.FragmentMapsBinding
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSource
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapplication.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.utility.Utility
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.common.api.internal.ConnectionCallbacks
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import java.io.IOException
import java.util.*

private val REQUEST_CODE = 500

class MapsFragment : Fragment(), OnMapReadyCallback,LocationListener
    , GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {


    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private var mLastLocation: Location? = null
    private var latLng : LatLng ? = null
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var mCurrLocationMarker: Marker
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLastLocationRequest: LocationRequest
    private lateinit var addressGeocoder:Geocoder
    private lateinit var favoriteWeather: FavoriteWeather
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    var key : Int = 0
    lateinit var lang:String


    private val callback = OnMapReadyCallback { googleMap ->
        Log.i("mariam", ": OnMapReadyCallback : $mLastLocation ")
        mMap = googleMap
        latLng = LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng!!).title("I Am Here!")
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng!!))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 15f))
        mMap.addMarker(markerOptions)
        googleMap.setOnMapClickListener {
            Log.i("mariam", "setOnMapClickListener: $mLastLocation ")

        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("language", Activity.MODE_PRIVATE)
        lang = sharedPreferences.getString("myLang","")!!
        val bundle= activity?.intent?.extras
        if (bundle != null) {
            key=bundle.getInt("key")
        }
        favoriteViewModelFactory = FavoriteViewModelFactory(WeatherRepo.getInstance(RemoteSourceImpl.getInstance(requireContext()), LocalSourceImpl(requireContext()),requireContext()))
        favoriteViewModel = ViewModelProvider(this,favoriteViewModelFactory).get(FavoriteViewModel::class.java)
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())
        addressGeocoder = Geocoder(requireContext(), Locale.getDefault())
        fetchLocation()


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        var view: View = binding.root
        binding.searchBtn.setOnClickListener {
            searchLocation()
        }

        binding.saveFavBtn.setOnClickListener {
            if (key == 3) {
                AddNewAlertFragment.address =getAddressAndDateForLocation(latLng!!.latitude, latLng!!.longitude)
                AddNewAlertFragment.lat = latLng!!.latitude
                AddNewAlertFragment.lon = latLng!!.longitude
                requireActivity().finish()
            } else {
                favoriteViewModel.insertFavoritePlace(favoriteWeather)
                requireActivity().finish()
            }
        }
        return view
    }


    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), REQUEST_CODE
            )
            return
        }
        val task = fusedLocation!!.lastLocation
        task.addOnSuccessListener { location ->
            Log.i("mariam", "fetchLocation: $location ")
            if (location != null) {
                mLastLocation = location
                Log.i("mariam", "fetchLocation: $mLastLocation ")
                val mapFragment = childFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment?.getMapAsync(callback)
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (REQUEST_CODE) {
            REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng!!)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)!!
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng!!))
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))
        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(requireActivity())
        }
    }

    override fun onConnected(p0: Bundle?) {
        mLastLocationRequest = LocationRequest()
        mLastLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mLastLocationRequest.interval = 1000
        mLastLocationRequest.fastestInterval = 1000
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(requireActivity())
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    fun getAddressAndDateForLocation(lat : Double, lon : Double) : String{
        //GPSLat GPSLong
        Locale.setDefault(Locale(lang))
         addressGeocoder  = Geocoder(requireContext(), Locale.getDefault())
        try {
            var myAddress : List<Address> = addressGeocoder.getFromLocation(lat, lon, 2)!!
            if(myAddress.isNotEmpty()){
                return "${myAddress[0].adminArea},${myAddress[0].countryName}"
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
        return ""
    }

    fun searchLocation() {
        var location =  binding.searchTxt.text.toString().trim()

        var addressList : List<Address>? = null

        if(location == null || location == ""){
            Toast.makeText(requireContext(),"provide location",Toast.LENGTH_SHORT).show()
        }else{
            Locale.setDefault(Locale(lang))
            val geoCoder = Geocoder(requireContext(), Locale.getDefault())
            try {
                addressList = geoCoder.getFromLocationName(location,1)

            }catch (e : Exception){
                e.printStackTrace()

            }
            val address = addressList!![0]
             latLng = LatLng(address.latitude,address.longitude)
            favoriteWeather = FavoriteWeather(getAddressAndDateForLocation(latLng!!.latitude,latLng!!.longitude),latLng!!.latitude,latLng!!.longitude)
            latLng ?.let {
                  mMap!!.addMarker(MarkerOptions().position(it).title(location))
                  mMap!!.animateCamera(CameraUpdateFactory.newLatLng(it))
              }
        }

        }



}
