package com.Appzia.enclosure.Fragments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.Appzia.enclosure.Adapter.get_user_active_chat_list_adapter;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Screens.dummyChattingScreen;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.FragmentChattingRoomBinding;
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

public class ChattingRoom extends Fragment implements ConnectivityReceiver.ConnectivityListener {

    FragmentChattingRoomBinding binding;
    public static RecyclerView Inviterecyclerview;
    ColorStateList tintList;
    String themColor;
    Context mContext;
    Handler handler;
    public static ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list;
    String currentTime;
    public static get_user_active_chat_list_adapter adapter2;
    public static String fontSizePref;
    public static LinearLayout ecnlosure;
    String uid;
    String notiKey;
    FirebaseDatabase database;
    private ConnectivityReceiver connectivityReceiver;
    boolean callOnce = true;

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected) {

        } else {
            try {
                Log.d("Network", "dissconnetced: " + "chattingRoom");
                get_user_active_contact_list.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_user_active_contact_list = dbHelper.getAllData();
                if(get_user_active_contact_list.size()>0){
                    setAdapter(get_user_active_contact_list);
                    binding.progressBar.setVisibility(View.GONE);
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            } catch (Exception ignored) {
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        try {

            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY, Constant.chattingBackValue);
            Constant.setSF.apply();

        } catch (Exception ignored) {
        }


        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        currentTime = sdf.format(new Date());
        binding.time.setText(currentTime);

        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {


            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY, Constant.chattingBackValue);
            Constant.setSF.apply();

        } catch (Exception ignored) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        String phoneIdx = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        String phoneNumber = Constant.getSF.getString(Constant.PHONE_NUMBERKEY, "");

        Webservice.getPhoneIdByMobile(mContext, phoneNumber, new Webservice.PhoneIdFetchListener() {
            @Override
            public void onSuccess(JSONObject data) {
                try {
                    String phoneId = data.getString("phone_id");

                    if(!phoneIdx.equals(phoneId)){
                        Constant.dialogueLayoutForAll(mContext, R.layout.delete_ac_dialogue);

                        TextView textView = Constant.dialogLayoutColor.findViewById(R.id.TextView);
                        textView.setText("Session ended:\nThis number is now active on another device.");

                        AppCompatButton cancel = Constant.dialogLayoutColor.findViewById(R.id.cancel);
                        cancel.setVisibility(View.GONE); // Hide cancel button

                        AppCompatButton sure = Constant.dialogLayoutColor.findViewById(R.id.sure);
                        sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    // Clear all app data: cache, storage, shared preferences, databases, etc.
                                    Runtime.getRuntime().exec("pm clear " + mContext.getPackageName());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // Restart the app after clearing
                                Intent intent = mContext.getPackageManager()
                                        .getLaunchIntentForPackage(mContext.getPackageName());
                                if (intent != null) {
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(intent);
                                    ((Activity) mContext).finishAffinity(); // Close all previous activities
                                    System.exit(0); // Kill the current process to ensure restart
                                }
                            }
                        });



                        Constant.dialogLayoutColor.setCanceledOnTouchOutside(false);
                        Constant.dialogLayoutColor.setCancelable(false);

                        Constant.dialogLayoutColor.show();

                        //   Toast.makeText(mContext, "⚠️ Number exists, but phone ID doesn't match.\nYou may have logged in on another device.", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                //  Toast.makeText(mContext, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


        Constant.getSfFuncion(mContext);
        String callOnceText = Constant.getSF.getString("callOnce","");

        if(callOnceText.equals("callOnce")){
            callOnce = true;
            Constant.setSfFunction(mContext);
            Constant.setSF.putString("callOnce","");
            Constant.setSF.apply();
        }

        //todo ----------------------------------------------------- online
        if(callOnce){

            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");

// Show progressBar if needed before delay
            binding.progressBar.setVisibility(View.GONE);

// Add 500ms delay before hitting API
//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                Webservice.get_user_active_chat_list(
//                        mContext,
//                        uid,
//                        ChattingRoom.this,
//                        binding.Inviterecyclerview,
//                        binding.enclosure,
//                        get_user_active_contact_list,
//                        binding.progressBar
//                );
//            }, 500);
        }




        try {
            Constant.getSfFuncion(mContext);
            notiKey = Constant.getSF.getString("notiKey", "");

            if (notiKey.equals("notiKey")) {
                binding.notiBack.setVisibility(View.INVISIBLE);
                binding.time.setTextColor(Color.parseColor("#808080"));
            } else {
                binding.notiBack.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
        }


        try {
            // TODO : - OFFLINE DATA LOAD HERE WHEN ,UNTIL WEBSERVICE 200 STATUS OR ERROR

            get_user_active_contact_list.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            get_user_active_contact_list = dbHelper.getAllData();

            if (get_user_active_contact_list.size() > 0) {
                // Toast.makeText(mContext, "not empty", Toast.LENGTH_SHORT).show();
                setAdapter(get_user_active_contact_list);
                binding.progressBar.setVisibility(View.GONE);
            } else {

                binding.progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        Log.d("Network", "connected: " + "chattingRoom");



        //todo -----------------------------------------------------



        //IMG_ARROW_KEY
        Bundle bundle = getArguments();
        if (bundle != null) {

            String data = bundle.getString("IMG_ARROW_KEY");
            if (data != null) {
                if (data.equals("IMG_ARROW_KEY")) {
                    bundle.putString("IMG_ARROW_KEY", null);
                } else {

                    try {
                        adapter2.notifyDataSetChanged();
                    } catch (Exception ignored) {

                    }

                    try {

                        Constant.getSfFuncion(mContext);
                        uid = Constant.getSF.getString(Constant.UID_KEY, "");
                 //       Webservice.get_user_active_chat_list(mContext, uid, ChattingRoom.this, binding.Inviterecyclerview, binding.enclosure, get_user_active_contact_list, binding.progressBar);
                        Log.d("#Count", "Web");


                    } catch (Exception ignored) {
                    }
//


                    if (MainActivityOld.searchIcon.getVisibility() == View.INVISIBLE) {
                        MainActivityOld.searchIcon.setVisibility(View.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                        MainActivityOld.searchIcon.setAnimation(animation);

                    }

                    ecnlosure = binding.getRoot().findViewById(R.id.enclosure);
                    try {
                        Constant.setSfFunction(mContext);
                        Constant.setSF.putString(Constant.BACKKEY, Constant.chattingBackValue);
                        Constant.setSF.apply();

                    } catch (Exception ignored) {
                    }
                }
            } else {


                try {
                    adapter2.notifyDataSetChanged();
                } catch (Exception ignored) {

                }

                try {

                    Constant.getSfFuncion(mContext);
                    uid = Constant.getSF.getString(Constant.UID_KEY, "");
                 //   Webservice.get_user_active_chat_list(mContext, uid, ChattingRoom.this, binding.Inviterecyclerview, binding.enclosure, get_user_active_contact_list, binding.progressBar);
                    Log.d("#Count", "Web");


                } catch (Exception ignored) {
                }
//


                if (MainActivityOld.searchIcon.getVisibility() == View.INVISIBLE) {
                    MainActivityOld.searchIcon.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                    MainActivityOld.searchIcon.setAnimation(animation);

                }

                ecnlosure = binding.getRoot().findViewById(R.id.enclosure);
                try {
                    Constant.setSfFunction(mContext);
                    Constant.setSF.putString(Constant.BACKKEY, Constant.chattingBackValue);
                    Constant.setSF.apply();

                } catch (Exception ignored) {
                }
            }
        }


        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {

                    binding.contact1img.setImageResource(R.drawable.pinklogopng);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");

                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);
                } else if (themColor.equals("#00A3E9")) {

                    binding.contact1img.setImageResource(R.drawable.ec_modern);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");
                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);
                } else if (themColor.equals("#7adf2a")) {


                    binding.contact1img.setImageResource(R.drawable.popatilogopng);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");
                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);

                } else if (themColor.equals("#ec0001")) {

                    binding.contact1img.setImageResource(R.drawable.redlogopng);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");
                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);

                } else if (themColor.equals("#16f3ff")) {

                    binding.contact1img.setImageResource(R.drawable.bluelogopng);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");
                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);
                } else if (themColor.equals("#FF8A00")) {

                    binding.contact1img.setImageResource(R.drawable.orangelogopng);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");
                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7F7F7F")) {

                    binding.contact1img.setImageResource(R.drawable.graylogopng);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");
                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {

                    binding.contact1img.setImageResource(R.drawable.yellowlogopng);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");
                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);

                } else if (themColor.equals("#346667")) {

                    binding.contact1img.setImageResource(R.drawable.greenlogoppng);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");
                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);

                } else if (themColor.equals("#9846D9")) {

                    binding.contact1img.setImageResource(R.drawable.voiletlogopng);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");
                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);

                } else if (themColor.equals("#A81010")) {

                    binding.contact1img.setImageResource(R.drawable.red2logopng);
                    Constant.getSfFuncion(mContext);
                    notiKey = Constant.getSF.getString("notiKey", "");
                    if (notiKey.equals("notiKey")) {
                        binding.time.setTextColor(Color.parseColor("#808080"));
                    } else {
                        binding.time.setTextColor(Color.parseColor(themColor));

                    }
                    binding.notiBack.setBackgroundTintList(tintList);

                } else {

                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentChattingRoomBinding.inflate(inflater, container, false);
        mContext = binding.getRoot().getContext();

        Inviterecyclerview = requireActivity().findViewById(R.id.Inviterecyclerview);

        database = FirebaseDatabase.getInstance();
        Constant.getSfFuncion(mContext);
        handler = new Handler();


        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);

        get_user_active_contact_list = new ArrayList<>();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter,mContext.RECEIVER_EXPORTED);


        binding.contact1text.setText("@Enclosureforworld");

        try {
            callOnce = false;

            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
           // Webservice.get_user_active_chat_list(mContext, uid, ChattingRoom.this, binding.Inviterecyclerview, binding.enclosure, get_user_active_contact_list,binding.progressBar);
            Log.d("#Count", "Web");



        } catch (Exception ignored) {
        }








        database.getReference()
                .child(Constant.chattingSocket)
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String message = snapshot.getValue(String.class);
                        if (message != null) {
                            Log.d("#Count", "Live: " + message);
//                            Webservice.get_user_active_chat_list(
//                                    mContext,
//                                    uid,
//                                    ChattingRoom.this,
//                                    binding.Inviterecyclerview,
//                                    binding.enclosure,
//                                    get_user_active_contact_list,
//                                    binding.progressBar
//                            );
                        } else {
                            Log.w("#Count", "No message found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "Database error: " + error.getMessage());
                        // Optionally notify the user
                    }
                });


        binding.enclosure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent0 = ((Activity)mContext).getIntent();
                if (intent0.hasExtra("grpKey")) {
                    intent0.removeExtra("grpKey");
                }

                Intent intent00 = ((Activity)mContext).getIntent();
                if (intent00.hasExtra("voiceCallKey")) {
                    intent00.removeExtra("voiceCallKey");
                }
                Intent intent000 = ((Activity)mContext).getIntent();
                if (intent000.hasExtra("youKey")) {
                    intent000.removeExtra("youKey");
                }
                Intent intent0000 = ((Activity)mContext).getIntent();
                if (intent0000.hasExtra("videoCallKey")) {
                    intent0000.removeExtra("videoCallKey");
                }

                Intent intent = new Intent(getActivity(), dummyChattingScreen.class);
                startActivity(intent);
            }
        });


