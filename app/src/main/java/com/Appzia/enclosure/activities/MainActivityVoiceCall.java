package com.Appzia.enclosure.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media3.common.C;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.get_voice_calling_adapter2;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.CallServiceVideoCall;
import com.Appzia.enclosure.Utils.CallServiceVoiceCall;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.MainApplication;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityMainVoiceCallBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivityVoiceCall extends AppCompatActivity {
    private ActivityMainVoiceCallBinding binding;
    private ConnectivityManager.NetworkCallback networkCallback;
    private DatabaseReference database;
    private String roomId, receiverId, myPeerId, myRoom, incoming, photo, name, username, createdBy, roomFlagKey;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private boolean hasRequestedPermissions = false; // Flag to prevent multiple permission requests
    private get_voice_calling_adapter2 adapter;
    private PowerManager.WakeLock wakeLock;
    private TextView customToastText;
    private ValueEventListener peersListener;
    private ArrayList<get_contact_model> get_calling_contact_list = new ArrayList<>();
    private ArrayList<String> participants = new ArrayList<>();
    private Context mContext;
    private CardView customToastCard;
    private LocalBroadcastManager localBroadcastManager;
    private ValueEventListener signalingListener;
    private static final String TAG = "MainActivityVoiceCall";
    private static final String TAG2 = "MainActivityVoiceCallYYYY";
    private boolean isFullScreen = false;
    private boolean isCallEnded = false;
    boolean iAmSender = false;
    private AudioManager audioManager;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximityListener;
    private MediaPlayer ringtonePlayer; // MediaPlayer for ringtone

    // SharedPreferences keys for call data
    public static final String PREFS_CALL_DATA = "CallDataPrefs";
    public static final String KEY_MY_ROOM = "myRoom";
    public static final String PEER_ID = "PEER_ID";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_INCOMING = "incoming";
    public static final String KEY_CREATED_BY = "createdBy";
    public static final String KEY_ROOM_FLAG_KEY = "roomFlagKey";
    public static final String KEY_PHOTO_RECEIVER = "photoReceiver";
    public static final String ReceiverPhoto = "ReceiverPhoto";
    public static final String ReceiverName = "ReceiverName";
    public static final String KEY_PHOTO_RECEIVERXX = "KEY_PHOTO_RECEIVERXX";
    public static final String KEY_NAME_RECEIVER = "nameReceiver";
    public static final String KEY_CALL_START_TIME = "callStartTime";
    public static final String KEY_ROOM_ID = "roomId";
    public static final String KEY_ROOM_IDXX = "roomIdXX";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_IS_SENDER = "isSender";

    @Override
    protected void onResume() {
        super.onResume();
        if (iAmSender) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = dateFormat.format(new Date());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
            String currentTime = timeFormat.format(Calendar.getInstance().getTime());
            SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
            receiverId = callDataPrefs.getString(KEY_RECEIVER_ID, "");
            iAmSender = callDataPrefs.getBoolean(KEY_IS_SENDER, false);

            Constant.getSfFuncion(mContext);
            String uids = Constant.getSF.getString(Constant.UID_KEY, "");

            // TODO: 09/08/25 OUTGOING (0)
            Webservice.create_group_calling(mContext, uids, receiverId, "", date, currentTime, "0", currentTime, "1");

        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = dateFormat.format(new Date());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
            String currentTime = timeFormat.format(Calendar.getInstance().getTime());
            SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
            receiverId = callDataPrefs.getString(KEY_RECEIVER_ID, "");
            iAmSender = callDataPrefs.getBoolean(KEY_IS_SENDER, false);

            Constant.getSfFuncion(mContext);
            String uids = Constant.getSF.getString(Constant.UID_KEY, "");
            // TODO: 09/08/25 INCOMING calling_flag (0)  -  call_type (1) VOICE call  -call_type  (2)Video call
            Webservice.create_group_calling(mContext, uids, receiverId, "", date, currentTime, "1", currentTime, "1");
            // Toast.makeText(mContext, "receiver", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "onResume called, restoring WebView state");
        SharedPreferences prefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        String webViewState = prefs.getString("webViewState", null);
        if (webViewState != null) {
            Bundle bundle = new Bundle();
            bundle.putString("webViewState", webViewState);
            binding.webView.restoreState(bundle); // WebView स्टेट रिस्टोअर करा
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called, saving WebView state");
        if (binding.webView != null) { // Check if WebView is valid
            try {
                Bundle webViewState = new Bundle();
                binding.webView.saveState(webViewState);
                SharedPreferences prefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("webViewState", webViewState.toString());
                editor.apply();
            } catch (Exception e) {
                Log.e(TAG, "Error saving WebView state: " + e.getMessage(), e);
            }
        } else {
            Log.w(TAG, "WebView is null or destroyed, skipping saveState");
        }
    }

    private final BroadcastReceiver timerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CallServiceVoiceCall.ACTION_TIMER_UPDATE.equals(intent.getAction())) {
                long durationSeconds = intent.getLongExtra("duration_seconds", 0);
                String time = String.format("%02d:%02d", durationSeconds / 60, durationSeconds % 60);
                if (!isFinishing() && !isDestroyed() && binding != null && binding.webView != null) {
                    binding.webView.evaluateJavascript("javascript:document.getElementById('callTimer').textContent = '" + time + "'", null);
                }
                Log.d(TAG, "Timer updated: " + time);
            } else if ("com.Appzia.enclosure.END_CALL".equals(intent.getAction())) {
                Log.d(TAG, "Received END_CALL broadcast, triggering end call");
                triggerEndCallButton();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainVoiceCallBinding.inflate(getLayoutInflater());

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

        setContentView(binding.getRoot());
        mContext = this;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        Intent intent = getIntent();
        boolean iAmSenderNew = intent.getBooleanExtra("iAmSenderNew", false);


        intent.removeExtra("iAmSenderNew");


        if (iAmSenderNew) {
            startRingtone();
        }


        database = FirebaseDatabase.getInstance().getReference();
        customToastCard = findViewById(R.id.includedToast);
        customToastText = customToastCard.findViewById(R.id.toastText);

        // Retrieve data from Intent
        String intentRoomFlagKey = getIntent().getStringExtra("roomFlagKey");
        String intentPhoto = getIntent().getStringExtra("photoReceiver");
        String intentName = getIntent().getStringExtra("nameReceiver");
        Constant.setSfFunction(mContext);
        Constant.setSF.putString(Constant.photoEmergency, intentPhoto);
        Constant.setSF.putString(Constant.nameEmergency, intentName);
        Constant.setSF.apply();
        String intentRoomId = getIntent().getStringExtra("roomId");
        String intentReceiverId = getIntent().getStringExtra("receiverId");
        String intentMyRoom = getIntent().getStringExtra("myRoom");
        String intentUsername = getIntent().getStringExtra("username");
        String intentIncoming = getIntent().getStringExtra("incoming");
        String intentCreatedBy = getIntent().getStringExtra("createdBy");
        boolean intentIsSender = getIntent().getBooleanExtra("iAmSender", false);


        //  senderId = getIntent().getStringExtra("senderId");
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        receiverId = getIntent().getStringExtra("receiverId");
        roomFlagKey = getIntent().getStringExtra("roomFlagKey");


        String deleteroom = receiverId + uid;

        DatabaseReference reference = database
                .child("declineVoiceKey")
                .child(deleteroom);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && mContext != null) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            database.child("declineVoiceKey").child(deleteroom).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    String calldroptext = "Call Dropped";
                                    String jsCode = "javascript:setCallStatus('" + calldroptext.replace("'", "\\'") + "')";
                                    binding.webView.evaluateJavascript(jsCode, null);
                                    stopRingtone();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        Constant.vibrateOneSecond(mContext);
                                    }

                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                        cleanupAndExit();
                                    }, 2000); // 500ms delay


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


        Log.d(TAG, "onCreate - name: " + name + ", photo: " + photo);

        // Save all call-related data to SharedPreferences (original values from Intent)
        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = callDataPrefs.edit();
        editor.putString(KEY_MY_ROOM, intentMyRoom);
        editor.putString(KEY_USERNAME, intentUsername);
        editor.putString(KEY_INCOMING, intentIncoming);
        editor.putString(KEY_CREATED_BY, intentCreatedBy);
        editor.putString(KEY_ROOM_FLAG_KEY, intentRoomFlagKey);
        editor.putString(KEY_PHOTO_RECEIVER, intentPhoto); // Save original photo
        editor.putString(KEY_PHOTO_RECEIVERXX, intentPhoto); // Save original photo
        editor.putString(KEY_NAME_RECEIVER, intentName);   // Save original name
        editor.putLong(KEY_CALL_START_TIME, System.currentTimeMillis());
        editor.putString(KEY_ROOM_ID, intentRoomId);
        editor.putString(KEY_RECEIVER_ID, intentReceiverId);

        editor.apply();

        // Save name and photo to SharedPreferences for CallServiceVoiceCall (Constant.getSF)
        Constant.getSfFuncion(mContext);
        SharedPreferences.Editor constantEditor = Constant.getSF.edit();
        constantEditor.putString(Constant.callName, name); // Use the potentially defaulted 'name'
        constantEditor.putString(Constant.photoReceiver, photo); // Use the potentially defaulted 'photo'
        constantEditor.apply();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (MainApplication.player != null && MainApplication.player.isPlaying()) {
            MainApplication.player.stop();
            MainApplication.player.release();
            MainApplication.player = null;
        }
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(100);
        } catch (Exception e) {
            Log.e(TAG, "Error cancelling notification: " + e.getMessage());
        }

        // Initialize and play ringtone based on iAmSender from SharedPreferences


        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(CallServiceVoiceCall.ACTION_TIMER_UPDATE);
        filter.addAction("com.Appzia.enclosure.END_CALL");
        localBroadcastManager.registerReceiver(timerReceiver, filter);
        Intent requestIntent = new Intent(CallServiceVoiceCall.ACTION_REQUEST_DURATION);
        localBroadcastManager.sendBroadcast(requestIntent);

        setupWebView();

        setupFirebaseListeners();
        //    setupDeleteListers();
    }

    private void startRingtone() {
        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            // Reset audio configurations to ensure a clean state
            audioManager.setMode(AudioManager.MODE_NORMAL); // Reset to normal mode first
            audioManager.setSpeakerphoneOn(false); // Explicitly disable speakerphone
            audioManager.setBluetoothScoOn(false); // Disable Bluetooth SCO
            audioManager.stopBluetoothSco(); // Stop any active Bluetooth SCO
            audioManager.setMicrophoneMute(false); // Ensure microphone is unmuted
            Log.d(TAG, "Audio configurations reset: speakerphone off, Bluetooth SCO off");

            // Set audio mode to communication for voice call routing
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            Log.d(TAG, "Audio mode set to MODE_IN_COMMUNICATION");

            // Route audio to earpiece for Android 12 (API 31) and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AudioDeviceInfo[] outputDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
                boolean earpieceFound = false;
                for (AudioDeviceInfo device : outputDevices) {
                    if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_EARPIECE) {
                        audioManager.setCommunicationDevice(device);
                        earpieceFound = true;
                        Log.d(TAG, "Audio routed to earpiece using setCommunicationDevice");
                        break;
                    }
                }
                if (!earpieceFound) {
                    Log.w(TAG, "Earpiece not found, falling back to default audio routing");
                    // Toast.makeText(this, "Earpiece not available, using default audio output", Toast.LENGTH_SHORT).show();
                }
            } else {
                // For older Android versions, ensure speakerphone is off to use earpiece
                audioManager.setSpeakerphoneOn(false);
                Log.d(TAG, "Speakerphone disabled for older Android versions to use earpiece");
            }

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
        }
    }

    private void setupWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // कॅशिंग सक्षम करा
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null); // हार्डवेअर ॲक्सलरेशन

        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                runOnUiThread(() -> {
                    try {
                        String[] requestedResources = request.getResources();
                        List<String> permissionsToGrant = new ArrayList<>();
                        for (String resource : requestedResources) {
                            if (PermissionRequest.RESOURCE_AUDIO_CAPTURE.equals(resource)) {
                                permissionsToGrant.add(resource);
                            }
                        }
                        if (!permissionsToGrant.isEmpty()) {
                            Log.d(TAG, "Granting WebView permissions: " + permissionsToGrant);
                            request.grant(permissionsToGrant.toArray(new String[0]));
                        } else {
                            Log.w(TAG, "No valid permissions to grant: " + String.join(", ", requestedResources));
                            request.deny();
                        }
                    } catch (IllegalStateException e) {
                        Log.e(TAG, "Permission request error: " + e.getMessage());
                        //   Toast.makeText(MainActivityVoiceCall.this, "Permission error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "WebView page finished loading: " + url);

                // Retrieve photo and name from SharedPreferences for WebView, applying defaults if needed
                SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                String photoToDisplay = callDataPrefs.getString(KEY_PHOTO_RECEIVER, "");
                String photoToDisplayXXX = callDataPrefs.getString(KEY_PHOTO_RECEIVERXX, "");
                String nameToDisplay = callDataPrefs.getString(KEY_NAME_RECEIVER, "");

                String intentPhoto = getIntent().getStringExtra("photoReceiver");
                String intentName = getIntent().getStringExtra("nameReceiver");

                if (intentName != null && !intentName.isEmpty()) {
                    Constant.setSF.putString(ReceiverPhoto, intentPhoto);
                    Constant.setSF.apply();
                    Constant.setSF.putString(ReceiverName, intentName);
                    Constant.setSF.apply();
                }

                String getReceiverPhoto = Constant.getSF.getString(ReceiverPhoto, "file:///android_asset/user.png");
                String getReceiverName = Constant.getSF.getString(ReceiverName, "Name");

                Constant.getSfFuncion(mContext);


                view.evaluateJavascript("javascript:setRemoteCallerInfo('" + getReceiverPhoto + "', '" + getReceiverName + "')", null);


                // ✅ Safe to call this after page is fully loaded
                registerNetworkCallback();
                setupProximitySensor();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "WebView error: code=" + errorCode + ", description=" + description + ", url=" + failingUrl);
                // Toast.makeText(MainActivityVoiceCall.this, "WebView error: " + description, Toast.LENGTH_SHORT).show();
            }
        });

        binding.webView.addJavascriptInterface(new WebAppInterface(), "Android");
        binding.webView.loadUrl("file:///android_asset/indexVoice.html");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Retrieve photo and name from SharedPreferences for WebView, applying defaults if needed
            SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);


            String intentPhoto = getIntent().getStringExtra("photoReceiver");
            String intentName = getIntent().getStringExtra("nameReceiver");

            if (intentName != null && !intentName.isEmpty()) {
                Constant.setSF.putString(ReceiverPhoto, intentPhoto);
                Constant.setSF.apply();
                Constant.setSF.putString(ReceiverName, intentName);
                Constant.setSF.apply();
            }

            String getReceiverPhoto = Constant.getSF.getString(ReceiverPhoto, "file:///android_asset/user.png");
            String getReceiverName = Constant.getSF.getString(ReceiverName, "Name");


            Constant.getSfFuncion(mContext);
            binding.webView.evaluateJavascript("javascript:setRemoteCallerInfo('" + getReceiverPhoto + "', '" + getReceiverName + "')", null);

        }, 100);

    }

    private void setupProximitySensor() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "MyApp:ProximityWakeLock");

        if (proximitySensor != null) {
            proximityListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    boolean isNear = event.values[0] < proximitySensor.getMaximumRange();
                    Log.d(TAG, "Proximity sensor: " + (isNear ? "Near" : "Far"));
                    binding.webView.evaluateJavascript("javascript:onProximityChanged(" + isNear + ")", null);

                    if (isNear && !wakeLock.isHeld()) {
                        wakeLock.acquire(1 * 60 * 1000L /* 1 minute */);
                    } else if (!isNear && wakeLock.isHeld()) {
                        wakeLock.release();
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
            sensorManager.registerListener(proximityListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.w(TAG, "Proximity sensor not available");
        }
    }

    private void checkBluetoothAvailability() {
        boolean isBluetoothAvailable = audioManager.isBluetoothA2dpOn() || audioManager.isBluetoothScoOn();
        binding.webView.evaluateJavascript("javascript:setBluetoothAvailability(" + isBluetoothAvailable + ")", null);
    }

    private void requestPermissions() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), PERMISSION_REQUEST_CODE);
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
                onPermissionsGranted();
            } else {
                Toast.makeText(this, "Microphone permission denied", Toast.LENGTH_SHORT).show();
                stopRingtone();
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

        Intent serviceIntent = new Intent(this, CallServiceVoiceCall.class);
        // No need to put extras here, data is already in SharedPreferences
        // serviceIntent.putExtra("myRoom", myRoom);
        // serviceIntent.putExtra("username", username);
        // serviceIntent.putExtra("incoming", incoming);
        // serviceIntent.putExtra("createdBy", createdBy);
        // serviceIntent.putExtra("roomFlagKey", roomFlagKey);
        // serviceIntent.putExtra("photoReceiver", photo);
        // serviceIntent.putExtra("nameReceiver", name);
        // serviceIntent.putExtra("callStartTime", System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class WebAppInterface {

        @JavascriptInterface
        public void syncParticipants() {
            // Fetch current peers from Firebase and update JS via evaluateJavascript
            database.child("rooms").child(roomId).child("peers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Parse peers and send to JS as in setupFirebaseListeners
                    // ... similar code to updatePeers in JS
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        @JavascriptInterface
        public void pingServer() {
            // Simple ping to Firebase or a server to check connectivity
            database.child("ping").setValue(System.currentTimeMillis()).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    runOnUiThread(() -> binding.webView.evaluateJavascript("javascript:handleNetworkLoss()", null));
                }
            });
        }

        @JavascriptInterface
        public void onCallConnected() {
            runOnUiThread(() -> {
                stopRingtone(); // Stop ringtone when call connects
                if (!hasRequestedPermissions) {
                    Log.d(TAG, "Call connected, requesting permissions");
                    requestPermissions();
                    hasRequestedPermissions = true;
                }

                SharedPreferences prefs = mContext.getSharedPreferences("CallDataPrefs", Context.MODE_PRIVATE);
                boolean isMicMuted = prefs.getBoolean("isMicMuted", false);
                runOnUiThread(() -> {
                    String message = isMicMuted ? "Microphone is muted" : "Microphone is unmuted";
                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    if (isMicMuted) {

                    } else {
                        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                        audioManager.setMicrophoneMute(false); // Explicitly unmute
                        Log.d(TAG, "Microphone set to unmuted on call connect");

                        SharedPreferences prefs567 = mContext.getSharedPreferences("CallDataPrefs", Context.MODE_PRIVATE);
                        prefs567.edit().putBoolean("isMicMuted", false).apply();

                    }

                });
            });
        }

        @JavascriptInterface
        public void callOnBackPressed() {
            runOnUiThread(() -> {
                stopRingtone(); // Stop ringtone on back press
                onBackPressed();
            });
        }

        @JavascriptInterface
        public void onPageReady() {
            runOnUiThread(() -> {
                // Retrieve data from SharedPreferences for WebView, applying defaults if needed
                SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                String roomId = callDataPrefs.getString(KEY_ROOM_ID, "");
                String nameToDisplay = callDataPrefs.getString(KEY_NAME_RECEIVER, "");
                String photoToDisplay = callDataPrefs.getString(KEY_PHOTO_RECEIVER, "");

                if (photoToDisplay == null || photoToDisplay.isEmpty())
                    photoToDisplay = "file:///android_asset/user.png";
                if (nameToDisplay == null || nameToDisplay.isEmpty()) nameToDisplay = "Name";


                binding.webView.evaluateJavascript("javascript:setRoomId('" + roomId + "')", null);
                binding.webView.evaluateJavascript("javascript:setCallerInfo('" + nameToDisplay + "', '" + photoToDisplay + "', 'self')", null);
                String themeColorHex = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                binding.webView.evaluateJavascript("javascript:setThemeColor('" + themeColorHex + "')", null);
                checkBluetoothAvailability();
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
        public void addParticipant(String peerId, String name, String photo) {
            runOnUiThread(() -> {
                Log.d(TAG, "Adding participant: peerId=" + peerId + ", name=" + name + ", photo=" + photo);
                binding.webView.evaluateJavascript("javascript:setCallerInfo('" + name + "', '" + photo + "', '" + peerId + "')", null);
            });
        }

        @JavascriptInterface
        public void sendPeerId(String peerId) {
            runOnUiThread(() -> {
                myPeerId = peerId;
                try {
                    Constant.getSfFuncion(mContext);
                    String name = Constant.getSF.getString(Constant.full_name, "");
                    String photo = Constant.getSF.getString(Constant.profilePic, "");

                    SharedPreferences callDataPrefs4 = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                    String peer = callDataPrefs4.getString(PEER_ID, "");

                    //   Toast.makeText(MainActivityVoiceCall.this, peer, Toast.LENGTH_SHORT).show();

                    JSONObject peerData;
                    if (!peer.isEmpty()) {
                        peerData = new JSONObject();
                        peerData.put("peerId", peer);
                        peerData.put("name", name);
                        peerData.put("photo", photo);
                        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                        String currentRoomId = callDataPrefs.getString(KEY_ROOM_ID, "");
                        SharedPreferences.Editor editor = callDataPrefs.edit();
                        editor.putString(PEER_ID, peer);
                        editor.putString(KEY_ROOM_ID, currentRoomId);
                        if (!currentRoomId.isEmpty()) {
                            editor.putString(KEY_ROOM_IDXX, currentRoomId);
                        }
                        editor.apply();
                        // Toast.makeText(MainActivityVoiceCall.this, currentRoomId, Toast.LENGTH_SHORT).show();
                        if (!currentRoomId.isEmpty()) {
                            database.child("rooms").child(currentRoomId).child("peers")
                                    .child(peerId)
                                    .setValue(peerData.toString())
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "✅ Peer uploaded with ID: " + peerId))
                                    .addOnFailureListener(e -> Log.e(TAG, "❌ Upload failed: " + e.getMessage()));
                        } else {
                            SharedPreferences callDataPrefs5 = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                            String currentRoomIdXX = callDataPrefs5.getString(KEY_ROOM_IDXX, "");
                            database.child("rooms").child(currentRoomIdXX).child("peers")
                                    .child(peer)
                                    .setValue(peerData.toString())
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "✅ Peer uploaded with ID: " + peerId))
                                    .addOnFailureListener(e -> Log.e(TAG, "❌ Upload failed: " + e.getMessage()));
                        }

                    } else {
                        peerData = new JSONObject();
                        peerData.put("peerId", peerId);
                        peerData.put("name", name);
                        peerData.put("photo", photo);

                        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                        String currentRoomId = callDataPrefs.getString(KEY_ROOM_ID, "");
                        SharedPreferences.Editor editor = callDataPrefs.edit();
                        editor.putString(PEER_ID, peerId);
                        editor.putString(KEY_ROOM_ID, currentRoomId);
                        if (!currentRoomId.isEmpty()) {
                            editor.putString(KEY_ROOM_IDXX, currentRoomId);
                        }
                        editor.apply();
                        //  Toast.makeText(MainActivityVoiceCall.this, currentRoomId, Toast.LENGTH_SHORT).show();

                        if (!currentRoomId.isEmpty()) {
                            database.child("rooms").child(currentRoomId).child("peers")
                                    .child(peerId)
                                    .setValue(peerData.toString())
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "✅ Peer uploaded with ID: " + peerId))
                                    .addOnFailureListener(e -> Log.e(TAG, "❌ Upload failed: " + e.getMessage()));
                        } else {
                            SharedPreferences callDataPrefs5 = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                            String currentRoomIdXX = callDataPrefs5.getString(KEY_ROOM_IDXX, "");
                            database.child("rooms").child(currentRoomIdXX).child("peers")
                                    .child(peer)
                                    .setValue(peerData.toString())
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "✅ Peer uploaded with ID: " + peerId))
                                    .addOnFailureListener(e -> Log.e(TAG, "❌ Upload failed: " + e.getMessage()));
                        }

                    }

                    setupDeleteListers();
                } catch (Exception e) {
                    Log.e(TAG, "❌ Error sending peer data: " + e.getMessage());
                }
            });
        }


        @JavascriptInterface
        public void sendSignalingData(String data) {
            // Retrieve roomId from SharedPreferences
            SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
            String currentRoomId = callDataPrefs.getString(KEY_ROOM_ID, "");
            database.child("rooms").child(currentRoomId).child("signaling").push().setValue(data);
        }

        @JavascriptInterface
        public void endCall() {
            runOnUiThread(MainActivityVoiceCall.this::triggerEndCallButton);
        }


        @JavascriptInterface
        public void setAudioOutput(String output) {
            runOnUiThread(() -> {
                try {
                    //  binding.webView.evaluateJavascript("javascript:restoreMuteState()", null);
                    Constant.getSfFuncion(mContext);

                    AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    Log.d(TAG, "Requested audio output: " + output);

                    Intent intent4 = getIntent();
                    boolean iAmBroadcast = intent4.getBooleanExtra("iAmBroadcast", false);

                    intent4.removeExtra("iAmBroadcast");
                    if (iAmBroadcast) {

                        binding.webView.evaluateJavascript("javascript:setCallStatus('Connected')", null); // कॉल स्टेटस अपडेट करा

                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        AudioDeviceInfo[] outputDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
                        Log.d(TAG, "Available output devices count: " + outputDevices.length);

                        for (AudioDeviceInfo device : outputDevices) {
                            int type = device.getType();
                            Log.d(TAG, "Found device: " + typeToString(type));

                            if (output.equals("earpiece") && type == AudioDeviceInfo.TYPE_BUILTIN_EARPIECE) {
                                boolean success = audioManager.setCommunicationDevice(device);
                                Log.d(TAG, "Attempted switch to earpiece: " + success);
                                Constant.getSfFuncion(mContext);
                                Constant.setSF.putString(Constant.audioSet, "earpiece");
                                Constant.setSF.apply();


                                // Toast.makeText(mContext, "earpiece", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (output.equals("speaker") && type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                                boolean success = audioManager.setCommunicationDevice(device);
                                Log.d(TAG, "Attempted switch to speaker: " + success);
                                //   Toast.makeText(mContext, "Speaker", Toast.LENGTH_SHORT).show();

                                Constant.getSfFuncion(mContext);
                                Constant.setSF.putString(Constant.audioSet, "speaker");
                                Constant.setSF.apply();

//                                Intent intent = getIntent();
//                                boolean iAmSenderNew = intent.getBooleanExtra("iAmSenderNew", false);
//
//                                intent.removeExtra("iAmSenderNew");
//
//                                if (iAmSenderNew) {
//                                    startRingtone();
//                                }
                                return;
                            }

                            if (output.equals("bluetooth") &&
                                    (type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO || type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {

                                if (!audioManager.isBluetoothScoAvailableOffCall()) {
                                    Log.w(TAG, "Bluetooth SCO not available off call");
                                    Toast.makeText(mContext, "Bluetooth SCO not available", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                audioManager.startBluetoothSco();
                                audioManager.setBluetoothScoOn(true);
                                boolean success = audioManager.setCommunicationDevice(device);
                                Log.d(TAG, "Attempted switch to bluetooth: " + success);
                                Constant.getSfFuncion(mContext);
                                Constant.setSF.putString(Constant.audioSet, "bluetooth");
                                Constant.setSF.apply();
                                return;
                            }
                        }

                        Log.w(TAG, "No matching device found for output: " + output + " - Falling back to legacy method");
                    }

                    // Legacy fallback
                    switch (output) {
                        case "earpiece":
                            Log.d(TAG, "Fallback: Switching to earpiece");
                            audioManager.stopBluetoothSco();
                            audioManager.setBluetoothScoOn(false);
                            audioManager.setSpeakerphoneOn(false);
                            Constant.getSfFuncion(mContext);
                            Constant.setSF.putString(Constant.audioSet, "earpiece");
                            Constant.setSF.apply();
                            break;
                        case "speaker":
                            Log.d(TAG, "Fallback: Switching to speaker");
                            audioManager.stopBluetoothSco();
                            audioManager.setBluetoothScoOn(false);
                            audioManager.setSpeakerphoneOn(true);
                            Constant.getSfFuncion(mContext);
                            Constant.setSF.putString(Constant.audioSet, "speaker");
                            Constant.setSF.apply();
                            break;
                        case "bluetooth":
                            Log.d(TAG, "Fallback: Attempting Bluetooth");
                            if (!audioManager.isBluetoothScoAvailableOffCall()) {
                                Log.w(TAG, "Bluetooth SCO not available");
                                Toast.makeText(mContext, "Bluetooth SCO not available", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            audioManager.startBluetoothSco();
                            audioManager.setBluetoothScoOn(true);
                            audioManager.setSpeakerphoneOn(false);
                            Constant.getSfFuncion(mContext);
                            Constant.setSF.putString(Constant.audioSet, "bluetooth");
                            Constant.setSF.apply();
                            break;
                    }

                    Log.d(TAG, "Audio output set via fallback: " + output);

                } catch (Exception e) {
                    Log.e(TAG, "Error setting audio output: " + e.getMessage(), e);
                    Toast.makeText(mContext, "Failed to set audio output", Toast.LENGTH_SHORT).show();
                }
            });
        }


        @JavascriptInterface
        public void checkBluetoothAvailability() {
            runOnUiThread(() -> {
                boolean isBluetoothAvailable = audioManager.isBluetoothA2dpOn() || audioManager.isBluetoothScoOn();
                binding.webView.evaluateJavascript("javascript:setBluetoothAvailability(" + isBluetoothAvailable + ")", null);
            });
        }

        @JavascriptInterface
        public void showToast(String message) {
            runOnUiThread(() -> Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show());
        }


        @JavascriptInterface
        public void toggleMicrophone(boolean mute) {
            runOnUiThread(() -> {
                try {
                    AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    audioManager.setMicrophoneMute(mute);
                    Log.d(TAG, "Microphone mute state set to: " + mute);
                    String status = mute ? "Microphone muted" : "Connected";
                    binding.webView.evaluateJavascript("javascript:setCallStatus('" + status + "')", null);
                } catch (Exception e) {
                    Log.e(TAG, "Error toggling microphone: " + e.getMessage(), e);
                    Toast.makeText(mContext, "Failed to toggle microphone", Toast.LENGTH_SHORT).show();
                    binding.webView.evaluateJavascript("javascript:setCallStatus('Microphone toggle failed')", null);
                }
            });
        }

        @JavascriptInterface
        public void saveMuteState(boolean mute) {
            SharedPreferences prefs = mContext.getSharedPreferences("CallDataPrefs", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("isMicMuted", mute).apply();
            runOnUiThread(() -> {
                String message = mute ? "Microphone muted" : "Microphone unmuted";
                //   Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Saved mute state: " + mute);
            });
        }

        @JavascriptInterface
        public boolean getMuteState() {
            SharedPreferences prefs = mContext.getSharedPreferences("CallDataPrefs", Context.MODE_PRIVATE);
            boolean isMicMuted = prefs.getBoolean("isMicMuted", false);
            runOnUiThread(() -> {
                String message = isMicMuted ? "Microphone is muted" : "Microphone is unmuted";
                //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                if (isMicMuted) {

                } else {
//                    AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
//                    audioManager.setMicrophoneMute(false); // Explicitly unmute
//                    Log.d(TAG, "Microphone set to unmuted on call connect");
//
//                    SharedPreferences prefs567 = mContext.getSharedPreferences("CallDataPrefs", Context.MODE_PRIVATE);
//                    prefs567.edit().putBoolean("isMicMuted", false).apply();

                }

            });
            return isMicMuted;
        }
    }

    private String typeToString(int type) {
        switch (type) {
            case AudioDeviceInfo.TYPE_BUILTIN_EARPIECE:
                return "EARPIECE";
            case AudioDeviceInfo.TYPE_BUILTIN_SPEAKER:
                return "SPEAKER";
            case AudioDeviceInfo.TYPE_BLUETOOTH_SCO:
                return "BLUETOOTH_SCO";
            case AudioDeviceInfo.TYPE_BLUETOOTH_A2DP:
                return "BLUETOOTH_A2DP";
            default:
                return "OTHER(" + type + ")";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        if (isCallEnded && isFinishing()) { // केवळ अ‍ॅक्टिव्हिटी खरोखरच बंद होत असल्यास क्लीनअप करा
            Log.d(TAG, "Cleaning up resources in onDestroy");
            stopRingtone();
            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
            }
            if (networkCallback != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    connectivityManager.unregisterNetworkCallback(networkCallback);
                }
            }
            if (proximityListener != null) {
                sensorManager.unregisterListener(proximityListener);
            }
            if (localBroadcastManager != null) {
                localBroadcastManager.unregisterReceiver(timerReceiver);
            }

            if (binding.webView != null) {
                binding.webView.destroy();
            }
        } else {
            Log.d(TAG, "onDestroy called but not finishing, keeping resources active");
        }
    }


    public void triggerEndCallButton() {


        Log.d(TAG2, "[triggerEndCallButton] Starting end call process at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        isCallEnded = true;

        runOnUiThread(() -> {
            try {
                stopRingtone();
                SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                String currentRoomId = callDataPrefs.getString(KEY_ROOM_ID, "");
                String currentRoomIdXX = callDataPrefs.getString(KEY_ROOM_IDXX, "");
                String myPeerId = callDataPrefs.getString(PEER_ID, "");
                String roomIdToUse = currentRoomId.isEmpty() ? currentRoomIdXX : currentRoomId;
                Log.d(TAG2, "[triggerEndCallButton] Using roomId: " + roomIdToUse);
                handleRoomCleanup(roomIdToUse, myPeerId);

            } catch (Exception e) {
                Log.e(TAG2, "[triggerEndCallButton] Exception in main try block: " + e.getMessage(), e);
                cleanupAndExit();
            }
        });
    }

    private void handleRoomCleanup(String roomId, String myPeerId) {

        //   Toast.makeText(mContext, "handleRoomCleanup", Toast.LENGTH_SHORT).show();
        database.child("rooms").child(roomId).child("peers").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG2, "[handleRoomCleanup] Failed to query peers: " + task.getException());
                cleanupAndExit();
                return;
            }

            DataSnapshot snapshot = task.getResult();
            long peerCount = snapshot.getChildrenCount();
            Log.d(TAG2, "[handleRoomCleanup] Peer count: " + peerCount);

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
                .addOnSuccessListener(aVoid -> Log.d(TAG2, "[removeEntireRoom] Room removed successfully"))
                .addOnFailureListener(e -> Log.e(TAG2, "[removeEntireRoom] Failed to remove room: " + e.getMessage()));
    }

    private void removeMyPeerOnly(String roomId, String myPeerId) {

        database.child("rooms").child(roomId).child("peers")
                .orderByChild("peerId").equalTo(myPeerId).get()
                .addOnCompleteListener(peerTask -> {
                    if (peerTask.isSuccessful()) {
                        for (DataSnapshot child : peerTask.getResult().getChildren()) {
                            child.getRef().removeValue();
                            Log.d(TAG2, "[removeMyPeerOnly] Removed peer: " + myPeerId);
                        }
                    } else {
                        Log.e(TAG2, "[removeMyPeerOnly] Failed to remove peer: " + peerTask.getException());
                    }
                    cleanupAndExit();
                });
    }

    private void sendRemoveCallNotificationIfSender() {

        // Toast.makeText(mContext, "sendRemoveCallNotificationIfSender", Toast.LENGTH_SHORT).show();
        boolean iAmSender = getIntent().getBooleanExtra("iAmSender", false);
        if (iAmSender && receiverId != null && !receiverId.isEmpty()) {
            try {
                String pushKey = database.child("removeCallNotification").child(receiverId).push().getKey();
                if (pushKey != null) {
                    database.child("removeCallNotification").child(receiverId).child(pushKey)
                            .setValue(pushKey)
                            .addOnSuccessListener(aVoid ->
                                    Log.d(TAG2, "[sendRemoveCallNotificationIfSender] removeCallNotification sent successfully")
                            )
                            .addOnFailureListener(e -> Log.e(TAG2, "[sendRemoveCallNotificationIfSender] Failed to send removeCallNotification: " + e.getMessage()));
                } else {
                    Log.e(TAG2, "[sendRemoveCallNotificationIfSender] Failed to generate pushKey");
                }
            } catch (Exception e) {
                Log.e(TAG2, "[sendRemoveCallNotificationIfSender] Exception: " + e.getMessage(), e);
            }
        }
    }


    private void cleanupAndExit() {
        Log.d(TAG, "[cleanupAndExit] Starting cleanup at " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        runOnUiThread(() -> {
            try {
                // --- Stop Call Service ---
                Log.d(TAG, "[cleanupAndExit] Stopping CallServiceVoiceCall");
                Intent stopServiceIntent = new Intent(mContext, CallServiceVoiceCall.class);
                stopServiceIntent.setAction(CallServiceVoiceCall.ACTION_END_CALL);
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
                //       Toast.makeText(mContext, "Failed to clean up call resources", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void goToMainActivityOld() {
        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        receiverId = callDataPrefs.getString(KEY_RECEIVER_ID, "");
        iAmSender = callDataPrefs.getBoolean(KEY_IS_SENDER, false);
        super.onBackPressed();
        ((Activity) mContext).finish(); // Finish current activity

        SharedPreferences callDataPrefs5 = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = callDataPrefs5.edit();
        editor.putString(KEY_MY_ROOM, "");
        editor.putString(KEY_USERNAME, "");
        editor.putString(KEY_INCOMING, "");
        editor.putString(KEY_CREATED_BY, "");
        editor.putString(KEY_ROOM_FLAG_KEY, "");
        editor.putString(KEY_PHOTO_RECEIVER, ""); // Save original photo
        editor.putString(KEY_NAME_RECEIVER, "");   // Save original name
        editor.putLong(KEY_CALL_START_TIME, 0);
        editor.putString(KEY_ROOM_ID, "");
        editor.putString(KEY_ROOM_IDXX, "");
        editor.putString(PEER_ID, "");
        editor.putString(KEY_RECEIVER_ID, "");
        editor.putBoolean(KEY_IS_SENDER, false);
        editor.apply();


        // Destroy WebView completely
        if (binding.webView != null) {
            // 1. Stop loading and remove from parent
            binding.webView.stopLoading();
            binding.webView.loadUrl("about:blank");
            binding.webView.clearHistory();
            binding.webView.clearCache(true);
            binding.webView.removeAllViews();

            // 2. Stop microphone
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMicrophoneMute(true); // Mute the microphone
            audioManager.setMode(AudioManager.MODE_NORMAL); // Reset audio mode
            Log.d(TAG, "Microphone muted and audio mode reset during WebView cleanup");

            // 3. Notify JavaScript to stop localStream (optional, if WebRTC is still active)
            binding.webView.evaluateJavascript("javascript:if (localStream) { localStream.getTracks().forEach(track => track.stop()); localStream = null; }", null);

            ViewParent parent = binding.webView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(binding.webView);
            }

            // 2. Destroy and nullify
            binding.webView.destroy();

            Constant.getSfFuncion(mContext);
            Constant.setSF.putString(Constant.audioSet, "");
            Constant.setSF.apply();

            SharedPreferences prefs = mContext.getSharedPreferences("CallDataPrefs", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("isMicMuted", false).apply();

        }


    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "Back button pressed, navigating to MainActivityOld");
        stopRingtone(); // रिंगटोन बंद करा
        binding.webView.evaluateJavascript("javascript:setCallStatus('Connected')", null); // कॉल स्टेटस अपडेट करा

        // WebView स्टेट सेव्ह करा
        Bundle webViewState = new Bundle();
        binding.webView.saveState(webViewState);
        SharedPreferences prefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("webViewState", webViewState.toString());
        editor.apply();

        // MainActivityOld वर रीडायरेक्ट करा
        super.onBackPressed();
        // finish() कॉल करू नका, जेणेकरून MainActivityVoiceCall सक्रिय राहील
    }

    public void setAdapter(ArrayList<get_contact_model> get_calling_contact_list,
                           RecyclerView recyclerview,
                           CardView customToastCard,
                           TextView customToastText,
                           String username,
                           String roomId,
                           Dialog dialogLayoutFullScreen) {

        Constant.getSfFuncion(mContext);
        String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");

        ArrayList<get_contact_model> filteredList = new ArrayList<>();

        for (get_contact_model model : get_calling_contact_list) {
            boolean isNotSelf = !model.getUid().equals(currentUid);
            boolean isNotAlreadyAdded = !participants.contains(model.getUid());
            boolean isNotBlocked = !model.isBlock();  // ✅ only keep if NOT blocked

            if (isNotSelf && isNotAlreadyAdded && isNotBlocked) {
                filteredList.add(model);
            }
        }

        this.get_calling_contact_list = filteredList;

        adapter = new get_voice_calling_adapter2(
                mContext,
                this.get_calling_contact_list,
                customToastCard,
                customToastText,
                username,
                roomId,
                dialogLayoutFullScreen
        );

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


    public void setupDeleteListers() {
        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        String currentRoomIdXX = callDataPrefs.getString(KEY_ROOM_IDXX, "");

        if (!currentRoomIdXX.isEmpty()) {
            database.child("rooms").child(currentRoomIdXX).child("peers")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long peerCount = snapshot.getChildrenCount();

                            if (peerCount == 0) {
                                if (!isCallEnded) {
                                    triggerEndCallButton();
                                }
                                //   Toast.makeText(mContext, "Peers: " + peerCount + currentRoomIdXX, Toast.LENGTH_SHORT).show();

                            }
                            //    Toast.makeText(mContext, "triggerEndCallButton", Toast.LENGTH_SHORT).show();
                            Log.d(TAG2, "[triggerEndCallButton] Peer count: " + peerCount);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG2, "Firebase error: " + error.getMessage());
                        }
                    });
        } else {
            Log.w(TAG2, "Room ID is empty, listener not attached.");
        }
    }

    private void setupFirebaseListeners() {
        // Retrieve roomId and receiverId from SharedPreferences
        SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
        roomId = callDataPrefs.getString(KEY_ROOM_ID, "");
        receiverId = callDataPrefs.getString(KEY_RECEIVER_ID, "");


        // Peer listener: Listen for new/updated peers
        peersListener = database.child("rooms").child(roomId).child("peers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> peers = new ArrayList<>();
                        Set<String> seenPeerIds = new HashSet<>();

                        for (DataSnapshot child : snapshot.getChildren()) {
                            try {
                                String value = child.getValue(String.class);
                                if (value == null || value.isEmpty()) continue;

                                JSONObject peerData = new JSONObject(value);
                                String peerId = peerData.optString("peerId", null);
                                if (peerId == null || seenPeerIds.contains(peerId)) continue;
                                seenPeerIds.add(peerId);
                                String peerName = peerData.optString("name", "Peer " + peerId);
                                String peerPhoto = peerData.optString("photo", "file:///android_asset/user.png");

                                peers.add(peerId);

                                // Send peer details to WebView
                                String jsCode = String.format("javascript:setCallerInfo('%s', '%s', '%s')",
                                        escapeJsString(peerName),
                                        escapeJsString(peerPhoto),
                                        escapeJsString(peerId));

                                binding.webView.evaluateJavascript(jsCode,
                                        value2 -> Log.d(TAG, "setCallerInfo for peer " + peerId + ": " + value));
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing peer data: " + e.getMessage());
                            }
                        }

                        // Update peer list in WebView
                        try {
                            JSONObject peersJson = new JSONObject();
                            peersJson.put("peers", new JSONArray(peers));
                            if (binding.webView.getProgress() == 100) {
                                binding.webView.evaluateJavascript("javascript:updatePeers(" + peersJson.toString() + ")", null);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error updating peers: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //  runOnUiThread(() -> Toast.makeText(MainActivityVoiceCall.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });

        // Signaling listener: handle messages sent to this peer or broadcast
        signalingListener = database.child("rooms").child(roomId).child("signaling")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            try {
                                String value = child.getValue(String.class);
                                if (value == null) continue;

                                JSONObject data = new JSONObject(value);
                                String receiver = data.optString("receiver", "");
                                if (receiver.equals(myPeerId) || receiver.equals("all")) {
                                    if (binding.webView.getProgress() == 100) {
                                        binding.webView.evaluateJavascript("javascript:handleSignalingData(" + data.toString() + ")", null);
                                        child.getRef().removeValue();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error handling signaling data: " + e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //  runOnUiThread(() -> Toast.makeText(MainActivityVoiceCall.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show());
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
                    // Reinitialize audio output
                    String currentOutput = Constant.getSF.getString(Constant.audioSet, "earpiece");
                    setAudioOutput(currentOutput);

                    // Turned onn mice
                    AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    audioManager.setMicrophoneMute(false); // Explicitly unmute
                    Log.d(TAG, "Microphone set to unmuted on call connect");
                    SharedPreferences callDataPrefs = getSharedPreferences(PREFS_CALL_DATA, Context.MODE_PRIVATE);
                    String currentRoomIdXX = callDataPrefs.getString(KEY_ROOM_IDXX, "");

                    database.child("rooms").child(currentRoomIdXX).child("peers").get().addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.e(TAG2, "[handleRoomCleanup] Failed to query peers: " + task.getException());
                            // cleanupAndExit();
                            return;
                        }

                        DataSnapshot snapshot = task.getResult();
                        long peerCount = snapshot.getChildrenCount();
                        Log.d(TAG2, "[handleRoomCleanup] Peer count: " + peerCount);

                        if (peerCount < 2) {

                        } else {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                if (!isFinishing() && !isDestroyed() && binding != null && binding.webView != null) {
                                    try {
                                        binding.webView.evaluateJavascript("javascript:setCallStatus('Connected')", null); // कॉल स्टेटस अपडेट करा
                                    } catch (Throwable t) {
                                        Log.w(TAG, "WebView destroyed, skipping call status update", t);
                                    }
                                }
                            }, 2000); // 2000ms = 2 seconds
                        }
                    });



                });
            }
        };

        connectivityManager.registerNetworkCallback(request, networkCallback);
    }

    private void setAudioOutput(String output) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        Log.d(TAG, "Reinitializing audio output: " + output);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AudioDeviceInfo[] outputDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
                for (AudioDeviceInfo device : outputDevices) {
                    int type = device.getType();
                    Log.d(TAG, "Found device: " + typeToString(type));

                    if (output.equals("earpiece") && type == AudioDeviceInfo.TYPE_BUILTIN_EARPIECE) {
                        audioManager.setCommunicationDevice(device);
                        Log.d(TAG, "Switched to earpiece");
                        return;
                    }
                    if (output.equals("speaker") && type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                        audioManager.setCommunicationDevice(device);
                        Log.d(TAG, "Switched to speaker");
                        return;
                    }
                    if (output.equals("bluetooth") && (type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO || type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                        if (audioManager.isBluetoothScoAvailableOffCall()) {
                            audioManager.startBluetoothSco();
                            audioManager.setBluetoothScoOn(true);
                            audioManager.setCommunicationDevice(device);
                            Log.d(TAG, "Switched to Bluetooth");
                            return;
                        }
                    }
                }
            }

            // Legacy fallback
            switch (output) {
                case "earpiece":
                    audioManager.stopBluetoothSco();
                    audioManager.setBluetoothScoOn(false);
                    audioManager.setSpeakerphoneOn(false);
                    Log.d(TAG, "Fallback: Switched to earpiece");
                    break;
                case "speaker":
                    audioManager.stopBluetoothSco();
                    audioManager.setBluetoothScoOn(false);
                    audioManager.setSpeakerphoneOn(true);
                    Log.d(TAG, "Fallback: Switched to speaker");
                    break;
                case "bluetooth":
                    if (audioManager.isBluetoothScoAvailableOffCall()) {
                        audioManager.startBluetoothSco();
                        audioManager.setBluetoothScoOn(true);
                        audioManager.setSpeakerphoneOn(false);
                        Log.d(TAG, "Fallback: Switched to Bluetooth");
                    } else {
                        Log.w(TAG, "Bluetooth SCO not available");
                        Toast.makeText(mContext, "Bluetooth SCO not available", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reinitializing audio output: " + e.getMessage(), e);
            Toast.makeText(mContext, "Failed to reinitialize audio output", Toast.LENGTH_SHORT).show();
        }
    }
}