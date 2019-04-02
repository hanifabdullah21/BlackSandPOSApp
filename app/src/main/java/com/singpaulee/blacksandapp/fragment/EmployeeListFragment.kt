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
import com.singpaulee.blacksandapp.adapter.EmployeeAdapter
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.BuyerModel
import com.singpaulee.blacksandapp.model.BuyerResultListModel
import com.singpaulee.blacksandapp.model.EmployeeModel
import com.singpaulee.blacksandapp.model.EmployeeResultListModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_buyer_list.view.*
import kotlinx.android.synthetic.main.fragment_employee_list.view.*
import org.jetbrains.anko.support.v4.toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EmployeeListFragment : Fragment() {

    val TAG = "EmployeeListFragment"
    lateinit var rootView: View

    var listEmployee: ArrayList<EmployeeModel>? = null
    var employeeAdapter: EmployeeAdapter? = null
    var employeeLayoutManager: RecyclerView.LayoutManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_employee_list, container, false)

        listEmployee = ArrayList()
        employeeLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        employeeAdapter = EmployeeAdapter(activity!!.applicationContext, listEmployee)

        rootView.elf_rv_list_employee.layoutManager = employeeLayoutManager
        rootView.elf_rv_list_employee.adapter = employeeAdapter

        getListOfEmployee()

        return rootView
    }

    @SuppressLint("CheckResult")
    private fun getListOfEmployee() {
        var prefManager = SharedPrefManager(activity?.applicationContext!!)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListEmployee: Observable<EmployeeResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getListEmployee(header)

        getListEmployee.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: EmployeeResultListModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    listEmployee = t?.result
                    employeeAdapter = EmployeeAdapter(activity!!.applicationContext, listEmployee)
                    employeeAdapter?.notifyDataSetChanged()
                    rootView.elf_rv_list_employee.adapter = employeeAdapter
                    toast("" + listEmployee?.size)
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menampilkan daftar karyawan")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }
}
