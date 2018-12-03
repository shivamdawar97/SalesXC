package com.example.shivam97.salesxc

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.shivam97.salesxc.MainActivity.Companion.priceList
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException

class RecyclerAdapter(private val ctx:Context,
                      private val code:ArrayList<String>,
                      private val name:ArrayList<String>,
                      private val rate:ArrayList<String>,
                      private val qty:ArrayList<String>)
    : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {


    fun addItem(c:String,n:String,r:String,q:String){
        code.add(c);name.add(n);rate.add(r);qty.add(q)
        this@RecyclerAdapter.notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_card, parent, false)
        priceList.add(0.0f)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return code.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val p=holder.adapterPosition
        holder.populate(code[p],name[p],rate[p],qty[p],p)
        holder.qty?.requestFocus()
        holder.remv?.setOnClickListener {
            try {
                code.removeAt(p);name.removeAt(p);rate.removeAt(p)
                qty.removeAt(p);priceList.removeAt(p)
                this.notifyItemRemoved(p)

            }catch (e:IndexOutOfBoundsException){}


        }
    }

   inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val remv = itemView?.findViewById<TextView>(R.id.item_remove)
        val code = itemView?.findViewById<EditText>(R.id.item_code)
        val name = itemView?.findViewById<EditText>(R.id.item_name)
        private val rate = itemView?.findViewById<TextView>(R.id.item_rate)
        val qty = itemView?.findViewById<EditText>(R.id.item_quantity)
        private val price = itemView?.findViewById<TextView>(R.id.item_price)

        fun populate(code1:String, name1:String,rate1:String,qty1:String,pos:Int){

            code?.setText(code1)
            name?.setText(name1)
            rate?.text = rate1
            qty?.setText(qty1)

            qty?.addTextChangedListener(Watcher(pos))

            try {
                val prc= rate1.toFloat()*qty1.toFloat()
                price?.text=prc.toString()
                priceList[pos]=prc
            }catch (e:NumberFormatException){

            }
            catch (e:IndexOutOfBoundsException){

            }



        }

    }


    inner class Watcher(private val position: Int) :TextWatcher{
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            try {
                val q=p0.toString().toFloat()
                val prc=rate[position].toFloat() * q
                priceList[position]=prc
                (ctx as MainActivity).onPriceListUpdated()
            }
            catch (e:NumberFormatException ){

            }

        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
        override fun afterTextChanged(p0: Editable?) {
        }

    }

}
