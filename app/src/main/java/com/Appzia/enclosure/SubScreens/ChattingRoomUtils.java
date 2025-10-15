package com.Appzia.enclosure.SubScreens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.get_user_active_chat_list_adapter;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Screens.dummyChattingScreen;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChattingRoomUtils implements ConnectivityReceiver.ConnectivityListener {
    private static final String TAG_KEY = "ChattingRoomUtils"; // Use string tag instead of resource ID
    private final Context mContext;
    private final View rootView;
    private RecyclerView inviterecyclerview;
    private LinearLayout enclosure;
    private ProgressBar progressBar;
    private ImageView contact1img;
    private TextView time;
    private View notiBack;
    private TextView contact1text;
    private ColorStateList tintList;
    private String themColor;
    private String uid;
    private String notiKey;
    private ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list;
    private get_user_active_chat_list_adapter adapter2;
    private FirebaseDatabase database;
    private ConnectivityReceiver connectivityReceiver;
    private boolean callOnce = true;
    private String fontSizePref;

    public ChattingRoomUtils(Context context, View rootView) {
        this.mContext = context;
        this.rootView = rootView;

        rootView.setTag(this);

        initializeViews();
        setup();
    }

    public static ChattingRoomUtils from(View root) {
        Object o = root.getTag();
        return (o instanceof ChattingRoomUtils) ? (ChattingRoomUtils) o : null;
    }

    public void refreshActiveChatList() {
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        Webservice.get_user_active_chat_list(
                rootView.getContext(), uid, this,
                rootView.findViewById(R.id.Inviterecyclerview),
                rootView.findViewById(R.id.enclosure),
                get_user_active_contact_list,
                rootView.findViewById(R.id.progressBar)
        );
    }

    private void initializeViews() {
        inviterecyclerview = rootView.findViewById(R.id.Inviterecyclerview);
        enclosure = rootView.findViewById(R.id.enclosure);
        progressBar = rootView.findViewById(R.id.progressBar);
        contact1img = rootView.findViewById(R.id.contact1img);
        time = rootView.findViewById(R.id.time);
        notiBack = rootView.findViewById(R.id.notiBack);
        contact1text = rootView.findViewById(R.id.contact1text);
    }

    public static void setupChattingRoomCode(View rootView) {
        new ChattingRoomUtils(rootView.getContext(), rootView);

    }

    private void setup() {


        database = FirebaseDatabase.getInstance();
        Constant.getSfFuncion(mContext);
        get_user_active_contact_list = new ArrayList<>();

        // Set up connectivity receiver
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, Context.RECEIVER_EXPORTED);

        // Set initial text
        contact1text.setText("@Enclosureforworld");

        // Set current time
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        time.setText(currentTime);

        // Load font size preference
        fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");

        // Load theme color
        themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        tintList = ColorStateList.valueOf(Color.parseColor(themColor));
        applyTheme();

        // Set back key
        Constant.setSfFunction(mContext);
        Constant.setSF.putString(Constant.BACKKEY, Constant.chattingBackValue);
        Constant.setSF.apply();

        // Check device ID
        String phoneIdx = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        String phoneNumber = Constant.getSF.getString(Constant.PHONE_NUMBERKEY, "");
        Webservice.getPhoneIdByMobile(mContext, phoneNumber, new Webservice.PhoneIdFetchListener() {
            @Override
            public void onSuccess(JSONObject data) {
                try {
                    String phoneId = data.getString("phone_id");
                    if (!phoneIdx.equals(phoneId)) {
                        showSessionEndedDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Toast.makeText(mContext, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Handle notification key
        notiKey = Constant.getSF.getString("notiKey", "");
        if (notiKey.equals("notiKey")) {
            notiBack.setVisibility(View.INVISIBLE);
            time.setTextColor(Color.parseColor("#808080"));
        } else {
            notiBack.setVisibility(View.VISIBLE);
        }

        // Load offline data
        loadOfflineData();

        // Fetch active chat list
        if (callOnce) {
            callOnce = false;
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                Webservice.get_user_active_chat_list(
                        mContext, uid, ChattingRoomUtils.this, inviterecyclerview, enclosure,
                        get_user_active_contact_list, progressBar);
            }, 500);
        }

        // Set up Firebase listener
        database.getReference()
                .child(Constant.chattingSocket)
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String message = snapshot.getValue(String.class);
                        if (message != null) {
                            Log.d("#Count", "Live: " + message);
                            Webservice.get_user_active_chat_list(
                                    mContext, uid, ChattingRoomUtils.this, inviterecyclerview,
                                    enclosure, get_user_active_contact_list, progressBar);
                        } else {
                            Log.w("#Count", "No message found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "Database error: " + error.getMessage());
                    }
                });

        // Set up enclosure click listener
        enclosure.setOnClickListener(v -> {
            Intent intent = ((Activity) mContext).getIntent();
            intent.removeExtra("grpKey");
            intent.removeExtra("voiceCallKey");
            intent.removeExtra("youKey");
            intent.removeExtra("videoCallKey");
            Intent newIntent = new Intent(mContext, dummyChattingScreen.class);
            SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, newIntent);
        });
    }



    private void applyTheme() {
        try {
            switch (themColor) {
                case "#ff0080":
                    contact1img.setImageResource(R.drawable.pinklogopng);
                    break;
                case "#00A3E9":
                    contact1img.setImageResource(R.drawable.ec_modern);
                    break;
                case "#7adf2a":
                    contact1img.setImageResource(R.drawable.popatilogopng);
                    break;
                case "#ec0001":
                    contact1img.setImageResource(R.drawable.redlogopng);
                    break;
                case "#16f3ff":
                    contact1img.setImageResource(R.drawable.bluelogopng);
                    break;
                case "#FF8A00":
                    contact1img.setImageResource(R.drawable.orangelogopng);
                    break;
                case "#7F7F7F":
                    contact1img.setImageResource(R.drawable.graylogopng);
                    break;
                case "#D9B845":
                    contact1img.setImageResource(R.drawable.yellowlogopng);
                    break;
                case "#346667":
                    contact1img.setImageResource(R.drawable.greenlogoppng);
                    break;
                case "#9846D9":
                    contact1img.setImageResource(R.drawable.voiletlogopng);
                    break;
                case "#A81010":
                    contact1img.setImageResource(R.drawable.red2logopng);
                    break;
            }
            if (!notiKey.equals("notiKey")) {
                time.setTextColor(Color.parseColor(themColor));
            }
            notiBack.setBackgroundTintList(tintList);
        } catch (Exception ignored) {
        }
    }

    private void showSessionEndedDialog() {
        Constant.dialogueLayoutForAll(mContext, R.layout.delete_ac_dialogue);
        TextView textView = Constant.dialogLayoutColor.findViewById(R.id.TextView);
        textView.setText("Session ended:\nThis number is now active on another device.");
        AppCompatButton cancel = Constant.dialogLayoutColor.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        AppCompatButton sure = Constant.dialogLayoutColor.findViewById(R.id.sure);
        sure.setOnClickListener(v -> {
            try {
                Runtime.getRuntime().exec("pm clear " + mContext.getPackageName());
                Intent intent = mContext.getPackageManager()
                        .getLaunchIntentForPackage(mContext.getPackageName());
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                    ((Activity) mContext).finishAffinity();
                    System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Constant.dialogLayoutColor.setCanceledOnTouchOutside(false);
        Constant.dialogLayoutColor.setCancelable(false);
        Constant.dialogLayoutColor.show();
    }

    private void loadOfflineData() {
        try {
            get_user_active_contact_list.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            get_user_active_contact_list = dbHelper.getAllData();
            if (get_user_active_contact_list.size() > 0) {
                setAdapter(get_user_active_contact_list);
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setAdapter(ArrayList<get_user_active_contact_list_Model> contactList) {
        this.get_user_active_contact_list = contactList;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        inviterecyclerview.setLayoutManager(linearLayoutManager);
        adapter2 = new get_user_active_chat_list_adapter(mContext, get_user_active_contact_list, enclosure);
        inviterecyclerview.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    public static void filteredList(String newText, ArrayList<get_user_active_contact_list_Model> contactList, get_user_active_chat_list_adapter adapter) {
        ArrayList<get_user_active_contact_list_Model> filteredList = new ArrayList<>();
        try {
            for (get_user_active_contact_list_Model list : contactList) {
                if (list.getFull_name().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(list);
                }
            }
            if (!filteredList.isEmpty()) {
                adapter.searchFilteredData(filteredList);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (!isConnected) {
            try {
                Log.d("Network", "disconnected: chattingRoom");
                get_user_active_contact_list.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_user_active_contact_list = dbHelper.getAllData();
                if (get_user_active_contact_list.size() > 0) {
                    setAdapter(get_user_active_contact_list);
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception ignored) {
            }
        }
    }

    public void cleanup() {
        mContext.unregisterReceiver(connectivityReceiver);
    }


}