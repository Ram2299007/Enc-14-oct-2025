package com.Appzia.enclosure.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.Appzia.enclosure.Adapter.forGroupVisibleMembersAdapter;
import com.Appzia.enclosure.Model.profileDBModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityForGroupVisibleBinding;
import com.Appzia.enclosure.models.members;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class forGroupVisible extends AppCompatActivity implements ConnectivityReceiver.ConnectivityListener {

    ActivityForGroupVisibleBinding binding;
    Context mContext;
    String recUserId;
    ColorStateList tintList;
    String group_id;
    forGroupVisibleMembersAdapter adapter;

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected) {

        } else {
            try {

                //TODO : GET PROFILE DETAILS
                Log.d("Network", "disconnected: " + "youFragment");
                binding.networkLoader.setVisibility(View.VISIBLE);
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                recUserId = getIntent().getStringExtra("recUserId");
                profileDBModel model = dbHelper.getAllDataProfile(recUserId);


                try {
                    binding.profile.setTag(model.getPhoto());
                    Picasso.get().load(model.getPhoto()).into(binding.profile);
                } catch (Exception ignored) {
                }


            } catch (Exception ignored) {
            }


        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Webservice.get_group_details(forGroupVisible.this, group_id, binding.profile, binding.pName,mContext);

        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

                // Set light status bar (white text/icons) for dark mode
                getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
            }

            Constant.getSfFuncion(mContext);
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            ColorStateList tintList;

            try {
                if (themColor.equals("#ff0080")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));

                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#00A3E9")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));


                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {

                    tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));

                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#ec0001")) {

                    tintList = ColorStateList.valueOf(Color.parseColor("#470000"));

                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#16f3ff")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));

                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#FF8A00")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#663700"));

                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));

                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#413815"));

                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#346667")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));

                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#9846D9")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));

                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#A81010")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#430706"));

                    binding.richBox.setBackgroundTintList(tintList);

                } else {
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                    binding.richBox.setBackgroundTintList(tintList);


                }
            } catch (Exception ignored) {
                tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                binding.richBox.setBackgroundTintList(tintList);


            }
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

                // Set dark status bar (black text/icons) for light mode
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
            // Replace #011224 with your hex color value
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForGroupVisibleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = forGroupVisible.this;
        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        group_id = getIntent().getStringExtra("group_id");

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void setAdapter(ArrayList<members> members) {
        adapter = new forGroupVisibleMembersAdapter(mContext, members);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

        SwipeNavigationHelper.finishWithSwipe(forGroupVisible.this);


    }
}