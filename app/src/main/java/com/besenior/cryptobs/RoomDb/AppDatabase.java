package com.besenior.cryptobs.RoomDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.besenior.cryptobs.RoomDb.Converters.AllMarketModelConverter;
import com.besenior.cryptobs.RoomDb.Converters.CryptoDataModelConverter;
import com.besenior.cryptobs.RoomDb.Entities.MarketDataEntity;
import com.besenior.cryptobs.RoomDb.Entities.MarketListEntity;

@TypeConverters({AllMarketModelConverter.class, CryptoDataModelConverter.class})
@Database(entities = {MarketListEntity.class, MarketDataEntity.class},version = 3,exportSchema = false)
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
