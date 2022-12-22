package com.apps.salesorder.ui.kendaraan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.apps.driverasrikatara.ui.kendaraan.fragment.CariItemUOMAdapter
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.ItemUOMDao
import com.apps.salesorder.data.model.ItemUom
import com.apps.salesorder.data.preferences.Preferences
import com.apps.salesorder.databinding.FragmentCariDefaultBinding
import com.apps.salesorder.ui.so.detail.AddDetailActivity
import timber.log.Timber

class CariItemUOMFragment : DialogFragment() {

    private val pref by lazy { Preferences(requireActivity()) }
    private lateinit var dataAdapter: CariItemUOMAdapter
    private lateinit var binding: FragmentCariDefaultBinding
    private lateinit var database: SoDB
    private lateinit var ItemUOMDao: ItemUOMDao
    private var itemCode = ""

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
        ItemUOMDao = database.getItemUOMDao()

        //get data dari activity
        itemCode = requireArguments().getString("item_code").toString()

        setupViewModel()
        setListener()
    }

    fun setListener(){
        binding.btClose.setOnClickListener {
            keluar()
        }
    }

    private fun setupViewModel() {
        binding.title.setText("Cari UOM")

        val listItems = arrayListOf<ItemUom>()
        listItems.addAll(ItemUOMDao.getByItemCode(itemCode))

        Timber.e("[DATA] $itemCode")
        Timber.e("[DATA LIST-ITEM] ${listItems.size}")
        setupAdapter(listItems)

    }

    private fun setupAdapter(listItem: ArrayList<ItemUom>) {

        dataAdapter = CariItemUOMAdapter(listItem, object : CariItemUOMAdapter.OnAdapterListener{
            override fun onClick(result: ItemUom) {
                (activity as AddDetailActivity?)?.setDataFromDialogCariUOM(result)
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