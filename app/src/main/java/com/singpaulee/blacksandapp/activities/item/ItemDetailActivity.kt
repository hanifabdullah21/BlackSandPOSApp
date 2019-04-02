package com.singpaulee.blacksandapp.activities.item

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.widget.TableRow
import android.widget.TextView
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.ItemModel
import com.singpaulee.blacksandapp.model.ItemResultListModel
import com.singpaulee.blacksandapp.model.ReportResultListDataModel2
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_list_report_monthly.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class ItemDetailActivity : AppCompatActivity() {

    val TAG = "ItemDetail"

    var itemModel: ItemModel? = null
    var itemTransactionModel: ArrayList<ItemModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        itemModel = intent.getParcelableExtra("ITEM")
        if (itemModel != null) {
            ida_tv_code.text = itemModel?.kode.toString()
            ida_tv_name.text = itemModel?.nama.toString()
            ida_tv_stock_minimal.text = itemModel?.stokMinimal.toString()
            ida_tv_stock.text = itemModel?.stok.toString()
            ida_tv_price.text = itemModel?.hargaRata.toString()
        }

        getListTransaction()
    }

    @SuppressLint("CheckResult")
    private fun getListTransaction() {
        Log.d(TAG, "GET LIST ITEM TRANSACTION")
        var prefManager = SharedPrefManager(this@ItemDetailActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val report: Observable<ItemResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getListItemTransaction(header, itemModel?.id!!)

        report.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "get list report as " + it.status?.success + " because " + it.status?.message)
                if (it.status?.success as Boolean) {
                    Log.d(TAG, "result list " + it.result?.toString())
                    itemTransactionModel = it.result
                    setupTable()
                } else {
                    toast("Gagal mendapatkan riwayat laporan")
                }
            }, {
                toast("Sedang ada masalah dengan server")
            })
    }

    private fun setupTable() {
        ida_tablelayout.removeViewsInLayout(1, ida_tablelayout.childCount - 1)

        for (i in itemTransactionModel!!.indices) {
            var tableRow = TableRow(this)
            val layoutParam = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.toFloat()
            )

            val llLayoutParam = TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.toFloat())
            tableRow.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tableRow.layoutParams = layoutParam

            //Tanggal
            var tv = TextView(this)
            tv.layoutParams = llLayoutParam
            tv.maxLines = 1
            tv.ellipsize = TextUtils.TruncateAt.END
            tv.text = itemTransactionModel?.get(i)!!.transaction?.tanggal
            tv.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv.padding = 8
            tableRow.addView(tv)

            //Jenis
            var tv1 = TextView(this)
            tv1.layoutParams = llLayoutParam
            tv1.maxLines = 1
            tv1.ellipsize = TextUtils.TruncateAt.END
            tv1.text = itemTransactionModel?.get(i)?.transaction?.jenis
            tv1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv1.padding = 8
            tableRow.addView(tv1)

            //No faktur
            var tv2 = TextView(this)
            tv2.layoutParams = llLayoutParam
            tv2.maxLines = 1
            tv2.ellipsize = TextUtils.TruncateAt.END
            tv2.text = itemTransactionModel?.get(i)!!.transaction?.noFaktur.toString()
            tv2.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv2.padding = 8
            tableRow.addView(tv2)

            //Barang masuk / keluar
            var tv3 = TextView(this)
            tv3.layoutParams = llLayoutParam
            tv3.maxLines = 1
            tv3.ellipsize = TextUtils.TruncateAt.END
            tv3.text = itemTransactionModel?.get(i)!!.kg.toString() + " kg"
            tv3.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv3.padding = 8
            tableRow.addView(tv3)

            //Harga barang saat transaksi
            var tv4 = TextView(this)
            tv4.layoutParams = llLayoutParam
            tv4.maxLines = 1
            tv4.ellipsize = TextUtils.TruncateAt.END
            tv4.text = HelperClass().setRupiahBig(itemTransactionModel?.get(i)?.hargaPesan.toString())
            tv4.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv4.padding = 8
            tableRow.addView(tv4)

            //Jumlah barang setelah transaksi
            var tv5 = TextView(this)
            tv5.layoutParams = llLayoutParam
            tv5.maxLines = 1
            tv5.ellipsize = TextUtils.TruncateAt.END
            tv5.text = itemTransactionModel?.get(i)!!.saldoKg.toString() + " kg"
            tv5.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv5.padding = 8
            tableRow.addView(tv5)

            //Harga barang setelah transaksi
            var tv6 = TextView(this)
            tv6.layoutParams = llLayoutParam
            tv6.maxLines = 1
            tv6.ellipsize = TextUtils.TruncateAt.END
            tv6.text = HelperClass().setRupiahBig(itemTransactionModel?.get(i)?.hargaRata.toString())
            tv6.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv6.padding = 8
            tableRow.addView(tv6)

            //Total harga barang saat ini
            var tv7 = TextView(this)
            tv7.layoutParams = llLayoutParam
            tv7.maxLines = 1
            tv7.ellipsize = TextUtils.TruncateAt.END
            tv7.text = HelperClass().setRupiahBig(itemTransactionModel?.get(i)?.saldoRp.toString())
            tv7.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv7.padding = 8
            tableRow.addView(tv7)

            ida_tablelayout.addView(tableRow)
        }
    }
}
