package com.app.googlespreadsheet.model

import com.google.gson.annotations.SerializedName


/**
 * TODO: Add a class header comment!
 */
data class InsertRecordResponse(
        @SerializedName("result")
        var result: String = "",
        @SerializedName("status")
        var status: Int = 0
)