package com.example.crypto_app.HomeFragment.Repository;

import android.app.Application;
import android.util.Log;

import com.example.crypto_app.Model.CryptoListModel.AllMarketModel;
import com.example.crypto_app.Model.CryptoMarketModel.CryptoMarketDataModel;
import com.example.crypto_app.RoomDb.AppDatabase;
import com.example.crypto_app.RoomDb.MarketDataEntity;
import com.example.crypto_app.RoomDb.RoomDao;
import com.example.crypto_app.RoomDb.MarketListEntity;
import com.example.crypto_app.retrofit.ServiceGenerator;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private CompositeDisposable compositeDisposable;

    private static HomeRepository instance;
    private RoomDao roomDao;

    //Contructor for class
    public HomeRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        compositeDisposable = new CompositeDisposable();
        roomDao = appDatabase.roomDao();
    }

    //call this method to create an instance of this repository class
    public static HomeRepository getInstance(Application application){
        if(instance == null){
            instance = new HomeRepository(application);
        }
        return instance;
    }

    //Call list of all Coins api
    public Future<Observable<AllMarketModel>> marketListFutureCall(){
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Callable<Observable<AllMarketModel>> myNetworkCallable = new Callable<Observable<AllMarketModel>>() {
            @Override
            public Observable<AllMarketModel> call() throws Exception {
                return ServiceGenerator.getRequestApi().makeMarketLatestListCall();
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
    public Future<Observable<CryptoMarketDataModel>> CryptomarketFutureCall(){
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Callable<Observable<CryptoMarketDataModel>> myNetworkCallable = new Callable<Observable<CryptoMarketDataModel>>() {
            @Override
            public Observable<CryptoMarketDataModel> call() throws Exception {
                return ServiceGenerator.getRequestApi().makeCryptoMarketDataCall();
            }
        };


        final Future<Observable<CryptoMarketDataModel>> futureObservable = new Future<Observable<CryptoMarketDataModel>>(){

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
            public Observable<CryptoMarketDataModel> get() throws ExecutionException, InterruptedException {
                return executor.submit(myNetworkCallable).get();
            }

            @Override
            public Observable<CryptoMarketDataModel> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return executor.submit(myNetworkCallable).get(timeout, unit);
            }
        };

        return futureObservable;

    }

    //insert list of All Coins to the Room Database
    public void InsertAllMarket(AllMarketModel allMarketModel){
        Completable.fromAction(() -> roomDao.insert(new MarketListEntity(allMarketModel))).subscribeOn(Schedulers.io())
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

    //insert Crypto Market Data to the Room Database
    public void InsertCryptoDataMarket(CryptoMarketDataModel cryptoMarketDataModel){
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

    //get Crypto Market Data from Room Database
    public Flowable<MarketDataEntity> getCryptoMarketData(){
        return roomDao.getCryptoMarketData();
    }

    //Call this method to prevent memory leak
    public void clearCompositdisposble(){
        compositeDisposable.clear();
    }


}
