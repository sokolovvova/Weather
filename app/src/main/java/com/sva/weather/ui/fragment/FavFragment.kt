package com.sva.weather.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sva.weather.R
import com.sva.weather.adapter.CityWeatherAdapter
import com.sva.weather.adapter.ModifiedArrayAdapter
import com.sva.weather.databinding.FragmentFavBinding
import com.sva.weather.databinding.FragmentWeatherBinding
import com.sva.weather.extensions.hideKeyboard
import com.sva.weather.model.CityPositionModel
import com.sva.weather.model.CityWeatherModel
import com.sva.weather.others.WeatherState
import com.sva.weather.viewmodels.FavViewModel
import com.sva.weather.viewmodels.WeatherViewModel

class FavFragment : Fragment() {

    private val viewModel: FavViewModel by viewModels()
    private var _binding: FragmentFavBinding? = null
    private lateinit var cityWeatherAdapter: CityWeatherAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.updateSavedWeatherData()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.savedWeatherData.observe(viewLifecycleOwner){
            updateRv(ArrayList(it))
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateRv(cityList: ArrayList<CityWeatherModel>) {
        cityWeatherAdapter = CityWeatherAdapter(cityList, requireContext())
        binding.weatherRv.layoutManager = LinearLayoutManager(requireContext())
        binding.weatherRv.adapter = cityWeatherAdapter

        cityWeatherAdapter.onItemClick = { item ->
            val bundle = bundleOf("weatherData" to item)
            findNavController().navigate(
                R.id.action_navigation_fav_to_navigation_daily_weather,
                bundle
            )
        }
    }
}