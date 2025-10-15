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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.Appzia.enclosure.Adapter.DummyAdapter;
import com.Appzia.enclosure.Adapter.call_history_adapter_for_voice;
import com.Appzia.enclosure.Adapter.calllogParentAdapterVoice;
import com.Appzia.enclosure.Adapter.get_voice_calling_adapter;
import com.Appzia.enclosure.Model.call_log_history_model;
import com.Appzia.enclosure.Model.callingUserInfoChildModel;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.Model.incomingVideoCallModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
//import com.Appzia.enclosure.Screens.voiceCallingSecond;
import com.Appzia.enclosure.Screens.chattingScreen;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.FcmNotificationsSender;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.Utils.WebserviceRetrofits.APIClient;
import com.Appzia.enclosure.activities.MainActivityVideoCall;
import com.Appzia.enclosure.activities.MainActivityVoiceCall;
import com.Appzia.enclosure.databinding.FragmentCallBinding;
import com.Appzia.enclosure.keyboardheight.MainActivity;
import com.Appzia.enclosure.models.get_call_log_1Child;
import com.Appzia.enclosure.models.user_infoModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class callFragment extends Fragment implements ConnectivityReceiver.ConnectivityListener {
    FragmentCallBinding binding;
    private boolean isScrollingUp = false;
    String themColor;
    // Define this as a class-level variable
    private boolean hasSlidBelowThreshold = false;

    LinearLayoutManager layoutManager;
    public static LinearLayout backarroe;
    int flag = 0;
    public static RecyclerView recyclerviewLast, recyclerviewAZ;
    ColorStateList tintList;
    private DummyAdapter DummyAdapteradapter;
    public static LinearLayout layoutName, searchData, label;
    call_history_adapter_for_voice call_history_adapter_for_voice;
    public static RelativeLayout contactLayout;
    public static BottomSheetBehavior<View> bottomSheetBehavior;
    public static ImageView img;
    public static TextView name1, mobile, dateLive;
    Drawable drawable;
    Context mContext;
    get_voice_calling_adapter adapter;
    calllogParentAdapterVoice calllogParentAdapterVoice;
    String uid;
    private boolean hasSlidDown = false;
    private boolean hasSlidUp = false;
    String name;
    int pixels;
    public static String fontSizePref;
    private GestureDetector gestureDetector;
    public static TextView mainname;
    String token;
    private String sampleToken = Constant.VIDEOSDKTOKEN;
    private static final int PERMISSION_REQ_ID = 22;
    private ConnectivityReceiver connectivityReceiver;
    FirebaseDatabase database;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};

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
    public static ArrayList<get_contact_model> get_calling_contact_list = new ArrayList<>();
    ArrayList<call_log_history_model> call_log_history_list;


    public static LinearLayout backlytCallFrag;
    public static RecyclerView recyclerViewDummy;
