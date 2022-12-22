package com.apps.salesorder.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "item_price")
@Parcelize
data class ItemPrice(
    @PrimaryKey(autoGenerate = false)@ColumnInfo(name = "ItemPriceKey") var itemPriceKey: String = "",
    @ColumnInfo(name = "ItemCode") var itemCode: String? = "",
    @ColumnInfo(name = "UOM") var UOM: String? = "",
    @ColumnInfo(name = "PriceCategory") var priceCategory: String? = "",
    @ColumnInfo(name = "AccNo") var accNo: String? = "",
    @ColumnInfo(name = "FixedPrice") var fixedPrice: Double? = 0.0,
    @ColumnInfo(name = "FixedDetailDiscount") var fixedDetailDiscount: Double? = 0.0
) :Parcelable{

}
