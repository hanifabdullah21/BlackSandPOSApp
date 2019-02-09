package com.singpaulee.blacksandapp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.MainActivity
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.ItemResultModel
import com.singpaulee.blacksandapp.model.LoginModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_item_add.*
import kotlinx.android.synthetic.main.fragment_item_add.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ItemAddFragment : Fragment() {

    private val TAG = "ItemAddFragment"
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_item_add, container, false)

        //Set this view in front of cardview
        ViewCompat.setTranslationZ(rootView.iaf_iv_logo_menu, 50.0F)

        rootView.iaf_btn_add.onClick {
            if (!validation()) {
                return@onClick
            }
            addNewItem(iaf_edt_code.text.toString(), iaf_edt_name.text.toString(), iaf_edt_stock.text.toString())
        }

        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        Log.d(TAG, "token "+token)

        return rootView
    }

    /* This function is used to request add new item to databases
    *
    * */
    @SuppressLint("CheckResult")
    private fun addNewItem(code:String, name: String, stock: String) {
        rootView.iaf_btn_add.isEnabled = false

        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer "+token

        val addNewItem: Observable<ItemResultModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .addNewItem(header,code, name, stock.toInt())

        addNewItem.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: ItemResultModel? ->
                rootView.iaf_btn_add.isEnabled = true
                if (t?.status?.success?.equals(true)!!){
                    toast("Barang baru telah ditambahkan")
                    rootView.iaf_edt_name.text.clear()
                    rootView.iaf_edt_stock.text.clear()
                }else{
                    Log.d(TAG, "FAILED "+t?.status?.message)
                    toast("Gagal menambahkan barang")
                }
            }, { error ->
                rootView.iaf_btn_add.isEnabled = true
                Log.d(TAG, "ERROR "+error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }

    /*This function is used to validate the data entered by the user,
    * whether it is still empty or not according to the conditions
    *
    * */
    private fun validation(): Boolean {
        if (rootView.iaf_edt_code.text.toString().isBlank()) {
            rootView.iaf_edt_code.error = "Tidak boleh kosong"
            rootView.iaf_edt_code.requestFocus()
            return false
        }
        if (rootView.iaf_edt_name.text.toString().isBlank()) {
            rootView.iaf_edt_name.error = "Tidak boleh kosong"
            rootView.iaf_edt_name.requestFocus()
            return false
        }
        if (rootView.iaf_edt_stock.text.toString().isBlank()) {
            rootView.iaf_edt_stock.error = "Tidak boleh kosong"
            rootView.iaf_edt_stock.requestFocus()
            return false
        }
        return true
    }


}
