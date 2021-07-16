package com.example.sunnyweather.Logic.network

import android.util.Log
import com.example.sunnyweather.DetailedWeather
import com.example.sunnyweather.Weather
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
    fun detailedParseJson(responseData: String):DetailedWeather{
            /*var data=responseData
            if(data.startsWith("\ufeff")){
                data=responseData.substring(1)
            }*/
            val JsonObject = JSONObject(responseData)
        try {


            val result = JsonObject.getJSONObject("result")
            val realtime = result.getJSONObject("realtime")
            val temperature = realtime.getDouble("temperature")
            val apparentTemperature=realtime.getDouble("apparent_temperature")
            val skycon = realtime.getString("skycon")
            val wind=realtime.getJSONObject("wind")
            val windSpeed=wind.getDouble("speed")
            val degree=wind.getDouble("direction")
            val windDirection=when{
                degree<=22.5&&degree>=337.5->"北风"
                degree>22.5&&degree<=67.5->"东北风"
                degree>67.5&&degree<=112.5->"东风"
                degree>112.5&&degree<=157.5->"东南风"
                degree>157.5&&degree<=202.5->"南风"
                degree>202.5&&degree<=247.5->"西南风"
                degree>247.5&&degree<292.5->"西风"
                degree>292.5&&degree<337.5->"西北风"
                else->"错误"
            }
            val humidity=realtime.getDouble("humidity")
            val lifeIndex=realtime.getJSONObject("life_index")
            val ultraviolet=lifeIndex.getJSONObject("ultraviolet").getString("desc")
            val visibility=realtime.getDouble("visibility")
            val pressure=realtime.getDouble("pressure")
            val airQuality=realtime.getJSONObject("air_quality").getJSONObject("description").getString("chn")
            val comfort=lifeIndex.getJSONObject("comfort").getString("desc")
            val arr1= parseHourlyToArr(result.getJSONObject("hourly"))
            val arr2= parseDailyToArr(result.getJSONObject("daily"))
            val detailedWeather=DetailedWeather(
                temperature =temperature,
                apparentTemperature = apparentTemperature, skycon = skycon,
            windSpeed = windSpeed, windDirection = windDirection, humidity = humidity,
                ultraviolet = ultraviolet, visibility = visibility, pressure = pressure,
                airQuality = airQuality, comfort = comfort, arrDaily = arr2, arrHourly = arr1
            )
            Log.e("data","正常解析")
            return detailedWeather
        }
        catch (e:Exception){
            e.printStackTrace()
            val arr1= arrayOfNulls<Weather>(0)
            val arr2= arrayOfNulls<Weather>(0)
            Log.e("data","错误解析")
            return DetailedWeather(0.0,0.0,"CLEAR_DAY",0.0,0.0,"弱",0.0,0.0,"东风","优","舒适",arr1,arr2)
        }

        /*
        */
    }
    fun parseHourlyToArr(hourly:JSONObject): Array<Weather?> {
        var arr=hourly.getJSONArray("temperature")
        val arr1= arrayOfNulls<Double>(24)
        for ( num in 0..23){
            arr1[num]=arr.getJSONObject(num).getDouble("value")
        }
        arr=hourly.getJSONArray("skycon")
        val arr2= arrayOfNulls<String>(24)
        for(num in 0..23){
            arr2[num]=arr.getJSONObject(num).getString("value")
        }
        val result= arrayOfNulls<Weather>(24)
        for(num in 0..23){
            result[num]= Weather(arr1[num].toString(),arr2[num]!!)
        }
        return result
    }
    fun parseDailyToArr(daily:JSONObject): Array<Weather?> {
        var arr=daily.getJSONArray("temperature")
        val arr1= arrayOfNulls<Double>(5)
        for ( num in 0..4){
            arr1[num]=arr.getJSONObject(num).getDouble("avg")
        }
        arr=daily.getJSONArray("skycon")
        val arr2= arrayOfNulls<String>(5)
        for(num in 0..4){
            arr2[num]=arr.getJSONObject(num).getString("value")
        }
        val result= arrayOfNulls<Weather>(5)
        for(num in 0..4){
            result[num]= Weather(arr1[num].toString(),arr2[num]!!)
        }
        return result
    }
}