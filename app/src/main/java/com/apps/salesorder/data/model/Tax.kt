package com.apps.salesorder.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tax")
@Parcelize
data class Tax(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "TaxType") var taxType: String? = "",
    @ColumnInfo(name = "TaxRate") var taxRate: Double? = 0.0
) :Parcelable{

}
