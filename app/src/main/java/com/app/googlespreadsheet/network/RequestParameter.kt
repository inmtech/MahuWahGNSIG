package com.app.kotlindemo.network

import com.google.gson.JsonObject

/**
 * TODO: Add a class header comment!
 */
class RequestParameter {

    companion object {
        fun getListData(apikey: String, userid: String): JsonObject {
            var jsonObject: JsonObject? = null
            jsonObject = JsonObject()
            jsonObject?.addProperty("api_key", apikey)
            jsonObject?.addProperty("userid", userid)
            return jsonObject!!
        }
    }
}