package com.example.shivam97.salesxc

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        cardLogIn.setOnClickListener {
            finish()
        }
        create.setOnClickListener {
            startActivity( Intent(this, SetupPagers::class.java))
        }
    }
}
