package com.Appzia.enclosure.Screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityLockScreen3Binding;

import java.util.Objects;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class lockScreen3 extends AppCompatActivity {

    ActivityLockScreen3Binding binding;
    public static  Dialog dialogLayoutColor;

    String lockKey;
    Context mContext;
    String mobilenumber;

    // 0 for unlocking lock screen
    public static String lock_status = "0";
    int count = 0;
    String themColor;
    ColorStateList tintList;
    String uid;
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            Constant.getSfFuncion(getApplicationContext());
            mobilenumber= Constant.getSF.getString(Constant.PHONE_NUMBERKEY,"");
        }catch (Exception ignored){}


        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (fontSizePref.equals(Constant.small)) {
                binding.forgetPass.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.turn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            } else if (fontSizePref.equals(Constant.medium)) {

                binding.forgetPass.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.turn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            } else if (fontSizePref.equals(Constant.large)) {
                binding.forgetPass.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.turn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
            }
        } catch (Exception ignored) {

        }

        try {

            Constant.getSfFuncion(getApplicationContext());
            lockKey = Constant.getSF.getString("lockKey", String.valueOf(0));
            Log.d("password#", lockKey);


            Constant.getSfFuncion(getApplicationContext());
            uid = Constant.getSF.getString(Constant.UID_KEY, "");

            if (lockKey.equals("0")) {
             //   Toast.makeText(this, lockKey, Toast.LENGTH_SHORT).show();
                SwipeNavigationHelper.startActivityWithSwipe(lockScreen3.this, new Intent(getApplicationContext(), lockscreen.class));
                // Toast.makeText(this, lockKey, Toast.LENGTH_SHORT).show();
            } else {

            }
        } catch (Exception ex) {

        }


        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.turn.setTextColor(Color.parseColor(themColor));
                    binding.linear.setBackgroundResource(R.drawable.pinkhover);
                    binding.circle.setBackgroundTintList(tintList);
                    binding.forgetPass.setTextColor(Color.parseColor(themColor));


                } else if (themColor.equals("#00A3E9")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    //  binding.forgetPass.setTextColor(getResources().getColor(R.color.bluetohovertext));
                    binding.linear.setBackgroundResource(R.drawable.buttonhoverfordone);
                    binding.circle.setBackgroundTintList(tintList);
                    binding.turn.setTextColor(Color.parseColor(themColor));

                } else if (themColor.equals("#7adf2a")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.popatihover);
                    binding.forgetPass.setTextColor(Color.parseColor(themColor));
                    binding.turn.setTextColor(Color.parseColor(themColor));

                } else if (themColor.equals("#ec0001")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.red_one_hover);
                    binding.forgetPass.setTextColor(Color.parseColor(themColor));
                    binding.turn.setTextColor(Color.parseColor(themColor));
                } else if (themColor.equals("#16f3ff")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.forgetPass.setTextColor(Color.parseColor(themColor));
                    binding.linear.setBackgroundResource(R.drawable.blue_hover);
                    binding.circle.setBackgroundTintList(tintList);
                    binding.turn.setTextColor(Color.parseColor(themColor));
                } else if (themColor.equals("#FF8A00")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.forgetPass.setTextColor(Color.parseColor(themColor));
                    binding.linear.setBackgroundResource(R.drawable.orangehover);
                    binding.circle.setBackgroundTintList(tintList);
                    binding.turn.setTextColor(Color.parseColor(themColor));

                } else if (themColor.equals("#7F7F7F")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.forgetPass.setTextColor(Color.parseColor(themColor));
                    binding.linear.setBackgroundResource(R.drawable.gray_hover);
                    binding.circle.setBackgroundTintList(tintList);
                    binding.turn.setTextColor(Color.parseColor(themColor));

                } else if (themColor.equals("#D9B845")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.forgetPass.setTextColor(Color.parseColor(themColor));
                    binding.linear.setBackgroundResource(R.drawable.yellow_hover);
                    binding.circle.setBackgroundTintList(tintList);
                    binding.turn.setTextColor(Color.parseColor(themColor));
                } else if (themColor.equals("#346667")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.forgetPass.setTextColor(Color.parseColor(themColor));
                    binding.linear.setBackgroundResource(R.drawable.greenoriginalhover);
                    binding.circle.setBackgroundTintList(tintList);
                    binding.turn.setTextColor(Color.parseColor(themColor));

                } else if (themColor.equals("#9846D9")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.linear.setBackgroundResource(R.drawable.voilethover);
                    binding.forgetPass.setTextColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
                    binding.turn.setTextColor(Color.parseColor(themColor));
                } else if (themColor.equals("#A81010")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.forgetPass.setTextColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.red_two_hover);
                    binding.turn.setTextColor(Color.parseColor(themColor));
                } else {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    //  binding.forgetPass.setTextColor(getResources().getColor(R.color.bluetohovertext));
                    binding.linear.setBackgroundResource(R.drawable.buttonhoverfordone);
                    binding.circle.setBackgroundTintList(tintList);
                    binding.turn.setTextColor(Color.parseColor(themColor));

                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockScreen3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        if (Build.VERSION.SDK_INT >= 21) {


            binding.progressBar.setMax(360);
            binding.progressBar.setProgress(0);

            mContext = binding.getRoot().getContext();

            binding.progressBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                @Override
                public void onProgressChanged(@Nullable CircularSeekBar circularSeekBar, float progress, boolean b) {
                    String progressData = String.valueOf(progress);
                    int intValue = (int) Math.round(Float.parseFloat(progressData));
                    int finalValue = (int) (intValue);
                    Integer intValueData = (int) Math.round(Float.parseFloat(String.valueOf(finalValue)));

                    binding.bigDegree.setText(String.valueOf(intValueData + "\u00B0"));
                }

                @Override
                public void onStopTrackingTouch(@Nullable CircularSeekBar circularSeekBar) {

                }

                @Override
                public void onStartTrackingTouch(@Nullable CircularSeekBar circularSeekBar) {

                }
            });
            binding.bigDegree.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Constant.Vibrator(getApplicationContext());
                    }


                    ///  Toast.makeText(mContext,  binding.bigDegree.getText().toString(), Toast.LENGTH_SHORT).show();


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            binding.bigDegree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Constant.Vibrator50(getApplicationContext());
                    }


                    String[] parts = binding.bigDegree.getText().toString().split("°");

                    String textBefore = parts[0];

                    count = Integer.parseInt(textBefore) + 1;

                    if(count >= 360){

                    }else {

                        binding.bigDegree.setText(String.valueOf(count) + "\u00B0");
                        binding.progressBar.setProgress(count);
                    }

                }
            });
            binding.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    //     Toast.makeText(lockScreen2.this, password, Toast.LENGTH_SHORT).show();
