package com.example.sunnyweather

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.UI.place.CityAdapter
import com.example.sunnyweather.UI.place.CityListManager
import com.example.sunnyweather.UI.weather.SearchWeather
import com.example.sunnyweather.databinding.ActivityCityManageBinding
import java.lang.Exception
import kotlin.concurrent.thread


class CityManageActivity : AppCompatActivity() {
    lateinit var cityListManager:CityListManager
    lateinit var cityList:ArrayList<SelectedCity>
    lateinit var binding:ActivityCityManageBinding
    lateinit var adapter:CityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        cityListManager= CityListManager(this)
        val reader=getSharedPreferences("monitorCity", Context.MODE_PRIVATE)
        val map=reader.all
        map.forEach{
            cityListManager.addCity(SelectedCity(it.key,"--",it.value.toString().toInt()))
        }
        cityList=cityListManager.getCityList() ?: ArrayList<SelectedCity>()
        super.onCreate(savedInstanceState)
        binding= ActivityCityManageBinding.inflate(layoutInflater)
        setSupportActionBar(binding.placeToolbar)
        supportActionBar?.title="城市管理"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val layoutManager=LinearLayoutManager(this)
        setContentView(binding.root)
        val recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager=layoutManager
        adapter=CityAdapter(cityList,this)
        recyclerView.adapter=adapter
        refreshTemperature(adapter)
        registerForContextMenu(recyclerView)
        binding.addCity.bringToFront()
        binding.addCity.setOnClickListener {
            startActivity(Intent(this,AddCityActivity::class.java))
            finish()
        }
        binding.swipeRefresh.setOnRefreshListener {
            thread {
                Thread.sleep(1000)
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                    binding.swipeRefresh.isRefreshing=false
                    refreshTemperature (adapter)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        thread {
            Thread.sleep(1000)
            runOnUiThread {
                adapter.notifyDataSetChanged()
                binding.swipeRefresh.isRefreshing=false
                refreshTemperature (adapter)
            }
        }
        try {
            val selectedCity=intent.getSerializableExtra("selectedCity") as City
            val newCity=SelectedCity(selectedCity.name!!,"--", selectedCity.id!!)
            val cityReader=getSharedPreferences("monitorCity",Context.MODE_PRIVATE)
            var repeat=false
            cityReader.all.forEach{
                if(it.value.toString().toInt()==newCity.id){
                    repeat=true
                }
            }
            if(repeat==true){
                Toast.makeText(this,"城市已经存在",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show()
                cityListManager.addCity(newCity)
                cityReader.edit {
                    putInt(newCity.name,newCity.id)
                    apply()
                }
            }
            adapter.notifyDataSetChanged()
            Log.d("getCity","${selectedCity.id}  ${selectedCity.name}")
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        refreshTemperature(adapter)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(0,1,1,"删除")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        getSharedPreferences("toDelete",Context.MODE_PRIVATE).getString("toDelete","")?.let {
            cityListManager.removeCity(it)
            getSharedPreferences("monitorCity",Context.MODE_PRIVATE).edit{
                remove(it)
            }
        }
        Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show()
        findViewById<RecyclerView>(R.id.recyclerView).adapter?.notifyDataSetChanged()
        getSharedPreferences("toDelete",Context.MODE_PRIVATE).edit {
            clear()
            apply()
        }
        Log.d("viewmodel", cityListManager.cityListViewModel.size.value.toString())
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                startActivity(Intent(this,MainActivity::class.java))
                finish()
                return true
            }
        }
        return true
    }
    fun refreshTemperature(adapter: CityAdapter){
        for(city in cityListManager.getCityList()!!){
            city.name
            val id =city.id
            SearchWeather.searchWeather(id)
            val weather=SearchWeather.getWeather(id)
            val temperature= weather?.temperature
            val skycon= weather?.skycon
            if (skycon != null) {
                city.skycon=skycon
            }
            if (temperature != null) {
                city.temperature=temperature
            }
        }
        adapter.notifyDataSetChanged()
    }
}