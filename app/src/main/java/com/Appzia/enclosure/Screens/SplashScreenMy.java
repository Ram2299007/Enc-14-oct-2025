package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import com.Appzia.enclosure.Utils.EdgeToEdgeHelper;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;

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

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivitySplashScreenBinding;

import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import java.io.File;


public class SplashScreenMy extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    String themColor;
    ColorStateList tintList;
    String uid;
    Context mContext;

    @Override
    protected void onResume() {
        super.onResume();
        // Status bar configuration is now handled by EdgeToEdgeHelper
        // No need for manual status bar handling as it's deprecated
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            navigateToNextScreen();
            return;
        }

        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup edge-to-edge display
        EdgeToEdgeHelper.setupEdgeToEdge(this, binding.getRoot());

        mContext = binding.getRoot().getContext();

        setupSplashScreen();
        startSplashThread();
    }

    private void navigateToNextScreen() {
        Constant.getSfFuncion(getApplicationContext());
        uid = Constant.getSF.getString(Constant.UID_KEY, "0");

        if (uid.equals("0")) {
            Intent intent = new Intent(this, SplashScreen2.class);
            SwipeNavigationHelper.startActivityWithSwipe(this, intent);
        } else {
            SwipeNavigationHelper.startActivityWithSwipe(this, new Intent(getApplicationContext(), lockScreen2.class));
        }
        finish();
    }

    private void setupSplashScreen() {
        try {
            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            // Set the logo based on the theme color
            if (themColor.equals("#ff0080")) {
                binding.mainLogo.setImageResource(R.drawable.pinklogopng);
            } else if (themColor.equals("#00A3E9")) {
                binding.mainLogo.setImageResource(R.drawable.ec_modern);
            } else if (themColor.equals("#7adf2a")) {
                binding.mainLogo.setImageResource(R.drawable.popatilogopng);
            } else if (themColor.equals("#ec0001")) {
                binding.mainLogo.setImageResource(R.drawable.redlogopng);
            } else if (themColor.equals("#16f3ff")) {
                binding.mainLogo.setImageResource(R.drawable.bluelogopng);
            } else if (themColor.equals("#FF8A00")) {
                binding.mainLogo.setImageResource(R.drawable.orangelogopng);
            } else if (themColor.equals("#7F7F7F")) {
                binding.mainLogo.setImageResource(R.drawable.graylogopng);
            } else if (themColor.equals("#D9B845")) {
                binding.mainLogo.setImageResource(R.drawable.yellowlogopng);
            } else if (themColor.equals("#346667")) {
                binding.mainLogo.setImageResource(R.drawable.greenlogoppng);
            } else if (themColor.equals("#9846D9")) {
                binding.mainLogo.setImageResource(R.drawable.voiletlogopng);
            } else if (themColor.equals("#A81010")) {
                binding.mainLogo.setImageResource(R.drawable.red2logopng);
            }
        } catch (Exception ignored) {
        }
    }

    private void startSplashThread() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    navigateToNextScreen();
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }




}
