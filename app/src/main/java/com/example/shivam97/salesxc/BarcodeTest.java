package com.example.shivam97.salesxc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;
import es.dmoral.toasty.Toasty;

public class BarcodeTest extends AppCompatActivity {

    CameraSource cameraSource;
    SurfaceView surfaceView;
    BarcodeDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_barcode_test);
        final TextView txtView = findViewById(R.id.txtContent);
        surfaceView = findViewById(R.id.sv_barcode);
        surfaceView.setZOrderMediaOverlay(true);

        detector = new BarcodeDetector.Builder(this)
                        .build();

        if(!detector.isOperational()){
            Toasty.error(BarcodeTest.this,"Sorry!,Couldn't setup the detector"
            ,Toast.LENGTH_LONG).show();
            this.finish();
        }
        cameraSource = new CameraSource.Builder(BarcodeTest.this, detector)
                .setRequestedPreviewSize(1920, 1024)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ContextCompat.checkSelfPermission(BarcodeTest.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(BarcodeTest.this,
                            new String[]{Manifest.permission.CAMERA}, 10);
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2){}

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release(){}
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                final Barcode thisCode = barcodes.valueAt(0);
                txtView.post(new Runnable() {
                    @Override
                    public void run() {
                        txtView.setText(thisCode.displayValue);
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else Toasty.warning(BarcodeTest.this,"Please Grant Camera Permission"
                ,Toast.LENGTH_LONG,true).show();
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detector.release();
        cameraSource.stop();
        cameraSource.release();


    }
}
