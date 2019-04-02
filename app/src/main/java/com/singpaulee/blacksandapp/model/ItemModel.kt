package com.singpaulee.blacksandapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import retrofit2.http.Field

@Parcelize
data class ItemModel(
    @field:SerializedName("id")
    var id: Int? = null,
    @field:SerializedName("kg")
    var kg: Int? = null,
    @field:SerializedName("kode")
    val kode: String? = null,
    @field:SerializedName("nama")
    val nama: String? = null,
    @field:SerializedName("stok")
    val stok: Int? = null,
    @field:SerializedName("stok_minimal")
    val stokMinimal: Int? = null,
    @field:SerializedName("harga_rata")
    val hargaRata: Int? = null,
    @field:SerializedName("tanggal")
    val tanggal: String? = null,
    @field:SerializedName("jumlah")
    var jumlahPesan: Int? = null,
    @field:SerializedName("harga")
    var hargaPesan: Int? = null,
    @field:SerializedName("total")
    var total: Int? = null,
    @field:SerializedName("saldo_kg")
    var saldoKg: Int? = null,
    @field:SerializedName("saldo_rp")
    var saldoRp: Int? = null,
    @field:SerializedName("transaksi")
    var transaction: TransactionModel? = null

) : Parcelable

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


