package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivityThemeScreenBinding;

import java.util.Objects;

public class themeScreen extends AppCompatActivity {
    ActivityThemeScreenBinding binding;
    public static String colorGlobal;
    ColorStateList tintList;
    public static String fontSizePref;
    String themColor;
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
            // Your existing color logic for dark mode

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");


            try {
                if (themColor.equals("#ff0080")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.pinksleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (themColor.equals("#00A3E9")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (themColor.equals("#7adf2a")) {

//                    sb.setThumb(getResources().getDrawable(R.drawable.popatisleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                    binding.mainlinear.setBackgroundTintList(tintList);

                } else if (themColor.equals("#ec0001")) {

//                    sb.setThumb(getResources().getDrawable(R.drawable.redonesleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                    binding.mainlinear.setBackgroundTintList(tintList);

                } else if (themColor.equals("#16f3ff")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.bluesleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (themColor.equals("#FF8A00")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.orangesleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (themColor.equals("#7F7F7F")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.graysleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                    binding.mainlinear.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {
                    //       sb.setThumb(getResources().getDrawable(R.drawable.yellowsleep));

                    tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (themColor.equals("#346667")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.greensleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (themColor.equals("#9846D9")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.voiletsleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (themColor.equals("#A81010")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.redtwonewsleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else {
//                    sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                }
            } catch (Exception ignored) {
//                sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                binding.mainlinear.setBackgroundTintList(tintList);
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
            // Your existing color logic for light mode

            Log.d("TAG", "onResume**: " + "light mode");
            tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
            binding.mainlinear.setBackgroundTintList(tintList);

        }
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
                binding.theme.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            } else if (fontSizePref.equals(Constant.medium)) {
                binding.theme.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.theme.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);  // Set the status bar to transparent
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        mContext = binding.getRoot().getContext();

        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {

                    binding.logoBtn.setImageResource(R.drawable.pinklogopng);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));

                } else if (themColor.equals("#00A3E9")) {

                    binding.logoBtn.setImageResource(R.drawable.ec_modern);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                } else if (themColor.equals("#7adf2a")) {


                    binding.logoBtn.setImageResource(R.drawable.popatilogopng);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));

                } else if (themColor.equals("#ec0001")) {

                    binding.logoBtn.setImageResource(R.drawable.redlogopng);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));

                } else if (themColor.equals("#16f3ff")) {

                    binding.logoBtn.setImageResource(R.drawable.bluelogopng);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));

                } else if (themColor.equals("#FF8A00")) {

                    binding.logoBtn.setImageResource(R.drawable.orangelogopng);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));


                } else if (themColor.equals("#7F7F7F")) {

                    binding.logoBtn.setImageResource(R.drawable.graylogopng);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));


                } else if (themColor.equals("#D9B845")) {

                    binding.logoBtn.setImageResource(R.drawable.yellowlogopng);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));


                } else if (themColor.equals("#346667")) {

                    binding.logoBtn.setImageResource(R.drawable.greenlogoppng);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));


                } else if (themColor.equals("#9846D9")) {

                    binding.logoBtn.setImageResource(R.drawable.voiletlogopng);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));


                } else if (themColor.equals("#A81010")) {

                    binding.logoBtn.setImageResource(R.drawable.red2logopng);
                    binding.noti1.setBackgroundTintList(tintList);
                    binding.noti2.setBackgroundTintList(tintList);
                    binding.noti3.setBackgroundTintList(tintList);
                    binding.noti4.setBackgroundTintList(tintList);
                    binding.txt1.setTextColor(Color.parseColor(themColor));
                    binding.txt2.setTextColor(Color.parseColor(themColor));
                    binding.txt3.setTextColor(Color.parseColor(themColor));
                    binding.txt4.setTextColor(Color.parseColor(themColor));
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));


                } else {

                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }

                colorGlobal = "#ff0080";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));


                binding.logoBtn.setImageResource(R.drawable.pinklogopng);

                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));


            }
        });
        binding.def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }

                colorGlobal = "#00A3E9";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));
                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));
                binding.logoBtn.setImageResource(R.drawable.ec_modern);

            }
        });
        binding.popati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                colorGlobal = "#7adf2a";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));
                binding.logoBtn.setImageResource(R.drawable.popatilogopng);

                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));
            }
        });
        binding.red1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                colorGlobal = "#ec0001";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));
                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));
                binding.logoBtn.setImageResource(R.drawable.redlogopng);

            }
        });
        binding.blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                colorGlobal = "#16f3ff";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));
                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));
                binding.logoBtn.setImageResource(R.drawable.bluelogopng);

            }
        });
        binding.orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                colorGlobal = "#FF8A00";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));
                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));
                binding.logoBtn.setImageResource(R.drawable.orangelogopng);


            }
        });


        binding.faintblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                colorGlobal = "#7F7F7F";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));
                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));
                binding.logoBtn.setImageResource(R.drawable.graylogopng);


            }
        });


        binding.yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                colorGlobal = "#D9B845";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));
                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));

                binding.logoBtn.setImageResource(R.drawable.yellowlogopng);

            }
        });

        binding.greensvg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                colorGlobal = "#346667";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));
                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));

                binding.logoBtn.setImageResource(R.drawable.greenlogoppng);

            }
        });
        binding.darkpink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                colorGlobal = "#9846D9";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));
                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));
                binding.logoBtn.setImageResource(R.drawable.voiletlogopng);


            }
        });
        binding.red2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                colorGlobal = "#A81010";
                setLienear(colorGlobal);
                tintList = ColorStateList.valueOf(Color.parseColor(colorGlobal));
                binding.noti1.setBackgroundTintList(tintList);
                binding.noti2.setBackgroundTintList(tintList);
                binding.noti3.setBackgroundTintList(tintList);
                binding.noti4.setBackgroundTintList(tintList);
                binding.txt1.setTextColor(Color.parseColor(colorGlobal));
                binding.txt2.setTextColor(Color.parseColor(colorGlobal));
                binding.txt3.setTextColor(Color.parseColor(colorGlobal));
                binding.txt4.setTextColor(Color.parseColor(colorGlobal));
                binding.menuPoint.setColorFilter(Color.parseColor(colorGlobal));
                binding.logoBtn.setImageResource(R.drawable.red2logopng);


            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.setSfFunction(getApplicationContext());
                Constant.setSF.putString(Constant.ThemeColorKey, colorGlobal);

                Constant.setSF.apply();

                /// neet to activate this
                try {
                    changeAppIcon();
                } catch (Exception e) {

                }

                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        SmoothNavigationHelper.finishWithSlideToRight(themeScreen.this);
    }

    private void changeAppIcon() {
        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();
        Constant.getSfFuncion(getApplicationContext());
        themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        tintList = ColorStateList.valueOf(Color.parseColor(themColor));

        // Default alias to enable (fallback)
        String aliasToEnable = packageName + ".SplashScreenMyAliasDefault";

        // Determine the alias to enable based on the theme color
        if (themColor.equals("#ff0080")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasPink";
        } else if (themColor.equals("#00A3E9")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasDefault";
        } else if (themColor.equals("#7adf2a")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasPopati";
        } else if (themColor.equals("#ec0001")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasRed";
        } else if (themColor.equals("#16f3ff")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasLightBlue";
        } else if (themColor.equals("#FF8A00")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasOrange";
        } else if (themColor.equals("#7F7F7F")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasgray";
        } else if (themColor.equals("#D9B845")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasyellow";
        } else if (themColor.equals("#346667")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasrichgreen";
        } else if (themColor.equals("#9846D9")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasVoilet";
        } else if (themColor.equals("#A81010")) {
            aliasToEnable = packageName + ".SplashScreenMyAliasred2";
        }

        try {
            // Disable all aliases except the one to enable
            String[] allAliases = {
                    packageName + ".SplashScreenMyAliasDefault",
                    packageName + ".SplashScreenMyAliasPink",
                    packageName + ".SplashScreenMyAliasPopati",
                    packageName + ".SplashScreenMyAliasRed",
                    packageName + ".SplashScreenMyAliasLightBlue",
                    packageName + ".SplashScreenMyAliasOrange",
                    packageName + ".SplashScreenMyAliasgray",
                    packageName + ".SplashScreenMyAliasyellow",
                    packageName + ".SplashScreenMyAliasrichgreen",
                    packageName + ".SplashScreenMyAliasVoilet",
                    packageName + ".SplashScreenMyAliasred2"
            };

            for (String alias : allAliases) {
                if (!alias.equals(aliasToEnable)) {
                    // Check if the alias is already disabled
                    if (packageManager.getComponentEnabledSetting(new ComponentName(packageName, alias))
                            != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                        packageManager.setComponentEnabledSetting(
                                new ComponentName(packageName, alias),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                        );
                    }
                }
            }

            // Enable the desired alias only if it's not already enabled
            if (packageManager.getComponentEnabledSetting(new ComponentName(packageName, aliasToEnable))
                    != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                packageManager.setComponentEnabledSetting(
                        new ComponentName(packageName, aliasToEnable),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                );
            }

            // Ensure the default component is disabled
            if (packageManager.getComponentEnabledSetting(new ComponentName(packageName, packageName + ".Screens.SplashScreenMy"))
                    != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                packageManager.setComponentEnabledSetting(
                        new ComponentName(packageName, packageName + ".Screens.SplashScreenMy"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                );
            }
        } catch (Exception ignored) {
            // Fallback: Enable the default alias in case of an error
            packageManager.setComponentEnabledSetting(
                    new ComponentName(packageName, packageName + ".SplashScreenMyAliasDefault"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
            );
        }
    }

    public void setLienear(String colorGlobal){

        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            try {
                if (colorGlobal.equals("#ff0080")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.pinksleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (colorGlobal.equals("#00A3E9")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (colorGlobal.equals("#7adf2a")) {

//                    sb.setThumb(getResources().getDrawable(R.drawable.popatisleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                    binding.mainlinear.setBackgroundTintList(tintList);

                } else if (colorGlobal.equals("#ec0001")) {

//                    sb.setThumb(getResources().getDrawable(R.drawable.redonesleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                    binding.mainlinear.setBackgroundTintList(tintList);

                } else if (colorGlobal.equals("#16f3ff")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.bluesleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (colorGlobal.equals("#FF8A00")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.orangesleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (colorGlobal.equals("#7F7F7F")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.graysleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                    binding.mainlinear.setBackgroundTintList(tintList);

                } else if (colorGlobal.equals("#D9B845")) {
                    //       sb.setThumb(getResources().getDrawable(R.drawable.yellowsleep));

                    tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (colorGlobal.equals("#346667")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.greensleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (colorGlobal.equals("#9846D9")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.voiletsleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else if (colorGlobal.equals("#A81010")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.redtwonewsleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                } else {
//                    sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                    binding.mainlinear.setBackgroundTintList(tintList);
                }
            } catch (Exception ignored) {
//                sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                binding.mainlinear.setBackgroundTintList(tintList);
            }
        }else{
            tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
            binding.mainlinear.setBackgroundTintList(tintList);
        }
    }



}
