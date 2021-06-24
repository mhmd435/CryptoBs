package com.example.crypto_app.HomeFragment.Repository;

import android.app.Application;
import android.util.Log;

import com.example.crypto_app.HomeFragment.Model.AllMarketModel;
import com.example.crypto_app.RoomDb.AppDatabase;
import com.example.crypto_app.RoomDb.RoomDao;
import com.example.crypto_app.RoomDb.RoomMarketEntity;
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
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeRepository {

    private static HomeRepository instance;
    private RoomDao roomDao;

    public HomeRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        roomDao = appDatabase.roomDao();
    }

    public static HomeRepository getInstance(Application application){
        if(instance == null){
            instance = new HomeRepository(application);
        }
        return instance;
    }

    public Future<Observable<AllMarketModel>> makeFutureQuery(){
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Callable<Observable<AllMarketModel>> myNetworkCallable = new Callable<Observable<AllMarketModel>>() {
            @Override
            public Observable<AllMarketModel> call() throws Exception {
                return ServiceGenerator.getRequestApi().makeObservableQuery();
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

    public void InsertAllMarket(AllMarketModel allMarketModel){
        Completable.fromAction(() -> roomDao.insert(new RoomMarketEntity(allMarketModel))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("insertAllMarket", "onSubscribe: ok");
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

    public Flowable<RoomMarketEntity> getAllMarketData(){
        return roomDao.getAllMarketData();
    }

}
