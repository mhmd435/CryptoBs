package com.example.crypto_app.retrofit;

import com.example.crypto_app.Model.CryptoListModel.AllMarketModel;
import com.example.crypto_app.Model.CryptoMarketModel.CryptoMarketDataModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface RequestApi {

    @GET("v1/cryptocurrency/listings/latest?limit=200")
    Observable<AllMarketModel> makeMarketLatestListCall();


    @GET("v1/global-metrics/quotes/latest")
    Observable<CryptoMarketDataModel> makeCryptoMarketDataCall();
}
