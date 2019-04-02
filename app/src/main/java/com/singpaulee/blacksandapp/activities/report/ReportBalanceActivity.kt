package com.singpaulee.blacksandapp.activities.report

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.ReportDataBalanceModel
import com.singpaulee.blacksandapp.model.ReportResultBalanceModel
import com.singpaulee.blacksandapp.model.ReportResultDataCashflow
import com.singpaulee.blacksandapp.model.ReportResultDataCashflow2
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_report_balance.*
import kotlinx.android.synthetic.main.activity_report_cash_flow.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ReportBalanceActivity : AppCompatActivity() {

    val TAG = "ReportBalance"
    private val STORAGE_CODE: Int = 100

    var dataBalanceModel: ReportDataBalanceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_balance)

        setPermission()

        rba_btn_create_report.isEnabled = false
        getDataBalance()

        rba_btn_create_report.onClick {
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
    private fun getDataBalance() {
        var header = HelperClass().getHeader(this)

        var capital: Observable<ReportResultBalanceModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getDataReportBalance(header)

        capital.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: ReportResultBalanceModel ->
                if (t.status?.success as Boolean) {
                    dataBalanceModel = t.result
                    rba_tv_cash.text = HelperClass().setRupiahBig(dataBalanceModel?.kas.toString())
                    rba_tv_capital.text = HelperClass().setRupiahBig(dataBalanceModel?.modal.toString())
                    rba_tv_final_inventory.text =
                            HelperClass().setRupiahBig(dataBalanceModel?.persediaanAkhir.toString())
                    rba_tv_debt.text = HelperClass().setRupiahBig(dataBalanceModel?.hutang.toString())
                    rba_tv_receivables.text = HelperClass().setRupiahBig(dataBalanceModel?.piutang.toString())
                    rba_tv_soil.text = HelperClass().setRupiahBig(dataBalanceModel?.tanah.toString())
                    rba_tv_equipment.text = HelperClass().setRupiahBig(dataBalanceModel?.perlengkapan.toString())
                    rba_tv_building.text = HelperClass().setRupiahBig(dataBalanceModel?.bangunan.toString())
                    rba_tv_depreciation_building.text =
                            HelperClass().setRupiahBig(dataBalanceModel?.depresiasiBangunan.toString())
                    rba_tv_vehicle.text = HelperClass().setRupiahBig(dataBalanceModel?.kendaraan.toString())
                    rba_tv_depreciation_vehicle.text =
                            HelperClass().setRupiahBig(dataBalanceModel?.depresiasiKendaraan.toString())
                    rba_tv_machine.text = HelperClass().setRupiahBig(dataBalanceModel?.peralatan.toString())
                    rba_tv_depreciation_machine.text =
                            HelperClass().setRupiahBig(dataBalanceModel?.depresiasiPeralatan.toString())
                    rba_btn_create_report.isEnabled = true
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
    private fun postReport() {
        var prefManager = SharedPrefManager(this@ReportBalanceActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val reportJson = JsonObject()
        reportJson.addProperty("tanggal", dataBalanceModel?.tanggal)
        reportJson.addProperty("tanggal_laporan", dataBalanceModel?.tanggalLaporan)
        reportJson.addProperty("kas", dataBalanceModel?.kas)
        reportJson.addProperty("modal", dataBalanceModel?.modal)
        reportJson.addProperty("persediaan_akhir", dataBalanceModel?.persediaanAkhir)
        reportJson.addProperty("hutang", dataBalanceModel?.hutang)
        reportJson.addProperty("piutang", dataBalanceModel?.piutang)
        reportJson.addProperty("tanah", dataBalanceModel?.tanah)
        reportJson.addProperty("perlengkapan", dataBalanceModel?.perlengkapan)
        reportJson.addProperty("bangunan", dataBalanceModel?.bangunan)
        reportJson.addProperty("depresiasi_bangunan", dataBalanceModel?.depresiasiBangunan)
        reportJson.addProperty("kendaraan", dataBalanceModel?.kendaraan)
        reportJson.addProperty("depresiasi_kendaraan", dataBalanceModel?.depresiasiKendaraan)
        reportJson.addProperty("peralatan", dataBalanceModel?.peralatan)
        reportJson.addProperty("depresiasi_peralatan", dataBalanceModel?.depresiasiPeralatan)
        reportJson.addProperty("aktiva", dataBalanceModel?.aktiva)
        reportJson.addProperty("passiva", dataBalanceModel?.passiva)

        val transaction: Observable<ReportResultBalanceModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .postDataReportBalance(header, reportJson)

        transaction.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.status?.success as Boolean) {
                    dataBalanceModel = response.result
                    Log.d(TAG, "Post Data Transaksi success")
                    Log.d(TAG, "" + dataBalanceModel.toString())
//                    createPDF()
                    HelperClass().createPDFNeraca(this@ReportBalanceActivity, dataBalanceModel)
                    toast("Laporan Neraca Berhasil")
                } else {
                    Log.d(TAG, "Post Data Transaksi failed " + response.status.message)
                    toast("Laporan Neraca Gagal")
                }
            }, { error ->
                Log.d(TAG, "" + error.message)
                toast("Laporan Neraca Gagal " + error.message)
            })

    }

    private fun createPDF() {
        val document = Document()
        val date = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(System.currentTimeMillis())
//        val filename = SimpleDateFormat("yyyy-dd", Locale.getDefault()).format(System.currentTimeMillis())
//        val filename = "Laporan Neraca " + date
        val filename = "Laporan Neraca " + dataBalanceModel?.tanggalLaporan
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
                    "Laporan Neraca " + date,
                    FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize)
                )
            )
            document.add(title)

            document.add(Chunk.NEWLINE)
            document.add(Chunk.NEWLINE)
            document.add(Chunk.NEWLINE)

            /*========================================== AKTIVA ================================================*/

            var p1 = Paragraph()
            p1.add(Chunk("AKTIVA"))
            document.add(p1)

            var p2 = Paragraph()
            p2.add(Chunk("Kas"))
            p2.tabSettings = TabSettings(56f)
            p2.add(Chunk.TABBING)
            p2.add(Chunk.TABBING)
            p2.add(Chunk.TABBING)
            p2.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.kas.toString())))
            document.add(p2)

            var p3 = Paragraph()
            p3.add(Chunk("Piutang"))
            p3.tabSettings = TabSettings(56f)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.piutang.toString())))
            document.add(p3)

            var p4 = Paragraph()
            p4.add(Chunk("Persediaan"))
            p4.tabSettings = TabSettings(56f)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.persediaanAkhir.toString())))
            document.add(p4)

            var p5 = Paragraph()
            p5.add(Chunk("Tanah"))
            p5.tabSettings = TabSettings(56f)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.tanah.toString())))
            document.add(p5)

            var p6 = Paragraph()
            p6.add(Chunk("Perlengkapan"))
            p6.tabSettings = TabSettings(56f)
            p6.add(Chunk.TABBING)
            p6.add(Chunk.TABBING)
            p6.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.perlengkapan.toString())))
            document.add(p6)

            var p7 = Paragraph()
            p7.add(Chunk("Bangunan"))
            p7.tabSettings = TabSettings(56f)
            p7.add(Chunk.TABBING)
            p7.add(Chunk.TABBING)
            p7.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.bangunan.toString())))
            document.add(p7)

            var p8 = Paragraph()
            p8.add(Chunk("Akmumulasi Depresiasi Bangunan"))
            p8.tabSettings = TabSettings(56f)
            p8.add(Chunk.TABBING)
            p8.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.depresiasiBangunan.toString())))
            document.add(p8)

            var p9 = Paragraph()
            p9.add(Chunk("Kendaraan"))
            p9.tabSettings = TabSettings(56f)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.kendaraan.toString())))
            document.add(p9)

            var p10 = Paragraph()
            p10.add(Chunk("Akumulasi Depresiasi Kendaraan"))
            p10.tabSettings = TabSettings(56f)
            p10.add(Chunk.TABBING)
            p10.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.depresiasiKendaraan.toString())))
            document.add(p10)

            var p11 = Paragraph()
            p11.add(Chunk("Peralatan"))
            p11.tabSettings = TabSettings(56f)
            p11.add(Chunk.TABBING)
            p11.add(Chunk.TABBING)
            p11.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.depresiasiPeralatan.toString())))
            document.add(p11)

            var p17 = Paragraph()
            p17.add(Chunk("Akumulasi Depresiasi Peralatan"))
            p17.tabSettings = TabSettings(56f)
            p17.add(Chunk.TABBING)
            p17.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.depresiasiPeralatan.toString())))
            document.add(p17)

            document.add(Chunk.NEWLINE)

            var p12 = Paragraph()
            p12.add(Chunk("PASSIVA"))
            document.add(p12)

            var p13 = Paragraph()
            p13.add(Chunk("Hutang"))
            p13.tabSettings = TabSettings(56f)
            p13.add(Chunk.TABBING)
            p13.add(Chunk.TABBING)
            p13.add(Chunk.TABBING)
            p13.add(Chunk.TABBING)
            p13.add(Chunk.TABBING)
            p13.add(Chunk.TABBING)
            p13.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.hutang.toString())))
            document.add(p13)

            var p14 = Paragraph()
            p14.add(Chunk("Modal"))
            p14.tabSettings = TabSettings(56f)
            p14.add(Chunk.TABBING)
            p14.add(Chunk.TABBING)
            p14.add(Chunk.TABBING)
            p14.add(Chunk.TABBING)
            p14.add(Chunk.TABBING)
            p14.add(Chunk.TABBING)
            p14.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.modal.toString())))
            document.add(p14)

            document.add(Chunk.NEWLINE)

            var p15 = Paragraph()
            p15.add(Chunk("Total Aktiva"))
            p15.tabSettings = TabSettings(56f)
            p15.add(Chunk.TABBING)
            p15.add(Chunk.TABBING)
            p15.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.aktiva.toString())))
            document.add(p15)

            var p16 = Paragraph()
            p16.add(Chunk("Total Passiva"))
            p16.tabSettings = TabSettings(56f)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk("" + HelperClass().setRupiahBig(dataBalanceModel?.passiva.toString())))
            document.add(p16)

            document.close()

            toast("PDF Laporan Neraca berhasil dibuat.")
        } catch (e: Exception) {
            toast("" + e.message)
        }
    }
}
