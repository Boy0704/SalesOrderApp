package com.apps.salesorder.data.response

data class SyncSoResp(
    val error: String,
    val message: String,
    val `data`: List<Any>
)