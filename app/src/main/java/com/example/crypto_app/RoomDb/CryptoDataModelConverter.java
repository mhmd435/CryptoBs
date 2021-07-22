package com.example.crypto_app.RoomDb;

import androidx.room.TypeConverter;

import com.example.crypto_app.Model.CryptoMarketModel.CryptoMarketDataModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class CryptoDataModelConverter {

    @TypeConverter
    public String tojson(CryptoMarketDataModel cryptoMarketDataModel) {
        if (cryptoMarketDataModel == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CryptoMarketDataModel>() {}.getType();
        String json = gson.toJson(cryptoMarketDataModel, type);
        return json;
    }

    @TypeConverter
    public CryptoMarketDataModel toDataClass(String cryptoData) {
        if (cryptoData == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CryptoMarketDataModel>() {}.getType();
        CryptoMarketDataModel cryptoMarketDataModel = gson.fromJson(cryptoData, type);
        return cryptoMarketDataModel;
    }
}
