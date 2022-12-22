package com.apps.salesorder.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.salesorder.R
import com.apps.salesorder.api.ApiEndPoint
import com.apps.salesorder.api.Resource
import com.apps.salesorder.data.response.*
import kotlinx.coroutines.launch

class HomeViewModel (
    val api: ApiEndPoint,
    val context : Context
) : ViewModel()  {

    val branchResp: MutableLiveData<Resource<BranchResp>> = MutableLiveData()
    val debtorResp: MutableLiveData<Resource<DebtorResp>> = MutableLiveData()
    val currencyResp: MutableLiveData<Resource<CurrencyResp>> = MutableLiveData()
    val itemResp: MutableLiveData<Resource<ItemResp>> = MutableLiveData()
    val itemUOMResp: MutableLiveData<Resource<ItemUomResp>> = MutableLiveData()
    val itemPriceResp: MutableLiveData<Resource<ItemPriceResp>> = MutableLiveData()
    val taxResp: MutableLiveData<Resource<TaxResp>> = MutableLiveData()


    fun getBranch(token: String) = viewModelScope.launch {
        branchResp.value = Resource.Loading()
        val dataApi = api.dataBranch(authorization = token)
        try {
            if (dataApi.code() == 401) {
                branchResp.value = Resource.Error("Token Unauthorized atau akun sedang tidak aktif!")
            } else {
                if (dataApi.body()!!.error.equals("false")) {
                    branchResp.value = Resource.Success( dataApi.body()!! )
                } else {
                    branchResp.value = Resource.Error( dataApi.body()?.message.toString() )
                }
            }

        } catch (e: Exception) {
            if(dataApi.code() == 500) {
                branchResp.value = Resource.Error(context.getString(R.string.error_server))
            } else {
                branchResp.value = Resource.Error( e.message.toString() )
            }
        }
    }

    fun getCurrency(token: String) = viewModelScope.launch {
        currencyResp.value = Resource.Loading()
        val dataApi = api.dataCurrency(authorization = token)
        try {
            if (dataApi.code() == 401) {
                currencyResp.value = Resource.Error("Token Unauthorized atau akun sedang tidak aktif!")
            } else {
                if (dataApi.body()!!.error.equals("false")) {
                    currencyResp.value = Resource.Success( dataApi.body()!! )
                } else {
                    currencyResp.value = Resource.Error( dataApi.body()?.message.toString() )
                }
            }

        } catch (e: Exception) {
            if(dataApi.code() == 500) {
                currencyResp.value = Resource.Error(context.getString(R.string.error_server))
            } else {
                currencyResp.value = Resource.Error( e.message.toString() )
            }
        }
    }

    fun getDebtor(token: String) = viewModelScope.launch {
        debtorResp.value = Resource.Loading()
        val dataApi = api.dataDebtor(authorization = token)
        try {
            if (dataApi.code() == 401) {
                debtorResp.value = Resource.Error("Token Unauthorized atau akun sedang tidak aktif!")
            } else {
                if (dataApi.body()!!.error.equals("false")) {
                    debtorResp.value = Resource.Success( dataApi.body()!! )
                } else {
                    debtorResp.value = Resource.Error( dataApi.body()?.message.toString() )
                }
            }

        } catch (e: Exception) {
            if(dataApi.code() == 500) {
                debtorResp.value = Resource.Error(context.getString(R.string.error_server))
            } else {
                debtorResp.value = Resource.Error( e.message.toString() )
            }
        }
    }

    fun getItem(token: String) = viewModelScope.launch {
        itemResp.value = Resource.Loading()
        val dataApi = api.dataItem(authorization = token)
        try {
            if (dataApi.code() == 401) {
                itemResp.value = Resource.Error("Token Unauthorized atau akun sedang tidak aktif!")
            } else {
                if (dataApi.body()!!.error.equals("false")) {
                    itemResp.value = Resource.Success( dataApi.body()!! )
                } else {
                    itemResp.value = Resource.Error( dataApi.body()?.message.toString() )
                }
            }

        } catch (e: Exception) {
            if(dataApi.code() == 500) {
                itemResp.value = Resource.Error(context.getString(R.string.error_server))
            } else {
                itemResp.value = Resource.Error( e.message.toString() )
            }
        }
    }

    fun getItemUOM(token: String) = viewModelScope.launch {
        itemUOMResp.value = Resource.Loading()
        val dataApi = api.dataItemUOM(authorization = token)
        try {
            if (dataApi.code() == 401) {
                itemUOMResp.value = Resource.Error("Token Unauthorized atau akun sedang tidak aktif!")
            } else {
                if (dataApi.body()!!.error.equals("false")) {
                    itemUOMResp.value = Resource.Success( dataApi.body()!! )
                } else {
                    itemUOMResp.value = Resource.Error( dataApi.body()?.message.toString() )
                }
            }

        } catch (e: Exception) {
            if(dataApi.code() == 500) {
                itemUOMResp.value = Resource.Error(context.getString(R.string.error_server))
            } else {
                itemUOMResp.value = Resource.Error( e.message.toString() )
            }
        }
    }

    fun getItemPrice(token: String) = viewModelScope.launch {
        itemPriceResp.value = Resource.Loading()
        val dataApi = api.dataItemPrice(authorization = token)
        try {
            if (dataApi.code() == 401) {
                itemPriceResp.value = Resource.Error("Token Unauthorized atau akun sedang tidak aktif!")
            } else {
                if (dataApi.body()!!.error.equals("false")) {
                    itemPriceResp.value = Resource.Success( dataApi.body()!! )
                } else {
                    itemPriceResp.value = Resource.Error( dataApi.body()?.message.toString() )
                }
            }

        } catch (e: Exception) {
            if(dataApi.code() == 500) {
                itemPriceResp.value = Resource.Error(context.getString(R.string.error_server))
            } else {
                itemPriceResp.value = Resource.Error( e.message.toString() )
            }
        }
    }

    fun getTax(token: String) = viewModelScope.launch {
        taxResp.value = Resource.Loading()
        val dataApi = api.dataTax(authorization = token)
        try {
            if (dataApi.code() == 401) {
                taxResp.value = Resource.Error("Token Unauthorized atau akun sedang tidak aktif!")
            } else {
                if (dataApi.body()!!.error.equals("false")) {
                    taxResp.value = Resource.Success( dataApi.body()!! )
                } else {
                    taxResp.value = Resource.Error( dataApi.body()?.message.toString() )
                }
            }

        } catch (e: Exception) {
            if(dataApi.code() == 500) {
                taxResp.value = Resource.Error(context.getString(R.string.error_server))
            } else {
                taxResp.value = Resource.Error( e.message.toString() )
            }
        }
    }
    
}