package com.singpaulee.blacksandapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.buyer.BuyerDetailActivity
import com.singpaulee.blacksandapp.model.BuyerModel
import com.singpaulee.blacksandapp.model.EmployeeModel
import kotlinx.android.synthetic.main.item_buyer.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class EmployeeAdapter(val context: Context, val list: ArrayList<EmployeeModel>?) :
    RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_buyer, parent, false)
        return EmployeeAdapter.ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.get(position)?.let { holder.bind(it) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: EmployeeModel) {
            Log.d("BuyerAdapter", model.toString())
            itemView.ib_tv_name.text = model.nama.toString()
            itemView.ib_tv_address.text = model.alamat.toString()

            itemView.onClick {
                var i = Intent(itemView.context, BuyerDetailActivity::class.java)
                i.putExtra("EMPLOYEE", model)
                itemView.context.startActivity(i)
            }
        }

    }
}