package com.singpaulee.blacksandapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.supplier.SupplierDetailActivity
import com.singpaulee.blacksandapp.model.SupplierModel
import kotlinx.android.synthetic.main.item_buyer.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class SupplierAdapter(val context: Context, val list: ArrayList<SupplierModel>?) :
    RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {

    lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_buyer, parent, false)
        return SupplierAdapter.ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.get(position)?.let { holder.bind(it) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(supplierModel: SupplierModel){
            itemView.ib_tv_name.text = supplierModel.nama.toString()
            itemView.ib_tv_address.text = supplierModel.alamat.toString()

            itemView.onClick {
                var i = Intent(itemView.context, SupplierDetailActivity::class.java)
                i.putExtra("SUPPLIER", supplierModel)
                itemView.context.startActivity(i)
            }
        }

    }
}