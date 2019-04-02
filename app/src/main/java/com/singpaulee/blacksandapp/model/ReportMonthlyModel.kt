package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName

data class ReportMonthlyModel(

    @field:SerializedName("beban_gaji")
    val bebanGaji: Int? = null,

    @field:SerializedName("persediaan_akhir")
    val persediaanAkhir: Int? = null,

    @field:SerializedName("laba_kotor")
    val labaKotor: Int? = null,

    @field:SerializedName("penjualan")
    val penjualan: Int? = null,

    @field:SerializedName("persediaan_awal")
    val persediaanAwal: Int? = null,

    @field:SerializedName("laba_bersih")
    val labaBersih: Int? = null,

    @field:SerializedName("pembelian")
    val pembelian: Int? = null,

    @field:SerializedName("beban_pembelian")
    val bebanPembelian: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    @field:SerializedName("beban_penjualan")
    val bebanPenjualan: Int? = null,

    @field:SerializedName("beban_operasional")
    val bebanOperasional: Int? = null,

    @field:SerializedName("beban_pajak")
    val bebanPajak: Int? = null,

    @field:SerializedName("tanggal_laporan")
    val tanggalLaporan: String? = null
)