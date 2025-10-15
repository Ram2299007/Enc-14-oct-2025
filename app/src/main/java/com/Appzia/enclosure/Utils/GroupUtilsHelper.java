package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.newGroupActivity;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import android.app.Activity;

import java.util.ArrayList;

/**
 * Helper class for GroupMsgUtils functionality
 * Helps break down large methods to comply with Google Play's 16 KB page size requirement
 */
public class GroupUtilsHelper {
    private static final String TAG = "GroupUtilsHelper";

    /**
     * Sets up bottom sheet behavior
     */
    public static void setupBottomSheetBehavior(View bottomSheet) {
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
        } else {
            Log.w(TAG, "localBottomSheet is null, cannot setup bottom sheet.");
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
     * Sets up floating action button click listener
     */
    public static void setupFloatingClickListener(View floating, Context context) {
        if (floating != null) {
            floating.setOnClickListener(v -> {
                Intent intent = new Intent(context, newGroupActivity.class);
                intent.putExtra("grpKey", "grpKey");
                if (context instanceof Activity) {
                    SwipeNavigationHelper.startActivityWithSwipe((Activity) context, intent);
                } else {
                    context.startActivity(intent);
                }
            });
        } else {
            Log.w(TAG, "floating is null, cannot set click listener.");
        }
    }

    /**
     * Sets up search icon click listener
     */
    public static void setupSearchIconClickListener(View searchIcon, View searchLytNew, EditText search) {
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                if (searchLytNew.getVisibility() == View.VISIBLE) {
                    searchLytNew.setVisibility(View.GONE);
                } else if (searchLytNew.getVisibility() == View.GONE) {
                    Animation animation = AnimationUtils.loadAnimation(searchIcon.getContext(), R.anim.anim_left_four);
                    searchLytNew.setAnimation(animation);
                    searchLytNew.setVisibility(View.VISIBLE);
                    if (search != null) {
                        search.requestFocus();
                    }
                }
            });
        } else {
            Log.w(TAG, "searchIcon is null, cannot set click listener.");
        }
    }

    /**
     * Sets up search touch listener
     */
    public static void setupSearchTouchListener(EditText search, View backlyt) {
        if (search != null) {
            search.setOnTouchListener((v, event) -> {
                if (MainActivityOld.linearMain != null && MainActivityOld.linearMain.getVisibility() == View.VISIBLE) {
                    MainActivityOld.slideUpContainer();
                    if (backlyt != null) {
                        backlyt.setVisibility(View.VISIBLE);
                    }
                }
                if (search != null) {
                    search.requestFocus();
                }
                return false;
            });
        } else {
            Log.w(TAG, "search is null, cannot set touch listener.");
        }
    }

    /**
     * Sets up scroll listener for recycler view
     */
    public static void setupScrollListener(RecyclerView grpRecycler, View backlyt, ArrayList<?> dataList) {
        if (grpRecycler != null) {
            grpRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private boolean hasSlidDown = false;
                private boolean hasSlidUp = false;

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                        int totalItemCount = layoutManager.getItemCount();

                        View firstView = layoutManager.findViewByPosition(firstVisiblePosition);
                        if (firstVisiblePosition == 0 && firstView != null && firstView.getTop() == 0) {
                            if (MainActivityOld.linearMain2 != null && MainActivityOld.linearMain2.getVisibility() == View.VISIBLE && !hasSlidDown) {
                                if (dataList.size() > 8) {
                                    MainActivityOld.slideDownContainer();
                                    if (backlyt != null) {
                                        backlyt.setVisibility(View.GONE);
                                    }
                                }
                                hasSlidDown = true;
                                hasSlidUp = false;
                            }
                        } else {
                            hasSlidDown = false;
                        }

                        if (lastVisiblePosition == totalItemCount - 1 && totalItemCount > 0) {
                            View lastView = layoutManager.findViewByPosition(lastVisiblePosition);
                            if (lastView != null && lastView.getBottom() <= recyclerView.getHeight()) {
                                if (MainActivityOld.linearMain != null && MainActivityOld.linearMain.getVisibility() == View.VISIBLE && !hasSlidUp) {
                                    if (dataList.size() > 8) {
                                        MainActivityOld.slideUpContainer();
                                        if (backlyt != null) {
                                            backlyt.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    hasSlidUp = true;
                                    hasSlidDown = false;
                                }
                            }
                        } else {
                            hasSlidUp = false;
                        }
                    }
                }
            });
        } else {
            Log.w(TAG, "grpRecycler is null, cannot set scroll listener.");
        }
    }
}
