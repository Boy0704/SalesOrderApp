package com.apps.salesorder.ui.sync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apps.salesorder.R
import com.apps.salesorder.api.ApiService
import com.apps.salesorder.api.Resource
import com.apps.salesorder.data.constants.Constants
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.*
import com.apps.salesorder.data.model.*
import com.apps.salesorder.data.preferences.Preferences
import com.apps.salesorder.databinding.ActivityPartialSyncBinding
import com.apps.salesorder.helper.LoadingScreen
import com.apps.salesorder.ui.home.HomeViewModel
import com.apps.salesorder.ui.home.HomeViewModelFactory
import com.wessam.library.NetworkChecker
import com.wessam.library.NoInternetLayout
import es.dmoral.toasty.Toasty
import timber.log.Timber

class PartialSyncActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPartialSyncBinding.inflate(layoutInflater) }
    private val api by lazy { ApiService.getClient(this) }
    private lateinit var viewModelFactory : HomeViewModelFactory
    private lateinit var viewModel : HomeViewModel
    private val pref by lazy { Preferences(this) }

    private lateinit var database: SoDB
    private lateinit var BranchDao: BranchDao
    private lateinit var DebtorDao: DebtorDao
    private lateinit var CurrencyDao: CurrencyDao
    private lateinit var ItemDao: ItemDao
    private lateinit var ItemUOMDao: ItemUOMDao
    private lateinit var ItemPriceDao: ItemPriceDao
    private lateinit var TaxDao: TaxDao
    private var totalSync: Int = 0
    private var limitSync: Int = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = SoDB.getDatabase(this)
        BranchDao = database.getBranchDao()
        DebtorDao = database.getDebtorDao()
        CurrencyDao = database.getCurrencyDao()
        ItemDao = database.getItemDao()
        ItemUOMDao = database.getItemUOMDao()
        ItemPriceDao = database.getItemPriceDao()
        TaxDao = database.getTaxDao()

        viewModelFactory = HomeViewModelFactory( api, this )
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)


        binding.BtnSync.setOnClickListener {

            if(
                binding.check1.isChecked ||
                binding.check2.isChecked ||
                binding.check3.isChecked ||
                binding.check4.isChecked ||
                binding.check5.isChecked ||
                binding.check6.isChecked ||
                binding.check7.isChecked
            ) {

                checkNetwork()
                //LoadingScreen.displayLoadingWithText(requireActivity(),"Silahkan Tunggu, Sedang mendownload data..",true)
                LoadingScreen.displayLoadingWithText(this, "Sedang Download Data..", false)
                downloadData()

            } else {

                Toasty.info(this, "Silahkan checklist data terlebih dahulu !", Toast.LENGTH_SHORT).show()

            }


        }
        binding.icBack.setOnClickListener {
            finish()
        }
    }

    private fun checkNetwork(){
        if (!NetworkChecker.isNetworkConnected(this)){
            NoInternetLayout
                .Builder(this, R.layout.activity_splash)

        }
    }

    private fun setNotif(txt: String){
        binding.prosesDownload.text = txt
    }

    private fun downloadData(){
        val token = pref.getString(Constants.DEFAULT.TOKEN)
        val SalesAgent = pref.getString(Constants.DEFAULT.USERNAME)
        viewModel.getItemPrice(token.toString())
        viewModel.getTax(token.toString())
        viewModel.getBranch(token.toString())
        viewModel.getDebtor(token.toString(), SalesAgent.toString())
        viewModel.getCurrency(token.toString())
        viewModel.getItem(token.toString(), SalesAgent.toString())
        viewModel.getItemUOM(token.toString())



        viewModel.itemPriceResp.observe(this, Observer {
            when(it) {
                is Resource.Loading -> {
                    setNotif("download data ITEM Price...")
                }
                is Resource.Success -> {
                    Timber.e("TAG DOWNLOAD TABLE : ${it.data!!.data.table}")
                    ItemPriceDao.deleteAll()
                    var total = 1;
                    while (total <= it.data.data.total_data ){
                        var i = total - 1;
                        ItemPriceDao.insert(
                            ItemPrice(
                                itemCode = it.data.data.master_data[i].ItemCode,
                                UOM = it.data.data.master_data[i].UOM,
                                itemPriceKey = it.data.data.master_data[i].ItemPriceKey,
                                priceCategory = it.data.data.master_data[i].PriceCategory,
                                accNo = it.data.data.master_data[i].AccNo as String,
                                fixedPrice = it.data.data.master_data[i].FixedPrice.toDouble(),
                                fixedDetailDiscount = if(it.data.data.master_data[i].FixedDetailDiscount.isNullOrBlank()) 0.0 else it.data.data.master_data[i].FixedDetailDiscount.toDouble(),

                                )
                        )
                        total++;
                        setNotif("download data ${it.data.data.table} : $total dari ${it.data.data.total_data} ")
                    }

                    setNotif("Done download : ${it.data.data.table}")
                    totalSync++;
                    checkIsLimitSync(totalSync)
                }
                is Resource.Error -> {
                    setNotif(it.message.toString())
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.debtorResp.observe(this, Observer {
            when(it) {
                is Resource.Loading -> {
                    setNotif("download data debtor...")
                }
                is Resource.Success -> {
                    Timber.e("TAG DOWNLOAD TABLE : ${it.data!!.data.table}")
                    Timber.e("TAG ${it.data!!.data.total_data}")
                    DebtorDao.deleteAll()
                    var total = 1;
                    while (total <= it.data.data.total_data ){
                        var i = total - 1;
                        DebtorDao.insert(
                            Debtor(
                                accNo = it.data.data.master_data[i].AccNo,
                                companyName = it.data.data.master_data[i].CompanyName,
                                address1 = it.data.data.master_data[i].Address1,
                                address2 = it.data.data.master_data[i].Address2,
                                address3 = it.data.data.master_data[i].Address3,
                                address4 = it.data.data.master_data[i].Address4,
                                deliverAddr1 = it.data.data.master_data[i].DeliverAddr1,
                                deliverAddr2 = it.data.data.master_data[i].DeliverAddr2,
                                deliverAddr3 = it.data.data.master_data[i].DeliverAddr3,
                                deliverAddr4 = it.data.data.master_data[i].DeliverAddr4,
                                displayTerm = it.data.data.master_data[i].DisplayTerm,
                                salesAgent = it.data.data.master_data[i].SalesAgent as String,
                                priceCategory = it.data.data.master_data[i].PriceCategory as String,
                                currencyCode = it.data.data.master_data[i].CurrencyCode,
                                taxType = it.data.data.master_data[i].TaxType as String
                            )
                        )
                        total++;
                        setNotif("download data ${it.data.data.table} : $total dari ${it.data.data.total_data} ")
                    }

                    setNotif("Done download : ${it.data.data.table}")
                    totalSync++;
                    checkIsLimitSync(totalSync)
                }
                is Resource.Error -> {
                    setNotif(it.message.toString())
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.branchResp.observe(this, Observer {
            when(it) {
                is Resource.Loading -> {
                    setNotif("download data branch...")
                }
                is Resource.Success -> {
                    Timber.e("TAG DOWNLOAD TABLE : ${it.data!!.data.table}")
                    BranchDao.deleteAll()
                    var total = 1;
                    while (total <= it.data.data.total_data ){
                        var i = total - 1;
                        BranchDao.insert(
                            Branch(
                                accNo = it.data.data.master_data[i].AccNo,
                                branchCode = it.data.data.master_data[i].BranchCode,
                                branchName = it.data.data.master_data[i].BranchName
                            )
                        )
                        total++;
                        setNotif("download data ${it.data.data.table} : $total dari ${it.data.data.total_data} ")
                    }

                    setNotif("Done download : ${it.data.data.table}")
                    totalSync++;
                    checkIsLimitSync(totalSync)
                }
                is Resource.Error -> {
                    setNotif(it.message.toString())
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.currencyResp.observe(this, Observer {
            when(it) {
                is Resource.Loading -> {
                    setNotif("download data currency...")
                }
                is Resource.Success -> {
                    Timber.e("TAG DOWNLOAD TABLE : ${it.data!!.data.table}")
                    CurrencyDao.deleteAll()
                    var total = 1;
                    while (total <= it.data.data.total_data ){
                        var i = total - 1;
                        CurrencyDao.insert(
                            Currency(
                                currencyCode = it.data.data.master_data[i].CurrencyCode,
                                currencyWord = it.data.data.master_data[i].CurrencyWord,
                                bankSellRate = it.data.data.master_data[i].BankSellRate

                            )
                        )
                        total++;
                        setNotif("download data ${it.data.data.table} : $total dari ${it.data.data.total_data} ")
                    }

                    setNotif("Done download : ${it.data.data.table}")
                    totalSync++;
                    checkIsLimitSync(totalSync)
                }
                is Resource.Error -> {
                    setNotif(it.message.toString())
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })


        viewModel.itemResp.observe(this, Observer {
            when(it) {
                is Resource.Loading -> {
                    setNotif("download data item...")
                }
                is Resource.Success -> {
                    Timber.e("TAG DOWNLOAD TABLE : ${it.data!!.data.table}")
                    ItemDao.deleteAll()
                    var total = 1;
                    while (total <= it.data.data.total_data ){
                        var i = total - 1;
                        ItemDao.insert(
                            Item(
                                itemCode = it.data.data.master_data[i].ItemCode,
                                docKey = it.data.data.master_data[i].DocKey,
                                desc = it.data.data.master_data[i].Description

                            )
                        )
                        total++;
                        setNotif("download data ${it.data.data.table} : $total dari ${it.data.data.total_data} ")
                    }

                    setNotif("Done download : ${it.data.data.table}")
                    totalSync++;
                    checkIsLimitSync(totalSync)
                }
                is Resource.Error -> {
                    setNotif(it.message.toString())
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.itemUOMResp.observe(this, Observer {
            when(it) {
                is Resource.Loading -> {
                    setNotif("download data ITEM UOM...")
                }
                is Resource.Success -> {
                    Timber.e("TAG DOWNLOAD TABLE : ${it.data!!.data.table}")
                    ItemUOMDao.deleteAll()
                    var total = 1;
                    while (total <= it.data.data.total_data ){
                        var i = total - 1;
                        ItemUOMDao.insert(
                            ItemUom(
                                itemCode = it.data.data.master_data[i].ItemCode,
                                UOM = it.data.data.master_data[i].UOM,
                                rate = it.data.data.master_data[i].Rate,
                                price = it.data.data.master_data[i].Price as String,
                                barCode = it.data.data.master_data[i].BarCode as String

                            )
                        )
                        total++;
                        setNotif("download data ${it.data.data.table} : $total dari ${it.data.data.total_data} ")
                    }

                    setNotif("Done download : ${it.data.data.table}")
                    totalSync++;
                    checkIsLimitSync(totalSync)
                }
                is Resource.Error -> {
                    setNotif(it.message.toString())
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.taxResp.observe(this, Observer {
            when(it) {
                is Resource.Loading -> {
                    setNotif("download data Tax...")
                }
                is Resource.Success -> {
                    Timber.e("TAG DOWNLOAD TABLE : ${it.data!!.data.table}")
                    TaxDao.deleteAll()
                    var total = 1;
                    while (total <= it.data.data.total_data ){
                        var i = total - 1;
                        TaxDao.insert(
                            Tax(
                                taxType = it.data.data.master_data[i].TaxType,
                                taxRate = it.data.data.master_data[i].TaxRate.toDouble(),

                                )
                        )
                        total++;
                        setNotif("download data ${it.data.data.table} : $total dari ${it.data.data.total_data} ")
                    }

                    setNotif("Done download : ${it.data.data.table}")
                    totalSync++;
                    checkIsLimitSync(totalSync)
                }
                is Resource.Error -> {
                    setNotif(it.message.toString())
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    private fun checkIsLimitSync(value: Int){
        Timber.e("TOTAL SYNC : $value")
        if (value.equals(limitSync)){
            setNotif("Sukses Melakukan Partial Sync")
            LoadingScreen.hideLoading()
            Toasty.success(this, "Berhasil melakukan Partial Syncron !", Toast.LENGTH_SHORT).show()

        }
    }

}