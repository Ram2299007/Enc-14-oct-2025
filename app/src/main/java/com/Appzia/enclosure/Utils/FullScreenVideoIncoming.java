package com.Appzia.enclosure.Utils;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.EdgeToEdgeHelper;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.activities.ConnectingActivity;
import com.Appzia.enclosure.activities.MainActivityVideoCall;
import com.Appzia.enclosure.databinding.ActivityFullScreenVideoIncomingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.Appzia.enclosure.Utils.ImageLoaderUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class FullScreenVideoIncoming extends AppCompatActivity {
    ActivityFullScreenVideoIncomingBinding binding;

    boolean isOkay = false;
    private static final int CAMERA_REQUEST_CODE = 100;
    private final int MAX_DOTS = 3;
    private ValueEventListener valueEventListener;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    boolean pageExit = false;
    private int dotCount = 1;
    String uniqueId = "";
    boolean peerReady = false;
    boolean isPeerConnected = false;
    boolean connIdAvailable = false;
    String username;
    String createdBy;
    String receiverId;

    String senderId;
    private String myRoom;
    String roomFlagKey;
    String  incoming, photo, name;
    String fTokenKey;
    String roomId;
    String friendsUsername = "";

    boolean isVideo = true;


    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private int requestCode = 101010;
    DatabaseReference firebaseRef;
    Context mContext;
    Window window;
    private Handler handler;
    FirebaseDatabase database;
    boolean isAudio = true;
    MediaPlayer mediaPlayer;
    Handler handler2;



    private BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Log.d("POWER_BUTTON", "Screen turned off, stopping ringtone");
                stopRingtone();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        binding.name.setText(Constant.getSF.getString(Constant.callName, ""));
        roomFlagKey = getIntent().getStringExtra("roomFlagKey");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullScreenVideoIncomingBinding.inflate(getLayoutInflater());
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

        // Setup edge-to-edge display
        EdgeToEdgeHelper.setupEdgeToEdge(this, binding.getRoot());

        mContext = FullScreenVideoIncoming.this;

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffReceiver, filter);



        Constant.getSfFuncion(mContext);
        database = FirebaseDatabase.getInstance();

        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.calling));

        }


        Constant.getSfFuncion(mContext);
        database = FirebaseDatabase.getInstance();
        username = Constant.getSF.getString(Constant.UID_KEY, "");
        photo = getIntent().getStringExtra("photoReceiver");
        ImageLoaderUtil.safeLoadImage(photo, binding.image, R.drawable.inviteimg);

        name = getIntent().getStringExtra("nameReceiver");
        if (name != null) {
            binding.name.setText(name);
        }


        senderId = getIntent().getStringExtra("senderId");
        receiverId = getIntent().getStringExtra("receiverId");
        roomFlagKey = getIntent().getStringExtra("roomFlagKey");
        roomId = getIntent().getStringExtra("roomId");
        String deleteroom = receiverId + senderId;


        ///misscall
        database.getReference().child("removeVideoCallNotification").child(Constant.getSF.getString(Constant.UID_KEY, "")).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                database.getReference().child("removeVideoCallNotification").child(Constant.getSF.getString(Constant.UID_KEY, "")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        runOnUiThread(FullScreenVideoIncoming.this::finish);
                    }
                });
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (roomFlagKey != null) {
            // todo sender
            myRoom = senderId + receiverId;
            firebaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(myRoom);
            fTokenKey = getIntent().getStringExtra("fTokenKey");
        } else {
            //todo receiver
            myRoom = receiverId + senderId;
            firebaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(myRoom);

            try {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);
            } catch (Exception e) {

            }
        }

        binding.endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("END_CALL", "End Call button clicked - Video");
                declineCall();
            }
        });

        binding.acceptCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String meetingId = getIntent().getStringExtra("meetingId");
                String phone = getIntent().getStringExtra("phone");
                String userName = getIntent().getStringExtra("nameReceiver");
                String photo = getIntent().getStringExtra("photoReceiver");
                String sampleToken = getIntent().getStringExtra("token");
                String senderId = getIntent().getStringExtra("senderId");
                String receiverId = getIntent().getStringExtra("receiverId");
                String device_type = getIntent().getStringExtra("device_type");
                String usernameWeb = getIntent().getStringExtra("username");
                String createdByWeb = getIntent().getStringExtra("createdBy");
                String incomingWeb = getIntent().getStringExtra("incoming");
                String roomId = getIntent().getStringExtra("roomId");

                Log.d("IntentData", "Meeting ID: " + meetingId);
                Log.d("IntentData", "Phone: " + phone);
                Log.d("IntentData", "User Name: " + userName);
                Log.d("IntentData", "Photo: " + photo);
                Log.d("IntentData", "Sample Token: " + sampleToken);
                Log.d("IntentData", "Sender ID: " + senderId);
                Log.d("IntentData", "Receiver ID: " + receiverId);
                Log.d("IntentData", "Device Type: " + device_type);
                Log.d("IntentData", "Username Web: " + usernameWeb);
                Log.d("IntentData", "Created By Web: " + createdByWeb);
                Log.d("IntentData", "Incoming Web: " + incomingWeb);





                acceptCall();
            }
        });

    }

    /**
     * Handle decline call with proper cleanup
     */
    private void declineCall() {
        Log.d("DECLINE_CALL", "Declining video call");
        
        // Stop ringtone
        stopRingtone();
        
        // Send decline signal to Firebase
        String deleteroom2 = senderId + receiverId;
        String pushKey = database.getReference().child("declineVideoKey").child(deleteroom2).push().getKey();
        database.getReference().child("declineVideoKey").child(deleteroom2).setValue(pushKey)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("DECLINE_CALL", "Decline signal sent successfully");
                    // Clean up and finish activity
                    cleanupAndFinish();
                }
            })
            .addOnFailureListener(e -> {
                Log.e("DECLINE_CALL", "Failed to send decline signal: " + e.getMessage());
                // Still finish activity even if Firebase fails
                cleanupAndFinish();
            });
    }

    /**
     * Handle accept call with proper cleanup
     */
    private void acceptCall() {
        Log.d("ACCEPT_CALL", "Accepting video call");
        
        // Stop ringtone
        stopRingtone();
        
        // Get intent data
        String meetingId = getIntent().getStringExtra("meetingId");
        String phone = getIntent().getStringExtra("phone");
        String userName = getIntent().getStringExtra("nameReceiver");
        String photo = getIntent().getStringExtra("photoReceiver");
        String sampleToken = getIntent().getStringExtra("token");
        String senderId = getIntent().getStringExtra("senderId");
        String receiverId = getIntent().getStringExtra("receiverId");
        String device_type = getIntent().getStringExtra("device_type");
        String usernameWeb = getIntent().getStringExtra("username");
        String createdByWeb = getIntent().getStringExtra("createdBy");
        String incomingWeb = getIntent().getStringExtra("incoming");
        String roomId = getIntent().getStringExtra("roomId");

        // Start video call activity
        Intent resultIntent = new Intent(mContext, MainActivityVideoCall.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra("meetingId", meetingId);
        resultIntent.putExtra("phone", phone);
        resultIntent.putExtra("photoReceiver", photo);
        resultIntent.putExtra("nameReceiver", userName);
        resultIntent.putExtra("token", sampleToken);
        resultIntent.putExtra("senderId", senderId);
        resultIntent.putExtra("receiverId", receiverId);
        resultIntent.putExtra("device_type", device_type);
        resultIntent.putExtra("stop_ringtone", true);
        resultIntent.putExtra("fromNoti", "fromNoti");
        resultIntent.putExtra("username", usernameWeb);
        resultIntent.putExtra("createdBy", createdByWeb);
        resultIntent.putExtra("incoming", incomingWeb);
        resultIntent.putExtra("roomId", roomId);
        
        startActivity(resultIntent);
        
        // Clean up and finish activity
        cleanupAndFinish();
    }

    /**
     * Clean up resources and properly finish activity
     */
    private void cleanupAndFinish() {
        Log.d("CLEANUP", "Cleaning up FullScreenVideoIncoming activity");
        
        // Remove Firebase listeners
        if (valueEventListener != null && firebaseRef != null) {
            try {
                firebaseRef.removeEventListener(valueEventListener);
            } catch (Exception e) {
                Log.e("CLEANUP", "Error removing Firebase listener: " + e.getMessage());
            }
        }
        
        // Unregister broadcast receiver
        try {
            unregisterReceiver(screenOffReceiver);
        } catch (IllegalArgumentException e) {
            Log.e("CLEANUP", "Receiver not registered: " + e.getMessage());
        }
        
        // Clear window flags
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        
        // Finish activity with proper flags
        runOnUiThread(() -> {
            try {
                finishAffinity();
                finishAndRemoveTask();
            } catch (Exception e) {
                Log.e("CLEANUP", "Error finishing activity: " + e.getMessage());
                finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LIFECYCLE", "FullScreenVideoIncoming onDestroy called");
        
        // Clean up resources
        if (valueEventListener != null && firebaseRef != null) {
            try {
                firebaseRef.removeEventListener(valueEventListener);
            } catch (Exception e) {
                Log.e("LIFECYCLE", "Error removing Firebase listener: " + e.getMessage());
            }
        }
        
        try {
            unregisterReceiver(screenOffReceiver);
        } catch (IllegalArgumentException e) {
            Log.e("LIFECYCLE", "Receiver not registered: " + e.getMessage());
        }
        
        // Clear window flags
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}

