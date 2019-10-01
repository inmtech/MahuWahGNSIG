package com.app.kotlindemo.network

import com.app.googlespreadsheet.util.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * TODO: Add a class header comment!
 */
public class ApiClient {

    private var retrofit: Retrofit? = null

    public fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(Config.scriptUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

}