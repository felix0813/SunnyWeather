package com.example.sunnyweather.logic.model

import androidx.lifecycle.ViewModel
import com.example.sunnyweather.DetailedWeather

object DetailedWeatherModel : ViewModel() {
    var size = 0
    val detailedWeatherList = HashMap<Int, DetailedWeather>()
    fun addWeather(id: Int, weather: DetailedWeather) {
        if (!detailedWeatherList.containsKey(id)) {
            size++
        }
        detailedWeatherList[id] = weather
    }
}