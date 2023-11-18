package com.apps.salesorder.ui.auth

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.salesorder.R
import com.apps.salesorder.api.ApiEndPoint
import com.apps.salesorder.api.Resource
import com.apps.salesorder.data.response.LoginResp
import kotlinx.coroutines.launch

class LoginViewModel (
    val api: ApiEndPoint,
    val context : Context
) : ViewModel() {

    val loginResponse: MutableLiveData<Resource<LoginResp>> = MutableLiveData()
    fun loginProses(username: String, password: String, androidId: String) = viewModelScope.launch {
        loginResponse.value = Resource.Loading()
        val dataApi = api.login(username = username, password = password, androidId = androidId)
        try {
            if (dataApi.code() == 401) {
                loginResponse.value = Resource.Error("Token Unauthorized atau akun sedang tidak aktif!")
            } else {
                if (dataApi.body()!!.error.equals("true")) {
                    loginResponse.value = Resource.Success( dataApi.body()!! )
                } else {
                    loginResponse.value = Resource.Error( "error", dataApi.body()!! )
                }
            }

        } catch (e: Exception) {
            if(dataApi.code() == 500) {
                loginResponse.value = Resource.Error(context.getString(R.string.error_server))
            } else {
                loginResponse.value = Resource.Error( e.message.toString() )
            }
        }
    }

    fun logoutProses(androidId: String) = viewModelScope.launch {
        loginResponse.value = Resource.Loading()
        val dataApi = api.logout(androidId = androidId)
        try {
            if (dataApi.code() == 401) {
                loginResponse.value = Resource.Error("Token Unauthorized atau akun sedang tidak aktif!")
            } else {
                if (dataApi.body()!!.error.equals("true")) {
                    loginResponse.value = Resource.Success( dataApi.body()!! )
                } else {
                    loginResponse.value = Resource.Error( "error", dataApi.body()!! )
                }
            }

        } catch (e: Exception) {
            if(dataApi.code() == 500) {
                loginResponse.value = Resource.Error(context.getString(R.string.error_server))
            } else {
                loginResponse.value = Resource.Error( e.message.toString() )
            }
        }
    }

}