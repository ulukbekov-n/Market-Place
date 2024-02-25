package com.example.marketplace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace.data.Product
import com.example.marketplace.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDealsProducts =
        MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealsProducts: StateFlow<Resource<List<Product>>> = _bestDealsProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    private val pagingInfo = PagingInfo()

    init {
        fetchSpecialProducts()
        fetchBestDeals()
        fetchBestProducts()


    }

    fun fetchSpecialProducts() {
        _specialProducts.value = Resource.Loading()
        firestore.collection("Products")
            .whereEqualTo("category", "Special products")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _specialProducts.value = Resource.Error(error.message.toString())
                    return@addSnapshotListener
                }

                val specialProductsList = value?.toObjects(Product::class.java) ?: emptyList()
                _specialProducts.value = Resource.Success(specialProductsList)
            }
    }

    fun fetchBestDeals() {
        _bestDealsProducts.value = Resource.Loading()
        firestore.collection("Products")
            .whereEqualTo("category", "Best Deals")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _bestDealsProducts.value = Resource.Error(error.message.toString())
                    return@addSnapshotListener
                }

                val bestDealsProducts = value?.toObjects(Product::class.java) ?: emptyList()
                _bestDealsProducts.value = Resource.Success(bestDealsProducts)
            }
    }

    fun fetchBestProducts() {
        if (!pagingInfo.isPagingEnd) {
            _bestProducts.value = Resource.Loading()
            firestore.collection("Products")
                .limit(pagingInfo.bestProductPage * 10)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        _bestProducts.value = Resource.Error(error.message.toString())
                        return@addSnapshotListener
                    }

                    val bestProducts = value?.toObjects(Product::class.java) ?: emptyList()
                    pagingInfo.isPagingEnd = bestProducts == pagingInfo.oldBestProducts
                    pagingInfo.oldBestProducts = bestProducts
                    _bestProducts.value = Resource.Success(bestProducts)
                    pagingInfo.bestProductPage++
                }
        }
    }

}

internal data class PagingInfo(
    var bestProductPage: Long = 1,
    var oldBestProducts:List<Product> = emptyList(),
    var isPagingEnd:Boolean = false
)
