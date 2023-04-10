package com.example.weatherapplication.home.view


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.FragmentHomeBinding
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.home.viewmodel.HomeViewModel
import com.example.weatherapplication.home.viewmodel.HomeViewModelFactory
import com.example.weatherapplication.model.Daily
import com.example.weatherapplication.model.Hourly
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.utility.ApiState
import com.example.weatherapplication.utility.TrackingUtility
import com.example.weatherapplication.utility.Utility
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
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
    lateinit var lang: String
    lateinit var unit: String
    private lateinit var wind:String
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
        val sharedPreferences = requireActivity().getSharedPreferences("getSharedPreferences", Activity.MODE_PRIVATE)
        lang = sharedPreferences.getString("myLang","eng")!!
        addressGeocoder = Geocoder(requireContext(), Locale.getDefault())
        return view
    }


    override fun onResume() {
        super.onResume()

        if(!Utility.isOnline(requireContext())){
            Toast.makeText(requireContext(), R.string.you_are_offline,Toast.LENGTH_SHORT).show()
        }else {
            getLastLocation()
            progressDialog = ProgressDialog(requireContext())
            progressDialog.setTitle("loading")
            progressDialog.setMessage("data is loading please wait")
            progressDialog.show()
            CoroutineScope(Dispatchers.IO).launch {
                delay(2000)
                progressDialog.dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModelFactory =
            HomeViewModelFactory(
                WeatherRepo.getInstance(
                    RemoteSourceImpl.getInstance(requireContext()),
                    LocalSourceImpl(requireContext()),
                    requireContext()
                )
            )
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(HomeViewModel::class.java)
        viewModel = HomeViewModel(
            WeatherRepo.getInstance(
                RemoteSourceImpl.getInstance(requireContext()),
                LocalSourceImpl(requireContext()),
                requireContext()
            )
        )
        val unitShared =
            requireActivity().getSharedPreferences("getSharedPreferences", Activity.MODE_PRIVATE)
        unit = unitShared.getString("units", "metric")!!
        val windShared = requireActivity().getSharedPreferences("getSharedPreferences", Activity.MODE_PRIVATE)
        wind = windShared.getString("wind","meter")!!
        if (Utility.isOnline(requireContext())) {
            getCurrentWeather()
            requestPermissions()
            initHoursRecycler()
            initWeekRecycler()
             getLastLocation()
            Log.i("mariam", "onViewCreated: ")
        } else {
            Snackbar.make(activity?.window?.decorView!!.rootView, "Offline", Snackbar.LENGTH_LONG)
                .setBackgroundTint(resources.getColor(android.R.color.holo_red_light))
                .show()
            viewModel.getCurrentWeatherDB()
            lifecycleScope.launch(){
                viewModel.weather.collectLatest{ it ->
                    when(it){
                        is ApiState.Failure -> {Toast. makeText ( requireContext(),  "Check ${it.msg}",Toast.LENGTH_SHORT) .show ()}
                        is ApiState.Loading -> {
                            progressDialog = ProgressDialog(requireContext())
                            progressDialog.setTitle("loading")
                            progressDialog.setMessage("data is loading please wait")
                            progressDialog.show()
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(2000)
                                progressDialog.dismiss()
                            }
                        }
                        is ApiState.Success -> {
                            binding.locationName.text = it.data.timezone
                            initHoursRecycler()
                            initWeekRecycler()
                            updateUIWithWeatherData(it.data)
                        }
                    }

                }
            }
        }
    }

    fun getCurrentWeather() {
     //   viewModel.getCurrentTemp(latitude,longitude,lang,unit)
        lifecycleScope.launch {
            viewModel.weather.collectLatest {
                it -> when(it){
                is ApiState.Failure -> {Toast. makeText ( requireContext(),  "Check ${it.msg}",Toast.LENGTH_SHORT) .show ()}
                is ApiState.Loading -> {
                    progressDialog = ProgressDialog(requireContext())
                    progressDialog.setTitle("loading")
                    progressDialog.setMessage("data is loading please wait")
                    progressDialog.show()
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(2000)
                        progressDialog.dismiss()
                    }
                }
                is ApiState.Success -> {
                    updateUIWithWeatherData(it.data)
                }
            }

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
        binding.todayTempStatusTxt.text = weather.current.weather[0].description
        binding.homeDate.text = Utility.timeStampToDate(weather.current.dt,lang)
        binding.todayTempStatusIcon.setImageResource(Utility.getWeatherIcon(weather.current.weather[0].icon))
        if(lang == "eng" && unit == "metric") {
            binding.todayTempDegreeTxt.text = "${weather.current.temp.toInt()}°C"
            binding.pressureValueTxt.text = "${weather.current.pressure} hPa"
            binding.humidityValueTxt.text = "${weather.current.humidity} %"
            binding.windValueTxt.text = "${weather.current.windSpeed} m/s"
            binding.cloudValueTxt.text = "${weather.current.clouds} m"
            binding.UVValueTxt.text = "${weather.current.uvi.toLong()}%"
            binding.visibilityValueTxt.text = "${weather.current.visibility} %"
        }else if(lang == "ar" && unit == "metric"){
            binding.todayTempDegreeTxt.text = "${Utility.convertNumbersToArabic(weather.current.temp.toInt())}س°"
            binding.pressureValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.pressure)} هـ ب أ"
            binding.humidityValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.humidity)} %"
            binding.windValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.windSpeed)} م/ث"
            binding.cloudValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.clouds)}   م"
            binding.UVValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.uvi)}%"
            binding.visibilityValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.visibility)} %"
        }else if(lang == "eng" && unit == "imperial" && wind == "milis"){
            binding.todayTempDegreeTxt.text = "${weather.current.temp.toInt()}℉"
            binding.pressureValueTxt.text = "${weather.current.pressure} hPa"
            binding.humidityValueTxt.text = "${weather.current.humidity} %"
            binding.windValueTxt.text = "${weather.current.windSpeed} km/h"
            binding.cloudValueTxt.text = "${weather.current.clouds}km"
            binding.UVValueTxt.text = "${weather.current.uvi.toLong()}%"
            binding.visibilityValueTxt.text = "${weather.current.visibility} %"
        }
        else if(lang == "ar" && unit == "imperial"){
            binding.todayTempDegreeTxt.text = "${Utility.convertNumbersToArabic(weather.current.temp.toInt())}ف°"
            binding.pressureValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.pressure)} هـ ب أ"
            binding.humidityValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.humidity)} %"
            binding.windValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.windSpeed)}كم/س"
            binding.cloudValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.clouds)}  كم "
            binding.UVValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.uvi)}%"
            binding.visibilityValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.visibility)} %"
        }
        else if(lang == "eng" && unit == "standard"){
            binding.todayTempDegreeTxt.text = "${weather.current.temp.toInt()}°K"
            binding.pressureValueTxt.text = "${weather.current.pressure} hPa"
            binding.humidityValueTxt.text = "${weather.current.humidity} %"
            binding.windValueTxt.text = "${weather.current.windSpeed} m/s"
            binding.cloudValueTxt.text = "${weather.current.clouds} m"
            binding.UVValueTxt.text = "${weather.current.uvi.toLong()}%"
            binding.visibilityValueTxt.text = "${weather.current.visibility} %"
        }
        else if(lang == "ar" && unit == "standard"){
            binding.todayTempDegreeTxt.text = "${Utility.convertNumbersToArabic(weather.current.temp.toInt())}ك°"
            binding.pressureValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.pressure)} هـ ب أ"
            binding.humidityValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.humidity)} %"
            binding.windValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.windSpeed)} م/ث"
            binding.cloudValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.clouds)}   م"
            binding.UVValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.uvi)}%"
            binding.visibilityValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.visibility)} %"
        }

    }


    fun initHoursRecycler() {
        todayHoursAdapter = TodayTempHoursAdapter(listOf<Hourly>(), context)
        binding.todayTempRecycler.setHasFixedSize(true)
        binding.todayTempRecycler.apply {
            this.adapter = todayHoursAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
    }

    fun initWeekRecycler() {
        weekTempAdapter = WeekTempAdapter(listOf<Daily>(), context)
        binding.allWeekTempRecycler.setHasFixedSize(true)
        binding.allWeekTempRecycler.apply {
            this.adapter = weekTempAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val mLastLocation: Location = locationResult.lastLocation
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude
                //Locale.setDefault(Locale(lang,"eg"))
                Log.i("mariam", "onLocationResult: $latitude and $longitude")
                viewModel.getCurrentTemp(latitude, longitude, lang, unit)
                if(lang == "ar") {
                   Locale.setDefault(Locale(lang))
                 }
                val address = addressGeocoder.getFromLocation(latitude, longitude, 2)
                if (address != null) {
                    binding.locationName.text =
                        "${address[0].subAdminArea}, ${address[0].adminArea}"
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
                newLocation()
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