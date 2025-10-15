package com.Appzia.enclosure.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.call_history_adapter;
import com.Appzia.enclosure.Adapter.calllogParentAdapter;
import com.Appzia.enclosure.Adapter.get_calling_contact_list_adapter;
import com.Appzia.enclosure.Model.call_log_history_model;
import com.Appzia.enclosure.Model.callingUserInfoChildModel;
import com.Appzia.enclosure.Model.callloglistModel;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.Model.incomingVideoCallModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
//import com.Appzia.enclosure.Screens.videoCallingTwo;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.FcmNotificationsSender;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.Utils.WebserviceRetrofits.APIClient;
import com.Appzia.enclosure.activities.ConnectingActivity;
import com.Appzia.enclosure.activities.MainActivityVideoCall;
import com.Appzia.enclosure.databinding.FragmentVideoCallBinding;

import com.Appzia.enclosure.models.get_call_log_1Child;
import com.Appzia.enclosure.models.get_call_log_1Model;
import com.Appzia.enclosure.models.user_infoModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class videoCallFragment extends Fragment implements ConnectivityReceiver.ConnectivityListener {

    FragmentVideoCallBinding binding;

    public static LinearLayout layoutName, searchData, label;
    public static RelativeLayout contactLayout;
    private boolean hasSlidDown = false;
    private boolean hasSlidUp = false;

    public static ImageView img;
    public static TextView name1, mobile, dateLive;

    private GestureDetector gestureDetector;
    public static LinearLayout bottomcaller2;
    call_history_adapter call_history_adapter;
    public static TextView mainname;
    public static TextView phone;
    public static TextView photo;
    public static TextView ftoken;
    public static TextView callerId;
    public static LinearLayout backarroe;
    private boolean isScrollingUp = false;
    String themColor;
    public static RecyclerView recyclerviewLast, recyclerviewAZ;
    ColorStateList tintList;
    get_calling_contact_list_adapter adapter;
    Drawable drawable;
    public static LinearLayout backlytVideoCall;

    public static String fontSizePref;
    String name;

    int pixels;
    Context mContext;
    String uid;
    String token;
    private String sampleToken = Constant.VIDEOSDKTOKEN;

    private static final int PERMISSION_REQ_ID = 22;

    FirebaseDatabase database;
    private ConnectivityReceiver connectivityReceiver;
    private boolean hasSlidBelowThreshold = false;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }


    public static ArrayList<get_call_log_1Child> get_call_log_1ChildList = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list1 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list2 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list3 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list4 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list5 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list6 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list7 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list8 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list9 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list10 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list11 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list12 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list13 = new ArrayList<>();
    public static ArrayList<callingUserInfoChildModel> list14 = new ArrayList<>();

    ArrayList<get_contact_model> get_calling_contact_list = new ArrayList<>();
    calllogParentAdapter calllogAdapter;

    ArrayList<callloglistModel> parentcallloglist = new ArrayList<>();
    ArrayList<call_log_history_model> call_log_history_list;


    @Override
    public void onConnectivityChanged(boolean isConnected) {

        if (isConnected) {
        } else {
            binding.recyclerviewLast.setVisibility(View.VISIBLE);
            binding.recyclerviewAZ.setVisibility(View.GONE);
            Log.d("Network", "disconnected: " + "callFragment");

            try {

                Log.d("Network", "dissconnetced: " + "callFragment");
                //  get_call_log_1ChildList.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);


                if (parentcallloglist.size() > 0) {
                    binding.progressBar.setVisibility(View.GONE);
                    setAdapterLog(get_call_log_1ChildList);
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                }


                binding.recyclerviewLast.setVisibility(View.VISIBLE);


            } catch (Exception ignored) {
            }

        }

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
            Constant.setSF.apply();

        } catch (Exception ignored) {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Constant.getSfFuncion(mContext);

        try {
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
            Constant.setSF.apply();
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

                binding.contact.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.log.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);

                binding.search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                binding.mainname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.contact.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.log.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                binding.search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                binding.mainname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.contact.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.log.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                binding.search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                binding.mainname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            }
        } catch (Exception ignored) {

        }

        final float scale = requireActivity().getResources().getDisplayMetrics().density;
        pixels = (int) (68 * scale + 0.5f);
        assert getFragmentManager() != null;

        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.call.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));

                } else if (themColor.equals("#00A3E9")) {
                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {
                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#ec0001")) {
                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#16f3ff")) {
                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#FF8A00")) {

                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7F7F7F")) {

                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {
                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#346667")) {
                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#9846D9")) {
                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#A81010")) {
                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                } else {
                    binding.call.setBackgroundTintList(tintList);
                    binding.pollyyddd.setBackgroundTintList(tintList);
                    binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                    binding.view.setBackgroundTintList(tintList);

                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // todo -------------------online------------------------------

        backlytVideoCall = binding.getRoot().findViewById(R.id.backlyt);
        backarroe = binding.getRoot().findViewById(R.id.backarroe);
        binding.recyclerviewLast.setVisibility(View.VISIBLE);
        binding.recyclerviewAZ.setVisibility(View.GONE);
        try {

            Log.d("Network", "dissconnetced: " + "callFragment");
            //   get_call_log_1ChildList.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            //    get_call_log_1ChildList = dbHelper.get_voice_call_logVIDEOTable();


            if (get_call_log_1ChildList.size() > 0) {

                binding.recyclerviewLast.setVisibility(View.VISIBLE);

                setAdapterLog(get_call_log_1ChildList);
                binding.progressBar.setVisibility(View.GONE);
            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.recyclerviewLast.setVisibility(View.GONE);

            }


        } catch (Exception ignored) {
        }

        // todo is connected call only one api here frgment
        Log.d("Network", "connected: " + "callFragment");
        try {

            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");


          //  Webservice.get_call_log(mContext, uid, videoCallFragment.this, binding.recyclerviewLast, binding.progressBar, binding.noData);


        } catch (Exception ignored) {
        }
        // todo -------------------online------------------------------
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (Build.VERSION.SDK_INT >= 35) {
//                // Show system bars when the fragment stops
//                requireActivity().getWindow().getInsetsController().show(WindowInsets.Type.systemBars());
//            }
//        }
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentVideoCallBinding.inflate(inflater, container, false);

        View bottomSheet = binding.getRoot().findViewById(R.id.localBottomSheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

// Prevent collapse/hidden
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetBehavior.setSkipCollapsed(true);
        bottomSheetBehavior.setHideable(false);

// RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerviewLast.setLayoutManager(layoutManager);
// binding.recyclerviewAZ.setLayoutManager(layoutManager);

// BottomSheetCallback
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

                // Enable dragging so user can pull slightly again
                if (slideOffset >= 1.0f) {

                    hasSlidBelowThreshold = false;
                }

                // Trigger only once when it drops below threshold
                else if (slideOffset < 0.996f && !hasSlidBelowThreshold) {
                    hasSlidBelowThreshold = true;

                    Log.d("BottomSheet", "Less than fully expanded");
                    if(get_call_log_1ChildList.size()>7) {
                        if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                            MainActivityOld.slideDownContainer();
                        }
                    }

//
                }
            }
        });



        binding.recyclerviewLast.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();

                    View firstView = layoutManager.findViewByPosition(firstVisiblePosition);

                    // Slide down only once when top is fully visible
                    if (firstVisiblePosition == 0 && firstView != null && firstView.getTop() == 0) {
                        if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE && !hasSlidDown) {
                            Log.d("Scroll", "Upper part of first item fully visible");
                            if(get_call_log_1ChildList.size()>7) {
                                if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                                    MainActivityOld.slideDownContainer();
                                }
                            }
                            hasSlidDown = true;
                            hasSlidUp = false; // Reset opposite flag
                        }
                    } else {
                        hasSlidDown = false; // Reset when user scrolls away from top
                    }

                    // Slide up only once when bottom is fully visible
                    if (lastVisiblePosition == totalItemCount - 1 && totalItemCount > 0) {
                        View lastView = layoutManager.findViewByPosition(lastVisiblePosition);
                        if (lastView != null && lastView.getBottom() <= recyclerView.getHeight()) {
                            if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE && !hasSlidUp) {
                                Log.d("Scroll", "Reached end of list. Sliding up container.");
                                if(get_call_log_1ChildList.size()>7) {
                                    if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                                        MainActivityOld.slideUpContainer();
                                    }

                                    if(call_log_history_list.size()>7){
                                        if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                                            MainActivityOld.slideUpContainer();
                                        }
                                    }
                                }
                                hasSlidUp = true;
                                hasSlidDown = false; // Reset opposite flag
                            }
                        }
                    } else {
                        hasSlidUp = false; // Reset when user scrolls away from bottom
                    }
                }
            }
        });


        binding.recyclerviewAZ.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();

                    View firstView = layoutManager.findViewByPosition(firstVisiblePosition);

                    // Slide down only once when top is fully visible
                    if (firstVisiblePosition == 0 && firstView != null && firstView.getTop() == 0) {
                        if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE && !hasSlidDown) {
                            Log.d("Scroll", "Upper part of first item fully visible");
                            if(get_calling_contact_list.size() >8) {
                                if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                                    MainActivityOld.slideDownContainer();
                                }
                            }
                            hasSlidDown = true;
                            hasSlidUp = false; // Reset opposite flag
                        }
                    } else {
                        hasSlidDown = false; // Reset when user scrolls away from top
                    }

                    // Slide up only once when bottom is fully visible
                    if (lastVisiblePosition == totalItemCount - 1 && totalItemCount > 0) {
                        View lastView = layoutManager.findViewByPosition(lastVisiblePosition);
                        if (lastView != null && lastView.getBottom() <= recyclerView.getHeight()) {
                            if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE && !hasSlidUp) {
                                Log.d("Scroll", "Reached end of list. Sliding up container.");
                                if(get_calling_contact_list.size() >8) {
                                    if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                                        MainActivityOld.slideUpContainer();
                                    }
                                }
                                hasSlidUp = true;
                                hasSlidDown = false; // Reset opposite flag
                            }
                        }
                    } else {
                        hasSlidUp = false; // Reset when user scrolls away from bottom
                    }
                }
            }
        });

        mContext = binding.getRoot().getContext();
        database = FirebaseDatabase.getInstance();
        layoutName = binding.getRoot().findViewById(R.id.layoutName);
        searchData = binding.getRoot().findViewById(R.id.searchData);
        label = binding.getRoot().findViewById(R.id.label);
        contactLayout = binding.getRoot().findViewById(R.id.contactLayout);
        img = binding.getRoot().findViewById(R.id.img);
        backlytVideoCall = binding.getRoot().findViewById(R.id.backlyt);
        name1 = binding.getRoot().findViewById(R.id.name1);
        mobile = binding.getRoot().findViewById(R.id.mobile);
        dateLive = binding.getRoot().findViewById(R.id.dateLive);
        recyclerviewLast = binding.getRoot().findViewById(R.id.recyclerviewLast);
        recyclerviewAZ = binding.getRoot().findViewById(R.id.recyclerviewAZ);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, Constant.searchVideoCall);
        binding.search.setAdapter(adapter);

        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, mContext.RECEIVER_EXPORTED);
        call_log_history_list = new ArrayList<>();

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideDownContainer();
                }
            }
        });

