package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.Debtor

@Dao
interface DebtorDao {
    @Insert
    fun insert(debtor: Debtor)

    @Update
    fun update(debtor: Debtor)

    @Delete
    fun delete(debtor: Debtor)

    @Query("SELECT * FROM debtor")
    fun getAll() : List<Debtor>

    @Query("SELECT * FROM debtor WHERE AccNo = :accNo")
    fun getByAccNo(accNo: String) : List<Debtor>

    @Query("SELECT CompanyName FROM debtor WHERE AccNo = :accNo")
    fun getCompName(accNo: String) : String

    @Query("SELECT COUNT(PriceCategory) FROM debtor WHERE PriceCategory!='' and AccNo = :accNo")
    fun checkPriceCategory(accNo: String) : Int

    @Query("DELETE FROM debtor")
    fun deleteAll()

}