package com.example.shivam97.salesxc;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SalesXC extends Application {
   public static FirebaseAuth mAuth;
   public static FirebaseUser mUser;
   public FirebaseFirestore firestore;

   private static SalesXC INSTANCE=null;
    @Override
    public void onCreate() {
        super.onCreate();
        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
    }

    public static SalesXC getINSTANCE() {
        if(INSTANCE==null){
            INSTANCE=new SalesXC();
        }

        return INSTANCE;
    }


}
