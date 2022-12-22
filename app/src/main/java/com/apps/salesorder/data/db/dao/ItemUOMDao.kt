package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.Debtor
import com.apps.salesorder.data.model.Item
import com.apps.salesorder.data.model.ItemUom

@Dao
interface ItemUOMDao {
    @Insert
    fun insert(itemuom: ItemUom)

    @Update
    fun update(itemuom: ItemUom)

    @Delete
    fun delete(itemuom: ItemUom)

    @Query("SELECT * FROM item_uom")
    fun getAll() : List<ItemUom>

    @Query("SELECT * FROM item_uom WHERE ItemCode = :itemCode")
    fun getByItemCode(itemCode: String) : List<ItemUom>

    @Query("DELETE FROM item_uom")
    fun deleteAll()

}