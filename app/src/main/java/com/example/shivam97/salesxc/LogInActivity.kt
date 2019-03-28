package com.example.shivam97.salesxc

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.shivam97.salesxc.AccSetup.SetupPagersAct
import com.example.shivam97.salesxc.SalesXC.mAuth
import com.example.shivam97.salesxc.orderClasses.MainActivity
import kotlinx.android.synthetic.main.a_login.*

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_login)

        if(mAuth.currentUser!=null)
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        var s1:String ; var s2:String
        cardLogIn.setOnClickListener {
            startActivity( Intent(this, MainActivity::class.java))

            /*
            s1= edit_username.text.toString()
       s2=edit_passkey.text.toString()

            if(!s1.isEmpty() && !s2.isEmpty()){
                s1+="@salesxc.com"
                mAuth.signInWithEmailAndPassword(s1,s2).addOnCompleteListener {task->
                    if(task.isSuccessful)
                        startActivity(Intent(baseContext,MainActivity::class.java))
                    else
                        Toasty.error(baseContext,"Log In Failed "+task.exception?.message
                        , Toast.LENGTH_LONG,true).show()
                }
            }*/
        }


        create.setOnClickListener {
            startActivity( Intent(this, SetupPagersAct::class.java))
        }
    }
}
