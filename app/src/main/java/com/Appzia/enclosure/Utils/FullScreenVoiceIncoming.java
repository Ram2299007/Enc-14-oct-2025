package com.Appzia.enclosure.Utils;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.activities.CallVoiceActivity;
import com.Appzia.enclosure.activities.MainActivityVideoCall;
import com.Appzia.enclosure.activities.MainActivityVoiceCall;
import com.Appzia.enclosure.databinding.ActivityFullScreenVoiceIncomingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.Appzia.enclosure.Utils.ImageLoaderUtil;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class FullScreenVoiceIncoming extends AppCompatActivity {
    ActivityFullScreenVoiceIncomingBinding binding;

    boolean isOkay = false;
    private final int MAX_DOTS = 3;
    private int dotCount = 1;
    String username;
    String receiverId;
    String senderId;
    private String myRoom;
    String roomFlagKey;
    String fTokenKey;
    String roomId;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private boolean isRingtonePlaying = false;
    private ValueEventListener valueEventListener;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private int requestCode = 101010;
    DatabaseReference firebaseRef;
    Context mContext;
    Window window;

    FirebaseDatabase database;

    //  MediaPlayer mediaPlayer;

    // Handler handler2;



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
        roomFlagKey = getIntent().getStringExtra("roomFlagKey");

        if (firebaseRef != null && valueEventListener != null) {
            firebaseRef.addValueEventListener(valueEventListener);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullScreenVoiceIncomingBinding.inflate(getLayoutInflater());
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

        mContext = FullScreenVoiceIncoming.this;
        // Proximity sensor setup

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


        askPermissions();
        username = Constant.getSF.getString(Constant.UID_KEY, "");


        // set profile data
        String photo = getIntent().getStringExtra("photoReceiver");
        ImageLoaderUtil.safeLoadImage(photo, binding.image, R.drawable.inviteimg);

        String name = getIntent().getStringExtra("nameReceiver");

        if (name != null) {
            binding.name.setText(name);
        }


        senderId = getIntent().getStringExtra("senderId");
        receiverId = getIntent().getStringExtra("receiverId");
        roomFlagKey = getIntent().getStringExtra("roomFlagKey");
        roomId = getIntent().getStringExtra("roomId");
        myRoom = senderId + receiverId;


        String deleteroom = receiverId + senderId;


        if (roomFlagKey != null) {
            // todo sender
            myRoom = senderId + receiverId;
            // Toast.makeText(mContext, myRoom, Toast.LENGTH_SHORT).show();
            firebaseRef = FirebaseDatabase.getInstance().getReference().child("usersVoice").child(myRoom);
            fTokenKey = getIntent().getStringExtra("fTokenKey");
        } else {
            //todo receiver
            myRoom = receiverId + senderId;
            //Toast.makeText(mContext, myRoom, Toast.LENGTH_SHORT).show();
            firebaseRef = FirebaseDatabase.getInstance().getReference().child("usersVoice").child(myRoom);
            // TODO: 17-Oct-24 if notificaion coming need to remove it

            try {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);
            } catch (Exception e) {

            }
        }

        ///misscall
        database.getReference().child("removeCallNotification").child(Constant.getSF.getString(Constant.UID_KEY, "")).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                database.getReference().child("removeCallNotification").child(Constant.getSF.getString(Constant.UID_KEY, "")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        runOnUiThread(FullScreenVoiceIncoming.this::finish);
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


        binding.endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("END_CALL", "End Call button clicked - Voice");
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
        Log.d("DECLINE_CALL", "Declining voice call");
        
        // Stop ringtone
        stopRingtone();
        
        // Send decline signal to Firebase
        String deleteroom2 = senderId + receiverId;
        String pushKey = database.getReference().child("declineVoiceKey").child(deleteroom2).push().getKey();
        database.getReference().child("declineVoiceKey").child(deleteroom2).setValue(pushKey)
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
        Log.d("ACCEPT_CALL", "Accepting voice call");
        
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

        // Start voice call activity
        Intent resultIntent = new Intent(mContext, MainActivityVoiceCall.class);
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
        Log.d("CLEANUP", "Cleaning up FullScreenVoiceIncoming activity");
        
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

    void askPermissions() {
        ActivityCompat.requestPermissions(this,
                permissions,
                requestCode
        );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCode) {
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

    private boolean isPermissionsGranted() {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }


    private void updateLoadingText() {
        String dots = "";
        for (int i = 0; i < dotCount; i++) {
            dots += ".";
        }
        binding.name2.setText(dots);

        // Increase the dot count and reset if it exceeds MAX_DOTS
        dotCount++;
        if (dotCount > MAX_DOTS) {
            dotCount = 1;
        }
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
        Log.d("LIFECYCLE", "FullScreenVoiceIncoming onDestroy called");
        
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

    @Override
    protected void onPause() {
        super.onPause();
        if (valueEventListener != null && firebaseRef != null) {
            firebaseRef.removeEventListener(valueEventListener);
        }

    }



}

