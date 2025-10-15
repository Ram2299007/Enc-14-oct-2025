package com.Appzia.enclosure.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.get_user_active_contact_list_msgLmt_Adapter;
import com.Appzia.enclosure.Model.get_user_active_contact_list_MessageLmt_Model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.FragmentMsgLimitBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


import java.util.ArrayList;

public class msgLimitFragment extends Fragment implements ConnectivityReceiver.ConnectivityListener {

    FragmentMsgLimitBinding binding;
    private boolean hasSlidBelowThreshold = false;
    private boolean hasSlidDown = false;
    private boolean hasSlidUp = false;

    private CardView customToastCard;
    private TextView customToastText;
    public static TextView txt1;
    ArrayList<get_user_active_contact_list_MessageLmt_Model> get_user_active_contact_listmsgLmt = new ArrayList<>();
    String themColor;
    ColorStateList tintList;
    public static String fontSizePref;

    public static RecyclerView msgLimitRecyclerview;
    Context mContext;
    get_user_active_contact_list_msgLmt_Adapter adapter;
    String uid;
    private ConnectivityReceiver connectivityReceiver;
    public static LinearLayout backlytGrpFrag;
//  backlyt = binding.getRoot().findViewById(R.id.backlyt);

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected) {
        } else {
            Log.d("Network", "dissconnected: " + "msgLimit");


            try {

                Log.d("Network", "dissconnetced: " + "chattingRoom");
                get_user_active_contact_listmsgLmt.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_user_active_contact_listmsgLmt = dbHelper.getAllDataMessageLimit();

                if (get_user_active_contact_listmsgLmt.size() > 0) {

                    setAdapter(get_user_active_contact_listmsgLmt);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                }


            } catch (Exception ignored) {
            }

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
            Constant.setSF.apply();

        } catch (Exception ignored) {
        }
    }

    @Override
    public void onStart() {
        super.onStart();


        try {
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
            Constant.setSF.apply();
        } catch (Exception ignored) {
        }
        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#00A3E9")) {

                    binding.view.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7adf2a")) {

                    binding.view.setBackgroundTintList(tintList);


                } else if (themColor.equals("#ec0001")) {

                    binding.view.setBackgroundTintList(tintList);


                } else if (themColor.equals("#16f3ff")) {

                    binding.view.setBackgroundTintList(tintList);


                } else if (themColor.equals("#FF8A00")) {

                    binding.view.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {


                    binding.view.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {

                    binding.view.setBackgroundTintList(tintList);


                } else if (themColor.equals("#346667")) {

                    binding.view.setBackgroundTintList(tintList);


                } else if (themColor.equals("#9846D9")) {

                    binding.view.setBackgroundTintList(tintList);


                } else if (themColor.equals("#A81010")) {

                    binding.view.setBackgroundTintList(tintList);


                } else {

                    binding.view.setBackgroundTintList(tintList);

                }
            } catch (Exception ignored) {

                binding.view.setBackgroundTintList(tintList);
            }


        } catch (Exception ignored) {
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //todo ----------------------online--------------------------

        backlytGrpFrag = binding.getRoot().findViewById(R.id.backlyt);
        try {
            //TODO : - OFFLINE DATA LOAD HERE WHEN ,UNTIL WEBSERVICE 200 STATUS OR ERROR

            get_user_active_contact_listmsgLmt.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            get_user_active_contact_listmsgLmt = dbHelper.getAllDataMessageLimit();

            if (get_user_active_contact_listmsgLmt.size() > 0) {
                // Toast.makeText(mContext, "not empty", Toast.LENGTH_SHORT).show();
                setAdapter(get_user_active_contact_listmsgLmt);
                binding.progressBar.setVisibility(View.GONE);
                binding.noData.setVisibility(View.GONE);
            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        Log.d("Network", "connected: " + "msgLimit");

        txt1 = requireActivity().findViewById(R.id.txt1);
        Constant.getSfFuncion(mContext);

        try {

            Constant.getSfFuncion(mContext);
            String msg_limitFORALL = Constant.getSF.getString(Constant.msg_limitFORALL,"0");

            if(!msg_limitFORALL.equals("0")){
                binding.txt1.setText(msg_limitFORALL);
            }

          //  Webservice.get_message_limit_for_all_users(mContext, Constant.getSF.getString(Constant.UID_KEY, ""), msgLimitFragment.this);
        } catch (Exception ignored) {
        }

        try {
            Constant.get_user_active_contact_list.clear();
            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");


         //   Webservice.get_user_active_chat_list_for_msgLmt(mContext, uid, msgLimitFragment.this, binding.msgLimitRecyclerview, binding.progressBar, binding.noData);


        } catch (Exception ignored) {
        }


        try {
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
            Constant.setSF.apply();
        } catch (Exception ignored) {
        }
        //todo ----------------------online--------------------------
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(connectivityReceiver);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMsgLimitBinding.inflate(inflater, container, false);

        mContext = binding.getRoot().getContext();

        customToastCard = binding.getRoot().findViewById(R.id.includedToast);
        customToastText = customToastCard.findViewById(R.id.toastText);



        // Inflate your custom bottom sheet layout
        View bottomSheet = binding.getRoot().findViewById(R.id.localBottomSheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

// Prevent collapse/hidden
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetBehavior.setSkipCollapsed(true);
        bottomSheetBehavior.setHideable(false);

// RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.msgLimitRecyclerview.setLayoutManager(layoutManager);
// binding.recyclerviewAZ.setLayoutManager(layoutManager);

// BottomSheetCallback
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED ||
                        newState == BottomSheetBehavior.STATE_HALF_EXPANDED ||
                        newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("BottomSheet", "Sliding: " + slideOffset);

                // Enable dragging so user can pull slightly again
                if (slideOffset >= 1.0f) {

                    hasSlidBelowThreshold = false;
                }

                // Trigger only once when it drops below threshold
                else if (slideOffset < 0.996f && !hasSlidBelowThreshold) {
                    hasSlidBelowThreshold = true;

                    Log.d("BottomSheet", "Less than fully expanded");
                    if(get_user_active_contact_listmsgLmt.size()>7) {
                        if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                            MainActivityOld.slideDownContainer();
                        }
                    }

//
                }
            }
        });



        binding.msgLimitRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();

                    View firstView = layoutManager.findViewByPosition(firstVisiblePosition);

                    // Slide down only once when top is fully visible
                    if (firstVisiblePosition == 0 && firstView != null && firstView.getTop() == 0) {
                        if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE && !hasSlidDown) {
                            Log.d("Scroll", "Upper part of first item fully visible");
                            if(get_user_active_contact_listmsgLmt.size()>7) {
                                MainActivityOld.slideDownContainer();
                            }
                            hasSlidDown = true;
                            hasSlidUp = false; // Reset opposite flag
                        }
                    } else {
                        hasSlidDown = false; // Reset when user scrolls away from top
                    }

                    // Slide up only once when bottom is fully visible
                    if (lastVisiblePosition == totalItemCount - 1 && totalItemCount > 0) {
                        View lastView = layoutManager.findViewByPosition(lastVisiblePosition);
                        if (lastView != null && lastView.getBottom() <= recyclerView.getHeight()) {
                            if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE && !hasSlidUp) {
                                Log.d("Scroll", "Reached end of list. Sliding up container.");
                                if(get_user_active_contact_listmsgLmt.size()>7) {
                                    if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                                        MainActivityOld.slideUpContainer();
                                    }

                                }
                                hasSlidUp = true;
                                hasSlidDown = false; // Reset opposite flag
                            }
                        }
                    } else {
                        hasSlidUp = false; // Reset when user scrolls away from bottom
                    }
                }
            }
        });




        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, Constant.searchMsgLmt);
        binding.search.setAdapter(adapter);

        backlytGrpFrag = binding.getRoot().findViewById(R.id.backlyt);
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, mContext.RECEIVER_EXPORTED);

        msgLimitRecyclerview = binding.getRoot().findViewById(R.id.msgLimitRecyclerview);

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideDownContainer();
                }
            }
        });
