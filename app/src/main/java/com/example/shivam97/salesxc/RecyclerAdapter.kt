package com.example.shivam97.salesxc

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
        this@RecyclerAdapter.notifyItemInserted(code.size-1)

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
        holder.populate(name[p],rate[p],qty[p],p)

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
       private val nameText = itemView?.findViewById<TextView>(R.id.item_name)
       private val rateText = itemView?.findViewById<TextView>(R.id.item_rate)
       private val qtyText = itemView?.findViewById<TextView>(R.id.item_quantity)
       private val qtyEdit=itemView?.findViewById<EditText>(R.id.item_quantity_edit)
       private val priceText = itemView?.findViewById<TextView>(R.id.item_price)

        fun populate( name1:String,rate1:String,qty1:String,pos:Int){

            nameText?.text = name1
            rateText?.text = rate1
            qtyText?.text = qty1

            qtyText?.setOnClickListener {
                qtyText.visibility=View.INVISIBLE
                qtyEdit?.visibility=View.VISIBLE
                qtyEdit?.requestFocus()
            }

            qtyEdit?.setOnEditorActionListener { _, actionId, _ ->
                if(actionId ==EditorInfo.IME_ACTION_DONE){
                    qtyText?.visibility=View.VISIBLE
                    qtyText?.text=qtyEdit.text
                    qty[pos]=qtyEdit.text.toString()
                    qtyEdit.visibility=View.INVISIBLE

                }
                 false
            }

            try {
                val prc= rate1.toFloat()*qty1.toFloat()
                priceText?.text=prc.toString()
                priceList[pos]=prc
            }catch (e:NumberFormatException){

            }
            catch (e:IndexOutOfBoundsException){

            }



        }

    }

}
