package com.app.googlespreadsheet.model

import com.google.gson.annotations.SerializedName


/**
 * TODO: Add a class header comment!
 */
data class ListDataModel(
    @SerializedName("records")
    var records: List<Record> = listOf()
) {
    data class Record(
        @SerializedName("customer_name")
        var customerName: String = "",
        @SerializedName("date")
        var date: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("invoice_number")
        var invoiceNumber: Double = 0.0,
        @SerializedName("item_name")
        var itemName: String = "",
        @SerializedName("payment_by")
        var paymentBy: String = "",
        @SerializedName("quantity")
        var quantity: Double = 0.0,
        @SerializedName("rate")
        var rate: Double = 0.0,
        @SerializedName("remark")
        var remark: String = "",
        @SerializedName("total_amount")
        var totalAmount: Double = 0.0
    )
}
