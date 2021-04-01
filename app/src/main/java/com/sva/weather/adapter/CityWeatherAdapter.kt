package com.sva.weather.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sva.weather.R
import com.sva.weather.model.CityWeatherModel

class CityWeatherAdapter(val cityList: ArrayList<CityWeatherModel>,val context: Context): RecyclerView.Adapter<CityWeatherAdapter.ViewHolder>() {

    var onItemClick: ((CityWeatherModel) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityName: TextView
        val countryName: TextView
        val temp: TextView
        val img: ImageView


        init {
            cityName = view.findViewById(R.id.time)
            temp = view.findViewById(R.id.wing_speed)
            img = view.findViewById(R.id.weather_img)
            countryName = view.findViewById(R.id.temperature)

            view.setOnClickListener {
                onItemClick?.invoke(cityList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_weather_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = cityList[position].city.city
        holder.countryName.text = cityList[position].city.country
        holder.temp.text = cityList[position].temp
        val ims = context.assets.open("weather_icons/${cityList[position].icon}.png")
        val img = Drawable.createFromStream(ims,null)
        holder.img.setImageDrawable(img)
    }

    override fun getItemCount() = cityList.size
}