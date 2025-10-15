package com.Appzia.enclosure.activities;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.CallServiceVoiceCall;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.MainApplication;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityCallVoiceBinding;
import com.Appzia.enclosure.models.InterfaceVoiceJava;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.Appzia.enclosure.Utils.ImageLoaderUtil;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class CallVoiceActivity extends AppCompatActivity implements SensorEventListener {
    ActivityCallVoiceBinding binding;
    String uniqueId = "";
    String username = "";
    String friendsUsername = "";
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    boolean isPeerConnected = false;
    DatabaseReference firebaseRef;
    private AudioManager audioManager;
    private boolean isLoudspeaker = false;
    boolean isAudio = true;
    boolean isVideo = true;
    String createdBy;
    String myRoom, incoming, photo, name;
    private Window window;
    boolean pageExit = false;
    Context mContext;
    String roomFlagKey;
    boolean connIdAvailable = false;
    boolean peerReady = false;
    private boolean isWebViewDestroyed = false;
    private boolean isCallEnding = false;
    public static CallVoiceActivity instance = null;

    private final BroadcastReceiver endCallReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("CallVoiceActivity", "Received end call broadcast");
            triggerEndCallButton();
        }
    };

    private final BroadcastReceiver timerUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long durationSeconds = intent.getLongExtra("duration_seconds", 0);
            String formattedTime = formatTime(durationSeconds);
            runOnUiThread(() -> {
                binding.timing.setText(formattedTime);
                Log.d("CallVoiceActivity", "Received timer update: " + formattedTime);
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        Log.d("CallVoiceActivity", "onCreate called");
        binding = ActivityCallVoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isWebViewDestroyed = false;

        // Register receivers
        requestPermissions();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                endCallReceiver,
                new IntentFilter("com.Appzia.enclosure.END_CALL_UI")
        );
        LocalBroadcastManager.getInstance(this).registerReceiver(
                timerUpdateReceiver,
                new IntentFilter(CallServiceVoiceCall.ACTION_TIMER_UPDATE)
        );
    }

    @Override
    protected void onStart() {
        super.onStart();


        MainApplication.registerActivity(this);
        instance = this;
        mContext = CallVoiceActivity.this;
        Log.d("CallVoiceActivity", "onStart called");


        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.MODIFY_AUDIO_SETTINGS}, 1734);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        myRoom = getIntent().getStringExtra("myRoom");
        username = getIntent().getStringExtra("username");
        incoming = getIntent().getStringExtra("incoming");
        createdBy = getIntent().getStringExtra("createdBy");
        roomFlagKey = getIntent().getStringExtra("roomFlagKey");
        photo = getIntent().getStringExtra("photoReceiver");
        name = getIntent().getStringExtra("nameReceiver");

        Log.d("CallVoiceActivity", "Intent extras - myRoom: " + myRoom + ", incoming: " + incoming);

        if (name != null) {
            binding.name.setText(name);
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.callName, name).apply();
        }

        friendsUsername = incoming;

        // Ensure myRoom is valid
        if (myRoom == null || myRoom.isEmpty()) {
            Constant.getSfFuncion(mContext);
            String myRoomXXX = Constant.getSF.getString(Constant.myRoomXXX, "");
            String usernameXXX = Constant.getSF.getString(Constant.usernameXXX, "");
            String incomingXXX = Constant.getSF.getString(Constant.incomingXXX, "");
            String createdByXXX = Constant.getSF.getString(Constant.createdByXXX, "");
            String roomFlagKeyXXX = Constant.getSF.getString(Constant.roomFlagKeyXXX, "");
            String photoReceiverXXX = Constant.getSF.getString(Constant.photoReceiverXXX, "");
            String nameReceiverXXX = Constant.getSF.getString(Constant.nameReceiverXXX, "");

            myRoom = myRoomXXX;
            username = usernameXXX;
            incoming = incomingXXX;
            createdBy = createdByXXX;
            roomFlagKey = roomFlagKeyXXX.trim();
            photo = photoReceiverXXX;
            name = nameReceiverXXX;

            Log.d("CallVoiceActivity", "Using SharedPreferences - myRoom: " + myRoom);
        }

        if (myRoom == null || myRoom.isEmpty()) {
            Log.e("CallVoiceActivity", "myRoom is null or empty, cannot proceed");
        //    Toast.makeText(mContext, "Error: Invalid call room", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("usersVoice").child(myRoom);

        ImageLoaderUtil.safeLoadImage(photo, binding.image, R.drawable.inviteimg);

        if (roomFlagKey != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = new Date();
            String date = dateFormat.format(date2);
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String amPm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
            String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;
            Log.d("currentTime", currentTime);
            Constant.getSfFuncion(mContext);
            String uid = Constant.getSF.getString(Constant.UID_KEY, "");
            Webservice.create_group_calling(mContext, friendsUsername, uid, "", date, currentTime, "0", "", "1");
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = new Date();
            String date = dateFormat.format(date2);
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String amPm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
            String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;
            Log.d("currentTime", currentTime);
            Constant.getSfFuncion(mContext);
            String uid = Constant.getSF.getString(Constant.UID_KEY, "");
            Webservice.create_group_calling(mContext, friendsUsername, uid, "", date, currentTime, "1", "", "1");
        }

        setupWebView();

        binding.micBtn.setOnClickListener(v -> {
            isAudio = !isAudio;
            callJavaScriptFunction("javascript:toggleAudio(\"" + isAudio + "\")");
            binding.micBtn.setImageResource(isAudio ? R.drawable.btn_unmute_normal : R.drawable.btn_mute_normal);
        });

        binding.speaker.setOnClickListener(v -> toggleAudioOutput());

        Constant.getSfFuncion(mContext);
        String myUid = Constant.getSF.getString(Constant.UID_KEY, "");

        FirebaseDatabase.getInstance().getReference().child("EndVoiceCallFlag").child(myRoom).child(myUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && !isCallEnding) {
                            isCallEnding = true;
                            FirebaseDatabase.getInstance().getReference().child("EndVoiceCallFlag").child(myRoom).child(myUid)
                                    .removeValue().addOnSuccessListener(unused -> {
                                        Constant.setSfFunction(mContext);
                                        Constant.setSF.putString("lastTimeKey", "").apply();
                                        callJavaScriptFunction("javascript:endCall(\"\")");
                                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                            if (binding.webView != null && !isWebViewDestroyed) {
                                                binding.webView.setVisibility(View.INVISIBLE);
                                                binding.webView.loadUrl("about:blank");
                                                binding.webView.clearHistory();
                                                binding.webView.clearCache(true);
                                                binding.webView.removeAllViews();
                                                binding.webView.destroy();
                                                isWebViewDestroyed = true;
                                            }
                                            Intent stopIntent = new Intent(mContext, CallServiceVoiceCall.class);
                                            stopService(stopIntent);
                                            Intent intent = new Intent(mContext, MainActivityOld.class);
                                            intent.putExtra("voiceCallKey", "voiceCallKey");
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                            finish();
                                        }, 300);

                                        FirebaseDatabase.getInstance().getReference().child("EndVoiceCallFlag")
                                                .child(username + incoming).child(myUid).removeValue();
                                        FirebaseDatabase.getInstance().getReference().child("EndVoiceCallFlag")
                                                .child(incoming + username).child(myUid).removeValue();
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CallVoiceActivity", "Firebase error: " + error.getMessage());
                    }
                });

        binding.endCall.setOnClickListener(v -> {
            if (isCallEnding) return;
            isCallEnding = true;
            Constant.setSfFunction(mContext);
            Constant.setSF.putString("lastTimeKey", "").apply();
            try {
                String pushKey = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("EndVoiceCallFlag")
                        .child(myRoom)
                        .child(friendsUsername)
                        .push()
                        .getKey();

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("EndVoiceCallFlag")
                        .child(myRoom)
                        .child(friendsUsername)
                        .setValue(pushKey)
                        .addOnSuccessListener(unused -> {
                            pageExit = true;
                            firebaseRef.removeValue();
                            callJavaScriptFunction("javascript:endCall()");
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                if (binding.webView != null && !isWebViewDestroyed) {
                                    binding.webView.setVisibility(View.INVISIBLE);
                                    binding.webView.loadUrl("about:blank");
                                    binding.webView.clearHistory();
                                    binding.webView.clearCache(true);
                                    binding.webView.removeAllViews();
                                    binding.webView.destroy();
                                    isWebViewDestroyed = true;
                                }
                                Intent stopIntent = new Intent(mContext, CallServiceVoiceCall.class);
                                stopService(stopIntent);
                                Intent intent = new Intent(mContext, MainActivityOld.class);
                                intent.putExtra("voiceCallKey", "voiceCallKey");
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                finish();
                            }, 300);
                        });
            } catch (Exception e) {
                Log.e("CallVoiceActivity", "End call error: " + e.getMessage());
                Constant.setSfFunction(mContext);
                Constant.setSF.putString("lastTimeKey", "").apply();
                callJavaScriptFunction("javascript:endCall()");
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (binding.webView != null && !isWebViewDestroyed) {
                        binding.webView.setVisibility(View.INVISIBLE);
                        binding.webView.loadUrl("about:blank");
                        binding.webView.clearHistory();
                        binding.webView.clearCache(true);
                        binding.webView.removeAllViews();
                        binding.webView.destroy();
                        isWebViewDestroyed = true;
                    }
                    Intent stopIntent = new Intent(mContext, CallServiceVoiceCall.class);
                    stopService(stopIntent);
                    Intent intent = new Intent(mContext, MainActivityOld.class);
                    intent.putExtra("voiceCallKey", "voiceCallKey");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();
                }, 300);
            }

            if (roomFlagKey != null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date2 = new Date();
                String date = dateFormat.format(date2);
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                String amPm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
                String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;
                Log.d("currentTime", currentTime);
                Constant.getSfFuncion(mContext);
                String uid = Constant.getSF.getString(Constant.UID_KEY, "");
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date2 = new Date();
                String date = dateFormat.format(date2);
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                String amPm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
                String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;
                Log.d("currentTime", currentTime);
                Constant.getSfFuncion(mContext);
                String uid = Constant.getSF.getString(Constant.UID_KEY, "");
            }
        });

        binding.backarrow.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(mContext, MainActivityOld.class);
        intent.putExtra("voiceCallKey", "voiceCallKey");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Constant.setSfFunction(mContext);
        Constant.setSF.putString("voiceRadioKey", Constant.voiceRadioKey).apply();
    }

    void setupWebView() {
        if (isWebViewDestroyed || binding.webView == null) {
            Log.e("CallVoiceActivity", "WebView is destroyed or null, cannot setup");
            return;
        }
        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }
            }
        });
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.webView.addJavascriptInterface(new InterfaceVoiceJava(this), "Android");
        loadVoiceCall();
    }

    public void loadVoiceCall() {
        if (isWebViewDestroyed || binding.webView == null) {
            Log.e("CallVoiceActivity", "WebView is destroyed or null, cannot load URL");
            return;
        }
        String filePath = "file:android_asset/callVoice.html";
        binding.webView.loadUrl(filePath);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                initializePeer();
            }
        });
    }

    void callJavaScriptFunction(String function) {
        if (isWebViewDestroyed || binding.webView == null) {
            Log.e("CallVoiceActivity", "WebView is destroyed or null, cannot call JS function: " + function);
            return;
        }
        binding.webView.post(() -> {
            if (!isWebViewDestroyed && binding.webView != null) {
                binding.webView.evaluateJavascript(function, null);
            } else {
                Log.e("CallVoiceActivity", "WebView destroyed during JS execution: " + function);
            }
        });
    }

    void initializePeer() {
        uniqueId = getUniqueId();
        callJavaScriptFunction("javascript:init(\"" + uniqueId + "\")");

        if (createdBy.equalsIgnoreCase(username)) {
            if (pageExit)
                return;
            firebaseRef.child(username).child("connId").setValue(uniqueId);
            firebaseRef.child(username).child("isAvailable").setValue(true);

            binding.connectingLyt.setVisibility(View.GONE);
            binding.controls.setVisibility(View.VISIBLE);
            binding.timing.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                toggleAudioOutputEarpiece();
                Log.d("AudioToggle", "Sender: toggleAudioOutputEarpiece called");
            }, 500);

        } else {
            friendsUsername = createdBy;
            Log.d("DEBUG", "myRoom: " + myRoom + ", friend: " + friendsUsername);

            firebaseRef.child(friendsUsername).child("connId").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && snapshot.getValue() != null) {
                        Log.d("DEBUG", "connId arrived: " + snapshot.getValue());
                        connIdAvailable = true;
                        trySendCallRequestWhenReady();
                        firebaseRef.child(friendsUsername).child("connId").removeEventListener(this);
                    } else {
                        Log.d("DEBUG", "Waiting for connId at: usersVoice/" + myRoom + "/" + friendsUsername + "/connId");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("DEBUG", "Firebase cancelled: " + error.getMessage());
                }
            });
        }

        Constant.setSfFunction(mContext);
        Constant.setSF
                .putString(Constant.myRoomXXX, myRoom)
                .putString(Constant.usernameXXX, username)
                .putString(Constant.incomingXXX, incoming)
                .putString(Constant.createdByXXX, createdBy)
                .putString(Constant.roomFlagKeyXXX, roomFlagKey)
                .putString(Constant.photoReceiverXXX, photo)
                .putString(Constant.nameReceiverXXX, name)
                .apply();

        String myRoomY = getIntent().getStringExtra("myRoom");
        if (myRoomY != null) {
            Intent serviceIntent = new Intent(mContext, CallServiceVoiceCall.class);
            ContextCompat.startForegroundService(mContext, serviceIntent);
        }
    }

    public void onPeerConnected() {
        isPeerConnected = true;
        peerReady = true;
        trySendCallRequestWhenReady();

        binding.connectingLyt.setVisibility(View.GONE);
        binding.controls.setVisibility(View.VISIBLE);
        binding.timing.setVisibility(View.VISIBLE);

        Intent startTimerIntent = new Intent(CallServiceVoiceCall.ACTION_START_TIMER);
        LocalBroadcastManager.getInstance(this).sendBroadcast(startTimerIntent);
        Log.d("CallVoiceActivity", "Sent ACTION_START_TIMER");

        Intent requestIntent = new Intent(CallServiceVoiceCall.ACTION_REQUEST_DURATION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(requestIntent);
        Log.d("CallVoiceActivity", "Requested current duration from service");
    }

    void sendCallRequest() {
        if (!isPeerConnected) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (isPeerConnected) {
                    listenConnId();
                } else {
                    Constant.showCustomToast("Oops! Try calling again", findViewById(R.id.includedToast), findViewById(R.id.toastText));
                }
            }, 500);
            return;
        }
        listenConnId();
    }

    void listenConnId() {
        firebaseRef.child(friendsUsername).child("connId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null)
                    return;

                binding.connectingLyt.setVisibility(View.GONE);
                binding.controls.setVisibility(View.VISIBLE);
                binding.timing.setVisibility(View.VISIBLE);

                String connId = snapshot.getValue(String.class);
                callJavaScriptFunction("javascript:startCall(\"" + connId + "\")");

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    toggleAudioOutputEarpiece();
                    Log.d("AudioToggle", "Receiver: toggleAudioOutputEarpiece called");
                }, 500);

                // Start timer in service
                Intent startTimerIntent = new Intent(CallServiceVoiceCall.ACTION_START_TIMER);
                LocalBroadcastManager.getInstance(CallVoiceActivity.this).sendBroadcast(startTimerIntent);
                Log.d("CallVoiceActivity", "Sent ACTION_START_TIMER");

                // Request current duration from service
                Intent requestIntent = new Intent(CallServiceVoiceCall.ACTION_REQUEST_DURATION);
                LocalBroadcastManager.getInstance(CallVoiceActivity.this).sendBroadcast(requestIntent);
                Log.d("CallVoiceActivity", "Requested current duration from service");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MainApplication.unregisterActivity(this);
        if (binding.webView != null && !isWebViewDestroyed) {
            binding.webView.loadUrl("about:blank");
            binding.webView.stopLoading();
            binding.webView.clearHistory();
            binding.webView.clearCache(true);
            binding.webView.removeAllViews();
            binding.webView.destroy();
            isWebViewDestroyed = true;
        }
        instance = null;
        Constant.setSfFunction(mContext);
        Constant.setSF.putString("voiceRadioKey", Constant.voiceRadioKey).apply();

        // Unregister receivers
        LocalBroadcastManager.getInstance(this).unregisterReceiver(endCallReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(timerUpdateReceiver);
        Log.d("CallVoiceActivity", "CallVoiceActivity destroyed");


    }

    public void onEndCallButtonClicked(View view) {
        Log.d("CallVoiceActivity", "End call button clicked");
        Intent intent = new Intent("com.Appzia.enclosure.END_CALL_BROADCAST");
        sendBroadcast(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Constant.setSfFunction(mContext);
        Constant.setSF.putString("voiceRadioKey", Constant.voiceRadioKey).apply();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < mProximity.getMaximumRange()) {
                if (!isLoudspeaker) {
                    turnScreenOff();
                }
            } else {
                turnScreenOn();
            }
        }
    }

    private void turnScreenOff() {
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "MyApp::ProximityScreenOff");
        }
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
    }

    private void turnScreenOn() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        if (mProximity != null) {
            mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        toggleAudioOutputEarpiece(); // Ensure audio focus on resume
        Intent requestIntent = new Intent(CallServiceVoiceCall.ACTION_REQUEST_DURATION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(requestIntent);
        Constant.getSfFuncion(mContext);
        binding.name.setText(Constant.getSF.getString(Constant.callName, ""));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void toggleAudioOutput() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

        int targetType = isLoudspeaker ? AudioDeviceInfo.TYPE_BUILTIN_EARPIECE : AudioDeviceInfo.TYPE_BUILTIN_SPEAKER;

        boolean switched = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);

            for (AudioDeviceInfo device : devices) {
                if (device.getType() == targetType) {
                    boolean success = audioManager.setCommunicationDevice(device);
                    Log.d("AudioToggle", "Switched using setCommunicationDevice: " + success);
                    switched = success;
                    break;
                }
            }
        }

        if (!switched) {
            audioManager.setSpeakerphoneOn(!isLoudspeaker);
            Log.d("AudioToggle", "Switched using setSpeakerphoneOn: " + (!isLoudspeaker));
        }

        isLoudspeaker = !isLoudspeaker;

        binding.speaker.setImageResource(
                isLoudspeaker ? R.drawable.btn_audio_pressed : R.drawable.btn_audio_normal
        );

        Log.d("AudioToggle", "Now outputting to: " + (isLoudspeaker ? "Speaker" : "Earpiece"));
    }

    private void toggleAudioOutputEarpiece() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        AudioFocusRequest focusRequest;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(audioAttributes)
                    .setOnAudioFocusChangeListener(focusChange -> {
                        Log.d("AudioFocus", "Focus changed: " + focusChange);
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            // Handle audio focus loss (e.g., pause VoIP call if needed)
                            callJavaScriptFunction("javascript:toggleAudio(\"false\")");
                        }
                    })
                    .build();
            int result = audioManager.requestAudioFocus(focusRequest);
            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Log.e("AudioToggle", "Failed to gain audio focus");
                return;
            }
        } else {
            audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            boolean earpieceFound = false;
            for (AudioDeviceInfo device : devices) {
                if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_EARPIECE) {
                    boolean success = audioManager.setCommunicationDevice(device);
                    Log.d("AudioToggle", "Switched to earpiece: " + success);
                    if (success) earpieceFound = true;
                    break;
                }
            }
            if (!earpieceFound) {
                audioManager.setSpeakerphoneOn(false);
                Log.d("AudioToggle", "Earpiece not found, using legacy method");
            }
        } else {
            audioManager.setSpeakerphoneOn(false);
            Log.d("AudioToggle", "Speakerphone disabled");
        }

        isLoudspeaker = false;
        binding.speaker.setImageResource(R.drawable.btn_audio_normal);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Log.e("CallVoiceActivity", "Permission denied: " + permissions[i]);
                    if (permissions[i].equals(android.Manifest.permission.READ_PHONE_STATE)) {
                        Toast.makeText(this, "Read phone state permission is required", Toast.LENGTH_LONG).show();
                        finish(); // Critical permission
                    } else if (permissions[i].equals(android.Manifest.permission.CALL_PHONE)) {
                       // Toast.makeText(this, "Call phone permission denied; some features may be limited", Toast.LENGTH_LONG).show();
                        // Continue without CALL_PHONE; rely on DND or manual rejection
                    } else {
                     //   Toast.makeText(this, "Permission " + permissions[i] + " denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    void trySendCallRequestWhenReady() {
        if (connIdAvailable && peerReady) {
            sendCallRequest();
        }
    }

    public void triggerEndCallButton() {
        if (binding.webView != null && !isWebViewDestroyed) {
            binding.webView.loadUrl("about:blank");
            binding.webView.stopLoading();
            binding.webView.clearHistory();
            binding.webView.clearCache(true);
            binding.webView.removeAllViews();
            binding.webView.destroy();
            isWebViewDestroyed = true;
        }

        runOnUiThread(() -> binding.endCall.performClick());
    }

    private void requestPermissions() {
        String[] permissions = {
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                android.Manifest.permission.FOREGROUND_SERVICE,
                android.Manifest.permission.FOREGROUND_SERVICE_MICROPHONE,
                android.Manifest.permission.FOREGROUND_SERVICE_PHONE_CALL,
                android.Manifest.permission.POST_NOTIFICATIONS,
                android.Manifest.permission.MODIFY_AUDIO_SETTINGS
        };
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsToRequest.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE);
        }
    }




}