package com.singpaulee.blacksandapp.activities.transaction

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.DebtModel
import com.singpaulee.blacksandapp.model.ItemResultListModel
import com.singpaulee.blacksandapp.model.TransactionModel
import com.singpaulee.blacksandapp.model.TransactionResultModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_transaction_add_buy.*
import kotlinx.android.synthetic.main.activity_transaction_detail_buy.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.singleLine
import org.jetbrains.anko.toast

class TransactionDetailBuyActivity : AppCompatActivity() {
    val TAG = "TransactionDetailBuy"

    var idTransaction: Int? = null

    var transactionModel: TransactionModel? = null

    //Repayment Of Debt
    var listRepaymentOfDebt: MutableList<DebtModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail_buy)

        idTransaction = intent.getIntExtra("TRANSACTIONID", -1)
        Log.d(TAG, "ID TRANSACTION: " + idTransaction)

        //TODO REQUEST TO GET DATA OF TRANSACTION
        getDataTransaction()

        listRepaymentOfDebt = mutableListOf()

        tdba_btn_repayment_debt.onClick {
            var i: Intent = Intent(this@TransactionDetailBuyActivity, DebtRepaymentActivity::class.java)
            i.putExtra("TRANSACTION", transactionModel)
            startActivity(i)
        }
    }

    /*This function to get data/detail of transaction*/
    @SuppressLint("CheckResult")
    private fun getDataTransaction() {
        var prefManager = SharedPrefManager(this@TransactionDetailBuyActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListItem: Observable<TransactionResultModel>? = idTransaction?.let {
            RestConfig.retrofit
                .create<ApiInterface>(ApiInterface::class.java)
                .getDataOfTransaction(header, it)
        }

        getListItem?.subscribeOn(Schedulers.newThread())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ t: TransactionResultModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    //TODO SET VALUE
                    transactionModel = t.result
                    tdba_tv_date.text = HelperClass().convertDateYMDhms(t.result?.tanggal.toString())
                    tdba_tv_status.text = if (t.result?.hutang!!.toInt() > 0) "Belum Lunas" else "Lunas"
                    tdba_tv_nofaktur.text = t.result.noFaktur
                    t.result.supplier?.let {
                        tdba_tv_title_supplier.text = "Nama pemasok"
                        tdba_tv_title_debt.text = "Hutang"
                        tdba_btn_repayment_debt.text = "Bayar hutang"
                        tdba_tv_supplier.text = t.result.supplier?.nama
                    }
                    t.result.buyer?.let {
                        tdba_tv_title_supplier.text = "Nama pelanggan"
                        tdba_tv_title_debt.text = "Piutang"
                        tdba_btn_repayment_debt.text = "Bayar piutang"
                        tdba_tv_supplier.text = t.result.buyer?.nama
                    }
                    tdba_tv_due_date.text = HelperClass().convertDateYMDhms(t.result?.tglJatuhTempo.toString())
                    tdba_tv_total.text = HelperClass().setRupiah(t.result.total.toString())
                    tdba_tv_debt.text = HelperClass().setRupiah(t.result.hutang.toString())
                    settingTableLayout()
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menampilkan barang")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }

    private fun settingTableLayout() {
        listRepaymentOfDebt = transactionModel?.listRepayment

        tdba_tl_repayment.removeViewsInLayout(1, tdba_tl_repayment.childCount - 1)
        for (i in listRepaymentOfDebt?.indices!!) {
            var tableRow = TableRow(this)
            val layoutParam = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.toFloat()
            )
            val llLayoutParam = TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.toFloat())
            tableRow.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tableRow.layoutParams = layoutParam

            var tvNumber = TextView(this)
            tvNumber.layoutParams = llLayoutParam
            tvNumber.text = "" + (i + 1)
            tvNumber.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tvNumber.padding = 8
            tableRow.addView(tvNumber)

            var tvIdRepaymnet = TextView(this)
            tvIdRepaymnet.layoutParams = llLayoutParam
            tvIdRepaymnet.text = "" + listRepaymentOfDebt?.get(i)?.idPelunasan
            tvIdRepaymnet.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tvIdRepaymnet.padding = 8
            tableRow.addView(tvIdRepaymnet)

            var tvDebit = TextView(this)
            tvDebit.layoutParams = llLayoutParam
            tvDebit.text = "" + HelperClass().setRupiah(listRepaymentOfDebt?.get(i)?.debit.toString())
            tvDebit.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tvDebit.padding = 8
            tableRow.addView(tvDebit)

            var tvKredit = TextView(this)
            tvKredit.layoutParams = llLayoutParam
            tvKredit.text = "" + HelperClass().setRupiah(listRepaymentOfDebt?.get(i)?.kredit.toString())
            tvKredit.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tvKredit.padding = 8
            tableRow.addView(tvKredit)

            var tvSaldo = TextView(this)
            tvSaldo.layoutParams = llLayoutParam
            tvSaldo.text = "" + HelperClass().setRupiah(listRepaymentOfDebt?.get(i)?.saldo.toString())
            tvSaldo.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tvSaldo.padding = 8
            tableRow.addView(tvSaldo)

            var tvDateRepaymnet = TextView(this)
            tvDateRepaymnet.layoutParams = llLayoutParam
            tvDateRepaymnet.text = "" + listRepaymentOfDebt?.get(i)?.paymentAt.toString()
            tvDateRepaymnet.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tvDateRepaymnet.padding = 8
            tvDateRepaymnet.singleLine = true
            tvDateRepaymnet.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tvDateRepaymnet)

            tdba_tl_repayment.addView(tableRow)
        }
    }
}
