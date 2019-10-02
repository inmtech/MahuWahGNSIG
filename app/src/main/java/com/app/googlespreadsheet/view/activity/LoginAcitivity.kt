package com.app.googlespreadsheet.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.model.ListDataModel
import com.app.googlespreadsheet.model.PasswordResponse
import com.app.googlespreadsheet.util.Config
import com.app.googlespreadsheet.util.SharedPrefsUtil
import com.app.googlespreadsheet.viewmodel.RecordViewModel
import com.app.kotlindemo.network.ApiClient
import com.app.kotlindemo.network.ApiPresenter
import com.app.kotlindemo.network.ApiServices
import com.app.kotlindemo.network.RetrofitListener
import com.app.kotlindemo.util.BaseActivity
import com.app.kotlindemo.util.CommonUtils
import com.app.kotlindemo.util.PreferenceHelper
import kotlinx.android.synthetic.main.activity_login_acitivity.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call

class LoginAcitivity : BaseActivity(), RetrofitListener, View.OnClickListener {
    var apiServices: ApiServices? = null
    var context: Context? = null

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnLogin -> if (validateControls()) {
                CommonUtils.hideKeyboard(this, btnLogin)
                callWSGetList()
            }
        }
    }

    override fun <T> retrofitOnSuccessResponse(success: T) {
        Log.i("Response", "" + success)
        Handler().postDelayed(object : Runnable {
            override fun run() {
                var arrlistData: ArrayList<PasswordResponse.Record> = ArrayList()
                if (success is PasswordResponse) {
                    arrlistData.addAll(success.records)
                    var isPasswordValied = false
                    for (i in 0 until arrlistData.size) {
                        if (edtxtPassword.getText().toString().equals(arrlistData.get(i).password)) {
                            isPasswordValied = true
                            context?.let { SharedPrefsUtil.getInstance().setLogin(it, true) }
                            startActivity(Intent(context, UnitSelectionActivity::class.java))
                            finish()
                        }
                    }
                    if (!isPasswordValied) {
                        context?.let { CommonUtils.displayToastShort(it, getString(R.string.warn_password_incorrect)) }
                        loginContainer.visibility = View.VISIBLE
                        tvLoading.visibility = View.GONE
                        edtxtPassword.setText("")
                    }
                }
            }
        }, 200)
    }

    override fun <T> retrofitOnFailureResponse(fail: T) {
        Log.e("SERVICE FAIL", "" + fail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_acitivity)
        context = this
        btnLogin.setOnClickListener(this)
        apiServices = ApiClient().getClient()?.create(ApiServices::class.java)
    }

    private fun validateControls(): Boolean {
        if (!CommonUtils.validateControl(this, edtxtPassword)) {
            CommonUtils.displayToastShort(this, getString(R.string.warn_enter_password))
            return false
        } else {
            return true
        }
    }


    private fun callWSGetList() {
        loginContainer.visibility = View.GONE
        tvLoading.visibility = View.VISIBLE
        var call: Call<PasswordResponse>? = null
        call = apiServices?.getPasswordList(getString(R.string.sheetname_password))
        ApiPresenter.call(call, this, this)
    }
}
