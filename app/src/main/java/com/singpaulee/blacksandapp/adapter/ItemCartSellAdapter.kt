package com.singpaulee.blacksandapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.transaction.TransactionAddSellActivity
import com.singpaulee.blacksandapp.model.ItemModel
import kotlinx.android.synthetic.main.dialog_add_item_cart.view.*
import kotlinx.android.synthetic.main.item_item_cart.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.selector

class ItemCartSellAdapter(val context: Context, var listItem: ArrayList<ItemModel>?) :
    RecyclerView.Adapter<ItemCartSellAdapter.ViewHolder>() {

    val TAG = "ItemCartAdapter"
    lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_item_cart, parent, false)
        return ItemCartSellAdapter.ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listItem?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this@ItemCartSellAdapter, listItem, position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var dialogChangeOrder: AlertDialog

        fun bind(itemCartSellAdapter: ItemCartSellAdapter, listItem: ArrayList<ItemModel>?, position: Int) {

            var transactionAddSellActivity = itemView.context as TransactionAddSellActivity
            var menu = listOf<String>("Ubah", "Hapus")

            itemView.iic_tv_name.text = listItem?.get(position)?.nama
            itemView.iic_tv_price.text = listItem?.get(position)?.hargaPesan.toString()
            itemView.iic_tv_total.text = listItem?.get(position)?.jumlahPesan.toString()

            itemView.onClick {
                itemView.context.selector("Pilihan", menu) { dialog, i ->
                    when (menu.get(i)) {
                        "Ubah" -> {
                            openDialogChangeOrder(listItem, position, itemCartSellAdapter)
                        }
                        "Hapus" -> {
                            var newItem = listItem?.get(position)?.copy(jumlahPesan = 0, hargaPesan = 0)
                            transactionAddSellActivity.updateList(newItem)
                            itemCartSellAdapter.removeItem(position)
                            itemCartSellAdapter.notifyListChanged()
                        }
                        else -> {
                        }
                    }
                }
            }
        }

        fun openDialogChangeOrder(
            listItem: ArrayList<ItemModel>?,
            position: Int,
            itemCartSellAdapter: ItemCartSellAdapter
        ) {
            val builder = AlertDialog.Builder(itemView.context)
            val layoutInflater: LayoutInflater = LayoutInflater.from(itemView.context)

            var dialog: View = layoutInflater.inflate(R.layout.dialog_add_item_cart, null)


            dialog.dai_tv_title_total.text = "Jumlah Beli"
            dialog.dai_tv_title_price.text = "Harga Beli"

            dialog.dai_tv_code.text = listItem?.get(position)?.kode
            dialog.dai_tv_name.text = listItem?.get(position)?.nama
            dialog.dai_tv_price.text = listItem?.get(position)?.hargaRata.toString()
            dialog.dai_tv_stock.text = listItem?.get(position)?.stok.toString()
            dialog.dai_edt_total.setText("" + listItem?.get(position)?.jumlahPesan)
            dialog.dai_edt_price.setText("" + listItem?.get(position)?.hargaPesan)

            dialog.dai_btn_add.onClick {
                if (!validasi(dialog)) {
                    return@onClick
                }
                var itemModel = listItem?.get(position)?.copy(
                    jumlahPesan = dialog.dai_edt_total.text.toString().toInt(),
                    hargaPesan = dialog.dai_edt_price.text.toString().toInt()
                )
                itemModel?.let { it1 -> listItem?.set(position, it1) }
                itemCartSellAdapter.updateList(listItem)
                dialogChangeOrder.dismiss()
            }

            builder.setView(dialog)
            dialogChangeOrder = builder.create()
            dialogChangeOrder.setCancelable(true)
            dialogChangeOrder.show()
        }

        fun validasi(dialog: View): Boolean {
            if (dialog.dai_edt_total.text.isBlank()) {
                dialog.dai_edt_total.error = "Tidak Boleh Kosong"
                dialog.dai_edt_total.requestFocus()
                return false
            }
            if (dialog.dai_edt_price.text.isBlank()) {
                dialog.dai_edt_price.error = "Tidak Boleh Kosong"
                dialog.dai_edt_price.requestFocus()
                return false
            }
            return true
        }
    }

    public fun removeItem(position: Int) {
        listItem?.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateList(listItem2: ArrayList<ItemModel>?) {
        listItem = listItem2
        notifyDataSetChanged()

        notifyListChanged()

        Log.d(TAG, listItem.toString())
    }

    fun notifyListChanged() {
        var activity = context as TransactionAddSellActivity
        activity.notifyListCart()
    }

    fun countTotal(): Int {
        var total = 0
        for (i in listItem?.indices!!) {
            total += (listItem?.get(i)?.hargaPesan!! * listItem?.get(i)?.jumlahPesan!!)
        }
        return total
    }
}