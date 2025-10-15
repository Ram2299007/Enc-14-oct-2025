package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.databinding.ActivityDummyChattingScreenBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class dummyChattingScreen extends AppCompatActivity {

    ActivityDummyChattingScreenBinding binding;
    ColorStateList tintList;
    String themColor;
    String currentTime;

    @Override
    protected void onResume() {
        super.onResume();
        int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

                // Set light status bar (white text/icons) for dark mode
                getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
            }
        }else{
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

                // Set dark status bar (black text/icons) for light mode
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDummyChattingScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding.name.setText("@Enclosureforworld");

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        currentTime = sdf.format(new Date());
        binding.recTime.setText(currentTime);


        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            binding.menuPoint.setColorFilter(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.pinklogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);

                } else if (themColor.equals("#00A3E9")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.ec_modern);
                    binding.sendGrp.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {
//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.popatilogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);


                } else if (themColor.equals("#ec0001")) {


//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.redlogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);


                } else if (themColor.equals("#16f3ff")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.bluelogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);


                } else if (themColor.equals("#FF8A00")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.orangelogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.graylogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);


                } else if (themColor.equals("#D9B845")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.yellowlogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);


                } else if (themColor.equals("#346667")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.greenlogoppng);
                    binding.sendGrp.setBackgroundTintList(tintList);


                } else if (themColor.equals("#9846D9")) {

                    binding.menu.setImageResource(R.drawable.voiletlogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);

                } else if (themColor.equals("#A81010")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.red2logopng);
                    binding.sendGrp.setBackgroundTintList(tintList);


                } else {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.red2logopng);
                    binding.sendGrp.setBackgroundTintList(tintList);


                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
       SwipeNavigationHelper.finishWithSwipe(dummyChattingScreen.this);
        Constant.setSfFunction(getApplicationContext());
        Constant.setSF.putString("notiKey", "notiKey");
        Constant.setSF.apply();
    }
}