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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.FcmNotificationSenderMisscall;
import com.Appzia.enclosure.Utils.MainApplication;
import com.Appzia.enclosure.databinding.ActivityConnectingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.Appzia.enclosure.Utils.ImageLoaderUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
public class ConnectingActivity extends AppCompatActivity {
    ActivityConnectingBinding binding;
    boolean isOkay = false;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private final int MAX_DOTS = 3;
    private ValueEventListener valueEventListener;
    private int dotCount = 1;
    String username;
    String receiverId;
    String senderId;
    private String myRoom;
    String roomFlagKey;
    String fTokenKey;
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
            //  Toast.makeText(mContext, "cancelled", Toast.LENGTH_SHORT).show();

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                binding.cancel.performClick(); // Trigger cancel logic
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }), 45000); // 45 seconds
    }
    private void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = ConnectingActivity.this;
        Constant.getSfFuncion(mContext);
        database = FirebaseDatabase.getInstance();

        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.calling));
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        } else {
            // Removed binding.cameraView.open();
        }

        askPermissions();
        username = Constant.getSF.getString(Constant.UID_KEY, "");
        handler = new Handler();
        handler2 = new Handler();


        // set profile data
        String photo = getIntent().getStringExtra("photoReceiver");
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


        if (roomFlagKey != null) {
            if (isPermissionsGranted()) {
                HashMap<String, Object> room = new HashMap<>();
                room.put("incoming", username);
                room.put("createdBy", username);
                room.put("isAvailable", true);
                room.put("status", 0);

                firebaseRef.child(username).setValue(room).addOnSuccessListener(unused -> {
                    valueEventListener = firebaseRef.child(username).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("status").exists() &&
                                    snapshot.child("status").getValue(Integer.class) == 1) {

                                if (isOkay) return;
                                isOkay = true;

                                Intent intent = new Intent(ConnectingActivity.this, CallActivity.class);
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
                                runOnUiThread(ConnectingActivity.this::finish);

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
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                });
            } else {
                askPermissions();
            }

        } else {
            if (isPermissionsGranted()) {
                valueEventListener = firebaseRef.child(receiverId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                            Intent intent = new Intent(ConnectingActivity.this, CallActivity.class);
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
                            runOnUiThread(ConnectingActivity.this::finish);

                            if (mediaPlayer != null) {
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                }
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                            handler2.removeCallbacksAndMessages(null); // Cancel pending stop
                        } else {
                            // Handle case where status is not 0
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
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
                                        String pushKey = database.getReference().child("removeVideoCallNotification").child(receiverId).push().getKey();

                                        database.getReference().child("removeVideoCallNotification").child(receiverId).child(pushKey.toString()).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            } else {


                            }
                        } else {
                            // TODO: 03/09/24  Send misscall notificartion from here


                            if (fTokenKey != null) {
                                FcmNotificationSenderMisscall fmisscall = new FcmNotificationSenderMisscall(fTokenKey, "Enclosure", Constant.videoMissCall, photo, userName, "1");
                                fmisscall.SendNotifications();
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                runOnUiThread(ConnectingActivity.this::finish);
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

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Removed binding.cameraView.open();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
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
        // Removed binding.cameraView.destroy();
        if (valueEventListener != null) {
            firebaseRef.child(username).removeEventListener(valueEventListener);
            firebaseRef.child(receiverId).removeEventListener(valueEventListener);
        }

    }






}