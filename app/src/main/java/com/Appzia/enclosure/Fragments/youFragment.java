package com.Appzia.enclosure.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.profilestatusAdapterYouFrag;
import com.Appzia.enclosure.Model.profileDBModel;
import com.Appzia.enclosure.Model.profilestatusModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Screens.editmyProfile;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.FragmentYouBinding;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class youFragment extends Fragment implements ConnectivityReceiver.ConnectivityListener {

    FragmentYouBinding binding;
    String themColor;

    ColorStateList tintList;
    public static RecyclerView multiimageRecyclerview;
    public static LinearLayout youImg, youprofileshimmerLyt;
    public static LinearLayout backlytYouFrag;
    //  backlyt = binding.getRoot().findViewById(R.id.backlyt);
    public static profilestatusAdapterYouFrag adapter;
    public static String fontSizePref;
    public static ImageView profile;
    public static TextView name;
    public static TextView caption;
    Context mContext;
    String uid;
    public static TextView phone;
    private ConnectivityReceiver connectivityReceiver;
    ArrayList<profilestatusModel> profilestatusList;


    @Override
    public void onConnectivityChanged(boolean isConnected) {
        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");
        if (isConnected) {

        } else {

            try {

                //TODO : GET PROFILE DETAILS
                Log.d("Network", "disconnected: " + "youFragment");
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                profileDBModel model = dbHelper.getAllDataProfile(uid);

                binding.name.setText(model.getFull_name());
                Log.d("model.getCaption()", model.getCaption());
                binding.caption.setText(model.getCaption());

                binding.phone.setText(model.getMobile_no());

                try {
                    binding.profile.setTag(model.getPhoto());
                    Picasso.get().load(model.getPhoto()).into(binding.profile);
                } catch (Exception ignored) {
                }


                binding.youImg.setVisibility(View.VISIBLE);

            } catch (Exception ignored) {
            }

            try {

                //TODO : GET PROFILE IMAGES STATUS
                Log.d("Network", "disconnected: " + "youFragment");
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                profilestatusList = dbHelper.getAllDataget_get_user_profile_imagesTablel(uid);
                Collections.reverse(profilestatusList);

                if (profilestatusList.size() > 0) {
                    setAdapter(profilestatusList);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }


                binding.youImg.setVisibility(View.VISIBLE);

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

        Log.d("TAG", "onStart: ");


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
            if (fontSizePref.equals(Constant.small)) {

                binding.edittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);


            } else if (fontSizePref.equals(Constant.medium)) {

                binding.edittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.edittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);


            }
        } catch (Exception ignored) {

        }


        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
                    //    binding.logoBtn.setImageResource(R.drawable.pinklogosvg);
                } else if (themColor.equals("#00A3E9")) {

                    //default color
                    //  binding.logoBtn.setImageResource(R.drawable.logo_short);
                } else if (themColor.equals("#7adf2a")) {
                    // binding.logoBtn.setImageResource(R.drawable.popatilogoscg);

                } else if (themColor.equals("#ec0001")) {
                    ///  binding.logoBtn.setImageResource(R.drawable.redlogosvg);

                } else if (themColor.equals("#16f3ff")) {
                    //  binding.logoBtn.setImageResource(R.drawable.bluelogosvg);

                } else if (themColor.equals("#FF8A00")) {
                    // binding.logoBtn.setImageResource(R.drawable.orangelogosvg);

                } else if (themColor.equals("#7F7F7F")) {
                    //  binding.logoBtn.setImageResource(R.drawable.graylogosvg);

                } else if (themColor.equals("#D9B845")) {
                    //  binding.logoBtn.setImageResource(R.drawable.yellowlogosvg);

                } else if (themColor.equals("#346667")) {
                    //  binding.logoBtn.setImageResource(R.drawable.greenlogosvg);

                } else if (themColor.equals("#9846D9")) {
                    //  binding.logoBtn.setImageResource(R.drawable.voiletlogosvg);

                } else if (themColor.equals("#A81010")) {
                    // binding.logoBtn.setImageResource(R.drawable.red2logosvg);

                } else if (themColor.equals("#A81010")) {
                    //     binding.logoBtn.setImageResource(R.drawable.red2logosvg);

                } else {
                    //  binding.logoBtn.setImageResource(R.drawable.logo_short);
                }
            } catch (Exception ignored) {
                //   binding.logoBtn.setImageResource(R.drawable.logo_short);
            }


        } catch (Exception ignored) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        backlytYouFrag = binding.getRoot().findViewById(R.id.backlyt);
      

        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            Constant.getSfFuncion(mContext);
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            ColorStateList tintList;

            try {
                if (themColor.equals("#ff0080")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);





                } else if (themColor.equals("#00A3E9")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {

                    tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);



                } else if (themColor.equals("#ec0001")) {

                    tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);



                } else if (themColor.equals("#16f3ff")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#FF8A00")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#346667")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else if (themColor.equals("#9846D9")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);


                } else if (themColor.equals("#A81010")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);

                } else {
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                    binding.MainSenderBox.setBackgroundTintList(tintList);
                    binding.richBox.setBackgroundTintList(tintList);


                }
            } catch (Exception ignored) {
                tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                binding.MainSenderBox.setBackgroundTintList(tintList);
                binding.richBox.setBackgroundTintList(tintList);


            }


        } else {
            tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
            binding.MainSenderBox.setBackgroundTintList(tintList); // Replace #011224 with your hex color value
        }
        


        // todo ------------------------online ----------------------------------


        Log.d("Network", "connected: " + "youFragment");

        try {

            //TODO : - offline call first for only profiles image

            Log.d("Network", "disconnected: " + "youFragment");
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            profileDBModel model = dbHelper.getAllDataProfile(uid);

            if (model != null) {
                binding.name.setText(model.getFull_name());
                Log.d("model.getCaption()", model.getCaption());

                binding.caption.setText(model.getCaption());

                binding.phone.setText(model.getMobile_no());

                try {
                    binding.profile.setTag(model.getPhoto());
                    Picasso.get().load(model.getPhoto()).into(binding.profile);
                } catch (Exception ignored) {
                }


                binding.youImg.setVisibility(View.VISIBLE);
            } else {

            }


        } catch (Exception ignored) {
        }


        try {
            //TODO : - offline call first for only status images
            Log.d("Network", "disconnected: " + "youFragment");
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            profilestatusList = dbHelper.getAllDataget_get_user_profile_imagesTablel(uid);
            Collections.reverse(profilestatusList);


            if (profilestatusList.size() > 0) {
              //  setAdapter(profilestatusList);
                binding.progressBar.setVisibility(View.GONE);
                binding.youImg.setVisibility(View.VISIBLE);

            } else {
                binding.progressBar.setVisibility(View.GONE);
            }


        } catch (Exception ignored) {
        }


        try {
            Webservice.get_profile_YouFragment(mContext, uid, binding.progressBar);


        } catch (Exception ignored) {
        }

        try {

           // Webservice.get_user_profile_images_youFragment(mContext, uid, youFragment.this, binding.youImg);


        } catch (Exception ignored) {
            Toast.makeText(mContext, ignored.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
            Constant.setSF.apply();
        } catch (Exception ignored) {
        }

        // todo ------------------------online ----------------------------------
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentYouBinding.inflate(inflater, container, false);

        mContext = binding.getRoot().getContext();
        Log.d("TAG", "onCreateView: ");
        backlytYouFrag = binding.getRoot().findViewById(R.id.backlyt);
        profile = binding.getRoot().findViewById(R.id.profile);
        name = binding.getRoot().findViewById(R.id.name);
        caption = binding.getRoot().findViewById(R.id.caption);
        phone = binding.getRoot().findViewById(R.id.phone);
        multiimageRecyclerview = binding.getRoot().findViewById(R.id.multiimageRecyclerview);
        youImg = binding.getRoot().findViewById(R.id.youImg);
        youprofileshimmerLyt = binding.getRoot().findViewById(R.id.youprofileshimmerLyt);

        profilestatusList = new ArrayList<>();

        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");


        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, mContext.RECEIVER_EXPORTED);


        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Intent intent = new Intent(mContext, show_image_Screen.class);
                    intent.putExtra("imageKey", binding.profile.getTag().toString());
                    intent.putExtra("youKey", "youKey");
                    SwipeNavigationHelper.startActivityWithSwipe(getActivity(), intent);
                }
            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideDownContainer();
                    backlytYouFrag.setVisibility(View.GONE);
                }
            }
        });

        binding.scrollView.setOnTouchListener(new OnSwipeTouchListener(mContext) {
            public void onSwipeTop() {


                if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideUpContainer();
                    backlytYouFrag.setVisibility(View.VISIBLE);
                }
            }

            public void onSwipeRight() {

            }

            public void onSwipeLeft() {

            }

            public void onSwipeBottom() {

                if (MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideDownContainer();
                    backlytYouFrag.setVisibility(View.GONE);
                }

                binding.multiimageRecyclerview.setOnTouchListener(new OnSwipeTouchListener(mContext) {
                    @Override
                    public void onSwipeTop() {
                        if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                            MainActivityOld.slideUpContainer();
                            backlytYouFrag.setVisibility(View.VISIBLE);
                        }
                        binding.multiimageRecyclerview.setOnTouchListener(null);
                    }
                });
            }

        });

        binding.multiimageRecyclerview.setOnTouchListener(new OnSwipeTouchListener(mContext) {
            public void onSwipeTop() {


                if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideUpContainer();
                    backlytYouFrag.setVisibility(View.VISIBLE);
                }
                binding.multiimageRecyclerview.setOnTouchListener(null);
            }

            public void onSwipeRight() {

            }

            public void onSwipeLeft() {

            }

            public void onSwipeBottom() {

            }

        });


        binding.editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    SwipeNavigationHelper.startActivityWithSwipe(getActivity(), new Intent(getActivity(), editmyProfile.class));
                }
            }
        });
        // for listen sceroll view scroll more than 710
        binding.scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                Log.d("@@scrollview", String.valueOf(scrollX) + ":" + String.valueOf(scrollY) + ":" + String.valueOf(oldScrollX) + ":" + String.valueOf(oldScrollY));

                //value will change after data is more than 100 to 500
                if (scrollY > oldScrollY) {
                    if (MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                        MainActivityOld.slideUpContainer();
                        backlytYouFrag.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        return binding.getRoot();


    }

    public void setAdapter(ArrayList<profilestatusModel> profilestatusLists) {
        this.profilestatusList = profilestatusLists;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.multiimageRecyclerview.setLayoutManager(linearLayoutManager);
        adapter = new profilestatusAdapterYouFrag(mContext, profilestatusList);
        binding.multiimageRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }


}