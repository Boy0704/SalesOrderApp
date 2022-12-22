package com.apps.salesorder.data.response

data class TaxResp(
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
            val TaxType: String,
            val Description: Any,
            val TaxRate: String,
            val Inclusive: String,
            val IsActive: String,
            val LastUpdate: String,
            val TaxTypeCategory: Any,
            val IRASTaxCode: String,
            val SupplyPurchase: String,
            val IsDefault: String,
            val TaxAccNo: String,
            val IsZeroRate: String,
            val UseTrxTaxAccNo: String,
            val AccountingBasis: String,
            val AddToCost: String,
            val Guid: Any
        )
    }
}