package com.apps.salesorder.data.response

data class ItemPriceResp(
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
            val ItemPriceKey: String,
            val ItemCode: String,
            val UOM: String,
            val PriceCategory: String,
            val AccNo: Any,
            val SuppCustItemCode: Any,
            val Ref: Any,
            val UseFixedPrice: String,
            val FixedPrice: String,
            val FixedDetailDiscount: String,
            val Qty1: Any,
            val Price1: Any,
            val DetailDiscount1: Any,
            val Qty2: Any,
            val Price2: Any,
            val DetailDiscount2: Any,
            val Qty3: Any,
            val Price3: Any,
            val DetailDiscount3: Any,
            val Qty4: Any,
            val Price4: Any,
            val DetailDiscount4: Any,
            val FOCLevel: Any,
            val FOCQty: Any,
            val BonusPointQty: Any,
            val BonusPoint: Any,
            val LastUpdate: String,
            val Guid: Any
        )
    }
}