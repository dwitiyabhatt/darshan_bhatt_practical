package com.naveentp.todo.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.darshan.darshanshop.models.pojo.Bid
import com.darshan.darshanshop.models.pojo.Product
import com.darshan.darshanshop.models.room.ProductDao
import com.darshan.darshanshop.models.room.ShopDatabase
import com.darshan.darshanshop.utils.CommonMethods

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductRepository(application: Application) {

    private val productDao: ProductDao
    private val allProducts: LiveData<List<Product>>

    init {
        val database = ShopDatabase.getInstance(application.applicationContext)
        productDao = database!!.productDao()
        allProducts = productDao.getAllProductList()
    }

    fun saveproduct(product: Product) = runBlocking {
        this.launch(Dispatchers.IO) {
            productDao.saveProduct(product)
        }
        CommonMethods.showLog("product_insert","2")
    }

    fun updateproduct(product: Product) = runBlocking {
        this.launch(Dispatchers.IO) {
            productDao.updateProduct(product)
        }
    }

    fun getProduct(productId: String): LiveData<Product> {
        return productDao.getProduct(productId)
    }

    fun getAllProductsList(): LiveData<List<Product>> {
        return allProducts
    }

    fun getAllBidList(productId : String): LiveData<List<Bid>> {
        return productDao.getAllBidsForProduct(productId)
    }


}