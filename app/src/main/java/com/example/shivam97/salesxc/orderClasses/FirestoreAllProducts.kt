package com.example.shivam97.salesxc.orderClasses

import android.util.Log
import com.example.shivam97.salesxc.SalesXC.docReference
import com.example.shivam97.salesxc.SalesXC.repository
import com.example.shivam97.salesxc.roomClasses.Product

public class FirestoreAllProducts {

    companion object {

        fun saveProductsToRoom() {
            val productDocs = docReference.collection("Products")
            productDocs.get().addOnSuccessListener {
                if (it != null) {
                   for(document in it){
                       val product= Product()
                       product.name=document.id
                       product.uniqueId=document["uid"].toString()
                       product.purchase=document["purchase"].toString()
                       product.selling=document["selling_price"].toString()
                       product.stock=document["total_stock"].toString()
                       if(repository.insert(product))
                           Log.i("INSERT","success")
                       else
                           Log.i("INSERT","failed")

                   }
                }

            }
        }
    }
}
