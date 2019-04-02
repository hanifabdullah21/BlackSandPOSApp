package com.singpaulee.blacksandapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.model.AssetModel
import kotlinx.android.synthetic.main.item_asset.view.*

class AssetAdapter(val context: Context, val list: ArrayList<AssetModel>?) :
    RecyclerView.Adapter<AssetAdapter.ViewHolder>() {

    lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_asset, parent, false)
        return AssetAdapter.ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.get(position)?.let { holder.bind(it) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(assetModel: AssetModel) {
            Log.d("AssetAdapter", assetModel.toString())

            itemView.ia_tv_id.text = "ID  :  " + assetModel.id
            itemView.ia_tv_nama.text = ": " + assetModel.nama
            itemView.ia_tv_date_of_purchase.text = ": " + HelperClass().convertDateYMDhms(assetModel.tanggal.toString())
            assetModel.masaBerakhir?.let {
                itemView.ia_tv_date_of_expired.text = ": " +
                        HelperClass().convertDateYMDhms(assetModel.masaBerakhir.toString())
            }.run {
                itemView.ia_tv_date_of_expired.text = ": - "
            }
            itemView.ia_tv_age.text = ": " + assetModel.umurTahun + " bulan"
            itemView.ia_tv_price_of_purchase.text = ": " + HelperClass().setRupiah(assetModel.hargaBeli.toString())
            itemView.ia_tv_depreciation_value.text = ": " +
                    HelperClass().setRupiah(assetModel.nilaiPenyusutan.toString())
            itemView.ia_tv_present_value.text = ": " + HelperClass().setRupiah(assetModel.nilaiSekarang.toString())
        }

    }
}