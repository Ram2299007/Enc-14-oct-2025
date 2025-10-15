package com.Appzia.enclosure.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.contactAdapter;
import com.Appzia.enclosure.Model.contactUploadModel;
import com.Appzia.enclosure.R;

import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityWhatsTheCodeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class whatsTheCode extends AppCompatActivity {
    ActivityWhatsTheCodeBinding binding;
    String phoneKy, uidKey, country_codeKey;


    public static ProgressDialog dialog;
    public File file;


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static Context mContext;
    private static final String[] PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

    public static String globalNumber;

    public static String token;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    ArrayList<contactUploadModel> contactList = new ArrayList<>();
    contactAdapter adapter;

    String cId;

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
    protected void onStart() {
        super.onStart();


        try {

            phoneKy = getIntent().getStringExtra("phoneKy");
            uidKey = getIntent().getStringExtra("uidKey");
            //  mob_otpKey = getIntent().getStringExtra("mob_otpKey");
            country_codeKey = getIntent().getStringExtra("country_codeKey");
            binding.mob.setText(phoneKy);

        } catch (Exception ex) {
        }

        //     Random random = new Random();


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
        binding = ActivityWhatsTheCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = binding.getRoot().getContext();
        dialog = new ProgressDialog(whatsTheCode.this);


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        cId = getIntent().getStringExtra("c_id");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            checkAndRequestPermissions();
            requestContactPermission();
        } else {
            checkAndRequestPermissions2();
            requestContactPermission();
        }


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (!task.isSuccessful()) {
                    Log.e("FCM", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                token = task.getResult();
                if (token != null) {
                    Log.d("token", "token : " + token);
                } else {
                    Log.d("token", "token is null");
                }
            }
        });


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


                    // Toast.makeText(whatsTheCode.this, "clicked", Toast.LENGTH_SHORT).show();

                    if (cId != null) {
                        Webservice.send_otp2(mContext, phoneKy, country_codeKey, "null", cId);
                    } else {
                        Toast.makeText(whatsTheCode.this, "Empty c_id", Toast.LENGTH_SHORT).show();
                    }


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
                    Toast.makeText(whatsTheCode.this, "Invalid Otp ?", Toast.LENGTH_SHORT).show();
                    binding.one.setText("");
                    binding.two.setText("");
                    binding.three.setText("");
                    binding.four.setText("");
                    binding.five.setText("");
                    binding.six.setText("");
                    binding.one.requestFocus();
                } else {

                    if (file != null) {
                        Webservice.verify_mobile_otp(mContext, uidKey, binding.one.getText().toString() + binding.two.getText().toString() + binding.three.getText().toString() + binding.four.getText().toString() + binding.five.getText().toString() + binding.six.getText().toString(), country_codeKey, token, "1", binding.progressBar, file, file.getName().toString());
                    } else {

                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                            finish(); // or navigate to next screen
                        } else {

                            Constant.dialoguePermision(mContext);
                            TextView textView = Constant.dialogLayoutColor.findViewById(R.id.text);
                            textView.setText("Please grant the required permissions in Settings to continue.");
                            AppCompatImageView dismiss = Constant.dialogLayoutColor.findViewById(R.id.dismiss);
                            AppCompatButton gotosetting = Constant.dialogLayoutColor.findViewById(R.id.gotosetting);
                            Constant.dialogLayoutColor.show();

                            gotosetting.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    SwipeNavigationHelper.startActivityWithSwipe(whatsTheCode.this, intent);
                                    Constant.dialogLayoutColor.dismiss();
                                }
                            });

                            dismiss.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Constant.dialogLayoutColor.dismiss();
                                }
                            });
                        }

                    }

                }
            }
        });


    }


//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(getApplicationContext(), whatsYourNumber.class));
//    }

    @Override
    public void onBackPressed() {
        SwipeNavigationHelper.finishWithSwipe(whatsTheCode.this);

        // TransitionHelper.performTransition(((Activity)mContext));
    }


