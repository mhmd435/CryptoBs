package com.example.crypto_app.retrofit;

import com.example.crypto_app.HomeFragment.Model.AllMarketModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.GET;

public interface RequestApi {

    @GET("v1/cryptocurrency/listings/latest?limit=200")
    Observable<AllMarketModel> makeObservableQuery();
}
