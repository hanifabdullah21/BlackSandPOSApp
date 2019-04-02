package com.singpaulee.blacksandapp.model

import com.google.gson.annotations.SerializedName

data class ReportDataCashFlow2(

    @field:SerializedName("beban_gaji")
    val bebanGaji: Int? = null,

    @field:SerializedName("asset_tanah")
    val assetTanah: Int? = null,

    @field:SerializedName("saldo_awal_bulan")
    val saldoAwalBulan: Int? = null,

    @field:SerializedName("asset_perlengkapan")
    val assetPerlengkapan: Int? = null,

    @field:SerializedName("asset_bangunan")
    val assetBangunan: Int? = null,

    @field:SerializedName("pelunasan_hutang")
    val pelunasanHutang: Int? = null,

    @field:SerializedName("total_operasi")
    val totalOperasi: Int? = null,

    @field:SerializedName("pelunasan_piutang")
    val pelunasanPiutang: Int? = null,

    @field:SerializedName("total_investasi")
    val totalInvestasi: Int? = null,

    @field:SerializedName("total_prive")
    val totalPrive: Int? = null,

    @field:SerializedName("asset_peralatan")
    val assetPeralatan: Int? = null,

    @field:SerializedName("asset_kendaraan")
    val assetKendaraan: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    @field:SerializedName("beban_operasional")
    val bebanOperasional: Int? = null,

    @field:SerializedName("beban_pajak")
    val bebanPajak: Int? = null,

    @field:SerializedName("saldo_akhir_bulan")
    val saldoAkhirBulan: Int? = null,

    @field:SerializedName("beban_angkut")
    val bebanAngkut: Int? = null,

    @field:SerializedName("tanggal_laporan")
    val tanggalLaporan: String? = null,

    @field:SerializedName("kenaikan_saldo")
    val kenaikanSaldo: Int? = null
)

data class ReportResultDataCashflow2(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ReportDataCashFlow2? = null
)

data class ReportResultListCashflow2(
    @field:SerializedName("status")
    val status: MainModel? = null,
    @field:SerializedName("result")
    val result: ArrayList<ReportDataCashFlow2>? = null
)