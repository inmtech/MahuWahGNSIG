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
import androidx.lifecycle.ViewModelProviders
import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.model.ListDataModel
import com.app.googlespreadsheet.model.UpdateRecordResponse
import com.app.googlespreadsheet.util.Config
import com.app.googlespreadsheet.util.SharedPrefsUtil
import com.app.googlespreadsheet.viewmodel.RecordViewModel
import com.app.kotlindemo.network.ApiClient
import com.app.kotlindemo.network.ApiPresenter
import com.app.kotlindemo.network.ApiServices
import com.app.kotlindemo.network.RetrofitListener
import com.app.kotlindemo.util.BaseActivity
import com.app.kotlindemo.util.BaseFragment
import com.app.kotlindemo.util.CommonUtils
import com.app.kotlindemo.util.PreferenceHelper
import com.app.kotlindemo.view.listener.listClickListener
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import java.util.*


/**
 *
 */
class EditRecordFragment : BaseFragment(), RetrofitListener, listClickListener, View.OnClickListener {


    var apiServices: ApiServices? = null
    var recordId = 0
    var viewModel: RecordViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmer_view_container.visibility = View.GONE
        btnAddRecord.setOnClickListener(this)
        btnUpdateRecord.setOnClickListener(this)
        btnDeleteRecord.setOnClickListener(this)
        ivSelectDate.setOnClickListener(this)
        (activity as AppCompatActivity).supportActionBar!!.title =
                SharedPrefsUtil.getInstance().getSheetName(requireContext()) + " Unit"
        edtxtDate.setText(CommonUtils.getCurrentDate("dd/MM/yyyy"))
        viewModel?.getRecordData()?.observe(this, androidx.lifecycle.Observer {
            setUpDataFromList(it)
        })
    }

    private fun setUpDataFromList(it: ListDataModel.Record?) {
        recordId = it?.id!!
        edtxtDate.setText(CommonUtils.getConvertedDate(it?.date))
        setProductSpinner(it?.itemName)
        setPaymentBySpinner(it?.paymentBy)
        edtxtRemark.setText(it?.remark)
        edtxtTotalAmt.setText("" + it?.totalAmount)
        edtxtPaymentBy.setText(it?.paymentBy)
        edtxtInvoiceNumber.setText("" + it?.invoiceNumber)
        edtxtQty.setText("" + it?.quantity)
        edtxtCustomerName.setText(it?.customerName)
        edtxtRate.setText("" + it?.rate)
        edtxtPaymentBy.setText(it?.paymentBy)
    }


    private fun init() {
        viewModel = activity?.let { ViewModelProviders.of(it).get(RecordViewModel::class.java) }
        apiServices = ApiClient().getClient()?.create(ApiServices::class.java)
        callWSGetList()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        init()
        return inflater.inflate(R.layout.fragment_edit_record, container, false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btnUpdateRecord -> if (validateControls()) callUpdateRecord()


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
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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
        call = apiServices?.getDataList(
                PreferenceHelper.defaultPrefs(requireContext()).getString(
                        Config.unitkey,
                        getString(R.string.masala_unit)
                )
        )
        ApiPresenter.call(call, this, requireContext())
    }

    override fun <T> retrofitOnSuccessResponse(success: T) {
        Log.i("Response", "" + success)
        Handler().postDelayed(object : Runnable {
            override fun run() {
                var updateRecordResponse: UpdateRecordResponse
                if (success is UpdateRecordResponse) {
                    updateRecordResponse = success
                    if (updateRecordResponse.status == 1) {
                        CommonUtils.displayToastShort(requireContext(), updateRecordResponse.result)
                        hideShimmer()
                        replaceFragment(requireContext(), DisplayRecordFragment(), activity as BaseActivity)
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
        } else if (spinnerPaymentBy.selectedItem.equals(getString(R.string.warn_payment_by_method))) {
            CommonUtils.displayToastShort(requireContext(), getString(R.string.warn_paymentby))
            return false
        } else {
            return true
        }
    }

    private fun callUpdateRecord() {
        CommonUtils.hideKeyboard(requireContext(), btnUpdateRecord)
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()
        var call: Call<UpdateRecordResponse>? = null
        call =
                apiServices?.updateRecord(
                        SharedPrefsUtil.getInstance().getSheetName(requireContext()),
                        "" + recordId,
                        edtxtDate.getText().toString(),
                        edtxtCustomerName.getText().toString(),
                        spinnerProductName.selectedItem.toString(),
                        edtxtQty.getText().toString(),
                        edtxtRate.getText().toString(),
                        edtxtTotalAmt.getText().toString(),
                        edtxtInvoiceNumber.getText().toString(),
                        spinnerPaymentBy.selectedItem.toString(),
                        edtxtRemark.getText().toString()
                )
        ApiPresenter.call(call, this, requireContext())
    }

    private fun setProductSpinner(itemName: String?) {
        var arrayProductName: Array<String>? = null
        if (SharedPrefsUtil.getInstance().getSheetName(requireContext()).equals(getString(R.string.kahakhara_unit))) {
            arrayProductName = resources.getStringArray(R.array.khakhara_product)
        } else if (SharedPrefsUtil.getInstance().getSheetName(requireContext()).equals(getString(R.string.masala_unit))) {
            arrayProductName = resources.getStringArray(R.array.spices_product)
        } else if (SharedPrefsUtil.getInstance().getSheetName(requireContext()).equals(getString(R.string.bakery_unit))) {
            arrayProductName = resources.getStringArray(R.array.cookies_product)
        }
        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayProductName)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProductName.setAdapter(aa)
        if (arrayProductName != null) {
            for (i in 0 until arrayProductName.size) {
                if (itemName.equals(arrayProductName[i])) {
                    spinnerProductName.setSelection(i)
                }
            }
        }
    }


    private fun setPaymentBySpinner(itemName: String) {
        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, resources.getStringArray(R.array.payment_method))
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPaymentBy.setAdapter(aa)
        for (i in 0 until resources.getStringArray(R.array.payment_method).size) {
            if (itemName.equals(resources.getStringArray(R.array.payment_method).get(i))) {
                spinnerPaymentBy.setSelection(i)
            }
        }

    }
}
