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
import com.singpaulee.blacksandapp.adapter.ItemAdapter
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.ItemModel
import com.singpaulee.blacksandapp.model.ItemResultListModel
import com.singpaulee.blacksandapp.model.ItemResultModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_item_add.view.*
import kotlinx.android.synthetic.main.fragment_item_list.view.*
import org.jetbrains.anko.support.v4.toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ItemListFragment : Fragment() {

    val TAG = "ItemListFragment"
    lateinit var rootView: View

    var listItem: ArrayList<ItemModel>? = null
    var listItemAdapter: ItemAdapter? = null
    var listItemLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_item_list, container, false)

        listItem = ArrayList()
        listItemLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        listItemAdapter = ItemAdapter(activity?.applicationContext!!, listItem)

        rootView.ilf_rv_list_item.layoutManager = listItemLayoutManager
        rootView.ilf_rv_list_item.adapter = listItemAdapter

        getListOfItem()

        return rootView
    }

    @SuppressLint("CheckResult")
    private fun getListOfItem() {
        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListItem: Observable<ItemResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getListItem(header)

        getListItem.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: ItemResultListModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    listItem = t?.result
                    listItemAdapter = ItemAdapter(activity!!.applicationContext, listItem)
                    listItemAdapter?.notifyDataSetChanged()
                    rootView.ilf_rv_list_item.adapter = listItemAdapter
                    toast(""+listItem?.size)
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menampilkan barang")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }


}
