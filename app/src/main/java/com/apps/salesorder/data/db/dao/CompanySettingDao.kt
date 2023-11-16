package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.CompanySetting

@Dao
interface CompanySettingDao {
    @Insert
    fun insert(compSet: CompanySetting)

    @Update
    fun update(compSet: CompanySetting)

    @Delete
    fun delete(compSet: CompanySetting)

    @Query("SELECT * FROM companySetting")
    fun getAll() : List<CompanySetting>

    @Query("SELECT * FROM companySetting WHERE company_id = :company_id")
    fun getByID(company_id: String) : List<CompanySetting>

    @Query("DELETE FROM companySetting")
    fun deleteAll()

}