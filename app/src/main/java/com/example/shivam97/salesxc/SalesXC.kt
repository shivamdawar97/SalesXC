package com.example.shivam97.salesxc

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import com.example.shivam97.salesxc.orderClasses.RecyclerAdapter
import com.example.shivam97.salesxc.roomClasses.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class SalesXC : Application() {
    override fun onCreate() {
        super.onCreate()
        customers = ArrayList()
        mAuth = FirebaseAuth.getInstance()
        repository = Repository(this)
        docReference = FirebaseFirestore.getInstance().collection("test")
                .document("shop1")
        addListenerForProducts()
        var dateFormat= SimpleDateFormat("dd-MMM-yy", Locale.US)
        val date =Date(System.currentTimeMillis())
        todayDate=dateFormat.format(date)
        dateFormat=SimpleDateFormat("MMM", Locale.US)
        currentMonth=dateFormat.format(date)
        dateFormat=SimpleDateFormat("dd", Locale.US)
        todayDay=dateFormat.format(date)

    }

    private fun addListenerForProducts() {

    }

    companion object {

        lateinit var mAuth: FirebaseAuth
        var mUser: FirebaseUser? = null
        lateinit var repository: Repository
        private var progressDialog: AlertDialog? = null
        lateinit var docReference: DocumentReference
        lateinit  var customers: ArrayList<String>
        lateinit var todayDate:String
        lateinit var currentMonth:String
        lateinit var todayDay:String

        fun showProgressDialog(context: Context) {
            val builder = AlertDialog.Builder(context)
            // View v=LayoutInflater.from(context).inflate(R.layout.global_progress,null);
            builder.setView(ProgressBar(context))
            progressDialog = builder.create()
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
            try {
                progressDialog!!.show()
            } catch (e: WindowManager.BadTokenException) {
                e.printStackTrace()
            }

        }

        fun hideProgressDialog() {
            progressDialog!!.dismiss()
        }

        fun showToast(ctx: Context, msg: String) {
            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
        }

        fun notifyProductDataChanged(totalSale:Float) {

            Log.i("UpdateReport","called size:"+RecyclerAdapter.orderProducts.size)
            val collection= docReference.collection("Products")
            docReference.firestore.runTransaction {
                transaction ->
                val exUpdateList=HashMap<DocumentReference,HashMap<String,Int>>()
                val exSetList=HashMap<DocumentReference,Any>()
                val monthList=HashMap<DocumentReference,Int>()
                for (product in RecyclerAdapter.orderProducts){

                    val qty = Math.round(product.quantity)
                    val sale=Math.round(product.rate*qty)
                    val pCollection=collection.document(product.name).collection("Report")
                    val pReport=pCollection.document(currentMonth)
                    val snap2=transaction.get(pReport)
                    if(snap2[todayDay]!=null)
                    {
                        val saleMap=(snap2[todayDay] as HashMap<String,Int>)
                     saleMap["qty"] =if(todayDay=="01") qty  else saleMap["qty"]!!+qty
                     saleMap["sale"]=if(todayDay=="01") sale else saleMap["sale"]!!+sale
                     exUpdateList[pReport]=saleMap
                    }

                    else{
                        val saleMap=  HashMap<String,Int>()
                        saleMap["qty"]=qty
                        saleMap["sale"]=sale
                        val map=HashMap<String,Any>()
                        map[todayDay]=saleMap
                        exSetList[pReport]=map
                    }

                    val pMReport=pCollection.document("AllMonth")
                    val snap3=transaction.get(pMReport)
                    if (snap3[currentMonth]!=null)
                    {
                        val mSale=snap3[currentMonth].toString().toInt()+sale
                        monthList[pMReport]=mSale
                    }
                }


                val shopMonthDoc= docReference.collection("Report").document(currentMonth)
                val snap4=transaction.get(shopMonthDoc)
                if(snap4[todayDay]!=null)
                {
                    var sale=snap4[todayDay] as Double
                    sale+=totalSale

                    transaction.update(shopMonthDoc, todayDay,sale)
                }

                else{
                    val map=HashMap<String,Float>()
                    map[todayDay]=totalSale
                    transaction.set(shopMonthDoc,map)
                }

               for(e in exUpdateList)
                    transaction.update(e.key, todayDay, e.value)

               for(e in exSetList)
                    transaction.set(e.key, e.value)

               for(e in monthList)
                    transaction.update(e.key, currentMonth,e.value)


            }.addOnCompleteListener {
                RecyclerAdapter.orderProducts.clear()
                if(!it.isSuccessful)
                {
                    it.exception?.printStackTrace()
                    Log.i("UpdateReport","Failed :"+it.exception?.message)
                }
                else{

                }
            }
        }
    }


}
