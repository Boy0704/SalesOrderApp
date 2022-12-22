package com.apps.salesorder.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apps.salesorder.data.db.dao.*
import com.apps.salesorder.data.model.*

@Database(entities = [
    Branch::class,
    Currency::class,
    Debtor::class,
    SoHeader::class,
    SoDetail::class,
    Item::class,
    ItemUom::class,
    ItemPrice::class,
    Tax::class
                     ], version = 1, exportSchema = false )
abstract class SoDB : RoomDatabase() {

    companion object{
        @Volatile
        private var INSTANCE: SoDB? = null
        fun getDatabase(context: Context): SoDB {
            return INSTANCE ?: synchronized(this) {
                val instance =  Room.databaseBuilder(
                    context.applicationContext,
                    SoDB::class.java,
                    "sales_order_db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun getSoHeader(): SoHeaderDao
    abstract fun getSoDetail(): SoDetailDao
    abstract fun getBranchDao(): BranchDao
    abstract fun getDebtorDao(): DebtorDao
    abstract fun getCurrencyDao(): CurrencyDao
    abstract fun getItemDao(): ItemDao
    abstract fun getItemUOMDao(): ItemUOMDao
    abstract fun getItemPriceDao(): ItemPriceDao
    abstract fun getTaxDao(): TaxDao

}