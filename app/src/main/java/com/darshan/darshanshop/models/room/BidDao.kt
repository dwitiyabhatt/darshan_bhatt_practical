package com.darshan.darshanshop.models.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.darshan.darshanshop.models.pojo.Bid
import com.darshan.darshanshop.models.pojo.Product

@Dao
interface BidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBid(bid: Bid)

    @Query("SELECT * FROM Bid WHERE product_id = :productId ORDER BY bid_id DESC")
    fun getAllBidsForProduct(productId : String): LiveData<List<Bid>>

}