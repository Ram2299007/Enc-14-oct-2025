package com.Appzia.enclosure.Screens;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.contactAdapter;
import com.Appzia.enclosure.Model.contactUploadModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityOtpverifyScreenBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class otpverifyScreenDelete extends AppCompatActivity {

    ActivityOtpverifyScreenBinding binding;

    String phoneKy, uidKey, country_codeKey;


    public static ProgressDialog dialog;


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static Context mContext;
    public static String globalNumber;

    public static String token;

    ArrayList<contactUploadModel> contactList = new ArrayList<>();
    contactAdapter adapter;

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
        }else{
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
    protected void onStart() {
        super.onStart();


        try {

            phoneKy = getIntent().getStringExtra("phoneKy");
            uidKey = getIntent().getStringExtra("uidKey");
            //    mob_otpKey = getIntent().getStringExtra("mob_otpKey");
            country_codeKey = getIntent().getStringExtra("country_codeKey");
            binding.mob.setText(phoneKy);

        } catch (Exception ex) {
        }

//        Random random = new Random();
//
//
//        try {
//            int number = Integer.parseInt(mob_otpKey); // Generate a 6-digit OTP
//
//            Toast.makeText(this, String.valueOf(number), Toast.LENGTH_SHORT).show();
//
//
//            int first = number / 100000; // Extract the first digit
//            int second = (number / 10000) % 10; // Extract the second digit
//            int third = (number / 1000) % 10; // Extract the third digit
//            int four = (number / 100) % 10; // Extract the fourth digit
//            int five = (number / 10) % 10; // Extract the fifth digit
//            int six = number % 10; // for six number
//
//
//            Log.d("All digits", String.valueOf(number));
//
//            Log.d("firstNumber", String.valueOf(first));
//            binding.one.setText(String.valueOf(first));
//            Log.d("secondNumber", String.valueOf(second));
//            binding.two.setText(String.valueOf(second));
//            Log.d("thirdNumber", String.valueOf(third));
//            binding.three.setText(String.valueOf(third));
//
//            Log.d("fourNumber", String.valueOf(four));
//            binding.four.setText(String.valueOf(four));
//
//            Log.d("fiveNumber", String.valueOf(five));
//            binding.five.setText(String.valueOf(five));
//
//            Log.d("sixNumber", String.valueOf(six));
//            binding.six.setText(String.valueOf(six));
//        } catch (Exception ignored) {
//        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpverifyScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        mContext = binding.getRoot().getContext();
        dialog = new ProgressDialog(otpverifyScreenDelete.this);

        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            checkAndRequestPermissions();
        } else {

            checkAndRequestPermissions2();
        }


        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                Log.d("token", "retrieve token successful : " + token);
                this.token = token;
            } else {
                Log.w("token", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(task -> Log.v("token", "This is the token : " + task.getResult()));


        binding.one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (binding.one.length() == 1) {
                    binding.two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //validation for otp edittext

                if (binding.two.length() == 1) {
                    binding.three.requestFocus();
                }

                if (binding.two.getText().toString().isEmpty()) {
                    binding.one.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //validation for otp edittext


                if (binding.three.length() == 1) {
                    binding.four.requestFocus();
                }

                if (binding.three.getText().toString().isEmpty()) {
                    binding.two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //validation for otp edittext


                if (binding.four.length() == 1) {
                    binding.five.requestFocus();
                }

                if (binding.four.getText().toString().isEmpty()) {
                    binding.three.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (binding.five.length() == 1) {
                    binding.six.requestFocus();
                }

                if (binding.five.getText().toString().isEmpty()) {
                    binding.four.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.six.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //validation for otp edittext
                if (binding.six.getText().toString().isEmpty()) {
                    binding.five.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.sendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.sendAgain.getText().toString().equals("Send again")) {


                    // Toast.makeText(otpverifyScreenDelete.this, "clicked", Toast.LENGTH_SHORT).show();
                    Constant.getSfFuncion(mContext);
                    String mobileOld= Constant.getSF.getString(Constant.PHONE_NUMBERKEY,"");

                    Webservice.reSendOtpDelete(mContext,mobileOld);

                    binding.one.setText("");
                    binding.two.setText("");
                    binding.three.setText("");
                    binding.four.setText("");
                    binding.five.setText("");
                    binding.six.setText("");
                    binding.one.requestFocus();


                    new CountDownTimer(60000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            binding.seconds.setVisibility(View.VISIBLE);
                            binding.sendAgain.setText("Send in");
                            binding.seconds.setTextColor(Color.parseColor("#9EA6B9"));
                            binding.sendAgain.setTextColor(Color.parseColor("#9EA6B9"));
                            binding.seconds.setText(millisUntilFinished / 1000 + " sec.");
                            // logic to set the EditText could go here
                        }

                        public void onFinish() {
                            binding.seconds.setVisibility(View.GONE);
                            binding.sendAgain.setTextColor(getResources().getColor(R.color.blacktogray));
                            binding.sendAgain.setText("Send again");
                        }

                    }.start();


                }


            }
        });


        binding.verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.one.getText().toString().equals("") || binding.two.getText().toString().equals("") || binding.three.getText().toString().equals("") || binding.four.getText().toString().equals("") || binding.five.getText().toString().equals("") || binding.six.getText().toString().equals("")) {
                    Toast.makeText(otpverifyScreenDelete.this, "Invalid Otp ?", Toast.LENGTH_SHORT).show();
                    binding.one.setText("");
                    binding.two.setText("");
                    binding.three.setText("");
                    binding.four.setText("");
                    binding.five.setText("");
                    binding.six.setText("");
                    binding.one.requestFocus();
                } else {


                    Webservice.verify_otp_for_delete_user(mContext, uidKey, binding.one.getText().toString() + binding.two.getText().toString() + binding.three.getText().toString() + binding.four.getText().toString() + binding.five.getText().toString() + binding.six.getText().toString());

                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkAndRequestPermissions() {


        try {
            int contact = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
            int storage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

            int camera = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA);
            int audio = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
            int notification = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WAKE_LOCK);
            int POST_NOTIFICATIONS = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (contact != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            }
            if (camera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
            }

            if (storage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

            }
            if (audio != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);

            }
            if (notification != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WAKE_LOCK);

            }
            if (POST_NOTIFICATIONS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);

            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(otpverifyScreenDelete.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

                return false;
            }
        } catch (Exception ex) {
            Log.d("permission%%", ex.getMessage());
        }
        return true;
    }

    private boolean checkAndRequestPermissions2() {


        try {
            int contact = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
            int storage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

            int camera = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA);
            int audio = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
            int notification = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WAKE_LOCK);
            int POST_NOTIFICATIONS = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS);

            List<String> listPermissionsNeeded = new ArrayList<>();

            if (contact != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            }
            if (camera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
            }

            if (storage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

            }
            if (audio != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);

            }
            if (notification != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WAKE_LOCK);

            }
            if (POST_NOTIFICATIONS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);

            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(otpverifyScreenDelete.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

                return false;
            }
        } catch (Exception ex) {
            Log.d("permission%%", ex.getMessage());
        }
        return true;
    }

}