package com.Appzia.enclosure.SubScreens;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.Appzia.enclosure.Adapter.profilestatusAdapterYouFrag;
import com.Appzia.enclosure.Model.profileDBModel;
import com.Appzia.enclosure.Model.profilestatusModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Screens.editmyProfile;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Utils.ThemeHelper;
import com.Appzia.enclosure.Utils.ViewSetupHelper;
import com.Appzia.enclosure.Utils.ViewInitializationHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;

public class YouUtils implements ConnectivityReceiver.ConnectivityListener {

    private final Context mContext;
    private final View rootView;
    private RecyclerView multiimageRecyclerview;
    private LinearLayout youImg;
    private LinearLayout youprofileshimmerLyt;
    public static LinearLayout backlytYouFrag;
    private ImageView profile;
    private TextView name;
    private TextView caption;
    private TextView phone;
    private TextView edittext;
    private View backarrow;
    private View scrollView; // Expected to be a RelativeLayout
    private View MainSenderBox;
    private View richBox;
    private View editProf;
    private String themColor;
    private ColorStateList tintList;
    private String uid;
    private ArrayList<profilestatusModel> profilestatusList;
    private profilestatusAdapterYouFrag adapter;
    private String fontSizePref;
    private ConnectivityReceiver connectivityReceiver;

    public YouUtils(Context context, View rootView) {
        this.mContext = context;
        this.rootView = rootView;
        initializeViews();
        setup();
    }

