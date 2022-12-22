package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.Tax

@Dao
interface TaxDao {
    @Insert
    fun insert(tax: Tax)

    @Update
    fun update(tax: Tax)

    @Delete
    fun delete(tax: Tax)

    @Query("SELECT * FROM tax")
    fun getAll() : List<Tax>

    @Query("SELECT * FROM tax WHERE TaxType = :taxType")
    fun getAll(taxType: String) : List<Tax>

    @Query("DELETE FROM tax")
    fun deleteAll()

}