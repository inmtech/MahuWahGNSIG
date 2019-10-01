package com.app.kotlindemo.network

/**
 * TODO: Add a class header comment!
 */
interface RetrofitListener {
    fun <T> retrofitOnSuccessResponse(response:T)
    fun <T>retrofitOnFailureResponse(fail:T)
}