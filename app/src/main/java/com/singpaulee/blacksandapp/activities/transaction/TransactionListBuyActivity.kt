package com.singpaulee.blacksandapp.activities.transaction

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.widget.TableRow
import android.widget.TextView
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.adapter.TransactionAdapter
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.ItemResultListModel
import com.singpaulee.blacksandapp.model.TransactionModel
import com.singpaulee.blacksandapp.model.TransactionResultListModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_transaction_add_buy.*
import kotlinx.android.synthetic.main.activity_transaction_list_buy.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class TransactionListBuyActivity : AppCompatActivity() {

    val TAG = "TransactionBuyListAct"

    var listTransaction: ArrayList<TransactionModel>? = null
    var transactionLayoutManager: RecyclerView.LayoutManager? = null
    var transactionAdapter: TransactionAdapter? = null

    var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_list_buy)

        listTransaction = ArrayList()
        transactionLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        transactionAdapter = TransactionAdapter(this, listTransaction)

        tlba_rv_list_transaction.layoutManager = transactionLayoutManager
        tlba_rv_list_transaction.adapter = transactionAdapter

        type = intent.getStringExtra("type")

        getListTransactionBuy()
    }

    @SuppressLint("CheckResult")
    private fun getListTransactionBuy() {
        var prefManager = SharedPrefManager(this@TransactionListBuyActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListTransaction: Observable<TransactionResultListModel>? = type?.let {
            RestConfig.retrofit
                .create<ApiInterface>(ApiInterface::class.java)
                .getListTransaction(header, it)
        }

        getListTransaction?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ t: TransactionResultListModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    listTransaction = t.result
                    transactionAdapter = TransactionAdapter(this, listTransaction)
                    tlba_rv_list_transaction.adapter = transactionAdapter
                    setTableListBuy()
                } else {
                    Log.d(TAG, "FAILED " + t.status.message)
                    toast("Gagal menampilkan transaksi pembelian")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }

    private fun setTableListBuy() {
        tlba_tablelayout.removeViewsInLayout(1, tlba_tablelayout.childCount - 1)
        for (i in listTransaction?.indices!!) {
            var tableRow = TableRow(this)
            val layoutParam = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.toFloat()
            )

            val llLayoutParam = TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.toFloat())
            tableRow.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tableRow.layoutParams = layoutParam

            //ID
            var tv = TextView(this)
            tv.layoutParams = llLayoutParam
            tv.maxLines = 1
            tv.ellipsize = TextUtils.TruncateAt.END
            tv.text = listTransaction?.get(i)!!.id.toString()
            tv.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv.padding = 8
            tableRow.addView(tv)

            //Jenis transaksi
            var tv1 = TextView(this)
            tv1.layoutParams = llLayoutParam
            tv1.maxLines = 1
            tv1.ellipsize = TextUtils.TruncateAt.END
            tv1.text = listTransaction?.get(i)!!.jenis.toString()
            tv1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv1.padding = 8
            tableRow.addView(tv1)

            //Nomer faktur
            var tv2 = TextView(this)
            tv2.layoutParams = llLayoutParam
            tv2.maxLines = 1
            tv2.ellipsize = TextUtils.TruncateAt.END
            tv2.text = listTransaction?.get(i)!!.noFaktur.toString()
            tv2.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv2.padding = 8
            tableRow.addView(tv2)

            //Tanggal transaksi
            var tv3 = TextView(this)
            tv3.layoutParams = llLayoutParam
            tv3.text = listTransaction?.get(i)!!.tanggal.toString()
            tv3.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv3.padding = 8
            tv3.maxLines = 1
            tv3.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tv3)

            //Tanggal jatuh tempo
            var tv4 = TextView(this)
            tv4.layoutParams = llLayoutParam
            tv4.text = listTransaction?.get(i)!!.tglJatuhTempo.toString()
            tv4.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv4.padding = 8
            tv4.maxLines = 1
            tv4.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tv4)

            //Pemasok
            var tv5 = TextView(this)
            tv5.layoutParams = llLayoutParam
            tv5.text = if (listTransaction?.get(i)!!.supplier !=null) listTransaction?.get(i)!!.supplier?.nama.toString() else listTransaction?.get(i)!!.buyer?.nama.toString()
            tv5.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv5.padding = 8
            tv5.maxLines = 1
            tv5.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tv5)

            //Total
            var tv6 = TextView(this)
            tv6.layoutParams = llLayoutParam
            tv6.text = HelperClass().setRupiahBig(listTransaction?.get(i)!!.total.toString())
            tv6.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv6.padding = 8
            tv6.maxLines = 1
            tv6.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tv6)

            //Beban Angkut
            var tv7 = TextView(this)
            tv7.layoutParams = llLayoutParam
            tv7.text = HelperClass().setRupiahBig(listTransaction?.get(i)!!.bebanAngkut.toString())
            tv7.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv7.padding = 8
            tv7.maxLines = 1
            tv7.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tv7)

            //Hutang
            var tv8 = TextView(this)
            tv8.layoutParams = llLayoutParam
            tv8.text = HelperClass().setRupiahBig(listTransaction?.get(i)!!.hutang.toString())
            tv8.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv8.padding = 8
            tv8.maxLines = 1
            tv8.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tv8)

            //Lunas
            var tv9 = TextView(this)
            tv9.layoutParams = llLayoutParam
            tv9.text =
                    HelperClass().setRupiahBig((listTransaction?.get(i)!!.total!! - listTransaction?.get(i)!!.hutang!!).toString())
            tv9.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv9.maxLines = 1
            tv9.ellipsize = TextUtils.TruncateAt.END
            tv9.padding = 8
            tableRow.addView(tv9)

            //Status
            var tv10 = TextView(this)
            tv10.layoutParams = llLayoutParam
            tv10.text = if (listTransaction?.get(i)!!.hutang == 0) "Lunas" else "Belum Lunas"
            tv10.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv10.padding = 8
            tv10.maxLines = 1
            tv10.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tv10)


            tableRow.onClick {
                var intent = Intent(this@TransactionListBuyActivity, TransactionDetailBuyActivity::class.java)
                intent.putExtra("TRANSACTIONID", listTransaction?.get(i)!!.id)
                startActivity(intent)
            }

            tlba_tablelayout.addView(tableRow)
        }
    }
}
