package com.singpaulee.blacksandapp.activities.report

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.TableRow
import android.widget.TextView
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.ReportDataCapitalModel
import com.singpaulee.blacksandapp.model.ReportResultListCapitalModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_report_capital.*
import kotlinx.android.synthetic.main.activity_list_report_monthly.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class ListReportCapitalActivity : AppCompatActivity() {

    val TAG = "ListReportCapital"
    var listReport: ArrayList<ReportDataCapitalModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_report_capital)

        listReport = ArrayList()
        getListReportCapital()
    }

    @SuppressLint("CheckResult")
    private fun getListReportCapital() {
        Log.d(TAG, "GET LIST REPORT MONTHLY")
        var prefManager = SharedPrefManager(this@ListReportCapitalActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val report: Observable<ReportResultListCapitalModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getDataListReportCapital(header)

        report.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "get list report as " + it.status?.success + " because " + it.status?.message)
                if (it.status?.success as Boolean) {
                    Log.d(TAG, "result list " + it.result?.toString())
                    listReport = it.result
                    setupTable()
                } else {
                    toast("Gagal mendapatkan riwayat laporan")
                }
            }, {
                toast("Sedang ada masalah dengan server")
            })

    }

    private fun setupTable() {
        lrca_tablelayout.removeViewsInLayout(1, lrca_tablelayout.childCount - 1)

        for (i in listReport!!.indices) {
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
            tv.text = listReport?.get(i)?.tanggal
            tv.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv.padding = 8
            tableRow.addView(tv)

            //Range Tanggal
            var tv1 = TextView(this)
            tv1.layoutParams = llLayoutParam
            tv1.maxLines = 1
            tv1.ellipsize = TextUtils.TruncateAt.END
            tv1.text = listReport?.get(i)?.rangeTanggal
            tv1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv1.padding = 8
            tableRow.addView(tv1)

            //Modal Awal
            var tv2 = TextView(this)
            tv2.layoutParams = llLayoutParam
            tv2.maxLines = 1
            tv2.ellipsize = TextUtils.TruncateAt.END
            tv2.text = HelperClass().setRupiahBig(listReport?.get(i)?.awal.toString())
            tv2.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv2.padding = 8
            tableRow.addView(tv2)

            //Total Laba Bersih
            var tv3 = TextView(this)
            tv3.layoutParams = llLayoutParam
            tv3.maxLines = 1
            tv3.ellipsize = TextUtils.TruncateAt.END
            tv3.text = HelperClass().setRupiahBig(listReport?.get(i)?.totalLabaBersih.toString())
            tv3.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv3.padding = 8
            tableRow.addView(tv3)

            //Total Prive
            var tv4 = TextView(this)
            tv4.layoutParams = llLayoutParam
            tv4.maxLines = 1
            tv4.ellipsize = TextUtils.TruncateAt.END
            tv4.text = HelperClass().setRupiahBig(listReport?.get(i)?.totalPrive.toString())
            tv4.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv4.padding = 8
            tableRow.addView(tv4)

            //Modal Akhir
            var tv5 = TextView(this)
            tv5.layoutParams = llLayoutParam
            tv5.maxLines = 1
            tv5.ellipsize = TextUtils.TruncateAt.END
            tv5.text = HelperClass().setRupiahBig(listReport?.get(i)?.akhir.toString())
            tv5.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv5.padding = 8
            tableRow.addView(tv5)

            //Cetak Ulang
            var tv16 = TextView(this)
            tv16.layoutParams = llLayoutParam
            tv16.maxLines = 1
            tv16.ellipsize = TextUtils.TruncateAt.END
            tv16.text = "Cetak"
            tv16.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv16.padding = 8
            tv16.onClick {
                HelperClass().createPDFCapital(this@ListReportCapitalActivity, listReport?.get(i)!!)
            }
            tableRow.addView(tv16)


            lrca_tablelayout.addView(tableRow)
        }
    }
}
