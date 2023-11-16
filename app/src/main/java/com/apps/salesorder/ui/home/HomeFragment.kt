package com.apps.salesorder.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.apps.salesorder.databinding.FragmentHomeBinding
import com.apps.salesorder.helper.LoadingScreen
import com.apps.salesorder.ui.so.header.SoHeaderActivity
import com.apps.salesorder.ui.sync.PartialSyncActivity
import com.apps.salesorder.ui.sync.SyncSoActivity
import com.tapadoo.alerter.Alerter
import com.wessam.library.NetworkChecker
import com.wessam.library.NoInternetLayout
import es.dmoral.toasty.Toasty
import timber.log.Timber


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val api by lazy { ApiService.getClient(requireActivity()) }
    private lateinit var viewModelFactory : HomeViewModelFactory
    private lateinit var viewModel : HomeViewModel
    private val pref by lazy { Preferences(requireActivity()) }

    private lateinit var database: SoDB
    private lateinit var BranchDao: BranchDao
    private lateinit var DebtorDao: DebtorDao
    private lateinit var CurrencyDao: CurrencyDao
    private lateinit var ItemDao: ItemDao
    private lateinit var ItemUOMDao: ItemUOMDao
    private lateinit var ItemPriceDao: ItemPriceDao
    private lateinit var SoHeaderDao: SoHeaderDao
    private lateinit var SoDetailDao: SoDetailDao
    private lateinit var TaxDao: TaxDao
    private lateinit var CompanySettingDao: CompanySettingDao
    private var totalSync: Int = 0
    private var limitSync: Int = 8

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nama = pref.getString(Constants.DEFAULT.NAMA)
        binding.tvNama.setText(nama.toString())

        database = SoDB.getDatabase(requireContext())
        BranchDao = database.getBranchDao()
        DebtorDao = database.getDebtorDao()
        CurrencyDao = database.getCurrencyDao()
        ItemDao = database.getItemDao()
        ItemUOMDao = database.getItemUOMDao()
        ItemPriceDao = database.getItemPriceDao()
        TaxDao = database.getTaxDao()
        SoHeaderDao = database.getSoHeader()
        SoDetailDao = database.getSoDetail()
        CompanySettingDao = database.getCompanySetting()

        viewModelFactory = HomeViewModelFactory( api, requireActivity() )
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)


        if(DebtorDao.getAll().size == 0){
            infoFullSync()
        }

        binding.fullSync.setOnClickListener {

            Alerter.create(requireActivity())
                .setDuration(60*1000)
                .setTitle("Alert")
                .setText("Full sync akan menghapus semua data, pastikan semua data transaksi sudah di singkronkan !")
                .addButton("Lanjut", R.style.AlertButton, View.OnClickListener {
                    Alerter.hide()
                    checkNetwork()
                    //LoadingScreen.displayLoadingWithText(requireActivity(),"Silahkan Tunggu, Sedang mendownload data..",true)
                    LoadingScreen.displayLoadingWithText(requireActivity(), "Sedang Download Data..", true)
                    downloadData()

                })
                .addButton("Batal", R.style.AlertButton, View.OnClickListener {
                    Alerter.hide()
                    Toasty.info(requireContext(), "Full Sync di batalkan !", Toast.LENGTH_SHORT).show()
                })
                .show()

        }
        binding.partialSync.setOnClickListener {
            val intent = Intent(requireActivity(), PartialSyncActivity::class.java)
            startActivity(intent)
        }
        binding.transSync.setOnClickListener {
            val intent = Intent(requireActivity(), SyncSoActivity::class.java)
            startActivity(intent)
        }
        binding.newSo.setOnClickListener {
            val intent = Intent(requireActivity(), SoHeaderActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fullSync(){

    }

    private fun infoFullSync(){
//        SweetAlertDialog(requireContext())
//            .setTitleText("Silahkan lakukan Full Sync untuk mendapatkan data master pendukung !")
//            .show();
        Toast.makeText(requireActivity(), "Silahkan lakukan Full Sync untuk mendapatkan data master pendukung", Toast.LENGTH_SHORT).show()
    }

    private fun checkNetwork(){
        if (!NetworkChecker.isNetworkConnected(requireContext())){
            NoInternetLayout
                .Builder(requireActivity(), R.layout.activity_splash)

        }
    }

    private fun setNotif(txt: String){
        binding.prosesDownload.text = txt
    }

    private fun downloadData(){

        SoHeaderDao.deleteAll()
        SoDetailDao.deleteAll()

        val token = pref.getString(Constants.DEFAULT.TOKEN)
        val SalesAgent = pref.getString(Constants.DEFAULT.USERNAME)
        viewModel.getItemPrice(token.toString())
        viewModel.getTax(token.toString())
        viewModel.getBranch(token.toString())
        viewModel.getDebtor(token.toString(), SalesAgent.toString())
        viewModel.getCurrency(token.toString())
        viewModel.getItem(token.toString(), SalesAgent.toString())
        viewModel.getItemUOM(token.toString())
        viewModel.getSetting(token.toString())



        viewModel.itemPriceResp.observe(viewLifecycleOwner, Observer {
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
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.debtorResp.observe(viewLifecycleOwner, Observer {
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
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.branchResp.observe(viewLifecycleOwner, Observer {
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
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.currencyResp.observe(viewLifecycleOwner, Observer {
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
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })


        viewModel.itemResp.observe(viewLifecycleOwner, Observer {
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
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.itemUOMResp.observe(viewLifecycleOwner, Observer {
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
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.taxResp.observe(viewLifecycleOwner, Observer {
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
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.settingResp.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Loading -> {
                    setNotif("download data Setting...")
                }
                is Resource.Success -> {
                    Timber.e("TAG DOWNLOAD TABLE : ${it.data!!.data.table}")
                    CompanySettingDao.deleteAll()
                    var total = 1;
                    while (total <= it.data.data.total_data ){
                        var i = total - 1;
                        CompanySettingDao.insert(
                            CompanySetting(
                                company_id = it.data.data.master_data[i].company_id,
                                company_name = it.data.data.master_data[i].company_name,
                                alamat = it.data.data.master_data[i].alamat,
                                logo = it.data.data.master_data[i].logo,
                                format_so = it.data.data.master_data[i].format_so,
                                next_so = it.data.data.master_data[i].next_so
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
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    private fun checkIsLimitSync(value: Int){
        Timber.e("TOTAL SYNC : $value")
        if (value.equals(limitSync)){
            setNotif("Sukses Melakukan Full Sync")
            LoadingScreen.hideLoading()

            Alerter.create(requireActivity())
                .setDuration(60*1000)
                .setTitle("Notif")
                .setText("Sukses Melakukan Full Sync !")
                .addButton("Oke", R.style.AlertButton, View.OnClickListener {
                    val listener = activity as OnFragmentInteractionListener
                    listener.onRefreshActivity()

                })

                .show()




        }
    }

}