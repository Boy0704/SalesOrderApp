package com.apps.salesorder.ui.so.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.apps.salesorder.R
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.*
import com.apps.salesorder.data.model.*
import com.apps.salesorder.databinding.ActivityAddDetailBinding
import com.apps.salesorder.databinding.ActivitySoHeaderBinding
import com.apps.salesorder.ui.kendaraan.fragment.CariBranchFragment
import com.apps.salesorder.ui.kendaraan.fragment.CariItemFragment
import com.apps.salesorder.ui.kendaraan.fragment.CariItemUOMFragment
import es.dmoral.toasty.Toasty

class AddDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddDetailBinding.inflate(layoutInflater) }
    private lateinit var database: SoDB
    private lateinit var SoHeaderDao: SoHeaderDao
    private lateinit var DebtorDao: DebtorDao
    private lateinit var ItemDao: ItemDao
    private lateinit var ItemPriceDao: ItemPriceDao
    private lateinit var ItemUOMDao: ItemUOMDao
    private lateinit var TaxDao: TaxDao
    private lateinit var SoDetailDao: SoDetailDao
    var codeBarcode: String = ""
    var soNo: String = ""
    var accNo: String = ""
    var itemCode: String = ""
    var satuanUOM: String = ""
    var itemUomPrice: Double = 0.0
    var unitPrice: Double = 0.0
    var discount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setuplistener()
    }

    private fun setupView() {
        database = SoDB.getDatabase(this)
        SoHeaderDao = database.getSoHeader()
        DebtorDao = database.getDebtorDao()
        ItemPriceDao = database.getItemPriceDao()
        ItemDao = database.getItemDao()
        ItemUOMDao = database.getItemUOMDao()
        TaxDao = database.getTaxDao()
        SoDetailDao = database.getSoDetail()

        codeBarcode = intent.getStringExtra("code_barcode").toString()
        soNo = intent.getStringExtra("so_no").toString()
        accNo = intent.getStringExtra("acc_no").toString()

        if (!codeBarcode.equals("null")){
            val dataItem = ItemDao.getAll(codeBarcode)
            if (dataItem.size > 0) {
                itemCode = codeBarcode
                binding.itemCode.setText(codeBarcode)
                binding.itemDesc.setText(dataItem.get(0).desc.toString())
            } else {
                Toasty.warning(this, "Item Code $codeBarcode tidak ditemukan !", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun setuplistener() {
        binding.itemCode.setOnClickListener {
            showDialogCariItem()
        }
        binding.satuanUom.setOnClickListener {
            showDialogCariItemUOM()
        }
        binding.scanCode.setOnClickListener {
            val intent = Intent(this, ScannerViewActivity::class.java)
            finish()
            startActivity(intent)
        }
        binding.simpan.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val qty = binding.qty.text.toString()
        if (qty.isEmpty()){
            Toasty.warning(this, "Qty tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if(satuanUOM.isEmpty()){
            Toasty.warning(this, "Satuan UOM tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {
            val checkPriceCategory = DebtorDao.checkPriceCategory(accNo)
            if (checkPriceCategory > 0){
                val checkItemPrice = ItemPriceDao.checkItemPrice(itemCode, satuanUOM)
                if (checkItemPrice > 0) {
                    val getItemPrice = ItemPriceDao.getItemPrice(itemCode, satuanUOM)
                    unitPrice = getItemPrice.get(0).fixedPrice!!.toDouble()
                    discount = getItemPrice.get(0).fixedDetailDiscount!!.toDouble()
                } else {
                    unitPrice = itemUomPrice
                }
            } else {
                unitPrice = itemUomPrice
            }

            val getDebtor = DebtorDao.getByAccNo(accNo)
            val subtotal = ( qty.toInt() * unitPrice ) - discount
            val ppn_code = getDebtor.get(0).taxType.toString()
            val ppn_rate = TaxDao.getAll(ppn_code).get(0).taxRate.toString()
            val ppn_amount = ppn_rate.toDouble().toInt() * subtotal / 100


            SoDetailDao.insert(
                SoDetail(
                    soNo = soNo,
                    itemCode = itemCode,
                    qty = qty.toInt(),
                    uom = satuanUOM,
                    unitPrice = unitPrice,
                    discount = discount.toInt(),
                    subtotal = subtotal,
                    ppnCode = ppn_code,
                    ppnRate = ppn_rate.toDouble().toInt(),
                    ppnAmount = ppn_amount,
                    projNo = ""
                )
            )

            Toasty.success(this, "Order Detail berhasil disimpan !", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, OrderDetailActivity::class.java)
            intent.putExtra("so_no", soNo)
            intent.putExtra("acc_no", accNo)
            finish()
            startActivity(intent)
        }


    }

    private fun showDialogCariItem() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val newFragment = CariItemFragment()
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        val mBundle = Bundle()
        newFragment.arguments = mBundle

        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()

    }

    private fun showDialogCariItemUOM() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val newFragment = CariItemUOMFragment()
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        val mBundle = Bundle()
        mBundle.putString("item_code", itemCode)
        newFragment.arguments = mBundle

        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()

    }

    fun setDataFromDialogCariItem(result: Item){
        itemCode = result.itemCode
        binding.itemCode.setText("${result.itemCode}")
        binding.itemDesc.setText("${result.desc}")
    }

    fun setDataFromDialogCariUOM(result: ItemUom){
        satuanUOM = result.UOM.toString()
        itemUomPrice = result.price!!.toString().toDouble()
        binding.satuanUom.setText("${result.UOM.toString()}")
    }

     fun setQRCODE(result: String){
        binding.itemCode.setText(result)
    }

}