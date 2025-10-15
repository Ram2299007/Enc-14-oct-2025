package com.Appzia.enclosure.Screens;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Utils.SharedElementBaseActivity;
import com.Appzia.enclosure.Utils.GlobalPermissionPopup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.WindowCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.DefaultTimeBar;
import androidx.media3.ui.PlayerView;
import androidx.media3.ui.TimeBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.Data;

import com.bumptech.glide.Glide;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.ImageAdapter;
import com.Appzia.enclosure.Adapter.ImageAdapter2;
import com.Appzia.enclosure.Adapter.emojiAdapterGrp;
import com.Appzia.enclosure.Adapter.groupChatAdapter;
import com.Appzia.enclosure.Adapter.MultiImagePreviewAdapter;
import com.Appzia.enclosure.Adapter.HorizontalImageAdapter;
import com.Appzia.enclosure.Adapter.MainImagePreviewAdapter;
import com.Appzia.enclosure.Adapter.GroupContactPreviewAdapter;
import com.Appzia.enclosure.Adapter.GroupHorizontalContactThumbnailAdapter;
import com.Appzia.enclosure.Adapter.DocumentDownloadPreviewAdapter;
import com.Appzia.enclosure.Adapters.VideoPreviewAdapter;
import com.Appzia.enclosure.Adapters.MainVideoPreviewAdapter;
import com.Appzia.enclosure.Fragments.CameraGalleryFragment;
import com.Appzia.enclosure.Fragments.CameraGalleryFragmentForGroup;
import com.Appzia.enclosure.Model.Emoji;
import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.group_messageModel;
import com.Appzia.enclosure.Model.group_messageModel2;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.messagemodel2;
import com.Appzia.enclosure.Model.selectionBunchModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.AutoScrollLinearLayoutManager;
import com.Appzia.enclosure.Utils.BroadcastReiciver.GroupMessageUploadService;

import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelper;
import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadHelper;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.PendingMessageUploader;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.StoragePathHelper;
import com.Appzia.enclosure.Utils.WhatsAppLikeImagePicker;
import com.Appzia.enclosure.Utils.WhatsAppLikeContactPicker;
import com.Appzia.enclosure.Utils.WhatsAppLikeVideoPicker;
import com.Appzia.enclosure.Utils.FileUtils;
import com.Appzia.enclosure.Utils.MediaPlayerManager;

import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityGrpChattingScreenBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.crypto.SecretKey;


public class grpChattingScreen extends SharedElementBaseActivity implements ConnectivityReceiver.ConnectivityListener {
    ActivityGrpChattingScreenBinding binding;
    private Chronometer chronometer;
    private static final int PAGE_SIZE = 20;
    private TextView elapsedTimeTextView;
    Handler handler = new Handler();
    Runnable updateProgressAction;
    private int resizeModeIndex = 0; // 0: FIT, 1: FILL, 2: ZOOM
    private boolean wasPlaying = false;
    private SecretKey key;
    public static LinearProgressIndicator progressbar;
    private CountDownTimer countDownTimer;
    boolean lastMessageVisible = true;
    public static groupChatAdapter groupChatAdapter;
    private BroadcastReceiver messageUploadReceiver;
    String previousText = "";
    private Set<String> uniqueDates = new HashSet<>();
    public static String docName;
    public static String userFTokenKey;
    private String currentCaption = ""; // Single caption for all media items
    ArrayList<group_messageModel> groupMessageList = new ArrayList<>();
    public static File globalFile = null;
    public static File FullImageFile = null;

    // Flag to prevent infinite loop in TextWatcher synchronization
    private boolean isUpdatingText = false;
    public static File micPhotoFile;
    public static EditText messageBox;
    AppCompatActivity activity;
    public static Animation ab, a;
    int PICK_IMAGE_REQUEST_CODE = 1;
    int PICK_VIDEO_REQUEST_CODE = 2;
    MediaRecorder recorder = new MediaRecorder();
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    String currentPhotoPath;
    private boolean isLastItemVisible = true;
    private static final String TAG = "XGXGXGXGXGXGXGXGX";

    // Helper method to check if user is within the last 3 messages
    private boolean isWithinLastThreeMessages() {
        if (groupMessageList == null || groupMessageList.isEmpty()) {
            Log.d("KEYBOARD_SCROLL_GROUP", "isWithinLastThreeMessages: groupMessageList is null or empty");
            return false;
        }

        LinearLayoutManager layoutManager = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
        if (layoutManager == null) {
            Log.d("KEYBOARD_SCROLL_GROUP", "isWithinLastThreeMessages: layoutManager is null");
            return false;
        }

        int totalItems = groupMessageList.size();
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

        Log.d("KEYBOARD_SCROLL_GROUP", "isWithinLastThreeMessages: totalItems=" + totalItems + ", lastVisiblePosition=" + lastVisiblePosition);

        // Check if user is within the last 3 messages
        boolean result = lastVisiblePosition >= totalItems - 3;
        Log.d("KEYBOARD_SCROLL_GROUP", "isWithinLastThreeMessages result: " + result);
        return result;
    }

    // Handle keyboard scroll logic (extracted for reuse)
    private void handleKeyboardScrollGroup() {
        boolean isLastVisible = isLastItemVisible;
        boolean isWithinLastThree = isWithinLastThreeMessages();

        Log.d("KEYBOARD_SCROLL_GROUP", "isLastItemVisible: " + isLastVisible);
        Log.d("KEYBOARD_SCROLL_GROUP", "isWithinLastThreeMessages: " + isWithinLastThree);
        Log.d("KEYBOARD_SCROLL_GROUP", "groupMessageList size: " + (groupMessageList != null ? groupMessageList.size() : "null"));

        // Check if user is within the last 3 messages
        if (isLastVisible || isWithinLastThree) {
            Log.d("KEYBOARD_SCROLL_GROUP", "Auto-scrolling to last message");
            // Disable manual scrolling temporarily to prevent interference
            binding.messageRecView.setNestedScrollingEnabled(false);
            binding.messageRecView.setScrollContainer(false);

            // Scroll to last message when keyboard opens and user is near bottom
            scrollToLastMessageWithRetryGroup(0);

            // Re-enable scrolling after keyboard settles
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("KEYBOARD_SCROLL_GROUP", "Re-enabling manual scrolling");
                    binding.messageRecView.setNestedScrollingEnabled(true);
                    binding.messageRecView.setScrollContainer(true);
                }
            }, 2000); // 2 seconds delay
        } else {
            Log.d("KEYBOARD_SCROLL_GROUP", "Not scrolling - user not within last 3 messages");
        }
    }

    // Fast scroll method - immediate execution with minimal delays
    private void scrollToLastMessageWithRetryGroup(int attempt) {
        Log.d("KEYBOARD_SCROLL_GROUP", "=== scrollToLastMessageWithRetryGroup START ===");
        Log.d("KEYBOARD_SCROLL_GROUP", "attempt: " + attempt);

        if (groupMessageList == null || groupMessageList.isEmpty()) {
            Log.d("KEYBOARD_SCROLL_GROUP", "Cannot scroll - groupMessageList is null or empty");
            return;
        }

        final int maxAttempts = 3;
        final int targetPosition = groupMessageList.size() - 1;

        Log.d("KEYBOARD_SCROLL_GROUP", "scrollToLastMessageWithRetryGroup attempt: " + attempt + "/" + maxAttempts);
        Log.d("KEYBOARD_SCROLL_GROUP", "targetPosition: " + targetPosition);

        // Minimal delay - much faster
        int delay = 50 + (attempt * 25); // 50ms, 75ms, 100ms
        Log.d("KEYBOARD_SCROLL_GROUP", "delay: " + delay + "ms");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("KEYBOARD_SCROLL_GROUP", "=== EXECUTING SCROLL ===");
                Log.d("KEYBOARD_SCROLL_GROUP", "Executing scroll to position: " + targetPosition + " (attempt " + (attempt + 1) + ")");

                // Check current position before scroll
                LinearLayoutManager layoutManager = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
                if (layoutManager != null) {
                    int beforeScroll = layoutManager.findLastVisibleItemPosition();
                    Log.d("KEYBOARD_SCROLL_GROUP", "Before scroll - lastVisible: " + beforeScroll);
                }

                // Use immediate scroll instead of smooth scroll to avoid keyboard interference
                binding.messageRecView.scrollToPosition(targetPosition);
                Log.d("KEYBOARD_SCROLL_GROUP", "scrollToPosition called (immediate)");

                // Check if we need to retry after a very short delay
                if (attempt < maxAttempts - 1) {
                    Log.d("KEYBOARD_SCROLL_GROUP", "Setting up retry check in 100ms");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("KEYBOARD_SCROLL_GROUP", "=== CHECKING SCROLL RESULT ===");
                            LinearLayoutManager layoutManager = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
                            if (layoutManager != null) {
                                int currentLastVisible = layoutManager.findLastVisibleItemPosition();
                                Log.d("KEYBOARD_SCROLL_GROUP", "After scroll - currentLastVisible: " + currentLastVisible + ", target: " + targetPosition);

                                // If we're not at the target, retry
                                if (currentLastVisible < targetPosition) {
                                    Log.d("KEYBOARD_SCROLL_GROUP", "RETRY NEEDED - not at target position");
                                    Log.d("KEYBOARD_SCROLL_GROUP", "Calling scrollToLastMessageWithRetryGroup(" + (attempt + 1) + ")");
                                    scrollToLastMessageWithRetryGroup(attempt + 1);
                                } else {
                                    Log.d("KEYBOARD_SCROLL_GROUP", "SUCCESS - reached target position");
                                    // Final force scroll with minimal delay
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("KEYBOARD_SCROLL_GROUP", "Final force scroll to ensure position");
                                            binding.messageRecView.scrollToPosition(targetPosition);
                                        }
                                    }, 100); // Much faster - 100ms instead of 500ms
                                }
                            } else {
                                Log.d("KEYBOARD_SCROLL_GROUP", "ERROR - layoutManager is null during retry check");
                            }
                        }
                    }, 100); // Much faster check - 100ms instead of 300ms
                } else {
                    Log.d("KEYBOARD_SCROLL_GROUP", "No more retries - this was the final attempt");
                }
            }
        }, delay);

        Log.d("KEYBOARD_SCROLL_GROUP", "=== scrollToLastMessageWithRetryGroup END ===");
    }

    int PICKFILE_REQUEST_CODE = 3;
    int REQUEST_CODE_PICK_CONTACT = 4;
    int PICK_DOCUMENT_REQUEST_CODE = 5;
    int PICK_CONTACT_MULTI_REQUEST_CODE = 6;
    int PICK_CONTACT_REQUEST_CODE = 7;
    ColorStateList tintList;
    String themColor;
    public static String finalName;
    public static ExoPlayer playerPreview2;
    MediaPlayer player;
    String profilepic;

    String path;
    LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private String lastKey = null;
    private boolean hasScrolledToLast = false;
    public static String modelId;
    BottomSheetDialog bottomSheetDialog, bottomSheetDialogRec, bottomSheetDialogData;
    public static FirebaseDatabase database;
    AutoScrollLinearLayoutManager autoScrollLinearLayoutManager;

    ArrayList<group_messageModel> messageModels = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private StorageReference reference = FirebaseStorage.getInstance().getReference("group_chats_storage");
    private Uri GlobalUri;
    private ArrayList<Uri> selectedImageUris = new ArrayList<>();
    private ArrayList<File> selectedImageFiles = new ArrayList<>();
    private ArrayList<File> selectedFullImageFiles = new ArrayList<>();
    private ArrayList<String> imagePaths = new ArrayList<>();

    // Video multi-selection variables
    private ArrayList<Uri> selectedVideoUris = new ArrayList<>();
    private ArrayList<File> selectedVideoFiles = new ArrayList<>();

    // Document multi-selection variables
    private ArrayList<Uri> selectedDocumentUris = new ArrayList<>();
    private ArrayList<File> selectedDocumentFiles = new ArrayList<>();

    // Contact multi-selection variables
    private ArrayList<Uri> selectedContactUris = new ArrayList<>();
    private ArrayList<ContactInfo> selectedContactInfos = new ArrayList<>();

    // Contact info class
    public static class ContactInfo {
        public String name;
        public String phoneNumber;
        public String email;

        public ContactInfo(String name, String phoneNumber, String email) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.email = email;
        }
    }

    StorageTask mStoragetask;


    public static String name, caption, photo;
    public static String fontSizePref;

    public static Context mContext;

    public static File mFilePath;
    String createdBy;


    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    public static Chronometer chronometerBtm;
    String time;

    private ConnectivityManager.NetworkCallback networkCallback;
    LinearProgressIndicator networkLoader;
    private ConnectivityReceiver connectivityReceiver;
    private PendingMessageUploader pendingMessageUploader;
    RecyclerView emojiRecyclerview;
    emojiAdapterGrp adapter;

    @Override
    public void onConnectivityChanged(boolean isConnected) {

        if (isConnected) {
            binding.networkLoader.setVisibility(View.GONE);

            // Upload pending group messages when internet is restored
            if (pendingMessageUploader != null && modelId != null && userFTokenKey != null) {
                Log.d("PendingUpload", "Internet restored, checking for pending group messages for group: " + modelId);
                pendingMessageUploader.uploadPendingGroupMessages(modelId, userFTokenKey);
            }
        } else {
            binding.networkLoader.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Constant.getSfFuncion(mContext);
            profilepic = Constant.getSF.getString(Constant.profilePic, "");
        } catch (Exception ignored) {
        }


        try {

            name = getIntent().getStringExtra("nameKey");
            caption = getIntent().getStringExtra("captionKey");
            photo = getIntent().getStringExtra("photoKey");
            createdBy = getIntent().getStringExtra("createdBy");


            binding.name.setText(name);

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

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.messageBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.messageBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


            } else if (fontSizePref.equals(Constant.large)) {

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.messageBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);

            }
        } catch (Exception ignored) {

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        loadImages();


        Webservice.get_emojiGrp(mContext, grpChattingScreen.this);

        Constant.getSfFuncion(mContext);
        String height = Constant.getSF.getString("keyboardHeightKey", "");
        RecyclerView emojiRecyclerview = findViewById(R.id.emojiRecyclerview);
        try {
            ViewGroup.LayoutParams params = emojiRecyclerview.getLayoutParams();
            params.height = Integer.parseInt(height);
            emojiRecyclerview.setLayoutParams(params);
        } catch (NumberFormatException e) {

        }

        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

                // Set light status bar (white text/icons) for dark mode
                getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
            }
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

                // Set dark status bar (black text/icons) for light mode
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        modelId = database.getReference().push().getKey();
        modelId = database.getReference().push().getKey();
        Log.d("XYZ", "onResume: ");
        networkLoader = findViewById(R.id.networkLoader);

        // Create a network callback
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                // Network is available

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        networkLoader.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                // Network is lost

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        networkLoader.setVisibility(View.GONE);
                    }
                });

            }
        };

        // Register the network callback
        registerNetworkCallback();

        //   chatAdapter.clear();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrpChattingScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Immediately hide progress bar to prevent any flash
        if (binding.progressBar != null) {
            binding.progressBar.setVisibility(View.GONE);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        mContext = binding.getRoot().getContext();

        // Initialize userFTokenKey
        userFTokenKey = getIntent().getStringExtra("userFTokenKey");

        chronometer = findViewById(R.id.chronometer);
        elapsedTimeTextView = findViewById(R.id.elapsed_time_textview);
        binding.messageRecView.setNestedScrollingEnabled(false);


        emojiRecyclerview = findViewById(R.id.emojiRecyclerview);


        Intent receivedIntent = getIntent();
        Bundle receivedBundle = receivedIntent.getExtras();


        activity = grpChattingScreen.this;
        Constant.getSfFuncion(mContext);

        messageUploadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract modelId from intent to stop progress indicator
                String modelId = intent.getStringExtra("modelId");
                if (modelId != null) {
                    // Stop the progress indicator for this specific message
                    stopMessageProgress(modelId);
                }

                // Update UI on the main thread
                ((Activity) mContext).runOnUiThread(() -> {
                    // Stop loader for last item once upload completes, same as single chat
                    groupChatAdapter.setLastItemVisible(false);
                    // Optionally keep the list pinned to last
                    // binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                });
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messageUploadReceiver, new IntentFilter("GROUP_MESSAGE_UPLOADED"));

        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, mContext.RECEIVER_EXPORTED);

        // Initialize pending message uploader
        pendingMessageUploader = new PendingMessageUploader(this);


        database = FirebaseDatabase.getInstance();
        messageBox = findViewById(R.id.messageBox);

        // Setup BottomSheetDialog for dataCardview - positioned above bottom navigation
        setupDataCardviewBottomSheet();

        bottomSheetDialog = new BottomSheetDialog(grpChattingScreen.this, R.style.BottomSheetDialog);
        View viewShape = getLayoutInflater().inflate(R.layout.bottom_sheet_layout_pick, null, false);
        bottomSheetDialog.setContentView(viewShape);

        Constant.getSfFuncion(getApplicationContext());
        final String grpIdKey = getIntent().getStringExtra("grpIdKey");
        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
        final String senderRoom = senderId + grpIdKey;
        final String receiverRoom = grpIdKey + senderId;
        Log.d("senderRoom", senderRoom + receiverRoom);


        // todo Initialize message list if it's null
        if (groupMessageList == null) {
            groupMessageList = new ArrayList<>();
        }

        // todo Set up RecyclerView and Adapter
        setupRecyclerViewAndAdapter();


        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keyboardHeight = screenHeight - r.bottom;

            if (keyboardHeight > screenHeight / 3) {
                Log.d("addOnGlobalLayoutListener", "visible: ");

                if (lastMessageVisible) {
                    RecyclerView.LayoutManager layoutManager = binding.messageRecView.getLayoutManager();
                    if (layoutManager != null) {
                        int lastPosition = layoutManager.getItemCount() - 1;
                        binding.messageRecView.scrollToPosition(lastPosition);
                    }
                    lastMessageVisible = false;

                }

            } else {
                Log.d("addOnGlobalLayoutListener", "Hidden: ");

                //    binding.messageBox.clearFocus();

            }
        });


        // Initialize group chat data with proper caching
        initializeGroupChatData(senderRoom);


        try {
            database.getReference().child(Constant.GROUPCHAT).child(senderRoom).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("FirebaseListener", "onDataChange called");
                    messageModel model = snapshot.getValue(messageModel.class);
                    if (model != null) {
                        runOnUiThread(() -> {
                            Log.d("FirebaseListener", "Setting View.GONE");
                            binding.valuable.setVisibility(View.GONE);
                        });
                    } else {
                        runOnUiThread(() -> {
                            Log.d("FirebaseListener", "Setting View.VISIBLE");
                            binding.progressBar.setVisibility(View.GONE);
                            binding.valuable.setVisibility(View.VISIBLE);

                        });
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {

            throw new RuntimeException(e);

        }


        //TODO :- senderId + grpIdKey ==senderRoom


//        LinearLayoutCompat cameraLyt = viewShape.findViewById(R.id.cameraLyt);
//        LinearLayoutCompat galleryLyt = viewShape.findViewById(R.id.galleryLyt);
//        LinearLayoutCompat documentLyt = viewShape.findViewById(R.id.documentLyt);
//        LinearLayoutCompat contactLyt = viewShape.findViewById(R.id.contactLyt);
//        LinearLayoutCompat videoLyt = viewShape.findViewById(R.id.videoLyt);

        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            binding.menuPoint.setColorFilter(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.pinklogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FF6D00"));

                } else if (themColor.equals("#00A3E9")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.ec_modern);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                } else if (themColor.equals("#7adf2a")) {
//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.popatilogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00C853"));


                } else if (themColor.equals("#ec0001")) {


//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.redlogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#ec7500"));


                } else if (themColor.equals("#16f3ff")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.bluelogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00F365"));

                } else if (themColor.equals("#FF8A00")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.orangelogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FFAB00"));

                } else if (themColor.equals("#7F7F7F")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.graylogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#314E6D"));

                } else if (themColor.equals("#D9B845")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.yellowlogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#b0d945"));
                } else if (themColor.equals("#346667")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.greenlogoppng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#729412"));

                } else if (themColor.equals("#9846D9")) {

                    binding.menu.setImageResource(R.drawable.voiletlogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#d946d1"));

                } else if (themColor.equals("#A81010")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.red2logopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#D85D01"));

                } else {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.ec_modern);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));

                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }


        binding.cameraLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");

                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                hideGalleryRecentView();
                bottomSheetDialog.dismiss();
                askCameraPermissions();
            }
        });

        binding.galleryLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }

                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");

                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                hideGalleryRecentView();
                // Use WhatsApp-like image picker
                Intent pickerIntent = new Intent(mContext, WhatsAppLikeImagePicker.class);
                pickerIntent.putExtra(WhatsAppLikeImagePicker.EXTRA_MAX_SELECTION, 30 - selectedImageUris.size());
                pickerIntent.putParcelableArrayListExtra(WhatsAppLikeImagePicker.EXTRA_SELECTED_IMAGES, new ArrayList<>(selectedImageUris));
                SwipeNavigationHelper.startActivityForResultWithSwipe(grpChattingScreen.this, pickerIntent, PICK_IMAGE_REQUEST_CODE);
                bottomSheetDialog.dismiss();
            }
        });

        binding.documentLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }

                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");

                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                hideGalleryRecentView();

                // Use system document picker for multi-selection
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                SwipeNavigationHelper.startActivityForResultWithSwipe(grpChattingScreen.this, Intent.createChooser(intent, "Select Documents"), PICK_DOCUMENT_REQUEST_CODE);
                bottomSheetDialog.dismiss();
            }
        });


        binding.videoLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }

                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");

                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                hideGalleryRecentView();

                // Use new permission flow for video picker
                GlobalPermissionPopup.handleGalleryClickWithLimitedAccess(grpChattingScreen.this, new GlobalPermissionPopup.PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {
                        // Use WhatsApp-like video picker for multi-selection
                        Intent pickerIntent = new Intent(mContext, WhatsAppLikeVideoPicker.class);
                        pickerIntent.putExtra(WhatsAppLikeVideoPicker.EXTRA_MAX_SELECTION, 5 - selectedVideoUris.size());
                        pickerIntent.putParcelableArrayListExtra(WhatsAppLikeVideoPicker.EXTRA_SELECTED_VIDEOS, new ArrayList<>(selectedVideoUris));
                        SwipeNavigationHelper.startActivityForResultWithSwipe(grpChattingScreen.this, pickerIntent, PICK_VIDEO_REQUEST_CODE);
                    }

                    @Override
                    public void onPermissionDenied() {
                        // Handle permission denied
                        Log.d(TAG, "Video permission denied");
                    }

                    @Override
                    public void onSettingsClicked() {
                        // Handle settings clicked
                        Log.d(TAG, "Settings clicked for video permission");
                    }
                });

                bottomSheetDialog.dismiss();
            }
        });


        binding.contactLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }


                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");

                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                hideGalleryRecentView();

                // Use custom contact picker for multiple selection
                Intent intent = new Intent(mContext, WhatsAppLikeContactPicker.class);
                intent.putExtra(WhatsAppLikeContactPicker.EXTRA_MAX_SELECTION, 50);
                intent.putParcelableArrayListExtra(WhatsAppLikeContactPicker.EXTRA_SELECTED_CONTACTS, new ArrayList<WhatsAppLikeContactPicker.ContactInfo>());
                SwipeNavigationHelper.startActivityForResultWithSwipe(grpChattingScreen.this, intent, PICK_CONTACT_MULTI_REQUEST_CODE);
                bottomSheetDialog.dismiss();
            }
        });

        Constant.dialogueLayoutSearchChatt(mContext, R.layout.search_layout);
        Dialog search_layout = Constant.dialogLayoutColor;
        TextView search = search_layout.findViewById(R.id.search);
        TextView viewProfile = search_layout.findViewById(R.id.viewProfile);
        TextView clearChat = search_layout.findViewById(R.id.clearChat);
        clearChat.setVisibility(View.GONE);

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), forGroupVisible.class);
                intent.putExtra("group_id", grpIdKey);
                SwipeNavigationHelper.startActivityWithSwipe(grpChattingScreen.this, intent);
                search_layout.dismiss();
            }
        });

        binding.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), forGroupVisible.class);
                intent.putExtra("group_id", grpIdKey);
                SwipeNavigationHelper.startActivityWithSwipe(grpChattingScreen.this, intent);
            }
        });

        binding.menunew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_layout.show();


            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_layout.dismiss();
                binding.name.setVisibility(View.GONE);
                binding.menu2.setVisibility(View.GONE);
                binding.searchEt.setVisibility(View.VISIBLE);

                binding.searchEt.requestFocus();
            }
        });
        binding.searchEt.addTextChangedListener(new TextWatcher() {
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

        //    Toast.makeText(this, grpIdKey, Toast.LENGTH_SHORT).show();

        // setSenderAdapter();


        // for fetching received chatt value from firebase (Fire base is a realtime data snap support)
        Constant.getSfFuncion(getApplicationContext());
        Log.d("senderRoom", senderRoom + receiverRoom);

        //TODO :- senderId + grpIdKey ==senderRoom


        binding.sendGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DIALOGUE_DEBUG", "=== SEND BUTTON CLICKED ===");
                Log.d("DIALOGUE_DEBUG", "Send button clicked for multi-images!");

                if (Integer.parseInt(binding.multiSelectSmallCounterText.getText().toString()) > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Constant.Vibrator(mContext);
                    }

                    // Show dialogue full screen for multi-select images
                    Log.d("MultiImagePreview", "Setting up preview with selected images");
                    Log.d("MultiImagePreview", "SelectedImageUris size: " + selectedImageUris.size());
                    Log.d("MultiImagePreview", "SelectedImageFiles size: " + selectedImageFiles.size());

                    // Check if dialogue is already showing
                    if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                        Log.d("MultiImagePreview", "Dialog already showing, dismissing it");
                        try {
                            Constant.dialogLayoutFullScreen.dismiss();
                            Log.d("DIALOGUE_DEBUG", "Dialogue dismissed from sendGrp onClickListener");
                        } catch (Exception e) {
                            Log.e("DIALOGUE_DEBUG", "Error dismissing dialogue: " + e.getMessage());
                        }
                        return; // Don't create a new dialogue
                    }

                    // Create dialogue using Constant method (same design)
                    Log.d("MultiImagePreview", "Creating dialogue using Constant method...");
                    Log.d("VIDEO_DIALOG_SHOW", "grpChattingScreen.showMultiImagePreviewDialog called");
                    Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);

                    // Show the dialogue directly here
                    Log.d("MultiImagePreview", "Dialog created, showing...");
                    Log.d("VIDEO_DIALOG_SHOW", "About to show dialog");
                    Constant.dialogLayoutFullScreen.show();
                    Log.d("MultiImagePreview", "Dialog shown successfully");

                    // Window setup करा (Setup window)
                    android.view.Window window = Constant.dialogLayoutFullScreen.getWindow();
                    if (window != null) {
                        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false);
                        View rootView = window.getDecorView().findViewById(android.R.id.content);
                        rootView.setFitsSystemWindows(false);
                        window.setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }

                    Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
                        Log.d("DIALOGUE_DEBUG", "=== DIALOGUE DISMISS LISTENER TRIGGERED ===");
                        Log.d("DIALOGUE_DEBUG", "Dismiss listener called - dialogue is being dismissed");
                        Log.d("DIALOGUE_DEBUG", "Current time: " + System.currentTimeMillis());

                        android.app.Activity activity = (android.app.Activity) mContext;
                        android.view.Window activityWindow = activity.getWindow();
                        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
                        View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
                        rootView.setFitsSystemWindows(true);
                        Log.d("DIALOGUE_DEBUG", "Window setup completed");

                        // Reset UI state when dialogue is dismissed
                        Log.d("DIALOGUE_DEBUG", "Starting UI reset...");
                        Log.d("DIALOGUE_DEBUG", "Calling loadImages()");

                        Log.d("DIALOGUE_DEBUG", "UI reset completed successfully");

                        Log.d("DIALOGUE_DEBUG", "=== DIALOGUE DISMISS LISTENER COMPLETED ===");
                    });

                    // Setup multi-image preview with selected data
                    setupMultiImagePreviewWithData();

                } else {
                    modelId = database.getReference().push().getKey();
                    String message = binding.messageBox.getText().toString().trim();
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    String currentDateTimeString = sdf.format(d);

                    if (binding.messageBox.getText().toString().trim().isEmpty()) {

                    } else {

                        // Create group_messageModel as in your original logic
                        final group_messageModel model;
                        try {
                            model = new group_messageModel(senderId, message, currentDateTimeString, "", Constant.Text, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", "", "", "", "", Constant.getCurrentDate(), "", "", "", "1", null);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }


                        name = getIntent().getStringExtra("nameKey");
                        final messageModel modelnew;
                        try {
                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                            emojiModels.add(new emojiModel("", ""));
                            modelnew = new messageModel(senderId, message, currentDateTimeString, "", Constant.Text, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverRoom, Constant.groupKey, name, "", "", "", "", "", 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        // Add to groupMessageList as in your original logic
                        try {
                            String uniqDate = model.getCurrentDate();
                            if (uniqueDates.add(uniqDate)) {
                                groupMessageList.add(new group_messageModel(model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMiceTiming(), model.getMicPhoto(), model.getCreatedBy(), model.getUserName(), model.getModelId(), model.getReceiverUid(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), uniqDate, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()));
                            } else {
                                groupMessageList.add(new group_messageModel(model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMiceTiming(), model.getMicPhoto(), model.getCreatedBy(), model.getUserName(), model.getModelId(), model.getReceiverUid(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), ":" + uniqDate, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()));
                            }

                            Constant.getSfFuncion(getApplicationContext());
                            final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                            final String senderRoom = senderId + grpIdKey;


                            group_messageModel2 model2 = new group_messageModel2(
                                    model.getUid(),
                                    model.getMessage(),
                                    model.getTime(),
                                    model.getDocument(),
                                    model.getDataType(),
                                    model.getExtension(),
                                    model.getName(),
                                    model.getPhone(),
                                    model.getMicPhoto(),
                                    model.getMiceTiming(),
                                    model.getCreatedBy(),
                                    model.getUserName(),
                                    model.getModelId(),
                                    model.getReceiverUid(),
                                    model.getDocSize(),
                                    model.getFileName(),
                                    model.getThumbnail(),
                                    model.getFileNameThumbnail(),
                                    model.getCaption(),
                                    model.getCurrentDate(),
                                    senderRoom,
                                    0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount() // active: 0 = still loading
                            );

                            // TODO: active: 0 = still loading
                            // TODO: active: 1 = completed


                            // Store message in SQLite pending table before upload
                            try {
                                new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(mContext).insertPendingGroupMessage(model);
                                Log.d("PendingMessages", "Group text message stored in pending table: " + model.getModelId());
                            } catch (Exception e) {
                                Log.e("PendingMessages", "Error storing group text message in pending table: " + e.getMessage(), e);
                            }

                            runOnUiThread(() -> {
                                groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                                groupChatAdapter.setLastItemVisible(true); // Show progress for pending message
                                binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                            });

                            UploadHelper uploadHelper = new UploadHelper(mContext, null, null, createdBy);
//
//                        // Use UploadHelper to handle the upload
                            uploadHelper.uploadContent(
                                    Constant.Text, // uploadType
                                    null, // uri (null for text)
                                    message, // captionText (using message as caption)
                                    modelId, // modelId
                                    null, // savedThumbnail
                                    null, // fileThumbName
                                    null, // fileName
                                    null, // contactName
                                    null, // contactPhone
                                    null, // audioTime
                                    null, // audioName
                                    null, // extension
                                    "", "", ""
                            );

                        } catch (Exception ignored) {
                            // Keep your original exception handling
                        }
                    }
                    binding.messageBox.setText("");


                    runOnUiThread(() -> {
                        if (binding.emojiLyt.getVisibility() == View.VISIBLE) {
                            // Toast.makeText(mContext, "visible", Toast.LENGTH_SHORT).show();

                            binding.messageBox.requestFocus();
                            if (binding.emojiRecyclerview.getVisibility() == View.VISIBLE) {
                                binding.messageBox.requestFocus(); // Ensure the EditText gains focus
                                binding.emojiRecyclerview.animate()
                                        .translationY(binding.emojiRecyclerview.getHeight()) // Move it downwards
                                        .setDuration(0) // Animation duration
                                        .withEndAction(() -> binding.emojiRecyclerview.setVisibility(View.GONE)) // Set to GONE after animation ends
                                        .start();

                                binding.emojiLyt.animate()
                                        .translationY(binding.emojiLyt.getHeight()) // Move it downwards
                                        .setDuration(0) // Animation duration
                                        .withEndAction(() -> binding.emojiLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                                        .start();

                                binding.emojiSearchContainerTop.setVisibility(View.GONE);
                                binding.emojiSearchContainerBottom.setVisibility(View.GONE);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.showSoftInput(binding.messageBox, InputMethodManager.SHOW_IMPLICIT);
                                }
                                binding.bitemoji.setImageResource(R.drawable.emojisvg);
                            }

                            if (bottomSheetDialogData != null && bottomSheetDialogData.isShowing()) {
                                binding.messageBox.requestFocus(); // Ensure the EditText gains focus
                                hideDataBottomSheet();

                                binding.emojiLyt.animate()
                                        .translationY(binding.emojiLyt.getHeight()) // Move it downwards
                                        .setDuration(0) // Animation duration
                                        .withEndAction(() -> binding.emojiLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                                        .start();

                                binding.emojiSearchContainerTop.setVisibility(View.GONE);
                                binding.emojiSearchContainerBottom.setVisibility(View.GONE);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.showSoftInput(binding.messageBox, InputMethodManager.SHOW_IMPLICIT);
                                }

                            }
                            binding.messageBox.requestFocus();
                        }
                    });
                }
            }
        });


        binding.gallary.setOnClickListener(v -> {
            Log.d("GalleryDebug", "=== GALLERY BUTTON CLICKED ===");
            // Vibrate on click for haptic feedback (WhatsApp-like)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(mContext);
            }
            
            // Check photo permission before proceeding
            GlobalPermissionPopup.handleGalleryClickWithLimitedAccess(grpChattingScreen.this, new GlobalPermissionPopup.PermissionCallback() {
                @Override
                public void onPermissionGranted() {
                    // Permission granted, proceed with gallery functionality
                    showGalleryInterface();
                }

                @Override
                public void onPermissionDenied() {
                    // User denied permission, do nothing
                    Log.d("GalleryDebug", "Photo permission denied by user");
                }

                @Override
                public void onSettingsClicked() {
                    // User clicked settings, do nothing for now
                    Log.d("GalleryDebug", "User clicked settings");
                }
            });
        });

        binding.emojiLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.VibratorLowBeam(mContext);
                }

                // Switch back to top search container, hide keyboard and show full grid layout
                binding.emojiSearchBox.clearFocus();
                binding.emojiSearchBoxBottom.clearFocus();
                binding.emojiSearchContainerTop.setVisibility(View.VISIBLE);
                binding.emojiSearchContainerBottom.setVisibility(View.GONE);
                binding.emojiLeftArrow.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(binding.emojiSearchBox.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(binding.emojiSearchBoxBottom.getWindowToken(), 0);
                }
                setRecyclerViewVertical();
            }
        });

