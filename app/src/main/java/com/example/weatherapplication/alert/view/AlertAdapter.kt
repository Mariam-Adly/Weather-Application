package com.example.weatherapplication.alert.view

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.AlertItemBinding
import com.example.weatherapplication.databinding.ItemFavoriteBinding
import com.example.weatherapplication.favorite.view.FavoriteAdapter
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.utility.Utility

class AlertAdapter (var alertList : List<Alert>,
var context: Context , private val deleteAlertAction: (myAlert: Alert) -> Unit) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    val sharedPreferences = context.getSharedPreferences("getSharedPreferences", Activity.MODE_PRIVATE)
    val language = sharedPreferences.getString("myLang","eng")
    class ViewHolder (val binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imgDelete
            binding.cityName
            binding.txtFrom
            binding.txtTo
            binding.txtTime
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding = AlertItemBinding.inflate(inflater, parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return alertList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = alertList[position]
        if(language == "eng"){
        holder.binding.cityName.text = item.AlertCityName
        holder.binding.txtFrom.text = Utility.timeStampToDate(item.startDay,language)
        holder.binding.txtTime.text = Utility.timeStampToHour(item.Time, language)
        holder.binding.txtTo.text = Utility.timeStampToDate(item.endDay,language)
        }else if(language == "ar"){
            holder.binding.cityName.text = item.AlertCityName
            holder.binding.txtFrom.text = Utility.timeStampToDate(item.startDay,language)
            holder.binding.txtTime.text = Utility.timeStampToHour(item.Time, language)
            holder.binding.txtTo.text = Utility.timeStampToDate(item.endDay,language)
        }
        holder.binding.imgDelete.setOnClickListener {
            deleteAlertAction(item)
        }
    }
}