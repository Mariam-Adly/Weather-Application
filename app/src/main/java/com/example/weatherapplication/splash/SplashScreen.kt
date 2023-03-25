package com.example.weatherapplication.splash

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.ActivitySplashScreenBinding
import com.example.weatherapplication.map.MapsActivity
import com.example.weatherapplication.utility.Utility

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val splashScreenScope = lifecycleScope
    lateinit var initialSettingDialog : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialSettingDialog = Dialog(this)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setLogoAnimated(binding)

    }

    fun setLogoAnimated(binding: ActivitySplashScreenBinding){

        binding.animationView.animate().translationX(1500F).setDuration(1000).setStartDelay(4000)
        Handler().postDelayed(Runnable {
            if (!isFirstTime()){
                Log.e("mariam", "onCreate: not first", )
                startActivity(Intent( this, MainActivity::class.java))
                finish()
            }
            else{
                Log.e("mariam", "onCreate: first", )
                openInitialSettingDialog()
            } }, 5000)
    }
    fun isFirstTime() : Boolean{
        var firstTime : SharedPreferences = getSharedPreferences("first", MODE_PRIVATE)
        Log.e("sandra", "isFirstTime: isFirstTime ${firstTime.getBoolean("first", true)}", )
        return firstTime.getBoolean("first", true)
    }

    fun openInitialSettingDialog(){
        initialSettingDialog.setContentView(R.layout.fragment_dialog)
        initialSettingDialog.window?.setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))
        var gpsRB : RadioButton = initialSettingDialog.findViewById(R.id.radio_button_GPS)
        var mapRB : RadioButton = initialSettingDialog.findViewById(R.id.radio_button_Maps)
        var locationGroup : RadioGroup = initialSettingDialog.findViewById(R.id.radioGroup)
        var setBtn : Button = initialSettingDialog.findViewById(R.id.setup_dialog_button)

        setBtn.setOnClickListener {
            if(gpsRB.isChecked or mapRB.isChecked){
                when(locationGroup.checkedRadioButtonId){
                    R.id.radio_button_GPS -> {
                        startActivity(Intent( this, MainActivity::class.java))
                        finish()
                        Utility.saveFirstTimeEnterAppSharedPref(applicationContext, "first", false)
                    }
                    R.id.radio_button_Maps -> {
                        Utility.saveIsMapSharedPref(this, "isMap", true)
                        startActivity(Intent( this, MapsActivity::class.java))
                        finish()
                        Utility.saveFirstTimeEnterAppSharedPref(applicationContext, "first", false)
                    }
                }
                initialSettingDialog.dismiss()
            }else{
                Toast.makeText(this,"please choise one of the choises", Toast.LENGTH_SHORT).show()
            }
        }
        initialSettingDialog.show()
    }

}
