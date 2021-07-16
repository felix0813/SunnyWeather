package com.example.sunnyweather.UI.place

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.City
import com.example.sunnyweather.Logic.model.ResultViewModel

class MyQueryTextListener(activity:AppCompatActivity, val _list:ArrayList<City>, val _list2:ArrayList<City>,val adapter: ResultAdapter,val adapter2: HintAdapter): SearchView.OnQueryTextListener {
    val cityDBManager=CityDBManager()
    val resultListViewModel=ViewModelProvider(activity).get(ResultViewModel::class.java)
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query==null){
            return false
        }
        else{
            _list2.clear()
            _list.clear()
            val list=cityDBManager.cityDAO.queryName(query)
            for(city in list){
                city.name?.let { Log.e("result", it) }

                _list.add(city)
                city.name?.let { resultListViewModel.addCity(it) }
            }
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
            return true
        }
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText==null){
            Log.e("newtext","null")
            _list.clear()
            adapter.notifyDataSetChanged()
            return false
        }
        else if(newText!=""){
            Log.d("newText",newText)
            _list.clear()
            val list=cityDBManager.cityDAO.queryName(newText)
            for(city in list){
                //city.name?.let { Log.e("result", it) }

                _list.add(city)
                city.name?.let { resultListViewModel.addCity(it) }
            }
            adapter.notifyDataSetChanged()
        }
        else{
            _list.clear()
            adapter.notifyDataSetChanged()
        }
        return false
    }

}