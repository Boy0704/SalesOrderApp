package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.SoDetail

@Dao
interface SoDetailDao {
    @Insert
    fun insert(sd: SoDetail)

    @Update
    fun update(sd: SoDetail)

    @Delete
    fun delete(sd: SoDetail)

    @Query("SELECT * FROM so_detail")
    fun getAll() : List<SoDetail>

    @Query("SELECT count(so_no) FROM so_detail")
    fun getCount() : Int

    @Query("SELECT * FROM so_detail WHERE so_no = :soNo")
    fun getBySoNo(soNo: String) : List<SoDetail>

    @Query("DELETE FROM so_detail")
    fun deleteAll()

    @Query("DELETE FROM so_detail WHERE so_no = :soNo ")
    fun deleteBySoNo(soNo: String)

}