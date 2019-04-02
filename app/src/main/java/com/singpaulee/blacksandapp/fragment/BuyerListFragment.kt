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
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.BuyerModel
import com.singpaulee.blacksandapp.model.BuyerResultListModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
class BuyerListFragment : Fragment() {

    val TAG = "BuyerListFragment"
    lateinit var rootView: View

    var listBuyer: ArrayList<BuyerModel>? = null
    var buyerAdapter: BuyerAdapter? = null
    var buyerLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_buyer_list, container, false)

        listBuyer = ArrayList()
        buyerLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        buyerAdapter = BuyerAdapter(activity!!.applicationContext, listBuyer)

        rootView.blf_rv_list_buyer.layoutManager = buyerLayoutManager
        rootView.blf_rv_list_buyer.adapter = buyerAdapter

        getListOfBuyer()

        return rootView
    }

    @SuppressLint("CheckResult")
    private fun getListOfBuyer() {
        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListBuyer: Observable<BuyerResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getListBuyer(header)

        getListBuyer.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: BuyerResultListModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    listBuyer = t?.result
                    buyerAdapter = BuyerAdapter(activity!!.applicationContext, listBuyer)
                    buyerAdapter?.notifyDataSetChanged()
                    rootView.blf_rv_list_buyer.adapter = buyerAdapter
                    toast("" + listBuyer?.size)
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menampilkan daftar pelanggan")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }
}
