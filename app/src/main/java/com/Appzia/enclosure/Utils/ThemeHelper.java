package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.Appzia.enclosure.R;

/**
 * Helper class for theme-related functionality
 * Helps break down large methods to comply with Google Play's 16 KB page size requirement
 */
public class ThemeHelper {
    private static final String TAG = "ThemeHelper";

    /**
     * Applies font size to a text view
     */
    public static void applyFontSize(TextView textView, String fontSizePref) {
        if (textView != null) {
            try {
                if (fontSizePref.equals(Constant.small)) {
                    textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 13);
                } else if (fontSizePref.equals(Constant.medium)) {
                    textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16);
                } else if (fontSizePref.equals(Constant.large)) {
                    textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 19);
                }
            } catch (Exception ignored) {
                Log.w(TAG, "Error applying font size: " + ignored.getMessage());
            }
        } else {
            Log.w(TAG, "TextView is null, cannot apply font size.");
        }
    }

    /**
     * Applies theme color to views
     */
    public static ColorStateList applyThemeColor(String themeColor) {
        try {
            // Note: Logo button code is preserved but not applied as it's not in the XML
            switch (themeColor) {
                case "#ff0080":
                    // binding.logoBtn.setImageResource(R.drawable.pinklogosvg);
                    break;
                case "#00A3E9":
                    // binding.logoBtn.setImageResource(R.drawable.logo_short);
                    break;
                case "#7adf2a":
                    // binding.logoBtn.setImageResource(R.drawable.popatilogoscg);
                    break;
                case "#ec0001":
                    // binding.logoBtn.setImageResource(R.drawable.redlogosvg);
                    break;
                case "#16f3ff":
                    // binding.logoBtn.setImageResource(R.drawable.bluelogosvg);
                    break;
                case "#FF8A00":
                    // binding.logoBtn.setImageResource(R.drawable.orangelogosvg);
                    break;
                case "#7F7F7F":
                    // binding.logoBtn.setImageResource(R.drawable.graylogosvg);
                    break;
                case "#D9B845":
                    // binding.logoBtn.setImageResource(R.drawable.yellowlogosvg);
                    break;
                case "#346667":
                    // binding.logoBtn.setImageResource(R.drawable.greenlogosvg);
                    break;
                case "#9846D9":
                    // binding.logoBtn.setImageResource(R.drawable.voiletlogosvg);
                    break;
                case "#A81010":
                    // binding.logoBtn.setImageResource(R.drawable.red2logosvg);
                    break;
                default:
                    // binding.logoBtn.setImageResource(R.drawable.logo_short);
                    break;
            }
            return ColorStateList.valueOf(Color.parseColor(themeColor));
        } catch (Exception ignored) {
            Log.w(TAG, "Error applying theme color: " + ignored.getMessage());
            // Fallback to default color
            return ColorStateList.valueOf(Color.parseColor("#00A3E9"));
        }
    }

    /**
     * Applies dark mode theme colors
     */
    public static ColorStateList applyDarkModeTheme(String themeColor) {
        try {
            switch (themeColor) {
                case "#ff0080":
                    return ColorStateList.valueOf(Color.parseColor("#4D0026"));
                case "#00A3E9":
                    return ColorStateList.valueOf(Color.parseColor("#01253B"));
                case "#7adf2a":
                    return ColorStateList.valueOf(Color.parseColor("#25430D"));
                case "#ec0001":
                    return ColorStateList.valueOf(Color.parseColor("#470000"));
                case "#16f3ff":
                    return ColorStateList.valueOf(Color.parseColor("#05495D"));
                case "#FF8A00":
                    return ColorStateList.valueOf(Color.parseColor("#663700"));
                case "#7F7F7F":
                    return ColorStateList.valueOf(Color.parseColor("#2B3137"));
                case "#D9B845":
                    return ColorStateList.valueOf(Color.parseColor("#413815"));
                case "#346667":
                    return ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                case "#9846D9":
                    return ColorStateList.valueOf(Color.parseColor("#2d1541"));
                case "#A81010":
                    return ColorStateList.valueOf(Color.parseColor("#430706"));
                default:
                    return ColorStateList.valueOf(Color.parseColor("#01253B"));
            }
        } catch (Exception ignored) {
            Log.w(TAG, "Error applying dark mode theme: " + ignored.getMessage());
            return ColorStateList.valueOf(Color.parseColor("#01253B"));
        }
    }

    /**
     * Applies dark mode to specific views
     */
    public static void applyDarkModeToViews(View mainSenderBox, View richBox, String themeColor) {
        if (mainSenderBox != null && richBox != null) {
            ColorStateList tintList = applyDarkModeTheme(themeColor);
            try {
                mainSenderBox.setBackgroundTintList(tintList);
                richBox.setBackgroundTintList(tintList);
            } catch (Exception e) {
                Log.w(TAG, "Error applying dark mode to views: " + e.getMessage());
            }
        } else {
            Log.w(TAG, "MainSenderBox or richBox is null, cannot apply dark mode.");
        }
    }

    /**
     * Applies light mode to specific views
     */
    public static void applyLightModeToViews(View mainSenderBox, View richBox) {
        if (mainSenderBox != null && richBox != null) {
            ColorStateList tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
            try {
                mainSenderBox.setBackgroundTintList(tintList);
                richBox.setBackgroundTintList(tintList);
            } catch (Exception e) {
                Log.w(TAG, "Error applying light mode to views: " + e.getMessage());
            }
        } else {
            Log.w(TAG, "MainSenderBox or richBox is null, cannot apply light mode.");
        }
    }
}
