package com.example.sunnyweather.Logic.network

import android.util.Log
import com.example.sunnyweather.Weather
import com.google.gson.JsonObject
import org.json.JSONObject

object ParseJson {
    fun simpleParseJson(responseData:String): Weather {
        try {
            Log.d("data", responseData)
            val JsonObject = JSONObject(responseData)
            val result = JsonObject.getJSONObject("result")
            val realtime = result.getJSONObject("realtime")
            val temperature = realtime.getDouble("temperature")
            val skycon = realtime.getString("skycon")
            val weather = Weather(temperature.toString(), skycon)
            return weather
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return Weather("--","CLEAR_DAY")
    }
}