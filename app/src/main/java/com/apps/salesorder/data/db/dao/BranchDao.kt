package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.Branch

@Dao
interface BranchDao {
    @Insert
    fun insert(branch: Branch)

    @Update
    fun update(branch: Branch)

    @Delete
    fun delete(branch: Branch)

    @Query("SELECT * FROM branch")
    fun getAll() : List<Branch>

    @Query("SELECT * FROM branch WHERE AccNo = :accNo")
    fun getByAccNo(accNo: String) : List<Branch>

    @Query("DELETE FROM branch")
    fun deleteAll()

}