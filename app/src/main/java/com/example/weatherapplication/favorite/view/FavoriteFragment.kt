package com.example.weatherapplication.favorite.view

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.databinding.FragmentFavoriteBinding
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapplication.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapplication.map.MapsActivity
import com.example.weatherapplication.model.FavoriteWeather
import kotlinx.coroutines.Dispatchers
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
                    RemoteSourceImpl.getInstance(),
                    LocalSourceImpl(requireContext())
                ))
        favoriteViewModel =
            ViewModelProvider(requireActivity(), favoriteViewModelFactory).get(FavoriteViewModel::class.java)
        return view
    }

    override fun onResume() {
        super.onResume()
        initFavRecycler()
        getFavPlaces()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addFavBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MapsActivity::class.java))
        }

    }


    fun initFavRecycler(){
        binding.favRecycler
        favoriteAdapter = FavoriteAdapter(listOf(), context!!,this)
        binding.favRecycler.setHasFixedSize(true)
        binding.favRecycler.apply {
            this.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
            this.adapter = favoriteAdapter
        }
        binding.apply {
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val favPlace = favoriteAdapter.favLocationList[viewHolder.adapterPosition]
                    favoriteViewModel.deleteFavoritePlace(favPlace)
                }

            }).attachToRecyclerView(favRecycler)
        }
    }
     fun getFavPlaces(){
        favoriteViewModel.getFavPlaces().observe(
            viewLifecycleOwner){
                favPlaces ->
                if(favPlaces.isNotEmpty()){
                  favoriteAdapter.favLocationList = favPlaces
                  favoriteAdapter.notifyDataSetChanged()
                }
            }
    }

    override fun onClickFavPlace(favPlace: FavoriteWeather) {
//        val action = FavoriteFragmentDirections.actionNavigationFavToNavigationHome()
//        Navigation.findNavController(requireView()).navigate(action)

    }

}