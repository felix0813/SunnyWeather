package com.example.sunnyweather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.example.sunnyweather.UI.weather.SearchWeather
import com.example.sunnyweather.UI.weather.WeatherAdapter
import com.example.sunnyweather.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var cityArray: Array<City?>
    lateinit var reader:SharedPreferences
    lateinit var map:MutableMap<String,*>
    var order=0
    lateinit var detailedWeather: DetailedWeather
    var nowCityName=""
    var nowCity=-1
    var init=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getReady()
        thread {  testBaiduLocation()}
        binding= ActivityMainBinding.inflate(layoutInflater)
        if(map.size!=0) {
            initialAll()
        }
        else{
            Toast.makeText(this,"当前无选中城市",Toast.LENGTH_LONG).show()
        }

        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        supportActionBar?.title=""
        binding.previousCity.bringToFront()
        binding.nextCity.bringToFront()
        binding.setCity.setOnClickListener {
            startActivity(Intent(this,CityManageActivity::class.java))
            finish()
        }
        binding.previousCity.setOnClickListener {
            Log.d("test",order.toString())
            Log.d("test","click")
            if(cityArray.size>1){
            order=if(order==0){
                cityArray.size-1
            }
            else{
                order-1
            }
                nowCity= cityArray[order]!!.id!!
                nowCityName=cityArray[order]!!.name!!
                initialAll()
            }
        }
        binding.nextCity.setOnClickListener {
            Log.d("test",order.toString())
            Log.d("test","click")
            if(cityArray.size>1){
            order=if(order==cityArray.size-1){
                0
            }
            else{
                order+1
            }
                nowCity= cityArray[order]!!.id!!
                nowCityName=cityArray[order]!!.name!!
                initialAll()
        }
        }
        binding.refreshButton.bringToFront()
        binding.refreshButton.setOnClickListener {
            if (map.size!=0){
                initialAll()
                Toast.makeText(this,"刷新成功",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"无选中城市,尝试定位当前城市",Toast.LENGTH_SHORT).show()
                thread {
                    testBaiduLocation()
                }
                getReady()
                if (map.size!=0){
                    initialAll()
                    Toast.makeText(this,"刷新成功",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun getReady() {
        reader = getSharedPreferences("monitorCity", MODE_PRIVATE)
        map = reader.all
        cityArray = arrayOfNulls(map.size)
        var num = 0
        map.forEach {
            if (init == false) {
                nowCityName = it.key
                nowCity = it.value.toString().toInt()
                init = true
            }
            cityArray[num] = City(it.key, it.value.toString().toInt())
            num++
            SearchWeather.searchDetailedWeather(it.value.toString().toInt())
        }
        Log.d("main","get ready")
    }

    private fun testBaiduLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    Toast.makeText(this, "自Android 6.0开始需要打开位置权限", Toast.LENGTH_SHORT).show()
                }
                //请求权限
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    1
                )
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    Toast.makeText(this, "自Android 6.0开始需要打开位置权限", Toast.LENGTH_SHORT).show()
                }
                //请求权限
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
                val city=location?.getCity()?:""
                var district: String = location?.getDistrict() ?: ""
                val adcode: String = location?.getAdCode() ?: ""
                Log.d("district", district + adcode)
                if(district==""){
                    district=city
                }
                if (location != null) {
                    val reader2=getSharedPreferences("monitorCity",Context.MODE_PRIVATE)
                    Log.e("error", location.locType.toString() + location.locTypeDescription)
                    if(!adcode.equals("")){
                        var contain=false
                        val map2=reader2.all
                        var repeatString=""
                        map2.forEach{
                            if(it.value.toString().equals(adcode)){
                                repeatString=it.key
                                contain=true
                            }
                        }
                        if(contain==false){
                        reader2.edit {
                            putInt(district,adcode.toInt())
                            apply()
                        }}
                        else{
                            reader2.edit {
                                remove(repeatString)
                                putInt(district,adcode.toInt())
                                apply()
                            }

                        }
                    }
                    else if(getSharedPreferences("monitorCity",Context.MODE_PRIVATE).all.size==0){
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
        Log.e("test","onStart")
    }

    private fun initialAll() {
        while (SearchWeather.getDetailedWeather(nowCity) == null) {
            Thread.sleep(100)
        }
        detailedWeather = SearchWeather.getDetailedWeather(nowCity)!!
        var _name =
            if (nowCityName.contains("省")) {
                nowCityName.split("省")[1]
            } else {
                nowCityName
            }
        try {
            _name = when {
                _name[_name.length - 1] == ('区') -> _name.split("市")[1]
                else -> _name
        }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        binding.mainTitle.setText(_name)
        binding.mainTemperature.setText(detailedWeather.temperature.toInt().toString())
        binding.mainSkycon.setText(getSkyconString(detailedWeather.skycon))
        binding.mainlayout.setBackgroundResource(getBackground(detailedWeather.skycon))
        binding.comfort.setText(detailedWeather.comfort)
        binding.airQuality.setText(detailedWeather.airQuality)
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
        if(getSharedPreferences("ak",Context.MODE_PRIVATE).all.size==0){
            AlertDialog.Builder(this).setTitle("警告").setMessage("您还没有填入令牌，请先填入令牌再使用").setCancelable(false).setPositiveButton("确定",{_,_->
                startActivity(Intent(this,SettingActivity::class.java))
            }).create().show()
        }
    }

    private fun initialView() {
        try {
            binding.day2.setText(getDate(1))
            binding.day3.setText(getDate(2))
            binding.day4.setText(getDate(3))
            binding.day5.setText(getDate(4))
            binding.skycon1.setImageResource(getSkyconIMG(0, detailedWeather))
            binding.skycon2.setImageResource(getSkyconIMG(1, detailedWeather))
            binding.skycon3.setImageResource(getSkyconIMG(2, detailedWeather))
            binding.skycon4.setImageResource(getSkyconIMG(3, detailedWeather))
            binding.skycon5.setImageResource(getSkyconIMG(4, detailedWeather))
            binding.skyconText1.setText(getSkyconString(detailedWeather.arrDaily[0]!!.skycon))
            binding.skyconText2.setText(getSkyconString(detailedWeather.arrDaily[1]!!.skycon))
            binding.skyconText3.setText(getSkyconString(detailedWeather.arrDaily[2]!!.skycon))
            binding.skyconText4.setText(getSkyconString(detailedWeather.arrDaily[3]!!.skycon))
            binding.skyconText5.setText(getSkyconString(detailedWeather.arrDaily[4]!!.skycon))
            binding.temperature1.setText(getTemperatureString(detailedWeather.arrDaily[0]!!.temperature))
            binding.temperature2.setText(getTemperatureString(detailedWeather.arrDaily[1]!!.temperature))
            binding.temperature3.setText(getTemperatureString(detailedWeather.arrDaily[2]!!.temperature))
            binding.temperature4.setText(getTemperatureString(detailedWeather.arrDaily[3]!!.temperature))
            binding.temperature5.setText(getTemperatureString(detailedWeather.arrDaily[4]!!.temperature))
            binding.apparentTemperature.text =
                getTemperatureString(detailedWeather.apparentTemperature.toString())
            binding.windSpeed.text = getWindDegree(detailedWeather.windSpeed)
            binding.windDirection.setText(detailedWeather.windDirection)
            binding.humidity.text = getHumidity(detailedWeather.humidity)
            binding.ultraviolet.text = detailedWeather.ultraviolet
            binding.visibility.text = getVisibility(detailedWeather.visibility)
            binding.pressure.text = getPressure(detailedWeather.pressure)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting->{
                startActivity(Intent(this,SettingActivity::class.java))
            }
        }
        return  true
    }
    fun getDate(addition:Int):String{
        val c = Calendar.getInstance()
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"))
        var mMonth = (c.get(Calendar.MONTH) + 1).toString()// 获取当前月份
        var mDay = (c.get(Calendar.DAY_OF_MONTH)+addition).toString()// 获取当前月份的日期号码
        var mWay = ((c.get(Calendar.DAY_OF_WEEK)+addition)%7).toString()
        if("1".equals(mWay)){
            mWay ="天"
        }else if("2".equals(mWay)){
            mWay ="一"
        }else if("3".equals(mWay)){
            mWay ="二"
        }else if("4".equals(mWay)){
            mWay ="三"
        }else if("5".equals(mWay)){
            mWay ="四"
        }else if("6".equals(mWay)){
            mWay ="五"
        }else if("0".equals(mWay)){
            mWay ="六"
        }
        if(mMonth.equals("1")||mMonth.equals("3")||mMonth.equals("5")||mMonth.equals("7")||mMonth.equals("8")||mMonth.equals("10")||mMonth.equals("12")){
            if(mDay.toInt()>31){
                mDay=(mDay.toInt()%31).toString()
                if(mMonth.equals("12")){
                    mMonth="1"
                }
                else{
                    mMonth=(mMonth.toInt()+1).toString()
                }
            }
        }
        else if(mMonth.equals("4")||mMonth.equals("6")||mMonth.equals("9")||mMonth.equals("11")){
            if(mDay.toInt()>30){
                mDay=(mDay.toInt()%30).toString()
                mMonth=(mMonth.toInt()+1).toString()
            }
        }
        else if(mMonth.equals("2")){
            if(mDay.toInt()>28){
                mDay=(mDay.toInt()%28).toString()
                mMonth="3"
            }
        }
        return mMonth+"月" + mDay+"日"+"/星期"+mWay
    }
    fun getBackground(skycon:String)=when(skycon){
        "CLEAR_DAY"->(R.drawable.bg_clear_day)
        "CLEAR_NIGHT"->(R.drawable.bg_clear_night)
        "CLOUDY"->(R.drawable.bg_cloudy)
        "PARTLY_CLOUDY_DAY"-> (R.drawable.bg_partly_cloudy_day)
        "PARTLY_CLOUDY_NIGHT"->(R.drawable.bg_partly_cloudy_night)
        "LIGHT_HAZE"->(R.drawable.bg_fog)
        "MODERATE_HAZE"->(R.drawable.bg_fog)
        "HEAVY_HAZE"->(R.drawable.bg_fog)
        "LIGHT_RAIN"->(R.drawable.bg_rain)
        "MODERATE_RAIN"->(R.drawable.bg_rain)
        "HEAVY_RAIN"->(R.drawable.bg_rain)
        "STORM_RAIN"->(R.drawable.bg_rain)
        "FOG"->(R.drawable.bg_fog)
        "LIGHT_SNOW"->(R.drawable.bg_snow)
        "MODERATE_SNOW"->(R.drawable.bg_snow)
        "HEAVY_SNOW"->(R.drawable.bg_snow)
        "STORM_SNOW"->(R.drawable.bg_snow)
        "DUST"->(R.drawable.bg_fog)
        "SAND"->(R.drawable.bg_fog)
        "WIND"->(R.drawable.bg_fog)
        else->(R.drawable.bg_place)
    }
    fun getSkyconIMG(num:Int,weather:DetailedWeather)=
        when(weather.arrDaily[num]!!.skycon){
            "CLEAR_DAY"->(R.drawable.ic_clear_day)
            "CLEAR_NIGHT"->(R.drawable.ic_clear_night)
            "CLOUDY"->(R.drawable.ic_cloudy)
            "PARTLY_CLOUDY_DAY"-> (R.drawable.ic_partly_cloud_day)
            "PARTLY_CLOUDY_NIGHT"->(R.drawable.ic_partly_cloud_night)
            "LIGHT_HAZE"->(R.drawable.ic_light_haze)
            "MODERATE_HAZE"->(R.drawable.ic_moderate_haze)
            "HEAVY_HAZE"->(R.drawable.ic_heavy_haze)
            "LIGHT_RAIN"->(R.drawable.ic_light_rain)
            "MODERATE_RAIN"->(R.drawable.ic_moderate_rain)
            "HEAVY_RAIN"->(R.drawable.ic_heavy_rain)
            "STORM_RAIN"->(R.drawable.ic_storm_rain)
            "FOG"->(R.drawable.ic_fog)
            "LIGHT_SNOW"->(R.drawable.ic_light_snow)
            "MODERATE_SNOW"->(R.drawable.ic_moderate_snow)
            "HEAVY_SNOW"->(R.drawable.ic_heavy_snow)
            "STORM_SNOW"->(R.drawable.ic_heavy_snow)
            "DUST"->(R.drawable.ic_dust)
            "SAND"->(R.drawable.ic_sand)
            "WIND"->(R.drawable.ic_wind)
            else-> Log.d("skycon","error")
        }
    fun getSkyconString(weather:String)=
        when(weather){
            "CLEAR_DAY"->"晴"
            "CLEAR_NIGHT"->"晴"
            "CLOUDY"->"阴"
            "PARTLY_CLOUDY_DAY"-> "多云"
            "PARTLY_CLOUDY_NIGHT"->"多云"
            "LIGHT_HAZE"->"轻霾"
            "MODERATE_HAZE"->"中霾"
            "HEAVY_HAZE"->"重霾"
            "LIGHT_RAIN"->"小雨"
            "MODERATE_RAIN"->"中雨"
            "HEAVY_RAIN"->"大雨"
            "STORM_RAIN"->"暴雨"
            "FOG"->"雾"
            "LIGHT_SNOW"->"小雪"
            "MODERATE_SNOW"->"中雪"
            "HEAVY_SNOW"->"大雪"
            "STORM_SNOW"->"暴雪"
            "DUST"->"浮尘"
            "SAND"->"沙尘"
            "WIND"->"大风"
            else->""
        }
    fun getWindDegree(speed:Double)=
        when{
            speed<=0.2->"0级"
            speed<=1.5->"1级"
            speed<=3.3 ->"2级"
            speed<=5.4 ->"3级"
            speed<=7.9->"4级"
            speed<=10.7->"5级"
            speed<=13.8->"6级"
            speed<=17.1->"7级"
            speed<=20.7->"8级"
            speed<=24.4->"9级"
            speed<=28.4->"10级"
            speed<=32.6->"11级"
            speed<=36.9->"12级"
            else->"13级"
        }
    fun getTemperatureString(str:String)=str.toDouble().toInt().toString()+"℃"
    fun getHumidity(humidity:Double)=(humidity*100).toInt().toString()+"%"
    fun getVisibility(num:Double)=num.toInt().toString()+"km"
    fun getPressure(num:Double)=(num/100).toInt().toString()+"hPa"

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

    }
}