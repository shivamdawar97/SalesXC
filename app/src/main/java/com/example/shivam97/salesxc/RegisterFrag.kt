package com.example.shivam97.salesxc


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.f_register.*
import kotlinx.android.synthetic.main.f_register.view.*

class RegisterFragment : Fragment() {

    lateinit var v:View
    lateinit var cname :String;lateinit var gstin :String;lateinit var oname :String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         v=inflater.inflate(R.layout.f_register, container, false)
        return v
    }
     fun checkValues(): Boolean {
        cname=v.company_name.text.toString()
         gstin=v.gst.text.toString()
         oname=v.owner.text.toString()

        return false
    }

}
