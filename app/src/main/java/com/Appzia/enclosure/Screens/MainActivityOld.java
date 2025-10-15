package com.Appzia.enclosure.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Environment;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.get_calling_parent_adapter;
import com.Appzia.enclosure.Fragments.ChattingRoom;
import com.Appzia.enclosure.Fragments.callFragment;
import com.Appzia.enclosure.Fragments.groupMsgFragment;
import com.Appzia.enclosure.Fragments.msgLimitFragment;
import com.Appzia.enclosure.Fragments.videoCallFragment;
import com.Appzia.enclosure.Fragments.youFragment;
import com.Appzia.enclosure.Model.contactUploadModel;
import com.Appzia.enclosure.Model.contact_parent_model;
import com.Appzia.enclosure.R;

import com.Appzia.enclosure.SubScreens.CallUtil;
import com.Appzia.enclosure.SubScreens.ChattingRoomUtils;
import com.Appzia.enclosure.SubScreens.GroupMsgUtils;
import com.Appzia.enclosure.SubScreens.MsgLimitUtils;
import com.Appzia.enclosure.SubScreens.VideoCallUtil;
import com.Appzia.enclosure.SubScreens.YouUtils;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.MainApplication;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.Utils.WebserviceRetrofits.APIClient;
import com.Appzia.enclosure.databinding.ActivityCreateProfileBinding;

import com.Appzia.enclosure.keyboardheight.KeyboardHeightObserver;
import com.Appzia.enclosure.keyboardheight.KeyboardHeightProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
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
import java.util.regex.Pattern;

public class MainActivityOld extends AppCompatActivity implements ConnectivityReceiver.ConnectivityListener, KeyboardHeightObserver {
    ActivityCreateProfileBinding binding;
    private CardView customToastCard;
    private TextView customToastText;
    private static final int REQUEST_CODE = 2345;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    public static Dialog dialogLayoutColor;
    private boolean isReceiverRegistered = false;
    get_calling_parent_adapter get_calling_parent_adapter;
    public static TextInputEditText name;
    ArrayList<contactUploadModel> contactList = new ArrayList<>();
    public static ArrayList<contact_parent_model> contact_parent_model_List = new ArrayList<>();
    static LinearLayout startshere;
    String TAG = "Enclosure";
    String BASE_URL = Webservice.BASE_URL;
    String GET_USERS_ALL_CONTACT = BASE_URL + "get_users_all_contact";
    String country_codeKey;
    public static TextView sliderealtime;
    public static String dataNew;
    public static String globalNumber;
    public static LinearLayout imgArrow;
    public static final int RESULT_ENABLE = 11;
    ProgressBar progressBar;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String[] PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
    public static int golbalDecinal;
    public static int finalDuration;
    public static int pixels;
    public static Activity activity;
    public static Context mContext;
    public static Window window;

    String lockKey, lockKeyIntent, lockSuccess;

    Dialog upper_layout;
    String themColor;
    ColorStateList tintList;
    public static ViewGroup.LayoutParams params;
    public static androidx.appcompat.widget.LinearLayoutCompat container, linearMain, linearMain2;

    public static ImageView imgArrow2;

    public static View view;


    public static LinearLayout logoandmenu, bacMenu, logoandsearch;

    public static TextView search;

    public static LinearLayout searchLyt;


    float[] lastEvent = null;
    float d = 0f;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading = false;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();

    AudioManager audioManager;
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;

    public static String fontSizePref;
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    private int requestCode = 101010;
    String uid;

    String GRPKEY;

    boolean isTouched, isTouched2;


    FirebaseDatabase database, database2, testDatabase;

    public static LinearLayout searchIcon;

    private static final int REQUEST_PERMISSION = 123;
    private ConnectivityReceiver connectivityReceiver;
    private FirebaseAuth mAuth;
    String declineVideoKey, declineVoiceKey, roomVideo, roomVoice;

    private KeyboardHeightProvider keyboardHeightProvider;
    LinearLayout SponswerRect;
    LinearLayout lockScreenRect;
    LinearLayout ThemeRect;
    LinearLayout PayRect;
    LinearLayout SettingsRect;
    LinearLayout sizeRect;
    TextView sponswerTxt;
    TextView lockscreenTxt;
    TextView ThemeTxt;
    TextView sizeTxt;
    TextView PayTxt;
    TextView SettingsTxt;
    AppCompatSeekBar sb;

