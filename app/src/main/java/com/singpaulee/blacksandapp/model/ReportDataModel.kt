package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName

data class ReportDataModel(

    @field:SerializedName("sudah")
    val sudah: Boolean? = null,

    @field:SerializedName("persediaan")
    val persediaan: ReportStock? = null,

    @field:SerializedName("penjualan")
    val penjualan: Int? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    @field:SerializedName("pembelian")
    val pembelian: Int? = null,

    @field:SerializedName("beban_angkut")
    val bebanAngkut: ReportTransportLoadModel? = null,

    @field:SerializedName("depresiasi")
    val depresiasi: ReportDepreciationModel? = null

)

data class ReportStock(

    @field:SerializedName("awal")
    val awal: Int? = null,

    @field:SerializedName("akhir")
    val akhir: Int? = null
)

data class ReportTransportLoadModel(

    @field:SerializedName("penjualan")
    val penjualan: Int? = null,

    @field:SerializedName("pembelian")
    val pembelian: Int? = null,

    @field:SerializedName("gaji")
    val gaji: Int? = null,

    @field:SerializedName("operasional")
    val operasional: Int? = null,

    @field:SerializedName("pajak")
    val pajak: Int? = null
)

data class ReportDepreciationModel(
    @field:SerializedName("bangunan")
    val buildings: Int? = null,
    @field:SerializedName("kendaraan")
    val vehicle: Int? = null,
    @field:SerializedName("peralatan")
    val equipment: Int? = null
)

data class ReportResultDataModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ReportDataModel? = null
)