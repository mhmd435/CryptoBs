package com.besenior.cryptobs.Hilt.modules;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.besenior.cryptobs.MainActivity;
import com.besenior.cryptobs.R;
import com.besenior.cryptobs.databinding.ActivityMainBinding;
import com.besenior.cryptobs.databinding.DrawerHeaderlayoutBinding;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@Module
@InstallIn(ActivityComponent.class)
public class HiltAppModule {


//    @Provides
//    NavController ProvideNavController(@ApplicationContext Context activity){
//        return Navigation.findNavController((Activity) activity,R.id.nav_host_fragment);
//    }

    @Provides
    ActivityMainBinding ProvideActivityMainBinding(Activity activity){
        return DataBindingUtil.setContentView(activity, R.layout.activity_main);
    }

//    @Provides
//    DrawerHeaderlayoutBinding ProvideDrawerHeaderlayoutBinding(ActivityMainBinding activityMainBinding){
//        return DrawerHeaderlayoutBinding.bind(activityMainBinding.navigationView.getHeaderView(0));
//    }

    @Provides
    @Named("MainActivityCompositeDisposable")
    CompositeDisposable ProvideCompositeDisposable(){
        return new CompositeDisposable();
    }

    @Provides
    SharedPreferences ProvideSharedPreferences(Activity activity){
        return PreferenceManager.getDefaultSharedPreferences(activity);
    }

//    @Provides
//    AppBarConfiguration ProvideAppBarConfiguration(ActivityMainBinding activityMainBinding){
//        return new AppBarConfiguration.Builder(R.id.homeFragment,R.id.marketFragment,R.id.watchlistFragment)
//                .setOpenableLayout(activityMainBinding.drawerlayout)
//                .build();
//    }

    @Provides
    ConnectivityManager ProvideConnectivityManager(@ActivityContext Context context){
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    NetworkRequest ProvideNetworkRequest(){
        return new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
    }

//    @Provides
//    DrawerLayout ProvideDrawerLayout(ActivityMainBinding activityMainBinding){
//        return activityMainBinding.drawerlayout;
//    }
}
