package com.apps.salesorder.data.response

data class ItemResp(
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
            val ItemCode: String,
            val DocKey: String,
            val Description: String,
            val Desc2: Any,
            val FurtherDescription: Any,
            val ItemGroup: String,
            val ItemType: Any,
            val AssemblyCost: Any,
            val LeadTime: Any,
            val StockControl: String,
            val HasSerialNo: String,
            val HasBatchNo: String,
            val DutyRate: String,
            val Taxtype: Any,
            val Note: Any,
            val Image: Any,
            val CostingMethod: String,
            val SalesUOM: String,
            val PurchaseUOM: String,
            val ReportUOM: String,
            val LastModified: String,
            val LastModifiedUserID: String,
            val CreatedTimeStamp: String,
            val CreatedUserID: String,
            val IsActive: String,
            val LastUpdate: String,
            val SNFormatName: Any,
            val IsCalcBonusPoint: String,
            val MarkupRatio: Any,
            val HasPromoter: String,
            val GlobalCode: Any,
            val ItemBrand: Any,
            val ItemClass: Any,
            val ItemCategory: Any,
            val LeadTimeDay: Any,
            val ExternalLink: Any,
            val Discontinued: String,
            val AutoUOMConversion: String,
            val BaseUOM: String,
            val BackOrderControl: String,
            val PurchaseTaxType: Any,
            val TariffCode: Any,
            val Guid: Any,
            val IsSalesItem: Any,
            val IsPurchaseItem: Any,
            val IsPOSItem: Any,
            val IsRawMaterialItem: Any,
            val IsFinishGoodsItem: Any,
            val MainSupplier: Any,
            val ImageFileName: Any
        )
    }
}