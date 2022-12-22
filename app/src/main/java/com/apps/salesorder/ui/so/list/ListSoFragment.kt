package com.apps.salesorder.ui.so.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.apps.driverasrikatara.ui.kendaraan.fragment.CariBranchAdapter
import com.apps.driverasrikatara.ui.kendaraan.fragment.SoListAdapter
import com.apps.salesorder.R
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.SoHeaderDao
import com.apps.salesorder.data.model.Branch
import com.apps.salesorder.data.model.SoHeader
import com.apps.salesorder.databinding.FragmentListSoBinding
import com.apps.salesorder.ui.so.header.SoHeaderActivity
import timber.log.Timber


class ListSoFragment : Fragment() {

    private lateinit var binding: FragmentListSoBinding
    private lateinit var dataAdapter: SoListAdapter
    private lateinit var database: SoDB
    private lateinit var SoHeaderDao: SoHeaderDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListSoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = SoDB.getDatabase(requireContext())
        SoHeaderDao = database.getSoHeader()

        setupViewModel()
        setListener()

    }

    private fun setupViewModel() {

        val listItems = arrayListOf<SoHeader>()
        listItems.addAll(SoHeaderDao.getAll())

        Timber.e("[DATA LIST-ITEM] ${listItems.size}")
        if (listItems.size > 0){
            binding.empty.visibility = View.GONE
        }
        setupAdapter(listItems)

    }

    private fun setListener() {

    }

    private fun setupAdapter(listItem: ArrayList<SoHeader>) {

        dataAdapter = SoListAdapter(listItem, requireContext(), requireActivity(),object : SoListAdapter.OnAdapterListener{
            override fun onClick(result: SoHeader) {

            }
        })

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