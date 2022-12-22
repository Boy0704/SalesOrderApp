package com.apps.salesorder.ui.kendaraan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.apps.driverasrikatara.ui.kendaraan.fragment.CariItemAdapter
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.ItemDao
import com.apps.salesorder.data.model.Item
import com.apps.salesorder.data.preferences.Preferences
import com.apps.salesorder.databinding.FragmentCariDefaultBinding
import com.apps.salesorder.ui.so.detail.AddDetailActivity
import timber.log.Timber

class CariItemFragment : DialogFragment() {

    private val pref by lazy { Preferences(requireActivity()) }
    private lateinit var dataAdapter: CariItemAdapter
    private lateinit var binding: FragmentCariDefaultBinding
    private lateinit var database: SoDB
    private lateinit var ItemDao: ItemDao

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
        ItemDao = database.getItemDao()

        setupViewModel()
        setListener()
    }

    fun setListener(){
        binding.btClose.setOnClickListener {
            keluar()
        }
    }

    private fun setupViewModel() {
        binding.title.setText("Cari Item")

        val listItems = arrayListOf<Item>()
        listItems.addAll(ItemDao.getAll())

        Timber.e("[DATA LIST-ITEM] ${listItems.size}")
        setupAdapter(listItems)

    }

    private fun setupAdapter(listItem: ArrayList<Item>) {

        dataAdapter = CariItemAdapter(listItem, object : CariItemAdapter.OnAdapterListener{
            override fun onClick(result: Item) {
                (activity as AddDetailActivity?)?.setDataFromDialogCariItem(result)
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