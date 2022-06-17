package com.besenior.cryptobs.ui.watchlistfragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.besenior.cryptobs.model.cryptolistmodel.DataItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.besenior.cryptobs.R;
import com.besenior.cryptobs.databinding.WatchlistRvItemBinding;

import java.util.ArrayList;

public class watchlistRV_adapter extends RecyclerView.Adapter<watchlistRV_adapter.watchlistRV_holder>{

    ArrayList<DataItem> dataItems;
    LayoutInflater layoutInflater;

    public watchlistRV_adapter(ArrayList<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public watchlistRV_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        WatchlistRvItemBinding watchlistRvItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.watchlist_rv_item,parent,false);
        return new watchlistRV_holder(watchlistRvItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull watchlistRV_holder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(dataItems.get(position));
        holder.watchlistRvItemBinding.watchedCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model", dataItems.get(position));

                Navigation.findNavController(v).navigate(R.id.action_watchlistFragment_to_cryptoDetailFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public void updateData(ArrayList<DataItem> newdata) {
        dataItems = newdata;
        notifyDataSetChanged();
    }

    static class watchlistRV_holder extends RecyclerView.ViewHolder {

         WatchlistRvItemBinding watchlistRvItemBinding;

        public watchlistRV_holder(@NonNull WatchlistRvItemBinding watchlistRvItemBinding) {
            super(watchlistRvItemBinding.getRoot());
            this.watchlistRvItemBinding = watchlistRvItemBinding;
        }

        public void bind(DataItem dataItem){
            loadCoinlogo(dataItem);
            loadCoinChart(dataItem);
            SetColorText(dataItem);
            SetDecimalsForPrice(dataItem);
            watchlistRvItemBinding.watchedCoinName.setText(dataItem.getSymbol());
            watchlistRvItemBinding.watchedCoinFullname.setText(dataItem.getName());
            //set + or - before precent change
            if (dataItem.getListQuote().get(0).getPercentChange24h() > 0){
                watchlistRvItemBinding.watchedCoinIconChange.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
                watchlistRvItemBinding.watchedCoinChange.setText(String.format("%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }else if (dataItem.getListQuote().get(0).getPercentChange24h() < 0){
                watchlistRvItemBinding.watchedCoinIconChange.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                watchlistRvItemBinding.watchedCoinChange.setText(String.format("%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }else {
                watchlistRvItemBinding.watchedCoinChange.setText(String.format("%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }
            watchlistRvItemBinding.executePendingBindings();
        }

        private void loadCoinlogo(DataItem dataItem) {
            Glide.with(watchlistRvItemBinding.getRoot().getContext())
                    .load("https://s2.coinmarketcap.com/static/img/coins/64x64/" + dataItem.getId() + ".png")
                    .thumbnail(Glide.with(watchlistRvItemBinding.getRoot().getContext()).load(R.drawable.loading))
                    .into(watchlistRvItemBinding.coinwatchlogo);
        }


        private void loadCoinChart(DataItem dataItem) {
            Glide.with(watchlistRvItemBinding.getRoot().getContext())
                    .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + dataItem.getId() + ".png")
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(watchlistRvItemBinding.watchedCoinChart);
        }

        //set Color Green and Red for price and chart
        private void SetColorText(DataItem dataItem){
            int greenColor = Color.parseColor("#FF00FF40");
            int redColor = Color.parseColor("#FFFF0000");
            int whiteColor = Color.parseColor("#FFFFFF");
            if (dataItem.getListQuote().get(0).getPercentChange24h() < 0){
                watchlistRvItemBinding.watchedCoinChart.setColorFilter(redColor);
                watchlistRvItemBinding.watchedCoinChange.setTextColor(Color.RED);
            }else if (dataItem.getListQuote().get(0).getPercentChange24h() > 0){
                watchlistRvItemBinding.watchedCoinChart.setColorFilter(greenColor);
                watchlistRvItemBinding.watchedCoinChange.setTextColor(Color.GREEN);
            }else {
                watchlistRvItemBinding.watchedCoinChart.setColorFilter(whiteColor);
                watchlistRvItemBinding.watchedCoinChange.setTextColor(Color.WHITE);
            }
        }

        //set diffrent decimals for diffrent price
        private void SetDecimalsForPrice(DataItem dataItem) {
            if (dataItem.getListQuote().get(0).getPrice() < 1){
                watchlistRvItemBinding.watchedCoinPrice.setText("$" + String.format("%.6f",dataItem.getListQuote().get(0).getPrice()));
            }else if (dataItem.getListQuote().get(0).getPrice() < 10){
                watchlistRvItemBinding.watchedCoinPrice.setText("$" + String.format("%.4f",dataItem.getListQuote().get(0).getPrice()));
            }else {
                watchlistRvItemBinding.watchedCoinPrice.setText("$" + String.format("%.2f",dataItem.getListQuote().get(0).getPrice()));
            }
        }
    }
}
