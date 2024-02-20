package com.example.marketplace.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newapptester1.R
import com.example.newapptester1.databinding.ActivityShoppingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    private var binding: ActivityShoppingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navController = findNavController(R.id.shoppingHostFragment)
        binding?.bottomNavigation?.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
       
        binding = null
    }
}
