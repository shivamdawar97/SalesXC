package com.example.shivam97.salesxc.management

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.example.shivam97.salesxc.BarcodeScanner
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.roomClasses.Product
import java.io.IOException
import es.dmoral.toasty.Toasty
import com.example.shivam97.salesxc.SalesXC.repository
import kotlinx.android.synthetic.main.a_add_product.*
import android.app.DatePickerDialog
import android.content.Intent
import java.util.*


class AddProduct : AppCompatActivity() {

  private lateinit var editName: EditText
  private lateinit var editUniqueId: EditText
  private lateinit var editPurchase: EditText
  private lateinit var editSelling: EditText
  private lateinit var editStock: EditText
  private var scanning=false
  private lateinit var scanner: BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_add_product)
        editName = findViewById(R.id.name_edit)
        editUniqueId = findViewById(R.id.unique_id_edit)
        editPurchase = findViewById(R.id.purchase_edit)
        editSelling = findViewById(R.id.selling_edit)
        editStock = findViewById(R.id.stock_edit)

    }

    fun scanBarcode(view: View) {
        scanner=BarcodeScanner(this)
        scanner.startScan(object:BarcodeScanner.ScannerCallback{
            override fun barcodeScanned(code: String) {
                scanner.stopScanning()
                unique_id_edit.setText(code)
            }
            override fun scannerFailed(message: String) {
                Toasty.error(this@AddProduct, message, Toast.LENGTH_LONG).show()
            }

        })
        scanning=true
    }



    fun saveProduct(view: View) {
        val name: String = editName.text.toString()
        val uid: String = editUniqueId.text.toString()
        val purchase: String = editPurchase.text.toString()
        val selling: String = editSelling.text.toString()
        val stock: String = editStock.text.toString()
        if (name.isEmpty() || uid.isEmpty() || purchase.isEmpty() || selling.isEmpty() || stock.isEmpty()) {

        } else {
            val product = Product()
            product.name = name
            product.uniqueId = uid
            product.purchase = purchase
            product.selling = selling
            product.stock = stock
            repository.insert(product)
            Toasty.success(this@AddProduct, "Product Addded", Toast.LENGTH_LONG).show()
            finish()
        }

    }

    override fun onBackPressed() {
        if (scanning)
          scanner.stopScanning()
        else
            super.onBackPressed()
    }

    fun finish(view: View) {
        finish()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } else
                    Toasty.warning(this@AddProduct, "Please Grant Camera Permission", Toast.LENGTH_LONG, true).show()
            }
        }
    }

    fun selectDate(view: View) {
        val myCalendar = Calendar.getInstance()
        DatePickerDialog(this@AddProduct, DatePickerDialog.OnDateSetListener {
            p0, p1, p2, p3 ->
            date_tv.text="$p3:${p2+1}:$p1"
            expiry_date_checkBox.isChecked=false
        }, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()

    }

    fun setExpiryNever(view :View){
        if(expiry_date_checkBox.isChecked)
        date_tv.text="Never"
        else
            selectDate(select_date_btn)
    }


}