//


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (binding.messageBox.getText().toString().trim().isEmpty()) {
                    if (Integer.parseInt(binding.multiSelectSmallCounterText.getText().toString()) > 0) {

                    } else {
                        runOnUiThread(() -> {
                            binding.send.setImageResource(R.drawable.mike);
                            // Hide character count when empty
                            binding.characterCount.setVisibility(View.GONE);
                        });
                    }
                } else {
                    binding.send.setImageResource(R.drawable.baseline_keyboard_double_arrow_right_24);
                    // Show character count when text is present
                    binding.characterCount.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Update character count
                int currentLength = s.length();
                int maxLength = 1000; // Set your desired max length

                binding.characterCount.setText(currentLength + "/" + maxLength);

                // Change color if approaching limit
                if (currentLength > maxLength * 0.8) {
                    binding.characterCount.setTextColor(getResources().getColor(R.color.red));
                } else {
                    binding.characterCount.setTextColor(getResources().getColor(R.color.chtbtncolor));
                }
            }
        });


        bottomSheetDialogRec = new BottomSheetDialog(grpChattingScreen.this, R.style.BottomSheetDialog);
        View viewShapeRec = getLayoutInflater().inflate(R.layout.bottom_sheet_dialogue_rec, null, false);
        bottomSheetDialogRec.setContentView(viewShapeRec);
        bottomSheetDialogRec.setCanceledOnTouchOutside(false);
        bottomSheetDialogRec.setCancelable(false);
        ImageView cross = viewShapeRec.findViewById(R.id.crossBtm);
        chronometerBtm = viewShapeRec.findViewById(R.id.chronometerBtm);
        LinearLayout sendGrpBtm = viewShapeRec.findViewById(R.id.sendGrpBtm);
        progressbar = viewShapeRec.findViewById(R.id.progressbar);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cancelRecording();
                chronometerBtm.stop();
                bottomSheetDialogRec.dismiss();
                Constant.ObjectAnimator(binding.sendGrp, 1f, 1f, 1f);
                Constant.animatorSet.start();
                //  countDownTimer.cancel();

            }
        });


        binding.sendGrp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (Integer.parseInt(binding.multiSelectSmallCounterText.getText().toString()) > 0) {

                } else {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e) {

                    }

                    if (binding.messageBox.getText().toString().isEmpty()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            Constant.Vibrator50(mContext);
                        }
                        int pixels;

                        // binding.sendGrpLyt.setLayoutParams(params);

