package com.example.weatherapplication.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container,MapsFragment(),"Blank Test Fragment")
        transaction.commit()
    }


}
