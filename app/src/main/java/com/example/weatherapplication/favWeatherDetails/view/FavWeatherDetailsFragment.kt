package com.example.weatherapplication.favWeatherDetails.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.databinding.FragmentFavWeatherDetailsBinding
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.favWeatherDetails.viewmodel.FavWeatherDetailsFactory
import com.example.weatherapplication.favWeatherDetails.viewmodel.FavWeatherDetailsViewModel
import com.example.weatherapplication.home.view.TodayTempHoursAdapter
import com.example.weatherapplication.home.view.WeekTempAdapter
import com.example.weatherapplication.model.Daily
import com.example.weatherapplication.model.Hourly
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.utility.Utility

class FavWeatherDetailsFragment : Fragment() {


    private lateinit var binding : FragmentFavWeatherDetailsBinding
    private lateinit var todayTempHoursAdapter: TodayTempHoursAdapter
    private lateinit var weekTempAdapter: WeekTempAdapter
    private lateinit var detailsViewModel: FavWeatherDetailsViewModel
    private lateinit var detailsFactory: FavWeatherDetailsFactory
    lateinit var lang : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavWeatherDetailsBinding.inflate(inflater, container, false)
        var view: View = binding.root
        return view
    }

    override fun onResume() {
        super.onResume()
        getCurrentWeather()
        initHoursRecycler()
        initWeekRecycler()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsFactory =
            FavWeatherDetailsFactory(
                WeatherRepo.getInstance(
                    RemoteSourceImpl.getInstance(requireContext()),
                    LocalSourceImpl(requireContext())
                ,requireContext()))
        detailsViewModel =
            ViewModelProvider(requireActivity(), detailsFactory).get(FavWeatherDetailsViewModel::class.java)
        detailsViewModel = FavWeatherDetailsViewModel(WeatherRepo.getInstance(RemoteSourceImpl.getInstance(requireContext()), LocalSourceImpl(requireContext()),requireContext()))
        val sharedPreferences = context?.getSharedPreferences("getSharedPreferences", Activity.MODE_PRIVATE)
         lang = sharedPreferences?.getString("myLang"," ")!!
        getCurrentWeather()
        initHoursRecycler()
        initWeekRecycler()
    }

    fun getCurrentWeather() {

        FavWeatherDetailsFragmentArgs.fromBundle(requireArguments()).favPlace?.let {
            detailsViewModel.getFavWeatherData(it)
            binding.locationName.text = it.favLocationName.toString()
            Log.i("mariam", "onViewCreated: $it")
        }
          detailsViewModel.weather.observe(requireActivity()){
            if (it != null) {
                updateUIWithWeatherData(it)
            }
        }
    }

    fun updateUIWithWeatherData(weather: OpenWeather) {
        getTodayTemp(weather)
        todayTempHoursAdapter.hoursList = weather.hourly
        todayTempHoursAdapter.notifyDataSetChanged()
        weekTempAdapter.weekList = weather.daily
        weekTempAdapter.notifyDataSetChanged()
    }

    private fun getTodayTemp(weather: OpenWeather) {
        if(lang == "eng") {
            binding.todayTempDegreeTxt.text = "${weather.current.temp.toInt()}°C"
            binding.todayTempStatusTxt.text = weather.current.weather[0].description
            binding.todayTempStatusIcon.setImageResource(Utility.getWeatherIcon(weather.current.weather[0].icon))
            binding.pressureValueTxt.text = "${weather.current.pressure} hPa"
            binding.humidityValueTxt.text = "${weather.current.humidity} %"
            binding.windValueTxt.text = "${weather.current.windSpeed} m/s"
            binding.cloudValueTxt.text = "${weather.current.clouds} m"
            binding.UVValueTxt.text = "${weather.current.uvi.toLong()}%"
            binding.visibilityValueTxt.text = "${weather.current.visibility} %"
            binding.homeDate.text = Utility.timeStampToDate(weather.current.dt,lang)
        }else if (lang == "ar"){
            binding.todayTempStatusTxt.text = weather.current.weather[0].description
            binding.todayTempStatusIcon.setImageResource(Utility.getWeatherIcon(weather.current.weather[0].icon))
            binding.homeDate.text = Utility.timeStampToDate(weather.current.dt,lang)
            binding.todayTempDegreeTxt.text = "${Utility.convertNumbersToArabic(weather.current.temp.toInt())}س°"
            binding.pressureValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.pressure)} هـ ب أ"
            binding.humidityValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.humidity)} %"
            binding.windValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.windSpeed)} م/ث"
            binding.cloudValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.clouds)}   م"
            binding.UVValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.uvi)}%"
            binding.visibilityValueTxt.text = "${Utility.convertNumbersToArabic(weather.current.visibility)} %"

        }

    }


    fun initHoursRecycler() {
        todayTempHoursAdapter = TodayTempHoursAdapter(listOf<Hourly>(), context)
        binding.todayTempRecycler.setHasFixedSize(true)
        binding.todayTempRecycler.apply {
            this.adapter = todayTempHoursAdapter
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

}