package com.apps.salesorder.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "so_header")
@Parcelize
data class SoHeader(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "so_no") var soNo: String? = "",
    @ColumnInfo(name = "acc_no") var accNo: String? = "",
    @ColumnInfo(name = "company_name") var companyName: String? = "",
    @ColumnInfo(name = "delivery1") var delivery1: String? = "",
    @ColumnInfo(name = "delivery2") var delivery2: String? = "",
    @ColumnInfo(name = "delivery3") var delivery3: String? = "",
    @ColumnInfo(name = "delivery4") var delivery4: String? = "",
    @ColumnInfo(name = "branch") var branch: String? = "",
    @ColumnInfo(name = "date") var date: String? = "",
    @ColumnInfo(name = "sales_agent") var salesAgent: String? = "",
    @ColumnInfo(name = "ref_doc") var refDoc: String? = "",
    @ColumnInfo(name = "subtotal") var subTotal: String? = "",
    @ColumnInfo(name = "taxable_amount") var taxableAmount: String? = "",
    @ColumnInfo(name = "ppn") var ppn: String? = "",
    @ColumnInfo(name = "currency_code") var currencyCode: String? = "",
    @ColumnInfo(name = "rate") var rate: String? = "",
    @ColumnInfo(name = "local_total") var localTotal: String? = "",
    @ColumnInfo(name = "total") var total: String? = "",
    @ColumnInfo(name = "status") var status: String? = "",
    @ColumnInfo(name = "created_at") var createdAt: String? = "",
    @ColumnInfo(name = "created_by") var createdBy: String? = "",
    @ColumnInfo(name = "updated_at") var updatedAt: String? = "",
    @ColumnInfo(name = "updated_by") var updatedBy: String? = ""
) :Parcelable{

}
