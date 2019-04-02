package com.singpaulee.blacksandapp.helper

import android.content.Context
import android.os.Environment
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.singpaulee.blacksandapp.model.ReportDataBalanceModel
import com.singpaulee.blacksandapp.model.ReportDataCapitalModel
import com.singpaulee.blacksandapp.model.ReportDataCashFlow2
import com.singpaulee.blacksandapp.model.ReportDataModel2
import kotlinx.android.synthetic.main.activity_report_capital.*
import kotlinx.android.synthetic.main.activity_report_cash_flow.*
import kotlinx.android.synthetic.main.activity_report_monthly.*
import org.jetbrains.anko.toast
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class HelperClass() {

    fun setRupiah(number: String): String {
        val localeID = Locale("in", "ID")
        val rupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        var priceRp: String = rupiah.format(number.toInt())
        var finalPriceRp = priceRp.substring(0, 2) + " " + priceRp.substring(2)
        return finalPriceRp
    }

    fun setRupiahBig(number: String): String {
        val localeID = Locale("in", "ID")
        val rupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        var priceRp: String = rupiah.format(number.toBigInteger())
        var finalPriceRp = priceRp.substring(0, 2) + " " + priceRp.substring(2)
        return finalPriceRp
    }

    fun convertDateYMDhms(date: String): String {
        var sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val dateFirst = sdf.parse(date)
        sdf = SimpleDateFormat("dd MMM yyyy hh:mm")
        var dateFinal = sdf.format(dateFirst)
        return dateFinal
    }

    fun getMonth(date: String): String {
        var month: String = ""
        var sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val dateFirst = sdf.parse(date)
        sdf = SimpleDateFormat("MM")
        var dateFinal = sdf.format(dateFirst)
        when (dateFinal) {
            "01" -> month = "Januari"
            "02" -> month = "Februari"
            "03" -> month = "Maret"
            "04" -> month = "April"
            "05" -> month = "Mei"
            "06" -> month = "Juni"
            "07" -> month = "Juli"
            "08" -> month = "Agustus"
            "09" -> month = "September"
            "10" -> month = "Oktober"
            "11" -> month = "November"
            "12" -> month = "Desember"
            else -> month = dateFinal
        }
        sdf = SimpleDateFormat("yyyy")
        var year = sdf.format(dateFirst)
        return month + " " + year
    }

    fun getHeader(context: Context): String {
        var prefManager = SharedPrefManager(context)
        var token = prefManager.getToken()
        var header = "Bearer " + token
        return header
    }

    /*================================================ P D F ============================================================*/

    fun createPDFMonthly(context: Context, model: ReportDataModel2) {
        val document = Document()
//        val date = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(System.currentTimeMillis())
        val date = getMonth(model.tanggal.toString())
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
                    "Laporan Laba Rugi Bulan Februari 2019",
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
            p1.add(Chunk("" + setRupiahBig(model.penjualan.toString())))
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
            p2.add(Chunk("" + setRupiahBig(model.persediaanAwal.toString())))
            document.add(p2)

            var pembelian = Paragraph()
            pembelian.add(Chunk("Pembelian"))
            pembelian.tabSettings = TabSettings(56f)
            pembelian.add(Chunk.TABBING)
            pembelian.add(Chunk.TABBING)
            pembelian.add(Chunk.TABBING)
            pembelian.add(Chunk("" + setRupiahBig(model.pembelian.toString())))
            document.add(pembelian)

            var bebanAngkutPembelian = Paragraph()
            bebanAngkutPembelian.add(Chunk("Beban Angkut Pembelian"))
            bebanAngkutPembelian.tabSettings = TabSettings(56f)
            bebanAngkutPembelian.add(Chunk.TABBING)
            bebanAngkutPembelian.add(Chunk.TABBING)
            bebanAngkutPembelian.add(Chunk("" + setRupiahBig(model.bebanPembelian.toString())))
            document.add(bebanAngkutPembelian)

            var persediaanAkhir = Paragraph()
            persediaanAkhir.add(Chunk("Persediaan Akhir"))
            persediaanAkhir.tabSettings = TabSettings(56f)
            persediaanAkhir.add(Chunk.TABBING)
            persediaanAkhir.add(Chunk.TABBING)
            persediaanAkhir.add(Chunk.TABBING)
            persediaanAkhir.add(Chunk("( " + setRupiahBig(model.persediaanAkhir.toString()) + " )"))
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
                model?.persediaanAwal!!.toInt() + model?.pembelian?.toInt()!! + model?.bebanPembelian?.toInt()!! - model?.persediaanAkhir?.toInt()!!
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

            var total2: Int = model?.penjualan!!.toInt() - total1
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
            bebanGaji.add(Chunk("" + HelperClass().setRupiah(model.bebanGaji.toString())))
            document.add(bebanGaji)

            var bebanOperasional = Paragraph()
            bebanOperasional.add(Chunk("Beban Listrik, Air dan Telpon"))
            bebanOperasional.tabSettings = TabSettings(56f)
            bebanOperasional.add(Chunk.TABBING)
            bebanOperasional.add(Chunk.TABBING)
            bebanOperasional.add(Chunk("" + HelperClass().setRupiah(model.bebanOperasional.toString())))
            document.add(bebanOperasional)

            var bebanAngkutPenjualan = Paragraph()
            bebanAngkutPenjualan.add(Chunk("Beban Angkut Penjualan"))
            bebanAngkutPenjualan.tabSettings = TabSettings(56f)
            bebanAngkutPenjualan.add(Chunk.TABBING)
            bebanAngkutPenjualan.add(Chunk.TABBING)
            bebanAngkutPenjualan.add(Chunk("" + model.bebanPenjualan.toString()))
            document.add(bebanAngkutPenjualan)

            var bebanPajak = Paragraph()
            bebanPajak.add(Chunk("Beban Pajak"))
            bebanPajak.tabSettings = TabSettings(56f)
            bebanPajak.add(Chunk.TABBING)
            bebanPajak.add(Chunk.TABBING)
            bebanPajak.add(Chunk.TABBING)
            bebanPajak.add(Chunk("" + HelperClass().setRupiah(model.bebanPajak.toString())))
            document.add(bebanPajak)

            var p7 = Paragraph()
            p7.add(Chunk("Depresiasi Bangunan"))
            p7.tabSettings = TabSettings(56f)
            p7.add(Chunk.TABBING)
            p7.add(Chunk.TABBING)
            p7.add(Chunk("" + HelperClass().setRupiahBig(model.depresiasiBangunan.toString())))
            document.add(p7)

            var p8 = Paragraph()
            p8.add(Chunk("Depresiasi Kendaraan"))
            p8.tabSettings = TabSettings(56f)
            p8.add(Chunk.TABBING)
            p8.add(Chunk.TABBING)
            p8.add(Chunk("" + HelperClass().setRupiahBig(model.depresiasiKendaraan.toString())))
            document.add(p8)

            var p9 = Paragraph()
            p9.add(Chunk("Depresiasi Peralatan"))
            p9.tabSettings = TabSettings(56f)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk("" + HelperClass().setRupiahBig(model.depresiasiPeralatan.toString())))
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
                model.bebanGaji!! + model.bebanOperasional!! + model.bebanPenjualan!! + model.bebanPajak!! + model.depresiasiBangunan!! + model.depresiasiKendaraan!! + model.depresiasiPeralatan!!
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

            context.toast("PDF Laporan Bulanan berhasil dibuat.")
        } catch (e: Exception) {
            context.toast("" + e.message)
        }
    }

    fun createPDFCapital(context: Context, model: ReportDataCapitalModel) {
        val document = Document()
//        val date = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(System.currentTimeMillis())
        val date = getMonth(model.tanggal.toString())
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
            p1.add(Chunk("" + setRupiahBig(model.awal.toString())))
            document.add(p1)

            var p2 = Paragraph()
            p2.add(Chunk("Setoran modal"))
            p2.tabSettings = TabSettings(56f)
            p2.add(Chunk.TABBING)
            p2.add(Chunk.TABBING)
            p2.add(Chunk.TABBING)
            p2.add(Chunk("" + setRupiahBig("0")))
            document.add(p2)

            var p3 = Paragraph()
            p3.add(Chunk("Laba bersih"))
            p3.tabSettings = TabSettings(56f)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk("" + setRupiahBig(model.totalLabaBersih.toString())))
            document.add(p3)

            var p4 = Paragraph()
            p4.add(Chunk("Prive"))
            p4.tabSettings = TabSettings(56f)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk("( " + setRupiahBig(model.totalPrive.toString()) + " )"))
            document.add(p4)

            var s1 = Paragraph()
            s1.tabSettings = TabSettings(56f)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk("________________ +"))
            document.add(s1)

            var total2: Int = 0 + model?.totalLabaBersih!!.toInt() - model?.totalPrive!!.toInt()
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
            totalP2.add(Chunk("" + setRupiahBig(model.akhir.toString())))
            document.add(totalP2)

            document.close()

            context.toast("PDF Laporan Perubahan modal berhasil dibuat.")
        } catch (e: Exception) {
            context.toast("" + e.message)
        }
    }

    fun createPDFCashflw(context: Context, model: ReportDataCashFlow2) {
        val document = Document()
        val date = getMonth(model.tanggal.toString())
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
            p2.add(Chunk("" + setRupiahBig(model.pelunasanPiutang.toString())))
            document.add(p2)

            var p3 = Paragraph()
            p3.add(Chunk("Pengeluaran kas untuk membayar hutang"))
            p3.tabSettings = TabSettings(56f)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk("(" + setRupiahBig(model.pelunasanHutang.toString()) + ")"))
            document.add(p3)

            var kumpulanBeban =
                model.bebanAngkut!! + model.bebanGaji!! + model.bebanOperasional!! + model.bebanPajak!!
            var p4 = Paragraph()
            p4.add(Chunk("Pembayaran biaya atau beban"))
            p4.tabSettings = TabSettings(56f)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk("(" + HelperClass().setRupiahBig(kumpulanBeban.toString()) + ")"))
            document.add(p4)

            var p5 = Paragraph()
            p5.add(Chunk("Arus kas bersih dari kegiatan operasi"))
            p5.tabSettings = TabSettings(56f)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk("(" + HelperClass().setRupiahBig(model?.totalOperasi.toString().replace("-", "")) + ")"))
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
            p7.add(Chunk("(" + setRupiahBig(model.assetTanah.toString()) + ")"))
            document.add(p7)

            var p8 = Paragraph()
            p8.add(Chunk("Pembelian perlengkapan"))
            p8.tabSettings = TabSettings(56f)
            p8.add(Chunk.TABBING)
            p8.add(Chunk.TABBING)
            p8.add(Chunk.TABBING)
            p8.add(Chunk("(" + setRupiahBig(model.assetPerlengkapan.toString()) + ")"))
            document.add(p8)

            var p9 = Paragraph()
            p9.add(Chunk("Pembelian bangunan"))
            p9.tabSettings = TabSettings(56f)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk("(" + setRupiahBig(model.assetBangunan.toString()) + ")"))
            document.add(p9)

            var p10 = Paragraph()
            p10.add(Chunk("Pembelian kendaraan"))
            p10.tabSettings = TabSettings(56f)
            p10.add(Chunk.TABBING)
            p10.add(Chunk.TABBING)
            p10.add(Chunk.TABBING)
            p10.add(Chunk("(" + setRupiahBig(model.assetKendaraan.toString()) + ")"))
            document.add(p10)

            var p11 = Paragraph()
            p11.add(Chunk("Pembelian peralatan"))
            p11.tabSettings = TabSettings(56f)
            p11.add(Chunk.TABBING)
            p11.add(Chunk.TABBING)
            p11.add(Chunk.TABBING)
            p11.add(Chunk.TABBING)
            p11.add(Chunk("(" + setRupiahBig(model.assetPeralatan.toString()) + ")"))
            document.add(p11)

            var p12 = Paragraph()
            p12.add(Chunk("Arus kas bersih dari kegiatan operasi"))
            p12.tabSettings = TabSettings(56f)
            p12.add(Chunk.TABBING)
            p12.add(Chunk.TABBING)
            p12.add(Chunk.TABBING)
            p12.add(Chunk.TABBING)
            p12.add(Chunk("(" + HelperClass().setRupiahBig(model?.totalInvestasi.toString()) + ")"))
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
            p16.add(Chunk("(" + HelperClass().setRupiah(model?.totalPrive.toString()) + ")"))
            document.add(p16)

            var p17 = Paragraph()
            p17.add(Chunk("Arus kas bersih dari kegiatan pendanaan"))
            p17.tabSettings = TabSettings(56f)
            p17.add(Chunk.TABBING)
            p17.add(Chunk.TABBING)
            p17.add(Chunk.TABBING)
            p17.add(Chunk.TABBING)
            p17.add(Chunk("(" + HelperClass().setRupiahBig(model?.totalPrive.toString()) + ")"))
            document.add(p17)

            document.add(Chunk.NEWLINE)

            /*=============================LAIN LAIN===============================*/

            var s1 = Paragraph()
            s1.tabSettings = TabSettings(56f)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk.TABBING)
            s1.add(Chunk("____________________ +"))
            document.add(s1)

            var p18 = Paragraph()
            p18.add(Chunk("Kenaikan saldo bersih"))
            p18.tabSettings = TabSettings(56f)
            p18.add(Chunk.TABBING)
            p18.add(Chunk.TABBING)
            p18.add(Chunk.TABBING)
            p18.add(Chunk.TABBING)
            p18.add(Chunk.TABBING)
            p18.add(Chunk.TABBING)
            p18.add(Chunk("(" + HelperClass().setRupiahBig(model?.kenaikanSaldo.toString().replace("-", "")) + ")"))
            document.add(p18)

            var p19 = Paragraph()
            p19.add(Chunk("Saldo kas awal bulan"))
            p19.tabSettings = TabSettings(56f)
            p19.add(Chunk.TABBING)
            p19.add(Chunk.TABBING)
            p19.add(Chunk.TABBING)
            p19.add(Chunk.TABBING)
            p19.add(Chunk.TABBING)
            p19.add(Chunk(HelperClass().setRupiahBig(model?.saldoAwalBulan.toString())))
            document.add(p19)

            var s2 = Paragraph()
            s2.tabSettings = TabSettings(56f)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk.TABBING)
            s2.add(Chunk("____________________ +"))
            document.add(s2)

            var p20 = Paragraph()
            p20.add(Chunk("Saldo kas akhir bulan (sekarang)"))
            p20.tabSettings = TabSettings(56f)
            p20.add(Chunk.TABBING)
            p20.add(Chunk.TABBING)
            p20.add(Chunk.TABBING)
            p20.add(Chunk.TABBING)
            p20.add(Chunk(HelperClass().setRupiahBig(model?.saldoAkhirBulan.toString())))
            document.add(p20)

            document.close()

            context.toast("PDF Laporan Perubahan Arus kas berhasil dibuat.")
        } catch (e: Exception) {
            context.toast("" + e.message)
        }
    }

    fun createPDFNeraca(context: Context, model: ReportDataBalanceModel?) {
        val document = Document()
        val date = getMonth(model?.tanggal.toString())
        val filename = "Laporan Neraca " + date
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
            p2.add(Chunk.TABBING)
            p2.add(Chunk("" + HelperClass().setRupiahBig(model?.kas.toString())))
            document.add(p2)

            var p3 = Paragraph()
            p3.add(Chunk("Piutang"))
            p3.tabSettings = TabSettings(56f)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk.TABBING)
            p3.add(Chunk("" + HelperClass().setRupiahBig(model?.piutang.toString())))
            document.add(p3)

            var p4 = Paragraph()
            p4.add(Chunk("Persediaan"))
            p4.tabSettings = TabSettings(56f)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk.TABBING)
            p4.add(Chunk("" + HelperClass().setRupiahBig(model?.persediaanAkhir.toString())))
            document.add(p4)

            var p5 = Paragraph()
            p5.add(Chunk("Tanah"))
            p5.tabSettings = TabSettings(56f)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk.TABBING)
            p5.add(Chunk("" + HelperClass().setRupiahBig(model?.tanah.toString())))
            document.add(p5)

            var p6 = Paragraph()
            p6.add(Chunk("Perlengkapan"))
            p6.tabSettings = TabSettings(56f)
            p6.add(Chunk.TABBING)
            p6.add(Chunk.TABBING)
            p6.add(Chunk.TABBING)
            p6.add(Chunk("" + HelperClass().setRupiahBig(model?.perlengkapan.toString())))
            document.add(p6)

            var p7 = Paragraph()
            p7.add(Chunk("Bangunan"))
            p7.tabSettings = TabSettings(56f)
            p7.add(Chunk.TABBING)
            p7.add(Chunk.TABBING)
            p7.add(Chunk.TABBING)
            p7.add(Chunk.TABBING)
            p7.add(Chunk("" + HelperClass().setRupiahBig(model?.bangunan.toString())))
            document.add(p7)

            var p8 = Paragraph()
            p8.add(Chunk("Akmumulasi Depresiasi Bangunan"))
            p8.tabSettings = TabSettings(56f)
            p8.add(Chunk.TABBING)
            p8.add(Chunk("(" + HelperClass().setRupiahBig(model?.depresiasiBangunan.toString()) + ")"))
            document.add(p8)

            var p9 = Paragraph()
            p9.add(Chunk("Kendaraan"))
            p9.tabSettings = TabSettings(56f)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk.TABBING)
            p9.add(Chunk("" + HelperClass().setRupiahBig(model?.kendaraan.toString())))
            document.add(p9)

            var p10 = Paragraph()
            p10.add(Chunk("Akumulasi Depresiasi Kendaraan"))
            p10.tabSettings = TabSettings(56f)
            p10.add(Chunk.TABBING)
            p10.add(Chunk("(" + HelperClass().setRupiahBig(model?.depresiasiKendaraan.toString()) + ")"))
            document.add(p10)

            var p11 = Paragraph()
            p11.add(Chunk("Peralatan"))
            p11.tabSettings = TabSettings(56f)
            p11.add(Chunk.TABBING)
            p11.add(Chunk.TABBING)
            p11.add(Chunk.TABBING)
            p11.add(Chunk.TABBING)
            p11.add(Chunk("" + HelperClass().setRupiahBig(model?.peralatan.toString())))
            document.add(p11)

            var p17 = Paragraph()
            p17.add(Chunk("Akumulasi Depresiasi Peralatan"))
            p17.tabSettings = TabSettings(56f)
            p17.add(Chunk.TABBING)
            p17.add(Chunk("(" + HelperClass().setRupiahBig(model?.depresiasiPeralatan.toString()) + ")"))
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
            p13.add(Chunk.TABBING)
            p13.add(Chunk("" + HelperClass().setRupiahBig(model?.hutang.toString())))
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
            p14.add(Chunk.TABBING)
            p14.add(Chunk("" + HelperClass().setRupiahBig(model?.modal.toString())))
            document.add(p14)

            document.add(Chunk.NEWLINE)

            var p15 = Paragraph()
            p15.add(Chunk("Total Aktiva"))
            p15.tabSettings = TabSettings(56f)
            p15.add(Chunk.TABBING)
            p15.add(Chunk.TABBING)
            p15.add(Chunk.TABBING)
            p15.add(Chunk("" + HelperClass().setRupiahBig(model?.aktiva.toString())))
            document.add(p15)

            var p16 = Paragraph()
            p16.add(Chunk("Total Passiva"))
            p16.tabSettings = TabSettings(56f)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk.TABBING)
            p16.add(Chunk("" + HelperClass().setRupiahBig(model?.passiva.toString())))
            document.add(p16)

            document.close()

            context.toast("PDF Laporan Neraca berhasil dibuat.")
        } catch (e: java.lang.Exception) {
            context.toast("" + e.message)
        }
    }
}
