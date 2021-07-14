package com.example.sunnyweather.UI.place

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.Logic.model.CityListViewModel
import com.example.sunnyweather.Logic.model.ResultViewModel
import com.example.sunnyweather.RecyclerCity
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class MyQueryTextListener(val activity:AppCompatActivity,val _list:ArrayList<String>,val adapter: ResultAdapter): SearchView.OnQueryTextListener {
    val cityDBManager=CityDBManager()
    val resultListViewModel=ViewModelProvider(activity).get(ResultViewModel::class.java)
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query==null){
            return false
        }
        else{
            _list.clear()
            val list=cityDBManager.cityDAO.queryName(query)
            for(name in list){
                Log.e("result",name)
                _list.add(name)
                resultListViewModel.addCity(name)
            }
            adapter.notifyDataSetChanged()
            return true
        }
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

}