package com.apps.salesorder.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "item_uom")
@Parcelize
data class ItemUom(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "ItemCode") var itemCode: String = "",
    @ColumnInfo(name = "UOM") var UOM: String? = "",
    @ColumnInfo(name = "Rate") var rate: String? = "",
    @ColumnInfo(name = "Price") var price: String? = ""
) :Parcelable{

}
