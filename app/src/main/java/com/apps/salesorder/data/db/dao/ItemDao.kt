package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.Debtor
import com.apps.salesorder.data.model.Item

@Dao
interface ItemDao {
    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)

    @Query("SELECT * FROM item")
    fun getAll() : List<Item>

    @Query("SELECT * FROM item WHERE ItemCode = :itemCode")
    fun getAll(itemCode: String) : List<Item>

    @Query("DELETE FROM item")
    fun deleteAll()

}