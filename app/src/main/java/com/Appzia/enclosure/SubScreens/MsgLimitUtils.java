package com.Appzia.enclosure.SubScreens;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.Appzia.enclosure.Adapter.get_user_active_contact_list_msgLmt_Adapter;
import com.Appzia.enclosure.Model.get_user_active_contact_list_MessageLmt_Model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Utils.Webservice;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.util.ArrayList;

public class MsgLimitUtils implements ConnectivityReceiver.ConnectivityListener {

    private final Context mContext;
    private final View rootView;
    private RecyclerView msgLimitRecyclerview;
    private AutoCompleteTextView search;
    private View searchIcon;
    private View searchLytNew;
    private View backarrow;
    private View allmsglmt;
    private View progressBar;
    private View noData;
    private View cardview;
    private View view;
    private TextView txt1;
    public static LinearLayout backlyt;
    private CardView customToastCard;
    private TextView customToastText;
    private ArrayList<get_user_active_contact_list_MessageLmt_Model> get_user_active_contact_listmsgLmt;
    private get_user_active_contact_list_msgLmt_Adapter adapter;
    private String themColor;
    private ColorStateList tintList;
    private String fontSizePref;
    private String uid;
    private ConnectivityReceiver connectivityReceiver;
    private boolean hasSlidBelowThreshold = false;
    private boolean hasSlidDown = false;
    private boolean hasSlidUp = false;

    public MsgLimitUtils(Context context, View rootView) {
        this.mContext = context;
        this.rootView = rootView;
        initializeViews();
        setup();
    }

    private void initializeViews() {
        msgLimitRecyclerview = rootView.findViewById(R.id.msgLimitRecyclerview);
        search = rootView.findViewById(R.id.search);
        searchIcon = rootView.findViewById(R.id.searchIcon);
        searchLytNew = rootView.findViewById(R.id.searchLytNew);
        backarrow = rootView.findViewById(R.id.backarrow);
        allmsglmt = rootView.findViewById(R.id.allmsglmt);
        progressBar = rootView.findViewById(R.id.progressBar);
        noData = rootView.findViewById(R.id.noData);
        cardview = rootView.findViewById(R.id.cardview);
        view = rootView.findViewById(R.id.view);
        txt1 = rootView.findViewById(R.id.txt1);
        backlyt = rootView.findViewById(R.id.backlyt);
        customToastCard = rootView.findViewById(R.id.includedToast);
        customToastText = customToastCard.findViewById(R.id.toastText);

    }

    public static void setupMsgLimitCode(View rootView) {
        new MsgLimitUtils(rootView.getContext(), rootView);
    }

    private void setup() {
        get_user_active_contact_listmsgLmt = new ArrayList<>();
        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");

        // Set up connectivity receiver
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, Context.RECEIVER_EXPORTED);

        // Set back key
        Constant.setSfFunction(mContext);
        Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
        Constant.setSF.apply();

        // Load font size preference
        fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");

        // Load theme color
        themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        tintList = ColorStateList.valueOf(Color.parseColor(themColor));
        applyTheme();

        // Load offline data
        loadOfflineData();

        // Fetch online data
        fetchOnlineData();

