package com.Appzia.enclosure.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.Appzia.enclosure.Utils.SwipeNavigationHelper;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Model.contactUploadModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityWhatsYourNumberBinding;
import com.Appzia.enclosure.keyboardheight.KeyboardHeightObserver;
import com.Appzia.enclosure.keyboardheight.KeyboardHeightProvider;

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

public class whatsYourNumber extends AppCompatActivity implements KeyboardHeightObserver {

    ActivityWhatsYourNumberBinding binding;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static FrameLayout flagfrag;
    private static final String[] PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
    public static String globalNumber;
    public static RecyclerView fontrecyclerview;
    public static TextView countrycode;
    TextView flagFinal;
    ArrayList<contactUploadModel> contactList = new ArrayList<>();
    public static String uid;
    public File file;
    public static Dialog dialogLayoutFont;
    Context mContext;
    public static RelativeLayout relativelayout;
    String countrycode2 = "+1";
    String flagFinal2;
    String cId;
    private KeyboardHeightProvider keyboardHeightProvider;

    @Override
    protected void onResume() {
        super.onResume();

        keyboardHeightProvider.setKeyboardHeightObserver(this);
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


        try {
            countrycode2 = getIntent().getStringExtra("countrycode");

            if (countrycode2.equals("+1")) {
                binding.countrycode.setText("+1");
            } else {
                binding.countrycode.setText(countrycode2);
            }

        } catch (Exception ex) {
            binding.countrycode.setText("+1");
        }


        flagFinal2 = getIntent().getStringExtra("flagFinal");
        cId = getIntent().getStringExtra("c_id");

        if (flagFinal2 != null) {
            flagFinal.setText(flagFinal2);
        } else {
            flagFinal.setText("US");
        }


        if (cId != null) {
            binding.cId.setText(cId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWhatsYourNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        mContext = whatsYourNumber.this;
        keyboardHeightProvider = new KeyboardHeightProvider(this);

        binding.getRoot().post(new Runnable() {
            public void run() {
                keyboardHeightProvider.start();
            }
        });
        countrycode = findViewById(R.id.countrycode);
        flagFinal = findViewById(R.id.flagFinal);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            checkAndRequestPermissions();
            requestContactPermission();
        } else {
            checkAndRequestPermissions2();
            requestContactPermission();
        }


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.mobileEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (binding.countrycode.getText().toString().equals("+91")) {
                    binding.mobileEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                } else {
                    binding.mobileEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.checkbox.isChecked()) {


                    if (binding.countrycode.getText().toString().equals("+91") || binding.countrycode.getText().toString().equals("+1") && binding.countrycode.getText().toString().equals("+44") &&
                            binding.countrycode.getText().toString().equals("+61")) {
                        Webservice.check_phone_id_match(mContext, binding.countrycode.getText().toString() + binding.mobileEdit.getText().toString(), new Webservice.PhoneIdCheckListener() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                //  Toast.makeText(mContext, "Success: " + response.toString(), Toast.LENGTH_SHORT).show();

                                if (binding.mobileEdit.getText().toString().equals("")) {
                                    Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                                    binding.mobileEdit.setError("Invalid number ?", customErrorDrawable);
                                    binding.mobileEdit.requestFocus();
                                } else if (binding.mobileEdit.getText().toString().length() < 10 && binding.countrycode.getText().toString().equals("+91")) {
                                    Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                                    binding.mobileEdit.setError("Invalid number ?", customErrorDrawable);
                                    binding.mobileEdit.requestFocus();
                                } else {

                                    binding.progressBar.setVisibility(View.VISIBLE);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                                        checkAndRequestPermissions();
                                        requestContactPermission();
                                    } else {

                                        checkAndRequestPermissions2();
                                        requestContactPermission();
                                    }

                                    Webservice.send_otp(mContext, binding.countrycode.getText().toString() + binding.mobileEdit.getText().toString(), binding.countrycode.getText().toString(), "", whatsYourNumber.this, binding.progressBar, binding.cId.getText().toString());
                                }

                            }

                            @Override
                            public void onPartialMatch(JSONObject response) {

                                Constant.dialogueLayoutForAll(mContext, R.layout.delete_ac_dialogue);
                                TextView TextView = Constant.dialogLayoutColor.findViewById(R.id.TextView);
                                TextView.setText("This number is in use on another device.\nLogging in will end that session.");
                                AppCompatButton cancel = Constant.dialogLayoutColor.findViewById(R.id.cancel);
                                Constant.dialogLayoutColor.show();
                                Constant.dialogLayoutColor.setCanceledOnTouchOutside(false);
                                Constant.dialogLayoutColor.setCancelable(false);  // 👈 Add this line
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Constant.dialogLayoutColor.dismiss();
                                    }
                                });
                                AppCompatButton sure = Constant.dialogLayoutColor.findViewById(R.id.sure);
                                sure.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (binding.mobileEdit.getText().toString().equals("")) {
                                            Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                                            customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                                            binding.mobileEdit.setError("Invalid number ?", customErrorDrawable);
                                            binding.mobileEdit.requestFocus();
                                        } else if (binding.mobileEdit.getText().toString().length() < 10 && binding.countrycode.getText().toString().equals("+91")) {
                                            Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                                            customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                                            binding.mobileEdit.setError("Invalid number ?", customErrorDrawable);
                                            binding.mobileEdit.requestFocus();
                                        } else {

                                            binding.progressBar.setVisibility(View.VISIBLE);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                                                checkAndRequestPermissions();
                                                requestContactPermission();
                                            } else {

                                                checkAndRequestPermissions2();
                                                requestContactPermission();
                                            }

                                            Webservice.send_otp(mContext, binding.countrycode.getText().toString() + binding.mobileEdit.getText().toString(), binding.countrycode.getText().toString(), "", whatsYourNumber.this, binding.progressBar, binding.cId.getText().toString());
                                        }
                                    }
                                });
                                //   Toast.makeText(mContext, "⚠️ Number exists, but phone ID doesn't match.\nYou may have logged in on another device.", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                //   Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "onFailureXXXXX: " + errorMessage);

                                if (binding.mobileEdit.getText().toString().equals("")) {
                                    Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                                    binding.mobileEdit.setError("Invalid number ?", customErrorDrawable);
                                    binding.mobileEdit.requestFocus();
                                } else if (binding.mobileEdit.getText().toString().length() < 10 && binding.countrycode.getText().toString().equals("+91")) {
                                    Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                                    binding.mobileEdit.setError("Invalid number ?", customErrorDrawable);
                                    binding.mobileEdit.requestFocus();
                                } else {

                                    binding.progressBar.setVisibility(View.VISIBLE);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                                        checkAndRequestPermissions();
                                        requestContactPermission();
                                    } else {

                                        checkAndRequestPermissions2();
                                        requestContactPermission();
                                    }

                                    Webservice.send_otp(mContext, binding.countrycode.getText().toString() + binding.mobileEdit.getText().toString(), binding.countrycode.getText().toString(), "", whatsYourNumber.this, binding.progressBar, binding.cId.getText().toString());
                                }