    private void initializeViews() {
        multiimageRecyclerview = rootView.findViewById(R.id.multiimageRecyclerview);
        youImg = rootView.findViewById(R.id.youImg);
        youprofileshimmerLyt = rootView.findViewById(R.id.youprofileshimmerLyt);
        backlytYouFrag = rootView.findViewById(R.id.backlyt);
        profile = rootView.findViewById(R.id.profile);
        name = rootView.findViewById(R.id.name);
        caption = rootView.findViewById(R.id.caption);
        phone = rootView.findViewById(R.id.phone);
        edittext = rootView.findViewById(R.id.edittext);
        backarrow = rootView.findViewById(R.id.backarrow);
        scrollView = rootView.findViewById(R.id.scrollView);
        MainSenderBox = rootView.findViewById(R.id.MainSenderBox);
        richBox = rootView.findViewById(R.id.richBox);
        editProf = rootView.findViewById(R.id.editProf);

        // Log missing views for debugging
        if (scrollView == null) {
            Log.e("YouUtils", "scrollView (RelativeLayout) is null. Check if R.id.scrollView exists in the layout.");
        }
        if (multiimageRecyclerview == null) {
            Log.e("YouUtils", "multiimageRecyclerview is null. Check if R.id.multiimageRecyclerview exists in the layout.");
        }
        if (profile == null) {
            Log.w("YouUtils", "profile is null. Check if R.id.profile exists in the layout.");
        }
        if (backarrow == null) {
            Log.w("YouUtils", "backarrow is null. Check if R.id.backarrow exists in the layout.");
        }
        if (editProf == null) {
            Log.w("YouUtils", "editProf is null. Check if R.id.editProf exists in the layout.");
        }
        if (edittext == null) {
            Log.w("YouUtils", "edittext is null. Check if R.id.edittext exists in the layout.");
        }
        if (MainSenderBox == null) {
            Log.w("YouUtils", "MainSenderBox is null. Check if R.id.MainSenderBox exists in the layout.");
        }
        if (richBox == null) {
            Log.w("YouUtils", "richBox is null. Check if R.id.richBox exists in the layout.");
        }
        if (youImg == null) {
            Log.w("YouUtils", "youImg is null. Check if R.id.youImg exists in the layout.");
        }
        if (youprofileshimmerLyt == null) {
            Log.w("YouUtils", "youprofileshimmerLyt is null. Check if R.id.youprofileshimmerLyt exists in the layout.");
        }

        // Debug view hierarchy
        if (rootView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) rootView;
            Log.d("YouUtils", "Root view children count: " + viewGroup.getChildCount());
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                String resourceName = child.getId() != View.NO_ID ? mContext.getResources().getResourceName(child.getId()) : "No ID";
                Log.d("YouUtils", "Child " + i + ": ID=" + resourceName + ", Class=" + child.getClass().getSimpleName());
            }
        } else {
            Log.w("YouUtils", "rootView is not a ViewGroup, cannot inspect children.");
        }
    }

    public static void setupYouCode(View rootView) {
        new YouUtils(rootView.getContext(), rootView);
    }

    private void setup() {
        profilestatusList = new ArrayList<>();
        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");

        // Set up connectivity receiver
        connectivityReceiver = ViewSetupHelper.setupConnectivityReceiver(mContext, this);

        // Set back key
        ViewSetupHelper.setupBackKey(mContext);

        // Load font size preference and apply
        fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");
        applyFontSize();

        // Load and apply theme
        themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        tintList = ThemeHelper.applyThemeColor(themColor);
        applyTheme();

        // Apply dark mode settings
        applyDarkMode();

        // Load offline profile data
        loadOfflineProfileData();

        // Load offline status images
        loadOfflineStatusImages();

        // Fetch online data
        fetchOnlineData();

        // Set up click listeners
        setupClickListeners();

        // Set up swipe listeners
        setupSwipeListeners();
    }

    private void applyFontSize() {
        ThemeHelper.applyFontSize(edittext, fontSizePref);
    }

    private void applyTheme() {
        // Theme color is already applied in setup() method
        // This method is kept for compatibility but logic moved to ThemeHelper
    }

    private void applyDarkMode() {
        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            ThemeHelper.applyDarkModeToViews(MainSenderBox, richBox, themColor);
        } else {
            ThemeHelper.applyLightModeToViews(MainSenderBox, richBox);
        }
    }

    private void loadOfflineProfileData() {
        try {
            Log.d("Network", "disconnected: youFragment");
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            profileDBModel model = dbHelper.getAllDataProfile(uid);
            if (model != null) {
                ViewSetupHelper.setTextSafely(name, model.getFull_name());
                Log.d("model.getCaption()", model.getCaption());
                ViewSetupHelper.setTextSafely(caption, model.getCaption());
                ViewSetupHelper.setTextSafely(phone, model.getMobile_no());
                ViewSetupHelper.loadProfileImage(profile, model.getPhoto());
                ViewSetupHelper.setVisibilitySafely(youImg, View.VISIBLE);
            }
        } catch (Exception ignored) {
        }
    }

    private void loadOfflineStatusImages() {
        try {
            Log.d("Network", "disconnected: youFragment");
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            profilestatusList = dbHelper.getAllDataget_get_user_profile_imagesTablel(uid);
            Collections.reverse(profilestatusList);
            if (profilestatusList.size() > 0) {
                setAdapter(profilestatusList);
                if (rootView.findViewById(R.id.progressBar) != null) {
                    rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                }
                if (youImg != null) youImg.setVisibility(View.VISIBLE);
            } else {
                if (rootView.findViewById(R.id.progressBar) != null) {
                    rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void fetchOnlineData() {
        try {
            Webservice.get_profile_YouFragment2(mContext, uid, rootView.findViewById(R.id.progressBar),name, caption, phone, profile,youImg,multiimageRecyclerview);
            Webservice.get_user_profile_images_youFragment(mContext, uid, null, youImg);
        } catch (Exception ignored) {
            Toast.makeText(mContext, ignored.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        ViewSetupHelper.setupProfileClickListener(profile, mContext);
        ViewSetupHelper.setupBackArrowClickListener(backarrow, backlytYouFrag);
        ViewSetupHelper.setupEditProfileClickListener(editProf, mContext);
    }

    private void setupSwipeListeners() {
        ViewSetupHelper.setupRecyclerViewSwipeListener(multiimageRecyclerview, backlytYouFrag);
        ViewSetupHelper.setupScrollViewSwipeListener(scrollView, backlytYouFrag);
    }

    public void setAdapter(ArrayList<profilestatusModel> profilestatusLists) {
        this.profilestatusList = profilestatusLists;
        if (multiimageRecyclerview != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            multiimageRecyclerview.setLayoutManager(linearLayoutManager);
            adapter = new profilestatusAdapterYouFrag(mContext, profilestatusList);
            multiimageRecyclerview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Log.w("YouUtils", "multiimageRecyclerview is null, cannot set adapter.");
        }
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (!isConnected) {
            loadOfflineProfileData();
            loadOfflineStatusImages();
        }
    }

    public void cleanup() {
        try {
            mContext.unregisterReceiver(connectivityReceiver);
        } catch (Exception e) {
            Log.w("YouUtils", "Error unregistering connectivity receiver: " + e.getMessage());
        }
    }
}