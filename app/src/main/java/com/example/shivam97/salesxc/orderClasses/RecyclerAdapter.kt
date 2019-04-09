package com.example.shivam97.salesxc.orderClasses

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.shivam97.salesxc.R
import java.lang.IndexOutOfBoundsException

class RecyclerAdapter(private val ctx:Context)
    : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    private val orderProducts=ArrayList<OrderProducts>()
    private var gTotal=0.0f
    private fun addToGTotal(amt:Float){
        gTotal = gTotal.plus(amt)
        (ctx as MainActivity).updateTAmount(gTotal)
    }
    private fun rmvFromGTotal(amt: Float){
        gTotal= gTotal.minus(amt)
        (ctx as MainActivity).updateTAmount(gTotal)
    }

    fun addItem(c:String,n:String,r:String,q:String){
        Log.e("ERROR_SALES", "$r:$q")
        val product=OrderProducts(c,n,r.toFloat(),q.toFloat())
        orderProducts.add(product)
        addToGTotal(product.total)
        this@RecyclerAdapter.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderProducts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val p=holder.adapterPosition
        holder.populate(p)
    }

   inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val rmv = itemView?.findViewById<TextView>(R.id.item_remove)
        private val name = itemView?.findViewById<TextView>(R.id.item_name)
        private val rate = itemView?.findViewById<TextView>(R.id.item_rate)
        private val qty = itemView?.findViewById<TextView>(R.id.item_quantity)
        private val price = itemView?.findViewById<TextView>(R.id.item_price)

        fun populate(i:Int){
            val p=orderProducts[i]
            name?.text = p.name
            rate?.text = p.rate.toString()
            qty?.text = p.quantity.toString()
            price?.text=p.total.toString()
            rmv?.setOnClickListener {
                try {
                    orderProducts.removeAt(i)
                    rmvFromGTotal(p.total)
                    notifyItemRemoved(i)
                    this@RecyclerAdapter.notifyItemRangeChanged(i,itemCount)
                }
                catch (e:IndexOutOfBoundsException){}
            }
        }
    }

    fun getItemsAsString():String{
        val itemString=StringBuilder()
        itemString.append("\n   Name     Rate          Quantity        Total\n")
        for(pdt in orderProducts)
        itemString.append("\n   ${pdt.name}     ${pdt.rate}     X     ${pdt.quantity}       =     ${pdt.total} \n")
        return itemString.toString()

    }
}
