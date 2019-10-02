package com.app.kotlindemo.network

import com.app.googlespreadsheet.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 *
 */
interface ApiServices {
    @GET("exec?action=readAll")
    fun getDataList(@Query("sheetname") sheetname: String): Call<ListDataModel>

    @GET("exec?action=readAll")
    fun getPasswordList(@Query("sheetname") sheetname: String): Call<PasswordResponse>


    @GET("exec?action=insert")
    fun addRecord(@Query("sheetname") sheetname: String, @Query("id") id: String, @Query("date") date: String, @Query("customer_name") customer_name: String, @Query("item_name") item_name: String, @Query("qty") qty: String, @Query("rate") rate: String, @Query("total_amount") total_amount: String, @Query("invoice_number") invoice_number: String, @Query("payment_by") payment_by: String, @Query("remark") remark: String): Call<InsertRecordResponse>

    @GET("exec?action=delete")
    fun deleteRecord(@Query("id") id: String,@Query("sheetname") sheetname: String): Call<DeleteRecordResponse>

    @GET("exec?action=update")
    fun updateRecord(@Query("sheetname") sheetname: String, @Query("id") id: String, @Query("date") date: String, @Query("customer_name") customer_name: String, @Query("item_name") item_name: String, @Query("qty") qty: String, @Query("rate") rate: String, @Query("total_amount") total_amount: String, @Query("invoice_number") invoice_number: String, @Query("payment_by") payment_by: String, @Query("remark") remark: String): Call<UpdateRecordResponse>




}