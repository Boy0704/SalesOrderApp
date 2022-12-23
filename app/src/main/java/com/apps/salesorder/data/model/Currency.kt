package com.apps.salesorder.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "currency")
@Parcelize
data class Currency(
    @PrimaryKey(autoGenerate = false)@ColumnInfo(name = "CurrencyCode") var currencyCode: String = "",
    @ColumnInfo(name = "CurrencyWord") var currencyWord: String? = "",
    @ColumnInfo(name = "BankSellRate") var bankSellRate: String? = ""
) :Parcelable{

}
