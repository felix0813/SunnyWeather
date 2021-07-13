package com.example.sunnyweather.UI.place

import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.RecyclerCity

class CityAdapter(val cityList:List<RecyclerCity>, val activity:AppCompatActivity) :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val cityName=view.findViewById<TextView>(R.id.cityName)
        val temperature=view.findViewById<TextView>(R.id.temperature)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.city_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city=cityList[position]
        holder.cityName.text=city.name
        holder.temperature.text=city.temperature
        holder.itemView.isLongClickable
        holder.itemView.setOnLongClickListener {
            activity.getSharedPreferences("toDelete",Context.MODE_PRIVATE).edit{
                putString("toDelete",holder.cityName.text.toString())
            }

            return@setOnLongClickListener false
        }
    }


    override fun getItemCount()=cityList.size
}