    private static final int REQUEST_CODE_UPDATE = 123;
    private AppUpdateManager appUpdateManager;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Important: Update the intent reference
        handleIntentData(intent);
    }

    private void handleIntentData(Intent intent) {
        String voiceCallKey = intent.getStringExtra("voiceCallKey");
        String videoCallKey = intent.getStringExtra("videoCallKey");
        Log.d(TAG, "handleIntentData voiceCallKey: " + voiceCallKey + videoCallKey);
        Log.d(TAG, "handleIntentData voiceCallKey: " + voiceCallKey);


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reset menu state to clear any highlights from previous navigation
        resetMenuState();

        View roomRoot = binding.chattingRoomFragment.getRoot();
        if (roomRoot.getVisibility() == View.VISIBLE) {
            ChattingRoomUtils utils = ChattingRoomUtils.from(roomRoot);
            if (utils != null) {
                Log.d(TAG, "onResumevqwbciwbicqw: ");
                utils.refreshActiveChatList();
            }
        }
        
        // Refresh group list when returning from group chat
        GroupMsgUtils.refreshGroupListIfExists();


        if(binding.container.getVisibility()==View.GONE){
            params = binding.linearMain.getLayoutParams();
            params.height = 350;
            binding.linearMain.setLayoutParams(params);
            binding.linearMain.setBackgroundResource(R.drawable.bg);
            binding.chattingRoomFragment.getRoot().setVisibility(View.VISIBLE);
            binding.callFragment.getRoot().setVisibility(View.GONE);
            binding.msgLimitFragment.getRoot().setVisibility(View.GONE);
            binding.groupFragment.getRoot().setVisibility(View.GONE);
            binding.youFragment.getRoot().setVisibility(View.GONE);
            binding.videoCallFragment.getRoot().setVisibility(View.GONE);

        }



        handleIntentData(getIntent()); // Handle it if it's the first launch


        String sentfromExternal = getIntent().getStringExtra("sentfromExternal");
        if (sentfromExternal != null) {
            String receiverUidsentfromExternal = getIntent().getStringExtra("receiverUidsentfromExternal");
            Constant.getSfFuncion(mContext);
            Webservice.get_individual_chattingUnion(Constant.getSF.getString(Constant.UID_KEY, ""), receiverUidsentfromExternal);
        }


        if (Build.VERSION.SDK_INT >= 35) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.logoandsearch.getLayoutParams();
            int topMarginInDp = 50;
            float scale = binding.getRoot().getContext().getResources().getDisplayMetrics().density;
            params.topMargin = (int) (topMarginInDp * scale);
            binding.logoandsearch.setLayoutParams(params);
        } else {

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.logoandsearch.getLayoutParams();
            int topMarginInDp = 20;
            float scale = binding.getRoot().getContext().getResources().getDisplayMetrics().density;
            params.topMargin = (int) (topMarginInDp * scale);
            binding.logoandsearch.setLayoutParams(params);
        }


        keyboardHeightProvider.setKeyboardHeightObserver(this);
        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            binding.bacMenu.setBackgroundResource(R.color.black);
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
                    binding.linearMain.setBackgroundTintList(tintList);
                } else if (themColor.equals("#00A3E9")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                    binding.linearMain.setBackgroundTintList(tintList);
                } else if (themColor.equals("#7adf2a")) {

//                    sb.setThumb(getResources().getDrawable(R.drawable.popatisleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                    binding.linearMain.setBackgroundTintList(tintList);

                } else if (themColor.equals("#ec0001")) {

//                    sb.setThumb(getResources().getDrawable(R.drawable.redonesleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                    binding.linearMain.setBackgroundTintList(tintList);

                } else if (themColor.equals("#16f3ff")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.bluesleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                    binding.linearMain.setBackgroundTintList(tintList);
                } else if (themColor.equals("#FF8A00")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.orangesleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                    binding.linearMain.setBackgroundTintList(tintList);
                } else if (themColor.equals("#7F7F7F")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.graysleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                    binding.linearMain.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {
                    //       sb.setThumb(getResources().getDrawable(R.drawable.yellowsleep));

                    tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                    binding.linearMain.setBackgroundTintList(tintList);
                } else if (themColor.equals("#346667")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.greensleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                    binding.linearMain.setBackgroundTintList(tintList);
                } else if (themColor.equals("#9846D9")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.voiletsleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                    binding.linearMain.setBackgroundTintList(tintList);
                } else if (themColor.equals("#A81010")) {
//                    sb.setThumb(getResources().getDrawable(R.drawable.redtwonewsleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                    binding.linearMain.setBackgroundTintList(tintList);
                } else {
//                    sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                    binding.linearMain.setBackgroundTintList(tintList);
                }
            } catch (Exception ignored) {
//                sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                binding.linearMain.setBackgroundTintList(tintList);
            }


        } else {
            binding.bacMenu.setBackgroundResource(R.color.white);

            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

                // Set dark status bar (black text/icons) for light mode
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            // Your existing color logic for light mode

            Log.d(TAG, "onResume**: " + "light mode");
            tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
            binding.linearMain.setBackgroundTintList(tintList);

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {  // Android 12 and above
            if (checkSelfPermission(Manifest.permission.MANAGE_OWN_CALLS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.MANAGE_OWN_CALLS}, REQUEST_CODE);
            }
        }


        // todo for video
        try {
            declineVideoKey = getIntent().getStringExtra("declineVideoKey");
            roomVideo = getIntent().getStringExtra("roomVideo");


            database = FirebaseDatabase.getInstance();


            if (declineVideoKey != null) {
                String deleteroom = uid + roomVideo;

                if (declineVideoKey.equals("declineVideoKey")) {

                    String pushKey = database.getReference().child("declineVideoKey").child(deleteroom).push().getKey();
                    database.getReference().child("declineVideoKey").child(deleteroom).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent2 = getIntent();
                            if (intent2.hasExtra("declineVideoKey")) {
                                intent2.removeExtra("declineVideoKey");
                            }
                        }
                    });

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // todo for voice
        try {
            declineVoiceKey = getIntent().getStringExtra("declineVoiceKey");
            roomVoice = getIntent().getStringExtra("roomVoice");
            String senderId = getIntent().getStringExtra("senderId");
            String receiverId = getIntent().getStringExtra("receiverId");
            //     Toast.makeText(mContext, senderId+receiverId, Toast.LENGTH_SHORT).show();
            database = FirebaseDatabase.getInstance();


            if (declineVoiceKey != null) {
                String deleteroom = uid + roomVoice;

                if (declineVoiceKey.equals("declineVoiceKey")) {

                    String pushKey = database.getReference().child("declineVoiceKey").child(deleteroom).push().getKey();
                    database.getReference().child("declineVoiceKey").child(deleteroom).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent2 = getIntent();
                            if (intent2.hasExtra("declineVoiceKey")) {
                                intent2.removeExtra("declineVoiceKey");
                            }
                        }
                    });


                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            binding.menuPoint.setColorFilter(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);

                    binding.switchcall.setTrackResource(R.drawable.bg_track_pink);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_pink);
                    sb.setThumb(getResources().getDrawable(R.drawable.pinksleep));
                    binding.logoBtn.setImageResource(R.drawable.pinklogopng);
                    progressBar.setIndeterminateTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FF6D00"));
                } else if (themColor.equals("#00A3E9")) {
                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track);
                    sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                    binding.logoBtn.setImageResource(R.drawable.ec_modern);
                    progressBar.setIndeterminateTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                } else if (themColor.equals("#7adf2a")) {

                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track_popati);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_popati);
                    sb.setThumb(getResources().getDrawable(R.drawable.popatisleep));
                    binding.logoBtn.setImageResource(R.drawable.popatilogopng);
                    progressBar.setIndeterminateTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00C853"));

                } else if (themColor.equals("#ec0001")) {

                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track_redone);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_redone);
                    sb.setThumb(getResources().getDrawable(R.drawable.redonesleep));
                    binding.logoBtn.setImageResource(R.drawable.redlogopng);
                    progressBar.setIndeterminateTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#ec7500"));

                } else if (themColor.equals("#16f3ff")) {
                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track_blue);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_blue);
                    sb.setThumb(getResources().getDrawable(R.drawable.bluesleep));
                    binding.logoBtn.setImageResource(R.drawable.bluelogopng);
                    progressBar.setIndeterminateTintList(tintList);

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00F365"));

                } else if (themColor.equals("#FF8A00")) {
                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track_orange);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_orange);
                    sb.setThumb(getResources().getDrawable(R.drawable.orangesleep));
                    binding.logoBtn.setImageResource(R.drawable.orangelogopng);
                    progressBar.setIndeterminateTintList(tintList);

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FFAB00"));

                } else if (themColor.equals("#7F7F7F")) {
                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track_gray);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_gray);
                    sb.setThumb(getResources().getDrawable(R.drawable.graysleep));
                    binding.logoBtn.setImageResource(R.drawable.graylogopng);
                    progressBar.setIndeterminateTintList(tintList);


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#314E6D"));

                } else if (themColor.equals("#D9B845")) {
                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track_yelloe);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_yelloe);
                    sb.setThumb(getResources().getDrawable(R.drawable.yellowsleep));
                    binding.logoBtn.setImageResource(R.drawable.yellowlogopng);
                    progressBar.setIndeterminateTintList(tintList);

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#b0d945"));
                } else if (themColor.equals("#346667")) {
                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track_green);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_green);
                    sb.setThumb(getResources().getDrawable(R.drawable.greensleep));
                    binding.logoBtn.setImageResource(R.drawable.greenlogoppng);
                    progressBar.setIndeterminateTintList(tintList);

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#729412"));

                } else if (themColor.equals("#9846D9")) {
                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track_voilet);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_voilet);
                    sb.setThumb(getResources().getDrawable(R.drawable.voiletsleep));
                    binding.logoBtn.setImageResource(R.drawable.voiletlogopng);
                    progressBar.setIndeterminateTintList(tintList);

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#d946d1"));

                } else if (themColor.equals("#A81010")) {
                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track_redtwo);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_redtwo);
                    sb.setThumb(getResources().getDrawable(R.drawable.redtwonewsleep));
                    binding.logoBtn.setImageResource(R.drawable.red2logopng);
                    progressBar.setIndeterminateTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#D85D01"));

                } else {
                    binding.pollyy.setBackgroundTintList(tintList);
                    binding.view.setBackgroundTintList(tintList);
                    binding.switchcall.setTrackResource(R.drawable.bg_track_redtwo);
                    binding.switchVideocall.setTrackResource(R.drawable.bg_track_redtwo);
                    sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                    binding.logoBtn.setImageResource(R.drawable.ec_modern);
                    progressBar.setIndeterminateTintList(tintList);

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                }
            } catch (Exception ignored) {
                binding.pollyy.setBackgroundTintList(tintList);
                binding.view.setBackgroundTintList(tintList);
                binding.switchcall.setTrackResource(R.drawable.bg_track);
                binding.switchVideocall.setTrackResource(R.drawable.bg_track);
                sb.setThumb(getResources().getDrawable(R.drawable.sleep));
                binding.logoBtn.setImageResource(R.drawable.ec_modern);
                progressBar.setIndeterminateTintList(tintList);
                binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
            }


        } catch (Exception ignored) {
            binding.pollyy.setBackgroundTintList(tintList);
            binding.view.setBackgroundTintList(tintList);
            binding.switchcall.setTrackResource(R.drawable.bg_track);
            binding.switchVideocall.setTrackResource(R.drawable.bg_track);
            sb.setThumb(getResources().getDrawable(R.drawable.sleep));
            binding.logoBtn.setImageResource(R.drawable.ec_modern);
            progressBar.setIndeterminateTintList(tintList);
            binding.networkLoader.setTrackColor(Color.parseColor(themColor));
            binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
        }








        Constant.getSfFuncion(this);
        String voiceRadioKey = Constant.getSF.getString(Constant.voiceRadioKey, "turnedOn");
        String videoRadioKey = Constant.getSF.getString(Constant.videoRadioKey, "turnedOn");
        if (voiceRadioKey.equals(Constant.voiceRadioKey)) {

            binding.switchcall.setChecked(true);
            binding.audiotext.setTextColor(Color.parseColor("#ffffff"));

        } else if (voiceRadioKey.equals("turnedOn")) {

            binding.switchcall.setChecked(true);
            binding.audiotext.setTextColor(Color.parseColor("#ffffff"));
        } else {
            binding.switchcall.setChecked(false);
            binding.audiotext.setTextColor(Color.parseColor("#9EA6B9"));
            // Toast.makeText(activity, "turned off", Toast.LENGTH_SHORT).show();


        }
        if (videoRadioKey.equals(Constant.videoRadioKey)) {
            binding.switchVideocall.setChecked(true);
            binding.videocallText.setTextColor(Color.parseColor("#ffffff"));
        } else if (videoRadioKey.equals("turnedOn")) {
            binding.switchVideocall.setChecked(true);
            binding.videocallText.setTextColor(Color.parseColor("#ffffff"));
        } else {
            binding.switchVideocall.setChecked(false);
            binding.videocallText.setTextColor(Color.parseColor("#9EA6B9"));
        }



        Animation fadeout = AnimationUtils.loadAnimation(MainActivityOld.this, R.anim.fade_out_zero);


        //todo create anonymous login firebase

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // After reload, check if UID exists
                    if (currentUser.getUid() != null) {
                        // User is signed in and likely valid (handle as needed)
                        Log.d("Anonymous", "User already signed in with UID: " + currentUser.getUid());
                        // You may update UI or proceed accordingly if user is already signed in
                    } else {
                        // If UID doesn't exist after reload, sign in anonymously
                        signInAnonymously();
                    }
                } else {
                    // Handle reload failure (could indicate network issues)
                    Log.w("Anonymous", "USER DELETED .");
                    Log.w("Anonymous", "Failed to reload user data .", task.getException());
                    signInAnonymously();
                }
            });
        } else {
            // User is not signed in
            // Handle this case if needed
            signInAnonymously();

        }







    }


    @Override
    public void onStart() {
        super.onStart();


//        try {
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.cancel(100);
//
//        } catch (Exception ignored) {
//        }

        try {
            Constant.setSfFunction(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
            Webservice.get_profile_MainActivity(mContext, uid);


        } catch (Exception ignored) {
        }


        try {
            Constant.getSfFuncion(getApplicationContext());
            uid = Constant.getSF.getString(Constant.UID_KEY, "");


        } catch (Exception ignored) {
        }


        logoandmenu = findViewById(R.id.logoandmenu);
        logoandsearch = findViewById(R.id.logoandsearch);
        linearMain2 = findViewById(R.id.linearMain2);
        search = findViewById(R.id.search);
        bacMenu = findViewById(R.id.bacMenu);
        imgArrow = findViewById(R.id.imgArrow);


        params = binding.linearMain.getLayoutParams();
        container = findViewById(R.id.container);
        imgArrow2 = findViewById(R.id.imgArrow2);
        linearMain = findViewById(R.id.linearMain);




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup swipe gestures for back navigation
        SwipeNavigationHelper.setupSwipeGestures(this);

        mContext = binding.getRoot().getContext();
        customToastCard = findViewById(R.id.includedToast);
        customToastText = customToastCard.findViewById(R.id.toastText);


        params = binding.linearMain.getLayoutParams();
        binding.container.setVisibility(View.GONE);
        params.height = 350;
        binding.linearMain.setLayoutParams(params);

        binding.linearMain.setBackgroundResource(R.drawable.bg);

        binding.chattingRoomFragment.getRoot().setVisibility(View.VISIBLE);
        binding.callFragment.getRoot().setVisibility(View.GONE);
        binding.msgLimitFragment.getRoot().setVisibility(View.GONE);
        binding.groupFragment.getRoot().setVisibility(View.GONE);
        binding.youFragment.getRoot().setVisibility(View.GONE);
        binding.videoCallFragment.getRoot().setVisibility(View.GONE);



        ChattingRoomUtils.setupChattingRoomCode(binding.chattingRoomFragment.getRoot());





        // ✅ App Update Manager सुरू करा
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // ✅ Update उपलब्ध आहे का हे तपासा
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // ✅ Update उपलब्ध आहे – लगेच update सुरू करा
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            REQUEST_CODE_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");


        keyboardHeightProvider = new KeyboardHeightProvider(this);

        binding.getRoot().post(new Runnable() {
            public void run() {
                keyboardHeightProvider.start();
            }
        });


        if (MainApplication.isFirstLaunch) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkAndRequestPermissions();
            } else {
                checkAndRequestPermissions2();
            }

            requestContactPermission(); // your contact permission
            MainApplication.isFirstLaunch = false;

        }


        String sleepKeyCheckOFF = getIntent().getStringExtra("sleepKeyCheckOFF");
        if (sleepKeyCheckOFF != null) {
            Constant.showCustomToast("Sleep Mode - OFF", customToastCard, customToastText);

        }


        String sentfromExternal = getIntent().getStringExtra("sentfromExternal");
        if (sentfromExternal != null) {
            String receiverUidsentfromExternal = getIntent().getStringExtra("receiverUidsentfromExternal");
            Constant.getSfFuncion(mContext);
            Webservice.get_individual_chattingUnion(Constant.getSF.getString(Constant.UID_KEY, ""), receiverUidsentfromExternal);
        }

        askPermissions();
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("stop_ringtone", false)) {
            stopRingtone();
        }

        database = FirebaseDatabase.getInstance();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            checkAndRequestPermissions();
            requestNotificationPermission();
        } else {

            checkAndRequestPermissions2();
            requestNotificationPermission();
        }


        mAuth = FirebaseAuth.getInstance();
        searchIcon = findViewById(R.id.searchIcon);
        searchLyt = findViewById(R.id.searchLyt);
        sliderealtime = findViewById(R.id.sliderealtime);


        database2 = FirebaseDatabase.getInstance();
        activity = MainActivityOld.this;

        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, mContext.RECEIVER_EXPORTED);
        isReceiverRegistered = true;

        // TODO to check network is connected ot not


        // create all folders   VIMP


        /*-----------------------------------------------------*/


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        } else {
        }

        File customFolder;
        //android 10
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ///       Toast.makeText(mContext, "latest", Toast.LENGTH_SHORT).show();
            // android 13,11,9
            // not working android 12,10
            customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
            String exactPath = customFolder.getAbsolutePath();
            Log.d("TAG", "exactPath: " + exactPath);
            //   Toast.makeText(mContext, exactPath, Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(mContext, "old", Toast.LENGTH_SHORT).show();
            customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");

        }
        if (!customFolder.exists()) {
            customFolder.mkdirs();
        }


        File customFolder2;
        //android 10
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ///       Toast.makeText(mContext, "latest", Toast.LENGTH_SHORT).show();
            // android 13,11,9
            // not working android 12,10
            customFolder2 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
            String exactPath = customFolder2.getAbsolutePath();
            Log.d("TAG", "exactPath: " + exactPath);
            //   Toast.makeText(mContext, exactPath, Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(mContext, "old", Toast.LENGTH_SHORT).show();
            customFolder2 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");

        }
        if (!customFolder2.exists()) {
            customFolder2.mkdirs();
        }


        File customFolder3;
        //android 10
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ///       Toast.makeText(mContext, "latest", Toast.LENGTH_SHORT).show();
            // android 13,11,9
            // not working android 12,10
            customFolder3 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
            String exactPath = customFolder3.getAbsolutePath();
            Log.d("TAG", "exactPath: " + exactPath);
            //   Toast.makeText(mContext, exactPath, Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(mContext, "old", Toast.LENGTH_SHORT).show();
            customFolder3 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");

        }
        if (!customFolder3.exists()) {
            customFolder3.mkdirs();
        }


        File customFolder4;
        //android 10
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ///       Toast.makeText(mContext, "latest", Toast.LENGTH_SHORT).show();
            // android 13,11,9
            // not working android 12,10
            customFolder4 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
            String exactPath = customFolder4.getAbsolutePath();
            Log.d("TAG", "exactPath: " + exactPath);
            //   Toast.makeText(mContext, exactPath, Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(mContext, "old", Toast.LENGTH_SHORT).show();
            customFolder4 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");

        }
        if (!customFolder4.exists()) {
            customFolder4.mkdirs();
        }





        /*-----------------------------------------------------*/


        /// app is crashed then auto make AVAILABEL for call


        view = findViewById(android.R.id.content);


        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        pixels = (int) (380 * scale + 0.5f);


        try {
            Constant.getSfFuncion(getApplicationContext());
            String nameSAved = Constant.getSF.getString("nameSAved", " 0");

            if (nameSAved.equals("nameSAved")) {

            } else {

                //for displaying first name
                dialogLayoutColor = new Dialog(MainActivityOld.this);
                dialogLayoutColor.setContentView(R.layout.whatname_row);
                dialogLayoutColor.setCanceledOnTouchOutside(true);
                dialogLayoutColor.setCancelable(false);
                dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

                //for margine to dialogue
                dialogLayoutColor.getWindow().setGravity(Gravity.CENTER);
                WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();

                dialogLayoutColor.getWindow().setAttributes(layoutParams);


                AppCompatButton submit = dialogLayoutColor.findViewById(R.id.submit);
                name = dialogLayoutColor.findViewById(R.id.nameEdit);


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogLayoutColor.dismiss();

                        if (Objects.requireNonNull(name.getText()).toString().equals("")) {
                            Drawable customErrorDrawable = getResources().getDrawable(R.drawable.circlewarn);
                            customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                            name.setError("Missing name ?", customErrorDrawable);
                            name.requestFocus();
                        } else {

                            Webservice.create_name(mContext, uid, name.getText().toString());

                            Constant.setSfFunction(mContext);
                            Constant.setSF.putString(Constant.videoRadioKey, Constant.videoRadioKey);
                            Constant.setSF.putString(Constant.voiceRadioKey, Constant.voiceRadioKey);
                            Constant.setSF.apply();


                        }
                    }
                });


                dialogLayoutColor.show();

                Window window = dialogLayoutColor.getWindow();
                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }


        } catch (Exception ignored) {


        }


        binding.searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                ChattingRoom.filteredList(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.imgArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                try {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    View view = activity.getCurrentFocus();
                    if (view == null) {
                        view = new View(activity);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } catch (Exception e) {
                }


                Intent intent = getIntent();
                if (intent.hasExtra("grpKey")) {
                    intent.removeExtra("grpKey");
                }


                Intent intent2 = getIntent();
                if (intent2.hasExtra("youKey")) {
                    intent2.removeExtra("youKey");
                }

                APIClient.cancelAllRequests();
                Webservice.cancelAllRequests();

                ViewGroup.LayoutParams params = binding.linearMain.getLayoutParams();
                params.height = 250;
                if (binding.container.getVisibility() == View.GONE) {


                    int topMarginInDp = 50; // Margin in dp
                    int topMarginInPx = (int) (topMarginInDp * binding.getRoot().getContext().getResources().getDisplayMetrics().density);

                    LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) binding.downArrow.getLayoutParams();
                    params5.setMargins(0, topMarginInPx, 0, 0); // left, top, right, bottom (in pixels)
                    binding.downArrow.setLayoutParams(params5);


                    if (binding.logoandmenu.getVisibility() == View.VISIBLE) {
                        //    binding.logoandmenu.setVisibility(View.GONE);
                        collapse(binding.logoandmenu);
                    }


                    binding.linearMain.setBackgroundResource(R.drawable.mainvector);
                    binding.imgArrow2.setImageResource(R.drawable.downarrowslide);

                    binding.container.setVisibility(View.VISIBLE);

                    ValueAnimator valueAnimator = ValueAnimator.ofInt(binding.linearMain.getHeight(), pixels);
                    valueAnimator.setDuration(400);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            binding.linearMain.getLayoutParams().height = (int) animation.getAnimatedValue();
                            binding.linearMain.requestLayout();
                        }
                    });
                    valueAnimator.start();
                    //   binding.linearMain.startAnimation(animation);


                    params.height = pixels;
                    binding.linearMain.setLayoutParams(params);

                    binding.CallRect.setBackgroundResource(R.drawable.bg_rect);
                    binding.CallRect.setBackgroundTintList(tintList);
                    binding.callTxt.setTextColor(Color.parseColor("#ffffff"));


                    binding.msgLmtRect.setBackgroundResource(0);
                    binding.grpMsgRect.setBackgroundResource(0);
                    binding.youRect.setBackgroundResource(0);
                    binding.videoCallRect.setBackgroundResource(0);
                    //binding.CallRect.setBackgroundResource(0);


                    //set text color to gray


                    binding.msglmtTxt.setTextColor(Color.parseColor("#9EA6B9"));
                    binding.grpmsgTxt.setTextColor(Color.parseColor("#9EA6B9"));
                    binding.youTxt.setTextColor(Color.parseColor("#9EA6B9"));
                    binding.videoTxt.setTextColor(Color.parseColor("#9EA6B9"));


                    if (binding.searchIcon.getVisibility() == View.VISIBLE) {
                        Animation animation3 = AnimationUtils.loadAnimation(mContext, R.anim.fadeoutthreethnd);
                        binding.searchIcon.setAnimation(animation3);
                        binding.searchIcon.setVisibility(View.INVISIBLE);

                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fadeoutthreethnd);
                        binding.searchLyt.setAnimation(anim);
                        binding.searchLyt.setVisibility(View.GONE);
                    }
                    valueAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            binding.CallRect.performClick();
                        }
                    });


                }
                else if (binding.container.getVisibility() == View.VISIBLE) {

                    int topMarginInDp = 39; // Margin in dp
                    int topMarginInPx = (int) (topMarginInDp * binding.getRoot().getContext().getResources().getDisplayMetrics().density);

                    LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) binding.downArrow.getLayoutParams();
                    params5.setMargins(0, topMarginInPx, 0, 0);
                    binding.downArrow.setLayoutParams(params5);


                    if (binding.logoandmenu.getVisibility() == View.GONE) {
                        //   binding.logoandmenu.setVisibility(View.VISIBLE);
                        expand(binding.logoandmenu);
                    }

                    imgArrow2.setImageResource(R.drawable.downarrowslide);
                    linearMain.setLayoutParams(params);

                    int initialHeight = 700; // The initial height of the view
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(initialHeight, 350);
                    valueAnimator.setDuration(400);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int height = (int) animation.getAnimatedValue();
                            linearMain.getLayoutParams().height = height;
                            linearMain.requestLayout();
                        }
                    });
                    valueAnimator.start();


                    container.setVisibility(View.GONE);

                    params.height = 350;
                    linearMain.setBackgroundResource(R.drawable.bg);


                    if (binding.searchLyt.getVisibility() == View.GONE) {
                        Animation animation3 = AnimationUtils.loadAnimation(mContext, R.anim.fade_inthreethousand);
                        binding.searchIcon.setAnimation(animation3);
                        binding.searchIcon.setVisibility(View.VISIBLE);

                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_inthreethousand);
                        binding.searchLyt.setAnimation(anim);
                        binding.searchLyt.setVisibility(View.GONE);
                    }


                    binding.chattingRoomFragment.getRoot().setVisibility(View.VISIBLE);
                    binding.callFragment.getRoot().setVisibility(View.GONE);
                    binding.msgLimitFragment.getRoot().setVisibility(View.GONE);
                    binding.groupFragment.getRoot().setVisibility(View.GONE);
                    binding.youFragment.getRoot().setVisibility(View.GONE);
                    binding.videoCallFragment.getRoot().setVisibility(View.GONE);


                    ChattingRoomUtils.setupChattingRoomCode(binding.chattingRoomFragment.getRoot());


                }
            }
        });
        binding.youRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                if (intent.hasExtra("grpKey")) {
                    intent.removeExtra("grpKey");
                }


                APIClient.cancelAllRequests();
                Webservice.cancelAllRequests();
                binding.youRect.setBackgroundResource(R.drawable.bg_rect);
                binding.youRect.setBackgroundTintList(tintList);
                binding.youTxt.setTextColor(Color.parseColor("#ffffff"));


                //set backgroud click null
                binding.msgLmtRect.setBackgroundResource(0);
                binding.grpMsgRect.setBackgroundResource(0);
                binding.videoCallRect.setBackgroundResource(0);
                binding.CallRect.setBackgroundResource(0);


                //set text color to gray

                binding.msglmtTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.grpmsgTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.videoTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.callTxt.setTextColor(Color.parseColor("#9EA6B9"));


                binding.youFragment.getRoot().setVisibility(View.VISIBLE);
                binding.callFragment.getRoot().setVisibility(View.GONE);
                binding.chattingRoomFragment.getRoot().setVisibility(View.GONE);
                binding.groupFragment.getRoot().setVisibility(View.GONE);
                binding.msgLimitFragment.getRoot().setVisibility(View.GONE);
                binding.videoCallFragment.getRoot().setVisibility(View.GONE);

                //     getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new youFragment()).commit();

                YouUtils.setupYouCode(binding.youFragment.getRoot());

            }
        });

        binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.searchLyt.getVisibility() == View.VISIBLE) {
                    binding.searchLyt.setVisibility(View.GONE);
                    binding.searchview.setText("");
                    View view = findViewById(android.R.id.content);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } else if (binding.searchLyt.getVisibility() == View.GONE) {
                    binding.searchview.requestFocus();
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_four);
                    binding.searchLyt.setAnimation(animation);
                    binding.searchLyt.setVisibility(View.VISIBLE);

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            binding.searchview.requestFocus();
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            if (inputMethodManager != null) {
                                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


//                    if (binding.container.getVisibility() == View.VISIBLE) {
//
//                        imgArrow2.setImageResource(R.drawable.downarrowslide);
//                        linearMain.setLayoutParams(params);
//
//                        int initialHeight = 700; // The initial height of the view
//                        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialHeight, 350);
//                        valueAnimator.setDuration(600);
//                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation) {
//                                int height = (int) animation.getAnimatedValue();
//                                linearMain.getLayoutParams().height = height;
//                                linearMain.requestLayout();
//                            }
//                        });
//                        valueAnimator.start();
//
//
//                        container.setVisibility(View.GONE);
//
//                        params.height = 350;
//                        linearMain.setBackgroundResource(R.drawable.bg);
//
//
//                        valueAnimator.addListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new ChattingRoom()).commit();
//
//                            }
//                        });
//
//
//                    }

                }

            }
        });


        binding.msgLmtRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                if (intent.hasExtra("grpKey")) {
                    intent.removeExtra("grpKey");
                }


                Intent intent2 = getIntent();
                if (intent2.hasExtra("youKey")) {
                    intent2.removeExtra("youKey");
                }
                APIClient.cancelAllRequests();
                Webservice.cancelAllRequests();

                binding.msgLmtRect.setBackgroundResource(R.drawable.bg_rect);
                binding.msgLmtRect.setBackgroundTintList(tintList);
                binding.msglmtTxt.setTextColor(Color.parseColor("#ffffff"));


                binding.youRect.setBackgroundResource(0);
                binding.grpMsgRect.setBackgroundResource(0);
                binding.videoCallRect.setBackgroundResource(0);
                binding.CallRect.setBackgroundResource(0);


                //set text color to gray
                binding.youTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.grpmsgTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.videoTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.callTxt.setTextColor(Color.parseColor("#9EA6B9"));

                binding.msgLimitFragment.getRoot().setVisibility(View.VISIBLE);
                binding.callFragment.getRoot().setVisibility(View.GONE);
                binding.chattingRoomFragment.getRoot().setVisibility(View.GONE);
                binding.groupFragment.getRoot().setVisibility(View.GONE);
                binding.youFragment.getRoot().setVisibility(View.GONE);
                binding.videoCallFragment.getRoot().setVisibility(View.GONE);

                MsgLimitUtils.setupMsgLimitCode(binding.msgLimitFragment.getRoot());

                //  getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new msgLimitFragment()).commit();

            }
        });
        binding.grpMsgRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent2 = getIntent();
                if (intent2.hasExtra("youKey")) {
                    intent2.removeExtra("youKey");
                }
                APIClient.cancelAllRequests();
                Webservice.cancelAllRequests();

                binding.grpMsgRect.setBackgroundResource(R.drawable.bg_rect);
                binding.grpMsgRect.setBackgroundTintList(tintList);
                binding.grpmsgTxt.setTextColor(Color.parseColor("#ffffff"));


                binding.msgLmtRect.setBackgroundResource(0);
                binding.youRect.setBackgroundResource(0);
                binding.videoCallRect.setBackgroundResource(0);
                binding.CallRect.setBackgroundResource(0);


                //set text color to gray


                binding.msglmtTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.youTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.videoTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.callTxt.setTextColor(Color.parseColor("#9EA6B9"));


                binding.groupFragment.getRoot().setVisibility(View.VISIBLE);
                binding.callFragment.getRoot().setVisibility(View.GONE);
                binding.chattingRoomFragment.getRoot().setVisibility(View.GONE);
                binding.msgLimitFragment.getRoot().setVisibility(View.GONE);
                binding.youFragment.getRoot().setVisibility(View.GONE);
                binding.videoCallFragment.getRoot().setVisibility(View.GONE);
                //       getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new groupMsgFragment()).commit();

                GroupMsgUtils.setupGroupMsgCode(binding.groupFragment.getRoot());
            }
        });
        binding.videoCallRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if (intent.hasExtra("grpKey")) {
                    intent.removeExtra("grpKey");
                }


                Intent intent2 = getIntent();
                if (intent2.hasExtra("youKey")) {
                    intent2.removeExtra("youKey");
                }
                APIClient.cancelAllRequests();
                Webservice.cancelAllRequests();
                binding.videoTxt.setTextColor(Color.parseColor("#ffffff"));
                binding.videoCallRect.setBackgroundResource(R.drawable.bg_rect);
                binding.videoCallRect.setBackgroundTintList(tintList);


                binding.msgLmtRect.setBackgroundResource(0);
                binding.grpMsgRect.setBackgroundResource(0);
                binding.youRect.setBackgroundResource(0);
                binding.CallRect.setBackgroundResource(0);


                //set text color to gray

                binding.msglmtTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.grpmsgTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.youTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.callTxt.setTextColor(Color.parseColor("#9EA6B9"));

                // TODO: 17/08/25 Load callfragment.xml here 
                // start integration from here

                binding.videoCallFragment.getRoot().setVisibility(View.VISIBLE);
                binding.callFragment.getRoot().setVisibility(View.GONE);
                binding.chattingRoomFragment.getRoot().setVisibility(View.GONE);
                binding.msgLimitFragment.getRoot().setVisibility(View.GONE);
                binding.youFragment.getRoot().setVisibility(View.GONE);
                binding.groupFragment.getRoot().setVisibility(View.GONE);

                binding.videoCallFragment.getRoot().findViewById(R.id.label).setVisibility(View.VISIBLE);
                binding.videoCallFragment.getRoot().findViewById(R.id.layoutName).setVisibility(View.GONE);


                VideoCallUtil.setupVideoCallCode(binding.videoCallFragment.getRoot());
                // getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new videoCallFragment()).commit();
            }
        });
        binding.CallRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if (intent.hasExtra("grpKey")) {
                    intent.removeExtra("grpKey");
                }


                Intent intent2 = getIntent();
                if (intent2.hasExtra("youKey")) {
                    intent2.removeExtra("youKey");
                }
                APIClient.cancelAllRequests();
                Webservice.cancelAllRequests();

                binding.CallRect.setBackgroundResource(R.drawable.bg_rect);
                binding.CallRect.setBackgroundTintList(tintList);
                binding.callTxt.setTextColor(Color.parseColor("#ffffff"));


                binding.msgLmtRect.setBackgroundResource(0);
                binding.grpMsgRect.setBackgroundResource(0);
                binding.youRect.setBackgroundResource(0);
                binding.videoCallRect.setBackgroundResource(0);
                //binding.CallRect.setBackgroundResource(0);


                //set text color to gray


                binding.msglmtTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.grpmsgTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.youTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.videoTxt.setTextColor(Color.parseColor("#9EA6B9"));


                binding.callFragment.getRoot().setVisibility(View.VISIBLE);
                binding.msgLimitFragment.getRoot().setVisibility(View.GONE);
                binding.chattingRoomFragment.getRoot().setVisibility(View.GONE);
                binding.groupFragment.getRoot().setVisibility(View.GONE);
                binding.youFragment.getRoot().setVisibility(View.GONE);
                binding.videoCallFragment.getRoot().setVisibility(View.GONE);

                binding.callFragment.getRoot().findViewById(R.id.label).setVisibility(View.VISIBLE);
                binding.callFragment.getRoot().findViewById(R.id.layoutName).setVisibility(View.GONE);

                CallUtil.setupCallCode(binding.callFragment.getRoot());


                // getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new callFragment()).commit();
            }
        });

        binding.logoandmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                binding.msgLmtRect.setBackgroundResource(0);
                binding.grpMsgRect.setBackgroundResource(0);
                binding.youRect.setBackgroundResource(0);
                binding.videoCallRect.setBackgroundResource(0);
                binding.CallRect.setBackgroundResource(0);


                //set text color to gray


                binding.msglmtTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.grpmsgTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.youTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.videoTxt.setTextColor(Color.parseColor("#9EA6B9"));
                binding.callTxt.setTextColor(Color.parseColor("#9EA6B9"));
                SwipeNavigationHelper.startActivityWithSwipe(MainActivityOld.this, new Intent(getApplicationContext(), inviteScreen.class));


            }
        });


