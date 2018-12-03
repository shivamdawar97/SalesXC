package com.example.shivam97.salesxc.roomClasses;

import android.app.Application;
import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

public class Repository {
    private ProductDao productDao;

    public Repository(Application application) {
        MyRoomDatabase database=MyRoomDatabase.getDatabase(application);
        productDao=database.productDao();
    }

   public void insert(Product product){
        new insertProduct(productDao).execute(product);
    }

   public Product getProduct(String uid){
       try {
           return new getProduct(productDao).execute(uid).get();
       } catch (InterruptedException | ExecutionException e) {
           e.printStackTrace();
           return null;
       }
   }

    private static class insertProduct extends AsyncTask<Product,Void,Void>{

        private ProductDao mProductDao;
        private insertProduct(ProductDao dao) {
            mProductDao=dao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            mProductDao.insert(products[0]);
            return null;
        }
    }

    private static class getProduct extends AsyncTask<String,Void,Product>{

        private ProductDao dao;
        private getProduct(ProductDao productDao){
            dao=productDao;
        }

        @Override
        protected Product doInBackground(String... strings) {

            return dao.getProduct(strings[0]);
        }
    }


}
