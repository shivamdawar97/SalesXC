package com.example.shivam97.salesxc.orderClasses

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.orderClasses.MainActivity.Companion.priceList
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException

class RecyclerAdapter(private val ctx:Context,
                      private val codes:ArrayList<String>,
                      private val names:ArrayList<String>,
                      private val rates:ArrayList<String>,
                      private val qtys:ArrayList<String>)
    : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {


    fun addItem(c:String,n:String,r:String,q:String){
        codes.add(c);names.add(n);rates.add(r);qtys.add(q)
        this@RecyclerAdapter.notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_card, parent, false)
        priceList.add(0.0f)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return codes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val p=holder.adapterPosition
        holder.populate(codes[p],names[p],rates[p],qtys[p],p)
        holder.qty?.requestFocus()
    }

   inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val remv = itemView?.findViewById<TextView>(R.id.item_remove)
        val name = itemView?.findViewById<TextView>(R.id.item_name)
        private val rate = itemView?.findViewById<TextView>(R.id.item_rate)
        val qty = itemView?.findViewById<TextView>(R.id.item_quantity)
        private val price = itemView?.findViewById<TextView>(R.id.item_price)

        fun populate(code1:String, name1:String,rate1:String,qty1:String,p:Int){
            name?.text = name1
            rate?.text = rate1
            qty?.text = qty1

            try {
                val prc= rate1.toFloat()*qty1.toFloat()
                price?.text=prc.toString()
                priceList[p]=prc
            }catch (e:NumberFormatException){

            }
            catch (e:IndexOutOfBoundsException){

            }
            remv?.setOnClickListener {
                try {
                    codes.removeAt(p);names.removeAt(p);rates.removeAt(p)
                    qtys.removeAt(p);priceList.removeAt(p)
                    notifyItemRemoved(p)
                    this@RecyclerAdapter.notifyItemRangeChanged(p,itemCount)

                }catch (e:IndexOutOfBoundsException){}


            }


        }

    }


}
