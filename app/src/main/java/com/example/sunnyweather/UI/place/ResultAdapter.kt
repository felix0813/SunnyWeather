package com.example.sunnyweather.UI.place

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.City
import com.example.sunnyweather.CityManageActivity
import com.example.sunnyweather.R

class ResultAdapter(val resultList: ArrayList<City>, val activity: AppCompatActivity) :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resultButton = view.findViewById<Button>(R.id.result_button)
        val idtext = view.findViewById<TextView>(R.id.idtext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = resultList[position]
        holder.idtext.text = result.id.toString()
        val tmp = result.name.toString()
        holder.resultButton.text = tmp
        holder.resultButton.setOnClickListener {

            //Toast.makeText(MyApplication.context,"click id is ${holder.idtext.text.toString()}",Toast.LENGTH_SHORT).show()
            Log.e("id", holder.idtext.text.toString())
            val intent = Intent()
            intent.putExtra("selectedCity", result)
            activity.setResult(Activity.RESULT_OK,intent)
            activity.finish()
        }
    }

    override fun getItemCount() = resultList.size

}