package com.singpaulee.blacksandapp.activities.main

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.model.RegisterModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class RegisterActivity : AppCompatActivity() {

    val REFERAL = "BLACKSAND"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        reg_btn_register.onClick {
            if (!validation()){
                return@onClick
            }
            register(reg_medt_name.text.toString(), reg_medt_email.text.toString(), reg_medt_password.text.toString())
        }
    }

    /* This function is request to server about registration
    * */
    @SuppressLint("CheckResult")
    private fun register(name: String, email: String, password: String) {
        val register: Observable<RegisterModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .register(name, email, password)

        register.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: RegisterModel? ->
                if (t?.status?.success?.equals(true)!!){
                    toast("Akun telah berhasil dibuat")
                    finish()
                }else{
                    toast("Gagal melakukan pendaftaran")
                }
            }, { error ->
                Log.d("LOGIN", error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }

    /* This function is validation when edittext is empty or wrong
    * */
    private fun validation(): Boolean {
        if (reg_medt_name.text.toString().isBlank()){
            reg_medt_name.error = "Tidak boleh kosong"
            reg_medt_name.requestFocus()
            return false
        }
        if (reg_medt_password.text.toString().isBlank()){
            reg_medt_password.error = "Tidak boleh kosong"
            reg_medt_password.requestFocus()
            return false
        }
        if (reg_medt_password.text.toString().length<6 || reg_medt_password.text.toString().length>30){
            reg_medt_password.error = "Password minimal 6 karakter dan maksimal 30 karakter"
            reg_medt_password.requestFocus()
            return false
        }
        if (reg_medt_confirm_password.text.toString().isBlank()){
            reg_medt_confirm_password.error = "Tidak boleh kosong"
            reg_medt_confirm_password.requestFocus()
            return false
        }
        if (!reg_medt_confirm_password.text.toString().equals(reg_medt_password.text.toString())){
            reg_medt_confirm_password.error = "Konfirmasi password tidak sesuai"
            reg_medt_confirm_password.requestFocus()
            return false
        }
        if (reg_medt_referal.text.toString().isBlank()){
            reg_medt_referal.error = "Tidak boleh kosong"
            reg_medt_referal.requestFocus()
            return false
        }
        if (!reg_medt_referal.text.toString().equals(REFERAL)){
            reg_medt_referal.error = "Referal tidak berlaku"
            reg_medt_referal.requestFocus()
            return false
        }
        return true
    }
}
