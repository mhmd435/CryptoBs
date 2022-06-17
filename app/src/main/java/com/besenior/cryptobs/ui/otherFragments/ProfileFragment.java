package com.besenior.cryptobs.ui.otherFragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.besenior.cryptobs.MainActivity;
import com.besenior.cryptobs.R;
import com.besenior.cryptobs.databinding.FragmentProfileBinding;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import me.ibrahimsn.lib.SmoothBottomBar;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding fragmentProfileBinding;

    MainActivity mainActivity;

    SmoothBottomBar bottomNavigationBar;
    String picturePath;
    String fname,lname,mail;
    String ImgFromStore;

    ActivityResultLauncher<Intent> GetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Handle the returned Uri
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();


                        fragmentProfileBinding.roundedImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    }
                }
            });



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);


        ReadDataStore();
        setdefaultValue();

        HideBottomNavigationbar();
        getphotofromGallery();
        setupSaveBtn();

        return fragmentProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.profileFragment)
                .build();
        Toolbar toolbar = view.findViewById(R.id.profiletoolbar);


        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if (destination.getId() == R.id.profileFragment){
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
                    toolbar.setTitle("Profile");
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

    private void getphotofromGallery() {
        fragmentProfileBinding.changeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
                }
                else {
                    Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    GetContent.launch(cameraIntent);
                }
            }
        });
    }

    private void setupSaveBtn() {
        fragmentProfileBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writetoDataStore();
                Snackbar.make(fragmentProfileBinding.ProfileCon,"Changes saved",1500).show();
            }
        });
    }

    private void ReadDataStore() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());

        ImgFromStore = sharedPrefs.getString("profileImg",null);
        fname = sharedPrefs.getString("firstname","");
        lname = sharedPrefs.getString("lastname","");
        mail = sharedPrefs.getString("male","");
    }


    // Write BookMarks ArrayList From Shared Prefrence
    private void writetoDataStore() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        SharedPreferences.Editor editor = sharedPrefs.edit();

        if (picturePath != null){
            editor.putString("profileImg", encodeTobase64(BitmapFactory.decodeFile(picturePath)));
        }
        editor.putString("firstname", fragmentProfileBinding.editTextTextPersonName.getText().toString());
        editor.putString("lastname", fragmentProfileBinding.editTextTextPersonName2.getText().toString());
        editor.putString("male", fragmentProfileBinding.editTextTextPersonName3.getText().toString());
        editor.apply();
    }

    // decode string to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void setdefaultValue() {
        if (ImgFromStore == null){
            fragmentProfileBinding.roundedImageView.setImageResource(R.drawable.profile_placeholder);
        }else {
            fragmentProfileBinding.roundedImageView.setImageBitmap(decodeBase64(ImgFromStore));
        }
        fragmentProfileBinding.editTextTextPersonName.setText(fname);
        fragmentProfileBinding.editTextTextPersonName2.setText(lname);
        fragmentProfileBinding.editTextTextPersonName3.setText(mail);
    }
}