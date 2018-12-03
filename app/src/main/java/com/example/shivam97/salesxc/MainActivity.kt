package com.example.shivam97.salesxc
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.example.shivam97.salesxc.Management.AddProduct
import com.example.shivam97.salesxc.SalesXC.repository
import kotlinx.android.synthetic.main.a_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

     var totalPrice:Float = 0.0f
    companion object {
        var priceList: ArrayList<Float> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val adapter=RecyclerAdapter(this,ArrayList(), ArrayList(),ArrayList(),ArrayList())
        main_recycler.adapter=adapter
        main_recycler.layoutManager=LinearLayoutManager(this)

        fab.setOnClickListener {

            val scanner=BarcodeScanner(this@MainActivity)
            (scanner_frame as FrameLayout).addView(scanner)
        scanner_frame.visibility= View.VISIBLE
        scanner.scan(object :BarcodeScanner.ScannerCallback{
            override fun barcodeScanned(code: String?) {

                (scanner_frame as FrameLayout).removeAllViews()
                val p= repository.getProduct(code) ?: return
                adapter.addItem(p.uniqueId,p.name,p.selling,""+0)
               /* val alertDialog=AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle(p.name)
                alertDialog.setMessage("Quantity:")
                val editText=EditText(this@MainActivity)
                editText.inputType=InputType.TYPE_CLASS_NUMBER
                alertDialog.setView(editText)
                alertDialog.setPositiveButton("Add") { p0, _ ->
                   val qty=editText.text.toString()
                    if(!qty.isEmpty())
                    {   adapter.addItem(p.uniqueId,p.name,p.selling,qty)
                        p0.dismiss()
                    }

                }
                alertDialog.setNegativeButton("Cancel"){p0,_->
                    p0.dismiss()
                }
                alertDialog.show()*/

            }
            override fun scannerFailed(message: String?) {
            }
        },scanner_frame as FrameLayout)
        }

    }


    override fun onBackPressed() {
        when {

            drawer_layout.isDrawerOpen(GravityCompat.START) ->
                drawer_layout.closeDrawer(GravityCompat.START)

            scanner_frame.visibility==View.VISIBLE ->
                scanner_frame.visibility=View.GONE

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
        return when (item.itemId) {
            R.id.action_settings -> true
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

            }
            R.id.nav_update_stock -> {

            }
            R.id.nav_manage_stock -> {

            }
            R.id.nav_bills -> {

            }
            R.id.nav_total_sales -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    fun onPriceListUpdated(){
        Toast.makeText(this@MainActivity,"called",Toast.LENGTH_LONG).show()
    }

}
