package com.singpaulee.blacksandapp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView

import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.model.FinancialModel
import com.singpaulee.blacksandapp.model.FinancialResultListModel
import com.singpaulee.blacksandapp.model.FinancialResultModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_transaction_detail_buy.*
import kotlinx.android.synthetic.main.fragment_prive.*
import kotlinx.android.synthetic.main.fragment_prive.view.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.singleLine
import org.jetbrains.anko.support.v4.toast
import java.lang.NumberFormatException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PriveFragment : Fragment() {

    val TAG = "PriveFragment"
    lateinit var rootView: View

    var listFinancial: MutableList<FinancialModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_prive, container, false)

        listFinancial = ArrayList()
        getListHistoryPrive()

        rootView.pf_btn_prive.onClick {
            if (!validasi()) {
                return@onClick
            }
            postPrive()
        }

        return rootView
    }

    /* -------------------------------- P R I V E --------------------------------*/

    private fun validasi(): Boolean {
        if (rootView.pf_edt_value.text.toString().isBlank()) {
            rootView.pf_edt_value.error = "Tidak boleh kosong"
            rootView.pf_edt_value.requestFocus()
            return false
        } else {
            try {
                rootView.pf_edt_value.text.toString().toInt()
            } catch (e: NumberFormatException) {
                rootView.pf_edt_value.error = "Nilai harus berupa angka"
                rootView.pf_edt_value.requestFocus()
                return false
            }
        }
        if (rootView.pf_edt_information.text.toString().isBlank()) {
            rootView.pf_edt_information.error = "Tidak boleh kosong"
            rootView.pf_edt_information.requestFocus()
            return false
        }
        return true
    }

    @SuppressLint("CheckResult")
    private fun postPrive() {
        var header: String = HelperClass().getHeader(activity!!.applicationContext)

        var prive: Observable<FinancialResultModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .postPrive(
                header,
                rootView.pf_edt_value.text.toString().toInt(),
                rootView.pf_edt_information.text.toString()
//                rootView.pf_edt_date.text.toString()
            )

        prive.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: FinancialResultModel ->
                if (t.status?.success as Boolean) {
                    toast("" + t.status.message)
                    rootView.pf_edt_value.setText(null)
                    rootView.pf_edt_information.setText(null)
                    getListHistoryPrive()
                } else {
                    toast("Prive gagal karena " + t.status.message)
                }
            }, { error ->
                Log.d(TAG, "Failure karena " + error.message)
                toast("Sedang ada masalah pada server")
            })
    }

    /* -------------------------------- H I S T O R Y P R I V E --------------------------------*/

    @SuppressLint("CheckResult")
    private fun getListHistoryPrive() {
        var header: String = HelperClass().getHeader(activity!!.applicationContext)

        var financial: Observable<FinancialResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getFinancial(header, "prive")

        financial.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: FinancialResultListModel ->
                if (t.status?.success as Boolean) {
                    listFinancial = t.result
                    settingTableLayout()
                } else {
                    Log.d(TAG, "Gagal mendapatkan list karena " + t.status.message)
                    toast("Gaga; mendapatkan riwayat prive")
                }
            }, { error ->
                Log.d(TAG, "Failure karena " + error.message)
                toast("Sedang ada masalah dalam server")
            })
    }

    private fun settingTableLayout() {
        rootView.pf_tablelayout.removeViewsInLayout(1, rootView.pf_tablelayout.childCount - 1)
        for (i in listFinancial?.indices!!) {
            var tableRow = TableRow(activity!!.applicationContext)
            val layoutParam = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.toFloat()
            )
            val llLayoutParam = TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.toFloat())
            val llLayoutParam2 = TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT, 1.toFloat())
            tableRow.backgroundDrawable =
                    ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.border_table)
            tableRow.layoutParams = layoutParam

            /*ID*/
            var tv1 = TextView(activity!!.applicationContext)
            tv1.layoutParams = llLayoutParam
            tv1.text = "" + listFinancial?.get(i)?.id
            tv1.backgroundDrawable = ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.border_table)
            tv1.padding = 8
            tableRow.addView(tv1)

            /*Tanggal*/
            var tv2 = TextView(activity!!.applicationContext)
            tv2.layoutParams = llLayoutParam
            tv2.text = "" + HelperClass().convertDateYMDhms(listFinancial?.get(i)?.tanggal.toString())
            tv2.backgroundDrawable = ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.border_table)
            tv2.padding = 8
            tv2.singleLine = true
            tv2.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tv2)

            /*Type*/
            var tv3 = TextView(activity!!.applicationContext)
            tv3.layoutParams = llLayoutParam
            tv3.text = "" + listFinancial?.get(i)?.jenis
            tv3.backgroundDrawable = ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.border_table)
            tv3.padding = 8
            tableRow.addView(tv3)

            /*Nilai*/
            var tv4 = TextView(activity!!.applicationContext)
            tv4.layoutParams = llLayoutParam
            tv4.text = "" + HelperClass().setRupiah(listFinancial?.get(i)?.nilai.toString())
            tv4.backgroundDrawable = ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.border_table)
            tv4.padding = 8
            tableRow.addView(tv4)

            /*Kategori*/
            var tv5 = TextView(activity!!.applicationContext)
            tv5.layoutParams = llLayoutParam
            tv5.text = "" + listFinancial?.get(i)?.kategori
            tv5.backgroundDrawable = ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.border_table)
            tv5.padding = 8
            tableRow.addView(tv5)

            /*Saldo kas*/
            var tv6 = TextView(activity!!.applicationContext)
            tv6.layoutParams = llLayoutParam2
            tv6.text = "" + HelperClass().setRupiahBig(listFinancial?.get(i)?.saldoKas.toString())
            tv6.backgroundDrawable = ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.border_table)
            tv6.padding = 8
            tv6.singleLine = true
            tv6.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tv6)

            /*Keterangan*/
            var tv7 = TextView(activity!!.applicationContext)
            tv7.layoutParams = llLayoutParam
            tv7.text = "" + listFinancial?.get(i)?.keterangan
            tv7.backgroundDrawable = ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.border_table)
            tv7.padding = 8
            tv7.singleLine = true
            tv7.ellipsize = TextUtils.TruncateAt.END
            tableRow.addView(tv7)

            rootView.pf_tablelayout.addView(tableRow)
        }
    }
}
