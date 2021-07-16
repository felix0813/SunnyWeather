package com.example.sunnyweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.UI.place.HintAdapter
import com.example.sunnyweather.UI.place.MyQueryTextListener
import com.example.sunnyweather.UI.place.ResultAdapter
import com.example.sunnyweather.databinding.ActivityAddCityBinding

class AddCityActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddCityBinding
    val list=ArrayList<City>()
    val list2=ArrayList<City>()
    override fun onCreate(savedInstanceState: Bundle?) {

        val adapter=ResultAdapter(list,this)
        val adapter2=HintAdapter(list2,this)
        val listener=MyQueryTextListener(this,list,list2,adapter,adapter2)
        super.onCreate(savedInstanceState)
        binding= ActivityAddCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchView.setOnSearchClickListener {
            binding.hintRecycler.visibility=View.VISIBLE
            binding.searchTitle.visibility= View.INVISIBLE
        }
        binding.searchView.setOnCloseListener {
            list.clear()
            binding.hintRecycler.visibility=View.GONE
            adapter.notifyDataSetChanged()
            binding.searchTitle.visibility= View.VISIBLE
            return@setOnCloseListener false
        }
        val layoutManager=GridLayoutManager(this,3)
        val resultRecyclerView=binding.resultRecycler
        resultRecyclerView.layoutManager=layoutManager
        resultRecyclerView.adapter=adapter



        val layoutManager2=LinearLayoutManager(this)
        val hintRecyclerView=binding.hintRecycler
        hintRecyclerView.layoutManager=layoutManager2
        hintRecyclerView.adapter=adapter2

        binding.cancel.setOnClickListener {
            startActivity((Intent(this,CityManageActivity::class.java)))
            finish()
        }
        binding.searchView.setOnQueryTextListener(listener)
    }
}