//                    binding.editLyt.setVisibility(View.GONE);
//                    binding.sickbarLyt.setVisibility(View.VISIBLE);

                        Constant.ObjectAnimator(binding.sendGrp, 1.3f, 1.3f, 0.8f);
                        Constant.animatorSet.start();

                        bottomSheetDialogRec.show();

                        try {

                            Constant.getSfFuncion(getApplicationContext());
                            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                            try {
                                if (themColor.equals("#ff0080")) {

                                    sendGrpBtm.setBackgroundTintList(tintList);
                                } else if (themColor.equals("#00A3E9")) {

                                    sendGrpBtm.setBackgroundTintList(tintList);
                                } else if (themColor.equals("#7adf2a")) {

                                    sendGrpBtm.setBackgroundTintList(tintList);

                                } else if (themColor.equals("#ec0001")) {

                                    sendGrpBtm.setBackgroundTintList(tintList);

                                } else if (themColor.equals("#16f3ff")) {

                                    sendGrpBtm.setBackgroundTintList(tintList);

                                } else if (themColor.equals("#FF8A00")) {

                                    sendGrpBtm.setBackgroundTintList(tintList);


                                } else if (themColor.equals("#7F7F7F")) {

                                    sendGrpBtm.setBackgroundTintList(tintList);


                                } else if (themColor.equals("#D9B845")) {


                                    sendGrpBtm.setBackgroundTintList(tintList);


                                } else if (themColor.equals("#346667")) {

                                    sendGrpBtm.setBackgroundTintList(tintList);


                                } else if (themColor.equals("#9846D9")) {

                                    sendGrpBtm.setBackgroundTintList(tintList);
                                } else if (themColor.equals("#A81010")) {
                                    sendGrpBtm.setBackgroundTintList(tintList);


                                } else {
                                    sendGrpBtm.setBackgroundTintList(tintList);

                                }
                            } catch (Exception ignored) {

                            }


                        } catch (Exception ignored) {
                        }


                        startRecording();
                        //     countDownTimer.cancel();

                    }
                }
                return true;
            }
        });

        sendGrpBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator50(mContext);
                }
                sendAndStopRecording();
                chronometerBtm.stop();
                Constant.ObjectAnimator(binding.sendGrp, 1f, 1f, 1f);
                Constant.animatorSet.start();

                Constant.ObjectAnimator(sendGrpBtm, 1.3f, 1.3f, 0.8f);
                Constant.animatorSet.start();


                bottomSheetDialogRec.dismiss();

                Constant.ObjectAnimator(sendGrpBtm, 1f, 1f, 1f);
                Constant.animatorSet.start();

            }
        });


        //for displaying first name
        final Dialog block = new Dialog(grpChattingScreen.this);
        ((android.app.Dialog) block).setContentView(R.layout.block_dialoguw);
        block.setCanceledOnTouchOutside(true);
        block.setCancelable(true);
        block.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        block.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        block.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = block.getWindow().getAttributes();
        block.getWindow().setAttributes(layoutParams);

        TextView nameKey = block.findViewById(R.id.name);
        TextView cancel = block.findViewById(R.id.cancel);


        final Dialog clearMsg = new Dialog(grpChattingScreen.this);
        ((android.app.Dialog) clearMsg).setContentView(R.layout.clearmsg_layout);
        clearMsg.setCanceledOnTouchOutside(true);
        clearMsg.setCancelable(true);
        clearMsg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        clearMsg.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        clearMsg.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams2 = block.getWindow().getAttributes();
        clearMsg.getWindow().setAttributes(layoutParams2);

        TextView cancel2 = clearMsg.findViewById(R.id.cancel);


        final Dialog muteNotiDialogue = new Dialog(grpChattingScreen.this);
        ((android.app.Dialog) muteNotiDialogue).setContentView(R.layout.mute_notification);
        muteNotiDialogue.setCanceledOnTouchOutside(true);
        muteNotiDialogue.setCancelable(true);
        muteNotiDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        muteNotiDialogue.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        muteNotiDialogue.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams3 = block.getWindow().getAttributes();
        muteNotiDialogue.getWindow().setAttributes(layoutParams3);

        TextView cancel3 = muteNotiDialogue.findViewById(R.id.cancel);

        binding.messageRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isHighSpeed = false;
            private final int SCROLL_SPEED_THRESHOLD = 0;
            private Handler handler = new Handler();
            private Runnable hideDateRunnable = new Runnable() {
                @Override
                public void run() {
                    collapse(binding.date);
                }
            };

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                isHighSpeed = Math.abs(dy) > SCROLL_SPEED_THRESHOLD;
                //  Toast.makeText(mContext, "Scrolled", Toast.LENGTH_SHORT).show();
                Log.w("TTTTTTTT", "onScrolled: ");
                String date = null;
                try {
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    date = groupMessageList.get(firstVisibleItemPosition).getCurrentDate();
                } catch (Exception e) {

                }

                try {

                    if (date.contains(":")) {
                        String cleanText = date.replace(":", "");

                        if (cleanText.equals(Constant.getCurrentDate())) {
                            binding.dateTxt.setText("Today");

                        } else if (cleanText.equals(Constant.getYesterdayDate())) {
                            binding.dateTxt.setText("Yesterday");
                        } else {
                            binding.dateTxt.setText(cleanText);
                        }
                    } else {
                        if (date.equals(Constant.getCurrentDate())) {
                            binding.dateTxt.setText("Today");
                        } else if (date.equals(Constant.getYesterdayDate())) {
                            binding.dateTxt.setText("Yesterday");
                        } else {
                            binding.dateTxt.setText(date);
                        }
                    }


                } catch (Exception e) {

                }


                // Remove any pending hideDateRunnable to reset the timer
                handler.removeCallbacks(hideDateRunnable);

                // pagination


                if (layoutManager != null && layoutManager.findFirstVisibleItemPosition() == 0 && !isLoading) {

                    if (binding.searchEt.getText().toString().isEmpty() && !binding.searchEt.hasFocus()) {
                        loadMore(senderRoom);
                    }


                }


            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    lastMessageVisible = (lastVisibleItemPosition == totalItemCount - 1);
                }

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        // User is actively scrolling - delay hiding the date
                        handler.removeCallbacks(hideDateRunnable);
                        break;

                    case RecyclerView.SCROLL_STATE_SETTLING:
                        // Fling or fast scroll - load low quality for smooth momentum
                        groupChatAdapter.setHighQualityLoading(false);
                        break;

                    case RecyclerView.SCROLL_STATE_IDLE:
                        // Scrolling stopped - load high quality
                        Log.w("TTTTTTTT", "onScrollStateChanged: ");
                        groupChatAdapter.setHighQualityLoading(true);
                        handler.postDelayed(hideDateRunnable, 1500);
                        break;
                }
            }
        });
        binding.dateTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();

                if (!newText.equals(previousText)) {
                    //    Toast.makeText(mContext, newText, Toast.LENGTH_SHORT).show();
                    if (binding.date.getVisibility() == View.VISIBLE) {
                        //Constant.Vibrator(mContext);
                    }

                    previousText = newText;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.messageRecView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (binding.date.getVisibility() == View.GONE) {
                    expand(binding.date);
                }
                return false;
            }
        });

        binding.messageBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.messageBox.requestFocus();

                if (binding.galleryRecentLyt.getVisibility() == View.VISIBLE) {
                    binding.messageBox.requestFocus(); // Ensure the EditText gains focus
                    hideGalleryRecentView();

                    binding.galleryRecentLyt.animate()
                            .translationY(binding.galleryRecentLyt.getHeight()) // Move it downwards
                            .setDuration(0) // Animation duration
                            .withEndAction(() -> binding.galleryRecentLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                            .start();

                    binding.emojiLyt.animate()
                            .translationY(binding.emojiLyt.getHeight()) // Move it downwards
                            .setDuration(0) // Animation duration
                            .withEndAction(() -> binding.emojiLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                            .start();

                    binding.galleryRecentLyt.setVisibility(View.GONE);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(binding.messageBox, InputMethodManager.SHOW_IMPLICIT);
                    }

                }

                if (binding.emojiRecyclerview.getVisibility() == View.VISIBLE) {
                    binding.messageBox.requestFocus(); // Ensure the EditText gains focus
                    binding.emojiRecyclerview.animate()
                            .translationY(binding.emojiRecyclerview.getHeight()) // Move it downwards
                            .setDuration(0) // Animation duration
                            .withEndAction(() -> binding.emojiRecyclerview.setVisibility(View.GONE)) // Set to GONE after animation ends
                            .start();

                    binding.emojiLyt.animate()
                            .translationY(binding.emojiLyt.getHeight()) // Move it downwards
                            .setDuration(0) // Animation duration
                            .withEndAction(() -> binding.emojiLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                            .start();

                    binding.galleryRecentLyt.animate()
                            .translationY(binding.galleryRecentLyt.getHeight()) // Move it downwards
                            .setDuration(0) // Animation duration
                            .withEndAction(() -> binding.galleryRecentLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                            .start();

                    binding.galleryRecentLyt.setVisibility(View.GONE);
                    binding.emojiSearchContainerTop.setVisibility(View.GONE);
                    binding.emojiSearchContainerBottom.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(binding.messageBox, InputMethodManager.SHOW_IMPLICIT);
                    }
                    binding.bitemoji.setImageResource(R.drawable.emojisvg);
                }


                binding.messageBox.requestFocus();
                return false;
            }
        });

        // Add focus change listener to messageBox for keyboard handling
        binding.messageBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("KEYBOARD_SCROLL_GROUP", "messageBox focus changed: " + hasFocus);
                if (hasFocus) {
                    handleKeyboardScrollGroup();
                }
            }
        });

        // Add click listener to messageBox for keyboard handling (for subsequent clicks)
        binding.messageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("KEYBOARD_SCROLL_GROUP", "messageBox clicked");
                // Immediate scroll - no delay
                handleKeyboardScrollGroup();
            }
        });

        binding.emoji.setOnClickListener(v -> {
            // Vibrate on click for haptic feedback (WhatsApp-like)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(mContext);
            }

            // Smoothly handle emoji recycler view visibility
            if (binding.galleryRecentLyt.getVisibility() == View.VISIBLE) {
                // Show card view with WhatsApp-like slide-up animation
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(binding.messageBox.getWindowToken(), 0);
                }

                // Reset view state to prevent blank space or flickering
                binding.emojiRecyclerview.setAlpha(0f); // Start with invisible to avoid flicker
                binding.emojiRecyclerview.setTranslationY(0);

                // Calculate and set card view height
                int keyboardHeight = getKeyboardHeight();
                int minHeight = dpToPx(300); // Minimum height 300dp
                int finalHeight = Math.max(keyboardHeight, minHeight);

                ViewGroup.LayoutParams params = binding.emojiRecyclerview.getLayoutParams();
                params.height = finalHeight;
                binding.emojiRecyclerview.setLayoutParams(params);

                // Prepare and start slide-up animation
                binding.emojiRecyclerview.setVisibility(View.VISIBLE);
                binding.galleryRecentLyt.setVisibility(View.GONE);
                binding.emojiLyt.setVisibility(View.VISIBLE);
                binding.emojiSearchContainerTop.setVisibility(View.VISIBLE);
                binding.emojiSearchContainerBottom.setVisibility(View.GONE);

                // Reset to vertical layout when emoji button is clicked
                setRecyclerViewVertical();
                binding.emojiRecyclerview.setTranslationY(finalHeight);
                ViewPropertyAnimatorCompat animator = ViewCompat.animate(binding.emojiRecyclerview)
                        .translationY(0)
                        .alpha(1f)
                        .setDuration(200) // WhatsApp-like animation duration
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .withStartAction(() -> binding.emojiRecyclerview.invalidate()) // Force redraw
                        .withEndAction(() -> binding.emojiRecyclerview.requestLayout()); // Ensure layout update
                animator.start();

                binding.messageBox.requestFocus();
                binding.messageBox.setCursorVisible(true);


            }

            // Handle card view and keyboard visibility
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (binding.emojiRecyclerview.getVisibility() == View.VISIBLE) {

                binding.messageBox.requestFocus();
                if (binding.galleryRecentLyt.getVisibility() == View.VISIBLE) {
                    binding.messageBox.requestFocus(); // Ensure the EditText gains focus
                    hideGalleryRecentView();

                    binding.emojiLyt.animate()
                            .translationY(binding.emojiLyt.getHeight()) // Move it downwards
                            .setDuration(0) // Animation duration
                            .withEndAction(() -> binding.emojiLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                            .start();

                    binding.emojiSearchContainerTop.setVisibility(View.GONE);
                    binding.emojiSearchContainerBottom.setVisibility(View.GONE);
                    InputMethodManager imm7 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm7 != null) {
                        imm7.showSoftInput(binding.messageBox, InputMethodManager.SHOW_IMPLICIT);
                    }
                    binding.bitemoji.setImageResource(R.drawable.emojisvg);
                }

                if (binding.emojiRecyclerview.getVisibility() == View.VISIBLE) {
                    binding.messageBox.requestFocus(); // Ensure the EditText gains focus
                    binding.emojiRecyclerview.animate()
                            .translationY(binding.emojiRecyclerview.getHeight()) // Move it downwards
                            .setDuration(0) // Animation duration
                            .withEndAction(() -> binding.emojiRecyclerview.setVisibility(View.GONE)) // Set to GONE after animation ends
                            .start();

                    binding.emojiLyt.animate()
                            .translationY(binding.emojiLyt.getHeight()) // Move it downwards
                            .setDuration(0) // Animation duration
                            .withEndAction(() -> binding.emojiLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                            .start();

                    binding.emojiSearchContainerTop.setVisibility(View.GONE);
                    binding.emojiSearchContainerBottom.setVisibility(View.GONE);
                    InputMethodManager imm5 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm5 != null) {
                        imm5.showSoftInput(binding.messageBox, InputMethodManager.SHOW_IMPLICIT);
                    }

                }
                binding.messageBox.requestFocus();

            } else {
                // Show card view with WhatsApp-like slide-up animation
                if (imm != null) {
                    imm.hideSoftInputFromWindow(binding.messageBox.getWindowToken(), 0);
                }

// Reset initial state to prevent flicker
                binding.emojiRecyclerview.setAlpha(0f);
                binding.emojiRecyclerview.setTranslationY(0);
                binding.emojiLyt.setAlpha(0f);
                binding.emojiLyt.setTranslationY(0);

// Calculate proper height
                int keyboardHeight = getKeyboardHeight();
                int minHeight = dpToPx(300); // Minimum height 300dp
                int finalHeight = Math.max(keyboardHeight, minHeight);

// Apply height
                ViewGroup.LayoutParams params = binding.emojiRecyclerview.getLayoutParams();
                params.height = finalHeight;
                binding.emojiRecyclerview.setLayoutParams(params);

// Make views visible
                binding.emojiRecyclerview.setVisibility(View.VISIBLE);
                binding.emojiLyt.setVisibility(View.VISIBLE);
                binding.emojiSearchContainerTop.setVisibility(View.VISIBLE);
                binding.emojiSearchContainerBottom.setVisibility(View.GONE);

                // Reset to vertical layout when emoji button is clicked
                setRecyclerViewVertical();

// Animate emojiRecyclerview
                binding.emojiRecyclerview.setTranslationY(finalHeight);
                ViewCompat.animate(binding.emojiRecyclerview)
                        .translationY(0)
                        .alpha(1f)
                        .setDuration(200)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .withStartAction(() -> binding.emojiRecyclerview.invalidate())
                        .withEndAction(() -> binding.emojiRecyclerview.requestLayout())
                        .start();

// Animate emojiLyt
                binding.emojiLyt.setTranslationY(finalHeight);
                ViewCompat.animate(binding.emojiLyt)
                        .translationY(0)
                        .alpha(1f)
                        .setDuration(200)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .withStartAction(() -> binding.emojiLyt.invalidate())
                        .withEndAction(() -> binding.emojiLyt.requestLayout())
                        .start();
// Focus EditText
                binding.messageBox.requestFocus();
                binding.messageBox.setCursorVisible(true);


            }
        });

        // Add emoji search functionality for top search box
        binding.emojiSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingText) return;

                String searchText = s.toString().toLowerCase().trim();
                // Sync text with bottom search box
                isUpdatingText = true;
                binding.emojiSearchBoxBottom.setText(s.toString());
                isUpdatingText = false;
                filterEmojis(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add emoji search functionality for bottom search box
        binding.emojiSearchBoxBottom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingText) return;

                String searchText = s.toString().toLowerCase().trim();
                // Sync text with top search box
                isUpdatingText = true;
                binding.emojiSearchBox.setText(s.toString());
                isUpdatingText = false;
                filterEmojis(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add click listener to open keyboard immediately
        binding.emojiSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Keep search at top, show left arrow, change to horizontal layout
                binding.emojiSearchContainerTop.setVisibility(View.VISIBLE);
                binding.emojiSearchContainerBottom.setVisibility(View.GONE);
                binding.emojiLeftArrow.setVisibility(View.VISIBLE);
                setRecyclerViewHorizontal();

                // Request focus and open keyboard
                binding.emojiSearchBox.requestFocus();
                binding.emojiSearchBox.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.showSoftInput(binding.emojiSearchBox, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }
                });
            }
        });

        // Add focus listener to change RecyclerView orientation for top search box
        binding.emojiSearchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Keep search at top, show left arrow, change to horizontal layout
                    binding.emojiSearchContainerTop.setVisibility(View.VISIBLE);
                    binding.emojiSearchContainerBottom.setVisibility(View.GONE);
                    binding.emojiLeftArrow.setVisibility(View.VISIBLE);
                    setRecyclerViewHorizontal();

                    // Open keyboard immediately
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(binding.emojiSearchBox, InputMethodManager.SHOW_IMPLICIT);
                    }
                } else {
                    // Hide left arrow and change back to vertical layout when search box loses focus
                    binding.emojiLeftArrow.setVisibility(View.GONE);
                    setRecyclerViewVertical();
                }
            }
        });


    }

    private void filterEmojis(String searchText) {
        if (adapter != null) {
            adapter.filterEmojis(searchText);
        }
    }

    private void setRecyclerViewHorizontal() {
        if (binding.emojiRecyclerview.getLayoutManager() == null) {
            return;
        }

        // Create horizontal LinearLayoutManager
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.emojiRecyclerview.setLayoutManager(horizontalLayoutManager);

        // Adjust height to show single row with minimal spacing
        ViewGroup.LayoutParams params = binding.emojiRecyclerview.getLayoutParams();
        params.height = dpToPx(50); // Minimal height to reduce spacing with keyboard
        binding.emojiRecyclerview.setLayoutParams(params);

        // Set adapter to horizontal layout (wrap_content width)
        if (adapter != null) {
            adapter.setHorizontalLayout(true);
        }
    }

    private void setRecyclerViewVertical() {
        if (binding.emojiRecyclerview.getLayoutManager() == null) {
            return;
        }

        // Create GridLayoutManager for grid layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 9); // 9 columns
        binding.emojiRecyclerview.setLayoutManager(gridLayoutManager);

        // Restore original height
        ViewGroup.LayoutParams params = binding.emojiRecyclerview.getLayoutParams();
        params.height = dpToPx(250); // Original height
        binding.emojiRecyclerview.setLayoutParams(params);

        // Set adapter to vertical layout (match_parent width)
        if (adapter != null) {
            adapter.setHorizontalLayout(false);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // this method is called when user will
        // grant the permission for audio recording.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        // Handle GlobalPermissionPopup permission results
        GlobalPermissionPopup.handlePermissionResult(requestCode, permissions, grantResults);
        
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case CAMERA_PERM_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<group_messageModel> filteredlist = new ArrayList<group_messageModel>();

        // running a for loop to compare elements.
        for (group_messageModel item : groupMessageList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getMessage().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {

            //Toast.makeText(mContext, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {

            groupChatAdapter.filterList(filteredlist);
        }
    }


    @Override
    public void onBackPressed() {


        if (binding.emojiLyt.getVisibility() == View.VISIBLE) {

            binding.emojiLyt.animate()
                    .translationY(binding.emojiLyt.getHeight()) // Move it downwards
                    .setDuration(0) // Animation duration
                    .withEndAction(() -> binding.emojiLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                    .start();

            return;
        }

        if (binding.galleryRecentLyt.getVisibility() == View.VISIBLE) {

            binding.galleryRecentLyt.animate()
                    .translationY(binding.galleryRecentLyt.getHeight()) // Move it downwards
                    .setDuration(0) // Animation duration
                    .withEndAction(() -> binding.galleryRecentLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                    .start();

            return;
        }

        // Check if camera fragment is currently visible
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.isVisible()) {
            Log.d("GrpChattingScreen", "onBackPressed() - camera fragment is visible, letting fragment handle back press");
            // Let the fragment handle the back press (it will call closeWithAnimation())
            super.onBackPressed();
            return;
        }

        // If search is visible/focused, clear it and finish immediately
        if (binding.searchEt.getVisibility() == View.VISIBLE && (binding.searchEt.hasFocus() || binding.searchEt.length() > 0)) {
            binding.searchEt.setText("");
            binding.searchEt.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(binding.searchEt.getWindowToken(), 0);
            }
            binding.searchEt.setVisibility(View.GONE);
            binding.name.setVisibility(View.VISIBLE);
            binding.menu2.setVisibility(View.VISIBLE);
            SwipeNavigationHelper.finishWithSwipe(grpChattingScreen.this);
            return;
        }

        if (binding.emojiRecyclerview.getVisibility() == View.VISIBLE) {
            binding.emojiRecyclerview.animate()
                    .translationY(binding.emojiRecyclerview.getHeight()) // Move it downwards
                    .setDuration(0) // Animation duration
                    .withEndAction(() -> binding.emojiRecyclerview.setVisibility(View.GONE)) // Set to GONE after animation ends
                    .start();
            binding.emojiLyt.animate()
                    .translationY(binding.emojiLyt.getHeight()) // Move it downwards
                    .setDuration(0) // Animation duration
                    .withEndAction(() -> binding.emojiLyt.setVisibility(View.GONE)) // Set to GONE after animation ends
                    .start();
            binding.bitemoji.setImageResource(R.drawable.emojisvg);
            SwipeNavigationHelper.finishWithSwipe(grpChattingScreen.this);
            return;
        } else if (bottomSheetDialogData != null && bottomSheetDialogData.isShowing()) {
            hideDataBottomSheet();
            SwipeNavigationHelper.finishWithSwipe(grpChattingScreen.this);
            return;

        } else {

            SwipeNavigationHelper.finishWithSwipe(grpChattingScreen.this);
            //   TransitionHelper.performTransition(((Activity)mContext));
            if (playerPreview2 != null) {
                playerPreview2.stop();
                playerPreview2.release();
            }
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        if (playerPreview2 != null) {
            playerPreview2.stop();
            playerPreview2.release();
        }
    }

    private void handleMultipleImageSelection() {
        Log.d("CAPTION_TRACE", "========================================");
        Log.d("CAPTION_TRACE", "handleMultipleImageSelection() CALLED");
        Log.d("CAPTION_TRACE", "selectedImageUris.size(): " + selectedImageUris.size());
        Log.d("CAPTION_TRACE", "currentCaption: '" + currentCaption + "'");
        Log.d("CAPTION_TRACE", "========================================");
        Log.d("MultiImageSelection", "handleMultipleImageSelection called with " + selectedImageUris.size() + " URIs");

        if (selectedImageUris.isEmpty()) {
            Log.d("MultiImageSelection", "No URIs selected, returning");
            return;
        }

        // Process each selected image
        for (Uri imageUri : selectedImageUris) {
            try {
                String extension;
                File f, f2;

                if (imageUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    extension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(imageUri));
                } else {
                    extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(imageUri.getPath()))));
                }

                // Get original filename
                String fileName = null;
                Cursor cursor = this.getContentResolver().query(imageUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        fileName = cursor.getString(index);
                    }
                    cursor.close();
                }

                if (fileName == null) {
                    fileName = "image_" + System.currentTimeMillis() + "." + extension;
                }

                // Create compressed version (20% quality)
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap bmpCompressed = BitmapFactory.decodeStream(imageStream);
                f = new File(getCacheDir() + "/" + fileName);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmpCompressed.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                byte[] bitmapdataCompressed = bos.toByteArray();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdataCompressed);
                fos.flush();
                fos.close();
                selectedImageFiles.add(f);

                // Create full quality version (80% quality)
                InputStream imageStream2 = getContentResolver().openInputStream(imageUri);
                Bitmap bmpFull = BitmapFactory.decodeStream(imageStream2);
                f2 = new File(getCacheDir() + "/full_" + fileName);

                ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
                bmpFull.compress(Bitmap.CompressFormat.JPEG, 80, bos2);
                byte[] bitmapdataFull = bos2.toByteArray();
                FileOutputStream fos2 = new FileOutputStream(f2);
                fos2.write(bitmapdataFull);
                fos2.flush();
                fos2.close();
                selectedFullImageFiles.add(f2);

                // Save to external storage
                File f2External = StoragePathHelper.getImagesStoragePath(mContext);

                if (!f2External.exists()) {
                    f2External.mkdirs();
                }

                File imageFile = new File(f2External, fileName);
                if (!imageFile.exists()) {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    FileOutputStream fs = new FileOutputStream(imageFile);
                    int read = 0;
                    int bufferSize = 1024;
                    final byte[] buffers = new byte[bufferSize];
                    while ((read = is.read(buffers)) != -1) {
                        fs.write(buffers, 0, read);
                    }
                    is.close();
                    fs.close();
                }

            } catch (Exception e) {
                Log.e("MultiImageSelection", "Error processing image: " + e.getMessage());
                Toast.makeText(mContext, "Error processing image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        Log.d("MultiImageSelection", "Finished processing. selectedImageFiles size: " + selectedImageFiles.size());
        Log.d("MultiImageSelection", "selectedFullImageFiles size: " + selectedFullImageFiles.size());

        // Show multi-image preview dialog
        showMultiImagePreviewDialog();
    }

    private void showMultiImagePreviewDialog() {
        Log.d("CAPTION_TRACE", "========================================");
        Log.d("CAPTION_TRACE", "showMultiImagePreviewDialog() CALLED");
        Log.d("CAPTION_TRACE", "currentCaption: '" + currentCaption + "'");
        Log.d("CAPTION_TRACE", "========================================");
        Log.d("SendButton", "=== showMultiImagePreviewDialog() called ===");
//        Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);
//        Constant.dialogLayoutFullScreen.show();

        Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false);
            View rootView = window.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(false);
        }

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
            Window activityWindow = ((Activity) mContext).getWindow();
            WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
            View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(true);
        });

        // Setup multi-image preview
        setupMultiImagePreview();
    }

    private void setupMultiImagePreview() {
        Log.d("CAPTION_TRACE", "========================================");
        Log.d("CAPTION_TRACE", "setupMultiImagePreview() CALLED");
        Log.d("CAPTION_TRACE", "selectedImageUris.size(): " + selectedImageUris.size());
        Log.d("CAPTION_TRACE", "currentCaption: '" + currentCaption + "'");
        Log.d("CAPTION_TRACE", "========================================");
        Log.d("MultiImagePreview", "Setting up preview with " + selectedImageUris.size() + " images");

        if (selectedImageUris.isEmpty()) {
            Log.d("MultiImagePreview", "No images selected, returning");
            return;
        }

        // Hide the single image view and show the horizontal gallery preview
        ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        if (singleImageView != null) {
            Log.d("MultiImagePreview", "Single image view was visible=" + (singleImageView.getVisibility() == View.VISIBLE));
            singleImageView.setVisibility(View.GONE);
        } else {
            Log.d("MultiImagePreview", "Single image view not found");
        }

        // Hide ViewPager2 and show horizontal gallery
        ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
        if (viewPager2 != null) {
            viewPager2.setVisibility(View.GONE);
        }

        // Setup horizontal gallery preview
        setupHorizontalGalleryPreview();
    }

    private void setupHorizontalGalleryPreview() {
        Log.d("HorizontalGallery", "Setting up horizontal gallery with " + selectedImageUris.size() + " images");
        for (int i = 0; i < selectedImageUris.size(); i++) {
            Log.d("HorizontalGallery", "uri[" + i + "]=" + selectedImageUris.get(i));
        }

        // Setup main image preview ViewPager2
        ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        if (mainImagePreview != null) {
            mainImagePreview.setVisibility(View.VISIBLE);
            Log.d("HorizontalGallery", "mainImagePreview set VISIBLE");

            // Setup adapter for main preview (use a copy so later mutations don't affect pages)
            MainImagePreviewAdapter mainAdapter = new MainImagePreviewAdapter(mContext, new ArrayList<>(selectedImageUris));
            mainImagePreview.setAdapter(mainAdapter);
            Log.d("HorizontalGallery", "MainImagePreviewAdapter attached with count=" + selectedImageUris.size());

            // Setup page change callback to sync with horizontal RecyclerView
            mainImagePreview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);


                    // Update counter
                    TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
                    if (imageCounter != null) {
                        imageCounter.setText((position + 1) + " / " + selectedImageUris.size());
                    }

                    // Update caption EditText with current caption
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        Log.d("PageChange", "Switched to position " + position + ", caption: '" + currentCaption + "'");

                        if (currentCaption != null) {
                            messageBoxMy.setText(currentCaption);
                            // Position cursor at the end of the text
                            messageBoxMy.setSelection(messageBoxMy.getText().length());
                        } else {
                            messageBoxMy.setText("");
                        }
                    }
                }
            });

            // Setup TextWatcher for caption input
            EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
            if (messageBoxMy != null) {
                messageBoxMy.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Save the caption as user types
                        currentCaption = s.toString();
                        Log.d("CaptionWatcher", "Caption updated: " + currentCaption);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        }

        // Setup horizontal RecyclerView


        // Hide other elements that might interfere
        View videoView = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
        if (videoView != null) {
            videoView.setVisibility(View.GONE);
        }

        View downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            downloadCtrl.setVisibility(View.GONE);
        }

        // Setup image counter
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setText("1 / " + selectedImageUris.size());
            imageCounter.setVisibility(View.VISIBLE);
        }


        // Setup send button for multiple images
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);

        // CRITICAL DEBUG: Verify the EditText
        Log.d("EditTextDebug", "messageBoxMy found: " + (messageBoxMy != null));
        if (messageBoxMy != null) {
            Log.d("EditTextDebug", "messageBoxMy ID: " + messageBoxMy.getId());
            Log.d("EditTextDebug", "messageBoxMy current text: '" + messageBoxMy.getText().toString() + "'");
            Log.d("EditTextDebug", "messageBoxMy is enabled: " + messageBoxMy.isEnabled());
            Log.d("EditTextDebug", "messageBoxMy is focusable: " + messageBoxMy.isFocusable());
        }


        if (sendGrp != null) {
            Log.d("SendButton", "Setting up send button for group multi-image preview");
            Log.d("SendButton", "sendGrp found: " + (sendGrp != null ? "yes" : "no"));
            Log.d("SendButton", "sendGrp is clickable: " + sendGrp.isClickable());
            Log.d("SendButton", "sendGrp visibility: " + sendGrp.getVisibility());
            Log.d("SendButton", "sendGrp is enabled: " + sendGrp.isEnabled());

            // Make sure the button is clickable and enabled
            sendGrp.setClickable(true);
            sendGrp.setEnabled(true);

            sendGrp.setOnClickListener(v -> {
                Log.d("SendButton", "Send button clicked for images!");

                // Get caption from dialog's EditText
                EditText dialogMessageBox = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                String dialogCaption = dialogMessageBox != null ? dialogMessageBox.getText().toString().trim() : "";

                Log.d("CAPTION_TRACE", "========================================");
                Log.d("CAPTION_TRACE", "SEND BUTTON CLICKED");
                Log.d("CAPTION_TRACE", "currentCaption field: '" + currentCaption + "'");
                Log.d("CAPTION_TRACE", "dialogMessageBox found: " + (dialogMessageBox != null));
                Log.d("CAPTION_TRACE", "dialogCaption from EditText: '" + dialogCaption + "'");
                Log.d("CAPTION_TRACE", "Will call sendMultipleImages() with: '" + dialogCaption + "'");
                Log.d("CAPTION_TRACE", "========================================");

                binding.messageBox.setText("");

                // Call the correct method to send multiple images - use dialog caption
                sendMultipleImages(dialogCaption);

                // Dismiss dialog after sending
                if (Constant.dialogLayoutFullScreen != null) {
                    Constant.dialogLayoutFullScreen.dismiss();
                    Log.d("SendButton", "Dialog dismissed after sending images");
                }

                // Clear selection after sending
                selectedImageUris.clear();
                selectedImageFiles.clear();
                selectedFullImageFiles.clear();
            });
        }

        // Setup back button
        LinearLayout backButton = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                // Clear selection when dialog is dismissed
                selectedImageUris.clear();
                selectedImageFiles.clear();
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }
    }

    private void sendMultipleImages(String caption) {
        Log.d("CAPTION_TRACE", "========================================");
        Log.d("CAPTION_TRACE", "sendMultipleImages() ENTRY");
        Log.d("CAPTION_TRACE", "Parameter caption: '" + caption + "'");
        Log.d("CAPTION_TRACE", "currentCaption field: '" + currentCaption + "'");
        Log.d("CAPTION_TRACE", "========================================");

        Log.d("MultiImageSend", "Sending " + selectedImageUris.size() + " images with caption: " + caption);
        Log.d("MultiImageSend", "selectedImageFiles size: " + selectedImageFiles.size());

        if (selectedImageUris.isEmpty() || selectedImageFiles.isEmpty()) {
            Log.d("MultiImageSend", "No images to send, returning");
            Toast.makeText(grpChattingScreen.this, "No images selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Individual captions are stored in new HashMap<>() Map

        Constant.getSfFuncion(getApplicationContext());
        final String groupId = getIntent().getStringExtra("grpIdKey");
        Log.d("SendMultipleImages", "grpChattingScreen.sendMultipleImages: groupId from Intent=" + groupId);
        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);

        // Send each image
        Log.d("SendMultipleImages", "Starting to send " + selectedImageUris.size() + " images");
        Log.d("SendMultipleImages", "new HashMap<>() map size: " + new HashMap<>().size());
        Log.d("SendMultipleImages", "new HashMap<>() map contents: " + new HashMap<>().toString());
        Log.d("SendMultipleImages", "=== DETAILED CAPTION ANALYSIS ===");
        for (Map.Entry<Integer, String> entry : new HashMap<Integer, String>().entrySet()) {
            Log.d("SendMultipleImages", "Image " + entry.getKey() + " caption: '" + entry.getValue() + "' (length: " + entry.getValue().length() + ")");
        }
        Log.d("SendMultipleImages", "=== END CAPTION ANALYSIS ===");

        // If multi-image, send as ONE bunch message (WhatsApp-like) and return
        if (selectedImageUris.size() > 1 && selectedImageFiles.size() == selectedImageUris.size()) {
            try {
                // Use already computed groupId/senderId/currentDateTimeString above

                String modelId = database.getReference().push().getKey();
                String uniqDate = Constant.getCurrentDate();

                // Build selectionBunch models from selected files
                ArrayList<selectionBunchModel> selectionBunchModels = new ArrayList<>();
                for (File file : selectedImageFiles) {
                    selectionBunchModels.add(new selectionBunchModel("", file != null ? file.getName() : ""));
                }

                // Prepare first image details for message primary fields
                File primaryCompressed = selectedImageFiles.get(0);
                Uri primaryUri = selectedImageUris.get(0);
                String[] primaryDimensions = Constant.calculateImageDimensions(mContext, primaryCompressed, primaryUri);
                String imageWidthDp = primaryDimensions.length >= 1 ? primaryDimensions[0] : "";
                String imageHeightDp = primaryDimensions.length >= 2 ? primaryDimensions[1] : "";
                String aspectRatio = primaryDimensions.length >= 3 ? primaryDimensions[2] : "";

                group_messageModel model = new group_messageModel(
                        senderId,
                        "",
                        currentDateTimeString,
                        "",
                        Constant.img,
                        getFileExtension(primaryUri),
                        "",
                        "",
                        "",
                        "",
                        Constant.getSF.getString(Constant.UID_KEY, ""),
                        Constant.getSF.getString(Constant.full_name, ""),
                        modelId,
                        groupId,
                        "",
                        primaryCompressed != null ? primaryCompressed.getName() : "",
                        "",
                        "",
                        caption != null ? caption : "",
                        uniqDate,
                        imageWidthDp,
                        imageHeightDp,
                        aspectRatio,
                        String.valueOf(selectedImageUris.size()),
                        selectionBunchModels
                );

                Log.d("CAPTION_TRACE", "========================================");
                Log.d("CAPTION_TRACE", "Multi-image model CREATED");
                Log.d("CAPTION_TRACE", "Model caption: '" + model.getCaption() + "'");
                Log.d("CAPTION_TRACE", "Model selectionCount: " + model.getSelectionCount());
                Log.d("CAPTION_TRACE", "Model modelId: " + model.getModelId());
                Log.d("CAPTION_TRACE", "========================================");

                // Add to UI list once
                groupMessageList.add(model);
                groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                groupChatAdapter.setLastItemVisible(isLastItemVisible);
                binding.messageRecView.smoothScrollToPosition(groupChatAdapter.getItemCount() - 1);

                // Build full-size file paths for UploadHelper
                ArrayList<String> selectionBunchFilePaths = new ArrayList<>();
                for (File fullFile : selectedFullImageFiles) {
                    if (fullFile != null) selectionBunchFilePaths.add(fullFile.getAbsolutePath());
                }

                UploadHelper uploadHelper = new UploadHelper(mContext, primaryCompressed, selectedFullImageFiles.isEmpty() ? null : selectedFullImageFiles.get(0), createdBy, model);

                Log.d("CAPTION_TRACE", "========================================");
                Log.d("CAPTION_TRACE", "BEFORE uploadHelper.uploadContent()");
                Log.d("CAPTION_TRACE", "Passing caption param: '" + caption + "'");
                Log.d("CAPTION_TRACE", "Model caption before upload: '" + model.getCaption() + "'");
                Log.d("CAPTION_TRACE", "========================================");

                uploadHelper.setSelectionBunchFilePaths(selectionBunchFilePaths);
                uploadHelper.uploadContent(
                        Constant.img,
                        primaryUri,
                        caption,
                        modelId,
                        null,
                        null,
                        primaryCompressed != null ? primaryCompressed.getName() : "",
                        null,
                        null,
                        null,
                        null,
                        getFileExtension(primaryUri),
                        imageWidthDp,
                        imageHeightDp,
                        aspectRatio,
                        groupId
                );

                return; // Avoid per-image loop; we've sent one bunch
            } catch (Exception e) {
                Log.e("SelectionBunch", "Error sending bunch: " + e.getMessage(), e);
            }
        }

        // Build selectionBunch for any multi-image send (WhatsApp-like)
        ArrayList<selectionBunchModel> selectionBunch = null;
        ArrayList<String> imageModelIds = new ArrayList<>();
        boolean isMultiImage = selectedImageUris.size() > 1;
        if (isMultiImage) {
            // Generate modelIds for images (if used downstream)
            for (int i = 0; i < selectedImageUris.size(); i++) {
                String imageModelId = database.getReference().push().getKey();
                imageModelIds.add(imageModelId);
            }

            selectionBunch = new ArrayList<>();
            for (int i = 0; i < selectedImageFiles.size(); i++) {
                File file = selectedImageFiles.get(i);
                selectionBunchModel bunchModel = new selectionBunchModel("", file != null ? file.getName() : "");
                selectionBunch.add(bunchModel);
            }
            Log.d("SelectionBunch", "Created selectionBunch (multi-image) size=" + (selectionBunch != null ? selectionBunch.size() : 0));
            Log.d("SelectionBunch", "ModelIds: " + imageModelIds.toString());
        }

        for (int i = 0; i < selectedImageUris.size(); i++) {
            Uri imageUri = selectedImageUris.get(i);
            File imageFile = selectedImageFiles.get(i);
            Log.d("MultiImageSend", "Processing image " + (i + 1) + ": " + imageUri + " -> " + imageFile);

            // Get individual caption for this image
            String individualCaption = "";
            if (individualCaption == null) {
                individualCaption = "";
                Log.d("IndividualCaption", "Image " + i + " caption was null, using empty string");
            } else {
                Log.d("IndividualCaption", "Image " + i + " caption: '" + individualCaption + "'");
            }

            // Use pre-generated modelId for each image
            String imageModelId;
            if (imageModelIds.size() > i) {
                imageModelId = imageModelIds.get(i);
            } else {
                imageModelId = database.getReference().push().getKey();
            }

            // Debug: Log the individualCaption right before creating messageModel
            Log.d("MessageModelCreation", "Creating messageModel for image " + i + " with caption: '" + individualCaption + "'");

            // Calculate image dimensions
            String[] dimensions = Constant.calculateImageDimensions(mContext, imageFile, imageUri);
            String imageWidthDp = dimensions[0];
            String imageHeightDp = dimensions[1];
            String aspectRatio = dimensions[2];

            // Debug logging for dimensions
            Log.d("GroupImageDimensions", "Image " + i + " - Calculated dimensions:");
            Log.d("GroupImageDimensions", "  imageWidthDp: " + imageWidthDp);
            Log.d("GroupImageDimensions", "  imageHeightDp: " + imageHeightDp);
            Log.d("GroupImageDimensions", "  aspectRatio: " + aspectRatio);
            Log.d("GroupImageDimensions", "  imageFile: " + imageFile);
            Log.d("GroupImageDimensions", "  imageUri: " + imageUri);

            // Create message model
            String uniqDate = Constant.getCurrentDate();
            ArrayList<emojiModel> emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));

            // Determine selectionCount based on caption logic
            String finalSelectionCount = isMultiImage ? String.valueOf(selectedImageUris.size()) : "1";

            group_messageModel model = new group_messageModel(
                    senderId,
                    "",
                    currentDateTimeString,
                    imageFile.toString(),
                    Constant.img,
                    getFileExtension(imageUri),
                    "",
                    "",
                    "",
                    "",
                    Constant.getSF.getString(Constant.full_name, ""),
                    "", // userName
                    imageModelId,
                    groupId,
                    "", // docSize
                    imageFile.getName(),
                    "", // thumbnail
                    "", // fileNameThumbnail
                    individualCaption,
                    uniqDate,
                    imageWidthDp,
                    imageHeightDp,
                    aspectRatio,
                    finalSelectionCount,
                    selectionBunch
            );

            // Debug logging for model values
            Log.d("GroupModelDebug", "Image " + i + " - Model created with:");
            Log.d("GroupModelDebug", "  model.getImageWidth(): " + model.getImageWidth());
            Log.d("GroupModelDebug", "  model.getImageHeight(): " + model.getImageHeight());
            Log.d("GroupModelDebug", "  model.getAspectRatio(): " + model.getAspectRatio());
            Log.d("GroupModelDebug", "  model.getSelectionCount(): " + model.getSelectionCount());
            Log.d("GroupModelDebug", "  model.getReceiverUid(): " + model.getReceiverUid());
            Log.d("GroupModelDebug", "  groupId used in constructor: " + groupId);

            // Store message in SQLite pending table before upload
            try {
                new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(mContext).insertPendingGroupMessage(model);
                Log.d("PendingMessages", "Group image message stored in pending table: " + model.getModelId());
            } catch (Exception e) {
                Log.e("PendingMessages", "Error storing group image message in pending table: " + e.getMessage(), e);
            }

            groupMessageList.add(model);

            // Update UI immediately to show the message
            ((Activity) mContext).runOnUiThread(() -> {
                groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                groupChatAdapter.setLastItemVisible(true); // Show progress for pending message
                binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
            });

            // Upload image using existing model to preserve selectionCount
            UploadHelper uploadHelper = new UploadHelper(mContext, imageFile, null, createdBy, model);
            uploadHelper.uploadContent(
                    Constant.img,
                    imageUri,
                    model.getCaption(),
                    imageModelId,
                    null, // savedThumbnail
                    null, // fileThumbName
                    imageFile.getName(),
                    null, // contactName
                    null, // contactPhone
                    null, // audioTime
                    null, // audioName
                    getFileExtension(imageUri),
                    imageWidthDp,
                    imageHeightDp,
                    aspectRatio,
                    groupId
            );
        }

        // Final UI update to ensure all messages are visible
        ((Activity) mContext).runOnUiThread(() -> {
            groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
            groupChatAdapter.setLastItemVisible(isLastItemVisible);
            binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
        });

        // Clear selections after sending
        selectedImageUris.clear();
        selectedImageFiles.clear();
        selectedFullImageFiles.clear();
    }

    private void setupMultiImagePreviewWithData() {
        Log.d("MultiImagePreviewWithData", "Setting up preview with " + selectedImageUris.size() + " images");
        Log.d("MultiImagePreviewWithData", "SelectedImageUris size: " + selectedImageUris.size());
        Log.d("MultiImagePreviewWithData", "SelectedImageFiles size: " + selectedImageFiles.size());

        if (selectedImageUris.isEmpty()) {
            Log.d("MultiImagePreviewWithData", "No images selected, returning");
            return;
        }

        // Ensure images are processed for sending
        if (selectedImageFiles.isEmpty()) {
            Log.d("MultiImagePreviewWithData", "Images not processed yet, processing now...");
            handleMultipleImageSelection();
        }

        // Hide the single image view and show the horizontal gallery preview
        ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        if (singleImageView != null) {
            singleImageView.setVisibility(View.GONE);
        }

        // Hide ViewPager2 and show horizontal gallery
        ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
        if (viewPager2 != null) {
            viewPager2.setVisibility(View.GONE);
        }

        // Hide document preview
        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            downloadCtrl.setVisibility(View.GONE);
        }

        // Hide contact preview
        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
        if (contactContainer != null) {
            contactContainer.setVisibility(View.GONE);
        }

        // Setup image counter
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setText("1 / " + selectedImageUris.size());
            imageCounter.setVisibility(View.VISIBLE);
        }

        // Setup horizontal gallery preview with selected data
        setupHorizontalGalleryPreviewWithData();

        // Setup caption input with current messageBox text
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null) {
            String currentCaption = binding.messageBox.getText().toString().trim();
            messageBoxMy.setText(currentCaption);
            Log.d("MultiImagePreviewWithData", "Set caption from messageBox: " + currentCaption);
        }

        // Setup send button for multi-image sending
        setupMultiImageSendButton();

        // Setup back arrow click listener
        LinearLayout backButton = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                // Clear selection when dialog is dismissed
                selectedImageUris.clear();
                selectedImageFiles.clear();
                selectedFullImageFiles.clear();
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }
    }

    private void setupHorizontalGalleryPreviewWithData() {
        Log.d("HorizontalGalleryWithData", "Setting up horizontal gallery with " + selectedImageUris.size() + " images");
        for (int i = 0; i < selectedImageUris.size(); i++) {
            Log.d("HorizontalGalleryWithData", "uri[" + i + "]=" + selectedImageUris.get(i));
        }

        // Setup main image preview ViewPager2
        ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        if (mainImagePreview != null) {
            mainImagePreview.setVisibility(View.VISIBLE);
            Log.d("HorizontalGalleryWithData", "mainImagePreview set VISIBLE");

            // Setup adapter for main preview (use a copy so later mutations don't affect pages)
            MainImagePreviewAdapter mainAdapter = new MainImagePreviewAdapter(mContext, new ArrayList<>(selectedImageUris));
            mainImagePreview.setAdapter(mainAdapter);
            Log.d("HorizontalGalleryWithData", "MainImagePreviewAdapter attached with count=" + selectedImageUris.size());

            // Setup page change callback to sync with horizontal RecyclerView
            mainImagePreview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    // Update counter
                    TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
                    if (imageCounter != null) {
                        imageCounter.setText((position + 1) + " / " + selectedImageUris.size());
                    }

                    // Update caption EditText with current caption
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        Log.d("PageChange", "Switched to position " + position + ", caption: '" + currentCaption + "'");

                        if (currentCaption != null) {
                            messageBoxMy.setText(currentCaption);
                            // Position cursor at the end of the text
                            messageBoxMy.setSelection(messageBoxMy.getText().length());
                        } else {
                            messageBoxMy.setText("");
                        }
                    }
                }
            });

            // Setup TextWatcher for caption input
            EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
            if (messageBoxMy != null) {
                messageBoxMy.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Save the caption as user types
                        currentCaption = s.toString();
                        Log.d("CaptionWatcher", "Caption updated: " + currentCaption);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        }

        // Hide other elements that might interfere
        View videoView = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
        if (videoView != null) {
            videoView.setVisibility(View.GONE);
        }

        View downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            downloadCtrl.setVisibility(View.GONE);
        }

        // Setup image counter
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setText("1 / " + selectedImageUris.size());
            imageCounter.setVisibility(View.VISIBLE);
        }
    }

    private void setupMultiImageSendButton() {
        // Setup send button for multiple images
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);

        // CRITICAL DEBUG: Verify the EditText
        Log.d("EditTextDebug", "messageBoxMy found: " + (messageBoxMy != null));
        if (messageBoxMy != null) {
            Log.d("EditTextDebug", "messageBoxMy ID: " + messageBoxMy.getId());
            Log.d("EditTextDebug", "messageBoxMy current text: '" + messageBoxMy.getText().toString() + "'");
            Log.d("EditTextDebug", "messageBoxMy is enabled: " + messageBoxMy.isEnabled());
            Log.d("EditTextDebug", "messageBoxMy is focusable: " + messageBoxMy.isFocusable());
        }

        if (sendGrp != null) {
            Log.d("SendButton", "Setting up send button for group multi-image preview");
            Log.d("SendButton", "sendGrp found: " + (sendGrp != null ? "yes" : "no"));
            Log.d("SendButton", "sendGrp is clickable: " + sendGrp.isClickable());
            Log.d("SendButton", "sendGrp visibility: " + sendGrp.getVisibility());
            Log.d("SendButton", "sendGrp is enabled: " + sendGrp.isEnabled());

            // Make sure the button is clickable and enabled
            sendGrp.setClickable(true);
            sendGrp.setEnabled(true);

            sendGrp.setOnClickListener(v -> {
                Log.d("SendButton", "Send button clicked for images!");

                loadImages();
                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");
                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                // Get caption from dialog's EditText
                EditText dialogMessageBox = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                String dialogCaption = dialogMessageBox != null ? dialogMessageBox.getText().toString().trim() : "";

                Log.d("CAPTION_TRACE", "========================================");
                Log.d("CAPTION_TRACE", "SEND BUTTON CLICKED");
                Log.d("CAPTION_TRACE", "currentCaption field: '" + currentCaption + "'");
                Log.d("CAPTION_TRACE", "dialogMessageBox found: " + (dialogMessageBox != null));
                Log.d("CAPTION_TRACE", "dialogCaption from EditText: '" + dialogCaption + "'");
                Log.d("CAPTION_TRACE", "Will call sendMultipleImages() with: '" + dialogCaption + "'");
                Log.d("CAPTION_TRACE", "========================================");

                binding.messageBox.setText("");

                // Call the correct method to send multiple images - use dialog caption
                sendMultipleImages(dialogCaption);

                // Dismiss dialog after sending
                if (Constant.dialogLayoutFullScreen != null) {
                    Constant.dialogLayoutFullScreen.dismiss();
                    Log.d("SendButton", "Dialog dismissed after sending images");
                }

                // Clear selection after sending
                selectedImageUris.clear();
                selectedImageFiles.clear();
                selectedFullImageFiles.clear();
            });
        }
    }

    private void handleWhatsAppPickerSelection(ArrayList<Uri> pickerUris) {
        if (pickerUris == null || pickerUris.isEmpty()) {
            Log.w("WhatsAppPicker", "handleWhatsAppPickerSelection: pickerUris empty, nothing to do");
            return;
        }

        try {
            SelectionBunchData pickerData = prepareSelectionBunchDataForPicker(pickerUris);
            if (pickerData.selectionBunch.isEmpty()) {
                Log.e("WhatsAppPicker", "prepareSelectionBunchDataForPicker returned empty selectionBunch");
                Toast.makeText(this, "Error preparing images, please try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> captions = collectCaptionsForPicker(pickerData.originalIndexes);
            createAndSendSelectionBunchMessageFromPicker(pickerData, captions);
        } catch (Exception e) {
            Log.e("WhatsAppPicker", "Error handling picker selection: " + e.getMessage(), e);
            Toast.makeText(this, "Error sending images: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private SelectionBunchData prepareSelectionBunchDataForPicker(ArrayList<Uri> pickerUris) {
        SelectionBunchData data = new SelectionBunchData();

        for (int i = 0; i < pickerUris.size(); i++) {
            Uri originalUri = pickerUris.get(i);
            String mimeType = getContentResolver().getType(originalUri);
            if (mimeType == null || !mimeType.startsWith("image/")) {
                Log.d("SelectionBunch", "Skipping non-image uri=" + originalUri);
                continue;
            }

            File compressedFile = createCompressedImageFileForPicker(originalUri);
            File fullSizeFile = createFullSizeImageFileForPicker(originalUri);

            if (compressedFile == null || fullSizeFile == null) {
                Log.e("SelectionBunch", "Failed to create local files for uri=" + originalUri);
                continue;
            }

            data.uris.add(originalUri);
            data.compressedFiles.add(compressedFile);
            data.fullSizeFiles.add(fullSizeFile);
            data.originalIndexes.add(i);

            String fileName = resolvePickerFileName(originalUri, fullSizeFile);
            data.selectionBunch.add(new SelectionBunchItem("", fileName));
            Log.d("SelectionBunch", "Added picker item, fileName=" + fileName);
        }

        Log.d("SelectionBunch", "prepareSelectionBunchDataForPicker size=" + data.selectionBunch.size());
        return data;
    }

    private void createAndSendSelectionBunchMessageFromPicker(SelectionBunchData data, ArrayList<String> captions) {
        if (data == null || data.selectionBunch.isEmpty()) {
            Log.e("SelectionBunch", "createAndSendSelectionBunchMessageFromPicker: data empty");
            return;
        }

        try {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", java.util.Locale.getDefault());
            String currentDateTimeString = sdf.format(d);

            Constant.getSfFuncion(mContext);
            String groupId = getIntent().getStringExtra("grpIdKey");
            String senderId = Constant.getSF.getString(Constant.UID_KEY, "");

            String modelId = database.getReference().push().getKey();
            String uniqDate = Constant.getCurrentDate();

            ArrayList<selectionBunchModel> selectionBunchModels = new ArrayList<>();
            for (SelectionBunchItem item : data.selectionBunch) {
                selectionBunchModel bunchModel = new selectionBunchModel(
                        item.imgUrl != null ? item.imgUrl : "",
                        item.fileName != null ? item.fileName : ""
                );
                selectionBunchModels.add(bunchModel);
            }

            String caption = (captions != null && !captions.isEmpty() && captions.get(0) != null && !captions.get(0).trim().isEmpty())
                    ? captions.get(0).trim()
                    : (currentCaption != null ? currentCaption.trim() : "");

            group_messageModel model;
            if (uniqueDates.add(uniqDate)) {
                model = new group_messageModel(senderId, "", currentDateTimeString, "", Constant.img,
                        "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", senderId,
                        Constant.getSF.getString(Constant.full_name, ""), modelId, groupId, "", "", "", "",
                        caption, uniqDate, "", "", "", String.valueOf(selectionBunchModels.size()), selectionBunchModels);
            } else {
                model = new group_messageModel(senderId, "", currentDateTimeString, "", Constant.img,
                        "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", senderId,
                        Constant.getSF.getString(Constant.full_name, ""), modelId, groupId, "", "", "", "",
                        caption, ":" + uniqDate, "", "", "", String.valueOf(selectionBunchModels.size()), selectionBunchModels);
            }

            ArrayList<String> selectionBunchFilePaths = new ArrayList<>();
            for (File fullFile : data.fullSizeFiles) {
                selectionBunchFilePaths.add(fullFile.getAbsolutePath());
            }

            if (!selectionBunchFilePaths.isEmpty()) {
                model.setFileName(data.selectionBunch.get(0).fileName);
            }

            String[] primaryDimensions = Constant.calculateImageDimensions(mContext, data.compressedFiles.get(0), data.uris.get(0));
            if (primaryDimensions.length >= 3) {
                model.setImageWidth(primaryDimensions[0]);
                model.setImageHeight(primaryDimensions[1]);
                model.setAspectRatio(primaryDimensions[2]);
            }

            // Store message in SQLite pending table before upload
            try {
                new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(mContext).insertPendingGroupMessage(model);
                Log.d("PendingMessages", "Group multiple images message stored in pending table: " + model.getModelId());
            } catch (Exception e) {
                Log.e("PendingMessages", "Error storing group multiple images message in pending table: " + e.getMessage(), e);
            }

            groupMessageList.add(model);
            groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
            groupChatAdapter.setLastItemVisible(true); // Show progress for pending message
            binding.messageRecView.smoothScrollToPosition(groupChatAdapter.getItemCount() - 1);

            UploadHelper uploadHelper = new UploadHelper(mContext, data.compressedFiles.get(0), data.fullSizeFiles.get(0), createdBy, model);
            uploadHelper.setSelectionBunchFilePaths(selectionBunchFilePaths);

            uploadHelper.uploadContent(
                    Constant.img,
                    data.uris.get(0),
                    caption,
                    modelId,
                    null,
                    null,
                    data.compressedFiles.get(0).getName(),
                    null,
                    null,
                    null,
                    null,
                    getFileExtension(data.uris.get(0)),
                    primaryDimensions[0],
                    primaryDimensions[1],
                    primaryDimensions[2],
                    groupId
            );

        } catch (Exception e) {
            Log.e("SelectionBunch", "createAndSendSelectionBunchMessageFromPicker error: " + e.getMessage(), e);
            Toast.makeText(this, "Error sending images: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<String> collectCaptionsForPicker(ArrayList<Integer> originalIndexes) {
        ArrayList<String> captions = new ArrayList<>();
        // Use the currently typed caption (if any) for all selected items.
        String baseCaption = currentCaption != null ? currentCaption.trim() : "";

        if (originalIndexes == null || originalIndexes.isEmpty()) {
            captions.add(baseCaption);
            return captions;
        }

        for (Integer index : originalIndexes) {
            // We currently support a single shared caption for the selection bunch (WhatsApp-like)
            captions.add(baseCaption);
        }
        return captions;
    }

    private File createCompressedImageFileForPicker(Uri uri) {
        String baseName = resolvePickerFileName(uri, null);
        if (baseName == null || baseName.isEmpty()) {
            baseName = "image_" + System.currentTimeMillis() + ".jpg";
        }
        File outputFile = new File(getCacheDir(), "cmp_" + baseName);
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            if (inputStream == null) {
                return null;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                return null;
            }
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos);
                fos.flush();
            }
            return outputFile;
        } catch (Exception e) {
            Log.e("SelectionBunch", "createCompressedImageFileForPicker error: " + e.getMessage(), e);
            return null;
        }
    }

    private File createFullSizeImageFileForPicker(Uri uri) {
        String baseName = resolvePickerFileName(uri, null);
        if (baseName == null || baseName.isEmpty()) {
            baseName = "image_" + System.currentTimeMillis() + ".jpg";
        }
        File outputFile = new File(getCacheDir(), "full_" + baseName);
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            if (inputStream == null) {
                return null;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                return null;
            }
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.flush();
            }
            return outputFile;
        } catch (Exception e) {
            Log.e("SelectionBunch", "createFullSizeImageFileForPicker error: " + e.getMessage(), e);
            return null;
        }
    }

    private String resolvePickerFileName(Uri uri, File fallbackFile) {
        String fileName = null;
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    fileName = cursor.getString(index);
                }
            }
        } catch (Exception e) {
            Log.e("SelectionBunch", "resolvePickerFileName error: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (fileName == null || fileName.isEmpty()) {
            fileName = fallbackFile != null ? fallbackFile.getName() : null;
        }
        if (fileName == null || fileName.isEmpty()) {
            fileName = "image_" + System.currentTimeMillis() + ".jpg";
        }
        return fileName;
    }

    private static class SelectionBunchData {
        ArrayList<Uri> uris = new ArrayList<>();
        ArrayList<SelectionBunchItem> selectionBunch = new ArrayList<>();
        ArrayList<File> compressedFiles = new ArrayList<>();
        ArrayList<File> fullSizeFiles = new ArrayList<>();
        ArrayList<Integer> originalIndexes = new ArrayList<>();
    }

    private static class SelectionBunchItem {
        String imgUrl;
        String fileName;

        SelectionBunchItem(String imgUrl, String fileName) {
            this.imgUrl = imgUrl;
            this.fileName = fileName;
        }
    }

    @OptIn(markerClass = UnstableApi.class)
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            modelId = database.getReference().push().getKey();

            // Handle WhatsApp-like image picker results
            ArrayList<Uri> newSelectedImages = data.getParcelableArrayListExtra(WhatsAppLikeImagePicker.EXTRA_SELECTED_IMAGES);
            if (newSelectedImages != null && !newSelectedImages.isEmpty()) {
                // Capture caption from picker, if provided
                String pickerCaption = data.getStringExtra("caption");

                Log.d("CAPTION_TRACE", "========================================");
                Log.d("CAPTION_TRACE", "onActivityResult() - PICK_IMAGE_REQUEST_CODE");
                Log.d("CAPTION_TRACE", "Received " + newSelectedImages.size() + " images from picker");
                Log.d("CAPTION_TRACE", "pickerCaption from Intent: '" + pickerCaption + "'");
                Log.d("CAPTION_TRACE", "currentCaption BEFORE: '" + currentCaption + "'");

                if (pickerCaption != null) {
                    currentCaption = pickerCaption;
                }

                Log.d("CAPTION_TRACE", "currentCaption AFTER: '" + currentCaption + "'");
                Log.d("CAPTION_TRACE", "About to call handleMultipleImageSelection()");
                Log.d("CAPTION_TRACE", "========================================");

                Log.d("GrpPicker", "onActivityResult: received " + newSelectedImages.size() + " images from picker");
                Log.d("GrpPicker", "onActivityResult: currentCaption='" + currentCaption + "'");
                // Mirror individual chat behavior: show preview dialog instead of sending
                selectedImageUris.clear();
                Log.d("GrpPicker", "onActivityResult: selectedImageUris cleared. size=" + selectedImageUris.size());
                selectedImageUris.addAll(newSelectedImages);
                Log.d("GrpPicker", "onActivityResult: selectedImageUris after add size=" + selectedImageUris.size());
                // IMPORTANT: Process files first so send button has data; this also opens the preview
                handleMultipleImageSelection();
                return;
            }

            if (mStoragetask != null && mStoragetask.isInProgress()) {
                Constant.showCustomToast("Upload in process", findViewById(R.id.includedToast), findViewById(R.id.toastText));
            } else {
                if (GlobalUri != null) {


                    try {
                        //for uploading document to mysql
                        globalFile = null;
                        Log.d("ImageFile000", GlobalUri.getAuthority());
                        Log.d("ImageFile000", GlobalUri.getScheme());


                        String extension;
                        File f, f2;
                        if (GlobalUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                            final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                            extension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(GlobalUri));

                        } else {
                            extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(GlobalUri.getPath()))));

                        }

                        InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(GlobalUri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Bitmap bmpCompresssSize = BitmapFactory.decodeStream(imageStream);
                        Log.d("extension", extension);

                        // for  getting original name
                        String fileName = null;
                        if (GlobalUri != null) {
                            Cursor cursor = this.getContentResolver().query(GlobalUri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                if (index != -1) {
                                    fileName = cursor.getString(index);
                                }
                                cursor.close();
                            }
                        }
                        Log.d("TAG", "actualName1: " + fileName);
                        f = new File(getCacheDir() + "/" + fileName);

                        try {
                            //part1
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bmpCompresssSize.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                            byte[] bitmapdataCompressed = bos.toByteArray();
                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(bitmapdataCompressed);
                            fos.flush();
                            fos.close();

                            Log.d("imageFile111", f.getPath());
                            globalFile = f;

                            long fileSize = getFileSize(globalFile.getPath());
                            Log.d("File size compressed", getFormattedFileSize(fileSize));


                        } catch (Exception e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onActivityResult: " + e.getMessage());

                        }


                        InputStream imageStream2 = null;
                        try {
                            imageStream2 = getContentResolver().openInputStream(GlobalUri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Bitmap bmpFullSize = BitmapFactory.decodeStream(imageStream2);


                        String fileName2 = null;
                        if (GlobalUri != null) {
                            Cursor cursor = this.getContentResolver().query(GlobalUri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                if (index != -1) {
                                    fileName2 = cursor.getString(index);
                                }
                                cursor.close();
                            }
                        }
                        Log.d("TAG", "actualName2: " + fileName2);


                        f2 = new File(getCacheDir() + "/" + fileName2);

                        try {
                            //part2
                            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
                            bmpFullSize.compress(Bitmap.CompressFormat.JPEG, 80, bos2);
                            byte[] bitmapdataFull = bos2.toByteArray();
                            FileOutputStream fos2 = new FileOutputStream(f2);
                            fos2.write(bitmapdataFull);
                            fos2.flush();
                            fos2.close();

                            Log.d("imageFile111", f.getPath());
                            FullImageFile = f2;
                            long fileSize = getFileSize(FullImageFile.getPath());
                            Log.d("File size Full", getFormattedFileSize(fileSize));


                        } catch (Exception e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onActivityResult: " + e.getMessage());

                        }


                        File f2External;
                        String exactPath2;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            f2External = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                            exactPath2 = f2External.getAbsolutePath();

                        } else {
                            f2External = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
                            exactPath2 = f2External.getAbsolutePath();
                        }


                        if (!f2External.exists()) {
                            f2External.mkdirs();
                        }

                        if (doesFileExist(exactPath2 + "/" + fileName2)) {

                        } else {


                            File imageFile = new File(f2External, fileName2);

                            try {
                                InputStream is = getContentResolver().openInputStream(GlobalUri);
                                FileOutputStream fs = new FileOutputStream(imageFile);
                                int read = 0;
                                int bufferSize = 1024;
                                final byte[] buffers = new byte[bufferSize];
                                while ((read = is.read(buffers)) != -1) {
                                    fs.write(buffers, 0, read);
                                }
                                is.close();
                                fs.close();

                                Log.d("imageFile111", imageFile.getPath());
                            } catch (Exception e) {
                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "imageofflineerror: " + e.getMessage());
                            }
                        }


                        // From here selecting screen code
                        Log.d("SendButton", "=== Dialog created in onActivityResult (single image) ===");

                        Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);
                        Constant.dialogLayoutFullScreen.show();


                        Window window = Constant.dialogLayoutFullScreen.getWindow();
                        if (window != null) {
                            WindowCompat.setDecorFitsSystemWindows(window, false);
                            View rootView = window.getDecorView().findViewById(android.R.id.content);
                            rootView.setFitsSystemWindows(false);
                        }

                        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

                        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
                            // Restore decor fitsSystemWindows true
                            Window activityWindow = ((Activity) mContext).getWindow();
                            WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
                            View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
                            rootView.setFitsSystemWindows(true);
                        });


                        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.backarrow);
                        ImageView image = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
                        PlayerView video = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
                        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);

                        // TODO: 18/04/25  messageBoxMy - Start

                        if (!binding.messageBox.getText().toString().isEmpty()) {
                            messageBoxMy.setText(binding.messageBox.getText().toString());
                        }

                        // TODO: 18/04/25  messageBoxMy - End

                        try {

                            Constant.getSfFuncion(getApplicationContext());
                            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                            sendGrp.setBackgroundTintList(tintList);


                        } catch (Exception i) {

                        }
                        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
                        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
                        //visibility
                        image.setVisibility(View.VISIBLE);
                        video.setVisibility(View.GONE);
                        downloadCtrl.setVisibility(View.GONE);
                        contactContainer.setVisibility(View.GONE);

                        if (doesFileExist(exactPath2 + "/" + fileName2)) {
                            image.setImageURI(Uri.parse(exactPath2 + "/" + fileName2));

                            sendGrp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("SendButton", "Send button clicked in onActivityResult (single image)!");
                                    binding.messageBox.setText("");

                                    String[] dimensions = Constant.calculateImageDimensions(mContext, globalFile, GlobalUri);
                                    String imageWidthDp = dimensions[0];
                                    String imageHeightDp = dimensions[1];
                                    String aspectRatio = dimensions[2];

                                    if (messageBoxMy.getText().toString().trim().equals("")) {


                                        Constant.getSfFuncion(getApplicationContext());
                                        final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                        final String senderRoom = senderId + grpIdKey;
                                        final String receiverRoom = grpIdKey + senderId;
                                        Log.d("senderRoom", senderRoom + receiverRoom);
                                        Date d = new Date();
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                        String currentDateTimeString = sdf.format(d);
                                        group_messageModel model = null;


                                        assert modelId != null;
                                        String uniqDate = Constant.getCurrentDate();
                                        if (uniqueDates.add(uniqDate)) {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), "", "", "", uniqDate, imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            groupMessageList.add(model);
                                        } else {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), "", "", "", ":" + uniqDate, imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            groupMessageList.add(model);
                                        }


                                        ((Activity) mContext).runOnUiThread(() -> {
                                            groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                                            groupChatAdapter.setLastItemVisible(true); // Show progress for pending message
                                            binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                                        });


                                        group_messageModel2 model2 = new group_messageModel2(
                                                model.getUid(),
                                                model.getMessage(),
                                                model.getTime(),
                                                model.getDocument(),
                                                model.getDataType(),
                                                model.getExtension(),
                                                model.getName(),
                                                model.getPhone(),
                                                model.getMicPhoto(),
                                                model.getMiceTiming(),
                                                model.getCreatedBy(),
                                                model.getUserName(),
                                                model.getModelId(),
                                                model.getReceiverUid(),
                                                model.getDocSize(),
                                                model.getFileName(),
                                                model.getThumbnail(),
                                                model.getFileNameThumbnail(),
                                                model.getCaption(),
                                                model.getCurrentDate(),
                                                senderRoom,
                                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount() // active: 0 = still loading
                                        );

                                        // TODO: active: 0 = still loading
                                        // TODO: active: 1 = completed

