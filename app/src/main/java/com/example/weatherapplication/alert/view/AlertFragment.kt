package com.example.weatherapplication.alert.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.FragmentAlertBinding
import com.example.weatherapplication.databinding.FragmentFavoriteBinding
import com.example.weatherapplication.favorite.view.FavoriteFragmentDirections

class AlertFragment : Fragment() {
    lateinit var binding : FragmentAlertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        var view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAlert.setOnClickListener {
            AddNewAlertFragment().show(requireActivity().supportFragmentManager,"MyAlertDialog")
        }
    }


}