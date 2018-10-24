package com.example.shivam97.salesxc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.a_setup_pagers.*
import java.util.ArrayList

class SetupPagersAct : AppCompatActivity() {

    lateinit var fragments: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_setup_pagers)
        fragments=ArrayList()
        fragments.add(RegisterFragment())
        fragments.add(PhoneFrag())
        fragments.add(DetailsFrag())
        pager.adapter=ViewPagerAdapter(supportFragmentManager)

        buttonNext.setOnClickListener {
            val i=pager.currentItem
            when (i) {
                0->{
                    buttonNext.background=getDrawable(R.drawable.round_bg_full)
                    buttonNext.isEnabled=false
                        (fragments[0] as RegisterFragment).checkValues()
                }
                1 -> {
                    buttonNext.background=getDrawable(R.drawable.round_bg_full)
                    buttonNext.isEnabled=false
                    (fragments[1] as PhoneFrag).goNext(buttonNext)
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
