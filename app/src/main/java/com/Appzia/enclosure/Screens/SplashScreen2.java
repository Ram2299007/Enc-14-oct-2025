package com.Appzia.enclosure.Screens;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import com.Appzia.enclosure.Utils.EdgeToEdgeHelper;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivitySplashScreen2Binding;

public class SplashScreen2 extends AppCompatActivity {
    ActivitySplashScreen2Binding binding;
    ColorStateList tintList;
    public static String fontSizePref;
    String themColor;

    @Override
    protected void onResume() {
        super.onResume();
        // Status bar configuration is now handled by EdgeToEdgeHelper
        // No need for manual status bar handling as it's deprecated
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreen2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Setup edge-to-edge display
        EdgeToEdgeHelper.setupEdgeToEdge(this, binding.getRoot());

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), selectLanguage.class);
                startActivity(intent);

            }
        });
    }

}