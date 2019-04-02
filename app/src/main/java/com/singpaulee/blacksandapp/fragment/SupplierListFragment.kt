package com.singpaulee.blacksandapp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.adapter.BuyerAdapter
import com.singpaulee.blacksandapp.adapter.SupplierAdapter
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.BuyerResultListModel
import com.singpaulee.blacksandapp.model.SupplierModel
import com.singpaulee.blacksandapp.model.SupplierResultListModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_buyer_list.view.*
import kotlinx.android.synthetic.main.fragment_supplier_list.view.*
import org.jetbrains.anko.support.v4.toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SupplierListFragment : Fragment() {

    val TAG = "SupplierListFragment"
    lateinit var rootView: View

    var listSupplier: ArrayList<SupplierModel>? = null
    var supplierAdapter: SupplierAdapter? = null
    var supplierLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_supplier_list, container, false)

        listSupplier = ArrayList()
        supplierLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        supplierAdapter = SupplierAdapter(activity!!.applicationContext, listSupplier)

        rootView.slf_rv_list_supplier.layoutManager = supplierLayoutManager
        rootView.slf_rv_list_supplier.adapter = supplierAdapter

        getListOfSupplier()

        return rootView
    }

    @SuppressLint("CheckResult")
    private fun getListOfSupplier() {
        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListSupplier: Observable<SupplierResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getListSupplier(header)

        getListSupplier.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: SupplierResultListModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    listSupplier = t?.result
                    supplierAdapter = SupplierAdapter(activity!!.applicationContext, listSupplier)
                    supplierAdapter?.notifyDataSetChanged()
                    rootView.slf_rv_list_supplier.adapter = supplierAdapter
                    toast("" + listSupplier?.size)
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menampilkan daftar pemasok")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }


}
