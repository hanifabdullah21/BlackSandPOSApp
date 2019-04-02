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
import com.singpaulee.blacksandapp.model.ReportDataModel2
import com.singpaulee.blacksandapp.model.ReportResultListDataModel2
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_report_monthly.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class ListReportMonthlyActivity : AppCompatActivity() {

    val TAG = "ListReportMonthly"

    var listReport: ArrayList<ReportDataModel2>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_report_monthly)

        listReport = ArrayList()
        getListReportMonthly()
    }

    @SuppressLint("CheckResult")
    private fun getListReportMonthly() {
        Log.d(TAG, "GET LIST REPORT MONTHLY")
        var prefManager = SharedPrefManager(this@ListReportMonthlyActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val report: Observable<ReportResultListDataModel2> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getDataListReportMonthly(header, "bulan")

        report.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "get list report as " + it.status?.success + " because " + it.status?.message)
                if (it.status?.success as Boolean) {
                    Log.d(TAG, "result list " + it.result?.report.toString())
                    listReport = it.result?.report
                    setupTable()
                } else {
                    toast("Gagal mendapatkan riwayat laporan")
                }
            }, {
                toast("Sedang ada masalah dengan server")
            })

    }

    private fun setupTable() {
        lrma_tablelayout.removeViewsInLayout(1, lrma_tablelayout.childCount - 1)

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

            //Bulan
            var tv1 = TextView(this)
            tv1.layoutParams = llLayoutParam
            tv1.maxLines = 1
            tv1.ellipsize = TextUtils.TruncateAt.END
            tv1.text = listReport?.get(i)?.tanggalLaporan
            tv1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv1.padding = 8
            tableRow.addView(tv1)

            //Penjualan
            var tv2 = TextView(this)
            tv2.layoutParams = llLayoutParam
            tv2.maxLines = 1
            tv2.ellipsize = TextUtils.TruncateAt.END
            tv2.text = HelperClass().setRupiahBig(listReport?.get(i)?.penjualan.toString())
            tv2.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv2.padding = 8
            tableRow.addView(tv2)

            //Pembelian
            var tv3 = TextView(this)
            tv3.layoutParams = llLayoutParam
            tv3.maxLines = 1
            tv3.ellipsize = TextUtils.TruncateAt.END
            tv3.text = HelperClass().setRupiahBig(listReport?.get(i)?.pembelian.toString())
            tv3.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv3.padding = 8
            tableRow.addView(tv3)

            //Persediaan Awal
            var tv4 = TextView(this)
            tv4.layoutParams = llLayoutParam
            tv4.maxLines = 1
            tv4.ellipsize = TextUtils.TruncateAt.END
            tv4.text = HelperClass().setRupiahBig(listReport?.get(i)?.persediaanAwal.toString())
            tv4.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv4.padding = 8
            tableRow.addView(tv4)

            //Persediaan Akhir
            var tv5 = TextView(this)
            tv5.layoutParams = llLayoutParam
            tv5.maxLines = 1
            tv5.ellipsize = TextUtils.TruncateAt.END
            tv5.text = HelperClass().setRupiahBig(listReport?.get(i)?.persediaanAkhir.toString())
            tv5.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv5.padding = 8
            tableRow.addView(tv5)

            //Beban angkut penjualan
            var tv6 = TextView(this)
            tv6.layoutParams = llLayoutParam
            tv6.maxLines = 1
            tv6.ellipsize = TextUtils.TruncateAt.END
            tv6.text = HelperClass().setRupiahBig(listReport?.get(i)?.bebanPenjualan.toString())
            tv6.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv6.padding = 8
            tableRow.addView(tv6)

            //Beban Angkut Pembelian
            var tv7 = TextView(this)
            tv7.layoutParams = llLayoutParam
            tv7.maxLines = 1
            tv7.ellipsize = TextUtils.TruncateAt.END
            tv7.text = HelperClass().setRupiahBig(listReport?.get(i)?.bebanPembelian.toString())
            tv7.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv7.padding = 8
            tableRow.addView(tv7)

            //Beban Gaji
            var tv8 = TextView(this)
            tv8.layoutParams = llLayoutParam
            tv8.maxLines = 1
            tv8.ellipsize = TextUtils.TruncateAt.END
            tv8.text = HelperClass().setRupiahBig(listReport?.get(i)?.bebanGaji.toString())
            tv8.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv8.padding = 8
            tableRow.addView(tv8)

            //Beban Operasional
            var tv9 = TextView(this)
            tv9.layoutParams = llLayoutParam
            tv9.maxLines = 1
            tv9.ellipsize = TextUtils.TruncateAt.END
            tv9.text = HelperClass().setRupiahBig(listReport?.get(i)?.bebanOperasional.toString())
            tv9.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv9.padding = 8
            tableRow.addView(tv9)

            //Beban Pajak
            var tv10 = TextView(this)
            tv10.layoutParams = llLayoutParam
            tv10.maxLines = 1
            tv10.ellipsize = TextUtils.TruncateAt.END
            tv10.text = HelperClass().setRupiahBig(listReport?.get(i)?.bebanPajak.toString())
            tv10.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv10.padding = 8
            tableRow.addView(tv10)

            //Depresiasi Bangunan
            var tv11 = TextView(this)
            tv11.layoutParams = llLayoutParam
            tv11.maxLines = 1
            tv11.ellipsize = TextUtils.TruncateAt.END
            tv11.text = HelperClass().setRupiahBig(listReport?.get(i)?.depresiasiBangunan.toString())
            tv11.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv11.padding = 8
            tableRow.addView(tv11)

            //Depresiasi Kendaraan
            var tv12 = TextView(this)
            tv12.layoutParams = llLayoutParam
            tv12.maxLines = 1
            tv12.ellipsize = TextUtils.TruncateAt.END
            tv12.text = HelperClass().setRupiahBig(listReport?.get(i)?.depresiasiKendaraan.toString())
            tv12.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv12.padding = 8
            tableRow.addView(tv12)

            //Depresiasi Peralatan
            var tv13 = TextView(this)
            tv13.layoutParams = llLayoutParam
            tv13.maxLines = 1
            tv13.ellipsize = TextUtils.TruncateAt.END
            tv13.text = HelperClass().setRupiahBig(listReport?.get(i)?.depresiasiPeralatan.toString())
            tv13.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv13.padding = 8
            tableRow.addView(tv13)

            //Laba Kotor
            var tv14 = TextView(this)
            tv14.layoutParams = llLayoutParam
            tv14.maxLines = 1
            tv14.ellipsize = TextUtils.TruncateAt.END
            tv14.text = HelperClass().setRupiahBig(listReport?.get(i)?.labaKotor.toString())
            tv14.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv14.padding = 8
            tableRow.addView(tv14)

            //Laba Bersih
            var tv15 = TextView(this)
            tv15.layoutParams = llLayoutParam
            tv15.maxLines = 1
            tv15.ellipsize = TextUtils.TruncateAt.END
            tv15.text = HelperClass().setRupiahBig(listReport?.get(i)?.labaBersih.toString())
            tv15.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv15.padding = 8
            tableRow.addView(tv15)

            //Cetak Ulang
            var tv16 = TextView(this)
            tv16.layoutParams = llLayoutParam
            tv16.maxLines = 1
            tv16.ellipsize = TextUtils.TruncateAt.END
            tv16.text = "Cetak"
            tv16.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv16.padding = 8
            tv16.onClick {
                HelperClass().createPDFMonthly(this@ListReportMonthlyActivity, listReport?.get(i)!!)
            }
            tableRow.addView(tv16)


            lrma_tablelayout.addView(tableRow)
        }
    }
}
