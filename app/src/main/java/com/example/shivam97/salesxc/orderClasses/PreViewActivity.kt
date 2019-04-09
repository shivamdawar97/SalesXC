package com.example.shivam97.salesxc.orderClasses

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.shivam97.salesxc.R
import kotlinx.android.synthetic.main.activity_pre_view.*

class PreViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_view)
        bill_preview_tv.text=intent.getStringExtra("bill")

    }

    fun finish(view: View){
        finish()
    }
}
