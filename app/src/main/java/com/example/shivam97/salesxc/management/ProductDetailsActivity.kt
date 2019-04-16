package com.example.shivam97.salesxc.management

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.SalesXC.Companion.docReference
import kotlinx.android.synthetic.main.activity_product_details.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class ProductDetailsActivity : AppCompatActivity() {

    private var productId="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        productId=intent.getStringExtra("uid")
        docReference.collection("Products").document(productId).get().addOnSuccessListener {
            product_details_name.text=it.id
            product_details_uniqueId.text=it["uid"].toString()
            product_details_purchasing.text=it["purchase"].toString()
            product_details_selling.text=it["selling_price"].toString()
            product_details_stock_available.text=it["total_stock"].toString()
           try {

               val expiries= TreeMap<String,Int>(it["expiry_date"] as HashMap<String, Int>)
               //using tree map for sorted order
               val dateFormat=SimpleDateFormat("dd - MMM - yy")
               val sBuilder=StringBuilder()
               for( m in expiries){
                   val date=dateFormat.format(m.key.toLong())
                   sBuilder.append("${m.value} items will expire on $date")
                   sBuilder.append("\n\n")
               }
               Log.i("MY_MAP",sBuilder.toString())
               product_details_expiries.text=sBuilder.toString()

           }catch ( e:TypeCastException ){
               e.printStackTrace()
               Log.e("MY_MAP","ERROR :"+e.message)
           }

        }

       startActivity(Intent(this,LineChartActivity::class.java))

    }

    fun finish(v: View){
        finish()
    }
}
