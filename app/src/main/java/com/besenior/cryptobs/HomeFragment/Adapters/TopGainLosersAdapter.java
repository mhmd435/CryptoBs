package com.besenior.cryptobs.HomeFragment.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.besenior.cryptobs.HomeFragment.TopGainLoseFrag;

import org.jetbrains.annotations.NotNull;

public class TopGainLosersAdapter extends FragmentStateAdapter {

    public TopGainLosersAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
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
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
