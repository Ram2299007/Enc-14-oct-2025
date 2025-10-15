package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivityChangeNumberBinding;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Objects;

public class changeNumber extends AppCompatActivity {
    ActivityChangeNumberBinding binding;

    String themColor;
    ColorStateList tintList;
    public static String fontSizePref;

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
                binding.enter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.edt91.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.oldnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.newnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.ent2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.nine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            } else if (fontSizePref.equals(Constant.medium)) {

                binding.cng.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.enter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.edt91.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.oldnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.newnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.ent2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.nine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.cng.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.enter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.edt91.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.oldnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.newnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.ent2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.nine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            }
        } catch (Exception ignored) {

        }


        try {

            Constant.getSfFuncion(getApplicationContext());
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

        Constant.getSfFuncion(getApplicationContext());
        String oldnumber = Constant.getSF.getString(Constant.PHONE_NUMBERKEY, "");
        String[] result = splitCountryCodeAndNationalNumber(oldnumber);
        System.out.println("Country Code: " + result[0]);
        System.out.println("National Number: " + result[1]);

        binding.edt91.setText(result[0]);
        binding.nine.setText(result[0]);
        binding.oldnumber.setText(result[1]);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding.oldnumber.getBackground().mutate().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        binding.newnumber.getBackground().mutate().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.oldnumber.getText().toString().isEmpty()) {

                    Drawable customErrorDrawable = getApplicationContext().getResources().getDrawable(R.drawable.circlewarn);
                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                    binding.oldnumber.setError("Missing old phone ?", customErrorDrawable);
                    binding.oldnumber.requestFocus();
                } else  if (binding.edt91.getText().toString().isEmpty()) {

                    Drawable customErrorDrawable = getApplicationContext().getResources().getDrawable(R.drawable.circlewarn);
                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                    binding.edt91.setError("Missing country code ?", customErrorDrawable);
                    binding.edt91.requestFocus();
                } else if (binding.newnumber.getText().toString().isEmpty()) {
                    Drawable customErrorDrawable = getApplicationContext().getResources().getDrawable(R.drawable.circlewarn);
                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                    binding.newnumber.setError("Missing new phone ?", customErrorDrawable);
                    binding.newnumber.requestFocus();
                } else if (binding.nine.getText().toString().isEmpty()) {
                    Drawable customErrorDrawable = getApplicationContext().getResources().getDrawable(R.drawable.circlewarn);
                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                    binding.nine.setError("Missing country code ?", customErrorDrawable);
                    binding.nine.requestFocus();
                } else {

                    Intent intent = new Intent(getApplicationContext(),deleteMyAccount.class);
                    intent.putExtra("newNumberKey","+"+binding.nine.getText().toString()+binding.newnumber.getText().toString());
                    SwipeNavigationHelper.startActivityWithSwipe(changeNumber.this, intent);
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        SwipeNavigationHelper.finishWithSwipe(changeNumber.this);
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public static String[] splitCountryCodeAndNationalNumber(String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String[] result = new String[2];
        try {
            Phonenumber.PhoneNumber numberProto = phoneNumberUtil.parse(phoneNumber, null);
            result[0] = String.valueOf(numberProto.getCountryCode());
            result[1] = String.valueOf(numberProto.getNationalNumber());
        } catch (Exception e) {
            e.printStackTrace();
            result[0] = "";
            result[1] = phoneNumber; // Return original if parsing fails
        }
        return result;
    }
}