package com.example.sunnyweather.Logic.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.MyApplication

class AKViewModel : ViewModel() {
    val AKCode = MutableLiveData<String>()
    fun setAK(token: String) {
        AKCode.value = token
        MyApplication.context.getSharedPreferences("ak", Context.MODE_PRIVATE).edit().apply {
            putString("ak", token)
            apply()
        }

    }
}