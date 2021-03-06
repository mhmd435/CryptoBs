package com.besenior.cryptobs.RoomDb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.besenior.cryptobs.RoomDb.Entities.MarketDataEntity;
import com.besenior.cryptobs.RoomDb.Entities.MarketListEntity;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MarketListEntity marketListEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MarketDataEntity marketDataEntity);

    @Query("SELECT * FROM AllMarket")
    Flowable<MarketListEntity> getAllMarketData();

    @Query("SELECT * FROM CryptoData")
    Flowable<MarketDataEntity> getCryptoMarketData();

}
