package com.apps.salesorder.data.response

data class ItemUomResp(
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
            val UOM: String,
            val Rate: String,
            val Shelf: Any,
            val Price: Any,
            val Cost: Any,
            val RealCost: String,
            val MostRecentlyCost: Any,
            val MinSalePrice: String,
            val MaxSalePrice: String,
            val MinPurchasePrice: String,
            val MaxPurchasePrice: String,
            val MinQty: Any,
            val MaxQty: Any,
            val NormalLevel: Any,
            val ReOLevel: Any,
            val ReOQty: Any,
            val FOCLevel: Any,
            val FOCQty: Any,
            val BonusPointQty: Any,
            val BonusPoint: Any,
            val Weight: Any,
            val WeightUOM: Any,
            val Volume: Any,
            val VolumeUOM: Any,
            val BarCode: Any,
            val LastUpdate: String,
            val RedeemBonusPoint: Any,
            val CSGNQty: Any,
            val Price2: Any,
            val Guid: Any,
            val Price3: Any,
            val Price4: Any,
            val Price5: Any,
            val Price6: Any,
            val MarkupRatio: Any,
            val MarkdownRatio2: Any,
            val MarkdownRatio3: Any,
            val MarkdownRatio4: Any,
            val MarkdownRatio5: Any,
            val MarkdownRatio6: Any,
            val MarkdownRatioMinPrice: Any,
            val MarkdownRatioMaxPrice: Any
        )
    }
}