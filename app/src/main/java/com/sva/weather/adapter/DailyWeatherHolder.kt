package com.sva.weather.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sva.weather.databinding.DailyWeatherItemBinding
import com.sva.weather.model.DailyWeatherConvertedModel
import com.sva.weather.model.DailyWeatherModel

class DailyWeatherHolder(val binding:DailyWeatherItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DailyWeatherConvertedModel){
        binding.data = item
        binding.executePendingBindings()
    }
}