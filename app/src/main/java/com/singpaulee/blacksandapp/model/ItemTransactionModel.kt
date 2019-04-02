package com.singpaulee.blacksandapp.model

import android.content.ClipData
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemTransactionModel(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("kg")
    val kg: Int? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("saldo_kg")
    val saldoKg: Int? = null,

    @field:SerializedName("harga_rata")
    val hargaRata: Int? = null,

    @field:SerializedName("saldo_rp")
    val saldoRp: Int? = null,

    @field:SerializedName("barang")
    val item: ItemModel? = null
) : Parcelable

data class ItemTransactionResultModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ItemTransactionModel? = null
)

data class ItemTransactionResultListModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ArrayList<ItemTransactionModel>? = null
)