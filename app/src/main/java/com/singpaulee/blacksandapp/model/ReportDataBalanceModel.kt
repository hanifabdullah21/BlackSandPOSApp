package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName

data class ReportDataBalanceModel(

    @field:SerializedName("depresiasi_kendaraan")
    val depresiasiKendaraan: Int? = null,

    @field:SerializedName("persediaan_akhir")
    val persediaanAkhir: Int? = null,

    @field:SerializedName("kendaraan")
    val kendaraan: Int? = null,

    @field:SerializedName("perlengkapan")
    val perlengkapan: Int? = null,

    @field:SerializedName("passiva")
    val passiva: Int? = null,

    @field:SerializedName("aktiva")
    val aktiva: Int? = null,

    @field:SerializedName("hutang")
    val hutang: Int? = null,

    @field:SerializedName("piutang")
    val piutang: Int? = null,

    @field:SerializedName("depresiasi_bangunan")
    val depresiasiBangunan: Int? = null,

    @field:SerializedName("tanah")
    val tanah: Int? = null,

    @field:SerializedName("bangunan")
    val bangunan: Int? = null,

    @field:SerializedName("depresiasi_peralatan")
    val depresiasiPeralatan: Int? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    @field:SerializedName("peralatan")
    val peralatan: Int? = null,

    @field:SerializedName("kas")
    val kas: Int? = null,

    @field:SerializedName("modal")
    val modal: Int? = null,

    @field:SerializedName("tanggal_laporan")
    val tanggalLaporan: String? = null
)

data class ReportResultBalanceModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ReportDataBalanceModel? = null
)

data class ReportResultListBalanceModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ArrayList<ReportDataBalanceModel>? = null
)