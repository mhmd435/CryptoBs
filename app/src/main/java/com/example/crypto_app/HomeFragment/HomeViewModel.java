package com.example.crypto_app.HomeFragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.crypto_app.R;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    MutableLiveData<ArrayList<SliderImageModel>> getImage(){
        MutableLiveData<ArrayList<SliderImageModel>> mutableLiveData = new MutableLiveData<>();

        ArrayList<SliderImageModel> arrayList = new ArrayList<>();
        arrayList.add(new SliderImageModel(R.drawable.image1));
        arrayList.add(new SliderImageModel(R.drawable.image2));
        arrayList.add(new SliderImageModel(R.drawable.image3));
        arrayList.add(new SliderImageModel(R.drawable.image4));
        arrayList.add(new SliderImageModel(R.drawable.image5));

        mutableLiveData.postValue(arrayList);
        return mutableLiveData;
    }
}
