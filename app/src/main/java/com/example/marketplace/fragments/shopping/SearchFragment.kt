package com.example.marketplace.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace.adapters.ProductAdapter
import com.example.marketplace.data.Product
import com.example.marketplace.viewmodel.AllProductsViewModel
import com.example.newapptester1.R
import com.example.newapptester1.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val viewModel: AllProductsViewModel by viewModels()
    private lateinit var searchView:SearchView
    private var productList = listOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = binding.searchView
        recyclerView = binding.recyclerView
        productAdapter = ProductAdapter()

        recyclerView.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }
        productAdapter.setOnClickListener{ product ->
            val bundle = Bundle().apply {
                putParcelable("product", product)
            }
            findNavController().navigate(R.id.action_searchFragment_to_productDetailsFragment, bundle)
        }

        viewModel.getAllProducts().observe(viewLifecycleOwner) { products ->
            productList = products
            productAdapter.submitList(products)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    private fun filterList(query: String?) {
        query?.let { searchText ->
            val filteredList = productList.filter { product ->
                product.name.contains(searchText, ignoreCase = true)
            }
            productAdapter.submitList(filteredList)
        }

    }
}
