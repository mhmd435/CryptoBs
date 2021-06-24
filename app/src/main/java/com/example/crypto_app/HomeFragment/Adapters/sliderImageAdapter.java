package com.example.crypto_app.HomeFragment.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_app.HomeFragment.Model.SliderImageModel;
import com.example.crypto_app.R;
import com.example.crypto_app.databinding.SliderImageItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class sliderImageAdapter extends RecyclerView.Adapter<sliderImageAdapter.SliderImageViewHolder> {

    LayoutInflater layoutInflater;

    ArrayList<SliderImageModel> arrayList;

    public sliderImageAdapter(ArrayList arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public SliderImageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SliderImageItemBinding sliderImageItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.slider_image_item,parent,false);

        return new SliderImageViewHolder(sliderImageItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SliderImageViewHolder holder, int position) {
        SliderImageModel sliderImageModel = arrayList.get(position);
        holder.bind(sliderImageModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class SliderImageViewHolder extends RecyclerView.ViewHolder{
        SliderImageItemBinding sliderImageItemBinding;

        public SliderImageViewHolder(@NonNull @NotNull SliderImageItemBinding sliderImageItemBinding) {
            super(sliderImageItemBinding.getRoot());
            this.sliderImageItemBinding = sliderImageItemBinding;
        }

        public void bind(SliderImageModel model) {
            sliderImageItemBinding.viewfading.setVisibility(View.VISIBLE);
            sliderImageItemBinding.imageSlide.setImageResource(model.getImage());
            sliderImageItemBinding.executePendingBindings();
        }
    }
}
