package com.example.shivam97.salesxc.management

import android.Manifest
import android.app.DatePickerDialog
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
import com.example.shivam97.salesxc.SalesXC.*
import com.example.shivam97.salesxc.SalesXC.Companion.docReference
import com.example.shivam97.salesxc.SalesXC.Companion.hideProgressDialog
import com.example.shivam97.salesxc.SalesXC.Companion.showProgressDialog
import com.example.shivam97.salesxc.roomClasses.Product

import java.io.IOException

import es.dmoral.toasty.Toasty

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.a_add_product.*
import java.util.*
import kotlin.collections.HashMap

class AddProduct : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editUniqueId: EditText
    private lateinit var editPurchase: EditText
    private lateinit var editSelling: EditText
    private lateinit var editStock: EditText
    private var date="never"
    private lateinit var scannerFrame: FrameLayout
    private lateinit var scanner: BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_add_product)
        editName = findViewById(R.id.name_edit)
        editUniqueId = findViewById(R.id.unique_id_edit)
        editPurchase = findViewById(R.id.purchase_edit)
        editSelling = findViewById(R.id.selling_edit)
        editStock = findViewById(R.id.stock_edit)
        scannerFrame = findViewById(R.id.scanner_frame)

    }

    fun scanBarcode(view: View) {
        scanner = BarcodeScanner(this@AddProduct)
        scannerFrame.addView(scanner)
        scannerFrame.visibility = View.VISIBLE
        scanner.scan(object : BarcodeScanner.ScannerCallback {
            override fun barcodeScanned(code: String) {
                scannerFrame.removeView(scanner)
                editUniqueId.setText(code)
            }

            override fun scannerFailed(message: String) {
                Toast.makeText(this@AddProduct, message, Toast.LENGTH_LONG).show()
            }
        }, scannerFrame)
    }

    fun saveProduct(view: View) {
        val name: String = editName.text.toString()
        val uid: String = editUniqueId.text.toString()
        val purchase: String = editPurchase.text.toString()
        val selling: String = editSelling.text.toString()
        val stock: String = editStock.text.toString()

        if (name.isEmpty() || uid.isEmpty() || purchase.isEmpty() || selling.isEmpty() || stock.isEmpty()) {
            Toast.makeText(this@AddProduct,"please fill all details",Toast.LENGTH_LONG).show()
        }
        else if(name.length>10){
            Toast.makeText(this@AddProduct,"Name should not be longer than 10 character",Toast.LENGTH_LONG).show()
        }
        else {
            /*val product = Product()
            product.name = name
            product.uniqueId = uid
            product.purchase = purchase
            product.selling = selling
            product.stock = stock
            repository.insertProduct(product)
            */
            showProgressDialog(this@AddProduct)
            val map=HashMap<String,Any>()
            map["uid"]=uid
            map["purchase"]=purchase.toFloat()
            map["selling_price"]=selling.toFloat()
            map["total_stock"]=stock.toInt()

            if(date != "never")
            {
                val expiryMap=HashMap<String,Int>()
                expiryMap[date]=stock.toInt()
                map["expiry_date"]=expiryMap

            }
            docReference.collection("Products")
                    .document(name).set(map).addOnCompleteListener {
                        hideProgressDialog()
                        if(it.isSuccessful){
                            Toasty.success(this@AddProduct, "Product Added", Toast.LENGTH_LONG).show()
                            finish()
                        }
                        else
                            Toasty.error(this@AddProduct,"Error in saving product"+it.exception?.message,Toast.LENGTH_LONG).show()
                    }
        }
    }

    override fun onBackPressed() {
        if (scannerFrame.visibility == View.VISIBLE)
        {
            scannerFrame.visibility = View.GONE
            scannerFrame.removeView(scanner)
        }
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
                        scanner.cameraSource.start(scanner.surfaceView.holder)
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
            val calendar =Calendar.getInstance()
            calendar.set(p1,p2,p3)
            date=calendar.timeInMillis.toString()
            expiry_date_checkBox.isChecked=false
        }, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun setExpiryNever(view :View){
        if(expiry_date_checkBox.isChecked)
        {
            date="never"
            date_tv.text="Never"
        }
        else
            selectDate(select_date_btn)
    }


}
