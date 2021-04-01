package com.sva.weather.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sva.weather.R
import com.sva.weather.adapter.CityWeatherAdapter
import com.sva.weather.adapter.ModifiedArrayAdapter
import com.sva.weather.databinding.FragmentWeatherBinding
import com.sva.weather.extensions.hideKeyboard
import com.sva.weather.model.CityPositionModel
import com.sva.weather.model.CityWeatherModel
import com.sva.weather.others.WeatherState
import com.sva.weather.viewmodels.WeatherViewModel
import java.lang.Exception

class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()
    private var _binding: FragmentWeatherBinding? = null
    private lateinit var cityWeatherAdapter: CityWeatherAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return root
    }

    override fun onResume() {
        binding.searchTw.setText("")
        viewModel.getAllCityData()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is WeatherState.DataGetSuccesState -> {
                    Toast.makeText(
                        requireContext(),
                        "There is no data to show",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.isVisible = false
                    updateRv(state.data)
                }
                is WeatherState.StartLoadingState -> {
                    binding.progressBar.isVisible = true
                }
            }
        }

        viewModel.cityData.observe(viewLifecycleOwner) { data ->
            val cityList = arrayListOf<String>()
            for (item in data) {
                cityList.add(item.city!! + " (" + item.country + ")")
            }
            binding.searchTw.setAdapter(
                ModifiedArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    cityList
                )
            )
        }

        binding.searchTw.setOnItemClickListener { _, _, _, id ->
            viewModel.getWeatherDataOfClickedItem(id.toInt())
            this.hideKeyboard()
        }

        binding.searchIcon.setOnClickListener {
            viewModel.getSearchedCityWeather(binding.searchTw.text.toString())
            this.hideKeyboard()
        }

        binding.searchTw.setOnKeyListener { v, keyCode, event ->
            var state = false
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    this.hideKeyboard()
                    binding.searchTw.dismissDropDown()
                    viewModel.getSearchedCityWeather(binding.searchTw.text.toString())
                    state = true
                }
            }
            state
        }

        binding.fab.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        viewModel.getNearestCitiesWeather(it)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Can't get last location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
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
                R.id.action_navigation_weather_to_navigation_daily_weather,
                bundle
            )
        }
    }
}