package com.example.sunnyweather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.UI.place.CityAdapter
import com.example.sunnyweather.UI.place.CityListManager
import com.example.sunnyweather.UI.weather.SearchWeather
import com.example.sunnyweather.databinding.ActivityCityManageBinding
import kotlin.concurrent.thread


class CityManageActivity : AppCompatActivity() {
    private lateinit var cityListManager: CityListManager
    private lateinit var cityList: ArrayList<SelectedCity>
    private lateinit var binding: ActivityCityManageBinding
    private lateinit var adapter: CityAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        cityListManager = CityListManager(this)
        val reader = getSharedPreferences("monitorCity", Context.MODE_PRIVATE)
        val map = reader.all
        map.forEach {
            cityListManager.addCity(SelectedCity(it.key, "--", it.value.toString().toInt()))
        }
        cityList = cityListManager.getCityList() ?: ArrayList()
        super.onCreate(savedInstanceState)
        binding = ActivityCityManageBinding.inflate(layoutInflater)
        setSupportActionBar(binding.placeToolbar)
        supportActionBar?.title = "城市管理"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val layoutManager = LinearLayoutManager(this)
        setContentView(binding.root)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        adapter = CityAdapter(cityList, this)
        recyclerView.adapter = adapter
        refreshTemperature(adapter)
        registerForContextMenu(recyclerView)
        binding.addCity.bringToFront()
        binding.addCity.setOnClickListener {
            startActivityForResult(Intent(this, AddCityActivity::class.java), 1)
        }
        binding.swipeRefresh.setOnRefreshListener {
            thread {
                Thread.sleep(1000)
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                    binding.swipeRefresh.isRefreshing = false
                    refreshTemperature(adapter)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            thread {
                Thread.sleep(1000)
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                    binding.swipeRefresh.isRefreshing = false
                    refreshTemperature(adapter)
                }
            }
            try {
                val selectedCity = data?.getSerializableExtra("selectedCity") as City
                val newCity = SelectedCity(selectedCity.name!!, "--", selectedCity.id!!)
                val cityReader = getSharedPreferences("monitorCity", Context.MODE_PRIVATE)
                var repeat = false
                cityReader.all.forEach {
                    if (it.value.toString().toInt() == newCity.id) {
                        repeat = true
                    }
                }
                if (repeat) {
                    Toast.makeText(this, "城市已经存在", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
                    cityListManager.addCity(newCity)
                    cityReader.edit {
                        putInt(newCity.name, newCity.id)
                        apply()
                    }
                }
                adapter.notifyDataSetChanged()
                Log.d("getCity", "${selectedCity.id}  ${selectedCity.name}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            refreshTemperature(adapter)
        }
    }

    //辽宁省
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        thread {
            Thread.sleep(1000)
            runOnUiThread {
                adapter.notifyDataSetChanged()
                binding.swipeRefresh.isRefreshing = false
                refreshTemperature(adapter)
            }
        }
        refreshTemperature(adapter)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(0, 1, 1, "删除")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onContextItemSelected(item: MenuItem): Boolean {

        getSharedPreferences("toDelete", Context.MODE_PRIVATE).getString("toDelete", "")?.let {
            cityListManager.removeCity(it)
            getSharedPreferences("monitorCity", Context.MODE_PRIVATE).edit {
                remove(it)
            }
        }
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
        findViewById<RecyclerView>(R.id.recyclerView).adapter?.notifyDataSetChanged()
        getSharedPreferences("toDelete", Context.MODE_PRIVATE).edit {
            clear()
            apply()
        }
        Log.d("viewmodel", cityListManager.cityListViewModel.size.value.toString())
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return true
            }
        }
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshTemperature(adapter: CityAdapter) {
        for (city in cityListManager.getCityList()!!) {
            city.name
            val id = city.id
            SearchWeather.searchWeather(id)
            val weather = SearchWeather.getWeather(id)
            val temperature = weather?.temperature
            val skycon = weather?.skycon
            if (skycon != null) {
                city.skycon = skycon
            }
            if (temperature != null) {
                city.temperature = temperature
            }
        }
        adapter.notifyDataSetChanged()
    }
}