package com.example.sunnyweather.Logic.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.SelectedCity

class CityListViewModel : ViewModel() {
    val cityList = MutableLiveData<ArrayList<SelectedCity>>()
    val size = MutableLiveData<Int>()

    init {
        size.value = 0
        cityList.value = ArrayList()
    }

    fun addCity(city: SelectedCity): Boolean {
        cityList.value?.add(city)
        size.value = cityList.value?.size
        return true
    }

    fun removeCity(_city: String): Boolean {
        for (city in cityList.value!!) {
            if (city.name == _city) {
                cityList.value!!.remove(city)
                size.value = cityList.value?.size
                return true
            }
        }

        return false
    }
}