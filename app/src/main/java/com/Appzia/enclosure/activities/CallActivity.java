package com.Appzia.enclosure.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.get_voice_calling_adapter2;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.CallServiceVideoCall;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.MainApplication;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityCallBinding;
import com.Appzia.enclosure.models.InterfaceJava;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.Appzia.enclosure.Utils.ImageLoaderUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CallActivity extends AppCompatActivity {
    private ActivityCallBinding binding; // test
    private String uniqueId = "";
    private CardView customToastCard;
    private TextView customToastText;
    private String username = "";
    private ArrayList<get_contact_model> get_calling_contact_list = new ArrayList<>();
    private String friendsUsername = "";
    private boolean isPeerConnected = false;
    private DatabaseReference firebaseRef;
    private boolean isAudio = true;
    private boolean isVideo = true;
    private String createdBy;
    private String myRoom, incoming, photo, name;
    private String roomFlagKey;
    private LinearLayoutManager layoutManager;
    private Context mContext;
    private boolean connIdAvailable = false;
    private boolean peerReady = false;
    private get_voice_calling_adapter2 adapter;
    private boolean pageExit = false;
    private LocalBroadcastManager localBroadcastManager;
    private boolean isInPictureInPictureMode = false;

    private final BroadcastReceiver timerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CallServiceVideoCall.ACTION_TIMER_UPDATE.equals(intent.getAction())) {
                long durationSeconds = intent.getLongExtra("duration_seconds", 0);
                updateTimerDisplay(durationSeconds);
                Log.d("TAG", "Received timer update: " + durationSeconds + " seconds");
            } else if ("com.Appzia.enclosure.END_CALL_UI_VIDEO".equals(intent.getAction())) {
                finish();
                Log.d("TAG", "Received end call broadcast, finishing activity");
            }
        }
    };

    private final BroadcastReceiver endCallReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("CallActivity", "Received end call broadcast");
            triggerEndCallButton();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallBinding.inflate(getLayoutInflater());
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
        customToastCard = findViewById(R.id.includedToast);
        customToastText = customToastCard.findViewById(R.id.toastText);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(CallServiceVideoCall.ACTION_TIMER_UPDATE);
        filter.addAction("com.Appzia.enclosure.END_CALL_UI_VIDEO");
        localBroadcastManager.registerReceiver(timerReceiver, filter);
        Intent requestIntent = new Intent(CallServiceVideoCall.ACTION_REQUEST_DURATION);
        localBroadcastManager.sendBroadcast(requestIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        if (savedInstanceState != null) {
            restoreCallState(savedInstanceState);
        } else {
            loadIntentData();
        }

        if (myRoom == null || myRoom.isEmpty() || username == null || username.isEmpty()) {
            Toast.makeText(mContext, "Invalid call data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (name != null && !name.trim().isEmpty()) {
            binding.name.setText(name);
        }
        ImageLoaderUtil.safeLoadImage(photo, binding.image, R.drawable.deflarge, R.drawable.inviteimg);

        friendsUsername = incoming;
        firebaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(myRoom);

        handleCallSetup();
        checkPermissions();
        setupButtonListeners();
        listenForCallEnd();


    }

    private void filteredList(String newText) {
        ArrayList<get_contact_model> filteredList = new ArrayList<>();
        for (get_contact_model list : this.get_calling_contact_list) {
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

    private void loadIntentData() {
        myRoom = getIntent().getStringExtra("myRoom");
        username = getIntent().getStringExtra("username");
        incoming = getIntent().getStringExtra("incoming");
        createdBy = getIntent().getStringExtra("createdBy");
        roomFlagKey = getIntent().getStringExtra("roomFlagKey");
        photo = getIntent().getStringExtra("photoReceiver");
        name = getIntent().getStringExtra("nameReceiver");

        if (myRoom == null || username == null || incoming == null) {
            Constant.getSfFuncion(mContext);
            myRoom = Constant.getSF.getString(Constant.myRoomXXX, "");
            username = Constant.getSF.getString(Constant.usernameXXX, "");
            incoming = Constant.getSF.getString(Constant.incomingXXX, "");
            createdBy = Constant.getSF.getString(Constant.createdByXXX, "");
            roomFlagKey = Constant.getSF.getString(Constant.roomFlagKeyXXX, "").trim();
            photo = Constant.getSF.getString(Constant.photoReceiverXXX, "");
            name = Constant.getSF.getString(Constant.nameReceiverXXX, "");
        }
        Log.d("CallActivity", "Loaded data: myRoom=" + myRoom + ", username=" + username);
    }

    private void restoreCallState(Bundle savedInstanceState) {
        myRoom = savedInstanceState.getString("myRoom", "");
        username = savedInstanceState.getString("username", "");
        incoming = savedInstanceState.getString("incoming", "");
        createdBy = savedInstanceState.getString("createdBy", "");
        roomFlagKey = savedInstanceState.getString("roomFlagKey", "");
        photo = savedInstanceState.getString("photoReceiver", "");
        name = savedInstanceState.getString("nameReceiver", "");
        isAudio = savedInstanceState.getBoolean("isAudio", true);
        isVideo = savedInstanceState.getBoolean("isVideo", true);
        isPeerConnected = savedInstanceState.getBoolean("isPeerConnected", false);
        connIdAvailable = savedInstanceState.getBoolean("connIdAvailable", false);
        peerReady = savedInstanceState.getBoolean("peerReady", false);
        isInPictureInPictureMode = savedInstanceState.getBoolean("isInPictureInPictureMode", false);
        Log.d("CallActivity", "Restored state: myRoom=" + myRoom + ", username=" + username);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("myRoom", myRoom);
        outState.putString("username", username);
        outState.putString("incoming", incoming);
        outState.putString("createdBy", createdBy);
        outState.putString("roomFlagKey", roomFlagKey);
        outState.putString("photoReceiver", photo);
        outState.putString("nameReceiver", name);
        outState.putBoolean("isAudio", isAudio);
        outState.putBoolean("isVideo", isVideo);
        outState.putBoolean("isPeerConnected", isPeerConnected);
        outState.putBoolean("connIdAvailable", connIdAvailable);
        outState.putBoolean("peerReady", peerReady);
        outState.putBoolean("isInPictureInPictureMode", isInPictureInPictureMode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent requestIntent = new Intent(CallServiceVideoCall.ACTION_REQUEST_DURATION);
        localBroadcastManager.sendBroadcast(requestIntent);
        if (!pageExit) {
            initializePeer();
            binding.webView.resumeTimers();
            binding.webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isInPictureInPictureMode && !pageExit) {
            // Prevent WebView from pausing to keep camera active
            binding.webView.pauseTimers();
            binding.webView.onPause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MainApplication.registerActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(timerReceiver);
        MainApplication.unregisterActivity(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(endCallReceiver);
        if (pageExit) {
            binding.webView.loadUrl("about:blank");
            binding.webView.stopLoading();
            binding.webView.clearHistory();
            binding.webView.clearCache(true);
            binding.webView.removeAllViews();
            binding.webView.destroy();
        }
    }

    @Override
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !pageExit && !isInPictureInPictureMode) {
            enterPipMode();
        }
    }

    private void enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder pipBuilder = new PictureInPictureParams.Builder();
            // Set aspect ratio to square (1:1) for PiP window
            pipBuilder.setAspectRatio(new Rational(1, 1));
            try {
                enterPictureInPictureMode(pipBuilder.build());
            } catch (IllegalStateException e) {
                Log.e("CallActivity", "Failed to enter PiP mode: " + e.getMessage());
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, @NonNull Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        this.isInPictureInPictureMode = isInPictureInPictureMode;
        if (isInPictureInPictureMode) {
            // Hide UI elements not needed in PiP mode
            binding.controls.setVisibility(View.GONE);
            binding.name.setVisibility(View.GONE);
            binding.image.setVisibility(View.GONE);

            binding.backarrow.setVisibility(View.GONE);
            binding.endCall.setVisibility(View.GONE);
            binding.micBtn.setVisibility(View.GONE);
            binding.videoBtn.setVisibility(View.GONE);
            // Ensure WebView continues running
            binding.webView.resumeTimers();
            binding.webView.onResume();
        } else {
            // Restore UI elements when exiting PiP mode
            binding.controls.setVisibility(View.VISIBLE);
            binding.name.setVisibility(View.VISIBLE);
            binding.image.setVisibility(View.VISIBLE);

            binding.backarrow.setVisibility(View.VISIBLE);
            binding.endCall.setVisibility(View.VISIBLE);
            binding.micBtn.setVisibility(View.VISIBLE);
            binding.videoBtn.setVisibility(View.VISIBLE);
        }
    }

    private void handleCallSetup() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = dateFormat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        String currentTime = timeFormat.format(calendar.getTime());
        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        if (roomFlagKey != null && !roomFlagKey.isEmpty()) {
            Log.d("CallActivity", "Acting as sender with roomFlagKey: " + roomFlagKey);
            Webservice.create_group_calling(mContext, friendsUsername, uid, "", date, currentTime, "0", "", "2");
        } else {
            Log.d("CallActivity", "Acting as receiver");
            Webservice.create_group_calling(mContext, friendsUsername, uid, "", date, currentTime, "1", "", "2");
        }
        Constant.setSfFunction(mContext);
        Constant.setSF.putString(Constant.myRoomXXX, myRoom);
        Constant.setSF.putString(Constant.usernameXXX, username);
        Constant.setSF.putString(Constant.incomingXXX, incoming);
        Constant.setSF.putString(Constant.createdByXXX, createdBy);
        Constant.setSF.putString(Constant.roomFlagKeyXXX, roomFlagKey);
        Constant.setSF.putString(Constant.photoReceiverXXX, photo);
        Constant.setSF.putString(Constant.nameReceiverXXX, name);
        Constant.setSF.apply();
    }

    private void setupWebView() {
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    runOnUiThread(() -> request.grant(request.getResources()));
                }
            }
        });

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                // Optional WebRTC fix
                view.evaluateJavascript(
                        "try {" +
                                "navigator.mediaDevices.getUserMedia = navigator.mediaDevices.getUserMedia || navigator.webkitGetUserMedia;" +
                                "window.RTCPeerConnection = window.RTCPeerConnection || window.webkitRTCPeerConnection;" +
                                "if (window.RTCPeerConnection) {" +
                                "const orig = window.RTCPeerConnection;" +
                                "window.RTCPeerConnection = function(config) {" +
                                "config.iceServers = [{" +
                                "urls: ['stun:stun.l.google.com:19302']" +
                                "}];" +
                                "return new orig(config);" +
                                "};" +
                                "}" +
                                "} catch(e) { console.log('WebRTC fix error:', e); }",
                        null
                );
            }
        });

        binding.webView.addJavascriptInterface(new InterfaceJava(this), "Android");
        loadVideoCall();

        binding.backarrow.setOnClickListener(v -> onBackPressed());
    }



    private void loadVideoCall() {
        String filePath = "file:android_asset/call.html";
        binding.webView.loadUrl(filePath);

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("CallActivity", "WebView page finished loading: " + url);
                initializePeer();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("CallActivity", "WebView error: " + description + " (Code: " + errorCode + ")");
              //  Toast.makeText(mContext, "WebView error: " + description, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializePeer() {
        if (pageExit) return;
        uniqueId = getUniqueId();
        Log.d("chromium", "initializePeer: "+uniqueId);
        callJavaScriptFunction("javascript:init(\"" + uniqueId + "\")");

        if (createdBy.equalsIgnoreCase(username)) {
            firebaseRef.child(username).child("connId").setValue(uniqueId);
            firebaseRef.child(username).child("isAvailable").setValue(true);
            binding.loadingGroup.setVisibility(View.GONE);
            binding.controls.setVisibility(View.VISIBLE);
            listenForSignalingData();
        } else {
            friendsUsername = createdBy;
            Log.d("CallActivity", "Callee listening for connId at: users/" + myRoom + "/" + friendsUsername);

            firebaseRef.child(friendsUsername).child("connId").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && snapshot.getValue() != null) {
                        Log.d("CallActivity", "Received connId: " + snapshot.getValue());
                        connIdAvailable = true;
                        trySendCallRequestWhenReady();
                        firebaseRef.child(friendsUsername).child("connId").removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("CallActivity", "Firebase connId error: " + error.getMessage());
                }
            });

            listenForSignalingData();
        }

        if (myRoom != null && !myRoom.isEmpty()) {
            Intent serviceIntent = new Intent(mContext, CallServiceVideoCall.class);
            serviceIntent.putExtra("myRoom", myRoom);
            serviceIntent.putExtra("username", username);
            serviceIntent.putExtra("incoming", incoming);
            serviceIntent.putExtra("createdBy", createdBy);
            serviceIntent.putExtra("roomFlagKey", roomFlagKey);
            serviceIntent.putExtra("photoReceiver", photo);
            serviceIntent.putExtra("nameReceiver", name);
            ContextCompat.startForegroundService(mContext, serviceIntent);
            getIntent().removeExtra("myRoom");
        }
    }

    private void listenForSignalingData() {
        firebaseRef.child("offer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String sdp = snapshot.getValue(String.class);
                    Log.d("CallActivity", "Received SDP offer: " + sdp);
                    callJavaScriptFunction("javascript:setOffer(\"" + sdp + "\")");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CallActivity", "SDP offer error: " + error.getMessage());
            }
        });

        firebaseRef.child("iceCandidates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot candidateSnapshot : snapshot.getChildren()) {
                        String candidate = candidateSnapshot.getValue(String.class);
                        Log.d("CallActivity", "Received ICE candidate: " + candidate);
                        callJavaScriptFunction("javascript:addIceCandidate(\"" + candidate + "\")");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CallActivity", "ICE candidate error: " + error.getMessage());
            }
        });
    }

    private void setupButtonListeners() {
        binding.micBtn.setOnClickListener(v -> {
            isAudio = !isAudio;
            callJavaScriptFunction("javascript:toggleAudio(\"" + isAudio + "\")");
            binding.micBtn.setImageResource(isAudio ? R.drawable.btn_unmute_normal : R.drawable.btn_mute_normal);
        });

        binding.videoBtn.setOnClickListener(v -> {
            isVideo = !isVideo;
            callJavaScriptFunction("javascript:flipCameraFromAndroid(\"\")");
            binding.videoBtn.setImageResource(isVideo ? R.drawable.btn_video_normal : R.drawable.btn_video_muted);
        });

        binding.endCall.setOnClickListener(v -> endCall());
    }

    private void listenForCallEnd() {
        Constant.getSfFuncion(mContext);
        String myUid = Constant.getSF.getString(Constant.UID_KEY, "");

        FirebaseDatabase.getInstance().getReference().child("EndVideoCallFlag").child(myRoom).child(myUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            FirebaseDatabase.getInstance().getReference().child("EndVideoCallFlag")
                                    .child(myRoom).child(myUid).removeValue()
                                    .addOnSuccessListener(unused -> {
                                        terminateCall();
                                        FirebaseDatabase.getInstance().getReference().child("EndVideoCallFlag")
                                                .child(username + incoming).child(myUid).removeValue();
                                        FirebaseDatabase.getInstance().getReference().child("EndVideoCallFlag")
                                                .child(incoming + username).child(myUid).removeValue();
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CallActivity", "End call listener error: " + error.getMessage());
                    }
                });
    }

    private void endCall() {
        pageExit = true;
        try {
            String pushKey = FirebaseDatabase.getInstance().getReference().child("EndVideoCallFlag")
                    .child(myRoom).child(friendsUsername).push().getKey();
            FirebaseDatabase.getInstance().getReference().child("EndVideoCallFlag")
                    .child(myRoom).child(friendsUsername).child(pushKey).setValue(true)
                    .addOnSuccessListener(unused -> {
                        firebaseRef.removeValue();
                        terminateCall();
                    });
        } catch (Exception error) {
            Log.e("CallActivity", "End call error: " + error.getMessage());
            terminateCall();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        String currentTime = timeFormat.format(Calendar.getInstance().getTime());

        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        String flag = (roomFlagKey != null && !roomFlagKey.isEmpty()) ? "0" : "1";
        Webservice.create_group_calling(mContext, friendsUsername, uid, "", date, currentTime, flag, currentTime, "2");
    }

    private void terminateCall() {
        callJavaScriptFunction("javascript:endCall(\"\")");
        Intent stopIntent = new Intent(mContext, CallServiceVideoCall.class);
        stopIntent.setAction(CallServiceVideoCall.ACTION_END_CALL);
        startService(stopIntent);
        new Handler(Looper.getMainLooper()).post(() -> {
            if (!isFinishing()) {
                onBackPressed();
            }
        });
    }

    public void onPeerConnected() {
        isPeerConnected = true;
        peerReady = true;
        Log.d("CallActivity", "Peer connected");
        trySendCallRequestWhenReady();
    }

    private void trySendCallRequestWhenReady() {
        if (connIdAvailable && peerReady) {
            sendCallRequest();
        }
    }

    private void sendCallRequest() {
        if (!isPeerConnected) {
            Log.w("CallActivity", "Peer not connected, retrying...");
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (isPeerConnected) {
                    listenConnId();
                } else {
                    Toast.makeText(mContext, "Connection failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            }, 500);
            return;
        }
        listenConnId();
    }

    private void listenConnId() {
        firebaseRef.child(friendsUsername).child("connId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    binding.loadingGroup.setVisibility(View.GONE);
                    binding.controls.setVisibility(View.VISIBLE);
                    String connId = snapshot.getValue(String.class);
                    Log.d("CallActivity", "Starting call with connId: " + connId);
                    callJavaScriptFunction("javascript:startCall(\"" + connId + "\")");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CallActivity", "ConnId listener error: " + error.getMessage());
            }
        });
    }

    private void callJavaScriptFunction(String function) {
        binding.webView.post(() -> {
            binding.webView.evaluateJavascript(function, value -> {
                Log.d("CallActivity", "JS function " + function + " result: " + value);
            });
        });
    }

    private String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public void triggerEndCallButton() {
        pageExit = true;
        binding.webView.loadUrl("about:blank");
        binding.webView.stopLoading();
        binding.webView.clearHistory();
        binding.webView.clearCache(true);
        binding.webView.removeAllViews();
        binding.webView.destroy();
        runOnUiThread(() -> binding.endCall.performClick());
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !pageExit && !isInPictureInPictureMode) {
            enterPipMode();
        } else {
            super.onBackPressed();
            Intent intent = new Intent(mContext, MainActivityOld.class);
            intent.putExtra("videoCallKey", "videoCallKey");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 100);
        } else {
            setupWebView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupWebView();
        } else {
            Toast.makeText(this, "Camera and microphone permissions required", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateTimerDisplay(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        String timeString = String.format("%02d:%02d", minutes, remainingSeconds);
        Log.d("TAG", "Updated timer display: " + timeString);
    }

    public void setAdapter(ArrayList<get_contact_model> get_calling_contact_list, RecyclerView recyclerview, Dialog dialogLayoutFullScreen, CardView customToastCard, TextView customToastText, String username) {
        Constant.getSfFuncion(mContext);
        String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");
        ArrayList<get_contact_model> filteredList = new ArrayList<>();
        for (get_contact_model model : get_calling_contact_list) {
            if (!model.getUid().equals(currentUid)) {
                filteredList.add(model);
            }
        }
        this.get_calling_contact_list = filteredList;
//        adapter = new get_voice_calling_adapter2(
//                mContext,
//                this.get_calling_contact_list, dialogLayoutFullScreen, customToastCard, customToastText, username
//        );
        layoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}