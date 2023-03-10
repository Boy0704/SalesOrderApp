package com.apps.salesorder.data.response

data class DebtorResp(
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
            val CompanyName: String,
            val Desc2: String,
            val RegisterNo: Any,
            val Address1: String,
            val Address2: String,
            val Address3: String,
            val Address4: String,
            val PostCode: Any,
            val DeliverAddr1: String,
            val DeliverAddr2: String,
            val DeliverAddr3: String,
            val DeliverAddr4: String,
            val DeliverPostCode: Any,
            val Attention: Any,
            val Phone1: String,
            val Phone2: Any,
            val Fax1: Any,
            val Fax2: Any,
            val AreaCode: Any,
            val SalesAgent: Any,
            val DebtorType: Any,
            val NatureOfBusiness: Any,
            val WebURL: Any,
            val EmailAddress: Any,
            val DisplayTerm: String,
            val CreditLimit: String,
            val AgingOn: String,
            val StatementType: String,
            val CurrencyCode: String,
            val AllowExceedCreditLimit: String,
            val Note: Any,
            val ExemptNo: Any,
            val ExpiryDate: Any,
            val PriceCategory: Any,
            val TaxType: Any,
            val DiscountPercent: String,
            val DetailDiscount: Any,
            val LastModified: String,
            val LastModifiedUserID: String,
            val CreatedTimeStamp: String,
            val CreatedUserID: String,
            val OverdueLimit: String,
            val HasBonusPoint: String,
            val OpeningBonusPoint: String,
            val QTBlockStatus: String,
            val SOBlockStatus: String,
            val DOBlockStatus: String,
            val IVBlockStatus: String,
            val CSBlockStatus: String,
            val QTBlockMessage: Any,
            val SOBlockMessage: Any,
            val DOBlockMessage: Any,
            val IVBlockMessage: Any,
            val CSBlockMessage: Any,
            val ExternalLink: Any,
            val IsGroupCompany: String,
            val IsActive: String,
            val LastUpdate: String,
            val ContactInfo: Any,
            val AccountGroup: Any,
            val MarkupRatio: Any,
            val TaxRegisterNo: Any,
            val CalcDiscountOnUnitPrice: String,
            val GSTStatusVerifiedDate: Any,
            val InclusiveTax: String,
            val RoundingMethod: String,
            val SelfBilledApprovalNo: String,
            val Guid: Any,
            val IsTaxRegistered: Any,
            val ReceiptWithholdingTaxCode: Any,
            val PaymentWithholdingTaxCode: Any,
            val MultiPrice: Any,
            val AllowChangeMultiPrice: Any,
            val TaxBranchID: Any,
            val ServiceTaxRegisterNo: Any,
            val Mobile: Any
        )
    }
}