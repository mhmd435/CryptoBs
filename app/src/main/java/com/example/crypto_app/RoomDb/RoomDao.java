package com.example.crypto_app.RoomDb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RoomMarketEntity roomMarketEntity);

    @Query("SELECT * FROM AllMarket")
    Flowable<RoomMarketEntity> getAllMarketData();

}