//                                        try {
//                                            new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                        } catch (Exception e) {
//                                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                        }


                                        UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, FullImageFile, createdBy);
                                        // Image upload
                                        uploadHelper.uploadContent(Constant.img, GlobalUri, messageBoxMy.getText().toString().trim(), modelId, null, null, globalFile != null ? globalFile.getName() : "", null, null, null, null, null, imageWidthDp, imageHeightDp, aspectRatio);

                                        Constant.dialogLayoutFullScreen.dismiss();

                                    } else {
                                        Constant.getSfFuncion(getApplicationContext());
                                        final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                        final String senderRoom = senderId + grpIdKey;
                                        final String receiverRoom = grpIdKey + senderId;
                                        Log.d("senderRoom", senderRoom + receiverRoom);
                                        Date d = new Date();
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                        String currentDateTimeString = sdf.format(d);
                                        group_messageModel model = null;


                                        assert modelId != null;
                                        String uniqDate = Constant.getCurrentDate();
                                        if (uniqueDates.add(uniqDate)) {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), "", "", messageBoxMy.getText().toString(), uniqDate, imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            groupMessageList.add(model);
                                        } else {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), "", "", messageBoxMy.getText().toString(), ":" + uniqDate, imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            groupMessageList.add(model);
                                        }

                                        ((Activity) mContext).runOnUiThread(() -> {
                                            groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                                            groupChatAdapter.setLastItemVisible(isLastItemVisible);
                                            binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                                        });

                                        Constant.dialogLayoutFullScreen.dismiss();

                                        group_messageModel2 model2 = new group_messageModel2(
                                                model.getUid(),
                                                model.getMessage(),
                                                model.getTime(),
                                                model.getDocument(),
                                                model.getDataType(),
                                                model.getExtension(),
                                                model.getName(),
                                                model.getPhone(),
                                                model.getMicPhoto(),
                                                model.getMiceTiming(),
                                                model.getCreatedBy(),
                                                model.getUserName(),
                                                model.getModelId(),
                                                model.getReceiverUid(),
                                                model.getDocSize(),
                                                model.getFileName(),
                                                model.getThumbnail(),
                                                model.getFileNameThumbnail(),
                                                model.getCaption(),
                                                model.getCurrentDate(),
                                                senderRoom,
                                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount() // active: 0 = still loading
                                        );

                                        // TODO: active: 0 = still loading
                                        // TODO: active: 1 = completed

//                                        try {
//                                            new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                        } catch (Exception e) {
//                                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                        }

                                        // String caption = messageBoxMy.getText().toString();


                                        UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, FullImageFile, createdBy);
                                        // Image upload
                                        uploadHelper.uploadContent(Constant.img, GlobalUri, messageBoxMy.getText().toString().trim(), modelId, null, null, globalFile != null ? globalFile.getName() : "", null, null, null, null, null, imageWidthDp, imageHeightDp, aspectRatio);

//

                                    }
                                }
                            });
                        }


                        backarrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Constant.dialogLayoutFullScreen.dismiss();

                            }
                        });


                    } catch (Exception ignored) {
                    }


                } else {
                    Toast.makeText(grpChattingScreen.this, "Please select image", Toast.LENGTH_SHORT).show();
                }


            }

        }
        // OLD SINGLE VIDEO HANDLER - DISABLED TO PREVENT CONFLICT WITH MULTI-VIDEO HANDLER
        // This handler was causing "Please select image" error when using WhatsApp-like video picker
        /*
        if (requestCode == PICK_VIDEO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            modelId = database.getReference().push().getKey();
            GlobalUri = data.getData();


            if (mStoragetask != null && mStoragetask.isInProgress()) {

                Constant.showCustomToast("Upload in process", findViewById(R.id.includedToast), findViewById(R.id.toastText));

            } else {
                if (GlobalUri != null) {
                    try {

                        globalFile = null;
                        Log.d("ImageFile000", GlobalUri.getAuthority());
                        Log.d("ImageFile000", GlobalUri.getScheme());

                        String extension;
                        File f;
                        if (GlobalUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                            final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                            extension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(GlobalUri));

                        } else {
                            extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(GlobalUri.getPath()))));

                        }
                        Log.d("extension", extension);


                        // for  getting original name
                        String fileName = null;
                        if (GlobalUri != null) {
                            Cursor cursor = this.getContentResolver().query(GlobalUri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                if (index != -1) {
                                    fileName = cursor.getString(index);
                                }
                                cursor.close();
                            }
                        }
                        Log.d("TAG", "actualName1: " + fileName);
                        File savedThumbnail = null;
                        String fileThumbName = null;

                        f = new File(getCacheDir() + "/" + fileName);
                        try {
                            InputStream is = getContentResolver().openInputStream(GlobalUri);
                            FileOutputStream fs = new FileOutputStream(f);
                            int read = 0;
                            int bufferSize = 1024;
                            final byte[] buffers = new byte[bufferSize];
                            while ((read = is.read(buffers)) != -1) {
                                fs.write(buffers, 0, read);
                            }
                            is.close();
                            fs.close();

                            Log.d("imageFile111", f.getPath());
                            globalFile = f;

                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(f.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

                            assert thumbnail != null;
                            savedThumbnail = FileUtils.saveBitmapToFile(getApplicationContext(), thumbnail, "thumbnail.png");
                            fileThumbName = fileName + "." + "png";


                            File f2External;
                            String exactPath2;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                f2External = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Enclosure/Media/Thumbnail");
                                exactPath2 = f2External.getAbsolutePath();

                            } else {
                                f2External = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Thumbnail");
                                exactPath2 = f2External.getAbsolutePath();
                            }


                            if (!f2External.exists()) {
                                f2External.mkdirs();
                            }

                            if (doesFileExist(exactPath2 + "/" + fileThumbName)) {

                            } else {

                                File imageFile = new File(f2External, fileThumbName);
                                try {
                                    InputStream is3 = getContentResolver().openInputStream(Uri.fromFile(savedThumbnail));
                                    FileOutputStream fs3 = new FileOutputStream(imageFile);
                                    int read3 = 0;
                                    int bufferSize3 = 1024;
                                    final byte[] buffers3 = new byte[bufferSize3];
                                    while ((read3 = is3.read(buffers3)) != -1) {
                                        fs3.write(buffers3, 0, read3);
                                    }
                                    is3.close();
                                    fs3.close();

                                    Log.d("imageFile111", f2External.getPath());


                                } catch (Exception e) {
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }


                        } catch (Exception e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }


                        File f2External;
                        String exactPath2;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            f2External = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                            exactPath2 = f2External.getAbsolutePath();

                        } else {
                            f2External = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Videos");
                            exactPath2 = f2External.getAbsolutePath();
                        }

                        if (!f2External.exists()) {
                            f2External.mkdirs();
                        }
                        if (doesFileExist(exactPath2 + "/" + fileName)) {

                        } else {

                            File imageFile = new File(f2External, fileName);

                            try {
                                InputStream is = getContentResolver().openInputStream(GlobalUri);
                                FileOutputStream fs = new FileOutputStream(imageFile);
                                int read = 0;
                                int bufferSize = 1024;
                                final byte[] buffers = new byte[bufferSize];
                                while ((read = is.read(buffers)) != -1) {
                                    fs.write(buffers, 0, read);
                                }
                                is.close();
                                fs.close();

                                Log.d("imageFile111", f2External.getPath());


                            } catch (Exception e) {
                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                        // From here selecting screen code

                        Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);
                        Constant.dialogLayoutFullScreen.show();


                        Window window = Constant.dialogLayoutFullScreen.getWindow();
                        if (window != null) {
                            WindowCompat.setDecorFitsSystemWindows(window, false);
                            View rootView = window.getDecorView().findViewById(android.R.id.content);
                            rootView.setFitsSystemWindows(false);
                        }

                        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

                        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
                            // Restore decor fitsSystemWindows true
                            Window activityWindow = ((Activity) mContext).getWindow();
                            WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
                            View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
                            rootView.setFitsSystemWindows(true);
                        });

                        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.backarrow);
                        ImageView image = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
                        PlayerView video = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
                        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
                        LinearLayout arrowback2 = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
                        arrowback2.setVisibility(View.GONE);
                        TextView nameTitle = Constant.dialogLayoutFullScreen.findViewById(R.id.nameTitle);
                        if (nameTitle != null) {
                            nameTitle.setText(fileName); // Set the retrieved fileName here
                        }

                        // TODO: 18/04/25  messageBoxMy - Start

                        if (!binding.messageBox.getText().toString().isEmpty()) {
                            messageBoxMy.setText(binding.messageBox.getText().toString());
                        }

                        // TODO: 18/04/25  messageBoxMy - End

                        try {

                            Constant.getSfFuncion(getApplicationContext());
                            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                            sendGrp.setBackgroundTintList(tintList);


                        } catch (Exception i) {

                        }
                        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
                        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
                        //visibility
                        image.setVisibility(View.GONE);
                        downloadCtrl.setVisibility(View.GONE);
                        contactContainer.setVisibility(View.GONE);
                        video.setVisibility(View.VISIBLE);
                        ImageView play = video.findViewById(R.id.play);
                        ImageView pause = video.findViewById(R.id.pause);
                        DefaultTimeBar exoProgress = video.findViewById(R.id.exoProgress);
                        TextView startTime = video.findViewById(R.id.startTime);
                        TextView totalTime = video.findViewById(R.id.totalTime);
                        if (doesFileExist(exactPath2 + "/" + fileName)) {

                            //here apply new changes
                            video.setUseController(true);
                            playerPreview2 = new ExoPlayer.Builder(mContext).build();
                            video.setPlayer(playerPreview2);
                            MediaItem mediaItem = MediaItem.fromUri(exactPath2 + "/" + fileName);
                            playerPreview2.setMediaItem(mediaItem);
                            playerPreview2.prepare();
                            // player.setPlayWhenReady(false);


                            File finalSavedThumbnail = savedThumbnail;
                            String finalFileThumbName = fileThumbName;
                            String finalFileName = fileName;
                            String finalFileThumbName1 = fileThumbName;
                            String finalFileThumbName2 = fileThumbName;
                            sendGrp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String[] dimensions = Constant.calculateImageDimensions(mContext, finalSavedThumbnail, Uri.fromFile(finalSavedThumbnail));
                                    String imageWidthDp = dimensions[0];
                                    String imageHeightDp = dimensions[1];
                                    String aspectRatio = dimensions[2];
                                    binding.messageBox.setText("");
                                    if (messageBoxMy.getText().toString().trim().equals("")) {


                                        Constant.getSfFuncion(getApplicationContext());
                                        final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                        final String senderRoom = senderId + grpIdKey;
                                        final String receiverRoom = grpIdKey + senderId;
                                        Log.d("senderRoom", senderRoom + receiverRoom);
                                        Date d = new Date();
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                        String currentDateTimeString = sdf.format(d);
                                        group_messageModel model = null;


                                        assert modelId != null;
                                        String uniqDate = Constant.getCurrentDate();
                                        if (uniqueDates.add(uniqDate)) {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.video, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), finalFileThumbName2, finalFileThumbName1, "", uniqDate, imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            
                                            // Store message in SQLite pending table before upload
                                            try {
                                                new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(mContext).insertPendingGroupMessage(model);
                                                Log.d("PendingMessages", "Group video message stored in pending table: " + model.getModelId());
                                            } catch (Exception e) {
                                                Log.e("PendingMessages", "Error storing group video message in pending table: " + e.getMessage(), e);
                                            }
                                            
                                            groupMessageList.add(model);
                                        } else {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.video, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), finalFileThumbName2, finalFileThumbName1, "", ":" + uniqDate, imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            
                                            // Store message in SQLite pending table before upload
                                            try {
                                                new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(mContext).insertPendingGroupMessage(model);
                                                Log.d("PendingMessages", "Group video message stored in pending table: " + model.getModelId());
                                            } catch (Exception e) {
                                                Log.e("PendingMessages", "Error storing group video message in pending table: " + e.getMessage(), e);
                                            }
                                            
                                            groupMessageList.add(model);
                                        }


                                        ((Activity)mContext).runOnUiThread(() -> {
                                            groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                                            groupChatAdapter.setLastItemVisible(isLastItemVisible);
                                            binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                                        });

                                        group_messageModel2 model2 = new group_messageModel2(
                                                model.getUid(),
                                                model.getMessage(),
                                                model.getTime(),
                                                model.getDocument(),
                                                model.getDataType(),
                                                model.getExtension(),
                                                model.getName(),
                                                model.getPhone(),
                                                model.getMicPhoto(),
                                                model.getMiceTiming(),
                                                model.getCreatedBy(),
                                                model.getUserName(),
                                                model.getModelId(),
                                                model.getReceiverUid(),
                                                model.getDocSize(),
                                                model.getFileName(),
                                                model.getThumbnail(),
                                                model.getFileNameThumbnail(),
                                                model.getCaption(),
                                                model.getCurrentDate(),
                                                senderRoom,
                                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()// active: 0 = still loading
                                        );

                                        // TODO: active: 0 = still loading
                                        // TODO: active: 1 = completed

//                                        try {
//                                            new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                        } catch (Exception e) {
//                                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                        }

                                        UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, FullImageFile, createdBy);
                                        // Video upload
                                        uploadHelper.uploadContent(Constant.video, GlobalUri, messageBoxMy.getText().toString().trim(), modelId, finalSavedThumbnail, finalFileThumbName, finalFileName, null, null, null, null, null, imageWidthDp, imageHeightDp, aspectRatio);


                                        Constant.dialogLayoutFullScreen.dismiss();
                                        if (playerPreview2 != null) {
                                            playerPreview2.stop();
                                            playerPreview2.release();
                                        }

                                    } else {


                                        Constant.getSfFuncion(getApplicationContext());
                                        final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                        final String senderRoom = senderId + grpIdKey;
                                        final String receiverRoom = grpIdKey + senderId;
                                        Log.d("senderRoom", senderRoom + receiverRoom);
                                        Date d = new Date();
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                        String currentDateTimeString = sdf.format(d);
                                        group_messageModel model = null;


                                        assert modelId != null;
                                        String uniqDate = Constant.getCurrentDate();
                                        if (uniqueDates.add(uniqDate)) {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.video, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), finalFileThumbName2, finalFileThumbName1, messageBoxMy.getText().toString(), uniqDate, imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            
                                            // Store message in SQLite pending table before upload
                                            try {
                                                new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(mContext).insertPendingGroupMessage(model);
                                                Log.d("PendingMessages", "Group video message with caption stored in pending table: " + model.getModelId());
                                            } catch (Exception e) {
                                                Log.e("PendingMessages", "Error storing group video message with caption in pending table: " + e.getMessage(), e);
                                            }
                                            
                                            groupMessageList.add(model);
                                        } else {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.video, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), finalFileThumbName2, finalFileThumbName1, messageBoxMy.getText().toString(), ":" + uniqDate, imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            
                                            // Store message in SQLite pending table before upload
                                            try {
                                                new com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper(mContext).insertPendingGroupMessage(model);
                                                Log.d("PendingMessages", "Group video message with caption stored in pending table: " + model.getModelId());
                                            } catch (Exception e) {
                                                Log.e("PendingMessages", "Error storing group video message with caption in pending table: " + e.getMessage(), e);
                                            }
                                            
                                            groupMessageList.add(model);
                                        }

                                        ((Activity)mContext).runOnUiThread(() -> {
                                            groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                                            groupChatAdapter.setLastItemVisible(isLastItemVisible);
                                            binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                                        });

                                        group_messageModel2 model2 = new group_messageModel2(
                                                model.getUid(),
                                                model.getMessage(),
                                                model.getTime(),
                                                model.getDocument(),
                                                model.getDataType(),
                                                model.getExtension(),
                                                model.getName(),
                                                model.getPhone(),
                                                model.getMicPhoto(),
                                                model.getMiceTiming(),
                                                model.getCreatedBy(),
                                                model.getUserName(),
                                                model.getModelId(),
                                                model.getReceiverUid(),
                                                model.getDocSize(),
                                                model.getFileName(),
                                                model.getThumbnail(),
                                                model.getFileNameThumbnail(),
                                                model.getCaption(),
                                                model.getCurrentDate(),
                                                senderRoom,
                                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()// active: 0 = still loading
                                        );

                                        // TODO: active: 0 = still loading
                                        // TODO: active: 1 = completed

//                                        try {
//                                            new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                        } catch (Exception e) {
//                                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                        }


                                        UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, FullImageFile, createdBy);
                                        // Video upload
                                        uploadHelper.uploadContent(Constant.video, GlobalUri, messageBoxMy.getText().toString().trim(), modelId, finalSavedThumbnail, finalFileThumbName, finalFileName, null, null, null, null, null, imageWidthDp, imageHeightDp, aspectRatio);


                                        Constant.dialogLayoutFullScreen.dismiss();
                                        if (playerPreview2 != null) {
                                            playerPreview2.stop();
                                            playerPreview2.release();
                                        }


                                    }
                                }
                            });

                            play.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    playerPreview2.play();
                                    pause.setVisibility(View.VISIBLE);
                                    play.setVisibility(View.GONE);
                                }
                            });

                            pause.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    playerPreview2.pause();
                                    pause.setVisibility(View.GONE);
                                    play.setVisibility(View.VISIBLE);
                                }
                            });

                            exoProgress.addListener(new TimeBar.OnScrubListener() {
                                @Override
                                public void onScrubStart(TimeBar timeBar, long position) {
                                    // Track current playback state
                                    wasPlaying = playerPreview2.getPlayWhenReady();

                                    // Pause the video while dragging
                                    playerPreview2.setPlayWhenReady(false);
                                }

                                @Override
                                public void onScrubMove(TimeBar timeBar, long position) {
                                    // Optional: update UI while scrubbing
                                    startTime.setText(stringForTime(position));
                                }

                                @Override
                                public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                                    // Seek to the selected position
                                    playerPreview2.seekTo(position);

                                    // Only resume playback if it was playing before
                                    if (wasPlaying) {
                                        playerPreview2.setPlayWhenReady(true);
                                    }
                                }
                            });


                            playerPreview2.addListener(new Player.Listener() {
                                @Override
                                public void onPlaybackStateChanged(int state) {
                                    if (state == Player.STATE_READY) {
                                        long duration = playerPreview2.getDuration();
                                        totalTime.setText(stringForTime(duration));
                                    } else if (state == Player.STATE_ENDED) {
                                        // Reset video to beginning
                                        playerPreview2.seekTo(0);
                                        playerPreview2.setPlayWhenReady(false); // or true to auto-play again

                                        // Reset UI to 0
                                        startTime.setText(stringForTime(0));
                                        exoProgress.setPosition(0);
                                        exoProgress.setDuration(playerPreview2.getDuration()); // optional, just to be accurate

                                        // Update play/pause button state
                                        play.setVisibility(View.VISIBLE);
                                        pause.setVisibility(View.GONE);
                                    }
                                }


                                @Override
                                public void onIsPlayingChanged(boolean isPlaying) {
                                    if (isPlaying) {
                                        // Update play/pause button visibility when video starts playing
                                        play.setVisibility(View.GONE);
                                        pause.setVisibility(View.VISIBLE);

                                        updateProgressAction = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (playerPreview2 != null && playerPreview2.isPlaying()) {
                                                    long currentPos = playerPreview2.getCurrentPosition();
                                                    long duration = playerPreview2.getDuration();

                                                    startTime.setText(stringForTime(currentPos));
                                                    totalTime.setText(stringForTime(duration));
                                                    exoProgress.setDuration(duration);
                                                    exoProgress.setPosition(currentPos);

                                                    handler.postDelayed(this, 500);
                                                }
                                            }
                                        };
                                        handler.post(updateProgressAction);
                                    } else {
                                        // Update play/pause button visibility when video pauses
                                        play.setVisibility(View.VISIBLE);
                                        pause.setVisibility(View.GONE);
                                        handler.removeCallbacks(updateProgressAction);
                                    }
                                }
                            });
                        }
                        backarrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Constant.dialogLayoutFullScreen.dismiss();
                                if (playerPreview2 != null) {
                                    playerPreview2.stop();
                                    playerPreview2.release();
                                }

                            }
                        });
                    } catch (Exception ignored) {
                    }


                } else {
                    Toast.makeText(grpChattingScreen.this, "Please select image", Toast.LENGTH_SHORT).show();
                }


            }

        }
        */
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            modelId = database.getReference().push().getKey();
            GlobalUri = data.getData();

            if (GlobalUri != null) {
                Cursor cursor = this.getContentResolver().query(GlobalUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        docName = cursor.getString(index);
                    }
                    cursor.close();
                }
            }
            if (docName != null) {
                // Use the original file name
                Log.d("docname78", docName);
            }
            if (mStoragetask != null && mStoragetask.isInProgress()) {

                Constant.showCustomToast("Upload in process", findViewById(R.id.includedToast), findViewById(R.id.toastText));

            } else {
                if (GlobalUri != null) {

                    try {
                        String extension;
                        globalFile = null;
                        Log.d("ImageFile000", GlobalUri.getAuthority());
                        Log.d("ImageFile000", GlobalUri.getScheme());


                        File f;
                        if (GlobalUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                            final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                            extension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(GlobalUri));

                        } else {
                            extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(GlobalUri.getPath()))));

                        }
                        Log.d("extension", extension);
                        f = new File(getCacheDir() + "/" + docName);
                        try {
                            InputStream is = getContentResolver().openInputStream(GlobalUri);
                            FileOutputStream fs = new FileOutputStream(f);
                            int read = 0;
                            int bufferSize = 1024;
                            final byte[] buffers = new byte[bufferSize];
                            while ((read = is.read(buffers)) != -1) {
                                fs.write(buffers, 0, read);
                            }
                            is.close();
                            fs.close();

                            Log.d("imageFile111", f.getPath());
                            globalFile = f;


                            String globalName = globalFile.getName();


                            File f2External;
                            String exactPath2;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                f2External = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                                exactPath2 = f2External.getAbsolutePath();

                            } else {
                                f2External = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents");
                                exactPath2 = f2External.getAbsolutePath();
                            }


                            if (!f2External.exists()) {
                                f2External.mkdirs();
                            }

                            if (doesFileExist(exactPath2 + "/" + globalName)) {

                            } else {


                                File imageFile = new File(f2External, globalName);

                                try {
                                    InputStream is3 = getContentResolver().openInputStream(GlobalUri);
                                    FileOutputStream fs3 = new FileOutputStream(imageFile);
                                    int read3 = 0;
                                    int bufferSize3 = 1024;
                                    final byte[] buffers3 = new byte[bufferSize3];
                                    while ((read3 = is3.read(buffers3)) != -1) {
                                        fs3.write(buffers3, 0, read3);
                                    }
                                    is3.close();
                                    fs3.close();

                                    Log.d("imageFile111", imageFile.getPath());
                                } catch (Exception e) {
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", "imageofflineerror: " + e.getMessage());
                                }
                            }


                            // From here selecting screen code

                            Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);
                            Constant.dialogLayoutFullScreen.show();

                            Window window = Constant.dialogLayoutFullScreen.getWindow();
                            if (window != null) {
                                WindowCompat.setDecorFitsSystemWindows(window, false);
                                View rootView = window.getDecorView().findViewById(android.R.id.content);
                                rootView.setFitsSystemWindows(false);
                            }

                            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

                            Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
                                // Restore decor fitsSystemWindows true
                                Window activityWindow = ((Activity) mContext).getWindow();
                                WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
                                View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
                                rootView.setFitsSystemWindows(true);
                            });

                            LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.backarrow);

                            //image
                            ImageView image = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
                            //video
                            PlayerView video = Constant.dialogLayoutFullScreen.findViewById(R.id.video);

                            EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                            LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);

                            // TODO: 18/04/25  messageBoxMy - Start

                            if (!binding.messageBox.getText().toString().isEmpty()) {
                                messageBoxMy.setText(binding.messageBox.getText().toString());
                            }

                            // TODO: 18/04/25  messageBoxMy - End

                            try {

                                Constant.getSfFuncion(getApplicationContext());
                                themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                                sendGrp.setBackgroundTintList(tintList);


                            } catch (Exception i) {

                            }

                            //document
                            LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
                            LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
                            TextView docName = Constant.dialogLayoutFullScreen.findViewById(R.id.docName);
                            TextView size = Constant.dialogLayoutFullScreen.findViewById(R.id.size);

                            image.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            contactContainer.setVisibility(View.GONE);
                            downloadCtrl.setVisibility(View.VISIBLE);


                            if (doesFileExist(exactPath2 + "/" + globalName)) {


                                //here apply new changes
                                docName.setText(globalName);
                                long fileSize = getFileSize(globalFile.getPath());
                                size.setText(getFormattedFileSize(fileSize));


                                sendGrp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        binding.messageBox.setText("");
                                        if (messageBoxMy.getText().toString().trim().equals("")) {


                                            Constant.getSfFuncion(getApplicationContext());
                                            final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                                            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                            final String senderRoom = senderId + grpIdKey;
                                            final String receiverRoom = grpIdKey + senderId;
                                            Log.d("senderRoom", senderRoom + receiverRoom);
                                            Date d = new Date();
                                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                            String currentDateTimeString = sdf.format(d);


                                            assert modelId != null;
                                            long finalfilesize = getFileSize(globalFile.getPath());

                                            String uniqDate = Constant.getCurrentDate();
                                            group_messageModel model;
                                            if (uniqueDates.add(uniqDate)) {

                                                model = new group_messageModel(senderId, "", currentDateTimeString, globalName.toString(), Constant.doc, extension, "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, String.valueOf(getFormattedFileSize(finalfilesize)), docName.getText().toString(), "", "", "", uniqDate, "", "", "", "1", null);
                                                groupMessageList.add(model);

                                            } else {
                                                model = new group_messageModel(senderId, "", currentDateTimeString, globalName.toString(), Constant.doc, extension, "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, String.valueOf(getFormattedFileSize(finalfilesize)), docName.getText().toString(), "", "", "", ":" + uniqDate, "", "", "", "1", null);
                                                groupMessageList.add(model);
                                            }

                                            ((Activity) mContext).runOnUiThread(() -> {
                                                groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                                                groupChatAdapter.setLastItemVisible(isLastItemVisible);
                                                binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                                            });

                                            group_messageModel2 model2 = new group_messageModel2(
                                                    model.getUid(),
                                                    model.getMessage(),
                                                    model.getTime(),
                                                    model.getDocument(),
                                                    model.getDataType(),
                                                    model.getExtension(),
                                                    model.getName(),
                                                    model.getPhone(),
                                                    model.getMicPhoto(),
                                                    model.getMiceTiming(),
                                                    model.getCreatedBy(),
                                                    model.getUserName(),
                                                    model.getModelId(),
                                                    model.getReceiverUid(),
                                                    model.getDocSize(),
                                                    model.getFileName(),
                                                    model.getThumbnail(),
                                                    model.getFileNameThumbnail(),
                                                    model.getCaption(),
                                                    model.getCurrentDate(),
                                                    senderRoom,
                                                    0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                                                    // active: 0 = still loading
                                            );

                                            // TODO: active: 0 = still loading
                                            // TODO: active: 1 = completed

//                                            try {
//                                                new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                                                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                            } catch (Exception e) {
//                                                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                            }


                                            UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, FullImageFile, createdBy);
                                            uploadHelper.uploadContent(Constant.doc, GlobalUri, messageBoxMy.getText().toString().trim(), modelId, null, null, docName.getText().toString(), null, null, null, null, extension, "", "", "");

                                            Constant.dialogLayoutFullScreen.dismiss();

                                        } else {


                                            Constant.getSfFuncion(getApplicationContext());
                                            final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                                            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                            final String senderRoom = senderId + grpIdKey;
                                            final String receiverRoom = grpIdKey + senderId;
                                            Log.d("senderRoom", senderRoom + receiverRoom);
                                            Date d = new Date();
                                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                            String currentDateTimeString = sdf.format(d);


                                            assert modelId != null;
                                            long finalfilesize = getFileSize(globalFile.getPath());

                                            String uniqDate = Constant.getCurrentDate();
                                            group_messageModel model;
                                            if (uniqueDates.add(uniqDate)) {
                                                model = new group_messageModel(senderId, "", currentDateTimeString, globalName.toString(), Constant.doc, extension, "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, String.valueOf(getFormattedFileSize(finalfilesize)), docName.getText().toString(), "", "", messageBoxMy.getText().toString(), uniqDate, "", "", "", "1", null);
                                                groupMessageList.add(model);
                                            } else {
                                                model = new group_messageModel(senderId, "", currentDateTimeString, globalName.toString(), Constant.doc, extension, "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, String.valueOf(getFormattedFileSize(finalfilesize)), docName.getText().toString(), "", "", messageBoxMy.getText().toString(), ":" + uniqDate, "", "", "", "1", null);
                                                groupMessageList.add(model);
                                            }


                                            ((Activity) mContext).runOnUiThread(() -> {
                                                groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                                                groupChatAdapter.setLastItemVisible(isLastItemVisible);
                                                binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                                            });

                                            group_messageModel2 model2 = new group_messageModel2(
                                                    model.getUid(),
                                                    model.getMessage(),
                                                    model.getTime(),
                                                    model.getDocument(),
                                                    model.getDataType(),
                                                    model.getExtension(),
                                                    model.getName(),
                                                    model.getPhone(),
                                                    model.getMicPhoto(),
                                                    model.getMiceTiming(),
                                                    model.getCreatedBy(),
                                                    model.getUserName(),
                                                    model.getModelId(),
                                                    model.getReceiverUid(),
                                                    model.getDocSize(),
                                                    model.getFileName(),
                                                    model.getThumbnail(),
                                                    model.getFileNameThumbnail(),
                                                    model.getCaption(),
                                                    model.getCurrentDate(),
                                                    senderRoom,
                                                    0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount() // active: 0 = still loading
                                            );

                                            // TODO: active: 0 = still loading
                                            // TODO: active: 1 = completed
//
//                                            try {
//                                                new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                                                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                            } catch (Exception e) {
//                                                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                            }

                                            UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, FullImageFile, createdBy);
                                            uploadHelper.uploadContent(Constant.doc, GlobalUri, messageBoxMy.getText().toString().trim(), modelId, null, null, docName.getText().toString(), null, null, null, null, extension, "", "", "");


                                            Constant.dialogLayoutFullScreen.dismiss();


                                        }
                                    }
                                });
                            }

                            backarrow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Constant.dialogLayoutFullScreen.dismiss();

                                }
                            });


                        } catch (Exception e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception ignored) {
                    }


                } else {
                    Toast.makeText(grpChattingScreen.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            modelId = database.getReference().push().getKey();
            File flexible = new File(currentPhotoPath);
            Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(flexible));

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(flexible);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            if (mStoragetask != null && mStoragetask.isInProgress()) {

                Toast.makeText(grpChattingScreen.this, "Upload in process", Toast.LENGTH_SHORT).show();

            } else {
                if (Uri.fromFile(flexible) != null) {


                    try {

                        //for uploading document to mysql
                        globalFile = null;
                        Log.d("ImageFile000", Uri.fromFile(flexible).getAuthority());
                        Log.d("ImageFile000", Uri.fromFile(flexible).getScheme());


                        String extension;
                        File f;
                        if (Uri.fromFile(flexible).getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                            final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                            extension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(Uri.fromFile(flexible)));

                        } else {
                            extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(Uri.fromFile(flexible).getPath()))));

                        }


                        InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(Uri.fromFile(flexible));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "imageStream checker", Toast.LENGTH_SHORT).show();
                        }

                        Bitmap bmpCompresssSize = BitmapFactory.decodeStream(imageStream);
                        Log.d("extensionxcx", extension);

                        String fileName = System.currentTimeMillis() + "." + extension;


                        f = new File(getCacheDir() + "/" + fileName);

                        try {
                            //part1
                            InputStream is = getContentResolver().openInputStream(Uri.fromFile(flexible));
                            FileOutputStream fs = new FileOutputStream(f);
                            int read = 0;
                            int bufferSize = 1024;
                            final byte[] buffers = new byte[bufferSize];
                            while ((read = is.read(buffers)) != -1) {
                                fs.write(buffers, 0, read);
                            }
                            is.close();
                            fs.close();

                            Log.d("imageFile111", f.getPath());
                            globalFile = f;

                            long fileSize = getFileSize(globalFile.getPath());
                            Log.d("File size compressed", getFormattedFileSize(fileSize));


                        } catch (Exception e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onActivityResult: " + e.getMessage());

                        }


                        File f2External;
                        String exactPath2;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            f2External = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                            exactPath2 = f2External.getAbsolutePath();

                        } else {
                            f2External = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
                            exactPath2 = f2External.getAbsolutePath();
                        }


                        if (!f2External.exists()) {
                            f2External.mkdirs();
                        }

                        if (doesFileExist(exactPath2 + "/" + fileName)) {

                        } else {


                            File imageFile = new File(f2External, fileName);

                            try {
                                InputStream is = getContentResolver().openInputStream(Uri.fromFile(flexible));
                                FileOutputStream fs = new FileOutputStream(imageFile);
                                int read = 0;
                                int bufferSize = 1024;
                                final byte[] buffers = new byte[bufferSize];
                                while ((read = is.read(buffers)) != -1) {
                                    fs.write(buffers, 0, read);
                                }
                                is.close();
                                fs.close();

                                Log.d("imageFile111", imageFile.getPath());
                            } catch (Exception e) {
                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "imageofflineerror: " + e.getMessage());
                            }
                        }


                        // From here selecting screen code

                        Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);
                        Constant.dialogLayoutFullScreen.show();

                        Window window = Constant.dialogLayoutFullScreen.getWindow();
                        if (window != null) {
                            WindowCompat.setDecorFitsSystemWindows(window, false);
                            View rootView = window.getDecorView().findViewById(android.R.id.content);
                            rootView.setFitsSystemWindows(false);
                        }

                        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

                        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
                            // Restore decor fitsSystemWindows true
                            Window activityWindow = ((Activity) mContext).getWindow();
                            WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
                            View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
                            rootView.setFitsSystemWindows(true);
                        });

                        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.backarrow);
                        ImageView image = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
                        PlayerView video = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
                        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);

                        // TODO: 18/04/25  messageBoxMy - Start

                        if (!binding.messageBox.getText().toString().isEmpty()) {
                            messageBoxMy.setText(binding.messageBox.getText().toString());
                        }

                        // TODO: 18/04/25  messageBoxMy - End

                        try {

                            Constant.getSfFuncion(getApplicationContext());
                            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                            sendGrp.setBackgroundTintList(tintList);


                        } catch (Exception i) {

                        }
                        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
                        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
                        //visibility
                        image.setVisibility(View.VISIBLE);
                        video.setVisibility(View.GONE);
                        downloadCtrl.setVisibility(View.GONE);
                        contactContainer.setVisibility(View.GONE);
                        if (doesFileExist(exactPath2 + "/" + fileName)) {
                            image.setImageURI(Uri.parse(exactPath2 + "/" + fileName));

                            sendGrp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String[] dimensions = Constant.calculateImageDimensions(mContext, globalFile, GlobalUri);
                                    String imageWidthDp = dimensions[0];
                                    String imageHeightDp = dimensions[1];
                                    String aspectRatio = dimensions[2];
                                    binding.messageBox.setText("");
                                    if (messageBoxMy.getText().toString().trim().equals("")) {


                                        Constant.getSfFuncion(getApplicationContext());
                                        final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                        final String senderRoom = senderId + grpIdKey;
                                        final String receiverRoom = grpIdKey + senderId;
                                        Log.d("senderRoom", senderRoom + receiverRoom);
                                        Date d = new Date();
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                        String currentDateTimeString = sdf.format(d);
                                        group_messageModel model = null;


                                        assert modelId != null;
                                        String uniqDate = Constant.getCurrentDate();
                                        if (uniqueDates.add(uniqDate)) {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), "", "", "", uniqDate, imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            groupMessageList.add(model);
                                        } else {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), "", "", "", ":" + uniqDate, imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            groupMessageList.add(model);
                                        }


                                        ((Activity) mContext).runOnUiThread(() -> {
                                            groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                                            groupChatAdapter.setLastItemVisible(isLastItemVisible);
                                            binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                                        });

                                        group_messageModel2 model2 = new group_messageModel2(
                                                model.getUid(),
                                                model.getMessage(),
                                                model.getTime(),
                                                model.getDocument(),
                                                model.getDataType(),
                                                model.getExtension(),
                                                model.getName(),
                                                model.getPhone(),
                                                model.getMicPhoto(),
                                                model.getMiceTiming(),
                                                model.getCreatedBy(),
                                                model.getUserName(),
                                                model.getModelId(),
                                                model.getReceiverUid(),
                                                model.getDocSize(),
                                                model.getFileName(),
                                                model.getThumbnail(),
                                                model.getFileNameThumbnail(),
                                                model.getCaption(),
                                                model.getCurrentDate(),
                                                senderRoom,
                                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount() // active: 0 = still loading
                                        );

                                        // TODO: active: 0 = still loading
                                        // TODO: active: 1 = completed

