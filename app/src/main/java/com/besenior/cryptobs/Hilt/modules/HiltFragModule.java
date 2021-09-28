package com.besenior.cryptobs.Hilt.modules;

import android.app.Activity;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.besenior.cryptobs.R;
import com.besenior.cryptobs.databinding.ActivityMainBinding;
import com.besenior.cryptobs.databinding.FragmentHomeBinding;
import com.besenior.cryptobs.viewmodel.AppViewModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@Module
@InstallIn(FragmentComponent.class)
public class HiltFragModule {

    @Provides
    CompositeDisposable ProvideCompositeDisposable(){
        return new CompositeDisposable();
    }

}
