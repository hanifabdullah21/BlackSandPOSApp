package com.singpaulee.blacksandapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmployeeModel(

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("telepon")
    val telepon: String? = null,

    @field:SerializedName("gaji")
    val gaji: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null
) : Parcelable

data class EmployeeResultListModel(
    @field:SerializedName("result")
    val result: ArrayList<EmployeeModel>? = null,

    @field:SerializedName("status")
    val status: MainModel? = null
)

data class EmployeeResultModel(
    @field:SerializedName("result")
    val result: EmployeeModel? = null,

    @field:SerializedName("status")
    val status: MainModel? = null
)