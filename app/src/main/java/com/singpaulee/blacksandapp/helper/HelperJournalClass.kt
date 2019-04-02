package com.singpaulee.blacksandapp.helper

import com.singpaulee.blacksandapp.model.JournalModel
import java.text.SimpleDateFormat

class HelperJournalClass() {

    fun getMonth(list: ArrayList<JournalModel>?): ArrayList<JournalModel> {
        var listJournalDate : ArrayList<JournalModel> = ArrayList()
        var monthYear: String = ""
        for (i in list!!.indices){
            var date = list.get(i).date
            var month: String = ""
            var sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val dateFirst = sdf.parse(date)
            sdf = SimpleDateFormat("MM")
            var dateFinal = sdf.format(dateFirst)
            when (dateFinal) {
                "01" -> month = "Januari"
                "02" -> month = "Februari"
                "03" -> month = "Maret"
                "04" -> month = "April"
                "05" -> month = "Mei"
                "06" -> month = "Juni"
                "07" -> month = "Juli"
                "08" -> month = "Agustus"
                "09" -> month = "September"
                "10" -> month = "Oktober"
                "11" -> month = "November"
                "12" -> month = "Desember"
                else -> month = dateFinal
            }
            sdf = SimpleDateFormat("yyyy")
            var year = sdf.format(dateFirst)
            monthYear = year+"-"+dateFinal

            if (!listJournalDate.any{x-> x.date == monthYear}){
                var model = JournalModel(date = monthYear, month = month , year = year)
                listJournalDate.add(model)
            }
        }
        return listJournalDate
    }

    fun getInformation01(code: String): String {
        var information: String
        when (code) {
            "1" -> information = "Persediaan"
            "2" -> information = "Piutang"
            "2.1" -> information = "HPP"
            "3" -> information = "B. Angkut Pembelian"
            "4" -> information = "B. Angkut Penjualan"
            "5" -> information = "Hutang"
            "6" -> information = "Kas"
            "7" -> information = "Prive"
            "8" -> information = "Asset"
            "8.1" -> information = "Tanah"
            "8.2" -> information = "Perlengkapan"
            "8.3" -> information = "Bangunan"
            "8.4" -> information = "Kendaraan"
            "8.5" -> information = "Peralatan"
            "9" -> information = "Depresiasi"
            "9.1" -> information = "B. Depresiasi Bangunan"
            "9.2" -> information = "B. Depresiasi Kendaraan"
            "9.3" -> information = "B. Depresiasi Peralatan"
            "10" -> information = "Beban Gaji"
            "11" -> information = "beban Operasional"
            "12" -> information = "Beban Pajak"
            else -> information = "Tidak diketahui"
        }
        return information
    }

    fun getInformation02(code: String): String {
        var information: String
        when (code) {
            "1" -> information = "Hutang"
            "2" -> information = "Penjualan"
            "2.1" -> information = "Persediaan"
            "3" -> information = "Kas"
            "4" -> information = "Kas"
            "5" -> information = "Kas"
            "6" -> information = "Piutang"
            "7" -> information = "Kas"
            "8" -> information = "Asset"
            "8.1" -> information = "Kas"
            "8.2" -> information = "Kas"
            "8.3" -> information = "Kas"
            "8.4" -> information = "Kas"
            "8.5" -> information = "Kas"
            "9" -> information = "Depresiasi"
            "9.1" -> information = "Ak. Depresiasi Bangunan"
            "9.2" -> information = "Ak. Depresiasi Kendaraan"
            "9.3" -> information = "Ak. Depresiasi Peralatan"
            "10" -> information = "Kas"
            "11" -> information = "Kas"
            "12" -> information = "Kas"
            else -> information = "Tidak diketahui"
        }
        return information
    }
}