package com.apps.salesorder.data.response

data class LoginResp(
    val error: String,
    val message: String,
    val `data`: List<Data>
) {
    data class Data(
        val id_user: String,
        val username: String,
        val nama_lengkap: String,
        val token: String
    )
}