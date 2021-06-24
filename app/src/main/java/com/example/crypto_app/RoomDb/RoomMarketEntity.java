package com.example.crypto_app.RoomDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.crypto_app.HomeFragment.Model.AllMarketModel;

@Entity(tableName = "AllMarket")
public class RoomMarketEntity {

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "AllMarket")
    public AllMarketModel allMarketModel;

    public RoomMarketEntity(AllMarketModel allMarketModel) {
        this.allMarketModel = allMarketModel;
    }

    public int getUid() {
        return uid;
    }

    public AllMarketModel getAllMarketModel() {
        return allMarketModel;
    }
}
