package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.ItemPrice

@Dao
interface ItemPriceDao {
    @Insert
    fun insert(itemPrice: ItemPrice)

    @Update
    fun update(itemPrice: ItemPrice)

    @Delete
    fun delete(itemPrice: ItemPrice)

    @Query("SELECT * FROM item_price")
    fun getAll() : List<ItemPrice>

    @Query("SELECT COUNT(ItemCode) FROM item_price WHERE ItemCode = :itemCode and  UOM = :uom ")
    fun checkItemPrice(itemCode: String, uom: String) : Int

    @Query("SELECT * FROM item_price WHERE ItemCode = :itemCode and  UOM = :uom ")
    fun getItemPrice(itemCode: String, uom: String) : List<ItemPrice>

    @Query("DELETE FROM item_price")
    fun deleteAll()

}