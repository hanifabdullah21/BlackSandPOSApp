package com.singpaulee.blacksandapp.activities.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.LoginModel
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSpannableTextView()

        al_btn_login.onClick {
            if (!validation()){
                return@onClick
            }
            login(al_medt_email.text.toString(), al_medt_password.text.toString())
        }

        //check if user logged in
        var prefManager = SharedPrefManager(this@LoginActivity)
        if (prefManager.getIsLogin()){
            startActivity(intentFor<MainActivity>())
            finish()
        }
    }

    /*This function request for login application
    * @param email : email that has been entered by user
    * @param password : password that has been entered by user
    *
    * */
    @SuppressLint("CheckResult")
    private fun login(email: String, password: String) {
        val loginUser: Observable<LoginModel?> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .login(email, password)

        loginUser.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: LoginModel? ->
                if (t?.status?.success?.equals(true)!!){
                    //if login success, save state in sharedpreference
                    var prefManager = SharedPrefManager(this@LoginActivity)
                    prefManager.savePrefBoolean(true)
                    prefManager.savePrefString(email,prefManager.KEY_EMAIL)
                    prefManager.savePrefString(t?.result?.token.toString(),prefManager.KEY_TOKEN)

                    //if login success go to MainActivity.class
                    startActivity(intentFor<MainActivity>())
                    finish()
                }else{
                    al_medt_email.text?.clear()
                    al_medt_password.text?.clear()
                    toast("Email/Password anda salah. Silahkan coba kembali.")
                }
            }, { error ->
                Log.d("LOGIN", error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }

    /* function of input validation
    * Check when email or password is empty
    *
    * */
    private fun validation(): Boolean {
        if (al_medt_email.text.toString().isBlank()){
            al_medt_email.error = "Tidak Boleh Kosong"
            al_medt_email.requestFocus()
            return false
        }
        if (al_medt_password.text.toString().isBlank()){
            al_medt_password.error = "Tidak Boleh Kosong"
            al_medt_password.requestFocus()
            return false
        }
        return true
    }

    /* Set Spannable Textview
    * ## Part of Textview can be clicked
    *
    * */
    private fun setSpannableTextView() {
        val string = "Belum punya akun? DAFTAR"
        val span = SpannableString(string)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                startActivity(intentFor<RegisterActivity>())
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        span.setSpan(clickableSpan, 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        try {
            al_tv_register.setText(span)
            al_tv_register.movementMethod = LinkMovementMethod.getInstance()
            al_tv_register.highlightColor = Color.TRANSPARENT
        } catch (e: Exception) {
            toast("Maaf Terjadi kesalahan")
            Log.d("PF", e.toString())
        }
    }
}
