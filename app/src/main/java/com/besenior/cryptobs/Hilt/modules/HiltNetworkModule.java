package com.besenior.cryptobs.Hilt.modules;

import android.app.Activity;
import android.app.Application;

import androidx.databinding.DataBindingUtil;

import com.besenior.cryptobs.HomeFragment.Repository.HomeRepository;
import com.besenior.cryptobs.R;
import com.besenior.cryptobs.RoomDb.RoomDao;
import com.besenior.cryptobs.databinding.ActivityMainBinding;
import com.besenior.cryptobs.retrofit.RequestApi;
import com.besenior.cryptobs.retrofit.ServiceGenerator;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class HiltNetworkModule {

    String Base_Url = "https://pro-api.coinmarketcap.com";

    @Provides
    @Singleton
    OkHttpClient ProvideOkHttpClient(){
        return new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("X-CMC_PRO_API_KEY","2f7b1b18-cbeb-4861-8515-bca6b05e3777")
                        .build();
                return chain.proceed(request);
            }
        }).build();
    }

    @Provides
    @Singleton
    Retrofit ProvideRetrofitBuilder(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(Base_Url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    RequestApi ProvideRequestApi(Retrofit retrofit){
        return retrofit.create(RequestApi.class);
    }

    @Provides
    @Singleton
    HomeRepository ProvideHomeRepository(RequestApi requestApi, RoomDao roomDao,@Named("networkCompositeDisposable") CompositeDisposable compositeDisposable){
        return new HomeRepository(requestApi,roomDao,compositeDisposable);
    }


    @Provides
    @Singleton
    @Named("networkCompositeDisposable")
    CompositeDisposable ProvideCompositeDisposable(){
        return new CompositeDisposable();
    }


}
