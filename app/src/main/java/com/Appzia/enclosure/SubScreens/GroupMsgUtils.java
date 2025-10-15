package com.Appzia.enclosure.SubScreens;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.Appzia.enclosure.Adapter.grpListAdapter;
import com.Appzia.enclosure.Model.grp_list_child_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Screens.newGroupActivity;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.ThemeHelper;
import com.Appzia.enclosure.Utils.ViewSetupHelper;
import com.Appzia.enclosure.Utils.GroupUtilsHelper;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.FragmentGroupMsgBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.util.ArrayList;

public class GroupMsgUtils implements ConnectivityReceiver.ConnectivityListener {

    private final Context mContext;
    private final View rootView;
    private RecyclerView grpRecycler;
    private AutoCompleteTextView search;
    private View searchIcon;
    private View searchLytNew;
    private View backarrow;
    private View floating;
    private View progressBar;
    public static LinearLayout backlyt;
    private View noData;
    private ArrayList<grp_list_child_model> dataList;
    private grpListAdapter adapter;
    private String themColor;
    private ColorStateList tintList;
    private String fontSizePref;
    private String uid;
    private ConnectivityReceiver connectivityReceiver;
    private boolean hasSlidBelowThreshold = false;
    private boolean hasSlidDown = false;
    private boolean hasSlidUp = false;
    
    // Static reference to current instance for refresh access
    private static GroupMsgUtils currentInstance;

    public GroupMsgUtils(Context context, View rootView) {
        this.mContext = context;
        this.rootView = rootView;
        currentInstance = this; // Set current instance
        initializeViews();
        setup();
    }

