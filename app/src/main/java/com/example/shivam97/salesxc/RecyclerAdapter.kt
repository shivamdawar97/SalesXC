package com.example.shivam97.salesxc

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class RecyclerAdapter(private val ctx:Context) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
     var count: Int = 0
    fun increaseInt() {
        count++
        this.notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return count
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.populate(position)
        holder.remv?.setOnClickListener {
            count--
            this.notifyItemRemoved(holder.adapterPosition)
        }
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val remv = itemView?.findViewById<TextView>(R.id.item_remove)
        val code = itemView?.findViewById<EditText>(R.id.item_code)
        val name = itemView?.findViewById<EditText>(R.id.item_name)
        private val rate = itemView?.findViewById<TextView>(R.id.item_rate)
        private val qty = itemView?.findViewById<EditText>(R.id.item_quantity)
        private val price = itemView?.findViewById<TextView>(R.id.item_price)

        fun populate(c:Int){

            code?.setText(c.toString())
            name?.setText("GAME")
            rate?.text = c.toString()
            qty?.setText(c.toString())

            val prc:Int=c*c
            price?.text=prc.toString()
        }



    }

}
