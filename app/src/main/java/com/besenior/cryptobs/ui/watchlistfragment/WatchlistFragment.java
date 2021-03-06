package com.besenior.cryptobs.ui.watchlistfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.besenior.cryptobs.MainActivity;
import com.besenior.cryptobs.RoomDb.Entities.MarketListEntity;
import com.besenior.cryptobs.model.cryptolistmodel.AllMarketModel;
import com.besenior.cryptobs.model.cryptolistmodel.DataItem;
import com.besenior.cryptobs.viewmodel.AppViewModel;
import com.besenior.cryptobs.R;
import com.besenior.cryptobs.databinding.FragmentWatchlistBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WatchlistFragment extends Fragment {
    private FirebaseAnalytics mFirebaseAnalytics;


    MainActivity mainActivity;
    FragmentWatchlistBinding fragmentWatchlistBinding;

    CompositeDisposable compositeDisposable;
    AppViewModel appViewModel;
    watchlistRV_adapter watchlistRV_adapter;

    ArrayList<String> bookmarksArray;
    ArrayList<DataItem> finalData;
    List<DataItem> dataItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentWatchlistBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_watchlist,container,false);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());

        finalData = new ArrayList<>();

        compositeDisposable = new CompositeDisposable();

        //Read Data watchlisted in sharedPrefrence
        ReadDataStore();

        //if data is empty show "there is no item" txt
        if (bookmarksArray.size() == 0){
            fragmentWatchlistBinding.watchlistNoItemTxt.setVisibility(View.VISIBLE);
        }

        //get all Data list from RoomDB
        getMarketListDataFromDb();
        return fragmentWatchlistBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    private void getMarketListDataFromDb() {
        Disposable disposable = appViewModel.getAllMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MarketListEntity>() {
                    @Override
                    public void accept(MarketListEntity roomMarketEntity) throws Throwable {
                        AllMarketModel allMarketModel = roomMarketEntity.getAllMarketModel();
                        dataItemList = allMarketModel.getRootData().getCryptoCurrencyList();

                        finalData.clear();
                        for (int i = 0;i < bookmarksArray.size();i++){
                            for (int j = 0;j < dataItemList.size();j++){
                                if (bookmarksArray.get(i).equals(dataItemList.get(j).getSymbol())){
                                    finalData.add(dataItemList.get(j));
                                }
                            }
                        }



                        if (fragmentWatchlistBinding.watchlistRv.getAdapter() != null) {
                            watchlistRV_adapter = (watchlistRV_adapter) fragmentWatchlistBinding.watchlistRv.getAdapter();
                            watchlistRV_adapter.updateData(finalData);
                        } else {
                            watchlistRV_adapter = new watchlistRV_adapter(finalData);
                            fragmentWatchlistBinding.watchlistRv.setAdapter(watchlistRV_adapter);
                        }

                        // check recyclerView changes like remove item and ....
                        watchlistRV_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onChanged() {
                                super.onChanged();
                                checkEmpty();
                            }

                            @Override
                            public void onItemRangeInserted(int positionStart, int itemCount) {
                                super.onItemRangeInserted(positionStart, itemCount);
                                checkEmpty();
                            }

                            @Override
                            public void onItemRangeRemoved(int positionStart, int itemCount) {
                                super.onItemRangeRemoved(positionStart, itemCount);
                                checkEmpty();
                            }

                            //chech if RV adapter is empty show "there is no item" txt
                            void checkEmpty(){
                                if (watchlistRV_adapter.getItemCount() == 0){
                                    fragmentWatchlistBinding.watchlistNoItemTxt.setVisibility(View.VISIBLE);
                                }else {
                                    fragmentWatchlistBinding.watchlistNoItemTxt.setVisibility(View.GONE);
                                }
                            }
                        });


                        //setup swiping and delete
                        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                            @Override
                            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                                return false;
                            }

                            @Override
                            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                                int pos = viewHolder.getAdapterPosition();
                                fragmentWatchlistBinding.watchlistRv.removeViewAt(pos);
                                finalData.remove(pos);

                                // remove Item from Array (arraylist from shared Prefrenc)
                                bookmarksArray.remove(pos);
                                writetoDataStore(bookmarksArray);
                                watchlistRV_adapter.notifyItemRemoved(pos);
                            }
                        };
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(fragmentWatchlistBinding.watchlistRv);

                    }
                });

        compositeDisposable.add(disposable);
    }

    // Write BookMarks ArrayList From Shared Prefrence
    private void writetoDataStore(ArrayList<String> newArray) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(newArray);

        editor.putString("bookmarks", json);
        editor.apply();
    }

    // read new BookMark on Shared Prefrence
    private void ReadDataStore() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("bookmarks", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        bookmarksArray = gson.fromJson(json, type);
        Log.e("TAG", "ReadDataStore: " + bookmarksArray);
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.watchlistFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();
        Toolbar toolbar = view.findViewById(R.id.toolbar);


        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        //set Click listener for profile menu Btn
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return NavigationUI.onNavDestinationSelected(item,navController);
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if (destination.getId() == R.id.watchlistFragment){
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_35);
                    toolbar.setTitle("WatchList");
                }
            }
        });
    }

//    private void logEventAnalytics(ArrayList<DataItem> dataItems) {
//        ArrayList<String> coinsName = new ArrayList<>();
//        for (int i = 0;i < dataItems.size();i++){
//            coinsName.add(dataItems.get(i).getSymbol());
//        }
//        Bundle params = new Bundle();
//        params.putStringArrayList(FirebaseAnalytics.Param.ITEMS, coinsName);
//        mFirebaseAnalytics.logEvent("WatchList_Coins", params);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}