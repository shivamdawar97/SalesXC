package com.example.shivam97.salesxc

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.shivam97.salesxc.FirstFragment
import com.example.shivam97.salesxc.R
import kotlinx.android.synthetic.main.activity_setup_pagers.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.ArrayList

class SetupPagers : AppCompatActivity() {

    lateinit var fragments: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_pagers)
        fragments=ArrayList()
        fragments.add(FirstFragment())
        pager.adapter=ViewPagerAdapter(supportFragmentManager)

        buttonNext.setOnClickListener {
            val i=pager.currentItem
            when (i) {
                0 -> {
                    buttonNext.setBackgroundColor(Color.parseColor("#50BFE6"));
                    buttonNext.isEnabled=false
                    (fragments[0] as FirstFragment).goNext(buttonNext)

                }

            }
        }
    }

    inner class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}
