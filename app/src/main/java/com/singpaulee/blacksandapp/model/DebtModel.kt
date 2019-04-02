package com.singpaulee.blacksandapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DebtModel(
//    @field:SerializedName("no_faktur")
//    val noFaktur: Int? = null,

    @field:SerializedName("id")
    val idPelunasan: String? = null,

    @field:SerializedName("debit")
    val debit: Int? = null,

    @field:SerializedName("kredit")
    val kredit: Int? = null,

    @field:SerializedName("saldo")
    val saldo: Int? = null,

    @field:SerializedName("nilai")
    val value: Int? = null,

    @field:SerializedName("tanggal")
    val paymentAt: String? = null,

    @field:SerializedName("keterangan")
    val information: String? = null
) : Parcelable

data class DebtResultModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: DebtModel? = null
)