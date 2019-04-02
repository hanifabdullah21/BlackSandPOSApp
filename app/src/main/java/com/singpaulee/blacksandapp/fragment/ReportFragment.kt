package com.singpaulee.blacksandapp.fragment


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.report.*
import kotlinx.android.synthetic.main.fragment_report.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ReportFragment : Fragment() {

    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_report, container, false)

        setPermission()

        rootView.rf_journal.onClick {
            var i = Intent(activity, ListJournalActivity::class.java)
            startActivity(i)
        }

        rootView.rf_ll_bulanan.onClick {
            var i = Intent(activity, ReportMonthlyActivity::class.java)
            startActivity(i)
        }

        rootView.rf_ll_list_bulanan.onClick {
            var i = Intent(activity, ListReportMonthlyActivity::class.java)
            startActivity(i)
        }

        rootView.rf_ll_perubahan_modal.onClick {
            var i = Intent(activity, ReportCapitalActivity::class.java)
            startActivity(i)
        }

        rootView.rf_ll_list_perubahan_modal.onClick {
            var i = Intent(activity, ListReportCapitalActivity::class.java)
            startActivity(i)
        }

        rootView.rf_ll_aruskas.onClick {
            var i = Intent(activity, ReportCashFlowActivity::class.java)
            startActivity(i)
        }

        rootView.rf_ll_list_aruskas.onClick {
            var i = Intent(activity, ListReportCashflowActivity::class.java)
            startActivity(i)
        }

        rootView.rf_ll_neraca.onClick {
            var i = Intent(activity, ReportBalanceActivity::class.java)
            startActivity(i)
        }

        rootView.rf_ll_list_neraca.onClick {
            var i = Intent(activity, ListReportBalanceActivity::class.java)
            startActivity(i)
        }


        return rootView
    }

    private val STORAGE_CODE: Int = 100
    private fun setPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, STORAGE_CODE)
            } else {
                createFolder()
            }
        } else {
            createFolder()
        }
    }

    private fun createFolder() {
        val myfolder: String = "" + Environment.getExternalStorageDirectory() + "/Laporan"
        val f = File(myfolder)
        if (!f.exists()) {
            if (!f.mkdir()) {
                toast(myfolder + " Tidak bisa dibuat")
            } else {
                toast(myfolder + " dibuat")
            }
        } else {
            toast(myfolder + " sudah ada")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createFolder()
                } else {
                    toast("Ijin ditolak")
                }
            }
        }
    }
}
