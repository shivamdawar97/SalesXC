package com.example.shivam97.salesxc.orderClasses

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.example.shivam97.salesxc.BarcodeScanner
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.management.AddProduct
import com.example.shivam97.salesxc.SalesXC.repository
import com.example.shivam97.salesxc.SalesXC.showToast
import com.example.shivam97.salesxc.customers.AddCustomer
import com.example.shivam97.salesxc.customers.AllCustomres
import com.example.shivam97.salesxc.management.ProductsList
import kotlinx.android.synthetic.main.a_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var adapter:RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        adapter= RecyclerAdapter(this)
        main_recycler.adapter=adapter
        main_recycler.layoutManager=LinearLayoutManager(this)

        fab.setOnClickListener {

            val scanner= BarcodeScanner(this@MainActivity)
            (scanner_frame as FrameLayout).addView(scanner)
        scanner_frame.visibility= View.VISIBLE
        scanner.scan(object : BarcodeScanner.ScannerCallback {
            override fun barcodeScanned(code: String?) {
                (scanner_frame as FrameLayout).removeAllViews()
                val p= repository.getProduct(code) ?: return
                val alertDialog= AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle(p.name)
                alertDialog.setMessage("Enter Quantity :")
                val editText= EditText(this@MainActivity)
                editText.inputType= InputType.TYPE_CLASS_NUMBER
                alertDialog.setView(editText)
                alertDialog.setPositiveButton("Add") { p0, _ ->
                   val qty=editText.text.toString()
                    if(!qty.isEmpty())
                    {   adapter.addItem(p.uniqueId,p.name,p.selling,qty)
                        p0.dismiss()
                    }
                    else editText.error="Please enter quantity"
                }
                alertDialog.setNegativeButton("Cancel"){p0,_->
                    p0.dismiss()
                }
                alertDialog.show()

            }
            override fun scannerFailed(message: String?) {
            }
        },scanner_frame as FrameLayout)
        }

        FirestoreAllProducts.saveProductsToRoom()

    }

    override fun onBackPressed() {
        when {

            drawer_layout.isDrawerOpen(GravityCompat.START) ->
                drawer_layout.closeDrawer(GravityCompat.START)

            scanner_frame.visibility==View.VISIBLE ->
            {
                scanner_frame.visibility=View.GONE
                (scanner_frame as FrameLayout).removeAllViews()
            }

            else -> super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.itemId==R.id.action_print){

            if(adapter.itemCount==0)
            { showToast(this,"Please Add Items");return false }

            val curFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            val date =Date(System.currentTimeMillis())
            val dateString=curFormatter.format(date)
            val prevIntent=Intent(this@MainActivity,PreViewActivity::class.java)
            val prevString=StringBuilder()
            prevString.append("                     Wayne Enterprises             \n")
            prevString.append("                           $dateString                \n")
            prevString.append("___________________________________________\n")
            prevString.append(adapter.getItemsAsString())
            prevString.append("___________________________________________\n")
            prevString.append("\n Total Amount:\t\t ${total_amount.text}")
            prevIntent.putExtra("bill",prevString.toString())
            startActivity(prevIntent)
        /*    val builderSingle = AlertDialog.Builder(this@MainActivity)
            builderSingle.setTitle("Select One Name:-")
            val arrayAdapter = ArrayAdapter<String>(this@AddOrder, android.R.layout.simple_selectable_list_item)

            for ( (key,value) in item.attributes )
            {
                arrayAdapter.add("$key/ Rs. $value")
            }

            builderSingle.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            builderSingle.setAdapter(arrayAdapter) { dialog, which ->

                val strName:String = keySet.elementAt(which)
                val rate= item.attributes[strName]
                addSimpleItem(strName,rate!!)
                dialog.dismiss()
            }

            builderSingle.show()
*/
        }

        return when (item.itemId) {
            R.id.action_print -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_add_product -> {
                // Handle the camera action
                startActivity(Intent(baseContext,AddProduct::class.java))
            }
            R.id.nav_manage_product -> {
                startActivity(Intent(baseContext,ProductsList::class.java))
            }

            R.id.nav_bills -> {

            }
            R.id.nav_total_sales -> {

            }

            R.id.nav_add_customer->{
                startActivity(Intent(baseContext,AddCustomer::class.java))
            }
            R.id.nav_all_customers->{
                startActivity(Intent(baseContext,AllCustomres::class.java))

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateTAmount(amt:Float){
        total_amount.text= String.format("₹ %d",Math.round(amt))
    }


}
