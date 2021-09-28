package com.besenior.cryptobs.HomeFragment.Repository;

import android.app.Application;
import android.util.Log;

import com.besenior.cryptobs.RoomDb.AppDatabase;
import com.besenior.cryptobs.RoomDb.Entities.MarketDataEntity;
import com.besenior.cryptobs.model.cryptomarketdataModel.CryptoMarketDataModel;
import com.besenior.cryptobs.model.SliderImageModel;
import com.besenior.cryptobs.model.cryptolistmodel.AllMarketModel;
import com.besenior.cryptobs.RoomDb.RoomDao;
import com.besenior.cryptobs.RoomDb.Entities.MarketListEntity;
import com.besenior.cryptobs.retrofit.RequestApi;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeRepository {

    CompositeDisposable compositeDisposable;
    RoomDao roomDao;
    RequestApi requestApi;

    //Contructor for class
    public HomeRepository(RequestApi requestApi,RoomDao roomDao,CompositeDisposable compositeDisposable) {
        this.requestApi = requestApi;
        this.compositeDisposable = compositeDisposable;
        this.roomDao = roomDao;
    }

    //Call list of all Coins api
    public Future<Observable<AllMarketModel>> marketListFutureCall(){
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Callable<Observable<AllMarketModel>> myNetworkCallable = new Callable<Observable<AllMarketModel>>() {
            @Override
            public Observable<AllMarketModel> call() throws Exception {
//                return serviceGenerator.getRequestApi().makeMarketLatestListCall();
                return requestApi.makeMarketLatestListCall();
            }
        };


        final Future<Observable<AllMarketModel>> futureObservable = new Future<Observable<AllMarketModel>>(){

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                if(mayInterruptIfRunning){
                    executor.shutdown();
                }
                return false;
            }

            @Override
            public boolean isCancelled() {
                return executor.isShutdown();
            }

            @Override
            public boolean isDone() {
                return executor.isTerminated();
            }

            @Override
            public Observable<AllMarketModel> get() throws ExecutionException, InterruptedException {
                return executor.submit(myNetworkCallable).get();
            }

            @Override
            public Observable<AllMarketModel> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return executor.submit(myNetworkCallable).get(timeout, unit);
            }
        };

        return futureObservable;

    }

    //Call Crypto Market Data like marketcap,volume,... api
    public Future<Observable<SliderImageModel>> PageViewImgFutureCall(){
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Callable<Observable<SliderImageModel>> myNetworkCallable = new Callable<Observable<SliderImageModel>>() {
            @Override
            public Observable<SliderImageModel> call() throws Exception {
//                return serviceGenerator.getRequestApi().makePageViewCall();
                return  requestApi.makePageViewCall();
            }
        };


        final Future<Observable<SliderImageModel>> futureObservable = new Future<Observable<SliderImageModel>>(){

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                if(mayInterruptIfRunning){
                    executor.shutdown();
                }
                return false;
            }

            @Override
            public boolean isCancelled() {
                return executor.isShutdown();
            }

            @Override
            public boolean isDone() {
                return executor.isTerminated();
            }

            @Override
            public Observable<SliderImageModel> get() throws ExecutionException, InterruptedException {
                return executor.submit(myNetworkCallable).get();
            }

            @Override
            public Observable<SliderImageModel> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return executor.submit(myNetworkCallable).get(timeout, unit);
            }
        };

        return futureObservable;

    }

    //insert list of All Coins to the Room Database
    public void InsertAllMarket(AllMarketModel allMarketModel){
        Completable.fromAction(() -> roomDao.insert(new MarketListEntity(allMarketModel)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("insertAllMarket", "onSubscribe: ok");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("insertAllMarket", "onComplete: ok");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("insertAllMarket", "onError: " + e.getMessage());
                    }
                });
    }

    //get the list of All Coins From Room Database
    public Flowable<MarketListEntity> getAllMarketData(){
        return roomDao.getAllMarketData();
    }

    //Call this method to prevent memory leak
    public void clearCompositdisposble(){
        compositeDisposable.clear();
    }

    public Flowable<MarketDataEntity> getCryptoMarketData(){
        return roomDao.getCryptoMarketData();
    }

    public void InsertCryptoDataMarket(CryptoMarketDataModel cryptoMarketDataModel) {
        Completable.fromAction(() -> roomDao.insert(new MarketDataEntity(cryptoMarketDataModel))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("insertAllMarket", "onSubscribe: ok");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("insertAllMarket", "onComplete: ok");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("insertAllMarket", "onError: " + e.getMessage());
                    }
                });
    }
}
