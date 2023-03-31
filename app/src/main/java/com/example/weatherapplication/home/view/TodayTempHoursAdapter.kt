package com.example.weatherapplication.home.view

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

//    var languageShared = context?.getSharedPreferences("Language", Context.MODE_PRIVATE)
//    var langu = languageShared?.getString(Utility.Language_Key, "en")!!
//    var unitsShared = context?.getSharedPreferences("Units", Context.MODE_PRIVATE)
//    var unit = unitsShared?.getString(Utility.TEMP_KEY,"metric")!!

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
            holder.binding.hourTxt.text = Utility.timeStampToHour(hoursList[position].dt)
            holder.binding.hourTempDegreeTxt.text = "${hoursList[position].temp!!.toInt()} ℃"
            holder.binding.hourTempStatusIcon.setImageResource(Utility.getWeatherIcon(hoursList[position].weather[0].icon!!))

    }

    override fun getItemCount(): Int {
        return hoursList.size -1
    }
}