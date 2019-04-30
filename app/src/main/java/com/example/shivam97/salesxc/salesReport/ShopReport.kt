package com.example.shivam97.salesxc.salesReport

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.SalesXC
import com.example.shivam97.salesxc.ChartAdapters.LineChartAdapter
import com.example.shivam97.salesxc.ChartAdapters.PieChartAdapter
import com.example.shivam97.salesxc.SalesXC.Companion.currentMonth
import com.example.shivam97.salesxc.SalesXC.Companion.docReference
import com.example.shivam97.salesxc.SalesXC.Companion.getMonth
import java.util.*
import kotlin.collections.ArrayList
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.activity_shop_report.*
import kotlin.collections.HashMap


class ShopReport : AppCompatActivity() {

   private lateinit var  chartEntries:ArrayList<Entry>
   private lateinit var pieChartEntries:ArrayList<PieEntry>
   private lateinit var lineChartAdapter:LineChartAdapter
   private lateinit var pieChartAdapter:PieChartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_report)
        months_spinner.setSelection(getMonth(currentMonth))
        chartEntries    =ArrayList()
        pieChartEntries =ArrayList()
        lineChartAdapter= LineChartAdapter(total_sales_line_chart)
        pieChartAdapter =PieChartAdapter(total_sales_pie_chart)


        docReference.collection("Report").document(currentMonth).get().addOnSuccessListener {
            populateCharts(it.data!!)
        }

        months_spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMonth=getMonth(position)
                docReference.collection("Report").document(selectedMonth).get().addOnSuccessListener {
                    populateCharts(it.data!!)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

  private  fun populateCharts(givenData:Map<String,Any>){
        val data= TreeMap<Float,Float>()
        pieChartEntries.clear()
        chartEntries.clear()
        total_invest_textView.text="--"
        total_sales_textView.text="--"
        (HashMap<String,Any>(givenData)).forEach {
            rawData->
            val d2=rawData.value.toString().toFloat()
            when (val d1=rawData.key) {
                "invest" -> {
                    total_invest_textView.text=" ${SalesXC.rupeeSymbol} $d2"
                    pieChartEntries.add(PieEntry(d2,"Invest"))
                }
                "total" -> {
                    total_sales_textView.text=" ${SalesXC.rupeeSymbol} $d2"
                    pieChartEntries.add(PieEntry(d2,"Sales"))
                }
                else -> {
                    data[d1.toFloat()] = d2
                }
            }
        }
        data.forEach {
            entry->
            chartEntries.add(Entry(entry.key,entry.value))
        }

        lineChartAdapter.setData(chartEntries)
        pieChartAdapter.setData(pieChartEntries)
    }

    fun finishActivity(v: View){
        finish()
    }

}
