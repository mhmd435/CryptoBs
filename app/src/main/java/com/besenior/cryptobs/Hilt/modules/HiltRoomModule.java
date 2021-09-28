package com.besenior.cryptobs.Hilt.modules;

import android.app.Application;
import android.content.Context;

import com.besenior.cryptobs.RoomDb.AppDatabase;
import com.besenior.cryptobs.RoomDb.RoomDao;
import com.besenior.cryptobs.retrofit.RequestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class HiltRoomModule {

    @Provides
    @Singleton
    AppDatabase ProvideAppDatabase(@ApplicationContext Context context){
        return AppDatabase.getInstance(context);
    }


    @Provides
    @Singleton
    RoomDao ProvideRoomDao(AppDatabase appDatabase){
        return appDatabase.roomDao();
    }
}
