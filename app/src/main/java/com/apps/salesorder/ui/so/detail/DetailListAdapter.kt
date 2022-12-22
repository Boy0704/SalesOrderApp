package com.apps.driverasrikatara.ui.kendaraan.fragment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apps.salesorder.R
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.ItemDao
import com.apps.salesorder.data.db.dao.SoDetailDao
import com.apps.salesorder.data.db.dao.SoHeaderDao
import com.apps.salesorder.data.model.SoDetail
import com.apps.salesorder.databinding.ItemListDetailBinding
import com.apps.salesorder.helper.Utils
import com.apps.salesorder.ui.so.detail.OrderDetailActivity
import com.tapadoo.alerter.Alerter
import es.dmoral.toasty.Toasty

class DetailListAdapter(
    val listData: ArrayList<SoDetail>,
    val context: Context,
    val activity: OrderDetailActivity,
    var listener: OnAdapterListener
): RecyclerView.Adapter<DetailListAdapter.ViewHolder>() {

    private lateinit var database: SoDB
    private lateinit var ItemDao: ItemDao
    private lateinit var SoDetailDao: SoDetailDao
    private lateinit var SoHeaderDao: SoHeaderDao

    private var dataFilter = ArrayList<SoDetail>()

    init {
        dataFilter = listData
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder (
        ItemListDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = dataFilter.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = dataFilter[position]

        database = SoDB.getDatabase(context)
        ItemDao = database.getItemDao()
        SoDetailDao = database.getSoDetail()
        SoHeaderDao = database.getSoHeader()

        val getItem = ItemDao.getAll(list.itemCode.toString())
        val getSoHeader = SoHeaderDao.getBySoNo(list.soNo.toString())
        val no = position + 1
        holder.binding.no.text = "#" +no.toString()
        holder.binding.itemCode.text = list.itemCode
        holder.binding.itemDesc.text = getItem.get(0).desc
        holder.binding.qty.text = list.qty.toString()
        holder.binding.uomSatuan.text = list.uom
        holder.binding.unitPrice.text = Utils.NUMBER.currencyFormat(list.unitPrice.toString())
        holder.binding.discount.text = list.discount.toString()
        holder.binding.subtotal.text = Utils.NUMBER.currencyFormat(list.subtotal.toString())
        holder.binding.ppnCode.text = list.ppnCode.toString()
        holder.binding.ppnRate.text = list.ppnRate.toString()
        holder.binding.ppnAmount.text = Utils.NUMBER.currencyFormat(list.ppnAmount.toString())
        holder.binding.projNo.text = list.projNo.toString()
//        holder.binding.ppn.text = if (list.ppn?.isEmpty() == true) "0" else list.ppn

        holder.binding.hapusItem.setOnClickListener {
            Alerter.create(activity)
                .setTitle("Alert")
                .setText("Yakin akan hapus data ini ?")
                .addButton("Oke", R.style.AlertButton, View.OnClickListener {
                    SoDetailDao.delete(list)
                    Toasty.success(context, "Order Detail berhasil dihapus !", Toast.LENGTH_SHORT).show()
                    val intent = Intent(holder.binding.mainLayout.context, OrderDetailActivity::class.java)
                    intent.putExtra("so_no", list.soNo)
                    intent.putExtra("acc_no", getSoHeader.get(0).accNo)
                    holder.binding.mainLayout.context.startActivity(intent)
                })
                .addButton("No", R.style.AlertButton, View.OnClickListener {

                })
                .show()


        }

    }

    class ViewHolder(val binding: ItemListDetailBinding): RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(result: SoDetail)
    }
    

}