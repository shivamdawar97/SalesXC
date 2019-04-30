package com.example.shivam97.salesxc.Bills

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.SalesXC.Companion.docReference
import com.example.shivam97.salesxc.orderClasses.PreViewActivity
import com.example.shivam97.salesxc.roomClasses.Product
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_all_bills.*
import kotlinx.android.synthetic.main.content_main.*

class AllBills : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_bills)

        val bills = ArrayList<QueryDocumentSnapshot>()
        val adapter=BillAdapter(bills)
        bill_recycler.adapter=adapter
        bill_recycler.layoutManager=LinearLayoutManager(this)
        docReference.collection("Bills").get().addOnCompleteListener {
            if(it.isSuccessful)
            {
                for(bill in it.result!!){
                    bills.add(bill)
                }
                adapter.notifyDataSetChanged()
            }
        }

    }

    fun finish(v: View){
        finish()
    }

   inner  class BillAdapter(private val bills:ArrayList<QueryDocumentSnapshot>)
       : RecyclerView.Adapter<BillAdapter.Holder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
            val view=layoutInflater.inflate(R.layout.bill_item,p0,false)
            return Holder(view)
        }

        override fun onBindViewHolder(p0: Holder, p1: Int) {
            p0.populate(p1)
        }

        override fun getItemCount(): Int {
          return  bills.size
        }

       inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
           fun populate(pos:Int){
               val data=bills[pos]
               val billNo=itemView.findViewById<TextView>(R.id.bill_no)
               val billAmt=  itemView.findViewById<TextView>(R.id.bill_amount)
               val billCusName=itemView.findViewById<TextView>(R.id.bill_cus_name)
               val billDate=itemView.findViewById<TextView>(R.id.bill_date)
               billNo.text=data["billNo"].toString()
               billAmt.text=data["amount"].toString()
               billCusName.text=data["cusName"].toString()
               billDate.text=data["date"].toString()
               itemView.setOnClickListener {
                   val prevIntent=Intent(this@AllBills,PreViewActivity::class.java)
                   prevIntent.putExtra("bill",data["data"].toString())
                   prevIntent.putExtra("total",data["amount"].toString())
                   prevIntent.putExtra("fromViewBills",true)
                   startActivity(prevIntent)
               }

           }
        }
    }

}

