package com.example.shivam97.salesxc;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class SalesXC extends Application {
   public static FirebaseAuth mAuth;
    @Override
    public void onCreate() {
        super.onCreate();
        mAuth=FirebaseAuth.getInstance();
    }
}
