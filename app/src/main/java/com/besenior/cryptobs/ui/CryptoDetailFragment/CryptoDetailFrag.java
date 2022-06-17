package com.besenior.cryptobs.ui.CryptoDetailFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.besenior.cryptobs.MainActivity;
import com.besenior.cryptobs.R;
import com.besenior.cryptobs.databinding.FragmentCryptoDetailBinding;
import com.bumptech.glide.Glide;
import com.besenior.cryptobs.model.cryptolistmodel.DataItem;

import com.besenior.cryptobs.viewmodel.CryptoDetailViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import me.ibrahimsn.lib.SmoothBottomBar;

public class CryptoDetailFrag extends Fragment {
    private FirebaseAnalytics mFirebaseAnalytics;

    boolean watchlistIsChecked = false;

    ArrayList<String> bookmarksArray;

    SmoothBottomBar bottomNavigationBar;


    FragmentCryptoDetailBinding fragmentCryptoDetailBinding;
    CryptoDetailViewModel viewmodel;

    ArrayList<String> detailkeysArray;
    ArrayList<String> detailValuessArray;
    DetailRvAdapter detailRvAdapter;

    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setLocale(mainActivity,"en");

        fragmentCryptoDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_crypto_detail, container, false);

        // setup FireBase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());

        HideBottomNavigationbar();


        // create factory for pass another Args to ViewModel
