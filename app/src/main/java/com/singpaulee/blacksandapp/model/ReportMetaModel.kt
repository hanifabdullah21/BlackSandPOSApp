package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName

data class ReportMetaModel(
    @field:SerializedName("jenis")
    val type: String? = null,
    @field:SerializedName("tangal")
    val date: String? = null
)