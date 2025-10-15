package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivityNotinewBinding;

import java.util.Objects;

public class notinew extends AppCompatActivity {
    ActivityNotinewBinding binding;
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

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.con.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.play.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.defaut.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.dd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.noti.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.vibrate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.ring.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.vibratse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

                binding.light.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.white.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.call.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.def.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.dff.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
            } else if (fontSizePref.equals(Constant.medium)) {

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.con.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.play.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.defaut.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.dd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.noti.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.vibrate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.ring.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.vibratse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                binding.light.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.white.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.call.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.def.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.dff.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);


            } else if (fontSizePref.equals(Constant.large)) {

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.con.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.play.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.defaut.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.dd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.noti.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.vibrate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.ring.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.vibratse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

                binding.light.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.white.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.call.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.def.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.dff.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            }
        } catch (Exception ignored) {

        }

        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {


                    binding.switchcall.setTrackResource(R.drawable.bgtrack2pink);

                } else if (themColor.equals("#00A3E9")) {

                    //default blue

                    binding.switchcall.setTrackResource(R.drawable.bg_track2blue);

                } else if (themColor.equals("#7adf2a")) {


                    binding.switchcall.setTrackResource(R.drawable.bgtrack2popati);


                } else if (themColor.equals("#ec0001")) {


                    binding.switchcall.setTrackResource(R.drawable.bgtrack2redone);


                } else if (themColor.equals("#16f3ff")) {


                    binding.switchcall.setTrackResource(R.drawable.bgtrack2skyblue);


                } else if (themColor.equals("#FF8A00")) {


                    binding.switchcall.setTrackResource(R.drawable.bgtrack2orange);


                } else if (themColor.equals("#7F7F7F")) {


                    binding.switchcall.setTrackResource(R.drawable.bgtrack2gray);


                } else if (themColor.equals("#D9B845")) {


                    binding.switchcall.setTrackResource(R.drawable.bgtrackyelloe);


                } else if (themColor.equals("#346667")) {


                    binding.switchcall.setTrackResource(R.drawable.bgtrack2green);


                } else if (themColor.equals("#9846D9")) {

                    binding.switchcall.setTrackResource(R.drawable.bgtrack2voilet);


                } else if (themColor.equals("#A81010")) {


                    binding.switchcall.setTrackResource(R.drawable.bgtrack2redtwo);


                } else {

                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotinewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = binding.getRoot().getContext();

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

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

                //TransitionHelper.performTransition(((Activity)mContext));
            }
        });
    }
}