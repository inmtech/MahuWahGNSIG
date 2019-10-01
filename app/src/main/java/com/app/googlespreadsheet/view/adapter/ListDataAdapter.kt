package com.app.googlespreadsheet.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.model.ListDataModel
import com.app.kotlindemo.util.CommonUtils
import com.app.kotlindemo.view.listener.listClickListener
import kotlinx.android.synthetic.main.raw_data.view.*


class ListDataAdapter(
        val items: ArrayList<ListDataModel.Record>,
        val context: Context,
        var listClickListener: listClickListener
) :
        RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.raw_data,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var record: ListDataModel.Record = items.get(position)
        holder.tvName.text = "Customer Name:" + record.customerName
        holder.tvInvoice.text = "Invoice Number:" + record.invoiceNumber
        holder.tvDate.text = "Date:" + CommonUtils.getConvertedDate(record.date)
        holder.tvItem.text = "Product Name:" + record.itemName
        holder.tvNumber.text = "Total Amount:" + record.totalAmount
        holder.itemView.setOnClickListener(View.OnClickListener {
            listClickListener.onListItemClickListener(position, record)
        })

        holder.imgDelete.setOnClickListener(View.OnClickListener {
            listClickListener.onListItemClickListener(-1, record)
        })

        holder.imgEdit.setOnClickListener(View.OnClickListener {
            listClickListener.onListItemClickListener(-2, record)
        })
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.tvCustomerName
    val tvNumber = view.tvAmt
    val tvItem = view.tvItemName
    val tvDate = view.tvDate
    val tvInvoice = view.tvInvoice
    val imgEdit = view.imgEdit
    val imgDelete = view.imgDelete
}