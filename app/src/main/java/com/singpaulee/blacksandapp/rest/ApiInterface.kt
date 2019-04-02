package com.singpaulee.blacksandapp.rest

import com.google.gson.JsonObject
import com.singpaulee.blacksandapp.model.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("nama") nama: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Observable<RegisterModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Observable<LoginModel?>

    /*================================ P R O F I L ========================================*/

    @GET("profil")
    fun getProfil(
        @Header("Authorization") header: String
    ): Observable<ProfilResultModel>

    @FormUrlEncoded
    @POST("profil/prive")
    fun postPrive(
        @Header("Authorization") header: String,
        @Field("prive") prive: Int,
        @Field("keterangan") keterangan: String,
        @Field("tgl") tanggal: String
    ): Observable<FinancialResultModel>

    @GET("profil/keuangan")
    fun getFinancial(
        @Header("Authorization") header: String,
        @Query("kategori") kategori: String
    ): Observable<FinancialResultListModel>

    @GET("profil/jurnal")
    fun getListJournalAll(
        @Header("Authorization") header: String
    ): Observable<JournalResultListModel>

    @GET("profil/jurnal")
    fun getListJournalByDate(
        @Header("Authorization") header: String,
        @Query("tanggal") date: String
//        @Query("latest") latest: String
    ): Observable<JournalResultListModel>

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

    @GET("barang/{id}/transaksi")
    fun getListItemTransaction(
        @Header("Authorization") header: String,
        @Path("id") idBarang: Int
    ): Observable<ItemResultListModel>

    /*================================ E M P L O Y E E ========================================*/

    @FormUrlEncoded
    @POST("karyawan")
    fun addNewEmployee(
        @Header("Authorization") header: String,
        @Field("nama") name: String?,
        @Field("email") email: String?,
        @Field("telepon") telp: String?,
        @Field("alamat") address: String?,
        @Field("gaji") salary: Int?
    ): Observable<EmployeeResultModel>

    @GET("karyawan")
    fun getListEmployee(
        @Header("Authorization") header: String
    ): Observable<EmployeeResultListModel>

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

    @GET("pelanggan")
    fun getListBuyer(
        @Header("Authorization") header: String
    ): Observable<BuyerResultListModel>

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

    @GET("pemasok")
    fun getListSupplier(
        @Header("Authorization") header: String
    ): Observable<SupplierResultListModel>

    /*================================ T R A N S A C T I O N ========================================*/

    @Headers("Content-Type: application/json")
    @POST("transaksi/beli")
    fun postNewTransactionBuy(
        @Header("Authorization") header: String,
        @Body transaction: JsonObject
    ): Observable<ItemTransactionResultModel>

    @Headers("Content-Type: application/json")
    @POST("transaksi/jual")
    fun postNewTransactionSell(
        @Header("Authorization") header: String,
        @Body transaction: JsonObject
    ): Observable<ItemTransactionResultModel>

    /*Get List transaction buy or sell depend on type*/
    @GET("transaksi")
    fun getListTransaction(
        @Header("Authorization") header: String,
        @Query("jenis") type: String
    ): Observable<TransactionResultListModel>

    @GET("transaksi/{id}")
    fun getDataOfTransaction(
        @Header("Authorization") header: String,
        @Path("id") transactionID: Int
    ): Observable<TransactionResultModel>

    @FormUrlEncoded
    @POST("transaksi/{id}/pelunasan")
    fun postRepayment(
        @Header("Authorization") header: String,
        @Path("id") transactionID: Int,
        @Field("nilai") value: Int,
        @Field("keterangan") information: String,
        @Field("tgl") tgl: String
    ): Observable<DebtResultModel>

    /*================================ A S S E T ========================================*/

    @FormUrlEncoded
    @POST("asset")
    fun postNewAsset(
        @Header("Authorization") header: String,
        @Field("nama") nama: String,
        @Field("kategori") kategori: String,
        @Field("nilai") nilai: Int,
        @Field("umur") umur: Int,
        @Field("tgl") tanggal: String
    ): Observable<AssetResultModel>

    @GET("asset")
    fun getAsset(
        @Header("Authorization") header: String,
        @Query("kategori") kategori: String
    ): Observable<AssetResultListModel>

    /*================================ R E P O R T ========================================*/

    @GET("laporan/bulanan")
    fun getDataReportWithoutParam(
        @Header("Authorization") header: String
    ): Observable<ReportResultDataModel>

    @Headers("Content-Type: application/json")
    @POST("laporan/bulanan")
    fun postDataReportMonthly(
        @Header("Authorization") header: String,
        @Body report: JsonObject
    ): Observable<ReportResultDataModel2>

    @GET("laporan")
    fun getDataListReportMonthly(
        @Header("Authorization") header: String,
        @Query("jenis") jenis: String
    ): Observable<ReportResultListDataModel2>

    @GET("laporan/modal")
    fun getDataReportModal(
        @Header("Authorization") header: String
    ): Observable<ReportResultDataCapitalModel>

    @Headers("Content-Type: application/json")
    @POST("laporan/modal")
    fun postDataReportCapital(
        @Header("Authorization") header: String,
        @Body report: JsonObject
    ): Observable<ReportResultDataCapitalModel>

    @GET("laporan/modal/riwayat")
    fun getDataListReportCapital(
        @Header("Authorization") header: String
    ): Observable<ReportResultListCapitalModel>

    @GET("laporan/kas")
    fun getDataReportCashflow(
        @Header("Authorization") header: String
    ): Observable<ReportResultDataCashflow>

    @GET("laporan/kas/riwayat")
    fun getDataListReportCashflow(
        @Header("Authorization") header: String
    ): Observable<ReportResultListCashflow2>

    @Headers("Content-Type: application/json")
    @POST("laporan/kas")
    fun postDataReportCashflow(
        @Header("Authorization") header: String,
        @Body report: JsonObject
    ): Observable<ReportResultDataCashflow2>

    @GET("laporan/neraca")
    fun getDataReportBalance(
        @Header("Authorization") header: String
    ): Observable<ReportResultBalanceModel>

    @Headers("Content-Type: application/json")
    @POST("laporan/neraca")
    fun postDataReportBalance(
        @Header("Authorization") header: String,
        @Body report: JsonObject
    ): Observable<ReportResultBalanceModel>

    @GET("laporan/neraca/riwayat")
    fun getDataListReportBalance(
        @Header("Authorization") header: String
    ): Observable<ReportResultListBalanceModel>


}