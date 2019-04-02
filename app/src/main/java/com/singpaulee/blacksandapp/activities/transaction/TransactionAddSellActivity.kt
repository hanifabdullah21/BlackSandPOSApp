package com.singpaulee.blacksandapp.activities.transaction

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.singpaulee.blacksandapp.R
import com.singpaulee.blacksandapp.activities.item.ItemSelectionActivity
import com.singpaulee.blacksandapp.adapter.ItemCartAdapter
import com.singpaulee.blacksandapp.adapter.ItemCartSellAdapter
import com.singpaulee.blacksandapp.helper.SharedPrefManager
import com.singpaulee.blacksandapp.model.*
import com.singpaulee.blacksandapp.rest.ApiInterface
import com.singpaulee.blacksandapp.rest.RestConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_transaction_add_buy.*
import kotlinx.android.synthetic.main.activity_transaction_add_sell.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionAddSellActivity : AppCompatActivity() {

    val TAG = "TransactionAddSell"

    //Buyer
    var listBuyer: java.util.ArrayList<BuyerModel>? = null //List of Buyer
    var listBuyerName: MutableList<String>? = null  //List name of Buyer
    var nameBuyer: String? = ""                     //Name of Buyer who choosen
    var idBuyer: Int? = -1                          //Id if Buyer who chosen

    //Item
    var listItem: java.util.ArrayList<ItemModel>? = null
    var listItemCart: java.util.ArrayList<ItemModel>? = null
    var itemAdapter: ItemCartSellAdapter? = null
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
        setContentView(R.layout.activity_transaction_add_sell)

        settingBuyer()
        settingItem()
        settingDate()

        atsa_btn_add_transaction.onClick {
            if (!validasi()) {
                return@onClick
            }
            if (atsa_edt_transport_load.text.toString().isBlank() || atsa_edt_transport_load.text.toString().isEmpty()) {
                openDialogConfirmation()
            } else {
                freightCost = atsa_edt_transport_load.text.toString().toInt()
                postTransaction()
            }
        }
    }

    /* ---------------------------- SUPPLIER ----------------------------------- */

    private fun settingBuyer() {
        listBuyer = java.util.ArrayList()
        listBuyerName = mutableListOf()
        atsa_tv_buyer.isEnabled = false
        getListOfBuyer()

        atsa_tv_buyer.onClick {
            selector("Pilih Pemasok", listBuyerName!!) { dialog, i ->
                nameBuyer = listBuyer?.get(i)?.nama
                idBuyer = listBuyer?.get(i)?.id
                atsa_tv_buyer.text = nameBuyer
            }
        }
    }

    @SuppressLint("CheckResult", "LongLogTag")
    private fun getListOfBuyer() {
        var prefManager = SharedPrefManager(this@TransactionAddSellActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        val getListBuyer: Observable<BuyerResultListModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .getListBuyer(header)

        getListBuyer.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t: BuyerResultListModel? ->
                if (t?.status?.success?.equals(true)!!) {
                    listBuyer = t?.result
                    for (i in listBuyer?.indices!!) {
                        listBuyerName?.add(listBuyer?.get(i)?.nama.toString())
                    }
                    atsa_tv_buyer.isEnabled = true
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
        atsa_btn_add_item.isEnabled = false

        listItem = java.util.ArrayList()
        listItemCart = java.util.ArrayList()
        itemLayoutManager = LinearLayoutManager(this@TransactionAddSellActivity, LinearLayoutManager.VERTICAL, false)
        itemAdapter = ItemCartSellAdapter(this@TransactionAddSellActivity, listItem)

        atsa_rv_list_item.layoutManager = itemLayoutManager
        atsa_rv_list_item.adapter = itemAdapter

        getListOfItem()

        atsa_btn_add_item.onClick {
            var i = Intent(this@TransactionAddSellActivity, ItemSelectionActivity::class.java)
            i.putParcelableArrayListExtra("LISTITEM", listItem)
            i.putExtra("ACTION", "SELL")
            startActivityForResult(i, 81)
        }
    }

    @SuppressLint("CheckResult")
    private fun getListOfItem() {
        var prefManager = SharedPrefManager(this@TransactionAddSellActivity)
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
                    atsa_btn_add_item.isEnabled = true
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
                    var list: java.util.ArrayList<ItemModel> =
                        data?.getParcelableArrayListExtra<ItemModel>("ITEMLIST") ?: java.util.ArrayList()
                    listItemCart?.add(itemModel)
                    listItem = list

                    itemAdapter = ItemCartSellAdapter(this, listItemCart)
                    itemAdapter?.notifyDataSetChanged()
                    atsa_rv_list_item.adapter = itemAdapter
                    atsa_tv_total.text = "" + itemAdapter?.countTotal()
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
        atsa_tv_total.text = "" + itemAdapter?.countTotal()
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
                atsa_tv_date.text = dateDMY
            }
        }

        atsa_tv_date.onClick {
            datePickerDialog = DatePickerDialog(
                this@TransactionAddSellActivity,
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
        if (atsa_tv_buyer.text.toString().isEmpty()) {
            atsa_tv_buyer.setError("Silahkan pilih supplier")
            atsa_tv_buyer.requestFocus()
            return false
        }
        if (listItemCart!!.isEmpty()) {
            toast("Daftar pesananmu masih kosong")
            return false
        }
        if (atsa_tv_date.text.toString().isEmpty()) {
            atsa_tv_date.setError("Silahkan masukkan tanggal jatuh tempo")
            atsa_tv_date.requestFocus()
            return false
        }
        return true
    }

    @SuppressLint("CheckResult")
    fun postTransaction() {
        var prefManager = SharedPrefManager(this@TransactionAddSellActivity)
        var token = prefManager.getToken()
        var header = "Bearer " + token

        indexPaidOff = checkCheckBox()

        val model = Gson().toJson(listItemCart)

        val transansactionJson = JsonObject()
        transansactionJson.addProperty("pelanggan_id", idBuyer)
        transansactionJson.addProperty("tanggal_tempo", dateYMD)
        transansactionJson.addProperty("beban_angkut", freightCost)
        transansactionJson.addProperty("lunas", indexPaidOff)
        transansactionJson.addProperty("tgl", atsa_edt_date.text.toString())
        transansactionJson.add("barang", JsonParser().parse(model).asJsonArray)

        Log.d(TAG, transansactionJson.toString())

        val transaction: Observable<ItemTransactionResultModel> = RestConfig.retrofit
            .create<ApiInterface>(ApiInterface::class.java)
            .postNewTransactionSell(header, transansactionJson)

        transaction.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.status?.success as Boolean) {
                    toast("Transaksi berhasil " + response.result?.id)

                    moveActivity(response.result?.id)
                } else {
                    toast("Transaksi Gagal")
                }
            }, { error ->
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
        if (atsa_cb_paid_off.isChecked) {
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
