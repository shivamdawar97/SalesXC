package com.example.shivam97.salesxc.roomClasses;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;


@Database(entities = {Product.class},version = 1)
public abstract class MyRoomDatabase  extends RoomDatabase {

    public abstract ProductDao productDao();

    private static volatile MyRoomDatabase INSTANCE;

    private static RoomDatabase.Callback callback=
            new RoomDatabase.Callback(){

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    //start open processes
                }
            };

    static MyRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (MyRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            MyRoomDatabase.class,"word_database")
                            .addCallback(callback)
                            .build();

                }            }
        }
        return INSTANCE;
    }


}
