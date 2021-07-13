package com.example.sunnyweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sunnyweather.UI.place.MyQueryTextListener
import com.example.sunnyweather.databinding.ActivityAddCityBinding

class AddCityActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddCityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchView.setOnSearchClickListener {
            binding.searchTitle.visibility= View.INVISIBLE
        }
        binding.searchView.setOnCloseListener {
            binding.searchTitle.visibility= View.VISIBLE
            return@setOnCloseListener false
        }
        binding.searchView.setOnQueryTextListener(MyQueryTextListener())

    }
}