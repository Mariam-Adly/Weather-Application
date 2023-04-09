package com.example.weatherapplication.favorite.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.FragmentFavoriteBinding
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapplication.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapplication.map.MapsActivity
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.utility.ApiStateList
import com.example.weatherapplication.utility.Utility
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment(),OnClickFavPlaceListener {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        var view: View = binding.root
        favoriteViewModelFactory =
            FavoriteViewModelFactory(
                WeatherRepo.getInstance(
                    RemoteSourceImpl.getInstance(requireContext()),
                    LocalSourceImpl(requireContext())
                ,requireContext()))
        favoriteViewModel =
            ViewModelProvider(requireActivity(), favoriteViewModelFactory).get(FavoriteViewModel::class.java)
        return view
    }

    override fun onResume() {
        super.onResume()
        initFavRecycler()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!Utility.isOnline(requireContext())){
            Toast.makeText(requireContext(),R.string.you_are_offline,Toast.LENGTH_SHORT).show()
            binding.addFavBtn.visibility = GONE
        }
        initFavRecycler()
        favoriteViewModel.getFavPlaces()
        binding.addFavBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MapsActivity::class.java))
        }

    }


    fun initFavRecycler(){
        lifecycleScope.launch {
            favoriteViewModel.favWeather.collectLatest {
                when(it){
                    is ApiStateList.Failure -> {Toast.makeText(requireContext(), "Check ${it.msg}", Toast.LENGTH_SHORT)
                        .show()}
                    ApiStateList.Loading -> {}
                    is ApiStateList.Success -> {
                        binding.favRecycler
                        favoriteAdapter = FavoriteAdapter(it.data, context!!, this@FavoriteFragment)
                        binding.favRecycler.setHasFixedSize(true)
                        binding.favRecycler.apply {
                            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                            this.adapter = favoriteAdapter
                        }
                }
            }
        }

        }
    }

    override fun onClickFavPlace(favPlace: FavoriteWeather) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToFavWeatherDetailsFragment(favPlace)
         Navigation.findNavController(requireView()).navigate(action)
    }

    override fun deleteTask(myFav: FavoriteWeather, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.AWTD)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { dialog, id ->
                favoriteViewModel.deleteFavoritePlace(myFav)
            }
            .setNegativeButton(R.string.no) { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

}