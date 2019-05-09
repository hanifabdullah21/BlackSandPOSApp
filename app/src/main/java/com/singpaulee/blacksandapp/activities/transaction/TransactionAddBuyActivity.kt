package com.singpaulee.blacksandapp.activities.transaction

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.item.ItemSelectionActivity
import com.singpaulee.blacksandapp.adapter.ItemCartAdapter
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.*
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.abc_tooltip.*
import kotlinx.android.synthetic.main.activity_transaction_add_buy.*
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*


class TransactionAddBuyActivity : AppCompatActivity() {

    private val TAG = "AddTransBuyActivity"

//    var dialog = progressDialog(message = "Loading")

    //Supplier
    var listSupplier: ArrayList<SupplierModel>? = null //List of supplier
    var listSupplierName: MutableList<String>? = null  //List name of supplier
    var nameSupplier: String? = ""                     //Name of supplier who choosen
    var idSupplier: Int? = -1                          //Id if supplier who chosen

    //Item
    var listItem: ArrayList<ItemModel>? = null
    var listItemCart: ArrayList<ItemModel>? = null
    var itemAdapter: ItemCartAdapter? = null
    var itemLayoutManager: RecyclerView.LayoutManager? = null

    //Date
    lateinit var datePickerDialog: DatePickerDialog
    var calendar = Calendar.getInstance()
    @SuppressLint("SimpleDateFormat")
    var sdfDayMonthYear: SimpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
    @SuppressLint("SimpleDateFormat")
    var sdfYearMonthDay: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var dateNow = ""
    var dateYMD = ""
    var dateDMY = ""

    //Pay
    var indexPaidOff: Int = 0

