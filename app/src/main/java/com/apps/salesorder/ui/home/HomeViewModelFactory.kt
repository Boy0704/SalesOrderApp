package com.apps.salesorder.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.salesorder.api.ApiEndPoint

class HomeViewModelFactory(
    private val api: ApiEndPoint,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(api,context) as T
    }

}