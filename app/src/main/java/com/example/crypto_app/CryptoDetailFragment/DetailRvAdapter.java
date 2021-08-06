package com.example.crypto_app.CryptoDetailFragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_app.R;
import com.example.crypto_app.databinding.DetailfragRvItemsBinding;

import java.util.ArrayList;

public class DetailRvAdapter extends RecyclerView.Adapter<DetailRvAdapter.DetailRvHolder>{
    ArrayList<String> keys;
    ArrayList<String> values;
    LayoutInflater layoutInflater;

    public DetailRvAdapter(ArrayList<String> keys, ArrayList<String> values) {
        this.keys = keys;
        this.values = values;
    }

    @NonNull
    @Override
    public DetailRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        DetailfragRvItemsBinding detailfragRvItemsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.detailfrag_rv_items,parent,false);
        return new DetailRvHolder(detailfragRvItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailRvHolder holder, int position) {
        holder.bind(keys.get(position),values.get(position));

    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    static class DetailRvHolder extends RecyclerView.ViewHolder {

        DetailfragRvItemsBinding detailfragRvItemsBinding;

        public DetailRvHolder(@NonNull DetailfragRvItemsBinding detailfragRvItemsBinding) {
            super(detailfragRvItemsBinding.getRoot());
            this.detailfragRvItemsBinding = detailfragRvItemsBinding;
        }

        public void bind(String key, String value){
            if (key.equals("PercentChange 7d") || key.equals("PercentChange 30d")){
                setColorText(value,detailfragRvItemsBinding.detailvalues);
                detailfragRvItemsBinding.detailvalues.setText(value + "%");
            }else {
                detailfragRvItemsBinding.detailvalues.setText(value);
            }
            detailfragRvItemsBinding.detailkeys.setText(key);
            detailfragRvItemsBinding.executePendingBindings();
        }

        private void setColorText(String value, TextView detailvalues) {
            if (Float.parseFloat(value) < 0){
                detailvalues.setTextColor(Color.RED);
            }else if (Float.parseFloat(value) > 0){
                detailvalues.setTextColor(Color.GREEN);
            }else {
                detailvalues.setTextColor(Color.WHITE);
            }

        }
    }
}
