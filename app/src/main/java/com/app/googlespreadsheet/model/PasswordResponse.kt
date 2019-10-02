package com.app.googlespreadsheet.model
import com.google.gson.annotations.SerializedName


data class PasswordResponse(
    @SerializedName("records")
    var records: List<Record> = listOf()
)
{
    data class Record(
            @SerializedName("password")
            var password: String = ""
    )
}

