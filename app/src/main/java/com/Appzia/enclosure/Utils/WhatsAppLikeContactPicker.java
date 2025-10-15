package com.Appzia.enclosure.Utils;

/**
 * WhatsAppLikeContactPicker - Contact picker with Firebase upload integration
 * 
 * INTEGRATION GUIDE:
 * 
 * 1. In chattingScreen.java onActivityResult():
 *    WhatsAppLikeContactPicker.handleContactPickerResult(
 *        mContext, requestCode, resultCode, data, 
 *        senderId, receiverUid, userFTokenKey, 
 *        false, null
 *    );
 * 
 * 2. In grpChattingScreen.java onActivityResult():
 *    WhatsAppLikeContactPicker.handleContactPickerResult(
 *        mContext, requestCode, resultCode, data, 
 *        senderId, null, null, 
 *        true, grpIdKey
 *    );
 * 
 * 3. To launch contact picker:
 *    Intent intent = new Intent(this, WhatsAppLikeContactPicker.class);
 *    startActivityForResult(intent, 1001);
 */

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.UUID;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.ContactPickerAdapter;
import com.Appzia.enclosure.R;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppLikeContactPicker extends AppCompatActivity {
    public static final String EXTRA_SELECTED_CONTACTS = "selected_contacts";
    public static final String EXTRA_MAX_SELECTION = "max_selection";
    public static final String EXTRA_CAPTION = "caption";
    public static final int DEFAULT_MAX_SELECTION = 50;
    private RecyclerView contactList;
    private TextView counterText;
    private TextView smallCounterText;
    private LinearLayout doneButton;
    private LinearLayout cancelButton;
    private ProgressBar loadingIndicator;
    private EditText searchEditText;
    private ContactPickerAdapter adapter;
    private List<ContactInfo> selectedContacts = new ArrayList<>();
    private List<ContactInfo> allContacts = new ArrayList<>();
    private List<ContactInfo> filteredContacts = new ArrayList<>();
    private Map<Integer, String> contactCaptions = new HashMap<>();
    private boolean isUpdatingContactText = false;
    private int maxSelection = DEFAULT_MAX_SELECTION;
    private static final int CONTACTS_PER_PAGE = 50;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMoreContacts = true;
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final int SEARCH_DELAY = 150; // 150ms delay for search
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener;
    private boolean isMessageBoxFocused = false;
    
    // Caption functionality
    private EditText messageBoxMy;
    private LinearLayout multiSelectDoneButton;
    private TextView multiSelectSmallCounterText;

    public static class ContactInfo implements Parcelable {
        public Uri contactUri;
        public String name;
        public String phone;
        public String email;
        public boolean isSelected;

        public ContactInfo(Uri contactUri, String name, String phone, String email) {
            this.contactUri = contactUri;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.isSelected = false;
        }

        protected ContactInfo(Parcel in) {
            contactUri = in.readParcelable(Uri.class.getClassLoader());
            name = in.readString();
            phone = in.readString();
            email = in.readString();
            isSelected = in.readByte() != 0;
        }

        public static final Creator<ContactInfo> CREATOR = new Creator<ContactInfo>() {
            @Override
            public ContactInfo createFromParcel(Parcel in) {
                return new ContactInfo(in);
            }

            @Override
            public ContactInfo[] newArray(int size) {
                return new ContactInfo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(contactUri, flags);
            dest.writeString(name);
            dest.writeString(phone);
            dest.writeString(email);
            dest.writeByte((byte) (isSelected ? 1 : 0));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_contact_picker);

        // Fix keyboard behavior - adjust pan to move content above keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        
        // Simplified back button handling - remove complex callback

        // Ensure the root view handles keyboard properly
        findViewById(android.R.id.content).setFitsSystemWindows(true);
        
        // Apply edge-to-edge display for better keyboard handling
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        
        // Set up window insets listener for better keyboard and navigation bar handling
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            androidx.core.graphics.Insets keyboardInsets = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.ime());
            androidx.core.graphics.Insets navBarInsets = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.navigationBars());
            boolean keyboardVisible = keyboardInsets.bottom > 0;
            
            Log.d("WindowInsets", "Keyboard insets bottom: " + keyboardInsets.bottom + 
                  ", NavBar insets bottom: " + navBarInsets.bottom + 
                  ", Keyboard visible: " + keyboardVisible);
            
            // No positioning changes - keep original layout
            return insets;
        });

        // Add keyboard visibility listener to handle layout adjustments
        keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Use activity root view for more accurate keyboard detection
                View rootView = getWindow().getDecorView().getRootView();
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getHeight();
                int keypadHeight = screenHeight - r.bottom;
                
                // More accurate keyboard detection - use 15% threshold for better detection
                boolean isKeyboardVisible = keypadHeight > screenHeight * 0.15;

                Log.d("KeyboardLayout", "Keypad height: " + keypadHeight + ", Screen height: " + screenHeight + 
                      ", Focus: " + isMessageBoxFocused + ", Keyboard visible: " + isKeyboardVisible + 
                      ", Threshold: " + (screenHeight * 0.15) + ", Rect bottom: " + r.bottom);
                
                // No positioning changes - keep original layout
            }
        };
        findViewById(android.R.id.content).getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

        // Get max selection from intent
        maxSelection = getIntent().getIntExtra(EXTRA_MAX_SELECTION, DEFAULT_MAX_SELECTION);

        // Get already selected contacts from intent
        ArrayList<ContactInfo> alreadySelected = getIntent().getParcelableArrayListExtra(EXTRA_SELECTED_CONTACTS);
        if (alreadySelected != null) {
            selectedContacts.addAll(alreadySelected);
        }

        initViews();
        setupAdapter();
        loadContacts();

        // Load all contacts in background for fast search immediately
        loadAllContactsInBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up keyboard listener to prevent memory leaks
        if (keyboardLayoutListener != null) {
            findViewById(android.R.id.content).getViewTreeObserver().removeOnGlobalLayoutListener(keyboardLayoutListener);
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
        
        Log.d("BackPressed", "MessageBox not focused - finishing activity");
        SwipeNavigationHelper.finishWithSwipe(WhatsAppLikeContactPicker.this); // Finish activity immediately
    }
    
    // Removed conflicting key event handlers to prevent double back press issue
    
    // Method to force clear focus and hide keyboard immediately
    private void forceClearFocusAndHideKeyboard() {
        Log.d("ForceClear", "Force clearing focus and hiding keyboard");
        
        if (messageBoxMy != null) {
            messageBoxMy.clearFocus();
            isMessageBoxFocused = false;
            
            // Multiple methods to hide keyboard
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(messageBoxMy.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(getCurrentFocus() != null ? getCurrentFocus().getWindowToken() : null, 0);
                imm.toggleSoftInput(android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        }
        
        // Clear focus from any focused view
        if (getCurrentFocus() != null) {
            getCurrentFocus().clearFocus();
        }
    }

    private void updateCaptionLayoutMargin() {
        // Simplified approach - just update based on focus state
        updateCaptionLayoutMargin(false); // Parameter ignored in simplified version
    }

    private void updateCaptionLayoutMargin(boolean isKeyboardVisible) {
        // Do not move captionlyt position - keep original layout
        Log.d("CaptionMargin", "Caption layout positioning disabled - keeping original position");
    }
    
    private int getNavigationBarHeight() {
        // Get navigation bar height to ensure proper positioning
        androidx.core.graphics.Insets insets = androidx.core.view.ViewCompat.getRootWindowInsets(findViewById(android.R.id.content))
                .getInsets(androidx.core.view.WindowInsetsCompat.Type.navigationBars());
        return insets.bottom;
    }

    // Method to manually test focus behavior
    public void testFocusBehavior() {
        Log.d("TestFocus", "Testing focus behavior - current state: " + isMessageBoxFocused);
        updateCaptionLayoutMargin();
    }

    // Method to manually test keyboard detection
    public void testKeyboardDetection() {
        View rootView = getWindow().getDecorView().getRootView();
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int screenHeight = rootView.getHeight();
        int keypadHeight = screenHeight - r.bottom;
        boolean isKeyboardVisible = keypadHeight > screenHeight * 0.15;
        
        Log.d("TestKeyboard", "Manual keyboard test - Keypad: " + keypadHeight + 
              ", Screen: " + screenHeight + ", Visible: " + isKeyboardVisible + 
              ", Threshold: " + (screenHeight * 0.15) + ", Rect bottom: " + r.bottom);
        
        // Force update with current keyboard state
        updateCaptionLayoutMargin(isKeyboardVisible);
    }
    
    // Method to force messageBox above keyboard (for testing)
    public void forceMessageBoxAboveKeyboard() {
        Log.d("ForceKeyboard", "Forcing messageBox above keyboard");
        isMessageBoxFocused = true;
        updateCaptionLayoutMargin(true);
    }
    
    // Method to immediately position messageBox above keyboard when clicked
    private void forceMessageBoxAboveKeyboardImmediate() {
        Log.d("ForceKeyboardImmediate", "Immediately positioning messageBox above keyboard");
        LinearLayout captionLyt = findViewById(R.id.captionlyt);
        if (captionLyt != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) captionLyt.getLayoutParams();
            
            // Check if keyboard is actually visible to use appropriate margin
            View rootView = getWindow().getDecorView().getRootView();
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getHeight();
            int keypadHeight = screenHeight - r.bottom;
            boolean isKeyboardActuallyVisible = keypadHeight > screenHeight * 0.15;
            
            // Use higher margin when keyboard is actually visible, lower when not
            int immediateMargin;
            if (isKeyboardActuallyVisible) {
                immediateMargin = (int) (120 * getResources().getDisplayMetrics().density); // 120dp when keyboard is open
                Log.d("ForceKeyboardImmediate", "Keyboard detected - using high margin");
            } else {
                immediateMargin = (int) (80 * getResources().getDisplayMetrics().density); // 80dp when keyboard not yet visible
                Log.d("ForceKeyboardImmediate", "Keyboard not yet visible - using medium margin");
            }
            
            params.bottomMargin = immediateMargin;
            
            captionLyt.setLayoutParams(params);
            captionLyt.requestLayout();
            
            // Force parent layout update
            View parent = (View) captionLyt.getParent();
            if (parent != null) {
                parent.requestLayout();
            }
            
            Log.d("ForceKeyboardImmediate", "Set immediate margin to " + immediateMargin + "px (" + (isKeyboardActuallyVisible ? "120dp" : "80dp") + ")");
        }
    }
    
    // Method to ensure messageBox is above navigation bar
    public void ensureMessageBoxAboveNavBar() {
        Log.d("NavBarFix", "Ensuring messageBox above navigation bar");
        LinearLayout captionLyt = findViewById(R.id.captionlyt);
        if (captionLyt != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) captionLyt.getLayoutParams();
            int navigationBarHeight = getNavigationBarHeight();
            int safeMargin = navigationBarHeight + (int) (32 * getResources().getDisplayMetrics().density); // Increased margin
            
            params.bottomMargin = safeMargin;
            captionLyt.setLayoutParams(params);
            captionLyt.requestLayout();
            
            Log.d("NavBarFix", "Set margin to " + safeMargin + "px to stay above nav bar (" + navigationBarHeight + "px)");
        }
    }
    
    // Method to position messageBox even higher (for testing)
    public void positionMessageBoxVeryHigh() {
        Log.d("VeryHigh", "Positioning messageBox very high");
        LinearLayout captionLyt = findViewById(R.id.captionlyt);
        if (captionLyt != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) captionLyt.getLayoutParams();
            int veryHighMargin = (int) (100 * getResources().getDisplayMetrics().density); // 100dp - very high
            
            params.bottomMargin = veryHighMargin;
            captionLyt.setLayoutParams(params);
            captionLyt.requestLayout();
            
            Log.d("VeryHigh", "Set very high margin to " + veryHighMargin + "px (100dp)");
        }
    }
    
    // Method to position messageBox very high when keyboard is open
    public void positionMessageBoxAboveKeyboardWhenOpen() {
        Log.d("KeyboardOpen", "Positioning messageBox very high above open keyboard");
        LinearLayout captionLyt = findViewById(R.id.captionlyt);
        if (captionLyt != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) captionLyt.getLayoutParams();
            int keyboardOpenMargin = (int) (150 * getResources().getDisplayMetrics().density); // 150dp - very high above keyboard
            
            params.bottomMargin = keyboardOpenMargin;
            captionLyt.setLayoutParams(params);
            captionLyt.requestLayout();
            
            Log.d("KeyboardOpen", "Set keyboard open margin to " + keyboardOpenMargin + "px (150dp)");
        }
    }

    // Helper method to check if a point is inside a view
    private boolean isPointInsideView(float x, float y, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        
        return (x >= viewX && x <= viewX + viewWidth && y >= viewY && y <= viewY + viewHeight);
    }

    private void initViews() {
        contactList = findViewById(R.id.contactList);
        counterText = findViewById(R.id.counterText);
        smallCounterText = findViewById(R.id.smallCounterText);
        doneButton = findViewById(R.id.doneButton);
        cancelButton = findViewById(R.id.cancelButton);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        searchEditText = findViewById(R.id.searchEditText);
        
        // Caption views
        messageBoxMy = findViewById(R.id.messageBoxMy);
        multiSelectDoneButton = findViewById(R.id.multiSelectDoneButton);
        multiSelectSmallCounterText = findViewById(R.id.multiSelectSmallCounterText);

        // Setup messageBoxMy interaction detection
        if (messageBoxMy != null) {
            Log.d("MessageBoxSetup", "Setting up messageBoxMy listeners");
            
            // Use TextWatcher to detect when user is actively typing
            messageBoxMy.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // User is about to type - set focus to true
                    if (!isMessageBoxFocused) {
                        Log.d("MessageBoxText", "User started typing, setting focus to true");
                        isMessageBoxFocused = true;
                        // No positioning changes - keep original layout
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // User is actively typing - keep focus true
                    if (!isMessageBoxFocused) {
                        Log.d("MessageBoxText", "Text changed, setting focus to true");
                        isMessageBoxFocused = true;
                        // No positioning changes - keep original layout
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Text change completed - keep focus true
                    if (!isMessageBoxFocused) {
                        Log.d("MessageBoxText", "After text changed, setting focus to true");
                        isMessageBoxFocused = true;
                        // No positioning changes - keep original layout
                    }
                }
            });

            // Add click listener to detect when user taps the field
            messageBoxMy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MessageBoxClick", "MessageBox clicked, setting focus to true");
                    isMessageBoxFocused = true;
                    // No positioning changes - keep original layout
                }
            });

            // Touch listener will be added later with double-tap detection

            // Add focus listener as additional backup
            messageBoxMy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.d("MessageBoxFocus", "Focus changed: " + hasFocus + " on view: " + v.getClass().getSimpleName());
                    isMessageBoxFocused = hasFocus;
                    // No positioning changes - keep original layout
                }
            });
            
            // Removed setOnKeyListener to prevent conflicts with onBackPressed
        } else {
            Log.e("MessageBoxSetup", "messageBoxMy is null!");
        }

        // Apply theme colors
        applyThemeColors();

        // Setup search functionality
        setupSearch();
        
        // Setup caption functionality
        setupCaptionFunctionality();

        // Add a test button to manually test focus behavior (for debugging)
        if (messageBoxMy != null) {
            messageBoxMy.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("TestFocus", "Long click detected - toggling focus state");
                    isMessageBoxFocused = !isMessageBoxFocused;
                    updateCaptionLayoutMargin();
                    return true;
                }
            });

            // Add touch listener with tap detection and focus setting
            messageBoxMy.setOnTouchListener(new View.OnTouchListener() {
                private long lastTouchTime = 0;
                private int tapCount = 0;
                private static final int TAP_TIME_DELTA = 300;

                @Override
                public boolean onTouch(View v, android.view.MotionEvent event) {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastTouchTime < TAP_TIME_DELTA) {
                            tapCount++;
                            if (tapCount == 2) {
                                // Double tap - test keyboard detection
                                Log.d("TestKeyboard", "Double tap detected - testing keyboard detection");
                                testKeyboardDetection();
                            } else if (tapCount == 3) {
                                // Triple tap - toggle focus state
                                Log.d("TestFocus", "Triple tap detected - toggling focus state");
                                isMessageBoxFocused = !isMessageBoxFocused;
                                updateCaptionLayoutMargin();
                                tapCount = 0; // Reset counter
                            }
                        } else {
                            // Single tap - set focus
                            Log.d("MessageBoxTouch", "MessageBox touched, setting focus to true");
                            isMessageBoxFocused = true;
                            // No positioning changes - keep original layout
                            tapCount = 1;
                        }
                        lastTouchTime = currentTime;
                    }
                    return false; // Let other touch events continue
                }
            });
        }

        // Add global touch listener to detect when user taps outside messageBoxMy
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, android.view.MotionEvent event) {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        Log.d("GlobalTouch", "Global touch detected at (" + event.getX() + ", " + event.getY() + ")");
                        // Check if the touch is outside messageBoxMy
                        if (messageBoxMy != null && !isPointInsideView(event.getX(), event.getY(), messageBoxMy)) {
                            Log.d("GlobalTouch", "Touched outside messageBoxMy, clearing focus");
                            isMessageBoxFocused = false;
                            // Clear focus from messageBoxMy and hide keyboard
                            messageBoxMy.clearFocus();
                            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.hideSoftInputFromWindow(messageBoxMy.getWindowToken(), 0);
                            }
                        } else {
                            Log.d("GlobalTouch", "Touched inside messageBoxMy, keeping focus true");
                        }
                    }
                    return false; // Let other touch events continue
                }
            });
        }

        doneButton.setOnClickListener(v -> {
            if (selectedContacts.isEmpty()) {
                Toast.makeText(this, "No contacts selected", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Show contact preview dialog first
            showContactPreviewDialog();
        });
        
        // Multi-select done button (same functionality as regular done button)
        if (multiSelectDoneButton != null) {
            multiSelectDoneButton.setOnClickListener(v -> {
                if (selectedContacts.isEmpty()) {
                    Toast.makeText(this, "No contacts selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Show contact preview dialog first
                showContactPreviewDialog();
            });
        }

        cancelButton.setOnClickListener(v -> {
            // Clear all selections when canceling
            selectedContacts.clear();
            if (adapter != null) {
                adapter.clearSelection();
            }
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }
    
    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancel previous search
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                // Create new search runnable
                searchRunnable = () -> {
                    String query = s.toString();
                    Log.d("ContactSearch", "Search query: '" + query + "'");

                    if (query.trim().isEmpty()) {
                        // If search is empty, go back to pagination mode
                        Log.d("ContactSearch", "Empty query - switching to pagination mode");
                        currentPage = 0;
                        hasMoreContacts = true;
                        loadContacts();
                    } else {
                        // If searching, ensure we have all contacts loaded for smooth search
                        if (allContacts == null || allContacts.isEmpty()) {
                            Log.d("ContactSearch", "Loading all contacts for smooth search");
                            loadAllContactsForSearch();
                        } else {
                            Log.d("ContactSearch", "Fast filtering with query: '" + query + "'");
                            fastFilterContacts(query);
                        }
                    }
                };

                // Delay search by 50ms to avoid too many searches
                searchHandler.postDelayed(searchRunnable, 50);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    
    private void setupCaptionFunctionality() {
        // Initialize caption functionality similar to WhatsApp video picker
        if (messageBoxMy != null) {
            // Set up caption input handling
            messageBoxMy.setOnFocusChangeListener((v, hasFocus) -> {
                // Handle focus changes if needed
            });
        }
    }
    

    private void fastFilterContacts(String query) {
        Log.d("ContactSearch", "fastFilterContacts called with query: '" + query + "'");

        if (allContacts == null || allContacts.isEmpty()) {
            Log.d("ContactSearch", "allContacts not ready yet, loading all contacts first");
            loadAllContactsForSearch();
            return;
        }

        long startTime = System.currentTimeMillis();
        filteredContacts = new ArrayList<>();
        String lowerQuery = query.toLowerCase().trim();

        Log.d("ContactSearch", "Fast filtering " + allContacts.size() + " contacts");
        for (ContactInfo contact : allContacts) {
            if (isContactMatching(contact, lowerQuery)) {
                filteredContacts.add(contact);
            }
        }

        long endTime = System.currentTimeMillis();
        Log.d("ContactSearch", "Fast filtered to " + filteredContacts.size() + " contacts in " + (endTime - startTime) + "ms");

        // Update adapter with filtered results
        if (adapter != null) {
            adapter.updateContacts(filteredContacts);
        }

        // Update UI to show search results
        updateUI();
    }

    private void filterContacts(String query) {
        Log.d("ContactSearch", "filterContacts called with query: '" + query + "'");
        Log.d("ContactSearch", "allContacts size: " + (allContacts != null ? allContacts.size() : "null"));

        if (query == null || query.trim().isEmpty()) {
            // If no query, show all loaded contacts
            if (allContacts != null && !allContacts.isEmpty()) {
                filteredContacts = new ArrayList<>(allContacts);
            } else {
                // If allContacts is empty, load all contacts first
                loadAllContactsForSearch();
                return;
            }
        } else {
            filteredContacts = new ArrayList<>();
            String lowerQuery = query.toLowerCase().trim();

            if (allContacts != null) {
                Log.d("ContactSearch", "Filtering " + allContacts.size() + " contacts");
                for (ContactInfo contact : allContacts) {
                    if (isContactMatching(contact, lowerQuery)) {
                        filteredContacts.add(contact);
                    }
                }
                Log.d("ContactSearch", "Filtered to " + filteredContacts.size() + " contacts");
            } else {
                Log.d("ContactSearch", "allContacts is null, loading all contacts first");
                loadAllContactsForSearch();
                return;
            }
        }

        // Update adapter with filtered results
        if (adapter != null) {
            Log.d("ContactSearch", "Updating adapter with " + filteredContacts.size() + " contacts");
            adapter.updateContacts(filteredContacts);
        } else {
            Log.d("ContactSearch", "Adapter is null!");
        }
    }

    private boolean isContactMatching(ContactInfo contact, String lowerQuery) {
        // Check name first (most common search)
        if (contact.name != null && contact.name.toLowerCase().contains(lowerQuery)) {
            return true;
        }

        // Check phone
        if (contact.phone != null && contact.phone.toLowerCase().contains(lowerQuery)) {
            return true;
        }

        // Check email
        if (contact.email != null && contact.email.toLowerCase().contains(lowerQuery)) {
            return true;
        }

        return false;
    }

    private void applyThemeColors() {
        // Get theme color from SharedPreferences
        String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        int color = Color.parseColor(themeColor);
       ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themeColor));
        // Create circular background for Done button
        GradientDrawable doneButtonBg = new GradientDrawable();
        doneButtonBg.setShape(GradientDrawable.OVAL);
        doneButtonBg.setColor(color);
        doneButton.setBackground(doneButtonBg);

        // Create circular background for small counter
        GradientDrawable counterBg = new GradientDrawable();
        counterBg.setShape(GradientDrawable.OVAL);
        counterBg.setColor(color);
        counterBg.setStroke(2, Color.WHITE); // White border
        smallCounterText.setBackground(counterBg);

        // Update adapter with theme color
        if (adapter != null) {
            adapter.setThemeColor(color,tintList);
        }
    }

    private void loadContacts() {
        if (isLoading) return;

        isLoading = true;
        loadingIndicator.setVisibility(View.VISIBLE);
        contactList.setVisibility(View.GONE);

        // Load contacts in background thread with pagination
        new Thread(() -> {
            List<ContactInfo> contacts = new ArrayList<>();

            String[] projection = {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
            };

            // Calculate offset for pagination
            int offset = currentPage * CONTACTS_PER_PAGE;
            String limit = String.valueOf(CONTACTS_PER_PAGE);

            Cursor cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0",
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC LIMIT " + limit + " OFFSET " + offset
            );

            if (cursor != null) {
                int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                while (cursor.moveToNext()) {
                    long contactId = cursor.getLong(idIndex);
                    String name = cursor.getString(nameIndex);

                    if (name != null && !name.trim().isEmpty()) {
                        Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));

                        // Get phone number
                        String phone = getContactPhone(contactId);

                        // Get email
                        String email = getContactEmail(contactId);

                        ContactInfo contactInfo = new ContactInfo(contactUri, name, phone, email);
                        contacts.add(contactInfo);
                    }
                }
                cursor.close();

                // Check if there are more contacts
                hasMoreContacts = contacts.size() == CONTACTS_PER_PAGE;
            }

            // Update UI on main thread
            runOnUiThread(() -> {
                loadingIndicator.setVisibility(View.GONE);
                contactList.setVisibility(View.VISIBLE);

                if (currentPage == 0) {
                    // First page - replace all contacts
                    allContacts = new ArrayList<>(contacts);
                    filteredContacts = new ArrayList<>(contacts);
                    Log.d("ContactSearch", "Initial load: " + allContacts.size() + " contacts");
                } else {
                    // Subsequent pages - append to existing contacts
                    if (allContacts == null) {
                        allContacts = new ArrayList<>();
                    }
                    allContacts.addAll(contacts);
                    filteredContacts.addAll(contacts);
                    Log.d("ContactSearch", "Added page: " + contacts.size() + " contacts, total: " + allContacts.size());
                }

                // Update adapter with filtered contacts
                if (adapter != null) {
                    adapter.updateContacts(filteredContacts);
                }

                isLoading = false;
                updateUI();
            });
        }).start();
    }

    private void loadAllContactsInBackground() {
        // Load all contacts in background for fast search
        new Thread(() -> {
            List<ContactInfo> allContactsList = new ArrayList<>();

            String[] projection = {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
            };

            Cursor cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0",
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            );

            if (cursor != null) {
                int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                while (cursor.moveToNext()) {
                    long contactId = cursor.getLong(idIndex);
                    String name = cursor.getString(nameIndex);

                    if (name != null && !name.trim().isEmpty()) {
                        Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));

                        // Get phone number
                        String phone = getContactPhone(contactId);

                        // Get email
                        String email = getContactEmail(contactId);

                        ContactInfo contactInfo = new ContactInfo(contactUri, name, phone, email);
                        allContactsList.add(contactInfo);
                    }
                }
                cursor.close();
            }

            // Update allContacts on main thread
            runOnUiThread(() -> {
                allContacts = allContactsList;
                Log.d("ContactSearch", "Background loaded " + allContacts.size() + " contacts for fast search");
            });
        }).start();
    }

    private void loadAllContactsForSearch() {
        // Load all contacts for search functionality
        new Thread(() -> {
            List<ContactInfo> allContactsList = new ArrayList<>();

            String[] projection = {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
            };

            Cursor cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0",
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            );

            if (cursor != null) {
                int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                while (cursor.moveToNext()) {
                    long contactId = cursor.getLong(idIndex);
                    String name = cursor.getString(nameIndex);

                    if (name != null && !name.trim().isEmpty()) {
                        Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));

                        // Get phone number
                        String phone = getContactPhone(contactId);

                        // Get email
                        String email = getContactEmail(contactId);

                        ContactInfo contactInfo = new ContactInfo(contactUri, name, phone, email);
                        allContactsList.add(contactInfo);
                    }
                }
                cursor.close();
            }

            // Update UI on main thread
            runOnUiThread(() -> {
                allContacts = allContactsList;
                Log.d("ContactSearch", "Loaded " + allContacts.size() + " contacts for global search");
                loadingIndicator.setVisibility(View.GONE);
                contactList.setVisibility(View.VISIBLE);

                // Filter with current search query
                String currentQuery = searchEditText.getText().toString();
                if (currentQuery != null && !currentQuery.trim().isEmpty()) {
                    fastFilterContacts(currentQuery);
                } else {
                    // If no query, show all loaded contacts
                    filteredContacts = new ArrayList<>(allContacts);
                    if (adapter != null) {
                        adapter.updateContacts(filteredContacts);
                    }
                    updateUI();
                }
            });
        }).start();
    }

    private void loadMoreContacts() {
        if (!isLoading && hasMoreContacts) {
            currentPage++;
            loadContacts();
        }
    }

    private String getContactPhone(long contactId) {
        String phone = null;
        Cursor phoneCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{String.valueOf(contactId)},
                null
        );

        if (phoneCursor != null && phoneCursor.moveToFirst()) {
            int phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            if (phoneIndex >= 0) {
                phone = phoneCursor.getString(phoneIndex);
            }
            phoneCursor.close();
        }

        return phone;
    }

    private String getContactEmail(long contactId) {
        String email = null;
        Cursor emailCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Email.DATA},
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{String.valueOf(contactId)},
                null
        );

        if (emailCursor != null && emailCursor.moveToFirst()) {
            int emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            if (emailIndex >= 0) {
                email = emailCursor.getString(emailIndex);
            }
            emailCursor.close();
        }

        return email;
    }

    private void setupAdapter() {
        adapter = new ContactPickerAdapter(this, new ArrayList<>(), selectedContacts, maxSelection);

        // Apply theme color to adapter
        String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        int color = Color.parseColor(themeColor);
        ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themeColor));

        adapter.setThemeColor(color, tintList);

        adapter.setOnContactSelectedListener(new ContactPickerAdapter.OnContactSelectedListener() {
            @Override
            public void onContactSelected(ContactInfo contactInfo, boolean isSelected) {
                if (isSelected) {
                    if (selectedContacts.size() < maxSelection) {
                        selectedContacts.add(contactInfo);
                    }
                } else {
                    selectedContacts.remove(contactInfo);
                }
                updateUI();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contactList.setLayoutManager(layoutManager);
        contactList.setAdapter(adapter);

        // Add scroll listener for pagination
        contactList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && hasMoreContacts) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadMoreContacts();
                    }
                }
            }
        });
    }

    private void updateUI() {
        int selectedCount = selectedContacts.size();
        counterText.setText(selectedCount + "/" + maxSelection);

        // Update small counter
        if (selectedCount > 0) {
            smallCounterText.setText(String.valueOf(selectedCount));
            smallCounterText.setVisibility(View.VISIBLE);
        } else {
            smallCounterText.setVisibility(View.GONE);
        }

        // Update done button state
        doneButton.setEnabled(selectedCount > 0);
        doneButton.setAlpha(selectedCount > 0 ? 1.0f : 0.5f);

        // Update adapter selection state
        adapter.notifyDataSetChanged();
    }
    
    private void showContactPreviewDialog() {
        if (selectedContacts.isEmpty()) {
            Toast.makeText(this, "No contacts selected for preview", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Log.d("ContactPreview", "Showing contact preview dialog with " + selectedContacts.size() + " contacts");
        
        // Create and show the preview dialog using the same layout as the main app
        Constant.dialogueFullScreen(this, R.layout.dialogue_full_screen_dialogue);
        
        if (Constant.dialogLayoutFullScreen != null) {
            Log.d("ContactPreview", "About to show dialog");
            Constant.dialogLayoutFullScreen.show();
            Log.d("ContactPreview", "Dialog show() called");
            
            // Configure dialog window
            android.view.Window window = Constant.dialogLayoutFullScreen.getWindow();
            if (window != null) {
                Log.d("ContactPreview", "Window found, configuring...");
                androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false);
                android.view.View rootView = window.getDecorView().findViewById(android.R.id.content);
                rootView.setFitsSystemWindows(false);
                
                // Ensure proper window configuration
                window.setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
                
                // Ensure the dialog content is visible
                window.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
                
                Log.d("ContactPreview", "Dialog window configured successfully");
            } else {
                Log.e("ContactPreview", "Window is null!");
            }
            
            // Setup contact preview with a small delay to ensure dialog is fully rendered
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                setupContactPreviewForPicker();
                
                // Set caption in dialogue messageBoxMy (from main contact picker's messageBoxMy)
                android.widget.EditText dialogueMessageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                String mainCaption = "";
                if (messageBoxMy != null) {
                    mainCaption = messageBoxMy.getText().toString().trim();
                }
                Log.d("ContactCaptionDebug", "Main contact picker caption: '" + mainCaption + "'");
                Log.d("ContactCaptionDebug", "dialogue messageBoxMy found: " + (dialogueMessageBoxMy != null));
                if (dialogueMessageBoxMy != null && !mainCaption.isEmpty()) {
                    dialogueMessageBoxMy.setText(mainCaption);
                    Log.d("ContactCaptionDebug", "Caption set in dialogue: '" + dialogueMessageBoxMy.getText().toString() + "'");
                } else {
                    Log.d("ContactCaptionDebug", "Caption not set - dialogue messageBoxMy null: " + (dialogueMessageBoxMy == null) + ", mainCaption empty: " + mainCaption.isEmpty());
                }
            }, 100);
            
            // Setup dismiss listener
            Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
                android.view.Window activityWindow = getWindow();
                androidx.core.view.WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
                android.view.View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
                rootView.setFitsSystemWindows(true);
            });
        } else {
            Log.e("ContactPreview", "Failed to create dialog");
            Toast.makeText(this, "Failed to show preview", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupContactPreviewForPicker() {
        Log.d("ContactPreview", "Setting up contact preview for picker with " + selectedContacts.size() + " contacts");
        
        if (selectedContacts.isEmpty()) {
            Log.d("ContactPreview", "No contacts selected, returning");
            return;
        }
        
        // Hide image and video views
        android.widget.ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        if (singleImageView != null) {
            singleImageView.setVisibility(android.view.View.GONE);
        }

            androidx.viewpager2.widget.ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
            if (viewPager2 != null) {
                viewPager2.setVisibility(android.view.View.GONE);
        }

        // Show contact container with downloadCtrl design (same as document preview)
        android.widget.LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            downloadCtrl.setVisibility(android.view.View.VISIBLE);
            setupContactContainerWithSlider(downloadCtrl);
        }

        // Setup horizontal thumbnails


        // Setup ViewPager indicator
        setupViewPagerIndicator();

        // Setup contact counter at top with delay to ensure layout is ready
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            setupContactCounter();
        }, 200);
        
        // Also try immediate setup as backup
        setupContactCounter();

        // Setup contact information display
        setupContactInfoDisplay();

        // Initialize captions for all contacts
        initializeContactCaptions();
        
        // Setup caption input
        setupContactCaptionInput();

        // Setup send button
        setupSendButtonForContacts();

        // Setup back button
        android.widget.LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.backarrow);
        if (backarrow != null) {
            backarrow.setVisibility(android.view.View.VISIBLE);
            backarrow.setOnClickListener(v -> {
                // Clear selections when going back
                selectedContacts.clear();
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }
    }
    
    private void setupContactContainerWithSlider(android.widget.LinearLayout downloadCtrl) {
        // Clear existing views
        downloadCtrl.removeAllViews();

        // Create ViewPager2 for contact sliding
        androidx.viewpager2.widget.ViewPager2 contactViewPager = new androidx.viewpager2.widget.ViewPager2(this);
        contactViewPager.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    ));

        // Create adapter for contact preview
        ContactPreviewAdapterForPicker adapter = new ContactPreviewAdapterForPicker(this, selectedContacts);
        contactViewPager.setAdapter(adapter);

        // Setup page change callback
        contactViewPager.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("ContactPreview", "Contact page selected: " + (position + 1) + "/" + selectedContacts.size());
                
                // Update counter in upper area
                android.widget.TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
                if (imageCounter != null) {
                    imageCounter.setText((position + 1) + " / " + selectedContacts.size());
                    imageCounter.setTextColor(getResources().getColor(R.color.gray3));
                    imageCounter.setTextSize(16);
                    imageCounter.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
                    Log.d("ContactPreview", "Counter updated to: " + (position + 1) + " / " + selectedContacts.size());
                } else {
                    Log.e("ContactPreview", "imageCounter not found when updating");
                    // Try to find and update custom counter
                    updateCustomCounter(position + 1);
                }
                
                // Keep the main caption unchanged when swiping between contacts
                android.widget.EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                if (messageBoxMy != null) {
                    // Don't change the caption when swiping - keep the main caption
                    Log.d("ContactPageChange", "Switched to contact position " + position + ", keeping main caption unchanged");
                    Log.d("ContactPageChange", "Current main caption: '" + messageBoxMy.getText().toString() + "'");
                    
                    // The caption should remain as the main caption from the contact picker
                    // No need to change it based on individual contact position
                }
            }
        });

        // Add ViewPager2 to container
        downloadCtrl.addView(contactViewPager);
    }
    
    private void setupHorizontalContactThumbnails(RecyclerView horizontalRecyclerView) {
        // Setup horizontal RecyclerView for contact thumbnails
        androidx.recyclerview.widget.LinearLayoutManager layoutManager = new androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(layoutManager);
        
        // Create adapter for horizontal thumbnails
        ContactThumbnailAdapter thumbnailAdapter = new ContactThumbnailAdapter(this, selectedContacts);
        horizontalRecyclerView.setAdapter(thumbnailAdapter);
    }
    
    private void setupViewPagerIndicator() {
        // Setup page indicator dots (if available in layout)
        // This would be similar to document preview indicator setup
        Log.d("ContactPreview", "Setting up view pager indicator");
    }
    
    private void setupContactInfoDisplay() {
        // Setup contact information display
        Log.d("ContactPreview", "Setting up contact info display");
    }
    
    private void initializeContactCaptions() {
        Log.d("ContactPreview", "Initializing captions for " + selectedContacts.size() + " contacts");
        
        // Initialize empty captions for all contacts
        for (int i = 0; i < selectedContacts.size(); i++) {
            if (!contactCaptions.containsKey(i)) {
                contactCaptions.put(i, "");
            }
        }
        
        Log.d("ContactPreview", "Initialized contactCaptions map: " + contactCaptions.toString());
    }
    
    private androidx.viewpager2.widget.ViewPager2 findViewPagerInDownloadCtrl() {
        // Find the ViewPager2 inside downloadCtrl container
        android.widget.LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            for (int i = 0; i < downloadCtrl.getChildCount(); i++) {
                android.view.View child = downloadCtrl.getChildAt(i);
                if (child instanceof androidx.viewpager2.widget.ViewPager2) {
                    Log.d("ContactPreview", "Found ViewPager2 in downloadCtrl at index " + i);
                    return (androidx.viewpager2.widget.ViewPager2) child;
                }
            }
        }
        Log.e("ContactPreview", "ViewPager2 not found in downloadCtrl");
        return null;
    }
    
    private void setupContactCaptionInput() {
        Log.d("ContactPreview", "Setting up contact caption input");
        
        // Find the caption input EditText
        android.widget.EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null) {
            Log.d("ContactPreview", "Found messageBoxMy, setting up caption input");
            messageBoxMy.setVisibility(android.view.View.VISIBLE);
            messageBoxMy.setHint("Add a caption...");
            messageBoxMy.setText("");
            
            // Setup TextWatcher for contact captions (same pattern as document)
            messageBoxMy.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d("ContactTextWatcher", "beforeTextChanged: '" + s + "'");
                }
                
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("ContactTextWatcher", "onTextChanged: '" + s + "'");
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    Log.d("ContactTextWatcher", "afterTextChanged triggered - isUpdatingContactText: " + isUpdatingContactText + ", text: '" + s.toString() + "'");
                    // Only save if we're not programmatically updating the text
                    if (!isUpdatingContactText) {
                        // Get current selected position from ViewPager2
                        androidx.viewpager2.widget.ViewPager2 contactViewPager = findViewPagerInDownloadCtrl();
                        if (contactViewPager != null) {
                            int currentPosition = contactViewPager.getCurrentItem();
                            String caption = s.toString().trim();
                            contactCaptions.put(currentPosition, caption);
                            Log.d("ContactCaptionSave", "Saved caption for contact " + currentPosition + ": '" + caption + "'");
                            Log.d("ContactCaptionSave", "Current contactCaptions map: " + contactCaptions.toString());
                        } else {
                            Log.d("ContactCaptionSave", "contactViewPager is null! This might be why captions aren't working properly.");
                        }
                    } else {
                        Log.d("ContactTextWatcher", "Skipping save because isUpdatingContactText is true");
                    }
                }
            });
            
            // Focus change listener removed - using document pattern with afterTextChanged
            
        } else {
            Log.e("ContactPreview", "messageBoxMy not found in layout");
        }
    }
    
    private void setupSendButtonForContacts() {
        // Setup send button for contacts
        android.widget.ImageView sendButton = Constant.dialogLayoutFullScreen.findViewById(R.id.send);
        if (sendButton != null) {
            sendButton.setVisibility(android.view.View.VISIBLE);
            sendButton.setOnClickListener(v -> {
                // Debug captions before sending
                debugContactCaptions();
                
                // Send selected contacts with captions
                Intent resultIntent = new Intent();
                resultIntent.putParcelableArrayListExtra(EXTRA_SELECTED_CONTACTS, new ArrayList<>(selectedContacts));
                resultIntent.putExtra("contact_captions", new HashMap<>(contactCaptions));
                
                // Add main caption from messageBoxMy
                if (messageBoxMy != null) {
                    String mainCaption = messageBoxMy.getText().toString().trim();
                    if (!mainCaption.isEmpty()) {
                        resultIntent.putExtra(EXTRA_CAPTION, mainCaption);
                        Log.d("ContactCaptionDebug", "Main caption included: '" + mainCaption + "'");
                    }
                }
                
                setResult(Activity.RESULT_OK, resultIntent);
                
                Log.d("ContactPreview", "Sending " + selectedContacts.size() + " contacts with captions");
                
                // Dismiss dialog and finish activity
                if (Constant.dialogLayoutFullScreen != null) {
                    Constant.dialogLayoutFullScreen.dismiss();
                }
                finish();
            });
        }
    }
    
    // Adapter for horizontal contact thumbnails
    private static class ContactThumbnailAdapter extends RecyclerView.Adapter<ContactThumbnailAdapter.ThumbnailViewHolder> {
        private final android.content.Context context;
        private final List<ContactInfo> contacts;
        
        public ContactThumbnailAdapter(android.content.Context context, List<ContactInfo> contacts) {
            this.context = context;
            this.contacts = contacts;
        }
        
        @NonNull
        @Override
        public ThumbnailViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(context).inflate(R.layout.item_contact_preview, parent, false);
            return new ThumbnailViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ThumbnailViewHolder holder, int position) {
            ContactInfo contact = contacts.get(position);
            
            // Set contact name
            holder.contactName.setText(contact.name);
            
            // Set contact phone
            if (contact.phone != null && !contact.phone.isEmpty()) {
                holder.contactPhone.setText(contact.phone);
                holder.contactPhone.setVisibility(android.view.View.VISIBLE);
        } else {
                holder.contactPhone.setVisibility(android.view.View.GONE);
            }
            
            // Set contact email
            if (contact.email != null && !contact.email.isEmpty()) {
                holder.contactEmail.setText(contact.email);
                holder.contactEmail.setVisibility(android.view.View.VISIBLE);
            } else {
                holder.contactEmail.setVisibility(android.view.View.GONE);
            }
            
            // Set contact avatar (first letter of name)
            if (contact.name != null && !contact.name.isEmpty()) {
                String firstLetter = contact.name.substring(0, 1).toUpperCase();
                holder.contactInitial.setText(firstLetter);
            } else {
                holder.contactInitial.setText("?");
            }
            
            // Selection number removed - counter is now shown in upper area
        }
        
        @Override
        public int getItemCount() {
            return contacts.size();
        }
        
        static class ThumbnailViewHolder extends RecyclerView.ViewHolder {
            android.widget.TextView contactName;
            android.widget.TextView contactPhone;
            android.widget.TextView contactEmail;
            android.widget.TextView contactInitial;
            
            ThumbnailViewHolder(@NonNull android.view.View itemView) {
                super(itemView);
                contactName = itemView.findViewById(R.id.contactName);
                contactPhone = itemView.findViewById(R.id.contactPhone);
                contactEmail = itemView.findViewById(R.id.contactEmail);
                contactInitial = itemView.findViewById(R.id.contactInitial);
            }
        }
    }
    
    private void setupContactCounter() {
        Log.d("ContactPreview", "Setting up contact counter with delay...");
        
        // Try multiple ways to find the counter
        android.widget.TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            Log.d("ContactPreview", "Found imageCounter, setting up...");
            imageCounter.setText("1 / " + selectedContacts.size());
            imageCounter.setVisibility(android.view.View.VISIBLE);
            imageCounter.setTextColor(getResources().getColor(R.color.gray3));
            imageCounter.setTextSize(16);
            imageCounter.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
            
            // Force visibility and bring to front
            imageCounter.bringToFront();
            imageCounter.setAlpha(1.0f);
            imageCounter.setBackgroundColor(android.graphics.Color.parseColor("#80000000")); // Semi-transparent black background
            
            Log.d("ContactPreview", "Counter set to: 1 / " + selectedContacts.size());
            Log.d("ContactPreview", "Counter visibility: " + imageCounter.getVisibility());
            Log.d("ContactPreview", "Counter alpha: " + imageCounter.getAlpha());
        } else {
            Log.e("ContactPreview", "imageCounter not found in layout");
            
            // Try to find alternative counter elements
            android.view.ViewGroup rootView = (android.view.ViewGroup) Constant.dialogLayoutFullScreen.findViewById(android.R.id.content);
            if (rootView != null) {
                Log.d("ContactPreview", "Root view found, searching for counter elements...");
                findCounterElements(rootView, 0);
            }
            
            // Create a custom counter if none found
            createCustomCounter();
        }
    }
    
    private void createCustomCounter() {
        Log.d("ContactPreview", "Creating custom counter...");
        
        // Create a custom TextView for the counter
        android.widget.TextView customCounter = new android.widget.TextView(this);
        customCounter.setText("1 / " + selectedContacts.size());
        customCounter.setTextColor(getResources().getColor(R.color.gray3));
        customCounter.setTextSize(16);
        customCounter.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        customCounter.setBackgroundColor(android.graphics.Color.parseColor("#80000000"));
        customCounter.setPadding(16, 8, 16, 8);
        customCounter.setGravity(android.view.Gravity.CENTER);
        
        // Position it at the top center
        android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(
            android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
            android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(android.widget.RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(android.widget.RelativeLayout.CENTER_HORIZONTAL);
        params.topMargin = 20;
        customCounter.setLayoutParams(params);
        
        // Add to the dialog
        android.view.ViewGroup rootView = (android.view.ViewGroup) Constant.dialogLayoutFullScreen.findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.addView(customCounter);
            Log.d("ContactPreview", "Custom counter added to dialog");
        }
    }
    
    private void debugContactCaptions() {
        Log.d("ContactPreview", "=== CONTACT CAPTIONS DEBUG ===");
        for (Map.Entry<Integer, String> entry : contactCaptions.entrySet()) {
            Log.d("ContactPreview", "Contact " + entry.getKey() + ": '" + entry.getValue() + "'");
        }
        Log.d("ContactPreview", "=== END DEBUG ===");
    }
    
    private void startContactUploadProcess() {
        Log.d("ContactUpload", "Starting contact upload process for " + selectedContacts.size() + " contacts");
        
        // This method will be called by the parent activity (chattingScreen or grpChattingScreen)
        // after the contact picker returns the selected contacts and captions
        // The actual Firebase upload will be handled in the parent activity
        
        // For now, just log the process
        for (int i = 0; i < selectedContacts.size(); i++) {
            ContactInfo contact = selectedContacts.get(i);
            String caption = contactCaptions.get(i);
            if (caption == null) caption = "";
            
            Log.d("ContactUpload", "Contact " + i + ": " + contact.name + " - " + contact.phone + " (Caption: '" + caption + "')");
        }
    }
    
    // Method to get contact data for Firebase upload (called by parent activity)
    public static class ContactUploadData {
        public final List<ContactInfo> contacts;
        public final Map<Integer, String> captions;
        
        public ContactUploadData(List<ContactInfo> contacts, Map<Integer, String> captions) {
            this.contacts = contacts;
            this.captions = captions;
        }
    }
    
    // Static method to upload contacts to Firebase (for use in chattingScreen and grpChattingScreen)
    @SuppressWarnings("unchecked")
    public static void uploadContactsToFirebase(android.content.Context context, List<ContactInfo> contacts, 
                                               Map<Integer, String> captions, String senderId, String receiverUid, 
                                               String userFTokenKey, boolean isGroupChat, String grpIdKey) {
        
        Log.d("ContactUpload", "Uploading " + contacts.size() + " contacts to Firebase");
        
        for (int i = 0; i < contacts.size(); i++) {
            ContactInfo contact = contacts.get(i);
            String caption = captions.get(i);
            if (caption == null) caption = "";
            
            // Generate unique model ID for each contact
            String modelId = UUID.randomUUID().toString();
            
            // Create dummy files for contact upload (contacts don't need actual files)
            File globalFile = null;
            File fullImageFile = null;
            
            try {
                if (isGroupChat) {
                    // Use UploadHelper for group chats
                    com.Appzia.enclosure.Utils.BroadcastReiciver.UploadHelper uploadHelper = 
                        new com.Appzia.enclosure.Utils.BroadcastReiciver.UploadHelper(context, globalFile, fullImageFile, senderId);
                    
                    uploadHelper.uploadContent(
                        com.Appzia.enclosure.Utils.Constant.contact, // uploadType
                        null, // uri
                        caption, // captionText
                        modelId, // modelId
                        null, // savedThumbnail
                        null, // fileThumbName
                        null, // fileName
                        contact.name, // contactName
                        contact.phone, // contactPhone
                        null, // audioTime
                        null, // audioName
                        null, // extension
                        "", // imageWidthDp
                        "", // imageHeightDp
                        "", // aspectRatio
                        grpIdKey // grpIdKey
                    );
                    
                    Log.d("ContactUpload", "Uploaded contact to group: " + contact.name + " (Caption: '" + caption + "')");
                    
                } else {
                    // Use UploadChatHelper for individual chats
                    com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelper uploadHelper = 
                        new com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelper(context, globalFile, fullImageFile, senderId, userFTokenKey);
                    
                    uploadHelper.uploadContent(
                        com.Appzia.enclosure.Utils.Constant.contact, // uploadType
                        null, // uri
                        caption, // captionText
                        modelId, // modelId
                        null, // savedThumbnail
                        null, // fileThumbName
                        null, // fileName
                        contact.name, // contactName
                        contact.phone, // contactPhone
                        null, // audioTime
                        null, // audioName
                        null, // extension
                        receiverUid, // receiverUid
                        "", // replyCrtPostion
                        "", // replyKey
                        "", // replyOldData
                        "", // replyType
                        "", // replytextData
                        "", // replydataType
                        "", // replyfilename
                        "", // forwaredKey
                        "", // imageWidthDp
                        "", // imageHeightDp
                        ""  // aspectRatio
            );
                    
                    Log.d("ContactUpload", "Uploaded contact to individual chat: " + contact.name + " (Caption: '" + caption + "')");
                }
                
            } catch (Exception e) {
                Log.e("ContactUpload", "Error uploading contact " + contact.name + ": " + e.getMessage(), e);
            }
        }
        
        Log.d("ContactUpload", "Completed uploading " + contacts.size() + " contacts to Firebase");
    }
    
    // Example usage methods for chattingScreen and grpChattingScreen
    
    /**
     * Example usage in chattingScreen.java:
     * 
     * // In onActivityResult method:
     * if (requestCode == CONTACT_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
     *     ArrayList<ContactInfo> selectedContacts = data.getParcelableArrayListExtra(WhatsAppLikeContactPicker.EXTRA_SELECTED_CONTACTS);
     *     Map<Integer, String> contactCaptions = (Map<Integer, String>) data.getSerializableExtra("contact_captions");
     *     
     *     if (selectedContacts != null && !selectedContacts.isEmpty()) {
     *         // Upload contacts to Firebase
     *         WhatsAppLikeContactPicker.uploadContactsToFirebase(
     *             mContext, 
     *             selectedContacts, 
     *             contactCaptions, 
     *             senderId, 
     *             receiverUid, 
     *             userFTokenKey, 
     *             false, // isGroupChat
     *             null   // grpIdKey (not needed for individual chat)
     *         );
     *     }
     * }
     */
    
    /**
     * Example usage in grpChattingScreen.java:
     * 
     * // In onActivityResult method:
     * if (requestCode == CONTACT_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
     *     ArrayList<ContactInfo> selectedContacts = data.getParcelableArrayListExtra(WhatsAppLikeContactPicker.EXTRA_SELECTED_CONTACTS);
     *     Map<Integer, String> contactCaptions = (Map<Integer, String>) data.getSerializableExtra("contact_captions");
     *     
     *     if (selectedContacts != null && !selectedContacts.isEmpty()) {
     *         // Upload contacts to Firebase
     *         WhatsAppLikeContactPicker.uploadContactsToFirebase(
     *             mContext, 
     *             selectedContacts, 
     *             contactCaptions, 
     *             senderId, 
     *             null,           // receiverUid (not needed for group chat)
     *             null,           // userFTokenKey (not needed for group chat)
     *             true,           // isGroupChat
     *             grpIdKey        // grpIdKey
     *         );
     *     }
     * }
     */
    
    private void updateCustomCounter(int currentPosition) {
        // Try to find custom counter in the root view
        android.view.ViewGroup rootView = (android.view.ViewGroup) Constant.dialogLayoutFullScreen.findViewById(android.R.id.content);
        if (rootView != null) {
            for (int i = 0; i < rootView.getChildCount(); i++) {
                android.view.View child = rootView.getChildAt(i);
                if (child instanceof android.widget.TextView) {
                    android.widget.TextView textView = (android.widget.TextView) child;
                    if (textView.getText().toString().contains(" / ")) {
                        textView.setText(currentPosition + " / " + selectedContacts.size());
                        Log.d("ContactPreview", "Custom counter updated to: " + currentPosition + " / " + selectedContacts.size());
                        return;
                    }
                }
            }
        }
    }
    
    private void findCounterElements(android.view.ViewGroup parent, int depth) {
        if (depth > 5) return; // Prevent infinite recursion
        
        for (int i = 0; i < parent.getChildCount(); i++) {
            android.view.View child = parent.getChildAt(i);
            if (child instanceof android.widget.TextView) {
                android.widget.TextView textView = (android.widget.TextView) child;
                Log.d("ContactPreview", "Found TextView: " + textView.getText() + " at depth " + depth);
                if (textView.getText().toString().contains("/") || textView.getText().toString().matches("\\d+")) {
                    Log.d("ContactPreview", "Potential counter TextView found: " + textView.getText());
                }
            } else if (child instanceof android.view.ViewGroup) {
                findCounterElements((android.view.ViewGroup) child, depth + 1);
            }
        }
    }
    
    private void setupCounterDisplay() {
        Log.d("ContactPreview###", "Setting up counter display for " + selectedContacts.size() + " contacts");
        
        // Find the image counter in the dialog layout
        android.widget.TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            Log.d("ContactPreview###", "Found imageCounter, making it visible");
            imageCounter.setVisibility(android.view.View.VISIBLE);
            imageCounter.setText("1/" + selectedContacts.size());
            imageCounter.setTextColor(getResources().getColor(R.color.gray3));
            imageCounter.setTextSize(16);
            imageCounter.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        } else {
            Log.e("ContactPreview###", "imageCounter not found in dialog layout");
        }
        
        // Setup ViewPager2 page change listener to update counter
        androidx.viewpager2.widget.ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        if (mainImagePreview != null) {
            Log.d("ContactPreview###", "Found mainImagePreview, setting up page change callback");
            mainImagePreview.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    Log.d("ContactPreview###", "Page selected: " + (position + 1) + "/" + selectedContacts.size());
                    if (imageCounter != null) {
                        imageCounter.setText((position + 1) + "/" + selectedContacts.size());
                    }
                }
            });
        } else {
            Log.e("ContactPreview###", "mainImagePreview not found in dialog layout");
        }
        
        // Setup a simple tap to confirm functionality
        if (mainImagePreview != null) {
            mainImagePreview.setOnClickListener(v -> {
                Log.d("ContactPreview###", "Preview tapped, confirming selection");
                // Tap anywhere on the preview to confirm selection
                Intent resultIntent = new Intent();
                resultIntent.putParcelableArrayListExtra(EXTRA_SELECTED_CONTACTS, new ArrayList<>(selectedContacts));
                
                // Add main caption from messageBoxMy
                if (messageBoxMy != null) {
                    String mainCaption = messageBoxMy.getText().toString().trim();
                    if (!mainCaption.isEmpty()) {
                        resultIntent.putExtra(EXTRA_CAPTION, mainCaption);
                        Log.d("ContactCaptionDebug", "Main caption included: '" + mainCaption + "'");
                    }
                }
                
                setResult(Activity.RESULT_OK, resultIntent);
                
                if (Constant.dialogLayoutFullScreen != null) {
                    Constant.dialogLayoutFullScreen.dismiss();
                }
                finish();
            });
        }
    }
    
    // Simple adapter for contact preview in picker
    private static class ContactPreviewAdapterForPicker extends androidx.recyclerview.widget.RecyclerView.Adapter<ContactPreviewAdapterForPicker.ViewHolder> {
        private final android.content.Context context;
        private final List<ContactInfo> contacts;
        
        public ContactPreviewAdapterForPicker(android.content.Context context, List<ContactInfo> contacts) {
            this.context = context;
            this.contacts = contacts;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            android.util.Log.d("ContactPreviewAdapter", "onCreateViewHolder called for viewType: " + viewType);
            android.view.View view = android.view.LayoutInflater.from(context).inflate(R.layout.item_contact_preview, parent, false);
            android.util.Log.d("ContactPreviewAdapter", "View inflated successfully");
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ContactInfo contact = contacts.get(position);
            android.util.Log.d("ContactPreviewAdapter", "Binding contact " + position + ": " + contact.name);
            
            // Set contact name
            holder.contactName.setText(contact.name);
            
            // Set contact phone
            if (contact.phone != null && !contact.phone.isEmpty()) {
                holder.contactPhone.setText(contact.phone);
                holder.contactPhone.setVisibility(android.view.View.VISIBLE);
            } else {
                holder.contactPhone.setVisibility(android.view.View.GONE);
            }
            
            // Set contact email
            if (contact.email != null && !contact.email.isEmpty()) {
                holder.contactEmail.setText(contact.email);
                holder.contactEmail.setVisibility(android.view.View.VISIBLE);
            } else {
                holder.contactEmail.setVisibility(android.view.View.GONE);
            }
            
            // Set contact avatar (first letter of name)
            if (contact.name != null && !contact.name.isEmpty()) {
                String firstLetter = contact.name.substring(0, 1).toUpperCase();
                holder.contactInitial.setText(firstLetter);
            } else {
                holder.contactInitial.setText("?");
            }
            
            // Selection number removed - counter is now shown in upper area
        }
        
        @Override
        public int getItemCount() {
            android.util.Log.d("ContactPreviewAdapter", "getItemCount called, returning: " + contacts.size());
            return contacts.size();
        }
        
        static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            android.widget.TextView contactName;
            android.widget.TextView contactPhone;
            android.widget.TextView contactEmail;
            android.widget.TextView contactInitial;
            
            ViewHolder(@NonNull android.view.View itemView) {
                super(itemView);
                contactName = itemView.findViewById(R.id.contactName);
                contactPhone = itemView.findViewById(R.id.contactPhone);
                contactEmail = itemView.findViewById(R.id.contactEmail);
                contactInitial = itemView.findViewById(R.id.contactInitial);
            }
        }
    }
    
    // Helper method to handle contact picker result in any activity
    public static void handleContactPickerResult(android.content.Context context, int requestCode, int resultCode, 
                                                android.content.Intent data, String senderId, String receiverUid, 
                                                String userFTokenKey, boolean isGroupChat, String grpIdKey) {
        
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) { // Use appropriate request code
            ArrayList<ContactInfo> selectedContacts = data.getParcelableArrayListExtra(EXTRA_SELECTED_CONTACTS);
            @SuppressWarnings("unchecked")
            Map<Integer, String> contactCaptions = (Map<Integer, String>) data.getSerializableExtra("contact_captions");
            
            if (selectedContacts != null && !selectedContacts.isEmpty()) {
                Log.d("ContactPicker", "Received " + selectedContacts.size() + " contacts with captions");
                
                // Upload contacts to Firebase
                uploadContactsToFirebase(
                    context, 
                    selectedContacts, 
                    contactCaptions, 
                    senderId, 
                    receiverUid, 
                    userFTokenKey, 
                    isGroupChat, 
                    grpIdKey
                );
            }
        }
    }
}

