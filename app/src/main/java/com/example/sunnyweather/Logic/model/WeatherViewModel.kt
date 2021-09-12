package com.example.sunnyweather.Logic.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.Weather

object WeatherViewModel : ViewModel() {
    val size = MutableLiveData<Int>()
    val weatherList = MutableLiveData<HashMap<Int, Weather>>()

    init {
        size.value = 0
        weatherList.value = HashMap()
    }

    fun addWeather(cityID: Int, weather: Weather) {
        weatherList.value!!.put(cityID, weather)
    }
}