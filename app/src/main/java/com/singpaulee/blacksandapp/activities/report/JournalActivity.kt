package com.singpaulee.blacksandapp.activities.report

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
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
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.padding
import org.jetbrains.anko.toast
import java.util.*

class JournalActivity : AppCompatActivity() {

    val TAG = "JournalActivity"
    var listJournal: ArrayList<JournalModel>? = null

    var montYear: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        montYear = intent.getStringExtra("MONTHYEAR")

        listJournal = ArrayList()
        getListJournal()
    }

    @SuppressLint("CheckResult")
    private fun getListJournal() {
        Log.d(TAG, "GET LIST REPORT MONTHLY")
        var prefManager = SharedPrefManager(this@JournalActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val report: Observable<JournalResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getListJournalByDate(header, montYear.toString())

        report.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "get list report as " + it.status?.success + " because " + it.status?.message)
                if (it.status?.success as Boolean) {
                    Log.d(TAG, "result list " + it.result?.toString())
                    listJournal = it.result
                    listJournal?.reverse()
                    setupTable()
                } else {
                    toast("Gagal mendapatkan riwayat laporan")
                }
            }, {
                toast("Sedang ada masalah dengan server")
            })
    }

    private fun setupTable() {
        ja_tablelayout.removeViewsInLayout(1, ja_tablelayout.childCount - 1)

        for (i in listJournal!!.indices) {
            var tableRow = TableRow(this)
            var tableRow2 = TableRow(this)
            var tableRow3 = TableRow(this)
            val layoutParam = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.toFloat()
            )

            val llLayoutParam = TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.toFloat())
            tableRow.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tableRow.layoutParams = layoutParam
            tableRow2.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tableRow2.layoutParams = layoutParam
            tableRow3.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tableRow3.layoutParams = layoutParam

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

            //Keterangan I
            var tv1 = TextView(this)
            tv1.layoutParams = llLayoutParam
            tv1.maxLines = 1
            tv1.ellipsize = TextUtils.TruncateAt.END
            tv1.text = HelperJournalClass().getInformation01(listJournal?.get(i)!!.code.toString())
            tv1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv1.padding = 8
            tableRow.addView(tv1)

            //Keterangan II
            var tv2 = TextView(this)
            tv2.layoutParams = llLayoutParam
            tv2.maxLines = 1
            tv2.ellipsize = TextUtils.TruncateAt.END
            tv2.text = ""
            tv2.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv2.padding = 8
            tableRow.addView(tv2)

            //Debit
            var tv3 = TextView(this)
            tv3.layoutParams = llLayoutParam
            tv3.maxLines = 1
            tv3.ellipsize = TextUtils.TruncateAt.END
            tv3.text = HelperClass().setRupiahBig(listJournal?.get(i)!!.value.toString())
            tv3.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv3.padding = 8
            tableRow.addView(tv3)

            //Kredit
            var tv4 = TextView(this)
            tv4.layoutParams = llLayoutParam
            tv4.maxLines = 1
            tv4.ellipsize = TextUtils.TruncateAt.END
            tv4.text = ""
            tv4.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv4.padding = 8
            tableRow.addView(tv4)

            ja_tablelayout.addView(tableRow)

            /*======================== BAGIAN II =================================*/

            //Tanggal
            var tv_ = TextView(this)
            tv_.layoutParams = llLayoutParam
            tv_.maxLines = 1
            tv_.ellipsize = TextUtils.TruncateAt.END
            tv_.text = ""
            tv_.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv_.padding = 8
            tableRow2.addView(tv_)

            //Keterangan I
            var tv_1 = TextView(this)
            tv_1.layoutParams = llLayoutParam
            tv_1.maxLines = 1
            tv_1.ellipsize = TextUtils.TruncateAt.END
            tv_1.text = ""
            tv_1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv_1.padding = 8
            tableRow2.addView(tv_1)

            //Keterangan II
            var tv_2 = TextView(this)
            tv_2.layoutParams = llLayoutParam
            tv_2.maxLines = 1
            tv_2.ellipsize = TextUtils.TruncateAt.END
            tv_2.text = HelperJournalClass().getInformation02(listJournal?.get(i)!!.code.toString())
            tv_2.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv_2.padding = 8
            tableRow2.addView(tv_2)

            //Debit
            var tv_3 = TextView(this)
            tv_3.layoutParams = llLayoutParam
            tv_3.maxLines = 1
            tv_3.ellipsize = TextUtils.TruncateAt.END
            tv_3.text = ""
            tv_3.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv_3.padding = 8
            tableRow2.addView(tv_3)

            //Kredit
            var tv_4 = TextView(this)
            tv_4.layoutParams = llLayoutParam
            tv_4.maxLines = 1
            tv_4.ellipsize = TextUtils.TruncateAt.END
            tv_4.text = HelperClass().setRupiahBig(listJournal?.get(i)!!.value.toString())
            tv_4.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table)
            tv_4.padding = 8
            tableRow2.addView(tv_4)

            ja_tablelayout.addView(tableRow2)

            /*======================== BAGIAN III =================================*/

            if (i != listJournal?.lastIndex) {
                //Tanggal
                var tv__ = TextView(this)
                tv__.layoutParams = llLayoutParam
                tv__.maxLines = 1
                tv__.ellipsize = TextUtils.TruncateAt.END
                tv__.text = ""
                tv__.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table_color)
                tv__.padding = 8
                tableRow3.addView(tv__)

                //Keterangan I
                var tv__1 = TextView(this)
                tv__1.layoutParams = llLayoutParam
                tv__1.maxLines = 1
                tv__1.ellipsize = TextUtils.TruncateAt.END
                tv__1.text = ""
                tv__1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table_color)
                tv__1.padding = 8
                tableRow3.addView(tv__1)

                //Keterangan II
                var tv__2 = TextView(this)
                tv__2.layoutParams = llLayoutParam
                tv__2.maxLines = 1
                tv__2.ellipsize = TextUtils.TruncateAt.END
                tv__2.text = ""
                tv__2.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table_color)
                tv__2.padding = 8
                tableRow3.addView(tv__2)

                //Debit
                var tv__3 = TextView(this)
                tv__3.layoutParams = llLayoutParam
                tv__3.maxLines = 1
                tv__3.ellipsize = TextUtils.TruncateAt.END
                tv__3.text = ""
                tv__3.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table_color)
                tv__3.padding = 8
                tableRow3.addView(tv__3)

                //Kredit
                var tv__4 = TextView(this)
                tv__4.layoutParams = llLayoutParam
                tv__4.maxLines = 1
                tv__4.ellipsize = TextUtils.TruncateAt.END
                tv__4.text = ""
                tv__4.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.border_table_color)
                tv__4.padding = 8
                tableRow3.addView(tv__4)

                ja_tablelayout.addView(tableRow3)
            }
        }
    }


}
