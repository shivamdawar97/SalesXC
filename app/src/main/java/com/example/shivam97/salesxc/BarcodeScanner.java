package com.example.shivam97.salesxc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class BarcodeScanner extends LinearLayout {

   public CameraSource cameraSource;
   public SurfaceView surfaceView;
   public BarcodeDetector detector;
   Context context;

    public interface ScannerCallback{

        void barcodeScanned(String code);
        void scannerFailed(String message);
    }
/*

    public BarcodeScanner(Context context,BarcodeScanner.ScannerCallback callback) {
        super(context);
        this.callback=callback;
        initiateLayout(context);
    }
*/

    public BarcodeScanner(Context context) {
        super(context);
        initiateLayout(context);
    }

    public BarcodeScanner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initiateLayout(context);
    }

    public BarcodeScanner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initiateLayout(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BarcodeScanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initiateLayout(context);
    }

    public void initiateLayout(Context context){
        this.context=context;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.barcode_scanner,this);

    }

    public void scan(final BarcodeScanner.ScannerCallback callback, final FrameLayout scannerFrame) {
        onFinishInflate();

        scannerFrame.setVisibility(VISIBLE);
        surfaceView = this.findViewById(R.id.sv_barcode);
        surfaceView.setZOrderMediaOverlay(true);

        detector = new BarcodeDetector.Builder(context)
                .build();

        if(!detector.isOperational()){
            callback.scannerFailed("Sorry!,Couldn't setup the detector");
        }
        cameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(1920, 1024)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.scannerFailed(e.getMessage());
                    }
                } else {
                    ActivityCompat.requestPermissions((Activity) context,
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


                SparseArray<Barcode> barcodes  = detections.getDetectedItems();
                if(barcodes.size()>0) {
                    final Barcode thisCode = barcodes.valueAt(0);
                    //callback
                    detector.release();
                    scannerFrame.post(new Runnable() {
                       @Override
                       public void run() {
                           scannerFrame.setVisibility(GONE);
                           callback.barcodeScanned(thisCode.displayValue);
                       }
                   });
                }

            }

        });
    }

}
