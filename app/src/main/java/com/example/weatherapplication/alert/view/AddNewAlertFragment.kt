package com.example.weatherapplication.alert.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapplication.alert.viewmodel.AlertViewModel
import com.example.weatherapplication.alert.viewmodel.AlertViewModelFactory
import com.example.weatherapplication.databinding.FragmentAddNewAlertBinding
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.favorite.view.FavoriteFragmentDirections
import com.example.weatherapplication.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapplication.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapplication.map.MapsActivity
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.utility.Utility
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.DoubleBinaryOperator
import kotlin.math.min

class AddNewAlertFragment : DialogFragment() {

    lateinit var binding: FragmentAddNewAlertBinding
    var date : String = ""
    var timeFormat : String = ""
    lateinit var myAlert : Alert
    lateinit var alertViewModel: AlertViewModel
    lateinit var alertFactory:AlertViewModelFactory

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
        alertFactory = AlertViewModelFactory(WeatherRepo.getInstance(RemoteSourceImpl.getInstance(), LocalSourceImpl(requireContext())))
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
            selectDate(binding.tvAlertStartDate)
        }

        binding.tvAlertEndDate.setOnClickListener {
            selectDate(binding.tvAlertEndDate)
        }
        binding.tvAlertTime.setOnClickListener {
            selectTime(binding.tvAlertTime)
        }
        binding.alertCancelBtn.setOnClickListener {
            dismiss()
        }
        binding.alertSubmitBtn.setOnClickListener {
            val current = Calendar.getInstance()
            setFirstUi(current.timeInMillis)
            alertViewModel.insertAlert(myAlert)
            dismiss()
        }

    }


    fun setFirstUi(current: Long) {
        val current = current.div(1000L)
        val dateplus = (86400L) + current
        myAlert = Alert(current,current,dateplus,0.0,0.0, address)
    }
    override fun onStart() {
        super.onStart()
        if(address.isNotEmpty()){
            binding.tvSelectedAlertLoc.text = address
        }
    }

    fun selectDate(textView: TextView){
        var format = SimpleDateFormat("dd MMM, YYYY", Locale.US)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener{
                view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            date = format.format(calendar.time)
            textView.text= date
        },
            year,month,day
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
         datePicker.show()
    }

    fun selectTime(textView: TextView){
        var format = SimpleDateFormat("HH:mm")
        val time = Calendar.getInstance()
        val hours = time.get(Calendar.HOUR_OF_DAY)
        val mins = time.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog.OnTimeSetListener { timePick, hour, minute ->
            time.set(Calendar.HOUR_OF_DAY, hour)
            time.set(Calendar.MINUTE,minute)
            timeFormat = format.format(time.time)
            textView.text= timeFormat
        }
        TimePickerDialog(requireContext(),timePicker,hours, mins,true).show()
    }

}