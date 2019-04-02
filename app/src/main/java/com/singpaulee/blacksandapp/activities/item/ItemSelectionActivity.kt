package com.singpaulee.blacksandapp.activities.item

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.model.ItemModel
import kotlinx.android.synthetic.main.activity_item_selection.*
import kotlinx.android.synthetic.main.dialog_add_item_cart.view.*
import kotlinx.android.synthetic.main.item_item_selection.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class ItemSelectionActivity : AppCompatActivity() {

    var listItem: ArrayList<ItemModel>? = null
    var action: String? = null

    lateinit var dialogBuy: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_selection)

        listItem = intent.getParcelableArrayListExtra("LISTITEM")
        action = intent.getStringExtra("ACTION")

        isa_ll_container.removeAllViews()
        for (i in listItem?.indices!!) {
            var layoutInflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var viewItem = layoutInflater.inflate(R.layout.item_item_selection, null)

            viewItem.iis_tv_code.text = listItem?.get(i)?.kode
            viewItem.iis_tv_name.text = listItem?.get(i)?.nama

            viewItem.onClick {
                openDialog(i)
            }

            isa_ll_container.addView(viewItem)
        }

    }

    private fun openDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        val layoutInflater: LayoutInflater = LayoutInflater.from(this)

        var dialog: View = layoutInflater.inflate(R.layout.dialog_add_item_cart, null)

        action?.let {
            dialog.dai_tv_title_total.text = "Jumlah Jual"
            dialog.dai_tv_title_price.text = "Harga Jual"
        } ?: run {
            dialog.dai_tv_title_total.text = "Jumlah Beli"
            dialog.dai_tv_title_price.text = "Harga Beli"
        }

        dialog.dai_tv_code.text = listItem?.get(position)?.kode
        dialog.dai_tv_name.text = listItem?.get(position)?.nama
        dialog.dai_tv_price.text = listItem?.get(position)?.hargaRata.toString()
        dialog.dai_tv_stock.text = listItem?.get(position)?.stok.toString()

        dialog.dai_btn_add.onClick {
            if (!validasi(dialog, position)) {
                return@onClick
            }
            var itemModel = listItem?.get(position)?.copy(
                jumlahPesan = dialog.dai_edt_total.text.toString().toInt(),
                hargaPesan = dialog.dai_edt_price.text.toString().toInt()
            )
            listItem?.removeAt(position)

            var returnIntent: Intent = Intent()
            returnIntent.putExtra("ITEMMODEL", itemModel)
            returnIntent.putExtra("ITEMLIST", listItem)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        builder.setView(dialog)
        dialogBuy = builder.create()
        dialogBuy.setCancelable(true)
        dialogBuy.show()
    }

    private fun validasi(dialog: View, position: Int): Boolean {
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
        action?.let {
            if (dialog.dai_edt_total.text.toString().toInt() > dialog.dai_tv_stock.text.toString().toInt()) {
                dialog.dai_edt_total.error = "Stok tidak mencukupi"
                dialog.dai_edt_total.requestFocus()
                return false
            }
            if (dialog.dai_edt_price.text.toString().toInt() < listItem?.get(position)?.hargaRata!!.toInt()) {
                dialog.dai_edt_price.error = "Harga terlalu rendah"
                dialog.dai_edt_price.requestFocus()
                return false
            }
        }
        return true
    }
}
