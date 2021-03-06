package com.example.shivam97.salesxc.accountSetup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.widget.Toast
import com.example.shivam97.salesxc.*
import com.google.firebase.auth.AuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.a_setup_pagers.*
import java.util.*

class SetupPagersAct : AppCompatActivity() {

    lateinit var fragments: ArrayList<Fragment>
    lateinit var  credential: AuthCredential
    private val f1= RegisterFragment()
    private val f2= PhoneFrag()
    private val f3= UsernameFrag()
    lateinit var pager: NonSwipeableViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_setup_pagers)
        pager=findViewById(R.id.pager)
        fragments=ArrayList()
        fragments.add(f1)
        fragments.add(f2)
        fragments.add(f3)
        pager.adapter=ViewPagerAdapter(supportFragmentManager)

        buttonNext.setOnClickListener {

            val i=pager.currentItem
            when (i) {
                0->{

                    if(f1.checkValues()){
                        pager.currentItem+=1
                        pager.setCurrentItem(pager.currentItem,true)
                    }
                }
                1->{
                    if(!f2.getCheck())
                        f2.goNext()
                    else
                        f2.signInWithPhone()
                }

                2->{
                   f3.createAcc()
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

    fun accSetup (){
        val user = HashMap<String,Any>()
        user["GSTIN"] = f1.gstin
        user["OwnerName"] = f1.oname
        user["PhoneNumber"] = f2.phoneNumber
        user["username"]= f3.username

       FirebaseFirestore.getInstance().collection(f1.cname)
               .document("details").set(user).addOnCompleteListener {
                   task->
                   if(task.isSuccessful){

                       Toasty.success(this@SetupPagersAct,"Account Created Successfully"
                       ,Toast.LENGTH_LONG,true).show()
                       startActivity(Intent(baseContext,MainActivity::class.java))

                   }
                   else{
                       Toasty.error(this@SetupPagersAct,"Error : "+task.exception?.message
                               ,Toast.LENGTH_LONG,true).show()
                   }
               }
    }
}