//  backlyt = binding.getRoot().findViewById(R.id.backlyt);

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
    public void onConnectivityChanged(boolean isConnected) {

        if (isConnected) {


        } else {
            binding.recyclerviewLast.setVisibility(View.VISIBLE);
            binding.recyclerviewAZ.setVisibility(View.GONE);
            Log.d("Network", "disconnected: " + "callFragment");

            try {

                Log.d("Network", "dissconnetced: " + "callFragment");
                get_call_log_1ChildList.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                //     get_call_log_1ChildList = dbHelper.get_voice_call_logTable();

                if (get_call_log_1ChildList.size() > 0) {
                    setAdapterLog(get_call_log_1ChildList);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                }


                binding.recyclerviewLast.setVisibility(View.VISIBLE);


            } catch (Exception ignored) {
            }

        }

    }
    @Override
    public void onStart() {
        super.onStart();
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


        final float scale = requireActivity().getResources().getDisplayMetrics().density;
        pixels = (int) (68 * scale + 0.5f);
        assert getFragmentManager() != null;

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
                        binding.call.setBackgroundTintList(tintList);
                        binding.view.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));

                    } else if (themColor.equals("#00A3E9")) {
                        binding.call.setBackgroundTintList(tintList);
                        binding.view.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);

                    } else if (themColor.equals("#7adf2a")) {

                        binding.call.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);
                    } else if (themColor.equals("#ec0001")) {

                        binding.call.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);
                    } else if (themColor.equals("#16f3ff")) {
                        binding.call.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);

                    } else if (themColor.equals("#FF8A00")) {
                        binding.call.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#7F7F7F")) {
                        binding.call.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#D9B845")) {
                        binding.call.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#346667")) {
                        binding.call.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#9846D9")) {
                        binding.call.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#A81010")) {
                        binding.call.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);

                    } else {
                        binding.call.setBackgroundTintList(tintList);
                        binding.menuPoint.setColorFilter(Color.parseColor(themColor));
                        binding.view.setBackgroundTintList(tintList);

                    }
                } catch (Exception ignored) {

                }


            } catch (Exception ignored) {
            }


        } catch (Exception ignored) {
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        backlytCallFrag = binding.getRoot().findViewById(R.id.backlyt);
        backarroe = binding.getRoot().findViewById(R.id.backarroe);
        binding.recyclerviewLast.setVisibility(View.VISIBLE);
        binding.recyclerviewAZ.setVisibility(View.GONE);
        try {

            Log.d("Network", "dissconnetced: " + "callFragment");
            get_call_log_1ChildList.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            //    get_call_log_1ChildList = dbHelper.get_voice_call_logTable();


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

       //     Webservice.get_voice_call_log_for_voice_call(mContext, uid, callFragment.this, binding.recyclerviewLast, binding.progressBar, binding.noData);


        } catch (Exception ignored) {
        }
        // todo ------------------------online----------------

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentCallBinding.inflate(inflater, container, false);

        // Inflate your custom bottom sheet layout
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
                                }
                                if(call_log_history_list.size()>7){
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
        backlytCallFrag = binding.getRoot().findViewById(R.id.backlyt);


        searchData = binding.getRoot().findViewById(R.id.searchData);
        label = binding.getRoot().findViewById(R.id.label);
        contactLayout = binding.getRoot().findViewById(R.id.contactLayout);
        img = binding.getRoot().findViewById(R.id.img);
        name1 = binding.getRoot().findViewById(R.id.name1);
        mobile = binding.getRoot().findViewById(R.id.mobile);
        dateLive = binding.getRoot().findViewById(R.id.dateLive);
        recyclerviewLast = binding.getRoot().findViewById(R.id.recyclerviewLast);

        recyclerviewAZ = binding.getRoot().findViewById(R.id.recyclerviewAZ);


        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        call_log_history_list = new ArrayList<>();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, mContext.RECEIVER_EXPORTED);


        // Set LinearLayoutManager for both RecyclerViews
        LinearLayoutManager layoutManagerLast = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManagerAZ = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerviewLast.setLayoutManager(layoutManagerLast);
        binding.recyclerviewAZ.setLayoutManager(layoutManagerAZ);



        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, Constant.searchVoiceCall);
        binding.search.setAdapter(adapter);

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


                Intent intent = new Intent(mContext, MainActivityVoiceCall.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fTokenKey", binding.ftoken.getText().toString());
                intent.putExtra("currentTime", currentTime);
                intent.putExtra("receiverId", binding.callerId.getText().toString().trim());
                intent.putExtra("phoneKey", binding.phone.getText().toString());
                intent.putExtra("senderId", uid);
                intent.putExtra("deviceType", binding.deviceType.getText().toString());
                intent.putExtra("firstKey", "firstKey");
                intent.putExtra("username", uid);
                intent.putExtra("createdBy", uid);
                intent.putExtra("incoming", uid);
                intent.putExtra("roomId", roomId);
                intent.putExtra("photo", profilePic);
                intent.putExtra("name", full_name);
                intent.putExtra("nameReceiver", binding.mainname.getText().toString());
                intent.putExtra("photoReceiver", binding.photo.getText().toString());
                intent.putExtra("roomFlagKey", "roomFlagKey");
                intent.putExtra("iAmSender", true);
                intent.putExtra("iAmSenderNew", true);
                startActivity(intent);
                requireActivity().finish();

                //todo send notification from here

                Constant.getSfFuncion(mContext);
                String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");


                if (sleepKey.equals(Constant.sleepKey)) {

                } else {
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(binding.ftoken.getText().toString(), "Enclosure", Constant.voicecall, mContext, requireActivity(), Constant.getSF.getString(Constant.full_name, ""), "meetingId", binding.phone.getText().toString(), Constant.getSF.getString(Constant.profilePic, ""), token, uid, binding.callerId.getText().toString(), binding.deviceType.getText().toString(), uid, uid, uid, roomId);
                    notificationsSender.SendNotifications();
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
                    binding.searchData.setAnimation(animation3);
                    binding.searchData.setVisibility(View.VISIBLE);
                    binding.search.requestFocus();

                }
                if (isInternetConnected()) {

                    try {
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


                    binding.bottomcaller.setVisibility(View.GONE);
                    binding.contact.setBackgroundResource(R.drawable.radius_black_6dp);
                    binding.log.setBackgroundResource(R.drawable.radius_6dp_transp);
                    binding.log.setTextColor(Color.parseColor("#000000"));
                    binding.contact.setTextColor(Color.parseColor("#ffffff"));
                    binding.menu.setVisibility(View.INVISIBLE);


                    Webservice.get_calling_contact_list_for_voiceCall(mContext, uid, callFragment.this, binding.recyclerviewAZ, binding.progressBar, binding.noData);


                } else {

                    Log.d("Network", "disconnected: " + "callFragment");
                    binding.clearcalllog.setVisibility(View.GONE);


                    binding.bottomcaller.setVisibility(View.GONE);
                    binding.contact.setBackgroundResource(R.drawable.radius_black_6dp);
                    binding.log.setBackgroundResource(R.drawable.radius_6dp_transp);
                    binding.log.setTextColor(Color.parseColor("#000000"));
                    binding.contact.setTextColor(Color.parseColor("#ffffff"));
                    binding.menu.setVisibility(View.INVISIBLE);


                    try {

                        get_calling_contact_list.clear();
                        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                        get_calling_contact_list = dbHelper.get_users_all_contactTable();
                        setAdapter(get_calling_contact_list);


                    } catch (Exception ignored) {
                    }

                }

            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideDownContainer();
                }
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
                        //  get_call_log_1ChildList = dbHelper.get_voice_call_logTable();


                        if (get_call_log_1ChildList.size() > 0) {

                            binding.recyclerviewLast.setVisibility(View.VISIBLE);

                            setAdapterLog(get_call_log_1ChildList);
                        } else {

                            binding.recyclerviewLast.setVisibility(View.GONE);

                        }


                    } catch (Exception ignored) {
                    }

                    // todo is connected call only one api here frgment
                    Log.d("Network", "connected: " + "callFragment");
                    try {

                        Constant.getSfFuncion(mContext);
                        uid = Constant.getSF.getString(Constant.UID_KEY, "");
                      //  Webservice.get_voice_call_log_for_voice_call(mContext, uid, callFragment.this, binding.recyclerviewLast, binding.progressBar, binding.noData);


                    } catch (Exception ignored) {
                    }
                } else {

                    if (binding.searchData.getVisibility() == View.VISIBLE) {
                        Animation animation3 = AnimationUtils.loadAnimation(mContext, R.anim.fadeoutthreethnd);
                        binding.searchData.setAnimation(animation3);
                        binding.searchData.setVisibility(View.GONE);

                    }


                    binding.menu.setVisibility(View.VISIBLE);

                    binding.contact.setBackgroundResource(R.drawable.radius_6dp_transp);
                    binding.log.setBackgroundResource(R.drawable.radius_black_6dp);
                    binding.contact.setTextColor(Color.parseColor("#000000"));
                    binding.log.setTextColor(Color.parseColor("#ffffff"));


                    try {

                        Log.d("Network", "dissconnetced: " + "callFragment");
                        get_call_log_1ChildList.clear();
                        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                        //    get_call_log_1ChildList = dbHelper.get_voice_call_logTable();
                        setAdapterLog(get_call_log_1ChildList);

                        binding.recyclerviewLast.setVisibility(View.VISIBLE);


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
                           // Webservice.clear_voice_calling_list(mContext, uid, callFragment.this, binding.noData, "1");
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
                         //   Webservice.clear_voice_calling_list(mContext, uid, callFragment.this, binding.noData, "1");
                            dialogueLayoutCLearLogBottom.dismiss();
                        }
                    });

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
                Constant.call_log_history_list.clear();
                call_history_adapter_for_voice.notifyDataSetChanged();
                binding.contact.setBackgroundResource(R.drawable.radius_6dp_transp);
                binding.log.setBackgroundResource(R.drawable.radius_black_6dp);
                binding.contact.setTextColor(Color.parseColor("#000000"));
                binding.log.setTextColor(Color.parseColor("#ffffff"));
                binding.recyclerviewLast.setVisibility(View.VISIBLE);
                binding.recyclerviewAZ.setVisibility(View.GONE);
                binding.log.performClick();
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


                } else if (binding.searchLytNew.getVisibility() == View.GONE) {
                    binding.search.requestFocus();
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_four);
                    binding.searchLytNew.setAnimation(animation);
                    binding.searchLytNew.setVisibility(View.VISIBLE);


                    if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                        MainActivityOld.slideUpContainer();
                    }

                    binding.search.requestFocus();


                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }

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

        // Set the filtered list
        this.get_calling_contact_list = filteredList;

        // Set up adapter and RecyclerView
        adapter = new get_voice_calling_adapter(
                mContext,
                binding.bottomcaller,
                binding.mainname,
                binding.phone,
                binding.photo,
                binding.ftoken,
                binding.callerId,
                this.get_calling_contact_list,
                binding.deviceType,
                binding.call
        );

        layoutManager = new LinearLayoutManager(mContext);
        binding.recyclerviewAZ.setLayoutManager(layoutManager);
        binding.recyclerviewAZ.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // Not necessary if using ListAdapter
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

        calllogParentAdapterVoice = new calllogParentAdapterVoice(
                mContext,
                this.get_call_log_1ChildList,
                binding.bottomcaller,
                binding.mainname,
                binding.phone,
                binding.photo,
                binding.ftoken,
                binding.callerId,
                null,
                binding.deviceType,
                binding.call
        );

        layoutManager = new LinearLayoutManager(mContext);
        binding.recyclerviewLast.setLayoutManager(layoutManager);
        binding.recyclerviewLast.setAdapter(calllogParentAdapterVoice);
        calllogParentAdapterVoice.notifyDataSetChanged();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.recyclerviewLast.getLayoutParams();
        int topMarginInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics());
        layoutParams.topMargin = topMarginInPx;
        binding.recyclerviewLast.setLayoutParams(layoutParams);
    }



