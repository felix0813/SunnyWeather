package com.example.sunnyweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sunnyweather.UI.place.MyQueryTextListener
import com.example.sunnyweather.UI.place.ResultAdapter
import com.example.sunnyweather.databinding.ActivityAddCityBinding

class AddCityActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddCityBinding
    val list=ArrayList<City>()
    override fun onCreate(savedInstanceState: Bundle?) {

        val adapter=ResultAdapter(list,this)
        val listener=MyQueryTextListener(this,list,adapter)
        super.onCreate(savedInstanceState)
        binding= ActivityAddCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchView.setOnSearchClickListener {
            binding.searchTitle.visibility= View.INVISIBLE
        }
        binding.searchView.setOnCloseListener {
            list.clear()
            adapter.notifyDataSetChanged()
            binding.searchTitle.visibility= View.VISIBLE
            return@setOnCloseListener false
        }
        val layoutManager=GridLayoutManager(this,3)
        val resultRecyclerView=binding.resultRecycler
        resultRecyclerView.layoutManager=layoutManager
        resultRecyclerView.adapter=adapter

        binding.cancel.setOnClickListener {
            finish()
        }
        binding.searchView.setOnQueryTextListener(listener)
    }
}