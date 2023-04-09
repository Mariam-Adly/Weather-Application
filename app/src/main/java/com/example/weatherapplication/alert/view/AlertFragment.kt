package com.example.weatherapplication.alert.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.weatherapplication.MainActivity
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
import com.example.weatherapplication.utility.ApiStateAlert
import com.example.weatherapplication.utility.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlertFragment : Fragment() {
    lateinit var binding : FragmentAlertBinding
    lateinit var alertViewModel: AlertViewModel
    lateinit var alertFactory: AlertViewModelFactory
    lateinit var alertAdapter: AlertAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       checkOverlayPermission()
    }

    override fun onResume() {
        super.onResume()
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
        alertFactory = AlertViewModelFactory(WeatherRepo.getInstance(RemoteSourceImpl.getInstance(requireContext()), LocalSourceImpl(requireContext()),requireContext()))
        alertViewModel = ViewModelProvider(this,alertFactory).get(AlertViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!Utility.isOnline(requireContext())){
            Toast.makeText(requireContext(),R.string.you_are_offline,Toast.LENGTH_SHORT).show()
            binding.addAlert.visibility = View.GONE
        }
        binding.addAlert.setOnClickListener {
            checkOverlayPermission()
            AddNewAlertFragment().show(requireActivity().supportFragmentManager,"MyAlertDialog")
        }
        initAlertRecycler()
        getAlert()
    }

    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle(getString(R.string.weather_alert))
                .setMessage(getString(R.string.features))
                .setPositiveButton(getText(R.string.ok)) { dialog: DialogInterface, i: Int ->
                    var myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    startActivity(myIntent)
                    dialog.dismiss()
                }.setNegativeButton(
                    getString(R.string.cancel)
                ) { dialog: DialogInterface, i: Int ->
                    dialog.dismiss()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }.show()
        }
    }

    fun initAlertRecycler(){
        lifecycleScope.launch {
            alertViewModel.alertWeather.collectLatest {
                when(it){
                    is ApiStateAlert.Failure -> { Toast.makeText(requireContext(), "Check ${it.msg}", Toast.LENGTH_SHORT)
                        .show()}
                    ApiStateAlert.Loading -> {}
                    is ApiStateAlert.Success -> {
                        binding.recAlertsWeathers
                       alertAdapter= AlertAdapter(it.data, context!!){
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setMessage(R.string.AWTD)
                                .setCancelable(false)
                                .setPositiveButton(R.string.yes) { dialog, id ->
                                    alertViewModel.deleteAlert(it)


                                    WorkManager.getInstance(requireContext()).cancelAllWorkByTag(it.startDay.toString()+it.endDay.toString())
                                    Toast.makeText(requireContext(), getString(R.string.deleted), Toast.LENGTH_SHORT).show()
                                }
                                .setNegativeButton(R.string.no) { dialog, id ->
                                    dialog.dismiss()
                                }
                            val alert = builder.create()
                            alert.show()
                        }
                        binding.recAlertsWeathers.setHasFixedSize(true)
                        binding.recAlertsWeathers.apply {
                            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                            this.adapter = alertAdapter
                        }
                    }
                }
            }
        }

    }

    fun getAlert(){
        lifecycleScope.launch {
            alertViewModel.getAlert().collectLatest {
                it -> when(it){
                is ApiStateAlert.Failure -> {Toast.makeText(requireContext(), "Check ${it.msg}", Toast.LENGTH_SHORT)
                    .show()}
                is ApiStateAlert.Loading -> {}
                is ApiStateAlert.Success -> {
                   // alertViewModel.getAlert()
                    alertAdapter.alertList = it.data
                    alertAdapter.notifyDataSetChanged()
                }
            }
            }
        }
    }

}