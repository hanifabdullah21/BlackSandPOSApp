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
import kotlinx.android.synthetic.main.activity_report_monthly.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ReportMonthlyActivity : AppCompatActivity() {

    val TAG = "ReportMonthlyActivity"

    var reportDataModel: ReportDataModel? = null
    var reportDataModel2: ReportDataModel2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_monthly)

        setPermission()

        getDataReportCureentMonth()

        rma_btn_create_report.onClick {
            if (!validasi()) {
                return@onClick
            }
//            createPDF()
            postDataReportMonthly()
        }
    }

    private fun validasi(): Boolean {
        if (rma_edt_operating_expense.text.toString().isBlank()) {
            rma_edt_operating_expense.setError("Tidak boleh kosong")
            rma_edt_operating_expense.requestFocus()
            return false
        }
        if (rma_edt_salary_expense.text.toString().isBlank()) {
            rma_edt_salary_expense.setError("Tidak boleh kosong")
            rma_edt_salary_expense.requestFocus()
            return false
        }
        if (rma_edt_tax_expense.text.toString().isBlank()) {
            rma_edt_tax_expense.setError("Tidak boleh kosong")
            rma_edt_tax_expense.requestFocus()
            return false
        }
        return true
    }

    @SuppressLint("CheckResult")
    private fun getDataReportCureentMonth() {
        var prefManager = SharedPrefManager(this@ReportMonthlyActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getDataReport: Observable<ReportResultDataModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getDataReportWithoutParam(header)

        getDataReport?.subscribeOn(Schedulers.newThread())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ t: ReportResultDataModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    reportDataModel = t?.result
                    rma_tv_date.text = t.result?.tanggal
                    rma_tv_done.text =
                            if (t.result?.sudah as Boolean) "Laporan bulan ini sudah dibuat" else "Laporan bulan ini belum dibuat"
                    rma_tv_total.text = HelperClass().setRupiah(t?.result?.penjualan.toString())
                    rma_tv_total_buying.text = HelperClass().setRupiah(t?.result?.pembelian.toString())
                    rma_tv_initial_preparation.text = HelperClass().setRupiah(t?.result?.persediaan?.awal.toString())
                    rma_tv_final_preparation.text = HelperClass().setRupiah(t?.result?.persediaan?.akhir.toString())
                    rma_tv_load_of_purchase.text = HelperClass().setRupiah(t?.result?.bebanAngkut?.pembelian.toString())
                    rma_tv_sales_expenses.text = HelperClass().setRupiah(t?.result?.bebanAngkut?.penjualan.toString())
                    rma_tv_depreciation_building.text =
                            HelperClass().setRupiahBig(t?.result?.depresiasi?.buildings.toString())
                    rma_tv_depreciation_vehicle.text =
                            HelperClass().setRupiahBig(t?.result?.depresiasi?.vehicle.toString())
                    rma_tv_depreciation_equipment.text =
                            HelperClass().setRupiahBig(t?.result?.depresiasi?.equipment.toString())
                    Log.d(TAG, t?.result?.tanggal + " " + t?.result?.sudah)
                    Log.d(TAG, "Get Data Transaksi success")
                    Log.d(TAG, "" + reportDataModel?.toString())
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menampilkan barang")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }

    @SuppressLint("CheckResult")
    fun postDataReportMonthly() {
        var prefManager = SharedPrefManager(this@ReportMonthlyActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val reportStock =
            ReportStock(
                reportDataModel?.persediaan?.awal.toString().toInt(),
                reportDataModel?.persediaan?.akhir.toString().toInt()
            )
        val jsonReportStock = Gson().toJson(reportStock)

        val transportLoad = ReportTransportLoadModel(
            reportDataModel?.bebanAngkut?.penjualan,
            reportDataModel?.bebanAngkut?.pembelian,
            rma_edt_salary_expense.text.toString().toInt(),
            rma_edt_operating_expense.text.toString().toInt(),
            rma_edt_tax_expense.text.toString().toInt()
        )
        val jsonTransportLoad = Gson().toJson(transportLoad)

        val depreaciation = ReportDepreciationModel(
            reportDataModel?.depresiasi?.buildings,
            reportDataModel?.depresiasi?.vehicle,
            reportDataModel?.depresiasi?.equipment
        )
        val jsonDepreciation = Gson().toJson(depreaciation)

        val reportJson = JsonObject()
        reportJson.addProperty("tanggal", reportDataModel?.tanggal)
        reportJson.addProperty("pembelian", reportDataModel?.pembelian.toString())
        reportJson.addProperty("penjualan", reportDataModel?.penjualan.toString())
        reportJson.addProperty("tgl", rma_edt_date.text.toString())
        reportJson.add("beban_angkut", JsonParser().parse(jsonTransportLoad).asJsonObject)
        reportJson.add("persediaan", JsonParser().parse(jsonReportStock).asJsonObject)
        reportJson.add("depresiasi", JsonParser().parse(jsonDepreciation).asJsonObject)

        Log.d(TAG, reportJson.toString())
        Log.d(TAG, header)

        val transaction: Observable<ReportResultDataModel2> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .postDataReportMonthly(header, reportJson)

        transaction.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.status?.success as Boolean) {
                    reportDataModel2 = response.result
                    Log.d(TAG, "Post Data Transaksi success")
                    Log.d(TAG, "" + reportDataModel2.toString())
//                    createPDF()
                    HelperClass().createPDFMonthly(this@ReportMonthlyActivity, reportDataModel2!!)
                    toast("Laporan Bulan Berhasil")
                } else {
                    Log.d(TAG, "Post Data Transaksi success")
                    toast("Laporan Bulan Gagal")
                }
            }, { error ->
                Log.d(TAG, "" + error.message)
                toast("Laporan Bulan Gagal " + error.message)
            })
    }

    private val STORAGE_CODE: Int = 100

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

    private fun createPDF() {
        val document = Document()
        val date = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(System.currentTimeMillis())
//        val filename = SimpleDateFormat("yyyy-dd", Locale.getDefault()).format(System.currentTimeMillis())
        val filename = "Laporan Bulanan " + date
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
                    "Laporan Laba Rugi Bulan $date",
                    FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize)
                )
            )
            document.add(title)

            document.add(Chunk.NEWLINE)
            document.add(Chunk.NEWLINE)
            document.add(Chunk.NEWLINE)

            var p1 = Paragraph()
            p1.add(Chunk("Penjualan bersih"))
            p1.tabSettings = TabSettings(56f)
            p1.add(Chunk.TABBING)
            p1.add(Chunk.TABBING)
            p1.add(Chunk.TABBING)
            p1.add(Chunk.TABBING)
            p1.add(Chunk.TABBING)
            p1.add(Chunk.TABBING)
            p1.add(Chunk("" + rma_tv_total.text.toString()))
            document.add(p1)

            var hargaPokokPenjualan = Paragraph()
            hargaPokokPenjualan.add("Harga Pokok Penjualan :")
            document.add(hargaPokokPenjualan)

            var p2 = Paragraph()
            p2.add(Chunk("Persediaan Awal"))
            p2.tabSettings = TabSettings(56f)
            p2.add(Chunk.TABBING)
            p2.add(Chunk.TABBING)
            p2.add(Chunk.TABBING)
            p2.add(Chunk("" + rma_tv_initial_preparation.text.toString()))
            document.add(p2)

            var pembelian = Paragraph()
            pembelian.add(Chunk("Pembelian"))
            pembelian.tabSettings = TabSettings(56f)
            pembelian.add(Chunk.TABBING)
            pembelian.add(Chunk.TABBING)
            pembelian.add(Chunk.TABBING)
            pembelian.add(Chunk("" + rma_tv_total_buying.text.toString()))
            document.add(pembelian)

            var bebanAngkutPembelian = Paragraph()
            bebanAngkutPembelian.add(Chunk("Beban Angkut Pembelian"))
            bebanAngkutPembelian.tabSettings = TabSettings(56f)
            bebanAngkutPembelian.add(Chunk.TABBING)
            bebanAngkutPembelian.add(Chunk.TABBING)
            bebanAngkutPembelian.add(Chunk("" + rma_tv_load_of_purchase.text.toString()))
            document.add(bebanAngkutPembelian)

            var persediaanAkhir = Paragraph()
            persediaanAkhir.add(Chunk("Persediaan Akhir"))
            persediaanAkhir.tabSettings = TabSettings(56f)
            persediaanAkhir.add(Chunk.TABBING)
            persediaanAkhir.add(Chunk.TABBING)
            persediaanAkhir.add(Chunk.TABBING)
            persediaanAkhir.add(Chunk("( " + rma_tv_final_preparation.text.toString() + " )"))
            document.add(persediaanAkhir)

            var s1 = Paragraph()
            s1.tabSettings = TabSettings(56f)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk("________________ +"))
            document.add(s1)

            var total1: Int =
                reportDataModel2?.persediaanAwal!!.toInt() + reportDataModel2?.pembelian?.toInt()!! + reportDataModel2?.bebanPembelian?.toInt()!! - reportDataModel2?.persediaanAkhir?.toInt()!!
            var totalP1 = Paragraph()
            totalP1.tabSettings = TabSettings(56f)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk.TABBING)
            totalP1.add(Chunk(HelperClass().setRupiah("" + total1)))
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
            s2.add(Chunk("________________ -"))
            document.add(s2)

            var total2: Int = reportDataModel?.penjualan!!.toInt() - total1
            var totalP2 = Paragraph()
            totalP2.add(Chunk("Laba Kotor"))
            totalP2.tabSettings = TabSettings(56f)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk.TABBING)
            totalP2.add(Chunk(HelperClass().setRupiah("" + total2)))
            document.add(totalP2)

            var beban = Paragraph()
            beban.add("Beban beban :")
            document.add(beban)

            var bebanGaji = Paragraph()
            bebanGaji.add(Chunk("Beban Gaji"))
            bebanGaji.tabSettings = TabSettings(56f)
            bebanGaji.add(Chunk.TABBING)
            bebanGaji.add(Chunk.TABBING)
            bebanGaji.add(Chunk.TABBING)
            bebanGaji.add(Chunk("" + HelperClass().setRupiah(rma_edt_salary_expense.text.toString())))
            document.add(bebanGaji)

            var bebanOperasional = Paragraph()
            bebanOperasional.add(Chunk("Beban Listrik, Air dan Telpon"))
            bebanOperasional.tabSettings = TabSettings(56f)
            bebanOperasional.add(Chunk.TABBING)
            bebanOperasional.add(Chunk.TABBING)
            bebanOperasional.add(Chunk("" + HelperClass().setRupiah(rma_edt_operating_expense.text.toString())))
            document.add(bebanOperasional)

            var bebanAngkutPenjualan = Paragraph()
            bebanAngkutPenjualan.add(Chunk("Beban Angkut Penjualan"))
            bebanAngkutPenjualan.tabSettings = TabSettings(56f)
            bebanAngkutPenjualan.add(Chunk.TABBING)
            bebanAngkutPenjualan.add(Chunk.TABBING)
            bebanAngkutPenjualan.add(Chunk("" + rma_tv_sales_expenses.text.toString()))
            document.add(bebanAngkutPenjualan)

            var bebanPajak = Paragraph()
            bebanPajak.add(Chunk("Beban Pajak"))
            bebanPajak.tabSettings = TabSettings(56f)
            bebanPajak.add(Chunk.TABBING)
            bebanPajak.add(Chunk.TABBING)
            bebanPajak.add(Chunk.TABBING)
            bebanPajak.add(Chunk("" + HelperClass().setRupiah(rma_edt_tax_expense.text.toString())))
            document.add(bebanPajak)

            var p7 = Paragraph()
            p7.add(Chunk("Depresiasi Bangunan"))
            p7.tabSettings = TabSettings(56f)
            p7.add(Chunk.TABBING)
            p7.add(Chunk.TABBING)
            p7.add(Chunk.TABBING)
            p7.add(Chunk("" + HelperClass().setRupiahBig(reportDataModel2?.depresiasiBangunan.toString())))
            document.add(p7)

            var p8 = Paragraph()
            p8.add(Chunk("Depresiasi Kendaraan"))
            p8.tabSettings = TabSettings(56f)
            p8.add(Chunk.TABBING)
            p8.add(Chunk.TABBING)
            p8.add(Chunk.TABBING)
            p8.add(Chunk("" + HelperClass().setRupiahBig(reportDataModel2?.depresiasiKendaraan.toString())))
            document.add(p8)

            var p9 = Paragraph()
            p9.add(Chunk("Depresiasi Peralatan"))
            p9.tabSettings = TabSettings(56f)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk("" + HelperClass().setRupiahBig(reportDataModel2?.depresiasiPeralatan.toString())))
            document.add(p9)

            var s3 = Paragraph()
            s3.tabSettings = TabSettings(56f)
            s3.add(Chunk.TABBING)
            s3.add(Chunk.TABBING)
            s3.add(Chunk.TABBING)
            s3.add(Chunk.TABBING)
            s3.add(Chunk("________________ +"))
            document.add(s3)

            var total3: Int =
                rma_edt_salary_expense.text.toString().toInt() + rma_edt_operating_expense.text.toString().toInt() + reportDataModel?.bebanAngkut?.penjualan!! + rma_edt_tax_expense.text.toString().toInt() + reportDataModel2?.depresiasiBangunan!! + reportDataModel2?.depresiasiKendaraan!! + reportDataModel2?.depresiasiPeralatan!!
            var totalP3 = Paragraph()
            totalP3.add(Chunk("Total Beban"))
            totalP3.tabSettings = TabSettings(56f)
            totalP3.add(Chunk.TABBING)
            totalP3.add(Chunk.TABBING)
            totalP3.add(Chunk.TABBING)
            totalP3.add(Chunk.TABBING)
            totalP3.add(Chunk.TABBING)
            totalP3.add(Chunk.TABBING)
            totalP3.add(Chunk(HelperClass().setRupiah("" + total3)))
            document.add(totalP3)

            var s4 = Paragraph()
            s4.tabSettings = TabSettings(56f)
            s4.add(Chunk.TABBING)
            s4.add(Chunk.TABBING)
            s4.add(Chunk.TABBING)
            s4.add(Chunk.TABBING)
            s4.add(Chunk.TABBING)
            s4.add(Chunk.TABBING)
            s4.add(Chunk.TABBING)
            s4.add(Chunk("________________ -"))
            document.add(s4)

            var total4: Int = total2 - total3
            var totalP4 = Paragraph()
            totalP4.add(Chunk("Laba Bersih"))
            totalP4.tabSettings = TabSettings(56f)
            totalP4.add(Chunk.TABBING)
            totalP4.add(Chunk.TABBING)
            totalP4.add(Chunk.TABBING)
            totalP4.add(Chunk.TABBING)
            totalP4.add(Chunk.TABBING)
            totalP4.add(Chunk.TABBING)
            totalP4.add(Chunk(HelperClass().setRupiah("" + total4)))
            document.add(totalP4)

            document.close()

            toast("PDF Laporan Bulanan berhasil dibuat.")
        } catch (e: Exception) {
            toast("" + e.message)
        }
    }
}
