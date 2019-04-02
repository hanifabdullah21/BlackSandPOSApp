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
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.adapter.AssetAdapter
import com.singpaulee.blacksandapp.adapter.BuyerAdapter
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.AssetModel
import com.singpaulee.blacksandapp.model.AssetResultListModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_asset_menu.view.*
import kotlinx.android.synthetic.main.fragment_buyer_list.view.*
import org.jetbrains.anko.support.v4.toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AssetMenuFragment : Fragment() {

    lateinit var rootView: View
    val TAG = "AssetMenuFragment"

    var category: String? = null

    var listAsset: ArrayList<AssetModel>? = null
    var assetAdapter: AssetAdapter? = null
    var assetLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_asset_menu, container, false)

        var position: Int = FragmentPagerItem.getPosition(arguments)

        when (position) {
            0 -> category = "tanah"
            1 -> category = "perlengkapan"
            2 -> category = "bangunan"
            3 -> category = "kendaraan"
            4 -> category = "peralatan"
            else -> ""
        }

        listAsset = ArrayList()
        assetLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        assetAdapter = AssetAdapter(activity!!.applicationContext, listAsset)

        rootView.amf_recyclerview.layoutManager = assetLayoutManager
        rootView.amf_recyclerview.adapter = assetAdapter

        getListAsset()

        return rootView
    }

    @SuppressLint("CheckResult")
    private fun getListAsset() {
        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListAsset: Observable<AssetResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getAsset(header, category!!)

        getListAsset.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: AssetResultListModel ->
                if (t.status?.success as Boolean) {
                    listAsset = t?.result
                    assetAdapter = AssetAdapter(activity!!.applicationContext, listAsset)
                    assetAdapter?.notifyDataSetChanged()
                    rootView.amf_recyclerview.adapter = assetAdapter
                } else {
                    Log.d(TAG, "Gagal karena " + t.status.message)
                    toast("Gagal mendapatkan daftar aset")
                }
            }, { error ->
                Log.d(TAG, "Failure karena "+error.message)
                toast("Sedang ada masalah pada server")
            })
    }


}
