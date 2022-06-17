package com.besenior.cryptobs.HomeFragment.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.besenior.cryptobs.model.SliderImageModel;
import com.bumptech.glide.Glide;
import com.besenior.cryptobs.R;
import com.besenior.cryptobs.databinding.SliderImageItemBinding;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class sliderImageAdapter extends RecyclerView.Adapter<sliderImageAdapter.SliderImageViewHolder> {

    LayoutInflater layoutInflater;

    ArrayList<SliderImageModel.PageViewImage> arrayList;
    ArrayList<Integer> images;

    public sliderImageAdapter(ArrayList<SliderImageModel.PageViewImage> arrayList) {
        this.arrayList = arrayList;

        images = new ArrayList<>();
        images.add(R.drawable.a1);
        images.add(R.drawable.a2);
        images.add(R.drawable.a3);
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
//        holder.bind(arrayList.get(position));
        holder.bind(images.get(position));
    }

    @Override
    public int getItemCount() {
        return 3;
//        return arrayList.size();
    }

    static class SliderImageViewHolder extends RecyclerView.ViewHolder{
        SliderImageItemBinding sliderImageItemBinding;

        public SliderImageViewHolder(@NonNull @NotNull SliderImageItemBinding sliderImageItemBinding) {
            super(sliderImageItemBinding.getRoot());
            this.sliderImageItemBinding = sliderImageItemBinding;
        }

        public void bind(Integer integer) {
            Glide.with(sliderImageItemBinding.getRoot().getContext())
                    .load(integer)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(sliderImageItemBinding.imageSlide);
            sliderImageItemBinding.executePendingBindings();

            //Gone visibility loading and show content
            GoneLoader();
        }

        public void GoneLoader() {
            sliderImageItemBinding.viewpagerTashieLoader.setVisibility(View.GONE);
            sliderImageItemBinding.viewfading.setVisibility(View.VISIBLE);
            sliderImageItemBinding.imageSlide.setVisibility(View.VISIBLE);
        }
    }
}
