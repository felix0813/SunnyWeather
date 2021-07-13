package com.example.sunnyweather.UI.place

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.sunnyweather.City
import com.example.sunnyweather.Logic.DAO.CityDB
import com.example.sunnyweather.MyApplication
import com.example.sunnyweather.R
import kotlin.concurrent.thread

@SuppressLint("StaticFieldLeak")
class CityDBManager() {
    val context = MyApplication.context
    val cityDAO = CityDB.getDB(context).cityDao()
    var hasInit = false

    init {
        checkInit()
    }

    private fun checkInit() {
        val reader = context.getSharedPreferences("city", AppCompatActivity.MODE_PRIVATE)
        hasInit = reader.getBoolean("hasInit", false)
        if (hasInit == false) {
            thread { initCity() }
        } else {
            thread { Log.d("citynum", cityDAO.queryCount().toString()) }
        }
    }

    fun initCity() {
        val inputStream = context.resources.openRawResource(R.raw.city_code)
        val reader = inputStream.bufferedReader()
        var count = 0
        reader.forEachLine {
            val arr = it.split(",")
            val id = arr[0].toInt()
            val name = arr[1]
            val city = City(name, id)
            cityDAO.insertCity(city)
            count++
        }
        Log.d("count", count.toString())
        reader.close()
        context.getSharedPreferences("city", Context.MODE_PRIVATE).edit {
            putBoolean("hasInit", true)
        }
    }
}