//
                            }
                        });

                    } else {
                        Constant.showCustomToast("Accessible in your country soon.\nEc is for all in the world.", findViewById(R.id.includedToast), findViewById(R.id.toastText));
                    }


                } else {
                    // Toast.makeText(mContext, "P", Toast.LENGTH_SHORT).show();

                }


            }
        });


        binding.mobileEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.editLayout.setBackgroundResource(R.drawable.spinner_bg_new_2);
                return false;

            }
        });


        binding.spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), flagScreen.class);
                SwipeNavigationHelper.startActivityWithSwipe(whatsYourNumber.this, intent);


            }
        });


        binding.flagFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spinner.performClick();
            }
        });

        binding.policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url;
                int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    url = "https://enclosureapp.com/black_policy";
                } else {
                    url = "https://enclosureapp.com/privacy-policy";
                }


                // Open the website using an Intent
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        binding.term.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String url = "https://enclosureapp.com/terms_and_conditions";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        SwipeNavigationHelper.finishWithSwipe(whatsYourNumber.this);
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkAndRequestPermissions() {


        try {
            int contact = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (contact != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            }


            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(whatsYourNumber.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

                return false;
            }
            //  getContactList(binding.progressBar);
        } catch (Exception ex) {
            Log.d("permission%%", ex.getMessage());
        }
        return true;
    }

    private boolean checkAndRequestPermissions2() {


        try {
            int contact = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);


            List<String> listPermissionsNeeded = new ArrayList<>();

            if (contact != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            }


            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(whatsYourNumber.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

                return false;
            }

        } catch (Exception ex) {
            Log.d("permission%%", ex.getMessage());
        }
        return true;
    }

    public static String PhoneNumberWithoutCountryCode(String phoneNumberWithCountryCode) {//+91 7698989898
        Pattern compile = Pattern.compile("\\+(?:998|996|995|994|993|992|977|976|975|974|973|972|971|970|968|967|966|965|964|963|962|961|960|886|880|856|855|853|852|850|692|691|690|689|688|687|686|685|683|682|681|680|679|678|677|676|675|674|673|672|670|599|598|597|595|593|592|591|590|509|508|507|506|505|504|503|502|501|500|423|421|420|389|387|386|385|383|382|381|380|379|378|377|376|375|374|373|372|371|370|359|358|357|356|355|354|353|352|351|350|299|298|297|291|290|269|268|267|266|265|264|263|262|261|260|258|257|256|255|254|253|252|251|250|249|248|246|245|244|243|242|241|240|239|238|237|236|235|234|233|232|231|230|229|228|227|226|225|224|223|222|221|220|218|216|213|212|211|98|95|94|93|92|91|90|86|84|82|81|66|65|64|63|62|61|60|58|57|56|55|54|53|52|51|49|48|47|46|45|44\\D?1624|44\\D?1534|44\\D?1481|44|43|41|40|39|36|34|33|32|31|30|27|20|7|1\\D?939|1\\D?876|1\\D?869|1\\D?868|1\\D?849|1\\D?829|1\\D?809|1\\D?787|1\\D?784|1\\D?767|1\\D?758|1\\D?721|1\\D?684|1\\D?671|1\\D?670|1\\D?664|1\\D?649|1\\D?473|1\\D?441|1\\D?345|1\\D?340|1\\D?284|1\\D?268|1\\D?264|1\\D?246|1\\D?242|1)\\D?");
        globalNumber = phoneNumberWithCountryCode.replaceAll(compile.pattern(), "");
        Log.e("number::_>", globalNumber);//OutPut::7698989898
        return globalNumber;
    }


    public void getContactList(ProgressBar progressBar) {
        //  Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
        //  Toast.makeText(mContext, "getContactList", Toast.LENGTH_SHORT).show();

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
                        Constant.getSfFuncion(mContext);
                        String uidKey = Constant.getSF.getString(Constant.UID_KEY, "");
                        String phoneKy = Constant.getSF.getString(Constant.PHONE_NUMBERKEY, "");
                        JSONObject obj2 = new JSONObject();

                        // trim issue
                        obj2.put("uid", uidKey);
                        obj2.put("mobile_no", phoneKy);
                        obj2.put("contact_name", name);
                        obj2.put("contact_number", binding.countrycode.getText().toString() + globalNumber.replaceAll("[()\\s-]+", "").trim());
                        arr.put(obj2);

                        // Increment the contact counter
                        contactCounter++;
                    }
                }

                Constant.getSfFuncion(mContext);
                String uidKey = Constant.getSF.getString(Constant.UID_KEY, "");
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

    public void requestContactPermission() {
        if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(whatsYourNumber.this, Manifest.permission.READ_CONTACTS)) {

            } else {
                ActivityCompat.requestPermissions(whatsYourNumber.this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
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

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {


        if (height > 0) {
            Constant.setSfFunction(mContext);
            Constant.setSF.putString("keyboardHeightKey", String.valueOf(height));
            Constant.setSF.apply();

            // Toast.makeText(mContext, "saved "+String.valueOf(height), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        keyboardHeightProvider.setKeyboardHeightObserver(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyboardHeightProvider.close();
    }
}