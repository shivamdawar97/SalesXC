package com.example.shivam97.salesxc.Management;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.shivam97.salesxc.BarcodeScanner;
import com.example.shivam97.salesxc.R;
import com.example.shivam97.salesxc.roomClasses.Product;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

import static com.example.shivam97.salesxc.SalesXC.repository;

public class AddProduct extends AppCompatActivity {

    EditText editName, editUniqueId,editPurchase, editSelling, editStock;
    FrameLayout scannerFrame;
    BarcodeScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_add_product);
        editName =findViewById(R.id.name_edit);
        editUniqueId =findViewById(R.id.unique_id_edit);
        editPurchase=findViewById(R.id.purchase_edit);
        editSelling =findViewById(R.id.selling_edit);
        editStock =findViewById(R.id.stock_edit);
        scannerFrame= findViewById(R.id.scanner_frame);

    }

    public void scanBarcode(View view) {
        scanner= new BarcodeScanner(AddProduct.this);
        scannerFrame.addView(scanner);
        scannerFrame.setVisibility(View.VISIBLE);
        scanner.scan(new BarcodeScanner.ScannerCallback() {
            @Override
            public void barcodeScanned(String code) {
                editUniqueId.setText(code);
            }

            @Override
            public void scannerFailed(String message) {
                Toast.makeText(AddProduct.this,message,Toast.LENGTH_LONG).show();
            }
        }, scannerFrame);
    }

    public void saveProduct(View view) {
        String name,uid,purchase,selling,stock;
        name=editName.getText().toString();
        uid=editUniqueId.getText().toString();
        purchase=editPurchase.getText().toString();
        selling=editSelling.getText().toString();
        stock=editStock.getText().toString();

        if(name.isEmpty() || uid.isEmpty() || purchase.isEmpty() || selling.isEmpty() || stock.isEmpty()){

        }
        else
        {
            Product product=new Product();
            product.setName(name);
            product.setUniqueId(uid);
            product.setPurchase(purchase);
            product.setSelling(selling);
            product.setStock(stock);
            repository.insert(product);
            Toasty.success(AddProduct.this,"Product Addded",Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if(scannerFrame.getVisibility()==View.VISIBLE)
            scannerFrame.setVisibility(View.GONE);
        else
            super.onBackPressed();
    }

    public void finish(View view) { finish(); }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        scanner.cameraSource.start(scanner.surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else Toasty.warning(AddProduct.this,"Please Grant Camera Permission"
                        ,Toast.LENGTH_LONG,true).show();
            }
        }
    }
}
