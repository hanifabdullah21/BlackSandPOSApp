package com.singpaulee.blacksandapp.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.transaction.TransactionAddBuyActivity
import com.singpaulee.blacksandapp.activities.transaction.TransactionAddSellActivity
import com.singpaulee.blacksandapp.activities.transaction.TransactionListBuyActivity
import kotlinx.android.synthetic.main.fragment_transaction_buy.view.*
import kotlinx.android.synthetic.main.fragment_transaction_sell.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TransactionSellFragment : Fragment() {

    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_transaction_sell, container, false)

        rootView.tsf_btn_add.onClick {
            var i = Intent(activity, TransactionAddSellActivity::class.java)
            startActivity(i)
        }

        rootView.tsf_btn_list.onClick {
            var i = Intent(activity, TransactionListBuyActivity::class.java)
            i.putExtra("type","penjualan")
            startActivity(i)
        }

        return rootView
    }


}
