package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class FinancialModel(

    @field:SerializedName("keterangan")
    val keterangan: String? = null,

    @field:SerializedName("nilai")
    val nilai: Int? = null,

    @field:SerializedName("jenis")
    val jenis: String? = null,

    @field:SerializedName("saldo_kas")
    val saldoKas: BigInteger? = null,

    @field:SerializedName("kategori")
    val kategori: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null
)

data class FinancialResultListModel(
    @field:SerializedName("result")
    val result: ArrayList<FinancialModel>? = null,

    @field:SerializedName("status")
    val status: MainModel? = null
)

data class FinancialResultModel(
    @field:SerializedName("result")
    val result: FinancialModel? = null,

    @field:SerializedName("status")
    val status: MainModel? = null
)