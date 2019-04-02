package com.singpaulee.blacksandapp.model


import com.google.gson.annotations.SerializedName

data class ReportModel(

    @field:SerializedName("tahun")
    val tahun: Any? = null,

    @field:SerializedName("meta")
    val meta: ReportMetaModel? = null,

    @field:SerializedName("bulan")
    val bulan: List<ReportMonthlyModel?>? = null
)

data class ReportResultModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ReportModel? = null
)