package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.databinding.ActivitySponsredBinding;

public class sponsred extends AppCompatActivity {

    ActivitySponsredBinding binding;
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
                binding.mainTiktke.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.live.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.nearby.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.a1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.a2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.a3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.a4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.a5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.t3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.t4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.t5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);

            } else if (fontSizePref.equals(Constant.medium)) {
                binding.mainTiktke.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.live.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.nearby.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.a1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.a2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.a3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.a4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.a5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.t3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.t4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.t5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.mainTiktke.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.live.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.nearby.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.a1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.a2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.a3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.a4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.a5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.t3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.t4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.t5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySponsredBinding.inflate(getLayoutInflater());
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
        binding.nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), nearbyScreen.class));
            }
        });
        binding.hori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), showLiveScreen.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        SwipeNavigationHelper.finishWithSwipe(sponsred.this);
         
        //TransitionHelper.performTransition(((Activity)mContext));
    }
}