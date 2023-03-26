package com.example.weatherapplication.favorite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.ItemFavoriteBinding
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.utility.Utility

class FavoriteAdapter( var favLocationList : List<FavoriteWeather>,
var context: Context, var listener : OnClickFavPlaceListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {



    class ViewHolder (val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root){
      init {
          binding.txtFavTimeZone
          binding.imgDelete
      }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFavoriteBinding.inflate(inflater, parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return favLocationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


            holder.binding.txtFavTimeZone.text = favLocationList[position].favLocationName
            holder.binding.favItem.setOnClickListener { favPlace ->
                if(Utility.isOnline(context)){
                    listener.onClickFavPlace(favLocationList[position])
                }else{
                    Toast.makeText(context, context?.getString(R.string.no_internet_msg), Toast.LENGTH_SHORT).show()
                }
            }

    }

}


