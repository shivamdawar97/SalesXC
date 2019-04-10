package com.example.shivam97.salesxc.management

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
        return "Day$value"
    }

    private val entries=ArrayList<Entry>()

    fun setData()
    {
        entries.add(Entry(0f,30f))
        entries.add(Entry(2f,20f))
        entries.add(Entry(5f,20f))
        entries.add(Entry(8f,50f))
        entries.add(Entry(10f,70f))
        entries.add(Entry(15f,90f))
        entries.add(Entry(17f,50f))
        entries.add(Entry(19f,70f))
        entries.add(Entry(20f,90f))
        entries.add(Entry(22f,50f))
        entries.add(Entry(25f,70f))
        entries.add(Entry(28f,90f))
        val dataSet =LineDataSet(entries,"Sales")
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
