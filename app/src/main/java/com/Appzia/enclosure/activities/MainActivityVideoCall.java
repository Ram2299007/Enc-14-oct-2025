package com.Appzia.enclosure.activities;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.get_video_calling_adapter2;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.CallServiceVideoCall;
import com.Appzia.enclosure.Utils.CallServiceVideoCall;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.MainApplication;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityMainVideoCallBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class MainActivityVideoCall extends AppCompatActivity {
    private ActivityMainVideoCallBinding binding;
    private ConnectivityManager.NetworkCallback networkCallback;
    private DatabaseReference database;
    private boolean isCallEnded = false;
    private String roomId, receiverId, myPeerId, myRoom, incoming, photo, name, username, createdBy, roomFlagKey;
    private boolean iAmSender = false;
    private static final int PERMISSION_REQUEST_CODE = 100;
    public static final String MY_PEER_ID = "myPeerId";
    private boolean hasRequestedPermissions = false;
    private get_video_calling_adapter2 adapter;
    private TextView customToastText;
    private ValueEventListener peersListener;
    private ArrayList<get_contact_model> get_calling_contact_list = new ArrayList<>();
    private ArrayList<String> participants = new ArrayList<>();
    private Context mContext;
    private CardView customToastCard;
    private LocalBroadcastManager localBroadcastManager;
    private ValueEventListener signalingListener;
    private static final String TAG = "MainActivityVideoCallXXXXXXX";
    private boolean isInPictureInPictureMode = false;
    private boolean isFullScreen = false;
    private MediaPlayer ringtonePlayer;

    // SharedPreferences keys for call data
    public static final String PREFS_CALL_DATA = "CallDataPrefs";
    public static final String KEY_MY_ROOM = "myRoom";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_INCOMING = "incoming";
    public static final String KEY_CREATED_BY = "createdBy";
    public static final String KEY_ROOM_FLAG_KEY = "roomFlagKey";
    public static final String KEY_PHOTO_RECEIVER = "photoReceiver";
    public static final String KEY_NAME_RECEIVER = "nameReceiver";
    public static final String KEY_CALL_START_TIME = "callStartTime";
    public static final String KEY_ROOM_ID = "roomId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_IS_SENDER = "isSender";
    private Bundle webViewBundle;


    private final BroadcastReceiver timerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CallServiceVideoCall.ACTION_TIMER_UPDATE.equals(intent.getAction())) {
                long durationSeconds = intent.getLongExtra("duration_seconds", 0);
                String time = String.format("%02d:%02d", durationSeconds / 60, durationSeconds % 60);
                //  binding.webView.evaluateJavascript("javascript:document.getElementById('callTimer').textContent = '" + time + "'", null);
                Log.d(TAG, "Received timer update: " + time);
            } else if ("com.Appzia.enclosure.END_CALL_UI_VIDEO".equals(intent.getAction())) {
                triggerEndCallButton();
                Log.d(TAG, "Received end call broadcast, triggering end call");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // Restore call data
        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        roomId = callDataPrefs.getString(KEY_ROOM_ID, "");
        receiverId = callDataPrefs.getString(KEY_RECEIVER_ID, "");
        iAmSender = callDataPrefs.getBoolean(KEY_IS_SENDER, false);
        name = callDataPrefs.getString(KEY_NAME_RECEIVER, "Name");
        photo = callDataPrefs.getString(KEY_PHOTO_RECEIVER, "file:///android_asset/user.png");

        // Reinitialize WebRTC and UI
        if (binding.webView != null) {
            binding.webView.setVisibility(View.VISIBLE);
            binding.webView.evaluateJavascript("javascript:reconnectPeer()", null);
            binding.webView.evaluateJavascript("javascript:setRemoteCallerInfo('" + photo.replace("'", "\\'") + "', '" + name.replace("'", "\\'") + "')", null);
            binding.webView.evaluateJavascript("javascript:updateVideoLayout()", null);
            binding.webView.evaluateJavascript("javascript:updateVideoMirroring()", null);

            Log.d(TAG, "Reinitialized WebRTC and UI on resume");
        }

        // Log call data
        if (iAmSender) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = dateFormat.format(new Date());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
            String currentTime = timeFormat.format(Calendar.getInstance().getTime());
            Constant.getSfFuncion(mContext);
            String uids = Constant.getSF.getString(Constant.UID_KEY, "");
            Webservice.create_group_calling(mContext, uids, receiverId, "", date, currentTime, "0", currentTime, "2");
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = dateFormat.format(new Date());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
            String currentTime = timeFormat.format(Calendar.getInstance().getTime());
            Constant.getSfFuncion(mContext);
            String uids = Constant.getSF.getString(Constant.UID_KEY, "");
            Webservice.create_group_calling(mContext, uids, receiverId, "", date, currentTime, "1", currentTime, "2");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainVideoCallBinding.inflate(getLayoutInflater());

        // Enable screen on and lock screen visibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            );
        }

        // Set initial full-screen mode
        setFullScreen(true);
        setContentView(binding.getRoot());
        mContext = MainActivityVideoCall.this;

        Intent intent = getIntent();
        boolean iAmSenderNew = intent.getBooleanExtra("iAmSenderNew", false);


        intent.removeExtra("iAmSenderNew");


        if (iAmSenderNew) {
            // Toast.makeText(mContext, "sender", Toast.LENGTH_SHORT).show();
            startRingtone();
        }


        if (MainApplication.player != null && MainApplication.player.isPlaying()) {
            MainApplication.player.stop();
            MainApplication.player.release();
            MainApplication.player = null;
        }

        // Configure system UI for immersive mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
                insetsController.hide(WindowInsetsController.BEHAVIOR_DEFAULT);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        database = FirebaseDatabase.getInstance().getReference();
        binding.webView.setVisibility(View.GONE);

        // Retrieve data from Intent
        String intentRoomFlagKey = getIntent().getStringExtra("roomFlagKey");
        String intentPhoto = getIntent().getStringExtra("photoReceiver");
        String intentName = getIntent().getStringExtra("nameReceiver");
        String intentRoomId = getIntent().getStringExtra("roomId");
        String intentReceiverId = getIntent().getStringExtra("receiverId");
        String intentMyRoom = getIntent().getStringExtra("myRoom");
        String intentUsername = getIntent().getStringExtra("username");
        String intentIncoming = getIntent().getStringExtra("incoming");
        String intentCreatedBy = getIntent().getStringExtra("createdBy");
        boolean intentIsSender = getIntent().getBooleanExtra("iAmSender", false);

        customToastCard = findViewById(R.id.includedToast);
        customToastText = customToastCard.findViewById(R.id.toastText);

        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        receiverId = getIntent().getStringExtra("receiverId");
        roomFlagKey = getIntent().getStringExtra("roomFlagKey");


        String deleteroom = receiverId + uid;


        DatabaseReference reference = database
                .child("declineVideoKey")
                .child(deleteroom);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && mContext != null) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            database.child("declineVideoKey").child(deleteroom).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    String calldroptext = "Call Dropped";
                                    Constant.showCustomToast(calldroptext, customToastCard, customToastText);
                                    stopRingtone();

                                    new Handler().postDelayed(() -> {
                                        triggerEndCallButton();
                                    }, 2000);
                                }
                            })

                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Assign to local variables, applying defaults for display/immediate use if needed
        name = (intentName == null || intentName.isEmpty()) ? "Name" : intentName;
        photo = (intentPhoto == null || intentPhoto.isEmpty()) ? "file:///android_asset/user.png" : intentPhoto;
        roomId = intentRoomId;
        receiverId = intentReceiverId;
        myRoom = intentMyRoom;
        username = intentUsername;
        incoming = intentIncoming;
        createdBy = intentCreatedBy;
        roomFlagKey = intentRoomFlagKey;
        iAmSender = intentIsSender;


        if(intentRoomId!=null) {
            // Save all call-related data to SharedPreferences (original values from Intent)
            SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = callDataPrefs.edit();
            editor.putString(KEY_MY_ROOM, intentMyRoom);
            editor.putString(KEY_USERNAME, intentUsername);
            editor.putString(KEY_INCOMING, intentIncoming);
            editor.putString(KEY_CREATED_BY, intentCreatedBy);
            editor.putString(KEY_ROOM_FLAG_KEY, intentRoomFlagKey);
            editor.putString(KEY_PHOTO_RECEIVER, intentPhoto);
            editor.putString(KEY_NAME_RECEIVER, intentName);
            editor.putLong(KEY_CALL_START_TIME, System.currentTimeMillis());
            editor.putString(KEY_ROOM_ID, intentRoomId);
            editor.putString(KEY_RECEIVER_ID, intentReceiverId);
            editor.putBoolean(KEY_IS_SENDER, intentIsSender);
            editor.apply();

            // Save name and photo to SharedPreferences for CallServiceVideoCall (Constant.getSF)
            Constant.getSfFuncion(mContext);
            SharedPreferences.Editor constantEditor = Constant.getSF.edit();
            constantEditor.putString(Constant.callName, name);
            constantEditor.putString(Constant.photoReceiver, photo);
            constantEditor.apply();

            // Initialize and play ringtone based on iAmSender
            if (iAmSender) {
                startRingtone();
            }
        }

        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(100);
        } catch (Exception e) {
            Log.e(TAG, "Error cancelling notification: " + e.getMessage());
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(CallServiceVideoCall.ACTION_TIMER_UPDATE);
        filter.addAction("com.Appzia.enclosure.END_CALL_UI_VIDEO");
        localBroadcastManager.registerReceiver(timerReceiver, filter);
        Intent requestIntent = new Intent(CallServiceVideoCall.ACTION_REQUEST_DURATION);
        localBroadcastManager.sendBroadcast(requestIntent);

        setupWebView();
        requestPermissions();

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (binding.webView != null) {
            webViewBundle = new Bundle();
            binding.webView.saveState(webViewBundle);
            outState.putBundle("webViewState", webViewBundle);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webViewBundle = savedInstanceState.getBundle("webViewState");
        if (webViewBundle != null && binding.webView != null) {
            binding.webView.restoreState(webViewBundle);
            // Reinitialize WebRTC session
            binding.webView.evaluateJavascript("javascript:reconnectPeer()", null);
            Log.d(TAG, "Restored WebView state and reinitialized WebRTC");
        }
    }


    private void startRingtone() {
        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            // Reset audio configurations to ensure a clean state
            audioManager.setMode(AudioManager.MODE_NORMAL); // Reset to normal mode first
            audioManager.setSpeakerphoneOn(true); // Explicitly disable speakerphone

            audioManager.setMicrophoneMute(false); // Ensure microphone is unmuted
            Log.d(TAG, "Audio configurations reset: speakerphone off, Bluetooth SCO off");

            // Set audio mode to communication for voice call routing
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            Log.d(TAG, "Audio mode set to MODE_IN_COMMUNICATION");

            // Route audio to earpiece for Android 12 (API 31) and above
            audioManager.setSpeakerphoneOn(true);

            // Initialize MediaPlayer
            if (ringtonePlayer != null) {
                Log.w(TAG, "Ringtone player already exists, releasing previous instance");
                stopRingtone(); // Release any existing MediaPlayer
            }
            ringtonePlayer = new MediaPlayer();
            Uri ringtoneUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ringtone);
            ringtonePlayer.setDataSource(this, ringtoneUri);
            ringtonePlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL); // Use voice call stream
            ringtonePlayer.setLooping(true);

            // Set listeners for MediaPlayer
            ringtonePlayer.setOnPreparedListener(mp -> {
                if (ringtonePlayer != null) {
                    Log.d(TAG, "MediaPlayer prepared, starting ringtone");
                    try {
                        ringtonePlayer.start();
                        Log.d(TAG, "Ringtone started through earpiece");
                    } catch (IllegalStateException e) {
                        Log.e(TAG, "Error starting ringtone: " + e.getMessage(), e);
                        Toast.makeText(this, "Failed to start ringtone", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Ringtone player is null after preparation");
                }
            });

            ringtonePlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
                Toast.makeText(this, "Ringtone playback error", Toast.LENGTH_SHORT).show();
                stopRingtone(); // Clean up on error
                return true; // Indicate error handled
            });

            // Prepare MediaPlayer asynchronously with a minimal delay to ensure audio routing
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (ringtonePlayer != null) {
                    try {
                        ringtonePlayer.prepareAsync();
                        Log.d(TAG, "Preparing MediaPlayer asynchronously");
                    } catch (IllegalStateException e) {
                        Log.e(TAG, "Error preparing MediaPlayer: " + e.getMessage(), e);
                        Toast.makeText(this, "Failed to prepare ringtone", Toast.LENGTH_SHORT).show();
                        stopRingtone();
                    }
                } else {
                    Log.e(TAG, "Cannot prepare ringtone: ringtonePlayer is null");
                    // Toast.makeText(this, "Ringtone initialization failed", Toast.LENGTH_SHORT).show();
                }
            }, 3000); // Reduced delay to 100ms to minimize race conditions

        } catch (Exception e) {
            Log.e(TAG, "Error initializing ringtone: " + e.getMessage(), e);
            Toast.makeText(this, "Failed to initialize ringtone", Toast.LENGTH_SHORT).show();
            stopRingtone(); // Clean up on failure
        }
    }


    private void stopRingtone() {
        if (ringtonePlayer != null) {
            if (ringtonePlayer.isPlaying()) {
                ringtonePlayer.stop();
            }
            ringtonePlayer.reset();
            ringtonePlayer.release();
            ringtonePlayer = null;
            Log.d(TAG, "Ringtone stopped and released");
        }
        if (MainApplication.player != null && MainApplication.player.isPlaying()) {
            MainApplication.player.stop();
            MainApplication.player.release();
            MainApplication.player = null;
        }
    }

    private void setFullScreen(boolean enable) {
        isFullScreen = enable;
        if (enable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsetsController insetsController = getWindow().getInsetsController();
                if (insetsController != null) {
                    insetsController.hide(WindowInsetsController.BEHAVIOR_DEFAULT);
                }
            } else {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );
            }
            Log.d(TAG, "Full-screen mode enabled");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsetsController insetsController = getWindow().getInsetsController();
                if (insetsController != null) {
                    insetsController.show(WindowInsetsController.BEHAVIOR_DEFAULT);
                }
            } else {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                );
            }
            Log.d(TAG, "Full-screen mode disabled");
        }
    }

    private void setupWebView() {
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // Enhanced WebView settings for optimal camera support
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        // Enable hardware acceleration for better video performance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        // Optimize for media playback
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // Enable WebRTC features
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // Cache path
        File cacheDir = getApplicationContext().getCacheDir();
        if (cacheDir != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        }

        // Load from cache first, then network
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // Allow autoplay videos and mixed content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Granting WebView permissions: " + String.join(", ", request.getResources()));
                    // Grant all requested permissions for optimal camera access
                    request.grant(request.getResources());
                });
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                Log.w(TAG, "WebView permission request was canceled");
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d(TAG, "WebView Console: " + consoleMessage.message() + " -- From line " + consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
                return true;
            }
        });

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "WebView page finished loading: " + url);

                // Restore data from SharedPreferences
                SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                String photoToDisplay = callDataPrefs.getString(KEY_PHOTO_RECEIVER, "");
                String nameToDisplay = callDataPrefs.getString(KEY_NAME_RECEIVER, "");
                String roomId = callDataPrefs.getString(KEY_ROOM_ID, "");

                if (photoToDisplay == null || photoToDisplay.isEmpty())
                    photoToDisplay = "file:///android_asset/user.png";
                if (nameToDisplay == null || nameToDisplay.isEmpty())
                    nameToDisplay = "Name";

                photoToDisplay = photoToDisplay.replace("'", "\\'");
                nameToDisplay = nameToDisplay.replace("'", "\\'");

                view.evaluateJavascript("javascript:setRoomId('" + roomId + "')", null);
                view.evaluateJavascript("javascript:setRemoteCallerInfo('" + photoToDisplay + "', '" + nameToDisplay + "')", null);
                view.evaluateJavascript("javascript:myPeerId = '" + myPeerId + "'", null); // Set myPeerId here

                Constant.getSfFuncion(mContext);
                String themeColorHex = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                binding.webView.evaluateJavascript("javascript:setThemeColor('" + themeColorHex + "')", null);

                // Full-screen & PiP listeners
                binding.webView.evaluateJavascript(
                        "javascript:document.getElementById('fullscreenButton').addEventListener('click', () => Android.toggleFullScreen())",
                        null
                );
                binding.webView.evaluateJavascript(
                        "javascript:document.getElementById('pipButton').addEventListener('click', () => Android.enterPiPModes())",
                        null
                );

                // Register network monitor
                registerNetworkCallback();
            }
        });

        binding.webView.addJavascriptInterface(new WebAppInterface(), "Android");

        // Restore previous state if available
        if (webViewBundle != null) {
            binding.webView.restoreState(webViewBundle);
            // Set myPeerId after restoring state
            binding.webView.evaluateJavascript("javascript:myPeerId = '" + myPeerId + "'", null);
            binding.webView.evaluateJavascript("javascript:reconnectPeer()", null); // Ensure WebRTC reconnection
        } else {
            binding.webView.loadUrl("file:///android_asset/index.html");
        }

        // Delay UI update after load
        new Handler().postDelayed(() -> {
            SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
            String photoToDisplay = callDataPrefs.getString(KEY_PHOTO_RECEIVER, "");
            String nameToDisplay = callDataPrefs.getString(KEY_NAME_RECEIVER, "");

            if (photoToDisplay == null || photoToDisplay.isEmpty())
                photoToDisplay = "file:///android_asset/user.png";
            if (nameToDisplay == null || nameToDisplay.isEmpty())
                nameToDisplay = "Name";

            photoToDisplay = photoToDisplay.replace("'", "\\'");
            nameToDisplay = nameToDisplay.replace("'", "\\'");

            binding.webView.evaluateJavascript("javascript:setRemoteCallerInfo('" + photoToDisplay + "', '" + nameToDisplay + "')", null);
        }, 100);
    }


    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
        };
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), PERMISSION_REQUEST_CODE);
            hasRequestedPermissions = true;
        } else {
            onPermissionsGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                Log.d(TAG, "All permissions granted");
                onPermissionsGranted();
            } else {
                Log.w(TAG, "Permissions denied");
                stopRingtone();
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void onPermissionsGranted() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            stopRingtone();
            finish();
            return;
        }
        Log.d(TAG, "Permissions granted, setting up WebView and listeners");

        binding.webView.setVisibility(View.VISIBLE);
        setupFirebaseListeners();

        Intent serviceIntent = new Intent(this, CallServiceVideoCall.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        Log.d(TAG, "Started CallServiceVideoCall as foreground service");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void enterPiPMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder pipBuilder = new PictureInPictureParams.Builder();
            Rational aspectRatio = new Rational(3, 5);
            pipBuilder.setAspectRatio(aspectRatio);
            try {
                enterPictureInPictureMode(pipBuilder.build());
                binding.webView.evaluateJavascript("javascript:document.querySelector('.controls-container').classList.add('hidden')", null);
                binding.webView.evaluateJavascript("javascript:document.querySelector('.top-bar').classList.add('hidden')", null);
                Log.d(TAG, "Entered Picture-in-Picture mode");
            } catch (IllegalStateException e) {
                Log.e(TAG, "Failed to enter PiP mode: " + e.getMessage());
                Toast.makeText(this, "Cannot enter PiP mode", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.w(TAG, "PiP mode not supported on this device");
            Toast.makeText(this, "PiP mode not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        this.isInPictureInPictureMode = isInPictureInPictureMode;

        if (isInPictureInPictureMode) {
            // Adjust UI for PiP
            binding.webView.evaluateJavascript("javascript:adjustForPiPMode()", null);
            binding.webView.evaluateJavascript("javascript:document.querySelector('.controls-container').classList.add('hidden')", null);
            binding.webView.evaluateJavascript("javascript:document.querySelector('.top-bar').classList.add('hidden')", null);

            // Hide secondary video after short delay
            binding.webView.evaluateJavascript(
                    "setTimeout(function() {" +
                            "let vid = document.querySelector('.secondary-video');" +
                            "if (vid) vid.style.display = 'none';" +
                            "}, 100);",
                    null
            );

            Log.d(TAG, "UI adjusted for PiP mode");
        } else {
            // Restore UI from PiP
            binding.webView.evaluateJavascript("javascript:reconnectPeer()", null);
            binding.webView.evaluateJavascript("javascript:updateVideoLayout()", null);
            binding.webView.evaluateJavascript("javascript:updateVideoMirroring()", null);
            binding.webView.evaluateJavascript("javascript:document.querySelector('.controls-container').classList.remove('hidden')", null);
            binding.webView.evaluateJavascript("javascript:document.querySelector('.top-bar').classList.remove('hidden')", null);

            // Show secondary video only if exactly 2 participants
            binding.webView.evaluateJavascript(
                    "setTimeout(function() {" +
                            "let participants = document.querySelectorAll('.remote-video').length;" +
                            "let vid = document.querySelector('.secondary-video');" +
                            "if (vid) {" +
                            "if (participants === 2) {" +
                            "vid.style.display = 'block';" +
                            "} else {" +
                            "vid.style.display = 'none';" +
                            "}" +
                            "}" +
                            "}, 0);",
                    null
            );

            // Restore fullscreen if it was previously set
            if (isFullScreen) {
                setFullScreen(true);
            }

            Log.d(TAG, "UI restored from PiP mode");
        }
    }


    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !isInPictureInPictureMode) {
            enterPiPMode();
        }
    }

    private class WebAppInterface {

        @JavascriptInterface
        public void onCallConnected() {
            runOnUiThread(() -> {
                stopRingtone();
                if (!hasRequestedPermissions) {
                    Log.d(TAG, "Call connected, requesting permissions");
                    requestPermissions();
                    hasRequestedPermissions = true;
                }
            });
        }

        @JavascriptInterface
        public void addMemberBtn() {
            runOnUiThread(() -> {
                ArrayList<get_contact_model> contactList = new DatabaseHelper(mContext).get_users_all_contactTable();
                Constant.dialogueFullScreen(mContext, R.layout.multiple_group_recyclerview);
                RecyclerView recyclerview = Constant.dialogLayoutFullScreen.findViewById(R.id.recyclerview);
                LinearLayout searchIcon = Constant.dialogLayoutFullScreen.findViewById(R.id.searchIcon);
                LinearLayout searchLytNew = Constant.dialogLayoutFullScreen.findViewById(R.id.searchLytNew);
                LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.backarrow);
                AutoCompleteTextView search = Constant.dialogLayoutFullScreen.findViewById(R.id.search);
                Constant.dialogLayoutFullScreen.show();

                // Retrieve receiverId and roomId from SharedPreferences
                SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                String receiverId = callDataPrefs.getString(KEY_RECEIVER_ID, "");
                String roomId = callDataPrefs.getString(KEY_ROOM_ID, "");

                setAdapter(contactList, recyclerview, customToastCard, customToastText, receiverId, roomId, Constant.dialogLayoutFullScreen);

                searchIcon.setOnClickListener(v1 -> {
                    if (searchLytNew.getVisibility() == View.VISIBLE) {
                        searchLytNew.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
                        }
                    } else {
                        search.requestFocus();
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_four);
                        searchLytNew.setAnimation(animation);
                        searchLytNew.setVisibility(View.VISIBLE);
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        if (inputMethodManager != null) {
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    }
                });

                search.addTextChangedListener(new TextWatcher() {
                    private final Handler handler = new Handler();
                    private Runnable runnable;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (runnable != null) {
                            handler.removeCallbacks(runnable);
                        }
                        runnable = () -> {
                            if (String.valueOf(s).isEmpty()) {
                                setAdapter(contactList, recyclerview, customToastCard, customToastText, receiverId, roomId, Constant.dialogLayoutFullScreen);
                            } else {
                                filteredList(String.valueOf(s));
                            }
                        };
                        handler.postDelayed(runnable, 1000);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                backarrow.setOnClickListener(v -> Constant.dialogLayoutFullScreen.dismiss());
            });
        }

        @JavascriptInterface
        public void callOnBackPressed() {
            runOnUiThread(() -> {
                stopRingtone();
                onBackPressed();
            });
        }

        @JavascriptInterface
        public void sendPeerId(String peerId) {
            runOnUiThread(() -> {
                Log.d(TAG, "Received peerId from JavaScript: " + peerId);
                myPeerId = peerId;

                SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);


                String MYPEERID = callDataPrefs.getString(MY_PEER_ID, "");

                //    Toast.makeText(mContext, MYPEERID, Toast.LENGTH_SHORT).show();

                if (MYPEERID.isEmpty()) {
                    MYPEERID = peerId;
                    SharedPreferences.Editor editor = callDataPrefs.edit();
                    editor.putString(MY_PEER_ID, peerId);
                    editor.apply();
                }

                try {
                    Constant.getSfFuncion(mContext);
                    String name = Constant.getSF.getString(Constant.full_name, "");
                    String photo = Constant.getSF.getString(Constant.profilePic, "");

                    JSONObject peerData = new JSONObject();
                    peerData.put("peerId", MYPEERID);
                    peerData.put("name", name);
                    peerData.put("photo", photo);

                    String currentRoomId = callDataPrefs.getString(KEY_ROOM_ID, "");
                    String finalMYPEERID = MYPEERID;
                    database.child("rooms").child(currentRoomId).child("peers")
                            .child(MYPEERID)
                            .setValue(peerData.toString())
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully wrote peerId: " + finalMYPEERID))
                            .addOnFailureListener(e -> Log.e(TAG, "Failed to write peerId: " + e.getMessage(), e));
                } catch (Exception e) {
                    Log.e(TAG, "Error sending peer data: " + e.getMessage(), e);
                }

                setupDeleteListers();
            });
        }



        @JavascriptInterface
        public void sendSignalingData(String data) {
            SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
            String currentRoomId = callDataPrefs.getString(KEY_ROOM_ID, "");
            database.child("rooms").child(currentRoomId).child("signaling").push().setValue(data)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully wrote signaling data"))
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to write signaling data: " + e.getMessage(), e));
        }

        @JavascriptInterface
        public void endCall() {
            triggerEndCallButton();
        }

        @JavascriptInterface
        public void enterPiPModes() {
            runOnUiThread(() -> {
                Log.d(TAG, "Entering PiP mode from JavaScript");
                enterPiPMode();
            });
        }

        @JavascriptInterface
        public void toggleFullScreen() {
            runOnUiThread(() -> {
                setFullScreen(!isFullScreen);
                binding.webView.evaluateJavascript("javascript:toggleFullScreen(" + !isFullScreen + ")", null);
            });
        }

        @JavascriptInterface
        public void toggleMicrophone(boolean mute) {
            runOnUiThread(() -> {
                try {
                    AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    audioManager.setMicrophoneMute(mute);
                    Log.d(TAG, "Microphone muted: " + mute);
                } catch (Exception e) {
                    Log.e(TAG, "Error toggling microphone: " + e.getMessage(), e);
                }
            });
        }

        @JavascriptInterface
        public void saveMuteState(boolean mute) {
            SharedPreferences prefs = mContext.getSharedPreferences("CallDataPrefs", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("isMicMuted", mute).apply();
            runOnUiThread(() -> {
                String message = mute ? "Microphone muted" : "Microphone unmuted";
                Log.d(TAG, message);
            });
        }

        @JavascriptInterface
        public boolean getMuteState() {
            SharedPreferences prefs = mContext.getSharedPreferences("CallDataPrefs", Context.MODE_PRIVATE);
            boolean isMicMuted = prefs.getBoolean("isMicMuted", false);
            runOnUiThread(() -> {
                String message = isMicMuted ? "Microphone is muted" : "Microphone is unmuted";
                Log.d(TAG, message);
            });
            return isMicMuted;
        }
    }

    private void goToMainActivityOld() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        String currentTime = timeFormat.format(Calendar.getInstance().getTime());

        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        receiverId = callDataPrefs.getString(KEY_RECEIVER_ID, "");
        iAmSender = callDataPrefs.getBoolean(KEY_IS_SENDER, false);

        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        String flag = (iAmSender) ? "0" : "1";
        Webservice.create_group_calling(mContext, receiverId, uid, "", date, currentTime, flag, currentTime, "2");

        if (binding.webView != null) {
            binding.webView.stopLoading();
            binding.webView.loadUrl("about:blank");
            binding.webView.clearHistory();
            binding.webView.clearCache(true);
            binding.webView.removeAllViews();
            Log.d(TAG, "[cleanupAndExit] WebView stopped and cleared");

            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMicrophoneMute(true);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            Log.d(TAG, "[cleanupAndExit] Microphone muted and audio mode reset");

            binding.webView.evaluateJavascript(
                    "javascript:if (localStream) { localStream.getTracks().forEach(track => track.stop()); localStream = null; }",
                    value -> Log.d(TAG, "[cleanupAndExit] JavaScript localStream stopped: " + value)
            );

            ViewParent parent = binding.webView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(binding.webView);
                Log.d(TAG, "[cleanupAndExit] WebView removed from parent");
            }
            binding.webView.destroy();

            Log.d(TAG, "[cleanupAndExit] WebView destroyed");
        }

        Log.d(TAG, "[cleanupAndExit] Clearing SharedPreferences");
        SharedPreferences callDataPrefs5 = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = callDataPrefs5.edit();
        editor.putString(KEY_MY_ROOM, "");
        editor.putString(KEY_USERNAME, "");
        editor.putString(KEY_INCOMING, "");
        editor.putString(KEY_CREATED_BY, "");
        editor.putString(KEY_ROOM_FLAG_KEY, "");
        editor.putString(KEY_PHOTO_RECEIVER, "");
        editor.putString(KEY_NAME_RECEIVER, "");
        editor.putLong(KEY_CALL_START_TIME, 0);
        editor.putString(KEY_ROOM_ID, "");
        editor.putString(MY_PEER_ID, "");
        editor.putString(KEY_RECEIVER_ID, "");
        editor.putBoolean(KEY_IS_SENDER, false);
        editor.putBoolean("isMicMuted", false);
        editor.apply();

        super.onBackPressed();
        ((Activity) mContext).finish();
    }

    public void triggerEndCallButton() {


        Log.d(TAG, "[triggerEndCallButton] Starting end call process at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        isCallEnded = true;

        runOnUiThread(() -> {
            try {
                stopRingtone();
                SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                String currentRoomId = callDataPrefs.getString(KEY_ROOM_ID, "");
                String currentRoomIdXX = callDataPrefs.getString(KEY_ROOM_ID, "");
                String myPeerId = callDataPrefs.getString(MY_PEER_ID, "");
                String roomIdToUse = currentRoomId.isEmpty() ? currentRoomIdXX : currentRoomId;
                Log.d(TAG, "[triggerEndCallButton] Using roomId: " + roomIdToUse);

                // 🔍 Debug Logs
                Log.e(TAG, "[triggerEndCallButton] PREFS_CALL_DATA: " + PREFS_CALL_DATA);
                Log.e(TAG, "[triggerEndCallButton] currentRoomId: " + currentRoomId);
                Log.e(TAG, "[triggerEndCallButton] currentRoomIdXX: " + currentRoomIdXX);
                Log.e(TAG, "[triggerEndCallButton] myPeerId: " + myPeerId);
                Log.e(TAG, "[triggerEndCallButton] roomIdToUse: " + roomIdToUse);

                handleRoomCleanup(roomIdToUse, myPeerId);

            } catch (Exception e) {
                Log.e(TAG, "[triggerEndCallButton] Exception in main try block: " + e.getMessage(), e);
                cleanupAndExit();
            }
        });
    }

    private void handleRoomCleanup(String roomId, String myPeerId) {

        //   Toast.makeText(mContext, "handleRoomCleanup", Toast.LENGTH_SHORT).show();
        database.child("rooms").child(roomId).child("peers").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "[handleRoomCleanup] Failed to query peers: " + task.getException());
                cleanupAndExit();
                return;
            }

            DataSnapshot snapshot = task.getResult();
            long peerCount = snapshot.getChildrenCount();
            Log.d(TAG, "[handleRoomCleanup] Peer count: " + peerCount);

            if (peerCount <= 2) {
                removeEntireRoom(roomId);
                sendRemoveCallNotificationIfSender();
                cleanupAndExit();
            } else {
                removeMyPeerOnly(roomId, myPeerId);
            }

        });
    }

    private void removeEntireRoom(String roomId) {

        database.child("rooms").child(roomId).removeValue()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "[removeEntireRoom] Room removed successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "[removeEntireRoom] Failed to remove room: " + e.getMessage()));
    }

    private void removeMyPeerOnly(String roomId, String myPeerId) {

        database.child("rooms").child(roomId).child("peers")
                .orderByChild("peerId").equalTo(myPeerId).get()
                .addOnCompleteListener(peerTask -> {
                    if (peerTask.isSuccessful()) {
                        for (DataSnapshot child : peerTask.getResult().getChildren()) {
                            child.getRef().removeValue();
                            Log.d(TAG, "[removeMyPeerOnly] Removed peer: " + myPeerId);
                        }
                    } else {
                        Log.e(TAG, "[removeMyPeerOnly] Failed to remove peer: " + peerTask.getException());
                    }
                    cleanupAndExit();
                });
    }

    private void sendRemoveCallNotificationIfSender() {

        // Toast.makeText(mContext, "sendRemoveCallNotificationIfSender", Toast.LENGTH_SHORT).show();
        boolean iAmSender = getIntent().getBooleanExtra("iAmSender", false);
        if (iAmSender && receiverId != null && !receiverId.isEmpty()) {
            try {
                String pushKey = database.child("removeVideoCallNotification").child(receiverId).push().getKey();
                if (pushKey != null) {
                    database.child("removeVideoCallNotification").child(receiverId).child(pushKey)
                            .setValue(pushKey)
                            .addOnSuccessListener(aVoid ->
                                    Log.d(TAG, "[sendRemoveCallNotificationIfSender] removeCallNotification sent successfully")
                            )
                            .addOnFailureListener(e -> Log.e(TAG, "[sendRemoveCallNotificationIfSender] Failed to send removeCallNotification: " + e.getMessage()));
                } else {
                    Log.e(TAG, "[sendRemoveCallNotificationIfSender] Failed to generate pushKey");
                }
            } catch (Exception e) {
                Log.e(TAG, "[sendRemoveCallNotificationIfSender] Exception: " + e.getMessage(), e);
            }
        }
    }


    private void cleanupAndExit() {
        Log.d(TAG, "[cleanupAndExit] Starting cleanup at " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        runOnUiThread(() -> {
            try {
                // --- Stop WebRTC Media Streams ---
                Log.d(TAG, "[cleanupAndExit] Stopping WebRTC media streams");
                if (binding.webView != null) {
                    binding.webView.evaluateJavascript("javascript:stopAllMediaStreams()", null);
                    binding.webView.evaluateJavascript("javascript:if(typeof localPeer !== 'undefined' && localPeer) { localPeer.close(); localPeer = null; }", null);
                    binding.webView.evaluateJavascript("javascript:if(typeof remotePeer !== 'undefined' && remotePeer) { remotePeer.close(); remotePeer = null; }", null);
                }

                // --- Reset AudioManager ---
                Log.d(TAG, "[cleanupAndExit] Resetting AudioManager");
                try {
                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager != null) {
                        audioManager.setMicrophoneMute(true);  // Explicitly mute microphone
                        audioManager.setMode(AudioManager.MODE_NORMAL);  // Reset to normal mode
                        audioManager.setSpeakerphoneOn(false);  // Turn off speakerphone
                        Log.d(TAG, "[cleanupAndExit] AudioManager reset complete");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "[cleanupAndExit] Error resetting AudioManager: " + e.getMessage(), e);
                }

                // --- Stop Call Service ---
                Log.d(TAG, "[cleanupAndExit] Stopping CallServiceVideoCall");
                Intent stopServiceIntent = new Intent(mContext, CallServiceVideoCall.class);
                stopServiceIntent.setAction(CallServiceVideoCall.ACTION_END_CALL);
                stopService(stopServiceIntent);
                Log.d(TAG, "[cleanupAndExit] Stop service intent sent");

                // --- Remove Firebase listeners ---
                SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                String currentRoomId = callDataPrefs.getString(KEY_MY_ROOM, "");
                if (!currentRoomId.isEmpty()) {
                    if (peersListener != null) {
                        database.child("rooms").child(currentRoomId).child("peers").removeEventListener(peersListener);
                        peersListener = null;
                        Log.d(TAG, "[cleanupAndExit] peersListener removed");
                    }
                    if (signalingListener != null) {
                        database.child("rooms").child(currentRoomId).child("signaling").removeEventListener(signalingListener);
                        signalingListener = null;
                        Log.d(TAG, "[cleanupAndExit] signalingListener removed");
                    }
                }

                // --- Stop WebRTC streams inside WebView ---
                Log.d(TAG, "[cleanupAndExit] Stopping localStream + PeerConnections via JS");
                binding.webView.evaluateJavascript(
                        "javascript:try {" +
                                "if (localStream) {" +
                                "   localStream.getTracks().forEach(track => track.stop());" +
                                "   localStream = null;" +
                                "}" +
                                "if (window.peerConnection) {" +
                                "   peerConnection.getSenders().forEach(s => { try {s.track.stop();} catch(e){} });" +
                                "   peerConnection.close();" +
                                "   window.peerConnection = null;" +
                                "}" +
                                "} catch(e) { console.log('Cleanup error: ' + e); }",
                        value -> Log.d(TAG, "[cleanupAndExit] JS cleanup result: " + value)
                );

                // --- Delay WebView destruction to let JS run ---
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    try {
                        Log.d(TAG, "[cleanupAndExit] Cleaning up WebView");
                        binding.webView.stopLoading();
                        binding.webView.loadUrl("about:blank");
                        binding.webView.clearHistory();
                        binding.webView.clearCache(true);
                        binding.webView.removeAllViews();

                        ViewParent parent = binding.webView.getParent();
                        if (parent instanceof ViewGroup) {
                            ((ViewGroup) parent).removeView(binding.webView);
                            Log.d(TAG, "[cleanupAndExit] WebView removed from parent");
                        }
                        binding.webView.destroy();
                        Log.d(TAG, "[cleanupAndExit] WebView destroyed");
                    } catch (Exception e) {
                        Log.e(TAG, "[cleanupAndExit] Error destroying WebView: " + e.getMessage());
                    }
                }, 500); // 0.5 sec delay

                // --- Force native WebRTC audio release (if using org.webrtc) ---


                // --- Reset AudioManager ---
                AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setMicrophoneMute(false); // false → actually releases mic
                audioManager.stopBluetoothSco();
                audioManager.setBluetoothScoOn(false);
                audioManager.setSpeakerphoneOn(false);
                Log.d(TAG, "[cleanupAndExit] AudioManager reset (mic released)");

                // --- Clear SharedPreferences ---
                Log.d(TAG, "[cleanupAndExit] Clearing SharedPreferences");
                SharedPreferences.Editor editor = callDataPrefs.edit();
                editor.clear().apply();
                Log.d(TAG, "[cleanupAndExit] SharedPreferences cleared");

                Constant.getSfFuncion(mContext);
                Constant.setSF.putString(Constant.audioSet, "").apply();
                Log.d(TAG, "[cleanupAndExit] Constant.audioSet cleared");

                // --- Navigate back to MainActivityOld ---
                Log.d(TAG, "[cleanupAndExit] Navigating to MainActivityOld");
                super.onBackPressed();

                finish();
                Log.d(TAG, "[cleanupAndExit] Activity finished");

            } catch (Exception e) {
                Log.e(TAG, "[cleanupAndExit] Exception during cleanup: " + e.getMessage(), e);
                //     Toast.makeText(mContext, "Failed to clean up call resources", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        triggerEndCallButton();
//        if (isCallEnded && isFinishing()) { // केवळ अ‍ॅक्टिव्हिटी खरोखरच बंद होत असल्यास क्लीनअप करा
//            Log.d(TAG, "Cleaning up resources in onDestroy");
//            stopRingtone();
//
////            if (networkCallback != null) {
////                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
////                if (connectivityManager != null) {
////                    connectivityManager.unregisterNetworkCallback(networkCallback);
////                }
////            }
////
////            if (localBroadcastManager != null) {
////                localBroadcastManager.unregisterReceiver(timerReceiver);
////            }
////
////            if (binding.webView != null) {
////                binding.webView.destroy();
////            }
//
//
//        } else {
//            Log.d(TAG, "onDestroy called but not finishing, keeping resources active");
//        }
    }

    @Override
    public void onBackPressed() {
        stopRingtone();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !isInPictureInPictureMode) {
            Log.d(TAG, "Back button pressed, entering PiP mode");
            enterPiPMode();
        } else {
            goToMainActivityOld();
        }
    }

    public void setAdapter(ArrayList<get_contact_model> get_calling_contact_list,
                           RecyclerView recyclerview,
                           CardView customToastCard,
                           TextView customToastText,
                           String username,
                           String roomId,
                           Dialog dialogLayoutFullScreen) {

        // Get current UID
        Constant.getSfFuncion(mContext);
        String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");

        // Filter list: remove current user, already added participants, and blocked users
        ArrayList<get_contact_model> filteredList = new ArrayList<>();
        for (get_contact_model model : get_calling_contact_list) {
            boolean isNotSelf = !model.getUid().equals(currentUid);
            boolean isNotAlreadyAdded = !participants.contains(model.getUid());
            boolean isNotBlocked = !model.isBlock(); // ✅ Filter out blocked users

            if (isNotSelf && isNotAlreadyAdded && isNotBlocked) {
                filteredList.add(model);
            }
        }

        // Assign filtered list
        this.get_calling_contact_list = filteredList;

        // Set up adapter
        adapter = new get_video_calling_adapter2(
                mContext,
                this.get_calling_contact_list,
                customToastCard,
                customToastText,
                username,
                roomId,
                dialogLayoutFullScreen
        );

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
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
            // Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.searchFilteredData(filteredList);
        }
    }

    private void setupFirebaseListeners() {
        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        roomId = callDataPrefs.getString(KEY_ROOM_ID, "");
        receiverId = callDataPrefs.getString(KEY_RECEIVER_ID, "");

        peersListener = database.child("rooms").child(roomId).child("peers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "Peers data changed: " + snapshot.toString());
                        List<String> peers = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            try {
                                String value = child.getValue(String.class);
                                if (value == null || value.isEmpty()) continue;

                                JSONObject peerData = new JSONObject(value);
                                String peerId = peerData.optString("peerId", null);
                                if (peerId == null || peerId.equals(myPeerId)) continue;

                                String peerName = peerData.optString("name", "Peer " + peerId);
                                String peerPhoto = peerData.optString("photo", "file:///android_asset/user.png");

                                peers.add(peerId);

                                String jsCode = String.format("javascript:setCallerInfo('%s', '%s', '%s')",
                                        escapeJsString(peerName),
                                        escapeJsString(peerPhoto),
                                        escapeJsString(peerId));

                                binding.webView.evaluateJavascript(jsCode, null);
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing peer data: " + e.getMessage());
                            }
                        }

                        try {
                            JSONObject peersJson = new JSONObject();
                            peersJson.put("peers", new JSONArray(peers));
                            if (binding.webView.getProgress() == 100) {
                                binding.webView.evaluateJavascript("javascript:updatePeers(" + peersJson.toString() + ")", value -> {
                                    Log.d(TAG, "updatePeers result: " + value);
                                });
                            } else {
                                Log.w(TAG, "WebView not fully loaded, skipping updatePeers");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error updating peers: " + e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Peers listener cancelled: " + error.getMessage());
                        //    runOnUiThread(() -> Toast.makeText(MainActivityVideoCall.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });

        signalingListener = database.child("rooms").child(roomId).child("signaling")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "Signaling data changed: " + snapshot.toString());
                        for (DataSnapshot child : snapshot.getChildren()) {
                            try {
                                String value = child.getValue(String.class);
                                if (value == null) continue;

                                JSONObject data = new JSONObject(value);
                                String receiver = data.getString("receiver");
                                if (receiver.equals(myPeerId) || receiver.equals("all")) {
                                    if (binding.webView.getProgress() == 100) {
                                        binding.webView.evaluateJavascript("javascript:handleSignalingData(" + data.toString() + ")", value2 -> {
                                            Log.d(TAG, "handleSignalingData result: " + value2);
                                        });
                                        child.getRef().removeValue();
                                    } else {
                                        Log.w(TAG, "WebView not fully loaded, skipping handleSignalingData");
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error handling signaling data: " + e.getMessage(), e);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Signaling listener cancelled: " + error.getMessage());
                        // runOnUiThread(() -> Toast.makeText(MainActivityVideoCall.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private String escapeJsString(String input) {
        return input.replace("\\", "\\\\").replace("'", "\\'");
    }

    private void registerNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest request = new NetworkRequest.Builder().build();

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(@NonNull Network network) {
                runOnUiThread(() -> {
                    binding.webView.evaluateJavascript("javascript:handleNetworkLoss()", null);
                });
            }

            @Override
            public void onAvailable(@NonNull Network network) {
                runOnUiThread(() -> {
                    binding.webView.evaluateJavascript("javascript:handleNetworkResume()", null);
                });
            }
        };

        connectivityManager.registerNetworkCallback(request, networkCallback);
    }

    public void setupDeleteListers() {
        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        String currentRoomIdXX = callDataPrefs.getString(KEY_ROOM_ID, "");

        if (!currentRoomIdXX.isEmpty()) {  // इथे condition उलट करून fix केलं
            database.child("rooms").child(currentRoomIdXX).child("peers")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long peerCount = snapshot.getChildrenCount();
                            //    Toast.makeText(mContext, String.valueOf(peerCount), Toast.LENGTH_SHORT).show();

                            if (peerCount == 0) {
                                if (!isCallEnded) {
                                    triggerEndCallButton();
                                }
                            }
                            Log.d(TAG, "[triggerEndCallButton] Peer count: " + peerCount);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Firebase error: " + error.getMessage());
                        }
                    });
        } else {
            Log.w(TAG, "Room ID is empty, listener not attached.");
        }
    }
}