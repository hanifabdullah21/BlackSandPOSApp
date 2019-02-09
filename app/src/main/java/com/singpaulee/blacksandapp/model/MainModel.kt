package com.singpaulee.blacksandapp.model

data class MainModel(
    val code: Int? = null,
    val success: Boolean? = null,
    val message: String? = null
)

/*Model for Login*/
data class RegisterModel(
    val status: MainModel? = null
)

