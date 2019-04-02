package com.singpaulee.blacksandapp.activities.report

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.*
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_report_capital.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ReportCapitalActivity : AppCompatActivity() {

    val TAG = "ReportCapital"
    private val STORAGE_CODE: Int = 100

    var dataCapitalModel: ReportDataCapitalModel? = null
    var dataCapitalModel2: ReportDataCapitalModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_capital)

        setPermission()

        getReport()

        rca_btn_report.onClick {
            postReport()
        }
    }

    private fun setPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, STORAGE_CODE)
            } else {
//                toast("Ijin diterima")
            }
        } else {
//            toast("Ijin diterima")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    createPDF()
                    toast("Ijin diterima")
                } else {
                    toast("Ijin ditolak")
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getReport() {
        var header = HelperClass().getHeader(this)

        var capital: Observable<ReportResultDataCapitalModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getDataReportModal(header)

        capital.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: ReportResultDataCapitalModel ->
                if (t.status?.success as Boolean) {
                    dataCapitalModel = t.result
                    rca_tv_range_date.text = t.result?.rangeTanggal
                    rca_tv_first_capital.text = HelperClass().setRupiahBig(t.result?.awal.toString())
                    rca_tv_deposit_capital.text = HelperClass().setRupiah("0")
                    rca_tv_net_profit.text = HelperClass().setRupiah(t.result?.totalLabaBersih.toString())
                    rca_tv_prive.text = HelperClass().setRupiah(t.result?.totalPrive.toString())
                    rca_tv_final_modal.text = HelperClass().setRupiahBig(t.result?.akhir.toString())
                } else {
                    Log.d(TAG, "Gagal mendapat data laporan karena " + t.status?.message)
                    toast("Gagal mendapat data laporan")
                }
            }, { error ->
                Log.d(TAG, "Failure karena " + error.message)
                toast("sedang ada masalah dengan server")
            })
    }

    @SuppressLint("CheckResult")
    fun postReport() {
        var prefManager = SharedPrefManager(this@ReportCapitalActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val reportJson = JsonObject()
        reportJson.addProperty("tanggal", dataCapitalModel?.tanggal)
        reportJson.addProperty("awal", dataCapitalModel?.awal)
        reportJson.addProperty("akhir", dataCapitalModel?.akhir)
        reportJson.addProperty("range_tanggal", dataCapitalModel?.rangeTanggal)
        reportJson.addProperty("total_laba_bersih", dataCapitalModel?.totalLabaBersih)
        reportJson.addProperty("total_prive", dataCapitalModel?.totalPrive)

        Log.d(TAG, reportJson.toString())
        Log.d(TAG, header)

        val transaction: Observable<ReportResultDataCapitalModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .postDataReportCapital(header, reportJson)

        transaction.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.status?.success as Boolean) {
                    dataCapitalModel2 = response.result
                    Log.d(TAG, "Post Data Transaksi success")
                    Log.d(TAG, "" + dataCapitalModel2.toString())
//                    createPDF()
                    HelperClass().createPDFCapital(this@ReportCapitalActivity,dataCapitalModel2!!)
                    toast("Laporan Arus kas Berhasil")
                } else {
                    Log.d(TAG, "Post Data Transaksi failed")
                    toast("Laporan Bulan Gagal")
                }
            }, { error ->
                Log.d(TAG, "" + error.message)
                toast("Laporan Bulan Gagal " + error.message)
            })
    }

    private fun createPDF() {
        val document = Document()
        val date = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(System.currentTimeMillis())
//        val filename = SimpleDateFormat("yyyy-dd", Locale.getDefault()).format(System.currentTimeMillis())
        val filename = "Laporan Perubahan Modal " + date
        val filePath = Environment.getExternalStorageDirectory().toString() + "/Laporan/" + filename + ".pdf"
        try {
            PdfWriter.getInstance(document, FileOutputStream(filePath))

            document.open()

            var fontSize: Float
            var lineSpacing: Float
            fontSize = 24f
            lineSpacing = 10f
            var title: Paragraph = Paragraph(
                Phrase(
                    lineSpacing,
                    "Laporan Perubahan Modal " + date,
                    FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize)
                )
            )
            document.add(title)

            document.add(Chunk.NEWLINE)
            document.add(Chunk.NEWLINE)
            document.add(Chunk.NEWLINE)

            var p1 = Paragraph()
            p1.add(Chunk("Modal awal"))
            p1.tabSettings = TabSettings(56f)
            p1.add(Chunk.TABBING)
            p1.add(Chunk.TABBING)
            p1.add(Chunk.TABBING)
            p1.add(Chunk.TABBING)
            p1.add(Chunk.TABBING)
            p1.add(Chunk.TABBING)
            p1.add(Chunk("" + rca_tv_first_capital.text.toString()))
            document.add(p1)

            var p2 = Paragraph()
            p2.add(Chunk("Setoran modal"))
            p2.tabSettings = TabSettings(56f)
            p2.add(Chunk.TABBING)
            p2.add(Chunk.TABBING)
            p2.add(Chunk.TABBING)
            p2.add(Chunk("" + rca_tv_deposit_capital.text.toString()))
            document.add(p2)

            var p3 = Paragraph()
            p3.add(Chunk("Laba bersih"))
            p3.tabSettings = TabSettings(56f)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk("" + rca_tv_net_profit.text.toString()))
            document.add(p3)

            var p4 = Paragraph()
            p4.add(Chunk("Prive"))
            p4.tabSettings = TabSettings(56f)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk("( " + rca_tv_prive.text.toString() + " )"))
            document.add(p4)

            var s1 = Paragraph()
            s1.tabSettings = TabSettings(56f)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk("________________ +"))
            document.add(s1)

            var total2: Int = 0 + dataCapitalModel?.totalLabaBersih!!.toInt() - dataCapitalModel?.totalPrive!!.toInt()
            var totalP1 = Paragraph()
            totalP1.add(Chunk("Penambahan modal"))
            totalP1.tabSettings = TabSettings(56f)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk(HelperClass().setRupiah("" + total2)))
            document.add(totalP1)

            var s2 = Paragraph()
            s2.tabSettings = TabSettings(56f)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk("________________ +"))
            document.add(s2)

            var totalP2 = Paragraph()
            totalP2.add(Chunk("Modal Akhir"))
            totalP2.tabSettings = TabSettings(56f)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk(rca_tv_final_modal.text.toString()))
            document.add(totalP2)

            document.close()

            toast("PDF Laporan Perubahan modal berhasil dibuat.")
        } catch (e: Exception) {
            toast("" + e.message)
        }
    }
}
