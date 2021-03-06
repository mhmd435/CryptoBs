package com.besenior.cryptobs.ui.HomeFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.besenior.cryptobs.ui.HomeFragment.Adapters.GainLoseRvAdapter;
import com.besenior.cryptobs.model.cryptolistmodel.AllMarketModel;
import com.besenior.cryptobs.model.cryptolistmodel.DataItem;
import com.besenior.cryptobs.viewmodel.AppViewModel;
import com.besenior.cryptobs.R;
import com.besenior.cryptobs.databinding.FragmentTopGainLoseBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class TopGainLoseFrag extends Fragment {

    FragmentTopGainLoseBinding fragmentTopGainLoseBinding;
    GainLoseRvAdapter gainLoseRvAdapter;
    AppViewModel appViewModel;
    List<DataItem> data;
    CompositeDisposable compositeDisposable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentTopGainLoseBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_top_gain_lose,container,false);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        // Inflate the layout for this fragment
        return fragmentTopGainLoseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compositeDisposable = new CompositeDisposable();
        //get data from viewPager2 adapter as bundle
        Bundle args = getArguments();
        int pos = args.getInt("pos");

        setupRecyclerView(pos);

    }

    public void setupRecyclerView(int pos) {

        Disposable disposable = appViewModel.getAllMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(roomMarketEntity -> {
                    AllMarketModel allMarketModel = roomMarketEntity.getAllMarketModel();
                    data = allMarketModel.getRootData().getCryptoCurrencyList();

                    // sort Model list by change percent (lowest to highest)
                    Collections.sort(data, new Comparator<DataItem>() {
                        @Override
                        public int compare(DataItem o1, DataItem o2) {
                            return Integer.valueOf((int) o1.getListQuote().get(0).getPercentChange24h()).compareTo((int) o2.getListQuote().get(0).getPercentChange24h());
                        }
                    });

                    try {
                        ArrayList<DataItem> dataItems = new ArrayList<>();
                        //if page was top Gainers
                        if (pos == 0){
                            //get 10 last Item
                            for (int i = 0;i < 10;i++){
                                dataItems.add(data.get(data.size() - 1 - i));
                            }

                            //if page was top Losers
                        }else if (pos == 1){
                            //get 10 first Item
                            for (int i = 0;i < 10;i++){
                                dataItems.add(data.get(i));
                            }
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        fragmentTopGainLoseBinding.gainLoseRv.setLayoutManager(linearLayoutManager);

                        if (fragmentTopGainLoseBinding.gainLoseRv.getAdapter() == null){
                            gainLoseRvAdapter = new GainLoseRvAdapter(dataItems);
                            fragmentTopGainLoseBinding.gainLoseRv.setAdapter(gainLoseRvAdapter);
                        }else {
                            gainLoseRvAdapter = (GainLoseRvAdapter) fragmentTopGainLoseBinding.gainLoseRv.getAdapter();
                            gainLoseRvAdapter.updateData(dataItems);
                        }
                        fragmentTopGainLoseBinding.gainloseTashieLoader.setVisibility(View.GONE);

                    }catch (Exception e){
                        Log.e("exception", "setupRecyclerView: " + e.getMessage());
                    }

                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}