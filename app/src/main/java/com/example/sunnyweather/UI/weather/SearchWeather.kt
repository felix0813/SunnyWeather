package com.example.sunnyweather.UI.weather

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.baidu.mapapi.search.weather.WeatherResult
import com.baidu.mapapi.search.weather.WeatherSearch
import com.example.sunnyweather.Logic.model.WeatherViewModel
import com.example.sunnyweather.Logic.network.ParseJson
import com.example.sunnyweather.Logic.network.WeatherRequest
import com.example.sunnyweather.MyApplication
import okhttp3.*
import java.io.IOException

object SearchWeather{

    fun sendOkHttpRequest(cityID:Int,callBack:okhttp3.Callback) {
        val client=OkHttpClient()
        val request=Request.Builder().url(WeatherRequest(cityID).url).build()
        client.newCall(request).enqueue(callBack)
    }
    fun search(id:Int){
        sendOkHttpRequest(id,object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(MyApplication.context,"天气信息获取失败",Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call, response: Response) {
                val responeseData=response.body?.string()
                responeseData?.let { ParseJson.simpleParseJson(it) }?.let {
                    WeatherViewModel.addWeather(id, it)
                }
            }
        })

    }
    fun getWeather(ID: Int)= WeatherViewModel.weatherList.value!![ID]

}