    private void initializeViews() {
        grpRecycler = rootView.findViewById(R.id.grpRecycler);
        search = rootView.findViewById(R.id.search);
        searchIcon = rootView.findViewById(R.id.searchIcon);
        searchLytNew = rootView.findViewById(R.id.searchLytNew);
        backarrow = rootView.findViewById(R.id.backarrow);
        floating = rootView.findViewById(R.id.floating);
        progressBar = rootView.findViewById(R.id.progressBar);
        backlyt = rootView.findViewById(R.id.backlyt);
        noData = rootView.findViewById(R.id.noData);

        // Log missing views for debugging
        if (grpRecycler == null) {
            Log.e("GroupMsgUtils", "grpRecycler is null. Check if R.id.grpRecycler exists in the layout.");
        }
        if (search == null) {
            Log.w("GroupMsgUtils", "search is null. Check if R.id.search exists in the layout.");
        }
        if (searchIcon == null) {
            Log.w("GroupMsgUtils", "searchIcon is null. Check if R.id.searchIcon exists in the layout.");
        }
        if (searchLytNew == null) {
            Log.w("GroupMsgUtils", "searchLytNew is null. Check if R.id.searchLytNew exists in the layout.");
        }
        if (backarrow == null) {
            Log.w("GroupMsgUtils", "backarrow is null. Check if R.id.backarrow exists in the layout.");
        }
        if (floating == null) {
            Log.w("GroupMsgUtils", "floating is null. Check if R.id.floating exists in the layout.");
        }
        if (progressBar == null) {
            Log.w("GroupMsgUtils", "progressBar is null. Check if R.id.progressBar exists in the layout.");
        }
        if (noData == null) {
            Log.w("GroupMsgUtils", "noData is null. Check if R.id.noData exists in the layout.");
        }

        // Debug view hierarchy
        if (rootView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) rootView;
            Log.d("GroupMsgUtils", "Root view children count: " + viewGroup.getChildCount());
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                String resourceName = child.getId() != View.NO_ID ? mContext.getResources().getResourceName(child.getId()) : "No ID";
                Log.d("GroupMsgUtils", "Child " + i + ": ID=" + resourceName + ", Class=" + child.getClass().getSimpleName());
            }
        } else {
            Log.w("GroupMsgUtils", "rootView is not a ViewGroup, cannot inspect children.");
        }
    }

    public static void setupGroupMsgCode(View rootView) {
        new GroupMsgUtils(rootView.getContext(), rootView);
    }
    
    public static void refreshGroupListIfExists() {
        if (currentInstance != null) {
            currentInstance.refreshGroupList();
        }
    }

    private void setup() {
        dataList = new ArrayList<>();
        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");

        // Set up connectivity receiver
        connectivityReceiver = ViewSetupHelper.setupConnectivityReceiver(mContext, this);

        // Set back key
        ViewSetupHelper.setupBackKey(mContext);

        // Load font size preference
        fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");

        // Load and apply theme
        themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        tintList = ThemeHelper.applyThemeColor(themColor);
        applyTheme();

        // Load offline data
        loadOfflineData();

        // Fetch online data
        fetchOnlineData();

        // Setup bottom sheet
        setupBottomSheet();

        // Setup listeners
        setupClickListeners();
        setupSearchTextWatcher();
        setupScrollListener();
    }

    private void applyTheme() {
        if (floating != null) {
            try {
                floating.setBackgroundTintList(tintList);
            } catch (Exception ignored) {
                floating.setBackgroundTintList(tintList);
            }
        } else {
            Log.w("GroupMsgUtils", "floating is null, cannot apply theme.");
        }
    }

    private void loadOfflineData() {
        try {
            dataList.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            dataList = dbHelper.getAllDataGrpListChildModel();
            if (dataList.size() > 0) {
                setAdapter(dataList);
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("GroupMsgUtils", "loadOfflineData error: " + e.getMessage());
        }
    }

    private void fetchOnlineData() {
        try {
            Webservice.get_group_list(mContext, uid, GroupMsgUtils.this, grpRecycler, (ProgressBar) progressBar, (CardView) noData);
        } catch (Exception ignored) {
        }
    }

    public void refreshGroupList() {
        try {
            Log.d("GroupMsgUtils", "Refreshing group list...");
            // Reload offline data first
            loadOfflineData();
            // Then fetch fresh online data
            fetchOnlineData();
        } catch (Exception e) {
            Log.e("GroupMsgUtils", "Error refreshing group list: " + e.getMessage());
        }
    }

    private void setupBottomSheet() {
        View bottomSheet = rootView.findViewById(R.id.localBottomSheet);
        GroupUtilsHelper.setupBottomSheetBehavior(bottomSheet);
    }

    private void setupClickListeners() {
        GroupUtilsHelper.setupBackArrowClickListener(backarrow, backlyt);
        GroupUtilsHelper.setupFloatingClickListener(floating, mContext);
        GroupUtilsHelper.setupSearchIconClickListener(searchIcon, searchLytNew, search);
        GroupUtilsHelper.setupSearchTouchListener(search, backlyt);
    }

    private void setupSearchTextWatcher() {
        if (search != null) {
            search.addTextChangedListener(new TextWatcher() {
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
        } else {
            Log.w("GroupMsgUtils", "search is null, cannot set text watcher.");
        }
    }

    private void setupScrollListener() {
        GroupUtilsHelper.setupScrollListener(grpRecycler, backlyt, dataList);
    }

    public void setAdapter(ArrayList<grp_list_child_model> data) {
        this.dataList = data;
        if (grpRecycler != null) {
            adapter = new grpListAdapter(mContext, dataList);
            grpRecycler.setLayoutManager(new LinearLayoutManager(mContext));
            // Removed setHasFixedSize(true) to fix lint error - incompatible with wrap_content height
            grpRecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Log.w("GroupMsgUtils", "grpRecycler is null, cannot set adapter.");
        }
    }

    private void filteredList(String newText) {
        ArrayList<grp_list_child_model> filteredList = new ArrayList<>();
        for (grp_list_child_model list : dataList) {
            if (list.getGroup_name().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(list);
            }
        }
        if (!filteredList.isEmpty()) {
            adapter.searchFilteredData(filteredList);
        }
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (!isConnected) {
            Log.d("Network", "disconnected: groupMsg");
            try {
                dataList.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                dataList = dbHelper.getAllDataGrpListChildModel();
                if (dataList.size() > 0) {
                    setAdapter(dataList);
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("GroupMsgUtils", "onConnectivityChanged error: " + e.getMessage());
            }
        }
    }

    public void cleanup() {
        try {
            mContext.unregisterReceiver(connectivityReceiver);
        } catch (Exception e) {
            Log.w("GroupMsgUtils", "Error unregistering connectivity receiver: " + e.getMessage());
        }
    }
}