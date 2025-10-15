package com.Appzia.enclosure.Screens;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Appzia.enclosure.Adapter.allContactListAdapter;
import com.Appzia.enclosure.Model.allContactListModel;
import com.Appzia.enclosure.Model.contactUploadModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.PaginationCallback;
import com.Appzia.enclosure.Utils.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.Appzia.enclosure.Adapter.get_user_active_contact_list_Adapter;
import com.Appzia.enclosure.Model.contactGetModel;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.databinding.ActivityInviteScreenBinding;

public class inviteScreen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityListener {

    ActivityInviteScreenBinding binding;
    private long page_no = 1;
    private Set<String> loadedIds = new HashSet<>();

    public static ArrayList<contactGetModel> contactGetList = new ArrayList<>();
    String TAG = "Enclosure";
    String BASE_URL = Webservice.BASE_URL;
    String GET_USERS_ALL_CONTACT = BASE_URL + "get_users_all_contact";
    public static LinearLayout linear;
    public static boolean isLoading = false;
    public static ProgressDialog dialog;

    public static ArrayList<allContactListModel> allContactListModelList = new ArrayList<>();

    allContactListAdapter allContactListAdapter;
    Context mContext;
    private GestureDetector gestureDetector;
    public static String globalNumber;
    ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list = new ArrayList<>();
    private boolean isScrollingUp = false;
    ArrayList<contactUploadModel> contactList = new ArrayList<>();
    Drawable drawable;

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String[] PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

    String country_codeKey;

    int count = 0;


    public static String dataNew;

    // contactAdapter adapter;
    get_user_active_contact_list_Adapter adapter2;
    public static Window window;
    public static String fontSizePref;
    String uid;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    ColorStateList tintList;
    String themColor;
    private ConnectivityReceiver connectivityReceiver;


    long type = 1;

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
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected) {

        } else {
            binding.networkLoader.setVisibility(View.VISIBLE);
            try {
                // TODO get_contact_model_List
                allContactListModelList.clear();
                ArrayList<allContactListModel> get_contact_model_List = new ArrayList<>();
                Log.d("Network", "dissconnetced: " + "inviteScreen");
                get_contact_model_List.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                allContactListModelList = dbHelper.get_users_all_contactTableInviteSpecial();

                if (allContactListModelList.size() > 0) {
                    binding.progressBarMain.setVisibility(View.GONE);
                } else {
                    binding.progressBarMain.setVisibility(View.VISIBLE);
                }

                setAdapter(allContactListModelList);


            } catch (Exception ignored) {
                Log.d(TAG, "onConnectivityChacwcvewcewcnged: " + ignored.getMessage());
            }


        }

    }


    @Override
    public void onStart() {
        super.onStart();

        //TODO : for only network loader Themes

        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FF6D00"));
                } else if (themColor.equals("#00A3E9")) {

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                } else if (themColor.equals("#7adf2a")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00C853"));

                } else if (themColor.equals("#ec0001")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#ec7500"));

                } else if (themColor.equals("#16f3ff")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00F365"));

                } else if (themColor.equals("#FF8A00")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FFAB00"));

                } else if (themColor.equals("#7F7F7F")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#314E6D"));

                } else if (themColor.equals("#D9B845")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#b0d945"));
                } else if (themColor.equals("#346667")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#729412"));

                } else if (themColor.equals("#9846D9")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#d946d1"));

                } else if (themColor.equals("#A81010")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#D85D01"));

                } else {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }
        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = binding.getRoot().getContext();

        linear = findViewById(R.id.linear);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        Constant.getSfFuncion(mContext);
        dialog = new ProgressDialog(inviteScreen.this);
        country_codeKey = Constant.getSF.getString(Constant.COUNTRY_CODE, "");


        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, mContext.RECEIVER_EXPORTED);
        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");


