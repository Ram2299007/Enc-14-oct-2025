package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.databinding.ActivitySizeBinding;

import java.util.Objects;

public class sizeActivity extends AppCompatActivity {

    ActivitySizeBinding binding;
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
                binding.size.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.smalltxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.mediumtxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.largetxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);


                binding.small.setChecked(true);
                binding.medium.setChecked(false);
                binding.large.setChecked(false);
                binding.smalltxt.setTextColor(Color.parseColor("#011224"));
                binding.mediumtxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.largetxt.setTextColor(Color.parseColor("#9EA6B9"));

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.size.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.smalltxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.mediumtxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.largetxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.small.setChecked(false);
                binding.medium.setChecked(true);
                binding.large.setChecked(false);

                binding.mediumtxt.setTextColor(Color.parseColor("#011224"));
                binding.largetxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.smalltxt.setTextColor(Color.parseColor("#9EA6B9"));


            } else if (fontSizePref.equals(Constant.large)) {
                binding.size.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.smalltxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.mediumtxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.largetxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.small.setChecked(false);
                binding.medium.setChecked(false);
                binding.large.setChecked(true);

                binding.largetxt.setTextColor(Color.parseColor("#011224"));
                binding.smalltxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.mediumtxt.setTextColor(Color.parseColor("#9EA6B9"));
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySizeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.small.isChecked()) {
                    binding.smalltxt.setTextColor(Color.parseColor("#011224"));
                    binding.mediumtxt.setTextColor(Color.parseColor("#9EA6B9"));
                    binding.largetxt.setTextColor(Color.parseColor("#9EA6B9"));
                    binding.medium.setChecked(false);
                    binding.large.setChecked(false);

                    Constant.setSfFunction(getApplicationContext());
                    Constant.setSF.putString(Constant.Font_Size,Constant.small);
                    Constant.setSF.apply();
                    Toast.makeText(sizeActivity.this, "small", Toast.LENGTH_SHORT).show();
                    onStart();
                    Constant.getSfFuncion(getApplicationContext());

                }
            }
        });
        binding.medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.medium.isChecked()) {
                    binding.mediumtxt.setTextColor(Color.parseColor("#011224"));
                    binding.largetxt.setTextColor(Color.parseColor("#9EA6B9"));
                    binding.smalltxt.setTextColor(Color.parseColor("#9EA6B9"));
                    binding.small.setChecked(false);
                    binding.large.setChecked(false);
                    Constant.setSfFunction(getApplicationContext());
                    Constant.setSF.putString(Constant.Font_Size,Constant.medium);
                    Constant.setSF.apply();
                    Toast.makeText(sizeActivity.this, "medium", Toast.LENGTH_SHORT).show();
                    onStart();
                }
            }
        });
        binding.large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.large.isChecked()) {
                    binding.largetxt.setTextColor(Color.parseColor("#011224"));
                    binding.smalltxt.setTextColor(Color.parseColor("#9EA6B9"));
                    binding.mediumtxt.setTextColor(Color.parseColor("#9EA6B9"));
                    binding.small.setChecked(false);
                    binding.medium.setChecked(false);
                    Constant.setSfFunction(getApplicationContext());
                    Constant.setSF.putString(Constant.Font_Size,Constant.large);
                    Constant.setSF.apply();
                    Toast.makeText(sizeActivity.this, "large", Toast.LENGTH_SHORT).show();
                    onStart();

                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        SwipeNavigationHelper.finishWithSwipe(sizeActivity.this);
        
        //TransitionHelper.performTransition(((Activity)mContext));
    }
}