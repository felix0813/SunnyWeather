package com.example.sunnyweather.UI.place

import android.content.Context
import androidx.appcompat.widget.SearchView
import androidx.core.content.edit
import com.example.sunnyweather.MyApplication
import kotlin.concurrent.thread

class MyQueryTextListener: SearchView.OnQueryTextListener {
    val cityDBManager=CityDBManager()
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query==null){
            return false
        }
        else{
            thread {
                val list=cityDBManager.cityDAO.queryName(query)
                for(name in list){
                    MyApplication.context.getSharedPreferences("queryResult",Context.MODE_PRIVATE).edit {
                        putString(name,"1")
                    }
                }
            }
            return true
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("Not yet implemented")
    }
}