package com.example.crypto_app.HomeFragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.crypto_app.HomeFragment.Adapters.TopCoinRvAdapter;
import com.example.crypto_app.HomeFragment.Adapters.TopGainLosersAdapter;
import com.example.crypto_app.HomeFragment.Adapters.sliderImageAdapter;
import com.example.crypto_app.HomeFragment.Model.AllMarketModel;
import com.example.crypto_app.HomeFragment.Model.DataItem;
import com.example.crypto_app.HomeFragment.Model.SliderImageModel;
import com.example.crypto_app.R;
import com.example.crypto_app.RoomDb.RoomMarketEntity;
import com.example.crypto_app.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    public List<String> top_wants = Arrays.asList("BTC","ETH","BNB","ADA","XRP","DOGE","DOT","UNI","LTC","LINK");

    FragmentHomeBinding fragmentHomeBinding;
    HomeViewModel homeViewModel;

    CompositeDisposable compositeDisposable;
    TopCoinRvAdapter topCoinRvAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        compositeDisposable = new CompositeDisposable();

        // Inflate the layout for this fragment by dataBinding
        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);

        setupViewModel();
        setupImageSlider();
        callApiReqoest();
        getAllMarketDataFromDb();

        return fragmentHomeBinding.getRoot();
    }

    private void setupTablayoutTapGainLose(View topGainIndicator, View topLoseIndicator, AllMarketModel allMarketModel) {

        fragmentHomeBinding.viewPager2.setAdapter(new TopGainLosersAdapter(this,allMarketModel));
        //Anim for In and out view tablayout
        Animation gainAnimIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_from_left);
        Animation gainAnimOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_out_left);
        Animation loseAnimIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_from_right);
        Animation loseAnimOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_out_right);

        fragmentHomeBinding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0){
                    topLoseIndicator.startAnimation(loseAnimOut);
                    topLoseIndicator.setVisibility(View.GONE);
                    topGainIndicator.setVisibility(View.VISIBLE);
                    topGainIndicator.startAnimation(gainAnimIn);

                }else {
                    topGainIndicator.startAnimation(gainAnimOut);
                    topGainIndicator.setVisibility(View.GONE);
                    topLoseIndicator.setVisibility(View.VISIBLE);
                    topLoseIndicator.startAnimation(loseAnimIn);
                }
            }
        });

        new TabLayoutMediator(fragmentHomeBinding.tablayout, fragmentHomeBinding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("TopGainers");
                }else {
                    tab.setText("TopLosers");
                }
            }
        }).attach();
    }

    private void callApiReqoest() {
        try {
            //disposable for avoid memory leak
            Disposable disposable = homeViewModel.makeFutureQuery().get()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<AllMarketModel>() {
                        @Override
                        public void accept(AllMarketModel allMarketModel) throws Throwable {
                            homeViewModel.insertAllMarket(allMarketModel);
                        }
                    });
            compositeDisposable.add(disposable);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getAllMarketDataFromDb() {
        Disposable disposable = homeViewModel.getAllMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RoomMarketEntity>() {
                    @Override
                    public void accept(RoomMarketEntity roomMarketEntity) throws Throwable {
                        AllMarketModel allMarketModel = roomMarketEntity.getAllMarketModel();

                        ArrayList<DataItem> top10 = new ArrayList<>();
                        top10.clear();
                        for (int i = 0;i < allMarketModel.getData().size();i++){
                            for (int j = 0;j < top_wants.size();j++){
                                String coin_name = top_wants.get(j);
                                if (allMarketModel.getData().get(i).getSymbol().equals(coin_name)){
                                    DataItem dataItem = allMarketModel.getData().get(i);
                                    top10.add(dataItem);
                                }
                            }
                        }

                        setupTablayoutTapGainLose(fragmentHomeBinding.topGainIndicator,fragmentHomeBinding.topLoseIndicator,allMarketModel);
                        topCoinRvAdapter = new TopCoinRvAdapter(top10);
                        fragmentHomeBinding.TopCoinRv.setAdapter(topCoinRvAdapter);

                    }
                });
        compositeDisposable.add(disposable);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}