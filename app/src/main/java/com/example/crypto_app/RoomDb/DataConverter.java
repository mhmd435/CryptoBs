package com.example.crypto_app.RoomDb;

import androidx.room.TypeConverter;

import com.example.crypto_app.HomeFragment.Model.AllMarketModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DataConverter {

    @TypeConverter
    public String fromCountryLangList(AllMarketModel allMarketModel) {
        if (allMarketModel == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<AllMarketModel>() {}.getType();
        String json = gson.toJson(allMarketModel, type);
        return json;
    }

    @TypeConverter
    public AllMarketModel toCountryLangList(String allMarket) {
        if (allMarket == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<AllMarketModel>() {}.getType();
        AllMarketModel allMarketModel = gson.fromJson(allMarket, type);
        return allMarketModel;
    }
}
