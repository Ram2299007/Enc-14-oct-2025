package com.Appzia.enclosure.Screens;

import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.databinding.ActivityCameraBinding;

public class cameraActivity extends AppCompatActivity {


    ActivityCameraBinding binding;
    private float initialTouchX, initialTouchY; // Variables to track initial touch positions
    private int initialPopupX, initialPopupY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        Button showWindowButton = findViewById(R.id.window_title);
        showWindowButton.setOnClickListener(v -> showFloatingWindow());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showFloatingWindow() {
        // Inflate the floating window layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.floating_windows, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                true);

        // Prevent the PopupWindow from being dismissed when touching outside
        popupWindow.setOutsideTouchable(false);  // Prevents the PopupWindow from dismissing on outside touch
        popupWindow.setFocusable(false);         // Disable focusable so it won't dismiss on outside click

        // Optionally set a transparent background if required
        popupWindow.setBackgroundDrawable(null); // Set background to null or transparent if you don't want one

        // Get the width of the screen to calculate the offset for right alignment
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;

        // Measure the width of the popupView to calculate the x-offset
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = popupView.getMeasuredWidth();

        // Set x-offset to align the popup to the right
        int xOffset = screenWidth - popupWidth;

        // y-offset can be 0 since you want it at the top
        int yOffset = 0;

        // Show the PopupWindow at the top-right corner of the screen
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY, xOffset, yOffset);

        // Save initial popup position
        initialPopupX = xOffset;
        initialPopupY = yOffset;

        // Set up drag functionality
        popupView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Capture initial touch positions
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    // Calculate the drag displacement
                    float dx = event.getRawX() - initialTouchX;
                    float dy = event.getRawY() - initialTouchY;

                    // Update the PopupWindow position with the drag displacement
                    initialPopupX += (int) dx;
                    initialPopupY += (int) dy;

                    // Update the PopupWindow position
                    popupWindow.update(initialPopupX, initialPopupY, -1, -1);

                    // Update initial touch positions for smooth drag
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();

                    return true;
            }
            return false;
        });
    }


}
