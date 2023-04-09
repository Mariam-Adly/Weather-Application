package com.example.weatherapplication.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.databinding.FragmentSettingBinding
import com.example.weatherapplication.map.MapsActivity
import com.example.weatherapplication.utility.Utility
import kotlinx.coroutines.newFixedThreadPoolContext
import java.util.Locale

class SettingFragment : Fragment() {

    private lateinit var binding : FragmentSettingBinding
    private lateinit var sharedPreference:SharedPreferences



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        var view : View = binding.root
         sharedPreference =
            requireActivity().getSharedPreferences("getSharedPreferences", Context.MODE_PRIVATE)
        initUI()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         sharedPreference =
            requireActivity().getSharedPreferences("getSharedPreferences", Context.MODE_PRIVATE)
         initUI()
        changeLocation()
        changeLanguage()
        changeTemp()
        //loadLocale()
        changeSound()
        changeSoundSpeed()

    }

    enum class Enum_sound(){alarm,notification}
    private fun changeSound(){
        binding.groupAlert.setOnCheckedChangeListener{
                radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.radioButtonAlarm.id -> {
                    sharedPreference.edit()
                        .putString("alert", Enum_sound.alarm.toString())
                        .commit()

                }
                checkedButtonId == binding.radioButtonNotification.id -> {
                    sharedPreference.edit()
                        .putString("alert", Enum_sound.notification.toString())
                        .commit()
                }
            }
        }
    }

    private fun changeSoundSpeed(){
        binding.groupSpeed.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.radioButtonMeter.id -> {
                    sharedPreference.edit().putString("wind","meter").commit()
                }
                checkedButtonId == binding.radioButtonMilis.id -> {
                    sharedPreference.edit().putString("wind","milis").commit()
                }
            }
        }
    }
    private fun changeLocation(){
        binding.groupLocation.setOnCheckedChangeListener{ radioGroup, checkedButtonId ->
            when{
                 checkedButtonId == binding.radioButtonGPS.id -> {
                     sharedPreference.edit().putString("location","gps")
                    startActivity(Intent( requireActivity(), MainActivity::class.java))
                }
                checkedButtonId == binding.radioButtonMaps.id -> {
                    sharedPreference.edit().putString("location","map")
                    startActivity(Intent( requireActivity(), MapsActivity::class.java))
                }
            }
        }
    }

    private fun changeLanguage(){

        binding.groupLan.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.radioButtonEnglish.id -> {
                    sharedPreference.edit().putString("myLang","eng")
                        .commit()
                     setLocale("eng")
                }
                checkedButtonId == binding.radioButtonArabic.id -> {
                    sharedPreference.edit().putString("myLang","ar")
                        .commit()
                     setLocale("ar")
                }
            }
        }
    }

     private  fun setLocale(language: String){
         val metric = resources.displayMetrics
         val configuration = resources.configuration
         configuration.locale = Locale(language)
         Locale.setDefault(Locale(language))
         configuration.setLayoutDirection(Locale(language))
         // update configuration
         resources.updateConfiguration(configuration, metric)
         // notify configuration
         onConfigurationChanged(configuration)
         requireActivity().recreate()
     }


    enum class Enum_units(){standard,metric,imperial}
    private fun changeTemp(){
        binding.groupTemp.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.radioButtonTempMetricCelsius.id ->{
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.METRIC)
                    sharedPreference.edit()
                        .putString("units", Enum_units.metric.toString()).commit()
                    startActivity(Intent( requireActivity(), MainActivity::class.java))
                }
                checkedButtonId == binding.radioButtonTempMetricKelvin.id ->{
                    sharedPreference.edit()
                        .putString("units", Enum_units.standard.toString()).commit()
                    startActivity(Intent( requireActivity(), MainActivity::class.java))
                }
                checkedButtonId == binding.radioButtonTempMetricFahrenheit.id ->{
                    sharedPreference.edit()
                        .putString("units", Enum_units.imperial.toString()).commit()
                    startActivity(Intent( requireActivity(), MainActivity::class.java))
                }
            }
        }
    }

    private fun refreshFragment(){
        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }

    fun initUI() {
        val sharedPreference =
            requireActivity().getSharedPreferences("getSharedPreferences", Context.MODE_PRIVATE)
        var lang = sharedPreference.getString("myLang", "")
        var alert = sharedPreference.getString("alert", Enum_sound.notification.toString())
        var location = sharedPreference.getString("location","")
        var wind = sharedPreference.getString("wind","")
        var units = sharedPreference.getString("units", Enum_units.metric.toString())
        if (lang == "eng") {
            binding.groupLan.check(binding.radioButtonEnglish.id)
        }
        if (lang == "ar") {
            binding.groupLan.check(binding.radioButtonArabic.id)
        }
        if(location == "gps"){
            binding.groupLocation.check(binding.radioButtonGPS.id)
        }
        if(location == "map"){
            binding.groupLocation.check(binding.radioButtonMaps.id)
        }
        if(wind == "meter"){
            binding.groupSpeed.check(binding.radioButtonMeter.id)
        }
        if(wind == "milis"){
            binding.groupSpeed.check(binding.radioButtonMilis.id)
        }
        if (alert == Enum_sound.notification.toString()) {
            binding.groupAlert.check(binding.radioButtonNotification.id)
        }
        if (alert == Enum_sound.alarm.toString()) {
            binding.groupAlert.check(binding.radioButtonAlarm.id)
        }

        if (units == Enum_units.metric.toString()) {
            binding.groupTemp.check(binding.radioButtonTempMetricCelsius.id)
        }
        if (units == Enum_units.standard.toString()) {
            binding.groupTemp.check(binding.radioButtonTempMetricKelvin.id)
        }
        if (units == Enum_units.imperial.toString()) {
            binding.groupTemp.check(binding.radioButtonTempMetricFahrenheit.id)
        }


    }

}