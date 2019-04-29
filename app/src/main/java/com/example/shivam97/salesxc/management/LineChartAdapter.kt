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
        entries.add(Entry(5f,900f))
        entries.add(Entry(6f,50f))
        entries.add(Entry(7f,7045f))
        entries.add(Entry(8f,9021f))
        entries.add(Entry(9f,9021f))
        entries.add(Entry(10f,9021f))
        entries.add(Entry(11f,9021f))
        entries.add(Entry(12f,9021f))
        entries.add(Entry(13f,9021f))
        entries.add(Entry(14f,9021f))
        entries.add(Entry(15f,9021f))
        entries.add(Entry(16f,9021f))
        entries.add(Entry(17f,7000f))
        entries.add(Entry(18f,3000f))
        entries.add(Entry(19f,20000f))
        entries.add(Entry(20f,0f))
        entries.add(Entry(21f,4500f))
        entries.add(Entry(22f,7000f))
        entries.add(Entry(23f,900f))
        entries.add(Entry(24f,50f))
        entries.add(Entry(25f,7045f))
        entries.add(Entry(26f,9021f))
        entries.add(Entry(27f,7000f))
        entries.add(Entry(28f,3000f))
        entries.add(Entry(29f,3500f))
        entries.add(Entry(30f,5000f))

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

    fun setData(chartEntries:ArrayList<Entry>){
        val dataSet =LineDataSet(chartEntries,"Sales")
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
