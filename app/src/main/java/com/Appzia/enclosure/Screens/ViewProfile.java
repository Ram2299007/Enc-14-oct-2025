package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityViewProfileBinding;

import java.util.Objects;

public class ViewProfile extends AppCompatActivity {

    public static ImageView profile;
    ActivityViewProfileBinding binding;

    public static String fontSizePref;
    String uid;
    Context mContext;

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Constant.setSfFunction(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
         Webservice.get_profile_ViewProfile(mContext,uid);



        } catch (Exception ignored) {
        }


        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (fontSizePref.equals(Constant.small)) {
                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
        profile = findViewById(R.id.profile);

        mContext = ViewProfile.this;
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}