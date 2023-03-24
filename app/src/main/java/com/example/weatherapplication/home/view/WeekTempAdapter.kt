package com.example.weatherapplication.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.utility.Utility
import com.example.weatherapplication.databinding.AllWeekTempBinding
import com.example.weatherapplication.model.Daily


class WeekTempAdapter(
    var weekList: List<Daily>,
    var context: Context?
) : RecyclerView.Adapter<WeekTempAdapter.ViewHolder>(){

    var languageShared = context?.getSharedPreferences("Language", Context.MODE_PRIVATE)
    var langu = languageShared?.getString(Utility.Language_Key, "en")!!
    var unitsShared = context?.getSharedPreferences("Units", Context.MODE_PRIVATE)
    var unit = unitsShared?.getString(Utility.TEMP_KEY,"metric")!!

    class ViewHolder(val binding: AllWeekTempBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.weekDayTxt
            binding.weekDayDateTxt
            binding.weekDayTempDegreeTxt
            binding.weekDayTempStatusIcon
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekTempAdapter.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding : AllWeekTempBinding = AllWeekTempBinding.inflate(inflater, parent, false)
        val viewHolder  : ViewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: WeekTempAdapter.ViewHolder, position: Int) {
        with(holder){
            holder.binding.weekDayTxt.text = Utility.timeStampToDay(weekList[position].dt)
            Log.i("mariam", "onBindViewHolder: ${Utility.timeStampToDay(weekList[position].dt)}")
            holder.binding.weekDayDateTxt.text = Utility.timeStampToDate(weekList[position].dt)

            if(langu == "en" && unit == "metric"){
                holder.binding.weekDayTempDegreeTxt.text = "${weekList[position].temp!!.max!!.toInt()} / ${weekList[position].temp!!.min!!.toInt()} ℃"
            }else if(langu == "ar" && unit == "metric"){
                holder.binding.weekDayTempDegreeTxt.text = "${Utility.convertNumbersToArabic(weekList[position].temp!!.max!!.toInt())} / ${Utility.convertNumbersToArabic(weekList[position].temp!!.min!!.toInt())} س° "
            }else if(langu == "en" && unit == "imperial"){
                holder.binding.weekDayTempDegreeTxt.text = "${weekList[position].temp!!.max!!.toInt()} / ${weekList[position].temp!!.min!!.toInt()} ℉"
            }else if(langu == "ar" && unit == "imperial"){
                holder.binding.weekDayTempDegreeTxt.text = "${Utility.convertNumbersToArabic(weekList[position].temp!!.max!!.toInt())} / ${Utility.convertNumbersToArabic(weekList[position].temp!!.min!!.toInt())}ف° "
            }else if(langu == "en" && unit == "standard"){
                holder.binding.weekDayTempDegreeTxt.text = "${weekList[position].temp!!.max!!.toInt()} / ${weekList[position].temp!!.min!!.toInt()} °K"
            }else if(langu == "ar" && unit == "standard"){
                holder.binding.weekDayTempDegreeTxt.text = "${Utility.convertNumbersToArabic(weekList[position].temp!!.max!!.toInt())} / ${Utility.convertNumbersToArabic(weekList[position].temp!!.min!!.toInt())} ك° "
            }
            holder.binding.weekDayTempStatusIcon.setImageResource(Utility.getWeatherIcon(weekList[position].weather[0].icon!!))
        }
    }

    override fun getItemCount(): Int {
        return weekList.size
    }
}