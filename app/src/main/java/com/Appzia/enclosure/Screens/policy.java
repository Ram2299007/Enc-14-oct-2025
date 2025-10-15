package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivityPolicyBinding;

import java.util.Objects;

public class policy extends AppCompatActivity {
    ActivityPolicyBinding binding;
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
                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.who.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.lastseen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.block.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.onezero.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.who.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.lastseen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.block.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.onezero.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.who.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.lastseen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.block.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.onezero.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = binding.getRoot().getContext();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(this.getResources().getColor(android.R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
               // TransitionHelper.performTransition(((Activity)mContext));
            }
        });
    }
}