//    public void setAdapterLog(ArrayList<get_call_log_1Child> model) {
//        this.get_call_log_1ChildList = model;
//        calllogParentAdapterVoice = new calllogParentAdapterVoice(mContext, this.get_call_log_1ChildList, binding.bottomcaller, binding.mainname, binding.phone, binding.photo, binding.ftoken, binding.callerId, callFragment.this, binding.deviceType, binding.call);
//        layoutManager = new LinearLayoutManager(mContext);
//        binding.recyclerviewLast.setLayoutManager(layoutManager);
//        binding.recyclerviewLast.setAdapter(calllogParentAdapterVoice);
//        calllogParentAdapterVoice.notifyDataSetChanged();
//
//        //  binding.recyclerviewLast.performClick();
//    }


    public void setCallHistoryAdapter(ArrayList<call_log_history_model> call_log_history_lists) {
        this.call_log_history_list = call_log_history_lists;
        call_history_adapter_for_voice = new call_history_adapter_for_voice(mContext, this.call_log_history_list);
        binding.recyclerviewLast.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerviewLast.setAdapter(call_history_adapter_for_voice);
        call_history_adapter_for_voice.notifyDataSetChanged();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.recyclerviewLast.getLayoutParams();
        int topMarginInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 40, mContext.getResources().getDisplayMetrics());
        layoutParams.topMargin = topMarginInPx;
        binding.recyclerviewLast.setLayoutParams(layoutParams);


    }


    private void filteredList(String newText) {
        ArrayList<get_contact_model> filteredList = new ArrayList<>();

        for (get_contact_model list : get_calling_contact_list) {
            if (list.getFull_name().toLowerCase().contains(newText.toLowerCase())) {

                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {
            // Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            try {
                adapter.searchFilteredData(filteredList);
            } catch (Exception e) {

            }
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


    // Method to check internet connectivity
    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(connectivityReceiver);
    }





}