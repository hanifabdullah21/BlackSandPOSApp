package com.singpaulee.blacksandapp.activities.buyer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.HelperClass
import com.singpaulee.blacksandapp.model.BuyerModel
import com.singpaulee.blacksandapp.model.EmployeeModel
import kotlinx.android.synthetic.main.activity_buyer_detail.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class BuyerDetailActivity : AppCompatActivity() {

    var buyerModel: BuyerModel? = null
    var employeeModel: EmployeeModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_detail)

        buyerModel = intent.getParcelableExtra("BUYER")
        employeeModel = intent.getParcelableExtra("EMPLOYEE")
        if (buyerModel != null) {
            bda_tv_title_salary.visibility = View.GONE
            bda_tv_salary.visibility = View.GONE
            bda_tv_name.text = ": " + buyerModel?.nama.toString()
            bda_tv_address.text = ": " + buyerModel?.alamat
            bda_tv_telp.text = ": " + buyerModel?.telepon
            bda_tv_email.text = ": " + buyerModel?.email
        } else if (employeeModel != null) {
            bda_tv_title_salary.visibility = View.VISIBLE
            bda_tv_salary.visibility = View.VISIBLE
            bda_tv_name.text = ": " + employeeModel?.nama.toString()
            bda_tv_address.text = ": " + employeeModel?.alamat
            bda_tv_telp.text = ": " + employeeModel?.telepon
            bda_tv_email.text = ": " + employeeModel?.email
            bda_tv_salary.visibility = View.VISIBLE
            bda_tv_salary.text = ": " + HelperClass().setRupiah(employeeModel?.gaji.toString())
        }

        bda_btn_add.onClick {
            toast("Maaf, fitur ini masih dalam pengembangan")
        }

        bda_btn_update.onClick {
            toast("Maaf, fitur ini masih dalam pengembangan")
        }
    }
}