//                                        try {
//                                            new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                        } catch (Exception e) {
//                                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                        }


                                        UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, FullImageFile, createdBy);
                                        uploadHelper.uploadContent(Constant.camera, Uri.fromFile(flexible), messageBoxMy.getText().toString().trim(), modelId, null, null, globalFile != null ? globalFile.getName() : (flexible != null ? flexible.getName() : ""), null, null, null, null, null, imageWidthDp, imageHeightDp, aspectRatio);

                                        Constant.dialogLayoutFullScreen.dismiss();

                                    } else {


                                        Constant.getSfFuncion(getApplicationContext());
                                        final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                        final String senderRoom = senderId + grpIdKey;
                                        final String receiverRoom = grpIdKey + senderId;
                                        Log.d("senderRoom", senderRoom + receiverRoom);
                                        Date d = new Date();
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                        String currentDateTimeString = sdf.format(d);
                                        group_messageModel model = null;


                                        assert modelId != null;
                                        String uniqDate = Constant.getCurrentDate();
                                        if (uniqueDates.add(uniqDate)) {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), "", "", messageBoxMy.getText().toString(), uniqDate
                                                    , imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            groupMessageList.add(model);
                                        } else {
                                            model = new group_messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", globalFile.getName(), "", "", messageBoxMy.getText().toString(), ":" + uniqDate
                                                    , imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            groupMessageList.add(model);
                                        }


                                        ((Activity) mContext).runOnUiThread(() -> {
                                            groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                                            groupChatAdapter.setLastItemVisible(isLastItemVisible);
                                            binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                                        });

                                        group_messageModel2 model2 = new group_messageModel2(
                                                model.getUid(),
                                                model.getMessage(),
                                                model.getTime(),
                                                model.getDocument(),
                                                model.getDataType(),
                                                model.getExtension(),
                                                model.getName(),
                                                model.getPhone(),
                                                model.getMicPhoto(),
                                                model.getMiceTiming(),
                                                model.getCreatedBy(),
                                                model.getUserName(),
                                                model.getModelId(),
                                                model.getReceiverUid(),
                                                model.getDocSize(),
                                                model.getFileName(),
                                                model.getThumbnail(),
                                                model.getFileNameThumbnail(),
                                                model.getCaption(),
                                                model.getCurrentDate(),
                                                senderRoom,
                                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount() // active: 0 = still loading
                                        );

                                        // TODO: active: 0 = still loading
                                        // TODO: active: 1 = completed
//
//                                        try {
//                                            new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                        } catch (Exception e) {
//                                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                        }

                                        Constant.dialogLayoutFullScreen.dismiss();


                                        UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, FullImageFile, createdBy);
                                        uploadHelper.uploadContent(Constant.camera, Uri.fromFile(flexible), messageBoxMy.getText().toString().trim(), modelId, null, null, globalFile != null ? globalFile.getName() : (flexible != null ? flexible.getName() : ""), null, null, null, null, null
                                                , imageWidthDp, imageHeightDp, aspectRatio);


//

                                    }
                                }
                            });
                        }


                        backarrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Constant.dialogLayoutFullScreen.dismiss();

                            }
                        });


                    } catch (Exception fg) {
                        Toast.makeText(mContext, "my" + fg.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(grpChattingScreen.this, "Please select image", Toast.LENGTH_SHORT).show();
                }


            }

        }
        if (requestCode == REQUEST_CODE_PICK_CONTACT && resultCode == RESULT_OK && data != null) {
            modelId = database.getReference().push().getKey();
            GlobalUri = data.getData();
            // Retrieve the contact name
            String name = null;
            Cursor cursor = getContentResolver().query(GlobalUri, null, null, null, null);
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }
            cursor.close();

            // Retrieve the contact phone number
            String phoneNumber = null;
            String number = null;
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{String.valueOf(ContentUris.parseId(GlobalUri))}, null);
            if (phoneCursor.moveToFirst()) {
                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                number = phoneNumber.replaceAll("[()\\s-]+", "").trim();
            }
            phoneCursor.close();


// From here selecting screen code

            Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);
            Constant.dialogLayoutFullScreen.show();

            Window window = Constant.dialogLayoutFullScreen.getWindow();
            if (window != null) {
                WindowCompat.setDecorFitsSystemWindows(window, false);
                View rootView = window.getDecorView().findViewById(android.R.id.content);
                rootView.setFitsSystemWindows(false);
            }

            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
                // Restore decor fitsSystemWindows true
                Window activityWindow = ((Activity) mContext).getWindow();
                WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
                View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
                rootView.setFitsSystemWindows(true);
            });

            LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.backarrow);
            ImageView image = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
            PlayerView video = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
            EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
            LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);


            // TODO: 18/04/25  messageBoxMy - Start

            if (!binding.messageBox.getText().toString().isEmpty()) {
                messageBoxMy.setText(binding.messageBox.getText().toString());
            }

            // TODO: 18/04/25  messageBoxMy - End


            try {

                Constant.getSfFuncion(getApplicationContext());
                themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                sendGrp.setBackgroundTintList(tintList);


            } catch (Exception i) {

            }
            LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
            LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
            TextView cName = Constant.dialogLayoutFullScreen.findViewById(R.id.cName);
            TextView cPhone = Constant.dialogLayoutFullScreen.findViewById(R.id.cPhone);
            TextView firstText = Constant.dialogLayoutFullScreen.findViewById(R.id.firstText);


            image.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            downloadCtrl.setVisibility(View.GONE);
            contactContainer.setVisibility(View.VISIBLE);

            /// Add logic here
            cName.setText(name);
            cPhone.setText(number);

            String text = name;
            String[] words = text.split(" ");
            String firstWord = words[0];
            firstText.setText(firstWord);


//            -->


            String finalName1 = name;
            String finalNumber = number;
            String finalName2 = name;
            sendGrp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.messageBox.setText("");
                    if (messageBoxMy.getText().toString().trim().equals("")) {


                        Constant.getSfFuncion(getApplicationContext());
                        final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                        final String senderRoom = senderId + grpIdKey;
                        final String receiverRoom = grpIdKey + senderId;

                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        String currentDateTimeString = sdf.format(d);

                        String uniqDate = Constant.getCurrentDate();
                        group_messageModel model;
                        if (uniqueDates.add(uniqDate)) {
                            model = new group_messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", finalName2, finalNumber, "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", "", "", "", "", uniqDate, "", "", "", "1", null);
                            groupMessageList.add(model);
                        } else {
                            model = new group_messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", finalName2, finalNumber, "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", "", "", "", "", ":" + uniqDate, "", "", "", "1", null);
                            groupMessageList.add(model);
                        }


                        ((Activity) mContext).runOnUiThread(() -> {
                            groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                            groupChatAdapter.setLastItemVisible(isLastItemVisible);
                            binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                        });

                        group_messageModel2 model2 = new group_messageModel2(
                                model.getUid(),
                                model.getMessage(),
                                model.getTime(),
                                model.getDocument(),
                                model.getDataType(),
                                model.getExtension(),
                                model.getName(),
                                model.getPhone(),
                                model.getMicPhoto(),
                                model.getMiceTiming(),
                                model.getCreatedBy(),
                                model.getUserName(),
                                model.getModelId(),
                                model.getReceiverUid(),
                                model.getDocSize(),
                                model.getFileName(),
                                model.getThumbnail(),
                                model.getFileNameThumbnail(),
                                model.getCaption(),
                                model.getCurrentDate(),
                                senderRoom,
                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()// active: 0 = still loading
                        );

                        // TODO: active: 0 = still loading
                        // TODO: active: 1 = completed

//                        try {
//                            new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                        } catch (Exception e) {
//                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                        }

                        UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, FullImageFile, createdBy);
                        // Contact upload
                        uploadHelper.uploadContent(Constant.contact, null, messageBoxMy.getText().toString().trim(), modelId, null, null, null, finalName1, finalNumber, null, null, null, "", "", "", grpIdKey);


                        Constant.dialogLayoutFullScreen.dismiss();

                    } else {
                        Constant.getSfFuncion(getApplicationContext());
                        final String grpIdKey = getIntent().getStringExtra("grpIdKey");
                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                        final String senderRoom = senderId + grpIdKey;
                        final String receiverRoom = grpIdKey + senderId;

                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        String currentDateTimeString = sdf.format(d);

                        String uniqDate = Constant.getCurrentDate();
                        group_messageModel model;
                        if (uniqueDates.add(uniqDate)) {
                            model = new group_messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", finalName2, finalNumber, "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", "", "", "", messageBoxMy.getText().toString(), uniqDate, "", "", "", "1", null);
                            groupMessageList.add(model);
                        } else {
                            model = new group_messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", finalName2, finalNumber, "", "", createdBy, Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", "", "", "", messageBoxMy.getText().toString(), ":" + uniqDate, "", "", "", "1", null);
                            groupMessageList.add(model);
                        }


                        ((Activity) mContext).runOnUiThread(() -> {
                            groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                            groupChatAdapter.setLastItemVisible(isLastItemVisible);
                            binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                        });

                        group_messageModel2 model2 = new group_messageModel2(
                                model.getUid(),
                                model.getMessage(),
                                model.getTime(),
                                model.getDocument(),
                                model.getDataType(),
                                model.getExtension(),
                                model.getName(),
                                model.getPhone(),
                                model.getMicPhoto(),
                                model.getMiceTiming(),
                                model.getCreatedBy(),
                                model.getUserName(),
                                model.getModelId(),
                                model.getReceiverUid(),
                                model.getDocSize(),
                                model.getFileName(),
                                model.getThumbnail(),
                                model.getFileNameThumbnail(),
                                model.getCaption(),
                                model.getCurrentDate(),
                                senderRoom,
                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount() // active: 0 = still loading
                        );

                        // TODO: active: 0 = still loading
                        // TODO: active: 1 = completed

