package com.apps.salesorder.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "item")
@Parcelize
data class Item(
    @PrimaryKey(autoGenerate = false)@ColumnInfo(name = "ItemCode") var itemCode: String = "",
    @ColumnInfo(name = "DocKey") var docKey: String? = "",
    @ColumnInfo(name = "Description") var desc: String? = ""
) :Parcelable{

}
