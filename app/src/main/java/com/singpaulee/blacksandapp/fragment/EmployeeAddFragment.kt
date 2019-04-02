package com.singpaulee.blacksandapp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.BuyerResultModel
import com.singpaulee.blacksandapp.model.EmployeeResultModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_buyer_add.view.*
import kotlinx.android.synthetic.main.fragment_employee_add.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EmployeeAddFragment : Fragment() {

    val TAG = "EmployeeAddFragment"
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_employee_add, container, false)

//Set this view in front of cardview
        ViewCompat.setTranslationZ(rootView.eaf_iv_logo_menu, 50.0F)

        rootView.eaf_btn_add.onClick {
            if (!validation()) {
                return@onClick
            }
            addNewEmployee(
                rootView.eaf_edt_name.text.toString(),
                rootView.eaf_edt_email.text.toString(),
                rootView.eaf_edt_telp.text.toString(),
                rootView.eaf_edt_address.text.toString(),
                rootView.eaf_edt_salary.text.toString().toInt()
            )
        }


        return rootView
    }

    @SuppressLint("CheckResult")
    private fun addNewEmployee(nama: String, email: String, telp: String, address: String, salary: Int) {
        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val addNewEmployee: Observable<EmployeeResultModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .addNewEmployee(header, nama, email, telp, address, salary)

        addNewEmployee.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: EmployeeResultModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    toast("Data karyawan baru telah ditambahkan")
                    rootView.eaf_edt_name.text.clear()
                    rootView.eaf_edt_email.text.clear()
                    rootView.eaf_edt_telp.text.clear()
                    rootView.eaf_edt_address.text.clear()
                    rootView.eaf_edt_salary.text.clear()
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menambahkan data karyawan")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }


    /*This function is used to validate the data entered by the user,
   * whether it is still empty or not according to the conditions
   *
   * */
    private fun validation(): Boolean {
        if (rootView.eaf_edt_name.text.toString().isBlank()) {
            rootView.eaf_edt_name.error = "Tidak boleh kosong"
            rootView.eaf_edt_name.requestFocus()
            return false
        }
        if (rootView.eaf_edt_email.text.toString().isBlank()) {
            rootView.eaf_edt_email.error = "Tidak boleh kosong"
            rootView.eaf_edt_email.requestFocus()
            return false
        }
        if (rootView.eaf_edt_telp.text.toString().isBlank()) {
            rootView.eaf_edt_telp.error = "Tidak boleh kosong"
            rootView.eaf_edt_telp.requestFocus()
            return false
        }
        if (rootView.eaf_edt_address.text.toString().isBlank()) {
            rootView.eaf_edt_address.error = "Tidak boleh kosong"
            rootView.eaf_edt_address.requestFocus()
            return false
        }
        if (rootView.eaf_edt_salary.text.toString().isBlank()) {
            rootView.eaf_edt_salary.error = "Tidak boleh kosong"
            rootView.eaf_edt_salary.requestFocus()
            return false
        }
        return true
    }
}
