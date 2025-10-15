package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
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
import com.Appzia.enclosure.databinding.ActivitySettingBinding;

import java.util.Objects;

public class settingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    public static String fontSizePref;
    Context mContext;

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
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

                // Set dark status bar (black text/icons) for light mode
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        Webservice.getBlockCount(mContext, uid, binding.blockCount);
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
                binding.setting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.accountTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.prive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.notiii.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.appl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.help.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

                binding.chaneg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.block.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.msgs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.eng.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);


            } else if (fontSizePref.equals(Constant.medium)) {

                binding.setting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.accountTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.prive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.notiii.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.appl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.help.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                binding.chaneg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.block.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.msgs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.eng.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.setting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.accountTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.prive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.notiii.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.appl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.help.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

                binding.chaneg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.block.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.msgs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.eng.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = binding.getRoot().getContext();


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeNavigationHelper.startActivityWithSwipe(settingActivity.this, new Intent(mContext, Acount.class));
                //((Activity)mContext).

            }
        });

        binding.policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeNavigationHelper.startActivityWithSwipe(settingActivity.this, new Intent(mContext, policy.class));
                //((Activity)mContext).

            }
        });

        binding.noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeNavigationHelper.startActivityWithSwipe(settingActivity.this, new Intent(mContext, notinew.class));
                //((Activity)mContext).

            }
        });

        binding.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeNavigationHelper.startActivityWithSwipe(settingActivity.this, new Intent(mContext, contact_us.class));
                //((Activity)mContext).

            }
        });
        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeNavigationHelper.startActivityWithSwipe(settingActivity.this, new Intent(mContext, privacy_policy.class));
            }
        });

        binding.blockLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.blockCount.getText().toString().equals("0")) {
                    SwipeNavigationHelper.startActivityWithSwipe(settingActivity.this, new Intent(mContext, block_contact_activity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(mContext, MainActivity.class);
//        startActivity(intent);

        SwipeNavigationHelper.finishWithSwipe(settingActivity.this);

        //TransitionHelper.performTransition(((Activity)mContext));
    }
}