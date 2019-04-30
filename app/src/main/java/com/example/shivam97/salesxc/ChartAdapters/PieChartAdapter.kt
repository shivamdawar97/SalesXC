package com.example.shivam97.salesxc.ChartAdapters

import android.graphics.Color
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PieChartAdapter (private var chart:PieChart){

    private val colors = ArrayList<Int>()
    init {
        colors.add(Color.BLACK)
        colors.add(Color.BLUE)
        colors.add(Color.GREEN)
        colors.add(Color.RED)
        colors.add(Color.MAGENTA)
    }

    fun setData(chartEnteries:ArrayList<PieEntry>){
        val dataSet= PieDataSet(chartEnteries,"Sales v Investments")
        dataSet.colors = colors
        chart.setDrawEntryLabels(true)
        chart.contentDescription="Pie Chart showing total sales and investment of the month"
        chart.data=PieData(dataSet)
        chart.invalidate()
    }
}
