package com.example.weatherapplication.home.view

import android.app.Activity
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

//    var languageShared = context?.getSharedPreferences("Language", Context.MODE_PRIVATE)
//    var langu = languageShared?.getString(Utility.Language_Key, "en")!!
     val unitShared = context?.getSharedPreferences("getSharedPreferences", Activity.MODE_PRIVATE)
    var unit = unitShared?.getString("units","metric")!!
val sharedPreferences = context?.getSharedPreferences("getSharedPreferences", Activity.MODE_PRIVATE)
    val language = sharedPreferences?.getString("myLang","eng")
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
          if(language == "eng" && unit == "metric") {
              holder.binding.weekDayTxt.text = Utility.timeStampToDay(weekList[position].dt,language)
              Log.i("mariam", "onBindViewHolder: ${Utility.timeStampToDay(weekList[position].dt,language)}")
              holder.binding.weekDayDateTxt.text = Utility.timeStampToDate(weekList[position].dt,language)
              holder.binding.weekDayTempDegreeTxt.text =
                  "${weekList[position].temp!!.max!!.toInt()} / ${weekList[position].temp!!.min!!.toInt()} ℃"
          }else if(language == "ar" && unit == "metric"){
              holder.binding.weekDayTxt.text = Utility.timeStampToDay(weekList[position].dt,language)
              Log.i("mariam", "onBindViewHolder: ${Utility.timeStampToDay(weekList[position].dt,language)}")
              holder.binding.weekDayDateTxt.text = Utility.timeStampToDate(weekList[position].dt,language)
              holder.binding.weekDayTempDegreeTxt.text =
                  "${Utility.convertNumbersToArabic(weekList[position].temp!!.max!!.toInt())} / ${Utility.convertNumbersToArabic(weekList[position].temp!!.min!!.toInt())} س°"
          } else if (language == "eng" && unit == "imperial"){
              holder.binding.weekDayTxt.text = Utility.timeStampToDay(weekList[position].dt,language)
              Log.i("mariam", "onBindViewHolder: ${Utility.timeStampToDay(weekList[position].dt,language)}")
              holder.binding.weekDayDateTxt.text = Utility.timeStampToDate(weekList[position].dt,language)
              holder.binding.weekDayTempDegreeTxt.text =
                  "${weekList[position].temp!!.max!!.toInt()} / ${weekList[position].temp!!.min!!.toInt()} ℉"
          } else if(language == "ar" && unit == "imperial"){
              holder.binding.weekDayTxt.text = Utility.timeStampToDay(weekList[position].dt,language)
              Log.i("mariam", "onBindViewHolder: ${Utility.timeStampToDay(weekList[position].dt,language)}")
              holder.binding.weekDayDateTxt.text = Utility.timeStampToDate(weekList[position].dt,language)
              holder.binding.weekDayTempDegreeTxt.text =
                  "${Utility.convertNumbersToArabic(weekList[position].temp!!.max!!.toInt())} / ${Utility.convertNumbersToArabic(weekList[position].temp!!.min!!.toInt())}ف°"
          } else if(language == "eng" && unit == "standard"){
              holder.binding.weekDayTxt.text = Utility.timeStampToDay(weekList[position].dt,language)
              Log.i("mariam", "onBindViewHolder: ${Utility.timeStampToDay(weekList[position].dt,language)}")
              holder.binding.weekDayDateTxt.text = Utility.timeStampToDate(weekList[position].dt,language)
              holder.binding.weekDayTempDegreeTxt.text =
                  "${weekList[position].temp!!.max!!.toInt()} / ${weekList[position].temp!!.min!!.toInt()} °K"

          }
         else if(language == "ar" && unit== "standard"){
              holder.binding.weekDayTxt.text = Utility.timeStampToDay(weekList[position].dt,language)
              Log.i("mariam", "onBindViewHolder: ${Utility.timeStampToDay(weekList[position].dt,language)}")
              holder.binding.weekDayDateTxt.text = Utility.timeStampToDate(weekList[position].dt,language)
              holder.binding.weekDayTempDegreeTxt.text =
                  "${Utility.convertNumbersToArabic(weekList[position].temp!!.max!!.toInt())} / ${Utility.convertNumbersToArabic(weekList[position].temp!!.min!!.toInt())} ك°"
         }
        holder.binding.weekDayTempStatusIcon.setImageResource(Utility.getWeatherIcon(weekList[position].weather[0].icon!!))

    }

    override fun getItemCount(): Int {
        return weekList.size
    }
}