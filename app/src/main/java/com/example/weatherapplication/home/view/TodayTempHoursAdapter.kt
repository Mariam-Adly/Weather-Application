package com.example.weatherapplication.home.view

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.utility.Utility
import com.example.weatherapplication.databinding.TodayTempBinding
import com.example.weatherapplication.model.Hourly
import java.time.LocalDateTime
import java.util.*

class TodayTempHoursAdapter(
    var hoursList: List<Hourly>,
    var context: Context?,
) : RecyclerView.Adapter<TodayTempHoursAdapter.ViewHolder>(){


    val unitShared = context?.getSharedPreferences("getSharedPreferences", Activity.MODE_PRIVATE)
    var unit = unitShared?.getString("units","metric")!!
 val sharedPreferences = context?.getSharedPreferences("language", Activity.MODE_PRIVATE)
   var lang = sharedPreferences?.getString("myLang","")!!

    inner class ViewHolder(val binding: TodayTempBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.hourTempStatusIcon
            binding.hourTxt
            binding.tempHour
            binding.hourTempDegreeTxt
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodayTempHoursAdapter.ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding : TodayTempBinding = TodayTempBinding.inflate(inflater, parent, false)
        val viewHolder : ViewHolder = ViewHolder(binding)
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TodayTempHoursAdapter.ViewHolder, position: Int) {
        holder.binding.hourTempStatusIcon.setImageResource(Utility.getWeatherIcon(hoursList[position].weather[0].icon!!))
        if(lang == "eng" && unit == "metric") {
            holder.binding.hourTempDegreeTxt.text = "${hoursList[position].temp!!.toInt()} ℃"
            holder.binding.hourTxt.text = Utility.timeStampToHour(hoursList[position].dt,lang)
            }else if (lang == "ar" && unit == "metric"){
            holder.binding.hourTxt.text = Utility.timeStampToHour(hoursList[position].dt,lang)
            holder.binding.hourTempDegreeTxt.text = "${Utility.convertNumbersToArabic(hoursList[position].temp!!.toInt())} س°"
            }else if (lang == "eng" && unit == "imperial"){
            holder.binding.hourTempDegreeTxt.text = "${hoursList[position].temp!!.toInt()} ℉"
            holder.binding.hourTxt.text = Utility.timeStampToHour(hoursList[position].dt,lang)
            }
             else if(lang =="ar" && unit == "imperial"){
            holder.binding.hourTempDegreeTxt.text = "${Utility.convertNumbersToArabic(hoursList[position].temp!!.toInt())} ف°"
            holder.binding.hourTxt.text = Utility.timeStampToHour(hoursList[position].dt,lang)
             }
             else if(lang == "eng" && unit == "standard"){
            holder.binding.hourTempDegreeTxt.text = "${hoursList[position].temp!!.toInt()} °K"
            holder.binding.hourTxt.text = Utility.timeStampToHour(hoursList[position].dt,lang)
             }
             else if(lang == "ar" && unit == "standard"){
            holder.binding.hourTempDegreeTxt.text = "${Utility.convertNumbersToArabic(hoursList[position].temp!!.toInt())} ك°"
            holder.binding.hourTxt.text = Utility.timeStampToHour(hoursList[position].dt,lang)
             }
    }

    override fun getItemCount(): Int {
        return hoursList.size -1
    }
}