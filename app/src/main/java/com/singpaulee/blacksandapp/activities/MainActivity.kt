package com.singpaulee.blacksandapp.activities

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
import com.singpaulee.blacksandapp.fragment.BuyerAddFragment
import com.singpaulee.blacksandapp.fragment.ItemAddFragment
import com.singpaulee.blacksandapp.fragment.ItemListFragment
import com.singpaulee.blacksandapp.fragment.SupplierAddFragment
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_item_add -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, ItemAddFragment())
                    .commit()
            }
            R.id.nav_item_list -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, ItemListFragment())
                    .commit()
            }
            R.id.nav_supplier_add -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, SupplierAddFragment())
                    .commit()
            }
            R.id.nav_buyer_add -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cm_framelayout, BuyerAddFragment())
                    .commit()
            }
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
