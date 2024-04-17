package com.example.marketplace.fragments.shopping

import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.marketplace.adapters.HomeViewpagerAdapter
import com.example.marketplace.adapters.ProductAdapter
import com.example.marketplace.data.Product
import com.example.marketplace.fragments.categories.AccessoryFragment
import com.example.marketplace.fragments.categories.ChairFragment
import com.example.marketplace.fragments.categories.FurnitureFragment
import com.example.marketplace.fragments.categories.MainCategoryFragment
import com.example.marketplace.fragments.categories.TableFragment
import com.example.newapptester1.R
import com.example.newapptester1.databinding.FragmentHomeBinding
import com.google.android.gms.common.api.Response
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment:Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var searchView: SearchView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var originalProducts: List<Product>

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

        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider) ?: return

        val findProductBtn = view.findViewById<Button>(R.id.findProductBtn)

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
        binding.scanBtn.setOnClickListener {
            val intent = Intent(requireContext(), ScannerActivity::class.java)
            startActivity(intent)
        }
        findProductBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
//        searchView = view.findViewById(R.id.searchView)
//        setupSearchView(searchView)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel("https://kuhnoteka.ru/wp-content/uploads/2017/10/0482.jpg", ""))
        imageList.add(
            SlideModel(
                "https://ekbkupe.ru/images/stories/virtuemart/product/2022-03-05_11-24-055.jpg",
                ""
            )
        )
//        imageList.add(
//            SlideModel(
//                "https://www.cucine.ru/upload/resize_cache/iblock/214/1200_878_16b651e4f962b08687c210ba0fcd27736/miton-cucine-limha-eurolux-1.jpg",
//                ""
//            )
//        )
        imageList.add(
            SlideModel(
                "https://dizainexpert.ru/wp-content/uploads/2020/07/kuhnya-haj-tek.jpg",
                ""
            )
        )
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome){tab, position ->
            when(position){
                    0->tab.text= "Главное"
                1-> tab.text = "Кухонные оборудования"
                2-> tab.text = "Пекарня"
                3-> tab.text = "Комплектующие"
                4-> tab.text = "Холодильное"
            }
        }.attach()
        setUpTabLayout()


    }
    private fun setUpTabLayout(){
        for(i in 0..4){
            val textView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_title,null)
                as TextView
            binding.tabLayout.getTabAt(i)?.customView = textView
        }
    }
        private fun setupSearchView(searchView: SearchView) {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterProducts(newText.orEmpty())
                    return true
                }
            })
        }

        private fun filterProducts(query: String) {
            val filteredList = originalProducts.filter { product ->
                product.name.contains(query, ignoreCase = true)
            }
            productAdapter.updateProductList(filteredList as MutableList<Product>)
        }
}