package com.apps.salesorder.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "debtor")
@Parcelize
data class Debtor(
    @PrimaryKey(autoGenerate = false)@ColumnInfo(name = "AccNo") var accNo: String = "",
    @ColumnInfo(name = "CompanyName") var companyName: String? = "",
    @ColumnInfo(name = "Address1") var address1: String? = "",
    @ColumnInfo(name = "Address2") var address2: String? = "",
    @ColumnInfo(name = "Address3") var address3: String? = "",
    @ColumnInfo(name = "Address4") var address4: String? = "",
    @ColumnInfo(name = "DeliverAddr1") var deliverAddr1: String? = "",
    @ColumnInfo(name = "DeliverAddr2") var deliverAddr2: String? = "",
    @ColumnInfo(name = "DeliverAddr3") var deliverAddr3: String? = "",
    @ColumnInfo(name = "DeliverAddr4") var deliverAddr4: String? = "",
    @ColumnInfo(name = "DisplayTerm") var displayTerm: String? = "",
    @ColumnInfo(name = "SalesAgent") var salesAgent: String? = "",
    @ColumnInfo(name = "PriceCategory") var priceCategory: String? = "",
    @ColumnInfo(name = "CurrencyCode") var currencyCode: String? = "",
    @ColumnInfo(name = "TaxType") var taxType: String? = "",

) :Parcelable{

}
