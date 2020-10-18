package com.naveentp.todo.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.darshan.darshanshop.models.pojo.Bid
import com.darshan.darshanshop.models.pojo.Product
import com.darshan.darshanshop.models.room.BidDao
import com.darshan.darshanshop.models.room.ProductDao
import com.darshan.darshanshop.models.room.ShopDatabase
import com.darshan.darshanshop.utils.CommonMethods

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BidRepository(application: Application) {

    private val bidDao: BidDao
    init {
        val database = ShopDatabase.getInstance(application.applicationContext)
        bidDao = database!!.bidDao()
    }

    fun saveBid(bid: Bid) = runBlocking {
        this.launch(Dispatchers.IO) {
            bidDao.saveBid(bid)
        }

    }
    fun getAllBidList(productId : String): LiveData<List<Bid>> {
        return bidDao.getAllBidsForProduct(productId)
    }


}