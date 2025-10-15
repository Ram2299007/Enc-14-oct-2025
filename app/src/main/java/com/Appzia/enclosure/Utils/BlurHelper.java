package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.graphics.RenderEffect;
import android.graphics.Shader;

import com.Appzia.enclosure.R;

public class BlurHelper {
    public static Dialog dialogLayoutColor;

    public static void showDialogWithBlurBackground(Context mContext, int layoutResId) {
        // Create the dialog
        dialogLayoutColor = new Dialog(mContext);
        dialogLayoutColor.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Create blurred/transparent background view
        FrameLayout backgroundView = new FrameLayout(mContext);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        int nightModeFlags = mContext.getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK;

        int overlayColor;
        if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            overlayColor = Color.parseColor("#000000"); // Dark semi-transparent (50% opacity)
        } else {
            overlayColor = Color.parseColor("#ffffff"); // Light semi-transparent (50% opacity)
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            RenderEffect blurEffect = RenderEffect.createBlurEffect(16f, 16f, Shader.TileMode.CLAMP);
            backgroundView.setRenderEffect(blurEffect);
        }


        backgroundView.setBackgroundColor(overlayColor);

        // Root layout that holds blur background + dialog content
        FrameLayout rootLayout = new FrameLayout(mContext);
        rootLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        rootLayout.addView(backgroundView);

        // Inflate custom layout and add above blur
        View contentView = ((Activity) mContext).getLayoutInflater().inflate(layoutResId, null);
        rootLayout.addView(contentView);

        // Set as dialog content
        dialogLayoutColor.setContentView(rootLayout);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);

        // Fullscreen and transparent background
        Window window = dialogLayoutColor.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);

            View decorView = window.getDecorView();
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(flags);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false);
                WindowManager.LayoutParams params = window.getAttributes();
                params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                window.setAttributes(params);
            }

            // Dismiss on outside tap (outside relativelayout)
            final GestureDetector gestureDetector = new GestureDetector(mContext,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            View content = dialogLayoutColor.findViewById(R.id.relativelayout);
                            if (content != null) {
                                int[] loc = new int[2];
                                content.getLocationOnScreen(loc);
                                float touchX = e.getRawX();
                                float touchY = e.getRawY();
                                int left = loc[0], top = loc[1];
                                int right = left + content.getWidth();
                                int bottom = top + content.getHeight();

                                if (touchX < left || touchX > right || touchY < top || touchY > bottom) {
                                    dialogLayoutColor.dismiss();
                                    return true;
                                }
                            }
                            return false;
                        }
                    });

            decorView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        }
    }

    /**
     * Positions the dialog within screen boundaries
     * @param context Context for getting screen dimensions
     * @param touchX X coordinate of touch point
     * @param touchY Y coordinate of touch point
     * @param dialogLayout The RelativeLayout to position
     */
    public static void positionDialogWithinBounds(Context context, float touchX, float touchY, RelativeLayout dialogLayout) {
        if (dialogLayout == null) return;

        // Wait for layout to be measured
        dialogLayout.post(() -> {
            int dialogWidth = dialogLayout.getWidth();
            int dialogHeight = dialogLayout.getHeight();
            
            // Get screen dimensions
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            
            // Calculate initial position (slightly above touch point)
            int x = (int) touchX;
            int y = (int) touchY - 150; // Offset upward
            
            // Adjust X position to stay within screen bounds
            if (x + dialogWidth > screenWidth) {
                x = screenWidth - dialogWidth - 20; // 20px margin from edge
            }
            if (x < 20) {
                x = 20; // 20px margin from left edge
            }
            
            // Adjust Y position to stay within screen bounds
            if (y + dialogHeight > screenHeight) {
                y = screenHeight - dialogHeight - 20; // 20px margin from bottom
            }
            if (y < 20) {
                y = 20; // 20px margin from top
            }
            
            // Apply the calculated position - handle both FrameLayout and RelativeLayout params
            ViewGroup.LayoutParams currentParams = dialogLayout.getLayoutParams();
            if (currentParams instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) currentParams;
                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;
                dialogLayout.setLayoutParams(layoutParams);
            } else if (currentParams instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) currentParams;
                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;
                dialogLayout.setLayoutParams(layoutParams);
            } else {
                // Fallback: create new FrameLayout.LayoutParams
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    currentParams.width, currentParams.height);
                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;
                dialogLayout.setLayoutParams(layoutParams);
            }
        });
    }
}
