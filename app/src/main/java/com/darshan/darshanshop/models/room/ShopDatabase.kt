package com.darshan.darshanshop.models.room

import android.content.Context
import androidx.room.*
import com.darshan.darshanshop.models.pojo.Bid
import com.darshan.darshanshop.models.pojo.Product
import com.darshan.darshanshop.utils.CommonMethods
import com.darshan.darshanshop.utils.Constants



@Database(entities = arrayOf(Product::class, Bid :: class), version = 1, exportSchema = false)
abstract class ShopDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun bidDao(): BidDao

    companion object {
        private var INSTANCE: ShopDatabase? = null

        fun getInstance(context: Context): ShopDatabase? {
            if (INSTANCE == null) {
                CommonMethods.showLog("product_insert","created table")
                synchronized(ShopDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context,
                        ShopDatabase::class.java,
                        Constants.DB_NAME)
                        .build()
                }
            }
            return INSTANCE
        }
    }

}