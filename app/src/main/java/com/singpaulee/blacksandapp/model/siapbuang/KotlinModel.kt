package com.singpaulee.blacksandapp.model.siapbuang

import com.google.gson.annotations.SerializedName


data class KotlinModel(

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null
)