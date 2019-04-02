package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName

data class ReportDataCashFlow(
    @field:SerializedName("tanggal")
    val date: String? = null,

    @field:SerializedName("pelunasan")
    val pelunasan: RDCFRepayment? = null,

    @field:SerializedName("beban")
    val beban: RDCFLoad? = null,

    @field:SerializedName("total_operasi")
    val totalOperasi: Int? = null,

    @field:SerializedName("asset")
    val asset: RDCFAsset? = null,

    @field:SerializedName("total_investasi")
    val totalInvestasi: Int? = null,

    @field:SerializedName("prive")
    val prive: Int? = null,

    @field:SerializedName("kenaikan_saldo")
    val kenaikanSaldo: Int? = null,

    @field:SerializedName("saldo_awal")
    val saldoAwal: Int? = null,

    @field:SerializedName("saldo_akhir")
    val saldoAkhir: Int? = null,

    @field:SerializedName("sudah")
    val sudah: Boolean? = null
)

data class RDCFRepayment(
    @field:SerializedName("piutang")
    val piutang: Int? = null,
    @field:SerializedName("hutang")
    val hutang: Int? = null
)

data class RDCFLoad(
    @field:SerializedName("angkut")
    val angkut: Int? = null,
    @field:SerializedName("gaji")
    val gaji: Int? = null,
    @field:SerializedName("operasional")
    val operasional: Int? = null,
    @field:SerializedName("pajak")
    val pajak: Int? = null
)

data class RDCFAsset(
    @field:SerializedName("tanah")
    val tanah: Int? = null,
    @field:SerializedName("perlengkapan")
    val perlengkapan: Int? = null,
    @field:SerializedName("bangunan")
    val bangunan: Int? = null,
    @field:SerializedName("kendaraan")
    val kendaraan: Int? = null,
    @field:SerializedName("peralatan")
    val peralatan: Int? = null
)

data class ReportResultDataCashflow(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ReportDataCashFlow? = null
)