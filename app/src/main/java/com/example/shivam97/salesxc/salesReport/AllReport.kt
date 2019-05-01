package com.example.shivam97.salesxc.salesReport

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.shivam97.salesxc.ChartAdapters.BarChartAdapter
import com.example.shivam97.salesxc.ChartAdapters.PieChartAdapter
import com.example.shivam97.salesxc.R
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_all_report.*

class AllReport : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_report)

        val collection= FirebaseFirestore.getInstance().collection("test")
        pie_sales_via_store.centerText="Revenue"
        pie_sales_via_store.setCenterTextSize(18f)
        val barChartAdapter= BarChartAdapter(bar_store_sales)
        val pieChartAdapter= PieChartAdapter(pie_sales_via_store)
        var totalRevenue:Float
        var shop1Revenue:Float
        var shop2Revenue:Float
        val pieChartEntries=ArrayList<PieEntry>()
        val barEntries1=ArrayList<BarEntry>()
        val barEntries2=ArrayList<BarEntry>()
        collection.document("shop1").get().addOnSuccessListener {
            shop1->
            collection.document("shop2").get().addOnSuccessListener {
                shop2->
                val d1=shop1["total_sales"].toString().toFloat()
                val d2=shop2["total_sales"].toString().toFloat()
                val d3=shop1["total_invest"].toString().toFloat()
                val d4=shop2["total_invest"].toString().toFloat()
                barEntries1.add(BarEntry(1f,d1))
                barEntries1.add(BarEntry(2f,d3))
                barEntries2.add(BarEntry(1f,d2))
                barEntries2.add(BarEntry(2f,d4))
                shop1Revenue=d1-d3
                shop2Revenue=d2-d4
                shop1_revenue.text=" Shop1 Revenue :$shop1Revenue"
                shop2_revenue.text=" Shop2 Revenue :$shop2Revenue"
                totalRevenue=shop1Revenue+shop2Revenue
                pieChartEntries.add(PieEntry(shop1Revenue,"Shop1"))
                pieChartEntries.add(PieEntry(shop2Revenue,"Shop2"))
                barChartAdapter.setData(barEntries1,barEntries2)
                pieChartAdapter.setData(pieChartEntries)
            }
        }

    }

    fun finishActivity(v: View){
        finish()
    }


}


