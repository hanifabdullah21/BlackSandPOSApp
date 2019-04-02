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
import com.singpaulee.blacksandapp.model.ReportDataCashFlow2
import com.singpaulee.blacksandapp.model.ReportResultListCashflow2
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_report_cashflow.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class ListReportCashflowActivity : AppCompatActivity() {

    val TAG = "ListReportCashflow"
    var listReport: ArrayList<ReportDataCashFlow2>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_report_cashflow)


        listReport = ArrayList()
        getListReport()
    }

    @SuppressLint("CheckResult")
    private fun getListReport() {
        Log.d(TAG, "GET LIST REPORT MONTHLY")
        var prefManager = SharedPrefManager(this@ListReportCashflowActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val report: Observable<ReportResultListCashflow2> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getDataListReportCashflow(header)

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
        lrcfa_tablelayout.removeViewsInLayout(1, lrcfa_tablelayout.childCount - 1)

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

            //Pelunasan Piutang
            var tv1 = TextView(this)
            tv1.layoutParams = llLayoutParam
            tv1.maxLines = 1
            tv1.ellipsize = TextUtils.TruncateAt.END
            tv1.text = HelperClass().setRupiahBig(listReport?.get(i)?.pelunasanPiutang.toString())
            tv1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv1.padding = 8
            tableRow.addView(tv1)

            //Pelunasan Hutang
            var tv2 = TextView(this)
            tv2.layoutParams = llLayoutParam
            tv2.maxLines = 1
            tv2.ellipsize = TextUtils.TruncateAt.END
            tv2.text = HelperClass().setRupiahBig(listReport?.get(i)?.pelunasanHutang.toString())
            tv2.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv2.padding = 8
            tableRow.addView(tv2)

            //Beban ANgkut
            var tv3 = TextView(this)
            tv3.layoutParams = llLayoutParam
            tv3.maxLines = 1
            tv3.ellipsize = TextUtils.TruncateAt.END
            tv3.text = HelperClass().setRupiahBig(listReport?.get(i)?.bebanAngkut.toString())
            tv3.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv3.padding = 8
            tableRow.addView(tv3)

            //Beban Gaji
            var tv4 = TextView(this)
            tv4.layoutParams = llLayoutParam
            tv4.maxLines = 1
            tv4.ellipsize = TextUtils.TruncateAt.END
            tv4.text = HelperClass().setRupiahBig(listReport?.get(i)?.bebanGaji.toString())
            tv4.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv4.padding = 8
            tableRow.addView(tv4)

            //Beban Operasional
            var tv5 = TextView(this)
            tv5.layoutParams = llLayoutParam
            tv5.maxLines = 1
            tv5.ellipsize = TextUtils.TruncateAt.END
            tv5.text = HelperClass().setRupiahBig(listReport?.get(i)?.bebanOperasional.toString())
            tv5.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv5.padding = 8
            tableRow.addView(tv5)

            //Beban Pajak
            var tv6 = TextView(this)
            tv6.layoutParams = llLayoutParam
            tv6.maxLines = 1
            tv6.ellipsize = TextUtils.TruncateAt.END
            tv6.text = HelperClass().setRupiahBig(listReport?.get(i)?.bebanPajak.toString())
            tv6.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv6.padding = 8
            tableRow.addView(tv6)

            //Total Operasional
            var tv7 = TextView(this)
            tv7.layoutParams = llLayoutParam
            tv7.maxLines = 1
            tv7.ellipsize = TextUtils.TruncateAt.END
            tv7.text = HelperClass().setRupiahBig(listReport?.get(i)?.totalOperasi.toString())
            tv7.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv7.padding = 8
            tableRow.addView(tv7)

            //Pebelian Tanah
            var tv8 = TextView(this)
            tv8.layoutParams = llLayoutParam
            tv8.maxLines = 1
            tv8.ellipsize = TextUtils.TruncateAt.END
            tv8.text = HelperClass().setRupiahBig(listReport?.get(i)?.assetTanah.toString())
            tv8.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv8.padding = 8
            tableRow.addView(tv8)

            //Pembelian Perlengkapan
            var tv9 = TextView(this)
            tv9.layoutParams = llLayoutParam
            tv9.maxLines = 1
            tv9.ellipsize = TextUtils.TruncateAt.END
            tv9.text = HelperClass().setRupiahBig(listReport?.get(i)?.assetPerlengkapan.toString())
            tv9.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv9.padding = 8
            tableRow.addView(tv9)

            //Pembelian bangunan
            var tv10 = TextView(this)
            tv10.layoutParams = llLayoutParam
            tv10.maxLines = 1
            tv10.ellipsize = TextUtils.TruncateAt.END
            tv10.text = HelperClass().setRupiahBig(listReport?.get(i)?.assetBangunan.toString())
            tv10.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv10.padding = 8
            tableRow.addView(tv10)

            //Pembelian Kendaraan
            var tv11 = TextView(this)
            tv11.layoutParams = llLayoutParam
            tv11.maxLines = 1
            tv11.ellipsize = TextUtils.TruncateAt.END
            tv11.text = HelperClass().setRupiahBig(listReport?.get(i)?.assetKendaraan.toString())
            tv11.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv11.padding = 8
            tableRow.addView(tv11)

            //Pembelian Peralatan
            var tv12 = TextView(this)
            tv12.layoutParams = llLayoutParam
            tv12.maxLines = 1
            tv12.ellipsize = TextUtils.TruncateAt.END
            tv12.text = HelperClass().setRupiahBig(listReport?.get(i)?.assetPeralatan.toString())
            tv12.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv12.padding = 8
            tableRow.addView(tv12)

            //Total Investasi
            var tv13 = TextView(this)
            tv13.layoutParams = llLayoutParam
            tv13.maxLines = 1
            tv13.ellipsize = TextUtils.TruncateAt.END
            tv13.text = HelperClass().setRupiahBig(listReport?.get(i)?.totalInvestasi.toString())
            tv13.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv13.padding = 8
            tableRow.addView(tv13)

            //Penambahan Modal
            var tv14 = TextView(this)
            tv14.layoutParams = llLayoutParam
            tv14.maxLines = 1
            tv14.ellipsize = TextUtils.TruncateAt.END
            tv14.text = HelperClass().setRupiahBig("0")
            tv14.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv14.padding = 8
            tableRow.addView(tv14)

            //Pinjam Bank
            var tv15 = TextView(this)
            tv15.layoutParams = llLayoutParam
            tv15.maxLines = 1
            tv15.ellipsize = TextUtils.TruncateAt.END
            tv15.text = HelperClass().setRupiahBig("0")
            tv15.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv15.padding = 8
            tableRow.addView(tv15)

            //Prive
            var tv16 = TextView(this)
            tv16.layoutParams = llLayoutParam
            tv16.maxLines = 1
            tv16.ellipsize = TextUtils.TruncateAt.END
            tv16.text = HelperClass().setRupiahBig(listReport?.get(i)?.totalPrive.toString())
            tv16.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv16.padding = 8
            tableRow.addView(tv16)

            //Total
            var tv17 = TextView(this)
            tv17.layoutParams = llLayoutParam
            tv17.maxLines = 1
            tv17.ellipsize = TextUtils.TruncateAt.END
            tv17.text = HelperClass().setRupiahBig(listReport?.get(i)?.totalPrive.toString())
            tv17.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv17.padding = 8
            tableRow.addView(tv17)

            //Kenaikan saldo
            var tv18 = TextView(this)
            tv18.layoutParams = llLayoutParam
            tv18.maxLines = 1
            tv18.ellipsize = TextUtils.TruncateAt.END
            tv18.text = HelperClass().setRupiahBig(listReport?.get(i)?.kenaikanSaldo.toString())
            tv18.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv18.padding = 8
            tableRow.addView(tv18)

            //Saldo awal
            var tv19 = TextView(this)
            tv19.layoutParams = llLayoutParam
            tv19.maxLines = 1
            tv19.ellipsize = TextUtils.TruncateAt.END
            tv19.text = HelperClass().setRupiahBig(listReport?.get(i)?.saldoAwalBulan.toString())
            tv19.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv19.padding = 8
            tableRow.addView(tv19)

            //Saldo Akhir
            var tv20 = TextView(this)
            tv20.layoutParams = llLayoutParam
            tv20.maxLines = 1
            tv20.ellipsize = TextUtils.TruncateAt.END
            tv20.text = HelperClass().setRupiahBig(listReport?.get(i)?.saldoAkhirBulan.toString())
            tv20.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv20.padding = 8
            tableRow.addView(tv20)

            //Cetak Ulang
            var tv21 = TextView(this)
            tv21.layoutParams = llLayoutParam
            tv21.maxLines = 1
            tv21.ellipsize = TextUtils.TruncateAt.END
            tv21.text = "Cetak"
            tv21.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv21.padding = 8
            tv21.onClick {
                HelperClass().createPDFCashflw(this@ListReportCashflowActivity, listReport?.get(i)!!)
            }
            tableRow.addView(tv21)


            lrcfa_tablelayout.addView(tableRow)
        }
    }

}