//                        try {
//                            new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                        } catch (Exception e) {
//                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                        }


                        UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, FullImageFile, createdBy);
                        // Contact upload
                        uploadHelper.uploadContent(Constant.contact, null, messageBoxMy.getText().toString().trim(), modelId, null, null, null, finalName1, finalNumber, null, null, null, "", "", "", grpIdKey);


                        Constant.dialogLayoutFullScreen.dismiss();


                    }
                }
            });
            backarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.dialogLayoutFullScreen.dismiss();

                }
            });


        }

        // Handle multi-video selection
        if (requestCode == PICK_VIDEO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Handle WhatsApp-like video picker results
            ArrayList<Uri> newSelectedVideos = data.getParcelableArrayListExtra(WhatsAppLikeVideoPicker.EXTRA_SELECTED_VIDEOS);
            if (newSelectedVideos != null && !newSelectedVideos.isEmpty()) {
                // Add new videos to existing selection
                selectedVideoUris.addAll(newSelectedVideos);

                // Convert URIs to Files
                for (Uri videoUri : newSelectedVideos) {
                    File videoFile = getVideoFileFromUri(videoUri);
                    if (videoFile != null) {
                        selectedVideoFiles.add(videoFile);
                    }
                }

                // Extract caption from video picker (but don't set in main messageBox)
                String caption = data.getStringExtra(WhatsAppLikeVideoPicker.EXTRA_CAPTION);
                if (caption != null && !caption.trim().isEmpty()) {
                    // Store caption for video dialogue use only
                    currentCaption = caption;
                    Log.d("VideoCaptionDebug", "Caption extracted from picker: '" + caption + "' (not set in main messageBox)");
                }

                // Handle multiple video selection
                handleMultipleVideoSelection();
            } else {
                Toast.makeText(grpChattingScreen.this, "No videos selected", Toast.LENGTH_SHORT).show();
            }
        }

        // Handle multi-document selection
        if (requestCode == PICK_DOCUMENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Check current selection count
            int currentCount = selectedDocumentUris.size();
            int maxDocuments = 5;

            if (data.getClipData() != null) {
                // Multiple documents selected
                int count = data.getClipData().getItemCount();

                if (currentCount + count > maxDocuments) {
                    Toast.makeText(this, "You can only select up to 5 documents", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < count; i++) {
                    Uri documentUri = data.getClipData().getItemAt(i).getUri();
                    selectedDocumentUris.add(documentUri);

                    // Convert URI to File
                    File documentFile = getDocumentFileFromUri(documentUri);
                    if (documentFile != null) {
                        selectedDocumentFiles.add(documentFile);
                    }
                }
            } else if (data.getData() != null) {
                // Single document selected
                if (currentCount >= maxDocuments) {
                    Toast.makeText(this, "You can only select up to 5 documents", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri documentUri = data.getData();
                selectedDocumentUris.add(documentUri);

                // Convert URI to File
                File documentFile = getDocumentFileFromUri(documentUri);
                if (documentFile != null) {
                    selectedDocumentFiles.add(documentFile);
                }
            }

            // Handle multiple document selection
            handleMultipleDocumentSelection();
        }

        // Handle multi-contact selection
        if (requestCode == PICK_CONTACT_MULTI_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Log.d("MultiContactSelection", "Custom contact picker result received");

            // Handle custom contact picker results
            ArrayList<WhatsAppLikeContactPicker.ContactInfo> selectedContacts = data.getParcelableArrayListExtra(WhatsAppLikeContactPicker.EXTRA_SELECTED_CONTACTS);
            @SuppressWarnings("unchecked")
            Map<Integer, String> contactCaptions = (Map<Integer, String>) data.getSerializableExtra("contact_captions");

            if (selectedContacts != null && !selectedContacts.isEmpty()) {
                Log.d("MultiContactSelection", "Selected contacts: " + selectedContacts.size());

                // Extract caption from contact picker
                String caption = data.getStringExtra(WhatsAppLikeContactPicker.EXTRA_CAPTION);
                if (caption != null && !caption.trim().isEmpty()) {
                    // Store caption for contact dialogue use only
                    currentCaption = caption;
                    Log.d("ContactCaptionDebug", "Caption extracted from picker: '" + caption + "' (not set in main messageBox)");
                }

                // Use the integrated Firebase upload method for group chats
                String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                String grpIdKey = getIntent().getStringExtra("grpIdKey");

                // Upload contacts directly to Firebase using the integrated method
                WhatsAppLikeContactPicker.uploadContactsToFirebase(
                        mContext,
                        selectedContacts,
                        new HashMap<>(),
                        senderId,
                        null,           // receiverUid (not needed for group chat)
                        null,           // userFTokenKey (not needed for group chat)
                        true,           // isGroupChat
                        grpIdKey        // grpIdKey
                );

                // Update UI after contact upload
                runOnUiThread(() -> {
                    groupChatAdapter.setLastItemVisible(true); // Ensure the last item is visible
                    groupChatAdapter.itemAdd(binding.messageRecView);
                    binding.messageBox.setEnabled(true);
                });

                Log.d("MultiContactSelection", "Contacts uploaded to Firebase successfully");

            } else {
                Log.d("MultiContactSelection", "No contacts selected");
            }
        }

        // Handle single contact selection from bottom sheet
        if (requestCode == PICK_CONTACT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Log.d("ContactSelection", "Single contact picker result received");

            ArrayList<WhatsAppLikeContactPicker.ContactInfo> selectedContacts = data.getParcelableArrayListExtra(WhatsAppLikeContactPicker.EXTRA_SELECTED_CONTACTS);
            @SuppressWarnings("unchecked")
            Map<Integer, String> contactCaptions = (Map<Integer, String>) data.getSerializableExtra("contact_captions");

            if (selectedContacts != null && !selectedContacts.isEmpty()) {
                Log.d("ContactSelection", "Selected contacts: " + selectedContacts.size());

                String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                String grpIdKey = getIntent().getStringExtra("grpIdKey");

                WhatsAppLikeContactPicker.uploadContactsToFirebase(
                        mContext,
                        selectedContacts,
                        new HashMap<>(),
                        senderId,
                        grpIdKey,
                        userFTokenKey,
                        true, // isGroupChat
                        grpIdKey   // grpIdKey
                );

                runOnUiThread(() -> {
                    groupChatAdapter.setLastItemVisible(true);
                    groupChatAdapter.itemAdd(binding.messageRecView);
                    binding.messageBox.setEnabled(true);
                });
            }
        }
    }


    private String getFileExtension(Uri mUri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(mUri));
    }

    // Contact multi-selection methods
    private void showContactSelectionOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Contact Option");
        builder.setItems(new String[]{"Single Contact", "Multiple Contacts (Up to 50)"}, (dialog, which) -> {
            if (which == 0) {
                // Single contact selection
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                SwipeNavigationHelper.startActivityForResultWithSwipe(grpChattingScreen.this, intent, REQUEST_CODE_PICK_CONTACT);
            } else {
                // Multiple contact selection
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                SwipeNavigationHelper.startActivityForResultWithSwipe(grpChattingScreen.this, Intent.createChooser(intent, "Select Contacts"), PICK_CONTACT_MULTI_REQUEST_CODE);
            }
        });
        builder.show();
    }

    private void handleMultipleContactSelection() {
        Log.d("MultiContactSelection", "handleMultipleContactSelection called with " + selectedContactUris.size() + " URIs");

        if (selectedContactUris.isEmpty()) {
            Log.d("MultiContactSelection", "No contact URIs selected, returning");
            return;
        }

        // Clear existing contact info list
        selectedContactInfos.clear();

        // Process each selected contact
        for (Uri contactUri : selectedContactUris) {
            try {
                ContactInfo contactInfo = getContactInfoFromUri(contactUri);
                if (contactInfo != null) {
                    selectedContactInfos.add(contactInfo);
                    Log.d("MultiContactSelection", "Processed contact: " + contactInfo.name + " - " + contactInfo.phoneNumber);
                } else {
                    Log.e("MultiContactSelection", "Failed to get contact info for: " + contactUri);
                }
            } catch (Exception e) {
                Log.e("MultiContactSelection", "Error processing contact: " + e.getMessage(), e);
            }
        }

        Log.d("MultiContactSelection", "Created " + selectedContactInfos.size() + " contact infos");

        // Show multi-contact preview dialog
        showMultiContactPreviewDialog();
    }

    private ContactInfo getContactInfoFromUri(Uri contactUri) {
        try {
            // Get contact name
            String name = null;
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    name = cursor.getString(nameIndex);
                }
                cursor.close();
            }

            if (name == null || name.isEmpty()) {
                name = "Unknown Contact";
            }

            // Get phone number
            String phoneNumber = null;
            Cursor phoneCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{String.valueOf(ContentUris.parseId(contactUri))},
                    null
            );
            if (phoneCursor != null && phoneCursor.moveToFirst()) {
                int phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                if (phoneIndex >= 0) {
                    phoneNumber = phoneCursor.getString(phoneIndex);
                }
                phoneCursor.close();
            }

            // Get email
            String email = null;
            Cursor emailCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{String.valueOf(ContentUris.parseId(contactUri))},
                    null
            );
            if (emailCursor != null && emailCursor.moveToFirst()) {
                int emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                if (emailIndex >= 0) {
                    email = emailCursor.getString(emailIndex);
                }
                emailCursor.close();
            }

            return new ContactInfo(name, phoneNumber, email);

        } catch (Exception e) {
            Log.e("ContactInfo", "Error getting contact info: " + e.getMessage(), e);
            return null;
        }
    }

    private void showMultiContactPreviewDialog() {
        Log.d("MultiContactPreview", "Showing multi-contact preview dialog with " + selectedContactInfos.size() + " contacts");

        Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);

        // Ensure dialog is properly configured before showing
        if (Constant.dialogLayoutFullScreen != null) {
            Constant.dialogLayoutFullScreen.show();

            Window window = Constant.dialogLayoutFullScreen.getWindow();
            if (window != null) {
                WindowCompat.setDecorFitsSystemWindows(window, false);
                View rootView = window.getDecorView().findViewById(android.R.id.content);
                rootView.setFitsSystemWindows(false);

                // Ensure proper window configuration
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

                // Ensure the dialog content is visible
                window.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

                Log.d("MultiContactPreview", "Dialog window configured successfully");
            } else {
                Log.e("MultiContactPreview", "Dialog window is null!");
                return;
            }
        } else {
            Log.e("MultiContactPreview", "dialogLayoutFullScreen is null after dialogueFullScreen call!");
            return;
        }

        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
            Window activityWindow = ((Activity) mContext).getWindow();
            WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
            View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(true);
        });

        if (Constant.dialogLayoutFullScreen != null) {
            // Add a small delay to ensure dialog is fully rendered
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                setupContactPreview();
            }, 100);
        }
    }

    private void setupContactPreview() {
        Log.d("ContactPreview", "Setting up preview with " + selectedContactInfos.size() + " contacts");

        if (selectedContactInfos.isEmpty()) {
            Log.d("ContactPreview", "No contacts selected, returning");
            return;
        }

        Log.d("ContactPreview", "Contact preview setup starting with " + selectedContactInfos.size() + " contacts");
        for (int i = 0; i < selectedContactInfos.size(); i++) {
            grpChattingScreen.ContactInfo contact = selectedContactInfos.get(i);
            Log.d("ContactPreview", "Contact " + i + ": " + contact.name + " - " + contact.phoneNumber);
        }

        // Hide image, video, and document views
        ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        singleImageView.setVisibility(View.GONE);

        ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
        if (viewPager2 != null) {
            viewPager2.setVisibility(View.GONE);
        }

        // Setup horizontal gallery preview (same as image preview)
        setupHorizontalContactGalleryPreview();

        // Setup contact information display with a small delay to ensure ViewPager2 is ready
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            setupContactInfoDisplay();
        }, 100);


        // Setup bottom section
        setupContactBottomSection();
    }

    private void setupHorizontalContactGalleryPreview() {
        Log.d("HorizontalContactGallery", "Setting up horizontal contact gallery with " + selectedContactInfos.size() + " contacts");

        // Setup main contact preview ViewPager2
        ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        if (mainImagePreview != null) {
            mainImagePreview.setVisibility(View.VISIBLE);


            // Show the image counter
            TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
            if (imageCounter != null) {
                imageCounter.setVisibility(View.VISIBLE);
            }

            // Setup adapter for main preview
            GroupContactPreviewAdapter mainAdapter = new GroupContactPreviewAdapter(mContext, selectedContactInfos);
            mainImagePreview.setAdapter(mainAdapter);

            // Force a layout pass to ensure ViewPager2 is properly displayed
            mainImagePreview.requestLayout();
            mainImagePreview.invalidate();

            // Ensure ViewPager2 is properly configured for sliding
            mainImagePreview.setOffscreenPageLimit(1);
            mainImagePreview.setUserInputEnabled(true);

            // Setup page change callback to sync with horizontal RecyclerView
            mainImagePreview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    // Update horizontal RecyclerView selection

                    // Update counter
                    TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
                    if (imageCounter != null) {
                        imageCounter.setText((position + 1) + " / " + selectedContactInfos.size());
                        imageCounter.setVisibility(View.VISIBLE); // Ensure it's always visible
                    }

                    // Update caption EditText with current contact's caption
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        String currentCaption = "";
                        Log.d("ContactPageChange", "Switched to position " + position + ", caption: '" + currentCaption + "'");

                        // Set flag to prevent TextWatcher from saving during programmatic update
                        isUpdatingText = true;

                        if (currentCaption != null) {
                            messageBoxMy.setText(currentCaption);
                            // Position cursor at the end of the text
                            messageBoxMy.setSelection(messageBoxMy.getText().length());
                        } else {
                            messageBoxMy.setText("");
                        }

                        // Reset flag to allow TextWatcher to save again
                        isUpdatingText = false;
                    }
                }
            });
        }


        // Setup send button
        LinearLayout sendGrpLyt = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrpLyt);
        if (sendGrpLyt != null) {
            sendGrpLyt.setVisibility(View.VISIBLE);
            sendGrpLyt.setOnClickListener(v -> {
                // Send all contacts with their captions
                sendMultipleContacts();
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }

        // Setup back arrow
        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.backarrow);
        if (backarrow != null) {
            backarrow.setVisibility(View.VISIBLE);
            backarrow.setOnClickListener(v -> {
                // Clear selections when going back
                selectedContactUris.clear();
                selectedContactInfos.clear();
                new HashMap<>().clear();
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }
    }

    private void setupContactContainerWithSlider(LinearLayout downloadCtrl) {
        // Placeholder - will be implemented
        Log.d("ContactContainer", "setupContactContainerWithSlider called");
    }

    private void setupHorizontalContactThumbnails(RecyclerView horizontalRecyclerView) {
        // Placeholder - will be implemented
        Log.d("ContactThumbnails", "setupHorizontalContactThumbnails called");
    }

    private void setupViewPagerIndicator() {
        // Placeholder - will be implemented
        Log.d("ViewPagerIndicator", "setupViewPagerIndicator called");
    }

    private void setupContactInfoDisplay() {
        // Placeholder - will be implemented
        Log.d("ContactInfo", "setupContactInfoDisplay called");
    }

    private void setupContactBottomSection() {
        // Placeholder - will be implemented
        Log.d("ContactBottom", "setupContactBottomSection called");
    }

    private void sendMultipleContacts() {
        Log.d("MultiContactSend", "Sending " + selectedContactInfos.size() + " contacts with individual captions");

        if (selectedContactInfos.isEmpty()) {
            return;
        }

        // Send each contact with its individual caption
        for (int i = 0; i < selectedContactInfos.size(); i++) {
            ContactInfo contactInfo = selectedContactInfos.get(i);
            String individualCaption = "";
            if (individualCaption == null) {
                individualCaption = "";
            }

            sendSingleContact(contactInfo, individualCaption);
        }

        // Clear selections after sending
        selectedContactUris.clear();
        selectedContactInfos.clear();
        new HashMap<>().clear();
    }

    private void sendSingleContact(ContactInfo contactInfo, String caption) {
        // Generate unique model ID for each contact
        String modelId = database.getReference().push().getKey();

        try {
            // Get current time
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);

            // Get sender info
            Constant.getSfFuncion(getApplicationContext());
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String grpIdKey = getIntent().getStringExtra("grpIdKey");
            final String senderRoom = senderId + grpIdKey;

            String uniqDate = Constant.getCurrentDate();
            group_messageModel model;

            // Create group message model with contact info
            model = new group_messageModel(senderId, "", currentDateTimeString, "",
                    Constant.contact, "", contactInfo.name, contactInfo.phoneNumber, "", "",
                    Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey, "", "", "", "",
                    "", caption, uniqDate, "", "", "", "1", null);

            // Add to group message list
            groupMessageList.add(model);

            // Update UI
            runOnUiThread(() -> {
                groupChatAdapter.setLastItemVisible(true);
                groupChatAdapter.itemAdd(binding.messageRecView);
                binding.messageBox.setEnabled(true);
            });

        } catch (Exception e) {
            Log.e("ContactSend", "Error sending contact: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private File getDocumentFileFromUri(Uri uri) {
        try {
            // Get original filename
            String fileName = null;
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    fileName = cursor.getString(index);
                }
                cursor.close();
            }

            if (fileName == null) {
                fileName = "document_" + System.currentTimeMillis();
            }

            // Create proper storage directory (same as single document picker)
            File storageDir;
            String exactPath;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                storageDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                exactPath = storageDir.getAbsolutePath();
            } else {
                storageDir = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents");
                exactPath = storageDir.getAbsolutePath();
            }

            // Create directory if it doesn't exist
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            // Check if file already exists in storage
            File existingFile = new File(exactPath + "/" + fileName);
            if (existingFile.exists()) {
                Log.d("DocumentFile", "File already exists in storage: " + existingFile.getAbsolutePath());
                return existingFile;
            }

            // Create new file in storage directory
            File documentFile = new File(storageDir, fileName);

            // Copy content from URI to storage file
            try (java.io.InputStream inputStream = getContentResolver().openInputStream(uri);
                 java.io.FileOutputStream outputStream = new java.io.FileOutputStream(documentFile)) {

                if (inputStream != null) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    Log.d("DocumentFile", "Document saved to storage: " + documentFile.getAbsolutePath());
                    return documentFile;
                }
            } catch (Exception e) {
                Log.e("DocumentFile", "Error creating storage file: " + e.getMessage());
            }

        } catch (Exception e) {
            Log.e("DocumentFile", "Error getting document file: " + e.getMessage());
        }

        return null;
    }


    private void handleMultipleDocumentSelection() {
        Log.d("MultiDocumentSelection", "handleMultipleDocumentSelection called with " + selectedDocumentUris.size() + " URIs");

        if (selectedDocumentUris.isEmpty()) {
            Log.d("MultiDocumentSelection", "No URIs selected, returning");
            return;
        }

        // Clear existing files list
        selectedDocumentFiles.clear();

        // Process each selected document
        for (Uri documentUri : selectedDocumentUris) {
            try {
                // Get original filename
                String fileName = null;
                Cursor cursor = this.getContentResolver().query(documentUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        fileName = cursor.getString(index);
                    }
                    cursor.close();
                }

                if (fileName == null) {
                    fileName = "document_" + System.currentTimeMillis();
                }

                // Create File object from URI
                File documentFile = getDocumentFileFromUri(documentUri);
                if (documentFile != null) {
                    selectedDocumentFiles.add(documentFile);
                    Log.d("MultiDocumentSelection", "Processed document: " + fileName + " -> " + documentFile.getAbsolutePath());
                } else {
                    Log.e("MultiDocumentSelection", "Failed to create file for: " + fileName);
                }

            } catch (Exception e) {
                Log.e("MultiDocumentSelection", "Error processing document: " + e.getMessage(), e);
            }
        }

        Log.d("MultiDocumentSelection", "Created " + selectedDocumentFiles.size() + " document files");

        // Show multi-document preview dialog
        showMultiDocumentPreviewDialog();
    }

    private void showMultiDocumentPreviewDialog() {
        // Use the same full screen dialog as image preview
        Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);
        Constant.dialogLayoutFullScreen.show();

        Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false);
            View rootView = window.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(false);
        }

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
            Window activityWindow = ((Activity) mContext).getWindow();
            WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
            View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(true);
        });

        // Setup document preview
        setupDocumentPreview();
    }

    private void setupDocumentPreview() {
        Log.d("DocumentPreview", "Setting up preview with " + selectedDocumentUris.size() + " documents");

        if (selectedDocumentUris.isEmpty()) {
            Log.d("DocumentPreview", "No documents selected, returning");
            return;
        }

        // Hide image and video views
        ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        singleImageView.setVisibility(View.GONE);

        PlayerView singleVideoView = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
        singleVideoView.setVisibility(View.GONE);

        // Hide contact container
        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
        contactContainer.setVisibility(View.GONE);

        // Show document container
        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        downloadCtrl.setVisibility(View.VISIBLE);

        // Setup document container with ViewPager2 for sliding between documents
        setupDocumentContainerWithSlider(downloadCtrl);

        // Setup counter
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setText("1 / " + selectedDocumentUris.size());
            imageCounter.setVisibility(View.VISIBLE);
        }

        // Setup document information display
        setupDocumentInfoDisplay();

        // Note: Navigation arrows are not available in the current layout
        // The ViewPager2 will handle navigation automatically

        // Setup send button
        setupSendButtonForDocuments();

        // Setup back button
        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.backarrow);
        backarrow.setOnClickListener(v -> {
            // Clear selections when going back
            selectedDocumentUris.clear();
            selectedDocumentFiles.clear();
            new HashMap<>().clear();
            Constant.dialogLayoutFullScreen.dismiss();
        });
    }

    private void setupDocumentInfoDisplay() {
        Log.d("DocumentInfo", "Setting up document info display");
        Log.d("DocumentInfo", "selectedDocumentFiles size: " + selectedDocumentFiles.size());
        Log.d("DocumentInfo", "selectedDocumentUris size: " + selectedDocumentUris.size());

        // Get document info views
        TextView docName = Constant.dialogLayoutFullScreen.findViewById(R.id.docName);
        TextView size = Constant.dialogLayoutFullScreen.findViewById(R.id.size);

        Log.d("DocumentInfo", "docName view: " + (docName != null ? "found" : "null"));
        Log.d("DocumentInfo", "size view: " + (size != null ? "found" : "null"));

        if (docName != null && size != null && !selectedDocumentFiles.isEmpty()) {
            // Show document info for the first document
            File firstDocument = selectedDocumentFiles.get(0);
            String fileName = firstDocument.getName();
            long fileSize = firstDocument.length();

            Log.d("DocumentInfo", "First document: " + fileName + " (" + fileSize + " bytes)");

            docName.setText(fileName);
            size.setText(getFormattedFileSize(fileSize));

            Log.d("DocumentInfo", "Displaying document: " + fileName + " (" + getFormattedFileSize(fileSize) + ")");

            // Make sure the views are visible
            docName.setVisibility(View.VISIBLE);
            size.setVisibility(View.VISIBLE);

            Log.d("DocumentInfo", "Document info views set to VISIBLE");
        } else {
            Log.e("DocumentInfo", "Document info views not found or no documents selected");
            if (docName == null) Log.e("DocumentInfo", "docName is null");
            if (size == null) Log.e("DocumentInfo", "size is null");
            if (selectedDocumentFiles.isEmpty())
                Log.e("DocumentInfo", "selectedDocumentFiles is empty");
        }
    }

    private void updateDocumentInfoDisplay(int position) {
        Log.d("DocumentInfo", "Updating document info for position: " + position);

        // Get document info views
        TextView docName = Constant.dialogLayoutFullScreen.findViewById(R.id.docName);
        TextView size = Constant.dialogLayoutFullScreen.findViewById(R.id.size);

        if (docName != null && size != null && position < selectedDocumentFiles.size()) {
            File document = selectedDocumentFiles.get(position);
            String fileName = document.getName();
            long fileSize = document.length();

            docName.setText(fileName);
            size.setText(getFormattedFileSize(fileSize));

            Log.d("DocumentInfo", "Updated document info: " + fileName + " (" + getFormattedFileSize(fileSize) + ")");
        } else {
            Log.e("DocumentInfo", "Cannot update document info - views not found or invalid position");
        }
    }

    private void setupDocumentContainerWithSlider(LinearLayout downloadCtrl) {
        // Clear existing views
        downloadCtrl.removeAllViews();

        // Create ViewPager2 for document sliding
        ViewPager2 documentViewPager = new ViewPager2(mContext);
        documentViewPager.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Create adapter for document preview
        DocumentDownloadPreviewAdapter adapter = new DocumentDownloadPreviewAdapter(mContext, selectedDocumentUris);
        documentViewPager.setAdapter(adapter);

        // Setup page change callback
        documentViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Update counter
                TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
                if (imageCounter != null) {
                    imageCounter.setText((position + 1) + " / " + selectedDocumentUris.size());
                }

                // Update document info display
                updateDocumentInfoDisplay(position);

                // Update caption EditText with current document's caption
                EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                if (messageBoxMy != null) {
                    String currentCaption = "";
                    Log.d("DocumentPageChange", "Switched to document position " + position + ", caption: '" + currentCaption + "'");
                    Log.d("DocumentPageChange", "Current new HashMap<>() map: " + new HashMap<>().toString());

                    // Set flag to prevent TextWatcher from saving during programmatic update
                    // Removed individual caption logic

                    if (currentCaption != null) {
                        messageBoxMy.setText(currentCaption);
                        // Position cursor at the end of the text
                        messageBoxMy.setSelection(messageBoxMy.getText().length());
                    } else {
                        messageBoxMy.setText("");
                    }

                    // Reset flag to allow TextWatcher to save again
                    // Removed individual caption logic
                }

                // Update horizontal RecyclerView selection
                // (Implementation would be similar to existing horizontal gallery)
            }
        });

        downloadCtrl.addView(documentViewPager);
    }

    private void setupSendButtonForDocuments() {
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);

        // Setup theme color
        try {
            Constant.getSfFuncion(getApplicationContext());
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themColor));
            sendGrp.setBackgroundTintList(tintList);
        } catch (Exception i) {
            // Handle exception
        }

        // Pre-fill message box
        if (!binding.messageBox.getText().toString().isEmpty()) {
            messageBoxMy.setText(binding.messageBox.getText().toString());
        }


        sendGrp.setOnClickListener(v -> {
            Log.d("SendButton", "Send button clicked for documents!");
            Log.d("SendButton", "Current caption: '" + currentCaption + "'");
            // Send multiple documents
            sendMultipleDocumentsWithIndividualCaptions();

            binding.messageBox.setText("");

            // Dismiss dialog after sending
            if (Constant.dialogLayoutFullScreen != null) {
                Constant.dialogLayoutFullScreen.dismiss();
                Log.d("SendButton", "Dialog dismissed after sending documents");
            }
        });
    }

    private void sendMultipleDocuments(String caption) {
        Log.d("MultiDocumentSend", "Sending " + selectedDocumentUris.size() + " documents with caption: " + caption);

        if (selectedDocumentUris.isEmpty()) {
            return;
        }

        // Send each document
        for (int i = 0; i < selectedDocumentUris.size(); i++) {
            Uri documentUri = selectedDocumentUris.get(i);
            File documentFile = selectedDocumentFiles.get(i);

            // Use caption only for the first document, empty for others (like WhatsApp)
            String documentCaption = (i == 0) ? caption : "";

            sendSingleDocument(documentUri, documentFile, documentCaption);
        }

        // Clear selections after sending
        selectedDocumentUris.clear();
        selectedDocumentFiles.clear();
        new HashMap<>().clear();
    }

    private void sendMultipleDocumentsWithIndividualCaptions() {
        Log.d("MultiDocumentSend", "Sending " + selectedDocumentUris.size() + " documents with individual captions");
        Log.d("MultiDocumentSend", "new HashMap<>() map size: " + new HashMap<>().size());
        Log.d("MultiDocumentSend", "new HashMap<>() map contents: " + new HashMap<>().toString());

        if (selectedDocumentUris.isEmpty()) {
            return;
        }

        // Send each document with its individual caption
        for (int i = 0; i < selectedDocumentUris.size(); i++) {
            Uri documentUri = selectedDocumentUris.get(i);
            File documentFile = selectedDocumentFiles.get(i);

            // Get individual caption for this document
            String individualCaption = "";
            if (individualCaption == null) {
                individualCaption = "";
                Log.d("IndividualCaption", "Document " + i + " caption was null, using empty string");
            }

            Log.d("MultiDocumentSend", "Sending document " + (i + 1) + " with caption: '" + individualCaption + "'");
            sendSingleDocument(documentUri, documentFile, individualCaption);
        }

        // Clear selections after sending
        selectedDocumentUris.clear();
        selectedDocumentFiles.clear();
        new HashMap<>().clear();
    }

    private ViewPager2 findViewPagerInDownloadCtrl() {
        // Try to find ViewPager2 in the downloadCtrl
        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null && downloadCtrl.getChildCount() > 0) {
            for (int i = 0; i < downloadCtrl.getChildCount(); i++) {
                View child = downloadCtrl.getChildAt(i);
                if (child instanceof ViewPager2) {
                    return (ViewPager2) child;
                }
            }
        }
        return null;
    }

    private void sendSingleDocument(Uri documentUri, File documentFile, String caption) {
        // Generate unique model ID for each document
        String modelId = database.getReference().push().getKey();

        try {
            // Get document name and size
            String fileName = documentFile.getName();
            long fileSize = documentFile.length();

            // Get current time
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);

            // Get sender info
            Constant.getSfFuncion(getApplicationContext());
            final String grpIdKey = getIntent().getStringExtra("grpIdKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String senderRoom = senderId + grpIdKey;

            String uniqDate = Constant.getCurrentDate();
            group_messageModel model;

            if (uniqueDates.add(uniqDate)) {
                model = new group_messageModel(senderId, caption, currentDateTimeString, "",
                        Constant.doc, "", "", "", "", "", createdBy,
                        Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey,
                        String.valueOf(fileSize), fileName, "", "", caption, uniqDate, "", "", "", "1", null);
                groupMessageList.add(model);
            } else {
                model = new group_messageModel(senderId, caption, currentDateTimeString, "",
                        Constant.doc, "", "", "", "", "", createdBy,
                        Constant.getSF.getString(Constant.full_name, ""), modelId, grpIdKey,
                        String.valueOf(fileSize), fileName, "", "", caption, ":" + uniqDate, "", "", "", "1", null);
                groupMessageList.add(model);
            }

            // Update UI
            ((Activity) mContext).runOnUiThread(() -> {
                groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                groupChatAdapter.setLastItemVisible(isLastItemVisible);
                binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
            });

            // Upload document using UploadHelper
            UploadHelper uploadHelper = new UploadHelper(mContext, documentFile, null, createdBy);
            uploadHelper.uploadContent(
                    Constant.doc,
                    documentUri,
                    caption,
                    modelId,
                    null,
                    null,
                    fileName,
                    null,
                    null,
                    null,
                    null,
                    getFileExtension(documentUri),
                    "", "", "");

        } catch (Exception e) {
            Log.e("DocumentSend", "Error sending document: " + e.getMessage(), e);
            Toast.makeText(mContext, "Error sending document", Toast.LENGTH_SHORT).show();
        }
    }

    private void startRecording() {
        chronometerBtm.setBase(SystemClock.elapsedRealtime());
        chronometerBtm.start();
        // elapsedTimeTextView.setText("00:00");
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        File audiosDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "Enclosure/Media/Audios");

// Ensure directory exists
        if (!audiosDir.exists()) {
            audiosDir.mkdirs();
        }

// Generate filename with timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String finalName = timeStamp + ".mp3";

