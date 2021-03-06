package com.example.sunnyweather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.example.sunnyweather.ui.weather.SearchWeather
import com.example.sunnyweather.ui.weather.WeatherAdapter
import com.example.sunnyweather.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.thread
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cityArray: Array<City?>
    private lateinit var reader: SharedPreferences
    private lateinit var map: MutableMap<String, *>
    private var order = 0
    private lateinit var detailedWeather: DetailedWeather
    private var nowCityName = ""
    private var nowCity = -1
    private var init = false
    private var isFirst by Delegates.notNull<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirst = getSharedPreferences("ak", Context.MODE_PRIVATE).all.isEmpty()
        val reader = getSharedPreferences("ak", Context.MODE_PRIVATE)
        reader.edit().apply {
            putString("ak", "Pt4ChzU3W0ca8SpE")
            apply()
        }
        getReady()
        thread { testBaiduLocation() }
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (map.isNotEmpty()) {
            initialAll()
        } else {
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_LONG).show()
        }

        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        supportActionBar?.title = ""
        binding.previousCity.bringToFront()
        binding.nextCity.bringToFront()

        binding.setCity.setOnClickListener {
            startActivity(Intent(this, CityManageActivity::class.java))
        }
        binding.previousCity.setOnClickListener {
            goToLastCity()
        }
        binding.nextCity.setOnClickListener {
            goToiNextCity()
        }
        binding.refreshButton.bringToFront()
        binding.refreshButton.setOnClickListener {
            refreshCity()

        }
    }

    private fun goToLastCity() {
        Log.d("test", order.toString())
        Log.d("test", "click")
        if (cityArray.size > 1) {
            order = if (order == 0) {
                cityArray.size - 1
            } else {
                order - 1
            }
            nowCity = cityArray[order]!!.id!!
            nowCityName = cityArray[order]!!.name!!
            runOnUiThread { initialAll() }

            Log.d("test", nowCityName)
        }
    }

    private fun goToiNextCity() {
        Log.d("test", order.toString())
        Log.d("test", "click")
        if (cityArray.size > 1) {
            order = if (order == cityArray.size - 1) {
                0
            } else {
                order + 1
            }
            nowCity = cityArray[order]!!.id!!
            nowCityName = cityArray[order]!!.name!!
            runOnUiThread { initialAll() }
            Log.d("test", nowCityName)
        }
    }

    private fun refreshCity() {
        if (map.isNotEmpty()) {
            initialAll()
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "???????????????,????????????????????????", Toast.LENGTH_SHORT).show()
            thread {
                testBaiduLocation()
            }
            getReady()
            if (map.isNotEmpty()) {
                initialAll()
                Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getReady() {
        reader = getSharedPreferences("monitorCity", MODE_PRIVATE)
        map = reader.all
        cityArray = arrayOfNulls(map.size)
        var num = 0
        map.forEach {
            if (!init) {
                nowCityName = it.key
                nowCity = it.value.toString().toInt()
                init = true
            }
            cityArray[num] = City(it.key, it.value.toString().toInt())
            num++
            SearchWeather.searchDetailedWeather(it.value.toString().toInt())
        }
        Log.d("main", "get ready")
    }

    private fun testBaiduLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//?????? API level ??????????????? 23(Android 6.0) ???
            //????????????????????????
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //???????????????????????????????????????????????????????????????
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    Toast.makeText(this, "???Android 6.0??????????????????????????????", Toast.LENGTH_SHORT).show()
                }
                //????????????
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    1
                )
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//?????? API level ??????????????? 23(Android 6.0) ???
            //????????????????????????
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //???????????????????????????????????????????????????????????????
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    Toast.makeText(this, "???Android 6.0??????????????????????????????", Toast.LENGTH_SHORT).show()
                }
                //????????????
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        }

        val mLocationClient = LocationClient(MyApplication.context)
        mLocationClient.registerLocationListener(object : BDAbstractLocationListener() {
            override fun onReceiveLocation(location: BDLocation?) {
                val city = location?.city ?: ""
                var district: String = location?.district ?: ""
                val adcode: String = location?.adCode ?: ""
                Log.d("district", district + adcode)
                if (district == "") {
                    district = city
                }
                if (location != null) {
                    val reader2 = getSharedPreferences("monitorCity", Context.MODE_PRIVATE)
                    Log.e("error", location.locType.toString() + location.locTypeDescription)
                    if (adcode != "") {
                        var contain = false
                        val map2 = reader2.all
                        var repeatString = ""
                        map2.forEach {
                            if (it.value.toString() == adcode) {
                                repeatString = it.key
                                contain = true
                            }
                        }
                        if (!contain) {
                            reader2.edit {
                                putInt(district, adcode.toInt())
                                apply()
                            }
                        } else {
                            reader2.edit {
                                remove(repeatString)
                                putInt(district, adcode.toInt())
                                apply()
                            }

                        }
                    } else if (getSharedPreferences(
                            "monitorCity",
                            Context.MODE_PRIVATE
                        ).all.isEmpty()
                    ) {
                        thread {
                            mLocationClient.restart()
                        }
                    }
                }
            }
        })
        val option = LocationClientOption()
        option.setIsNeedAddress(true)
        option.setNeedNewVersionRgc(true)
        option.openGps = true
        mLocationClient.locOption = option
        mLocationClient.start()
        Log.e("test", "onStart")
    }

    private fun initialAll() {
        while (SearchWeather.getDetailedWeather(nowCity) == null) {
            Thread.sleep(100)
        }
        detailedWeather = SearchWeather.getDetailedWeather(nowCity)!!
        var _name =
            if (nowCityName.contains("???")) {
                if (nowCityName.split("???")[1].length >= 2) {
                    nowCityName.split("???")[1]
                } else {
                    nowCityName.split("???")[0]
                }

            } else {
                nowCityName
            }
        try {
            _name = when {
                _name[_name.length - 1] == ('???') -> _name.substring(_name.indexOf('???')+1)
                else -> _name
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.mainTitle.text = _name
        binding.mainTemperature.text = detailedWeather.temperature.toInt().toString()
        binding.mainSkycon.text = getSkyconString(detailedWeather.skycon)
        binding.mainlayout.setBackgroundResource(getBackground(detailedWeather.skycon))
        binding.comfort.text = detailedWeather.comfort
        binding.airQuality.text = detailedWeather.airQuality
        val recyclerView = binding.hourlyView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        val adapter = WeatherAdapter(detailedWeather.arrHourly)
        recyclerView.adapter = adapter
        initialView()
    }

    override fun onResume() {
        super.onResume()
        getReady()
        if (map.isNotEmpty()) {
            runOnUiThread { initialAll() }
        } else {
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_LONG).show()
        }
    }

    private fun initialView() {
        try {
            binding.day2.text = getDate(1)
            binding.day3.text = getDate(2)
            binding.day4.text = getDate(3)
            binding.day5.text = getDate(4)
            binding.skycon1.setImageResource(getSkyconIMG(0, detailedWeather))
            binding.skycon2.setImageResource(getSkyconIMG(1, detailedWeather))
            binding.skycon3.setImageResource(getSkyconIMG(2, detailedWeather))
            binding.skycon4.setImageResource(getSkyconIMG(3, detailedWeather))
            binding.skycon5.setImageResource(getSkyconIMG(4, detailedWeather))
            binding.skyconText1.text = getSkyconString(detailedWeather.arrDaily[0]!!.skycon)
            binding.skyconText2.text = getSkyconString(detailedWeather.arrDaily[1]!!.skycon)
            binding.skyconText3.text = getSkyconString(detailedWeather.arrDaily[2]!!.skycon)
            binding.skyconText4.text = getSkyconString(detailedWeather.arrDaily[3]!!.skycon)
            binding.skyconText5.text = getSkyconString(detailedWeather.arrDaily[4]!!.skycon)
            binding.temperature1.text =
                getTemperatureString(detailedWeather.arrDaily[0]!!.temperature)
            binding.temperature2.text =
                getTemperatureString(detailedWeather.arrDaily[1]!!.temperature)
            binding.temperature3.text =
                getTemperatureString(detailedWeather.arrDaily[2]!!.temperature)
            binding.temperature4.text =
                getTemperatureString(detailedWeather.arrDaily[3]!!.temperature)
            binding.temperature5.text =
                getTemperatureString(detailedWeather.arrDaily[4]!!.temperature)
            binding.apparentTemperature.text =
                getTemperatureString(detailedWeather.apparentTemperature.toString())
            binding.windSpeed.text = getWindDegree(detailedWeather.windSpeed)
            binding.windDirection.text = detailedWeather.windDirection
            binding.humidity.text = getHumidity(detailedWeather.humidity)
            binding.ultraviolet.text = detailedWeather.ultraviolet
            binding.visibility.text = getVisibility(detailedWeather.visibility)
            binding.pressure.text = getPressure(detailedWeather.pressure)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getDate(addition: Int): String {
        val c = Calendar.getInstance()
        c.timeZone = TimeZone.getTimeZone("GMT+8:00")
        var mMonth = (c.get(Calendar.MONTH) + 1).toString()// ??????????????????
        var mDay = (c.get(Calendar.DAY_OF_MONTH) + addition).toString()// ?????????????????????????????????
        var mWay = ((c.get(Calendar.DAY_OF_WEEK) + addition) % 7).toString()
        when (mWay) {
            "1" -> {
                mWay = "???"
            }
            "2" -> {
                mWay = "???"
            }
            "3" -> {
                mWay = "???"
            }
            "4" -> {
                mWay = "???"
            }
            "5" -> {
                mWay = "???"
            }
            "6" -> {
                mWay = "???"
            }
            "0" -> {
                mWay = "???"
            }
        }
        if (mMonth == "1" || mMonth == "3" || mMonth == "5" || mMonth == "7" || mMonth == "8" || mMonth == "10" || mMonth == "12"
        ) {
            if (mDay.toInt() > 31) {
                mDay = (mDay.toInt() % 31).toString()
                mMonth = if (mMonth == "12") {
                    "1"
                } else {
                    (mMonth.toInt() + 1).toString()
                }
            }
        } else if (mMonth == "4" || mMonth == "6" || mMonth == "9" || mMonth == "11") {
            if (mDay.toInt() > 30) {
                mDay = (mDay.toInt() % 30).toString()
                mMonth = (mMonth.toInt() + 1).toString()
            }
        } else if (mMonth == "2") {
            if (mDay.toInt() > 28) {
                mDay = (mDay.toInt() % 28).toString()
                mMonth = "3"
            }
        }
        return mMonth + "???" + mDay + "???" + "/??????" + mWay
    }

    private fun getBackground(skycon: String) = when (skycon) {
        "CLEAR_DAY" -> (R.drawable.bg_clear_day)
        "CLEAR_NIGHT" -> (R.drawable.bg_clear_night)
        "CLOUDY" -> (R.drawable.bg_cloudy)
        "PARTLY_CLOUDY_DAY" -> (R.drawable.bg_partly_cloudy_day)
        "PARTLY_CLOUDY_NIGHT" -> (R.drawable.bg_partly_cloudy_night)
        "LIGHT_HAZE" -> (R.drawable.bg_fog)
        "MODERATE_HAZE" -> (R.drawable.bg_fog)
        "HEAVY_HAZE" -> (R.drawable.bg_fog)
        "LIGHT_RAIN" -> (R.drawable.bg_rain)
        "MODERATE_RAIN" -> (R.drawable.bg_rain)
        "HEAVY_RAIN" -> (R.drawable.bg_rain)
        "STORM_RAIN" -> (R.drawable.bg_rain)
        "FOG" -> (R.drawable.bg_fog)
        "LIGHT_SNOW" -> (R.drawable.bg_snow)
        "MODERATE_SNOW" -> (R.drawable.bg_snow)
        "HEAVY_SNOW" -> (R.drawable.bg_snow)
        "STORM_SNOW" -> (R.drawable.bg_snow)
        "DUST" -> (R.drawable.bg_fog)
        "SAND" -> (R.drawable.bg_fog)
        "WIND" -> (R.drawable.bg_fog)
        else -> (R.drawable.bg_place)
    }

    private fun getSkyconIMG(num: Int, weather: DetailedWeather) =
        when (weather.arrDaily[num]!!.skycon) {
            "CLEAR_DAY" -> (R.drawable.ic_clear_day)
            "CLEAR_NIGHT" -> (R.drawable.ic_clear_night)
            "CLOUDY" -> (R.drawable.ic_cloudy)
            "PARTLY_CLOUDY_DAY" -> (R.drawable.ic_partly_cloud_day)
            "PARTLY_CLOUDY_NIGHT" -> (R.drawable.ic_partly_cloud_night)
            "LIGHT_HAZE" -> (R.drawable.ic_light_haze)
            "MODERATE_HAZE" -> (R.drawable.ic_moderate_haze)
            "HEAVY_HAZE" -> (R.drawable.ic_heavy_haze)
            "LIGHT_RAIN" -> (R.drawable.ic_light_rain)
            "MODERATE_RAIN" -> (R.drawable.ic_moderate_rain)
            "HEAVY_RAIN" -> (R.drawable.ic_heavy_rain)
            "STORM_RAIN" -> (R.drawable.ic_storm_rain)
            "FOG" -> (R.drawable.ic_fog)
            "LIGHT_SNOW" -> (R.drawable.ic_light_snow)
            "MODERATE_SNOW" -> (R.drawable.ic_moderate_snow)
            "HEAVY_SNOW" -> (R.drawable.ic_heavy_snow)
            "STORM_SNOW" -> (R.drawable.ic_heavy_snow)
            "DUST" -> (R.drawable.ic_dust)
            "SAND" -> (R.drawable.ic_sand)
            "WIND" -> (R.drawable.ic_wind)
            else -> Log.d("skycon", "error")
        }

    private fun getSkyconString(weather: String) =
        when (weather) {
            "CLEAR_DAY" -> "???"
            "CLEAR_NIGHT" -> "???"
            "CLOUDY" -> "???"
            "PARTLY_CLOUDY_DAY" -> "??????"
            "PARTLY_CLOUDY_NIGHT" -> "??????"
            "LIGHT_HAZE" -> "??????"
            "MODERATE_HAZE" -> "??????"
            "HEAVY_HAZE" -> "??????"
            "LIGHT_RAIN" -> "??????"
            "MODERATE_RAIN" -> "??????"
            "HEAVY_RAIN" -> "??????"
            "STORM_RAIN" -> "??????"
            "FOG" -> "???"
            "LIGHT_SNOW" -> "??????"
            "MODERATE_SNOW" -> "??????"
            "HEAVY_SNOW" -> "??????"
            "STORM_SNOW" -> "??????"
            "DUST" -> "??????"
            "SAND" -> "??????"
            "WIND" -> "??????"
            else -> ""
        }

    private fun getWindDegree(speed: Double) =
        when {
            speed <= 0.2 -> "0???"
            speed <= 1.5 -> "1???"
            speed <= 3.3 -> "2???"
            speed <= 5.4 -> "3???"
            speed <= 7.9 -> "4???"
            speed <= 10.7 -> "5???"
            speed <= 13.8 -> "6???"
            speed <= 17.1 -> "7???"
            speed <= 20.7 -> "8???"
            speed <= 24.4 -> "9???"
            speed <= 28.4 -> "10???"
            speed <= 32.6 -> "11???"
            speed <= 36.9 -> "12???"
            else -> "13???"
        }

    private fun getTemperatureString(str: String) = str.toDouble().toInt().toString() + "???"
    private fun getHumidity(humidity: Double) = (humidity * 100).toInt().toString() + "%"
    private fun getVisibility(num: Double) = num.toInt().toString() + "km"
    private fun getPressure(num: Double) = (num / 100).toInt().toString() + "hPa"

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

    }
}