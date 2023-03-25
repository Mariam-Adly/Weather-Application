package com.example.weatherapplication

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import pub.devrel.easypermissions.EasyPermissions
import androidx.core.content.ContextCompat.startActivity
import com.example.weatherapplication.databinding.FragmentHomeBinding
import com.example.weatherapplication.home.view.PERMISSION_ID
import com.example.weatherapplication.home.view.TodayTempHoursAdapter
import com.example.weatherapplication.home.view.WeekTempAdapter
import com.example.weatherapplication.home.viewmodel.HomeViewModel
import com.example.weatherapplication.home.viewmodel.HomeViewModelFactory
import com.example.weatherapplication.utility.TrackingUtility
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import pub.devrel.easypermissions.AppSettingsDialog

class Location :EasyPermissions.PermissionCallbacks, AppCompatActivity(){

    private lateinit var binding: FragmentHomeBinding
    lateinit var todayHoursAdapter: TodayTempHoursAdapter
    lateinit var viewModel: HomeViewModel
    lateinit var viewModelFactory: HomeViewModelFactory
    lateinit var weekTempAdapter: WeekTempAdapter
    private lateinit var fusedClient: FusedLocationProviderClient
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var lang: String = "eng"
    var unit: String = "metric"
    lateinit var addressGeocoder: Geocoder
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermissions()
        getLastLocation()
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val mLastLocation: Location? = locationResult.lastLocation
            if (mLastLocation != null) {
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude
                val address = addressGeocoder.getFromLocation(latitude, longitude, 1)
                if (address != null) {
                    binding.locationName.text =
                        "${address[0].subAdminArea}, ${address[0].adminArea}"
                }
            }
        }

    }


   private fun checkPermissions(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
               this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
           this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


     fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(this)) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "you need to accept location to use this app.",
                PERMISSION_ID,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {

            EasyPermissions.requestPermissions(
                this,
                "you need to accept location to use this app.",
                PERMISSION_ID,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )

        }
    }

    @SuppressLint("MissingPermission")
     fun getLastLocation(): Unit {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        newLocation()
                    } else {
                        latitude = location.latitude
                        longitude = location.longitude
                        try {
                            viewModel.getCurrentTemp(latitude, longitude, lang, unit)
                            val address = addressGeocoder.getFromLocation(
                                location.latitude,
                                location.longitude,
                                1
                            )
                            binding.locationName.text =
                                "${address?.get(0)!!.subAdminArea}, ${address[0].adminArea}"
                            Log.i("mariam", "getLastLocation: ${location.longitude} ")
                        } catch (e: Exception) {
                            val snackBar =
                                Snackbar.make(binding.root, "${e.message}", Snackbar.LENGTH_LONG)
                            snackBar.show()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "please turn your GPS location",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                this.startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun newLocation() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        fusedClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper()!!)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }
}