//

        Constant.dialogueLayoutUpper(mContext, R.layout.upper_layout);
        upper_layout = Constant.dialogLayoutColor;

        SponswerRect = upper_layout.findViewById(R.id.SponswerRect);
        lockScreenRect = upper_layout.findViewById(R.id.lockScreenRect);
        ThemeRect = upper_layout.findViewById(R.id.ThemeRect);
        PayRect = upper_layout.findViewById(R.id.PayRect);
        SettingsRect = upper_layout.findViewById(R.id.SettingsRect);
        sizeRect = upper_layout.findViewById(R.id.sizeRect);
        sponswerTxt = upper_layout.findViewById(R.id.sponswerTxt);
        lockscreenTxt = upper_layout.findViewById(R.id.lockscreenTxt);
        ThemeTxt = upper_layout.findViewById(R.id.ThemeTxt);
        sizeTxt = upper_layout.findViewById(R.id.sizeTxt);
        PayTxt = upper_layout.findViewById(R.id.PayTxt);
        SettingsTxt = upper_layout.findViewById(R.id.SettingsTxt);
        sb = upper_layout.findViewById(R.id.sb);


        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upper_layout.show();


            }
        });
        SponswerRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SponswerRect.setBackgroundResource(R.drawable.bg_rect);
                sponswerTxt.setTextColor(Color.parseColor("#ffffff"));
                SponswerRect.setBackgroundTintList(tintList);

                //set backgroud click null
                lockScreenRect.setBackgroundResource(0);
                ThemeRect.setBackgroundResource(0);
                sizeRect.setBackgroundResource(0);
                PayRect.setBackgroundResource(0);
                SettingsRect.setBackgroundResource(0);


                //set text color to gray
                lockscreenTxt.setTextColor(Color.parseColor("#9EA6B9"));
                ThemeTxt.setTextColor(Color.parseColor("#9EA6B9"));
                sizeTxt.setTextColor(Color.parseColor("#9EA6B9"));
                PayTxt.setTextColor(Color.parseColor("#9EA6B9"));
                SettingsTxt.setTextColor(Color.parseColor("#9EA6B9"));

                SwipeNavigationHelper.startActivityWithSwipe(MainActivityOld.this, new Intent(getApplicationContext(), sponsred.class));
                //
                upper_layout.dismiss();
                //
            }
        });
        lockScreenRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lockScreenRect.setBackgroundResource(R.drawable.bg_rect);
                lockscreenTxt.setTextColor(Color.parseColor("#ffffff"));
                lockScreenRect.setBackgroundTintList(tintList);

                //set backgroud click null
                SponswerRect.setBackgroundResource(0);
                ThemeRect.setBackgroundResource(0);
                sizeRect.setBackgroundResource(0);
                PayRect.setBackgroundResource(0);
                SettingsRect.setBackgroundResource(0);


                //set text color to gray
                sponswerTxt.setTextColor(Color.parseColor("#9EA6B9"));
                ThemeTxt.setTextColor(Color.parseColor("#9EA6B9"));
                sizeTxt.setTextColor(Color.parseColor("#9EA6B9"));
                PayTxt.setTextColor(Color.parseColor("#9EA6B9"));
                SettingsTxt.setTextColor(Color.parseColor("#9EA6B9"));
                SwipeNavigationHelper.startActivityWithSwipe(MainActivityOld.this, new Intent(getApplicationContext(), lockscreen.class));
                //
                upper_layout.dismiss();

            }
        });
        ThemeRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ThemeRect.setBackgroundResource(R.drawable.bg_rect);
                ThemeTxt.setTextColor(Color.parseColor("#ffffff"));
                ThemeRect.setBackgroundTintList(tintList);

                //set backgroud click null
                SponswerRect.setBackgroundResource(0);
                lockScreenRect.setBackgroundResource(0);
                sizeRect.setBackgroundResource(0);
                PayRect.setBackgroundResource(0);
                SettingsRect.setBackgroundResource(0);
                //set text color to gray
                sponswerTxt.setTextColor(Color.parseColor("#9EA6B9"));
                lockscreenTxt.setTextColor(Color.parseColor("#9EA6B9"));
                sizeTxt.setTextColor(Color.parseColor("#9EA6B9"));
                PayTxt.setTextColor(Color.parseColor("#9EA6B9"));
                SettingsTxt.setTextColor(Color.parseColor("#9EA6B9"));
                SwipeNavigationHelper.startActivityWithSwipe(MainActivityOld.this, new Intent(getApplicationContext(), themeScreen.class));
                upper_layout.dismiss();
            }
        });
        sizeRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sizeRect.setBackgroundResource(R.drawable.bg_rect);
                sizeTxt.setTextColor(Color.parseColor("#ffffff"));
                sizeRect.setBackgroundTintList(tintList);

                //set backgroud click null
                SponswerRect.setBackgroundResource(0);
                lockScreenRect.setBackgroundResource(0);
                ThemeRect.setBackgroundResource(0);
                PayRect.setBackgroundResource(0);
                SettingsRect.setBackgroundResource(0);


                //set text color to gray
                sponswerTxt.setTextColor(Color.parseColor("#9EA6B9"));
                lockscreenTxt.setTextColor(Color.parseColor("#9EA6B9"));
                ThemeTxt.setTextColor(Color.parseColor("#9EA6B9"));
                PayTxt.setTextColor(Color.parseColor("#9EA6B9"));
                SettingsTxt.setTextColor(Color.parseColor("#9EA6B9"));

                SwipeNavigationHelper.startActivityWithSwipe(MainActivityOld.this, new Intent(getApplicationContext(), sizeActivity.class));
                //
                upper_layout.dismiss();

            }
        });
        PayRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PayRect.setBackgroundResource(R.drawable.bg_rect);
                PayTxt.setTextColor(Color.parseColor("#ffffff"));
                PayRect.setBackgroundTintList(tintList);

                //set backgroud click null
                SponswerRect.setBackgroundResource(0);
                lockScreenRect.setBackgroundResource(0);
                ThemeRect.setBackgroundResource(0);
                sizeRect.setBackgroundResource(0);
                SettingsRect.setBackgroundResource(0);


                //set text color to gray
                sponswerTxt.setTextColor(Color.parseColor("#9EA6B9"));
                lockscreenTxt.setTextColor(Color.parseColor("#9EA6B9"));
                ThemeTxt.setTextColor(Color.parseColor("#9EA6B9"));
                sizeTxt.setTextColor(Color.parseColor("#9EA6B9"));
                SettingsTxt.setTextColor(Color.parseColor("#9EA6B9"));
                SwipeNavigationHelper.startActivityWithSwipe(MainActivityOld.this, new Intent(getApplicationContext(), payActivity.class));
                //
                upper_layout.dismiss();

            }
        });
        SettingsRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingsRect.setBackgroundResource(R.drawable.bg_rect);
                SettingsTxt.setTextColor(Color.parseColor("#ffffff"));
                SettingsRect.setBackgroundTintList(tintList);

                //set backgroud click null
                SponswerRect.setBackgroundResource(0);
                lockScreenRect.setBackgroundResource(0);
                ThemeRect.setBackgroundResource(0);
                sizeRect.setBackgroundResource(0);
                PayRect.setBackgroundResource(0);


                //set text color to gray
                sponswerTxt.setTextColor(Color.parseColor("#9EA6B9"));
                lockscreenTxt.setTextColor(Color.parseColor("#9EA6B9"));
                ThemeTxt.setTextColor(Color.parseColor("#9EA6B9"));
                sizeTxt.setTextColor(Color.parseColor("#9EA6B9"));
                PayTxt.setTextColor(Color.parseColor("#9EA6B9"));

                SwipeNavigationHelper.startActivityWithSwipe(MainActivityOld.this, new Intent(getApplicationContext(), settingActivity.class));
                //
                upper_layout.dismiss();

            }
        });
        Dialog dlg = new Dialog(MainActivityOld.this);
        dlg.setContentView(R.layout.sleep_gialogue);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setCancelable(true);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//
