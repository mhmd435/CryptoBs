package com.example.crypto_app.RoomDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.crypto_app.Model.CryptoMarketModel.CryptoMarketDataModel;

@Entity(tableName = "CryptoData")
public class MarketDataEntity {

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "MarketData")
    public CryptoMarketDataModel cryptoMarketDataModel;

    public MarketDataEntity(CryptoMarketDataModel cryptoMarketDataModel) {
        this.cryptoMarketDataModel = cryptoMarketDataModel;
    }

    public int getUid() {
        return uid;
    }

    public CryptoMarketDataModel getCryptoMarketDataModel() {
        return cryptoMarketDataModel;
    }
}
