package com.apps.driverasrikatara.ui.kendaraan.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.apps.salesorder.data.model.Branch
import com.apps.salesorder.data.model.Debtor
import com.apps.salesorder.data.model.Item
import com.apps.salesorder.databinding.ItemListDuaBinding
import timber.log.Timber

class CariItemAdapter(
    val listData: ArrayList<Item>,
    var listener: OnAdapterListener
): RecyclerView.Adapter<CariItemAdapter.ViewHolder>(), Filterable {

    private var dataFilter = ArrayList<Item>()

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
        holder.binding.name.text = list.itemCode
        holder.binding.description.text = list.desc
        //holder.binding.description.visibility = View.GONE

        holder.binding.mainLayout.setOnClickListener {
            listener.onClick( list )
        }

    }

    class ViewHolder(val binding: ItemListDuaBinding): RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(result: Item)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Timber.d("charSearch: $charSearch")
                if (charSearch.isEmpty()) {
                    dataFilter = listData
                } else {
                    val dataFiltered = ArrayList<Item>()
                    for (datas in listData) {
                        if ( datas.itemCode.toLowerCase().contains(charSearch.toLowerCase()) or datas.desc!!.toLowerCase().contains(charSearch.toLowerCase()) ) {
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
                dataFilter = results?.values as ArrayList<Item>
                notifyDataSetChanged()
            }

        }
    }

}