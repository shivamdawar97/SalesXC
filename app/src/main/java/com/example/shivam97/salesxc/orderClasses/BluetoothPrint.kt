package com.example.shivam97.salesxc.orderClasses

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Method
import java.nio.charset.Charset
import java.util.UUID


class BluetoothPrint(private val context: Context) {
    private var socket: BluetoothSocket? = null
    private var bluetoothDevice: BluetoothDevice? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private var readBuffer: ByteArray? = null
    private var readBufferPosition: Int = 0
    @Volatile
    private var stopWorker: Boolean = false
    private var value = ""


    init {
        initPrint()
    }

    private fun initPrint() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        try {
            if (!bluetoothAdapter.isEnabled) {
                val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                (context as PreViewActivity).startActivityForResult(enableBluetooth, 0)
            }

            val pairedDevices = bluetoothAdapter.bondedDevices

            if (pairedDevices.size > 0) {
                for (device in pairedDevices) {
                    if (device.name == "MTP-3")
                    //Note, you will need to change this to match the name of your device
                    {
                        bluetoothDevice = device
                        break
                    }
                }

                val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") //Standard SerialPortService ID
                socket = bluetoothDevice!!.createRfcommSocketToServiceRecord(uuid)
                bluetoothAdapter.cancelDiscovery()
                socket!!.connect()
                outputStream = socket!!.outputStream
                inputStream = socket!!.inputStream
                beginListenForData()
            } else {
                value += "No Devices found"
                Toast.makeText(context, value, Toast.LENGTH_LONG).show()

            }
        } catch (ex: Exception) {
            value += "$ex\n InitPrinter \n"
            Toast.makeText(context, value, Toast.LENGTH_LONG).show()
        }

    }

    private fun beginListenForData() {

        try {

            val handler = Handler()

            // this is the ASCII code for a newline character
            val delimiter: Byte = 10

            stopWorker = false
            readBufferPosition = 0
            readBuffer = ByteArray(1024)

            // specify US-ASCII encoding
            // tell the user data were sent to bluetooth printer device
            val workerThread = Thread(Runnable {
                while (!Thread.currentThread().isInterrupted && !stopWorker) {

                    try {

                        val bytesAvailable = inputStream!!.available()

                        if (bytesAvailable > 0) {

                            val packetBytes = ByteArray(bytesAvailable)
                            inputStream!!.read(packetBytes)

                            for (i in 0 until bytesAvailable) {

                                val b = packetBytes[i]
                                if (b == delimiter) {

                                    val encodedBytes = ByteArray(readBufferPosition)
                                    System.arraycopy(
                                            readBuffer!!, 0,
                                            encodedBytes, 0,
                                            encodedBytes.size
                                    )

                                    // specify US-ASCII encoding
                                    val data = String(encodedBytes, Charset.forName("US-ASCII"))
                                    readBufferPosition = 0

                                    // tell the user data were sent to bluetooth printer device
                                    handler.post { Log.d("Bluetooth_Error", data) }

                                } else {
                                    readBuffer!![readBufferPosition++] = b
                                }
                            }
                        }

                    } catch (ex: IOException) {
                        stopWorker = true
                    }

                }
            })

            workerThread.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    internal fun printData( txt: String) = try {
        var text = txt.replace("₹", "Rs.").replace("                     ","          ")
        text += "\n\n\n\n"
        val buffer = text.toByteArray()
        outputStream!!.write(buffer, 0, buffer.size)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    @Throws(IOException::class)
    internal fun disconnectBT() {
        try {
            stopWorker = true
            outputStream!!.close()
            inputStream!!.close()
            socket!!.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

}
