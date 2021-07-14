package com.example.sunnyweather.UI.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.MyApplication
import com.example.sunnyweather.R

class ResultAdapter(val resultList:ArrayList<String>,val activity:AppCompatActivity):RecyclerView.Adapter<ResultAdapter.ViewHolder> (){
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val resultButton=view.findViewById<Button>(R.id.result_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.result_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result=resultList[position]
        holder.resultButton.text=result
        holder.itemView.setOnClickListener {
            Toast.makeText(MyApplication.context,"click",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount()=resultList.size

}