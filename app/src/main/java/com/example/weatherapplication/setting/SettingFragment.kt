package com.example.weatherapplication.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
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

     lateinit var binding : FragmentSettingBinding
     lateinit var sharedPreference:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        var view : View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       sharedPreference =
            requireActivity().getSharedPreferences("getSharedPreferences", Context.MODE_PRIVATE)
        changeLocation()
        changeLanguage()
        changeTemp()
        loadLocale()
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
                    startActivity(Intent( requireActivity(), MainActivity::class.java))
                }
                checkedButtonId == binding.radioButtonMilis.id -> {
                    startActivity(Intent( requireActivity(), MapsActivity::class.java))
                }
            }
        }
    }
    private fun changeLocation(){
        binding.groupLocation.setOnCheckedChangeListener{ radioGroup, checkedButtonId ->
            when{
                 checkedButtonId == binding.radioButtonGPS.id -> {
                    startActivity(Intent( requireActivity(), MainActivity::class.java))
                }
                checkedButtonId == binding.radioButtonMaps.id -> {
                    startActivity(Intent( requireActivity(), MapsActivity::class.java))
                }
            }
        }
    }

    private fun changeLanguage(){

        binding.groupLan.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.radioButtonEnglish.id -> {
                     setLocale("eng")
                     refreshFragment()
                    startActivity(Intent( requireActivity(), MainActivity::class.java))
                }
                checkedButtonId == binding.radioButtonArabic.id -> {
                     setLocale("ar")
                     refreshFragment()
                    startActivity(Intent( requireActivity(), MainActivity::class.java))
                }
            }
        }
    }

     private  fun setLocale(language: String){
         val metric = resources.displayMetrics
         var locale = Locale(language)
         Locale.setDefault(locale)
         var congig = Configuration()
         congig.locale = locale
         resources.updateConfiguration(congig,metric)
         val editor = requireActivity().getSharedPreferences("language",Context.MODE_PRIVATE).edit()
         editor.putString("myLang",language)
         editor.apply()
     }
     private fun loadLocale(){
         val sharedPreferences = requireActivity().getSharedPreferences("language",Activity.MODE_PRIVATE)
         val language = sharedPreferences.getString("myLang","")
         setLocale(language!!)
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
        val ft: FragmentTransaction = this.fragmentManager!!.beginTransaction()
        ft.detach(this)
        ft.attach(this)
        ft.commit()
       // fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }

}