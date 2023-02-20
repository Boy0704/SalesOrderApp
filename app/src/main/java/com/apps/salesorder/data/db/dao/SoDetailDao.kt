package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.SoDetail
import com.apps.salesorder.data.model.SoHeader

@Dao
interface SoDetailDao {
    @Insert
    fun insert(sd: SoDetail)

    @Update
    fun update(sd: SoDetail)

    @Delete
    fun delete(sd: SoDetail)

    @Query("UPDATE so_detail SET checked=:checked")
    fun updateCheckedAll(checked: String)

    @Query("UPDATE so_detail SET checked=:checked WHERE so_no = :soNo")
    fun updateCheckedDetail(checked: String,soNo: String)

    @Query("UPDATE so_detail SET checked=:value WHERE checked = :checked")
    fun updateCheckedDetailAll(checked: String,value: String)

    @Query("SELECT * FROM so_detail")
    fun getAll() : List<SoDetail>

    @Query("SELECT count(so_no) FROM so_detail")
    fun getCount() : Int

    @Query("SELECT * FROM so_detail WHERE so_no = :soNo")
    fun getBySoNo(soNo: String) : List<SoDetail>

    @Query("SELECT * FROM so_detail WHERE checked = :checked")
    fun getByChecked(checked: String) : List<SoDetail>

    @Query("DELETE FROM so_detail")
    fun deleteAll()

    @Query("DELETE FROM so_detail WHERE so_no = :soNo ")
    fun deleteBySoNo(soNo: String)

}