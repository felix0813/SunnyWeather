package com.example.sunnyweather.ui.place

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.City
import com.example.sunnyweather.logic.model.ResultViewModel

class MyQueryTextListener(
    activity: AppCompatActivity,
    private val _list: ArrayList<City>,
    private val _list2: ArrayList<City>,
    private val adapter: ResultAdapter,
    //沈阳市
    private val adapter2: HintAdapter
) : SearchView.OnQueryTextListener {
    private val cityDBManager = CityDBManager()
    private val resultListViewModel = ViewModelProvider(activity).get(ResultViewModel::class.java)

    @SuppressLint("NotifyDataSetChanged")
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query == null) {
            return false
        } else {
            _list2.clear()
            _list.clear()
            val list = cityDBManager.cityDAO.queryName(query)
            for (city in list) {
                city.name?.let { Log.e("result", it) }

                _list.add(city)
                city.name?.let { resultListViewModel.addCity(it) }
            }
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
            return true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onQueryTextChange(newText: String?): Boolean {
        when {
            newText == null -> {
                Log.e("newtext", "null")
                _list.clear()
                adapter.notifyDataSetChanged()
                return false
            }
            newText != "" -> {
                Log.d("newText", newText)
                _list.clear()
                val list = cityDBManager.cityDAO.queryName(newText)
                for (city in list) {
                    //city.name?.let { Log.e("result", it) }

                    _list.add(city)
                    city.name?.let { resultListViewModel.addCity(it) }
                }
                adapter.notifyDataSetChanged()
            }
            else -> {
                _list.clear()
                adapter.notifyDataSetChanged()
            }
        }
        return false
    }

}