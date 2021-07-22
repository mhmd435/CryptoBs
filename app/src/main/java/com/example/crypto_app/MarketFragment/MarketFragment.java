package com.example.crypto_app.MarketFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.crypto_app.HomeFragment.Adapters.GainLoseRvAdapter;
import com.example.crypto_app.Model.CryptoMarketModel.CryptoMarketDataModel;
import com.example.crypto_app.RoomDb.MarketDataEntity;
import com.example.crypto_app.RoomDb.MarketListEntity;
import com.example.crypto_app.ViewModel.AppViewModel;
import com.example.crypto_app.Model.CryptoListModel.AllMarketModel;
import com.example.crypto_app.Model.CryptoListModel.DataItem;
import com.example.crypto_app.MainActivity;
import com.example.crypto_app.R;
import com.example.crypto_app.databinding.FragmentMarketBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MarketFragment extends Fragment {
    FragmentMarketBinding fragmentMarketBinding;
    MainActivity mainActivity;
    AppViewModel appViewModel;

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    GainLoseRvAdapter marketRvAdapter;


    CompositeDisposable compositeDisposable;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_market_tb);
        setupToolbar(view);
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.marketFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();

        NavigationUI.setupWithNavController(collapsingToolbarLayout,toolbar,navController,appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if (destination.getId() == R.id.marketFragment){
                    collapsingToolbarLayout.setTitleEnabled(false);
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_35);
                    toolbar.setTitle("Market");
                    toolbar.setTitleTextColor(Color.WHITE);

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentMarketBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_market,container,false);
        compositeDisposable = new CompositeDisposable();
        setupViewModel();
        setupSwipRefresh();
        getMarketListDataFromDb();
        getCryptoMarketDataFromDb();


        // Inflate the layout for this fragment
        return fragmentMarketBinding.getRoot();
    }

    private void getCryptoMarketDataFromDb() {
        Disposable disposable = appViewModel.getCryptoMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MarketDataEntity>() {
                    @Override
                    public void accept(MarketDataEntity roomMarketEntity) throws Throwable {
                        CryptoMarketDataModel cryptoMarketDataModel = roomMarketEntity.getCryptoMarketDataModel();

                        //set BTC.D on UI
                        fragmentMarketBinding.CryptoBTCD.setText(String.format("%.2f",cryptoMarketDataModel.getData().getBtcDominance()) + "%");
                        //set market cap  on UI
                        double totalmarketCap = cryptoMarketDataModel.getData().getQuote().getUSD().getTotalMarketCap();
                        double firstDigit = Double.parseDouble(Double.toString(totalmarketCap).substring(0, 4));
                        fragmentMarketBinding.CryptoMarketCap.setText(firstDigit + "T");
                        //----------------------------
                        //set market Vol on UI
                        String vol = Double.toString(cryptoMarketDataModel.getData().getQuote().getUSD().getTotalVolume24h()).charAt(0)
                        + Double.toString(cryptoMarketDataModel.getData().getQuote().getUSD().getTotalVolume24h()).substring(2);
                        fragmentMarketBinding.CryptoVolume.setText(vol.substring(0,2) + "." + vol.charAt(2) + "B");




                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setupSwipRefresh() {
        fragmentMarketBinding.marketSwipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragmentMarketBinding.marketSwipRefresh.setRefreshing(false);
                    }
                },1500);
                mainActivity.callAllApiReqoest();
            }
        });
    }

    private void getMarketListDataFromDb() {
        Disposable disposable = appViewModel.getAllMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MarketListEntity>() {
                    @Override
                    public void accept(MarketListEntity roomMarketEntity) throws Throwable {
                        AllMarketModel allMarketModel = roomMarketEntity.getAllMarketModel();


                        if (fragmentMarketBinding.marketRv.getAdapter() != null) {
                            marketRvAdapter = (GainLoseRvAdapter) fragmentMarketBinding.marketRv.getAdapter();
                            marketRvAdapter.updateData((ArrayList<DataItem>) allMarketModel.getData());
                        } else {
                            marketRvAdapter = new GainLoseRvAdapter((ArrayList<DataItem>) allMarketModel.getData());
                            fragmentMarketBinding.marketRv.setAdapter(marketRvAdapter);
                        }

                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setupViewModel() {
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}