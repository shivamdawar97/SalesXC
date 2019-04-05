package com.example.shivam97.salesxc.roomClasses;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.shivam97.salesxc.roomClasses.Product;

import java.util.List;

@Dao
public interface ProductDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Product product);

    @Query("Delete from Products where uniqueId=:uniqueId")
    void deleteProduct(String uniqueId);

    @Query("Update Products SET stock=:stock Where uniqueId=:uniqueId")
    void updateStock(String uniqueId,int stock);

    @Query("Select * from Products where uniqueId=:uniqueId")
    Product getProduct(String uniqueId);

    @Query("select * from Products order by name ASC")
    LiveData<List<Product>> getAllProducts();


}
