package com.example.shivam97.salesxc.orderClasses

import com.example.shivam97.salesxc.SalesXC
import com.example.shivam97.salesxc.SalesXC.customers

class FireStoreAllCustomers
{
    companion object {

        fun saveCustomersToRoom() {
            val cusDocs = SalesXC.docReference.collection("Customers")
            cusDocs.get().addOnSuccessListener {
                if (it != null) {
                    for(document in it){
                        customers.add(document.id)
                    }
                }

            }
        }
    }
}