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
import com.singpaulee.blacksandapp.model.SupplierResultModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_supplier_add.view.*
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
class SupplierAddFragment : Fragment() {

    val TAG = "SupplierAddFragment"
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_supplier_add, container, false)

        //Set this view in front of cardview
        ViewCompat.setTranslationZ(rootView.saf_iv_logo_menu, 50.0F)

        rootView.saf_btn_add.onClick {
            if (!validation()) {
                return@onClick
            }
            addNewSupplier(
                rootView.saf_edt_name_s.text.toString(),
                rootView.saf_edt_email_s.text.toString(),
                rootView.saf_edt_telp_s.text.toString(),
                rootView.saf_edt_address_s.text.toString(),
                rootView.saf_edt_bank_s.text.toString(),
                rootView.saf_edt_norek_s.text.toString(),
                rootView.saf_edt_anrek_s.text.toString()
            )
        }

        return rootView
    }

    @SuppressLint("CheckResult")
    private fun addNewSupplier(
        name: String,
        email: String,
        telp: String,
        address: String,
        bank: String,
        norek: String,
        anrek: String
    ) {
        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val addNewSupplier: Observable<SupplierResultModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .addNewSupplier(header, name, email, telp, address, bank, norek, anrek)

        addNewSupplier.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: SupplierResultModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    toast("Data pemasok baru telah ditambahkan")
                    rootView.saf_edt_name_s.text.clear()
                    rootView.saf_edt_email_s.text.clear()
                    rootView.saf_edt_telp_s.text.clear()
                    rootView.saf_edt_address_s.text.clear()
                    rootView.saf_edt_bank_s.text.clear()
                    rootView.saf_edt_norek_s.text.clear()
                    rootView.saf_edt_anrek_s.text.clear()
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menambahkan data pemasok")
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
        if (rootView.saf_edt_name_s.text.toString().isBlank()) {
            rootView.saf_edt_name_s.error = "Tidak boleh kosong"
            rootView.saf_edt_name_s.requestFocus()
            return false
        }
        if (rootView.saf_edt_email_s.text.toString().isBlank()) {
            rootView.saf_edt_email_s.error = "Tidak boleh kosong"
            rootView.saf_edt_email_s.requestFocus()
            return false
        }
        if (rootView.saf_edt_telp_s.text.toString().isBlank()) {
            rootView.saf_edt_telp_s.error = "Tidak boleh kosong"
            rootView.saf_edt_telp_s.requestFocus()
            return false
        }
        if (rootView.saf_edt_address_s.text.toString().isBlank()) {
            rootView.saf_edt_address_s.error = "Tidak boleh kosong"
            rootView.saf_edt_address_s.requestFocus()
            return false
        }
        if (rootView.saf_edt_bank_s.text.toString().isBlank()) {
            rootView.saf_edt_bank_s.error = "Tidak boleh kosong"
            rootView.saf_edt_bank_s.requestFocus()
            return false
        }
        if (rootView.saf_edt_norek_s.text.toString().isBlank()) {
            rootView.saf_edt_norek_s.error = "Tidak boleh kosong"
            rootView.saf_edt_norek_s.requestFocus()
            return false
        }
        if (rootView.saf_edt_anrek_s.text.toString().isBlank()) {
            rootView.saf_edt_anrek_s.error = "Tidak boleh kosong"
            rootView.saf_edt_anrek_s.requestFocus()
            return false
        }
        return true
    }


}
