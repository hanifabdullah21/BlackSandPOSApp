package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class ItemModel(
    @field:SerializedName("id")
    val id: Int? = null,
    @field:SerializedName("kode")
    val kode: Int? = null,
    @field:SerializedName("nama")
    val nama: String? = null,
    @field:SerializedName("stok")
    val stok: Int? = null,
    @field:SerializedName("stok_minimal")
    val stokMinimal: Int? = null,
    @field:SerializedName("harga_rata")
    val hargaRata: Int? = null,
    @field:SerializedName("tanggal")
    val tanggal: String? = null
)

data class ItemResultModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ItemModel? = null
)

data class ItemResultListModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ArrayList<ItemModel>? = null
)


