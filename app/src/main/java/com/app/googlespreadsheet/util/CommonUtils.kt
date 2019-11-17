package com.app.kotlindemo.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Editable
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * TODO: Add a class header comment!
 */
class CommonUtils {
    companion object {
        //app id ca-app-pub-4326312080857500~2915883709
        //banner add id ca-app-pub-4326312080857500/9034074653

        var progressBar: ProgressBar? = null

        fun isInternetAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            return isConnected
        }

        fun showProgress(context: Context) {
            progressBar = ProgressBar(context)
            val params = RelativeLayout.LayoutParams(100, 100)
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
        }

        fun changeScreenTitle(activity: BaseActivity, screenName: String) {
            (activity as AppCompatActivity).supportActionBar!!.setTitle(screenName)
        }

        fun displayToastShort(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun displayToastLong(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun validateControl(context: Context, editText: EditText): Boolean {
            var result: Boolean = false
            if (!editText.text.toString().isEmpty())
                result = true
            return result
        }

        fun getCurrentDate(dateFormat: String): String {
            val sdf = SimpleDateFormat(dateFormat)
            val currentDate = sdf.format(Date())
            return currentDate.toString()
        }

        fun getConvertedDate(strDate: String): String {
            var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            spf.timeZone= TimeZone.getTimeZone("UTC")
            val newDate = spf.parse(strDate)
            spf = SimpleDateFormat("dd/MM/yyyy")
            return spf.format(newDate)
        }

        fun hideKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }
}