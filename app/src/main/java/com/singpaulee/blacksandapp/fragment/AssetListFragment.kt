package com.singpaulee.blacksandapp.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.singpaulee.blacksandapp.R
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.singpaulee.blacksandapp.adapter.AssetAdapter
import com.singpaulee.blacksandapp.adapter.BuyerAdapter
import com.singpaulee.blacksandapp.model.AssetModel
import com.singpaulee.blacksandapp.model.BuyerModel
import kotlinx.android.synthetic.main.fragment_asset_list.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AssetListFragment : Fragment() {

    lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_asset_list, container, false)

        val adapter = FragmentPagerItemAdapter(
            activity!!.getSupportFragmentManager(), FragmentPagerItems.with(activity)
                .add("Tanah", AssetMenuFragment::class.java)
                .add("Perlengkapan", AssetMenuFragment::class.java)
                .add("Bangunan", AssetMenuFragment::class.java)
                .add("Kendaraan", AssetMenuFragment::class.java)
                .add("Peralatan", AssetMenuFragment::class.java)
                .create()
        )

        rootView.alf_viewpager.adapter = adapter
        rootView.alf_smarttablayout.setViewPager(rootView.alf_viewpager)

        return rootView
    }


}
