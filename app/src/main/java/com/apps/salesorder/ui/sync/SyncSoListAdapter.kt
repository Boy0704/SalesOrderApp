package com.apps.driverasrikatara.ui.kendaraan.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.apps.salesorder.R
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.CompanySettingDao
import com.apps.salesorder.data.db.dao.ItemDao
import com.apps.salesorder.data.db.dao.SoDetailDao
import com.apps.salesorder.data.db.dao.SoHeaderDao
import com.apps.salesorder.data.model.*
import com.apps.salesorder.databinding.ItemListDuaBinding
import com.apps.salesorder.databinding.ItemListSoBinding
import com.apps.salesorder.databinding.ItemListSoSyncBinding
import com.apps.salesorder.helper.Utils
import com.apps.salesorder.ui.so.detail.OrderDetailActivity
import com.apps.salesorder.ui.so.pdf.PDFConverter
import com.apps.salesorder.ui.so.pdf.XmlToPDFConverter
import com.tapadoo.alerter.Alerter
import es.dmoral.toasty.Toasty
import timber.log.Timber

class SyncSoListAdapter(
    val listData: ArrayList<SoHeader>,
    val context: Context,
    val activity: Activity,
    var listener: OnAdapterListener
): RecyclerView.Adapter<SyncSoListAdapter.ViewHolder>(), Filterable {

    private var dataFilter = ArrayList<SoHeader>()
    private lateinit var database: SoDB
    private lateinit var SoDetailDao: SoDetailDao
    private lateinit var SoHeaderDao: SoHeaderDao
    private lateinit var CompanySettingDao: CompanySettingDao
    private var isCheckAll: Boolean = false

    init {
        dataFilter = listData
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder (
        ItemListSoSyncBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = dataFilter.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        database = SoDB.getDatabase(context)
        SoDetailDao = database.getSoDetail()
        SoHeaderDao = database.getSoHeader()
        CompanySettingDao = database.getCompanySetting()

        val list = dataFilter[position]

        /* tidak di pakai */

//        if (list.status.equals("sync")){
//            holder.binding.soNoLast.visibility = View.VISIBLE
//            holder.binding.soNo.visibility = View.GONE
//            holder.binding.soNoLast.text = list.soNo
//        } else {
//            holder.binding.soNo.text = list.soNo
//        }

        holder.binding.companyName.text = list.companyName
        holder.binding.tanggal.text = list.date
        holder.binding.status.text = list.status
        if (list.status.equals("draft")){
            holder.binding.status.setTextColor(Color.RED)
        } else {
            holder.binding.status.setTextColor(Color.GREEN)
        }
        holder.binding.ppn.text = Utils.NUMBER.currencyFormat(if (list.ppn?.isEmpty() == true) "0" else list.ppn.toString())
        holder.binding.subtotal.text = Utils.NUMBER.currencyFormat(if (list.subTotal?.isEmpty() == true) "0" else list.subTotal.toString())
        holder.binding.total.text = Utils.NUMBER.currencyFormat(if (list.total?.isEmpty() == true) "0" else list.total.toString())

        if (isCheckAll){
            holder.binding.soNo.isChecked = true
            SoHeaderDao.updateCheckedAll("ya")
            SoDetailDao.updateCheckedAll("ya")
        } else {
            holder.binding.soNo.isChecked = false
            SoHeaderDao.updateCheckedAll("tidak")
            SoDetailDao.updateCheckedAll("tidak")
        }

        holder.binding.btnDetail.setOnClickListener {
            val intent = Intent(holder.binding.mainLayout.context, OrderDetailActivity::class.java)
            intent.putExtra("so_no", list.soNo)
            intent.putExtra("acc_no", list.accNo)
            holder.binding.mainLayout.context.startActivity(intent)
        }

        holder.binding.soNo.setOnCheckedChangeListener { _, b ->
            if (b){
                SoHeaderDao.updateCheckedHeader("ya",list.soNo.toString())
                SoDetailDao.updateCheckedDetail("ya",list.soNo.toString())
            } else {
                SoHeaderDao.updateCheckedHeader("tidak",list.soNo.toString())
                SoDetailDao.updateCheckedDetail("tidak",list.soNo.toString())
            }
        }

        holder.binding.btnHapus.setOnClickListener {
            Alerter.create(activity)
                .setTitle("Alert")
                .setText("Yakin akan hapus data ini ?")
                .addButton("Oke", R.style.AlertButton, View.OnClickListener {
                    SoHeaderDao.deleteBySoNo(list.soNo.toString())
                    SoDetailDao.deleteBySoNo(list.soNo.toString())
                    notifyDataSetChanged()
                    Toasty.success(context, "Data SO berhasil dihapus !", Toast.LENGTH_SHORT).show()

                })
                .addButton("No", R.style.AlertButton, View.OnClickListener {

                })
                .show()
        }

        holder.binding.btnCetak.setOnClickListener {
            if(list.status.equals("draft")){
                Toasty.warning(context, "silahkan submit SO dahulu !", Toast.LENGTH_SHORT).show()
            } else {
                //val pdfConverter = PDFConverter()
                val xmlToPDFConverter = XmlToPDFConverter()
                val listItemsSoDetail = arrayListOf<SoDetail>()
                val setting = arrayListOf<CompanySetting>()
                listItemsSoDetail.addAll(SoDetailDao.getBySoNo(list.soNo.toString()))
                setting.addAll(CompanySettingDao.getAll())
                //pdfConverter.createPdf(context, list, listItemsSoDetail, activity)
                xmlToPDFConverter.createPdf(context, list, listItemsSoDetail, setting)
            }

        }
        holder.binding.btnShare.visibility = View.GONE
        holder.binding.btnShare.setOnClickListener {

        }

        holder.binding.mainLayout.setOnClickListener {
            if (holder.binding.detail.visibility.equals(View.GONE)) {
                holder.binding.detail.visibility = View.VISIBLE
            } else {
                holder.binding.detail.visibility = View.GONE
            }
            listener.onClick( list )
        }

    }

    public fun checkAll(value: Boolean){
        isCheckAll = value
        Timber.e("CHECK ALL SO SYNC : $isCheckAll");
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemListSoSyncBinding): RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(result: SoHeader)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Timber.d("charSearch: $charSearch")
                if (charSearch.isEmpty()) {
                    dataFilter = listData
                } else {
                    val dataFiltered = ArrayList<SoHeader>()
                    for (datas in listData) {
                        if ( datas.companyName!!.toLowerCase().contains(charSearch.toLowerCase()) ) {
                            dataFiltered.add(datas)
                        }
                    }
                    dataFilter = dataFiltered
                }
                val dataFilteredResult = FilterResults()
                dataFilteredResult.values = dataFilter
                return dataFilteredResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataFilter = results?.values as ArrayList<SoHeader>
                notifyDataSetChanged()
            }

        }
    }

}