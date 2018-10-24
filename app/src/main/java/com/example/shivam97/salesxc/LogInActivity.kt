package com.example.shivam97.salesxc

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.a_login.*

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_login)

        cardLogIn.setOnClickListener {
            finish()
        }
        create.setOnClickListener {
            startActivity( Intent(this, SetupPagersAct::class.java))
        }
    }
}