//        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
//            @NonNull
//            @Override
//            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//                return (T) new CryptoDetailViewModel(getActivity().getApplication(),fragmentCryptoDetailBinding);
//            }
//        };

        // ViewModel Setup
        viewmodel = new ViewModelProvider(requireActivity()).get(CryptoDetailViewModel.class);
        fragmentCryptoDetailBinding.setViewmodel(viewmodel);

        // get Data from Bundle and set to databinding
        DataItem dataItem = getArguments().getParcelable("model");
        fragmentCryptoDetailBinding.setModel(dataItem);


        seeCoin_logEventAnalytics(dataItem);
        setupCoinLogo(dataItem);
        setupBackButtonOnClick();
        onWatchListClick(dataItem);
        setupAllText(dataItem);
        setupchart(dataItem);
        setupRecyclerView(dataItem);
        setupfullScreenTxt(dataItem);

        // Inflate the layout for this fragment
        return fragmentCryptoDetailBinding.getRoot();
    }

    public static void setLocale(MainActivity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void HideBottomNavigationbar() {
        bottomNavigationBar = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationBar.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    private void seeCoin_logEventAnalytics(DataItem dataItem) {
        Bundle params = new Bundle();
        params.putString("Coin_name", dataItem.getName());
        params.putString("Coin_symbol", dataItem.getSymbol());
        mFirebaseAnalytics.logEvent("Watched_Coins", params);
    }

    private void addCoin_logEventAnalytics(DataItem dataItem) {
        Bundle params = new Bundle();
        params.putString("Coin_name", dataItem.getName());
        params.putString("Coin_symbol", dataItem.getSymbol());
        mFirebaseAnalytics.logEvent("add_to_watchlist", params);
    }

    private void setupCoinLogo(DataItem dataItem) {
        Glide.with(fragmentCryptoDetailBinding.getRoot().getContext())
                .load("https://s2.coinmarketcap.com/static/img/coins/64x64/" + dataItem.getId() + ".png")
                .thumbnail(Glide.with(fragmentCryptoDetailBinding.getRoot().getContext()).load(R.drawable.loading))
                .into(fragmentCryptoDetailBinding.detailCoinLogo);
    }

    private void setupRecyclerView(DataItem dataItem) {
        fillkeysArray();
        fillValuesArray(dataItem);
        detailRvAdapter = new DetailRvAdapter(detailkeysArray,detailValuessArray);
        fragmentCryptoDetailBinding.derailRV.setAdapter(detailRvAdapter);


    }

    //set diffrent decimals for diffrent price
    private String SetDecimals(double price) {
        if (price < 1){
            return "$" + String.format("%.6f",price);
        }else if (price < 10){
            return "$" + String.format("%.4f",price);
        }else {
            return "$" + String.format("%.2f",price);
        }
    }

    private void fillValuesArray(DataItem dataItem) {
        detailValuessArray = new ArrayList<>();

        // fix numbers
        String high24 = SetDecimals(dataItem.getHigh24h());
        String low24 = SetDecimals(dataItem.getLow24h());
        String ath = SetDecimals(dataItem.getAth());
        String atl = SetDecimals(dataItem.getAtl());
        // remove float section and get a int
        String marketCap = dataItem.getListQuote().get(0).getMarketCap().toString().split("\\.")[0];
        String volume24 = dataItem.getListQuote().get(0).getVolume24h().toString().split("\\.")[0];
        String totalsupply = dataItem.getTotalSupply().toString().split("\\.")[0];


        Log.e("TAG", "MarketCap detail: " + dataItem.getListQuote().get(0).getMarketCap());
        detailValuessArray.add(dataItem.getName());
        detailValuessArray.add("$" + marketCap);
        detailValuessArray.add("$" + volume24);
        detailValuessArray.add(String.format("%.2f", dataItem.getListQuote().get(0).getDominance()) + "%");
        detailValuessArray.add(String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange7d()));
        detailValuessArray.add(String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange30d()));
        detailValuessArray.add(high24);
        detailValuessArray.add(low24);
        detailValuessArray.add(ath);
        detailValuessArray.add(atl);
        detailValuessArray.add(totalsupply);
    }

    private void fillkeysArray() {
        detailkeysArray = new ArrayList<>();
        detailkeysArray.add("Name");
        detailkeysArray.add("Market Cap");
        detailkeysArray.add("Volume 24h");
        detailkeysArray.add("Dominance");
        detailkeysArray.add("PercentChange 7d");
        detailkeysArray.add("PercentChange 30d");
        detailkeysArray.add("High 24h");
        detailkeysArray.add("Low 24h");
        detailkeysArray.add("All Time High");
        detailkeysArray.add("All Time Low");
        detailkeysArray.add("Total Supply");
    }

    private void setupBackButtonOnClick() {
        fragmentCryptoDetailBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
    }


    // set Price and PriceChange
    private void setupAllText(DataItem dataItem) {
        fragmentCryptoDetailBinding.detailSymbol.setText(dataItem.getSymbol() + "/USD");
        SetDecimalsForPrice(dataItem);
        SetColorText(dataItem);

        //Chech price for set price change and price change Icon (red or green)
        if (dataItem.getListQuote().get(0).getPercentChange24h() > 0) {
            fragmentCryptoDetailBinding.detailPriceChangeIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
            fragmentCryptoDetailBinding.detailCoinChange.setText(String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
        } else if (dataItem.getListQuote().get(0).getPercentChange24h() < 0) {
            fragmentCryptoDetailBinding.detailPriceChangeIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
            fragmentCryptoDetailBinding.detailCoinChange.setText(String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
        } else {
            fragmentCryptoDetailBinding.detailCoinChange.setText(String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
        }

    }

    // setup Chart (WebView) for first Time
    private void setupchart(DataItem dataItem) {
        fragmentCryptoDetailBinding.detaillChart.getSettings().setJavaScriptEnabled(true);
        fragmentCryptoDetailBinding.detaillChart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        fragmentCryptoDetailBinding.detaillChart.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT");

    }

    //set diffrent decimals for diffrent price
    private void SetDecimalsForPrice(DataItem dataItem) {
        if (dataItem.getListQuote().get(0).getPrice() < 1) {
            fragmentCryptoDetailBinding.detailCoinPrice.setText("$" + String.format("%.6f", dataItem.getListQuote().get(0).getPrice()));
        } else if (dataItem.getListQuote().get(0).getPrice() < 10) {
            fragmentCryptoDetailBinding.detailCoinPrice.setText("$" + String.format("%.4f", dataItem.getListQuote().get(0).getPrice()));
        } else {
            fragmentCryptoDetailBinding.detailCoinPrice.setText("$" + String.format("%.2f", dataItem.getListQuote().get(0).getPrice()));
        }
    }

    //set Color Green and Red for price
    private void SetColorText(DataItem dataItem) {
        if (dataItem.getListQuote().get(0).getPercentChange24h() < 0) {
            fragmentCryptoDetailBinding.detailCoinChange.setTextColor(Color.RED);
        } else if (dataItem.getListQuote().get(0).getPercentChange24h() > 0) {
            fragmentCryptoDetailBinding.detailCoinChange.setTextColor(Color.GREEN);
        } else {
            fragmentCryptoDetailBinding.detailCoinChange.setTextColor(Color.WHITE);
        }
    }

    // setup Click for bookmark Btn
    public void onWatchListClick(DataItem dataItem) {

        ReadDataStore();

        // show diffrent Icons when get data from shared Prefrence
        if (bookmarksArray.contains(dataItem.getSymbol())) {
            watchlistIsChecked = true;
            fragmentCryptoDetailBinding.bookmarkbtn.setImageResource(R.drawable.ic_baseline_star_rate_24);
        } else {
            watchlistIsChecked = false;
            fragmentCryptoDetailBinding.bookmarkbtn.setImageResource(R.drawable.ic_baseline_star_outline_24);
        }


        // write or delete data from Shared with click on bookmark Btn
        fragmentCryptoDetailBinding.bookmarkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (watchlistIsChecked == false) {
                    if (!bookmarksArray.contains(dataItem.getSymbol())){
                        bookmarksArray.add(dataItem.getSymbol());
                    }
                    writetoDataStore();
                    fragmentCryptoDetailBinding.bookmarkbtn.setImageResource(R.drawable.ic_baseline_star_rate_24);
                    watchlistIsChecked = true;

                    //send event to firebase analytics
                    addCoin_logEventAnalytics(dataItem);
                } else {
                    fragmentCryptoDetailBinding.bookmarkbtn.setImageResource(R.drawable.ic_baseline_star_outline_24);
                    //clear bookmark
                    bookmarksArray.remove(dataItem.getSymbol());
                    writetoDataStore();
                    watchlistIsChecked = false;
                }
            }


        });
    }

    // read new BookMark on Shared Prefrence
    private void ReadDataStore() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("bookmarks", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        bookmarksArray = gson.fromJson(json, type);
        Log.e("TAG", "ReadDataStore: " + bookmarksArray);
    }

    // Write BookMarks ArrayList From Shared Prefrence
    private void writetoDataStore() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(bookmarksArray);

        editor.putString("bookmarks", json);
        editor.apply();
    }

    private void setupfullScreenTxt(DataItem dataItem) {
        String udata="fullscreen";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        fragmentCryptoDetailBinding.fullscreenTxt.setText(content);

        fragmentCryptoDetailBinding.fullscreenTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model", dataItem);

                Navigation.findNavController(v).navigate(R.id.action_cryptoDetailFragment_to_landScapChartFrag,bundle);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bottomNavigationBar.setVisibility(View.VISIBLE);
    }
}