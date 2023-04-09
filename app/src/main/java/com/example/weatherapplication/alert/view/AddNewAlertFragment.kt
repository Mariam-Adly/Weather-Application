package com.example.weatherapplication.alert.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.alert.viewmodel.AlertViewModel
import com.example.weatherapplication.alert.viewmodel.AlertViewModelFactory
import com.example.weatherapplication.databinding.FragmentAddNewAlertBinding
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.map.MapsActivity
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.utility.Utility
import com.example.weatherapplication.workers.OneTimeWork
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AddNewAlertFragment : DialogFragment() {

    lateinit var binding: FragmentAddNewAlertBinding
    var startDate  = 0L
    var endDate = 0L
    var timeFormat  = 0L
    lateinit var myAlert : Alert
    lateinit var alertViewModel: AlertViewModel
    lateinit var alertFactory:AlertViewModelFactory
    var startDay : Int =0
    var startMonth : Int =0
    var startYear : Int =0
    var endDay : Int =0
    var endMonth : Int =0
    var endYear : Int =0
    var hours : Int =0
    var mins : Int =0
    lateinit var language : String

    companion object{
       var address : String = ""
        var lat : Double = 0.0
        var lon : Double = 0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNewAlertBinding.inflate(inflater, container, false)
        var view: View = binding.root
        alertFactory = AlertViewModelFactory(WeatherRepo.getInstance(RemoteSourceImpl.getInstance(requireContext()), LocalSourceImpl(requireContext()),requireContext()))
        alertViewModel = ViewModelProvider(this,alertFactory).get(AlertViewModel::class.java)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setAlertLocBtn.setOnClickListener {
            val intent = Intent(requireActivity(), MapsActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("key", 3)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        binding.tvAlertStartDate.setOnClickListener {
            selectStartDate(binding.tvAlertStartDate)
        }

        binding.tvAlertEndDate.setOnClickListener {
            selectEndDate(binding.tvAlertEndDate)
        }
        binding.tvAlertTime.setOnClickListener {
            selectTime(binding.tvAlertTime)
        }
        binding.alertCancelBtn.setOnClickListener {
            dismiss()
        }

        binding.alertSubmitBtn.setOnClickListener {
            myAlert = Alert(timeFormat, startDate, endDate, lat, lon, address)
            alertViewModel.setAlarm(myAlert)
            OneTimeWork.setWorker(myAlert, requireContext())
            dismiss()
        }
        val sharedPreferences = requireActivity().getSharedPreferences("language", Activity.MODE_PRIVATE)
        language = sharedPreferences.getString("myLang","")!!


    }



    override fun onStart() {
        super.onStart()
        if(address.isNotEmpty()){
            binding.tvSelectedAlertLoc.text = address
        }
    }

    fun selectStartDate(textView: TextView){
        val calendar = Calendar.getInstance()
         startYear = calendar.get(Calendar.YEAR)
         startMonth = calendar.get(Calendar.MONTH)
         startDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener{
                view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            var month = month
            month +=1
            val date = "$dayOfMonth/$month/$year"
            startDate = Utility.getDateMillis(date)
            textView.text= Utility.timeStampToDate(startDate,language)
            startYear = year
            startMonth = month
            startDay = dayOfMonth
        },
            startYear,startMonth,startDay
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
         datePicker.show()
    }

    fun selectEndDate(textView: TextView){
        val calendar = Calendar.getInstance()
        endYear = calendar.get(Calendar.YEAR)
        endMonth = calendar.get(Calendar.MONTH)
        endDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener{
                view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            var month = month
            month +=1
            val date = "$dayOfMonth/$month/$year"
             endDate = Utility.getDateMillis(date)
            textView.text= Utility.timeStampToDate(endDate,language)
            endYear = year
            endMonth = month
           endDay = dayOfMonth
        },
            endYear,endMonth,endDay
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }
    fun selectTime(textView: TextView){
        val time = Calendar.getInstance()
         hours = time.get(Calendar.HOUR_OF_DAY)
         mins = time.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(requireContext(), { view, hour, minute ->
            time.set(Calendar.HOUR_OF_DAY, hour)
            time.set(Calendar.MINUTE,minute)
            timeFormat = (TimeUnit.MINUTES.toSeconds(minute.toLong()) + TimeUnit.HOURS.toSeconds(hour.toLong()))
              timeFormat = timeFormat.minus(3600L * 2)
            textView.text= Utility.timeStampToHour(timeFormat,language)
            hours = hour
            mins = minute
        }, hours,mins,false)
        timePicker.show()
    }



}