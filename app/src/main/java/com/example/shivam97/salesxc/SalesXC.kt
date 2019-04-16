package com.example.shivam97.salesxc

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import com.example.shivam97.salesxc.orderClasses.OrderProducts
import com.example.shivam97.salesxc.orderClasses.RecyclerAdapter
import com.example.shivam97.salesxc.roomClasses.Product
import com.example.shivam97.salesxc.roomClasses.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
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
            progressDialog!!.setOnKeyListener { dialogInterface, keyCode, keyEvent -> keyCode == KeyEvent.KEYCODE_BACK }
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


        fun notifyProductDataChanged() {

            val collection= docReference.collection("Products")
            docReference.firestore.runTransaction {
                transaction ->
                val exList=HashMap<DocumentReference,HashMap<String,Int>>()
                Log.i("UpdateReport","called size:"+RecyclerAdapter.orderProducts.size)
                for (product in RecyclerAdapter.orderProducts){
                    val pDoc = collection.document(product.name)
                    val snap = transaction.get(pDoc)
                    val qty = Math.round(product.quantity)
                    val sellPrice=(snap["selling_price"] as Double).toInt()
                    val sale=sellPrice*qty
                    val pReport=pDoc.collection("Report").document(currentMonth)
                    val snap2=transaction.get(pReport)
                    if(snap2[todayDay]!=null)
                    {
                     val saleMap=(snap2[todayDay] as HashMap<String,Int>)
                     saleMap["qty"]?.plus(qty)
                     saleMap["sale"]?.plus(sale)
                     exList[pReport]=saleMap
                    }
                    else{
                        val saleMap=  HashMap<String,Int>()
                        saleMap["qty"]=qty
                        saleMap["sale"]=sale
                        exList[pReport]=saleMap
                        val map=HashMap<String,Any>()
                        map[todayDay]=saleMap
                        pReport.set(map)
                    }
                }

                for(e in exList)
                    transaction.update(e.key, todayDay, e.value)
            }.addOnCompleteListener {
                RecyclerAdapter.orderProducts.clear()
                if(!it.isSuccessful)
                {
                    it.exception?.printStackTrace()
                    Log.i("UpdateReport","Failed :"+it.exception?.message)
                }
            }
        }
    }


}
