package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class ReportDataCapitalModel(

    @field:SerializedName("total_laba_bersih")
    val totalLabaBersih: Int? = null,

    @field:SerializedName("total_prive")
    val totalPrive: Int? = null,

    @field:SerializedName("sudah")
    val sudah: Boolean? = null,

    @field:SerializedName("range_tanggal")
    val rangeTanggal: String? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    @field:SerializedName("awal")
    val awal: BigInteger? = null,

    @field:SerializedName("akhir")
    val akhir: BigInteger? = null
)

data class ReportResultDataCapitalModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ReportDataCapitalModel? = null
)

data class ReportResultListCapitalModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ArrayList<ReportDataCapitalModel>? = null
)

