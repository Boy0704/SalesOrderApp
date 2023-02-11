package com.apps.salesorder.data.db.dao

import androidx.room.*
import com.apps.salesorder.data.model.Debtor
import com.apps.salesorder.data.model.SoHeader

@Dao
interface SoHeaderDao {
    @Insert
    fun insert(sh: SoHeader)

    @Update
    fun update(sh: SoHeader)

    @Delete
    fun delete(sh: SoHeader)

    @Query("UPDATE so_header SET subtotal=:subtotal, taxable_amount=:taxableAmount, ppn=:ppn, currency_code=:currencyCode, rate=:rate, local_total=:localTotal, total=:total, status=:status WHERE so_no = :soNo")
    fun updateHeader(soNo: String,
                     subtotal: String,
                     taxableAmount: String,
                     ppn: String,
                     currencyCode: String,
                     rate: String,
                     localTotal: String,
                     total: String,
                     status: String
    )

    @Query("UPDATE so_header SET checked=:checked WHERE so_no = :soNo")
    fun updateCheckedHeader(checked: String,soNo: String)

    @Query("UPDATE so_header SET checked=:value, status=:value WHERE checked = :checked")
    fun updateCheckedHeaderAll(checked: String,value: String)

    @Query("SELECT * FROM so_header")
    fun getAll() : List<SoHeader>

    @Query("SELECT count(so_no) FROM so_header")
    fun getCount() : Int

    @Query("SELECT * FROM so_header WHERE so_no = :soNo")
    fun getBySoNo(soNo: String) : List<SoHeader>

    @Query("SELECT * FROM so_header WHERE checked = :checked")
    fun getByChecked(checked: String) : List<SoHeader>

    @Query("SELECT * FROM so_header WHERE checked != :checked")
    fun getNotSync(checked: String) : List<SoHeader>

    @Query("DELETE FROM so_header")
    fun deleteAll()

    @Query("DELETE FROM so_header WHERE so_no = :soNo ")
    fun deleteBySoNo(soNo: String)

}