//        binding.main.setOnTouchListener(new OnSwipeTouchListener(mContext) {
//            public void onSwipeTop() {
//
//                MainActivityOld.slideUpContainer();
//                binding.msgLimitRecyclerview.setOnTouchListener(null);
//
//            }
//
//            public void onSwipeRight() {
//
//            }
//
//            public void onSwipeLeft() {
//
//            }
//
//            public void onSwipeBottom() {
//
//                MainActivityOld.slideDownContainer();
//                binding.msgLimitRecyclerview.setOnTouchListener(new OnSwipeTouchListener(mContext) {
//                    @Override
//                    public void onSwipeTop() {
//                        MainActivityOld.slideUpContainer();
//                        binding.msgLimitRecyclerview.setOnTouchListener(null);
//
//
//                    }
//                });
//            }
//
//        });
//        binding.msgLimitRecyclerview.setOnTouchListener(new OnSwipeTouchListener(mContext) {
//            public void onSwipeTop() {
//
//                MainActivityOld.slideUpContainer();
//                binding.msgLimitRecyclerview.setOnTouchListener(null);
//            }
//
//            public void onSwipeRight() {
//
//            }
//
//            public void onSwipeLeft() {
//
//            }
//
//            public void onSwipeBottom() {
//
//                //   MainActivity.slideDownContainer();
//            }
//
//        });


        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                filteredList(String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // for listen sceroll view scroll more than 710


        binding.search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideUpContainer();
                }

                binding.search.requestFocus();
                return false;

            }
        });

        Constant.dialogueLayoutMsgLmt(mContext, R.layout.message_lmt_lyt);
        Dialog dialogueLayoutMsgLmt = Constant.dialogLayoutColor;
        EditText et1 = dialogueLayoutMsgLmt.findViewById(R.id.et1);

        binding.allmsglmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideUpContainer();
                }


                dialogueLayoutMsgLmt.show();
                et1.requestFocus();
                et1.setSelection(et1.getText().length());

                if (et1.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }
        });


        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (et1.getText().toString().isEmpty()) {
                    binding.txt1.setText("0");
                }else {
                    binding.txt1.setText(et1.getText().toString());
                }


                if (et1.getText().toString().length() == 3) {
                    binding.cardview.setVisibility(View.GONE);
                    //   et1.setText("");

                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Constant.getSfFuncion(mContext);
                String uid = Constant.getSF.getString(Constant.UID_KEY, "");


                try {


                //    Webservice.set_message_limit_for_all_users(mContext, uid, binding.txt1.getText().toString(), msgLimitFragment.this, binding.msgLimitRecyclerview, binding.cardview, binding.progressBar, binding.noData, et1,customToastCard,customToastText);


                } catch (Exception ignored) {
                }


            }
        });

        binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.searchLytNew.getVisibility() == View.VISIBLE) {
                    binding.searchLytNew.setVisibility(View.GONE);


                    View view = requireActivity().findViewById(android.R.id.content);
                    InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } else if (binding.searchLytNew.getVisibility() == View.GONE) {

                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_four);
                    binding.searchLytNew.setAnimation(animation);
                    binding.searchLytNew.setVisibility(View.VISIBLE);

                    binding.search.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }

            }
        });


        return binding.getRoot();
    }

    public void setAdapter(ArrayList<get_user_active_contact_list_MessageLmt_Model> get_user_active_contact_listmsgLmt) {
        // Get the current user's UID
        Constant.getSfFuncion(mContext);
        String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");

        // Filter the list: exclude current user and blocked users
        ArrayList<get_user_active_contact_list_MessageLmt_Model> filteredList = new ArrayList<>();
        for (get_user_active_contact_list_MessageLmt_Model model : get_user_active_contact_listmsgLmt) {
            if (!model.getUid().equals(currentUid) && !model.isBlock()) {
                filteredList.add(model);
            }
        }

        // Use the filtered list
        this.get_user_active_contact_listmsgLmt = filteredList;

        // Set up the RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.msgLimitRecyclerview.setLayoutManager(linearLayoutManager);

        adapter = new get_user_active_contact_list_msgLmt_Adapter(mContext, this.get_user_active_contact_listmsgLmt, customToastCard, customToastText);
        binding.msgLimitRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    private void filteredList(String newText) {
        ArrayList<get_user_active_contact_list_MessageLmt_Model> filteredList = new ArrayList<>();

        for (get_user_active_contact_list_MessageLmt_Model list : get_user_active_contact_listmsgLmt) {
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