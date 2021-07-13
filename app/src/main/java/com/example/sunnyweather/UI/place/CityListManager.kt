package com.example.sunnyweather.UI.place

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.Logic.model.CityListViewModel
import com.example.sunnyweather.RecyclerCity

class CityListManager(val activity:AppCompatActivity) {
    val cityListViewModel=ViewModelProvider(activity).get(CityListViewModel::class.java)
    var last=-1
    fun getCityList():ArrayList<RecyclerCity>?{
        last= cityListViewModel.size.value!!
        return cityListViewModel.cityList.value
    }
    fun observeList(){
        cityListViewModel.size.observe(activity, Observer {
         Toast.makeText(activity,"城市有变化，请刷新",Toast.LENGTH_SHORT).show()
        })
    }
    fun addCity(city:RecyclerCity){
        cityListViewModel.addCity(city)
    }
    fun removeCity(city:String){
        cityListViewModel.removeCity(city)
    }
}