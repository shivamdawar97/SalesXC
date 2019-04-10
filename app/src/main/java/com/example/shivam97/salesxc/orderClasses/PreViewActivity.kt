package com.example.shivam97.salesxc.orderClasses

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.SalesXC.docReference
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

    fun printPreview(view: View){
        if(!printed)
        {
            printed=true
            for(product in RecyclerAdapter.orderProducts){
                 val pDoc= docReference.collection("Products").document(product.name)
                pDoc.get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val snap=it.result!!
                        var qty= Math.round(product.quantity)
                        try {
                            val exMap= snap["expiry_date"] as HashMap<String, Int>
                            val sortedExMap=TreeMap<String,Int>(exMap)
                            for(stock in sortedExMap){
                                Log.i("MY_UPDATE", "qty Before:${stock.value}")
                                var st=stock.value
                                if(qty==st)
                                {
                                    exMap.remove(stock.key)
                                    break
                                }
                                else if(qty>st)
                                { qty-=st
                                    exMap.remove(stock.key)
                                    continue
                                }
                                else
                                { st-=qty
                                   exMap[stock.key]=st
                                   break
                                }
                            }
                            var tStock= snap["total_stock"] as Int
                            tStock-=Math.round(product.quantity)
                            pDoc.update("expiry_date",exMap).addOnCompleteListener {t->
                                if(t.isSuccessful)
                                {
                                    pDoc.update("total_stock",tStock)
                                    Toasty.success(this@PreViewActivity,"updated", Toast.LENGTH_LONG).show()
                                }
                                else{
                                    Toasty.error(this@PreViewActivity,"failed", Toast.LENGTH_LONG).show()
                                    Log.i("MY_UPDATE",t.exception?.message)
                                }

                            }

                        }
                        catch (e:TypeCastException){

                        }

                    }
                }
            }
        }
        //send print command


    }

}
