package com.singpaulee.blacksandapp.activities.supplier

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.model.SupplierModel
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_supplier_detail.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class SupplierDetailActivity : AppCompatActivity() {

    var supplierModel: SupplierModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_detail)

        supplierModel = intent.getParcelableExtra("SUPPLIER")
        if (supplierModel!=null){
            sda_tv_name.text = ": "+supplierModel?.nama
            sda_tv_address.text = ": "+supplierModel?.alamat
            sda_tv_telp.text = ": "+supplierModel?.telepon
            sda_tv_email.text = ": "+supplierModel?.email
            sda_tv_bank.text = ": "+supplierModel?.bank
            sda_tv_norek.text = ": "+supplierModel?.norek
            sda_tv_anrek.text = ": "+supplierModel?.anrek
        }

        sda_btn_add.onClick {
            toast("Maaf, fitur ini masih dalam pengembangan")
        }

        sda_btn_update.onClick {
            toast("Maaf, fitur ini masih dalam pengembangan")
        }
    }
}
