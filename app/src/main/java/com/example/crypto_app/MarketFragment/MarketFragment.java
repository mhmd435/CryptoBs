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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.crypto_app.model.CryptoMarketModel.CryptoMarketDataModel;
import com.example.crypto_app.RoomDb.MarketDataEntity;
import com.example.crypto_app.RoomDb.MarketListEntity;
import com.example.crypto_app.viewmodel.AppViewModel;
import com.example.crypto_app.model.cryptolistmodel.AllMarketModel;
import com.example.crypto_app.model.cryptolistmodel.DataItem;
import com.example.crypto_app.MainActivity;
import com.example.crypto_app.R;
import com.example.crypto_app.databinding.FragmentMarketBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    marketRV_Adapter marketRvAdapter;
    List<DataItem> dataItemList;


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

        setupSearchBox();
        setupViewModel();
        setupSwipRefresh();
        getMarketListDataFromDb();
        getCryptoMarketDataFromDb();


        // Inflate the layout for this fragment
        return fragmentMarketBinding.getRoot();
    }

    private void setupSearchBox() {
        fragmentMarketBinding.searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String name){
        ArrayList<DataItem> newList = new ArrayList<>();
        for (DataItem item : dataItemList){
            if (item.getSymbol().toLowerCase().contains(name) || item.getName().toLowerCase().contains(name.toLowerCase())){
                newList.add(item);
            }
        }

        marketRvAdapter.updateData(newList);
        marketRvAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }

            void checkEmpty(){
                if (marketRvAdapter.getItemCount() == 0){
                    fragmentMarketBinding.itemnotFoundTxt.setVisibility(View.VISIBLE);
                }else {
                    fragmentMarketBinding.itemnotFoundTxt.setVisibility(View.GONE);
                }
            }
        });

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
                        fragmentMarketBinding.CryptoBTCD.setText(cryptoMarketDataModel.getBTC_Dominance());
                        String[] str3 = cryptoMarketDataModel.getBTCD_change().split("%");
                        if (Float.parseFloat(str3[0]) > 0){
                            fragmentMarketBinding.BTCDIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
                            fragmentMarketBinding.BTCChange.setTextColor(Color.GREEN);
                        }else if (Float.parseFloat(str3[0]) < 0){
                            fragmentMarketBinding.BTCDIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                            fragmentMarketBinding.BTCChange.setTextColor(Color.RED);
                        }else {
                            fragmentMarketBinding.BTCDIcon.setBackgroundResource(R.drawable.ic_baseline_horizontal_rule_24);
                            fragmentMarketBinding.BTCChange.setTextColor(Color.WHITE);
                        }
                        fragmentMarketBinding.BTCChange.setText(cryptoMarketDataModel.getBTCD_change());


                        //set market cap  on UI
                        fragmentMarketBinding.CryptoMarketCap.setText(cryptoMarketDataModel.getMarketCap());
                        // get marketcap without %
                        String[] str = cryptoMarketDataModel.getMarketCap_change().split("%");
                        Log.e("TAG", "accept: " + str[0]);
                        if (Float.parseFloat(str[0]) > 0){
                            fragmentMarketBinding.marketcapIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
                            fragmentMarketBinding.MarketCapChange.setTextColor(Color.GREEN);
                        }else if (Float.parseFloat(str[0]) < 0){
                            fragmentMarketBinding.marketcapIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                            fragmentMarketBinding.MarketCapChange.setTextColor(Color.RED);
                        }else {
                            fragmentMarketBinding.marketcapIcon.setBackgroundResource(R.drawable.ic_baseline_horizontal_rule_24);
                            fragmentMarketBinding.MarketCapChange.setTextColor(Color.WHITE);
                        }
                        fragmentMarketBinding.MarketCapChange.setText(cryptoMarketDataModel.getMarketCap_change());


                        //set market Vol on UI
                        fragmentMarketBinding.CryptoVolume.setText(cryptoMarketDataModel.getVol_24h());
                        //get VolumeChange without %
                        String[] str2 = cryptoMarketDataModel.getVol_change().split("%");
                        if (Float.parseFloat(str2[0]) > 0){
                            fragmentMarketBinding.VolumeIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
                            fragmentMarketBinding.VolumeChange.setTextColor(Color.GREEN);
                        }else if (Float.parseFloat(str2[0]) < 0){
                            fragmentMarketBinding.VolumeIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                            fragmentMarketBinding.VolumeChange.setTextColor(Color.RED);
                        }else {
                            fragmentMarketBinding.VolumeIcon.setBackgroundResource(R.drawable.ic_baseline_horizontal_rule_24);
                            fragmentMarketBinding.VolumeChange.setTextColor(Color.WHITE);
                        }
                        fragmentMarketBinding.VolumeChange.setText(cryptoMarketDataModel.getVol_change());




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
                        dataItemList = allMarketModel.getRootData().getCryptoCurrencyList();

                        if (fragmentMarketBinding.marketRv.getAdapter() != null) {
                            marketRvAdapter = (marketRV_Adapter) fragmentMarketBinding.marketRv.getAdapter();
                            marketRvAdapter.updateData((ArrayList<DataItem>) allMarketModel.getRootData().getCryptoCurrencyList());
                        } else {
                            marketRvAdapter = new marketRV_Adapter((ArrayList<DataItem>) allMarketModel.getRootData().getCryptoCurrencyList());
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