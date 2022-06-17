package com.besenior.cryptobs.ui.otherFragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.besenior.cryptobs.MainActivity;
import com.besenior.cryptobs.R;
import com.besenior.cryptobs.databinding.FragmentPrivacypolicyBinding;

import org.jetbrains.annotations.NotNull;

import me.ibrahimsn.lib.SmoothBottomBar;

public class PrivacypolicyFragment extends Fragment {
    FragmentPrivacypolicyBinding fragmentPrivacypolicyBinding;
    SmoothBottomBar bottomNavigationBar;

    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentPrivacypolicyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_privacypolicy,container,false);
        HideBottomNavigationbar();


        // Inflate the layout for this fragment
        return fragmentPrivacypolicyBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.privacypolicyFragment)
                .build();
        Toolbar toolbar = view.findViewById(R.id.emptytoolbar);


        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if (destination.getId() == R.id.privacypolicyFragment){
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
                    toolbar.setTitle("privacy policy");
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bottomNavigationBar.setVisibility(View.VISIBLE);
    }


    private void HideBottomNavigationbar() {
        bottomNavigationBar = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationBar.setVisibility(View.GONE);
    }
}