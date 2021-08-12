package com.example.crypto_app.HomeFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.crypto_app.viewmodel.AppViewModel;
import com.example.crypto_app.HomeFragment.Adapters.TopCoinRvAdapter;
import com.example.crypto_app.HomeFragment.Adapters.TopGainLosersAdapter;
import com.example.crypto_app.HomeFragment.Adapters.sliderImageAdapter;
import com.example.crypto_app.model.cryptolistmodel.AllMarketModel;
import com.example.crypto_app.model.cryptolistmodel.DataItem;
import com.example.crypto_app.model.SliderImageModel;
import com.example.crypto_app.MainActivity;
import com.example.crypto_app.R;
import com.example.crypto_app.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    public List<String> top_wants = Arrays.asList("BTC","ETH","BNB","ADA","XRP","DOGE","DOT","UNI","LTC","LINK");

    FragmentHomeBinding fragmentHomeBinding;
    AppViewModel appViewModel;

    MainActivity mainActivity;
    CompositeDisposable compositeDisposable;
    TopCoinRvAdapter topCoinRvAdapter;
    TopGainLosersAdapter topGainLosersAdapter;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        compositeDisposable = new CompositeDisposable();


        // Inflate the layout for this fragment by dataBinding
        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);

        setupViewModel();
        setupImageSlider();
        setupSwipeRefreshLayout();
        getAllMarketDataFromDb();
        setupTablayoutTapGainLose(fragmentHomeBinding.topGainIndicator,fragmentHomeBinding.topLoseIndicator);

        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();
        Toolbar toolbar = view.findViewById(R.id.toolbar);


        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if (destination.getId() == R.id.homeFragment){
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_35);
                    toolbar.setTitle("CryptoBs");
                }
            }
        });
    }

    private void setupSwipeRefreshLayout() {
        fragmentHomeBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragmentHomeBinding.swipeRefresh.setRefreshing(false);
                    }
                },1500);
                mainActivity.callAllApiReqoest();
            }
        });
    }



    private void setupTablayoutTapGainLose(View topGainIndicator, View topLoseIndicator) {
            topGainLosersAdapter = new TopGainLosersAdapter(this);
            fragmentHomeBinding.viewPager2.setAdapter(topGainLosersAdapter);


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

        new TabLayoutMediator(fragmentHomeBinding.tablayout, fragmentHomeBinding.viewPager2, (tab, position) -> {
            if (position == 0){
                tab.setText("TopGainers");
            }else {
                tab.setText("TopLosers");
            }
        }).attach();
    }


    private void getAllMarketDataFromDb() {
        Disposable disposable = appViewModel.getAllMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(roomMarketEntity -> {
                    AllMarketModel allMarketModel = roomMarketEntity.getAllMarketModel();

                    ArrayList<DataItem> top10 = new ArrayList<>();
                    for (int i = 0;i < allMarketModel.getRootData().getCryptoCurrencyList().size();i++){
                        for (int j = 0;j < top_wants.size();j++){
                            String coin_name = top_wants.get(j);
                            if (allMarketModel.getRootData().getCryptoCurrencyList().get(i).getSymbol().equals(coin_name)){
                                DataItem dataItem = allMarketModel.getRootData().getCryptoCurrencyList().get(i);
                                top10.add(dataItem);
                            }
                        }
                    }


                    if (fragmentHomeBinding.TopCoinRv.getAdapter() != null){
                        topCoinRvAdapter = (TopCoinRvAdapter) fragmentHomeBinding.TopCoinRv.getAdapter();
                        topCoinRvAdapter.updateData(top10);
                    }else {
                        topCoinRvAdapter = new TopCoinRvAdapter(top10);
                        fragmentHomeBinding.TopCoinRv.setAdapter(topCoinRvAdapter);
                    }

                });
        compositeDisposable.add(disposable);
    }

    private void setupViewModel() {
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }

    private void setupImageSlider() {
        appViewModel.getImage().observe(getViewLifecycleOwner(), new Observer<ArrayList<SliderImageModel>>() {
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
                compositePageTransformer.addTransformer(new MarginPageTransformer(10));
                compositePageTransformer.addTransformer((page, position) -> {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
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