//
//                    if (lockKey.equals(binding.bigDegree.getText().toString())) {
//                        Intent intent = new Intent(getApplicationContext(), lockscreen.class);
//                        intent.putExtra("lockSuccess", "lockSuccess");
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(lockScreen3.this, "Incorrect lock key", Toast.LENGTH_SHORT).show();
//                    }
                    Webservice.lock_screen(mContext, uid, lock_status, binding.bigDegree.getText().toString().replaceAll("\u00B0", ""),Constant.lock3,findViewById(R.id.includedToast),findViewById(R.id.toastText));




                }
            });


            dialogLayoutColor = new Dialog(lockScreen3.this);
            dialogLayoutColor.setContentView(R.layout.forget_row);
            dialogLayoutColor.setCanceledOnTouchOutside(true);
            dialogLayoutColor.setCancelable(true);
            dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//
//
            //for margine to dialogue
            dialogLayoutColor.getWindow().setGravity(Gravity.CENTER);
            WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
// top margin
            dialogLayoutColor.getWindow().setAttributes(layoutParams);
            AppCompatButton ok = dialogLayoutColor.findViewById(R.id.ok);

            binding.forgetPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogLayoutColor.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogLayoutColor.dismiss();
                        }
                    });


                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Webservice.forget_lock_screen(mContext,uid,mobilenumber);


                        }
                    });

                    dialogLayoutColor.show();

                    Window window = dialogLayoutColor.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            });

        }
    }

}