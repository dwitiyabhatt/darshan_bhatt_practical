package com.darshan.darshanshop.models.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.darshan.darshanshop.models.pojo.Bid
import com.darshan.darshanshop.models.pojo.Product
import com.darshan.darshanshop.utils.CommonMethods
import com.naveentp.todo.data.BidRepository
import com.naveentp.todo.data.ProductRepository


class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository = ProductRepository(application)
    private val bidRepository: BidRepository = BidRepository(application)
    private val allProductsList: LiveData<List<Product>> = repository.getAllProductsList()

    fun saveTodo(product: Product) {
        CommonMethods.showLog("product_insert","1")
        repository.saveproduct(product)
    }

    fun getProduct(productId: String) : LiveData<Product> {
        CommonMethods.showLog("product_queried "+productId,"1")
        return repository.getProduct(productId)
    }

    fun updateTodo(product: Product){
        repository.updateproduct(product)
    }

    fun getAllProductsList(): LiveData<List<Product>> {
        return allProductsList
    }

    fun saveBid(bid: Bid) {
        bidRepository.saveBid(bid)
    }

    fun getBidListing(productId: String) : LiveData<List<Bid>> {
        return repository.getAllBidList(productId)
    }

}
