package com.example.crypto_app.CryptoDetailFragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.crypto_app.model.cryptolistmodel.DataItem;
import com.example.crypto_app.R;
import com.example.crypto_app.viewmodel.CryptoDetailViewModel;
import com.example.crypto_app.databinding.FragmentCryptoDetailBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CryptoDetailFrag extends Fragment {

    boolean watchlistIsChecked = false;

    ArrayList<String> bookmarksArray;

    FragmentCryptoDetailBinding fragmentCryptoDetailBinding;
    CryptoDetailViewModel viewmodel;

    ArrayList<String> detailkeysArray;
    ArrayList<String> detailValuessArray;
    DetailRvAdapter detailRvAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentCryptoDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_crypto_detail, container, false);

        // create factory for pass another Args to ViewModel
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CryptoDetailViewModel(getActivity().getApplication(),fragmentCryptoDetailBinding);
            }
        };

        // ViewModel Setup
        viewmodel = new ViewModelProvider(requireActivity(),factory).get(CryptoDetailViewModel.class);
        fragmentCryptoDetailBinding.setViewmodel(viewmodel);

        // get Data from Bundle and set to databinding
        DataItem dataItem = getArguments().getParcelable("model");
        fragmentCryptoDetailBinding.setModel(dataItem);


        setupCoinLogo(dataItem);
        setupBackButtonOnClick();
        onWatchListClick(dataItem);
        setupAllText(dataItem);
        setupchart(dataItem);
        setupRecyclerView(dataItem);

        // Inflate the layout for this fragment
        return fragmentCryptoDetailBinding.getRoot();
    }

    private void setupCoinLogo(DataItem dataItem) {
        Glide.with(fragmentCryptoDetailBinding.getRoot().getContext())
                .load("https://s2.coinmarketcap.com/static/img/coins/64x64/" + dataItem.getId() + ".png")
                .transition(DrawableTransitionOptions.withCrossFade())
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

        String high24 = SetDecimals(dataItem.getHigh24h());
        String low24 = SetDecimals(dataItem.getLow24h());
        String ath = SetDecimals(dataItem.getAth());
        String atl = SetDecimals(dataItem.getAtl());

        detailValuessArray.add(dataItem.getName());
        detailValuessArray.add(String.valueOf(dataItem.getListQuote().get(0).getMarketCap()));
        detailValuessArray.add(String.format("%.2f", dataItem.getListQuote().get(0).getDominance()) + "%");
        detailValuessArray.add(String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange7d()));
        detailValuessArray.add(String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange30d()));
        detailValuessArray.add(high24);
        detailValuessArray.add(low24);
        detailValuessArray.add(ath);
        detailValuessArray.add(atl);
        detailValuessArray.add(String.valueOf(dataItem.getTotalSupply()));
    }

    private void fillkeysArray() {
        detailkeysArray = new ArrayList<>();
        detailkeysArray.add("Name");
        detailkeysArray.add("Market Cap");
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
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
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
}