package com.example.crypto_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.crypto_app.model.cryptolistmodel.AllMarketModel;
import com.example.crypto_app.model.CryptoMarketModel.CryptoMarketDataModel;
import com.example.crypto_app.viewmodel.AppViewModel;
import com.example.crypto_app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    NavHostFragment navHostFragment;
    NavController navController;
    ActivityMainBinding activityMainBinding;
    AppBarConfiguration appBarConfiguration;
    public DrawerLayout drawerLayout;

    AppViewModel appViewModel;

    CompositeDisposable compositeDisposable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setupViewModel();
        CheckConnection();


        drawerLayout = activityMainBinding.drawerlayout;
        setupNavigationComponent();
    }

    //check phone is connecting to the internet or not?
    private void CheckConnection() {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                Log.e("TAG", "onAvailable: ");
                callAllApiReqoest();
            }

            @Override
            public void onLost(@NonNull Network network) {
                Log.e("TAG", "onLost: ");
            }
        };
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }
    }

    //method that include all api requests
    public void callAllApiReqoest() {
        CallListApiRequest();
        CallCryptoMarketApiRequest();
    }

    //api call with RxJava (Crypto Data api)
    private void CallCryptoMarketApiRequest() {

        Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    Document pageSrc = Jsoup.connect("https://coinmarketcap.com/").get();

                    // Scraping Market Data like (marketCap,Dominance,...)
                    Elements ScrapeMarketData= pageSrc.getElementsByClass("cmc-link");
                    //for spliting BTC and ETH dominance in txt
                    String[] dominance_txt = ScrapeMarketData.get(4).text().split(" ");

                    // Scraping Market number of changes like (MarketcapChange,volumeChange,...)
                    Elements ScrapeMarketChange = pageSrc.getElementsByClass("sc-27sy12-0 gLZJFn");
                    String[] changePercent = ScrapeMarketChange.text().split(" ");

                    // Scraping All span Tag
                    Elements ScrapeChangeIcon = pageSrc.getElementsByTag("span");

                    // get all span Tag wth Icon (class= caretUp and caretDown)
                    ArrayList<String> IconList = new ArrayList();
                    for (Element i : ScrapeChangeIcon){
                        if (i.hasClass("icon-Caret-down") || i.hasClass("icon-Caret-up")){
                            IconList.add(i.attr("class"));
                        }
                    }

                    // matching - or + element of PercentChanges
                    ArrayList<String> finalchangePercent = new ArrayList<>();
                    for (int i = 0;i < 3;i++){
                        if (IconList.get(i).equals("icon-Caret-up")){
                            finalchangePercent.add(changePercent[i]);
                        }else{
                            finalchangePercent.add("-" + changePercent[i]);
                        }
                    }

                    // initialize all data
                    String Cryptos = ScrapeMarketData.get(0).text();
                    String Exchanges = ScrapeMarketData.get(1).text();
                    String MarketCap = ScrapeMarketData.get(2).text();
                    String Vol_24h = ScrapeMarketData.get(3).text();

                    String BTC_Dominance = dominance_txt[1];
                    String ETH_Dominance = dominance_txt[3];

                    String MarketCap_change = finalchangePercent.get(0);
                    String vol_change = finalchangePercent.get(1);
                    String BTCD_change = finalchangePercent.get(2);

                    CryptoMarketDataModel cryptoMarketDataModel = new CryptoMarketDataModel(Cryptos,Exchanges,MarketCap,Vol_24h,BTC_Dominance,ETH_Dominance,MarketCap_change,vol_change,BTCD_change);
                    // insert model class to RoomDatabase
                    appViewModel.insertCryptoDataMarket(cryptoMarketDataModel);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        Log.e("on", "onSubscribe: ok");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("on", "onComplete: ok");
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("on", "onError: " + e.getMessage());
                    }
                });
    }

    //api Call with RxJava (list of Coins api)
    private void CallListApiRequest() {
        try {
            /* disposable for avoid memory leak */
            appViewModel.MarketFutureCall().get()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.rxjava3.core.Observer<AllMarketModel>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable disposable) {
                            Log.e("TAG", "onSubscribe");
                            compositeDisposable.add(disposable);
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull AllMarketModel allMarketModel) {

                            Log.e("TAG", "onNext: " + allMarketModel.getRootData().getCryptoCurrencyList().size());
                            appViewModel.insertAllMarket(allMarketModel);
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.e("TAG", "onError: " + e.getMessage());
                            Snackbar.make(activityMainBinding.mainCon,"an Error apears...",1500).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //setup ViewModels
    private void setupViewModel() {
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }

    //setup NavComponent (bottomNavigationView,Drawer)
    private void setupNavigationComponent() {

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        //Top Level Fragments
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment,R.id.marketFragment,R.id.watchlistFragment)
                .setOpenableLayout(activityMainBinding.drawerlayout)
                .build();

        //for connect navigationView to navController
        NavigationUI.setupWithNavController(activityMainBinding.navigationView,navController);

        //for connect bottomNavigation to navController
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigation,navController);

        //use NavOptions for set Animation for transition between fragments
        activityMainBinding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                NavOptions.Builder optionsBuilder = new NavOptions.Builder();

                switch (item.getItemId()) {
                    case R.id.homeFragment:
                    case R.id.marketFragment:
                    case R.id.watchlistFragment:
                        optionsBuilder
                                .setEnterAnim(R.anim.fade_in)
                                .setExitAnim(R.anim.slide_out_right)
                                .setPopEnterAnim(R.anim.fade_in)
                                .setPopExitAnim(R.anim.slide_out_left);
                         break;
                }
                navController.navigate(item.getItemId(), null, optionsBuilder.build());
                return true;
            }
        });

        //for do nothing in reSelect Items when Integrat with Nav Component
        activityMainBinding.bottomNavigation.setOnNavigationItemReselectedListener(item -> { });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, activityMainBinding.drawerlayout) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (activityMainBinding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            activityMainBinding.drawerlayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}