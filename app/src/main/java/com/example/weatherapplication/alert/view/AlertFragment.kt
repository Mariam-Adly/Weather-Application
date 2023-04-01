package com.example.weatherapplication.alert.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.alert.viewmodel.AlertViewModel
import com.example.weatherapplication.alert.viewmodel.AlertViewModelFactory
import com.example.weatherapplication.databinding.FragmentAlertBinding
import com.example.weatherapplication.databinding.FragmentFavoriteBinding
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.favorite.view.FavoriteAdapter
import com.example.weatherapplication.favorite.view.FavoriteFragmentDirections

class AlertFragment : Fragment() {
    lateinit var binding : FragmentAlertBinding
    lateinit var alertViewModel: AlertViewModel
    lateinit var alertFactory: AlertViewModelFactory
    lateinit var alertAdapter: AlertAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        initAlertRecycler()
        getAlert()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        var view: View = binding.root
        alertFactory = AlertViewModelFactory(WeatherRepo.getInstance(RemoteSourceImpl.getInstance(), LocalSourceImpl(requireContext())))
        alertViewModel = ViewModelProvider(this,alertFactory).get(AlertViewModel::class.java)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAlert.setOnClickListener {
            AddNewAlertFragment().show(requireActivity().supportFragmentManager,"MyAlertDialog")
        }
    }

    fun initAlertRecycler(){
        binding.recAlertsWeathers
       alertAdapter = AlertAdapter(listOf(), context!!)
        binding.recAlertsWeathers.setHasFixedSize(true)
        binding.recAlertsWeathers.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
            this.adapter = alertAdapter
        }
        binding.apply {
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val alert = alertAdapter.alertList[viewHolder.adapterPosition]
                    alertViewModel.deleteAlert(alert)
                }

            }).attachToRecyclerView(recAlertsWeathers)
        }
    }
    fun getAlert(){
        alertViewModel.getAlert().observe(
            viewLifecycleOwner){
                alert ->
            if(alert.isNotEmpty()){
                alertAdapter.alertList = alert
                alertAdapter.notifyDataSetChanged()
            }
        }
    }


}