package com.example.shivam97.salesxc.ChartAdapters

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class BarChartAdapter(private var barChart:BarChart) {

    init {

        val shops = arrayOf("Shop1", "Shop2")
        val xAxis=barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(shops)
        xAxis.setCenterAxisLabels(true)
        xAxis.position=XAxis.XAxisPosition.BOTTOM
        xAxis.granularity=1f
        xAxis.isGranularityEnabled=true

        barChart.isDragEnabled=true
        barChart.setVisibleXRangeMaximum(2f)


    }

    fun setData(chartEntries1:ArrayList<BarEntry>,chartEntries2:ArrayList<BarEntry>){
        val dataSet1= BarDataSet(chartEntries1,"Sales")
        val dataSet2= BarDataSet(chartEntries2,"Return")
        dataSet1.color = Color.BLUE
        dataSet2.color= Color.BLACK

        val barData= BarData(dataSet1,dataSet2)
        barChart.data=barData
        barData.barWidth=0.15f

        val barSpace=0.1f
        val groupSpace=0.5f

        barChart.xAxis.axisMinimum=0f
        barChart.axisLeft.axisMaximum=0f
        barChart.groupBars(0f,groupSpace,barSpace)

        barChart.invalidate()


    }

}