package com.apps.salesorder.ui.kendaraan.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.apps.driverasrikatara.ui.kendaraan.fragment.CariBranchAdapter
import com.apps.driverasrikatara.ui.kendaraan.fragment.CariCompCodeAdapter
import com.apps.salesorder.R
import com.apps.salesorder.api.ApiService
import com.apps.salesorder.api.Resource
import com.apps.salesorder.data.constants.Constants
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.BranchDao
import com.apps.salesorder.data.db.dao.DebtorDao
import com.apps.salesorder.data.model.Branch
import com.apps.salesorder.data.model.Debtor
import com.apps.salesorder.data.preferences.Preferences
import com.apps.salesorder.databinding.FragmentCariDefaultBinding
import com.apps.salesorder.helper.LoadingScreen
import com.apps.salesorder.ui.so.header.SoHeaderActivity
import timber.log.Timber

class CariBranchFragment : DialogFragment() {

    private val pref by lazy { Preferences(requireActivity()) }
    private lateinit var dataAdapter: CariBranchAdapter
    private lateinit var binding: FragmentCariDefaultBinding
    private lateinit var database: SoDB
    private lateinit var BranchDao: BranchDao
    private var accNo = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCariDefaultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = SoDB.getDatabase(requireContext())
        BranchDao = database.getBranchDao()

        //get data dari activity
        accNo = requireArguments().getString("accno").toString()

        setupViewModel()
        setListener()
    }

    fun setListener(){
        binding.btClose.setOnClickListener {
            keluar()
        }
    }

    private fun setupViewModel() {
        binding.title.setText("Cari Branch")

        val listItems = arrayListOf<Branch>()
        listItems.addAll(BranchDao.getByAccNo(accNo))

        Timber.e("[DATA] $accNo")
        Timber.e("[DATA LIST-ITEM] ${listItems.size}")
        setupAdapter(listItems)

    }

    private fun setupAdapter(listItem: ArrayList<Branch>) {

        dataAdapter = CariBranchAdapter(listItem, object : CariBranchAdapter.OnAdapterListener{
            override fun onClick(result: Branch) {
                (activity as SoHeaderActivity?)?.setDataFromDialogCariBranch(result)
                keluar()
            }
        })

        binding.rvKendaraan.apply {
            layoutManager = GridLayoutManager(context,1)
            adapter = dataAdapter
        }

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

    private fun keluar(){
        activity?.supportFragmentManager?.popBackStack()
        dismiss()
    }

}