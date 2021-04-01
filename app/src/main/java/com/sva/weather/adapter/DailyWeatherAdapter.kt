package com.sva.weather.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sva.weather.R
import com.sva.weather.databinding.DailyWeatherItemBinding
import com.sva.weather.model.DailyWeatherConvertedModel

class DailyWeatherAdapter(var data: List<DailyWeatherConvertedModel>): RecyclerView.Adapter<DailyWeatherHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<DailyWeatherItemBinding>(inflater,R.layout.daily_weather_item, parent, false)
        return DailyWeatherHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyWeatherHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}