package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import com.Appzia.enclosure.Adapter.flagAdapter;
import com.Appzia.enclosure.Model.flagModel;
import com.Appzia.enclosure.Model.flagNewModelChild;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityFlagScreenBinding;

import java.util.ArrayList;
import java.util.Objects;

public class flagScreen extends AppCompatActivity {
    ActivityFlagScreenBinding binding;
    ArrayList<flagNewModelChild> flagModelList = new ArrayList<>();
    Context mContext;
    public static flagAdapter adapter;
    public static flagModel[] model;

    @Override
    protected void onResume() {
        super.onResume();
        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
                getWindow().getDecorView().setSystemUiVisibility(0);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlagScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        mContext = binding.getRoot().getContext();
        
        Webservice.get_country_list(mContext, flagScreen.this, binding.progressBar);
        binding.searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                filteredList(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void filteredList(String newText) {
        ArrayList<flagNewModelChild> filteredList = new ArrayList<>();

        if (newText.isEmpty()) {
            // If newText is empty, show the whole list
            filteredList.addAll(flagModelList);
        } else {
            String searchText = newText.toLowerCase();

            if (searchText.length() >= 1) { // Check if searchText is at least 1 character long
                for (flagNewModelChild list : flagModelList) {
                    String name = list.getCountry_name().toLowerCase();

                    if (name.length() >= 1 && name.substring(0, 1).equals(searchText.substring(0, 1))) {
                        if (name.contains(searchText)) {
                            filteredList.add(list);
                        }
                    }
                }
            }
        }

        if (filteredList.isEmpty()) {
            // Handle the case where there are no matching items
        } else {
            adapter.searchFilteredData(filteredList);
        }
    }


    @Override
    public void onBackPressed() {
        SmoothNavigationHelper.finishWithSlideToRight(flagScreen.this);
    }

    public void setAdaper(ArrayList<flagNewModelChild> data) {
        this.flagModelList = data;
        adapter = new flagAdapter(mContext, data);
        binding.fontrecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        // Removed setHasFixedSize(true) to fix lint error - incompatible with wrap_content height
        binding.fontrecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}