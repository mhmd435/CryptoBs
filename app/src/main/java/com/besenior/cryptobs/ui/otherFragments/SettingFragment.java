package com.besenior.cryptobs.ui.otherFragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.besenior.cryptobs.MainActivity;
import com.besenior.cryptobs.R;
import com.besenior.cryptobs.databinding.FragmentSettingBinding;
import com.suke.widget.SwitchButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import me.ibrahimsn.lib.SmoothBottomBar;

public class SettingFragment extends Fragment {

    FragmentSettingBinding fragmentSettingBinding;
    MainActivity mainActivity;

    ArrayList<String> langString = new ArrayList<>();

    SmoothBottomBar bottomNavigationBar;

    String versionName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting,container,false);
        HideBottomNavigationbar();

        setupSpinner();
        setuoVersionTxt();
        // Inflate the layout for this fragment
        return fragmentSettingBinding.getRoot();
    }

    private void setuoVersionTxt() {
        try {
            versionName = getActivity().getApplicationContext().getPackageManager().getPackageInfo(getActivity().getApplicationContext().getPackageName(), 0).versionName;
            fragmentSettingBinding.versionTxt.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupSpinner() {
        langString.add("English");
        langString.add("Persian");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,langString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentSettingBinding.spinner.setEnabled(false);
        fragmentSettingBinding.spinner.setAdapter(adapter);

        fragmentSettingBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        fragmentSettingBinding.switchButton.setEnableEffect(true);
        fragmentSettingBinding.switchButton.setEnabled(false);
        fragmentSettingBinding.switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.settingFragment)
                .build();
        Toolbar toolbar = view.findViewById(R.id.emptytoolbar);


        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if (destination.getId() == R.id.settingFragment){
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
                    toolbar.setTitle("Settings");
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