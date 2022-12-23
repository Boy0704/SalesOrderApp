package com.apps.driverasrikatara.ui.kendaraan.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.apps.salesorder.data.model.Branch
import com.apps.salesorder.data.model.Debtor
import com.apps.salesorder.databinding.ItemListDuaBinding
import timber.log.Timber

class CariBranchAdapter(
    val listData: ArrayList<Branch>,
    var listener: OnAdapterListener
): RecyclerView.Adapter<CariBranchAdapter.ViewHolder>(), Filterable {

    private var dataFilter = ArrayList<Branch>()

    init {
        dataFilter = listData
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder (
        ItemListDuaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = dataFilter.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = dataFilter[position]
        holder.binding.name.text = list.branchCode
        holder.binding.description.text = list.branchName
        //holder.binding.description.visibility = View.GONE

        holder.binding.mainLayout.setOnClickListener {
            listener.onClick( list )
        }

    }

    class ViewHolder(val binding: ItemListDuaBinding): RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(result: Branch)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Timber.d("charSearch: $charSearch")
                if (charSearch.isEmpty()) {
                    dataFilter = listData
                } else {
                    val dataFiltered = ArrayList<Branch>()
                    for (datas in listData) {
                        if ( datas.branchCode!!.toLowerCase().contains(charSearch.toLowerCase()) ) {
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
                dataFilter = results?.values as ArrayList<Branch>
                notifyDataSetChanged()
            }

        }
    }

}