package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
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
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.databinding.ActivityPayBinding;

import java.util.Objects;

public class payActivity extends AppCompatActivity {

    ActivityPayBinding binding;
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
                binding.pay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.payForMonth.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.last.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.oct.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.prof.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.msglmt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.call.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.videocall.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.lockscreen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.theme.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.grpcall.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.free1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
//                binding.rs1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
//                binding.rs3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

                binding.threeM.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.sixY.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.Year.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.oneM.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            } else if (fontSizePref.equals(Constant.medium)) {

                binding.pay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.payForMonth.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.last.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.oct.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.prof.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.msglmt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.call.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.videocall.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.lockscreen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.theme.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.grpcall.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.oneM.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.threeM.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.sixY.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.Year.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.free1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//                binding.rs1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//                binding.rs3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.pay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.payForMonth.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.last.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.oct.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.prof.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.msglmt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.call.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.videocall.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.lockscreen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.theme.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.grpcall.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.free1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);


                binding.threeM.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.sixY.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.Year.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.oneM.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            }
        } catch (Exception ignored) {

        }


        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {
                    binding.paynow.setBackgroundResource(R.drawable.pinkhover);
                    binding.label.setBackgroundTintList(tintList);


                } else if (themColor.equals("#00A3E9")) {

                    //  binding.forgetPass.setTextColor(getResources().getColor(R.color.bluetohovertext));
                    binding.paynow.setBackgroundResource(R.drawable.buttonhoverfordone);
                    binding.label.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {


                    binding.paynow.setBackgroundResource(R.drawable.popatihover);
                    binding.label.setBackgroundTintList(tintList);

                } else if (themColor.equals("#ec0001")) {


                    binding.paynow.setBackgroundResource(R.drawable.red_one_hover);
                    binding.label.setBackgroundTintList(tintList);
                } else if (themColor.equals("#16f3ff")) {


                    binding.paynow.setBackgroundResource(R.drawable.blue_hover);
                    binding.label.setBackgroundTintList(tintList);

                } else if (themColor.equals("#FF8A00")) {


                    binding.paynow.setBackgroundResource(R.drawable.orangehover);
                    binding.label.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7F7F7F")) {


                    binding.paynow.setBackgroundResource(R.drawable.gray_hover);
                    binding.label.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {


                    binding.paynow.setBackgroundResource(R.drawable.yellow_hover);
                    binding.label.setBackgroundTintList(tintList);
                } else if (themColor.equals("#346667")) {


                    binding.paynow.setBackgroundResource(R.drawable.greenoriginalhover);
                    binding.label.setBackgroundTintList(tintList);

                } else if (themColor.equals("#9846D9")) {

                    binding.paynow.setBackgroundResource(R.drawable.voilethover);
                    binding.label.setBackgroundTintList(tintList);

                } else if (themColor.equals("#A81010")) {

                    binding.label.setBackgroundTintList(tintList);
                    binding.paynow.setBackgroundResource(R.drawable.red_two_hover);
                } else {

                    //  binding.forgetPass.setTextColor(getResources().getColor(R.color.bluetohovertext));
                    binding.paynow.setBackgroundResource(R.drawable.buttonhoverfordone);
                    binding.label.setBackgroundTintList(tintList);

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
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = binding.getRoot().getContext();

        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding.spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.spindata.getVisibility() == View.GONE) {

                    binding.spindata.setVisibility(View.VISIBLE);
                } else if (binding.spindata.getVisibility() == View.VISIBLE) {

                    binding.spindata.setVisibility(View.GONE);
                }
            }
        });

        binding.payForMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spinner.performClick();
            }
        });

        binding.oneM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = binding.oneM.getText().toString();

                binding.payForMonth.setText("Pay for a " + data);
                binding.spindata.setVisibility(View.GONE);
            }
        });


        binding.threeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = binding.threeM.getText().toString();

                binding.payForMonth.setText("Pay for a " + data);
                binding.spindata.setVisibility(View.GONE);
            }
        });
        binding.sixY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = binding.sixY.getText().toString();

                binding.payForMonth.setText("Pay for a " + data);
                binding.spindata.setVisibility(View.GONE);
            }
        });
        binding.Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = binding.Year.getText().toString();

                binding.payForMonth.setText("Pay for a " + data);
                binding.spindata.setVisibility(View.GONE);
            }
        });


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, paymentActivity.class);
                startActivity(intent);
                //((Activity)mContext).

            }
        });
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(mContext, MainActivity.class);
//        startActivity(intent);
        SwipeNavigationHelper.finishWithSwipe(payActivity.this);
        //
    }
}