package com.example.crypto_app.HomeFragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.crypto_app.HomeFragment.Model.AllMarketModel;
import com.example.crypto_app.HomeFragment.Model.SliderImageModel;
import com.example.crypto_app.HomeFragment.Repository.HomeRepository;
import com.example.crypto_app.R;
import com.example.crypto_app.RoomDb.RoomMarketEntity;

import java.util.ArrayList;
import java.util.concurrent.Future;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public class HomeViewModel extends AndroidViewModel {

    private HomeRepository repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = HomeRepository.getInstance(application);
    }

    public Future<Observable<AllMarketModel>> makeFutureQuery(){
        return repository.makeFutureQuery();
    }

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

    public void insertAllMarket(AllMarketModel allMarketModel){
        repository.InsertAllMarket(allMarketModel);
    }

    public Flowable<RoomMarketEntity> getAllMarketData(){
        return repository.getAllMarketData();
    }


}
