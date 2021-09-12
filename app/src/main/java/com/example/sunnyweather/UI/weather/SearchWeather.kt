package com.example.sunnyweather.UI.weather

import android.util.Log
import android.widget.Toast
import com.example.sunnyweather.Logic.model.DetailedWeatherModel
import com.example.sunnyweather.Logic.model.WeatherViewModel
import com.example.sunnyweather.Logic.network.ParseJson
import com.example.sunnyweather.Logic.network.WeatherRequest
import com.example.sunnyweather.MyApplication
import okhttp3.*
import java.io.IOException

object SearchWeather {

    fun sendOkHttpRequest(cityID: Int, callBack: Callback) {
        val client = OkHttpClient()
        val request = Request.Builder().url(WeatherRequest(cityID).url).build()
        client.newCall(request).enqueue(callBack)
    }

    fun searchWeather(id: Int) {
        sendOkHttpRequest(id, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(MyApplication.context, "天气信息获取失败", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responeseData = response.body?.string()
                responeseData?.let { ParseJson.simpleParseJson(it) }?.let {
                    WeatherViewModel.addWeather(id, it)
                }
            }
        })

    }

    fun getWeather(ID: Int) = WeatherViewModel.weatherList.value!![ID]

    fun searchDetailedWeather(id: Int) {
        sendOkHttpRequest(id, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(MyApplication.context, "天气信息获取失败", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responeseData = response.body?.string()
                if (responeseData != null) {
                    Log.e("data", responeseData)
                } else {
                    Log.e("data", "error")
                }
                responeseData?.let { ParseJson.detailedParseJson(it) }?.let {
                    DetailedWeatherModel.addWeather(id, it)
                }
            }
        })
    }

    fun getDetailedWeather(ID: Int) = DetailedWeatherModel.detailedWeatherList[ID]
}