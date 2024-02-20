package com.example.marketplace.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.marketplace.adapters.HomeViewpagerAdapter
import com.example.marketplace.fragments.categories.AccessoryFragment
import com.example.marketplace.fragments.categories.ChairFragment
import com.example.marketplace.fragments.categories.FurnitureFragment
import com.example.marketplace.fragments.categories.MainCategoryFragment
import com.example.marketplace.fragments.categories.TableFragment
import com.example.newapptester1.R
import com.example.newapptester1.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment:Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )
        binding.viewpagerHome.isUserInputEnabled = false
        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome){tab, position ->
            when(position){
                    0->tab.text= "Main"
                1-> tab.text = "Chair"
                2-> tab.text = "Cupboard"
                3-> tab.text = "Accessory"
                4-> tab.text = "Furniture"
            }
        }.attach()
    }
}