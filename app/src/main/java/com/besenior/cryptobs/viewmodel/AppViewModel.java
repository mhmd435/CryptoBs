package com.besenior.cryptobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.besenior.cryptobs.RoomDb.Entities.MarketDataEntity;
import com.besenior.cryptobs.model.cryptomarketdataModel.CryptoMarketDataModel;
import com.besenior.cryptobs.model.cryptolistmodel.AllMarketModel;
import com.besenior.cryptobs.model.SliderImageModel;
import com.besenior.cryptobs.ui.HomeFragment.Repository.HomeRepository;
import com.besenior.cryptobs.RoomDb.Entities.MarketListEntity;

import java.util.concurrent.Future;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class AppViewModel extends AndroidViewModel {

    @Inject
    public HomeRepository repository;

    @Inject
    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public Future<Observable<AllMarketModel>> MarketFutureCall(){
        return repository.marketListFutureCall();
    }

    public Future<Observable<SliderImageModel>> PageViewFutureCall(){
        return repository.PageViewImgFutureCall();
    }

    public void insertAllMarket(AllMarketModel allMarketModel){
        repository.InsertAllMarket(allMarketModel);
    }

    public Flowable<MarketListEntity> getAllMarketData(){
        return repository.getAllMarketData();
    }


    public void insertCryptoDataMarket(CryptoMarketDataModel cryptoMarketDataModel) {
        repository.InsertCryptoDataMarket(cryptoMarketDataModel);
    }

    public Flowable<MarketDataEntity> getCryptoMarketData(){
        return repository.getCryptoMarketData();
    }

    public void clearRepoComposable(){
       repository.clearCompositdisposble();
    }


}
