package com.example.shivam97.salesxc.orderClasses

import android.util.Log
import com.example.shivam97.salesxc.SalesXC.Companion.docReference
import com.example.shivam97.salesxc.SalesXC.Companion.repository
import com.example.shivam97.salesxc.roomClasses.Product

public class FirestoreAllProducts {

    companion object {

        val allProducts=ArrayList<Product>()
        fun saveProductsToRoom() {
            val productDocs = docReference.collection("Products")
            productDocs.get().addOnSuccessListener {
                if (it != null) {
                    val products = arrayOfNulls<Product>(it.count())
                    for((i, document) in it.withIndex()){
                       val product= Product()
                       product.name=document.id
                       product.uniqueId=document["uid"].toString()
                       product.purchase=document["purchase"].toString()
                       product.selling=document["selling_price"].toString()
                       product.stock=document["total_stock"].toString()
                       allProducts.add(product)
                       products[i]=product
                    }
                    if(repository.insertProduct(products))
                        Log.i("INSERT","success")
                    else
                        Log.i("INSERT","failed")

                }

            }
        }
    }
}
