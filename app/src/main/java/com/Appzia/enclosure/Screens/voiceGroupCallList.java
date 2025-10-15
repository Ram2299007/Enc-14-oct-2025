package com.Appzia.enclosure.Screens;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.get_video_calling_adapter2;
import com.Appzia.enclosure.Adapter.get_voice_calling_adapter2;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.databinding.ActivityVoiceGroupCallListBinding;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class voiceGroupCallList extends AppCompatActivity {
    ActivityVoiceGroupCallListBinding binding;
    private ArrayList<get_contact_model> get_calling_contact_list = new ArrayList<>();
    String receiverId, roomId;
    Context mContext;
    private CardView customToastCard;
    private get_voice_calling_adapter2 adapter;
    public DatabaseReference firebaseRef;
    String roomFlagKey;
    private TextView customToastText;
    private ArrayList<String> participants = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityVoiceGroupCallListBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        mContext = voiceGroupCallList.this;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window2 = getWindow();
//            window2.setStatusBarColor(Color.BLACK); // Make status bar background black
//        }
//
//        receiverId = getIntent().getStringExtra("receiverId");
//        roomId = getIntent().getStringExtra("roomId");
//
//        customToastCard = findViewById(R.id.includedToast);
//        customToastText = customToastCard.findViewById(R.id.toastText);
//
//        ArrayList<get_contact_model> contactList = new DatabaseHelper(mContext).get_users_all_contactTable();
//
//
//        setAdapter(contactList, binding.recyclerview, customToastCard, customToastText, receiverId,roomId);
//        binding.searchIcon.setOnClickListener(v1 -> {
//            if (binding.searchLytNew.getVisibility() == View.VISIBLE) {
//                binding.searchLytNew.setVisibility(View.GONE);
//                View view = findViewById(android.R.id.content);
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//            } else {
//                binding.search.requestFocus();
//                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_four);
//                binding.searchLytNew.setAnimation(animation);
//                binding.searchLytNew.setVisibility(View.VISIBLE);
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                if (inputMethodManager != null) {
//                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                }
//            }
//        });
//
//        binding.search.addTextChangedListener(new TextWatcher() {
//            private final Handler handler = new Handler();
//            private Runnable runnable;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (runnable != null) {
//                    handler.removeCallbacks(runnable);
//                }
//                runnable = () -> {
//                    if (String.valueOf(s).isEmpty()) {
//                        setAdapter(contactList, binding.recyclerview, customToastCard, customToastText, receiverId, roomId);
//                    } else {
//                        filteredList(String.valueOf(s));
//                    }
//                };
//                handler.postDelayed(runnable, 1000);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        binding.backarrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//    }

//    public void setAdapter(ArrayList<get_contact_model> get_calling_contact_list, RecyclerView recyclerview, CardView customToastCard, TextView customToastText, String username, String roomId) {
//        Constant.getSfFuncion(mContext);
//        String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");
//
//        ArrayList<get_contact_model> filteredList = new ArrayList<>();
//        for (get_contact_model model : get_calling_contact_list) {
//            if (!model.getUid().equals(currentUid) && !participants.contains(model.getUid())) {
//                filteredList.add(model);
//            }
//        }
//        this.get_calling_contact_list = filteredList;
//
//        adapter = new get_voice_calling_adapter2(
//                mContext,
//                this.get_calling_contact_list,
//                customToastCard,
//                customToastText,
//                username,roomId
//        );
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        recyclerview.setLayoutManager(layoutManager);
//        recyclerview.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }

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
}