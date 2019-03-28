package com.example.shivam97.salesxc

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import java.io.IOException

class BarcodeScanner(private val ctx: Context) : ConstraintLayout(ctx) {
    private var surfaceView: SurfaceView? = null
    private var detector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null
    private var scannerView:View?=null
    private var containerView:ViewGroup?=null

    init {
      scannerView= inflate(ctx,R.layout.barcode_scanner,this)
      containerView= ((ctx as Activity).findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        containerView?.addView(scannerView)
        scannerView?.findViewById<View>(R.id.scanner_fab_close)?.setOnClickListener {
            stopScanning()
        }

    }

    interface ScannerCallback {

        fun barcodeScanned(code: String)
        fun scannerFailed(message: String)
    }


    fun startScan(callback: ScannerCallback) {

      /*  val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_QR_CODE,
                        FirebaseVisionBarcode.FORMAT_AZTEC)
                .build()*/

        surfaceView = this.findViewById(R.id.sv_barcode)
        surfaceView!!.setZOrderMediaOverlay(true)

        detector = BarcodeDetector.Builder(ctx)
                .build()

        if (!detector!!.isOperational) {
            callback.scannerFailed("Sorry!,Couldn't setup the barcode detector")

        }
        cameraSource = CameraSource.Builder(ctx, detector!!)
                .setRequestedPreviewSize(1920, 1024)
                .setRequestedFps(24f)
                .setAutoFocusEnabled(true)
                .build()

        surfaceView!!.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                if (ContextCompat.checkSelfPermission(ctx,
                                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource!!.start(surfaceView!!.holder)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        stopScanning()
                        callback.scannerFailed(e.message!!)
                    }

                } else {
                    ActivityCompat.requestPermissions(ctx as Activity,
                            arrayOf(Manifest.permission.CAMERA), 10)
                }
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                stopScanning()
            }
        })

        detector!!.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {

                val barcodes = detections.detectedItems
                if (barcodes.size() > 0) {
                    val thisCode = barcodes.valueAt(0)
                    //callback
                    Toast.makeText(ctx,thisCode.displayValue,Toast.LENGTH_LONG).show()
                    callback.barcodeScanned(thisCode.displayValue)
                }

            }

        })

    }


    fun stopScanning() {
        cameraSource?.stop()
        detector?.release()
        scannerView?.visibility= View.GONE

    }
}
