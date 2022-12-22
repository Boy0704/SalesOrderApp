package com.apps.salesorder.data.response

data class CurrencyResp(
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
            val CurrencyCode: String,
            val CurrencyWord: String,
            val CurrencyWord2: Any,
            val CurrencySymbol: String,
            val BankBuyRate: String,
            val BankSellRate: String,
            val FCGainAccount: Any,
            val FCLossAccount: Any,
            val GainLossJournalType: Any,
            val LastUpdate: String,
            val Guid: Any
        )
    }
}