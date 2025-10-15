package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivityAcountBinding;


import java.util.Objects;

public class Acount extends AppCompatActivity {
    ActivityAcountBinding binding;
    String themColor;
    ColorStateList tintList;
    public static String fontSizePref;
    Context mContext;
    @Override
    protected void onStart() {
        super.onStart();
        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (fontSizePref.equals(Constant.small)) {
                binding.cng.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.changing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.before.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.you.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.cng.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.changing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.before.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.you.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.cng.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.changing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.before.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.you.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
            }
        } catch (Exception ignored) {

        }

        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            //  binding.menuPoint.setColorFilter(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {

                    binding.linear.setBackgroundResource(R.drawable.pinkhover);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);


                } else if (themColor.equals("#00A3E9")) {

                    binding.linear.setBackgroundResource(R.drawable.buttonhoverfordone);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {

                    binding.linear.setBackgroundResource(R.drawable.popatihover);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);

                } else if (themColor.equals("#ec0001")) {

                    binding.linear.setBackgroundResource(R.drawable.red_one_hover);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);

                } else if (themColor.equals("#16f3ff")) {

                    binding.linear.setBackgroundResource(R.drawable.blue_hover);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);

                } else if (themColor.equals("#FF8A00")) {

                    binding.linear.setBackgroundResource(R.drawable.orangehover);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {

                    binding.linear.setBackgroundResource(R.drawable.gray_hover);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);


                } else if (themColor.equals("#D9B845")) {

                    binding.linear.setBackgroundResource(R.drawable.yellow_hover);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);
                } else if (themColor.equals("#346667")) {

                    binding.linear.setBackgroundResource(R.drawable.greenoriginalhover);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);
                } else if (themColor.equals("#9846D9")) {

                    binding.linear.setBackgroundResource(R.drawable.voilethover);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);
                } else if (themColor.equals("#A81010")) {

                    binding.linear.setBackgroundResource(R.drawable.red_two_hover);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);

                } else {

                    binding.linear.setBackgroundResource(R.drawable.buttonhoverfordone);
                    binding.view1.setBackgroundTintList(tintList);
                    binding.view2.setBackgroundTintList(tintList);
                    binding.view3.setBackgroundTintList(tintList);
                    binding.sim1lyt.setBackgroundTintList(tintList);
                    binding.sim2lyt.setBackgroundTintList(tintList);
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
        binding = ActivityAcountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = binding.getRoot().getContext();

        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeNavigationHelper.startActivityWithSwipe(Acount.this, new Intent(mContext, changeNumber.class));
                // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);


            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        SwipeNavigationHelper.finishWithSwipe(Acount.this);

       // TransitionHelper.performTransition(((Activity)mContext));
    }
}