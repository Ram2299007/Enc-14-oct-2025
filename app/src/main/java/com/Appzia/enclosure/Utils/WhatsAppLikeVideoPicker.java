package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.Appzia.enclosure.Adapter.VideoGridAdapter;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppLikeVideoPicker extends AppCompatActivity {
    
    public static final String EXTRA_SELECTED_VIDEOS = "selected_videos";
    public static final String EXTRA_MAX_SELECTION = "max_selection";
    public static final String EXTRA_CAPTION = "caption";
    public static final int DEFAULT_MAX_SELECTION = 30;
    
    private GridView videoGrid;
    private TextView counterText;
    private TextView smallCounterText;
    private LinearLayout doneButton;
    private LinearLayout cancelButton;
    private ProgressBar loadingIndicator;
    private VideoGridAdapter adapter;
    private List<Uri> selectedVideos = new ArrayList<>();
    private int maxSelection = DEFAULT_MAX_SELECTION;
    
    // Caption functionality
    private EditText messageBoxMy;
    private LinearLayout multiSelectDoneButton;
    private TextView multiSelectSmallCounterText;
    
    // Keyboard handling variables
    private boolean isMessageBoxFocused = false;
    private LinearLayout captionLyt;
    
    // Permission text handling
    private TextView managePermissionText;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_video_picker);
        
        // Fix keyboard behavior - adjust pan to move content above keyboard
        getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        
        // Get extras
        if (getIntent() != null) {
            maxSelection = getIntent().getIntExtra(EXTRA_MAX_SELECTION, DEFAULT_MAX_SELECTION);
            ArrayList<Uri> existingVideos = getIntent().getParcelableArrayListExtra(EXTRA_SELECTED_VIDEOS);
            if (existingVideos != null) {
                selectedVideos.addAll(existingVideos);
            }
        }
        
        initViews();
        setupClickListeners();
        loadVideos();
    }
    
    private void initViews() {
        videoGrid = findViewById(R.id.videoGrid);
        counterText = findViewById(R.id.counterText);
        smallCounterText = findViewById(R.id.smallCounterText);
        doneButton = findViewById(R.id.doneButton);
        cancelButton = findViewById(R.id.cancelButton);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        
        // Caption views
        messageBoxMy = findViewById(R.id.messageBoxMy);
        multiSelectDoneButton = findViewById(R.id.multiSelectDoneButton);
        multiSelectSmallCounterText = findViewById(R.id.multiSelectSmallCounterText);
        captionLyt = findViewById(R.id.captionlyt);
        managePermissionText = findViewById(R.id.managePermissionText);
        
        applyThemeColors();
        updateCounter();
        setupCaptionFunctionality();
        setupKeyboardHandling();
        setupPermissionText();
    }
    
    private void applyThemeColors() {
        // Get theme color from SharedPreferences
        String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        int color = Color.parseColor(themeColor);
        
        // Create circular background for Done button
        GradientDrawable doneButtonBg = new GradientDrawable();
        doneButtonBg.setShape(GradientDrawable.OVAL);
        doneButtonBg.setColor(color);
        doneButton.setBackground(doneButtonBg);
        
        // Apply theme color to small counter background
        GradientDrawable counterBg = new GradientDrawable();
        counterBg.setShape(GradientDrawable.OVAL);
        counterBg.setColor(color);
        smallCounterText.setBackground(counterBg);
    }
    
    private void setupClickListeners() {
        cancelButton.setOnClickListener(v -> {
            // Clear all selections when canceling
            selectedVideos.clear();
            if (adapter != null) {
                adapter.clearSelection();
            }
            setResult(RESULT_CANCELED);
            SwipeNavigationHelper.finishWithSwipe(WhatsAppLikeVideoPicker.this);
        });
        
        doneButton.setOnClickListener(v -> {
            if (selectedVideos.isEmpty()) {
                Toast.makeText(this, "Please select at least one video", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String caption = messageBoxMy.getText().toString().trim();
            Log.d("WhatsAppLikeVideoPicker", "Done button clicked - Caption: '" + caption + "'");
            
            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra(EXTRA_SELECTED_VIDEOS, new ArrayList<>(selectedVideos));
            resultIntent.putExtra(EXTRA_CAPTION, caption);
            setResult(RESULT_OK, resultIntent);
            SwipeNavigationHelper.finishWithSwipe(WhatsAppLikeVideoPicker.this);
        });
        
        // Multi-select done button (same functionality as regular done button)
        multiSelectDoneButton.setOnClickListener(v -> {
            if (selectedVideos.isEmpty()) {
                Toast.makeText(this, "Please select at least one video", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String caption = messageBoxMy.getText().toString().trim();
            Log.d("WhatsAppLikeVideoPicker", "Multi-select done button clicked - Caption: '" + caption + "'");
            
            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra(EXTRA_SELECTED_VIDEOS, new ArrayList<>(selectedVideos));
            resultIntent.putExtra(EXTRA_CAPTION, caption);
            setResult(RESULT_OK, resultIntent);
            SwipeNavigationHelper.finishWithSwipe(WhatsAppLikeVideoPicker.this);
        });
    }
    
    private void loadVideos() {
        new Thread(() -> {
            List<Uri> videoUris = getAllVideoUris();
            
            runOnUiThread(() -> {
                loadingIndicator.setVisibility(View.GONE);
                videoGrid.setVisibility(View.VISIBLE);
                
                // Debug: Log the number of videos found
                android.util.Log.d("VideoPicker", "Found " + videoUris.size() + " videos");
                
                // Remove the permission error toast - handle empty results gracefully like image picker
                // The permission check is already handled by GlobalPermissionPopup before opening this activity
                
                adapter = new VideoGridAdapter(this, videoUris, selectedVideos, maxSelection);
                
                // Apply theme color to adapter
                String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                int color = Color.parseColor(themeColor);
                ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themeColor));
                adapter.setThemeColor(color,tintList);
                
                videoGrid.setAdapter(adapter);
                
                adapter.setOnVideoSelectionListener(new VideoGridAdapter.OnVideoSelectionListener() {
                    @Override
                    public void onVideoSelected(Uri videoUri) {
                        if (selectedVideos.contains(videoUri)) {
                            selectedVideos.remove(videoUri);
                        } else {
                            if (selectedVideos.size() < maxSelection) {
                                selectedVideos.add(videoUri);
                            } else {
                                Toast.makeText(WhatsAppLikeVideoPicker.this, 
                                    "Maximum " + maxSelection + " videos allowed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        updateCounter();
                        adapter.notifyDataSetChanged();
                    }
                });
            });
        }).start();
    }
    
    private List<Uri> getAllVideoUris() {
        List<Uri> videoUris = new ArrayList<>();
        String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME};
        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";
        
        try {
            Cursor cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            );
            
            if (cursor != null) {
                android.util.Log.d("VideoPicker", "Cursor count: " + cursor.getCount());
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    Uri videoUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
                    videoUris.add(videoUri);
                    android.util.Log.d("VideoPicker", "Added video: " + displayName + " (ID: " + id + ")");
                }
                cursor.close();
            } else {
                android.util.Log.e("VideoPicker", "Cursor is null - permission issue?");
            }
        } catch (Exception e) {
            android.util.Log.e("VideoPicker", "Error querying videos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return videoUris;
    }
    
    private void updateCounter() {
        int count = selectedVideos.size();
        counterText.setText(count + "/" + maxSelection);
        
        if (count > 0) {
            smallCounterText.setText(String.valueOf(count));
            multiSelectSmallCounterText.setText(String.valueOf(count));
            smallCounterText.setVisibility(View.VISIBLE);
            multiSelectSmallCounterText.setVisibility(View.VISIBLE);
            doneButton.setEnabled(true);
            multiSelectDoneButton.setEnabled(true);
            doneButton.setAlpha(1.0f);
            multiSelectDoneButton.setAlpha(1.0f);
        } else {
            smallCounterText.setVisibility(View.GONE);
            multiSelectSmallCounterText.setVisibility(View.GONE);
            doneButton.setEnabled(false);
            multiSelectDoneButton.setEnabled(false);
            doneButton.setAlpha(0.5f);
            multiSelectDoneButton.setAlpha(0.5f);
        }
    }
    
    private void setupCaptionFunctionality() {
        // Initialize caption functionality similar to WhatsApp image picker
        if (messageBoxMy != null) {
            // Set up caption input handling
            messageBoxMy.setOnFocusChangeListener((v, hasFocus) -> {
                // Handle focus changes if needed
            });
        }
    }
    
    private void setupKeyboardHandling() {
        if (messageBoxMy == null) return;
        
        // Set initial 55dp bottom margin for captionLyt
        updateCaptionLayoutMargin(false);
        
        // Setup messageBox listeners
        messageBoxMy.setOnClickListener(v -> {
            Log.d("MessageBoxTouch", "MessageBox touched, setting focus to true");
            isMessageBoxFocused = true;
            updateCaptionLayoutMargin(true);
        });
        
        messageBoxMy.setOnFocusChangeListener((v, hasFocus) -> {
            Log.d("MessageBoxFocus", "MessageBox focus changed: " + hasFocus);
            isMessageBoxFocused = hasFocus;
            updateCaptionLayoutMargin(hasFocus);
        });
        
        // Setup keyboard visibility listener
        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Detect keyboard visibility
                View rootView = getWindow().getDecorView().getRootView();
                android.graphics.Rect r = new android.graphics.Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getHeight();
                int keypadHeight = screenHeight - r.bottom;
                
                // Keyboard is visible if keypad height is more than 15% of screen height
                boolean isKeyboardVisible = keypadHeight > screenHeight * 0.15;
                
                Log.d("KeyboardLayout", "Keypad height: " + keypadHeight + ", Screen height: " + screenHeight + 
                      ", Keyboard visible: " + isKeyboardVisible);
                
                updateCaptionLayoutMargin(isKeyboardVisible);
            }
        });
    }
    
    private void updateCaptionLayoutMargin(boolean isKeyboardVisible) {
        if (captionLyt == null) return;
        
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) captionLyt.getLayoutParams();
        if (params != null) {
            int baseMargin = (int) (55 * getResources().getDisplayMetrics().density); // 55dp base margin
            int keyboardMargin = (int) (120 * getResources().getDisplayMetrics().density); // 120dp when keyboard is open
            
            if (isKeyboardVisible && isMessageBoxFocused) {
                params.bottomMargin = keyboardMargin;
                Log.d("CaptionMargin", "Keyboard visible and focused - setting margin to " + keyboardMargin + "px (120dp)");
            } else {
                params.bottomMargin = baseMargin;
                Log.d("CaptionMargin", "Keyboard not visible or not focused - setting margin to " + baseMargin + "px (55dp)");
            }
            
            captionLyt.setLayoutParams(params);
            captionLyt.requestLayout();
        }
    }
    
    @Override
    public void onBackPressed() {
        Log.d("BackPressed", "Back button pressed - messageBoxMy focus: " + (messageBoxMy != null ? messageBoxMy.hasFocus() : "null") + ", isMessageBoxFocused: " + isMessageBoxFocused);
        
        // Simple one-tap logic: if messageBox is focused, clear focus; otherwise finish activity
        if (messageBoxMy != null && (messageBoxMy.hasFocus() || isMessageBoxFocused)) {
            Log.d("BackPressed", "MessageBox is focused - clearing focus and hiding keyboard");
            
            // Clear focus immediately
            messageBoxMy.clearFocus();
            isMessageBoxFocused = false;
            
            // Hide keyboard with the most reliable method
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(messageBoxMy.getWindowToken(), 0);
            }
            
            Log.d("BackPressed", "Focus cleared - staying in activity");
            return; // Don't finish activity, just clear focus
        }
        
        // Clear all selections when going back
        selectedVideos.clear();
        if (adapter != null) {
            adapter.clearSelection();
        }
        setResult(RESULT_CANCELED);
        
        Log.d("BackPressed", "MessageBox not focused - finishing activity");
        SwipeNavigationHelper.finishWithSwipe(WhatsAppLikeVideoPicker.this); // Finish activity with swipe
    }
    
    private void setupPermissionText() {
        if (managePermissionText == null) {
            Log.d("PermissionText", "managePermissionText is null - TextView not found");
            return;
        }
        
        boolean hasLimitedAccess = hasLimitedPhotoAccess();
        Log.d("PermissionText", "hasLimitedAccess: " + hasLimitedAccess);
        
        if (hasLimitedAccess) {
            Log.d("PermissionText", "Showing managePermissionText");
            managePermissionText.setVisibility(View.VISIBLE);
            setupUnderlinedManageText();
        } else {
            Log.d("PermissionText", "Hiding managePermissionText");
            managePermissionText.setVisibility(View.GONE);
        }
    }
    
    private boolean hasLimitedPhotoAccess() {
        return com.Appzia.enclosure.Utils.GlobalPermissionPopup.hasLimitedPhotoAccess(this);
    }
    
    private void setupUnderlinedManageText() {
        if (managePermissionText == null) return;
        
        String fullText = "You've given Enclosure permission to access only a select number of photos. Manage";
        String clickableText = "Manage";
        
        // Create SpannableString
        android.text.SpannableString spannableString = new android.text.SpannableString(fullText);
        
        // Find the start and end positions of the clickable text
        int start = fullText.indexOf(clickableText);
        int end = start + clickableText.length();
        
        if (start != -1) {
            // Get theme color from SharedPreferences
            String themeColor = com.Appzia.enclosure.Utils.Constant.getSF.getString(com.Appzia.enclosure.Utils.Constant.ThemeColorKey, "#00A3E9");
            int color = android.graphics.Color.parseColor(themeColor);
            
            // Create clickable span
            android.text.style.ClickableSpan clickableSpan = new android.text.style.ClickableSpan() {
                @Override
                public void onClick(android.view.View widget) {
                    // Open app settings
                    android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    android.net.Uri uri = android.net.Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    SwipeNavigationHelper.startActivityWithSwipe(WhatsAppLikeVideoPicker.this, intent);
                }
                
                @Override
                public void updateDrawState(android.text.TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(true);
                    ds.setColor(color); // Use theme color for the "Manage" text
                }
            };
            
            // Apply underline span
            spannableString.setSpan(new android.text.style.UnderlineSpan(), start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Apply clickable span
            spannableString.setSpan(clickableSpan, start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Set the text and make it clickable
            managePermissionText.setText(spannableString);
            managePermissionText.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        }
    }
}