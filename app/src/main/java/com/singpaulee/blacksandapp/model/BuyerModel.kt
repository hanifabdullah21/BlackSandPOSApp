package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName

data class BuyerModel(
	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("telepon")
	val telepon: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
)

data class BuyerResultListModel(
	@field:SerializedName("result")
	val result: ArrayList<BuyerModel?>? = null,

	@field:SerializedName("status")
	val status: MainModel? = null
)

data class BuyerResultModel(
	@field:SerializedName("result")
	val result: BuyerModel? = null,

	@field:SerializedName("status")
	val status: MainModel? = null
)