//


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkAndRequestPermissions() {
        try {
            List<String> listPermissionsNeeded = new ArrayList<>();

            // Required: READ_CONTACTS
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            }

            // READ_EXTERNAL_STORAGE is for API < 33
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            } else {
                // For Android 13+ use READ_MEDIA_IMAGES
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
//                }
            }

            // CAMERA
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }

            // RECORD_AUDIO
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
            }

            // POST_NOTIFICATIONS (API 33+ only)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);
                }
            }

            // Optional (Normal permissions — auto-granted)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "WAKE_LOCK is normal; no runtime request needed.");
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "MODIFY_AUDIO_SETTINGS is normal; no runtime request needed.");
            }

            // Request dangerous permissions if any are missing
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(
                        this,
                        listPermissionsNeeded.toArray(new String[0]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS
                );
                return false;
            }
        } catch (Exception ex) {
            Log.e("Permissions", "Permission check error: " + ex.getMessage());
        }
        return true;
    }


    private boolean checkAndRequestPermissions2() {
        try {
            List<String> listPermissionsNeeded = new ArrayList<>();

            // Dangerous permissions — must request at runtime
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);


            } else {
//                // For Android 13+ use READ_MEDIA_IMAGES
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
//                }
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
            }

            // POST_NOTIFICATIONS is needed only on Android 13+ (TIRAMISU)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);
                }
            }

            // Optional: Log if WAKE_LOCK or MODIFY_AUDIO_SETTINGS are not granted (not required to request)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "WAKE_LOCK is a normal permission and is granted automatically.");
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "MODIFY_AUDIO_SETTINGS is a normal permission and is granted automatically.");
            }

            // Request only dangerous permissions
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(
                        this,
                        listPermissionsNeeded.toArray(new String[0]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS
                );
                return false;
            }
        } catch (Exception ex) {
            Log.e("Permissions", "Permission check error: " + ex.getMessage());
        }
        return true;
    }


    public void requestContactPermission() {
        if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(whatsTheCode.this, Manifest.permission.READ_CONTACTS)) {

                Constant.dialoguePermision(mContext);
                TextView textView = Constant.dialogLayoutColor.findViewById(R.id.text);
                textView.setText("Please Grant The Contacts permissions In Settings To Continue.");
                AppCompatImageView dismiss = Constant.dialogLayoutColor.findViewById(R.id.dismiss);
                AppCompatButton gotosetting = Constant.dialogLayoutColor.findViewById(R.id.gotosetting);
                Constant.dialogLayoutColor.show();

                gotosetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        SwipeNavigationHelper.startActivityWithSwipe(whatsTheCode.this, intent);
                        Constant.dialogLayoutColor.dismiss();
                    }
                });

                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constant.dialogLayoutColor.dismiss();
                    }
                });


            } else {
                ActivityCompat.requestPermissions(whatsTheCode.this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            getContactList(binding.progressBar);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContactList(binding.progressBar);
            } else {
                //  Toast.makeText(binding.getRoot().getContext(), "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
            }
        }


    }

    public void getContactList(ProgressBar progressBar) {

        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor != null) {
            HashSet<String> mobileNoSet = new HashSet<String>();
            HashSet<String> nameSet = new HashSet<String>();
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name = "", number;
                JSONArray arr = new JSONArray();

                // Counter for limiting to the first 100 contacts
                int contactCounter = 0;

                // change on need 100 to 1000
                while (cursor.moveToNext() && contactCounter < 10000) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    number = number.replace(" ", "");

                    if (!mobileNoSet.contains(number) && !nameSet.contains(name)) {
                        contactList.add(new contactUploadModel(name, number));
                        mobileNoSet.add(number);
                        nameSet.add(name);
                        PhoneNumberWithoutCountryCode(number);
                        uidKey = getIntent().getStringExtra("uidKey");
                        JSONObject obj2 = new JSONObject();
                        country_codeKey = getIntent().getStringExtra("country_codeKey");
                        phoneKy = getIntent().getStringExtra("phoneKy");
                        // trim issue
                        obj2.put("uid", uidKey);
                        obj2.put("mobile_no", phoneKy);
                        obj2.put("contact_name", name);
                        obj2.put("contact_number", country_codeKey + globalNumber.replaceAll("[()\\s-]+", "").trim());
                        arr.put(obj2);

                        // Increment the contact counter
                        contactCounter++;
                    }
                }

                Constant.getSfFuncion(mContext);
                uidKey = getIntent().getStringExtra("uidKey");

                String fileNameUid = "contact_" + uidKey;
                String dataNew = arr.toString();
                String final_data = "{ \"contact\":" + dataNew + "}";
                File cacheDir = getCacheDir();
                file = new File(cacheDir, fileNameUid + ".json");
                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write(final_data);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

    }

    public static String PhoneNumberWithoutCountryCode(String phoneNumberWithCountryCode) {//+91 7698989898
        Pattern compile = Pattern.compile("\\+(?:998|996|995|994|993|992|977|976|975|974|973|972|971|970|968|967|966|965|964|963|962|961|960|886|880|856|855|853|852|850|692|691|690|689|688|687|686|685|683|682|681|680|679|678|677|676|675|674|673|672|670|599|598|597|595|593|592|591|590|509|508|507|506|505|504|503|502|501|500|423|421|420|389|387|386|385|383|382|381|380|379|378|377|376|375|374|373|372|371|370|359|358|357|356|355|354|353|352|351|350|299|298|297|291|290|269|268|267|266|265|264|263|262|261|260|258|257|256|255|254|253|252|251|250|249|248|246|245|244|243|242|241|240|239|238|237|236|235|234|233|232|231|230|229|228|227|226|225|224|223|222|221|220|218|216|213|212|211|98|95|94|93|92|91|90|86|84|82|81|66|65|64|63|62|61|60|58|57|56|55|54|53|52|51|49|48|47|46|45|44\\D?1624|44\\D?1534|44\\D?1481|44|43|41|40|39|36|34|33|32|31|30|27|20|7|1\\D?939|1\\D?876|1\\D?869|1\\D?868|1\\D?849|1\\D?829|1\\D?809|1\\D?787|1\\D?784|1\\D?767|1\\D?758|1\\D?721|1\\D?684|1\\D?671|1\\D?670|1\\D?664|1\\D?649|1\\D?473|1\\D?441|1\\D?345|1\\D?340|1\\D?284|1\\D?268|1\\D?264|1\\D?246|1\\D?242|1)\\D?");
        globalNumber = phoneNumberWithCountryCode.replaceAll(compile.pattern(), "");
        if (globalNumber != null) {
            Log.e("number::_>", globalNumber);//OutPut::7698989898
        } else {
            Log.e("number::_>", "globalNumber is null");
        }
        return globalNumber;
    }
}