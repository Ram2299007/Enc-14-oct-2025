package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Appzia.enclosure.R;

/**
 * Helper class to manage edge-to-edge display and window insets handling
 * for Android 15+ compatibility
 */
public class EdgeToEdgeHelper {

    /**
     * Enable edge-to-edge display for the activity
     * This should be called in onCreate() before setContentView()
     */
    public static void enableEdgeToEdge(@NonNull Activity activity) {
        // Enable edge-to-edge for backward compatibility
        if (activity instanceof ComponentActivity) {
            EdgeToEdge.enable((ComponentActivity) activity);
        }
        
        // Set decor fits system windows to false for proper edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
    }

    /**
     * Apply proper insets handling to a view
     * This should be called after setContentView()
     */
    public static void applyWindowInsets(@NonNull View rootView) {
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Apply proper insets handling to a view with custom padding
     */
    public static void applyWindowInsets(@NonNull View rootView, int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                systemBars.left + leftPadding,
                systemBars.top + topPadding,
                systemBars.right + rightPadding,
                systemBars.bottom + bottomPadding
            );
            return insets;
        });
    }

    /**
     * Configure status bar appearance based on theme
     */
    public static void configureStatusBar(@NonNull Activity activity, boolean isDarkTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = activity.getWindow().getInsetsController();
            if (insetsController != null) {
                if (isDarkTheme) {
                    // Light status bar (white text/icons) for dark theme
                    insetsController.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
                } else {
                    // Dark status bar (black text/icons) for light theme
                    insetsController.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    );
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For older APIs - use the newer approach with WindowCompat
            WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView())
                .setAppearanceLightStatusBars(!isDarkTheme);
        }
    }

    /**
     * Configure status bar for dark theme
     */
    public static void configureStatusBarForDarkTheme(@NonNull Activity activity) {
        configureStatusBar(activity, true);
    }

    /**
     * Configure status bar for light theme
     */
    public static void configureStatusBarForLightTheme(@NonNull Activity activity) {
        configureStatusBar(activity, false);
    }

    /**
     * Set transparent status bar
     */
    public static void setTransparentStatusBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Set transparent navigation bar
     */
    public static void setTransparentNavigationBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Set both status and navigation bars transparent
     */
    public static void setTransparentBars(@NonNull Activity activity) {
        setTransparentStatusBar(activity);
        setTransparentNavigationBar(activity);
    }

    /**
     * Configure status bar based on current theme (dark/light mode)
     */
    public static void configureStatusBarForCurrentTheme(@NonNull Activity activity) {
        int nightModeFlags = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDarkTheme = (nightModeFlags == Configuration.UI_MODE_NIGHT_YES);
        configureStatusBar(activity, isDarkTheme);
    }

    /**
     * Hide system bars for immersive experience
     */
    public static void hideSystemBars(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = activity.getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsetsCompat.Type.systemBars());
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            // For older APIs - use WindowCompat for better compatibility
            WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView())
                .hide(WindowInsetsCompat.Type.systemBars());
        }
    }

    /**
     * Show system bars
     */
    public static void showSystemBars(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = activity.getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.show(WindowInsetsCompat.Type.systemBars());
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_DEFAULT);
            }
        } else {
            // For older APIs - use WindowCompat for better compatibility
            WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView())
                .show(WindowInsetsCompat.Type.systemBars());
        }
    }

    /**
     * Complete edge-to-edge setup for an activity
     * Call this in onCreate() after setContentView()
     */
    public static void setupEdgeToEdge(@NonNull Activity activity, @NonNull View rootView) {
        // Enable edge-to-edge
        enableEdgeToEdge(activity);
        
        // Apply window insets
        applyWindowInsets(rootView);
        
        // Configure status bar for current theme
        configureStatusBarForCurrentTheme(activity);
        
        // Set transparent bars
        setTransparentBars(activity);
    }

    /**
     * Complete edge-to-edge setup for an activity with custom padding
     */
    public static void setupEdgeToEdge(@NonNull Activity activity, @NonNull View rootView, 
                                     int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        // Enable edge-to-edge
        enableEdgeToEdge(activity);
        
        // Apply window insets with custom padding
        applyWindowInsets(rootView, leftPadding, topPadding, rightPadding, bottomPadding);
        
        // Configure status bar for current theme
        configureStatusBarForCurrentTheme(activity);
        
        // Set transparent bars
        setTransparentBars(activity);
    }
}
