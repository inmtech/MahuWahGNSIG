package com.app.googlespreadsheet.view.fragment


import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.model.DeleteRecordResponse
import com.app.googlespreadsheet.model.ListDataModel
import com.app.googlespreadsheet.util.Config
import com.app.googlespreadsheet.util.SharedPrefsUtil
import com.app.googlespreadsheet.view.adapter.ListDataAdapter
import com.app.kotlindemo.network.ApiClient
import com.app.kotlindemo.network.ApiPresenter
import com.app.kotlindemo.network.ApiServices
import com.app.kotlindemo.network.RetrofitListener
import com.app.kotlindemo.util.BaseFragment
import com.app.kotlindemo.util.CommonUtils
import com.app.kotlindemo.util.PreferenceHelper
import com.app.kotlindemo.view.listener.listClickListener
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.lifecycle.ViewModelProviders
import com.app.googlespreadsheet.viewmodel.RecordViewModel
import com.app.kotlindemo.util.BaseActivity


/**
 * A simple [Fragment] subclass.
 *
 */
class DisplayRecordFragment : BaseFragment(), RetrofitListener, listClickListener {
    var listData: ArrayList<ListDataModel.Record> = ArrayList()
    var apiServices: ApiServices? = null
    var recordId = 0
    var viewModel: RecordViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return inflater.inflate(R.layout.fragment_display_record, container, false)
    }

    private fun init() {
        apiServices = ApiClient().getClient()?.create(ApiServices::class.java)
        callWSGetList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmer_view_container.startShimmer()
        setRecyclerviewAdapter()
        viewModel = activity?.let { ViewModelProviders.of(it).get(RecordViewModel::class.java) }
    }

    private fun setRecyclerviewAdapter() {
        recyclerviewDataList.layoutManager = LinearLayoutManager(requireContext())
        recyclerviewDataList.adapter =
            ListDataAdapter(listData, requireContext(), this)
    }

    override fun <T> retrofitOnSuccessResponse(success: T) {
        Log.i("Response", "" + success)
        Handler().postDelayed(object : Runnable {
            override fun run() {
                var arrlistData: ListDataModel
                if (success is ListDataModel) {
                    arrlistData = success as ListDataModel
                    listData?.clear()
                    listData?.addAll(arrlistData.records)
                    setRecyclerviewAdapter()
                    if (recyclerviewDataList != null) {
                        hideShimmer()
                    }
                }

                var deleteRecordResponse: DeleteRecordResponse
                if (success is DeleteRecordResponse) {
                    deleteRecordResponse = success
                    if (deleteRecordResponse.status == 1) {
                        CommonUtils.displayToastShort(requireContext(), deleteRecordResponse.result)
                        callWSGetList()
                    }
                }
            }
        }, 200)
    }

    override fun <T> retrofitOnFailureResponse(fail: T) {
        hideShimmer()
    }

    private fun callWSGetList() {
//        PreferenceHelper.defaultPrefs(requireContext()).getStringSet(Config.unitkey,getString(R.string.masala_unit));
        var call: Call<ListDataModel>? = null
        call = apiServices?.getDataList(
            PreferenceHelper.defaultPrefs(requireContext()).getString(
                Config.unitkey,
                SharedPrefsUtil.getInstance().getSheetName(requireContext())
            )
        )
        ApiPresenter.call(call, this, requireContext())
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

        if (position == -2) {
            viewModel?.setRecordData(listData)
            replaceFragment(requireContext(), EditRecordFragment(), activity as BaseActivity)
        }
        if (position == -1) {
            showDeleteAlert()
        }
    }

    fun showDeleteAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(getString(R.string.alertMessageForDeleteItem))
        builder.setPositiveButton(getString(R.string.alertYes)) { dialog, which ->
            callDeleteRecord()
            dialog.dismiss()
        }
        builder.setNegativeButton(
            getString(
                R.string
                    .alertNo
            )
        ) { dialog, which ->

            dialog.dismiss()
        }
        /*// Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->
        }*/
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun callDeleteRecord() {
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()
        var call: Call<DeleteRecordResponse>? = null
        call = apiServices?.deleteRecord("" + recordId, SharedPrefsUtil.getInstance().getSheetName(requireContext()))
        ApiPresenter.call(call, this, requireContext())
    }
}
