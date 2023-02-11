package com.apps.salesorder.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "so_detail")
@Parcelize
data class SoDetail(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "so_no") var soNo: String? = "",
    @ColumnInfo(name = "item_code") var itemCode: String? = "",
    @ColumnInfo(name = "qty") var qty: Int? = 0,
    @ColumnInfo(name = "uom") var uom: String? = "",
    @ColumnInfo(name = "unit_price") var unitPrice: Double? = 0.0,
    @ColumnInfo(name = "discount") var discount: Int? = 0,
    @ColumnInfo(name = "subtotal") var subtotal: Double? = 0.0,
    @ColumnInfo(name = "ppn_code") var ppnCode: String? = "",
    @ColumnInfo(name = "ppn_rate") var ppnRate: Int? = 0,
    @ColumnInfo(name = "ppn_amount") var ppnAmount: Double? = 0.0,
    @ColumnInfo(name = "proj_no") var projNo: String? = "",
    @ColumnInfo(name = "checked") var checked: String? = ""
) :Parcelable{

}