//        binding.Inviterecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (MainActivityOld.upperMainuContainer.getVisibility() == View.VISIBLE) {
//
//                    Animation fadeout = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out);
//                    MainActivityOld.upperMainuContainer.setVisibility(View.GONE);
//                    MainActivityOld.upperMainuContainer.startAnimation(fadeout);
//
//                }
//            }
//        });




        return binding.getRoot();
    }

    public void setAdapter(ArrayList<get_user_active_contact_list_Model> get_user_active_contact_lists) {
        this.get_user_active_contact_list = get_user_active_contact_lists;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.Inviterecyclerview.setLayoutManager(linearLayoutManager);
        adapter2 = new get_user_active_chat_list_adapter(mContext, get_user_active_contact_list,binding.enclosure);
        binding.Inviterecyclerview.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    public static void filteredList(String newText) {
        ArrayList<get_user_active_contact_list_Model> filteredList = new ArrayList<>();

        try {
            for (get_user_active_contact_list_Model list :  get_user_active_contact_list) {
                if (list.getFull_name().toLowerCase().contains(newText.toLowerCase())) {

                    filteredList.add(list);
                }
            }
        } catch (Exception e) {

        }

        if (filteredList.isEmpty()) {
            //   Toast.makeText(ecnlosure.getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter2.searchFilteredData(filteredList);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(connectivityReceiver);
    }


}