//
        //for margine to dialogue
        dlg.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = dlg.getWindow().getAttributes();
// top margin
        dlg.getWindow().setAttributes(layoutParams);
        progressBar = dlg.findViewById(R.id.progress);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                if (progress == 100) {


                    try {


                        Constant.getSfFuncion(getApplicationContext());
                        lockKey = Constant.getSF.getString("lockKey", String.valueOf(0));
                        Log.d("password#$$$$$$$$", lockKey);


                        if (lockKey.equals("0")) {

                            Constant.setSfFunction(mContext);
                            Constant.setSF.putString("lockKey", "360");
                            Constant.setSF.putString("sleepKeyCheckOFF", "on");
                            Constant.setSF.putString(Constant.sleepKey, Constant.sleepKey);
                            Constant.setSF.apply();
                            Webservice.lock_screenDummy(mContext, uid, "1", "360", "", customToastCard, customToastText);
                            Constant.showCustomToast("Sleep Mode - ON", customToastCard, customToastText);


                        } else {
                            Constant.setSfFunction(mContext);
                            Constant.setSF.putString("lockKey", lockKey);
                            Constant.setSF.putString(Constant.sleepKey, Constant.sleepKey);
                            Constant.setSF.putString("sleepKeyCheckOFF", "on");
                            //   Log.d("password", lock_screen_pin);
                            Constant.setSF.apply();

                            Constant.showCustomToast("Sleep Mode - ON", customToastCard, customToastText);
                        }


                        TextView timer = dlg.findViewById(R.id.timer);
                        progressBar.setVisibility(View.VISIBLE);

                        new CountDownTimer(1000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                timer.setText(millisUntilFinished / 1000 + " sec.");
                            }

                            public void onFinish() {

                                finishAndRemoveTask();
                                finishAffinity();

                            }

                        }.start();


                        //   dlg.show();
                        upper_layout.dismiss();

                        Window window = dlg.getWindow();
                        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                    } catch (Exception ex) {

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sb.setProgress(0);

            }
        });


        binding.switchcall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouched = true;
                return false;
            }
        });

        binding.switchcall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isTouched) {
                    isTouched = false;
                    if (isChecked) {


                        Constant.setSfFunction(mContext);
                        Constant.setSF.putString(Constant.voiceRadioKey, Constant.voiceRadioKey);
                        Constant.setSF.apply();
                        binding.audiotext.setTextColor(getResources().getColor(R.color.white));
                        binding.incomingonofftxt.setText("Incoming Calls : ON");
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in2);
                        binding.incomingonoffLyt.setAnimation(animation);
                        binding.incomingonoffLyt.setVisibility(View.VISIBLE);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.fade_outnew);
                                binding.incomingonoffLyt.setAnimation(animation2);
                                binding.incomingonoffLyt.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    } else {


                        Constant.setSfFunction(mContext);
                        Constant.setSF.putString(Constant.voiceRadioKey, "");
                        Constant.setSF.apply();
                        binding.audiotext.setTextColor(Color.parseColor("#9EA6B9"));
                        binding.incomingonofftxt.setText("Incoming Calls : OFF");
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in2);
                        binding.incomingonoffLyt.setAnimation(animation);
                        binding.incomingonoffLyt.setVisibility(View.VISIBLE);

                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.fade_outnew);
                                binding.incomingonoffLyt.setAnimation(animation2);
                                binding.incomingonoffLyt.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
            }
        });

        binding.switchVideocall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouched2 = true;
                return false;
            }
        });

        binding.switchVideocall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isTouched2) {
                    isTouched2 = false;
                    if (isChecked) {

                        Constant.setSfFunction(mContext);
                        Constant.setSF.putString(Constant.videoRadioKey, Constant.videoRadioKey);
                        Constant.setSF.apply();
                        binding.videocallText.setTextColor(getResources().getColor(R.color.white));
                        binding.incomingonofftxt.setText("Incoming Video Calls : ON");
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in2);
                        binding.incomingonoffLyt.setAnimation(animation);
                        binding.incomingonoffLyt.setVisibility(View.VISIBLE);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.fade_outnew);
                                binding.incomingonoffLyt.setAnimation(animation2);
                                binding.incomingonoffLyt.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    } else {

                        Constant.setSfFunction(mContext);
                        Constant.setSF.putString(Constant.videoRadioKey, "");
                        Constant.setSF.apply();
                        binding.videocallText.setTextColor(Color.parseColor("#9EA6B9"));
                        binding.incomingonofftxt.setText("Incoming Video Calls : OFF");
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in2);
                        binding.incomingonoffLyt.setAnimation(animation);
                        binding.incomingonoffLyt.setVisibility(View.VISIBLE);

                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.fade_outnew);
                                binding.incomingonoffLyt.setAnimation(animation2);
                                binding.incomingonoffLyt.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
            }
        });


