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
import com.singpaulee.blacksandapp.model.ItemResultModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_buyer_add.view.*
import kotlinx.android.synthetic.main.fragment_item_add.view.*
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
class BuyerAddFragment : Fragment() {

    val TAG = "BuyerAddFragment"
    lateinit var rootView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_buyer_add, container, false)

        //Set this view in front of cardview
        ViewCompat.setTranslationZ(rootView.baf_iv_logo_menu, 50.0F)

        rootView.baf_btn_add.onClick {
            if (!validation()){
                return@onClick
            }
            addNewBuyer(
                rootView.baf_edt_name_b.text.toString(),
                rootView.baf_edt_email_b.text.toString(),
                rootView.baf_edt_telp_b.text.toString(),
                rootView.baf_edt_address_b.text.toString(),
                rootView.baf_edt_bank_b.text.toString(),
                rootView.baf_edt_norek_b.text.toString(),
                rootView.baf_edt_anrek_b.text.toString()
            )
        }

        return rootView
    }

    @SuppressLint("CheckResult")
    private fun addNewBuyer(nama: String, email: String, telp: String, address: String, bank: String, norek: String, anrek: String) {
        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer "+token

        val addNewBuyer: Observable<BuyerResultModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .addNewBuyer(header ,nama, email, telp, address)

        addNewBuyer.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: BuyerResultModel? ->
                if (t?.status?.success?.equals(true)!!){
                    toast("Data pelanggan baru telah ditambahkan")
                    rootView.baf_edt_name_b.text.clear()
                    rootView.baf_edt_email_b.text.clear()
                    rootView.baf_edt_telp_b.text.clear()
                    rootView.baf_edt_address_b.text.clear()
                }else{
                    Log.d(TAG, "FAILED "+t?.status?.message)
                    toast("Gagal menambahkan data pelanggan")
                }
            }, { error ->
                Log.d(TAG, "ERROR "+error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }

    /*This function is used to validate the data entered by the user,
    * whether it is still empty or not according to the conditions
    *
    * */
    private fun validation(): Boolean {
        if (rootView.baf_edt_name_b.text.toString().isBlank()){
            rootView.baf_edt_name_b.error = "Tidak boleh kosong"
            rootView.baf_edt_name_b.requestFocus()
            return false
        }
        if (rootView.baf_edt_email_b.text.toString().isBlank()){
            rootView.baf_edt_email_b.error = "Tidak boleh kosong"
            rootView.baf_edt_email_b.requestFocus()
            return false
        }
        if (rootView.baf_edt_telp_b.text.toString().isBlank()){
            rootView.baf_edt_telp_b.error = "Tidak boleh kosong"
            rootView.baf_edt_telp_b.requestFocus()
            return false
        }
        if (rootView.baf_edt_address_b.text.toString().isBlank()){
            rootView.baf_edt_address_b.error = "Tidak boleh kosong"
            rootView.baf_edt_address_b.requestFocus()
            return false
        }
//        if (rootView.baf_edt_bank_b.text.toString().isBlank()){
//            rootView.baf_edt_bank_b.error = "Tidak boleh kosong"
//            rootView.baf_edt_bank_b.requestFocus()
//            return false
//        }
//        if (rootView.baf_edt_norek_b.text.toString().isBlank()){
//            rootView.baf_edt_norek_b.error = "Tidak boleh kosong"
//            rootView.baf_edt_norek_b.requestFocus()
//            return false
//        }
//        if (rootView.baf_edt_anrek_b.text.toString().isBlank()){
//            rootView.baf_edt_anrek_b.error = "Tidak boleh kosong"
//            rootView.baf_edt_anrek_b.requestFocus()
//            return false
//        }
        return true
    }


}
