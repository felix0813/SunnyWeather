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
import com.example.sunnyweather.UI.place.CityDBManager
import com.example.sunnyweather.UI.place.CityListManager
import com.example.sunnyweather.databinding.ActivityPlaceBinding



class PlaceActivity : AppCompatActivity() {
    lateinit var cityListManager:CityListManager
    lateinit var cityList:ArrayList<RecyclerCity>
    val cityDBManager by lazy { CityDBManager() }
    lateinit var binding:ActivityPlaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        cityListManager= CityListManager(this)
        val reader=getSharedPreferences("cityList", Context.MODE_PRIVATE)
        val map=reader.all
        map.forEach{
            cityListManager.addCity(RecyclerCity(it.key,it.value.toString()))
        }

        cityList=cityListManager.getCityList() ?: ArrayList<RecyclerCity>()
        super.onCreate(savedInstanceState)
        binding= ActivityPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.placeToolbar)
        supportActionBar?.title="城市管理"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val layoutManager=LinearLayoutManager(this)
        val recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager=layoutManager
        val adapter=CityAdapter(cityList,this)
        recyclerView.adapter=adapter
        registerForContextMenu(recyclerView)
        cityListManager.observeList()
        cityListManager.addCity(RecyclerCity("深圳","30"))
        binding.addCity.bringToFront()
        binding.addCity.setOnClickListener {
            startActivity(Intent(this,AddCityActivity::class.java))
        }
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
        }
        Toast.makeText(this,"delete",Toast.LENGTH_SHORT).show()
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
                finish()
                return true
            }
        }
        return true
    }
}