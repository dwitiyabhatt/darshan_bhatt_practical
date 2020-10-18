package com.darshan.darshanshop.models.pojo

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "bid")
@Parcelize()
data class Bid(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bid_id")
    val bidId: Int?,

    @ColumnInfo(name = "product_id")
    var productId: Int,

    @ColumnInfo(name = "bid_price")
    var bidPrice: Double,

    @ColumnInfo(name = "bid_time_stamp")
    var bidTimeStamp: Long

) : Parcelable