//        binding.recyclerviewLast.setOnTouchListener(new OnSwipeTouchListener(mContext) {
//            public void onSwipeTop() {
////                Toast.makeText(mContext, "up", Toast.LENGTH_SHORT).show();
//                MainActivityOld.slideUpContainer();
//
//                binding.recyclerviewLast.setOnTouchListener(null);
//
//
//
//            }
//
//            public void onSwipeRight() {
////                Toast.makeText(mContext, "right", Toast.LENGTH_SHORT).show();
//
//            }
//
//            public void onSwipeLeft() {
////                Toast.makeText(mContext, "left", Toast.LENGTH_SHORT).show();
//            }
//
//            public void onSwipeBottom() {
//
//                //      MainActivity.slideDownContainer();
//
//            }
//
//        });
//        binding.recyclerviewAZ.setOnTouchListener(new OnSwipeTouchListener(mContext) {
//            public void onSwipeTop() {
////                Toast.makeText(mContext, "up", Toast.LENGTH_SHORT).show();
//                MainActivityOld.slideUpContainer();
//                binding.recyclerviewAZ.setOnTouchListener(null);
////                binding.recyclerviewLast.setOnTouchListener(null);
//
//            }
//
//            public void onSwipeRight() {
////                Toast.makeText(mContext, "right", Toast.LENGTH_SHORT).show();
//
//            }
//
//            public void onSwipeLeft() {
////                Toast.makeText(mContext, "left", Toast.LENGTH_SHORT).show();
//            }
//
//            public void onSwipeBottom() {
//
//                //  MainActivity.slideDownContainer();
////                binding.recyclerviewLast.setOnTouchListener(null);
//
//            }
//
//        });

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (TextUtils.isDigitsOnly(binding.search.getText().toString().trim()) && binding.search.getText().toString().trim().length() == 10) {
                    filteredListNumber(Constant.getSF.getString(Constant.COUNTRY_CODE, "") + String.valueOf(s));
                } else {
                    filteredList(String.valueOf(s));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MainActivityOld.slideUpContainer();
                binding.search.requestFocus();
                return false;

            }
        });


        try {
            mainname = requireActivity().findViewById(R.id.mainname);
            bottomcaller2 = requireActivity().findViewById(R.id.bottomcaller2);
            phone = requireActivity().findViewById(R.id.phone);
            ftoken = requireActivity().findViewById(R.id.ftoken);
            photo = requireActivity().findViewById(R.id.photo);
            callerId = requireActivity().findViewById(R.id.callerId);

        } catch (Exception ignored) {

        }


        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date2 = new Date();
                String currentDate = dateFormat.format(date2);
                Log.d("currentDate", currentDate);
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Format the time with AM/PM
                String amPm;
                if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                    amPm = "am";
                } else {
                    amPm = "pm";
                }
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
                String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;

                Constant.getSfFuncion(mContext);
                String username = Constant.getSF.getString(Constant.UID_KEY, "");
                //   createMeeting(token);


                uid = Constant.getSF.getString(Constant.UID_KEY, "");
                String profilePic = Constant.getSF.getString(Constant.profilePic, "");
                String full_name = Constant.getSF.getString(Constant.full_name, "");
                String roomId = Constant.generateRoomIdFromTimestamp();

                Intent intent = new Intent(mContext, MainActivityVideoCall.class);
                intent.putExtra("fTokenKey", binding.ftoken.getText().toString());
                intent.putExtra("currentTime", currentTime);
                intent.putExtra("receiverId", binding.callerId.getText().toString().trim());
                intent.putExtra("phoneKey", binding.phone.getText().toString());
                intent.putExtra("senderId", uid);
                intent.putExtra("deviceType", binding.deviceType.getText().toString());
                intent.putExtra("firstKey", "firstKey");
                intent.putExtra("username", uid);
                intent.putExtra("createdBy", uid);
                intent.putExtra("roomId", roomId);
                intent.putExtra("incoming", uid);
                intent.putExtra("photo", profilePic);
                intent.putExtra("name", full_name);
                intent.putExtra("nameReceiver", binding.mainname.getText().toString());
                intent.putExtra("photoReceiver", binding.photo.getText().toString());
                intent.putExtra("roomFlagKey", "roomFlagKey");
                intent.putExtra("iAmSenderNew", true);
                startActivity(intent);
                requireActivity().finish();

                //todo send notification from here

                Constant.getSfFuncion(mContext);
                String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");


                if (sleepKey.equals(Constant.sleepKey)) {

                } else {
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(binding.ftoken.getText().toString(), "Enclosure", Constant.videocall, mContext, requireActivity(), Constant.getSF.getString(Constant.full_name, ""), "meetingId", binding.phone.getText().toString(), Constant.getSF.getString(Constant.profilePic, ""), token, uid, binding.callerId.getText().toString(), binding.deviceType.getText().toString(), uid, uid, uid, roomId);
                    notificationsSender.SendNotifications();
                }

            }
        });

        binding.backarroe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.label.setVisibility(View.VISIBLE);
                binding.layoutName.setVisibility(View.GONE);
                binding.searchData.setVisibility(View.VISIBLE);
                binding.dateLive.setVisibility(View.GONE);
                binding.contact.setBackgroundResource(R.drawable.radius_6dp_transp);
                binding.log.setBackgroundResource(R.drawable.radius_black_6dp);
                binding.contact.setTextColor(Color.parseColor("#000000"));
                binding.log.setTextColor(Color.parseColor("#ffffff"));
                binding.recyclerviewLast.setVisibility(View.VISIBLE);
                binding.recyclerviewAZ.setVisibility(View.GONE);
                binding.log.performClick();
            }
        });


        binding.log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                Webservice.cancelAllRequests();
                APIClient.cancelAllRequests();
                binding.recyclerviewLast.setVisibility(View.VISIBLE);
                binding.recyclerviewAZ.setVisibility(View.GONE);
                if (binding.searchData.getVisibility() == View.VISIBLE) {
                    Animation animation3 = AnimationUtils.loadAnimation(mContext, R.anim.fadeoutthreethnd);
                    binding.searchData.setAnimation(animation3);
                    binding.searchData.setVisibility(View.GONE);
                }
                if (isInternetConnected()) {
                    binding.menu.setVisibility(View.VISIBLE);
                    binding.contact.setBackgroundResource(R.drawable.radius_6dp_transp);
                    binding.log.setBackgroundResource(R.drawable.radius_black_6dp);
                    binding.contact.setTextColor(Color.parseColor("#000000"));
                    binding.log.setTextColor(Color.parseColor("#ffffff"));
                    try {
                        Log.d("Network", "dissconnetced: " + "callFragment");
                        get_call_log_1ChildList.clear();
                        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                        //    get_call_log_1ChildList = dbHelper.get_voice_call_logVIDEOTable();


//                        if (get_call_log_1ChildList.size() > 0) {
//
//                            binding.recyclerviewLast.setVisibility(View.VISIBLE);
//
//                            setAdapterLog(get_call_log_1ChildList);
//                        } else {
//
//
//                            binding.recyclerviewLast.setVisibility(View.GONE);
//
//                        }

                        binding.recyclerviewLast.setVisibility(View.VISIBLE);

                        setAdapterLog(get_call_log_1ChildList);


                    } catch (Exception ignored) {
                    }

                    // todo is connected call only one api here frgment
                    Log.d("Network", "connected: " + "callFragment");
                    try {

                        Constant.getSfFuncion(mContext);
                        uid = Constant.getSF.getString(Constant.UID_KEY, "");
                      //  Webservice.get_call_log(mContext, uid, videoCallFragment.this, binding.recyclerviewLast, binding.progressBar, binding.noData);


                    } catch (Exception ignored) {
                    }
                } else {


                    binding.menu.setVisibility(View.VISIBLE);

                    binding.contact.setBackgroundResource(R.drawable.radius_6dp_transp);
                    binding.log.setBackgroundResource(R.drawable.radius_black_6dp);
                    binding.contact.setTextColor(Color.parseColor("#000000"));
                    binding.log.setTextColor(Color.parseColor("#ffffff"));


                    try {

                        Log.d("Network", "dissconnetced: " + "callFragment");
                        get_call_log_1ChildList.clear();
                        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                        //  get_call_log_1ChildList = dbHelper.get_voice_call_logVIDEOTable();
                        setAdapterLog(get_call_log_1ChildList);

                        binding.recyclerviewLast.setVisibility(View.VISIBLE);


                    } catch (Exception ignored) {
                    }


                }


            }
        });

        binding.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.clearcalllog.getVisibility() == View.VISIBLE) {
                    binding.clearcalllog.setVisibility(View.GONE);
                }
                binding.noData.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                Webservice.cancelAllRequests();
                APIClient.cancelAllRequests();
                binding.recyclerviewLast.setVisibility(View.GONE);
                binding.recyclerviewAZ.setVisibility(View.VISIBLE);
                if (binding.searchData.getVisibility() == View.GONE) {
                    Animation animation3 = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                    binding.searchData.setAnimation(animation3);
                    binding.searchData.setVisibility(View.VISIBLE);
                    binding.search.requestFocus();

                }
                if (isInternetConnected()) {
                    Log.d("Network", "connected: " + "callFragment");


                    // todo this is first will taking offline data first


                    try {
                        //TODO : - OFFLINE DATA LOAD HERE WHEN ,UNTIL WEBSERVICE 200 STATUS OR ERROR

                        get_calling_contact_list.clear();
                        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                        get_calling_contact_list = dbHelper.get_users_all_contactTable();

                        if (get_calling_contact_list.size() > 0) {

                            if (get_calling_contact_list.size() > 0) {
                                binding.progressBar.setVisibility(View.GONE);
                            } else {
                                binding.progressBar.setVisibility(View.VISIBLE);
                            }
                            // Toast.makeText(mContext, "not empty", Toast.LENGTH_SHORT).show();
                            setAdapter(get_calling_contact_list);
                            binding.noData.setVisibility(View.GONE);


                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    binding.recyclerviewLast.setVisibility(View.GONE);
                    binding.bottomcaller2.setVisibility(View.GONE);
                    binding.contact.setBackgroundResource(R.drawable.radius_black_6dp);
                    binding.log.setBackgroundResource(R.drawable.radius_6dp_transp);
                    binding.log.setTextColor(Color.parseColor("#000000"));
                    binding.contact.setTextColor(Color.parseColor("#ffffff"));
                    binding.menu.setVisibility(View.INVISIBLE);
                    binding.recyclerviewLast.setVisibility(View.GONE);


               //     Webservice.get_calling_contact_list(mContext, uid, videoCallFragment.this, binding.recyclerviewAZ, binding.progressBar, binding.noData);


                } else {

                    Log.d("Network", "disconnected: " + "callFragment");
                    binding.clearcalllog.setVisibility(View.GONE);


                    binding.recyclerviewLast.setVisibility(View.GONE);
                    binding.bottomcaller2.setVisibility(View.GONE);
                    binding.contact.setBackgroundResource(R.drawable.radius_black_6dp);
                    binding.log.setBackgroundResource(R.drawable.radius_6dp_transp);
                    binding.log.setTextColor(Color.parseColor("#000000"));
                    binding.contact.setTextColor(Color.parseColor("#ffffff"));
                    binding.menu.setVisibility(View.INVISIBLE);
                    binding.recyclerviewLast.setVisibility(View.GONE);

                    try {

                        get_calling_contact_list.clear();
                        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                        get_calling_contact_list = dbHelper.get_users_all_contactTable();
                        setAdapter(get_calling_contact_list);


                        binding.recyclerviewLast.setVisibility(View.GONE);


                    } catch (Exception ignored) {
                    }

                }

            }
        });


        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivityOld.sliderealtime.getText().toString().equals("up")) {
                    Constant.dialogueLayoutCLearLogUp(mContext, R.layout.clear_log_lyt);
                    Dialog dialogueLayoutCLearLogUp = Constant.dialogLayoutColor;
                    LinearLayout clearcalllog = dialogueLayoutCLearLogUp.findViewById(R.id.clearcalllog);


                    dialogueLayoutCLearLogUp.show();
                    clearcalllog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  Webservice.clear_video_calling_list(mContext, uid, videoCallFragment.this, binding.noData, "2");
                            dialogueLayoutCLearLogUp.dismiss();
                        }
                    });
                } else {

                    Constant.dialogueLayoutCLearLogBottom(mContext, R.layout.clear_log_lyt);
                    Dialog dialogueLayoutCLearLogBottom = Constant.dialogLayoutColor;
                    LinearLayout clearcalllog = dialogueLayoutCLearLogBottom.findViewById(R.id.clearcalllog);

                    dialogueLayoutCLearLogBottom.show();
                    clearcalllog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        //    Webservice.clear_video_calling_list(mContext, uid, videoCallFragment.this, binding.noData, "2");
                            dialogueLayoutCLearLogBottom.dismiss();
                        }
                    });

                }


            }
        });


        binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.searchLytNew.getVisibility() == View.VISIBLE) {
                    binding.searchLytNew.setVisibility(View.GONE);

                    View view = requireActivity().findViewById(android.R.id.content);
                    InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    MainActivityOld.slideUpContainer();
                    binding.search.requestFocus();

                } else if (binding.searchLytNew.getVisibility() == View.GONE) {
                    binding.search.requestFocus();
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_four);
                    binding.searchLytNew.setAnimation(animation);
                    binding.searchLytNew.setVisibility(View.VISIBLE);


                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }

                    MainActivityOld.slideUpContainer();
                    binding.search.requestFocus();
                }

            }
        });

        return binding.getRoot();
    }

    public void setAdapter(ArrayList<get_contact_model> get_calling_contact_list) {
        // Get current user UID from shared preferences
        Constant.getSfFuncion(mContext);
        String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");

        // Filter out the current user and blocked users
        ArrayList<get_contact_model> filteredList = new ArrayList<>();
        for (get_contact_model model : get_calling_contact_list) {
            if (!model.getUid().equals(currentUid) && !model.isBlock()) {
                filteredList.add(model);
            }
        }

        // Use the filtered list
        this.get_calling_contact_list = filteredList;

        // Set up adapter and RecyclerView
        adapter = new get_calling_contact_list_adapter(
                mContext,
                binding.bottomcaller2,
                binding.mainname,
                binding.phone,
                binding.photo,
                binding.ftoken,
                binding.callerId,
                this.get_calling_contact_list,
                binding.deviceType,
                binding.call
        );

        binding.recyclerviewAZ.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerviewAZ.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    private void filteredList(String newText) {
        ArrayList<get_contact_model> filteredList = new ArrayList<>();

        for (get_contact_model list : get_calling_contact_list) {
            if (list.getFull_name().toLowerCase().contains(newText.toLowerCase())) {

                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {
            //  Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.searchFilteredData(filteredList);
        }
    }

    private void filteredListNumber(String newText) {
        ArrayList<get_contact_model> filteredList = new ArrayList<>();

        for (get_contact_model list : get_calling_contact_list) {
            if (list.getMobile_no().toLowerCase().contains(newText.toLowerCase())) {

                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {
            //  Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.searchFilteredData(filteredList);
        }
    }

    private void createMeeting(String token) {


        // we will make an API call to VideoSDK Server to get a roomId
        AndroidNetworking.post("https://api.videosdk.live/v2/rooms")
                .addHeaders("Authorization", token) //we will pass the token in the Headers
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            // response will contain `roomId`
                            final String meetingId = response.getString("roomId");
                            Constant.getSfFuncion(mContext);
                            String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");


                            if (sleepKey.equals(Constant.sleepKey)) {

                            } else {
//                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(binding.ftoken.getText().toString(), "Enclosure", Constant.videocall, mContext, requireActivity(), Constant.getSF.getString(Constant.full_name, ""), meetingId, binding.phone.getText().toString(), Constant.getSF.getString(Constant.profilePic, ""), token, uid, binding.callerId.getText().toString(), binding.deviceType.getText().toString(), username, createdBy, incoming);
//                                notificationsSender.SendNotifications();
                            }


                            final incomingVideoCallModel model = new incomingVideoCallModel(binding.ftoken.getText().toString(), Constant.getSF.getString(Constant.full_name, ""), meetingId, binding.phone.getText().toString(), Constant.getSF.getString(Constant.profilePic, ""), token, uid, binding.callerId.getText().toString(), "");
                            database.getReference().child(Constant.INCOMING_VIDEO_CALL).child(binding.callerId.getText().toString().trim()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //  Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date2 = new Date();
                            String currentDate = dateFormat.format(date2);
                            Log.d("currentDate", currentDate);
                            Calendar calendar = Calendar.getInstance();
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);

                            // Format the time with AM/PM
                            String amPm;
                            if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                                amPm = "am";
                            } else {
                                amPm = "pm";
                            }
                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
                            String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;

                            Log.d("currentTime", currentTime);
                            Log.d("meetingId##", meetingId);

                            //for outgoing flag 0
                            //1 for voice call 2 for video call


                            // starting the MeetingActivity with received roomId and our sampleToken
                            //    Intent intent = new Intent(mContext, videoCallingTwo.class);
                            //  intent.putExtra("token", sampleToken);
                            // intent.putExtra("meetingId", meetingId);
                            // intent.putExtra("firstKey", "firstKey");
                            // intent.putExtra("playKey", "playKey");
//                            intent.putExtra("fTokenKey", binding.ftoken.getText().toString());
//                            intent.putExtra("currentTime", currentTime);
//                            intent.putExtra("receiverId", binding.callerId.getText().toString().trim());
//                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(mContext, "Token expired" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void setAdapterLog(ArrayList<get_call_log_1Child> model) {
        Constant.getSfFuncion(mContext);
        String currentUid = Constant.getSF.getString(Constant.UID_KEY,""); // Replace with actual UID retrieval logic

        ArrayList<get_call_log_1Child> filteredList = new ArrayList<>();

        for (get_call_log_1Child item : model) {
            ArrayList<user_infoModel> originalUserInfoList = item.getUser_info();
            ArrayList<user_infoModel> filteredUserInfoList = new ArrayList<>();

            for (user_infoModel userInfo : originalUserInfoList) {
//                if (!userInfo.getId().equals(currentUid) && !userInfo.isBlock()) {
//                    filteredUserInfoList.add(userInfo);
//                }
                if ( !userInfo.isBlock()) {
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

//        calllogAdapter = new calllogParentAdapter(
//                mContext,
//                this.get_call_log_1ChildList,
//                binding.bottomcaller2,
//                binding.mainname,
//                binding.phone,
//                binding.photo,
//                binding.ftoken,
//                binding.callerId,
//                null,
//                binding.deviceType,
//                binding.call
//        );

        binding.recyclerviewLast.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerviewLast.setAdapter(calllogAdapter);
        calllogAdapter.notifyDataSetChanged();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.recyclerviewLast.getLayoutParams();
        int topMarginInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics());
        layoutParams.topMargin = topMarginInPx;
        binding.recyclerviewLast.setLayoutParams(layoutParams);
    }


    public void setCallHistoryAdapter(ArrayList<call_log_history_model> call_log_history_lists) {


        this.call_log_history_list = call_log_history_lists;
        call_history_adapter = new call_history_adapter(mContext, this.call_log_history_list);
        binding.recyclerviewLast.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerviewLast.setAdapter(call_history_adapter);
        call_history_adapter.notifyDataSetChanged();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.recyclerviewLast.getLayoutParams();
        int topMarginInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 40, mContext.getResources().getDisplayMetrics());
        layoutParams.topMargin = topMarginInPx;
        binding.recyclerviewLast.setLayoutParams(layoutParams);

    }


    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}