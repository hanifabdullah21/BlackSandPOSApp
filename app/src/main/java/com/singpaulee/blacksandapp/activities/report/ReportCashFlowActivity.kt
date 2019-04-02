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
import kotlinx.android.synthetic.main.activity_report_cash_flow.*
import kotlinx.android.synthetic.main.activity_report_monthly.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ReportCashFlowActivity : AppCompatActivity() {

    val TAG = "ReportCashFlow"
    private val STORAGE_CODE: Int = 100

    var dataCashFlow: ReportDataCashFlow? = null
    var dataCashFlow2: ReportDataCashFlow2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_cash_flow)

        setPermission()

        rcfa_btn_create_report.isEnabled = false
        getDataCashflow()

        rcfa_btn_create_report.onClick {
            postDataReportCashflow()
        }
    }

    @SuppressLint("CheckResult")
    fun postDataReportCashflow() {
        var prefManager = SharedPrefManager(this@ReportCashFlowActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val pelunasan =
            RDCFRepayment(
                dataCashFlow?.pelunasan?.piutang,
                dataCashFlow?.pelunasan?.hutang
            )
        val jsonReportPelunasan = Gson().toJson(pelunasan)

        val beban =
            RDCFLoad(
                dataCashFlow?.beban?.angkut,
                dataCashFlow?.beban?.gaji,
                dataCashFlow?.beban?.operasional,
                dataCashFlow?.beban?.pajak
            )
        val jsonReportBeban = Gson().toJson(beban)

        val aset =
            RDCFAsset(
                dataCashFlow?.asset?.tanah,
                dataCashFlow?.asset?.perlengkapan,
                dataCashFlow?.asset?.bangunan,
                dataCashFlow?.asset?.kendaraan,
                dataCashFlow?.asset?.peralatan
            )
        val jsonReportAset = Gson().toJson(aset)

        val reportJson = JsonObject()
        reportJson.addProperty("tanggal", dataCashFlow?.date)
        reportJson.add("pelunasan", JsonParser().parse(jsonReportPelunasan).asJsonObject)
        reportJson.add("beban", JsonParser().parse(jsonReportBeban).asJsonObject)
        reportJson.addProperty("total_operasi", dataCashFlow?.totalOperasi)
        reportJson.add("asset", JsonParser().parse(jsonReportAset).asJsonObject)
        reportJson.addProperty("total_investasi", dataCashFlow?.totalInvestasi)
        reportJson.addProperty("prive", dataCashFlow?.prive)
        reportJson.addProperty("kenaikan_saldo", dataCashFlow?.kenaikanSaldo)
        reportJson.addProperty("saldo_awal", dataCashFlow?.saldoAwal)
        reportJson.addProperty("saldo_akhir", dataCashFlow?.saldoAkhir)

        Log.d(TAG, reportJson.toString())
        Log.d(TAG, header)

        val transaction: Observable<ReportResultDataCashflow2> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .postDataReportCashflow(header, reportJson)

        transaction.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.status?.success as Boolean) {
                    dataCashFlow2 = response.result
                    Log.d(TAG, "Post Data Transaksi success")
                    Log.d(TAG, "" + dataCashFlow2.toString())
//                    createPDF()
                    HelperClass().createPDFCashflw(this@ReportCashFlowActivity, dataCashFlow2!!)
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
    private fun getDataCashflow() {
        var header = HelperClass().getHeader(this)

        var capital: Observable<ReportResultDataCashflow> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getDataReportCashflow(header)

        capital.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: ReportResultDataCashflow ->
                if (t.status?.success as Boolean) {
                    dataCashFlow = t.result
                    rcfa_tv_piutang.text = HelperClass().setRupiahBig(dataCashFlow?.pelunasan?.piutang.toString())
                    rcfa_tv_hutang.text = HelperClass().setRupiahBig(dataCashFlow?.pelunasan?.hutang.toString())
                    rcfa_tv_beban_angkut.text = HelperClass().setRupiahBig(dataCashFlow?.beban?.angkut.toString())
                    rcfa_tv_beban_gaji.text = HelperClass().setRupiahBig(dataCashFlow?.beban?.gaji.toString())
                    rcfa_tv_beban_operasional.text =
                            HelperClass().setRupiahBig(dataCashFlow?.beban?.operasional.toString())
                    rcfa_tv_beban_pajak.text = HelperClass().setRupiahBig(dataCashFlow?.beban?.pajak.toString())
                    rcfa_tv_tanah.text = HelperClass().setRupiahBig(dataCashFlow?.asset?.tanah.toString())
                    rcfa_tv_perlengkapan.text =
                            HelperClass().setRupiahBig(dataCashFlow?.asset?.perlengkapan.toString())
                    rcfa_tv_bangunan.text = HelperClass().setRupiahBig(dataCashFlow?.asset?.bangunan.toString())
                    rcfa_tv_kendaraan.text = HelperClass().setRupiahBig(dataCashFlow?.asset?.kendaraan.toString())
                    rcfa_tv_peralatan.text = HelperClass().setRupiahBig(dataCashFlow?.asset?.peralatan.toString())
                    rcfa_tv_tambahan_modal.text = HelperClass().setRupiahBig(0.toString())
                    rcfa_tv_pinjam_bank.text = HelperClass().setRupiahBig(0.toString())
                    rcfa_tv_prive.text = HelperClass().setRupiahBig(dataCashFlow?.prive.toString())
                    rcfa_btn_create_report.isEnabled = true
                } else {
                    Log.d(TAG, "Gagal mendapat data laporan karena " + t.status?.message)
                    toast("Gagal mendapat data laporan")
                }
            }, { error ->
                Log.d(TAG, "Failure karena " + error.message)
                toast("sedang ada masalah dengan server")
            })
    }

    private fun createPDF() {
        val document = Document()
        val date = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(System.currentTimeMillis())
//        val filename = SimpleDateFormat("yyyy-dd", Locale.getDefault()).format(System.currentTimeMillis())
        val filename = "Laporan Arus Kas " + date
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
                    "Laporan Arus Kas " + date,
                    FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize)
                )
            )
            document.add(title)

            document.add(Chunk.NEWLINE)
            document.add(Chunk.NEWLINE)
            document.add(Chunk.NEWLINE)

            /*=============================ARUS KAS DARI KEGIATAN OPERASI===============================*/

            var p1 = Paragraph()
            p1.add(Chunk("ARUS KAS DARI KEGIATAN OPERASI"))
            document.add(p1)

            var p2 = Paragraph()
            p2.add(Chunk("Penerimaan kas dari pelanggan"))
            p2.tabSettings = TabSettings(56f)
            p2.add(Chunk.TABBING)
            p2.add(Chunk.TABBING)
            p2.add(Chunk("" + rcfa_tv_piutang.text.toString()))
            document.add(p2)

            var p3 = Paragraph()
            p3.add(Chunk("Pengeluaran kas untuk membayar hutang"))
            p3.tabSettings = TabSettings(56f)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk("" + rcfa_tv_hutang.text.toString()))
            document.add(p3)

            var kumpulanBeban =
                dataCashFlow2?.bebanAngkut!! + dataCashFlow2?.bebanGaji!! + dataCashFlow2?.bebanOperasional!! + dataCashFlow2?.bebanPajak!!
            var p4 = Paragraph()
            p4.add(Chunk("Pembayaran biaya atau beban"))
            p4.tabSettings = TabSettings(56f)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk(HelperClass().setRupiahBig(kumpulanBeban.toString())))
            document.add(p4)

            var p5 = Paragraph()
            p5.add(Chunk("Arus kas bersih dari kegiatan operasi"))
            p5.tabSettings = TabSettings(56f)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk(HelperClass().setRupiahBig(dataCashFlow2?.totalOperasi.toString())))
            document.add(p5)

            document.add(Chunk.NEWLINE)

            /*=============================ARUS KAS DARI KEGIATAN INVESTASI===============================*/

            var p6 = Paragraph()
            p6.add(Chunk("ARUS KAS DARI KEGIATAN INVESTASI"))
            document.add(p6)

            var p7 = Paragraph()
            p7.add(Chunk("Pembelian tanah"))
            p7.tabSettings = TabSettings(56f)
            p7.add(Chunk.TABBING)
            p7.add(Chunk.TABBING)
            p7.add(Chunk.TABBING)
            p7.add(Chunk.TABBING)
            p7.add(Chunk("" + rcfa_tv_tanah.text.toString()))
            document.add(p7)

            var p8 = Paragraph()
            p8.add(Chunk("Pembelian perlengkapan"))
            p8.tabSettings = TabSettings(56f)
            p8.add(Chunk.TABBING)
            p8.add(Chunk.TABBING)
            p8.add(Chunk.TABBING)
            p8.add(Chunk("" + rcfa_tv_perlengkapan.text.toString()))
            document.add(p8)

            var p9 = Paragraph()
            p9.add(Chunk("Pembelian bangunan"))
            p9.tabSettings = TabSettings(56f)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk("" + rcfa_tv_bangunan.text.toString()))
            document.add(p9)

            var p10 = Paragraph()
            p10.add(Chunk("Pembelian kendaraan"))
            p10.tabSettings = TabSettings(56f)
            p10.add(Chunk.TABBING)
            p10.add(Chunk.TABBING)
            p10.add(Chunk.TABBING)
            p10.add(Chunk("" + rcfa_tv_kendaraan.text.toString()))
            document.add(p10)

            var p11 = Paragraph()
            p11.add(Chunk("Pembelian peralatan"))
            p11.tabSettings = TabSettings(56f)
            p11.add(Chunk.TABBING)
            p11.add(Chunk.TABBING)
            p11.add(Chunk.TABBING)
            p11.add(Chunk.TABBING)
            p11.add(Chunk("" + rcfa_tv_peralatan.text.toString()))
            document.add(p11)

            var p12 = Paragraph()
            p12.add(Chunk("Arus kas bersih dari kegiatan operasi"))
            p12.tabSettings = TabSettings(56f)
            p12.add(Chunk.TABBING)
            p12.add(Chunk.TABBING)
            p12.add(Chunk.TABBING)
            p12.add(Chunk.TABBING)
            p12.add(Chunk(HelperClass().setRupiahBig(dataCashFlow2?.totalInvestasi.toString())))
            document.add(p12)

            document.add(Chunk.NEWLINE)

            /*=============================ARUS KAS DARI KEGIATAN PENDANAAN===============================*/

            var p13 = Paragraph()
            p13.add(Chunk("ARUS KAS DARI KEGIATAN PENDANAAN"))
            document.add(p13)

            var p14 = Paragraph()
            p14.add(Chunk("Setor modal pemilik"))
            p14.tabSettings = TabSettings(56f)
            p14.add(Chunk.TABBING)
            p14.add(Chunk.TABBING)
            p14.add(Chunk.TABBING)
            p14.add(Chunk.TABBING)
            p14.add(Chunk(HelperClass().setRupiah("0")))
            document.add(p14)

            var p15 = Paragraph()
            p15.add(Chunk("Meminjam bank"))
            p15.tabSettings = TabSettings(56f)
            p15.add(Chunk.TABBING)
            p15.add(Chunk.TABBING)
            p15.add(Chunk.TABBING)
            p15.add(Chunk.TABBING)
            p15.add(Chunk(HelperClass().setRupiah("0")))
            document.add(p15)

            var p16 = Paragraph()
            p16.add(Chunk("Pengambilan prive"))
            p16.tabSettings = TabSettings(56f)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk(HelperClass().setRupiah(dataCashFlow2?.totalPrive.toString())))
            document.add(p16)

            var p17 = Paragraph()
            p17.add(Chunk("Arus kas bersih dari kegiatan pendanaan"))
            p17.tabSettings = TabSettings(56f)
            p17.add(Chunk.TABBING)
            p17.add(Chunk.TABBING)
            p17.add(Chunk.TABBING)
            p17.add(Chunk.TABBING)
            p17.add(Chunk(HelperClass().setRupiahBig(dataCashFlow2?.totalPrive.toString())))
            document.add(p17)

            document.add(Chunk.NEWLINE)

            /*=============================LAIN LAIN===============================*/

            var p18 = Paragraph()
            p18.add(Chunk("Kenaikan saldo bersih"))
            p18.tabSettings = TabSettings(56f)
            p18.add(Chunk.TABBING)
            p18.add(Chunk.TABBING)
            p18.add(Chunk.TABBING)
            p18.add(Chunk.TABBING)
            p18.add(Chunk.TABBING)
            p18.add(Chunk(HelperClass().setRupiahBig(dataCashFlow2?.kenaikanSaldo.toString().replace("-", ""))))
            document.add(p18)

            var p19 = Paragraph()
            p19.add(Chunk("Saldo kas awal bulan"))
            p19.tabSettings = TabSettings(56f)
            p19.add(Chunk.TABBING)
            p19.add(Chunk.TABBING)
            p19.add(Chunk.TABBING)
            p19.add(Chunk.TABBING)
            p19.add(Chunk.TABBING)
            p19.add(Chunk(HelperClass().setRupiahBig(dataCashFlow2?.saldoAwalBulan.toString())))
            document.add(p19)

            var p20 = Paragraph()
            p20.add(Chunk("Saldo kas akhir bulan (sekarang)"))
            p20.tabSettings = TabSettings(56f)
            p20.add(Chunk.TABBING)
            p20.add(Chunk.TABBING)
            p20.add(Chunk.TABBING)
            p20.add(Chunk.TABBING)
            p20.add(Chunk(HelperClass().setRupiahBig(dataCashFlow2?.saldoAkhirBulan.toString())))
            document.add(p20)

            document.close()

            toast("PDF Laporan Perubahan modal berhasil dibuat.")
        } catch (e: Exception) {
            toast("" + e.message)
        }
    }
}
