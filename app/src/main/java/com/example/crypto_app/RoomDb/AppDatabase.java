package com.example.crypto_app.RoomDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.crypto_app.HomeFragment.Model.AllMarketModel;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

@TypeConverters(DataConverter.class)
@Database(entities = {RoomMarketEntity.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String Db_Name = "AppDb";
    private static AppDatabase instance;
    public abstract RoomDao roomDao();

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,Db_Name)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
