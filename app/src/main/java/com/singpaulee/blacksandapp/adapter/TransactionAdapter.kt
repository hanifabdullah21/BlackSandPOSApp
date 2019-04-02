package com.singpaulee.blacksandapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.transaction.TransactionDetailBuyActivity
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.model.TransactionModel
import kotlinx.android.synthetic.main.item_transaction.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class TransactionAdapter(val context: Context, val listTransaction: ArrayList<TransactionModel>?) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionAdapter.ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listTransaction?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listTransaction?.get(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(transactionModel: TransactionModel?) {
            itemView.it_tv_nofaktur.text = transactionModel?.noFaktur
            itemView.it_tv_date.text = HelperClass().convertDateYMDhms(transactionModel?.tanggal.toString())

            var debtStatus = if (transactionModel?.hutang!!.toInt() > 0) "Belum Lunas" else "LUNAS"
            itemView.it_tv_status.text = debtStatus

            itemView.onClick {
                var i = Intent(itemView.context, TransactionDetailBuyActivity::class.java)
                i.putExtra("TRANSACTIONID", transactionModel.id)
                itemView.context.startActivity(i)
            }
        }
    }
}