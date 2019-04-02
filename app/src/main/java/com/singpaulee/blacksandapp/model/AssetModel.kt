package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName

data class AssetModel(

    @field:SerializedName("nilai_sekarang")
    val nilaiSekarang: Int? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("nilai_penyusutan")
    val nilaiPenyusutan: Int? = null,

    @field:SerializedName("harga_beli")
    val hargaBeli: Int? = null,

    @field:SerializedName("kategori")
    val kategori: String? = null,

    @field:SerializedName("umur_tahun")
    val umurTahun: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    @field:SerializedName("masa_berakhir")
    val masaBerakhir: String? = null
)

data class AssetResultListModel(
    @field:SerializedName("result")
    val result: ArrayList<AssetModel>? = null,

    @field:SerializedName("status")
    val status: MainModel? = null
)

data class AssetResultModel(
    @field:SerializedName("result")
    val result: AssetModel? = null,

    @field:SerializedName("status")
    val status: MainModel? = null
)