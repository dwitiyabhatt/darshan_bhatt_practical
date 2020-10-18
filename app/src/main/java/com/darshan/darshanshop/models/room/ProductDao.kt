package com.darshan.darshanshop.models.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.darshan.darshanshop.models.pojo.Bid
import com.darshan.darshanshop.models.pojo.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT * FROM Product ORDER BY product_id DESC")
    fun getAllProductList(): LiveData<List<Product>>

    @Query("SELECT * FROM Product WHERE product_id = :productId")
    fun getProduct(productId : String): LiveData<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBid(bid: Bid)

    @Query("SELECT * FROM Bid WHERE product_id = :productId ORDER BY bid_id DESC")
    fun getAllBidsForProduct(productId : String): LiveData<List<Bid>>
}