package com.example.marketplace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marketplace.data.Product
import com.google.firebase.firestore.FirebaseFirestore

class AllProductsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    fun getAllProducts(): LiveData<List<Product>> {
        val productsLiveData = MutableLiveData<List<Product>>()

        firestore.collection("Products").get()
            .addOnSuccessListener { result ->
                val productList = mutableListOf<Product>()
                for (document in result) {
                    val product = document.toObject(Product::class.java)
                    productList.add(product)
                }
                productsLiveData.value = productList
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }

        return productsLiveData
    }
}