package com.apps.salesorder.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "branch")
@Parcelize
data class Branch(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "AccNo") var accNo: String = "",
    @ColumnInfo(name = "BranchCode") var branchCode: String = "",
    @ColumnInfo(name = "BranchName") var branchName: String = ""
) :Parcelable{

}
