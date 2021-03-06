package com.example.sunnyweather.ui.weather

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.Weather
import java.util.*

class WeatherAdapter(private val weatherList: Array<Weather?>) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val skyconImg: ImageView = view.findViewById(R.id.skyconImg)
        val temperature: TextView = view.findViewById(R.id.subTemperature)
        val time: TextView = view.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = weatherList[position]
        if (position == 0) {
            holder.time.text = "现在"
        } else {
            val tmp = ((Calendar.getInstance()
                .get(Calendar.HOUR_OF_DAY) + position) % 24).toString() + ":00"
            holder.time.text = tmp
        }
        val tmp = weather!!.temperature.toDouble().toInt().toString() + "℃"
        holder.temperature.text = tmp
        when (weather.skycon) {
            "CLEAR_DAY" -> holder.skyconImg.setImageResource(R.drawable.ic_clear_day)
            "CLEAR_NIGHT" -> holder.skyconImg.setImageResource(R.drawable.ic_clear_night)
            "CLOUDY" -> holder.skyconImg.setImageResource(R.drawable.ic_cloudy)
            "PARTLY_CLOUDY_DAY" -> holder.skyconImg.setImageResource(R.drawable.ic_partly_cloud_day)
            "PARTLY_CLOUDY_NIGHT" -> holder.skyconImg.setImageResource(R.drawable.ic_partly_cloud_night)
            "LIGHT_HAZE" -> holder.skyconImg.setImageResource(R.drawable.ic_light_haze)
            "MODERATE_HAZE" -> holder.skyconImg.setImageResource(R.drawable.ic_moderate_haze)
            "HEAVY_HAZE" -> holder.skyconImg.setImageResource(R.drawable.ic_heavy_haze)
            "LIGHT_RAIN" -> holder.skyconImg.setImageResource(R.drawable.ic_light_rain)
            "MODERATE_RAIN" -> holder.skyconImg.setImageResource(R.drawable.ic_moderate_rain)
            "HEAVY_RAIN" -> holder.skyconImg.setImageResource(R.drawable.ic_heavy_rain)
            "STORM_RAIN" -> holder.skyconImg.setImageResource(R.drawable.ic_storm_rain)
            "FOG" -> holder.skyconImg.setImageResource(R.drawable.ic_fog)
            "LIGHT_SNOW" -> holder.skyconImg.setImageResource(R.drawable.ic_light_snow)
            "MODERATE_SNOW" -> holder.skyconImg.setImageResource(R.drawable.ic_moderate_snow)
            "HEAVY_SNOW" -> holder.skyconImg.setImageResource(R.drawable.ic_heavy_snow)
            "STORM_SNOW" -> holder.skyconImg.setImageResource(R.drawable.ic_heavy_snow)
            "DUST" -> holder.skyconImg.setImageResource(R.drawable.ic_dust)
            "SAND" -> holder.skyconImg.setImageResource(R.drawable.ic_sand)
            "WIND" -> holder.skyconImg.setImageResource(R.drawable.ic_wind)
            else -> Log.d("skycon", weather.skycon)
        }
    }

    override fun getItemCount() = weatherList.size

}