package com.apps.driverasrikatara.ui.kendaraan.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.ItemDao
import com.apps.salesorder.data.db.dao.SoDetailDao
import com.apps.salesorder.data.db.dao.SoHeaderDao
import com.apps.salesorder.data.model.SoDetail
import com.apps.salesorder.databinding.ItemCetakPdfBinding
import com.apps.salesorder.databinding.ItemListDetailBinding
import com.apps.salesorder.helper.Utils

class DetailPdfAdapter(
    val listData: ArrayList<SoDetail>,
    val context: Context,
): RecyclerView.Adapter<DetailPdfAdapter.ViewHolder>() {

    private lateinit var database: SoDB
    private lateinit var ItemDao: ItemDao
    private lateinit var SoDetailDao: SoDetailDao

    private var dataFilter = ArrayList<SoDetail>()

    init {
        dataFilter = listData
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder (
        //ItemListDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ItemCetakPdfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = dataFilter.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = dataFilter[position]

        database = SoDB.getDatabase(context)
        ItemDao = database.getItemDao()
        SoDetailDao = database.getSoDetail()

        val getItem = ItemDao.getAll(list.itemCode.toString())
        val no = position + 1
//        holder.binding.no.text = "#" +no.toString()
//        holder.binding.itemCode.text = list.itemCode
//        holder.binding.itemDesc.text = getItem.get(0).desc
//        holder.binding.qty.text = list.qty.toString()
//        holder.binding.uomSatuan.text = list.uom
//        holder.binding.unitPrice.text = Utils.NUMBER.currencyFormat(list.unitPrice.toString())
//        holder.binding.discount.text = list.discount.toString()
//        holder.binding.subtotal.text = Utils.NUMBER.currencyFormat(list.subtotal.toString())
//        holder.binding.ppnCode.text = list.ppnCode.toString()
//        holder.binding.ppnRate.text = list.ppnRate.toString()
//        holder.binding.ppnAmount.text = Utils.NUMBER.currencyFormat(list.ppnAmount.toString())
//        holder.binding.projNo.text = list.projNo.toString()
//
//        holder.binding.hapusItem.visibility = View.GONE

        holder.binding.desc.text = getItem.get(0).desc + " - "+list.uom
        holder.binding.qty.text = list.qty.toString()
        holder.binding.harga.text = Utils.NUMBER.currencyFormat(list.unitPrice.toString())
        holder.binding.diskonItem.text = Utils.NUMBER.currencyFormat(list.discount.toString())
        holder.binding.totalHarga.text = Utils.NUMBER.currencyFormat(list.subtotal.toString())

    }

    class ViewHolder(val binding: ItemCetakPdfBinding): RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(result: SoDetail)
    }
    

}