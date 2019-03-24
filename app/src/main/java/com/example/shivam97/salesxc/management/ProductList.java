package com.example.shivam97.salesxc.management;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.example.shivam97.salesxc.R;
import com.example.shivam97.salesxc.roomClasses.Product;
import java.util.ArrayList;

import static com.example.shivam97.salesxc.SalesXC.repository;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_product_list);
        recyclerView=findViewById(R.id.products_recycler_View);
        search=findViewById(R.id.search_product);
        adapter=new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        search.addTextChangedListener(new SearchTextWatcher(adapter));
    }

    public void finish(View view) {
        finish();
    }

    public void scanBarcode(View view) {

    }

    private static class SearchTextWatcher implements TextWatcher {
        RecyclerAdapter adapter;

        SearchTextWatcher(RecyclerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            adapter.getFilter().filter(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //adapter.getFilter().filter(editable);

        }
    }

    private class RecyclerAdapter extends Adapter<RecyclerAdapter.ViewHolder>
                                  implements Filterable {

        private ArrayList<Product> products;
        private ArrayList<Product> filteredProducts;

        RecyclerAdapter() {
            this.products = (ArrayList<Product>) repository.getAllProducts();
            products.add(0,null);
            filteredProducts=products;
        }

        @NonNull
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view=LayoutInflater.from(ProductList.this).inflate(R.layout.product_card,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           int i=holder.getAdapterPosition();
            holder.populate(i,filteredProducts.get(i));
        }

        @Override
        public int getItemCount() {
            return filteredProducts.size();
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString=charSequence.toString();
                    if(charString.isEmpty())
                    filteredProducts=products;
                    else
                    {
                        ArrayList<Product> filtered=new ArrayList<>();
                        for(Product row:products){
                            if(row.getName().toLowerCase().contains(charString.toLowerCase())
                               || row.getUniqueId().contains(charSequence)){
                                filtered.add(row);
                            }
                        }
                        filteredProducts=filtered;
                    }
                    FilterResults results=new FilterResults();
                    results.values=filteredProducts;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    filteredProducts=(ArrayList<Product>)filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name,stock;
            ViewHolder(View itemView) {
                 super(itemView);
                 name=itemView.findViewById(R.id.product_name);
                 stock=itemView.findViewById(R.id.product_stock);
             }
             void populate(int pos,Product p)
             {
                 if(pos==0){
                     name.setText("Products");
                     stock.setText("Stock Available");
                     name.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                     stock.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                     name.setTextColor(Color.parseColor("#ffffff"));
                     stock.setTextColor(Color.parseColor("#ffffff"));

                 }
                 else {
                     name.setText(p.getName());stock.setText(p.getStock());
                 }
             }
        }
    }



}
