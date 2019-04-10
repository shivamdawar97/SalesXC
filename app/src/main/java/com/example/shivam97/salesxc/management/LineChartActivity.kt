package com.example.shivam97.salesxc.management

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.shivam97.salesxc.R
import kotlinx.android.synthetic.main.activity_line_chart.*

class LineChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)
        val adapter= LineChartAdapter(line_chart)
        adapter.setData()
    }
}
