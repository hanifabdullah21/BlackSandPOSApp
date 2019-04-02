package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName

data class JournalModel(
    @field:SerializedName("id")
    val id : Int? = null,

    @field:SerializedName("tanggal")
    val date: String? = null,

    @field:SerializedName("kode")
    val code: String? = null,

    @field:SerializedName("nilai")
    val value: Int? = null,

    @field:SerializedName("keterangan")
    val information: String? = null,

    @field:SerializedName("year")
    val year: String? = null,

    @field:SerializedName("month")
    val month: String? = null
)

data class JournalResultListModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ArrayList<JournalModel>? = null
)