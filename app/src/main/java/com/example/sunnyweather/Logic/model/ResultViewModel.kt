package com.example.sunnyweather.Logic.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {
    val resultList = MutableLiveData<ArrayList<String>>()
    val size = MutableLiveData<Int>()

    init {
        size.value = 0
        resultList.value = ArrayList()
    }

    fun addCity(result: String): Boolean {
        resultList.value?.add(result)
        size.value = resultList.value?.size
        return true
    }
}