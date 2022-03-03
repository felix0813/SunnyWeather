package com.example.sunnyweather.ui.place

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.SelectedCity
import com.example.sunnyweather.logic.model.CityListViewModel

class CityListManager(activity: AppCompatActivity) {
    //val cityListViewModel=CityListViewModel
    val cityListViewModel = ViewModelProvider(activity).get(CityListViewModel::class.java)
    private var last = -1
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