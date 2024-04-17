package com.example.marketplace.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.marketplace.util.Resource
import com.example.marketplace.viewmodel.CartViewModel
import com.example.newapptester1.R
import com.example.newapptester1.databinding.ActivityShoppingBinding

import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = binding.root.rootView.height - binding.root.height
            if (heightDiff > 100) {
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
//        observeCartProducts()
    }

//    private fun observeCartProducts() {
//        lifecycleScope.launchWhenStarted {
//            viewModel.cartProducts.collectLatest { resource ->
//                when (resource) {
//                    is Resource.Success -> {
//                        val count = resource.data?.size ?: 0
//                        binding.bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
//                            number = count
//                            backgroundColor = ContextCompat.getColor(this@ShoppingActivity, R.color.g_blue)
//                        }
//                    }
//                    else -> Unit
//                }
//            }
//        }
//    }
}