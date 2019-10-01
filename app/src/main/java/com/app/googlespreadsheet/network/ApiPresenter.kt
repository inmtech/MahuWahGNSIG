package com.app.kotlindemo.network

import android.content.Context
import android.widget.Toast
import com.app.kotlindemo.util.CommonUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 *
 */
class ApiPresenter {
    companion object {
        fun <T> call(call: Call<T>?, retrofitListener: RetrofitListener, context: Context) {
            if (CommonUtils.isInternetAvailable(context)) {
                call?.enqueue(object : Callback<T> {
                    override fun onFailure(call: Call<T>?, t: Throwable?) {
                        retrofitListener?.retrofitOnFailureResponse(t)
//                        Toast.makeText(context, "Failure:" + t?.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<T>?, response: Response<T>?) {
                        retrofitListener?.retrofitOnSuccessResponse(response?.body())
                    }
                })
            } else {
                Toast.makeText(context, "Internet is not available.", Toast.LENGTH_LONG).show()
            }
        }
    }
}