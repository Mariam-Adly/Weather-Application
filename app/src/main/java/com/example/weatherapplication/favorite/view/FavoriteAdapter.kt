package com.example.weatherapplication.favorite.view

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.ItemFavoriteBinding
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.utility.Utility

class FavoriteAdapter(
    var favLocationList: List<FavoriteWeather>,
    var context: Context, private val favInterface: OnClickFavPlaceListener
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {



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
    val sharedPreferences = context.getSharedPreferences("language", Activity.MODE_PRIVATE)
    val lang = sharedPreferences.getString("myLang","eng")!!

    override fun getItemCount(): Int {
        return favLocationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


         if(lang == "eng") {
             holder.binding.txtFavTimeZone.text = favLocationList[position].favLocationName

         }else{
             holder.binding.txtFavTimeZone.text = favLocationList[position].favLocationName
         }
        holder.binding.favItem.setOnClickListener { favPlace ->
                if(Utility.isOnline(context)){
                    favInterface.onClickFavPlace(favLocationList[position])
                }else{
                    Toast.makeText(context, context?.getString(R.string.no_internet_msg), Toast.LENGTH_SHORT).show()
                }
            }
         holder.binding.imgDelete.setOnClickListener {
             favInterface.deleteTask(favLocationList[position],position)
         }

    }

}


