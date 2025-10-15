package com.Appzia.enclosure.SubScreens;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.call_history_adapter_for_voice;
import com.Appzia.enclosure.Adapter.calllogParentAdapterVoice;
import com.Appzia.enclosure.Adapter.get_voice_calling_adapter;
import com.Appzia.enclosure.Model.call_log_history_model;
import com.Appzia.enclosure.Model.callingUserInfoChildModel;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.FcmNotificationsSender;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Utils.WebserviceRetrofits.APIClient;
import com.Appzia.enclosure.activities.MainActivityVoiceCall;
import com.Appzia.enclosure.databinding.FragmentCallBinding;
import com.Appzia.enclosure.models.get_call_log_1Child;
import com.Appzia.enclosure.models.user_infoModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CallUtil implements ConnectivityReceiver.ConnectivityListener {

    private final Context mContext;
    private final View rootView;
    private FragmentCallBinding binding;
    private boolean isScrollingUp = false;
    private boolean hasSlidBelowThreshold = false;
    private LinearLayout backarroe;
    private RecyclerView recyclerviewLast, recyclerviewAZ;
    private String themColor;

    private ColorStateList tintList;
    public static LinearLayout layoutName, searchData, label;
    public static RelativeLayout contactLayout;
    public static BottomSheetBehavior<View> bottomSheetBehavior;
    public static ImageView img;
    public static TextView name1, mobile, dateLive;
    public static LinearLayoutManager layoutManager;
    public static get_voice_calling_adapter adapter;
    public static calllogParentAdapterVoice calllogParentAdapterVoice;
    public static LinearLayout backlyt;
    public static boolean hasSlidDown = false;
    private boolean hasSlidUp = false;
    private call_history_adapter_for_voice call_history_adapter_for_voice;
    private String fontSizePref;
    private String uid;
    private ConnectivityReceiver connectivityReceiver;
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    public static ArrayList<get_call_log_1Child> get_call_log_1ChildList = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list1 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list2 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list3 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list4 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list5 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list6 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list7 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list8 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list9 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list10 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list11 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list12 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list13 = new ArrayList<>();
    private ArrayList<callingUserInfoChildModel> list14 = new ArrayList<>();
    private ArrayList<get_contact_model> get_calling_contact_list = new ArrayList<>();
    private ArrayList<call_log_history_model> call_log_history_list = new ArrayList<>();

    public CallUtil(Context context, View rootView) {
        this.mContext = context;
        this.rootView = rootView;
        this.binding = FragmentCallBinding.bind(rootView);
        initializeViews();
        setup();
    }

    public static void setupCallCode(View rootView) {
        new CallUtil(rootView.getContext(), rootView);
    }

    private void initializeViews() {
        backarroe = rootView.findViewById(R.id.backarroe);
        recyclerviewLast = rootView.findViewById(R.id.recyclerviewLast);
        recyclerviewAZ = rootView.findViewById(R.id.recyclerviewAZ);
        layoutName = rootView.findViewById(R.id.layoutName);
        searchData = rootView.findViewById(R.id.searchData);
        label = rootView.findViewById(R.id.label);
        contactLayout = rootView.findViewById(R.id.contactLayout);
        img = rootView.findViewById(R.id.img);
        name1 = rootView.findViewById(R.id.name1);
        mobile = rootView.findViewById(R.id.mobile);
        backlyt = rootView.findViewById(R.id.backlyt);
        dateLive = rootView.findViewById(R.id.dateLive);

        // Log missing views for debugging
        if (recyclerviewLast == null)
            Log.w("CallUtil", "recyclerviewLast is null. Check if R.id.recyclerviewLast exists.");
        if (recyclerviewAZ == null)
            Log.w("CallUtil", "recyclerviewAZ is null. Check if R.id.recyclerviewAZ exists.");
        if (layoutName == null)
            Log.w("CallUtil", "layoutName is null. Check if R.id.layoutName exists.");
        if (searchData == null)
            Log.w("CallUtil", "searchData is null. Check if R.id.searchData exists.");
        if (label == null) Log.w("CallUtil", "label is null. Check if R.id.label exists.");
        if (contactLayout == null)
            Log.w("CallUtil", "contactLayout is null. Check if R.id.contactLayout exists.");
        if (img == null) Log.w("CallUtil", "img is null. Check if R.id.img exists.");
        if (name1 == null) Log.w("CallUtil", "name1 is null. Check if R.id.name1 exists.");
        if (mobile == null) Log.w("CallUtil", "mobile is null. Check if R.id.mobile exists.");
        if (dateLive == null) Log.w("CallUtil", "dateLive is null. Check if R.id.dateLive exists.");
        if (backarroe == null)
            Log.w("CallUtil", "backarroe is null. Check if R.id.backarroe exists.");

        // Debug view hierarchy
        if (rootView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) rootView;
            Log.d("CallUtil", "Root view children count: " + viewGroup.getChildCount());
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                String resourceName = child.getId() != View.NO_ID ? mContext.getResources().getResourceName(child.getId()) : "No ID";
                Log.d("CallUtil", "Child " + i + ": ID=" + resourceName + ", Class=" + child.getClass().getSimpleName());
            }
        } else {
            Log.w("CallUtil", "rootView is not a ViewGroup, cannot inspect children.");
        }
    }

    private void setup() {
        // Initialize data lists
        get_call_log_1ChildList.clear();
        get_calling_contact_list.clear();
        call_log_history_list.clear();

        // Set up connectivity receiver
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, Context.RECEIVER_EXPORTED);

        // Set back key
        Constant.setSfFunction(mContext);
        Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
        Constant.setSF.apply();

        // Load font size preference and apply
        Constant.getSfFuncion(mContext);
        fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        // Load and apply theme
        themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        tintList = ColorStateList.valueOf(Color.parseColor(themColor));
        applyTheme();

        // Set up bottom sheet
        setupBottomSheet();

        // Set up RecyclerViews
        setupRecyclerViews();

        // Set up search
        setupSearch();

        // Set up click listeners
        setupClickListeners();


        // Load initial data
        loadInitialData();
    }


    private void applyTheme() {
        try {
            if (binding.call != null) binding.call.setBackgroundTintList(tintList);
            if (binding.menuPoint != null)
                binding.menuPoint.setColorFilter(Color.parseColor(themColor));
            if (binding.view != null) binding.view.setBackgroundTintList(tintList);
        } catch (Exception ignored) {
            Log.w("CallUtil", "Error applying theme: " + ignored.getMessage());
        }
    }

    private void setupBottomSheet() {
        View bottomSheet = rootView.findViewById(R.id.localBottomSheet);
        if (bottomSheet != null) {
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetBehavior.setSkipCollapsed(true);
            bottomSheetBehavior.setHideable(false);

            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED ||
                            newState == BottomSheetBehavior.STATE_HALF_EXPANDED ||
                            newState == BottomSheetBehavior.STATE_HIDDEN) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    Log.d("BottomSheet", "Sliding: " + slideOffset);
                    if (slideOffset >= 1.0f) {
                        hasSlidBelowThreshold = false;
                    } else if (slideOffset < 0.996f && !hasSlidBelowThreshold) {
                        hasSlidBelowThreshold = true;
                        Log.d("BottomSheet", "Less than fully expanded");
                        if (get_call_log_1ChildList.size() > 7) {
                            if (MainActivityOld.linearMain2 != null && MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                                MainActivityOld.slideDownContainer();
                                backlyt.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });
        } else {
            Log.w("CallUtil", "localBottomSheet is null. Check if R.id.localBottomSheet exists.");
        }
    }

    private void setupRecyclerViews() {
        if (recyclerviewLast != null) {
            LinearLayoutManager layoutManagerLast = new LinearLayoutManager(mContext);
            recyclerviewLast.setLayoutManager(layoutManagerLast);

            recyclerviewLast.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private boolean userScrolled = false;

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    userScrolled = (newState == RecyclerView.SCROLL_STATE_DRAGGING);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!userScrolled) return; // Ignore automatic layout scrolls

                    int firstVisiblePosition = layoutManagerLast.findFirstVisibleItemPosition();
                    int lastVisiblePosition = layoutManagerLast.findLastVisibleItemPosition();
                    int totalItemCount = layoutManagerLast.getItemCount();
                    View firstView = layoutManagerLast.findViewByPosition(firstVisiblePosition);

                    // Top reached
                    if (firstVisiblePosition == 0 && firstView != null && firstView.getTop() == 0) {
                        if (MainActivityOld.linearMain2 != null && MainActivityOld.linearMain2.getVisibility() == View.VISIBLE && !hasSlidDown) {
                            if (get_call_log_1ChildList.size() > 7 || call_log_history_list.size() > 7) {
                                MainActivityOld.slideDownContainer();
                                backlyt.setVisibility(View.GONE);
                            }
                            hasSlidDown = true;
                            hasSlidUp = false;
                        }
                    } else {
                        hasSlidDown = false;
                    }

                    // Bottom reached
                    if (lastVisiblePosition == totalItemCount - 1 && totalItemCount > 0) {
                        View lastView = layoutManagerLast.findViewByPosition(lastVisiblePosition);
                        if (lastView != null && lastView.getBottom() <= recyclerView.getHeight()) {
                            if (MainActivityOld.linearMain != null && MainActivityOld.linearMain.getVisibility() == View.VISIBLE && !hasSlidUp) {
                                if (get_call_log_1ChildList.size() > 7 || call_log_history_list.size() > 7) {
                                    MainActivityOld.slideUpContainer();
                                    backlyt.setVisibility(View.VISIBLE);
                                }
                                hasSlidUp = true;
                                hasSlidDown = false;
                            }
                        }
                    } else {
                        hasSlidUp = false;
                    }
                }
            });
        }

        if (recyclerviewAZ != null) {
            LinearLayoutManager layoutManagerAZ = new LinearLayoutManager(mContext);
            recyclerviewAZ.setLayoutManager(layoutManagerAZ);

            recyclerviewAZ.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private boolean userScrolled = false;

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    userScrolled = (newState == RecyclerView.SCROLL_STATE_DRAGGING);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!userScrolled) return; // Only react to manual scrolls

                    int firstVisiblePosition = layoutManagerAZ.findFirstVisibleItemPosition();
                    int lastVisiblePosition = layoutManagerAZ.findLastVisibleItemPosition();
                    int totalItemCount = layoutManagerAZ.getItemCount();
                    View firstView = layoutManagerAZ.findViewByPosition(firstVisiblePosition);

                    // Top reached
                    if (firstVisiblePosition == 0 && firstView != null && firstView.getTop() == 0) {
                        if (MainActivityOld.linearMain2 != null && MainActivityOld.linearMain2.getVisibility() == View.VISIBLE && !hasSlidDown) {
                            if (get_calling_contact_list.size() > 7) {
                                MainActivityOld.slideDownContainer();
                                backlyt.setVisibility(View.GONE);
                            }
                            hasSlidDown = true;
                            hasSlidUp = false;
                        }
                    } else {
                        hasSlidDown = false;
                    }

                    // Bottom reached
                    if (lastVisiblePosition == totalItemCount - 1 && totalItemCount > 0) {
                        View lastView = layoutManagerAZ.findViewByPosition(lastVisiblePosition);
                        if (lastView != null && lastView.getBottom() <= recyclerView.getHeight()) {
                            if (MainActivityOld.linearMain != null && MainActivityOld.linearMain.getVisibility() == View.VISIBLE && !hasSlidUp) {
                                if (get_calling_contact_list.size() > 7) {
                                    MainActivityOld.slideUpContainer();
                                    backlyt.setVisibility(View.VISIBLE);
                                }
                                hasSlidUp = true;
                                hasSlidDown = false;
                            }
                        }
                    } else {
                        hasSlidUp = false;
                    }
                }
            });
        }
    }


    private void setupSearch() {
        if (binding.search != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, Constant.searchVideoCall); // Adjust if needed for voice call
            binding.search.setAdapter(adapter);

            binding.search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isDigitsOnly(s.toString().trim()) && s.toString().trim().length() == 10) {
                        filteredListNumber(Constant.getSF.getString(Constant.COUNTRY_CODE, "") + s.toString());
                    } else {
                        filteredList(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            binding.search.setOnTouchListener((v, event) -> {
                if (MainActivityOld.linearMain != null) MainActivityOld.slideUpContainer();
                backlyt.setVisibility(View.VISIBLE);
                binding.search.requestFocus();
                return false;
            });
        } else {
            Log.w("CallUtil", "search is null. Check if R.id.search exists.");
        }
    }

    private void setupClickListeners() {
        if (binding.backarrow != null) {
            binding.backarrow.setOnClickListener(v -> {
                if (MainActivityOld.linearMain2 != null && MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideDownContainer();
                    backlyt.setVisibility(View.GONE);

                }
            });
        }

        if (binding.call != null) {
            binding.call.setOnClickListener(v -> {
                if (checkPermissions()) {
                    initiateCall();
                }
            });
        }

        if (binding.backarroe != null) {
            binding.backarroe.setOnClickListener(v -> {
                if (binding.label != null) binding.label.setVisibility(View.VISIBLE);
                if (binding.layoutName != null) binding.layoutName.setVisibility(View.GONE);
                if (binding.searchData != null) binding.searchData.setVisibility(View.VISIBLE);
                if (binding.dateLive != null) binding.dateLive.setVisibility(View.GONE);
                call_log_history_list.clear();
                if (call_history_adapter_for_voice != null)
                    call_history_adapter_for_voice.notifyDataSetChanged();
                if (binding.contact != null)
                    binding.contact.setBackgroundResource(R.drawable.radius_6dp_transp);
                if (binding.log != null)
                    binding.log.setBackgroundResource(R.drawable.radius_black_6dp);
                if (binding.contact != null)
                    binding.contact.setTextColor(Color.parseColor("#000000"));
                if (binding.log != null) binding.log.setTextColor(Color.parseColor("#ffffff"));
                if (binding.recyclerviewLast != null)
                    binding.recyclerviewLast.setVisibility(View.VISIBLE);
                if (binding.recyclerviewAZ != null) binding.recyclerviewAZ.setVisibility(View.GONE);
                if (binding.log != null) binding.log.performClick();
            });
        }

        if (binding.log != null) {
            binding.log.setOnClickListener(v -> handleLogTabClick());
        }

        if (binding.contact != null) {
            binding.contact.setOnClickListener(v -> handleContactTabClick());
        }

        if (binding.menu != null) {
            binding.menu.setOnClickListener(v -> showClearLogDialog());
        }

        if (binding.searchIcon != null) {
            binding.searchIcon.setOnClickListener(v -> toggleSearchBar());
        }
    }


    private void loadInitialData() {
        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");
        if (binding.recyclerviewLast != null) binding.recyclerviewLast.setVisibility(View.VISIBLE);
        if (binding.recyclerviewAZ != null) binding.recyclerviewAZ.setVisibility(View.GONE);

        if (isInternetConnected()) {
            Webservice.get_voice_call_log_for_voice_call(mContext, uid, CallUtil.this, binding.recyclerviewLast, binding.progressBar, binding.noData);
        } else {
            loadOfflineCallLog();
        }
    }

    private void loadOfflineCallLog() {
        try {
            Log.d("Network", "disconnected: CallUtil");
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            get_call_log_1ChildList.clear();
            // get_call_log_1ChildList = dbHelper.get_voice_call_logTable();
            if (get_call_log_1ChildList.size() > 0) {
                if (binding.recyclerviewLast != null)
                    binding.recyclerviewLast.setVisibility(View.VISIBLE);
                setAdapterLog(get_call_log_1ChildList);
                if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
            } else {
                if (binding.progressBar != null) binding.progressBar.setVisibility(View.VISIBLE);
                if (binding.recyclerviewLast != null)
                    binding.recyclerviewLast.setVisibility(View.GONE);
            }
        } catch (Exception ignored) {
            Log.w("CallUtil", "Error loading offline call log: " + ignored.getMessage());
        }
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (!isConnected) {
            if (binding.recyclerviewLast != null)
                binding.recyclerviewLast.setVisibility(View.VISIBLE);
            if (binding.recyclerviewAZ != null) binding.recyclerviewAZ.setVisibility(View.GONE);
            loadOfflineCallLog();
        } else {
            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
            Webservice.get_voice_call_log_for_voice_call(mContext, uid, CallUtil.this, binding.recyclerviewLast, binding.progressBar, binding.noData);
        }
    }

    private boolean checkPermissions() {
        for (String permission : REQUESTED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.w("CallUtil", "Permission " + permission + " not granted. Activity required for permission request.");
                Toast.makeText(mContext, "Permissions required for call", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void initiateCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Constant.Vibrator(mContext);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = new Date();
        String currentDate = dateFormat.format(date2);
        Calendar calendar = Calendar.getInstance();
        String amPm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;

        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");
        String profilePic = Constant.getSF.getString(Constant.profilePic, "");
        String full_name = Constant.getSF.getString(Constant.full_name, "");
        String roomId = Constant.generateRoomIdFromTimestamp();

        Intent intent = new Intent(mContext, MainActivityVoiceCall.class);
        intent.putExtra("fTokenKey", binding.ftoken != null ? binding.ftoken.getText().toString() : "");
        intent.putExtra("currentTime", currentTime);
        intent.putExtra("receiverId", binding.callerId != null ? binding.callerId.getText().toString().trim() : "");
        intent.putExtra("phoneKey", binding.phone != null ? binding.phone.getText().toString() : "");
        intent.putExtra("senderId", uid);
        intent.putExtra("deviceType", binding.deviceType != null ? binding.deviceType.getText().toString() : "");
        intent.putExtra("firstKey", "firstKey");
        intent.putExtra("username", uid);
        intent.putExtra("createdBy", uid);
        intent.putExtra("roomId", roomId);
        intent.putExtra("incoming", uid);
        intent.putExtra("photo", profilePic);
        intent.putExtra("name", full_name);
        intent.putExtra("nameReceiver", binding.mainname != null ? binding.mainname.getText().toString() : "");
        intent.putExtra("photoReceiver", binding.photo != null ? binding.photo.getText().toString() : "");
        intent.putExtra("roomFlagKey", "roomFlagKey");
        intent.putExtra("iAmSenderNew", true);
        SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);

        String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");
        if (!sleepKey.equals(Constant.sleepKey) && binding.ftoken != null && binding.callerId != null && binding.phone != null && binding.deviceType != null) {
            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                    binding.ftoken.getText().toString(), "Enclosure", Constant.voicecall, mContext, null,
                    Constant.getSF.getString(Constant.full_name, ""), "meetingId", binding.phone.getText().toString(),
                    Constant.getSF.getString(Constant.profilePic, ""), "", uid, binding.callerId.getText().toString(),
                    binding.deviceType.getText().toString(), uid, uid, uid, roomId);
            notificationsSender.SendNotifications();
        }
    }

    private void handleLogTabClick() {
        if (binding.progressBar != null) binding.progressBar.setVisibility(View.VISIBLE);
        Webservice.cancelAllRequests();
        APIClient.cancelAllRequests();
        if (binding.recyclerviewLast != null) binding.recyclerviewLast.setVisibility(View.VISIBLE);
        if (binding.recyclerviewAZ != null) binding.recyclerviewAZ.setVisibility(View.GONE);
        if (binding.searchData != null && binding.searchData.getVisibility() == View.VISIBLE) {
            Animation animation3 = AnimationUtils.loadAnimation(mContext, R.anim.fadeoutthreethnd);
            binding.searchData.setAnimation(animation3);
            binding.searchData.setVisibility(View.GONE);
        }
        if (binding.menu != null) binding.menu.setVisibility(View.VISIBLE);
        if (binding.contact != null)
            binding.contact.setBackgroundResource(R.drawable.radius_6dp_transp);
        if (binding.log != null) binding.log.setBackgroundResource(R.drawable.radius_black_6dp);
        if (binding.contact != null) binding.contact.setTextColor(Color.parseColor("#000000"));
        if (binding.log != null) binding.log.setTextColor(Color.parseColor("#ffffff"));

        if (isInternetConnected()) {
            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
            Webservice.get_voice_call_log_for_voice_call(mContext, uid, CallUtil.this, binding.recyclerviewLast, binding.progressBar, binding.noData);
        } else {
            loadOfflineCallLog();
        }
    }

    private void handleContactTabClick() {
        if (binding.clearcalllog != null && binding.clearcalllog.getVisibility() == View.VISIBLE) {
            binding.clearcalllog.setVisibility(View.GONE);
        }
        if (binding.noData != null) binding.noData.setVisibility(View.GONE);
        if (binding.progressBar != null) binding.progressBar.setVisibility(View.VISIBLE);
        Webservice.cancelAllRequests();
        APIClient.cancelAllRequests();
        if (binding.recyclerviewLast != null) binding.recyclerviewLast.setVisibility(View.GONE);
        if (binding.recyclerviewAZ != null) binding.recyclerviewAZ.setVisibility(View.VISIBLE);
        if (binding.searchData != null && binding.searchData.getVisibility() == View.GONE) {
            Animation animation3 = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
            binding.searchData.setAnimation(animation3);
            binding.searchData.setVisibility(View.VISIBLE);
            if (binding.search != null) binding.search.requestFocus();
        }
        if (binding.bottomcaller != null) binding.bottomcaller.setVisibility(View.GONE);
        if (binding.contact != null)
            binding.contact.setBackgroundResource(R.drawable.radius_black_6dp);
        if (binding.log != null) binding.log.setBackgroundResource(R.drawable.radius_6dp_transp);
        if (binding.log != null) binding.log.setTextColor(Color.parseColor("#000000"));
        if (binding.contact != null) binding.contact.setTextColor(Color.parseColor("#ffffff"));
        if (binding.menu != null) binding.menu.setVisibility(View.INVISIBLE);

        if (isInternetConnected()) {
            try {
                get_calling_contact_list.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_calling_contact_list = dbHelper.get_users_all_contactTable();
                if (get_calling_contact_list.size() > 0) {
                    if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
                    setAdapter(get_calling_contact_list);
                    if (binding.noData != null) binding.noData.setVisibility(View.GONE);
                } else {
                    if (binding.progressBar != null)
                        binding.progressBar.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Webservice.get_calling_contact_list2(mContext, uid, CallUtil.this, binding.recyclerviewAZ, binding.progressBar, binding.noData);
        } else {
            try {
                get_calling_contact_list.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_calling_contact_list = dbHelper.get_users_all_contactTable();
                setAdapter(get_calling_contact_list);
            } catch (Exception ignored) {
                Log.w("CallUtil", "Error loading offline contacts: " + ignored.getMessage());
            }
        }
    }

    private void showClearLogDialog() {
        if (MainActivityOld.sliderealtime != null && MainActivityOld.sliderealtime.getText().toString().equals("up")) {
            Constant.dialogueLayoutCLearLogUp(mContext, R.layout.clear_log_lyt);
            Dialog dialogueLayoutCLearLogUp = Constant.dialogLayoutColor;
            LinearLayout clearcalllog = dialogueLayoutCLearLogUp.findViewById(R.id.clearcalllog);
            dialogueLayoutCLearLogUp.show();
            clearcalllog.setOnClickListener(v -> {
                Webservice.clear_voice_calling_list(mContext, uid, CallUtil.this, binding.noData, "1");
                dialogueLayoutCLearLogUp.dismiss();
            });
        } else {
            Constant.dialogueLayoutCLearLogBottom(mContext, R.layout.clear_log_lyt);
            Dialog dialogueLayoutCLearLogBottom = Constant.dialogLayoutColor;
            LinearLayout clearcalllog = dialogueLayoutCLearLogBottom.findViewById(R.id.clearcalllog);
            dialogueLayoutCLearLogBottom.show();
            clearcalllog.setOnClickListener(v -> {
                Webservice.clear_voice_calling_list(mContext, uid, CallUtil.this, binding.noData, "1");
                dialogueLayoutCLearLogBottom.dismiss();
            });
        }
    }

    private void toggleSearchBar() {
        if (binding.searchLytNew != null) {
            if (binding.searchLytNew.getVisibility() == View.VISIBLE) {
                binding.searchLytNew.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                }
                if (MainActivityOld.linearMain != null) {
                    MainActivityOld.slideUpContainer();
                    backlyt.setVisibility(View.VISIBLE);
                }
                if (binding.search != null) binding.search.requestFocus();
            } else {
                if (binding.search != null) binding.search.requestFocus();
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_four);
                binding.searchLytNew.setAnimation(animation);
                binding.searchLytNew.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                if (MainActivityOld.linearMain != null) MainActivityOld.slideUpContainer();
                if (binding.search != null) binding.search.requestFocus();
            }
        }
    }

    public void setAdapter(ArrayList<get_contact_model> get_calling_contact_list) {
        Constant.getSfFuncion(mContext);
        String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");
        ArrayList<get_contact_model> filteredList = new ArrayList<>();
        for (get_contact_model model : get_calling_contact_list) {
            if (!model.getUid().equals(currentUid) && !model.isBlock()) {
                filteredList.add(model);
            }
        }
        this.get_calling_contact_list = filteredList;

        if (recyclerviewAZ != null) {
            adapter = new get_voice_calling_adapter(
                    mContext, binding.bottomcaller, binding.mainname, binding.phone, binding.photo, binding.ftoken,
                    binding.callerId, this.get_calling_contact_list, binding.deviceType, binding.call);
            layoutManager = new LinearLayoutManager(mContext);
            recyclerviewAZ.setLayoutManager(layoutManager);
            recyclerviewAZ.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Log.w("CallUtil", "recyclerviewAZ is null, cannot set adapter.");
        }
    }

    private void filteredList(String newText) {
        ArrayList<get_contact_model> filteredList = new ArrayList<>();
        for (get_contact_model list : get_calling_contact_list) {
            if (list.getFull_name().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(list);
            }
        }
        if (!filteredList.isEmpty()) {
            if (adapter != null) adapter.searchFilteredData(filteredList);
        }
    }

    private void filteredListNumber(String newText) {
        ArrayList<get_contact_model> filteredList = new ArrayList<>();
        for (get_contact_model list : get_calling_contact_list) {
            if (list.getMobile_no().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(list);
            }
        }
        if (!filteredList.isEmpty()) {
            if (adapter != null) adapter.searchFilteredData(filteredList);
        }
    }

    public void setAdapterLog(ArrayList<get_call_log_1Child> model) {
        Constant.getSfFuncion(mContext);
        ArrayList<get_call_log_1Child> filteredList = new ArrayList<>();
        for (get_call_log_1Child item : model) {
            ArrayList<user_infoModel> originalUserInfoList = item.getUser_info();
            ArrayList<user_infoModel> filteredUserInfoList = new ArrayList<>();
            for (user_infoModel userInfo : originalUserInfoList) {
                if (!userInfo.isBlock()) {
                    filteredUserInfoList.add(userInfo);
                }
            }
            if (!filteredUserInfoList.isEmpty()) {
                get_call_log_1Child filteredItem = new get_call_log_1Child();
                filteredItem.setDate(item.getDate());
                filteredItem.setSr_nos(item.getSr_nos());
                filteredItem.setUser_info(filteredUserInfoList);
                filteredList.add(filteredItem);
            }
        }
        this.get_call_log_1ChildList = filteredList;

        if (recyclerviewLast != null) {
            calllogParentAdapterVoice = new calllogParentAdapterVoice(
                    mContext, this.get_call_log_1ChildList, binding.bottomcaller, binding.mainname,
                    binding.phone, binding.photo, binding.ftoken, binding.callerId, CallUtil.this, binding.deviceType, binding.call);
            layoutManager = new LinearLayoutManager(mContext);
            recyclerviewLast.setLayoutManager(layoutManager);
            recyclerviewLast.setAdapter(calllogParentAdapterVoice);
            calllogParentAdapterVoice.notifyDataSetChanged();

            ViewGroup.LayoutParams currentParams = recyclerviewLast.getLayoutParams();
            if (currentParams instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) currentParams;
                int topMarginInPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics());
                layoutParams.topMargin = topMarginInPx;
                recyclerviewLast.setLayoutParams(layoutParams);
            } else if (currentParams instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) currentParams;
                int topMarginInPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics());
                layoutParams.topMargin = topMarginInPx;
                recyclerviewLast.setLayoutParams(layoutParams);
            }
        } else {
            Log.w("CallUtil", "recyclerviewLast is null, cannot set adapter.");
        }
    }

    public void setCallHistoryAdapter(ArrayList<call_log_history_model> call_log_history_lists) {
        this.call_log_history_list = call_log_history_lists;
        if (recyclerviewLast != null) {
            call_history_adapter_for_voice = new call_history_adapter_for_voice(mContext, this.call_log_history_list);
            recyclerviewLast.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerviewLast.setAdapter(call_history_adapter_for_voice);
            call_history_adapter_for_voice.notifyDataSetChanged();

            ViewGroup.LayoutParams currentParams = recyclerviewLast.getLayoutParams();
            if (currentParams instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) currentParams;
                int topMarginInPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 40, mContext.getResources().getDisplayMetrics());
                layoutParams.topMargin = topMarginInPx;
                recyclerviewLast.setLayoutParams(layoutParams);
            } else if (currentParams instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) currentParams;
                int topMarginInPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 40, mContext.getResources().getDisplayMetrics());
                layoutParams.topMargin = topMarginInPx;
                recyclerviewLast.setLayoutParams(layoutParams);
            }
        } else {
            Log.w("CallUtil", "recyclerviewLast is null, cannot set call history adapter.");
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void cleanup() {
        try {
            mContext.unregisterReceiver(connectivityReceiver);
        } catch (Exception e) {
            Log.w("CallUtil", "Error unregistering connectivity receiver: " + e.getMessage());
        }
    }
}