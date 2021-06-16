package com.example.crypto_app.HomeFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.crypto_app.R;
import com.example.crypto_app.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding fragmentHomeBinding;
    HomeViewModel homeViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setupViewModel();
        setupImageSlider();

        // Inflate the layout for this fragment by dataBinding
        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        return fragmentHomeBinding.getRoot();
    }

    private void setupViewModel() {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    private void setupImageSlider() {
        homeViewModel.getImage().observe(getViewLifecycleOwner(), new Observer<ArrayList<SliderImageModel>>() {
            @Override
            public void onChanged(ArrayList<SliderImageModel> sliderImageModels) {

                fragmentHomeBinding.viewPagerImageSlider.setAdapter(new sliderImageAdapter(sliderImageModels));
                fragmentHomeBinding.viewPagerImageSlider.setOffscreenPageLimit(3);
                fragmentHomeBinding.viewPagerImageSlider.setVisibility(View.VISIBLE);

                setupSliderIndicator(sliderImageModels.size());

                fragmentHomeBinding.viewPagerImageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        setCurrentIndicator(position);
                    }
                });

                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        float r = 1 - Math.abs(position);
                        page.setScaleY(0.85f + r * 0.15f);
                    }
                });

                fragmentHomeBinding.viewPagerImageSlider.setPageTransformer(compositePageTransformer);

            }
        });
    }

    private void setupSliderIndicator(int count){
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,0,0,0);

        for (int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getActivity().getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.slider_indicator_inactive));

            indicators[i].setLayoutParams(layoutParams);
            fragmentHomeBinding.sliderindicator.addView(indicators[i]);
        }
        fragmentHomeBinding.sliderindicator.setVisibility(View.VISIBLE);
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position){
        int childCount = fragmentHomeBinding.sliderindicator.getChildCount();

        for (int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView) fragmentHomeBinding.sliderindicator.getChildAt(i);
            if (i == position){
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.slider_indicator_active));
            }else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.slider_indicator_inactive ));
            }
        }
    }
}