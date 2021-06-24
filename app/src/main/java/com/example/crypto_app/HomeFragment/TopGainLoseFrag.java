package com.example.crypto_app.HomeFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.crypto_app.HomeFragment.Adapters.GainLoseRvAdapter;
import com.example.crypto_app.HomeFragment.Model.AllMarketModel;
import com.example.crypto_app.HomeFragment.Model.DataItem;
import com.example.crypto_app.R;
import com.example.crypto_app.databinding.FragmentTopGainLoseBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class TopGainLoseFrag extends Fragment {

    FragmentTopGainLoseBinding fragmentTopGainLoseBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentTopGainLoseBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_top_gain_lose,container,false);

        // Inflate the layout for this fragment
        return fragmentTopGainLoseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        int pos = args.getInt("pos");
        AllMarketModel allMarketModel = args.getParcelable("data");

        setupRecyclerView(allMarketModel.getData());

    }

    private void setupRecyclerView(List<DataItem> data) {
        ArrayList<DataItem> dataItems = new ArrayList<>();
        for (int i = 0;i < 10;i++){
            dataItems.add(data.get(i));
        }
        fragmentTopGainLoseBinding.gainLoseRv.setAdapter(new GainLoseRvAdapter(dataItems));
    }
}