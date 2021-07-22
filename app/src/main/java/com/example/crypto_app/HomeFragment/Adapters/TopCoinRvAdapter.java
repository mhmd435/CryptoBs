package com.example.crypto_app.HomeFragment.Adapters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_app.Model.CryptoListModel.DataItem;
import com.example.crypto_app.R;
import com.example.crypto_app.databinding.TopmarketItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TopCoinRvAdapter extends RecyclerView.Adapter<TopCoinRvAdapter.TopCoinRvHolder>{

    LayoutInflater layoutInflater;
    ArrayList<DataItem> dataItems;
    int[] icon_logo = {R.drawable.btc,R.drawable.eth,R.drawable.bnb,R.drawable.ada,R.drawable.doge,R.drawable.xrp,R.drawable.dot,R.drawable.uni,R.drawable.ltc,R.drawable.link};


    public TopCoinRvAdapter(ArrayList<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @NotNull
    @Override
    public TopCoinRvHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        TopmarketItemBinding topmarketItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.topmarket_item,parent,false);
        return new TopCoinRvHolder(topmarketItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TopCoinRvHolder holder, int position) {
        holder.bind(dataItems.get(position),icon_logo[position]);
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


    static class TopCoinRvHolder extends RecyclerView.ViewHolder{

        TopmarketItemBinding topmarketItemBinding;

        public TopCoinRvHolder(TopmarketItemBinding topmarketItemBinding) {
            super(topmarketItemBinding.getRoot());
            this.topmarketItemBinding = topmarketItemBinding;
        }

        public void bind(DataItem dataItem, int icon_logo){
            SetColorText(dataItem);
            SetDecimalsForPrice(dataItem);
            topmarketItemBinding.TopCoinName.setText(String.format("%s/USD", dataItem.getSymbol()));
            if (dataItem.getQuote().getUSD().getPercentChange24h() > 0){
                topmarketItemBinding.TopCoinChange.setText("+" + String.format("%.2f",dataItem.getQuote().getUSD().getPercentChange24h()) + "%");
            }else {
                topmarketItemBinding.TopCoinChange.setText(String.format("%.2f",dataItem.getQuote().getUSD().getPercentChange24h()) + "%");
            }
            topmarketItemBinding.coinlogo.setImageResource(icon_logo);
            topmarketItemBinding.executePendingBindings();
        }

        public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

        public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }


        //set diffrent decimals for diffrent price
        private void SetDecimalsForPrice(DataItem dataItem) {
            if (dataItem.getQuote().getUSD().getPrice() < 1){
                topmarketItemBinding.TopCoinPrice.setText(String.format("%.6f",dataItem.getQuote().getUSD().getPrice()));
            }else if (dataItem.getQuote().getUSD().getPrice() < 10){
                topmarketItemBinding.TopCoinPrice.setText(String.format("%.4f",dataItem.getQuote().getUSD().getPrice()));
            }else {
                topmarketItemBinding.TopCoinPrice.setText(String.format("%.2f",dataItem.getQuote().getUSD().getPrice()));
            }
        }

        //set Color Green and Red for price
        private void SetColorText(DataItem dataItem){
            if (dataItem.getQuote().getUSD().getPercentChange24h() < 0){
                topmarketItemBinding.TopCoinChange.setTextColor(Color.RED);
                topmarketItemBinding.TopCoinPrice.setTextColor(Color.RED);
            }else {
                topmarketItemBinding.TopCoinChange.setTextColor(Color.GREEN);
                topmarketItemBinding.TopCoinPrice.setTextColor(Color.GREEN);
            }
        }

    }
}
