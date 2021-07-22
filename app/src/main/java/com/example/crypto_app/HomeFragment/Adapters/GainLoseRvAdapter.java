package com.example.crypto_app.HomeFragment.Adapters;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_app.Model.CryptoListModel.DataItem;
import com.example.crypto_app.R;
import com.example.crypto_app.databinding.GainloseRvItemBinding;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GainLoseRvAdapter extends RecyclerView.Adapter<GainLoseRvAdapter.GainLoseRvHolder>{

    ArrayList<DataItem> dataItems;
    LayoutInflater layoutInflater;

    public GainLoseRvAdapter(ArrayList<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @NotNull
    @Override
    public GainLoseRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        GainloseRvItemBinding gainloseRvItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.gainlose_rv_item,parent,false);
        return new GainLoseRvHolder(gainloseRvItemBinding);
    }

    @Override
    public void onBindViewHolder(GainLoseRvHolder holder, int position) {
        holder.bind(dataItems.get(position));
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public void updateData(ArrayList<DataItem> newdata) {
        dataItems.clear();
        dataItems.addAll(newdata);
        notifyDataSetChanged();
    }

    static class GainLoseRvHolder extends RecyclerView.ViewHolder {
        List<Entry> entries;

        GainloseRvItemBinding gainloseRvItemBinding;
        LineDataSet dataSet;

        public GainLoseRvHolder(GainloseRvItemBinding gainloseRvItemBinding) {
            super(gainloseRvItemBinding.getRoot());
            this.gainloseRvItemBinding = gainloseRvItemBinding;
        }

        public void bind(DataItem dataItem){

            setupPriceChart(dataItem);
            SetColorText(dataItem);
            gainloseRvItemBinding.GLCoinName.setText(dataItem.getName());
            gainloseRvItemBinding.GLcoinSymbol.setText(dataItem.getSymbol());
            SetDecimalsForPrice(dataItem);
            //set + or - before precent change
            if (dataItem.getQuote().getUSD().getPercentChange24h() > 0){
                dataSet.setColor(Color.GREEN);
                gainloseRvItemBinding.UpDownIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
                gainloseRvItemBinding.GLcoinChange.setText(String.format("%.2f",dataItem.getQuote().getUSD().getPercentChange24h()) + "%");
            }else if (dataItem.getQuote().getUSD().getPercentChange24h() < 0){
                dataSet.setColor(Color.RED);
                gainloseRvItemBinding.UpDownIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                gainloseRvItemBinding.GLcoinChange.setText(String.format("%.2f",dataItem.getQuote().getUSD().getPercentChange24h()) + "%");
            }else {
                dataSet.setColor(Color.WHITE);
                gainloseRvItemBinding.GLcoinChange.setText(String.format("%.2f",dataItem.getQuote().getUSD().getPercentChange24h()) + "%");
            }
            gainloseRvItemBinding.executePendingBindings();
        }

        //setup chaet setting and style
        private void setupPriceChart(DataItem dataItem) {
            entries = new ArrayList<>();
            for (int i = 1;i < 10;i++){
                entries.add(new Entry(i,new Random().nextInt(100) + 30));
            }

//            find90DaysAgoPrice(dataItem);
            dataSet = new LineDataSet(entries, "Label"); // add entries to dataset

            dataSet.setHighlightEnabled(false);
            dataSet.setDrawCircles(false);
            dataSet.setDrawValues(false);
            dataSet.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                if (dataItem.getQuote().getUSD().getPercentChange24h() < 0){
                    Drawable drawable = ContextCompat.getDrawable(gainloseRvItemBinding.getRoot().getContext(), R.drawable.red_chart_bg);
                    dataSet.setFillDrawable(drawable);
                }else {
                    Drawable drawable = ContextCompat.getDrawable(gainloseRvItemBinding.getRoot().getContext(), R.drawable.green_chart_bg);
                    dataSet.setFillDrawable(drawable);
                }

            }
            else {
                dataSet.setFillColor(Color.BLACK);
            }
            LineData lineData = new LineData(dataSet);
            gainloseRvItemBinding.chart.getXAxis().setEnabled(false);
            gainloseRvItemBinding.chart.getAxisLeft().setEnabled(false);
            gainloseRvItemBinding.chart.getAxisRight().setEnabled(false);
            gainloseRvItemBinding.chart.getDescription().setEnabled(false);
            gainloseRvItemBinding.chart.getLegend().setEnabled(false);
            gainloseRvItemBinding.chart.setData(lineData);
            gainloseRvItemBinding.chart.invalidate();
        }

        private void find90DaysAgoPrice(DataItem dataItem) {
            double currentPrice = dataItem.getQuote().getUSD().getPrice();
            double h1_Price = AlghoritmForFindPrice(currentPrice,dataItem.getQuote().getUSD().getPercentChange1h());
            double h24_Price = AlghoritmForFindPrice(currentPrice, dataItem.getQuote().getUSD().getPercentChange1h());
            double day7_Price = AlghoritmForFindPrice(currentPrice, dataItem.getQuote().getUSD().getPercentChange7d());
            entries = new ArrayList<>();
//            entries.add(new Entry(1, (float) day90_Price));
//            entries.add(new Entry(2, (float) day60_Price));
//            entries.add(new Entry(1, (float) day30_Price));
            entries.add(new Entry(2, (float) day7_Price));
            entries.add(new Entry(3, (float) h24_Price));
            entries.add(new Entry(4, (float) h1_Price));
            entries.add(new Entry(5, (float) currentPrice));

        }

        private double AlghoritmForFindPrice(double currentPrice, double percentChange) {
            if (percentChange > 0){
                return currentPrice - ((currentPrice / 100)*percentChange);
            }else {
                return currentPrice + ((currentPrice / 100)*percentChange);
            }
        }

        //set diffrent decimals for diffrent price
        private void SetDecimalsForPrice(DataItem dataItem) {
            if (dataItem.getQuote().getUSD().getPrice() < 1){
                gainloseRvItemBinding.GLcoinPrice.setText("$" + String.format("%.6f",dataItem.getQuote().getUSD().getPrice()));
            }else if (dataItem.getQuote().getUSD().getPrice() < 10){
                gainloseRvItemBinding.GLcoinPrice.setText("$" + String.format("%.4f",dataItem.getQuote().getUSD().getPrice()));
            }else {
                gainloseRvItemBinding.GLcoinPrice.setText("$" + String.format("%.2f",dataItem.getQuote().getUSD().getPrice()));
            }
        }

        //set Color Green and Red for price
        private void SetColorText(DataItem dataItem){
            if (dataItem.getQuote().getUSD().getPercentChange24h() < 0){
                gainloseRvItemBinding.GLcoinChange.setTextColor(Color.RED);
            }else if (dataItem.getQuote().getUSD().getPercentChange24h() > 0){
                gainloseRvItemBinding.GLcoinChange.setTextColor(Color.GREEN);
            }else {
                gainloseRvItemBinding.GLcoinChange.setTextColor(Color.WHITE);
            }
        }
    }
}
