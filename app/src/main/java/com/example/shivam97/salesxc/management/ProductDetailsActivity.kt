package com.example.shivam97.salesxc.management

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.SalesXC.docReference
import kotlinx.android.synthetic.main.activity_product_details.*

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
               var expiries=it["expiry_date"].toString()
               expiries=expiries.replace("{","")
               expiries=expiries.replace("}","")
               expiries=expiries.replace("="," : ")
               product_details_expiries.text=expiries

               /*
               expiries=expiries as Map<*, *>
               for(e in expiries){
                   product_details_expiries.text="${product_details_expiries.text} \n ${e.key} : ${e.value}"
               }*/
           }catch ( e:TypeCastException ){
               e.printStackTrace()
               Log.e("MAP_ERROR",e.message)
           }

        }

    }

    fun finish(v: View){
        finish()
    }
}
