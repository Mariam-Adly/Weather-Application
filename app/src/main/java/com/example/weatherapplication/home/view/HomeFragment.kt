package com.example.weatherapplication.home.view


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.databinding.FragmentHomeBinding
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.home.viewmodel.HomeViewModel
import com.example.weatherapplication.home.viewmodel.HomeViewModelFactory
import com.example.weatherapplication.model.Daily
import com.example.weatherapplication.model.Hourly
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.utility.TrackingUtility
import com.example.weatherapplication.utility.Utility
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

const val PERMISSION_ID = 44

class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        var view: View = binding.root
        return view
        requestPermissions()
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("loading")
        progressDialog.setMessage("data is loading please wait")
        progressDialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            progressDialog.dismiss()
        }
        initHoursRecycler()
        initWeekRecycler()
        getCurrentWeather()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModelFactory =
            HomeViewModelFactory(WeatherRepo.getInstance(RemoteSourceImpl.getInstance(),LocalSourceImpl(requireContext())))
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(HomeViewModel::class.java)
        addressGeocoder = Geocoder(requireContext(), Locale.getDefault())
        viewModel = HomeViewModel(WeatherRepo.getInstance(RemoteSourceImpl.getInstance(), LocalSourceImpl(requireContext())))
    }

    fun getCurrentWeather() {

        viewModel.weather.observe(requireActivity()) {
            if (it != null) {
                updateUIWithWeatherData(it)
            }
        }
    }

    fun updateUIWithWeatherData(weather: OpenWeather) {
        getTodayTemp(weather)
        todayHoursAdapter.hoursList = weather.hourly
        todayHoursAdapter.notifyDataSetChanged()
        weekTempAdapter.weekList = weather.daily
        weekTempAdapter.notifyDataSetChanged()
    }

    private fun getTodayTemp(weather: OpenWeather) {
        binding.todayTempDegreeTxt.text = "${weather.current.temp.toInt()}"
        binding.todayTempStatusTxt.text = weather.current.weather[0].description
        binding.todayTempStatusIcon.setImageResource(Utility.getWeatherIcon(weather.current.weather[0].icon))
        binding.pressureValueTxt.text = "${weather.current.pressure.toInt()} hPa"
        binding.humidityValueTxt.text = "${weather.current.humidity.toInt()} %"
        binding.windValueTxt.text = "${weather.current.windSpeed} m/s"
        binding.cloudValueTxt.text = "${weather.current.clouds.toInt()} m"
        binding.UVValueTxt.text = "${weather.current.uvi} %"
        binding.visibilityValueTxt.text = "${weather.current.visibility.toInt()} %"
        binding.homeDate.text = Utility.timeStampToDate(weather.current.dt)

    }


    fun initHoursRecycler() {
        binding.todayTempRecycler
        todayHoursAdapter = TodayTempHoursAdapter(listOf<Hourly>(), context)
        binding.todayTempRecycler.setHasFixedSize(true)
        binding.todayTempRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            this.adapter = todayHoursAdapter
        }
    }

    fun initWeekRecycler() {
        binding.allWeekTempRecycler
        weekTempAdapter = WeekTempAdapter(listOf<Daily>(), context)
        binding.allWeekTempRecycler.setHasFixedSize(true)
        binding.allWeekTempRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = weekTempAdapter
        }
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
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
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
    private fun getLastLocation(): Unit {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        newLocation()
                    } else {
                        try {
                            latitude = location.latitude
                            longitude = location.longitude
                            viewModel.getCurrentTemp(latitude, longitude, lang, unit)
                            val address = addressGeocoder.getFromLocation(
                                location.latitude,
                                location.longitude,
                                1
                            )
                            binding.locationName.text =
                                "${address?.get(0)!!.subAdminArea}, ${address[0].adminArea}"
                            Log.i("mariam", "getLastLocation: ${location.longitude} ${location.latitude} ")
                        } catch (e: Exception) {
                            val snackBar =
                                Snackbar.make(binding.root, "${e.message}", Snackbar.LENGTH_LONG)
                            snackBar.show()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "please turn your GPS location",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

//    @SuppressLint("MissingPermission")
//    private fun getLastLocation() {
//
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                fusedClient.lastLocation.addOnCompleteListener { task ->
//                    var location: Location? = task.result
//                     newLocation()
//                    Log.i("mariam", location?.latitude.toString())
//                    if (location != null) {
//                        viewModel.getCurrentTemp(latitude, longitude, lang, unit)
//                    }
//
//                }
//            } else {
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }
//        } else {
//            requestPermissions()
//        }
//    }

    @SuppressLint("MissingPermission")
    private fun newLocation() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper()!!)
    }

}