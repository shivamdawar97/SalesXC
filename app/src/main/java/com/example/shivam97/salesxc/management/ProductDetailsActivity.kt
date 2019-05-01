package com.example.shivam97.salesxc.management

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import com.example.shivam97.salesxc.ChartAdapters.LineChartAdapter
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.SalesXC
import com.example.shivam97.salesxc.SalesXC.Companion.currentMonth
import com.example.shivam97.salesxc.SalesXC.Companion.docReference
import com.example.shivam97.salesxc.SalesXC.Companion.getMonth
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var allMonthSale: MutableMap<String, Any>
    private lateinit var chartAdapter: LineChartAdapter
    private lateinit var chartEntries: ArrayList<Entry>
    private var productId="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        productId=intent.getStringExtra("uid")
        val pDoc=docReference.collection("Products").document(productId)
        pDoc.get().addOnSuccessListener {
            product_details_name.text=it.id
            product_details_uniqueId.text=it["uid"].toString()
            product_details_purchasing.text=it["purchase"].toString()
            product_details_selling.text=it["selling_price"].toString()
            product_details_stock_available.text=it["total_stock"].toString()

           try {

               val expiries= TreeMap<String,Int>(it["expiry_date"] as HashMap<String, Int>)
               //using tree map for sorted order
               val dateFormat=SimpleDateFormat("dd - MMM - yy")
               val sBuilder=StringBuilder()
               for( m in expiries){
                   val date=dateFormat.format(m.key.toLong())
                   sBuilder.append("${m.value} items will expire on $date")
                   sBuilder.append("\n\n")
               }
               Log.i("MY_MAP",sBuilder.toString())
               product_details_expiries.text=sBuilder.toString()

           }catch ( e:TypeCastException ){
               e.printStackTrace()
               Log.e("MY_MAP","ERROR :"+e.message)
           }

        }

        product_sales_chart.setOnTouchListener { v, event ->
            when(event.action){

                    MotionEvent.ACTION_DOWN->
                        nested_scroll_details.isEnabled=false

                    MotionEvent.ACTION_UP->
                        nested_scroll_details.isEnabled=true
            }
             false

        }

        nested_scroll_details.setOnTouchListener { v, event ->

            product_sales_chart.parent.requestDisallowInterceptTouchEvent(false)
            false
        }

        product_sales_chart.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }

        pDoc.collection("Report").document("AllMonth").get().addOnSuccessListener {
            if(it.data!=null)
            allMonthSale=it.data!!
        }


        months_spinner.setSelection(getMonth(currentMonth))
        chartEntries=ArrayList()
        chartAdapter=LineChartAdapter(product_sales_chart)
        pDoc.collection("Report").document(currentMonth).get().addOnSuccessListener { snap->
            if(snap.data==null)
            {
                total_sale_textView.text="--"
                return@addOnSuccessListener
            }
            populateChart(snap.data!!)
            try {

                total_sale_textView.text=allMonthSale[currentMonth].toString()
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }

        months_spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pDoc.collection("Report").document(getMonth(position)).get().addOnSuccessListener {
                    snap->
                    if(snap.data==null)
                    {
                        total_sale_textView.text="--"
                        return@addOnSuccessListener
                    }
                    populateChart(snap.data!!)
                    total_sale_textView.text=allMonthSale[getMonth(position)].toString()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun populateChart(givenData: Map<String, Any>) {
        chartEntries.clear()
        val sortedData=TreeMap<Float,Float>()
        (HashMap<String,Any>(givenData)).forEach {
            data ->
            val dataMap=data.value as HashMap<String,Long>
            val d1=data.key.toFloat()
            val d2=dataMap["sale"]?.toFloat()!!
            sortedData[d1]=d2
//            chartEntries.add(Entry(d1,d2))
        }
        sortedData.forEach {
            chartEntries.add(Entry(it.key,it.value))
        }
        chartAdapter.setData(chartEntries)
    }


    fun finish(v: View){
        finish()
    }
}
