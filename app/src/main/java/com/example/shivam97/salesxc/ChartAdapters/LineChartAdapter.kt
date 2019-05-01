package com.example.shivam97.salesxc.ChartAdapters

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class LineChartAdapter(private var chart: LineChart):IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return "${Math.round(value)}"
    }


    fun setData()
    {
        val entries=ArrayList<Entry>()
        //example data set
        entries.add(Entry(1f,20000f))
        entries.add(Entry(2f,0f))
        entries.add(Entry(3f,4500f))
        entries.add(Entry(4f,7000f))
        entries.add(Entry(6f,50f))
        entries.add(Entry(7f,7045f))
        entries.add(Entry(12f,9021f))
        entries.add(Entry(13f,9021f))
        entries.add(Entry(14f,9021f))
        entries.add(Entry(20f,0f))
        entries.add(Entry(21f,4500f))
        entries.add(Entry(22f,7000f))
        entries.add(Entry(24f,50f))
        entries.add(Entry(27f,7000f))
        entries.add(Entry(28f,3000f))
        entries.add(Entry(29f,3500f))
        entries.add(Entry(30f,5000f))
        setData(entries)
    }

    fun setData(chartEntries:ArrayList<Entry>){
        val dataSet =LineDataSet(chartEntries,"Sales in Rupees")
        dataSet.fillAlpha=110
        dataSet.lineWidth=2f
        dataSet.color = Color.BLUE
        dataSet.valueTextColor=Color.BLUE
        val dataSets=ArrayList<ILineDataSet>()
        dataSets.add(dataSet)
        val lineData=LineData(dataSets)
        val xAxis=chart.xAxis
        xAxis.valueFormatter=this
        chart.data=lineData
        chart.invalidate()
    }

}
