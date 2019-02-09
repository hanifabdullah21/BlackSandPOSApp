package com.singpaulee.blacksandapp.model

/*Model for Login*/
data class LoginModel(
    val result: ResultLogin? = null,
    val status: MainModel? = null
)

/*Result Login*/
data class ResultLogin(
    val token: String? = null
)