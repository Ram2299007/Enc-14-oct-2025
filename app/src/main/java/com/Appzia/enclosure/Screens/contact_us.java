package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityContactUsBinding;

import java.util.Objects;

public class contact_us extends AppCompatActivity {

    ActivityContactUsBinding binding;

    String themColor;
    Context mContext;
    ColorStateList tintList;
    public static String fontSizePref;
    String uid;

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
        } catch (Exception ignored) {
        }


        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (fontSizePref.equals(Constant.small)) {
                binding.cntus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.get.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.email.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
            } else if (fontSizePref.equals(Constant.medium)) {

                binding.cntus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.get.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.email.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.cntus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.get.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.email.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
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


                } else if (themColor.equals("#00A3E9")) {

                    binding.linear.setBackgroundResource(R.drawable.buttonhoverfordone);


                } else if (themColor.equals("#7adf2a")) {

                    binding.linear.setBackgroundResource(R.drawable.popatihover);


                } else if (themColor.equals("#ec0001")) {

                    binding.linear.setBackgroundResource(R.drawable.red_one_hover);


                } else if (themColor.equals("#16f3ff")) {

                    binding.linear.setBackgroundResource(R.drawable.blue_hover);


                } else if (themColor.equals("#FF8A00")) {

                    binding.linear.setBackgroundResource(R.drawable.orangehover);


                } else if (themColor.equals("#7F7F7F")) {

                    binding.linear.setBackgroundResource(R.drawable.gray_hover);


                } else if (themColor.equals("#D9B845")) {

                    binding.linear.setBackgroundResource(R.drawable.yellow_hover);


                } else if (themColor.equals("#346667")) {

                    binding.linear.setBackgroundResource(R.drawable.greenoriginalhover);


                } else if (themColor.equals("#9846D9")) {

                    binding.linear.setBackgroundResource(R.drawable.voilethover);


                } else if (themColor.equals("#A81010")) {

                    binding.linear.setBackgroundResource(R.drawable.red_two_hover);


                } else {

                    binding.linear.setBackgroundResource(R.drawable.buttonhoverfordone);


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
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        mContext = binding.getRoot().getContext();
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.name.getText().toString().isEmpty()) {
                    Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                    binding.name.setError("Invalid phone ?", customErrorDrawable);
                    binding.name.requestFocus();
                } else if (binding.email.getText().toString().isEmpty()) {
                    Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                    binding.email.setError("Invalid email ?", customErrorDrawable);
                    binding.email.requestFocus();

                } else if (binding.msg.getText().toString().isEmpty()) {

                    Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                    binding.msg.setError("Invalid message ?", customErrorDrawable);
                    binding.msg.requestFocus();
                } else {

                    Webservice.get_in_touch(mContext, uid, binding.name.getText().toString(), binding.email.getText().toString(), binding.msg.getText().toString());



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

    @Override
    public void onBackPressed() {
        SmoothNavigationHelper.finishWithSlideToRight(contact_us.this);

        //TransitionHelper.performTransition(((Activity)mContext));


    }
}