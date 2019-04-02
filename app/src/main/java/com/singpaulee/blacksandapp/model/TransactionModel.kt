package com.singpaulee.blacksandapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.lang.reflect.Constructor

@Parcelize
data class TransactionModel(

    //id of transaction
    @field:SerializedName("id")
    val id: Int? = null,

    //type of transaction ("pembelian" or "penjualan")
    @field:SerializedName("jenis")
    val jenis: String? = null,

    //faktur number of transaction
    @field:SerializedName("nofaktur")
    val noFaktur: String? = null,

    //date of transaction created
    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    //due date for repayment of transaction of debt
    @field:SerializedName("tanggal_tempo")
    var tglJatuhTempo: String? = null,

    //id supplier
    @field:SerializedName("pemasok_id")
    var supplierId: String? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    //debt of transaction
    @field:SerializedName("ph_utang")
    val hutang: Int? = null,

    @field:SerializedName("lunas")
    var lunas: String? = null,

    //the total cost of carrying each transaction
    @field:SerializedName("beban_angkut")
    var bebanAngkut: Int? = null,

    //supplier detail
    @field:SerializedName("pemasok")
    val supplier: SupplierModel? = null,

    //buyer detail
    @field:SerializedName("pelanggan")
    val buyer: BuyerModel? = null,

    //buyer id
    @field:SerializedName("pelanggan_id")
    val buyerID: String? = null,

    //list of item transaction
    @field:SerializedName("barang_transaksi")
    var itemTransaction: ArrayList<ItemTransactionModel>? = null,

    //list of items used to hold transaction items
    @field:SerializedName("barang")
    var item: ArrayList<ItemModel>? = null,

    @field:SerializedName("pelunasan")
    var listRepayment: ArrayList<DebtModel>? = null
) : Parcelable {
    constructor(
        supplierId: String?, tglJatuhTempo: String?, bebanAngkut: Int?, lunas: String?, item: ArrayList<ItemModel>?
    ) : this() {
        this.supplierId = supplierId
        this.tglJatuhTempo = tglJatuhTempo
        this.bebanAngkut = bebanAngkut
        this.lunas = lunas
        this.item = item
    }
}

data class TransactionResultModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: TransactionModel? = null
)

data class TransactionResultListModel(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ArrayList<TransactionModel>? = null
)