        // Setup search adapter
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, Constant.searchMsgLmt);
        search.setAdapter(searchAdapter);

        // Setup bottom sheet
        setupBottomSheet();

        // Setup listeners
        setupClickListeners();

        // Setup swipe listeners (commented in original, so commented here too)
        // setupSwipeListeners();

        // Setup search text watcher
        setupSearchTextWatcher();

        // Setup dialog for all msg lmt
        setupAllMsgLmtDialog();
    }

    private void applyTheme() {
        try {
            view.setBackgroundTintList(tintList);
        } catch (Exception ignored) {
            view.setBackgroundTintList(tintList);
        }
    }

    private void loadOfflineData() {
        try {
            get_user_active_contact_listmsgLmt.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            get_user_active_contact_listmsgLmt = dbHelper.getAllDataMessageLimit();
            if (get_user_active_contact_listmsgLmt.size() > 0) {
                setAdapter(get_user_active_contact_listmsgLmt);
                progressBar.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchOnlineData() {
        try {
            String msg_limitFORALL = Constant.getSF.getString(Constant.msg_limitFORALL, "0");
            if (!msg_limitFORALL.equals("0")) {
                txt1.setText(msg_limitFORALL);
            }
            Webservice.get_message_limit_for_all_users(mContext, Constant.getSF.getString(Constant.UID_KEY, ""), MsgLimitUtils.this,txt1);
        } catch (Exception ignored) {
        }

        try {
            Webservice.get_user_active_chat_list_for_msgLmt(mContext, uid, MsgLimitUtils.this, msgLimitRecyclerview,(ProgressBar) progressBar,(CardView) noData);
        } catch (Exception ignored) {
        }
    }

    private void setupBottomSheet() {
        View bottomSheet = rootView.findViewById(R.id.localBottomSheet);
        if (bottomSheet != null) {
            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetBehavior.setSkipCollapsed(true);
            bottomSheetBehavior.setHideable(false);

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
                    // Handle slide offset if needed
                }
            });
        }
    }

    private void setupClickListeners() {
        if (backarrow != null) {
            backarrow.setOnClickListener(v -> {
                if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideDownContainer();
                    backlyt.setVisibility(View.GONE);
                }
            });
        } else {
            Log.w("MsgLimitUtils", "backarrow is null, cannot set click listener.");
        }

        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                if (searchLytNew.getVisibility() == View.VISIBLE) {
                    searchLytNew.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    }
                } else if (searchLytNew.getVisibility() == View.GONE) {
                    searchLytNew.setVisibility(View.VISIBLE);
                    search.requestFocus();
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            });
        } else {
            Log.w("MsgLimitUtils", "searchIcon is null, cannot set click listener.");
        }

        if (search != null) {
            search.setOnTouchListener((v, event) -> {
                if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideUpContainer();
                    backlyt.setVisibility(View.VISIBLE);
                }
                search.requestFocus();
                return false;
            });
        } else {
            Log.w("MsgLimitUtils", "search is null, cannot set touch listener.");
        }
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
            Log.w("MsgLimitUtils", "search is null, cannot set text watcher.");
        }
    }

    private void setupAllMsgLmtDialog() {
        Constant.dialogueLayoutMsgLmt(mContext, R.layout.message_lmt_lyt);
        Dialog dialogueLayoutMsgLmt = Constant.dialogLayoutColor;
        EditText et1 = dialogueLayoutMsgLmt.findViewById(R.id.et1);

        if (allmsglmt != null) {
            allmsglmt.setOnClickListener(v -> {
                if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideUpContainer();
                    backlyt.setVisibility(View.VISIBLE);
                }

                dialogueLayoutMsgLmt.show();
                et1.requestFocus();
                et1.setSelection(et1.getText().length());

                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        } else {
            Log.w("MsgLimitUtils", "allmsglmt is null, cannot set click listener.");
        }

        if (et1 != null) {
            et1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (et1.getText().toString().isEmpty()) {
                        txt1.setText("0");
                    } else {
                        txt1.setText(et1.getText().toString());
                    }

                    if (et1.getText().toString().length() == 3) {
                        cardview.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                    try {
                        Webservice.set_message_limit_for_all_users(mContext, uid, txt1.getText().toString(), MsgLimitUtils.this, msgLimitRecyclerview, (CardView) cardview,(ProgressBar) progressBar,(CardView) noData, et1, customToastCard, customToastText);
                    } catch (Exception ignored) {
                    }
                }
            });
        } else {
            Log.w("MsgLimitUtils", "et1 is null, cannot set text watcher.");
        }
    }

    public void setAdapter(ArrayList<get_user_active_contact_list_MessageLmt_Model> get_user_active_contact_listmsgLmt) {
        String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");
        ArrayList<get_user_active_contact_list_MessageLmt_Model> filteredList = new ArrayList<>();
        for (get_user_active_contact_list_MessageLmt_Model model : get_user_active_contact_listmsgLmt) {
            if (!model.getUid().equals(currentUid) && !model.isBlock()) {
                filteredList.add(model);
            }
        }
        this.get_user_active_contact_listmsgLmt = filteredList;

        if (msgLimitRecyclerview != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            msgLimitRecyclerview.setLayoutManager(linearLayoutManager);
            adapter = new get_user_active_contact_list_msgLmt_Adapter(mContext, this.get_user_active_contact_listmsgLmt, customToastCard, customToastText);
            msgLimitRecyclerview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Log.w("MsgLimitUtils", "msgLimitRecyclerview is null, cannot set adapter.");
        }
    }

    private void filteredList(String newText) {
        ArrayList<get_user_active_contact_list_MessageLmt_Model> filteredList = new ArrayList<>();
        for (get_user_active_contact_list_MessageLmt_Model list : get_user_active_contact_listmsgLmt) {
            if (list.getFull_name().toLowerCase().contains(newText.toLowerCase())) {
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
            Log.d("Network", "dissconnected: msgLimit");
            try {
                get_user_active_contact_listmsgLmt.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_user_active_contact_listmsgLmt = dbHelper.getAllDataMessageLimit();
                if (get_user_active_contact_listmsgLmt.size() > 0) {
                    setAdapter(get_user_active_contact_listmsgLmt);
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            } catch (Exception ignored) {
            }
        }
    }

    public void cleanup() {
        mContext.unregisterReceiver(connectivityReceiver);
    }
}