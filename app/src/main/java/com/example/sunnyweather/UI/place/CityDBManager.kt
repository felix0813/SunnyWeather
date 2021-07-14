package com.example.sunnyweather.UI.place

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.sunnyweather.Address
import com.example.sunnyweather.City
import com.example.sunnyweather.Logic.DAO.AddressDB
import com.example.sunnyweather.Logic.DAO.CityDB
import com.example.sunnyweather.MyApplication
import com.example.sunnyweather.R
import java.lang.Exception
import kotlin.concurrent.thread

@SuppressLint("StaticFieldLeak")
class CityDBManager() {
    val context = MyApplication.context
    val cityDAO = CityDB.getDB(context).cityDao()
    val addressDAO=AddressDB.getAddressDB(context).addressDAO()
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
        val inputStream2 = context.resources.openRawResource(R.raw.district_code)
        val reader2 = inputStream2.bufferedReader()
        var count2 = 0
        reader2.forEachLine {
            val arr = it.split(",")
            val id = arr[0].toInt()
            Log.e("num",id.toString())
            val name = arr[3]
            val x=arr[1].toFloat()
            val y=arr[2].toFloat()
            val address=Address(id,x,y)
            val city = City(name, id)
            try {
                addressDAO.insertAddress(address)
                cityDAO.insertCity(city)
                count2++
            }
            catch (e:Exception){
                e.printStackTrace()
                Log.e("repeatedID","$id $name")
            }
        }
        reader2.close()
        context.getSharedPreferences("city", Context.MODE_PRIVATE).edit {
            putBoolean("hasInit", true)
        }
    }
}