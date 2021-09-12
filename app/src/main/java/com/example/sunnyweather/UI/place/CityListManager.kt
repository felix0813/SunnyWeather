package com.example.sunnyweather.UI.place

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.Logic.model.CityListViewModel
import com.example.sunnyweather.SelectedCity

class CityListManager(activity: AppCompatActivity) {
    //val cityListViewModel=CityListViewModel
    val cityListViewModel = ViewModelProvider(activity).get(CityListViewModel::class.java)
    var last = -1
    fun getCityList(): ArrayList<SelectedCity>? {
        last = cityListViewModel.size.value!!
        return cityListViewModel.cityList.value
    }

    fun addCity(city: SelectedCity) {
        cityListViewModel.addCity(city)
    }

    fun removeCity(city: String) {
        cityListViewModel.removeCity(city)
    }
}