package com.example.shivam97.salesxc.roomClasses

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import org.json.JSONObject



class MySharedPreferences(context: Context) {
    val c=context
    lateinit var s:SharedPreferences
    private val productKey="products"
    private val stockKey="stocks"

    fun getProduct(name :String):JSONObject{
         s= c.getSharedPreferences(productKey,MODE_PRIVATE)
        val resultString=s.getString(name,null)
        return JSONObject(resultString)
    }

    fun saveProduct(name: String,data:JSONObject){
        s= c.getSharedPreferences(productKey,MODE_PRIVATE)
        val jsonString = data.toString()
        s.edit().putString(name, jsonString).apply()
    }

    fun getStock(name: String):JSONObject{
        s= c.getSharedPreferences(stockKey,MODE_PRIVATE)
        val resultString=s.getString(name,null)
        return JSONObject(resultString)
    }

    fun saveStock(name: String,data:JSONObject){
        s= c.getSharedPreferences(stockKey,MODE_PRIVATE)
        val jsonString = data.toString()
        s.edit().putString(name, jsonString).apply()
    }



}
