package com.singpaulee.blacksandapp.activities.transaction

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.main.MainActivity
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.DebtResultModel
import com.singpaulee.blacksandapp.model.TransactionModel
import com.singpaulee.blacksandapp.model.TransactionResultModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_debt_repayment.*
import kotlinx.android.synthetic.main.activity_transaction_detail_buy.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class DebtRepaymentActivity : AppCompatActivity() {

    val TAG = "DebtRepaymentActivity"

    var idTransaction: Int? = null
    var transactionModel: TransactionModel? = null
    var debt: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_repayment)

        //TODO GET DATA FROM PREVIOUS ACTIVITY
        transactionModel = intent.getParcelableExtra("TRANSACTION") //From detail transaction activity
        idTransaction = intent.getIntExtra("ID_", -1)    //From add transaction buy

        setDataTransaction()
        getDataTransaction()

        dra_edt_debit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                dra_edt_debit.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()

                    val longval: Long?
                    val longvalBalance: Long?
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    var balanceResult = (debt!! - originalString.toInt()).toString()
                    longval = java.lang.Long.parseLong(originalString)
                    longvalBalance = java.lang.Long.parseLong(balanceResult)

                    val formatter = NumberFormat.getCurrencyInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString = formatter.format(longval)
                    val formattedBalance = formatter.format(longvalBalance)

                    //setting text after format to EditText
                    dra_tv_saldo.text = formattedBalance
                    dra_edt_debit.setText(formattedString)
                    dra_edt_debit.setSelection(dra_edt_debit.getText().length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                dra_edt_debit.addTextChangedListener(this)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        //TODO Send request when button repayment clicked!
        dra_btn_payment.onClick {
            repaymentDebt()
        }
    }

    private fun getDataTransaction() {
        idTransaction?.let {
            if (idTransaction!! > -1) {
                var prefManager = SharedPrefManager(this@DebtRepaymentActivity)
                var token = prefManager.getToken()
                var header = "Bearer " + token

                val getDataTransaction: Observable<TransactionResultModel>? = idTransaction?.let {
                    RestConfig.retrofit
                        .create<ApiInterface>(ApiInterface::class.java)
                        .getDataOfTransaction(header, it)
                }

                getDataTransaction?.subscribeOn(Schedulers.newThread())?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({ t: TransactionResultModel? ->
                        if (t?.status?.success?.equals(true)!!) {
                            //TODO SET VALUE
                            transactionModel = t.result
                            setDataTransaction()
                        } else {
                            Log.d(TAG, "FAILED " + t?.status?.message)
                            toast("Gagal menampilkan barang")
                        }
                    }, { error ->
                        Log.d(TAG, "ERROR " + error.message)
                        toast("Maaf, sedang ada masalah dengan server.")
                    })
            }
        }
    }

    private fun setDataTransaction() {
        transactionModel?.let {
            //TODO SET DETAIL TRANSACTION
            dra_cv_detail_transaction.visibility = View.VISIBLE
            dra_tv_date.text = HelperClass().convertDateYMDhms(it.tanggal.toString())
            dra_tv_nofaktur.text = it.noFaktur.toString()
            it.supplier?.let { it2 ->
                dra_tv_supplier.text = it.supplier?.nama.toString()
                dra_tv_title_supplier.text = "Nama pemasok"
            } ?: run {
                dra_tv_supplier.text = it.buyer?.nama.toString()
                dra_tv_title_supplier.text = "Nama pelanggan"
            }
            dra_tv_due_date.text = HelperClass().convertDateYMDhms(it.tglJatuhTempo.toString())
            dra_tv_total.text = HelperClass().setRupiah(it.total.toString())
            dra_tv_debt.text = HelperClass().setRupiah(it.hutang.toString())
            dra_tv_periode.text = (it.listRepayment?.size!! + 1).toString()
            debt = it.hutang
        } ?: run {
            dra_cv_detail_transaction.visibility = View.GONE
            dra_tv_periode.text = "1"
        }
    }

    @SuppressLint("CheckResult")
    private fun repaymentDebt() {
        if (!validation()) {
            return
        }
        var prefManager = SharedPrefManager(this@DebtRepaymentActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListItem: Observable<DebtResultModel>? = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .postRepayment(header,
                transactionModel?.id!!,
                dra_edt_debit.text.toString().replace(",", "").toInt(),
                "",
                dra_edt_date.text.toString())

        getListItem?.subscribeOn(Schedulers.newThread())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ t: DebtResultModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    toast("sukses")
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menampilkan barang")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }

    private fun validation(): Boolean {
        if (dra_edt_debit.text.toString().isBlank()) {
            dra_edt_debit.error = "Tidak boleh kosong"
            dra_edt_debit.requestFocus()
            return false
        }
        if (dra_edt_debit.text.toString().replace(",", "").toInt() > debt!!) {
            dra_edt_debit.error = "Nominal bayar terlalu banyak"
            dra_edt_debit.requestFocus()
            return false
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        idTransaction?.let {
            if (idTransaction!! > -1) {
                finish()
                var intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                finish()
            }
        }
    }

}
