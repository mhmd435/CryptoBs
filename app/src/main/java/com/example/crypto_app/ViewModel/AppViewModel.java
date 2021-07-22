package com.example.crypto_app.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.crypto_app.Model.CryptoListModel.AllMarketModel;
import com.example.crypto_app.Model.CryptoMarketModel.CryptoMarketDataModel;
import com.example.crypto_app.Model.SliderImageModel;
import com.example.crypto_app.HomeFragment.Repository.HomeRepository;
import com.example.crypto_app.R;
import com.example.crypto_app.RoomDb.MarketDataEntity;
import com.example.crypto_app.RoomDb.MarketListEntity;

import java.util.ArrayList;
import java.util.concurrent.Future;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public class AppViewModel extends AndroidViewModel {

    private HomeRepository repository;

    public AppViewModel(@NonNull Application application) {
        super(application);
        repository = HomeRepository.getInstance(application);
    }

    public Future<Observable<AllMarketModel>> MarketFutureCall(){
        return repository.marketListFutureCall();
    }

    public Future<Observable<CryptoMarketDataModel>> CryptoMarketFutureCall(){
        return repository.CryptomarketFutureCall();
    }

    public MutableLiveData<ArrayList<SliderImageModel>> getImage(){
        MutableLiveData<ArrayList<SliderImageModel>> mutableLiveData = new MutableLiveData<>();

        ArrayList<SliderImageModel> arrayList = new ArrayList<>();
        arrayList.add(new SliderImageModel(R.drawable.image1));
        arrayList.add(new SliderImageModel(R.drawable.image2));
        arrayList.add(new SliderImageModel(R.drawable.image3));
        arrayList.add(new SliderImageModel(R.drawable.image4));
        arrayList.add(new SliderImageModel(R.drawable.image5));

        mutableLiveData.setValue(arrayList);
        return mutableLiveData;
    }

    public void insertAllMarket(AllMarketModel allMarketModel){
        repository.InsertAllMarket(allMarketModel);
    }

    public Flowable<MarketListEntity> getAllMarketData(){
        return repository.getAllMarketData();
    }


    public void insertCryptoDataMarket(CryptoMarketDataModel cryptoMarketDataModel){
        repository.InsertCryptoDataMarket(cryptoMarketDataModel);
    }

    public Flowable<MarketDataEntity> getCryptoMarketData(){
        return repository.getCryptoMarketData();
    }




}
