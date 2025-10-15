package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.Appzia.enclosure.Adapter.userinfoofflineadapter;
import com.Appzia.enclosure.Model.profileDBModel;
import com.Appzia.enclosure.Model.profilestatusModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityUserInfoScreenBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class userInfoScreen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityListener {
    public static ImageView profile;
    ActivityUserInfoScreenBinding binding;
    Context mContext;
    public static userinfoofflineadapter adapter;
    String uid;
    String recUserId;
    Window window;
    public static TextView pName, pCaption, phone;
    private ConnectivityReceiver connectivityReceiver;
    ArrayList<profilestatusModel> profilestatusList;
    String themColor;
    ColorStateList tintList;
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


                binding.phone.setText(model.getMobile_no());
                binding.pCaption.setText(model.getCaption());

                try {
                    binding.profile.setTag(model.getPhoto());
                    Picasso.get().load(model.getPhoto()).into(binding.profile);
                } catch (Exception ignored) {
                }


            } catch (Exception ignored) {
            }


            try {

                //TODO : GET PROFILE IMAGES STATUS
                Log.d("Network", "disconnected: " + "youFragment");
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                profilestatusList = dbHelper.getAllDataget_get_user_profile_imagesTablel(recUserId);

                Collections.reverse(profilestatusList);
                setAdapter(profilestatusList);

                binding.multiimageRecyclerview.setVisibility(View.VISIBLE);
            } catch (Exception ignored) {
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            recUserId = getIntent().getStringExtra("recUserId");
            profile = findViewById(R.id.profile);
            pName = findViewById(R.id.pName);
            pCaption = findViewById(R.id.pCaption);
            phone = findViewById(R.id.phone);
        } catch (Exception ignored) {
        }


        try {
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
            Constant.setSF.apply();
        } catch (Exception ignored) {
        }


        //TODO : for only network loader Themes

        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FF6D00"));
                } else if (themColor.equals("#00A3E9")) {

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                } else if (themColor.equals("#7adf2a")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00C853"));

                } else if (themColor.equals("#ec0001")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#ec7500"));

                } else if (themColor.equals("#16f3ff")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00F365"));

                } else if (themColor.equals("#FF8A00")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FFAB00"));

                } else if (themColor.equals("#7F7F7F")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#314E6D"));

                } else if (themColor.equals("#D9B845")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#b0d945"));
                } else if (themColor.equals("#346667")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#729412"));

                } else if (themColor.equals("#9846D9")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#d946d1"));

                } else if (themColor.equals("#A81010")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#D85D01"));

                } else {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }


    }

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

                // Set light status bar (white text/icons) for dark mode
                getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
            }

            Constant.getSfFuncion(mContext);
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            ColorStateList tintList;

            try {
                if (themColor.equals("#ff0080")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#00A3E9")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {

                    tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#ec0001")) {

                    tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#16f3ff")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#FF8A00")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#346667")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#9846D9")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#A81010")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else {
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);


                }
            } catch (Exception ignored) {
                tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                binding.MainSenderBox.setBackgroundTintList(tintList);
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
            binding.MainSenderBox.setBackgroundTintList(tintList); // Replace #011224 with your hex color value
        }
        try {


            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
            String fcm = Constant.getSF.getString(Constant.FCM_TOKEN, "");


            Webservice.get_profile_UserInfo_Under(mContext, recUserId, binding.profile, binding.pName, binding.pCaption, binding.phone, uid, fcm);


        } catch (Exception ignored) {
        }
        try {
            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
            String fcm = Constant.getSF.getString(Constant.FCM_TOKEN, "");

            Webservice.get_user_profile_images_userInfo(mContext, recUserId, userInfoScreen.this, binding.profile, binding.pName, binding.pCaption, binding.phone, uid, fcm);


        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = binding.getRoot().getContext();


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        String recUserName = getIntent().getStringExtra("recUserName");
        binding.pName.setText(recUserName);

        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        profilestatusList = new ArrayList<>();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, mContext.RECEIVER_EXPORTED);


        //todo -----------------------------onnline ---------------------------


        recUserId = getIntent().getStringExtra("recUserId");
        binding.networkLoader.setVisibility(View.GONE);
        try {

            //TODO : - offline call first for only profiles image

            Log.d("Network", "disconnected: " + "youFragment");
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            profileDBModel model = dbHelper.getAllDataProfile(recUserId);

            if (model != null) {


                binding.phone.setText(model.getMobile_no());
                binding.pCaption.setText(model.getCaption());

                try {
                    binding.profile.setTag(model.getPhoto());
                    Picasso.get().load(model.getPhoto()).into(binding.profile);
                } catch (Exception ignored) {
                }

            } else {

            }


        } catch (Exception ignored) {
        }


        try {
            //TODO : - offline call first for only status images
            Log.d("Network", "disconnected: " + "youFragment");
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            profilestatusList = dbHelper.getAllDataget_get_user_profile_imagesTablel(recUserId);

            Collections.reverse(profilestatusList);
            if (profilestatusList.size() > 0) {
                setAdapter(profilestatusList);

                binding.multiimageRecyclerview.setVisibility(View.VISIBLE);
            } else {

            }


        } catch (Exception ignored) {
        }


        try {


            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
            String fcm = Constant.getSF.getString(Constant.FCM_TOKEN, "");


            Webservice.get_profile_UserInfo_Under(mContext, recUserId, binding.profile, binding.pName, binding.pCaption, binding.phone, uid, fcm);


        } catch (Exception ignored) {
        }
        try {
            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
            String fcm = Constant.getSF.getString(Constant.FCM_TOKEN, "");
            Webservice.get_user_profile_images_userInfo(mContext, recUserId, userInfoScreen.this, binding.profile, binding.pName, binding.pCaption, binding.phone, uid, fcm);

        } catch (Exception ignored) {
        }
        //todo -----------------------------onnline ---------------------------

        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = binding.profile.getTag();
                if (tag != null) {
                    Intent intent = new Intent(mContext, show_image_Screen.class);
                    intent.putExtra("imageKey", tag.toString());
                    SwipeNavigationHelper.startActivityWithSwipe(userInfoScreen.this, intent);
                } else {
                    // Optionally show a toast or log a warning
                    //  Toast.makeText(mContext, "Image not available", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }

    public void setAdapter(ArrayList<profilestatusModel> profilestatusLists) {

        this.profilestatusList = profilestatusLists;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.multiimageRecyclerview.setLayoutManager(linearLayoutManager);
        adapter = new userinfoofflineadapter(mContext, profilestatusList);
        binding.multiimageRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        SwipeNavigationHelper.finishWithSwipe(userInfoScreen.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(connectivityReceiver);
    }


}