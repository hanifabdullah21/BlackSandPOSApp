package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class ProfilModel(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("nama")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("kas")
    val kas: BigInteger? = null,

    @field:SerializedName("modal")
    val modal: BigInteger? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null
)

data class ProfilResultListModel(
    @field:SerializedName("result")
    val result: ArrayList<ProfilModel>? = null,

    @field:SerializedName("status")
    val status: MainModel? = null
)

data class ProfilResultModel(
    @field:SerializedName("result")
    val result: ProfilModel? = null,

    @field:SerializedName("status")
    val status: MainModel? = null
)