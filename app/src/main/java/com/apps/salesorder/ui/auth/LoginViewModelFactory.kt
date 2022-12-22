package com.apps.salesorder.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.salesorder.api.ApiEndPoint

class LoginViewModelFactory(
    private val api: ApiEndPoint,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(api,context) as T
    }

}