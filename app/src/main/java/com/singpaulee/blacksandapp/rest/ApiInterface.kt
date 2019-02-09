package com.singpaulee.blacksandapp.rest

import com.singpaulee.blacksandapp.model.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("register")
    fun register(@Field("nama") nama:String?,
                 @Field("email") email:String?,
                 @Field("password") password:String?
    ): Observable<RegisterModel>

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email:String?,
              @Field("password") password:String?
    ): Observable<LoginModel?>

    /*================================ I T E M ========================================*/

    @FormUrlEncoded
    @POST("barang")
    fun addNewItem(
        @Header("Authorization") header: String,
        @Field("kode") code: String?,
        @Field("nama") nama: String?,
        @Field("stok_minimal") stock: Int
    ): Observable<ItemResultModel>

    @GET("barang")
    fun getListItem(
        @Header("Authorization") header: String
    ): Observable<ItemResultListModel>

    /*================================ B U Y E R ========================================*/

    @FormUrlEncoded
    @POST("pelanggan")
    fun addNewBuyer(
        @Header("Authorization") header: String,
        @Field("nama") name: String?,
        @Field("email") email: String?,
        @Field("telepon") telp: String?,
        @Field("alamat") address: String?
    ): Observable<BuyerResultModel>

    /*================================ S U P P L I E R ========================================*/

    @FormUrlEncoded
    @POST("pemasok")
    fun addNewSupplier(
        @Header("Authorization") header: String,
        @Field("nama") name: String?,
        @Field("email") email: String?,
        @Field("telepon") telp: String?,
        @Field("alamat") address: String?,
        @Field("bank") bank: String?,
        @Field("no_rekening") norek: String?,
        @Field("an_rekening") anrek: String?
    ): Observable<SupplierResultModel>
}