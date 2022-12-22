package com.apps.salesorder.data.response

data class BranchResp(
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
            val AccNo: String,
            val BranchCode: String,
            val BranchName: String,
            val Address1: Any,
            val Address2: Any,
            val Address3: Any,
            val Address4: Any,
            val PostCode: Any,
            val Contact: Any,
            val Phone1: Any,
            val Phone2: Any,
            val Fax1: Any,
            val Fax2: Any,
            val LastUpdate: String,
            val AreaCode: Any,
            val SalesAgent: Any,
            val PurchaseAgent: Any,
            val EmailAddress: Any,
            val IsActive: String,
            val TaxBranchID: Any,
            val Mobile: Any
        )
    }
}