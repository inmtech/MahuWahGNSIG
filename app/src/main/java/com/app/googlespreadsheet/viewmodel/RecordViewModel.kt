package com.app.googlespreadsheet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.app.googlespreadsheet.model.ListDataModel
import com.app.googlespreadsheet.model.ListDataModel.Record


class RecordViewModel : ViewModel() {

    val data = MutableLiveData<ListDataModel.Record>()

    fun setRecordData(item: ListDataModel.Record) {
        data.value = item
    }

    fun getRecordData(): MutableLiveData<Record> {
        return data
    }

}