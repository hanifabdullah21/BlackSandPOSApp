package com.singpaulee.blacksandapp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.ProfilResultModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cek_profil.view.*
import org.jetbrains.anko.support.v4.toast
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CekProfilFragment : Fragment() {

    val TAG = "CekProfilFragment"
    lateinit var rootView: View

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_cek_profil, container, false)

        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getProfil: Observable<ProfilResultModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getProfil(header)

        getProfil.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ProfilResultModel ->
                if (result.status?.success as Boolean) {
                    rootView.cpf_tv_name.text = result.result?.name.toString()
                    rootView.cpf_tv_email.text = result.result?.email.toString()
                    rootView.cpf_tv_kas.text = HelperClass().setRupiahBig(result.result?.kas.toString())
                    rootView.cpf_tv_modal.text = HelperClass().setRupiahBig(result.result?.modal.toString())
                } else {
                    Log.d(TAG, "Gagal karena " + result.status.message)
                    toast("Gagal menampilkan profil")
                }
            }, { error ->
                Log.d(TAG, "Failure karena " + error.message)
                toast("Maaf, sedang ada masalah pada server")
            })

        return rootView
    }


}