// Full file path
        File audioFile = new File(audiosDir, finalName);

        recorder.setOutputFile(audioFile.getAbsolutePath());
        mFilePath = audioFile;

        countDownTimer = new CountDownTimer(60000, 1000) { // 5 minutes countdown
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("millisUntilFinished", String.valueOf(millisUntilFinished));
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                time = String.format("%02d:%02d", minutes, seconds);
                int progress = (int) (millisUntilFinished / (60000 / 100));
                Log.d("time", time);

                progressbar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                sendAndStopRecording();
                bottomSheetDialogRec.dismiss();
                chronometerBtm.stop();
                Constant.ObjectAnimator(binding.sendGrp, 1f, 1f, 1f);
                Constant.animatorSet.start();
                countDownTimer.cancel();
            }
        }.start();

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendAndStopRecording() {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
            Log.d("time2", time);

            String timeString = time; // Example time value
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            long totalSeconds = 0;
            try {
                Date date = dateFormat.parse(timeString);
                totalSeconds = date.getTime() / 1000;
                System.out.println("Total seconds: " + totalSeconds);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long result = 0;


            result = 59 - totalSeconds;

            int value = (int) result; // Example single digit value
            String formattedValue = String.format("%02d", value);
            System.out.println("Formatted value: " + formattedValue);

            String finalTime = "00 : " + String.valueOf(formattedValue);
            Log.d("result", String.valueOf(finalTime));


            Constant.getSfFuncion(getApplicationContext());
            final String grpIdKey = getIntent().getStringExtra("grpIdKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String senderRoom = senderId + grpIdKey;
            final String receiverRoom = grpIdKey + senderId;
            Log.d("senderRoom", senderRoom + receiverRoom);
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);
            Log.d("one33", "1", null);

            int elapsed = (int) (SystemClock.elapsedRealtime() - chronometerBtm.getBase());
            //  Log.d("elapsedMillis", String.valueOf(elapsed));


            String modelIdNew = database.getReference().push().getKey();

            assert modelIdNew != null;

            String uniqDate = Constant.getCurrentDate();

            // Extract filename from file path for voice audio
            String fileName = mFilePath.getName();
            Log.d("VoiceAudioDebug", "Group chat - Extracted fileName: " + fileName);

            group_messageModel model;
            if (uniqueDates.add(uniqDate)) {
                model = new group_messageModel(senderId, "", currentDateTimeString, mFilePath.toString(), Constant.voiceAudio, "", "", "", finalTime, profilepic, createdBy, Constant.getSF.getString(Constant.full_name, ""), modelIdNew, grpIdKey, "", fileName, "", "", "", uniqDate, "", "", "", "1", null);
                groupMessageList.add(model);
            } else {
                model = new group_messageModel(senderId, "", currentDateTimeString, mFilePath.toString(), Constant.voiceAudio, "", "", "", finalTime, profilepic, createdBy, Constant.getSF.getString(Constant.full_name, ""), modelIdNew, grpIdKey, "", fileName, "", "", "", ":" + uniqDate, "", "", "", "1", null);
                groupMessageList.add(model);
            }


            ((Activity) mContext).runOnUiThread(() -> {
                groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                groupChatAdapter.setLastItemVisible(isLastItemVisible);
                binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
            });

            group_messageModel2 model2 = new group_messageModel2(
                    model.getUid(),
                    model.getMessage(),
                    model.getTime(),
                    model.getDocument(),
                    model.getDataType(),
                    model.getExtension(),
                    model.getName(),
                    model.getPhone(),
                    model.getMicPhoto(),
                    model.getMiceTiming(),
                    model.getCreatedBy(),
                    model.getUserName(),
                    model.getModelId(),
                    model.getReceiverUid(),
                    model.getDocSize(),
                    model.getFileName(),
                    model.getThumbnail(),
                    model.getFileNameThumbnail(),
                    model.getCaption(),
                    model.getCurrentDate(),
                    senderRoom,
                    0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()// active: 0 = still loading
            );

            // TODO: active: 0 = still loading
            // TODO: active: 1 = completed

//            try {
//                new DatabaseHelper(getApplicationContext()).insertMessageGroup(model2);
//                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//            } catch (Exception e) {
//                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//            }


            UploadHelper uploadHelper = new UploadHelper(mContext, globalFile, mFilePath, createdBy);
            Uri uri = Uri.fromFile(mFilePath);

            // Debug the fileName before calling uploadContent
            Log.d("VoiceAudioDebug", "=== BEFORE UPLOADHELPER CALL ===");
            Log.d("VoiceAudioDebug", "fileName variable: '" + fileName + "'");
            Log.d("VoiceAudioDebug", "fileName length: " + (fileName != null ? fileName.length() : "null"));
            Log.d("VoiceAudioDebug", "fileName isEmpty: " + (fileName != null ? fileName.isEmpty() : "null"));
            Log.d("VoiceAudioDebug", "mFilePath: " + mFilePath.getAbsolutePath());
            Log.d("VoiceAudioDebug", "mFilePath.getName(): " + mFilePath.getName());
            Log.d("VoiceAudioDebug", "UploadHelper instance: " + uploadHelper.hashCode());
            Log.d("VoiceAudioDebug", "================================");

            uploadHelper.uploadContent(Constant.voiceAudio, uri, null, modelIdNew, null, null, mFilePath.getName(), null, null, finalTime, null, null, "", "", "");

            Log.d("VoiceAudioDebug", "UploadHelper called with fileName: " + fileName);


            countDownTimer.cancel();

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public void cancelRecording() {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
            countDownTimer.cancel();
            mFilePath.delete();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }

    public static String getFormattedFileSize(long fileSize) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = fileSize;

        while (size > 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", size, units[unitIndex]);
    }

    private File getVideoFileFromUri(Uri uri) {
        try {
            String[] projection = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                if (filePath != null) {
                    return new File(filePath);
                }
            }
        } catch (Exception e) {
            Log.e("VideoPicker", "Error getting video file: " + e.getMessage());
        }

        return null;
    }

    private void handleMultipleVideoSelection() {
        Log.d("MultiVideoSelection", "handleMultipleVideoSelection called with " + selectedVideoUris.size() + " URIs");

        if (selectedVideoUris.isEmpty()) {
            Log.d("MultiVideoSelection", "No URIs selected, returning");
            return;
        }

        // Process each selected video
        for (Uri videoUri : selectedVideoUris) {
            try {
                String extension;
                File f, f2;

                if (videoUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    extension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(videoUri));
                } else {
                    extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(videoUri.getPath()))));
                }

                // Get original filename
                String fileName = null;
                Cursor cursor = this.getContentResolver().query(videoUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        fileName = cursor.getString(index);
                    }
                    cursor.close();
                }

                if (fileName == null) {
                    fileName = "video_" + System.currentTimeMillis() + "." + extension;
                }

                // Create video file
                f = new File(getCacheDir() + "/" + fileName);

                // Copy video to cache directory
                InputStream videoStream = getContentResolver().openInputStream(videoUri);
                FileOutputStream fos = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = videoStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                videoStream.close();
                fos.close();

                // Add to selected video files
                selectedVideoFiles.add(f);

                Log.d("MultiVideoSelection", "Processed video: " + fileName);

            } catch (Exception e) {
                Log.e("MultiVideoSelection", "Error processing video: " + e.getMessage(), e);
            }
        }

        // Initialize captions for all videos
        for (int i = 0; i < selectedVideoUris.size(); i++) {
            if (!new HashMap<>().containsKey(i)) {
                new HashMap<>().put(i, "");
            }
        }

        // Setup video preview
        setupMultiVideoPreview();
    }

    private void setupMultiVideoPreview() {
        Log.d("MultiVideoPreview", "Setting up preview with " + selectedVideoUris.size() + " videos");

        if (selectedVideoUris.isEmpty()) {
            Log.d("MultiVideoPreview", "No videos selected, returning");
            return;
        }

        // Show video preview dialog
        showMultiVideoPreviewDialog();
    }

    private void showMultiVideoPreviewDialog() {
        Log.d("VIDEO_DIALOG_SHOW", "grpChattingScreen.showMultiVideoPreviewDialog called");
        // Create and show video preview dialog
        Constant.dialogLayoutFullScreen = new Dialog(grpChattingScreen.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ((android.app.Dialog) Constant.dialogLayoutFullScreen).setContentView(R.layout.dialogue_video_preview);
        Log.d("VIDEO_DIALOG_SHOW", "About to show multi-video dialog");

        // Make dialog full screen
        Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        Constant.dialogLayoutFullScreen.show();

        // Set caption in dialogue messageBoxMy (from stored currentCaption)
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        Log.d("VideoCaptionDebug", "Stored currentCaption: '" + currentCaption + "'");
        Log.d("VideoCaptionDebug", "messageBoxMy found: " + (messageBoxMy != null));
        if (messageBoxMy != null && !currentCaption.isEmpty()) {
            messageBoxMy.setText(currentCaption);
            Log.d("VideoCaptionDebug", "Caption set in dialogue: '" + messageBoxMy.getText().toString() + "'");
        } else {
            Log.d("VideoCaptionDebug", "Caption not set - messageBoxMy null: " + (messageBoxMy == null) + ", currentCaption empty: " + currentCaption.isEmpty());
        }

        // Setup video preview
        setupHorizontalVideoPreview();
    }

    private void setupHorizontalVideoPreview() {
        Log.d("HorizontalVideoGallery", "Setting up horizontal video gallery with " + selectedVideoUris.size() + " videos");

        // Setup main video preview ViewPager2
        ViewPager2 mainVideoPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainVideoPreview);
        if (mainVideoPreview != null) {
            mainVideoPreview.setVisibility(View.VISIBLE);

            // Setup adapter for main preview
            MainVideoPreviewAdapter mainAdapter = new MainVideoPreviewAdapter(mContext, selectedVideoUris);
            mainVideoPreview.setAdapter(mainAdapter);

            // Setup page change callback to sync with horizontal RecyclerView
            mainVideoPreview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    // Update horizontal RecyclerView selection
                    RecyclerView horizontalRecyclerView = Constant.dialogLayoutFullScreen.findViewById(R.id.horizontalVideoRecyclerView);
                    if (horizontalRecyclerView != null) {
                        VideoPreviewAdapter adapter = (VideoPreviewAdapter) horizontalRecyclerView.getAdapter();
                        if (adapter != null) {
                            // Update selection in adapter if needed
                        }
                    }

                    // Update counter
                    TextView videoCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.videoCounter);
                    if (videoCounter != null) {
                        videoCounter.setText((position + 1) + " / " + selectedVideoUris.size());
                    }

                    // Update caption EditText with current video's caption
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        String currentCaption = "";
                        Log.d("VideoPageChange", "Switched to position " + position + ", caption: '" + currentCaption + "'");
                        Log.d("VideoPageChange", "Current new HashMap<>() map: " + new HashMap<>().toString());

                        // Set flag to prevent TextWatcher from saving during programmatic update
                        // Removed individual caption logic

                        if (currentCaption != null) {
                            messageBoxMy.setText(currentCaption);
                            // Position cursor at the end of the text
                            messageBoxMy.setSelection(messageBoxMy.getText().length());
                        } else {
                            messageBoxMy.setText("");
                        }

                        // Reset flag after update
                        // Removed individual caption logic
                    }
                }
            });

            // Setup click listener for main preview
            mainAdapter.setOnVideoClickListener(position -> {
                // Handle video play/pause
                // This will be handled by the VideoView in the adapter
            });
        }

        // Setup horizontal video thumbnails RecyclerView
        RecyclerView horizontalRecyclerView = Constant.dialogLayoutFullScreen.findViewById(R.id.horizontalVideoRecyclerView);
        if (horizontalRecyclerView != null) {
            horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

            // Setup adapter
            VideoPreviewAdapter adapter = new VideoPreviewAdapter(mContext, selectedVideoUris, selectedVideoFiles);
            horizontalRecyclerView.setAdapter(adapter);

            // Setup click listener for thumbnails
            adapter.setOnVideoClickListener(position -> {
                // Switch to selected video
                if (mainVideoPreview != null) {
                    mainVideoPreview.setCurrentItem(position, true);
                }

                // Update counter
                TextView videoCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.videoCounter);
                if (videoCounter != null) {
                    videoCounter.setText((position + 1) + " / " + selectedVideoUris.size());
                }

                // Update caption EditText
                EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                if (messageBoxMy != null) {
                    // Use class variable for caption persistence
                    Log.d("VideoThumbnailClick", "Clicked thumbnail " + position + ", caption: '" + currentCaption + "'");

                    // Set flag to prevent TextWatcher from saving during programmatic update
                    // Removed individual caption logic

                    if (currentCaption != null) {
                        messageBoxMy.setText(currentCaption);
                        messageBoxMy.setSelection(messageBoxMy.getText().length());
                    } else {
                        messageBoxMy.setText("");
                    }

                    // Reset flag after update
                    // Removed individual caption logic
                }
            });
        }

        // Setup caption EditText with TextWatcher
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null) {
            Log.d("VideoTextWatcherSetup", "Setting up TextWatcher for messageBoxMy");

            // Clear any existing listeners
            messageBoxMy.clearFocus();

            if (messageBoxMy.getText() != null) {
                messageBoxMy.getText().clear();
            }

            Log.d("VideoTextWatcherSetup", "messageBoxMy is not null, adding TextWatcher");

            messageBoxMy.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d("VideoTextWatcher", "beforeTextChanged: " + s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("VideoTextWatcher", "onTextChanged: " + s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.d("VideoTextWatcher", "afterTextChanged triggered - false: " + false + ", text: " + s.toString());

                    if (!false && mainVideoPreview != null) {
                        int currentPosition = mainVideoPreview.getCurrentItem();
                        String caption = s.toString().trim();
                        new HashMap<>().put(currentPosition, caption);
                        Log.d("VideoCaptionSave", "Saved caption for video " + currentPosition + ": '" + caption + "'");
                        Log.d("VideoCaptionSave", "Current new HashMap<>() map: " + new HashMap<>().toString());
                    } else {
                        Log.d("VideoCaptionSave", "Skipping save due to false flag");
                    }
                }
            });

            Log.d("VideoTextWatcherSetup", "TextWatcher added successfully");
        }

        // Setup send button
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        if (sendGrp != null) {
            sendGrp.setOnClickListener(v -> {
                Log.d("VideoSendButton", "=== MANUAL VIDEO CAPTION SAVE START ===");

                // MANUAL CAPTION SAVE - Force save all captions before sending
                ViewPager2 currentPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainVideoPreview);
                if (currentPreview != null) {
                    int currentPosition = currentPreview.getCurrentItem();
                    String currentCaption = messageBoxMy.getText().toString().trim();
                    new HashMap<>().put(currentPosition, currentCaption);
                    Log.d("VideoSendButton", "Saving final caption for position " + currentPosition + ": '" + currentCaption + "'");
                }

                // Force save captions for ALL videos by simulating text input
                for (int i = 0; i < selectedVideoUris.size(); i++) {
                    if (!new HashMap<>().containsKey(i)) {
                        new HashMap<>().put(i, "");
                        Log.d("VideoSendButton", "Added empty caption for missing position " + i);
                    }
                }

                Log.d("VideoSendButton", "Final new HashMap<>() map before sending: " + new HashMap<>().toString());
                Log.d("VideoSendButton", "=== MANUAL VIDEO CAPTION SAVE END ===");

                // Create a copy of captions to prevent race conditions
                Map<Integer, String> captionsCopy = new HashMap<>(new HashMap<>());
                Map<Integer, String> originalCaptions = new HashMap<>(new HashMap<>());

                // Temporarily replace new HashMap<>() with copy
                new HashMap<>().clear();
                new HashMap<>().putAll(captionsCopy);

                sendMultipleVideos(currentCaption);

                // Restore original captions
                new HashMap<>().clear();
                new HashMap<>().putAll(originalCaptions);

                // Clear selection after sending
                selectedVideoUris.clear();
                selectedVideoFiles.clear();
                new HashMap<>().clear();
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }

        // Setup back button
        LinearLayout backButton = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                // Clear selection when dialog is dismissed
                selectedVideoUris.clear();
                selectedVideoFiles.clear();
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }
    }

    private void sendMultipleVideos(String caption) {
        if (selectedVideoUris.isEmpty() || selectedVideoFiles.isEmpty()) {
            return;
        }

        Constant.getSfFuncion(getApplicationContext());
        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
        final String grpIdKey = getIntent().getStringExtra("grpIdKey");
        final String senderRoom = senderId + grpIdKey;
        final String receiverRoom = grpIdKey + senderId;

        Log.d("senderRoom", senderRoom + receiverRoom);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);

        // Send each video
        Log.d("SendMultipleVideos", "Starting to send " + selectedVideoUris.size() + " videos");
        Log.d("SendMultipleVideos", "new HashMap<>() map size: " + new HashMap<>().size());
        Log.d("SendMultipleVideos", "new HashMap<>() map contents: " + new HashMap<>().toString());
        Log.d("SendMultipleVideos", "=== DETAILED VIDEO CAPTION ANALYSIS ===");
        for (Map.Entry<Integer, String> entry : new HashMap<Integer, String>().entrySet()) {
            Log.d("SendMultipleVideos", "Video " + entry.getKey() + " caption: '" + entry.getValue() + "' (length: " + entry.getValue().length() + ")");
        }
        Log.d("SendMultipleVideos", "=== END VIDEO CAPTION ANALYSIS ===");

        for (int i = 0; i < selectedVideoUris.size(); i++) {
            Uri videoUri = selectedVideoUris.get(i);
            File videoFile = selectedVideoFiles.get(i);

            // Get individual caption for this video
            String individualCaption = caption;
            if (individualCaption == null) {
                individualCaption = "";
                Log.d("VideoIndividualCaption", "Video " + i + " caption was null, using empty string");
            } else {
                Log.d("VideoIndividualCaption", "Video " + i + " caption: '" + individualCaption + "'");
            }

            // Generate unique modelId for each video
            String videoModelId = database.getReference().push().getKey();

            // Debug: Log the individualCaption right before creating messageModel
            Log.d("VideoMessageModelCreation", "Creating messageModel for video " + i + " with caption: '" + individualCaption + "'");

            // Create video thumbnail
            File savedThumbnail = null;
            String fileThumbName = null;
            try {
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoFile.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
                if (thumbnail != null) {
                    // Create unique thumbnail name
                    String thumbnailName = "thumb_" + videoModelId + ".png";
                    savedThumbnail = FileUtils.saveBitmapToFile(mContext, thumbnail, thumbnailName);
                    fileThumbName = thumbnailName;
                    Log.d("VideoThumbnail", "Created thumbnail for video " + i + ": " + savedThumbnail.getAbsolutePath());
                } else {
                    Log.e("VideoThumbnail", "Failed to create thumbnail for video " + i);
                }
            } catch (Exception e) {
                Log.e("VideoThumbnail", "Error creating thumbnail for video " + i + ": " + e.getMessage(), e);
            }

            // Save video to local storage
            File localVideoFile = null;
            try {
                File videoDir = getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Videos");
                localVideoFile = new File(videoDir, videoFile.getName());

                // Copy video to local storage if it doesn't exist
                if (!localVideoFile.exists()) {
                    copyUriToFile(videoUri, localVideoFile);
                    Log.d("VideoLocalStorage", "Saved video to local storage: " + localVideoFile.getAbsolutePath());
                } else {
                    Log.d("VideoLocalStorage", "Video already exists in local storage: " + localVideoFile.getAbsolutePath());
                }
            } catch (Exception e) {
                Log.e("VideoLocalStorage", "Error saving video to local storage: " + e.getMessage(), e);
                // Fallback to original video file if local storage fails
                localVideoFile = videoFile;
            }

            // Save thumbnail to local storage
            File localThumbnailFile = null;
            if (savedThumbnail != null && savedThumbnail.exists()) {
                try {
                    File thumbnailDir = getExternalStorageDir(Environment.DIRECTORY_PICTURES, "Enclosure/Media/Thumbnail");
                    localThumbnailFile = new File(thumbnailDir, fileThumbName);

                    // Copy thumbnail to local storage if it doesn't exist
                    if (!localThumbnailFile.exists()) {
                        copyFile(savedThumbnail, localThumbnailFile);
                        Log.d("ThumbnailLocalStorage", "Saved thumbnail to local storage: " + localThumbnailFile.getAbsolutePath());
                    } else {
                        Log.d("ThumbnailLocalStorage", "Thumbnail already exists in local storage: " + localThumbnailFile.getAbsolutePath());
                    }
                } catch (Exception e) {
                    Log.e("ThumbnailLocalStorage", "Error saving thumbnail to local storage: " + e.getMessage(), e);
                    // Fallback to original thumbnail file
                    localThumbnailFile = savedThumbnail;
                }
            }

            // Calculate video dimensions using thumbnail if available, otherwise use video file
            String[] dimensions;
            if (savedThumbnail != null && savedThumbnail.exists()) {
                // Use thumbnail for dimension calculation
                dimensions = Constant.calculateImageDimensions(mContext, savedThumbnail, Uri.fromFile(savedThumbnail));
            } else {
                // Fallback to video file
                dimensions = Constant.calculateImageDimensions(mContext, videoFile, videoUri);
            }
            String videoWidthDp = dimensions[0];
            String videoHeightDp = dimensions[1];
            String aspectRatio = dimensions[2];

            Log.d("VideoDimensions", "Video " + i + " - Width: " + videoWidthDp + "dp, Height: " + videoHeightDp + "dp, AspectRatio: " + aspectRatio);

            // Create message model
            String uniqDate = Constant.getCurrentDate();
            ArrayList<emojiModel> emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));

            group_messageModel model = new group_messageModel(
                    senderId,
                    "",
                    currentDateTimeString,
                    localVideoFile.getAbsolutePath(),
                    Constant.video,
                    "", "", "", "", "",
                    senderId,
                    Constant.getSF.getString(Constant.full_name, ""),
                    videoModelId,
                    grpIdKey,
                    "",
                    localVideoFile.getName(),
                    localThumbnailFile != null ? localThumbnailFile.getAbsolutePath() : "",
                    fileThumbName,
                    individualCaption,
                    uniqueDates.add(uniqDate) ? uniqDate : ":" + uniqDate,
                    videoWidthDp,
                    videoHeightDp,
                    aspectRatio,
                    "1"
            );

            groupMessageList.add(model);

            Log.d("VideoMessageModelCreation", "group_messageModel created with caption: '" + model.getCaption() + "'");

            // Add to adapter
            groupChatAdapter.itemAdd(binding.messageRecView);
            groupChatAdapter.setLastItemVisible(isLastItemVisible);
            groupChatAdapter.notifyItemInserted(groupMessageList.size() - 1);

            // Update UI immediately to show the message
            ((Activity) mContext).runOnUiThread(() -> {
                groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                groupChatAdapter.setLastItemVisible(isLastItemVisible);
                binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
            });

            // Upload video with thumbnail
            UploadHelper uploadHelper = new UploadHelper(mContext, localVideoFile, null, senderId);
            uploadHelper.uploadContent(
                    Constant.video,
                    videoUri,
                    model.getCaption(),
                    videoModelId,
                    savedThumbnail, // savedThumbnail
                    fileThumbName, // fileThumbName
                    localVideoFile.getName(),
                    null, // contactName
                    null, // contactPhone
                    null, // audioTime
                    null, // audioName
                    getFileExtension(videoUri),
                    videoWidthDp,
                    videoHeightDp,
                    aspectRatio,
                    grpIdKey // Pass grpIdKey directly
            );
        }

        // Clear selections after sending
        selectedVideoUris.clear();
        selectedVideoFiles.clear();
    }


    private void filteredList(String newText) {
        ArrayList<group_messageModel> filteredList = new ArrayList<>();

        for (group_messageModel list : groupMessageList) {
            if (list.getMessage().toLowerCase().contains(newText.toLowerCase())) {

                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {
            //Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            groupChatAdapter.searchFilteredData(filteredList);
        }
    }

    public boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private void registerNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cleanup connectivity receiver
        try {
            if (connectivityReceiver != null) {
                mContext.unregisterReceiver(connectivityReceiver);
            }
        } catch (IllegalArgumentException e) {
            // Receiver not registered, handle this gracefully
            Log.e("TAG", "Receiver not registered: " + e.getMessage());
        }

        // Cleanup pending message uploader
        if (pendingMessageUploader != null) {
            pendingMessageUploader.cleanup();
        }
    }

    private void setupRecyclerViewAndAdapter() {
        String grpIdKey = getIntent().getStringExtra("grpIdKey");
        name = getIntent().getStringExtra("nameKey");
        caption = getIntent().getStringExtra("captionKey");
        groupChatAdapter = new groupChatAdapter(mContext, groupMessageList, activity, binding.valuable, binding.messageRecView, grpIdKey, name, caption);
        // Always enable stable IDs for proper RecyclerView behavior in both debug and release modes
        groupChatAdapter.enableStableIds();
        binding.messageRecView.setAdapter(groupChatAdapter);
        binding.messageRecView.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(mContext);
        binding.messageRecView.setLayoutManager(layoutManager);
        // ✅ Performance tuning and smooth scroll enhancements
        binding.messageRecView.setItemViewCacheSize(50); // Increased cache size for smoother scrolling
        binding.messageRecView.setDrawingCacheEnabled(true);
        binding.messageRecView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // ✅ Anti-flickering optimizations
        binding.messageRecView.setItemAnimator(null); // Disable animations to prevent flickering
        binding.messageRecView.setDrawingCacheBackgroundColor(android.R.color.transparent); // Prevent black background
        binding.messageRecView.setLayerType(View.LAYER_TYPE_HARDWARE, null); // Hardware acceleration for smooth rendering
        // Removed setHasFixedSize(true) to fix lint error - incompatible with wrap_content height

        // Add scroll listener to track isLastItemVisible
        binding.messageRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Update isLastItemVisible flag based on scroll position
                if (layoutManager != null && groupMessageList != null && !groupMessageList.isEmpty()) {
                    int totalItems = groupMessageList.size();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

                    // Update isLastItemVisible - user is at bottom if they can see the last message
                    isLastItemVisible = (lastVisiblePosition >= totalItems - 1);

                    Log.d("KEYBOARD_SCROLL_GROUP", "onScrolled: lastVisiblePosition=" + lastVisiblePosition +
                            ", totalItems=" + totalItems + ", isLastItemVisible=" + isLastItemVisible);
                }
            }
        });
    }


    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        ab = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        ab.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(ab);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(a);
    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }

    }

    private File createImageFile() throws IOException {
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @OptIn(markerClass = UnstableApi.class)
    /**
     * Helper method to get the external storage path for media files
     */
    private String getExternalStoragePath(String fileName, String mediaType) {
        try {
            File externalDir;
            if (mediaType.equals(Constant.img)) {
                externalDir = StoragePathHelper.getImagesStoragePath(mContext);
            } else if (mediaType.equals(Constant.video)) {
                externalDir = StoragePathHelper.getVideosStoragePath(mContext);
            } else {
                externalDir = StoragePathHelper.getDocumentsStoragePath(mContext);
            }

            File mediaFile = new File(externalDir, fileName);
            return mediaFile.getAbsolutePath();
        } catch (Exception e) {
            Log.e(TAG, "Error getting external storage path: " + e.getMessage());
            // Fallback to cache file path if external storage fails
            return globalFile.toString();
        }
    }

    @OptIn(markerClass = UnstableApi.class)
    private void dispatchTakePictureIntent() {
        String grpIdKey = getIntent().getStringExtra("grpIdKey");
        if (groupMessageList == null) {
            groupMessageList = new ArrayList<>();
            Log.w(TAG, "messageList was null in activity, initialized to empty ArrayList");
        }
        if (uniqueDates == null) {
            uniqueDates = new HashSet<>();
            Log.w(TAG, "uniqueDates was null in activity, initialized to empty HashSet");
        }

        CameraGalleryFragmentForGroup fragment = CameraGalleryFragmentForGroup.newInstance(
                modelId,
                "userFTokenKey",
                isLastItemVisible,
                groupMessageList,
                true,
                uniqueDates,
                groupChatAdapter,
                binding.messageRecView,
                grpIdKey
                // permissionsGranted
        );

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Assuming this method is within your Activity or Fragment
    private void fetchMessages(String senderRoom, chattingScreen.OnMessagesFetchedListener listener) {
        if (isLoading) return;  // Prevents multiple simultaneous fetches

        // If we already have messages (cached data), don't show loader
        if (!groupMessageList.isEmpty()) {
            Log.d("fetchMessages", "Group messages already available, skipping network fetch");
            if (listener != null) {
                listener.onMessagesFetched();
            }
            return;
        }

        isLoading = true;
        // Only show progress bar if we have no messages
        if (groupMessageList.isEmpty() && binding.progressBar != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        Query query = database.getReference().child(Constant.GROUPCHAT)
                .child(senderRoom)
                .orderByKey()
                .limitToLast(10);

        if (lastKey != null) {
            query = query.endBefore(lastKey);  // Fetch older messages if lastKey is set
        }

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    group_messageModel model = snapshot.getValue(group_messageModel.class);

                    if (model != null) {
                        // Remove message from group pending table since it's now in Firebase
                        try {
                            boolean removed = new DatabaseHelper(grpChattingScreen.this).removePendingGroupMessage(model.getModelId(), getIntent().getStringExtra("grpIdKey"));
                            if (removed) {
                                Log.d("PendingMessages", "Removed pending group message from SQLite: " + model.getModelId());
                            }
                        } catch (Exception e) {
                            Log.e("PendingMessages", "Error removing pending group message: " + e.getMessage(), e);
                        }

                        List<group_messageModel> updatedGroupMessageList = new ArrayList<>(groupMessageList);

                        int existingIndex = -1;
                        for (int i = 0; i < updatedGroupMessageList.size(); i++) {
                            if (updatedGroupMessageList.get(i).getModelId().equals(model.getModelId())) {
                                existingIndex = i;
                                break;
                            }
                        }

                        if (existingIndex != -1) {
                            updatedGroupMessageList.set(existingIndex, model);
                            Log.d("fetchMessages", "Updated existing message with ID: " + model.getModelId());
                            try {
                                new DatabaseHelper(mContext).deleteMessageByModelIdGROUP(model.getModelId());
                            } catch (Exception e) {
                                Log.e("handleChildAdded", "Error deleting message from DB: " + e.getMessage());
                            }
                        } else {
                            // If it's a truly new message, add it.
                            String uniqDate = model.getCurrentDate();
                            String formattedDate = uniqueDates.add(uniqDate) ? uniqDate : ":" + uniqDate;

                            group_messageModel newModelWithFormattedDate = new group_messageModel(
                                    model.getUid(), model.getMessage(), model.getTime(), model.getDocument(),
                                    model.getDataType(), model.getExtension(), model.getName(), model.getPhone(),
                                    model.getMiceTiming(), model.getMicPhoto(), model.getCreatedBy(), model.getUserName(),
                                    model.getModelId(), model.getReceiverUid(), model.getDocSize(), model.getFileName(),
                                    model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), formattedDate, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount(), model.getSelectionBunch()
                            );
                            updatedGroupMessageList.add(newModelWithFormattedDate);
                            Log.d("fetchMessages", "Added new message with ID: " + model.getModelId());
                        }

                        // Safely update UI on main thread using the adapter's DiffUtil method.
                        runOnUiThread(() -> {
                            groupChatAdapter.updateMessageList(updatedGroupMessageList);
                            groupMessageList = (ArrayList<group_messageModel>) updatedGroupMessageList;
                            groupChatAdapter.setLastItemVisible(false); // Hide progress indicator since message is now in Firebase

                            binding.progressBar.setVisibility(View.GONE);
                            // Scroll to the latest message.
                            binding.messageRecView.scrollToPosition(groupChatAdapter.getItemCount() - 1);
                        });
                    }
                } catch (Exception e) {
                    Log.e("fetchMessages", "Error in onChildAdded: " + e.getMessage(), e);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    group_messageModel updatedModel = snapshot.getValue(group_messageModel.class);
                    if (updatedModel != null) {
                        // Create a new list from the current state.
                        List<group_messageModel> updatedList = new ArrayList<>(groupMessageList);
                        int index = -1;
                        // Find the position of the item that was changed.
                        for (int i = 0; i < updatedList.size(); i++) {
                            if (updatedList.get(i).getModelId().equals(updatedModel.getModelId())) {
                                index = i;
                                break;
                            }
                        }
                        if (index != -1) {
                            // Replace the old model with the updated one in the new list.
                            updatedList.set(index, updatedModel);
                            // Update the adapter on the UI thread.
                            runOnUiThread(() -> {
                                groupChatAdapter.updateMessageList(updatedList);
                                // Update your local reference.
                                groupMessageList = (ArrayList<group_messageModel>) updatedList;
                                Log.d("fetchMessages", "Changed message with ID: " + updatedModel.getModelId());
                            });
                        }
                    }
                } catch (Exception e) {
                    Log.e("fetchMessages", "Error in onChildChanged: " + e.getMessage(), e);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                group_messageModel removedModel = snapshot.getValue(group_messageModel.class);
                if (removedModel != null) {
                    Constant.getSfFuncion(mContext);
                    String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                    group_messageModel model2 = null;
                    try {
                        // Create a new list that excludes the removed item.
                        List<group_messageModel> updatedList = new ArrayList<>(groupMessageList);
                        int indexOfItem = -1;
                        // Find the index of the item to be removed.
                        for (int i = 0; i < updatedList.size(); i++) {
                            model2 = updatedList.get(i);
                            if (model2.getModelId().equals(removedModel.getModelId())) {
                                indexOfItem = i;
                                break;
                            }
                        }

                        if (indexOfItem != -1) {
                            // Remove the item from the new list.
                            updatedList.remove(indexOfItem);
                            // Update the adapter on the UI thread.
                            group_messageModel finalModel = model2;
                            runOnUiThread(() -> {
                                groupChatAdapter.updateMessageList(updatedList);
                                // Update your local reference.
                                groupMessageList = (ArrayList<group_messageModel>) updatedList;
                                Log.d("fetchMessages", "Removed message with ID: " + removedModel.getModelId());
                                try {
                                    new DatabaseHelper(mContext).deleteMessageByModelIdGROUP(finalModel.getModelId());
                                } catch (Exception e) {
                                    Log.e("handleChildAdded", "Error deleting message from DB: " + e.getMessage());
                                }
                            });
                        }
                        // If you have a local database, consider deleting the message from there as well.
                        // new DatabaseHelper(mContext).deleteMessageByModelIdGROUP(removedModel.getModelId());
                    } catch (Exception e) {
                        Log.e("Error", "Failed to remove item: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // This method is called when a child's order changes.
                // For most chat applications, messages are ordered by timestamp,
                // so this might not be frequently used unless message order can change dynamically.
                // If order changes are important, you would typically re-fetch and update the list,
                // or implement specific logic to move items in your `groupMessageList` and then
                // call `groupChatAdapter.updateMessageList()`.
                Log.d("fetchMessages", "Child moved: " + snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "fetchMessagesGrp:onCancelled", error.toException());
                isLoading = false;
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        // The listener callback should ideally be invoked after the initial data load is complete.
        // For ChildEventListener, onChildAdded fires for each existing child.
        // If you need to know when the *initial* sync is done, you might use a ValueEventListener
        // on the same query that fires once, or track the number of expected children.
        // For now, keeping it here as per your original code, but be aware of this nuance.
        if (listener != null) {
            listener.onMessagesFetched();
        }

        isLoading = false;
    }


    private void loadMore(String senderRoom) {
        if (isLoading) return;  // Prevents multiple simultaneous fetches

        // If we already have messages, don't show loader for loadMore
        if (!groupMessageList.isEmpty()) {
            Log.d("loadMore", "Group messages already available, skipping loadMore");
            return;
        }

        isLoading = true;

        // No progress bar here typically, as loadMore is often triggered by scroll and should be seamless

        Query query = database.getReference().child(Constant.GROUPCHAT)
                .child(senderRoom)
                .orderByKey()
                .limitToLast(PAGE_SIZE);

        if (lastKey != null) {
            query = query.endBefore(lastKey);  // Fetch older messages if lastKey is set
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<group_messageModel> fetchedNewMessages = new ArrayList<>(); // Renamed for clarity
                String newLastKey = null;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotKey = snapshot.getKey(); // Get the key for lastKey tracking
                    group_messageModel model = snapshot.getValue(group_messageModel.class);

                    if (model != null) {
                        try {
                            // Avoid duplicate messages (DiffUtil will also help, but explicit check is fine)
                            boolean exists = false;
                            for (group_messageModel existingModel : groupMessageList) { // Check against the current list
                                if (existingModel.getModelId().equals(model.getModelId())) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                // Check for unique date and add new message model
                                String uniqDate = model.getCurrentDate();
                                // For loaded older messages, typically they will have a date header
                                // unless it's the very first message of that date.
                                // The original code uses ":" + uniqDate, which is fine if that's your convention.
                                String formattedDate = ":" + uniqDate; // Assuming you always want a colon prefix for loaded older dates

                                group_messageModel decryptedModel = new group_messageModel(
                                        model.getUid(), model.getMessage(), model.getTime(), model.getDocument(),
                                        model.getDataType(), model.getExtension(), model.getName(), model.getPhone(),
                                        model.getMiceTiming(), model.getMicPhoto(), model.getCreatedBy(), model.getUserName(),
                                        model.getModelId(), model.getReceiverUid(), model.getDocSize(), model.getFileName(),
                                        model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), formattedDate, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount(), model.getSelectionBunch()
                                );
                                fetchedNewMessages.add(decryptedModel);
                            }

                            // Track the smallest (oldest) key in this batch for the next pagination
                            if (newLastKey == null || snapshotKey.compareTo(newLastKey) < 0) {
                                newLastKey = snapshotKey;
                            }

                        } catch (Exception e) {
                            Log.e("DecryptionError", "Failed to process message: " + e.getMessage());
                        }
                    }
                }

                // --- Integration with updateMessageList ---
                if (!fetchedNewMessages.isEmpty()) {
                    lastKey = newLastKey; // Update lastKey for the next pagination request

                    // Create a new combined list: newly fetched (older) messages at the top,
                    // then existing messages.
                    List<group_messageModel> combinedList = new ArrayList<>();
                    combinedList.addAll(fetchedNewMessages); // Add the newly fetched messages first
                    combinedList.addAll(groupMessageList);   // Then add the messages already in the list

                    // Update the adapter with the new combined list on the UI thread.
                    runOnUiThread(() -> {
                        groupChatAdapter.updateMessageList(combinedList);
                        // IMPORTANT: Update your Activity/Fragment's 'groupMessageList' reference
                        // to point to this new combined list. This is crucial for consistency
                        // in subsequent calls to loadMore or ChildEventListener events.
                        groupMessageList = (ArrayList<group_messageModel>) combinedList;

                        // You might want to scroll to the first newly loaded item, or maintain scroll position.
                        // For loading more at the top, often you want to keep the current view stable
                        // or scroll to the first new item.
                        // Example: binding.messageRecView.scrollToPosition(fetchedNewMessages.size() - 1);
                    });
                }

                isLoading = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", "loadMore:onCancelled", databaseError.toException());
                isLoading = false;
            }
        });
    }

    boolean isLastItemVisible() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
        if (layoutManager != null) {
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();

            return lastVisibleItemPosition == totalItemCount - 1;
        }
        return false;
    }

    public void setAdapteEMojirrr(List<Emoji> emojis) {

        adapter = new emojiAdapterGrp(mContext, emojis, binding.messageBox);
        emojiRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 9));
        emojiRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    private int getKeyboardHeight() {
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int screenHeight = getWindow().getDecorView().getRootView().getHeight();
        int keyboardHeight = screenHeight - rect.bottom;
        if (keyboardHeight < 0) {
            keyboardHeight = 0;
        }
        return keyboardHeight;
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void updateMessageList(group_messageModel model) {
        String uniqDate = model.getCurrentDate();
        if (uniqueDates.add(uniqDate)) {
            groupMessageList.add(new group_messageModel(model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMiceTiming(), model.getMicPhoto(), model.getCreatedBy(), model.getUserName(), model.getModelId(), model.getReceiverUid(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), uniqDate, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()));
        } else {
            groupMessageList.add(new group_messageModel(model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMiceTiming(), model.getMicPhoto(), model.getCreatedBy(), model.getUserName(), model.getModelId(), model.getReceiverUid(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), ":" + uniqDate, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()));
        }
    }


    /**
     * Initialize group chat data with proper caching and network fallback
     */
    private void initializeGroupChatData(String senderRoom) {
        Log.d("GroupChatInit", "Initializing group chat data for room: " + senderRoom);

        // Ensure progress bar is hidden before any operations
        if (binding.progressBar != null) {
            binding.progressBar.setVisibility(View.GONE);
        }

        // Step 1: Try to load from local cache first (instant)
        try {
            //   getAllMessages(senderRoom, mContext);
            if (!groupMessageList.isEmpty()) {
                Log.d("GroupChatInit", "Found " + groupMessageList.size() + " cached group messages");

                // Show cached messages immediately - no UI thread delay
                if (binding.progressBar != null) {
                    binding.progressBar.setVisibility(View.GONE);
                }
                if (groupChatAdapter != null) {
                    groupChatAdapter.notifyDataSetChanged();
                    // Scroll to bottom to show latest messages
                    if (binding.messageRecView != null && groupMessageList.size() > 0) {
                        binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
                    }
                }
                // Ensure loading state is properly reset
                isLoading = false;
                Log.d("GroupChatInit", "Group chat initialized with cached data");
                return; // Exit early if we have cached data
            }
        } catch (Exception e) {
            Log.e("GroupChatInit", "Error loading cached group messages: " + e.getMessage());
        }

        // Step 2: Only fetch from network if no cached data
        if (groupMessageList.isEmpty()) {
            Log.d("GroupChatInit", "No cached data found, fetching from network");
            fetchMessages(senderRoom, () -> {
                Log.d("GroupChatInit", "Network fetch completed successfully");
            });
        }

        // Step 3: Load pending group messages
        loadPendingGroupMessages(getIntent().getStringExtra("grpIdKey"));
    }

    private void loadPendingGroupMessages(String grpIdKey) {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            ArrayList<group_messageModel> pendingMessages = dbHelper.getPendingGroupMessages(grpIdKey);

            if (pendingMessages != null && !pendingMessages.isEmpty()) {
                Log.d("PendingMessages", "Loading " + pendingMessages.size() + " pending group messages");

                HashSet<String> existingIds = new HashSet<>();
                for (group_messageModel m : groupMessageList) existingIds.add(m.getModelId());
                for (group_messageModel pendingMsg : pendingMessages) {
                    if (!existingIds.contains(pendingMsg.getModelId())) {
                        groupMessageList.add(pendingMsg);
                        existingIds.add(pendingMsg.getModelId());
                    }
                }

                // Update UI to show pending messages with progress
                groupChatAdapter.updateMessageList(new ArrayList<>(groupMessageList));
                groupChatAdapter.setLastItemVisible(true); // Show progress for pending messages
                binding.messageRecView.scrollToPosition(groupMessageList.size() - 1);
            }
        } catch (Exception e) {
            Log.e("PendingMessages", "Error loading pending group messages: " + e.getMessage(), e);
        }
    }

    public void getAllMessages(String senderRoom, Context mContext) {

        List<group_messageModel2> messages2 = new DatabaseHelper(mContext).getMessagesFromDatabaseGroup(senderRoom);
        List<group_messageModel> messages = convertToMessageModel(messages2);
        for (group_messageModel model : messages) {
            updateMessageList(model);
            Log.d("getAllMessagesfghjkdsss", "Message: " +
                    "\nText: " + model.getMessage() +
                    "\nTime: " + model.getTime() +
                    "\nUID: " + model.getUid() +
                    "\nDate: " + model.getCurrentDate() +
                    "\nType: " + model.getDataType());

//            if (model.getDataType().equals(Constant.Text)) {
//                UploadHelper uploadHelper = new UploadHelper(mContext, null, null, model.getCreatedBy());
//
//                uploadHelper.uploadContent(
//                        Constant.Text, // uploadType
//                        null, // uri (null for text)
//                        model.getMessage(), // captionText (using message as caption)
//                        model.getModelId(), // modelId
//                        null, // savedThumbnail
//                        null, // fileThumbName
//                        null, // fileName
//                        null, // contactName
//                        null, // contactPhone
//                        null, // audioTime
//                        null, // audioName
//                        null, "", "", "" // extension
//                );
//
//            } else
//                if (model.getDataType().equals(Constant.img)) {
//
//                File file = new File(mContext.getCacheDir(), model.getFileName());
//
//                Uri uri = FileProvider.getUriForFile(
//                        mContext,
//                        "com.Appzia.enclosure",
//                        file
//                );
//
//
//                UploadHelper uploadHelper = new UploadHelper(mContext, file, file, model.getCreatedBy());
//                // Image upload
//                uploadHelper.uploadContent(Constant.img, uri, model.getCaption(), model.getModelId(), null, null, null, null, null, null, null, null, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio());
//
//
//            } else
//                if (model.getDataType().equals(Constant.doc)) {
//
//                File file = new File(mContext.getCacheDir(), model.getFileName());
//
//                Uri uri = FileProvider.getUriForFile(
//                        mContext,
//                        "com.Appzia.enclosure",
//                        file
//                );
//
//
//                UploadHelper uploadHelper = new UploadHelper(mContext, file, file, model.getCreatedBy());
//                uploadHelper.uploadContent(Constant.doc, uri, model.getCaption(), model.getModelId(), null, null, null, null, null, null, null, model.getExtension(), "", "", "");
//
//
//            } else
//                if (model.getDataType().equals(Constant.contact)) {
//
//                File file = new File(mContext.getCacheDir(), model.getFileName());
//
//                Uri uri = FileProvider.getUriForFile(
//                        mContext,
//                        "com.Appzia.enclosure",
//                        file
//                );
//
//                UploadHelper uploadHelper = new UploadHelper(mContext, file, file, model.getCreatedBy());
//                // Contact upload
//                uploadHelper.uploadContent(Constant.contact, null, model.getCaption(), model.getModelId(), null, null, null, model.getName(), model.getPhone(), null, null, null, "", "", "");
//
//
//            } else
//                if (model.getDataType().equals(Constant.video)) {
//
//                File file = new File(mContext.getCacheDir(), model.getFileName());
//
//                Uri uri = FileProvider.getUriForFile(
//                        mContext,
//                        "com.Appzia.enclosure",
//                        file
//                );
//
//                UploadHelper uploadHelper = new UploadHelper(mContext, file, file, model.getCreatedBy());
//                // Video upload
//                uploadHelper.uploadContent(Constant.video, uri, model.getCaption(), modelId, file, model.getFileNameThumbnail(), model.getFileName(), null, null, null, null, null, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio());
//
//
//            } else
//                if (model.getDataType().equals(Constant.voiceAudio)) {
//
//                File file = new File(mContext.getCacheDir(), model.getFileName());
//
//                Uri uri = FileProvider.getUriForFile(
//                        mContext,
//                        "com.Appzia.enclosure",
//                        file
//                );
//
//                UploadHelper uploadHelper = new UploadHelper(mContext, file, file, model.getCreatedBy());
//
//                uploadHelper.uploadContent(Constant.voiceAudio, uri, model.getCaption(), model.getModelId(), null, null, model.getFileName(), null, null, model.getMiceTiming(), null, null, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio());
//
//
//            }
        }
        if (!messages.isEmpty()) {
            runOnUiThread(() -> {
                groupChatAdapter.setLastItemVisible(true); // Ensure the last item is visible
                groupChatAdapter.itemAdd(binding.messageRecView);
                binding.messageBox.setEnabled(true);
            });
        }
    }

    private List<group_messageModel> convertToMessageModel(List<group_messageModel2> messages2) {
        List<group_messageModel> messages = new ArrayList<>();
        for (group_messageModel2 model2 : messages2) {
            group_messageModel model = new group_messageModel(
                    model2.getUid(),
                    model2.getMessage(),
                    model2.getTime(),
                    model2.getDocument(),
                    model2.getDataType(),
                    model2.getExtension(),
                    model2.getName(),
                    model2.getPhone(),
                    model2.getMicPhoto(),
                    model2.getMiceTiming(),
                    model2.getCreatedBy(),
                    model2.getUserName(),
                    model2.getModelId(),
                    model2.getReceiverUid(),
                    model2.getDocSize(),
                    model2.getFileName(),
                    model2.getThumbnail(),
                    model2.getFileNameThumbnail(),
                    model2.getCaption(),
                    model2.getCurrentDate(),
                    model2.getImageWidth(),
                    model2.getImageHeight(),
                    model2.getAspectRatio(),
                    model2.getSelectionCount()
            );
            messages.add(model);
        }
        return messages;
    }


    private void loadImages() {
        imagePaths.clear(); // Clear existing paths
        Uri collection;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_ADDED
        };

        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = getContentResolver().query(
                collection,
                projection,
                null,
                null,
                sortOrder
        )) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int count = 0;

                while (cursor.moveToNext() && count < 45) {
                    long id = cursor.getLong(idColumn);
                    Uri imageUri = ContentUris.withAppendedId(collection, id);
                    imagePaths.add(imageUri.toString());
                    count++;
                }
            }
        }
        // 🔹 Set horizontal layout
        binding.dataRecview.setLayoutManager(new GridLayoutManager(mContext, 4));

        // Configure ultra-smooth scrolling with advanced optimizations
        binding.dataRecview.setHasFixedSize(true);
        binding.dataRecview.setItemAnimator(null); // Disable animations for smoother scrolling
        binding.dataRecview.setNestedScrollingEnabled(false);
        binding.dataRecview.setDrawingCacheEnabled(true);
        binding.dataRecview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.dataRecview.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_DEFAULT);
        binding.dataRecview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.dataRecview.setClipToPadding(false);
        binding.dataRecview.setClipChildren(false);

        // Additional performance optimizations
        binding.dataRecview.setLayerType(View.LAYER_TYPE_HARDWARE, null); // Hardware acceleration
        binding.dataRecview.setWillNotCacheDrawing(false); // Enable drawing cache
        binding.dataRecview.setDrawingCacheBackgroundColor(Color.TRANSPARENT); // Transparent background cache

        ImageAdapter2 imageAdapter = new ImageAdapter2(imagePaths, mContext, null, PICK_IMAGE_REQUEST_CODE, bottomSheetDialog, modelId, database, globalFile, binding.messageRecView, FullImageFile, binding.messageBox, uniqueDates, groupMessageList, userFTokenKey, isLastItemVisible, groupChatAdapter, (LinearLayout) binding.gallary, binding.messageBox, (LinearLayout) binding.emoji, binding.send, binding.multiSelectSmallCounterText);
        binding.dataRecview.setAdapter(imageAdapter);

        // Enable multi-selection mode for images
        imageAdapter.setMultiSelectionMode(true);
        imageAdapter.setOnImageSelectionListener(new ImageAdapter2.OnImageSelectionListener() {
            @Override
            public void onImageSelectionChanged(ArrayList<Uri> selectedUris) {
                Log.d("ImageUpload", "=== chattingScreen onImageSelectionChanged ===");
                Log.d("ImageUpload", "Callback object: " + this.getClass().getSimpleName());
                Log.d("ImageUpload", "Selected images: " + selectedUris.size());
                Log.d("ImageUpload", "SelectedImageUris before: " + selectedImageUris.size());

                // Update the selectedImageUris list
                selectedImageUris.clear();
                selectedImageUris.addAll(selectedUris);

                Log.d("ImageUpload", "SelectedImageUris after: " + selectedImageUris.size());
                Log.d("ImageUpload", "About to call updateMultiSelectionUI");
                updateMultiSelectionUI(selectedUris.size());
                Log.d("ImageUpload", "Called updateMultiSelectionUI");
            }

            @Override
            public void onMultiSelectionModeChanged(boolean isMultiMode) {
                Log.d("ImageSelection", "Multi-selection mode: " + isMultiMode);
                showMultiSelectionUI(isMultiMode);
            }
        });

        // Setup multi-selection send button click listener
        setupMultiSelectionSendButton(imageAdapter);
    }

    /**
     * Update multi-selection UI based on selection count
     */
    private void updateMultiSelectionUI(int selectionCount) {
        try {
            LinearLayout sendGrpLyt = findViewById(R.id.multiSelectSendGrpLyt);
            TextView smallCounterText = findViewById(R.id.multiSelectSmallCounterText);
            LinearLayout doneButton = findViewById(R.id.multiSelectDoneButton);

            if (sendGrpLyt != null && smallCounterText != null && doneButton != null) {
                if (selectionCount > 0) {
                    sendGrpLyt.setVisibility(View.VISIBLE);
                    smallCounterText.setVisibility(View.VISIBLE);
                    smallCounterText.setText(String.valueOf(selectionCount));
                    doneButton.setEnabled(true);
                    doneButton.setAlpha(1.0f);
                } else {
                    smallCounterText.setVisibility(View.GONE);
                    doneButton.setEnabled(false);
                    doneButton.setAlpha(0.5f);
                }
            }
        } catch (Exception e) {
            Log.e("MultiSelectionUI", "Error updating multi-selection UI: " + e.getMessage());
        }
    }

    /**
     * Show/hide multi-selection UI
     */
    private void showMultiSelectionUI(boolean show) {
        try {
            LinearLayout sendGrpLyt = findViewById(R.id.multiSelectSendGrpLyt);
            if (sendGrpLyt != null) {
                sendGrpLyt.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        } catch (Exception e) {
            Log.e("MultiSelectionUI", "Error showing multi-selection UI: " + e.getMessage());
        }
    }

    /**
     * Setup multi-selection send button click listener
     */
    private void setupMultiSelectionSendButton(ImageAdapter2 imageAdapter) {
        try {
            LinearLayout doneButton = findViewById(R.id.multiSelectDoneButton);
            if (doneButton != null) {
                doneButton.setOnClickListener(v -> {
                    // Add haptic vibration
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Constant.Vibrator(mContext);
                    }

                    Log.d("MultiSelectionSend", "Multi-selection send button clicked");
                    ArrayList<Uri> selectedUris = imageAdapter.getSelectedImageUris();
                    if (selectedUris != null && !selectedUris.isEmpty()) {
                        // Capture caption from bottom sheet
                        String caption = "";
                        EditText messageBoxMy = findViewById(R.id.messageBoxMy);
                        if (messageBoxMy != null) {
                            caption = messageBoxMy.getText().toString().trim();
                            Log.d("CaptionCapture", "Captured caption from bottom sheet: " + caption);
                        }

                        Log.d("MultiSelectionSend", "Calling setupMultiImagePreview with " + selectedUris.size() + " images and caption: " + caption);
                        imageAdapter.setupMultiImagePreview(caption, null);
                    } else {
                        Log.d("MultiSelectionSend", "No images selected, cannot show preview");
                    }
                });
            } else {
                Log.w("MultiSelectionSend", "multiSelectDoneButton not found in layout");
            }
        } catch (Exception e) {
            Log.e("MultiSelectionSend", "Error setting up multi-selection send button: " + e.getMessage());
        }
    }

    private String stringForTime(long timeMs) {
        int totalSeconds = (int) (timeMs / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

    // Method to stop progress indicator for a specific message
    public void stopMessageProgress(String modelId) {
        try {
            if (groupChatAdapter != null) {
                groupChatAdapter.stopProgressIndicator(modelId);
            }
        } catch (Exception e) {
            Log.e("ProgressIndicator", "Error stopping message progress: " + e.getMessage());
        }
    }

    private File getExternalStorageDir(String type, String subPath) {
        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dir = new File(mContext.getExternalFilesDir(type), subPath);
        } else {
            dir = new File(mContext.getExternalFilesDir(null), subPath);
        }
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    private void copyUriToFile(Uri uri, File destination) throws IOException {
        InputStream is = getContentResolver().openInputStream(uri);
        FileOutputStream fos = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        is.close();
        fos.close();
    }

    private void copyFile(File source, File destination) throws IOException {
        InputStream is = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        is.close();
        fos.close();
    }

    /**
     * Setup BottomSheetDialog for dataCardview
     */
    private void setupDataCardviewBottomSheet() {
        Log.d("BottomSheetDebug", "Setting up data cardview bottom sheet...");

        bottomSheetDialogData = new BottomSheetDialog(grpChattingScreen.this, R.style.TransparentBottomSheetDialog);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_data_cardview, null, false);
        Log.d("BottomSheetDebug", "Bottom sheet view inflated: " + (bottomSheetView != null));
        bottomSheetDialogData.setContentView(bottomSheetView);

        if (bottomSheetDialogData.getWindow() != null) {
            bottomSheetDialogData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            bottomSheetDialogData.getWindow().setDimAmount(0.0f);
            bottomSheetDialogData.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            bottomSheetDialogData.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );

            // Set soft input mode to adjustNothing to prevent bottom sheet from moving
            bottomSheetDialogData.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
            );

            WindowManager.LayoutParams params = bottomSheetDialogData.getWindow().getAttributes();
            params.y = 0;
            bottomSheetDialogData.getWindow().setAttributes(params);
        }

        // ✅ Set fixed height constraints like camera fragment
        bottomSheetDialogData.setOnShowListener(dialog -> {
            // Set fixed height constraints like camera fragment
            View contentView = bottomSheetDialogData.findViewById(android.R.id.content);
            if (contentView != null) {
                // Set minimum height constraint
                contentView.setMinimumHeight((int) (400 * getResources().getDisplayMetrics().density)); // 400dp

                // Set maximum height by constraining the layout
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    contentView.setLayoutParams(layoutParams);
                }

                Log.d("BottomSheetHeight", "Set minimum height: 400dp");
            }
        });

        // ✅ Control BottomSheet Behavior (half-screen default, full on expand, hide on swipe down)
        bottomSheetDialogData.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);

                behavior.setDraggable(false); // Disable dragging to keep it fixed
                behavior.setFitToContents(true); // Fit to contents to keep it at bottom
                behavior.setExpandedOffset(0);
                behavior.setSkipCollapsed(true); // Skip collapsed state to stay at bottom
                behavior.setHideable(true);
                behavior.setHalfExpandedRatio(0.0f); // No half-expanded state
                behavior.setMaxHeight((int) (400 * getResources().getDisplayMetrics().density)); // Fixed max height

                // Set peek height to match content height for truly fixed state
                int peekHeightInPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());
                behavior.setPeekHeight(peekHeightInPx);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                // Force collapsed state after a short delay to ensure it's applied
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }, 100);

                behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        View bottomActions = bottomSheetView.findViewById(R.id.bottomview);
                        if (bottomActions != null) {
                            bottomActions.setVisibility(View.VISIBLE);
                            bottomActions.bringToFront();
                        }
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            hideDataBottomSheet();
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        View bottomActions = bottomSheetView.findViewById(R.id.bottomview);
                        if (bottomActions != null) {
                            bottomActions.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        // Setup RecyclerView


        // Setup button listeners
        setupBottomSheetListeners(bottomSheetView);
    }


    /**
     * Setup RecyclerView in bottom sheet
     */


    private View.OnTouchListener swipeTouchListener;
    private float startY = 0;
    private float startX = 0;
    private static final int SWIPE_THRESHOLD = 100; // Minimum distance for swipe
    private static final int SWIPE_VELOCITY_THRESHOLD = 100; // Minimum velocity for swipe

    private void addSwipeTouchListener(RecyclerView recyclerView) {
        if (swipeTouchListener == null) {
            swipeTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startY = event.getY();
                            startX = event.getX();
                            Log.d("BottomSheetGesture", "Touch started at Y: " + startY);
                            break;

                        case MotionEvent.ACTION_UP:
                            float endY = event.getY();
                            float endX = event.getX();
                            float deltaY = endY - startY;
                            float deltaX = endX - startX;

                            Log.d("BottomSheetGesture", "Touch ended - deltaY: " + deltaY + ", deltaX: " + deltaX);

                            // Check if it's a vertical swipe (not horizontal)
                            if (Math.abs(deltaY) > Math.abs(deltaX) && Math.abs(deltaY) > SWIPE_THRESHOLD) {
                                if (deltaY > 0) {
                                    // Swipe down - collapse bottom sheet
                                    Log.d("BottomSheetGesture", "SWIPE DOWN DETECTED - COLLAPSING BOTTOM SHEET!");
                                    if (bottomSheetDialogData != null && bottomSheetDialogData.isShowing()) {
                                        hideDataBottomSheet();
                                    }
                                } else {
                                    // Swipe up - remove touch listener
                                    Log.d("BottomSheetGesture", "SWIPE UP DETECTED - REMOVING TOUCH LISTENER");
                                    removeSwipeTouchListener(recyclerView);
                                }
                            }
                            break;
                    }
                    return false; // Don't consume the event, let RecyclerView handle it
                }
            };
        }

        recyclerView.setOnTouchListener(swipeTouchListener);
        Log.d("BottomSheetGesture", "Touch listener added to RecyclerView");
    }

    private void removeSwipeTouchListener(RecyclerView recyclerView) {
        recyclerView.setOnTouchListener(null);
        Log.d("BottomSheetGesture", "Touch listener removed from RecyclerView");
    }

    /**
     * Setup click listeners for bottom sheet buttons
     */
    private void setupBottomSheetListeners(View bottomSheetView) {
        // Debug: Check if bottom view is found
        Log.d("BottomSheetDebug", "Setting up bottom sheet listeners...");
        Log.d("BottomSheetDebug", "Bottom sheet view: " + (bottomSheetView != null));

        View bottomView = bottomSheetView.findViewById(R.id.bottomview);
        Log.d("BottomSheetDebug", "Bottom view found: " + (bottomView != null));
        if (bottomView != null) {
            Log.d("BottomSheetDebug", "Bottom view visibility: " + bottomView.getVisibility());
            Log.d("BottomSheetDebug", "Bottom view height: " + bottomView.getHeight());
            Log.d("BottomSheetDebug", "Bottom view width: " + bottomView.getWidth());
        } else {
            Log.e("BottomSheetDebug", "Bottom view NOT FOUND! Check layout file.");
        }

        // Debug: Check all button views
        View cameraLyt = bottomSheetView.findViewById(R.id.cameraLyt);
        View galleryLyt = bottomSheetView.findViewById(R.id.galleryLyt);
        View videoLyt = bottomSheetView.findViewById(R.id.videoLyt);
        View documentLyt = bottomSheetView.findViewById(R.id.documentLyt);
        View contactLyt = bottomSheetView.findViewById(R.id.contactLyt);

        Log.d("BottomSheetDebug", "Camera button found: " + (cameraLyt != null));
        Log.d("BottomSheetDebug", "Gallery button found: " + (galleryLyt != null));
        Log.d("BottomSheetDebug", "Video button found: " + (videoLyt != null));
        Log.d("BottomSheetDebug", "Document button found: " + (documentLyt != null));
        Log.d("BottomSheetDebug", "Contact button found: " + (contactLyt != null));

        // Force layout measurement for bottom view
        if (bottomView != null) {
            bottomView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            bottomView.layout(0, 0, bottomView.getMeasuredWidth(), bottomView.getMeasuredHeight());
            Log.d("BottomSheetDebug", "After measure - Bottom view height: " + bottomView.getHeight());
            Log.d("BottomSheetDebug", "After measure - Bottom view width: " + bottomView.getWidth());
        }

        // Camera button
        bottomSheetView.findViewById(R.id.cameraLyt).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(mContext);
            }
            hideDataBottomSheet();
            // Call existing camera functionality
            askCameraPermissions();
        });

        // Gallery button
        bottomSheetView.findViewById(R.id.galleryLyt).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(mContext);
            }
            hideDataBottomSheet();
            // Use WhatsApp-like image picker
            Intent pickerIntent = new Intent(mContext, WhatsAppLikeImagePicker.class);
            pickerIntent.putExtra(WhatsAppLikeImagePicker.EXTRA_MAX_SELECTION, 30 - selectedImageUris.size());
            pickerIntent.putParcelableArrayListExtra(WhatsAppLikeImagePicker.EXTRA_SELECTED_IMAGES, new ArrayList<>(selectedImageUris));
            SwipeNavigationHelper.startActivityForResultWithSwipe(grpChattingScreen.this, pickerIntent, PICK_IMAGE_REQUEST_CODE);
        });

        // Video button
        bottomSheetView.findViewById(R.id.videoLyt).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(mContext);
            }
            hideDataBottomSheet();
            // Use WhatsApp-like video picker for multi-selection
            Intent pickerIntent = new Intent(mContext, WhatsAppLikeVideoPicker.class);
            pickerIntent.putExtra(WhatsAppLikeVideoPicker.EXTRA_MAX_SELECTION, 5 - selectedVideoUris.size());
            pickerIntent.putParcelableArrayListExtra(WhatsAppLikeVideoPicker.EXTRA_SELECTED_VIDEOS, new ArrayList<>(selectedVideoUris));
            startActivityForResult(pickerIntent, PICK_VIDEO_REQUEST_CODE);
        });

        // Document button
        bottomSheetView.findViewById(R.id.documentLyt).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(mContext);
            }
            hideDataBottomSheet();
            // Use system document picker for multi-selection
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            SwipeNavigationHelper.startActivityForResultWithSwipe(grpChattingScreen.this, Intent.createChooser(intent, "Select Documents"), PICK_DOCUMENT_REQUEST_CODE);
        });

        // Contact button
        bottomSheetView.findViewById(R.id.contactLyt).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(mContext);
            }
            hideDataBottomSheet();
            // Use custom contact picker for multiple selection
            Log.d("ContactSelection", "Opening custom contact picker for multi-selection");
            Intent intent = new Intent(mContext, WhatsAppLikeContactPicker.class);
            intent.putExtra(WhatsAppLikeContactPicker.EXTRA_MAX_SELECTION, 50);
            intent.putParcelableArrayListExtra(WhatsAppLikeContactPicker.EXTRA_SELECTED_CONTACTS, new ArrayList<WhatsAppLikeContactPicker.ContactInfo>());
            startActivityForResult(intent, PICK_CONTACT_REQUEST_CODE);
        });
    }

    /**
     * Show the data bottom sheet with smooth slide-up animation
     */
    private void showDataBottomSheet() {
        if (bottomSheetDialogData != null) {
            // Get the bottom sheet view and refresh RecyclerView data
            View bottomSheetView = bottomSheetDialogData.findViewById(android.R.id.content);
            if (bottomSheetView != null) {


                // Set initial state BEFORE showing dialog to prevent flicker
                bottomSheetView.setVisibility(View.INVISIBLE);
                bottomSheetView.setTranslationY(1000); // Start off-screen
                bottomSheetView.setAlpha(0.0f);

                // Ensure bottom sheet respects system gesture/navigation insets and expands above them
                bottomSheetDialogData.setOnShowListener(dialog -> {
                    android.widget.FrameLayout sheet = bottomSheetDialogData.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    if (sheet != null) {
                        com.google.android.material.bottomsheet.BottomSheetBehavior<android.widget.FrameLayout> behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(sheet);
                        behavior.setDraggable(false); // Disable dragging completely - fixed at bottom
                        behavior.setFitToContents(false);
                        behavior.setExpandedOffset(0);
                        behavior.setSkipCollapsed(false);
                        behavior.setHideable(true);
                        behavior.setHalfExpandedRatio(0.5f); // Standard ratio
                        behavior.setMaxHeight((int) (700 * getResources().getDisplayMetrics().density)); // Max 700dp

                        // Set peek height to 700dp
                        int peekHeightInPx = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 700, getResources().getDisplayMetrics());
                        behavior.setPeekHeight(peekHeightInPx);

                        // Set behavior state immediately to prevent full screen flash
                        behavior.setState(com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED);

                        // Use post with delay to ensure proper measurement and avoid flickering
                        bottomSheetView.post(() -> {
                            // Small delay to ensure view is properly measured
                            bottomSheetView.postDelayed(() -> {
                                bottomSheetView.setVisibility(View.VISIBLE);
                                // Animate the bottom sheet sliding up smoothly
                                bottomSheetView.animate()
                                        .translationY(0)
                                        .alpha(1.0f)
                                        .setDuration(180) // Much faster opening animation
                                        .setInterpolator(new android.view.animation.DecelerateInterpolator(1.5f))
                                        .start();
                            }, 50); // Reduced delay for faster response
                        });

                        behavior.addBottomSheetCallback(new com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback() {
                            @Override
                            public void onStateChanged(@androidx.annotation.NonNull android.view.View bottomSheet, int newState) {
                                android.view.View bottomActions = bottomSheetView.findViewById(R.id.bottomview);
                                if (bottomActions != null) {
                                    bottomActions.setVisibility(android.view.View.VISIBLE);
                                    bottomActions.bringToFront();
                                }
                                if (newState == com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN) {
                                    hideDataBottomSheet();
                                }
                            }

                            @Override
                            public void onSlide(@androidx.annotation.NonNull android.view.View bottomSheet, float slideOffset) {
                                android.view.View bottomActions = bottomSheetView.findViewById(R.id.bottomview);
                                if (bottomActions != null) {
                                    bottomActions.setVisibility(android.view.View.VISIBLE);
                                }
                            }
                        });

                        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(sheet, (v, insets) -> {
                            androidx.core.graphics.Insets sys = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars() | androidx.core.view.WindowInsetsCompat.Type.ime());
                            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), sys.bottom);
                            return insets;
                        });

                        // Also apply insets to content root for extra safety
                        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(bottomSheetView, (v, insets) -> {
                            androidx.core.graphics.Insets sys = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars() | androidx.core.view.WindowInsetsCompat.Type.ime());
                            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), sys.bottom);
                            return insets;
                        });
                    }
                });

                // Force layout for bottom view after dialog is shown
                bottomSheetView.post(() -> {
                    View bottomView = bottomSheetView.findViewById(R.id.bottomview);
                    if (bottomView != null) {
                        Log.d("BottomSheetDebug", "Post-show - Bottom view height: " + bottomView.getHeight());
                        Log.d("BottomSheetDebug", "Post-show - Bottom view width: " + bottomView.getWidth());

                        // If still 0, force measurement
                        if (bottomView.getHeight() == 0) {
                            bottomView.measure(
                                    View.MeasureSpec.makeMeasureSpec(bottomSheetView.getWidth(), View.MeasureSpec.EXACTLY),
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                            );
                            Log.d("BottomSheetDebug", "Forced measure - Bottom view height: " + bottomView.getMeasuredHeight());
                        }

                        // Ensure bottom view is visible and on top
                        bottomView.setVisibility(View.VISIBLE);
                        bottomView.bringToFront();
                        bottomView.invalidate();
                        Log.d("BottomSheetDebug", "Bottom view visibility set to VISIBLE and brought to front");

                        // Additional debugging for layout position
                        Log.d("BottomSheetDebug", "Bottom view X: " + bottomView.getX() + ", Y: " + bottomView.getY());
                        Log.d("BottomSheetDebug", "Bottom view parent: " + (bottomView.getParent() != null ? bottomView.getParent().getClass().getSimpleName() : "null"));

                        // Check if bottom view is actually visible on screen
                        int[] location = new int[2];
                        bottomView.getLocationOnScreen(location);
                        Log.d("BottomSheetDebug", "Bottom view screen position - X: " + location[0] + ", Y: " + location[1]);

                        // Remove default sheet background to avoid double-layer corners
                        android.widget.FrameLayout sheet = bottomSheetDialogData.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                        if (sheet != null) {
                            sheet.setBackground(null);
                        }

                        // Force a redraw
                        bottomView.requestLayout();
                        bottomView.forceLayout();
                    }
                });
            }
            bottomSheetDialogData.show();
        }
    }

    /**
     * Hide the data bottom sheet with smooth slide-down animation
     */
    public void hideDataBottomSheet() {
        if (bottomSheetDialogData != null && bottomSheetDialogData.isShowing()) {
            View bottomSheetView = bottomSheetDialogData.findViewById(android.R.id.content);
            if (bottomSheetView != null) {
                // Animate the bottom sheet sliding down smoothly
                bottomSheetView.animate()
                        .translationY(bottomSheetView.getHeight())
                        .alpha(0.0f)
                        .setDuration(150) // Super fast dismiss animation
                        .setInterpolator(new android.view.animation.AccelerateInterpolator(1.5f))
                        .withEndAction(() -> {
                            bottomSheetView.setVisibility(View.INVISIBLE);
                            bottomSheetDialogData.dismiss();
                        })
                        .start();
            } else {
                bottomSheetDialogData.dismiss();
            }
        }
    }

    /**
     * Show gallery interface with permission check
     */
    private void showGalleryInterface() {
        // Hide keyboard if open
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.messageBox.getWindowToken(), 0);
        }

        // Hide emoji recycler view if visible
        if (binding.emojiRecyclerview.getVisibility() == View.VISIBLE) {
            binding.emojiRecyclerview.animate()
                    .translationY(binding.emojiRecyclerview.getHeight())
                    .setDuration(0)
                    .withEndAction(() -> binding.emojiRecyclerview.setVisibility(View.GONE))
                    .start();

            binding.emojiLyt.animate()
                    .translationY(binding.emojiLyt.getHeight())
                    .setDuration(0)
                    .withEndAction(() -> binding.emojiLyt.setVisibility(View.GONE))
                    .start();

            binding.emojiSearchContainerTop.setVisibility(View.GONE);
            binding.emojiSearchContainerBottom.setVisibility(View.GONE);
        }

        // Toggle gallery recent view visibility
        Log.d("GalleryDebug", "Gallery recent view visibility: " + binding.galleryRecentLyt.getVisibility());
        Log.d("GalleryDebug", "Gallery alpha: " + binding.galleryRecentLyt.getAlpha());
        Log.d("GalleryDebug", "Gallery translationY: " + binding.galleryRecentLyt.getTranslationY());

        if (binding.galleryRecentLyt.getVisibility() == View.VISIBLE) {
            Log.d("GalleryDebug", "Hiding gallery recent view");
            hideGalleryRecentView();
            binding.messageBox.requestFocus();
            binding.messageBox.setCursorVisible(true);
            binding.galleryRecentLyt.setVisibility(View.GONE);
            binding.messageBox.post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(binding.messageBox, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            });

        } else {
            Log.d("GalleryDebug", "Showing gallery recent view");
            // First, try to show gallery without animation to test basic functionality
            binding.galleryRecentLyt.setTranslationY(0);
            binding.galleryRecentLyt.setAlpha(1.0f);
            binding.galleryRecentLyt.setVisibility(View.VISIBLE);
            binding.bottomview.setVisibility(View.VISIBLE);
            Log.d("GalleryDebug", "Gallery shown immediately - visibility: " + binding.galleryRecentLyt.getVisibility());
            Log.d("GalleryDebug", "Gallery alpha: " + binding.galleryRecentLyt.getAlpha());
            Log.d("GalleryDebug", "Gallery translationY: " + binding.galleryRecentLyt.getTranslationY());

            // Load images for the gallery
            loadImages();

            // Setup permission text visibility and click handling
            setupPermissionText();

            Log.d("GalleryDebug", "Images loaded for immediate display, count: " + imagePaths.size());
        }

        binding.messageBox.requestFocus();
    }

    /**
     * Setup permission text visibility and click handling
     */
    private void setupPermissionText() {
        TextView managePermissionText = findViewById(R.id.managePermissionText);
        if (managePermissionText != null) {
            Log.d("PermissionText", "managePermissionText found: " + (managePermissionText != null));
            
            // Check if user has limited photo access
            boolean hasLimitedAccess = hasLimitedPhotoAccess();
            Log.d("PermissionText", "hasLimitedPhotoAccess: " + hasLimitedAccess);
            
            if (hasLimitedAccess) {
                // Show the permission text with underlined "Manage" text
                Log.d("PermissionText", "Showing permission text");
                managePermissionText.setVisibility(View.VISIBLE);
                setupUnderlinedManageText(managePermissionText);
            } else {
                // Hide the permission text if user has full access
                Log.d("PermissionText", "Hiding permission text");
                managePermissionText.setVisibility(View.GONE);
            }
        } else {
            Log.d("PermissionText", "managePermissionText is null - TextView not found");
        }
    }

    /**
     * Check if user has limited photo access
     */
    private boolean hasLimitedPhotoAccess() {
        // Use the new hasLimitedPhotoAccess method from GlobalPermissionPopup
        return GlobalPermissionPopup.hasLimitedPhotoAccess(this);
    }

    /**
     * Setup underlined "Manage" text with click handling
     */
    private void setupUnderlinedManageText(TextView textView) {
        String fullText = "You've given Enclosure permission to access only a select number of photos. Manage";
        String manageText = "Manage";
        
        // Create SpannableString with underlined "Manage" text
        SpannableString spannableString = new SpannableString(fullText);
        int manageStart = fullText.indexOf(manageText);
        int manageEnd = manageStart + manageText.length();
        
        if (manageStart != -1) {
            // Add underline to "Manage" text
            spannableString.setSpan(new UnderlineSpan(), manageStart, manageEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Add clickable span to "Manage" text
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // Open app settings
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    SwipeNavigationHelper.startActivityWithSwipe(grpChattingScreen.this, intent);
                }
                
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    // Set color for the clickable text using ThemeColorKey
                    String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                    int color = android.graphics.Color.parseColor(themeColor);
                    ds.setColor(color);
                }
            };
            
            spannableString.setSpan(clickableSpan, manageStart, manageEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Make the TextView clickable
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(spannableString);
        }
    }

    /**
     * Show the gallery recent view with smooth slide-up animation
     */
    private void showGalleryRecentView() {
        runOnUiThread(() -> {
            Log.d("GalleryDebug", "=== showGalleryRecentView called ===");

            // Check permissions first
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                Log.d("GalleryDebug", "Permission not granted, requesting permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
                return;
            }

            // Load images first to populate the RecyclerView
            Log.d("GalleryDebug", "Loading images...");
            loadImages();
            Log.d("GalleryDebug", "Images loaded, count: " + imagePaths.size());

            // Set visibility first
            binding.galleryRecentLyt.setVisibility(View.VISIBLE);
            binding.bottomview.setVisibility(View.VISIBLE);
            Log.d("GalleryDebug", "Gallery views set to VISIBLE");

            // Get gallery height and animate
            int galleryHeight = binding.galleryRecentLyt.getHeight();
            Log.d("GalleryDebug", "Gallery height: " + galleryHeight);
            Log.d("GalleryDebug", "Gallery visibility after setup: " + binding.galleryRecentLyt.getVisibility());
            Log.d("GalleryDebug", "Gallery alpha before animation: " + binding.galleryRecentLyt.getAlpha());
            Log.d("GalleryDebug", "Gallery translationY before animation: " + binding.galleryRecentLyt.getTranslationY());

            // If height is 0, use a default value for animation
            if (galleryHeight == 0) {
                galleryHeight = 300; // Use the layout height from XML
                Log.d("GalleryDebug", "Gallery height was 0, using default: " + galleryHeight);
            }

            // Animate the gallery view sliding up smoothly
            binding.galleryRecentLyt.setTranslationY(galleryHeight);
            binding.galleryRecentLyt.setAlpha(0.0f);
            binding.galleryRecentLyt.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(200)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator(1.0f))
                    .withEndAction(() -> {
                        Log.d("GalleryDebug", "Animation completed - Gallery visibility: " + binding.galleryRecentLyt.getVisibility());
                        Log.d("GalleryDebug", "Animation completed - Gallery alpha: " + binding.galleryRecentLyt.getAlpha());
                        Log.d("GalleryDebug", "Animation completed - Gallery translationY: " + binding.galleryRecentLyt.getTranslationY());
                    })
                    .start();
            Log.d("GalleryDebug", "Animation started with height: " + galleryHeight);
        });
    }

    /**
     * Hide the gallery recent view with smooth slide-down animation
     */
    private void hideGalleryRecentView() {
        runOnUiThread(() -> {
            if (binding.galleryRecentLyt.getVisibility() == View.VISIBLE) {
                // Animate the gallery view sliding down smoothly
                binding.galleryRecentLyt.animate()
                        .translationY(binding.galleryRecentLyt.getHeight())
                        .alpha(0.0f)
                        .setDuration(150) // Super fast dismiss animation
                        .setInterpolator(new android.view.animation.AccelerateInterpolator(1.5f))
                        .withEndAction(() -> {
                            binding.galleryRecentLyt.setVisibility(View.GONE);
                            binding.bottomview.setVisibility(View.GONE);
                        })
                        .start();
            }
        });
    }

}