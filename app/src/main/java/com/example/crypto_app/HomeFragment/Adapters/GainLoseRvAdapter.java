package com.example.crypto_app.HomeFragment.Adapters;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_app.HomeFragment.Model.DataItem;
import com.example.crypto_app.R;
import com.example.crypto_app.databinding.GainloseRvItemBinding;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.model.GradientColor;
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

    static class GainLoseRvHolder extends RecyclerView.ViewHolder {

        GainloseRvItemBinding gainloseRvItemBinding;
        LineDataSet dataSet;

        public GainLoseRvHolder(GainloseRvItemBinding gainloseRvItemBinding) {
            super(gainloseRvItemBinding.getRoot());
            this.gainloseRvItemBinding = gainloseRvItemBinding;
        }

        public void bind(DataItem dataItem){

            setupPriceChart();
            SetColorText(dataItem);

            gainloseRvItemBinding.GLCoinName.setText(dataItem.getSymbol() + "/USD");
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

        private void setupPriceChart() {
            List<Entry> entries = new ArrayList<>();
            for (int i = 1;i < 11;i++){
                entries.add(new Entry(i, new Random().nextInt(50)));
            }
            dataSet = new LineDataSet(entries, "Label"); // add entries to dataset

            dataSet.setHighlightEnabled(false);
            dataSet.setDrawCircles(false);
            dataSet.setDrawValues(false);
//            dataSet.setDrawFilled(true);
//            if (Utils.getSDKInt() >= 18) {
//                // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(gainloseRvItemBinding.getRoot().getContext(), R.drawable.topmarket_bg);
//                dataSet.setFillDrawable(drawable);
//            }
//            else {
//                dataSet.setFillColor(Color.BLACK);
//            }

            LineData lineData = new LineData(dataSet);
            gainloseRvItemBinding.chart.getXAxis().setEnabled(false);

            gainloseRvItemBinding.chart.getAxisLeft().setEnabled(false);
            gainloseRvItemBinding.chart.getAxisRight().setEnabled(false);
            gainloseRvItemBinding.chart.getDescription().setEnabled(false);
            gainloseRvItemBinding.chart.getLegend().setEnabled(false);
            gainloseRvItemBinding.chart.setData(lineData);
            gainloseRvItemBinding.chart.invalidate();
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
