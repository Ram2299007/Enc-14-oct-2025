package com.Appzia.enclosure.Screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityLockscreenBinding;

import java.util.Objects;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class lockscreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityLockscreenBinding binding;
    CircularSeekBar progressBar;
    public static String lock_status = "1";
    private CardView customToastCard;
    int CurrentProgress = 0;
    Context mContext;
    private TextView customToastText;


    String value;
    int count = 0;
    String themColor;
    ColorStateList tintList;
    public static String fontSizePref;

    String uid;

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Constant.getSfFuncion(getApplicationContext());
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
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

                binding.setlock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            } else if (fontSizePref.equals(Constant.medium)) {


                binding.setlock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            } else if (fontSizePref.equals(Constant.large)) {

                binding.setlock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            }
        } catch (Exception ignored) {

        }


        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
//          themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            //  binding.menuPoint.setColorFilter(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.pinkhover);
                    binding.threesixty.setBackgroundTintList(tintList);

                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);

                } else if (themColor.equals("#00A3E9")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.buttonhoverfordone);
                    binding.threesixty.setBackgroundTintList(tintList);
                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);
                } else if (themColor.equals("#7adf2a")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.popatihover);
                    binding.threesixty.setBackgroundTintList(tintList);

                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);

                } else if (themColor.equals("#ec0001")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));

                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.red_one_hover);
                    binding.threesixty.setBackgroundTintList(tintList);

                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);
                } else if (themColor.equals("#16f3ff")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.blue_hover);
                    binding.threesixty.setBackgroundTintList(tintList);

                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);
                } else if (themColor.equals("#FF8A00")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.orangehover);
                    binding.threesixty.setBackgroundTintList(tintList);
                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);

                } else if (themColor.equals("#7F7F7F")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.gray_hover);
                    binding.threesixty.setBackgroundTintList(tintList);

                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);

                } else if (themColor.equals("#D9B845")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.yellow_hover);
                    binding.threesixty.setBackgroundTintList(tintList);

                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);
                } else if (themColor.equals("#346667")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.greenoriginalhover);
                    binding.threesixty.setBackgroundTintList(tintList);

                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);

                } else if (themColor.equals("#9846D9")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.voilethover);
                    binding.threesixty.setBackgroundTintList(tintList);

                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);
                } else if (themColor.equals("#A81010")) {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.red_two_hover);
                    binding.threesixty.setBackgroundTintList(tintList);

                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);

                } else {
                    binding.progressBar.setCircleProgressColor(Color.parseColor(themColor));
                    binding.circle.setBackgroundTintList(tintList);
//                    binding.linear.setBackgroundTintList(tintList);
                    binding.linear.setBackgroundResource(R.drawable.buttonhoverfordone);
                    binding.threesixty.setBackgroundTintList(tintList);

                    binding.setlock.setTextColor(Color.parseColor(themColor));
                    binding.setImg.setImageTintList(tintList);
                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }


    }

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        customToastCard = findViewById(R.id.includedToast);
        customToastText = customToastCard.findViewById(R.id.toastText);
        mContext = binding.getRoot().getContext();

        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        progressBar = findViewById(R.id.progressBar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.degree, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);

        binding.progressBar.setMax(360);
        binding.progressBar.setProgress(0);


        binding.progressBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(@Nullable CircularSeekBar circularSeekBar, float progress, boolean b) {
                String progressData = String.valueOf(progress);
                int intValue = (int) Math.round(Float.parseFloat(progressData));
                int finalValue = (int) (intValue);
                Integer intValueData = (int) Math.round(Float.parseFloat(String.valueOf(finalValue)));

                binding.bigDegree.setText(String.valueOf(intValueData + "\u00B0"));
            }

            @Override
            public void onStopTrackingTouch(@Nullable CircularSeekBar circularSeekBar) {

            }

            @Override
            public void onStartTrackingTouch(@Nullable CircularSeekBar circularSeekBar) {

            }
        });
        binding.bigDegree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }


                ///  Toast.makeText(mContext,  binding.bigDegree.getText().toString(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.bigDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator50(mContext);
                }


                String[] parts = binding.bigDegree.getText().toString().split("°");

                String textBefore = parts[0];
                Log.d("textbefore", textBefore);

                count = Integer.parseInt(textBefore) + 1;

                if (count >= 360) {

                } else {

                    binding.bigDegree.setText(String.valueOf(count) + "\u00B0");
                    binding.progressBar.setProgress(count);
                }


            }
        });


        binding.l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spinner.performClick();
            }
        });

        binding.set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.bigDegree.getText().toString().equals("0" + "\u00B0")) {
            //        Toast.makeText(mContext, "please set degree first", Toast.LENGTH_SHORT).show();
                    Constant.showCustomToast("please set degree first",customToastCard,customToastText);
                } else {
                    Constant.setSfFunction(mContext);
                    Constant.setSF.putString("sleepKeyCheckOFF", "off");
                    Constant.setSF.apply();
                    Webservice.lock_screen(mContext, uid, lock_status, binding.bigDegree.getText().toString().replaceAll("\u00B0", ""), "", findViewById(R.id.includedToast), findViewById(R.id.toastText));
                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        //  Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        if (text.equals("")) {

        } else if (text.equals("360")) {
            binding.degreeValue.setText(text + "\u00B0");
            CurrentProgress = Integer.parseInt(text);
            binding.progressBar.setProgress(CurrentProgress);
            binding.bigDegree.setText(text + "\u00B0");
        } else if (text.equals("180")) {
            binding.degreeValue.setText(text + "\u00B0");
            CurrentProgress = Integer.parseInt(text);
            binding.progressBar.setProgress(CurrentProgress);
            binding.bigDegree.setText(text + "\u00B0");
        } else if (text.equals("90")) {
            binding.degreeValue.setText(text + "\u00B0");
            CurrentProgress = Integer.parseInt(text);
            binding.progressBar.setProgress(CurrentProgress);
            binding.bigDegree.setText(text + "\u00B0");
        } else if (text.equals("0")) {
            binding.degreeValue.setText(text + "\u00B0");
            CurrentProgress = Integer.parseInt(text);
            binding.progressBar.setProgress(CurrentProgress);
            binding.bigDegree.setText(text + "\u00B0");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("lockKeyIntent", "lockKeyIntent");
//        startActivity(intent);

        SwipeNavigationHelper.finishWithSwipe(lockscreen.this);

        //TransitionHelper.performTransition(((Activity)mContext));
    }
}