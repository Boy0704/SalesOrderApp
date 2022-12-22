package com.apps.salesorder.ui.so.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.apps.driverasrikatara.ui.kendaraan.fragment.DetailListAdapter
import com.apps.salesorder.R
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.CurrencyDao
import com.apps.salesorder.data.db.dao.DebtorDao
import com.apps.salesorder.data.db.dao.SoDetailDao
import com.apps.salesorder.data.db.dao.SoHeaderDao
import com.apps.salesorder.data.model.SoDetail
import com.apps.salesorder.data.model.SoHeader
import com.apps.salesorder.databinding.ActivityOrderDetailBinding
import com.apps.salesorder.helper.Utils
import com.apps.salesorder.ui.home.HomeActivity
import es.dmoral.toasty.Toasty
import timber.log.Timber
import kotlin.collections.ArrayList

class OrderDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOrderDetailBinding.inflate(layoutInflater) }
    private lateinit var dataAdapter: DetailListAdapter
    private lateinit var database: SoDB
    private lateinit var SoHeaderDao: SoHeaderDao
    private lateinit var SoDetailDao: SoDetailDao
    private lateinit var DebtorDao: DebtorDao
    private lateinit var CurrencyDao: CurrencyDao
    var soNo: String = ""
    var accNo: String = ""
    var allQty: Int = 0
    var allSubtotal: Double = 0.0
    var allPPNAmount: Double = 0.0
    var currencyCode: String = ""
    var rate: String = ""
    var ppn: Double = 0.0
    var total: Double = 0.0
    var localTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupview()
        setuplisten()
    }

    private fun setupview(){
        database = SoDB.getDatabase(this)
        SoHeaderDao = database.getSoHeader()
        SoDetailDao = database.getSoDetail()
        DebtorDao = database.getDebtorDao()
        CurrencyDao = database.getCurrencyDao()

        soNo = intent.getStringExtra("so_no").toString()
        accNo = intent.getStringExtra("acc_no").toString()
        val header: List<SoHeader> = SoHeaderDao.getBySoNo(soNo)


        binding.companyCode.setText(header.get(0).accNo)
        binding.companyName.setText(header.get(0).companyName)
        binding.soNo.setText(header.get(0).soNo)
        binding.status.setText(header.get(0).status)
        if (header.get(0).status.equals("draft")){
            binding.status.setTextColor(getResources().getColor(R.color.orange))
        } else {
            binding.status.setTextColor(getResources().getColor(R.color.green))
        }

        val listItems = arrayListOf<SoDetail>()
        listItems.addAll(SoDetailDao.getBySoNo(soNo))

        Timber.e("[DATA LIST-ITEM] ${listItems.size}")


        currencyCode = DebtorDao.getByAccNo(accNo).get(0).currencyCode.toString()
        rate = CurrencyDao.getByCurrencyCode(currencyCode).get(0).bankSellRate


        listItems.forEach({
            allQty += it.qty!!
            allSubtotal += it.subtotal!!
            allPPNAmount += it.ppnAmount!!
        })
        ppn = allPPNAmount * rate.toDouble().toInt()
        total = allSubtotal + allPPNAmount
        localTotal = total * rate.toDouble().toInt()


        setupAdapter(listItems)
        binding.allQty.setText(allQty.toString())
        binding.allSubtotal.setText(Utils.NUMBER.currencyFormat(allSubtotal.toString()))
        binding.allPpnAmount.setText(Utils.NUMBER.currencyFormat(allPPNAmount.toString()))
        binding.subtotalEx.setText(Utils.NUMBER.currencyFormat(allSubtotal.toString()))
        binding.taxableAmount.setText(Utils.NUMBER.currencyFormat(allSubtotal.toString()))
        binding.ppn.setText(Utils.NUMBER.currencyFormat(ppn.toString()))
        binding.localTotal.setText(Utils.NUMBER.currencyFormat(localTotal.toString()))
        binding.total.setText(Utils.NUMBER.currencyFormat(total.toString()))
    }

    private fun setuplisten() {
        binding.addDetail.setOnClickListener {
            val intent = Intent(this, AddDetailActivity::class.java)
            intent.putExtra("so_no", soNo)
            intent.putExtra("acc_no", accNo)
            startActivity(intent)
        }
        binding.submit.setOnClickListener {
            submit()
        }
    }

    private fun submit(){
        SoHeaderDao.updateHeader(
            soNo,
            subtotal = allSubtotal.toString(),
            taxableAmount = allSubtotal.toString(),
            ppn = ppn.toString(),
            currencyCode = currencyCode,
            rate = rate,
            localTotal = localTotal.toString(),
            total = total.toString(),
            status = "submit"
        )
        Toasty.success(this, "SO berhasil di submit", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun setupAdapter(listItem: ArrayList<SoDetail>) {

        dataAdapter = DetailListAdapter(listItem, this , this@OrderDetailActivity ,object : DetailListAdapter.OnAdapterListener{
            override fun onClick(result: SoDetail) {

            }
        })

        binding.rvData.apply {
            layoutManager = GridLayoutManager(context,1)
            adapter = dataAdapter
        }

    }

}