package com.singpaulee.blacksandapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.item.ItemDetailActivity
import com.singpaulee.blacksandapp.model.ItemModel
import kotlinx.android.synthetic.main.item_item.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class ItemAdapter(val context: Context, val list: ArrayList<ItemModel>?) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.get(position)?.let { holder.bind(it) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemModel: ItemModel) {
            itemView.ii_tv_code.text = "#"+itemModel.kode.toString()
            itemView.ii_tv_name.text = itemModel.nama.toString()
            itemView.ii_tv_price.text = itemModel.hargaRata.toString()
            itemView.ii_tv_stock.text = "Stok: "+itemModel.stok.toString()
            when {
                itemModel.stok!!.toInt() > itemModel.stokMinimal!!.toInt()+3 ->
                    //TODO HIDE NOTIF STOCK
                    itemView.ii_tv_notif_stock.gone()
                itemModel.stok!!.toInt() > itemModel.stokMinimal!!.toInt() -> {
                    itemView.ii_tv_notif_stock.visible()
                    itemView.ii_tv_notif_stock.text = "Stok hampir mencapai batas minimal"
                }
                itemModel.stok!!.toInt() < itemModel.stokMinimal!!.toInt() -> {
                    itemView.ii_tv_notif_stock.visible()
                    itemView.ii_tv_notif_stock.text = "Stok hampir habis"
                }
                itemModel.stok!!.toInt() == 0 -> {
                    itemView.ii_tv_notif_stock.visible()
                    itemView.ii_tv_notif_stock.text = "Stok habis"
                }
            }

            itemView.onClick {
                var i = Intent(itemView.context, ItemDetailActivity::class.java)
                i.putExtra("ITEM", itemModel)
                itemView.context.startActivity(i)
            }
        }

        private fun View.visible() {
            visibility = View.VISIBLE
        }

        private fun View.gone() {
            visibility = View.GONE
        }
    }
}
