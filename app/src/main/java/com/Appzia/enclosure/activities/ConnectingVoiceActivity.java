package com.Appzia.enclosure.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.FcmNotificationSenderMisscall;
import com.Appzia.enclosure.Utils.MainApplication;
import com.Appzia.enclosure.databinding.ActivityConnectingVoiceBinding;
import com.google.android.gms.tasks.OnSuccessListener;
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


public class ConnectingVoiceActivity extends AppCompatActivity {
    ActivityConnectingVoiceBinding binding;
    boolean isOkay = false;
    private final int MAX_DOTS = 3;
    private int dotCount = 1;
    String username;
    String receiverId;
    String senderId;
    private String myRoom;
    String roomFlagKey;
    String fTokenKey;
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
    Handler handler;
    // Handler handler2;
    MediaPlayer mediaPlayer;
    Handler handler2;

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler2.removeCallbacksAndMessages(null); // Cancel pending stop
    }


    @Override
    protected void onResume() {
        super.onResume();
        roomFlagKey = getIntent().getStringExtra("roomFlagKey");

        // Stop any existing playback
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        int audioResId = (roomFlagKey != null) ? R.raw.ringing : R.raw.connecting;
        mediaPlayer = MediaPlayer.create(this, audioResId);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        handler2.postDelayed(() -> runOnUiThread(() -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                binding.cancel.performClick();
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }), 45000);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityConnectingVoiceBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        mContext = ConnectingVoiceActivity.this;



        Constant.getSfFuncion(mContext);
        database = FirebaseDatabase.getInstance();
        
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.calling));

        }


        // TODO: 16-Oct-24
        // neet to ask permission in a main activity
        askPermissions();
        username = Constant.getSF.getString(Constant.UID_KEY, "");
        handler = new Handler();
        handler2 = new Handler();
      //  animateDots();
        // set profile data
        String photo = getIntent().getStringExtra("photoReceiver");
        Constant.setSfFunction(mContext);
        Constant.setSF.putString(Constant.photoReceiver,photo).apply();
        ImageLoaderUtil.safeLoadImage(photo, binding.image, R.drawable.inviteimg);

        String name = getIntent().getStringExtra("nameReceiver");

        if (name != null) {
            binding.name.setText(name);
        }

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("stop_ringtone", false)) {
            stopRingtone();
        }

        senderId = getIntent().getStringExtra("senderId");
        receiverId = getIntent().getStringExtra("receiverId");
        roomFlagKey = getIntent().getStringExtra("roomFlagKey");
        myRoom = senderId + receiverId;


        String deleteroom = receiverId + senderId;


        DatabaseReference reference = database.getReference()
                .child("declineVoiceKey")
                .child(deleteroom);



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && mContext != null) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            database.getReference().child("declineVoiceKey").child(deleteroom).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Constant.Vibrator50(mContext);
                                    binding.voiceCalling.setVisibility(View.VISIBLE);
                                    binding.name2.setVisibility(View.INVISIBLE);
                                    binding.voiceCalling.setText(Constant.callDrop);

                                    if (mediaPlayer != null) {
                                        if (mediaPlayer.isPlaying()) {
                                            mediaPlayer.stop();
                                        }
                                        mediaPlayer.release();
                                        mediaPlayer = null;
                                    }
                                    handler2.removeCallbacksAndMessages(null);
                                }
                            })

                    );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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


        if (roomFlagKey != null) {
            //  Toast.makeText(mContext, "Sender", Toast.LENGTH_SHORT).show();

            HashMap<String, Object> room = new HashMap<>();
            room.put("incoming", username);
            room.put("createdBy", username);
            room.put("isAvailable", true);
            room.put("status", 0);

            firebaseRef
                    .child(username)
                    .setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            valueEventListener = firebaseRef
                                    .child(username)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if (snapshot.child("status").exists()) {
                                                if (snapshot.child("status").getValue(Integer.class) == 1) {

                                                    if (isOkay)
                                                        return;

                                                    isOkay = true;
                                                    Intent intent = new Intent(ConnectingVoiceActivity.this, CallVoiceActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);

                                                    String incoming = snapshot.child("incoming").getValue(String.class);
                                                    String createdBy = snapshot.child("createdBy").getValue(String.class);
                                                    boolean isAvailable = snapshot.child("isAvailable").getValue(Boolean.class);
                                                    intent.putExtra("username", username);
                                                    intent.putExtra("incoming", incoming);
                                                    intent.putExtra("createdBy", createdBy);
                                                    intent.putExtra("isAvailable", isAvailable);
                                                    intent.putExtra("myRoom", myRoom);
                                                    intent.putExtra("photoReceiver", photo);
                                                    intent.putExtra("nameReceiver", name);
                                                    intent.putExtra("roomFlagKey", roomFlagKey);
                                                    startActivity(intent);
                                                    runOnUiThread(ConnectingVoiceActivity.this::finish);

                                                    if (mediaPlayer != null) {
                                                        if (mediaPlayer.isPlaying()) {
                                                            mediaPlayer.stop();
                                                        }
                                                        mediaPlayer.release();
                                                        mediaPlayer = null;
                                                    }
                                                    handler2.removeCallbacksAndMessages(null);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        }
                                    });
                        }
                    });
        } else {
            if (isPermissionsGranted()) {
                valueEventListener = firebaseRef
                        .child(receiverId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    return;
                                }

                                Long status = snapshot.child("status").getValue(Long.class);
                                Boolean isAvailable = snapshot.child("isAvailable").getValue(Boolean.class);
                                String createdBy = snapshot.child("createdBy").getValue(String.class);
                                String incoming = snapshot.child("incoming").getValue(String.class);

                                Log.d("SNAPSHOT_DATA", "status: " + status);
                                Log.d("SNAPSHOT_DATA", "isAvailable: " + isAvailable);
                                Log.d("SNAPSHOT_DATA", "incoming: " + incoming);
                                Log.d("SNAPSHOT_DATA", "createdBy: " + createdBy);

                                if (status != null && status == 0) {
                                    snapshot.getRef().child("incoming").setValue(username);
                                    snapshot.getRef().child("status").setValue(1);
                                    Intent intent = new Intent(ConnectingVoiceActivity.this, CallVoiceActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.putExtra("username", username);
                                    intent.putExtra("incoming", incoming);
                                    intent.putExtra("createdBy", createdBy);
                                    intent.putExtra("isAvailable", isAvailable != null && isAvailable);
                                    intent.putExtra("myRoom", myRoom);
                                    intent.putExtra("photoReceiver", photo);
                                    intent.putExtra("nameReceiver", name);
                                    intent.putExtra("roomFlagKey", roomFlagKey);
                                    startActivity(intent);
                                    runOnUiThread(ConnectingVoiceActivity.this::finish);

                                    if (mediaPlayer != null) {
                                        if (mediaPlayer.isPlaying()) {
                                            mediaPlayer.stop();
                                        }
                                        mediaPlayer.release();
                                        mediaPlayer = null;
                                    }
                                    handler2.removeCallbacksAndMessages(null);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                Log.e("FIREBASE_ERROR", "Database error: " + error.getMessage());
                            }
                        });

            } else {
                askPermissions();
            }
        }


        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (valueEventListener != null) {
                    firebaseRef.child(username).removeEventListener(valueEventListener);
                    firebaseRef.child(receiverId).removeEventListener(valueEventListener);
                }
                // Toast.makeText(mContext, "cliecked", Toast.LENGTH_SHORT).show();
                firebaseRef.child(username).setValue(null);

                Constant.getSfFuncion(mContext);
                String userName = Constant.getSF.getString(Constant.full_name, "");
                database.getReference().child("NOTIARRIVED").child("status").child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String status = snapshot.getValue(String.class);
                            if (status != null) {

                                if (status.equals("TRUE")) {
                                    try {
                                        // todo this is for disconnect next notification
                                        String pushKey = database.getReference().child("removeCallNotification").child(receiverId).push().getKey();

                                        database.getReference().child("removeCallNotification").child(receiverId).child(pushKey.toString()).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }


                                    try {
                                        // TODO: 05/09/24 this for detect notification is arrived or not
                                        database.getReference().child("NOTIARRIVED").child("status").child(receiverId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    // TODO: 03/09/24  Send misscall notificartion from here

                                    if(!binding.voiceCalling.getText().toString().equals(Constant.callDrop)) {
                                     //   Toast.makeText(mContext, binding.voiceCalling.getText().toString(), Toast.LENGTH_SHORT).show();

                                        if (fTokenKey != null) {
                                            FcmNotificationSenderMisscall fmisscall = new FcmNotificationSenderMisscall(fTokenKey, "Enclosure", Constant.videoMissCall, photo, userName, "1");
                                            fmisscall.SendNotifications();


                                            try {
                                                // TODO: 05/09/24 this for detect notification is arrived or not
                                                database.getReference().child("NOTIARRIVED").child("status").child(receiverId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                });
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }


                                        }
                                    }

                                }
                            } else {


                            }
                        } else {
                            // TODO: 03/09/24  Send misscall notificartion from here

                            if(!binding.voiceCalling.getText().toString().equals(Constant.callDrop)) {
                                if (fTokenKey != null) {
                                    FcmNotificationSenderMisscall fmisscall = new FcmNotificationSenderMisscall(fTokenKey, "Enclosure", Constant.videoMissCall, photo, userName, "1");
                                    fmisscall.SendNotifications();
                                }
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Intent intent = new Intent(mContext, MainActivityOld.class);
                intent.putExtra("voiceCallKey", "voiceCallKey");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

//    private void animateDots() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateLoadingText();
//                animateDots();
//            }
//        }, 800); // Adjust the duration of each animation cycle (milliseconds)
//    }
//
//    private void updateLoadingText() {
//        String dots = "";
//        for (int i = 0; i < dotCount; i++) {
//            dots += ".";
//        }
//       // binding.name2.setText(dots);
//
//        // Increase the dot count and reset if it exceeds MAX_DOTS
//        dotCount++;
//        if (dotCount > MAX_DOTS) {
//            dotCount = 1;
//        }
//    }


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
        //  binding.cancel.performClick();
        if (valueEventListener != null) {
            firebaseRef.child(username).removeEventListener(valueEventListener);
            firebaseRef.child(receiverId).removeEventListener(valueEventListener);
        }
    }

    @Override
    public void onBackPressed() {
        // Stop MediaPlayer if playing
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Remove any delayed callbacks to avoid memory leaks or unexpected actions
        if (handler2 != null) {
            handler2.removeCallbacksAndMessages(null);
        }

        // Remove Firebase node if user is caller
        if (firebaseRef != null && username != null) {
            firebaseRef.child(username).setValue(null);
        }

        Intent intent = new Intent(mContext, MainActivityOld.class);
        intent.putExtra("voiceCallKey", "voiceCallKey");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


    }

}
