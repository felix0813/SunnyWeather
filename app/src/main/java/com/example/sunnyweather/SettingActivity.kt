package com.example.sunnyweather

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.Logic.model.AKViewModel
import com.example.sunnyweather.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    lateinit var viewModel: AKViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.settingToolbar)
        supportActionBar?.title = "设置"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val reader = getSharedPreferences("ak", Context.MODE_PRIVATE)
        var AK = reader.getString("ak", "")
        viewModel = ViewModelProvider(this).get(AKViewModel::class.java)
        viewModel.AKCode.value = AK
        binding.tokenCode.setText(viewModel.AKCode.value)


        binding.Button.setOnClickListener {
            AK = binding.tokenCode.text.toString()
            viewModel.setAK(AK!!)
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show()
            while (getSharedPreferences("ak", Context.MODE_PRIVATE).all.size == 0) {
                Thread.sleep(1000)
            }
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return true
    }
}