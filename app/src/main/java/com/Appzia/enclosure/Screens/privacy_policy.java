package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityPrivacyPolicyBinding;

import java.util.Objects;

public class privacy_policy extends AppCompatActivity {

    ActivityPrivacyPolicyBinding binding;
    public static String fontSizePref;
    String uid;
    public static  Context mContext;
    String themColor;
    ColorStateList tintList;
    public static TextView ol;

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


            WebSettings webSettings = binding.webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);

            binding.webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true; // Prevent the system browser from opening the URL
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    // Hide the ProgressBar when the page finishes loading
                    binding.progressBar.setVisibility(View.GONE);
                }
            });

// Load the URL
            binding.webView.loadUrl("https://enclosureapp.com/black_policy");


        }else{
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

                // Set dark status bar (black text/icons) for light mode
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            WebSettings webSettings = binding.webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webSettings.setDomStorageEnabled(true); // To enable DOM storage


            binding.webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true; // Prevent the system browser from opening the URL
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    // Hide the ProgressBar when the page finishes loading
                    binding.progressBar.setVisibility(View.GONE);
                }
            });

            binding.webView.loadUrl("https://enclosureapp.com/privacy-policy");

        }




    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
            ol = findViewById(R.id.o1);


             //   Webservice.get_privacy_policy(mContext,uid,webView);



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

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);



            } else if (fontSizePref.equals(Constant.large)) {

            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = binding.getRoot().getContext();

        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

      //  webView = findViewById(R.id.webViewk);





        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
              //  TransitionHelper.performTransition(((Activity)mContext));
            }
        });

        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));



            try {
                if (themColor.equals("#ff0080")) {

                    binding.mainLogo.setImageResource(R.drawable.pinklogopng);
                } else if (themColor.equals("#00A3E9")) {

                    binding.mainLogo.setImageResource(R.drawable.ec_modern);
                } else if (themColor.equals("#7adf2a")) {


                    binding.mainLogo.setImageResource(R.drawable.popatilogopng);

                } else if (themColor.equals("#ec0001")) {

                    binding.mainLogo.setImageResource(R.drawable.redlogopng);

                } else if (themColor.equals("#16f3ff")) {

                    binding.mainLogo.setImageResource(R.drawable.bluelogopng);

                } else if (themColor.equals("#FF8A00")) {

                    binding.mainLogo.setImageResource(R.drawable.orangelogopng);


                } else if (themColor.equals("#7F7F7F")) {

                    binding.mainLogo.setImageResource(R.drawable.graylogopng);


                } else if (themColor.equals("#D9B845")) {

                    binding.mainLogo.setImageResource(R.drawable.yellowlogopng);


                } else if (themColor.equals("#346667")) {

                    binding.mainLogo.setImageResource(R.drawable.greenlogoppng);


                } else if (themColor.equals("#9846D9")) {

                    binding.mainLogo.setImageResource(R.drawable.voiletlogopng);


                } else if (themColor.equals("#A81010")) {

                    binding.mainLogo.setImageResource(R.drawable.red2logopng);


                } else {

                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }

    }

    @Override
    public void onBackPressed() {
        SmoothNavigationHelper.finishWithSlideToRight(privacy_policy.this);
    }

}