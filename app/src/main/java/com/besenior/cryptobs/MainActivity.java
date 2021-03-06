package com.besenior.cryptobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.besenior.cryptobs.databinding.ActivityMainBinding;
import com.besenior.cryptobs.databinding.DrawerHeaderlayoutBinding;
import com.besenior.cryptobs.viewmodel.AppViewModel;
import com.besenior.cryptobs.model.cryptomarketdataModel.CryptoMarketDataModel;
import com.besenior.cryptobs.model.cryptolistmodel.AllMarketModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.components.ActivityComponent;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    NavHostFragment navHostFragment;
    NavController navController;

    @Inject
    ActivityMainBinding activityMainBinding;

    DrawerHeaderlayoutBinding drawerHeaderlayoutBinding;
    AppBarConfiguration appBarConfiguration;
    public DrawerLayout drawerLayout;
    AppViewModel appViewModel;

    @Inject
    @Named("MainActivityCompositeDisposable")
    CompositeDisposable compositeDisposable;

    @Inject
    SharedPreferences sharedPrefs;

    @Inject
    ConnectivityManager connectivityManager;

    @Inject
    NetworkRequest request;

    private String ImgFromStore,fname,lname,mail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

//        compositeDisposable = new CompositeDisposable();

//        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setLocale(this,"en");
        drawerHeaderlayoutBinding = DrawerHeaderlayoutBinding.bind(activityMainBinding.navigationView.getHeaderView(0));

        setupViewModel();
        CheckConnection();


        drawerLayout = activityMainBinding.drawerlayout;
        setupNavigationComponent();
        initDrawerHeader();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale(this,"en");
    }

    public static void setLocale(MainActivity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


    private void initDrawerHeader() {
        //get Data SharedPrefrence
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ImgFromStore = sharedPrefs.getString("profileImg",null);
        fname = sharedPrefs.getString("firstname","");
        lname = sharedPrefs.getString("lastname","");
        mail = sharedPrefs.getString("male","");


        //set profile image
        if (ImgFromStore == null){
            drawerHeaderlayoutBinding.drawerHeaderImage.setImageResource(R.drawable.profile_placeholder);
        }else {
            drawerHeaderlayoutBinding.drawerHeaderImage.setImageBitmap(decodeBase64(ImgFromStore));
        }
        //set firstname and lastname
        if (fname.equals("") || lname.equals("")){
            drawerHeaderlayoutBinding.drawerheaderName.setText("set your name in profile");
        }else{
            drawerHeaderlayoutBinding.drawerheaderName.setText(fname + lname);
        }
        //set mail
        if (mail.equals("")){
            drawerHeaderlayoutBinding.drawerheaderMail.setText("example@mail.com");
        }else{
            drawerHeaderlayoutBinding.drawerheaderMail.setText(mail);
        }


    }

    // decode string to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
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
                Snackbar.make(activityMainBinding.mainCon,"Internet connection lost",2000).show();
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

        Observable.interval(20, TimeUnit.SECONDS)
                .flatMap(n -> appViewModel.MarketFutureCall().get().subscribeOn(Schedulers.io()))
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
//                        Snackbar.make(activityMainBinding.mainCon,"an Error apears...",1500).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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

        //set on Click for Exit Btn in drawerlayout
        activityMainBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.ExitBtn){
                    finish();
                }else {
                    NavigationUI.onNavDestinationSelected(item, navController);
                    activityMainBinding.drawerlayout.closeDrawers();
                }
                return false;
            }
        });

        setupSmoothBottomMenu();

        //for connect normal bottomNavigation to navController
        //NavigationUI.setupWithNavController(activityMainBinding.bottomNavigation,navController);

        //use NavOptions for set Animation for transition between fragments
//        activityMainBinding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
//
//                NavOptions.Builder optionsBuilder = new NavOptions.Builder();
//
//                switch (item.getItemId()) {
//                    case R.id.homeFragment:
//                    case R.id.marketFragment:
//                    case R.id.watchlistFragment:
//                        optionsBuilder
//                                .setEnterAnim(R.anim.fade_in)
//                                .setExitAnim(R.anim.slide_out_right)
//                                .setPopEnterAnim(R.anim.fade_in)
//                                .setPopExitAnim(R.anim.slide_out_left);
//                         break;
//                }
//                navController.navigate(item.getItemId(), null, optionsBuilder.build());
//                return true;
//            }
//        });



    }

    private void setupSmoothBottomMenu() {
        PopupMenu popupMenu = new PopupMenu(this, null);
        popupMenu.inflate(R.menu.bottom_navigation_menu);
        Menu menu = popupMenu.getMenu();
        activityMainBinding.bottomNavigation.setupWithNavController(menu, navController);

        //for do nothing in reSelect Items when Integrat with Nav Component
        activityMainBinding.bottomNavigation.setOnItemReselectedListener(item -> { });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, activityMainBinding.drawerlayout) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
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
        appViewModel.clearRepoComposable();
    }
}