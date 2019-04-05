package com.example.shivam97.salesxc.customers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.SalesXC
import com.google.firebase.firestore.FirebaseFirestore
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_add_customer.*

class AddCustomer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)

    }


    fun finish(v: View){
        finish()
    }

    fun addCustomer(v:View){
        val name=add_cus_name.text?.toString()
        val phone=add_cus_phn.text?.toString()
        val place=add_cus_place.text?.toString()

        SalesXC.showProgressDialog(this@AddCustomer)
        val map=HashMap<String,Any>()
        map["name"]=name!!
        map["phone"]=phone!!
        map["place"]=place!!
        map["due_amount"]=0

        FirebaseFirestore.getInstance().collection("test")
                .document("shop1").collection("Customers")
                .document(name).set(map).addOnCompleteListener {
                    SalesXC.hideProgressDialog()
                    if(it.isSuccessful){
                        Toasty.success(this@AddCustomer, "Customer Added", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else
                        Toasty.error(this@AddCustomer,"Error in saving product"+it.exception?.message, Toast.LENGTH_LONG).show()
                }
    }

}

