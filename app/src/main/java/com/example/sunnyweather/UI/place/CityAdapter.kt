package com.example.sunnyweather.UI.place

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.SelectedCity
import java.util.*

class CityAdapter(val cityList: List<SelectedCity>, val activity: AppCompatActivity) :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityName = view.findViewById<TextView>(R.id.cityName)
        val temperature = view.findViewById<TextView>(R.id.temperature)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cityList[position]
        when (city.skycon) {
            "CLEAR_DAY" -> holder.itemView.setBackgroundResource(R.drawable.sunny1)
            "CLEAR_NIGHT" -> {
                holder.itemView.setBackgroundResource(R.drawable.sunny2);holder.itemView.background.alpha =
                    200
            }
            "CLOUDY" -> holder.itemView.setBackgroundResource(R.drawable.cloudy3)
            "PARTLY_CLOUDY_DAY" -> {
                holder.itemView.setBackgroundResource(R.drawable.cloudy1)
                holder.itemView.background.alpha = 200
            }
            "PARTLY_CLOUDY_NIGHT" -> {
                holder.itemView.setBackgroundResource(R.drawable.cloudy2)
                holder.itemView.background.alpha = 200
            }
            "LIGHT_HAZE" -> holder.itemView.setBackgroundResource(R.drawable.lighthaze)
            "MODERATE_HAZE" -> holder.itemView.setBackgroundResource(R.drawable.midhaze)
            "HEAVY_HAZE" -> holder.itemView.setBackgroundResource(R.drawable.heavyhaze)
            "LIGHT_RAIN" -> {
                holder.itemView.apply {
                    setBackgroundResource(R.drawable.lightrain)
                    background.alpha = 200
                }

            }
            "MODERATE_RAIN" -> holder.itemView.setBackgroundResource(R.drawable.midrain)
            "HEAVY_RAIN" -> holder.itemView.setBackgroundResource(R.drawable.heavyrain)
            "STORM_RAIN" -> holder.itemView.setBackgroundResource(R.drawable.stormrain)
            "FOG" -> holder.itemView.setBackgroundResource(R.drawable.foggy)
            "LIGHT_SNOW" -> holder.itemView.setBackgroundResource(R.drawable.lightsnow)
            "MODERATE_SNOW" -> holder.itemView.setBackgroundResource(R.drawable.midsnow)
            "HEAVY_SNOW" -> holder.itemView.setBackgroundResource(R.drawable.heavysnow)
            "STORM_SNOW" -> holder.itemView.setBackgroundResource(R.drawable.stormsnow)
            "DUST" -> holder.itemView.setBackgroundResource(R.drawable.dust)
            "SAND" -> holder.itemView.setBackgroundResource(R.drawable.sand)
            "WIND" -> holder.itemView.setBackgroundResource(R.drawable.wind)
            else -> Log.d("skycon", city.skycon)
        }
        val cityName = StringBuilder()
        var past = false
        if (city.name.contains("省")&&city.name.get(city.name.length-1)!='省') {
            for (char in city.name) {
                if (past) {
                    cityName.append(char)
                } else {
                    if (char == '省') {
                        past = true
                    }
                }
            }
        } else {
            cityName.append(city.name)
        }
        holder.cityName.text = cityName
        holder.temperature.text = city.temperature
        try {
            holder.temperature.text = city.temperature.toDouble().toInt().toString()
        } catch (e: Exception) {

        }
        holder.itemView.isLongClickable
        holder.itemView.setOnLongClickListener {
            activity.getSharedPreferences("toDelete", Context.MODE_PRIVATE).edit {
                putString("toDelete", city.name)
            }

            return@setOnLongClickListener false
        }
    }


    override fun getItemCount() = cityList.size
}