    //Transport load
    var freightCost: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_add_buy)

        settingSupplier()
        settingItem()
        settingDate()

        atba_btn_add_transaction.onClick {
            if (!validasi()) {
                return@onClick
            }
            if (atba_edt_transport_load.text.toString().isBlank() || atba_edt_transport_load.text.toString().isEmpty()) {
                openDialogConfirmation()
            } else {
                freightCost = atba_edt_transport_load.text.toString().toInt()
                postTransaction()
            }
        }
    }

    /* ---------------------------- SUPPLIER ----------------------------------- */

    private fun settingSupplier() {
        listSupplier = ArrayList()
        listSupplierName = mutableListOf()
        atba_tv_supplier.isEnabled = false
        getListOfSupplier()

        atba_tv_supplier.onClick {
            selector("Pilih Pemasok", listSupplierName!!) { dialog, i ->
                nameSupplier = listSupplier?.get(i)?.nama
                idSupplier = listSupplier?.get(i)?.id
                atba_tv_supplier.text = nameSupplier
            }
        }
    }

    @SuppressLint("CheckResult", "LongLogTag")
    private fun getListOfSupplier() {
        var prefManager = SharedPrefManager(this@TransactionAddBuyActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListSupplier: Observable<SupplierResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getListSupplier(header)

        getListSupplier.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: SupplierResultListModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    listSupplier = t?.result
                    for (i in listSupplier?.indices!!) {
                        listSupplierName?.add(listSupplier?.get(i)?.nama.toString())
                    }
                    atba_tv_supplier.isEnabled = true
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menampilkan daftar pemasok")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }

    /* ----------------------------- ITEM ------------------------------------- */

    private fun settingItem() {
        atba_btn_add_item.isEnabled = false

        listItem = ArrayList()
        listItemCart = ArrayList()
        itemLayoutManager = LinearLayoutManager(this@TransactionAddBuyActivity, LinearLayoutManager.VERTICAL, false)
        itemAdapter = ItemCartAdapter(this@TransactionAddBuyActivity, listItem)

        atba_rv_list_item.layoutManager = itemLayoutManager
        atba_rv_list_item.adapter = itemAdapter

        getListOfItem()

        atba_btn_add_item.onClick {
            var i = Intent(this@TransactionAddBuyActivity, ItemSelectionActivity::class.java)
            i.putParcelableArrayListExtra("LISTITEM", listItem)
            startActivityForResult(i, 81)
        }
    }

    @SuppressLint("CheckResult")
    private fun getListOfItem() {
        var prefManager = SharedPrefManager(this@TransactionAddBuyActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListItem: Observable<ItemResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getListItem(header)

        getListItem.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: ItemResultListModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    listItem = t?.result
                    atba_btn_add_item.isEnabled = true
                } else {
                    Log.d(TAG, "FAILED " + t?.status?.message)
                    toast("Gagal menampilkan barang")
                }
            }, { error ->
                Log.d(TAG, "ERROR " + error.message)
                toast("Maaf, sedang ada masalah dengan server.")
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            81 -> {
                if (resultCode == Activity.RESULT_OK) {
                    var itemModel: ItemModel = data?.getParcelableExtra("ITEMMODEL") ?: ItemModel()
                    var list: ArrayList<ItemModel> =
                        data?.getParcelableArrayListExtra<ItemModel>("ITEMLIST") ?: ArrayList()
                    listItemCart?.add(itemModel)
                    listItem = list

                    itemAdapter = ItemCartAdapter(this, listItemCart)
                    itemAdapter?.notifyDataSetChanged()
                    atba_rv_list_item.adapter = itemAdapter
                    atba_tv_total.text = "" + itemAdapter?.countTotal()
                }
            }
            else -> {
            }
        }
    }

    fun updateList(model: ItemModel?) {
//        model?.let { listItem?.add(it) }
        if (model != null) {
            listItem?.add(model)
        } else {
            toast("null")
        }
        Log.d(TAG, listItem.toString())
    }

    fun notifyListCart() {
        atba_tv_total.text = "" + itemAdapter?.countTotal()
    }

    /* ----------------------------- DATE ------------------------------------- */

    fun settingDate() {
        dateNow = sdfYearMonthDay.format(calendar.time)

        var date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            view.minDate = System.currentTimeMillis() - 1000

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            dateDMY = sdfDayMonthYear.format(calendar.time)
            dateYMD = sdfYearMonthDay.format(calendar.time)

            if (sdfYearMonthDay.parse(dateYMD).before(sdfYearMonthDay.parse(dateNow))) {
                toast("Tanggal tidak valid")
            } else {
                atba_tv_date.text = dateDMY
            }
        }

        atba_tv_date.onClick {
            datePickerDialog = DatePickerDialog(
                this@TransactionAddBuyActivity,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }
    }

    /* ----------------------------- TRANSACTION ------------------------------ */
    private fun validasi(): Boolean {
        if (atba_tv_supplier.text.toString().isEmpty()) {
            atba_tv_supplier.setError("Silahkan pilih supplier")
            atba_tv_supplier.requestFocus()
            return false
        }
        if (listItemCart!!.isEmpty()) {
            toast("Daftar pesananmu masih kosong")
            return false
        }
        if (atba_tv_date.text.toString().isEmpty()) {
            atba_tv_date.setError("Silahkan masukkan tanggal jatuh tempo")
            atba_tv_date.requestFocus()
            return false
        }
        return true
    }

    @SuppressLint("CheckResult")
    fun postTransaction() {
//        dialog.show()
        var prefManager = SharedPrefManager(this@TransactionAddBuyActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        indexPaidOff = checkCheckBox()

        val model = Gson().toJson(listItemCart)

        val transansactionJson = JsonObject()
        transansactionJson.addProperty("pemasok_id", idSupplier)
        transansactionJson.addProperty("tanggal_tempo", dateYMD)
        transansactionJson.addProperty("beban_angkut", freightCost)
        transansactionJson.addProperty("lunas", indexPaidOff)
        transansactionJson.addProperty("tgl", atba_edt_date.text.toString())
        transansactionJson.add("barang", JsonParser().parse(model).asJsonArray)

        Log.d(TAG, transansactionJson.toString())

        val transaction: Observable<ItemTransactionResultModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .postNewTransactionBuy(header, transansactionJson)

        transaction.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
//                dialog.dismiss()
                if (response.status?.success as Boolean) {
                    Log.d(TAG, "Post transasction " + response.status?.message)
                    toast("Transaksi berhasil " + response.result?.id)
                    moveActivity(response.result?.id)
                } else {
                    Log.d(TAG, "Post transasction Failed " + response.status?.message)
                    toast("Transaksi Gagal")
                }
            }, { error ->
//                dialog.dismiss()
                Log.d(TAG, "" + error.message)
                toast("Transaksi Gagal karena " + error.message)
            })
    }

    fun moveActivity(id: Int?) {
        when (indexPaidOff) {
            0 -> {
                var intent = Intent(this, DebtRepaymentActivity::class.java)
                intent.putExtra("ID_", id)
                startActivity(intent)
            }
            1 -> {

            }
        }
    }

    /* -----------------------------  ------------------------------ */
    fun checkCheckBox(): Int {
        var index: Int
        if (atba_cb_paid_off.isChecked) {
            index = 1
        } else {
            index = 0
        }
        return index
    }

    /* ----------------------------- Beban angkut ------------------------------ */

    fun openDialogConfirmation() {
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Biaya Angkut")
            setMessage("Apakah anda yakin tidak ada biaya angkut?")
            setPositiveButton("YA", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton("TIDAK", negativeButtonClick)
            show()
        }
    }

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        postTransaction()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        dialog.dismiss()
    }
}
