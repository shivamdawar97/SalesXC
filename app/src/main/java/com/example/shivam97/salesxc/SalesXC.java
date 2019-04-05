package com.example.shivam97.salesxc;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.shivam97.salesxc.roomClasses.Repository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SalesXC extends Application {
   public static FirebaseAuth mAuth;
   public static FirebaseUser mUser;
   public static Repository repository;
    private static AlertDialog progressDialog;
   public static DocumentReference docReference;
   public static ArrayList<String> customers;
    @Override
    public void onCreate() {
        super.onCreate();
        customers=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        repository=new Repository(this);
        docReference=FirebaseFirestore.getInstance().collection("test")
                .document("shop1");
    }


    public static void showProgressDialog(Context context)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        // View v=LayoutInflater.from(context).inflate(R.layout.global_progress,null);
        builder.setView(new ProgressBar(context));
        progressDialog =builder.create();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        try { progressDialog.show(); }
        catch (WindowManager.BadTokenException e){ e.printStackTrace(); }

    }

    public static void hideProgressDialog(){
        progressDialog.dismiss();
    }


}
