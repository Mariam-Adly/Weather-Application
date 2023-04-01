package com.example.weatherapplication.alert.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.AlertItemBinding
import com.example.weatherapplication.databinding.ItemFavoriteBinding
import com.example.weatherapplication.favorite.view.FavoriteAdapter
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.utility.Utility

class AlertAdapter (var alertList : List<Alert>,
var context: Context) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {


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
        holder.binding.cityName.text = item.AlertCityName
        holder.binding.txtFrom.text = Utility.timeStampToDate(item.startDay)
        holder.binding.txtTime.text = Utility.timeStampToHour(item.Time)
        holder.binding.txtTo.text = Utility.timeStampToDate(item.endDay)

    }
}