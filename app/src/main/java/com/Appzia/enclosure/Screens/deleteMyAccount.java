package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.content.Context;
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
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityDeleteMyAccounbtBinding;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Objects;

public class deleteMyAccount extends AppCompatActivity {

    ActivityDeleteMyAccounbtBinding binding;

    String themColor;
    ColorStateList tintList;

    String newNumberKey;
    Context mContext;
    public static String fontSizePref;

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

        Constant.getSfFuncion(getApplicationContext());
        String oldnumber = Constant.getSF.getString(Constant.PHONE_NUMBERKEY, "");
        String[] result = splitCountryCodeAndNationalNumber(oldnumber);
        System.out.println("Country Code: " + result[0]);
        System.out.println("National Number: " + result[1]);

        binding.ninty1.setText(result[0]);
        binding.pn2.setText(result[1]);

    }

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

                binding.dlt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.deleting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.d1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.d2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.d3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.d4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.changeno.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.ninty1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.todlete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.ctr.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.ind.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.pn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.pn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.dlt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.deleting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.d1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.d2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.d3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.d4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.changeno.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.ninty1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.todlete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.ctr.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.ind.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.pn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.pn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);



            } else if (fontSizePref.equals(Constant.large)) {

                binding.dlt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.deleting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.d1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.d2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.d3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.d4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.changeno.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.ninty1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.todlete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.ctr.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.ind.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.pn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.pn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
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


                } else if (themColor.equals("#19f3ff")) {

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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityDeleteMyAccounbtBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = deleteMyAccount.this;

        

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Constant.getSfFuncion(getApplicationContext());
        String uid = Constant.getSF.getString(Constant.UID_KEY,"");
        String mobile_no_old = Constant.getSF.getString(Constant.PHONE_NUMBERKEY,"");

        newNumberKey = getIntent().getStringExtra("newNumberKey");
      //  Toast.makeText(this, newNumberKey, Toast.LENGTH_SHORT).show();

        binding.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Webservice.change_number(deleteMyAccount.this,uid,mobile_no_old,newNumberKey,binding.ninty1);
            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Webservice.send_otpDelete(mContext,mobile_no_old,uid,binding.ninty1);
            }
        });


    }



    @Override
    public void onBackPressed() {
        SmoothNavigationHelper.finishWithSlideToRight(deleteMyAccount.this);
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