//        binding.linearMain2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                slideDownContainer();
//                return false;
//
//            }
//        });

//        binding.linearMain2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                slideDownContainer();
//            }
//        });


    }

    /**
     * Reset menu item highlights to default state
     * This method clears any background highlights and resets text colors
     */
    private void resetMenuState() {
        if (upper_layout != null) {
            // Reset background resources to default (no background)
            if (SponswerRect != null) SponswerRect.setBackgroundResource(0);
            if (lockScreenRect != null) lockScreenRect.setBackgroundResource(0);
            if (ThemeRect != null) ThemeRect.setBackgroundResource(0);
            if (PayRect != null) PayRect.setBackgroundResource(0);
            if (SettingsRect != null) SettingsRect.setBackgroundResource(0);
            if (sizeRect != null) sizeRect.setBackgroundResource(0);

            // Reset text colors to default gray
            if (sponswerTxt != null) sponswerTxt.setTextColor(Color.parseColor("#9EA6B9"));
            if (lockscreenTxt != null) lockscreenTxt.setTextColor(Color.parseColor("#9EA6B9"));
            if (ThemeTxt != null) ThemeTxt.setTextColor(Color.parseColor("#9EA6B9"));
            if (PayTxt != null) PayTxt.setTextColor(Color.parseColor("#9EA6B9"));
            if (SettingsTxt != null) SettingsTxt.setTextColor(Color.parseColor("#9EA6B9"));
            if (sizeTxt != null) sizeTxt.setTextColor(Color.parseColor("#9EA6B9"));
        }
    }

    private void setupChattingRoomCode(RelativeLayout root) {
    }

    private void viewTransformation(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);

                break;

        }
    }


    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    //   Toast.makeText(MainActivity.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {

    
        

        


        ViewGroup.LayoutParams params = binding.linearMain.getLayoutParams();
        params.height = 250;

        if (linearMain2.getVisibility() == View.VISIBLE) {

            if (binding.container.getVisibility() == View.VISIBLE) {

                binding.linearMain.setBackgroundResource(R.drawable.mainvector);
                binding.imgArrow2.setImageResource(R.drawable.downarrowslide);
                binding.container.setVisibility(View.VISIBLE);


                ValueAnimator valueAnimator = ValueAnimator.ofInt(binding.linearMain.getHeight(), pixels);
                valueAnimator.setDuration(270);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        binding.linearMain.getLayoutParams().height = (int) animation.getAnimatedValue();
                        binding.linearMain.requestLayout();
                        //    window.setStatusBarColor(mContext.getResources().getColor(R.color.white));
                        imgArrow2.setVisibility(View.VISIBLE);
                        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                        //     logoandsearch.startAnimation(aniFade);
                        logoandsearch.setVisibility(View.VISIBLE);
                        linearMain2.setVisibility(View.GONE);
                        binding.sliderealtime.setText("down");
                        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {

                            bacMenu.setBackgroundColor(Color.parseColor("#000000"));

                            if (Build.VERSION.SDK_INT >= 21) {
                                Window window = activity.getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.setStatusBarColor(Color.parseColor("#000000"));
                                activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR

                            }

                        } else {
                            bacMenu.setBackgroundColor(Color.parseColor("#ffffff")); // Replace #FF5733 with your hex color value

                            if (Build.VERSION.SDK_INT >= 21) {
                                Window window = activity.getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.setStatusBarColor(Color.parseColor("#ffffff"));
                                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                            }

                        }
                    }
                });
                valueAnimator.start();


                //   binding.linearMain.startAnimation(animation);


                params.height = pixels;
                binding.linearMain.setLayoutParams(params);
                imgArrow.setEnabled(true);


                try {
                    ///CAll fragment back press Need GONE
                    if (CallUtil.backlyt.getVisibility() == View.VISIBLE) {
                        CallUtil.backlyt.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }


                try {
                    ///videoCallFragment  back press Need GONE
                    if (VideoCallUtil.backlytVideoCall.getVisibility() == View.VISIBLE) {
                        VideoCallUtil.backlytVideoCall.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }

                try {
                    ///groupMsgFragment  back press Need GONE
                    if (GroupMsgUtils.backlyt.getVisibility() == View.VISIBLE) {
                        GroupMsgUtils.backlyt.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
                try {
                    ///msgLimitFragment  back press Need GONE
                    if (MsgLimitUtils.backlyt.getVisibility() == View.VISIBLE) {

                        MsgLimitUtils.backlyt.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
                try {
                    ///msgLimitFragment  back press Need GONE
                    if (YouUtils.backlytYouFrag.getVisibility() == View.VISIBLE) {
                        YouUtils.backlytYouFrag.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }

            }
       //     Toast.makeText(mContext, "called", Toast.LENGTH_SHORT).show();
        } else
        {

            expand(binding.logoandmenu);

            String backKey = null;

            try {
                Constant.getSfFuncion(mContext);
                backKey = Constant.getSF.getString(Constant.BACKKEY, "");
                // Toast.makeText(activity, backKey, Toast.LENGTH_SHORT).show();

            } catch (Exception ex) {
                // Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }


            if (backKey.equals(Constant.otherBackValue)) {

                 if (binding.container.getVisibility() == View.VISIBLE) {

                    int topMarginInDp = 39; // Margin in dp
                    int topMarginInPx = (int) (topMarginInDp * binding.getRoot().getContext().getResources().getDisplayMetrics().density);

                    LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) binding.downArrow.getLayoutParams();
                    params5.setMargins(0, topMarginInPx, 0, 0);
                    binding.downArrow.setLayoutParams(params5);


                    if (binding.logoandmenu.getVisibility() == View.GONE) {
                        //   binding.logoandmenu.setVisibility(View.VISIBLE);
                        expand(binding.logoandmenu);
                    }

                    imgArrow2.setImageResource(R.drawable.downarrowslide);
                    linearMain.setLayoutParams(params);

                    int initialHeight = 700; // The initial height of the view
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(initialHeight, 350);
                    valueAnimator.setDuration(400);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int height = (int) animation.getAnimatedValue();
                            linearMain.getLayoutParams().height = height;
                            linearMain.requestLayout();
                        }
                    });
                    valueAnimator.start();


                    container.setVisibility(View.GONE);

                    params.height = 350;
                    linearMain.setBackgroundResource(R.drawable.bg);


                    if (binding.searchLyt.getVisibility() == View.GONE) {
                        Animation animation3 = AnimationUtils.loadAnimation(mContext, R.anim.fade_inthreethousand);
                        binding.searchIcon.setAnimation(animation3);
                        binding.searchIcon.setVisibility(View.VISIBLE);

                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_inthreethousand);
                        binding.searchLyt.setAnimation(anim);
                        binding.searchLyt.setVisibility(View.GONE);
                    }


                    binding.chattingRoomFragment.getRoot().setVisibility(View.VISIBLE);
                    binding.callFragment.getRoot().setVisibility(View.GONE);
                    binding.msgLimitFragment.getRoot().setVisibility(View.GONE);
                    binding.groupFragment.getRoot().setVisibility(View.GONE);
                    binding.youFragment.getRoot().setVisibility(View.GONE);
                    binding.videoCallFragment.getRoot().setVisibility(View.GONE);


                    ChattingRoomUtils.setupChattingRoomCode(binding.chattingRoomFragment.getRoot());


                }

            } else if (backKey.equals(Constant.chattingBackValue)) {

                moveTaskToBack(true);

            } else if (backKey.isEmpty()) {
                if (binding.container.getVisibility() == View.VISIBLE) {

                    int topMarginInDp = 39; // Margin in dp
                    int topMarginInPx = (int) (topMarginInDp * binding.getRoot().getContext().getResources().getDisplayMetrics().density);

                    LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) binding.downArrow.getLayoutParams();
                    params5.setMargins(0, topMarginInPx, 0, 0);
                    binding.downArrow.setLayoutParams(params5);


                    if (binding.logoandmenu.getVisibility() == View.GONE) {
                        //   binding.logoandmenu.setVisibility(View.VISIBLE);
                        expand(binding.logoandmenu);
                    }

                    imgArrow2.setImageResource(R.drawable.downarrowslide);
                    linearMain.setLayoutParams(params);

                    int initialHeight = 700; // The initial height of the view
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(initialHeight, 350);
                    valueAnimator.setDuration(400);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int height = (int) animation.getAnimatedValue();
                            linearMain.getLayoutParams().height = height;
                            linearMain.requestLayout();
                        }
                    });
                    valueAnimator.start();


                    container.setVisibility(View.GONE);

                    params.height = 350;
                    linearMain.setBackgroundResource(R.drawable.bg);


                    if (binding.searchLyt.getVisibility() == View.GONE) {
                        Animation animation3 = AnimationUtils.loadAnimation(mContext, R.anim.fade_inthreethousand);
                        binding.searchIcon.setAnimation(animation3);
                        binding.searchIcon.setVisibility(View.VISIBLE);

                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_inthreethousand);
                        binding.searchLyt.setAnimation(anim);
                        binding.searchLyt.setVisibility(View.GONE);
                    }


                    binding.chattingRoomFragment.getRoot().setVisibility(View.VISIBLE);
                    binding.callFragment.getRoot().setVisibility(View.GONE);
                    binding.msgLimitFragment.getRoot().setVisibility(View.GONE);
                    binding.groupFragment.getRoot().setVisibility(View.GONE);
                    binding.youFragment.getRoot().setVisibility(View.GONE);
                    binding.videoCallFragment.getRoot().setVisibility(View.GONE);


                    ChattingRoomUtils.setupChattingRoomCode(binding.chattingRoomFragment.getRoot());


                }

            }
        }


    }

    public void onback2() {
        SwipeNavigationHelper.finishWithSwipe(MainActivityOld.this);
    }


    public static void slideUpContainer() {

        sliderealtime.setText("up");
        if (linearMain2.getVisibility() == View.GONE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(mContext);
            }
            if (container.getVisibility() == View.VISIBLE) {
                imgArrow2.setImageResource(R.drawable.downarrowslide);

                if (linearMain2.getVisibility() == View.GONE) {
                    linearMain2.setVisibility(View.VISIBLE);
                }

                linearMain.setLayoutParams(params);

                ValueAnimator valueAnimator;
                if (Build.VERSION.SDK_INT >= 35) {
                    int initialHeight = 760; // The initial height of the view
                    valueAnimator = ValueAnimator.ofInt(initialHeight, 140);
                } else {
                    int initialHeight = 700; // The initial height of the view
                    valueAnimator = ValueAnimator.ofInt(initialHeight, 80);
                }


                if (golbalDecinal > 1500) {
                    finalDuration = 500;
                } else if (golbalDecinal < 1500 && golbalDecinal > 10) {
                    finalDuration = 1000;
                } else if (golbalDecinal == 0) {
                    finalDuration = 600;
                }


                //previous 1000 duration value
                valueAnimator.setDuration(270);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        linearMain.getLayoutParams().height = (int) animation.getAnimatedValue();
                        linearMain.requestLayout();
                    }

                });

                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {

                        int colorFrom = mContext.getResources().getColor(R.color.white);
                        int colorTo = mContext.getResources().getColor(R.color.appThemeColor);
                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(270); // milliseconds
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {

                                logoandsearch.setVisibility(View.GONE);
                            }

                        });
                        colorAnimation.start();

                        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                            // Dark mode is active
                            Constant.getSfFuncion(mContext);
                            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                            ColorStateList tintList;

                            try {
                                if (themColor.equals("#ff0080")) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#4D0026"));


                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();

                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#4D0026"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR

                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }


                                } else if (themColor.equals("#00A3E9")) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#01253B"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }
                                    bacMenu.setBackgroundColor(Color.parseColor("#01253B"));
                                } else if (themColor.equals("#7adf2a")) {

                                    tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#25430D"));

                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#25430D"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }

                                } else if (themColor.equals("#ec0001")) {

                                    tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#470000"));

                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#470000"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }

                                } else if (themColor.equals("#16f3ff")) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#05495D"));
                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#05495D"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }
                                } else if (themColor.equals("#FF8A00")) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#663700"));

                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#663700"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }
                                } else if (themColor.equals("#7F7F7F")) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#2B3137"));
                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#2B3137"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }
                                } else if (themColor.equals("#D9B845")) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#413815"));
                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#413815"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }
                                } else if (themColor.equals("#346667")) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#1F3D3E"));

                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#1F3D3E"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }
                                } else if (themColor.equals("#9846D9")) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#2d1541"));

                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#2d1541"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }
                                } else if (themColor.equals("#A81010")) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#430706"));
                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#430706"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }
                                } else {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                    bacMenu.setBackgroundColor(Color.parseColor("#01253B"));

                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(Color.parseColor("#01253B"));
                                        activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                    }
                                }
                            } catch (Exception ignored) {
                                tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                bacMenu.setBackgroundColor(Color.parseColor("#01253B"));

                                if (Build.VERSION.SDK_INT >= 21) {
                                    Window window = activity.getWindow();
                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                    window.setStatusBarColor(Color.parseColor("#01253B"));
                                    activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
                                }
                            }


                        } else {
                            bacMenu.setBackgroundColor(Color.parseColor("#011224")); // Replace #FF5733 with your hex color value

                            if (Build.VERSION.SDK_INT >= 21) {
                                Window window = activity.getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.setStatusBarColor(Color.parseColor("#011224"));
                                activity.getWindow().getDecorView().setSystemUiVisibility(0);

                            }
                        }


                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {


                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) {

                    }
                });


                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });

                valueAnimator.start();


                params.height = 90;


            }


        }



    }

    public static void slideDownContainer() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            Constant.Vibrator(mContext);
