package com.example.crypto_app.HomeFragment.Adapters;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.crypto_app.HomeFragment.Model.AllMarketModel;
import com.example.crypto_app.HomeFragment.TopGainLoseFrag;

import org.jetbrains.annotations.NotNull;

public class TopGainLosersAdapter extends FragmentStateAdapter {
    AllMarketModel allMarketModel;

    public TopGainLosersAdapter(@NonNull @NotNull Fragment fragment, AllMarketModel allMarketModel) {
        super(fragment);
        this.allMarketModel = allMarketModel;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        Fragment fragment = new TopGainLoseFrag();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt("pos", position);
        args.putParcelable("data",allMarketModel);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
