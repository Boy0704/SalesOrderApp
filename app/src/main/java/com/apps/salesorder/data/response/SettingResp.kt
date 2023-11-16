package com.apps.salesorder.data.response

data class SettingResp(
    val error: String,
    val message: String,
    val `data`: Data
) {
    data class Data(
        val total_data: Int,
        val table: String,
        val master_data: List<MasterData>
    ) {
        data class MasterData(
            val id: Int,
            val company_id: String,
            val company_name: String,
            val alamat: String,
            val logo: String,
            val format_so: String,
            val next_so: String
        )
    }
}