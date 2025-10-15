package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.editmyProfile;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.OnSwipeTouchListener;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.ImageLoaderUtil;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import android.app.Activity;

/**
 * Helper class for view setup functionality
 * Helps break down large methods to comply with Google Play's 16 KB page size requirement
 */
public class ViewSetupHelper {
    private static final String TAG = "ViewSetupHelper";

    /**
     * Sets up connectivity receiver
     */
    public static ConnectivityReceiver setupConnectivityReceiver(Context context, ConnectivityReceiver.ConnectivityListener listener) {
        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(listener);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(connectivityReceiver, filter, Context.RECEIVER_EXPORTED);
        return connectivityReceiver;
    }

    /**
     * Sets up back key functionality
     */
    public static void setupBackKey(Context context) {
        Constant.setSfFunction(context);
        Constant.setSF.putString(Constant.BACKKEY, Constant.otherBackValue);
        Constant.setSF.apply();
    }

    /**
     * Sets up profile click listener
     */
    public static void setupProfileClickListener(ImageView profile, Context context) {
        if (profile != null) {
            profile.setOnClickListener(v -> {
                Intent intent = new Intent(context, show_image_Screen.class);
                intent.putExtra("imageKey", profile.getTag().toString());
                intent.putExtra("youKey", "youKey");
                if (context instanceof Activity) {
                    SwipeNavigationHelper.startActivityWithSwipe((Activity) context, intent);
                } else {
                    context.startActivity(intent);
                }
            });
        } else {
            Log.w(TAG, "profile is null, cannot set click listener.");
        }
    }

    /**
     * Sets up back arrow click listener
     */
    public static void setupBackArrowClickListener(View backarrow, View backlyt) {
        if (backarrow != null) {
            backarrow.setOnClickListener(v -> {
                if (MainActivityOld.linearMain2 != null && MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideDownContainer();
                    if (backlyt != null) {
                        backlyt.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            Log.w(TAG, "backarrow is null, cannot set click listener.");
        }
    }

    /**
     * Sets up edit profile click listener
     */
    public static void setupEditProfileClickListener(View editProf, Context context) {
        if (editProf != null) {
            editProf.setOnClickListener(v -> {
                if (context instanceof Activity) {
                    SwipeNavigationHelper.startActivityWithSwipe((Activity) context, new Intent(context, editmyProfile.class));
                } else {
                    context.startActivity(new Intent(context, editmyProfile.class));
                }
            });
        } else {
            Log.w(TAG, "editProf is null, cannot set click listener.");
        }
    }

    /**
     * Sets up swipe listeners for recycler view
     */
    public static void setupRecyclerViewSwipeListener(View multiimageRecyclerview, View backlyt) {
        if (multiimageRecyclerview != null) {
            multiimageRecyclerview.setOnTouchListener(new OnSwipeTouchListener(multiimageRecyclerview.getContext()) {
                @Override
                public void onSwipeTop() {
                    if (MainActivityOld.linearMain != null && MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                        MainActivityOld.slideUpContainer();
                        if (backlyt != null) {
                            backlyt.setVisibility(View.VISIBLE);
                        }
                    }
                    multiimageRecyclerview.setOnTouchListener(null);
                }
            });
        } else {
            Log.w(TAG, "multiimageRecyclerview is null, cannot set swipe listener.");
        }
    }

    /**
     * Sets up swipe listeners for scroll view
     */
    public static void setupScrollViewSwipeListener(View scrollView, View backlyt) {
        if (scrollView != null) {
            scrollView.setOnTouchListener(new OnSwipeTouchListener(scrollView.getContext()) {
                @Override
                public void onSwipeTop() {
                    if (MainActivityOld.linearMain != null && MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                        MainActivityOld.slideUpContainer();
                        if (backlyt != null) {
                            backlyt.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onSwipeBottom() {
                    if (MainActivityOld.linearMain2 != null && MainActivityOld.linearMain2.getVisibility() == View.VISIBLE) {
                        MainActivityOld.slideDownContainer();
                        if (backlyt != null) {
                            backlyt.setVisibility(View.GONE);
                        }
                    }
                }
            });
        } else {
            Log.w(TAG, "scrollView is null, cannot set swipe listener.");
        }
    }

    /**
     * Loads profile image safely
     */
    public static void loadProfileImage(ImageView profile, String photoUrl) {
        if (profile != null) {
            try {
                profile.setTag(photoUrl);
                ImageLoaderUtil.safeLoadImage(photoUrl, profile, R.drawable.inviteimg);
            } catch (Exception e) {
                Log.w(TAG, "Error loading profile image: " + e.getMessage());
            }
        } else {
            Log.w(TAG, "profile is null, cannot load image.");
        }
    }

    /**
     * Sets text safely
     */
    public static void setTextSafely(TextView textView, String text) {
        if (textView != null && text != null) {
            textView.setText(text);
        } else {
            Log.w(TAG, "TextView or text is null, cannot set text.");
        }
    }

    /**
     * Sets visibility safely
     */
    public static void setVisibilitySafely(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        } else {
            Log.w(TAG, "View is null, cannot set visibility.");
        }
    }
}
