package com.besenior.cryptobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.besenior.cryptobs.databinding.ActivitySplashScreenBinding;
import com.besenior.cryptobs.model.cryptolistmodel.AllMarketModel;
import com.besenior.cryptobs.viewmodel.AppViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class SplashScreenActivity extends AppCompatActivity {

    AppViewModel appViewModel;
    ActivitySplashScreenBinding activitySplashScreenBinding;
    CompositeDisposable compositeDisposable;

    @Inject
    ConnectivityManager connectivityManager;

    @Inject
    NetworkRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
        //hide status bar and show fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        activitySplashScreenBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash_screen);
        compositeDisposable = new CompositeDisposable();

        setupViewModel();
        CheckConnection();
        boolean isCon = isNetworkConnected();
        if (!isCon){
            activitySplashScreenBinding.loadingTxt.setText(R.string.connection_lost);
        }

    }

    //setup ViewModels
    private void setupViewModel() {
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }

    private boolean isNetworkConnected() {
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    //check phone is connecting to the internet or not?
    private void CheckConnection() {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                Log.e("TAG", "onAvailable: ");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activitySplashScreenBinding.loadingTxt.setText(R.string.Getting_Data);
                    }
                });

                callAllApiReqoest();
            }

            @Override
            public void onLost(@NonNull Network network) {
                Log.e("TAG", "onLost: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activitySplashScreenBinding.loadingTxt.setText(R.string.connection_lost);
                    }
                });
            }
        };
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
//            NetworkRequest request = new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }
    }


    //method that include all api requests
    public void callAllApiReqoest() {
        CallListApiRequest();
    }

    //api Call with RxJava (list of Coins api)
    private void CallListApiRequest() {
        try {
            appViewModel.MarketFutureCall().get().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AllMarketModel>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            Log.e("TAG", "onSubscribe");
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull AllMarketModel allMarketModel) {
                            Log.e("TAG", "onNext: " + allMarketModel.getRootData().getCryptoCurrencyList().size());
                            appViewModel.insertAllMarket(allMarketModel);

                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.e("TAG", "onError: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            finish();
                        }
                    });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}