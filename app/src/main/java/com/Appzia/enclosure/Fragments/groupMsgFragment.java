package com.Appzia.enclosure.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.Appzia.enclosure.Adapter.grpListAdapter;
import com.Appzia.enclosure.Model.grp_list_child_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Screens.newGroupActivity;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.FragmentGroupMsgBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class groupMsgFragment extends Fragment implements ConnectivityReceiver.ConnectivityListener {
    FragmentGroupMsgBinding binding;
    String themColor;
    ColorStateList tintList;
    private boolean hasSlidDown = false;
    private boolean hasSlidUp = false;
    public static String fontSizePref;
    Context mContext;
    private boolean hasSlidBelowThreshold = false;
    ArrayList<grp_list_child_model> dataList;
    grpListAdapter adapter;
    public static RecyclerView grpRecycler;
    String uid;
    private ConnectivityReceiver connectivityReceiver;
    public static LinearLayout backlytGroupFrag;
    //  backlyt = binding.getRoot().findViewById(R.id.backlyt);
    @Override
    public void onConnectivityChanged(boolean isConnected) {

        if (isConnected) {
        } else {
            Log.d("Network", "onConnectivityChanged: " + "discconnected");


            try {

                dataList.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                dataList = dbHelper.getAllDataGrpListChildModel();
                if(dataList.size()>0){
                    binding.progressBar.setVisibility(View.GONE);
                    setAdapter(dataList);
                }else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                }


            } catch (Exception ignored) {
                Toast.makeText(mContext, ignored.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onConnectivityChangedGrpMessage: "+ignored.getMessage());
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //todo ----------------online---------------------------------

        backlytGroupFrag = binding.getRoot().findViewById(R.id.backlyt);

        try {
            //TODO : - OFFLINE DATA LOAD HERE WHEN ,UNTIL WEBSERVICE 200 STATUS OR ERROR

            dataList.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            dataList = dbHelper.getAllDataGrpListChildModel();
            if (dataList.size() > 0) {
                setAdapter(dataList);
                binding.progressBar.setVisibility(View.GONE);
            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        try {
            Log.d("Network", "onConnectivityChanged: " + "connected");
            //  Toast.makeText(mContext, "connected", Toast.LENGTH_SHORT).show();
            Constant.getSfFuncion(mContext);
            uid = Constant.getSF.getString(Constant.UID_KEY, "");
         //   Webservice.get_group_list(mContext, uid, groupMsgFragment.this, binding.grpRecycler,binding.progressBar,binding.noData);

        } catch (Exception ignored) {
        }

        try {
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
            Constant.setSF.apply();
            //  Toast.makeText(mContext, "grp", Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) {
        }
        //todo ----------------online---------------------------------
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
            //  Toast.makeText(mContext, "grp", Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) {
        }

        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
                    binding.floating.setBackgroundTintList(tintList);
                } else if (themColor.equals("#00A3E9")) {
                    binding.floating.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {
                    binding.floating.setBackgroundTintList(tintList);

                } else if (themColor.equals("#ec0001")) {
                    binding.floating.setBackgroundTintList(tintList);

                } else if (themColor.equals("#16f3ff")) {
                    binding.floating.setBackgroundTintList(tintList);

                } else if (themColor.equals("#FF8A00")) {
                    binding.floating.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7F7F7F")) {

                    binding.floating.setBackgroundTintList(tintList);
                } else if (themColor.equals("#D9B845")) {
                    binding.floating.setBackgroundTintList(tintList);

                } else if (themColor.equals("#346667")) {
                    binding.floating.setBackgroundTintList(tintList);

                } else if (themColor.equals("#9846D9")) {
                    binding.floating.setBackgroundTintList(tintList);

                } else if (themColor.equals("#A81010")) {
                    binding.floating.setBackgroundTintList(tintList);

                } else {
                    binding.floating.setBackgroundTintList(tintList);
                }
            } catch (Exception ignored) {
                binding.floating.setBackgroundTintList(tintList);
            }


        } catch (Exception ignored) {
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        // Revert the changes when the fragment is not visible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT >= 35) {
                // Show system bars when the fragment stops
                requireActivity().getWindow().getInsetsController().show(WindowInsets.Type.systemBars());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupMsgBinding.inflate(inflater, container, false);



        mContext = binding.getRoot().getContext();
        backlytGroupFrag = binding.getRoot().findViewById(R.id.backlyt);
        dataList = new ArrayList<>();
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        grpRecycler = binding.getRoot().findViewById(R.id.grpRecycler);

        // Inflate your custom bottom sheet layout
        View bottomSheet = binding.getRoot().findViewById(R.id.localBottomSheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

// Prevent collapse/hidden
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetBehavior.setSkipCollapsed(true);
        bottomSheetBehavior.setHideable(false);

// RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.grpRecycler.setLayoutManager(layoutManager);
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
                    if(dataList.size() > 8) {
                        if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                            MainActivityOld.slideDownContainer();
                        }
                    }

//
                }
            }
        });




        binding.grpRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            if(dataList.size() > 8) {
                                if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                                    MainActivityOld.slideDownContainer();
                                }
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

                                if(dataList.size() > 8){
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



        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter,mContext.RECEIVER_EXPORTED);




        binding.floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, newGroupActivity.class);
                intent.putExtra("grpKey", "grpKey");
                startActivity(intent);
                //


            }
        });

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

        binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.searchLytNew.getVisibility() == View.VISIBLE) {
                    binding.searchLytNew.setVisibility(View.GONE);
                } else if (binding.searchLytNew.getVisibility() == View.GONE) {
                    binding.search.requestFocus();
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_four);
                    binding.searchLytNew.setAnimation(animation);
                    binding.searchLytNew.setVisibility(View.VISIBLE);
                }

            }
        });
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
//                binding.grpRecycler.setOnTouchListener(null);
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
//                binding.grpRecycler.setOnTouchListener(new OnSwipeTouchListener(mContext){
//                    @Override
//                    public void onSwipeTop() {
//                        MainActivityOld.slideUpContainer();
//                        binding.grpRecycler.setOnTouchListener(null);
//
//
//                    }
//                });
//            }
//
//        });


//        binding.grpRecycler.setOnTouchListener(new OnSwipeTouchListener(mContext) {
//            public void onSwipeTop() {
//
//                MainActivityOld.slideUpContainer();
//                binding.grpRecycler.setOnTouchListener(null);
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
//
//            }
//
//        });

        // for listen sceroll view scroll more than 710


        return binding.getRoot();
    }

    private void filteredList(String newText) {
        ArrayList<grp_list_child_model> filteredList = new ArrayList<>();

        for (grp_list_child_model list : dataList) {
            if (list.getGroup_name().toLowerCase().contains(newText.toLowerCase())) {

                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {
            // Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.searchFilteredData(filteredList);
        }
    }

    public void setAdapter(ArrayList<grp_list_child_model> data) {
        this.dataList = data;
        adapter = new grpListAdapter(mContext, dataList);
        binding.grpRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        // Removed setHasFixedSize(true) to fix lint error - incompatible with wrap_content height
        binding.grpRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}