//        }
        sliderealtime.setText("down");
        ViewGroup.LayoutParams params = linearMain.getLayoutParams();
        params.height = 250;

        if (linearMain2.getVisibility() == View.VISIBLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(mContext);
            }
            if (container.getVisibility() == View.VISIBLE) {

                linearMain.setBackgroundResource(R.drawable.mainvector);
                imgArrow2.setImageResource(R.drawable.downarrowslide);

                container.setVisibility(View.VISIBLE);


                ValueAnimator valueAnimator = ValueAnimator.ofInt(linearMain.getHeight(), pixels);
                valueAnimator.setDuration(270);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        linearMain.getLayoutParams().height = (int) animation.getAnimatedValue();
                        linearMain.requestLayout();

                        //  window.setStatusBarColor(mContext.getResources().getColor(R.color.white));

                        imgArrow2.setVisibility(View.VISIBLE);

                        Animation aniFade = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
                        // logoandsearch.startAnimation(aniFade);
                        logoandsearch.setVisibility(View.VISIBLE);
                        linearMain2.setVisibility(View.GONE);


                        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {

                            bacMenu.setBackgroundColor(Color.parseColor("#000000"));

                            if (Build.VERSION.SDK_INT >= 21) {
                                Window window = activity.getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.setStatusBarColor(Color.parseColor("#000000"));
                                activity.getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR

                            }

                        } else {
                            bacMenu.setBackgroundColor(Color.parseColor("#ffffff")); // Replace #FF5733 with your hex color value

                            if (Build.VERSION.SDK_INT >= 21) {
                                Window window = activity.getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.setStatusBarColor(Color.parseColor("#ffffff"));
                                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                            }

                        }


                    }
                });
                valueAnimator.start();


                //   linearMain.startAnimation(animation);


                params.height = pixels;
                linearMain.setLayoutParams(params);


            }


            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }


        }


    }


    @Override
    protected void onStop() {
        super.onStop();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.linearMain.getLayoutParams();
        int currentHeight = layoutParams.height;

//


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        keyboardHeightProvider.close();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.linearMain.getLayoutParams();
        int currentHeight = layoutParams.height;

        if (currentHeight == 350) {
            binding.searchIcon.setVisibility(View.VISIBLE);
        } else {

            expand(binding.logoandmenu);
            int topMarginInDp = 39; // Margin in dp
            int topMarginInPx = (int) (topMarginInDp * binding.getRoot().getContext().getResources().getDisplayMetrics().density);

            LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) binding.downArrow.getLayoutParams();
            params5.setMargins(0, topMarginInPx, 0, 0); // left, top, right, bottom (in pixels)
            binding.downArrow.setLayoutParams(params5);
            ViewGroup.LayoutParams params = linearMain.getLayoutParams();
            params.height = 250;

            if (linearMain2.getVisibility() == View.VISIBLE) {
                if (container.getVisibility() == View.VISIBLE) {

                    linearMain.setBackgroundResource(R.drawable.mainvector);
                    imgArrow2.setImageResource(R.drawable.downarrowslide);

                    container.setVisibility(View.VISIBLE);
                    linearMain.requestLayout();

                    window.setStatusBarColor(mContext.getResources().getColor(R.color.white));

                    imgArrow2.setVisibility(View.VISIBLE);

                    logoandsearch.setVisibility(View.VISIBLE);
                    linearMain2.setVisibility(View.GONE);


                    params.height = pixels;
                    linearMain.setLayoutParams(params);

                }


                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }


            } else {


            }

        }


        if (isReceiverRegistered) {
            unregisterReceiver(connectivityReceiver);
            isReceiverRegistered = false;
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        keyboardHeightProvider.setKeyboardHeightObserver(null);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.linearMain.getLayoutParams();
        int currentHeight = layoutParams.height;



    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            Log.d("Network", "connected: " + "MainActivity");
            binding.networkLoader.setVisibility(View.GONE);
        } else {
            Log.d("Network", "disConnected: " + "MainActivity");
            binding.networkLoader.setVisibility(View.VISIBLE);

        }
    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d("Anonymous", "signInAnonymously:success with UID: " + user.getUid());
            } else {
                // If sign in fails, display a message to the user.
                Log.w("Anonymous", "signInAnonymously:failure", task.getException());
            }
        });
    }


    private void stopRingtone() {
        if (MainApplication.player != null && MainApplication.player.isPlaying()) {
            MainApplication.player.stop();
            MainApplication.player.release();
            MainApplication.player = null;
        }
    }


    private boolean checkAndRequestPermissions2() {


        try {
            int contact = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS);


            List<String> listPermissionsNeeded = new ArrayList<>();

            if (contact != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);
            }


            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(MainActivityOld.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

                return false;
            }

        } catch (Exception ex) {
            Log.d("permission%%", ex.getMessage());
        }
        return true;
    }

    public void requestNotificationPermission() {

        if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityOld.this, Manifest.permission.POST_NOTIFICATIONS)) {


                Constant.dialoguePermision(mContext);
                TextView textView = Constant.dialogLayoutColor.findViewById(R.id.text);
                textView.setText("Please Grant The Notification permissions In Settings To Continue.");
                AppCompatImageView dismiss = Constant.dialogLayoutColor.findViewById(R.id.dismiss);
                AppCompatButton gotosetting = Constant.dialogLayoutColor.findViewById(R.id.gotosetting);
                Constant.dialogLayoutColor.show();

                gotosetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        SwipeNavigationHelper.startActivityWithSwipe(MainActivityOld.this, intent);
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
                // ActivityCompat.requestPermissions((()), new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityOld.this, Manifest.permission.POST_NOTIFICATIONS)) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivityOld.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                } else {
                    Constant.dialoguePermision(mContext);
                    TextView textView = Constant.dialogLayoutColor.findViewById(R.id.text);
                    textView.setText("Please Grant The Notification permissions In Settings To Continue.");
                    AppCompatImageView dismiss = Constant.dialogLayoutColor.findViewById(R.id.dismiss);
                    AppCompatButton gotosetting = Constant.dialogLayoutColor.findViewById(R.id.gotosetting);
                    Constant.dialogLayoutColor.show();

                    gotosetting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            SwipeNavigationHelper.startActivityWithSwipe(MainActivityOld.this, intent);
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

            } else {
            }
        } else if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with call handling
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission needed to manage calls", Toast.LENGTH_SHORT).show();
            }
            // Toast.makeText(binding.getRoot().getContext(), "You have disabled a notifiation permission", Toast.LENGTH_LONG).show();
        } else if (requestCode == this.requestCode) {

            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {

                //    binding.btn.performClick();
            } else {
                // Handle the case where permission was denied
                Toast.makeText(this, "Permissions are required to proceed", Toast.LENGTH_SHORT).show();
            }
        }


    }

    void askPermissions() {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }


    private boolean isPermissionsGranted() {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }


    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {


        if (height > 0) {
            Constant.setSfFunction(mContext);
            Constant.setSF.putString("keyboardHeightKey", String.valueOf(height));
            Constant.setSF.apply();

            //   Toast.makeText(mContext, "saved "+String.valueOf(height), Toast.LENGTH_SHORT).show();
        }
    }


    public static void expand(final View v) {
//        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
//        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
//        final int targetHeight = v.getMeasuredHeight();
//
//        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
//        v.getLayoutParams().height = 1;
//        v.setVisibility(View.VISIBLE);
//        Animation a = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                v.getLayoutParams().height = interpolatedTime == 1
//                        ? ViewGroup.LayoutParams.WRAP_CONTENT
//                        : (int) (targetHeight * interpolatedTime);
//                v.requestLayout();
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//
//        // Expansion speed of 1dp/ms
//        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
//        v.startAnimation(a);
    }

    public static void collapse(final View v) {
//        final int initialHeight = v.getMeasuredHeight();
//
//        Animation a = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                if (interpolatedTime == 1) {
//                    v.setVisibility(View.GONE);
//                } else {
//                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
//                    v.requestLayout();
//                }
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//
//        // Collapse speed of 1dp/ms
//        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
//        v.startAnimation(a);
    }


    private void getContactList() {
        contactList.clear();
        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                // Load UID and phone number from SharedPreferences
                Constant.getSfFuncion(mContext);
                String uidKey = Constant.getSF.getString(Constant.UID_KEY, "");
                String phoneKey = Constant.getSF.getString(Constant.PHONE_NUMBERKEY, "");

                // Define the file that stores old contacts
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

                    // Normalize number
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

                    // Add to in-memory contact list
                    contactList.add(new contactUploadModel(trimmedName, formattedNumber));

                    boolean isNew = !oldContactsMap.containsKey(formattedNumber);
                    boolean isUpdated = oldContactsMap.containsKey(formattedNumber) &&
                            !oldContactsMap.get(formattedNumber).trim().equalsIgnoreCase(trimmedName);

                    if (isNew || isUpdated) {
                        JSONObject contactObj = new JSONObject();
                        contactObj.put("uid", uidKey);
                        contactObj.put("mobile_no", phoneKey);
                        contactObj.put("contact_name", trimmedName);
                        contactObj.put("contact_number", formattedNumber);

                        newOrUpdatedContacts.put(contactObj);

                        Log.d("ContactChange", "New/Updated Contact: " + formattedNumber +
                                " (Old: " + oldContactsMap.get(formattedNumber) + ", New: " + trimmedName + ")");
                    }

                    contactCounter++;
                }

                // If any modified contacts found, save and upload
                if (newOrUpdatedContacts.length() > 0) {
                    String finalData = "{ \"contact\":" + newOrUpdatedContacts.toString() + "}";

                    try (FileWriter fileWriter = new FileWriter(file)) {
                        fileWriter.write(finalData);
                        Log.d("ContactUpdate", "Saved modified contacts: " + newOrUpdatedContacts.length());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Webservice.upload_user_contact_listRetrofitMainActivity(
                            mContext,
                            uidKey,
                            file,
                            "",
                            "+91",  // or dynamic country code
                            "token",
                            "deviceId",
                            MainActivityOld.this
                    );
                } else {
                    Log.d("ContactUpdate", "No new or modified contacts found. Skipping upload.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkAndRequestPermissions() {


        try {
            int contact = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
            int storage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int post = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS);


            List<String> listPermissionsNeeded = new ArrayList<>();
            if (contact != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);

            }

            if (storage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

            }

            if (post != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);

            }


            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(MainActivityOld.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

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
                ActivityCompat.requestPermissions(MainActivityOld.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

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

    public void requestContactPermission() {
        if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityOld.this, Manifest.permission.READ_CONTACTS)) {
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
                ActivityCompat.requestPermissions(MainActivityOld.this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            getContactList();
        }
    }
}