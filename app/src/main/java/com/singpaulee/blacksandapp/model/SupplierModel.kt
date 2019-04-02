package com.singpaulee.blacksandapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SupplierModel(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("telepon")
    val telepon: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null,

    @field:SerializedName("bank")
    val bank: String? = null,

    @field:SerializedName("no_rekening")
    val norek: String? = null,

    @field:SerializedName("an_rekening")
    val anrek: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null
) : Parcelable

data class SupplierResultListModel(
    @field:SerializedName("result")
    val result: ArrayList<SupplierModel>? = null,

    @field:SerializedName("status")
    val status: MainModel? = null
)

data class SupplierResultModel(
    @field:SerializedName("result")
    val result: SupplierModel? = null,

    @field:SerializedName("status")
    val status: MainModel? = null
)