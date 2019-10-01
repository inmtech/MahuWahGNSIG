package com.app.googlespreadsheet.view.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.model.ListDataModel
import com.app.googlespreadsheet.util.Config
import com.app.googlespreadsheet.util.SharedPrefsUtil
import com.app.googlespreadsheet.viewmodel.RecordViewModel
import com.app.kotlindemo.network.ApiPresenter
import com.app.kotlindemo.network.ApiServices
import com.app.kotlindemo.network.RetrofitListener
import com.app.kotlindemo.util.BaseActivity
import com.app.kotlindemo.util.CommonUtils
import com.app.kotlindemo.util.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call

class LoginAcitivity : BaseActivity(), RetrofitListener, View.OnClickListener {
    var apiServices: ApiServices? = null

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnLogin -> if (validateControls()) {
                callWSGetList()
            }
        }
    }

    override fun <T> retrofitOnSuccessResponse(success: T) {
        Log.i("Response", "" + success)
        Handler().postDelayed(object : Runnable {
            override fun run() {
                var arrlistData: ListDataModel
                if (success is ListDataModel) {
                    arrlistData = success as ListDataModel

                }
            }
        }, 200)
    }

    override fun <T> retrofitOnFailureResponse(fail: T) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_acitivity)
    }

    private fun validateControls(): Boolean {
        if (!CommonUtils.validateControl(this, edtxtDate)) {
            CommonUtils.displayToastShort(this, getString(R.string.warn_enter_date))
            return false
        } else {
            return true
        }
    }


    private fun callWSGetList() {
//        PreferenceHelper.defaultPrefs(this).getStringSet(Config.unitkey,getString(R.string.masala_unit));
        var call: Call<ListDataModel>? = null
        call = apiServices?.getDataList(
                PreferenceHelper.defaultPrefs(this).getString(
                        Config.unitkey,
                        SharedPrefsUtil.getInstance().getSheetName(this)
                )
        )
        ApiPresenter.call(call, this, this)
    }
}
