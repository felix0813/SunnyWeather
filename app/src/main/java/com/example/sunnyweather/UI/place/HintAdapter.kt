package com.example.sunnyweather.UI.place

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.City
import com.example.sunnyweather.R

class HintAdapter(private val hintList: ArrayList<City>, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<HintAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.hintText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hint_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = hintList[position]
        holder.text.text = city.name

        holder.itemView.setOnClickListener {

            //Toast.makeText(MyApplication.context,"click id is ${holder.idtext.text.toString()}",Toast.LENGTH_SHORT).show()
            Log.e("id", holder.text.text.toString())
            val intent = Intent()
            intent.putExtra("selectedCity", city)
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }
    }

    override fun getItemCount() = hintList.size

}