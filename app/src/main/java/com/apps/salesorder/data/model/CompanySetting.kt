package com.apps.salesorder.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "companySetting")
@Parcelize
data class CompanySetting(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "company_id") var company_id: String? = "",
    @ColumnInfo(name = "company_name") var company_name: String? = "",
    @ColumnInfo(name = "alamat") var alamat: String? = "",
    @ColumnInfo(name = "logo") var logo: String? = "",
    @ColumnInfo(name = "format_so") var format_so: String? = "",
    @ColumnInfo(name = "next_so") var next_so: String? = ""
) :Parcelable{

}
