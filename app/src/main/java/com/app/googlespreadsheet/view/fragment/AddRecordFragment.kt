package com.app.googlespreadsheet.view.fragment


import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.model.InsertRecordResponse
import com.app.googlespreadsheet.model.ListDataModel
import com.app.googlespreadsheet.model.UpdateRecordResponse
import com.app.googlespreadsheet.util.SharedPrefsUtil
import com.app.kotlindemo.network.ApiClient
import com.app.kotlindemo.network.ApiPresenter
import com.app.kotlindemo.network.ApiServices
import com.app.kotlindemo.network.RetrofitListener
import com.app.kotlindemo.util.BaseFragment
import com.app.kotlindemo.util.CommonUtils
import com.app.kotlindemo.view.listener.listClickListener
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList


/**
 *
 */
class AddRecordFragment : BaseFragment(), RetrofitListener, listClickListener, View.OnClickListener {

    var listData: ArrayList<ListDataModel.Record> = ArrayList()
    var apiServices: ApiServices? = null
    var recordId = 0
    var selectedProductName: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmer_view_container.startShimmer()
        btnAddRecord.setOnClickListener(this)
        btnUpdateRecord.setOnClickListener(this)
        btnDeleteRecord.setOnClickListener(this)
        ivSelectDate.setOnClickListener(this)
        (activity as AppCompatActivity).supportActionBar!!.title =
            SharedPrefsUtil.getInstance().getSheetName(requireContext()) + " Unit"
        edtxtDate.setText(CommonUtils.getCurrentDate("dd/MM/yyyy"))
        setProductSpinner()
    }

    private fun init() {
        apiServices = ApiClient().getClient()?.create(ApiServices::class.java)
        callWSGetList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return inflater.inflate(com.app.googlespreadsheet.R.layout.fragment_home, container, false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
           R.id.btnAddRecord -> if (validateControls()) callAddRecord()

           R.id.btnUpdateRecord -> if (validateControls()) callUpdateRecord()

           R.id.btnDeleteRecord -> if (validateControls()) {
            }

           R.id.ivSelectDate -> openDatePickerDialog()

        }
    }

    private fun openDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year,   monthOfYear, dayOfMonth ->
                month = month + 1
                edtxtDate.setText("" + dayOfMonth + "/" + month + "/" + year)
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun callWSGetList() {
        var call: Call<ListDataModel>? = null
        call = apiServices?.getDataList(SharedPrefsUtil.getInstance().getSheetName(requireContext()))
        ApiPresenter.call(call, this, requireContext())
    }

    override fun <T> retrofitOnSuccessResponse(success: T) {
        Log.i("Response",  "" + success)
        Handler().postDelayed(object : Runnable {
            override fun run() {
                var arrlistData: ListDataModel
                if (success is ListDataModel) {
                    arrlistData = success as ListDataModel
                    listData= ArrayList()
                    listData?.clear()
                    listData?.addAll(arrlistData.records)
                    if (recyclerviewDataList != null) {
                        hideShimmer()
                    }
                }

                var insertRecordResponse: InsertRecordResponse
                if (success is InsertRecordResponse) {
                    insertRecordResponse = success
                    if (insertRecordResponse.status == 1) {
                        CommonUtils.displayToastShort(requireContext(), insertRecordResponse.result)
                        edtxtDate.setText("")
                        edtxtItemName.setText("")
                        edtxtCustomerName.setText("")
                        edtxtTotalAmt.setText("")
                        edtxtQty.setText("")
                        edtxtRate.setText("")
                        edtxtTotalAmt.setText("")
                        edtxtInvoiceNumber.setText("")
                        edtxtPaymentBy.setText("")
                        edtxtRemark.setText("")
                        setProductSpinner()
                        callWSGetList()
                    }
                }


                var updateRecordResponse: UpdateRecordResponse
                if (success is UpdateRecordResponse) {
                    updateRecordResponse = success
                    if (updateRecordResponse.status == 1) {
                        CommonUtils.displayToastShort(requireContext(), updateRecordResponse.result)
                        edtxtCustomerName.setText("")
                        edtxtTotalAmt.setText("")
                        callWSGetList()
                    }
                }


            }
        }, 200)
    }

    override fun <T> retrofitOnFailureResponse(fail: T) {
        hideShimmer()
    }

    private fun hideShimmer() {
        if (shimmer_view_container.isShimmerStarted) {
            shimmer_view_container.stopShimmer()
            shimmer_view_container.visibility = View.GONE
        }
    }

    override fun <T> onListItemClickListener(position: Int, listObj: T) {
        var listData: ListDataModel.Record
        listData = listObj as ListDataModel.Record
        recordId = listData.id
        edtxtCustomerName.setText("" + listData.itemName)
        edtxtTotalAmt.setText("" + listData.totalAmount)
    }

    private fun validateControls(): Boolean {
        if (!CommonUtils.validateControl(requireContext(), edtxtDate)) {
            CommonUtils.displayToastShort(requireContext(), getString(R.string.warn_enter_date))
            return false
        } else if (!CommonUtils.validateControl(requireContext(), edtxtCustomerName)) {
            CommonUtils.displayToastShort(requireContext(), getString(R.string.warn_required_cutname))
            return false
        } else if (spinnerProductName.selectedItem.equals(getString(R.string.warn_productname))) {
            CommonUtils.displayToastShort(requireContext(), getString(R.string.warn_productname))
            return false
        } else if (!CommonUtils.validateControl(requireContext(), edtxtQty)) {
            CommonUtils.displayToastShort(requireContext(), getString(R.string.warn_qty))
            return false
        } else if (!CommonUtils.validateControl(requireContext(), edtxtRate)) {
            CommonUtils.displayToastShort(requireContext(), getString(R.string.warn_rate))
            return false
        } else if (!CommonUtils.validateControl(requireContext(), edtxtTotalAmt)) {
            CommonUtils.displayToastShort(requireContext(), getString(R.string.warn_totalamt))
            return false
        } else if (!CommonUtils.validateControl(requireContext(), edtxtInvoiceNumber)) {
            CommonUtils.displayToastShort(requireContext(), getString(R.string.warn_invoice))
            return false
        } else if (!CommonUtils.validateControl(requireContext(), edtxtPaymentBy)) {
            CommonUtils.displayToastShort(requireContext(), getString(R.string.warn_paymentby))
            return false
        } else {
            return true
        }
    }

    private fun callAddRecord() {
        if (listData.size > 0) {
            for (i in 0 until listData.size) {
                if (i == listData.size - 1) {
                    recordId = listData.get(i).id
                    recordId = recordId + 1
                }
            }
        }
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()
        var call: Call<InsertRecordResponse>? = null
        call = apiServices?.addRecord(
            SharedPrefsUtil.getInstance().getSheetName(requireContext()),
            "" + recordId,
            edtxtDate.getText().toString(),
            edtxtCustomerName.getText().toString(),
            spinnerProductName.selectedItem.toString(),
            edtxtQty.getText().toString(),
            edtxtRate.getText().toString(),
            edtxtTotalAmt.getText().toString(),
            edtxtInvoiceNumber.getText().toString(),
            edtxtPaymentBy.getText().toString(),
            edtxtRemark.getText().toString()
        )
        ApiPresenter.call(call, this, requireContext())
    }


    private fun callUpdateRecord() {
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()
        var call: Call<UpdateRecordResponse>? = null
        call =
            apiServices?.updateRecord(
                "Masala",
                "" + recordId,
                edtxtDate.getText().toString(),
                edtxtCustomerName.getText().toString(),
                spinnerProductName.selectedItem.toString(),
                edtxtQty.getText().toString(),
                edtxtRate.getText().toString(),
                edtxtTotalAmt.getText().toString(),
                edtxtInvoiceNumber.getText().toString(),
                edtxtPaymentBy.getText().toString(),
                edtxtRemark.getText().toString()
            )
        ApiPresenter.call(call, this, requireContext())
    }

    private fun setProductSpinner() {
        var arrayProductName: Array<String>? = null
        if (SharedPrefsUtil.getInstance().getSheetName(requireContext()).equals(getString(com.app.googlespreadsheet.R.string.kahakhara_unit))) {
            arrayProductName = resources.getStringArray(com.app.googlespreadsheet.R.array.khakhara_product)
        } else if (SharedPrefsUtil.getInstance().getSheetName(requireContext()).equals(getString(com.app.googlespreadsheet.R.string.masala_unit))) {
            arrayProductName = resources.getStringArray(com.app.googlespreadsheet.R.array.spices_product)
        } else if (SharedPrefsUtil.getInstance().getSheetName(requireContext()).equals(getString(com.app.googlespreadsheet.R.string.bakery_unit))) {
            arrayProductName = resources.getStringArray(com.app.googlespreadsheet.R.array.cookies_product)
        }
        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayProductName)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProductName.setAdapter(aa)
    }

}
