package com.sva.weather.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sva.weather.adapter.DailyWeatherAdapter
import com.sva.weather.databinding.FragmentDailyWeatherBinding
import com.sva.weather.extensions.toDailyWeatherConvertedModel
import com.sva.weather.model.CityWeatherModel
import com.sva.weather.model.DailyWeatherConvertedModel
import com.sva.weather.viewmodels.DailyWeatherViewModel

class DailyWeatherFragment : Fragment() {

    private val viewModel: DailyWeatherViewModel by viewModels()
    private var _binding: FragmentDailyWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.updateWeatherData(requireArguments().get("weatherData") as CityWeatherModel)
        viewModel.updateSavedWeatherData()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyWeatherBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.savedWeatherData.observe(viewLifecycleOwner){
            viewModel.checkSaved()
        }
        viewModel.weatherData.observe(viewLifecycleOwner){
            val newData = arrayListOf<DailyWeatherConvertedModel>()
            for(item in it.daily){
                newData.add(item.toDailyWeatherConvertedModel(requireContext()))
            }
            updateRV(newData)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateRV(data: List<DailyWeatherConvertedModel>){
        val adapter = DailyWeatherAdapter(data)
        binding.adapter = adapter
    }
}