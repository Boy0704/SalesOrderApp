package com.apps.salesorder.ui.sync

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.apps.driverasrikatara.ui.kendaraan.fragment.SoListAdapter
import com.apps.driverasrikatara.ui.kendaraan.fragment.SyncSoListAdapter
import com.apps.salesorder.api.ApiService
import com.apps.salesorder.api.Resource
import com.apps.salesorder.data.constants.Constants
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.SoDetailDao
import com.apps.salesorder.data.db.dao.SoHeaderDao
import com.apps.salesorder.data.model.SoDetail
import com.apps.salesorder.data.model.SoHeader
import com.apps.salesorder.data.model.SoSync
import com.apps.salesorder.data.preferences.Preferences
import com.apps.salesorder.databinding.ActivityPartialSyncBinding
import com.apps.salesorder.databinding.ActivitySyncSoBinding
import com.apps.salesorder.helper.LoadingScreen
import com.apps.salesorder.ui.home.HomeActivity
import com.apps.salesorder.ui.home.HomeViewModel
import com.apps.salesorder.ui.home.HomeViewModelFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import es.dmoral.toasty.Toasty
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SyncSoActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySyncSoBinding.inflate(layoutInflater) }
    private val api by lazy { ApiService.getClient(this) }
    private lateinit var viewModelFactory : HomeViewModelFactory
    private lateinit var viewModel : HomeViewModel
    private val pref by lazy { Preferences(this) }

    private lateinit var dataAdapter: SyncSoListAdapter
    private lateinit var database: SoDB
    private lateinit var SoHeaderDao: SoHeaderDao
    private lateinit var SoDetailDao: SoDetailDao
    private var dataHeader = arrayListOf<SoHeader>()
    private var dataHeaderJson = arrayListOf<SoHeader>()
    private var dataDetailJson = arrayListOf<SoDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = SoDB.getDatabase(this)
        SoHeaderDao = database.getSoHeader()
        SoDetailDao = database.getSoDetail()

        setupViewModel()
        setListener()
    }

    private fun setupViewModel() {

        viewModelFactory = HomeViewModelFactory( api, this )
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)


        //dataHeader.addAll(SoHeaderDao.getNotSync("sync"))
        dataHeader.addAll(SoHeaderDao.getByNotStatus("draft"))

        Timber.e("[DATA LIST-ITEM] ${dataHeader.size}")
        if (dataHeader.size > 0){
            binding.empty.visibility = View.GONE
        }
        setupAdapter(dataHeader)

        binding.BtnSync.setOnClickListener {
            sync()
            //Toasty.success(this, "Data SO berhasil di Syncron !", Toast.LENGTH_SHORT).show()
        }
        binding.icBack.setOnClickListener {
            finish()
        }


    }

    private fun sync(){
        val gson = Gson()

        dataHeaderJson.addAll(SoHeaderDao.getByChecked("ya"))
        dataDetailJson.addAll(SoDetailDao.getByChecked("ya"))

        val soSync: SoSync = SoSync(dataHeaderJson, dataDetailJson)
        val json: String = gson.toJson(soSync)

        Timber.e("JSON = $json ")
        if (json.equals("{\"so_detail\":[],\"so_header\":[]}")){
            Toasty.info(this, "Silahkan select data dahulu !", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.soSyncProses(json)

            viewModel.syncSoResp.observe(this, Observer {
                when(it){
                    is Resource.Loading -> {
                        LoadingScreen.displayLoadingWithText(this,"Silahkan Tunggu..",false)
                    }
                    is Resource.Success -> {
                        LoadingScreen.hideLoading()
                        Timber.e("TAG ${it.data!!.data}")

                        SoHeaderDao.updateCheckedHeaderAll("ya","sync")
                        SoDetailDao.updateCheckedDetailAll("ya","sync")

                        Toasty.success(this, "Data SO berhasil di Syncron !", Toast.LENGTH_SHORT).show()
                        finish();
                        startActivity(getIntent());
                    }
                    is Resource.Error -> {
                        LoadingScreen.hideLoading()
                        Toast.makeText(this, "terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        }

    }

    private fun setListener() {

    }

    private fun setupAdapter(listItem: ArrayList<SoHeader>) {

        dataAdapter = SyncSoListAdapter(listItem, this, this, object : SyncSoListAdapter.OnAdapterListener{
            override fun onClick(result: SoHeader) {

            }
        })

        binding.selectall.setOnCheckedChangeListener { _, b ->
            if (b){
                dataAdapter.checkAll(true)
            } else {
                dataAdapter.checkAll(false)
            }
        }

        binding.rvData.apply {
            layoutManager = GridLayoutManager(context,1)
            adapter = dataAdapter

        }
        dataAdapter.notifyDataSetChanged()

        binding.cari.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dataAdapter.filter.filter(newText)
                return false
            }

        })

    }


}