//        //TODO FIRST CALL OFFLINE DATA HERE
        try {
            // TODO get_contact_model_List
            allContactListModelList.clear();
            ArrayList<allContactListModel> get_contact_model_List = new ArrayList<>();
            Log.d("Network", "dissconnetced: " + "inviteScreen");
            get_contact_model_List.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            allContactListModelList = dbHelper.get_users_all_contactTableInviteSpecial();

            if (get_contact_model_List.size() > 0) {
                binding.progressBarMain.setVisibility(View.GONE);
            } else {
                binding.progressBarMain.setVisibility(View.VISIBLE);
            }

            setAdapter(allContactListModelList);


        } catch (Exception ignored) {
        }


        Webservice.getDataFromAPIInitial(mContext, uid, inviteScreen.this, binding.progressBarMain, binding.clearcalllog, binding.valuable, binding.textCard, page_no);


        //todo ------------------------------online--------------------------------------


        binding.networkLoader.setVisibility(View.GONE);


        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");
//        getDataFromAPIInitial(mContext, uid, inviteScreen.this, binding.progressBarMain, binding.clearcalllog);


        //todo ------------------------------online--------------------------------------


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);  // Set the status bar to transparent
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));


            try {

                Constant.getSfFuncion(mContext);
                String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                try {
                    if (themColor.equals("#ff0080")) {

                        binding.view.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));

                    } else if (themColor.equals("#00A3E9")) {

                        binding.view.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);

                    } else if (themColor.equals("#7adf2a")) {


                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);
                    } else if (themColor.equals("#ec0001")) {


                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);
                    } else if (themColor.equals("#16f3ff")) {

                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);

                    } else if (themColor.equals("#FF8A00")) {

                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#7F7F7F")) {

                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#D9B845")) {

                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#346667")) {

                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#9846D9")) {

                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#A81010")) {

                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);

                    } else {

                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);

                    }
                } catch (Exception ignored) {

                }


            } catch (Exception ignored) {
            }


        } catch (Exception ignored) {
        }


        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.dialogueLayoutInviteScreen(mContext, R.layout.refresh_ly);
                Dialog dialogueLayoutCLearLogUp = Constant.dialogLayoutColor;
                LinearLayout clearcalllog = dialogueLayoutCLearLogUp.findViewById(R.id.clearcalllog);


                dialogueLayoutCLearLogUp.show();
                clearcalllog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogueLayoutCLearLogUp.dismiss();


                        dialog.setTitle("Please wait");
                        dialog.setMessage("Your contacts are synchronizing...");
                        dialog.show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            checkAndRequestPermissions(dialog);
                        } else {
                            checkAndRequestPermissions2(dialog);
                        }
                        requestContactPermission(dialog);
                    }
                });


            }
        });


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }


        });

        binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.searchLytNew.getVisibility() == View.VISIBLE) {
                    binding.searchLytNew.setVisibility(View.GONE);

                    View view = findViewById(android.R.id.content);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                } else if (binding.searchLytNew.getVisibility() == View.GONE) {
                    binding.search.requestFocus();
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_four);
                    binding.searchLytNew.setAnimation(animation);
                    binding.searchLytNew.setVisibility(View.VISIBLE);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            }
        });


        binding.search.addTextChangedListener(new TextWatcher() {
            private Handler handler = new Handler();
            private Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                runnable = new Runnable() {
                    @Override
                    public void run() {

                        if (String.valueOf(s).isEmpty()) {
                            Webservice.getDataFromAPIInitial(mContext, uid, inviteScreen.this, binding.progressBarMain, binding.clearcalllog, binding.valuable, binding.textCard, page_no);
                        } else {
                            Webservice.search_from_all_contact(mContext, uid, String.valueOf(s), binding.progressBarMain, inviteScreen.this, binding.clearcalllog, binding.valuable, binding.textCard);
                        }

                    }
                };
                handler.postDelayed(runnable, 1000); // 500ms delay
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        binding.activeChatListRecyclverview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();
                    int visibleThreshold = 3;

                    if (!isLoading && lastVisibleItemPosition >= totalItemCount - visibleThreshold && dy > 0) {
                        isLoading = true;
                        binding.progressbar.setVisibility(View.VISIBLE);

                        Webservice.getDataFromAPIInitialLoadMore(
                                inviteScreen.this,
                                uid,
                                inviteScreen.this,
                                binding.progressBarMain,
                                binding.clearcalllog,
                                binding.valuable,
                                binding.textCard,
                                page_no,
                                binding.progressbar,
                                new PaginationCallback() {
                                    @Override
                                    public void onPageLoadSuccess() {
                                        Log.d("InviteScreen", "onPageLoadSuccess: incrementing page from " + page_no);
                                        page_no++;
                                        isLoading = false;
                                        Log.d("InviteScreen", "onPageLoadSuccess: new page=" + page_no);
                                    }

                                    @Override
                                    public void onPageLoadFailure() {
                                        isLoading = false;
                                        binding.progressbar.setVisibility(View.GONE);
                                        Log.d("InviteScreen", "onPageLoadFailure: keeping page=" + page_no);
                                    }
                                }
                        );
                    }
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        SmoothNavigationHelper.finishWithSlideToRight(inviteScreen.this);
        //  TransitionHelper.performTransition(((Activity)mContext));
    }

    public void setAdapter(ArrayList<allContactListModel> newItems) {
        allContactListModelList.clear();
        loadedIds.clear();

        for (allContactListModel model : newItems) {
            String uniqueId = model.getC_flag().equals("1") ? model.getUid() : model.getContact_number();
            if (!loadedIds.contains(uniqueId)) {
                loadedIds.add(uniqueId);
                allContactListModelList.add(model);
            }
        }

        if (allContactListAdapter == null) {
            allContactListAdapter = new allContactListAdapter(mContext, allContactListModelList, binding.valuable, binding.textCard);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            binding.activeChatListRecyclverview.setLayoutManager(layoutManager);
            binding.activeChatListRecyclverview.setAdapter(allContactListAdapter);
        } else {
            // Just update the existing adapter
            allContactListAdapter.updateList(new ArrayList<>(allContactListModelList));
        }
    }


    public void setAdapterNotifiyInserted(ArrayList<allContactListModel> newItems) {
        if (allContactListAdapter == null) {
            allContactListModelList.clear();
            loadedIds.clear();

            for (allContactListModel model : newItems) {
                String uniqueId = model.getC_flag().equals("1") ? model.getUid() : model.getContact_number();
                if (!loadedIds.contains(uniqueId)) {
                    loadedIds.add(uniqueId);
                    allContactListModelList.add(model);
                }
            }

            allContactListAdapter = new allContactListAdapter(mContext, allContactListModelList, binding.valuable, binding.textCard);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            binding.activeChatListRecyclverview.setLayoutManager(layoutManager);
            binding.activeChatListRecyclverview.setAdapter(allContactListAdapter);
        } else {
            int previousSize = allContactListModelList.size();

            for (allContactListModel model : newItems) {
                String uniqueId = model.getC_flag().equals("1") ? model.getUid() : model.getContact_number();
                if (!loadedIds.contains(uniqueId)) {
                    loadedIds.add(uniqueId);
                    allContactListModelList.add(model);
                }
            }

            int newSize = allContactListModelList.size() - previousSize;

            if (newSize > 0) {
                allContactListAdapter.updateList(new ArrayList<>(allContactListModelList));
                // Alternatively, you can notify only the inserted range but updateList calls notifyDataSetChanged()
                // If you want to optimize, implement a more fine-grained update method.
            }
        }
    }


    public void requestContactPermission(ProgressDialog dialog) {
        if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(inviteScreen.this, Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
                builder.setTitle("Read Contacts permission");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage("Please enable access to contacts.");
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(inviteScreen.this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            getContactList(dialog);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContactList(dialog);
            } else {
                //  Toast.makeText(binding.getRoot().getContext(), "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
            }
        }


    }

    private void getContactList(ProgressDialog dialog) {
        contactList.clear();
        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                // Load SharedPreferences
                Constant.getSfFuncion(mContext);
                String uidKey = Constant.getSF.getString(Constant.UID_KEY, "");
                String phoneKey = Constant.getSF.getString(Constant.PHONE_NUMBERKEY, "");
                String fileNameUid = "contact_" + uidKey;
                File file = new File(getCacheDir(), fileNameUid + ".json");

                // Load old contacts
                HashMap<String, String> oldContactsMap = new HashMap<>();
                if (file.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        StringBuilder jsonBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            jsonBuilder.append(line);
                        }
                        JSONObject json = new JSONObject(jsonBuilder.toString());
                        JSONArray contactArray = json.getJSONArray("contact");
                        for (int i = 0; i < contactArray.length(); i++) {
                            JSONObject contact = contactArray.getJSONObject(i);
                            String name = contact.getString("contact_name").trim();
                            String number = contact.getString("contact_number").trim();
                            oldContactsMap.put(number, name);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                JSONArray newOrUpdatedContacts = new JSONArray();
                HashSet<String> seenNumbers = new HashSet<>();
                int contactCounter = 0;

                while (cursor.moveToNext() && contactCounter < 10000) {
                    String name = cursor.getString(nameIndex);
                    String rawNumber = cursor.getString(numberIndex);
                    if (name == null || rawNumber == null) continue;

                    // Normalize phone number
                    String normalizedNumber = PhoneNumberUtils.normalizeNumber(rawNumber).replaceAll("[^0-9]", "");
                    if (normalizedNumber.startsWith("0")) {
                        normalizedNumber = normalizedNumber.substring(1);
                    }
                    if (!normalizedNumber.startsWith("91")) {
                        normalizedNumber = "91" + normalizedNumber;
                    }
                    String formattedNumber = "+" + normalizedNumber;
                    String trimmedName = name.trim();

                    // Avoid duplicates
                    if (seenNumbers.contains(formattedNumber)) continue;
                    seenNumbers.add(formattedNumber);

                    // Add to local contact list
                    contactList.add(new contactUploadModel(trimmedName, formattedNumber));

                    // Check if contact is new or updated
                    boolean isNewContact = !oldContactsMap.containsKey(formattedNumber);
                    boolean isUpdatedContact = oldContactsMap.containsKey(formattedNumber)
                            && !oldContactsMap.get(formattedNumber).trim().equalsIgnoreCase(trimmedName);

                    if (isNewContact || isUpdatedContact) {
                        JSONObject obj = new JSONObject();
                        obj.put("uid", uidKey);
                        obj.put("mobile_no", phoneKey);
                        obj.put("contact_name", trimmedName);
                        obj.put("contact_number", formattedNumber);
                        newOrUpdatedContacts.put(obj);

                        Log.d("ContactChange", "New/Updated Contact: " + formattedNumber +
                                " (Old: " + oldContactsMap.get(formattedNumber) + ", New: " + trimmedName + ")");
                    }

                    contactCounter++;
                }

                if (newOrUpdatedContacts.length() > 0) {
                    String finalData = "{ \"contact\":" + newOrUpdatedContacts.toString() + "}";

                    // Save only modified contacts to JSON file
                    try (FileWriter fileWriter = new FileWriter(file)) {
                        fileWriter.write(finalData);
                        Log.d("ContactUpdate", "Saved modified contacts: " + newOrUpdatedContacts.length());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Upload only if changes detected
                    Webservice.upload_user_contact_listRetrofitTwo(
                            mContext,
                            uidKey,
                            file,
                            "",
                            "+91", // replace with dynamic code if needed
                            "token",
                            "deviceId",
                            inviteScreen.this,
                            dialog,
                            binding.clearcalllog,
                            "invite",
                            binding.valuable,
                            binding.textCard,
                            binding.progressBarMain
                    );
                } else {
                    Log.d("ContactUpdate", "No new or modified contacts found. Skipping upload.");
                    dialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            } finally {
                cursor.close();
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkAndRequestPermissions(ProgressDialog dialog) {


        try {
            int contact = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
            int storage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);


            List<String> listPermissionsNeeded = new ArrayList<>();
            if (contact != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            }

            if (storage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

            }


            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(inviteScreen.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

                return false;
            }
        } catch (Exception ex) {
            Log.d("permission%%", ex.getMessage());
        }
        return true;
    }

    private boolean checkAndRequestPermissions2(ProgressDialog dialog) {


        try {
            int contact = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
            int storage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);


            List<String> listPermissionsNeeded = new ArrayList<>();

            if (contact != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            }

            if (storage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(inviteScreen.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(connectivityReceiver);
    }

//    public void filteredListActive(String newText) {
//        ArrayList<allContactListModel> filteredList = new ArrayList<>();
//
//        for (allContactListModel list : allContactListModelList) {
//            if (list.getFull_name().toLowerCase().contains(newText.toLowerCase()) || list.getContact_name().toLowerCase().contains(newText.toLowerCase())) {
//
//                filteredList.add(list);
//            }
//        }
//
//        if (filteredList.isEmpty()) {
//            // Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
//        } else {
//            allContactListAdapter.searchFilteredData(filteredList);
//        }
//    }
}