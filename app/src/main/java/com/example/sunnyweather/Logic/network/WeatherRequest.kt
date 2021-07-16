package com.example.sunnyweather.Logic.network

import android.content.Context
import com.example.sunnyweather.MyApplication

class WeatherRequest(cityID:Int) {
    val token=MyApplication.context.getSharedPreferences("ak", Context.MODE_PRIVATE).getString("ak","")
    val url="https://api.caiyunapp.com/v2.5/"+token+"/weather.json?adcode="+cityID

}