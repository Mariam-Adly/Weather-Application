package com.example.weatherapplication

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.weatherapplication.databinding.ActivityMainBinding

class SplashScreen : AppCompatActivity() {
    lateinit var initialSettingDialog : Dialog
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSettingDialog = Dialog(this)
         setLogoAnimated(binding)
    }

    fun setLogoAnimated(binding: ActivityMainBinding){

        binding.lottieWeather.animate().setDuration(1000).setStartDelay(4000)
        Handler().postDelayed(Runnable {
            if (!isFirstTime()){
                Log.e(TAG, "onCreate: not first", )
                startActivity(Intent( this, HomeActivity::class.java))
                finish()
            }
            else{
                Log.e(TAG, "onCreate: first", )
                 showDialog()
            } }, 5000)
    }

    fun isFirstTime() : Boolean{
        var firstTime : SharedPreferences = getSharedPreferences("first", MODE_PRIVATE)
        Log.e(TAG, "isFirstTime: ${firstTime.getBoolean("first", true)}")
        return firstTime.getBoolean("first", true)
    }

    fun showDialog(){
      initialSettingDialog.setContentView(R.layout.fragment_dialog)
        var gps:RadioButton = initialSettingDialog.findViewById(R.id.radio_button_GPS)
        var map:RadioButton = initialSettingDialog.findViewById(R.id.radio_button_Maps)
        var locationGroup : RadioGroup = initialSettingDialog.findViewById(R.id.radioGroup)
        var setBtn : Button = initialSettingDialog.findViewById(R.id.setup_dialog_button)

        setBtn.setOnClickListener {
          if(gps.isChecked or map.isChecked){
              when(locationGroup.checkedRadioButtonId){
                  R.id.radio_button_GPS ->{
                      startActivity(Intent( this, HomeActivity::class.java))
                      finish()
                      Utility.saveFirstTimeEnterAppSharedPref(applicationContext, "first", false)
                  }
                  R.id.radio_button_Maps -> {
                      var mapIntent : Intent = Intent(this, HomeActivity::class.java)
                      startActivity(mapIntent)
                      finish()
                      Utility.saveFirstTimeEnterAppSharedPref(applicationContext, "first", false)
                  }
              }
              initialSettingDialog.dismiss()
          }else{
              Toast.makeText(this, "please choose get location by Map or GPS", Toast.LENGTH_SHORT).show()
          }
        }
        initialSettingDialog.show()
    }
}
