package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;

import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivityGroupInfoBinding;

import java.util.Objects;

public class groupInfo extends AppCompatActivity {

    ActivityGroupInfoBinding binding;
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

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.family.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.grp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.four.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.audio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.video.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.created.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.prachi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.eight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.links.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.media.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.and.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.docs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.participant.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.joey.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.jot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.jjj.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.family.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.grp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.four.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.audio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.video.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.created.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.prachi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.eight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.links.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.media.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.and.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.docs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.participant.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.joey.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.jot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.jjj.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.family.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.grp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.four.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.audio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.video.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.created.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.prachi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.eight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.links.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.media.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.and.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.docs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.participant.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.joey.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.jot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.jjj.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(this.getResources().getColor(android.R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.menuRect.getVisibility() == View.GONE) {
                    binding.menuRect.setVisibility(View.VISIBLE);
                } else if (binding.menuRect.getVisibility() == View.VISIBLE) {
                    binding.menuRect.setVisibility(View.GONE);
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
}