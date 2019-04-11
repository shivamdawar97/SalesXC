package com.example.shivam97.salesxc.orderClasses

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.SalesXC.*
import com.google.firebase.firestore.DocumentReference
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_pre_view.*
import java.util.*
import kotlin.collections.HashMap

class PreViewActivity : AppCompatActivity() {

    private var printed=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_view)
        bill_preview_tv.text=intent.getStringExtra("bill")

    }

    fun finish(view: View){
        finish()
    }

    @Throws(TypeCastException::class)
    fun printPreview(view: View){
        if(!printed)
        {
            showProgressDialog(this)
            printed=true
            val collection= docReference.collection("Products")
            docReference.firestore.runTransaction {
                transaction ->
                val exList=HashMap<DocumentReference,HashMap<String,Int>>()
                val stList=HashMap<DocumentReference,Int>()
                for(product in RecyclerAdapter.orderProducts) {
                    val pDoc = collection.document(product.name)
                    val snap = transaction.get(pDoc)
                    var qty = Math.round(product.quantity)
                    Log.e("FireStore_STUFF", "HERE1")
                    if (snap["expiry_date"] != null) {
                    val exMap = snap["expiry_date"] as HashMap<String, Int>
                    Log.e("FireStore_STUFF", "HERE2")
                    val sortedExMap = TreeMap<String, Int>(exMap)
                    for (stock in sortedExMap) {
                        Log.i("MY_UPDATE", "qty Before:${stock.value}")
                        var st = stock.value
                        if (qty == st) {
                            exMap.remove(stock.key)
                            break
                        } else if (qty > st) {
                            qty -= st
                            exMap.remove(stock.key)
                            continue
                        } else {
                            st -= qty
                            exMap[stock.key] = st
                            break
                        }
                    }
                    exList[pDoc]=exMap
                }
                    var tStock= snap["total_stock"].toString().toInt()
                    tStock-=Math.round(product.quantity)
                    stList[pDoc]=tStock
                }

                for(e in exList)
                    transaction.update(e.key, "expiry_date", e.value)
                for(s in stList)
                    transaction.update(s.key,"total_stock",s.value)

            }.addOnCompleteListener {
                hideProgressDialog()
                if(!it.isSuccessful)
                {
                    Toasty.error(this@PreViewActivity,"failed", Toast.LENGTH_LONG).show()
                    Log.e("FireStore_STUFF",it.exception?.message)
                    it.exception?.printStackTrace()
                }
                else
                    Toasty.success(this@PreViewActivity,"updated", Toast.LENGTH_LONG).show()
            }


        }
        //send print command


    }

}
