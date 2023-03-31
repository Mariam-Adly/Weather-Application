package com.example.weatherapplication.alert.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.FragmentAddNewAlertBinding
import com.example.weatherapplication.databinding.FragmentAlertBinding

class AddNewAlertFragment : DialogFragment() {

    lateinit var binding: FragmentAddNewAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNewAlertBinding.inflate(inflater, container, false)
        var view: View = binding.root
        return view
    }


}