package com.example.shivam97.salesxc.roomClasses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "Products")
public class Product {

    @PrimaryKey
    @NonNull
    @ColumnInfo
    private String uniqueId;
    @ColumnInfo private String name;
    @ColumnInfo private String purchase;
    @ColumnInfo private String selling;
    @ColumnInfo private String stock;

    public  Product(){ }


    @NonNull
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(@NonNull String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getSelling() {
        return selling;
    }

    public void setSelling(String selling) {
        this.selling = selling;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @NonNull
    public Product getProduct(){
       /* Product p=new Product();
        p.name=this.name;
        p.purchase=this.purchase;
        p.selling=this.selling;
        p.uniqueId=this.uniqueId;
        p.stock=this.stock;*/
        return this;
    }


}
