package com.singpaulee.blacksandapp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton

import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.AssetResultModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_asset.*
import kotlinx.android.synthetic.main.fragment_add_asset.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddAssetFragment : Fragment() {

    val TAG = "AddAssetFragment"
    lateinit var rootView: View

    var category: String? = ""
    var age: Int? = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_asset, container, false)

        rootView.aaf_rg_category_asset.setOnCheckedChangeListener { group, checkedId ->
            var rbChecked: RadioButton = find(checkedId)
            category = rbChecked.text.toString()
            if (category.equals("Tanah") || category.equals("Perlengkapan")) {
                rootView.aaf_ll_age_asset.visibility = View.GONE
                age = 0
            } else {
                rootView.aaf_ll_age_asset.visibility = View.VISIBLE
                age = null
                rootView.aaf_edt_age.setText(age)
            }

        }

        rootView.aaf_btn_add_asset.onClick {
            if (!validasi()) {
                return@onClick
            }
            postNewAsset()
        }

        return rootView
    }

    @SuppressLint("CheckResult")
    private fun postNewAsset() {
        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        var postAsset: Observable<AssetResultModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .postNewAsset(
                header,
                rootView.aaf_edt_name.text.toString(),
                category!!.toLowerCase(),
                rootView.aaf_edt_price.text.toString().toInt(),
                age!!
            )

        postAsset.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: AssetResultModel ->
                if (t.status?.success as Boolean) {
                    toast("Aset berhasil ditambahkan")
                    rootView.aaf_edt_name.setText(null)
                    rootView.aaf_edt_price.setText(null)
                    rootView.aaf_edt_age.setText(null)
//                    rootView.aaf_rg_category_asset.clearCheck()
                    category = ""
                    age = null
                } else {
                    Log.d(TAG, "Gagal menambah aset karena " + t.status.message)
                    toast("Gagal menambahkan aset karena " + t.status.message)
                }
            }, { error ->
                Log.d(TAG, "Failure menambah aset karena " + error.message)
                toast("Sedang ada masalah pada server")
            })
    }

    private fun validasi(): Boolean {
        if (rootView.aaf_edt_name.text.toString().isBlank()) {
            rootView.aaf_edt_name.error = "Tidak boleh kosong"
            rootView.aaf_edt_name.requestFocus()
            return false
        }
        if (category == null || category?.equals("")!!) {
            toast("Harap pilih kategori terlebih dahulu")
            return false
        }
        if (rootView.aaf_edt_price.text.toString().isBlank()) {
            rootView.aaf_edt_price.error = "Tidak boleh kosong"
            rootView.aaf_edt_price.requestFocus()
            return false
        } else {
            try {
                rootView.aaf_edt_price.text.toString().toInt()
            } catch (e: NumberFormatException) {
                rootView.aaf_edt_price.error = "Harus format angka"
                return false
            }
        }
        if (category.equals("Tanah") || category.equals("Perlengkapan")) {
            rootView.aaf_edt_age.setText("" + age)
        } else {
            if (rootView.aaf_edt_age.text.toString().isBlank()) {
                rootView.aaf_edt_age.error = "Tidak boleh kosong"
                rootView.aaf_edt_age.requestFocus()
                return false
            } else {
                try {
                    age = rootView.aaf_edt_age.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    age = null
                    rootView.aaf_edt_age.error = "Harus format angka"
                    return false
                }
            }
        }
        return true
    }

}
