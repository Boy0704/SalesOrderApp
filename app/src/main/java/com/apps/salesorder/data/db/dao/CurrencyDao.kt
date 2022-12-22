package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.Branch
import com.apps.salesorder.data.model.Currency

@Dao
interface CurrencyDao {
    @Insert
    fun insert(currency: Currency)

    @Update
    fun update(currency: Currency)

    @Delete
    fun delete(currency: Currency)

    @Query("SELECT * FROM currency")
    fun getAll() : List<Currency>

    @Query("SELECT * FROM currency WHERE CurrencyCode = :currencyCode")
    fun getByCurrencyCode(currencyCode: String) : List<Currency>

    @Query("DELETE FROM currency")
    fun deleteAll()

}