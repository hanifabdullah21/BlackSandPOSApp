package com.singpaulee.blacksandapp.activities.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.fragment.*
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var layoutID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        var prefManager = SharedPrefManager(this@MainActivity)
        var email = prefManager.getEmail()

        var tvEmail = nav_view.getHeaderView(0).findViewById<TextView>(R.id.nhm_tv_email)
        tvEmail.text = email
        var tvNama = nav_view.getHeaderView(0).findViewById<TextView>(R.id.nhm_tv_name)
        tvNama.text = "Selamat Datang"


        supportFragmentManager.beginTransaction()
            .replace(R.id.cm_framelayout, CekProfilFragment())
            .commit()
        layoutID = R.id.nav_check_profil
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (layoutID != R.id.nav_check_profil) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, CekProfilFragment())
                    .commit()
                layoutID = R.id.nav_check_profil
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            /* ------------------------ P R O F I L ---------------------------*/
            R.id.nav_check_profil -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, CekProfilFragment())
                    .commit()
                layoutID = R.id.nav_check_profil
            }
            R.id.nav_prive -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, PriveFragment())
                    .commit()
                layoutID = R.id.nav_prive
            }
            /* ------------------------ E M P L O Y E E ---------------------------*/
            R.id.nav_employee_add -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, EmployeeAddFragment())
                    .commit()
                layoutID = R.id.nav_employee_add
            }
            R.id.nav_employee_list -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, EmployeeListFragment())
                    .commit()
                layoutID = R.id.nav_employee_list
            }
            /* ------------------------ I T E M ---------------------------*/
            R.id.nav_item_add -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, ItemAddFragment())
                    .commit()
                layoutID = R.id.nav_item_add
            }
            R.id.nav_item_list -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, ItemListFragment())
                    .commit()
                layoutID = R.id.nav_item_list
            }
            /* ------------------------ S U P P L I E R ---------------------------*/
            R.id.nav_supplier_add -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, SupplierAddFragment())
                    .commit()
                layoutID = R.id.nav_supplier_add
            }
            R.id.nav_supplier_list -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, SupplierListFragment())
                    .commit()
                layoutID = R.id.nav_supplier_list
            }
            /* ------------------------ B U Y E R ---------------------------*/
            R.id.nav_buyer_add -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, BuyerAddFragment())
                    .commit()
                layoutID = R.id.nav_buyer_add
            }
            R.id.nav_buyer_list -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, BuyerListFragment())
                    .commit()
                layoutID = R.id.nav_buyer_list
            }
            /* ------------------------ T R A N S A C T I O N ---------------------------*/
            R.id.nav_transaction_buy -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, TransactionBuyFragment())
                    .commit()
                layoutID = R.id.nav_transaction_buy
            }
            R.id.nav_transaction_sell -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, TransactionSellFragment())
                    .commit()
                layoutID = R.id.nav_transaction_sell
            }
            /* ------------------------ A S S E T ---------------------------*/
            R.id.nav_add_aset -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, AddAssetFragment())
                    .commit()
                layoutID = R.id.nav_add_aset
            }
            R.id.nav_list_aset -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, AssetListFragment())
                    .commit()
                layoutID = R.id.nav_list_aset
            }
            /* ------------------------ R E P O R T ---------------------------*/
            R.id.nav_report_all -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, ReportFragment())
                    .commit()
                layoutID = R.id.nav_report_all
            }
            /* ------------------------ L O G O U T ---------------------------*/
            R.id.nav_logout -> {
                var prefManager = SharedPrefManager(this@MainActivity)
                prefManager.savePrefBoolean(false)

                startActivity(intentFor<LoginActivity>())
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /*Unused*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }
    /*Unused*/

}
