package com.singpaulee.blacksandapp.activities.report

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.widget.TableRow
import android.widget.TextView
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.helper.HelperJournalClass
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.JournalModel
import com.singpaulee.blacksandapp.model.JournalResultListModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_journal.*
import kotlinx.android.synthetic.main.activity_list_journal.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class ListJournalActivity : AppCompatActivity() {

    val TAG = "JournalActivity"
    var listJournal: ArrayList<JournalModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_journal)

        listJournal = ArrayList()
        getListJournal()
    }

    @SuppressLint("CheckResult")
    private fun getListJournal() {
        Log.d(TAG, "GET LIST REPORT MONTHLY")
        var prefManager = SharedPrefManager(this@ListJournalActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val report: Observable<JournalResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getListJournalAll(header)

        report.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "get list report as " + it.status?.success + " because " + it.status?.message)
                if (it.status?.success as Boolean) {
                    Log.d(TAG, "result list " + it.result?.toString())
                    listJournal = HelperJournalClass().getMonth(it.result)
                    setupTable()
                } else {
                    toast("Gagal mendapatkan riwayat laporan")
                }
            }, {
                toast("Sedang ada masalah dengan server")
            })
    }

    private fun setupTable() {
        lja_tablelayout.removeViewsInLayout(1, lja_tablelayout.childCount - 1)

        for (i in listJournal!!.indices) {
            var tableRow = TableRow(this)
            var tableRow2 = TableRow(this)
            val layoutParam = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.toFloat()
            )

            val llLayoutParam = TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.toFloat())
            tableRow.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tableRow.layoutParams = layoutParam

            /*======================== BAGIAN I =================================*/

            //Tanggal
            var tv = TextView(this)
            tv.layoutParams = llLayoutParam
            tv.maxLines = 1
            tv.ellipsize = TextUtils.TruncateAt.END
            tv.text = listJournal?.get(i)?.date
            tv.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv.padding = 8
            tableRow.addView(tv)

            //Bulan
            var tv1 = TextView(this)
            tv1.layoutParams = llLayoutParam
            tv1.maxLines = 1
            tv1.ellipsize = TextUtils.TruncateAt.END
            tv1.text = listJournal?.get(i)!!.month.toString()
            tv1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv1.padding = 8
            tableRow.addView(tv1)

            //Keterangan II
            var tv2 = TextView(this)
            tv2.layoutParams = llLayoutParam
            tv2.maxLines = 1
            tv2.ellipsize = TextUtils.TruncateAt.END
            tv2.text = listJournal?.get(i)!!.year.toString()
            tv2.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv2.padding = 8
            tableRow.addView(tv2)

            tableRow.onClick {
                var intent = Intent(this@ListJournalActivity, JournalActivity::class.java)
                intent.putExtra("MONTHYEAR", listJournal?.get(i)!!.date.toString())
                startActivity(intent)
            }

            lja_tablelayout.addView(tableRow)
        }
    }
}
