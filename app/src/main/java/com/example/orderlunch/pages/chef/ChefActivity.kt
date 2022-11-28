package com.example.orderlunch.pages.chef

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.orderlunch.adapters.AdapterPager
import com.example.orderlunch.databinding.ActivityChefBinding

class ChefActivity : AppCompatActivity() {
    lateinit var binding: ActivityChefBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChefBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}