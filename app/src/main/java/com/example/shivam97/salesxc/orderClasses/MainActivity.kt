package com.example.shivam97.salesxc.orderClasses

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.transition.TransitionManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.Html
import android.text.InputType
import android.text.TextWatcher
import android.view.*
import android.widget.*
import com.example.shivam97.salesxc.BarcodeScanner
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.SalesXC.Companion.repository
import com.example.shivam97.salesxc.SalesXC.Companion.showToast
import com.example.shivam97.salesxc.SalesXC.Companion.todayDate
import com.example.shivam97.salesxc.management.AddProduct
import com.example.shivam97.salesxc.customers.AddCustomer
import com.example.shivam97.salesxc.customers.AllCustomres
import com.example.shivam97.salesxc.management.ProductsList
import com.example.shivam97.salesxc.roomClasses.Product
import kotlinx.android.synthetic.main.a_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.search_box
import java.lang.StringBuilder


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var adapter:RecyclerAdapter
    lateinit var barcodeCallback:BarcodeScanner.ScannerCallback

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
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity=Gravity.TOP

        search_items.layoutManager=LinearLayoutManager(this)

        barcodeCallback=object : BarcodeScanner.ScannerCallback {
            override fun barcodeScanned(code: String?) {
                removeAddItemFrames()
                val p= repository.getProduct(code) ?: return
                val alertDialog= AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle(p.name)
                val message= Html.fromHtml(" Enter Quantity :<br>" +
                        "<font color='#ff0000'> Stock Available:</font>"+p.stock)
                alertDialog.setMessage(message)
                val editText= EditText(this@MainActivity)
                editText.inputType= InputType.TYPE_CLASS_NUMBER
                alertDialog.setView(editText)
                alertDialog.setPositiveButton("Add") { p0, _ ->
                    val qty=editText.text.toString()
                    if(!qty.isEmpty())
                    {   if(qty.toInt()<=p.stock.toInt())
                    {
                        adapter.addItem(p.uniqueId,p.name,p.selling,qty)
                        p0.dismiss()
                    }
                    else editText.error="Quantity exceeds the stock available"
                    }
                    else editText.error="Please enter quantity"
                }
                alertDialog.setNegativeButton("Cancel"){p0,_->
                    p0.dismiss()
                }
                alertDialog.show()

            }
            override fun scannerFailed(message: String?) {
                removeAddItemFrames()
            }
        }

        fab.setOnClickListener {
            fab.isEnabled=false
            search_box.visibility=View.VISIBLE
            search_box.setOnTouchListener { v, event ->
                TransitionManager.beginDelayedTransition(search_frame)
                v.layoutParams=params
                scanner_frame.visibility=View.GONE
                (scanner_frame as FrameLayout).removeAllViews()
                search_items.visibility=View.VISIBLE
                v.setOnTouchListener(null)
                search_box.setHintTextColor(Color.DKGRAY)
                search_frame.setBackgroundColor(Color.LTGRAY)
                val searchAdapter=SearchItemAdapter(FirestoreAllProducts.allProducts)
                search_items.adapter=searchAdapter
                true
            }


            val scanner= BarcodeScanner(this@MainActivity)
            (scanner_frame as FrameLayout).addView(scanner)
            scanner_frame.visibility= View.VISIBLE


        scanner.scan(barcodeCallback,scanner_frame as FrameLayout)

        }

        FirestoreAllProducts.saveProductsToRoom()


        search_box.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                (search_items?.adapter as SearchItemAdapter).filter.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })


    }

    override fun onBackPressed() {
        when {

            drawer_layout.isDrawerOpen(GravityCompat.START) ->
            {
                drawer_layout.closeDrawer(GravityCompat.START)
            }

            search_box.visibility==View.VISIBLE->
            {
                removeAddItemFrames()
            }
            else -> super.onBackPressed()
        }
    }


    private fun removeAddItemFrames(){
        fab.isEnabled=true
        scanner_frame.visibility=View.GONE
        (scanner_frame as FrameLayout).removeAllViews()
        search_box.visibility=View.INVISIBLE
        search_items.visibility=View.INVISIBLE
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity=Gravity.CENTER
        scanner_frame.visibility=View.GONE
        search_box.layoutParams=params
        search_box.setHintTextColor(Color.WHITE)
        search_frame.setBackgroundColor(Color.TRANSPARENT)
        search_items.adapter=null

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
            {
                showToast(this,"Please Add Items");return false }

            val prevIntent=Intent(this@MainActivity,PreViewActivity::class.java)
            val prevString=StringBuilder()

            prevString.append("                     Wayne Enterprises             \n")
            prevString.append("                           $todayDate                \n")
            prevString.append("___________________________________________\n")
            prevString.append(adapter.getItemsAsString())
            prevString.append("___________________________________________\n")
            prevString.append("\n Total Amount:\t\t ${total_amount.text}")
            prevIntent.putExtra("bill",prevString.toString())
            startActivity(prevIntent)

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


    inner class SearchItemAdapter(private val allItems: ArrayList<Product>) :
            RecyclerView.Adapter<SearchItemAdapter.MyViewHolder>(),Filterable {

        private var filteredItems:ArrayList<Product> = allItems

        override fun getFilter(): Filter {

            return object : Filter() {
                override fun performFiltering(charSequence: CharSequence): FilterResults? {
                    val charString = charSequence.toString()
                    if (charString.isEmpty())
                        filteredItems = allItems
                    else {
                        val filtered = ArrayList<Product>()
                        for (product in allItems) {
                            if (product.name.toLowerCase().contains(charString.toLowerCase()) || product.uniqueId.toLowerCase().contains(charSequence)) {
                                filtered.add(product)
                            }
                        }
                        filteredItems = filtered
                    }
                    val results=FilterResults()
                    results.values=filteredItems
                    return results

                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                            filteredItems= results?.values as ArrayList<Product>
                            notifyDataSetChanged()

                }
            }
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view=layoutInflater.inflate(R.layout.search_item_card,p0,false)
         return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
        return filteredItems.size
        }

        override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
            p0.populate(p1)
        }

        inner class MyViewHolder(itemView:View?) : RecyclerView.ViewHolder(itemView!!) {
                fun populate(pos:Int){
                    var view=itemView.findViewById<TextView>(R.id.search_item_name)
                    view.text = filteredItems[pos].name
                    view=itemView.findViewById<TextView>(R.id.search_item_uid)
                    view.text = filteredItems[pos].uniqueId
                    itemView.setOnClickListener {
                        barcodeCallback.barcodeScanned(filteredItems[pos].uniqueId)
                    }

                }
        }

    }



}
