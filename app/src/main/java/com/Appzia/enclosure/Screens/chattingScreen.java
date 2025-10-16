package com.Appzia.enclosure.Screens;

import java.util.ArrayList;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.Appzia.enclosure.Model.selectionBunchModel;
import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelperForward;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Utils.GlobalPermissionPopup;
import com.Appzia.enclosure.Utils.ChatadapterFiles.otherFunctions;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.WindowCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.DefaultTimeBar;
import androidx.media3.ui.PlayerView;
import androidx.media3.ui.TimeBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.ProgressDialog;
import android.content.OperationApplicationException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.widget.AutoCompleteTextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;

import com.Appzia.enclosure.Utils.BlurHelper;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.Model.forwardnameModel;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.Rect;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import java.text.DecimalFormat;

import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.DocumentDownloadPreviewAdapter;
import com.Appzia.enclosure.Adapter.DocumentThumbnailAdapter;
import com.Appzia.enclosure.Adapter.HorizontalDocumentThumbnailAdapter;
import com.Appzia.enclosure.Adapter.ImageAdapter;
import com.Appzia.enclosure.Adapter.chatAdapter;
import com.Appzia.enclosure.Adapter.emojiAdapter;
import com.Appzia.enclosure.Adapter.MainDocumentPreviewAdapter;
import com.Appzia.enclosure.Adapter.MultiImagePreviewAdapter;
import com.Appzia.enclosure.Adapter.HorizontalImageAdapter;
import com.Appzia.enclosure.Adapter.ContactPreviewAdapter;
import com.Appzia.enclosure.Adapter.HorizontalContactThumbnailAdapter;
import com.Appzia.enclosure.Adapter.MainImagePreviewAdapter;
import com.Appzia.enclosure.Adapters.VideoPreviewAdapter;
import com.Appzia.enclosure.Adapters.MainVideoPreviewAdapter;

import com.Appzia.enclosure.Fragments.CameraGalleryFragment;
import com.Appzia.enclosure.Model.Emoji;
import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.messagemodel2;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.AutoScrollLinearLayoutManager;
import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelper;
import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadHelper;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.PendingMessageUploader;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.StoragePathHelper;
import com.Appzia.enclosure.Utils.WhatsAppLikeImagePicker;
import com.Appzia.enclosure.Utils.WhatsAppLikeContactPicker;
import com.Appzia.enclosure.Utils.WhatsAppLikeVideoPicker;
import com.Appzia.enclosure.Utils.WhatsAppLikeDocumentPicker;
import com.Appzia.enclosure.Utils.FileUtils;
import com.Appzia.enclosure.Utils.HalfSwipeCallback;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityPersonalmsgLimitMsgBinding;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.crypto.SecretKey;

public class chattingScreen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityListener {
    ActivityPersonalmsgLimitMsgBinding binding;
    public static ProgressBar progressBar;
    public static boolean isChatScreenActive = false;
    private boolean isFirstLoad = true; // Flag for initial scroll only
    public static String isChatScreenActiveUid = "";
    private CardView customToastCard;
    private TextView customToastText;
    private static final String TAG = "XGXGXGXGXGXGXGXGX";
    private ActivityResultLauncher<String[]> permissionLauncher;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                    Manifest.permission.READ_MEDIA_IMAGES :
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                    Manifest.permission.READ_MEDIA_VIDEO :
                    Manifest.permission.READ_EXTERNAL_STORAGE
    };
    ColorStateList tintList;
    String original_name;

    private Window window;

    // Multi-selection forwarding fields
    private static final int FORWARD_MULTIPLE_REQUEST_CODE = 1001;
    private ArrayList<messageModel> selectedMessagesForForward;

    // Forward dialog fields
    private ArrayList<get_user_active_contact_list_Model> get_user_active_contact_forward_list = new ArrayList<>();
    private ArrayList<forwardnameModel> forwardNameList;

    // Multi-image preview dialogue
    private android.app.Dialog multiImagePreviewDialog;
    private View dx;
    private View richBoxForward;
    private RecyclerView recyclerview;
    private RecyclerView namerecyclerview;
    private TextView forwardText;


    private Chronometer chronometer;
    public static int notification = 0;

    private int limittolast = 10;
    String previousText = "";
    public static LinearLayout sendGrpBtm;
    private TextView elapsedTimeTextView;
    private String currentCaption = ""; // Single caption for all media items
    boolean lastMessageVisible = true;
    public static String finalName;
    public static Animation ab;
    AppCompatActivity mActivity;
    private float dY;
    EditText phone2;
    ArrayList<messageModel> messageList;
    public static LinearProgressIndicator progressbar;
    int notificationId;
    private SecretKey key;
    boolean scrollFlag = false;
    String themColor;
    private CountDownTimer countDownTimer;
    public static Animation a;
    public static chatAdapter chatAdapter;
    private boolean isLiveUpdate = false; // Set this true only for real-time listener messages
    private boolean isSyncingText = false; // Prevent circular text change calls
    public static String docName;
    public static File globalFile = null;
    public static File FullImageFile = null;
    private Set<String> uniqueDates = new HashSet<>();
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;
    public static String userFTokenKey;
    public static File micPhotoFile;
    LinearLayoutManager layoutManager;
    public static ExoPlayer playerPreview;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean batchingInProgress = false;
    private final List<messageModel> pendingMessages = new ArrayList<>();

    Handler handler = new Handler();
    Runnable updateProgressAction;
    private int resizeModeIndex = 0; // 0: FIT, 1: FILL, 2: ZOOM
    private boolean wasPlaying = false;
    public static EditText messageBox;
    private ConnectivityReceiver connectivityReceiver;
    private PendingMessageUploader pendingMessageUploader;
    int PICK_IMAGE_REQUEST_CODE = 1;
    int PICK_VIDEO_REQUEST_CODE = 2;
    int PICK_DOCUMENT_REQUEST_CODE = 5;
    int PICK_CONTACT_REQUEST_CODE = 7;
    private Long lastTimestamp = null;   // track oldest message timestamp

    private boolean isLastItemVisible = true;
    private BroadcastReceiver messageUploadReceiver;
    MediaRecorder recorder = new MediaRecorder();
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    int PICKFILE_REQUEST_CODE = 3;
    int REQUEST_CODE_PICK_CONTACT = 4;
    int PICK_CONTACT_MULTI_REQUEST_CODE = 6;
    MediaPlayer player;
    String profilepic;
    int down_count = 0;
    String deviceType;
    File path;
    ArrayList<String> list = new ArrayList<>();

    private boolean hasScrolledToLast = false;
    private final Map<String, Boolean> changeFlags = new HashMap<>();

    BottomSheetDialog bottomSheetDialog, bottomSheetDialogRec, bottomSheetDialogData;
    public static FirebaseDatabase database;
    AutoScrollLinearLayoutManager autoScrollLinearLayoutManager;

    ArrayList<messageModel> messageModels = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private StorageReference reference = FirebaseStorage.getInstance().getReference("chats_storage");
    private Uri GlobalUri;
    private ArrayList<Uri> selectedImageUris = new ArrayList<>();
    private ArrayList<File> selectedImageFiles = new ArrayList<>();
    private ArrayList<File> selectedFullImageFiles = new ArrayList<>();
    private ArrayList<selectionBunchModel> selectionBunch = new ArrayList<>();
    private ArrayList<String> imagePaths = new ArrayList<>();

    // Video multi-selection variables
    private ArrayList<Uri> selectedVideoUris = new ArrayList<>();
    private ArrayList<File> selectedVideoFiles = new ArrayList<>();
    private MainVideoPreviewAdapter mainVideoAdapter;

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
    private RecyclerView messageRecView;

    String currentPhotoPath;
    String name, caption, photo, createdBy;
    public static String fontSizePref;

    public static Context mContext;

    public static File mFilePath;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    public static Chronometer chronometerBtm;
    String time;
    TextView cancelContact;
    TextView saveContact;

    TextView mobile2Contact;
    TextView mobileContact;
    EditText firstnameContact;
    EditText lastNameContact;
    EditText phoneContact;
    EditText phone2Contact;
    BottomSheetDialog bottomSheetDialogContact;
    String msgLmtKey;

    public String modelId;

    private ConnectivityManager.NetworkCallback networkCallback;
    LinearProgressIndicator networkLoader;

    String FROM_NOTI_KEY;


    public static final int PAGE_SIZE = 20;
    private boolean isLoading = false;
    private String lastKey = null;
    private boolean isKeyboardOpen = false;
    private LinearLayout bottomPanel;
    private float yDelta = 0;

    private LinearLayout mBottomSheetLayout;
    // private ImageView arrow_i_v;
    RecyclerView emojiRecyclerview;
    emojiAdapter adapter;
    boolean emojiFlag = false;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Update the activity's intent finest codes
        Log.d("ChattingScreen", "onNewIntent called");
        initialLoadDone = true;
        isLoading = false;
        // Ensure progress bar is hidden when new intent is received
        if (binding.progressBar != null) {
            binding.progressBar.setVisibility(View.GONE);
        }
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            Log.d("handleIntent", "Intent received");

            // Test toast to see if handleIntent is called
            // Toast.makeText(mContext, "handleIntent called", Toast.LENGTH_SHORT).show();

            // Debug: Show all intent extras
            Bundle extras = intent.getExtras();
            if (extras != null) {
                for (String key : extras.keySet()) {
                    Object value = extras.get(key);
                    Log.d("handleIntent", "Intent extra - " + key + ": " + value);
                }
            }

            String receiverUidss = intent.getStringExtra("friendUidKey");
            name = intent.getStringExtra("nameKey");
            caption = intent.getStringExtra("captionKey");
            photo = intent.getStringExtra("photoKey");
            original_name = intent.getStringExtra("original_name");
            String forwardShort = intent.getStringExtra("forwardShort");
            boolean block = intent.getBooleanExtra("block", false); // <-- key point
            boolean iamblocked = intent.getBooleanExtra("iamblocked", false); // <-- key point

            // Check if coming from shareExternalDataScreen


            if (iamblocked) {


                binding.networkLoader.setVisibility(View.VISIBLE);
                binding.gallary.setEnabled(false);
                binding.emoji.setEnabled(false);
                binding.messageBox.setEnabled(false);
                binding.sendGrp.setEnabled(false);
                binding.name.setEnabled(false);
                binding.menu.setEnabled(false);
                binding.menunew.setEnabled(false);


                binding.gallary.setFocusable(false);
                binding.name.setFocusable(false);
                binding.emoji.setFocusable(false);
                binding.messageBox.setFocusable(false);
                binding.sendGrp.setFocusable(false);
                binding.name.setFocusable(false);
                binding.menu.setFocusable(false);
                binding.menunew.setFocusable(false);
                //  binding.messageRecView.setLayoutFrozen(true);


                // Prevent screen shot
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE);

                chatAdapter.notifyDataSetChanged();

                // Disable chat input or UI
            } else {
                // Not blocked
                binding.networkLoader.setVisibility(View.GONE);
                binding.gallary.setEnabled(true);
                binding.emoji.setEnabled(true);
                binding.messageBox.setEnabled(true);
                binding.sendGrp.setEnabled(true);
                binding.name.setEnabled(true);
                binding.menu.setEnabled(true);
                binding.menunew.setEnabled(true);


                binding.gallary.setFocusable(true);
                binding.name.setFocusable(true);
                binding.emoji.setFocusable(true);
                binding.messageBox.setFocusable(true);
                binding.sendGrp.setFocusable(true);
                binding.name.setFocusable(true);
                binding.menu.setFocusable(true);
                binding.menunew.setFocusable(true);
                //  binding.messageRecView.setLayoutFrozen(false);
//                    Toast.makeText(mContext, "Not Blocked", Toast.LENGTH_SHORT).show();
                // Eligible to screen short
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                chatAdapter.notifyDataSetChanged();
            }


            Log.d("handleIntent", "block value received = " + block);

            // Show toast and set UI
            if (block) {
                //  Toast.makeText(mContext, "User is blocked", Toast.LENGTH_SHORT).show();
                binding.blockUser.setText("true");
                binding.blockContainer.setVisibility(View.VISIBLE);
                binding.messageboxContainer.setVisibility(View.GONE);
            } else {
                // Toast.makeText(mContext, "User is not blocked", Toast.LENGTH_SHORT).show();
                binding.blockUser.setText("false");
                binding.blockContainer.setVisibility(View.GONE);
                binding.messageboxContainer.setVisibility(View.VISIBLE);
            }

            // Continue loading if forward
            if (forwardShort != null && receiverUidss != null) {
                binding.name.setText(name != null ? name : "Unknown");

                String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                String recroom = receiverUidss + uid;

                fetchMessages(recroom, () -> {
                    Log.d("handleIntent", "Messages fetched");
                });

                binding.progressBar.setVisibility(View.GONE);
            } else {
                Log.d("handleIntent", "forwardShort is null or receiverUidss is null");
            }
        } else {
            Log.e("handleIntent", "Intent is null");
        }
    }


    @Override
    public void onConnectivityChanged(boolean isConnected) {

        if (isConnected) {
            binding.networkLoader.setVisibility(View.GONE);

            // Upload pending messages when internet is restored
            if (pendingMessageUploader != null && userFTokenKey != null) {
                String receiverUid = getIntent().getStringExtra("friendUidKey");
                if (receiverUid != null) {
                    Log.d("PendingUpload", "Internet restored, checking for pending messages for receiver: " + receiverUid);
                    pendingMessageUploader.uploadPendingIndividualMessages(receiverUid, userFTokenKey);
                }
            }
        } else {
            binding.networkLoader.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d("XYZ", "onStart: ");
        String receiverUid = getIntent().getStringExtra("friendUidKey");

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

            Constant.getSfFuncion(mContext);
            String uid = Constant.getSF.getString(Constant.UID_KEY, "");
            if (uid.equals(receiverUid)) {


                String nameText = name;
                String youText = " (You)";
                SpannableString spannable = new SpannableString(nameText + youText);

                // Set color for name (black)
                spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                        0, nameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // Set color for (You) (gray)
                spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                        nameText.length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                binding.name.setText(nameText + youText);
            } else {
                binding.name.setText(name);

            }


            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(this);

                get_user_active_contact_list_Model dataModel = databaseHelper.getSingleDataNotification(receiverUid);

                String originalName = dataModel.getFull_name();


                if (originalName != null) {
                    if (uid.equals(receiverUid)) {


                        String nameText = originalName;
                        String youText = " (You)";
                        SpannableString spannable = new SpannableString(nameText + youText);

                        // Set color for name (black)
                        spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                                0, nameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        // Set color for (You) (gray)
                        spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                                nameText.length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.name.setText(nameText + youText);
                    } else {
                        binding.name.setText(originalName);

                    }


                } else {
                    if (uid.equals(receiverUid)) {


                        String nameText = name;
                        String youText = " (You)";
                        SpannableString spannable = new SpannableString(nameText + youText);

                        // Set color for name (black)
                        spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                                0, nameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        // Set color for (You) (gray)
                        spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                                nameText.length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.name.setText(nameText + youText);
                    } else {
                        binding.name.setText(name);

                    }
                }

            } catch (Exception e) {

                //  Log.d("cdscdscscs", "onMessageReceived: "+e.getMessage());
            }


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


        // TODO ERROR CAUSE


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ScrollText", "=== onResume START ===");

        loadImages();

        try {
            // Ensure RecyclerView/Adapter are initialized before any updates
            Constant.getSfFuncion(mContext);
            final String receiverUidOnResume = getIntent().getStringExtra("friendUidKey");

            boolean needsInit = false;
            try {
                needsInit = (chatAdapter == null)
                        || binding == null
                        || binding.messageRecView == null
                        || binding.messageRecView.getAdapter() == null
                        || binding.messageRecView.getLayoutManager() == null;
            } catch (Exception ignored) {
            }

            if (needsInit) {
                try {
                    setupRecyclerViewAndAdapter();
                } catch (Exception e) {
                    Log.e("ChattingScreen", "onResume: failed to init RecyclerView/Adapter: " + e.getMessage());
                }
            }

            // Load messages from local DB and refresh adapter on UI thread
            try {
                Log.d("ScrollText", "onResume: Loading messages from DB");
                getAllMessages(receiverUidOnResume, mContext);
                Log.d("ScrollText", "onResume: Message count after getAllMessages: " + messageList.size());

                runOnUiThread(() -> {
                    try {
                        if (chatAdapter != null) {
                            Log.d("ScrollText", "onResume: Updating adapter with message list");
                            otherFunctions.updateMessageList(new ArrayList<>(messageList), chatAdapter);

                            // Check current scroll position after update
                            LinearLayoutManager layoutManager = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
                            if (layoutManager != null) {
                                int firstVisible = layoutManager.findFirstVisibleItemPosition();
                                int lastVisible = layoutManager.findLastVisibleItemPosition();
                                int itemCount = chatAdapter.getItemCount();
                                Log.d("ScrollText", "onResume: After update - Item count: " + itemCount + ", First visible: " + firstVisible + ", Last visible: " + lastVisible);
                            }
                            // Note: Scrolling to last position removed from onResume - only happens in onCreate
                        } else {
                            Log.d("ScrollText", "onResume: chatAdapter is null");
                        }
                    } catch (Exception e) {
                        Log.d("ScrollText", "onResume: Exception in UI thread: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.e("PendingMessages", "onResume: error loading main messages: " + e.getMessage());
            }

            // Append any pending (upload in-progress) messages
            try {
                loadPendingMessages(receiverUidOnResume, false); // Don't scroll on onResume
            } catch (Exception e) {
                Log.e("PendingMessages", "onResume: error loading pending messages: " + e.getMessage());
            }
        } catch (Exception ignored) {
        }


        Constant.getSfFuncion(mContext);
        final String receiverUidxc = getIntent().getStringExtra("friendUidKey");
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        Webservice.checkIfBlockedByReceiver(mContext, uid, receiverUidxc, new Webservice.CallbackBlock() {
            @Override
            public void onBlocked(boolean isBlocked, String message) {
                if (isBlocked) {
                    // Blocked by receiver
                    // Toast.makeText(mContext, "Blocked: " + message, Toast.LENGTH_SHORT).show();

                    binding.networkLoader.setVisibility(View.VISIBLE);
                    binding.gallary.setEnabled(false);
                    binding.emoji.setEnabled(false);
                    binding.messageBox.setEnabled(false);
                    binding.sendGrp.setEnabled(false);
                    binding.name.setEnabled(false);
                    binding.menu.setEnabled(false);
                    binding.menunew.setEnabled(false);


                    binding.gallary.setFocusable(false);
                    binding.name.setFocusable(false);
                    binding.emoji.setFocusable(false);
                    binding.messageBox.setFocusable(false);
                    binding.sendGrp.setFocusable(false);
                    binding.name.setFocusable(false);
                    binding.menu.setFocusable(false);
                    binding.menunew.setFocusable(false);
                    //  binding.messageRecView.setLayoutFrozen(true);


                    // Prevent screen shot
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE);

                    chatAdapter.notifyDataSetChanged();

                    // Disable chat input or UI
                } else {
                    // Not blocked
                    binding.networkLoader.setVisibility(View.GONE);
                    binding.gallary.setEnabled(true);
                    binding.emoji.setEnabled(true);
                    binding.messageBox.setEnabled(true);
                    binding.sendGrp.setEnabled(true);
                    binding.name.setEnabled(true);
                    binding.menu.setEnabled(true);
                    binding.menunew.setEnabled(true);


                    binding.gallary.setFocusable(true);
                    binding.name.setFocusable(true);
                    binding.emoji.setFocusable(true);
                    binding.messageBox.setFocusable(true);
                    binding.sendGrp.setFocusable(true);
                    binding.name.setFocusable(true);
                    binding.menu.setFocusable(true);
                    binding.menunew.setFocusable(true);
                    //  binding.messageRecView.setLayoutFrozen(false);
//                    Toast.makeText(mContext, "Not Blocked", Toast.LENGTH_SHORT).show();
                    // Eligible to screen short
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {
                //  Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        // Removed misplaced touch/gesture block that referenced dataRecview outside its scope.
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

        isChatScreenActive = true;
// Use the friend's UID so FCM can match and suppress notifications for this chat
        isChatScreenActiveUid = getIntent().getStringExtra("friendUidKey");

        msgLmtKey = getIntent().getStringExtra("msgLmtKey");
        boolean iamblocked = getIntent().getBooleanExtra("iamblocked", false); // <-- key point
        ItemTouchHelper.Callback callback = new HalfSwipeCallback(mContext, (LinearLayout) binding.replylyout, binding.messageBox, binding.namereply, binding.msgreply, binding.imgreply, binding.view, binding.cancel, binding.replysvg, binding.view2, binding.view3, binding.bottom, binding.messageRecView, chatAdapter, binding.contactContainer, binding.firstText, binding.imgcardview, binding.pageLyt, binding.pageText, binding.replyDataType, binding.listcrntpostion, messageList, binding.captionTextView, binding.fileNameTextview, binding.thumbnailTextview, binding.documentTextview, binding.name, binding.thumbnailimagedata, binding.imageWidthDp, binding.imageHeightDp, binding.aspectRatio, iamblocked, binding.editLyt, binding.imgcardviewVoiceAudio, binding.imgcardviewVoiceMusic);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.messageRecView);


        Log.d("XYZ", "onResume: ");
        Constant.getSfFuncion(getApplicationContext());
        String receiverUid = getIntent().getStringExtra("friendUidKey");
        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
        final String senderRoom = senderId + receiverUid;
        final String receiverRoom = receiverUid + senderId;
        Webservice.get_individual_chatting(mContext, senderId, receiverUid, binding.limitStatus, binding.totalMsgLimit);


        boolean fromShareExternalData = getIntent().getBooleanExtra("fromShareExternalData", false);
        Log.d("handleIntent", "fromShareExternalData: " + fromShareExternalData);

        if (fromShareExternalData) {
            Log.d("handleIntent", "Processing shared message models");
            ArrayList<messagemodel2> sharedMessageModels = getIntent().getParcelableArrayListExtra("sharedMessageModels");
            String messageType = getIntent().getStringExtra("messageType");

            Log.d("handleIntent", "sharedMessageModels size: " + (sharedMessageModels != null ? sharedMessageModels.size() : "null"));
            Log.d("handleIntent", "messageType: " + messageType);

            if (sharedMessageModels != null && !sharedMessageModels.isEmpty()) {
                Log.d("handleIntent", "Adding shared messages to chat after fetchMessages");

                // Get receiverUid for fetchMessages
                receiverUid = getIntent().getStringExtra("friendUidKey");
                uid = Constant.getSF.getString(Constant.UID_KEY, "");
                String recroom = receiverUid + uid;

                // First fetch messages, then add shared messages
                fetchMessages(recroom, () -> {
                    Log.d("handleIntent", "Messages fetched, now adding shared messages");
                    // Add the shared messages to the chat after fetchMessages completes
                    runOnUiThread(() -> {
                        //  Toast.makeText(mContext, "sharedMessageModels: " + sharedMessageModels.size(), Toast.LENGTH_LONG).show();
                        chatAdapter.setMessagesFromShareExternalData(sharedMessageModels);
                        // Removed scrollToBottomSafely from shared content handling
                        // if (isFirstTimeOnCreate && sharedMessageModels.size() > 0) {
                        //     scrollToBottomSafely();
                        // }
                        binding.progressBar.setVisibility(View.GONE);
                    });
                });
            } else {
                Log.e("handleIntent", "sharedMessageModels is null or empty");
            }
        } else {
            Log.d("handleIntent", "Not from shareExternalDataScreen");
        }


        binding.forwardAll.setCompoundDrawableTintList(tintList);

        LayerDrawable layerDrawable = (LayerDrawable) binding.forwardAll.getBackground();
        GradientDrawable borderDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.border);
        borderDrawable.setStroke(5, Color.parseColor(themColor));
        binding.forwardAll.invalidate();

    }


    public List<messageModel> getMessageList() {
        return messageList;
    }

    public RecyclerView getMessageRecView() {
        return binding.messageRecView;
    }

    // Helper method to check if user is within the last 3 messages
    private boolean isWithinLastThreeMessages() {
        if (messageList == null || messageList.isEmpty()) {
            Log.d("KEYBOARD_SCROLL", "isWithinLastThreeMessages: messageList is null or empty");
            return false;
        }

        LinearLayoutManager layoutManager = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
        if (layoutManager == null) {
            Log.d("KEYBOARD_SCROLL", "isWithinLastThreeMessages: layoutManager is null");
            return false;
        }

        int totalItems = messageList.size();
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

        Log.d("KEYBOARD_SCROLL", "isWithinLastThreeMessages: totalItems=" + totalItems + ", lastVisiblePosition=" + lastVisiblePosition);

        // Check if user is within the last 3 messages
        boolean result = lastVisiblePosition >= totalItems - 3;
        Log.d("KEYBOARD_SCROLL", "isWithinLastThreeMessages result: " + result);
        return result;
    }

    // Handle keyboard scroll logic (extracted for reuse)
    private void handleKeyboardScroll() {
        boolean isLastVisible = isLastItemVisible;
        boolean isWithinLastThree = isWithinLastThreeMessages();

        Log.d("KEYBOARD_SCROLL", "isLastItemVisible: " + isLastVisible);
        Log.d("KEYBOARD_SCROLL", "isWithinLastThreeMessages: " + isWithinLastThree);
        Log.d("KEYBOARD_SCROLL", "messageList size: " + (messageList != null ? messageList.size() : "null"));

        // Check if user is within the last 3 messages
        if (isLastVisible || isWithinLastThree) {
            Log.d("KEYBOARD_SCROLL", "Auto-scrolling to last message");
            // Disable manual scrolling temporarily to prevent interference
            binding.messageRecView.setNestedScrollingEnabled(false);
            binding.messageRecView.setScrollContainer(false);

            // Scroll to last message when keyboard opens and user is near bottom
            scrollToLastMessageWithRetry(0);

            // Re-enable scrolling after keyboard settles
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("KEYBOARD_SCROLL", "Re-enabling manual scrolling");
                    binding.messageRecView.setNestedScrollingEnabled(true);
                    binding.messageRecView.setScrollContainer(true);
                }
            }, 2000); // 2 seconds delay
        } else {
            Log.d("KEYBOARD_SCROLL", "Not scrolling - user not within last 3 messages");
        }
    }

    // Fast scroll method - immediate execution with minimal delays
    private void scrollToLastMessageWithRetry(int attempt) {
        Log.d("KEYBOARD_SCROLL", "=== scrollToLastMessageWithRetry START ===");
        Log.d("KEYBOARD_SCROLL", "attempt: " + attempt);

        if (messageList == null || messageList.isEmpty()) {
            Log.d("KEYBOARD_SCROLL", "Cannot scroll - messageList is null or empty");
            return;
        }

        final int maxAttempts = 3;
        final int targetPosition = messageList.size() - 1;

        Log.d("KEYBOARD_SCROLL", "scrollToLastMessageWithRetry attempt: " + attempt + "/" + maxAttempts);
        Log.d("KEYBOARD_SCROLL", "targetPosition: " + targetPosition + ", messageList.size(): " + messageList.size());
        
        // Check what type of message is at the target position
        if (targetPosition >= 0 && targetPosition < messageList.size()) {
            messageModel targetMessage = messageList.get(targetPosition);
            if (targetMessage != null) {
                Log.d("KEYBOARD_SCROLL", "Target message UID=" + targetMessage.getUid() + ", Message=" + targetMessage.getMessage());
            }
        }

        // Minimal delay - much faster
        int delay = 50 + (attempt * 25); // 50ms, 75ms, 100ms
        Log.d("KEYBOARD_SCROLL", "delay: " + delay + "ms");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("KEYBOARD_SCROLL", "=== EXECUTING SCROLL ===");
                Log.d("KEYBOARD_SCROLL", "Executing scroll to position: " + targetPosition + " (attempt " + (attempt + 1) + ")");

                // Check current position before scroll
                LinearLayoutManager layoutManager = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
                if (layoutManager != null) {
                    int beforeScroll = layoutManager.findLastVisibleItemPosition();
                    Log.d("KEYBOARD_SCROLL", "Before scroll - lastVisible: " + beforeScroll);
                }

                // Use instant scroll first to prevent flickering, then smooth scroll
                binding.messageRecView.scrollToPosition(targetPosition);
                Log.d("KEYBOARD_SCROLL", "scrollToPosition called (instant) to position: " + targetPosition);
                
                // Check what's actually visible after scroll
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    LinearLayoutManager layoutManager2 = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
                    if (layoutManager2 != null) {
                        int firstVisible = layoutManager2.findFirstVisibleItemPosition();
                        int lastVisible = layoutManager2.findLastVisibleItemPosition();
                        Log.d("KEYBOARD_SCROLL", "After instant scroll - First visible: " + firstVisible + ", Last visible: " + lastVisible + ", Target: " + targetPosition);
                    }
                }, 10);
                
                // Then smooth scroll for better visual effect
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    binding.messageRecView.smoothScrollToPosition(targetPosition);
                    Log.d("KEYBOARD_SCROLL", "smoothScrollToPosition called to position: " + targetPosition);
                }, 50);

                // Check if we need to retry after a very short delay
                if (attempt < maxAttempts - 1) {
                    Log.d("KEYBOARD_SCROLL", "Setting up retry check in 100ms");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("KEYBOARD_SCROLL", "=== CHECKING SCROLL RESULT ===");
                            LinearLayoutManager layoutManager = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
                            if (layoutManager != null) {
                                int currentLastVisible = layoutManager.findLastVisibleItemPosition();
                                Log.d("KEYBOARD_SCROLL", "After scroll - currentLastVisible: " + currentLastVisible + ", target: " + targetPosition);

                                // If we're not at the target, retry
                                if (currentLastVisible < targetPosition) {
                                    Log.d("KEYBOARD_SCROLL", "RETRY NEEDED - not at target position");
                                    Log.d("KEYBOARD_SCROLL", "Calling scrollToLastMessageWithRetry(" + (attempt + 1) + ")");
                                    scrollToLastMessageWithRetry(attempt + 1);
                                } else {
                                    Log.d("KEYBOARD_SCROLL", "SUCCESS - reached target position");
                                    // Final force scroll with minimal delay
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("KEYBOARD_SCROLL", "Final force scroll to ensure position");
                                            binding.messageRecView.scrollToPosition(targetPosition);
                                        }
                                    }, 100); // Much faster - 100ms instead of 500ms
                                }
                            } else {
                                Log.d("KEYBOARD_SCROLL", "ERROR - layoutManager is null during retry check");
                            }
                        }
                    }, 100); // Much faster check - 100ms instead of 300ms
                } else {
                    Log.d("KEYBOARD_SCROLL", "No more retries - this was the final attempt");
                }
            }
        }, delay);

        Log.d("KEYBOARD_SCROLL", "=== scrollToLastMessageWithRetry END ===");
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPersonalmsgLimitMsgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup swipe gestures for back navigation
        SwipeNavigationHelper.setupSwipeGestures(this);

        // Fix keyboard behavior - adjust resize to keep bottom content above keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Ensure the root view handles keyboard properly
        binding.getRoot().setFitsSystemWindows(true);

        // Add keyboard visibility listener to handle layout adjustments
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                binding.getRoot().getWindowVisibleDisplayFrame(r);
                int screenHeight = binding.getRoot().getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                // Check if we're in multi-selection mode
                boolean isMultiSelectActive = chatAdapter != null && chatAdapter.isMultiSelectMode();

                if (keypadHeight > screenHeight * 0.15) { // Keyboard is visible
                    // Keyboard is open, but respect multi-selection state
                    if (isMultiSelectActive) {
                        binding.bottom.setVisibility(View.GONE);
                        Log.d("MultiSelect", "Keyboard open but multi-select active - bottom kept GONE");
                    } else {
                        binding.bottom.setVisibility(View.VISIBLE);
                        Log.d("MultiSelect", "Keyboard open and multi-select inactive - bottom set to VISIBLE");
                    }
                } else {
                    // Keyboard is closed, but respect multi-selection state
                    if (isMultiSelectActive) {
                        binding.bottom.setVisibility(View.GONE);
                        Log.d("MultiSelect", "Keyboard closed but multi-select active - bottom kept GONE");
                    } else {
                        binding.bottom.setVisibility(View.VISIBLE);
                        Log.d("MultiSelect", "Keyboard closed and multi-select inactive - bottom set to VISIBLE");
                    }
                }
            }
        });

        // Immediately hide progress bar to prevent any flash
        if (binding.progressBar != null) {
            binding.progressBar.setVisibility(View.GONE);
        }

        mContext = chattingScreen.this;
        customToastCard = findViewById(R.id.includedToast);
        customToastText = customToastCard.findViewById(R.id.toastText);
        database = FirebaseDatabase.getInstance();
        modelId = database.getReference().push().getKey();

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Webservice.get_emoji(mContext, chattingScreen.this);

        emojiRecyclerview = findViewById(R.id.emojiRecyclerview);

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean allGranted = true;
                    for (String permission : REQUIRED_PERMISSIONS) {
                        boolean granted = permissions.getOrDefault(permission, false);
                        Log.d(TAG, "Permission " + permission + ": " + (granted ? "Granted" : "Denied"));
                        if (!granted) {
                            allGranted = false;
                            // Check if permission is permanently denied
                            if (!shouldShowRequestPermissionRationale(permission)) {
                                Log.d(TAG, "Permission " + permission + " permanently denied");
                                Toast.makeText(this,
                                        "Please enable Camera and Photos permissions in Settings.",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                SwipeNavigationHelper.startActivityWithSwipe(chattingScreen.this, intent);
                            } else {
                                Log.d(TAG, "Permission " + permission + " denied, can request again");
                                Toast.makeText(this, "Camera and Photos Permissions are Required.", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    }
                    if (allGranted) {
                        Log.d(TAG, "All permissions granted, launching fragment");
                        dispatchTakePictureIntent();
                    }
                });


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
                runOnUiThread(() -> {
                    // Stop loader for last item once upload completes
                    chatAdapter.setLastItemVisible(false);
                });
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messageUploadReceiver, new IntentFilter("MESSAGE_UPLOADED"));

        // Listen for layout changes to measure keyboard height


        FROM_NOTI_KEY = getIntent().getStringExtra("FROM_NOTI_KEY");


        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, RECEIVER_EXPORTED);

        // Initialize pending message uploader
        pendingMessageUploader = new PendingMessageUploader(this);


        mActivity = chattingScreen.this;

        messageList = new ArrayList<>();

        deviceType = getIntent().getStringExtra("deviceType");


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
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FF6D00"));

                } else if (themColor.equals("#00A3E9")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.ec_modern);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                } else if (themColor.equals("#7adf2a")) {
//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.popatilogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00C853"));


                } else if (themColor.equals("#ec0001")) {


//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.redlogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#ec7500"));


                } else if (themColor.equals("#16f3ff")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.bluelogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00F365"));

                } else if (themColor.equals("#FF8A00")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.orangelogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FFAB00"));

                } else if (themColor.equals("#7F7F7F")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.graylogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);

                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#314E6D"));

                } else if (themColor.equals("#D9B845")) {

//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.yellowlogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#b0d945"));
                } else if (themColor.equals("#346667")) {
//                    binding.view.setBackgroundTintList(tintList);
//                    binding.view2.setBackgroundTintList(tintList);
//                    binding.view3.setBackgroundTintList(tintList);
                    binding.menu.setImageResource(R.drawable.greenlogoppng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#729412"));

                } else if (themColor.equals("#9846D9")) {

                    binding.menu.setImageResource(R.drawable.voiletlogopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#d946d1"));

                } else if (themColor.equals("#A81010")) {
                    binding.menu.setImageResource(R.drawable.red2logopng);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#D85D01"));

                } else {
                    binding.menu.setImageResource(R.drawable.ec_modern);
                    binding.sendGrp.setBackgroundTintList(tintList);
                    binding.downImg.setImageTintList(tintList);
                    binding.countDownTxt.setTextColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineSpotShadowColor(Color.parseColor(themColor));
                    binding.downCardview.setOutlineAmbientShadowColor(Color.parseColor(themColor));
                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));

                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }


        binding.searchEt.clearFocus();


        userFTokenKey = getIntent().getStringExtra("userFTokenKey");

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Constant.getSfFuncion(getApplicationContext());
                final String receiverUid = getIntent().getStringExtra("friendUidKey");
                final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                final String receiverRoom = receiverUid + senderId;
                String query = s.toString().trim();

                if (!query.isEmpty()) {
                    searchMessages(query, receiverRoom);
                } else {
                    // Optionally reload all messages if search is cleared
                    // TODO: 21/04/25  KEEP as it is messga list as it first
                    initialLoadDone = false;

                    fetchMessages(receiverRoom, () -> {

                    }, false); // Don't scroll when search is cleared
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mContext = binding.getRoot().getContext();


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


        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
//            Rect r = new Rect();
//            rootView.getWindowVisibleDisplayFrame(r);
//            int screenHeight = rootView.getRootView().getHeight();
//            int keyboardHeight = screenHeight - r.bottom;
//
//            if (keyboardHeight > screenHeight / 3) {
//                Log.d("addOnGlobalLayoutListener", "visible: ");
//
//
//                // Scroll to the last message
//                if (lastMessageVisible) {
//                    RecyclerView.LayoutManager layoutManager = binding.messageRecView.getLayoutManager();
//                    if (layoutManager != null) {
//                        int lastPosition = layoutManager.getItemCount() - 1;
//                        binding.messageRecView.scrollToPosition(lastPosition);
//                    }
//                    lastMessageVisible = false;
//
//                }
//
//
//            } else {
//                Log.d("addOnGlobalLayoutListener", "Hidden: ");
//
//                Log.d("TAG", "hrrrrrrr: " + keyboardHeight);
//
//                // binding.messageBox.clearFocus();
//
//
//            }


        });


        Constant.getSfFuncion(mContext);
        messageBox = findViewById(R.id.messageBox);

        // Setup Gallery Recent Images View - positioned above bottom navigation
        setupGalleryRecentView();

        Constant.bottomsheetContact(mContext, R.layout.add_to_existing_contact_layout);
        bottomSheetDialogContact = Constant.bottomSheetDialogContact;

        cancelContact = Constant.bottomSheetDialogContact.findViewById(R.id.cancel);
        saveContact = Constant.bottomSheetDialogContact.findViewById(R.id.save);
        mobileContact = Constant.bottomSheetDialogContact.findViewById(R.id.mobile);
        firstnameContact = Constant.bottomSheetDialogContact.findViewById(R.id.firstname);
        lastNameContact = Constant.bottomSheetDialogContact.findViewById(R.id.lastName);
        phoneContact = Constant.bottomSheetDialogContact.findViewById(R.id.phoneNumber);
        phone2Contact = Constant.bottomSheetDialogContact.findViewById(R.id.phoneNumber2);
        mobile2Contact = Constant.bottomSheetDialogContact.findViewById(R.id.mobile2);


        progressBar = findViewById(R.id.progressBar);


        Constant.getSfFuncion(getApplicationContext());
        final String receiverUid = getIntent().getStringExtra("friendUidKey");
        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
        final String senderRoom = senderId + receiverUid;
        final String receiverRoom = receiverUid + senderId;
        Log.d("senderRoom", senderRoom + receiverRoom);

        Webservice.get_individual_chatting(mContext, senderId, receiverUid, binding.limitStatus, binding.totalMsgLimit);


        //     database.setPersistenceEnabled(true);


        // todo Initialize message list if it's null
        if (messageList == null) {
            messageList = new ArrayList<>();
        }

        // todo Set up RecyclerView and Adapter
        setupRecyclerViewAndAdapter();
        boolean iamblocked = getIntent().getBooleanExtra("iamblocked", false); // <-- key point
        ItemTouchHelper.Callback callback = new HalfSwipeCallback(mContext, (LinearLayout) binding.replylyout, binding.messageBox, binding.namereply, binding.msgreply, binding.imgreply, binding.view, binding.cancel, binding.replysvg, binding.view2, binding.view3, binding.bottom, binding.messageRecView, chatAdapter, binding.contactContainer, binding.firstText, binding.imgcardview, binding.pageLyt, binding.pageText, binding.replyDataType, binding.listcrntpostion, messageList, binding.captionTextView, binding.fileNameTextview, binding.thumbnailTextview, binding.documentTextview, binding.name, binding.thumbnailimagedata, binding.imageWidthDp, binding.imageHeightDp, binding.aspectRatio, iamblocked, binding.editLyt, binding.imgcardviewVoiceAudio, binding.imgcardviewVoiceMusic);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.messageRecView);


        // Verify Firebase persistence is working
        ///  checkFirebaseOfflineCapability();

        // Fast path: load locally cached messages instantly (no spinner)
        Log.d("ScrollText", "=== onCreate: Calling initializeChatData ===");
        initializeChatData(receiverUid, receiverRoom);
        
        // Mark that onCreate has been called once
        isFirstTimeOnCreate = false;
        Log.d("ScrollText", "=== onCreate: isFirstTimeOnCreate set to false ===");


        try {
            database.getReference().child(Constant.CHAT).child(receiverRoom).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Log.d("FirebaseListener", "onDataChange called");
                    messageModel model = snapshot.getValue(messageModel.class);
                    if (model != null) {
                        // Parse selectionBunch from Firebase
                        messageModel.parseSelectionBunchFromSnapshot(snapshot, model);
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


        chronometer = findViewById(R.id.chronometer);
        elapsedTimeTextView = findViewById(R.id.elapsed_time_textview);


        bottomSheetDialog = new BottomSheetDialog(chattingScreen.this, R.style.BottomSheetDialog);
        View viewShape = getLayoutInflater().inflate(R.layout.bottom_sheet_layout_pick, null, false);
        bottomSheetDialog.setContentView(viewShape);
//        LinearLayoutCompat cameraLyt = viewShape.findViewById(R.id.cameraLyt);
//        LinearLayoutCompat galleryLyt = viewShape.findViewById(R.id.galleryLyt);
//        LinearLayoutCompat documentLyt = viewShape.findViewById(R.id.documentLyt);
//        LinearLayoutCompat contactLyt = viewShape.findViewById(R.id.contactLyt);
//        LinearLayoutCompat videoLyt = viewShape.findViewById(R.id.videoLyt);

        binding.videoLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.Vibrator(mContext);

                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");

                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                hideGalleryRecentView();

                // Use new permission flow for video picker
                GlobalPermissionPopup.handleGalleryClickWithLimitedAccess(chattingScreen.this, new GlobalPermissionPopup.PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {
                        // Use WhatsApp-like video picker for multi-selection
                        Intent pickerIntent = new Intent(mContext, WhatsAppLikeVideoPicker.class);
                        pickerIntent.putExtra(WhatsAppLikeVideoPicker.EXTRA_MAX_SELECTION, 5 - selectedVideoUris.size());
                        pickerIntent.putParcelableArrayListExtra(WhatsAppLikeVideoPicker.EXTRA_SELECTED_VIDEOS, new ArrayList<>(selectedVideoUris));
                        SwipeNavigationHelper.startActivityForResultWithSwipe(chattingScreen.this, pickerIntent, PICK_VIDEO_REQUEST_CODE);
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


        binding.cameraLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.Vibrator(mContext);

                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");

                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                hideGalleryRecentView();
                bottomSheetDialog.dismiss();
                // Initialize permission launcher


                // Check and request permissions
                askCameraPermissions();
            }
        });

        binding.galleryLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ImageUpload", "=== GALLERY BUTTON CLICKED (Main) ===");
                Constant.Vibrator(mContext);


                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");

                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                // Hide keyboard if open
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(binding.messageBox.getWindowToken(), 0);
                }

                // Use WhatsApp-like image picker
                Log.d("ImageUpload", "=== LAUNCHING WhatsAppLikeImagePicker ===");
                Log.d("ImageUpload", "PICK_IMAGE_REQUEST_CODE: " + PICK_IMAGE_REQUEST_CODE);
                Log.d("ImageUpload", "Current selectedImageUris size: " + selectedImageUris.size());

                Intent pickerIntent = new Intent(mContext, WhatsAppLikeImagePicker.class);
                pickerIntent.putExtra(WhatsAppLikeImagePicker.EXTRA_MAX_SELECTION, 30 - selectedImageUris.size());
                pickerIntent.putParcelableArrayListExtra(WhatsAppLikeImagePicker.EXTRA_SELECTED_IMAGES, new ArrayList<>(selectedImageUris));
                SwipeNavigationHelper.startActivityForResultWithSwipe(chattingScreen.this, pickerIntent, PICK_IMAGE_REQUEST_CODE);
            }
        });

        binding.documentLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.Vibrator(mContext);

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
                SwipeNavigationHelper.startActivityForResultWithSwipe(chattingScreen.this, Intent.createChooser(intent, "Select Documents"), PICK_DOCUMENT_REQUEST_CODE);

                bottomSheetDialog.dismiss();
            }
        });

        binding.contactLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.Vibrator(mContext);


                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");

                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                hideGalleryRecentView();

                // Use custom contact picker for multiple selection
                Log.d("ContactSelection", "Opening custom contact picker for multi-selection");
                Intent intent = new Intent(mContext, WhatsAppLikeContactPicker.class);
                intent.putExtra(WhatsAppLikeContactPicker.EXTRA_MAX_SELECTION, 50);
                intent.putParcelableArrayListExtra(WhatsAppLikeContactPicker.EXTRA_SELECTED_CONTACTS, new ArrayList<WhatsAppLikeContactPicker.ContactInfo>());
                SwipeNavigationHelper.startActivityForResultWithSwipe(chattingScreen.this, intent, PICK_CONTACT_MULTI_REQUEST_CODE);
                bottomSheetDialog.dismiss();
            }
        });


        //    Toast.makeText(this, receiverUid, Toast.LENGTH_SHORT).show();

        // setSenderAdapter();
        messageList.clear();
        userFTokenKey = getIntent().getStringExtra("userFTokenKey");


        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Constant.Vibrator50(mContext);

                collapse(binding.replylyout, binding.editLyt, binding.date, binding.progressBar);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        float elevationDp = 10.0f; // 10 dp
                        float elevationPx = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                elevationDp,
                                getResources().getDisplayMetrics()
                        );
                        binding.bottom.setElevation(elevationPx);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });
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
                    Log.d("VIDEO_DIALOG_SHOW", "chattingScreen.showMultiImagePreviewDialog called");
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

                }
                else {

                    String message = binding.messageBox.getText().toString().trim();
                    if (message.isEmpty()) return;

                    new Thread(() -> {
                        try {
                            String modelId = database.getReference().push().getKey();
                            String currentDateTimeString = new SimpleDateFormat("hh:mm a").format(new Date());

                            // Create messageModel and update messageList (as in original code)
                            messageModel model = createMessageModel(message, modelId, currentDateTimeString, senderId, receiverUid); // Refactor model creation

                            // Store message in SQLite pending table before upload
                            try {
                                new DatabaseHelper(mContext).insertPendingMessage(model);
                                Log.d("PendingMessages", "Message stored in pending table: " + model.getModelId());
                            } catch (Exception e) {
                                Log.e("PendingMessages", "Error storing message in pending table: " + e.getMessage(), e);
                            }

                            updateMessageList(model);

                            messagemodel2 model2 = new messagemodel2(
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
                                    model.getUserName(),
                                    model.getReplytextData(),
                                    model.getReplyKey(),
                                    model.getReplyType(),
                                    model.getReplyOldData(),
                                    model.getReplyCrtPostion(),
                                    model.getModelId(),
                                    model.getReceiverUid(),
                                    model.getForwaredKey(),
                                    model.getGroupName(),
                                    model.getDocSize(),
                                    model.getFileName(),
                                    model.getThumbnail(),
                                    model.getFileNameThumbnail(),
                                    model.getCaption(),
                                    model.getNotification(),
                                    model.getCurrentDate(),
                                    model.getEmojiModel(),
                                    model.getEmojiCount(),
                                    model.getTimestamp(),
                                    0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                            );

                            //TODO : active : 0 = still loading
                            //TODO : active : 1 = completed


                            Log.e("SendGroupClickListener", "SUccessfname: " + model.getExtension());


                            runOnUiThread(() -> {
                                chatAdapter.itemAdd(binding.messageRecView);
                                chatAdapter.setLastItemVisible(true); // Show progress for pending message
                                chatAdapter.notifyItemInserted(messageList.size() - 1);
                                binding.messageBox.setEnabled(true);
                            });

                            if (binding.limitStatus.getText().toString().equals("0")) {
                                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, userFTokenKey);
                                uploadHelper.uploadContent(Constant.Text, null, message, modelId, null, model.getFileNameThumbnail(), null, model.getName(), model.getPhone(), model.getMiceTiming(), null, model.getExtension(), receiverUid, model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(), model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(), model.getImageWidth(), model.getImageHeight(), model.getAspectRatio());
                            } else {
                                Constant.showCustomToast(
                                        "Msg limit set for privacy in a day - " + binding.totalMsgLimit.getText().toString(),
                                        customToastCard, customToastText
                                );
                            }


                        } catch (Exception e) {
                            Log.e("SendGroupClickListener", "Error: " + e.getMessage());
                            runOnUiThread(() -> binding.messageBox.setEnabled(true));
                        }
                        runOnUiThread(() -> {
                            binding.messageBox.setText("");
                        });


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
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (imm != null) {
                                        imm.showSoftInput(binding.messageBox, InputMethodManager.SHOW_IMPLICIT);
                                    }

                                }
                                binding.messageBox.requestFocus();
                            }
                        });

                    }).start();
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
            GlobalPermissionPopup.handleGalleryClickWithLimitedAccess(chattingScreen.this, new GlobalPermissionPopup.PermissionCallback() {
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
//

        binding.blockClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogLayoutColor = new Dialog(chattingScreen.this);
                ((android.app.Dialog) dialogLayoutColor).setContentView(R.layout.delete_popup_row);
                dialogLayoutColor.setCanceledOnTouchOutside(true);
                dialogLayoutColor.setCancelable(true);
                dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

                dialogLayoutColor.getWindow().setGravity(Gravity.CENTER);
                WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();

                dialogLayoutColor.getWindow().setAttributes(layoutParams);
                AppCompatButton ok = dialogLayoutColor.findViewById(R.id.ok);
                AppCompatButton cancel = dialogLayoutColor.findViewById(R.id.cancel);
                AppCompatImageView dismiss = dialogLayoutColor.findViewById(R.id.dismiss);

                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogLayoutColor.dismiss();
                    }
                });

                dialogLayoutColor.show();


                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.getReference().child(Constant.CHAT).child(receiverRoom)
                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        chatAdapter.notifyDataSetChanged();


                                        Constant.getSfFuncion(mContext);
                                        String sId = Constant.getSF.getString(Constant.UID_KEY, "");

                                        Webservice.delete_sender_all_msg(mContext, sId, receiverUid, receiverRoom);

                                    }
                                });
                        dialogLayoutColor.dismiss();

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogLayoutColor.dismiss();
                    }
                });

            }
        });

        binding.blockUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.getSfFuncion(mContext);
                String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                Webservice.unblockUser(mContext, uid, receiverUid, chattingScreen.this, binding.blockContainer, binding.messageboxContainer, binding.blockUser);
            }
        });

        Constant.dialogueLayoutSearchChatt(mContext, R.layout.search_layout);
        Dialog search_layout = Constant.dialogLayoutColor;
        TextView search = search_layout.findViewById(R.id.search);
        TextView viewProfile = search_layout.findViewById(R.id.viewProfile);
        TextView clearChat = search_layout.findViewById(R.id.clearChat);
        TextView blockUser = search_layout.findViewById(R.id.blockUser);

        Constant.getSfFuncion(mContext);
        final String receiverUidxc = getIntent().getStringExtra("friendUidKey");
        final String uid = Constant.getSF.getString(Constant.UID_KEY, "");

        if (uid.equals(receiverUidxc)) {
            blockUser.setVisibility(View.GONE);
        } else {
            blockUser.setVisibility(View.VISIBLE);
        }


        binding.menunew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.blockUser.getText().toString().equals("true")) {
                    // Toast.makeText(mContext, "Unblock", Toast.LENGTH_SHORT).show();
                    blockUser.setText("Unblock");
                } else {
                    //Toast.makeText(mContext, "Block", Toast.LENGTH_SHORT).show();
                    blockUser.setText("Block");
                }
                search_layout.show();


                blockUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search_layout.dismiss();
                        if (blockUser.getText().toString().equals("Block")) {
                            Constant.dialogueLayoutForAll(mContext, R.layout.delete_ac_dialogue);
                            TextView TextView = Constant.dialogLayoutColor.findViewById(R.id.TextView);
                            TextView.setText("Block this user.\nBlock it's message.");
                            AppCompatButton cancel = Constant.dialogLayoutColor.findViewById(R.id.cancel);
                            cancel.setVisibility(View.VISIBLE);
                            Constant.dialogLayoutColor.show();
                            Dialog dialog = Constant.dialogLayoutColor;
                            Constant.dialogLayoutColor.setCanceledOnTouchOutside(true);

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Constant.dialogLayoutColor.dismiss();
                                }
                            });
                            AppCompatButton sure = Constant.dialogLayoutColor.findViewById(R.id.sure);
                            sure.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Constant.getSfFuncion(mContext);
                                    String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                                    Webservice.insertBlockUser(mContext, uid, receiverUid, dialog, chattingScreen.this, binding.blockUser, binding.blockContainer, binding.messageboxContainer);
                                }
                            });

                        } else if (blockUser.getText().toString().equals("Unblock")) {
                            Constant.getSfFuncion(mContext);
                            String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                            Webservice.unblockUser(mContext, uid, receiverUid, chattingScreen.this, binding.blockContainer, binding.messageboxContainer, binding.blockUser);

                        }
                    }
                });
            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), userInfoScreen.class);
                intent.putExtra("recUserId", receiverUid);
                SwipeNavigationHelper.startActivityWithSwipe(chattingScreen.this, intent);
                search_layout.dismiss();

            }
        });

        clearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_layout.dismiss();

                Dialog dialogLayoutColor = new Dialog(chattingScreen.this);
                ((android.app.Dialog) dialogLayoutColor).setContentView(R.layout.delete_popup_row);
                dialogLayoutColor.setCanceledOnTouchOutside(true);
                dialogLayoutColor.setCancelable(true);
                dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

                dialogLayoutColor.getWindow().setGravity(Gravity.CENTER);
                WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();

                dialogLayoutColor.getWindow().setAttributes(layoutParams);
                AppCompatButton ok = dialogLayoutColor.findViewById(R.id.ok);
                AppCompatButton cancel = dialogLayoutColor.findViewById(R.id.cancel);
                AppCompatImageView dismiss = dialogLayoutColor.findViewById(R.id.dismiss);

                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogLayoutColor.dismiss();
                    }
                });

                dialogLayoutColor.show();


                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.getReference().child(Constant.CHAT).child(receiverRoom)
                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        chatAdapter.notifyDataSetChanged();


                                        Constant.getSfFuncion(mContext);
                                        String sId = Constant.getSF.getString(Constant.UID_KEY, "");

                                        Webservice.delete_sender_all_msg(mContext, sId, receiverUid, receiverRoom);

                                    }
                                });
                        dialogLayoutColor.dismiss();

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogLayoutColor.dismiss();
                    }
                });


            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.name.setVisibility(View.GONE);
                binding.menu2.setVisibility(View.GONE);
                binding.searchlyt.setVisibility(View.VISIBLE);
                binding.searchEt.setFocusable(true);
                binding.searchEt.requestFocus();

                search_layout.dismiss();
            }
        });


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed();
            }
        });


        binding.messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // binding.messageRecView.scrollToPosition(messageList.size() - 1);

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

        // Add focus change listener to messageBox for keyboard handling
        binding.messageBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("KEYBOARD_SCROLL", "messageBox focus changed: " + hasFocus);
                if (hasFocus) {
                    handleKeyboardScroll();
                }
            }
        });

        // Add click listener to messageBox for keyboard handling (for subsequent clicks)
        binding.messageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("KEYBOARD_SCROLL", "messageBox clicked");
                // Immediate scroll - no delay
                handleKeyboardScroll();
            }
        });

        bottomSheetDialogRec = new BottomSheetDialog(chattingScreen.this, R.style.BottomSheetDialog2);
        View viewShapeRec = getLayoutInflater().inflate(R.layout.bottom_sheet_dialogue_rec, null, false);
        bottomSheetDialogRec.setContentView(viewShapeRec);
        bottomSheetDialogRec.setCanceledOnTouchOutside(false);
        bottomSheetDialogRec.setCancelable(false);
        ImageView cross = viewShapeRec.findViewById(R.id.crossBtm);
        chronometerBtm = viewShapeRec.findViewById(R.id.chronometerBtm);
        sendGrpBtm = viewShapeRec.findViewById(R.id.sendGrpBtm);
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

                    if (binding.limitStatus.getText().toString().equals("0")) {

                        Constant.Vibrator50(mContext);
                        int pixels;


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

                    } else {
                        View parentLayout = binding.getRoot();
                        Snackbar snackbar = Snackbar.make(parentLayout, "Msg limit set for privacy in a day - " + binding.totalMsgLimit.getText().toString(), Snackbar.LENGTH_INDEFINITE);
                        View snackbarView = snackbar.getView();
                        int topMarginInDp = 30;
                        float scale = snackbarView.getResources().getDisplayMetrics().density;
                        int topMarginInPx = (int) (topMarginInDp * scale + 0.5f);
// Change gravity to top and add margin
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                        params.gravity = Gravity.TOP;
                        params.topMargin = topMarginInPx;
                        snackbarView.setLayoutParams(params);
                        snackbar.setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        });

                        try {

                            Constant.getSfFuncion(mContext);
                            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");


                            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

                            try {
                                if (themColor.equals("#ff0080")) {
                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();


                                } else if (themColor.equals("#00A3E9")) {

                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();


                                } else if (themColor.equals("#7adf2a")) {

                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();


                                } else if (themColor.equals("#ec0001")) {

                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();


                                } else if (themColor.equals("#16f3ff")) {

                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();


                                } else if (themColor.equals("#FF8A00")) {

                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();


                                } else if (themColor.equals("#7F7F7F")) {


                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();

                                } else if (themColor.equals("#D9B845")) {

                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();


                                } else if (themColor.equals("#346667")) {

                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();


                                } else if (themColor.equals("#9846D9")) {

                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();


                                } else if (themColor.equals("#A81010")) {

                                    snackbar.setActionTextColor(Color.parseColor(themColor)).show();


                                } else {

                                    snackbar.setActionTextColor(Color.parseColor("#00A3E9")).show();

                                }
                            } catch (Exception ignored) {

                                snackbar.setActionTextColor(Color.parseColor("#00A3E9")).show();
                            }


                        } catch (Exception ignored) {
                        }


                    }
                }


                return true;
            }
        });

        sendGrpBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Constant.Vibrator50(mContext);
                sendAndStopRecording(finalName);
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
        final Dialog block = new Dialog(chattingScreen.this);
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


        final Dialog clearMsg = new Dialog(chattingScreen.this);
        ((android.app.Dialog) clearMsg).setContentView(R.layout.clearmsg_layout);
        clearMsg.setCanceledOnTouchOutside(true);
        clearMsg.setCancelable(true);
        clearMsg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        clearMsg.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        clearMsg.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams2 = block.getWindow().getAttributes();
        clearMsg.getWindow().setAttributes(layoutParams2);

        TextView cancel2 = clearMsg.findViewById(R.id.cancel);


        final Dialog muteNotiDialogue = new Dialog(chattingScreen.this);
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

                // Update isLastItemVisible flag based on scroll position
                if (layoutManager != null && messageList != null && !messageList.isEmpty()) {
                    int totalItems = messageList.size();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

                    // Update isLastItemVisible - user is at bottom if they can see the last message
                    isLastItemVisible = (lastVisiblePosition >= totalItems - 1);

                    Log.d("KEYBOARD_SCROLL", "onScrolled: lastVisiblePosition=" + lastVisiblePosition +
                            ", totalItems=" + totalItems + ", isLastItemVisible=" + isLastItemVisible);
                }
                
                
                String date = null;
                try {
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    date = messageList.get(firstVisibleItemPosition).getCurrentDate();
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

                }// Remove any pending hideDateRunnable to reset the timer
                handler.removeCallbacks(hideDateRunnable);


                if (layoutManager != null && layoutManager.findFirstVisibleItemPosition() == 0 && !isLoading) {

                    if (binding.searchEt.getText().toString().isEmpty() && !binding.searchEt.hasFocus()) {
                        Log.d("tvxasocjijaca", "onScrolled: " + "Visited");
                        loadMore(receiverRoom, receiverUid);
                    } else {
                        Log.d("tvxasocjijaca", "onScrolled: " + "Stucked");
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
                        chatAdapter.setHighQualityLoading(false);
                        break;

                    case RecyclerView.SCROLL_STATE_IDLE:
                        // Scrolling stopped - load high quality
                        chatAdapter.setHighQualityLoading(true);
                        handler.postDelayed(hideDateRunnable, 1500);
                        break;
                }
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
                        // Constant.Vibrator(mContext);
                    }

                    previousText = newText;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        binding.replylyout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.messageRecView.scrollToPosition(Integer.parseInt(binding.listcrntpostion.getText().toString()));
//            }
//        });

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
                if (false) return;

                String searchText = s.toString().toLowerCase().trim();
                // Sync text with bottom search box
                // Removed individual caption logic
                if (!isSyncingText) {
                    isSyncingText = true;
                    binding.emojiSearchBoxBottom.setText(s.toString());
                    isSyncingText = false;
                }
                // Removed individual caption logic
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
                if (false) return;

                String searchText = s.toString().toLowerCase().trim();
                // Sync text with top search box
                // Removed individual caption logic
                if (!isSyncingText) {
                    isSyncingText = true;
                    binding.emojiSearchBox.setText(s.toString());
                    isSyncingText = false;
                }
                // Removed individual caption logic
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


        binding.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), userInfoScreen.class);
                intent.putExtra("recUserId", receiverUid);
                SwipeNavigationHelper.startActivityWithSwipe(chattingScreen.this, intent);
            }
        });


        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), userInfoScreen.class);
                intent.putExtra("recUserId", receiverUid);
                SwipeNavigationHelper.startActivityWithSwipe(chattingScreen.this, intent);
            }
        });

        handleIntent(getIntent());


    }


    private void filteredList(String newText) {
        ArrayList<messageModel> filteredList = new ArrayList<>();

        for (messageModel list : messageList) {
            if (list.getMessage().toLowerCase().contains(newText.toLowerCase())) {

                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {

        } else {

            otherFunctions.searchFilteredData(filteredList, chatAdapter);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Handle GlobalPermissionPopup permission results
        GlobalPermissionPopup.handlePermissionResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && permissions.length == grantResults.length) {
                boolean permissionToRecord = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissionToStore = grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (permissionToRecord && permissionToStore) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
            } else {
                // Handle unexpected lengths
                Toast.makeText(getApplicationContext(), "Unexpected permission result length", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 100) {
            boolean allGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                // Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show();

                // ✅ Load recent gallery images now that permissions are granted
                loadImages();
            } else {
                //Toast.makeText(this, "Some permissions denied. Cannot proceed.", Toast.LENGTH_LONG).show();
            }
        }
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

        ImageAdapter imageAdapter = new ImageAdapter(imagePaths, mContext, null, PICK_IMAGE_REQUEST_CODE, bottomSheetDialog, modelId, database, globalFile, binding.messageRecView, FullImageFile, binding.messageBox, uniqueDates, messageList, userFTokenKey, isLastItemVisible, chatAdapter, (LinearLayout) binding.gallary, binding.messageBox, (LinearLayout) binding.emoji, binding.send, binding.multiSelectSmallCounterText);
        binding.dataRecview.setAdapter(imageAdapter);

        // Enable multi-selection mode for images
        imageAdapter.setMultiSelectionMode(true);
        imageAdapter.setOnImageSelectionListener(new ImageAdapter.OnImageSelectionListener() {
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
     * Force bottom layout to be visible - used during multi-selection
     */
    private void ensureBottomLayoutVisible() {
        try {
            if (binding.bottom != null) {
                binding.bottom.setVisibility(View.VISIBLE);
                binding.bottom.bringToFront();
                Log.d("MultiSelect", "Force ensuring bottom layout is visible");
            }
        } catch (Exception e) {
            Log.e("MultiSelect", "Error ensuring bottom layout visibility: " + e.getMessage());
        }
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
    private void setupMultiSelectionSendButton(ImageAdapter imageAdapter) {
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


    public boolean CheckPermissions() {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onBackPressed() {
        Log.d("DIALOGUE_DEBUG", "=== onBackPressed() CALLED ===");
        Log.d("DIALOGUE_DEBUG", "Current time: " + System.currentTimeMillis());
        Log.d("DIALOGUE_DEBUG", "Constant.dialogLayoutFullScreen is null: " + (Constant.dialogLayoutFullScreen == null));

        if (Constant.dialogLayoutFullScreen != null) {
            Log.d("DIALOGUE_DEBUG", "Constant.dialogLayoutFullScreen is not null, checking if showing...");
            Log.d("DIALOGUE_DEBUG", "Constant.dialogLayoutFullScreen.isShowing(): " + Constant.dialogLayoutFullScreen.isShowing());
        }

        // Check if multi-image preview dialogue is showing
        if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
            Log.d("DIALOGUE_DEBUG", "=== NAVBAR BACK BUTTON - DIALOGUE DETECTED ===");
            Log.d("DIALOGUE_DEBUG", "Device back button pressed while dialogue is showing");

            // Reset UI state immediately when device back button is pressed
            Log.d("DIALOGUE_DEBUG", "=== NAVBAR BACK UI RESET START ===");
            Log.d("DIALOGUE_DEBUG", "Calling loadImages() from navbar back");
            loadImages();
            Log.d("DIALOGUE_DEBUG", "Setting emoji visibility to VISIBLE from navbar back");
            binding.emoji.setVisibility(View.VISIBLE);
            Log.d("DIALOGUE_DEBUG", "Setting message hint to 'Message on Ec' from navbar back");
            binding.messageBox.setHint("Message on Ec");
            Log.d("DIALOGUE_DEBUG", "Setting send button image to mike from navbar back");
            binding.send.setImageResource(R.drawable.mike);
            Log.d("DIALOGUE_DEBUG", "Hiding multiSelectSmallCounterText from navbar back");
            binding.multiSelectSmallCounterText.setVisibility(View.GONE);
            Log.d("DIALOGUE_DEBUG", "=== NAVBAR BACK UI RESET COMPLETED ===");

            // Dismiss the dialogue
            try {
                Log.d("DIALOGUE_DEBUG", "Dismissing dialogue from navbar back");
                Constant.dialogLayoutFullScreen.dismiss();
                Log.d("DIALOGUE_DEBUG", "Dialogue dismissed successfully from navbar back");
            } catch (Exception e) {
                Log.e("DIALOGUE_DEBUG", "Error dismissing dialogue from navbar back: " + e.getMessage());
            }

            Log.d("DIALOGUE_DEBUG", "=== NAVBAR BACK BUTTON - DIALOGUE HANDLED ===");
            return; // Don't proceed with normal back press logic
        } else {
            Log.d("DIALOGUE_DEBUG", "=== NAVBAR BACK BUTTON - NO DIALOGUE DETECTED ===");
            Log.d("DIALOGUE_DEBUG", "Proceeding with normal back press logic");
        }

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


        Log.d("ChattingScreen", "onBackPressed() called");

        // Check if camera fragment is currently visible
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.isVisible()) {
            Log.d("ChattingScreen", "onBackPressed() - camera fragment is visible, letting fragment handle back press");
            // Let the fragment handle the back press (it will call closeWithAnimation())
            super.onBackPressed();
            return;
        }

        if (binding.header2Cardview.getVisibility() == View.VISIBLE) {
            Log.d("ChattingScreen", "onBackPressed() - header2Cardview is visible, hiding it");
            //  Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
            binding.header2Cardview.setVisibility(View.GONE);
            binding.cross.performClick();
            binding.header1Cardview.setVisibility(View.VISIBLE);
            Log.d("ChattingScreen", "onBackPressed() - header2Cardview hidden, returning");
            return;
        }


        String fromInviteKey = getIntent().getStringExtra("fromInviteKey");
        Log.d("ChattingScreen", "onBackPressed() - fromInviteKey: " + fromInviteKey);
        if (fromInviteKey != null) {
            Log.d("ChattingScreen", "onBackPressed() - handling fromInviteKey case, navigating to MainActivityOld");
            SwipeNavigationHelper.startActivityWithBackSwipe(chattingScreen.this, new Intent(mContext, MainActivityOld.class), true);
            final String receiverUid = getIntent().getStringExtra("friendUidKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String senderRoom = senderId + receiverUid;
            final String receiverRoom = receiverUid + senderId;
            Log.d("senderRoom", senderRoom + receiverRoom);
            Log.d("ChattingScreen", "onBackPressed() - calling get_individual_chatting for fromInviteKey case");

            Webservice.get_individual_chatting(mContext, senderId, receiverUid, binding.limitStatus, binding.totalMsgLimit);

        } else {
            String ecKey = getIntent().getStringExtra("ecKey");
            Log.d("ChattingScreen", "onBackPressed() - ecKey: " + ecKey);
            if (binding.searchEt.isFocused()) {
                Log.d("ChattingScreen", "onBackPressed() - searchEt is focused, clearing search and finishing");
                binding.searchEt.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(binding.searchEt.getWindowToken(), 0);
                }
                binding.name.setVisibility(View.VISIBLE);
                binding.searchlyt.setVisibility(View.GONE);
                SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);
                return;
            } else {
                if (binding.emojiRecyclerview.getVisibility() == View.VISIBLE) {
                    Log.d("ChattingScreen", "onBackPressed() - emojiRecyclerview is visible, hiding emoji panel");
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
                    binding.bitemoji.setImageResource(R.drawable.emojisvg);
                    Log.d("ChattingScreen", "onBackPressed() - emoji panel hidden");
                    SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);
                    return;
                } else if (binding.galleryRecentLyt.getVisibility() == View.VISIBLE) {
                    Log.d("ChattingScreen", "onBackPressed() - gallery recent view is showing, hiding it");
                    hideGalleryRecentView();
                    SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);
                    return;

                } else {
                    Log.d("ChattingScreen", "onBackPressed() - entering main navigation logic");
                    if (ecKey != null) {
                        Log.d("ChattingScreen", "onBackPressed() - ecKey is not null: " + ecKey);
                        if (ecKey.equals("ecKey")) {
                            Log.d("ChattingScreen", "onBackPressed() - ecKey equals 'ecKey', handling navigation");

                            Constant.getSfFuncion(getApplicationContext());
                            final String receiverUid = getIntent().getStringExtra("friendUidKey");
                            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                            final String senderRoom = senderId + receiverUid;
                            final String receiverRoom = receiverUid + senderId;
                            Log.d("senderRoom", senderRoom + receiverRoom);
                            Log.d("ChattingScreen", "onBackPressed() - calling get_individual_chatting for ecKey case");

                            Webservice.get_individual_chatting(mContext, senderId, receiverUid, binding.limitStatus, binding.totalMsgLimit);

                            if (FROM_NOTI_KEY != null) {
                                Log.d("ChattingScreen", "onBackPressed() - FROM_NOTI_KEY: " + FROM_NOTI_KEY);
                                if (FROM_NOTI_KEY.equals("FROM_NOTI_KEY")) {
                                    Log.d("ChattingScreen", "onBackPressed() - FROM_NOTI_KEY equals 'FROM_NOTI_KEY', finishing with slide to right");
                                    SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);

                                } else {
                                    Log.d("ChattingScreen", "onBackPressed() - FROM_NOTI_KEY not equals 'FROM_NOTI_KEY', setting callOnce and finishing");
                                    Constant.setSfFunction(mContext);
                                    Constant.setSF.putString("callOnce", "callOnce");
                                    Constant.setSF.apply();
                                    SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);
                                }
                            } else {
                                Log.d("ChattingScreen", "onBackPressed() - FROM_NOTI_KEY is null, setting callOnce and finishing");
                                Constant.setSfFunction(mContext);
                                Constant.setSF.putString("callOnce", "callOnce");
                                Constant.setSF.apply();
                                SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);
                            }

                        } else {
                            Log.d("ChattingScreen", "onBackPressed() - ecKey not equals 'ecKey', handling alternative navigation");

                            Constant.getSfFuncion(getApplicationContext());
                            final String receiverUid = getIntent().getStringExtra("friendUidKey");
                            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                            final String senderRoom = senderId + receiverUid;
                            final String receiverRoom = receiverUid + senderId;
                            Log.d("senderRoom", senderRoom + receiverRoom);
                            Log.d("ChattingScreen", "onBackPressed() - calling get_individual_chatting for alternative ecKey case");

                            Webservice.get_individual_chatting(mContext, senderId, receiverUid, binding.limitStatus, binding.totalMsgLimit);

                            if (FROM_NOTI_KEY != null) {
                                Log.d("ChattingScreen", "onBackPressed() - alternative case FROM_NOTI_KEY: " + FROM_NOTI_KEY);
                                if (FROM_NOTI_KEY.equals("FROM_NOTI_KEY")) {
                                    Log.d("ChattingScreen", "onBackPressed() - alternative case FROM_NOTI_KEY equals 'FROM_NOTI_KEY', finishing with slide to right");
                                    SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);
                                } else {
                                    Log.d("ChattingScreen", "onBackPressed() - alternative case FROM_NOTI_KEY not equals 'FROM_NOTI_KEY', setting callOnce and finishing");
                                    Constant.setSfFunction(mContext);
                                    Constant.setSF.putString("callOnce", "callOnce");
                                    Constant.setSF.apply();
                                    SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);
                                }
                            } else {
                                Log.d("ChattingScreen", "onBackPressed() - alternative case FROM_NOTI_KEY is null, setting callOnce and finishing");
                                Constant.setSfFunction(mContext);
                                Constant.setSF.putString("callOnce", "callOnce");
                                Constant.setSF.apply();
                                SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);

                            }

                            // TransitionHelper.performTransition(((Activity)mContext));
                        }
                    } else {
                        Log.d("ChattingScreen", "onBackPressed() - ecKey is null, handling default navigation");

                        Constant.getSfFuncion(getApplicationContext());
                        final String receiverUid = getIntent().getStringExtra("friendUidKey");
                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                        final String senderRoom = senderId + receiverUid;
                        final String receiverRoom = receiverUid + senderId;
                        Log.d("senderRoom", senderRoom + receiverRoom);
                        Log.d("ChattingScreen", "onBackPressed() - calling get_individual_chatting for default case");

                        Webservice.get_individual_chatting(mContext, senderId, receiverUid, binding.limitStatus, binding.totalMsgLimit);


                        if (FROM_NOTI_KEY != null) {
                            Log.d("ChattingScreen", "onBackPressed() - default case FROM_NOTI_KEY: " + FROM_NOTI_KEY);
                            if (FROM_NOTI_KEY.equals("FROM_NOTI_KEY")) {
                                Log.d("ChattingScreen", "onBackPressed() - default case FROM_NOTI_KEY equals 'FROM_NOTI_KEY', finishing with slide to right");
                                SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);
                            } else {
                                Log.d("ChattingScreen", "onBackPressed() - default case FROM_NOTI_KEY not equals 'FROM_NOTI_KEY', setting callOnce and finishing");
                                Constant.setSfFunction(mContext);
                                Constant.setSF.putString("callOnce", "callOnce");
                                Constant.setSF.apply();
                                SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);
                            }
                        } else {
                            Log.d("ChattingScreen", "onBackPressed() - default case FROM_NOTI_KEY is null, setting callOnce and finishing");
                            Constant.setSfFunction(mContext);
                            Constant.setSF.putString("callOnce", "callOnce");
                            Constant.setSF.apply();
                            SwipeNavigationHelper.finishWithSwipe(chattingScreen.this);
                        }

                        //  TransitionHelper.performTransition(((Activity)mContext));
                    }

                    if (playerPreview != null) {
                        Log.d("ChattingScreen", "onBackPressed() - cleaning up playerPreview");
                        playerPreview.stop();
                        playerPreview.release();
                    }
                }
            }

        }
        Log.d("ChattingScreen", "onBackPressed() - method completed");
    }

    @Override
    protected void onPause() {
        super.onPause();

        isChatScreenActive = false;
        isChatScreenActiveUid = "";
        Log.d("XYZ", "onPause: ");
        //


        if (playerPreview != null) {
            playerPreview.stop();
            playerPreview.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("XYZ", "onStop: ");
        try {
            //
        } catch (Exception e) {

        }


        Constant.getSfFuncion(getApplicationContext());
        final String receiverUid = getIntent().getStringExtra("friendUidKey");
        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
        final String senderRoom = senderId + receiverUid;
        final String receiverRoom = receiverUid + senderId;
        Log.d("senderRoom", senderRoom + receiverRoom);

        Webservice.get_individual_chatting(mContext, senderId, receiverUid, binding.limitStatus, binding.totalMsgLimit);


        if (playerPreview != null) {
            playerPreview.stop();
            playerPreview.release();
        }


        try {
            if (messageList != null) {
                messageModel model5 = messageList.get(chatAdapter.getItemCount() - 1);

                if (model5 != null) {
                    Log.d("TAG", "messageModel: " + model5.getUid());
                    Log.d("TAG", "messageModel: " + model5.getNotification());
                    Log.d("TAG", "onScrolled: " + "bottom_scroll_flag");


//                    if (!model5.getUid().equals(Constant.getSF.getString(Constant.UID_KEY, ""))) {
//
//                        database.getReference().child(Constant.CHAT).child(receiverRoom).child(model5.getModelId()).child("notification").setValue(Integer.parseInt(binding.countDownTxt.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Log.d("TAG", "onFailure: " + e.getMessage());
//                            }
//                        });
//
//                    }
                }
            }
        } catch (Exception ignored) {
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Constant.setSfFunction(mContext);
        Constant.setSF.putString("callOnce", "");
        Constant.setSF.apply();

        try {
            if (mContext != null && connectivityReceiver != null) {
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


        Log.d("XYZ", "onDestroy: ");


        Constant.getSfFuncion(getApplicationContext());
        final String receiverUid = getIntent().getStringExtra("friendUidKey");
        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
        final String senderRoom = senderId + receiverUid;
        final String receiverRoom = receiverUid + senderId;
        Log.d("senderRoom", senderRoom + receiverRoom);

        Webservice.get_individual_chatting(mContext, senderId, receiverUid, binding.limitStatus, binding.totalMsgLimit);

        if (playerPreview != null) {
            playerPreview.stop();
            playerPreview.release();
        }


    }

    private void handleMultipleImageSelection() {
        Log.d("ImageUpload", "=== handleMultipleImageSelection START ===");
        Log.d("ImageUpload", "SelectedImageUris size: " + selectedImageUris.size());
        Log.d("ImageUpload", "SelectedImageFiles size: " + selectedImageFiles.size());

        if (selectedImageUris.isEmpty()) {
            Log.w("ImageUpload", "SelectedImageUris is empty, returning");
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

        // Build selectionBunch for all selected images
        ArrayList<selectionBunchModel> selectionBunch = new ArrayList<>();

        // Check for duplicate URIs
        Set<String> uriSet = new HashSet<>();
        for (int i = 0; i < selectedImageUris.size(); i++) {
            String uriString = selectedImageUris.get(i).toString();
            if (uriSet.contains(uriString)) {
                Log.w("bunch###", "Duplicate URI detected at index " + i + ": " + uriString);
            } else {
                uriSet.add(uriString);
            }
        }
        Log.d("bunch###", "Total unique URIs: " + uriSet.size() + " out of " + selectedImageUris.size());

        for (int i = 0; i < selectedImageUris.size(); i++) {
            Uri imageUri = selectedImageUris.get(i);
            String fileName = getFileNameFromUri(imageUri);
            if (fileName == null || fileName.isEmpty()) {
                if (i < selectedImageFiles.size() && selectedImageFiles.get(i) != null) {
                    fileName = selectedImageFiles.get(i).getName();
                } else if (i < selectedFullImageFiles.size() && selectedFullImageFiles.get(i) != null) {
                    fileName = selectedFullImageFiles.get(i).getName();
                } else {
                    fileName = "image_" + System.currentTimeMillis() + "_" + i + ".jpg";
                }
            }

            // Decode URL-encoded characters in filename
            try {
                String decodedFileName = java.net.URLDecoder.decode(fileName, "UTF-8");
                Log.d("bunch###", "Decoded filename for bunch: " + fileName + " -> " + decodedFileName);
                fileName = decodedFileName;
            } catch (Exception e) {
                Log.w("bunch###", "Failed to decode filename for bunch: " + fileName, e);
            }

            // Remove any subdirectory prefixes like "chats/" from the filename
            if (fileName.contains("/")) {
                String originalFileName = fileName;
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                Log.d("bunch###", "Removed subdirectory prefix for bunch: " + originalFileName + " -> " + fileName);
            }

            // Add debugging to check file paths
            String filePath = "";
            if (i < selectedImageFiles.size() && selectedImageFiles.get(i) != null) {
                filePath = selectedImageFiles.get(i).getAbsolutePath();
            }
            String fullFilePath = "";
            if (i < selectedFullImageFiles.size() && selectedFullImageFiles.get(i) != null) {
                fullFilePath = selectedFullImageFiles.get(i).getAbsolutePath();
            }

            Log.d("bunch###", "Image " + (i + 1) + " - URI: " + imageUri + ", FileName: " + fileName);
            Log.d("bunch###", "Image " + (i + 1) + " - Compressed Path: " + filePath);
            Log.d("bunch###", "Image " + (i + 1) + " - Full Path: " + fullFilePath);

            // Check file sizes to ensure they're different
            if (i < selectedImageFiles.size() && selectedImageFiles.get(i) != null) {
                File compressedFile = selectedImageFiles.get(i);
                Log.d("bunch###", "Image " + (i + 1) + " - Compressed file size: " + compressedFile.length() + " bytes");
            }
            if (i < selectedFullImageFiles.size() && selectedFullImageFiles.get(i) != null) {
                File fullFile = selectedFullImageFiles.get(i);
                Log.d("bunch###", "Image " + (i + 1) + " - Full file size: " + fullFile.length() + " bytes");
            }

            selectionBunch.add(new selectionBunchModel("", fileName));
            Log.d("SelectionBunchMulti", "Added selectionBunch item " + fileName);
            Log.d("bunch###", "Added bunch item " + (i + 1) + "/" + selectedImageUris.size() + ": " + fileName);
        }

        Log.d("bunch###", "Selection bunch built successfully with " + selectionBunch.size() + " items");

        // Debug: Log each selectionBunch item
        for (int i = 0; i < selectionBunch.size(); i++) {
            selectionBunchModel item = selectionBunch.get(i);
            Log.d("bunch###", "SelectionBunch item " + (i + 1) + ": fileName='" + item.getFileName() + "', imgUrl='" + item.getImgUrl() + "'");
        }

        // Store selectionBunch for later use
        this.selectionBunch = selectionBunch;

        // Show multi-image preview dialog
        showMultiImagePreviewDialog();
    }

    private void showMultiImagePreviewDialog() {
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

        // Setup image counter visibility
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setVisibility(View.VISIBLE);
            imageCounter.setText("1 / " + selectedImageUris.size());
        }

        // Setup multi-image preview
        setupMultiImagePreview();
    }

    private void setupMultiImagePreview() {
        Log.d("MultiImagePreview", "Setting up preview with " + selectedImageUris.size() + " images");

        if (selectedImageUris.isEmpty()) {
            Log.d("MultiImagePreview", "No images selected, returning");
            return;
        }

        // Hide the single image view and show the horizontal gallery preview
        ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        singleImageView.setVisibility(View.GONE);

        // Hide ViewPager2 and show horizontal gallery
        ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
        if (viewPager2 != null) {
            viewPager2.setVisibility(View.GONE);
        }

        // Setup horizontal gallery preview
        setupHorizontalGalleryPreview();
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
        LinearLayout backArrow = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
        if (backArrow != null) {
            backArrow.setOnClickListener(v -> {
                Log.d("DIALOGUE_DEBUG", "Back arrow clicked, dismissing dialogue and calling onBackPressed");

                // Reset UI state immediately when back arrow is clicked
                Log.d("DIALOGUE_DEBUG", "=== BACK ARROW UI RESET START ===");
                Log.d("DIALOGUE_DEBUG", "Calling loadImages() from back arrow");

                Log.d("DIALOGUE_DEBUG", "=== BACK ARROW UI RESET COMPLETED ===");

                try {
                    if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                        Log.d("DIALOGUE_DEBUG", "Dismissing dialogue from back arrow");
                        Constant.dialogLayoutFullScreen.dismiss();
                        Log.d("DIALOGUE_DEBUG", "Dialogue dismissed successfully from back arrow");
                    }
                } catch (Exception e) {
                    Log.e("DIALOGUE_DEBUG", "Error dismissing dialogue from back arrow: " + e.getMessage());
                }
                onBackPressed();

            });
        }
    }

    private void setupHorizontalGalleryPreview() {
        Log.d("HorizontalGallery", "Setting up horizontal gallery with " + selectedImageUris.size() + " images");


        // Setup main image preview ViewPager2
        ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        if (mainImagePreview != null) {
            mainImagePreview.setVisibility(View.VISIBLE);

            // Setup adapter for main preview
            MainImagePreviewAdapter mainAdapter = new MainImagePreviewAdapter(mContext, selectedImageUris);
            mainImagePreview.setAdapter(mainAdapter);

            // Setup page change callback to sync with horizontal RecyclerView
            mainImagePreview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    // Update counter
                    TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
                    if (imageCounter != null) {
                        imageCounter.setText((position + 1) + " / " + selectedImageUris.size());
                        imageCounter.setVisibility(View.VISIBLE); // Ensure it's always visible
                    }

                    // Update caption EditText with current caption
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        messageBoxMy.setText(currentCaption);
                        messageBoxMy.setSelection(messageBoxMy.getText().length());
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
            Log.d("ImageCounter", "setupHorizontalGalleryPreview - Setting imageCounter to VISIBLE with text: 1 / " + selectedImageUris.size());
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
            sendGrp.setOnClickListener(v -> {

                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");

                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);
                Log.d("SendButton", "Send button clicked for images!");
                Log.d("ImageUpload", "=== CALLING sendMultipleImages from SendButton ===");
                Log.d("ImageUpload", "Current caption: '" + currentCaption + "'");
                binding.messageBox.setText("");
                sendMultipleImages(currentCaption);



                // Note: clearing and dialog dismissal now handled inside sendMultipleImages()
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

    private void setupHorizontalGalleryPreviewWithData() {
        Log.d("HorizontalGalleryWithData", "Setting up horizontal gallery with " + selectedImageUris.size() + " images");

        // Setup main image preview ViewPager2
        ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        if (mainImagePreview != null) {
            mainImagePreview.setVisibility(View.VISIBLE);

            // Setup adapter for main preview
            MainImagePreviewAdapter mainAdapter = new MainImagePreviewAdapter(mContext, selectedImageUris);
            mainImagePreview.setAdapter(mainAdapter);

            // Setup page change callback to sync with horizontal RecyclerView
            mainImagePreview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    // Update counter
                    TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
                    if (imageCounter != null) {
                        imageCounter.setText((position + 1) + " / " + selectedImageUris.size());
                        imageCounter.setVisibility(View.VISIBLE);
                    }

                    // Update caption EditText with current caption
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        String currentCaption = binding.messageBox.getText().toString().trim();
                        messageBoxMy.setText(currentCaption);
                        messageBoxMy.setSelection(messageBoxMy.getText().length());
                    }
                }
            });

            // Setup TextWatcher for caption input
            EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
            if (messageBoxMy != null) {
                // Set initial caption from messageBox
                String initialCaption = binding.messageBox.getText().toString().trim();
                messageBoxMy.setText(initialCaption);
                messageBoxMy.setSelection(messageBoxMy.getText().length());

                messageBoxMy.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Update the main messageBox as user types
                        binding.messageBox.setText(s.toString());
                        Log.d("CaptionWatcher", "Caption updated in messageBox: " + s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        }

        // Setup horizontal RecyclerView for thumbnails (using emojiRecyclerview as it exists in layout)
        RecyclerView horizontalRecyclerView = Constant.dialogLayoutFullScreen.findViewById(R.id.emojiRecyclerview);
        if (horizontalRecyclerView != null) {
            horizontalRecyclerView.setVisibility(View.VISIBLE);

            // Setup horizontal adapter with correct constructor
            HorizontalImageAdapter horizontalAdapter = new HorizontalImageAdapter(mContext, selectedImageUris);
            horizontalRecyclerView.setAdapter(horizontalAdapter);

            // Setup layout manager
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            horizontalRecyclerView.setLayoutManager(layoutManager);

            // Setup click listener for horizontal items
            horizontalAdapter.setOnImageClickListener((position, imageUri) -> {
                if (mainImagePreview != null) {
                    mainImagePreview.setCurrentItem(position, true);
                }
            });
        }

        // Setup image counter
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setText("1 / " + selectedImageUris.size());
            imageCounter.setVisibility(View.VISIBLE);
            Log.d("ImageCounter", "setupHorizontalGalleryPreviewWithData - Setting imageCounter to VISIBLE with text: 1 / " + selectedImageUris.size());
        }
    }

    private void setupMultiImageSendButton() {
        // Setup send button
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        if (sendGrp != null) {
            sendGrp.setOnClickListener(v -> {
                Log.d("DIALOGUE_DEBUG", "=== SEND BUTTON CLICKED ===");
                Log.d("DIALOGUE_DEBUG", "Send button clicked for multi-images!");
                Log.d("DIALOGUE_DEBUG", "SelectedImageUris size: " + selectedImageUris.size());
                Log.d("DIALOGUE_DEBUG", "SelectedImageFiles size: " + selectedImageFiles.size());
                Log.d("DIALOGUE_DEBUG", "DialogLayoutFullScreen is null: " + (Constant.dialogLayoutFullScreen == null));
                Log.d("DIALOGUE_DEBUG", "DialogLayoutFullScreen is showing: " + (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()));

                // Reset UI state when send button is clicked in dialogue

                loadImages();
                binding.emoji.setVisibility(View.VISIBLE);
                binding.messageBox.setHint("Message on Ec");
                binding.send.setImageResource(R.drawable.mike);
                binding.multiSelectSmallCounterText.setVisibility(View.GONE);

                // Check if images are ready to send
                if (selectedImageUris.isEmpty()) {
                    Log.w("DIALOGUE_DEBUG", "No images selected, cannot send");
                    Toast.makeText(mContext, "No images selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If images haven't been processed yet, process them now
                if (selectedImageFiles.isEmpty()) {
                    Log.d("DIALOGUE_DEBUG", "Images not processed yet, processing now...");
                    handleMultipleImageSelection();

                    // Wait a bit for processing to complete
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                        Log.d("DIALOGUE_DEBUG", "Processing delay completed, checking files...");
                        if (selectedImageFiles.isEmpty()) {
                            Log.e("DIALOGUE_DEBUG", "Failed to process images");
                            Toast.makeText(mContext, "Failed to process images, please try again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("DIALOGUE_DEBUG", "Images processed successfully, calling proceedWithSending");
                        proceedWithSending();
                    }, 1000);
                } else {
                    Log.d("DIALOGUE_DEBUG", "Images already processed, calling proceedWithSending directly");
                    proceedWithSending();
                }
            });
        }

        // Setup back button
        LinearLayout backButton = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                // Clear selection when dialog is dismissed
                selectedImageUris.clear();
                selectedImageFiles.clear();
                selectedFullImageFiles.clear();
                selectionBunch.clear();
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }
    }

    private void proceedWithSending() {
        Log.d("DIALOGUE_DEBUG", "=== proceedWithSending START ===");
        Log.d("DIALOGUE_DEBUG", "DialogLayoutFullScreen is null: " + (Constant.dialogLayoutFullScreen == null));
        Log.d("DIALOGUE_DEBUG", "DialogLayoutFullScreen is showing: " + (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()));

        // Get caption from the dialog's messageBox
        EditText messageBoxMy = Constant.dialogLayoutFullScreen != null ? Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy) : null;
        Log.d("DIALOGUE_DEBUG", "messageBoxMy found: " + (messageBoxMy != null));

        final String caption;
        if (messageBoxMy != null) {
            caption = messageBoxMy.getText().toString().trim();
            Log.d("DIALOGUE_DEBUG", "Caption from messageBoxMy: '" + caption + "'");
        } else {
            caption = "";
            Log.d("DIALOGUE_DEBUG", "messageBoxMy is null, using empty caption");
        }

        Log.d("DIALOGUE_DEBUG", "Final caption: '" + caption + "'");
        Log.d("DIALOGUE_DEBUG", "SelectedImageUris: " + selectedImageUris.size() + ", SelectedImageFiles: " + selectedImageFiles.size());

        // Clear the main messageBox
        binding.messageBox.setText("");

        binding.emoji.setVisibility(View.VISIBLE);
        binding.messageBox.setHint("Message on Ec");

        binding.send.setImageResource(R.drawable.mike);
        binding.multiSelectSmallCounterText.setVisibility(View.GONE);

        Log.d("DIALOGUE_DEBUG", "Cleared main messageBox");

        // Send multiple images
        Log.d("DIALOGUE_DEBUG", "Starting send process with caption: '" + caption + "'");
        sendMultipleImages(caption);

        // Dismiss dialogue immediately after starting send process
        Log.d("DIALOGUE_DEBUG", "Dismissing dialogue immediately after starting send process");
        if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
            Log.d("DIALOGUE_DEBUG", "Dismissing dialogue in proceedWithSending");
            try {
                Constant.dialogLayoutFullScreen.dismiss();
                Log.d("DIALOGUE_DEBUG", "Dialogue dismissed successfully in proceedWithSending");
            } catch (Exception e) {
                Log.e("DIALOGUE_DEBUG", "Error dismissing dialogue in proceedWithSending: " + e.getMessage());
            }

            // Set a flag to prevent immediate recreation
            Constant.dialogLayoutFullScreen = null;
            Log.d("DIALOGUE_DEBUG", "Set dialogLayoutFullScreen to null to prevent recreation");
        } else {
            Log.d("DIALOGUE_DEBUG", "Dialogue not showing or null in proceedWithSending");
        }

        Log.d("DIALOGUE_DEBUG", "=== proceedWithSending END ===");
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
                File f;

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

        // No caption initialization needed

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
        Log.d("VIDEO_DIALOG_SHOW", "chattingScreen.showMultiVideoPreviewDialog called");
        // Create and show video preview dialog
        Constant.dialogLayoutFullScreen = new Dialog(chattingScreen.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ((android.app.Dialog) Constant.dialogLayoutFullScreen).setContentView(R.layout.dialogue_video_preview);
        Log.d("VIDEO_DIALOG_SHOW", "About to show multi-video dialog");

        // Make dialog full screen
        Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        Constant.dialogLayoutFullScreen.show();

        // Add dismiss listener to destroy videos when dialog is dismissed
        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
            Log.d("VIDEO_DIALOG", "Video dialog dismissed - cleaning up videos");
            // Destroy all videos when dialog is dismissed
            if (mainVideoAdapter != null) {
                Log.d("VIDEO_DIALOG", "Destroying all videos via adapter");
                try {
                    mainVideoAdapter.destroyAllVideos();
                } catch (Exception e) {
                    Log.e("VIDEO_DIALOG", "Error destroying videos: " + e.getMessage());
                }
            } else {
                Log.w("VIDEO_DIALOG", "MainVideoAdapter is null - cannot destroy videos");
            }
            // Clear video selections
            selectedVideoUris.clear();
            selectedVideoFiles.clear();
            Log.d("VIDEO_DIALOG", "Cleared video selections");
        });

        // Setup video counter visibility
        TextView videoCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.videoCounter);
        if (videoCounter != null) {
            videoCounter.setVisibility(View.VISIBLE);
            videoCounter.setText("1 / " + selectedVideoUris.size());
        }

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

            // Store adapter reference for video control
            this.mainVideoAdapter = mainAdapter;

            // Configure ViewPager to prevent aggressive recycling
            mainVideoPreview.setOffscreenPageLimit(2); // Keep 2 pages in memory
            mainVideoPreview.setUserInputEnabled(true);

            Log.d("VIDEO_VIEWPAGER", "ViewPager configured with " + selectedVideoUris.size() + " videos, offscreen limit: 2");

            // Setup page change callback to sync with horizontal RecyclerView
            mainVideoPreview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    // Stop any currently playing video when switching pages
                    Log.d("VIDEO_PAGE", "Page changed to position " + position + " - pausing current video");
                    if (mainAdapter != null) {
                        Log.d("VIDEO_PAGE", "Pausing current video via adapter");
                        mainAdapter.pauseCurrentVideo();
                    } else {
                        Log.w("VIDEO_PAGE", "MainAdapter is null - cannot pause video");
                    }

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
                        // Use class variable for caption persistence
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


        // Setup send button
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        if (sendGrp != null) {
            sendGrp.setOnClickListener(v -> {
                Log.d("SendButton", "Send button clicked for videos!");
                Log.d("SendButton", "Current caption: '" + currentCaption + "'");
                sendMultipleVideos(currentCaption);

                // Clear selection after sending
                selectedVideoUris.clear();
                selectedVideoFiles.clear();

                // Destroy all videos before dismissing dialog
                Log.d("VIDEO_SEND", "Send button clicked - destroying videos before sending");
                if (mainVideoAdapter != null) {
                    Log.d("VIDEO_SEND", "Destroying all videos via adapter");
                    try {
                        mainVideoAdapter.destroyAllVideos();
                    } catch (Exception e) {
                        Log.e("VIDEO_SEND", "Error destroying videos: " + e.getMessage());
                    }
                } else {
                    Log.w("VIDEO_SEND", "MainVideoAdapter is null - cannot destroy videos");
                }

                // Dismiss dialog after sending
                if (Constant.dialogLayoutFullScreen != null) {
                    Constant.dialogLayoutFullScreen.dismiss();
                    Log.d("VIDEO_SEND", "Dialog dismissed after sending videos");
                }
            });
        }

        // Setup back button
        LinearLayout backButton = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                // Destroy all videos before dismissing dialog
                Log.d("VIDEO_BACK", "Back button clicked - destroying videos before dismissing");
                if (mainVideoAdapter != null) {
                    Log.d("VIDEO_BACK", "Destroying all videos via adapter");
                    try {
                        mainVideoAdapter.destroyAllVideos();
                    } catch (Exception e) {
                        Log.e("VIDEO_BACK", "Error destroying videos: " + e.getMessage());
                    }
                } else {
                    Log.w("VIDEO_BACK", "MainVideoAdapter is null - cannot destroy videos");
                }

                // Clear selection when dialog is dismissed
                selectedVideoUris.clear();
                selectedVideoFiles.clear();
                Log.d("VIDEO_BACK", "Cleared video selections");
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
        final String receiverUid = getIntent().getStringExtra("friendUidKey");
        final String senderRoom = senderId + receiverUid;
        final String receiverRoom = receiverUid + senderId;

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

            messageModel model = new messageModel(
                    senderId,
                    "",
                    currentDateTimeString,
                    localVideoFile.getAbsolutePath(),
                    Constant.video,
                    "", "", "", "", "",
                    Constant.getSF.getString(Constant.full_name, ""),
                    "", "", "", "", "",
                    videoModelId,
                    receiverUid,
                    "", "", "",
                    localVideoFile.getName(),
                    localThumbnailFile != null ? localThumbnailFile.getAbsolutePath() : "",
                    fileThumbName,
                    individualCaption,
                    1,
                    uniqueDates.add(uniqDate) ? uniqDate : ":" + uniqDate,
                    emojiModels,
                    "",
                    Constant.getCurrentTimestamp(),
                    videoWidthDp,
                    videoHeightDp,
                    aspectRatio,
                    "1"
            );

            // Store message in SQLite pending table before upload
            try {
                new DatabaseHelper(mContext).insertPendingMessage(model);
                Log.d("PendingMessages", "Video message stored in pending table: " + model.getModelId());
            } catch (Exception e) {
                Log.e("PendingMessages", "Error storing video message in pending table: " + e.getMessage(), e);
            }

            messageList.add(model);

            // Create messagemodel2
            Log.d("VideoMessageModel2Creation", "Creating messagemodel2 for video " + i + " with caption from model: '" + model.getCaption() + "'");
            messagemodel2 model2 = new messagemodel2(
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
                    model.getUserName(),
                    model.getReplytextData(),
                    model.getReplyKey(),
                    model.getReplyType(),
                    model.getReplyOldData(),
                    model.getReplyCrtPostion(),
                    model.getModelId(),
                    model.getReceiverUid(),
                    model.getForwaredKey(),
                    model.getGroupName(),
                    model.getDocSize(),
                    model.getFileName(),
                    model.getThumbnail(),
                    model.getFileNameThumbnail(),
                    model.getCaption(),
                    model.getNotification(),
                    model.getCurrentDate(),
                    model.getEmojiModel(),
                    model.getEmojiCount(),
                    model.getTimestamp(),
                    0,
                    model.getImageWidth(),
                    model.getImageHeight(),
                    model.getAspectRatio(),
                    model.getSelectionCount()
            );

            Log.d("VideoMessageModel2Creation", "messagemodel2 created with caption: '" + model2.getCaption() + "'");

            // Upload video with thumbnail
            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, localVideoFile, savedThumbnail, senderId, userFTokenKey);
            uploadHelper.uploadContent(
                    Constant.video,
                    videoUri,
                    model2.getCaption(),
                    videoModelId,
                    savedThumbnail, // savedThumbnail
                    fileThumbName, // fileThumbName
                    localVideoFile.getName(),
                    null, // contactName
                    null, // contactPhone
                    null, // audioTime
                    null, // audioName
                    getFileExtension(videoUri),
                    receiverUid,
                    model.getReplyCrtPostion(),
                    model.getReplyKey(),
                    model.getReplyOldData(),
                    model.getReplyType(),
                    model.getReplytextData(),
                    model.getDataType(),
                    model.getFileName(),
                    model.getForwaredKey(),
                    videoWidthDp,
                    videoHeightDp,
                    aspectRatio
            );
        }

        // Clear selections after sending
        selectedVideoUris.clear();
        selectedVideoFiles.clear();

        Log.d("DIALOGUE_DEBUG", "=== createAndSendSelectionBunchMessageFromPicker COMPLETED ===");
    }

    private void sendMultipleImages(String caption) {
        Log.d("DIALOGUE_DEBUG", "=== sendMultipleImages START ===");
        Log.d("DIALOGUE_DEBUG", "Received caption: '" + caption + "'");
        Log.d("DIALOGUE_DEBUG", "Caption length: " + (caption != null ? caption.length() : "null"));
        Log.d("DIALOGUE_DEBUG", "SelectedImageUris size: " + selectedImageUris.size());
        Log.d("DIALOGUE_DEBUG", "SelectedImageFiles size: " + selectedImageFiles.size());

        if (selectedImageUris.isEmpty() || selectedImageFiles.isEmpty()) {
            Log.w("DIALOGUE_DEBUG", "No images to send, aborting.");
            Log.d("DIALOGUE_DEBUG", "No images to send, aborting bunch operation");
            return;
        }

        Log.d("DIALOGUE_DEBUG", "Images validation passed, proceeding with send...");

        Constant.getSfFuncion(getApplicationContext());
        final String receiverUid = getIntent().getStringExtra("friendUidKey");
        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
        final String senderRoom = senderId + receiverUid;
        final String receiverRoom = receiverUid + senderId;
        Log.d("DIALOGUE_DEBUG", "senderRoom: " + senderRoom + ", receiverRoom: " + receiverRoom);

        Log.d("DIALOGUE_DEBUG", "receiverUid: " + receiverUid);
        Log.d("DIALOGUE_DEBUG", "senderId: " + senderId);
        Log.d("DIALOGUE_DEBUG", "userFTokenKey: " + userFTokenKey);
        Log.d("DIALOGUE_DEBUG", "Starting bunch image send - Total images: " + selectedImageUris.size() + ", Receiver: " + receiverUid);

        if (userFTokenKey == null || userFTokenKey.isEmpty()) {
            Log.e("DIALOGUE_DEBUG", "userFTokenKey is null or empty! Cannot proceed with upload.");
            Toast.makeText(mContext, "Error: Missing user token. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("DIALOGUE_DEBUG", "userFTokenKey validation passed, proceeding...");

        int totalImages = selectedImageUris.size();
        Log.d("DIALOGUE_DEBUG", "Starting to send " + totalImages + " images (WhatsApp picker)");

        if (totalImages > 1) {
            Log.d("DIALOGUE_DEBUG", "Using selectionBunch flow for WhatsAppLikeImagePicker");
            Log.d("DIALOGUE_DEBUG", "Current selectionBunch size: " + this.selectionBunch.size());
            Log.d("DIALOGUE_DEBUG", "Caption: '" + caption + "'");

            // Use the selectionBunch built in handleMultipleImageSelection()
            if (this.selectionBunch.isEmpty()) {
                Log.e("DIALOGUE_DEBUG", "Selection bunch is empty, aborting multi-image send.");
                Toast.makeText(mContext, "Error preparing images. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("DIALOGUE_DEBUG", "Selection bunch validation passed, proceeding with bunch send...");

            // Debug: Log the selectionBunch before sending
            Log.d("bunch###", "About to send selectionBunch with " + this.selectionBunch.size() + " items:");
            for (int i = 0; i < this.selectionBunch.size(); i++) {
                selectionBunchModel item = this.selectionBunch.get(i);
                Log.d("bunch###", "  Item " + (i + 1) + ": fileName='" + item.getFileName() + "', imgUrl='" + item.getImgUrl() + "'");
            }

            // Debug: Log the selectionBunch before sending
            Log.d("DIALOGUE_DEBUG", "About to call createAndSendSelectionBunchMessageFromPicker with selectionBunch size: " + this.selectionBunch.size());
            for (int i = 0; i < this.selectionBunch.size(); i++) {
                selectionBunchModel item = this.selectionBunch.get(i);
                Log.d("DIALOGUE_DEBUG", "  SelectionBunch item " + (i + 1) + ": fileName='" + item.getFileName() + "', imgUrl='" + item.getImgUrl() + "'");
            }

            Log.d("DIALOGUE_DEBUG", "Calling createAndSendSelectionBunchMessageFromPicker with caption: '" + caption + "'");
            createAndSendSelectionBunchMessageFromPicker(this.selectionBunch, caption);
            Log.d("DIALOGUE_DEBUG", "createAndSendSelectionBunchMessageFromPicker completed");
        } else {
            Log.d("DIALOGUE_DEBUG", "Only one image selected, falling back to single-image flow.");
            Log.d("DIALOGUE_DEBUG", "Calling sendSingleImageFromPicker with caption: '" + caption + "'");
            sendSingleImageFromPicker(caption);
            Log.d("DIALOGUE_DEBUG", "sendSingleImageFromPicker completed");
        }

        // Clear selections after sending
        selectedImageUris.clear();
        selectedImageFiles.clear();
        selectedFullImageFiles.clear();
        selectionBunch.clear();

        // Note: Dialog dismissal is now handled in proceedWithSending() after calling this method
    }

    private ArrayList<selectionBunchModel> buildSelectionBunchForPicker() {
        ArrayList<selectionBunchModel> selectionBunch = new ArrayList<>();
        Log.d("bunch###", "Building selection bunch for " + selectedImageUris.size() + " images");

        for (int i = 0; i < selectedImageUris.size(); i++) {
            Uri imageUri = selectedImageUris.get(i);
            String fileName = getFileNameFromUri(imageUri);
            if (fileName == null || fileName.isEmpty()) {
                if (i < selectedImageFiles.size() && selectedImageFiles.get(i) != null) {
                    fileName = selectedImageFiles.get(i).getName();
                } else if (i < selectedFullImageFiles.size() && selectedFullImageFiles.get(i) != null) {
                    fileName = selectedFullImageFiles.get(i).getName();
                } else {
                    fileName = "image_" + System.currentTimeMillis() + "_" + i + ".jpg";
                }
            }

            // Decode URL-encoded characters in filename
            try {
                String decodedFileName = java.net.URLDecoder.decode(fileName, "UTF-8");
                Log.d("bunch###", "Decoded filename for bunch: " + fileName + " -> " + decodedFileName);
                fileName = decodedFileName;
            } catch (Exception e) {
                Log.w("bunch###", "Failed to decode filename for bunch: " + fileName, e);
            }

            // Remove any subdirectory prefixes like "chats/" from the filename
            if (fileName.contains("/")) {
                String originalFileName = fileName;
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                Log.d("bunch###", "Removed subdirectory prefix for bunch: " + originalFileName + " -> " + fileName);
            }

            selectionBunch.add(new selectionBunchModel("", fileName));
            Log.d("SelectionBunchPicker", "Added selectionBunch item " + fileName);
            Log.d("bunch###", "Added bunch item " + (i + 1) + "/" + selectedImageUris.size() + ": " + fileName);
        }

        Log.d("bunch###", "Selection bunch built successfully with " + selectionBunch.size() + " items");
        return selectionBunch;
    }

    private void createAndSendSelectionBunchMessageFromPicker(ArrayList<selectionBunchModel> selectionBunch, String caption) {
        Log.d("DIALOGUE_DEBUG", "=== createAndSendSelectionBunchMessageFromPicker START ===");
        Log.d("DIALOGUE_DEBUG", "Received selectionBunch size: " + selectionBunch.size());
        Log.d("DIALOGUE_DEBUG", "Received caption: '" + caption + "'");

        try {
            Log.d("SelectionBunchPicker", "Creating message with " + selectionBunch.size() + " images");
            Log.d("SelectionBunchPicker", "Caption: '" + caption + "'");
            Log.d("bunch###", "Creating bunch message with " + selectionBunch.size() + " images");

            // Debug: Log the selectionBunch parameter
            Log.d("bunch###", "SelectionBunch parameter size: " + selectionBunch.size());
            for (int i = 0; i < selectionBunch.size(); i++) {
                selectionBunchModel item = selectionBunch.get(i);
                Log.d("bunch###", "  Parameter item " + (i + 1) + ": fileName='" + item.getFileName() + "', imgUrl='" + item.getImgUrl() + "'");
            }

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);

            Constant.getSfFuncion(mContext);
            final String receiverUid = getIntent().getStringExtra("friendUidKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            Log.d("bunch###", "Bunch message - Receiver: " + receiverUid + ", Sender: " + senderId);

            String modelId = database.getReference().push().getKey();
            String uniqDate = Constant.getCurrentDate();
            Log.d("bunch###", "Bunch message ID: " + modelId + ", Date: " + uniqDate);

            ArrayList<emojiModel> emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));

            // Get the first image file for preview
            File firstImageFile = null;
            String firstImagePath = "";
            String imageWidthDp = "";
            String imageHeightDp = "";
            String aspectRatio = "";

            if (!selectedImageFiles.isEmpty() && selectedImageFiles.get(0) != null) {
                firstImageFile = selectedImageFiles.get(0);
                firstImagePath = firstImageFile.getAbsolutePath();

                // Calculate dimensions for the first image
                if (!selectedImageUris.isEmpty()) {
                    String[] dimensions = Constant.calculateImageDimensions(mContext, firstImageFile, selectedImageUris.get(0));
                    imageWidthDp = dimensions[0];
                    imageHeightDp = dimensions[1];
                    aspectRatio = dimensions[2];
                }
            }

            messageModel model;
            if (uniqueDates.add(uniqDate)) {
                model = new messageModel(senderId, "", currentDateTimeString, firstImagePath,
                        Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", firstImageFile != null ? firstImageFile.getName() : "", "", "",
                        caption, 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(),
                        imageWidthDp, imageHeightDp, aspectRatio, String.valueOf(selectionBunch.size()), selectionBunch);
            } else {
                model = new messageModel(senderId, "", currentDateTimeString, firstImagePath,
                        Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", firstImageFile != null ? firstImageFile.getName() : "", "", "",
                        caption, 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(),
                        imageWidthDp, imageHeightDp, aspectRatio, String.valueOf(selectionBunch.size()), selectionBunch);
            }

            // Debug: Log the created message model
            Log.d("bunch###", "Created message model with selectionBunch size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : "null"));
            Log.d("bunch###", "Message model - document: " + model.getDocument() + ", fileName: " + model.getFileName());
            Log.d("bunch###", "Message model - imageWidth: " + model.getImageWidth() + ", imageHeight: " + model.getImageHeight() + ", aspectRatio: " + model.getAspectRatio());
            if (model.getSelectionBunch() != null) {
                for (int i = 0; i < model.getSelectionBunch().size(); i++) {
                    selectionBunchModel item = model.getSelectionBunch().get(i);
                    Log.d("bunch###", "  Model item " + (i + 1) + ": fileName='" + item.getFileName() + "', imgUrl='" + item.getImgUrl() + "'");
                }
            }

            // Store message in SQLite pending table before upload
            try {
                new DatabaseHelper(mContext).insertPendingMessage(model);
                Log.d("PendingMessages", "Selection bunch message stored in pending table: " + model.getModelId());
            } catch (Exception e) {
                Log.e("PendingMessages", "Error storing selection bunch message in pending table: " + e.getMessage(), e);
            }

            // Add message to UI immediately for preview
            messageList.add(model);
            runOnUiThread(() -> {
                otherFunctions.updateMessageList(new ArrayList<>(messageList), chatAdapter);
                chatAdapter.setLastItemVisible(true); // Show progress for pending message
                binding.messageRecView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);

                // UI updated for selectionBunch message
                Log.d("DIALOGUE_DEBUG", "UI updated for selectionBunch message");
            });

            Log.d("bunch###", "Message model created and added to UI, starting upload process");

            uploadSelectionBunchImagesFromPicker(selectionBunch, model, modelId, receiverUid);

        } catch (Exception e) {
            Log.e("SelectionBunchPicker", "Error creating selectionBunch message: " + e.getMessage(), e);
            Log.e("bunch###", "Error creating bunch message: " + e.getMessage());
            Toast.makeText(mContext, "Error sending images: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadSelectionBunchImagesFromPicker(ArrayList<selectionBunchModel> selectionBunch,
                                                      messageModel model, String modelId, String receiverUid) {
        try {
            Log.d("UploadSelectionBunch", "Uploading " + selectionBunch.size() + " images (WhatsApp picker)");
            Log.d("bunch###", "Starting upload of " + selectionBunch.size() + " bunch images");

            Constant.getSfFuncion(mContext);
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            Log.d("bunch###", "Upload bunch - Sender: " + senderId + ", Receiver: " + receiverUid + ", ModelID: " + modelId);

            if (selectionBunch.isEmpty()) {
                Log.w("UploadSelectionBunch", "Selection bunch empty, skipping upload");
                Log.w("bunch###", "Selection bunch empty, skipping upload");
                return;
            }

            ArrayList<File> compressedFiles = new ArrayList<>();
            ArrayList<File> fullSizeFiles = new ArrayList<>();
            ArrayList<String> selectionBunchFilePaths = new ArrayList<>();

            Uri primaryImageUri = null;
            String primaryCaption = "";
            String primaryImageWidthDp = "";
            String primaryImageHeightDp = "";
            String primaryAspectRatio = "";

            for (int i = 0; i < selectionBunch.size() && i < selectedImageUris.size(); i++) {
                Uri imageUri = selectedImageUris.get(i);
                File compressedFile = (i < selectedImageFiles.size()) ? selectedImageFiles.get(i) : null;
                File fullSizeFile = (i < selectedFullImageFiles.size()) ? selectedFullImageFiles.get(i) : null;

                if (compressedFile == null || fullSizeFile == null) {
                    Log.e("UploadSelectionBunch", "Missing local files for image index " + i + ", skipping");
                    Log.e("bunch###", "Missing local files for bunch image " + (i + 1) + ", skipping");
                    continue;
                }

                compressedFiles.add(compressedFile);
                fullSizeFiles.add(fullSizeFile);
                selectionBunchFilePaths.add(fullSizeFile.getAbsolutePath());

                String[] dimensions = Constant.calculateImageDimensions(mContext, compressedFile, imageUri);

                if (i == 0) {
                    primaryImageUri = imageUri;
                    // Use the caption from the picker instead of empty string
                    primaryCaption = currentCaption != null ? currentCaption : "";
                    primaryImageWidthDp = dimensions[0];
                    primaryImageHeightDp = dimensions[1];
                    primaryAspectRatio = dimensions[2];
                    Log.d("bunch###", "Primary image set - Caption: '" + primaryCaption + "', Dimensions: " + primaryImageWidthDp + "x" + primaryImageHeightDp);
                }

                Log.d("UploadSelectionBunch", "Prepared local files for image " + (i + 1) + "/" + selectionBunch.size());
                Log.d("bunch###", "Prepared bunch image " + (i + 1) + "/" + selectionBunch.size() + " - Compressed: " + compressedFile.getName() + ", Full: " + fullSizeFile.getName());
            }

            if (selectionBunchFilePaths.isEmpty() || compressedFiles.isEmpty()) {
                Log.e("UploadSelectionBunch", "No valid files prepared for selectionBunch upload (WhatsApp picker)");
                Log.e("bunch###", "No valid files prepared for bunch upload");
                return;
            }

            if (model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty()) {
                model.setFileName(model.getSelectionBunch().get(0).getFileName());
                Log.d("bunch###", "Set model filename to: " + model.getSelectionBunch().get(0).getFileName());
            }

            File primaryCompressed = compressedFiles.get(0);
            File primaryFull = fullSizeFiles.get(0);

            if (primaryImageUri == null) {
                primaryImageUri = selectedImageUris.get(0);
            }

            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, primaryCompressed, primaryFull, senderId, userFTokenKey, model);
            uploadHelper.setSelectionBunchFilePaths(selectionBunchFilePaths);

            // Debug: Check if UploadChatHelper will process selectionBunch
            Log.d("bunch###", "UploadHelper setup complete - checking conditions:");
            Log.d("bunch###", "  - model != null: " + (model != null));
            Log.d("bunch###", "  - model.getSelectionBunch() != null: " + (model.getSelectionBunch() != null));
            Log.d("bunch###", "  - model.getSelectionBunch().isEmpty(): " + (model.getSelectionBunch() != null ? model.getSelectionBunch().isEmpty() : "N/A"));
            Log.d("bunch###", "  - selectionBunchFilePaths.isEmpty(): " + selectionBunchFilePaths.isEmpty());

            Log.d("UploadSelectionBunch", "selectionBunchFilePaths collected: " + selectionBunchFilePaths.size());
            Log.d("bunch###", "Bunch file paths collected: " + selectionBunchFilePaths.size());
            for (int i = 0; i < selectionBunchFilePaths.size(); i++) {
                Log.d("UploadSelectionBunch", "selectionBunchFilePaths[" + i + "]: " + selectionBunchFilePaths.get(i));
                Log.d("bunch###", "Bunch file path " + (i + 1) + ": " + selectionBunchFilePaths.get(i));
            }

            Log.d("ImageUpload", "=== CALLING UploadChatHelper from chattingScreen ===");
            Log.d("ImageUpload", "PrimaryImageUri: " + primaryImageUri);
            Log.d("ImageUpload", "PrimaryCaption: '" + primaryCaption + "'");
            Log.d("ImageUpload", "PrimaryCompressed: " + (primaryCompressed != null ? primaryCompressed.getAbsolutePath() : "null"));
            Log.d("ImageUpload", "PrimaryFull: " + (primaryFull != null ? primaryFull.getAbsolutePath() : "null"));

            // Debug: Log the model being passed to upload helper
            Log.d("ImageUpload", "Model being passed to upload helper:");
            Log.d("ImageUpload", "  - selectionBunch size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : "null"));
            Log.d("ImageUpload", "  - selectionCount: " + model.getSelectionCount());
            Log.d("ImageUpload", "  - dataType: " + model.getDataType());
            Log.d("ImageUpload", "  - document: " + model.getDocument());

            Log.d("bunch###", "Starting upload of bunch images to Firebase");
            Log.d("bunch###", "UploadHelper created with model - selectionBunch size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : "null"));
            Log.d("bunch###", "UploadHelper created with model - selectionCount: " + model.getSelectionCount());
            uploadHelper.uploadContent(
                    Constant.img,
                    primaryImageUri,
                    primaryCaption,
                    modelId,
                    null,
                    null,
                    primaryCompressed.getName(),
                    null,
                    null,
                    null,
                    null,
                    getFileExtension(primaryImageUri),
                    receiverUid,
                    model.getReplyCrtPostion(),
                    model.getReplyKey(),
                    model.getReplyOldData(),
                    model.getReplyType(),
                    model.getReplytextData(),
                    model.getDataType(),
                    model.getFileName(),
                    model.getForwaredKey(),
                    primaryImageWidthDp,
                    primaryImageHeightDp,
                    primaryAspectRatio
            );
            Log.d("bunch###", "Bunch upload initiated successfully");

            // Debug: Check model state after uploadContent call
            Log.d("bunch###", "After uploadContent call - model state:");
            Log.d("bunch###", "  - model.getSelectionBunch() != null: " + (model.getSelectionBunch() != null));
            Log.d("bunch###", "  - model.getSelectionBunch().size(): " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : "null"));

            // Add a delayed check to see if UploadChatHelper processed the selectionBunch
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                Log.d("bunch###", "Checking UploadChatHelper processing after 3 seconds...");
                Log.d("bunch###", "Model selectionBunch size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : "null"));
                if (model.getSelectionBunch() != null) {
                    for (int i = 0; i < model.getSelectionBunch().size(); i++) {
                        com.Appzia.enclosure.Model.selectionBunchModel item = model.getSelectionBunch().get(i);
                        Log.d("bunch###", "  Item " + (i + 1) + ": fileName='" + item.getFileName() + "', imgUrl='" + item.getImgUrl() + "'");
                    }
                }
            }, 3000);

        } catch (Exception e) {
            Log.e("UploadSelectionBunch", "Error uploading selectionBunch images (WhatsApp picker): " + e.getMessage(), e);
            Log.e("bunch###", "Error uploading bunch images: " + e.getMessage());
        }
    }

    private void sendSingleImageFromPicker(String caption) {
        Log.d("DIALOGUE_DEBUG", "=== sendSingleImageFromPicker START ===");
        Log.d("DIALOGUE_DEBUG", "Received caption: '" + caption + "'");
        Log.d("DIALOGUE_DEBUG", "SelectedImageUris size: " + selectedImageUris.size());
        Log.d("DIALOGUE_DEBUG", "SelectedImageFiles size: " + selectedImageFiles.size());
        Log.d("DIALOGUE_DEBUG", "SelectedFullImageFiles size: " + selectedFullImageFiles.size());

        if (selectedImageUris.isEmpty()) {
            Log.e("DIALOGUE_DEBUG", "No image URIs available, aborting");
            return;
        }

        Log.d("DIALOGUE_DEBUG", "ImageUri: " + selectedImageUris.get(0));
        Log.d("DIALOGUE_DEBUG", "ImageFile: " + (selectedImageFiles.get(0) != null ? selectedImageFiles.get(0).getAbsolutePath() : "null"));

        try {
            Log.d("DIALOGUE_DEBUG", "Getting image data...");
            Uri imageUri = selectedImageUris.get(0);
            File imageFile = selectedImageFiles.get(0);
            File fullImageFile = (!selectedFullImageFiles.isEmpty()) ? selectedFullImageFiles.get(0) : null;
            Log.d("DIALOGUE_DEBUG", "Image data retrieved successfully");

            String individualCaption = caption; // Use the passed caption instead of empty string
            Log.d("DIALOGUE_DEBUG", "Using caption: '" + individualCaption + "'");

            Log.d("DIALOGUE_DEBUG", "Calculating image dimensions...");
            String[] dimensions = Constant.calculateImageDimensions(mContext, imageFile, imageUri);
            String imageWidthDp = dimensions[0];
            String imageHeightDp = dimensions[1];
            String aspectRatio = dimensions[2];
            Log.d("DIALOGUE_DEBUG", "Dimensions calculated: " + imageWidthDp + "x" + imageHeightDp + ", ratio: " + aspectRatio);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);
            Log.d("DIALOGUE_DEBUG", "Current time: " + currentDateTimeString);

            Log.d("DIALOGUE_DEBUG", "Getting user data...");
            Constant.getSfFuncion(getApplicationContext());
            final String receiverUid = getIntent().getStringExtra("friendUidKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            Log.d("DIALOGUE_DEBUG", "receiverUid: " + receiverUid + ", senderId: " + senderId);

            String modelId = database.getReference().push().getKey();
            String uniqDate = Constant.getCurrentDate();
            Log.d("DIALOGUE_DEBUG", "modelId: " + modelId + ", uniqDate: " + uniqDate);

            ArrayList<emojiModel> emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));

            messageModel model;
            if (uniqueDates.add(uniqDate)) {
                model = new messageModel(senderId, "", currentDateTimeString, imageFile.toString(),
                        Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", imageFile.getName(), "", "",
                        individualCaption, 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(),
                        imageWidthDp, imageHeightDp, aspectRatio, "1");
            } else {
                model = new messageModel(senderId, "", currentDateTimeString, imageFile.toString(),
                        Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", imageFile.getName(), "", "",
                        individualCaption, 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(),
                        imageWidthDp, imageHeightDp, aspectRatio, "1");
            }

            // Store message in SQLite pending table before upload
            try {
                new DatabaseHelper(mContext).insertPendingMessage(model);
                Log.d("PendingMessages", "Image message stored in pending table: " + model.getModelId());
            } catch (Exception e) {
                Log.e("PendingMessages", "Error storing image message in pending table: " + e.getMessage(), e);
            }

            messageList.add(model);
            chatAdapter.itemAdd(binding.messageRecView);
            chatAdapter.setLastItemVisible(true); // Show progress for pending message
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            binding.messageRecView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);

            // Dismiss dialogue after UI update
            if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                Log.d("DIALOGUE_DEBUG", "Dismissing dialogue after UI update in sendSingleImageFromPicker");
                Constant.dialogLayoutFullScreen.dismiss();

                // Set a flag to prevent immediate recreation
                Constant.dialogLayoutFullScreen = null;
                Log.d("DIALOGUE_DEBUG", "Set dialogLayoutFullScreen to null to prevent recreation in sendSingleImageFromPicker");
            }

            Log.d("DIALOGUE_DEBUG", "=== CALLING UploadChatHelper from sendSingleImageFromPicker ===");
            Log.d("DIALOGUE_DEBUG", "ImageFile: " + (imageFile != null ? imageFile.getAbsolutePath() : "null"));
            Log.d("DIALOGUE_DEBUG", "FullImageFile: " + (fullImageFile != null ? fullImageFile.getAbsolutePath() : "null"));
            Log.d("DIALOGUE_DEBUG", "ImageUri: " + imageUri);
            Log.d("DIALOGUE_DEBUG", "IndividualCaption: '" + individualCaption + "'");

            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, imageFile, fullImageFile, senderId, userFTokenKey, model);
            Log.d("DIALOGUE_DEBUG", "UploadChatHelper created, calling uploadContent...");

            uploadHelper.uploadContent(
                    Constant.img,
                    imageUri,
                    individualCaption,
                    modelId,
                    null,
                    null,
                    imageFile.getName(),
                    null,
                    null,
                    null,
                    null,
                    getFileExtension(imageUri),
                    receiverUid,
                    model.getReplyCrtPostion(),
                    model.getReplyKey(),
                    model.getReplyOldData(),
                    model.getReplyType(),
                    model.getReplytextData(),
                    model.getDataType(),
                    model.getFileName(),
                    model.getForwaredKey(),
                    imageWidthDp,
                    imageHeightDp,
                    aspectRatio
            );

            Log.d("DIALOGUE_DEBUG", "UploadChatHelper.uploadContent completed");
            Log.d("DIALOGUE_DEBUG", "=== sendSingleImageFromPicker COMPLETED SUCCESSFULLY ===");

        } catch (Exception e) {
            Log.e("DIALOGUE_DEBUG", "Error sending single image: " + e.getMessage(), e);
            Toast.makeText(mContext, "Error sending image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("ImageUpload", "=== onActivityResult ===");
        Log.d("ImageUpload", "RequestCode: " + requestCode + ", ResultCode: " + resultCode);
        Log.d("ImageUpload", "PICK_IMAGE_REQUEST_CODE: " + PICK_IMAGE_REQUEST_CODE);
        Log.d("ImageUpload", "Data: " + (data != null ? "not null" : "null"));

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            modelId = database.getReference().push().getKey();

            // Handle WhatsApp-like image picker results
            ArrayList<Uri> newSelectedImages = data.getParcelableArrayListExtra(WhatsAppLikeImagePicker.EXTRA_SELECTED_IMAGES);
            Log.d("ImageUpload", "NewSelectedImages: " + (newSelectedImages != null ? newSelectedImages.size() : "null"));

            if (newSelectedImages != null && !newSelectedImages.isEmpty()) {
                // Capture caption from picker, if provided
                String pickerCaption = data.getStringExtra("caption");
                if (pickerCaption != null) {
                    currentCaption = pickerCaption;
                }
                // Add new images to existing selection
                Log.d("ImageUpload", "Adding " + newSelectedImages.size() + " images to selection");
                selectedImageUris.addAll(newSelectedImages);
                Log.d("ImageUpload", "SelectedImageUris size after adding: " + selectedImageUris.size());
                handleMultipleImageSelection();
            }

            if (mStoragetask != null && mStoragetask.isInProgress()) {
                Constant.showCustomToast("Upload in process", findViewById(R.id.includedToast), findViewById(R.id.toastText));
            }

        }

        if (requestCode == PICK_VIDEO_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            GlobalUri = data.getData();
            modelId = database.getReference().push().getKey();

            if (mStoragetask != null && mStoragetask.isInProgress()) {
                Constant.showCustomToast("Upload in process", findViewById(R.id.includedToast), findViewById(R.id.toastText));
                return;
            }

            try {
                // Get original filename
                String fileName = getFileNameFromUri(GlobalUri);
                if (fileName == null) {
                    Toast.makeText(this, "Unable to get file name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Copy video to internal cache
                globalFile = new File(getCacheDir(), fileName);
                copyUriToFile(GlobalUri, globalFile);

                // Create thumbnail
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(globalFile.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
                File savedThumbnail = FileUtils.saveBitmapToFile(getApplicationContext(), thumbnail, "thumbnail.png");
                String fileThumbName = fileName + ".png";

                // Save thumbnail to external storage
                File thumbDir = getExternalStorageDir(Environment.DIRECTORY_PICTURES, "Enclosure/Media/Thumbnail");
                File thumbnailFile = new File(thumbDir, fileThumbName);
                if (!thumbnailFile.exists()) {
                    try {
                        java.nio.file.Files.copy(savedThumbnail.toPath(), thumbnailFile.toPath());
                    } catch (Exception e) {
                        Log.e("FileCopy", "Error copying file", e);
                    }
                }

                // Show video preview dialog
                showVideoPreviewDialog(globalFile, thumbnailFile, fileThumbName, fileName);

            } catch (Exception e) {
                Toast.makeText(this, "Error processing video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VideoProcessing", "Error processing video", e);
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
                Constant.showCustomToast("Upload in process", findViewById(R.id.includedToast), findViewById(R.id.toastText));
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

                        // Save to external storage
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


                        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
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
                                    binding.messageBox.setText("");
                                    Constant.getSfFuncion(getApplicationContext());
                                    final String receiverUid = getIntent().getStringExtra("friendUidKey");
                                    final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                    final String senderRoom = senderId + receiverUid;
                                    final String receiverRoom = receiverUid + senderId;
                                    Log.d("senderRoom", senderRoom + receiverRoom);
                                    Date d = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                    String currentDateTimeString = sdf.format(d);
                                    messageModel model = null;

                                    String[] dimensions = Constant.calculateImageDimensions(mContext, globalFile, Uri.fromFile(flexible));
                                    String imageWidthDp = dimensions[0];
                                    String imageHeightDp = dimensions[1];
                                    String aspectRatio = dimensions[2];

                                    if (messageBoxMy.getText().toString().trim().equals("")) {
                                        assert modelId != null;
                                        String uniqDate = Constant.getCurrentDate();
                                        if (uniqueDates.add(uniqDate)) {
                                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                            emojiModels.add(new emojiModel("", ""));
                                            model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", "", 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            messageList.add(model);
                                        } else {
                                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                            emojiModels.add(new emojiModel("", ""));
                                            model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", "", 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            messageList.add(model);
                                        }

                                        messagemodel2 model2 = new messagemodel2(
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
                                                model.getUserName(),
                                                model.getReplytextData(),
                                                model.getReplyKey(),
                                                model.getReplyType(),
                                                model.getReplyOldData(),
                                                model.getReplyCrtPostion(),
                                                model.getModelId(),
                                                model.getReceiverUid(),
                                                model.getForwaredKey(),
                                                model.getGroupName(),
                                                model.getDocSize(),
                                                model.getFileName(),
                                                model.getThumbnail(),
                                                model.getFileNameThumbnail(),
                                                model.getCaption(),
                                                model.getNotification(),
                                                model.getCurrentDate(),
                                                model.getEmojiModel(),
                                                model.getEmojiCount(),
                                                model.getTimestamp(),
                                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                                        );

                                        chatAdapter.itemAdd(binding.messageRecView);
                                        chatAdapter.setLastItemVisible(isLastItemVisible);
                                        chatAdapter.notifyItemInserted(messageList.size() - 1);

                                        Log.d("TAG", "actualName: " + globalFile.getName());

                                        UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, null, senderId, userFTokenKey);
                                        uploadHelper.uploadContent(
                                                Constant.img,
                                                Uri.fromFile(flexible), // uri
                                                messageBoxMy.getText().toString().trim(), // captionText (use dialog caption)
                                                modelId, // modelId
                                                null, // savedThumbnail
                                                null, // fileThumbName
                                                globalFile.getName(), // fileName
                                                null, // contactName
                                                null, // contactPhone
                                                null, // audioTime
                                                null, // audioName
                                                getFileExtension(Uri.fromFile(flexible)), // extension
                                                receiverUid, model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(),// receiverUid
                                                model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(),
                                                imageWidthDp, imageHeightDp, aspectRatio);

                                        Constant.dialogLayoutFullScreen.dismiss();

                                    } else {
                                        assert modelId != null;

                                        String uniqDate = Constant.getCurrentDate();
                                        if (uniqueDates.add(uniqDate)) {
                                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                            emojiModels.add(new emojiModel("", ""));
                                            model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", messageBoxMy.getText().toString(), 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            messageList.add(model);
                                        } else {
                                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                            emojiModels.add(new emojiModel("", ""));
                                            model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", messageBoxMy.getText().toString(), 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            messageList.add(model);
                                        }

                                        messagemodel2 model2 = new messagemodel2(
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
                                                model.getUserName(),
                                                model.getReplytextData(),
                                                model.getReplyKey(),
                                                model.getReplyType(),
                                                model.getReplyOldData(),
                                                model.getReplyCrtPostion(),
                                                model.getModelId(),
                                                model.getReceiverUid(),
                                                model.getForwaredKey(),
                                                model.getGroupName(),
                                                model.getDocSize(),
                                                model.getFileName(),
                                                model.getThumbnail(),
                                                model.getFileNameThumbnail(),
                                                model.getCaption(),
                                                model.getNotification(),
                                                model.getCurrentDate(),
                                                model.getEmojiModel(),
                                                model.getEmojiCount(),
                                                model.getTimestamp(),
                                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                                        );

                                        chatAdapter.itemAdd(binding.messageRecView);
                                        chatAdapter.setLastItemVisible(isLastItemVisible);
                                        chatAdapter.notifyItemInserted(messageList.size() - 1);

                                        Constant.dialogLayoutFullScreen.dismiss();

                                        UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, null, senderId, userFTokenKey);
                                        uploadHelper.uploadContent(
                                                Constant.img, // uploadType
                                                Uri.fromFile(flexible), // uri
                                                messageBoxMy.getText().toString().trim(), // captionText
                                                modelId, // modelId
                                                null, // savedThumbnail
                                                null, // fileThumbName
                                                globalFile.getName(), // fileName
                                                null, // contactName
                                                null, // contactPhone
                                                null, // audioTime
                                                null, // audioName
                                                getFileExtension(Uri.fromFile(flexible)), // extension
                                                receiverUid, model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(),// receiverUid
                                                model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(),
                                                imageWidthDp, imageHeightDp, aspectRatio);
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
                    Toast.makeText(chattingScreen.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == PICK_VIDEO_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            GlobalUri = data.getData();
            modelId = database.getReference().push().getKey();

            if (mStoragetask != null && mStoragetask.isInProgress()) {
                Constant.showCustomToast("Upload in process", findViewById(R.id.includedToast), findViewById(R.id.toastText));
                return;
            }

            try {
                // Get original filename
                String fileName = getFileNameFromUri(GlobalUri);
                if (fileName == null) {
                    Toast.makeText(this, "Unable to get file name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Copy video to internal cache
                globalFile = new File(getCacheDir(), fileName);
                copyUriToFile(GlobalUri, globalFile);

                // Create thumbnail
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(globalFile.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
                File savedThumbnail = FileUtils.saveBitmapToFile(getApplicationContext(), thumbnail, "thumbnail.png");
                String fileThumbName = fileName + ".png";

                // Save thumbnail to external storage
                File thumbDir = getExternalStorageDir(Environment.DIRECTORY_PICTURES, "Enclosure/Media/Thumbnail");
                File thumbnailFile = new File(thumbDir, fileThumbName);
                if (!thumbnailFile.exists()) {
                    copyUriToFile(Uri.fromFile(savedThumbnail), thumbnailFile);
                }

                // Save video to external storage
                File videoDir = getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Videos");
                File videoFile = new File(videoDir, fileName);
                if (!videoFile.exists()) {
                    copyUriToFile(GlobalUri, videoFile);
                }

                // Show preview dialog
                showVideoPreviewDialog(videoFile, thumbnailFile, fileThumbName, fileName);

            } catch (Exception e) {
                // Toast.makeText(mContext, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
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


                        File f;
                        if (GlobalUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                            final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                            extension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(GlobalUri));

                        } else {
                            extension = MimeTypeMap.getFileExtensionFromUrl(GlobalUri.getPath());

                        }


                        // Log.d("extension", extension);
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
                                //   Toast.makeText(mActivity, "offline", Toast.LENGTH_SHORT).show();

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

                                    Log.d("imageFile111677t7", imageFile.getPath());
                                    //  Toast.makeText(mActivity, imageFile.getPath(), Toast.LENGTH_SHORT).show();
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

                            LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);

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
                                            final String receiverUid = getIntent().getStringExtra("friendUidKey");
                                            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                            final String senderRoom = senderId + receiverUid;
                                            final String receiverRoom = receiverUid + senderId;
                                            Log.d("senderRoom", senderRoom + receiverRoom);
                                            Date d = new Date();
                                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                            String currentDateTimeString = sdf.format(d);


                                            long finalFileSize = getFileSize(globalFile.getPath());

                                            assert modelId != null;
                                            messageModel model;

                                            String uniqDate = Constant.getCurrentDate();
                                            if (uniqueDates.add(uniqDate)) {
                                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                                emojiModels.add(new emojiModel("", ""));
                                                model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), docName.getText().toString(), extension, "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", String.valueOf(getFormattedFileSize(finalFileSize)), globalFile.getName(), "", "", "", 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);
                                                messageList.add(model);
                                            } else {
                                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                                emojiModels.add(new emojiModel("", ""));
                                                model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), docName.getText().toString(), extension, "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", String.valueOf(getFormattedFileSize(finalFileSize)), globalFile.getName(), "", "", "", 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);
                                                messageList.add(model);
                                            }

                                            messagemodel2 model2 = new messagemodel2(
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
                                                    model.getUserName(),
                                                    model.getReplytextData(),
                                                    model.getReplyKey(),
                                                    model.getReplyType(),
                                                    model.getReplyOldData(),
                                                    model.getReplyCrtPostion(),
                                                    model.getModelId(),
                                                    model.getReceiverUid(),
                                                    model.getForwaredKey(),
                                                    model.getGroupName(),
                                                    model.getDocSize(),
                                                    model.getFileName(),
                                                    model.getThumbnail(),
                                                    model.getFileNameThumbnail(),
                                                    model.getCaption(),
                                                    model.getNotification(),
                                                    model.getCurrentDate(),
                                                    model.getEmojiModel(),
                                                    model.getEmojiCount(),
                                                    model.getTimestamp(),
                                                    0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                                            );

                                            //TODO : active : 0 = still loading
                                            //TODO : active : 1 = completed

//                                            try {
//                                                new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                            } catch (Exception e) {
//                                                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                            }

                                            chatAdapter.itemAdd(binding.messageRecView);
                                            chatAdapter.setLastItemVisible(isLastItemVisible);
                                            chatAdapter.notifyItemInserted(messageList.size() - 1);

                                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, userFTokenKey);

                                            uploadHelper.uploadContent(
                                                    Constant.doc,
                                                    GlobalUri,
                                                    "",
                                                    modelId,
                                                    null,
                                                    null,
                                                    globalName,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    extension,
                                                    receiverUid, "", "", "", "",
                                                    "", "", "", "", "", "", "");

                                            //   uploadtoFirebaseDoc(GlobalUri, extension, globalName, "", modelId);

                                            Constant.dialogLayoutFullScreen.dismiss();

                                        } else {

                                            Constant.getSfFuncion(getApplicationContext());
                                            final String receiverUid = getIntent().getStringExtra("friendUidKey");
                                            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                            final String senderRoom = senderId + receiverUid;
                                            final String receiverRoom = receiverUid + senderId;
                                            Log.d("senderRoom", senderRoom + receiverRoom);
                                            Date d = new Date();
                                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                            String currentDateTimeString = sdf.format(d);


                                            long finalFileSize = getFileSize(globalFile.getPath());

                                            assert modelId != null;
                                            messageModel model;

                                            String uniqDate = Constant.getCurrentDate();
                                            if (uniqueDates.add(uniqDate)) {
                                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                                emojiModels.add(new emojiModel("", ""));
                                                model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), docName.getText().toString(), extension, "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", String.valueOf(getFormattedFileSize(finalFileSize)), globalFile.getName(), "", "", messageBoxMy.getText().toString(), 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);
                                                messageList.add(model);
                                            } else {
                                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                                emojiModels.add(new emojiModel("", ""));
                                                model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), docName.getText().toString(), extension, "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", String.valueOf(getFormattedFileSize(finalFileSize)), globalFile.getName(), "", "", messageBoxMy.getText().toString(), 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);
                                                messageList.add(model);
                                            }

                                            messagemodel2 model2 = new messagemodel2(
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
                                                    model.getUserName(),
                                                    model.getReplytextData(),
                                                    model.getReplyKey(),
                                                    model.getReplyType(),
                                                    model.getReplyOldData(),
                                                    model.getReplyCrtPostion(),
                                                    model.getModelId(),
                                                    model.getReceiverUid(),
                                                    model.getForwaredKey(),
                                                    model.getGroupName(),
                                                    model.getDocSize(),
                                                    model.getFileName(),
                                                    model.getThumbnail(),
                                                    model.getFileNameThumbnail(),
                                                    model.getCaption(),
                                                    model.getNotification(),
                                                    model.getCurrentDate(),
                                                    model.getEmojiModel(),
                                                    model.getEmojiCount(),
                                                    model.getTimestamp(),
                                                    0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                                            );

                                            //TODO : active : 0 = still loading
                                            //TODO : active : 1 = completed


                                            chatAdapter.itemAdd(binding.messageRecView);
                                            chatAdapter.setLastItemVisible(isLastItemVisible);
                                            chatAdapter.notifyItemInserted(messageList.size() - 1);


                                            Constant.dialogLayoutFullScreen.dismiss();

                                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, userFTokenKey);
                                            uploadHelper.uploadContent(
                                                    Constant.doc,
                                                    GlobalUri,
                                                    messageBoxMy.getText().toString().trim(),
                                                    modelId,
                                                    null,
                                                    null,
                                                    globalName,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    extension,
                                                    receiverUid, "", "", "", "",
                                                    "", "", "", "", "", "", "");

                                            //    uploadtoFirebaseDoc(GlobalUri, extension, globalName, messageBoxMy.getText().toString().trim(), modelId);

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
                    } catch (Exception x) {
                        Toast.makeText(mActivity, x.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(chattingScreen.this, "Please select image", Toast.LENGTH_SHORT).show();
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

                Constant.showCustomToast("Upload in process", findViewById(R.id.includedToast), findViewById(R.id.toastText));

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

                        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
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
                            //    image.setImageBitmap(bitmap);
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
                                        final String receiverUid = getIntent().getStringExtra("friendUidKey");
                                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                        final String senderRoom = senderId + receiverUid;
                                        final String receiverRoom = receiverUid + senderId;
                                        Log.d("senderRoom", senderRoom + receiverRoom);
                                        Date d = new Date();
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                        String currentDateTimeString = sdf.format(d);
                                        messageModel model = null;


                                        assert modelId != null;

                                        String uniqDate = Constant.getCurrentDate();
                                        if (uniqueDates.add(uniqDate)) {
                                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                            emojiModels.add(new emojiModel("", ""));
                                            model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", "", 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            messageList.add(model);
                                        } else {
                                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                            emojiModels.add(new emojiModel("", ""));
                                            model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", "", 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            messageList.add(model);
                                        }
                                        messagemodel2 model2 = new messagemodel2(
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
                                                model.getUserName(),
                                                model.getReplytextData(),
                                                model.getReplyKey(),
                                                model.getReplyType(),
                                                model.getReplyOldData(),
                                                model.getReplyCrtPostion(),
                                                model.getModelId(),
                                                model.getReceiverUid(),
                                                model.getForwaredKey(),
                                                model.getGroupName(),
                                                model.getDocSize(),
                                                model.getFileName(),
                                                model.getThumbnail(),
                                                model.getFileNameThumbnail(),
                                                model.getCaption(),
                                                model.getNotification(),
                                                model.getCurrentDate(),
                                                model.getEmojiModel(),
                                                model.getEmojiCount(),
                                                model.getTimestamp(),
                                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                                        );

                                        //TODO : active : 0 = still loading
                                        //TODO : active : 1 = completed

//                                        try {
//                                            new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                        } catch (Exception e) {
//                                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                        }

                                        chatAdapter.itemAdd(binding.messageRecView);
                                        chatAdapter.setLastItemVisible(isLastItemVisible);
                                        UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, userFTokenKey);

                                        uploadHelper.uploadContent(
                                                Constant.camera,
                                                Uri.fromFile(flexible),
                                                "",
                                                modelId,
                                                null,
                                                null,
                                                globalFile.getName(),
                                                null,
                                                null,
                                                null,
                                                null,
                                                getFileExtension(Uri.fromFile(flexible)),
                                                receiverUid, "", "", "", "",
                                                model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(), imageWidthDp, imageHeightDp, aspectRatio);

                                        // uploadtoFirebaseCamera(Uri.fromFile(flexible), "", modelId);
                                        Constant.dialogLayoutFullScreen.dismiss();

                                    } else {


                                        Constant.getSfFuncion(getApplicationContext());
                                        final String receiverUid = getIntent().getStringExtra("friendUidKey");
                                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                        final String senderRoom = senderId + receiverUid;
                                        final String receiverRoom = receiverUid + senderId;
                                        Log.d("senderRoom", senderRoom + receiverRoom);
                                        Date d = new Date();
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                        String currentDateTimeString = sdf.format(d);
                                        messageModel model = null;


                                        assert modelId != null;
                                        String uniqDate = Constant.getCurrentDate();
                                        if (uniqueDates.add(uniqDate)) {
                                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                            emojiModels.add(new emojiModel("", ""));
                                            model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", messageBoxMy.getText().toString(), 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            messageList.add(model);
                                        } else {
                                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                            emojiModels.add(new emojiModel("", ""));
                                            model = new messageModel(senderId, "", currentDateTimeString, globalFile.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", globalFile.getName(), "", "", messageBoxMy.getText().toString(), 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1");
                                            messageList.add(model);
                                        }

                                        messagemodel2 model2 = new messagemodel2(
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
                                                model.getUserName(),
                                                model.getReplytextData(),
                                                model.getReplyKey(),
                                                model.getReplyType(),
                                                model.getReplyOldData(),
                                                model.getReplyCrtPostion(),
                                                model.getModelId(),
                                                model.getReceiverUid(),
                                                model.getForwaredKey(),
                                                model.getGroupName(),
                                                model.getDocSize(),
                                                model.getFileName(),
                                                model.getThumbnail(),
                                                model.getFileNameThumbnail(),
                                                model.getCaption(),
                                                model.getNotification(),
                                                model.getCurrentDate(),
                                                model.getEmojiModel(),
                                                model.getEmojiCount(),
                                                model.getTimestamp(),
                                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                                        );

                                        //TODO : active : 0 = still loading
                                        //TODO : active : 1 = completed

//                                        try {
//                                            new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                        } catch (Exception e) {
//                                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                        }

                                        chatAdapter.itemAdd(binding.messageRecView);
                                        chatAdapter.setLastItemVisible(isLastItemVisible);

                                        Constant.dialogLayoutFullScreen.dismiss();

                                        // String caption = messageBoxMy.getText().toString();
                                        //   uploadtoFirebase(GlobalUri, messageBoxMy.getText().toString().trim());

                                        UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, userFTokenKey);

                                        uploadHelper.uploadContent(
                                                Constant.camera,
                                                Uri.fromFile(flexible),
                                                messageBoxMy.getText().toString().trim(),
                                                modelId,
                                                null,
                                                null,
                                                globalFile.getName(),
                                                null,
                                                null,
                                                null,
                                                null,
                                                getFileExtension(Uri.fromFile(flexible)),
                                                receiverUid, "", "", "", "",
                                                model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(), imageWidthDp, imageHeightDp, aspectRatio);

                                        //  uploadtoFirebaseCamera(Uri.fromFile(flexible), messageBoxMy.getText().toString().trim(), modelId);


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
                    Toast.makeText(chattingScreen.this, "Please select image", Toast.LENGTH_SHORT).show();
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

            LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
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

            String finalName1 = name;
            String finalNumber = number;
            String finalName2 = name;
            sendGrp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.messageBox.setText("");
                    if (messageBoxMy.getText().toString().trim().equals("")) {


                        Constant.getSfFuncion(getApplicationContext());
                        final String receiverUid = getIntent().getStringExtra("friendUidKey");
                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                        final String senderRoom = senderId + receiverUid;
                        final String receiverRoom = receiverUid + senderId;
                        Log.d("senderRoom", senderRoom + receiverRoom);
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        String currentDateTimeString = sdf.format(d);

                        messageModel model;

                        assert modelId != null;
                        String uniqDate = Constant.getCurrentDate();
                        if (uniqueDates.add(uniqDate)) {
                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                            emojiModels.add(new emojiModel("", ""));
                            model = new messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", finalName1, finalNumber, "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", "", "", "", "", 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);
                            messageList.add(model);
                        } else {
                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                            emojiModels.add(new emojiModel("", ""));
                            model = new messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", finalName1, finalNumber, "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", "", "", "", "", 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);
                            messageList.add(model);
                        }

                        messagemodel2 model2 = new messagemodel2(
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
                                model.getUserName(),
                                model.getReplytextData(),
                                model.getReplyKey(),
                                model.getReplyType(),
                                model.getReplyOldData(),
                                model.getReplyCrtPostion(),
                                model.getModelId(),
                                model.getReceiverUid(),
                                model.getForwaredKey(),
                                model.getGroupName(),
                                model.getDocSize(),
                                model.getFileName(),
                                model.getThumbnail(),
                                model.getFileNameThumbnail(),
                                model.getCaption(),
                                model.getNotification(),
                                model.getCurrentDate(),
                                model.getEmojiModel(),
                                model.getEmojiCount(),
                                model.getTimestamp(),
                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                        );

                        //TODO : active : 0 = still loading
                        //TODO : active : 1 = completed

//                        try {
//                            new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                        } catch (Exception e) {
//                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                        }
                        chatAdapter.itemAdd(binding.messageRecView);
                        chatAdapter.setLastItemVisible(isLastItemVisible);
                        chatAdapter.notifyItemInserted(messageList.size() - 1);

                        UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, userFTokenKey);
                        uploadHelper.uploadContent(
                                Constant.contact, // uploadType
                                null, // uri
                                "", // captionText
                                modelId, // modelId
                                null, // savedThumbnail
                                null, // fileThumbName
                                null, // fileName
                                finalName1, // contactName
                                finalNumber, // contactPhone
                                null, // audioTime
                                null, // audioName
                                null, // extension
                                receiverUid // receiverUid
                                , "", "", "", "",
                                "", "", "", "", "", "", "");

                        Constant.dialogLayoutFullScreen.dismiss();

                    } else {

                        Constant.getSfFuncion(getApplicationContext());
                        final String receiverUid = getIntent().getStringExtra("friendUidKey");
                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                        final String senderRoom = senderId + receiverUid;
                        final String receiverRoom = receiverUid + senderId;
                        Log.d("senderRoom", senderRoom + receiverRoom);
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        String currentDateTimeString = sdf.format(d);


                        assert modelId != null;
                        messageModel model;


                        String uniqDate = Constant.getCurrentDate();
                        if (uniqueDates.add(uniqDate)) {
                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                            emojiModels.add(new emojiModel("", ""));
                            model = new messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", finalName1, finalNumber, "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", "", "", "", "", 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);
                            messageList.add(model);
                        } else {
                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                            emojiModels.add(new emojiModel("", ""));
                            model = new messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", finalName1, finalNumber, "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, receiverUid, "", "", "", "", "", "", "", 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);
                            messageList.add(model);
                        }

                        messagemodel2 model2 = new messagemodel2(
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
                                model.getUserName(),
                                model.getReplytextData(),
                                model.getReplyKey(),
                                model.getReplyType(),
                                model.getReplyOldData(),
                                model.getReplyCrtPostion(),
                                model.getModelId(),
                                model.getReceiverUid(),
                                model.getForwaredKey(),
                                model.getGroupName(),
                                model.getDocSize(),
                                model.getFileName(),
                                model.getThumbnail(),
                                model.getFileNameThumbnail(),
                                model.getCaption(),
                                model.getNotification(),
                                model.getCurrentDate(),
                                model.getEmojiModel(),
                                model.getEmojiCount(),
                                model.getTimestamp(),
                                0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
                        );

                        //TODO : active : 0 = still loading
                        //TODO : active : 1 = completed

//                        try {
//                            new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                            Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                        } catch (Exception e) {
//                            Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                        }
                        chatAdapter.itemAdd(binding.messageRecView);
                        chatAdapter.setLastItemVisible(isLastItemVisible);
                        chatAdapter.notifyItemInserted(messageList.size() - 1);

                        UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, userFTokenKey);
                        uploadHelper.uploadContent(
                                Constant.contact, // uploadType
                                null, // uri
                                messageBoxMy.getText().toString().trim(), // captionText
                                modelId, // modelId
                                null, // savedThumbnail
                                null, // fileThumbName
                                null, // fileName
                                finalName1, // contactName
                                finalNumber, // contactPhone
                                null, // audioTime
                                null, // audioName
                                null, // extension
                                receiverUid // receiverUid
                                , "", "", "", "",
                                "", "", "", "", "", "", "");
                        //    uploadtoFirebaseContact(finalName1, finalNumber, messageBoxMy.getText().toString().trim(), modelId);
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

        // Handle single contact selection from bottom sheet
        if (requestCode == PICK_CONTACT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Log.d("ContactSelection", "Single contact picker result received");

            // Handle custom contact picker results
            ArrayList<WhatsAppLikeContactPicker.ContactInfo> selectedContacts = data.getParcelableArrayListExtra(WhatsAppLikeContactPicker.EXTRA_SELECTED_CONTACTS);
            @SuppressWarnings("unchecked")
            Map<Integer, String> contactCaptions = (Map<Integer, String>) data.getSerializableExtra("contact_captions");

            if (selectedContacts != null && !selectedContacts.isEmpty()) {
                Log.d("ContactSelection", "Selected contacts: " + selectedContacts.size());

                // Use the integrated Firebase upload method
                String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                String receiverUid = getIntent().getStringExtra("friendUidKey");

                // Upload contacts directly to Firebase using the integrated method
                WhatsAppLikeContactPicker.uploadContactsToFirebase(
                        mContext,
                        selectedContacts,
                        new HashMap<>(),
                        senderId,
                        receiverUid,
                        userFTokenKey,
                        false, // isGroupChat
                        null   // grpIdKey (not needed for individual chat)
                );

                // Update UI after contact upload
                runOnUiThread(() -> {
                    chatAdapter.setLastItemVisible(true); // Ensure the last item is visible
                    chatAdapter.itemAdd(binding.messageRecView);
                    binding.messageBox.setEnabled(true);
                });
            }
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

                // Use the integrated Firebase upload method
                String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                String receiverUid = getIntent().getStringExtra("friendUidKey");

                // Upload contacts directly to Firebase using the integrated method
                WhatsAppLikeContactPicker.uploadContactsToFirebase(
                        mContext,
                        selectedContacts,
                        new HashMap<>(),
                        senderId,
                        receiverUid,
                        userFTokenKey,
                        false, // isGroupChat
                        null   // grpIdKey (not needed for individual chat)
                );

                // Update UI after contact upload
                runOnUiThread(() -> {
                    chatAdapter.setLastItemVisible(true); // Ensure the last item is visible
                    chatAdapter.itemAdd(binding.messageRecView);
                    binding.messageBox.setEnabled(true);
                });

                Log.d("MultiContactSelection", "Contacts uploaded to Firebase successfully");

            } else {
                Log.d("MultiContactSelection", "No contacts selected");
            }
        }


        // for picking contact for existing contact
        if (requestCode == 7185 && resultCode == RESULT_OK && data != null) {

            handleContact(data.getData());

        }

        // Handle multi-video selection
        if (requestCode == PICK_VIDEO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Handle WhatsApp-like video picker results
            ArrayList<Uri> newSelectedVideos = data.getParcelableArrayListExtra(WhatsAppLikeVideoPicker.EXTRA_SELECTED_VIDEOS);
            if (newSelectedVideos != null && !newSelectedVideos.isEmpty()) {
                // Clear existing selection and add new videos
                selectedVideoUris.clear();
                selectedVideoFiles.clear();
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
            }
        }

        // Handle multi-document selection
        if (requestCode == PICK_DOCUMENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Check current selection count
            int currentCount = selectedDocumentUris.size();
            int maxDocuments = 5;

            // Handle system document picker results
            if (data.getClipData() != null) {
                // Multiple documents selected
                int count = data.getClipData().getItemCount();
                int availableSlots = maxDocuments - currentCount;

                if (count > availableSlots) {
                    Toast.makeText(this, "You can only select " + availableSlots + " more documents (max 5 total)", Toast.LENGTH_SHORT).show();
                    count = availableSlots;
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

        // Setup image counter visibility
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            Log.d("DocumentCounter", "showMultiDocumentPreviewDialog - Setting imageCounter to VISIBLE with text: 1 / " + selectedDocumentUris.size());
            imageCounter.setVisibility(View.VISIBLE);
            imageCounter.setText("1 / " + selectedDocumentUris.size());
            imageCounter.bringToFront(); // Bring to front
            imageCounter.invalidate(); // Force redraw
            Log.d("DocumentCounter", "showMultiDocumentPreviewDialog - imageCounter visibility after set: " + imageCounter.getVisibility());
            Log.d("DocumentCounter", "showMultiDocumentPreviewDialog - imageCounter alpha: " + imageCounter.getAlpha());
            Log.d("DocumentCounter", "showMultiDocumentPreviewDialog - imageCounter background: " + (imageCounter.getBackground() != null));
        } else {
            Log.d("DocumentCounter", "showMultiDocumentPreviewDialog - imageCounter is NULL!");
        }

        // Setup document preview
        setupDocumentPreview();

        // Force imageCounter visibility after a short delay to ensure layout is fully inflated
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            TextView delayedImageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
            if (delayedImageCounter != null) {
                Log.d("DocumentCounter", "Delayed setup - imageCounter found, setting to VISIBLE");
                delayedImageCounter.setVisibility(View.VISIBLE);
                delayedImageCounter.setText("1 / " + selectedDocumentUris.size());
                delayedImageCounter.bringToFront();
                delayedImageCounter.invalidate();

                // Try to force it to be on top by setting elevation
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    delayedImageCounter.setElevation(1000f); // High elevation to ensure it's on top
                }

                // Try to set a bright background color to make it more visible
                delayedImageCounter.setBackgroundColor(0xFF000000); // Black background
                delayedImageCounter.setTextColor(0xFFFFFFFF); // White text

                Log.d("DocumentCounter", "Delayed setup - imageCounter visibility after set: " + delayedImageCounter.getVisibility());
                Log.d("DocumentCounter", "Delayed setup - imageCounter alpha: " + delayedImageCounter.getAlpha());
                Log.d("DocumentCounter", "Delayed setup - imageCounter elevation: " + delayedImageCounter.getElevation());
                Log.d("DocumentCounter", "Delayed setup - imageCounter text: '" + delayedImageCounter.getText() + "'");
            } else {
                Log.d("DocumentCounter", "Delayed setup - imageCounter is NULL!");
            }
        }, 200); // 200ms delay
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

        ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
        if (viewPager2 != null) {
            viewPager2.setVisibility(View.GONE);
        }

        // Show document container with downloadCtrl design
        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            downloadCtrl.setVisibility(View.VISIBLE);
            setupDocumentContainerWithSlider(downloadCtrl);
        }

        // Setup ViewPager indicator
        setupViewPagerIndicator();

        // Setup document counter at top
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            Log.d("DocumentCounter", "setupDocumentPreview - Setting imageCounter to VISIBLE with text: 1 / " + selectedDocumentUris.size());
            imageCounter.setVisibility(View.VISIBLE);
            imageCounter.setText("1 / " + selectedDocumentUris.size());
            imageCounter.setBackgroundResource(R.drawable.black_background_hover);
            imageCounter.setPadding(16, 8, 16, 8);
            imageCounter.bringToFront(); // Bring to front
            imageCounter.invalidate(); // Force redraw
            Log.d("DocumentCounter", "setupDocumentPreview - imageCounter visibility after set: " + imageCounter.getVisibility());
            Log.d("DocumentCounter", "setupDocumentPreview - imageCounter alpha: " + imageCounter.getAlpha());
            Log.d("DocumentCounter", "setupDocumentPreview - imageCounter background: " + (imageCounter.getBackground() != null));
            Log.d("DocumentCounter", "setupDocumentPreview - imageCounter text: '" + imageCounter.getText() + "'");
        } else {
            Log.d("DocumentCounter", "setupDocumentPreview - imageCounter is NULL!");
        }

        // Setup document information display with a small delay to ensure ViewPager2 is ready
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            setupDocumentInfoDisplay();
        }, 100);

        // Setup navigation arrows


        // Setup back button functionality
        setupBackButton();

        // Setup bottom section
        setupDocumentBottomSection();
    }

    private void setupDocumentInfoDisplay() {
        Log.d("DocumentInfo", "=== SETUP DOCUMENT INFO DISPLAY ===");
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
            Log.d("DocumentInfo", "File exists: " + firstDocument.exists());
            Log.d("DocumentInfo", "File path: " + firstDocument.getAbsolutePath());

            docName.setText(fileName);
            size.setText(getFormattedFileSize(fileSize));

            Log.d("DocumentInfo", "Displaying document: " + fileName + " (" + getFormattedFileSize(fileSize) + ")");

            // Make sure the views are visible
            docName.setVisibility(View.VISIBLE);
            size.setVisibility(View.VISIBLE);

            Log.d("DocumentInfo", "Document info views set to VISIBLE");
            Log.d("DocumentInfo", "docName visibility: " + docName.getVisibility());
            Log.d("DocumentInfo", "size visibility: " + size.getVisibility());
            Log.d("DocumentInfo", "docName text: '" + docName.getText() + "'");
            Log.d("DocumentInfo", "size text: '" + size.getText() + "'");
        } else {
            Log.e("DocumentInfo", "Document info views not found or no documents selected");
            if (docName == null) Log.e("DocumentInfo", "docName is null");
            if (size == null) Log.e("DocumentInfo", "size is null");
            if (selectedDocumentFiles.isEmpty())
                Log.e("DocumentInfo", "selectedDocumentFiles is empty");
        }
        Log.d("DocumentInfo", "=== END SETUP DOCUMENT INFO DISPLAY ===");
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

    private void setupBackButton() {
        // Try to find back button in the layout
        View backButton = Constant.dialogLayoutFullScreen.findViewById(R.id.backButton);
        if (backButton == null) {
            // Try other possible back button IDs
            backButton = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
        }


        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                Constant.dialogLayoutFullScreen.dismiss();
            });
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
                    Log.d("DocumentCounter", "ViewPager2 page change - updating imageCounter text to: " + (position + 1) + " / " + selectedDocumentUris.size());
                    imageCounter.setText((position + 1) + " / " + selectedDocumentUris.size());
                    imageCounter.setVisibility(View.VISIBLE); // Ensure it's always visible
                    Log.d("DocumentCounter", "ViewPager2 page change - imageCounter visibility after set: " + imageCounter.getVisibility());
                }

                // Update caption EditText with current document's caption
                EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                if (messageBoxMy != null) {
                    // Use class variable for caption persistence
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

                // Update document info display
                updateDocumentInfoDisplay(position);

                // Update horizontal RecyclerView selection


                // Update ViewPager indicator
                updateViewPagerIndicator(position);
            }
        });

        downloadCtrl.addView(documentViewPager);

        // Setup document info display immediately after ViewPager2 is added
        setupDocumentInfoDisplay();
    }

    private ViewPager2 findViewPagerInDownloadCtrl() {
        Log.d("ViewPagerFinder", "Starting ViewPager2 search...");

        // First search in downloadCtrl (where the dynamic ViewPager2 is added)
        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            Log.d("ViewPagerFinder", "downloadCtrl found, child count: " + downloadCtrl.getChildCount());
            if (downloadCtrl.getChildCount() > 0) {
                // Search through all children to find ViewPager2
                for (int i = 0; i < downloadCtrl.getChildCount(); i++) {
                    View child = downloadCtrl.getChildAt(i);
                    Log.d("ViewPagerFinder", "Child " + i + " type: " + child.getClass().getSimpleName());
                    if (child instanceof ViewPager2) {
                        Log.d("ViewPagerFinder", "Found ViewPager2 at index: " + i);
                        return (ViewPager2) child;
                    }
                }
                Log.e("ViewPagerFinder", "ViewPager2 not found in downloadCtrl, child count: " + downloadCtrl.getChildCount());
            } else {
                Log.e("ViewPagerFinder", "downloadCtrl has no children");
            }
        } else {
            Log.e("ViewPagerFinder", "downloadCtrl is null");
        }

        // If not found in downloadCtrl, try to find by ID in the dialog layout
        ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
        if (viewPager2 != null) {
            Log.d("ViewPagerFinder", "Found ViewPager2 by ID");
            return viewPager2;
        } else {
            Log.d("ViewPagerFinder", "ViewPager2 not found by ID");
        }

        Log.e("ViewPagerFinder", "ViewPager2 not found anywhere!");
        return null;
    }

    private void setupHorizontalDocumentThumbnails(RecyclerView horizontalRecyclerView) {
        Log.d("ThumbnailSetup", "Setting up horizontal thumbnails with " + selectedDocumentUris.size() + " documents");

        // Setup horizontal layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(layoutManager);

        // Create adapter for horizontal thumbnails
        HorizontalDocumentThumbnailAdapter adapter = new HorizontalDocumentThumbnailAdapter(mContext, selectedDocumentUris, new HorizontalDocumentThumbnailAdapter.OnThumbnailClickListener() {
            @Override
            public void onThumbnailClick(int position) {
                Log.d("ThumbnailClick", "Thumbnail clicked at position: " + position);
                // Navigate to selected document in the document ViewPager2
                ViewPager2 documentViewPager = findViewPagerInDownloadCtrl();
                if (documentViewPager != null) {
                    Log.d("ThumbnailClick", "Navigating to document position: " + position);
                    documentViewPager.setCurrentItem(position, true);
                } else {
                    Log.e("ThumbnailClick", "Document ViewPager2 not found!");
                }
            }
        });
        horizontalRecyclerView.setAdapter(adapter);

        Log.d("ThumbnailSetup", "Adapter set, RecyclerView visibility: " + horizontalRecyclerView.getVisibility());
        Log.d("ThumbnailSetup", "RecyclerView height: " + horizontalRecyclerView.getHeight() + ", width: " + horizontalRecyclerView.getWidth());
    }

    private LinearLayout indicatorContainer;

    private void setupViewPagerIndicator() {
        Log.d("ViewPagerIndicator", "Setting up indicator for " + selectedDocumentUris.size() + " documents");

        // Create a simple dot indicator
        indicatorContainer = new LinearLayout(mContext);
        indicatorContainer.setOrientation(LinearLayout.HORIZONTAL);
        indicatorContainer.setGravity(android.view.Gravity.CENTER);
        indicatorContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        indicatorContainer.setPadding(16, 8, 16, 8);
        indicatorContainer.setBackgroundColor(0x80000000); // Semi-transparent background for debugging

        // Add dots for each document
        for (int i = 0; i < selectedDocumentUris.size(); i++) {
            View dot = new View(mContext);
            LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(16, 16);
            dotParams.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(dotParams);
            dot.setBackgroundResource(R.drawable.circle_indicator_dot);
            dot.setAlpha(i == 0 ? 1.0f : 0.3f);
            dot.setTag(i);
            indicatorContainer.addView(dot);
        }

        // Add indicator to the layout - add it after the ViewPager2
        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            Log.d("ViewPagerIndicator", "Adding indicator to downloadCtrl, current child count: " + downloadCtrl.getChildCount());
            // Add indicator after the ViewPager2 (index 1)
            if (downloadCtrl.getChildCount() > 0) {
                downloadCtrl.addView(indicatorContainer, 1);
            } else {
                downloadCtrl.addView(indicatorContainer);
            }
            Log.d("ViewPagerIndicator", "Indicator added, new child count: " + downloadCtrl.getChildCount());
        } else {
            Log.e("ViewPagerIndicator", "downloadCtrl is null!");
        }
    }

    private void updateViewPagerIndicator(int currentPosition) {
        if (indicatorContainer != null) {
            for (int i = 0; i < indicatorContainer.getChildCount(); i++) {
                View dot = indicatorContainer.getChildAt(i);
                if (dot != null) {
                    dot.setAlpha(i == currentPosition ? 1.0f : 0.3f);
                }
            }
        }
    }

    private void setupDocumentBottomSection() {
        // Get bottom section views
        LinearLayout editLyt = Constant.dialogLayoutFullScreen.findViewById(R.id.editLyt);
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);

        if (editLyt != null) {
            editLyt.setVisibility(View.VISIBLE);
        }


        if (messageBoxMy != null) {
            messageBoxMy.setHint("Add Caption");
            messageBoxMy.setVisibility(View.VISIBLE);

        }

        // Setup send button
        if (sendGrp != null) {
            sendGrp.setOnClickListener(v -> {
                Log.d("SendButton", "Send button clicked for documents!");
                Log.d("SendButton", "Current caption: '" + currentCaption + "'");
                // Send all documents
                sendMultipleDocuments();

                // Clear selections
                selectedDocumentUris.clear();

                // Clear files and dismiss dialog after sending
                selectedDocumentFiles.clear();
                if (Constant.dialogLayoutFullScreen != null) {
                    Constant.dialogLayoutFullScreen.dismiss();
                    Log.d("SendButton", "Dialog dismissed after sending documents");
                }
            });
        }
    }

    private String getDocumentFileName(Uri uri) {
        String fileName = null;
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (index != -1) {
                fileName = cursor.getString(index);
            }
            cursor.close();
        }
        return fileName != null ? fileName : "Unknown Document";
    }

    private String getDocumentFileSize(Uri uri) {
        try {
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (sizeIndex != -1) {
                    long size = cursor.getLong(sizeIndex);
                    cursor.close();
                    return formatFileSize(size);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown Size";
    }

    private String getDocumentFileType(Uri uri) {
        try {
            String mimeType = this.getContentResolver().getType(uri);
            if (mimeType != null) {
                // Images
                if (mimeType.startsWith("image/jpeg") || mimeType.startsWith("image/jpg"))
                    return "jpg";
                if (mimeType.startsWith("image/png")) return "png";
                if (mimeType.startsWith("image/gif")) return "gif";
                if (mimeType.startsWith("image/webp")) return "webp";
                if (mimeType.startsWith("image/bmp")) return "bmp";
                if (mimeType.startsWith("image/")) return "img";

                // Videos
                if (mimeType.startsWith("video/mp4")) return "mp4";
                if (mimeType.startsWith("video/avi")) return "avi";
                if (mimeType.startsWith("video/mov")) return "mov";
                if (mimeType.startsWith("video/wmv")) return "wmv";
                if (mimeType.startsWith("video/mkv")) return "mkv";
                if (mimeType.startsWith("video/")) return "vid";

                // Audio
                if (mimeType.startsWith("audio/mp3")) return "mp3";
                if (mimeType.startsWith("audio/wav")) return "wav";
                if (mimeType.startsWith("audio/aac")) return "aac";
                if (mimeType.startsWith("audio/ogg")) return "ogg";
                if (mimeType.startsWith("audio/")) return "aud";

                // Documents
                if (mimeType.startsWith("application/pdf")) return "pdf";
                if (mimeType.startsWith("application/msword") || mimeType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml"))
                    return "doc";
                if (mimeType.startsWith("application/vnd.ms-excel") || mimeType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml"))
                    return "xls";
                if (mimeType.startsWith("application/vnd.ms-powerpoint") || mimeType.startsWith("application/vnd.openxmlformats-officedocument.presentationml"))
                    return "ppt";
                if (mimeType.startsWith("text/plain")) return "txt";
                if (mimeType.startsWith("text/")) return "txt";
                if (mimeType.startsWith("application/rtf")) return "rtf";
                if (mimeType.startsWith("application/vnd.oasis.opendocument")) return "odt";

                // Archives
                if (mimeType.startsWith("application/zip")) return "zip";
                if (mimeType.startsWith("application/x-rar")) return "rar";
                if (mimeType.startsWith("application/x-7z")) return "7z";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "file";
    }

    private void setDocumentIcon(ImageView iconView, String type) {
        switch (type.toLowerCase()) {
            // Images - use gallery icon from chattingScreen
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "webp":
            case "bmp":
            case "img":
                iconView.setImageResource(R.drawable.gallery);
                break;
            // Videos - use videopng icon from chattingScreen
            case "mp4":
            case "avi":
            case "mov":
            case "wmv":
            case "mkv":
            case "vid":
                iconView.setImageResource(R.drawable.videopng);
                break;
            // Audio - use contact icon for audio files
            case "mp3":
            case "wav":
            case "aac":
            case "ogg":
            case "aud":
                iconView.setImageResource(R.drawable.contact);
                break;
            // Documents - use documentsvg icon from chattingScreen
            case "pdf":
            case "doc":
            case "docx":
            case "xls":
            case "xlsx":
            case "ppt":
            case "pptx":
            case "txt":
            case "zip":
            case "rar":
            case "7z":
                iconView.setImageResource(R.drawable.documentsvg);
                break;
            default:
                iconView.setImageResource(R.drawable.documentsvg);
                break;
        }
    }

    private String formatFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private void sendMultipleDocuments() {
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

    private void sendSingleDocument(Uri documentUri, File documentFile, String caption) {
        // Generate unique model ID for each document
        String modelId = database.getReference().push().getKey();

        try {
            // Verify file exists
            if (documentFile == null || !documentFile.exists()) {
                Log.e("DocumentSend", "Document file does not exist: " + (documentFile != null ? documentFile.getAbsolutePath() : "null"));
                Toast.makeText(mContext, "Document file not found", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get document name and size
            String fileName = documentFile.getName();
            long fileSize = documentFile.length();

            Log.d("DocumentSend", "Sending document: " + fileName + " (size: " + fileSize + " bytes)");

            // Get current time
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);

            // Get sender info
            Constant.getSfFuncion(getApplicationContext());
            final String receiverUid = getIntent().getStringExtra("friendUidKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String senderRoom = senderId + receiverUid;
            final String receiverRoom = receiverUid + senderId;

            String uniqDate = Constant.getCurrentDate();
            messageModel model;

            // Create emoji models list
            ArrayList<emojiModel> emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));

            if (uniqueDates.add(uniqDate)) {
                model = new messageModel(senderId, caption, currentDateTimeString, documentFile.toString(),
                        Constant.doc, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""),
                        "", "", "", "", "", modelId, receiverUid, "", "", String.valueOf(fileSize),
                        fileName, "", "", caption, 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);
            } else {
                model = new messageModel(senderId, caption, currentDateTimeString, documentFile.toString(),
                        Constant.doc, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""),
                        "", "", "", "", "", modelId, receiverUid, "", "", String.valueOf(fileSize),
                        fileName, "", "", caption, 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1", null);
            }

            // Store message in SQLite pending table before upload
            try {
                new DatabaseHelper(mContext).insertPendingMessage(model);
                Log.d("PendingMessages", "Document message stored in pending table: " + model.getModelId());
            } catch (Exception e) {
                Log.e("PendingMessages", "Error storing document message in pending table: " + e.getMessage(), e);
            }

            messageList.add(model);

            // Update UI
            runOnUiThread(() -> {
                chatAdapter.setLastItemVisible(true); // Ensure the last item is visible
                chatAdapter.itemAdd(binding.messageRecView);
                binding.messageBox.setEnabled(true);
            });

            // Upload document
            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, documentFile, null, senderId, userFTokenKey);
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
                    getFileExtension(fileName),
                    receiverUid, "", "", "", "",
                    "", "", "", "", "", "", "");

        } catch (Exception e) {
            Log.e("DocumentSend", "Error sending document: " + e.getMessage(), e);
            Toast.makeText(mContext, "Error sending document", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            Log.d("FileExtension", "Extracted extension: " + extension + " from file: " + fileName);
            return extension;
        }
        Log.d("FileExtension", "No extension found for file: " + fileName);
        return "";
    }

    // Contact multi-selection methods
    private void showContactSelectionOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Contact Option");
        builder.setItems(new String[]{"Single Contact", "Multiple Contacts (Up to 50)"}, (dialog, which) -> {
            if (which == 0) {
                // Single contact selection
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                SwipeNavigationHelper.startActivityForResultWithSwipe(chattingScreen.this, intent, REQUEST_CODE_PICK_CONTACT);
            } else {
                // Multiple contact selection
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                SwipeNavigationHelper.startActivityForResultWithSwipe(chattingScreen.this, Intent.createChooser(intent, "Select Contacts"), PICK_CONTACT_MULTI_REQUEST_CODE);
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
        Log.d("MultiContactPreview", "=== SHOWING MULTI-CONTACT PREVIEW DIALOG ===");
        Log.d("MultiContactPreview", "selectedContactInfos.size(): " + selectedContactInfos.size());
        Log.d("MultiContactPreview", "selectedContactUris.size(): " + selectedContactUris.size());

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

        Log.d("MultiContactPreview", "Constant.dialogLayoutFullScreen: " + (Constant.dialogLayoutFullScreen != null ? "not null" : "null"));

        if (Constant.dialogLayoutFullScreen != null) {
            Log.d("MultiContactPreview", "Calling setupContactPreview()");

            // Add a small delay to ensure dialog is fully rendered
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                setupContactPreview();
            }, 100);
        } else {
            Log.e("MultiContactPreview", "dialogLayoutFullScreen is null!");
        }

        // Additional debugging
        Log.d("MultiContactPreview", "Dialog should now be visible");
    }

    private void setupContactPreview() {
        Log.d("ContactPreview", "=== SETUP CONTACT PREVIEW ===");
        Log.d("ContactPreview", "selectedContactInfos.size(): " + selectedContactInfos.size());
        Log.d("ContactPreview", "selectedContactUris.size(): " + selectedContactUris.size());

        if (selectedContactInfos.isEmpty()) {
            Log.e("ContactPreview", "No contacts selected, returning");
            Toast.makeText(mContext, "No contacts to preview", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("ContactPreview", "Contact preview setup starting with " + selectedContactInfos.size() + " contacts");
        for (int i = 0; i < selectedContactInfos.size(); i++) {
            chattingScreen.ContactInfo contact = selectedContactInfos.get(i);
            Log.d("ContactPreview", "Contact " + i + ": " + contact.name + " - " + contact.phoneNumber);
        }

        Log.d("ContactPreview", "Setting up contact preview with " + selectedContactInfos.size() + " contacts");

        // Hide image, video, and document views
        ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        singleImageView.setVisibility(View.GONE);

        ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
        if (viewPager2 != null) {
            viewPager2.setVisibility(View.GONE);
        }

        // Setup horizontal gallery preview (same as image preview)
        setupHorizontalContactGalleryPreview();
    }

    private void setupHorizontalContactGalleryPreview() {
        Log.d("HorizontalContactGallery", "Setting up horizontal contact gallery with " + selectedContactInfos.size() + " contacts");

        // Setup main contact preview ViewPager2
        ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        Log.d("HorizontalContactGallery", "mainImagePreview found: " + (mainImagePreview != null ? "YES" : "NO"));
        if (mainImagePreview != null) {
            mainImagePreview.setVisibility(View.VISIBLE);
            Log.d("HorizontalContactGallery", "mainImagePreview set to VISIBLE");


            // Show the image counter
            TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
            if (imageCounter != null) {
                imageCounter.setVisibility(View.VISIBLE);
                Log.d("HorizontalContactGallery", "imageCounter set to VISIBLE");
            }

            // Setup adapter for main preview
            ContactPreviewAdapter mainAdapter = new ContactPreviewAdapter(mContext, selectedContactInfos);
            mainImagePreview.setAdapter(mainAdapter);
            Log.d("HorizontalContactGallery", "ContactPreviewAdapter set with " + selectedContactInfos.size() + " contacts");

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
                        // Use class variable for caption persistence
                        Log.d("ContactPageChange", "Switched to position " + position + ", caption: '" + currentCaption + "'");

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
                }
            });
        }


        // Setup caption functionality
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null) {
            messageBoxMy.setVisibility(View.VISIBLE);
            messageBoxMy.setHint("Add a caption...");

            // Setup TextWatcher for caption
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
        // Clear existing views
        downloadCtrl.removeAllViews();

        // Create ViewPager2 for contact sliding
        ViewPager2 contactViewPager = new ViewPager2(mContext);
        contactViewPager.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Create adapter for contact preview - placeholder for now
        // ContactPreviewAdapter adapter = new ContactPreviewAdapter(mContext, selectedContactInfos);
        // contactViewPager.setAdapter(adapter);

        // Setup page change callback
        contactViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Update counter
                TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
                if (imageCounter != null) {
                    imageCounter.setText((position + 1) + " / " + selectedContactInfos.size());
                    imageCounter.setVisibility(View.VISIBLE);
                }

                // Update caption EditText with current contact's caption
                EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                if (messageBoxMy != null) {
                    // Use class variable for caption persistence
                    // Removed individual caption logic
                    if (currentCaption != null) {
                        messageBoxMy.setText(currentCaption);
                        messageBoxMy.setSelection(messageBoxMy.getText().length());
                    } else {
                        messageBoxMy.setText("");
                    }
                    // Removed individual caption logic
                }

                // Update contact info display
                updateContactInfoDisplay(position);

                // Update horizontal RecyclerView selection - placeholder for now
                // RecyclerView horizontalRecyclerView = Constant.dialogLayoutFullScreen.findViewById(R.id.horizontalRecyclerView);
                // if (horizontalRecyclerView != null && horizontalRecyclerView.getAdapter() instanceof HorizontalContactThumbnailAdapter) {
                //     ((HorizontalContactThumbnailAdapter) horizontalRecyclerView.getAdapter()).setSelectedPosition(position);
                // }

                // Update ViewPager indicator
                updateViewPagerIndicator(position);
            }
        });

        downloadCtrl.addView(contactViewPager);

        // Setup contact info display immediately after ViewPager2 is added
        setupContactInfoDisplay();
    }

    private void setupContactInfoDisplay() {
        Log.d("ContactInfo", "=== SETUP CONTACT INFO DISPLAY ===");
        Log.d("ContactInfo", "selectedContactInfos size: " + selectedContactInfos.size());

        // Get contact info views
        TextView docName = Constant.dialogLayoutFullScreen.findViewById(R.id.docName);
        TextView size = Constant.dialogLayoutFullScreen.findViewById(R.id.size);

        if (docName != null && size != null && !selectedContactInfos.isEmpty()) {
            // Show contact info for the first contact
            ContactInfo firstContact = selectedContactInfos.get(0);
            docName.setText(firstContact.name);
            size.setText(firstContact.phoneNumber != null ? firstContact.phoneNumber : "No phone");

            // Make sure the views are visible
            docName.setVisibility(View.VISIBLE);
            size.setVisibility(View.VISIBLE);

            Log.d("ContactInfo", "Displaying contact: " + firstContact.name + " - " + firstContact.phoneNumber);
        } else {
            Log.e("ContactInfo", "Contact info views not found or no contacts selected");
        }
    }

    private void updateContactInfoDisplay(int position) {
        Log.d("ContactInfo", "Updating contact info for position: " + position);

        TextView docName = Constant.dialogLayoutFullScreen.findViewById(R.id.docName);
        TextView size = Constant.dialogLayoutFullScreen.findViewById(R.id.size);

        if (docName != null && size != null && position < selectedContactInfos.size()) {
            ContactInfo contact = selectedContactInfos.get(position);
            docName.setText(contact.name);
            size.setText(contact.phoneNumber != null ? contact.phoneNumber : "No phone");
        }
    }

    private void setupContactBottomSection() {
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);

        // Make sure send button is visible
        if (sendGrp != null) {
            sendGrp.setVisibility(View.VISIBLE);
        }

        // Make sure message box is visible
        if (messageBoxMy != null) {
            messageBoxMy.setVisibility(View.VISIBLE);
            messageBoxMy.setHint("Add caption for this contact...");
        }

        // Setup theme color
        try {
            Constant.getSfFuncion(getApplicationContext());
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themColor));
            if (sendGrp != null) {
                sendGrp.setBackgroundTintList(tintList);
            }
        } catch (Exception i) {
            // Handle exception
        }

        // Pre-fill message box
        if (messageBoxMy != null && !binding.messageBox.getText().toString().isEmpty()) {
            messageBoxMy.setText(binding.messageBox.getText().toString());
        }


        sendGrp.setOnClickListener(v -> {
            Log.d("SendButton", "Send button clicked for contacts!");
            Log.d("SendButton", "Current caption: '" + currentCaption + "'");
            // Send multiple contacts
            sendMultipleContacts();

            binding.messageBox.setText("");
            if (Constant.dialogLayoutFullScreen != null) {
                Constant.dialogLayoutFullScreen.dismiss();
                Log.d("SendButton", "Dialog dismissed after sending contacts");
            }
        });
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
            final String receiverUid = getIntent().getStringExtra("friendUidKey");
            final String senderRoom = senderId + receiverUid;

            String uniqDate = Constant.getCurrentDate();
            messageModel model;

            if (uniqueDates.add(uniqDate)) {
                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                emojiModels.add(new emojiModel("", ""));
                model = new messageModel(senderId, caption, currentDateTimeString, "",
                        Constant.contact, "", contactInfo.name, contactInfo.phoneNumber, "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", "", "", "", "", 1, uniqDate, emojiModels, "",
                        Constant.getCurrentTimestamp(), contactInfo.email, "", "", "1", null);
            } else {
                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                emojiModels.add(new emojiModel("", ""));
                model = new messageModel(senderId, caption, currentDateTimeString, "",
                        Constant.contact, "", contactInfo.name, contactInfo.phoneNumber, "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", "", "", "", "", 1, ":" + uniqDate, emojiModels, "",
                        Constant.getCurrentTimestamp(), contactInfo.email, "", "", "1", null);
            }

            // Store message in SQLite pending table before upload
            try {
                new DatabaseHelper(mContext).insertPendingMessage(model);
                Log.d("PendingMessages", "Contact message stored in pending table: " + model.getModelId());
            } catch (Exception e) {
                Log.e("PendingMessages", "Error storing contact message in pending table: " + e.getMessage(), e);
            }

            messageList.add(model);

            // Update UI
            runOnUiThread(() -> {
                chatAdapter.setLastItemVisible(true);
                chatAdapter.itemAdd(binding.messageRecView);
                binding.messageBox.setEnabled(true);
            });

            // Upload contact using UploadChatHelper for individual chats
            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, null, null, senderId, userFTokenKey);
            uploadHelper.uploadContent(
                    Constant.contact,     // uploadType
                    null,                 // uri
                    caption,              // captionText
                    modelId,              // modelId
                    null,                 // savedThumbnail
                    null,                 // fileThumbName
                    null,                 // fileName
                    contactInfo.name,     // contactName
                    contactInfo.phoneNumber, // contactPhone
                    null,                 // audioTime
                    null,                 // audioName
                    null,                 // extension
                    receiverUid,          // receiverUid
                    "",                   // replyCrtPostion
                    "",                   // replyKey
                    "",                   // replyOldData
                    "",                   // replyType
                    "",                   // replytextData
                    "",                   // replydataType
                    "",                   // replyfilename
                    "",                   // forwaredKey
                    "",                   // imageWidthDp
                    "",                   // imageHeightDp
                    ""                    // aspectRatio
            );

        } catch (Exception e) {
            Log.e("ContactSend", "Error sending contact: " + e.getMessage(), e);
            Toast.makeText(mContext, "Error sending contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupHorizontalContactThumbnails(RecyclerView horizontalRecyclerView) {
        // Placeholder - will be implemented
        Log.d("ContactThumbnails", "setupHorizontalContactThumbnails called");
    }

    private File getDocumentFileFromUri(Uri uri) {
        try {
            // Get original filename
            String fileName = getDocumentFileName(uri);
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


    @OptIn(markerClass = UnstableApi.class)
    private void showVideoPreviewDialog(File videoFile, File thumbnailFile, String fileThumbName, String fileName) {
        Log.d("VIDEO_DIALOG_SHOW", "chattingScreen.showVideoPreviewDialog called for: " + fileName);
        Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);
        Log.d("VIDEO_DIALOG_SHOW", "About to show dialog");
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

        PlayerView video = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        LinearLayout arrowback2 = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);

        video.setVisibility(View.VISIBLE);
        arrowback2.setVisibility(View.GONE);


        video.setUseController(true);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(mContext);
        TrackSelectionParameters trackSelectionParameters = trackSelector.getParameters().
                buildUpon().setForceLowestBitrate(true).build();
        trackSelector.setParameters((DefaultTrackSelector.Parameters) trackSelectionParameters);

        playerPreview = new ExoPlayer.Builder(mContext).setTrackSelector(trackSelector).build();
        playerPreview.setMediaItem(MediaItem.fromUri(Uri.fromFile(videoFile)));
        playerPreview.prepare();
        playerPreview.setPlayWhenReady(false);

        video.setPlayer(playerPreview);
        Log.d("VIDEO_PLAYER_INIT", "PlayerView bound. File: " + fileName + ", uri=" + Uri.fromFile(videoFile));

        ImageView play = video.findViewById(R.id.play);
        ImageView pause = video.findViewById(R.id.pause);
        ImageView reply = video.findViewById(R.id.reply);
        ImageView forward = video.findViewById(R.id.forward);
        LinearLayout backarrow = video.findViewById(R.id.backarrow);
        LinearLayout rsizeLyt = video.findViewById(R.id.rsizeLyt);
        TextView resizeText = video.findViewById(R.id.resizeText);
        ImageView resizeImg = video.findViewById(R.id.resizeImg);
        // Remove or comment out these lines if you don't need references to them
        // SeekBar brightness = video.findViewById(R.id.brightness);
        // SeekBar volume = video.findViewById(R.id.volume);
        TextView nameTitle = video.findViewById(R.id.nameTitle);
        TextView totalTime = video.findViewById(R.id.totalTime);
        TextView startTime = video.findViewById(R.id.startTime);
        DefaultTimeBar exoProgress = video.findViewById(R.id.exoProgress);

        if (video.getVisibility() == View.VISIBLE) {
            if (backarrow != null) {
                backarrow.setVisibility(View.VISIBLE);
                backarrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constant.dialogLayoutFullScreen.dismiss();
                    }
                });
            }
            if (nameTitle != null) {
                nameTitle.setText(fileName);
            }
        }


        rsizeLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (resizeModeIndex) {
                    case 0:
                        video.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                        resizeText.setText("Fit Mode");
                        break;
                    case 1:
                        video.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                        resizeText.setText("Fill Mode");
                        break;
                    case 2:
                        video.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                        resizeText.setText("Zoom Mode");
                        break;
                }

                resizeModeIndex = (resizeModeIndex + 1) % 3; // Cycle through 0 → 1 → 2 → 0 ...
            }
        });


        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPreview.seekTo(playerPreview.getCurrentPosition() + 10000);
            }
        });


        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long num = playerPreview.getCurrentPosition() - 10000;
                if (num < 0) {
                    playerPreview.seekTo(0);
                } else {
                    playerPreview.seekTo(playerPreview.getCurrentPosition() - 10000);
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("VIDEO_PLAY_CLICK", "Play pressed. isPlaying=" + playerPreview.isPlaying());
                playerPreview.play();
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("VIDEO_PAUSE_CLICK", "Pause pressed. positionMs=" + playerPreview.getCurrentPosition());
                playerPreview.pause();
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            }
        });

        exoProgress.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {
                // Track current playback state
                wasPlaying = playerPreview.getPlayWhenReady();

                // Pause the video while dragging
                playerPreview.setPlayWhenReady(false);
            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                // Optional: update UI while scrubbing
                startTime.setText(stringForTime(position));
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                // Seek to the selected position
                playerPreview.seekTo(position);

                // Only resume playback if it was playing before
                if (wasPlaying) {
                    playerPreview.setPlayWhenReady(true);
                }
            }
        });


        playerPreview.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                Log.d("VIDEO_STATE", "onPlaybackStateChanged state=" + state);
                if (state == Player.STATE_READY) {
                    long duration = playerPreview.getDuration();
                    totalTime.setText(stringForTime(duration));
                } else if (state == Player.STATE_ENDED) {
                    // Reset video to beginning
                    playerPreview.seekTo(0);
                    playerPreview.setPlayWhenReady(false); // or true to auto-play again

                    // Reset UI to 0
                    startTime.setText(stringForTime(0));
                    exoProgress.setPosition(0);
                    exoProgress.setDuration(playerPreview.getDuration()); // optional, just to be accurate

                    // Update play/pause button state
                    play.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.GONE);
                }
            }


            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Log.d("VIDEO_IS_PLAYING", "onIsPlayingChanged isPlaying=" + isPlaying);
                if (isPlaying) {
                    // Update play/pause button visibility when video starts playing
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);

                    updateProgressAction = new Runnable() {
                        @Override
                        public void run() {
                            if (playerPreview != null && playerPreview.isPlaying()) {
                                long currentPos = playerPreview.getCurrentPosition();
                                long duration = playerPreview.getDuration();

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


        if (!binding.messageBox.getText().toString().isEmpty()) {
            messageBoxMy.setText(binding.messageBox.getText().toString());
        }

        // Tint UI
        try {
            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));
            sendGrp.setBackgroundTintList(tintList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendGrp.setOnClickListener(v -> {
            String[] dimensions = Constant.calculateImageDimensions(mContext, thumbnailFile, Uri.fromFile(thumbnailFile));
            String imageWidthDp = dimensions[0];
            String imageHeightDp = dimensions[1];
            String aspectRatio = dimensions[2];
            binding.messageBox.setText("");
            String msgText = messageBoxMy.getText().toString().trim();

            String receiverUid = getIntent().getStringExtra("friendUidKey");
            String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            String senderRoom = senderId + receiverUid;
            String receiverRoom = receiverUid + senderId;

            String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            String uniqDate = Constant.getCurrentDate();
            boolean isNewDate = uniqueDates.add(uniqDate);

            ArrayList<emojiModel> emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));

            messageModel model = new messageModel(
                    senderId, msgText, currentTime, globalFile.toString(), Constant.video,
                    "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""),
                    "", "", "", "", "", modelId, receiverUid,
                    "", "", "", globalFile.getName(), fileThumbName, fileThumbName,
                    "", 1, isNewDate ? uniqDate : ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1"
            );

            messageList.add(model);
            chatAdapter.itemAdd(binding.messageRecView);
            chatAdapter.setLastItemVisible(isLastItemVisible);

            messagemodel2 model2 = new messagemodel2(
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
                    model.getUserName(),
                    model.getReplytextData(),
                    model.getReplyKey(),
                    model.getReplyType(),
                    model.getReplyOldData(),
                    model.getReplyCrtPostion(),
                    model.getModelId(),
                    model.getReceiverUid(),
                    model.getForwaredKey(),
                    model.getGroupName(),
                    model.getDocSize(),
                    model.getFileName(),
                    model.getThumbnail(),
                    model.getFileNameThumbnail(),
                    model.getCaption(),
                    model.getNotification(),
                    model.getCurrentDate(),
                    model.getEmojiModel(),
                    model.getEmojiCount(),
                    model.getTimestamp(),
                    0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
            );

            //TODO : active : 0 = still loading
            //TODO : active : 1 = completed

//            try {
//                new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//            } catch (Exception e) {
//                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//            }

            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, userFTokenKey);
            uploadHelper.uploadContent(
                    Constant.video, // uploadType
                    GlobalUri, // uri
                    "", // captionText
                    modelId, // modelId
                    thumbnailFile, // savedThumbnail
                    fileThumbName, // fileThumbName
                    fileName, // fileName
                    null, // contactName
                    null, // contactPhone
                    null, // audioTime
                    null, // audioName
                    getFileExtension(GlobalUri), // extension
                    receiverUid // receiverUid
                    , "", "", "", "",
                    model.getReplytextData(), model.getDataType(), model.getFileName(), "", imageWidthDp, imageHeightDp, aspectRatio);
            // uploadtoFirebaseVideo(GlobalUri, thumbnailFile, fileThumbName, fileName, "", modelId);

            Constant.dialogLayoutFullScreen.dismiss();
            if (playerPreview != null) {
                playerPreview.stop();
                playerPreview.release();
            }
        });
    }


    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (fileName == null) {
            fileName = uri.getPath();
            int lastSlashIndex = fileName != null ? fileName.lastIndexOf('/') : -1;
            if (lastSlashIndex != -1) {
                fileName = fileName.substring(lastSlashIndex + 1);
            }
        }
        if (fileName == null) {
            fileName = "document_" + System.currentTimeMillis();
        }
        return fileName;
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


    private void sendAndStopRecording(String finalName) {
        try {
            if (recorder == null) {
                Log.e("TAG", "Recorder is null, cannot stop recording");
                return;
            }

            recorder.stop();
            recorder.release();
            recorder = null;
            Log.d("TAG", "Stopped recording, file: " + mFilePath.getAbsolutePath() + ", exists: " + mFilePath.exists() + ", size: " + mFilePath.length());

            // Verify audio file
            if (!mFilePath.exists() || mFilePath.length() == 0) {
                Log.e("TAG", "Audio file not found or empty: " + mFilePath.getAbsolutePath());
                Toast.makeText(mContext, "Failed to record audio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate audio duration
            String finalTime = getAudioDuration(mFilePath);
            Log.d("TAG", "Audio duration: " + finalTime);

            // Initialize Firebase and user data
            Constant.getSfFuncion(getApplicationContext());
            final String receiverUid = getIntent().getStringExtra("friendUidKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String senderRoom = senderId + receiverUid;
            final String receiverRoom = receiverUid + senderId;
            Log.d("TAG", "senderRoom: " + senderRoom + ", receiverRoom: " + receiverRoom);

            // Get current time
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);

            // Generate unique model ID
            String modelIdNew = database.getReference().push().getKey();
            if (modelIdNew == null) {
                Log.e("TAG", "Failed to generate modelId");
                return;
            }

            // Prepare messageModel
            String uniqDate = Constant.getCurrentDate();
            ArrayList<emojiModel> emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));

            // Extract filename from file path for voice audio
            String fileName = mFilePath.getName();
            Log.d("VoiceAudioDebug", "Extracted fileName: " + fileName);

            messageModel model;
            if (uniqueDates.add(uniqDate)) {
                model = new messageModel(
                        senderId, "", currentDateTimeString, mFilePath.toString(), Constant.voiceAudio,
                        "", "", "", profilepic, finalTime, Constant.getSF.getString(Constant.full_name, ""),
                        "", "", "", "", "", modelIdNew, receiverUid, "", "", "", fileName, "", "", "",
                        1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1"
                );
            } else {
                model = new messageModel(
                        senderId, "", currentDateTimeString, mFilePath.toString(), Constant.voiceAudio,
                        "", "", "", profilepic, finalTime, Constant.getSF.getString(Constant.full_name, ""),
                        "", "", "", "", "", modelIdNew, receiverUid, "", "", "", fileName, "", "", "",
                        1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), "", "", "", "1"
                );
            }

            messagemodel2 model2 = new messagemodel2(
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
                    model.getUserName(),
                    model.getReplytextData(),
                    model.getReplyKey(),
                    model.getReplyType(),
                    model.getReplyOldData(),
                    model.getReplyCrtPostion(),
                    model.getModelId(),
                    model.getReceiverUid(),
                    model.getForwaredKey(),
                    model.getGroupName(),
                    model.getDocSize(),
                    model.getFileName(),
                    model.getThumbnail(),
                    model.getFileNameThumbnail(),
                    model.getCaption(),
                    model.getNotification(),
                    model.getCurrentDate(),
                    model.getEmojiModel(),
                    model.getEmojiCount(),
                    model.getTimestamp(),
                    0, model.getImageWidth(), model.getImageHeight(), model.getAspectRatio(), model.getSelectionCount()
            );

            //TODO : active : 0 = still loading
            //TODO : active : 1 = completed

//            try {
//                new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//            } catch (Exception e) {
//                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//            }

            // Store message in SQLite pending table before upload
            try {
                new DatabaseHelper(mContext).insertPendingMessage(model);
                Log.d("PendingMessages", "Voice audio message stored in pending table: " + model.getModelId());
            } catch (Exception e) {
                Log.e("PendingMessages", "Error storing voice audio message in pending table: " + e.getMessage(), e);
            }

            // Update UI
            messageList.add(model);
            chatAdapter.itemAdd(binding.messageRecView);
            chatAdapter.setLastItemVisible(true); // Show progress for pending message

            // Upload audio file
            globalFile = mFilePath; // Explicitly set globalFile
            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, null, senderId, userFTokenKey);
            uploadHelper.uploadContent(
                    Constant.voiceAudio,
                    Uri.fromFile(mFilePath),
                    "",
                    modelIdNew,
                    null,
                    null,
                    fileName, // Use extracted fileName instead of finalName
                    null,
                    null,
                    finalTime,
                    finalName,
                    getFileExtension(Uri.fromFile(mFilePath)),
                    receiverUid
                    , "", "", "", "",
                    model.getReplytextData(), model.getDataType(), model.getFileName(), "", "", "", "");

            Log.d("VoiceAudioDebug", "UploadChatHelper called with fileName: " + fileName);

            // Clean up
            countDownTimer.cancel();
            globalFile = null; // Reset to prevent reuse
        } catch (Exception ex) {
            Log.e("TAG", "Error in sendAndStopRecording: " + ex.getMessage(), ex);
            Toast.makeText(mContext, "Error recording audio", Toast.LENGTH_SHORT).show();
        }
    }

    private String getAudioDuration(File audioFile) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFile.getAbsolutePath());
            mediaPlayer.prepare();
            int durationMs = mediaPlayer.getDuration();
            mediaPlayer.release();
            int seconds = durationMs / 1000;
            int minutes = seconds / 60;
            seconds = seconds % 60;
            return String.format("%02d:%02d", minutes, seconds);
        } catch (Exception e) {
            Log.e("TAG", "Failed to get audio duration: " + e.getMessage(), e);
            return "00:00";
        }
    }

    private String getFileExtension(Uri uri) {
        if (uri == null) return "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (extension != null && !extension.isEmpty()) {
            return extension;
        }
        String mimeType = mContext.getContentResolver().getType(uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());

        String currentDateAndTime = dateFormat.format(new Date());


        path = new File(mContext.getCacheDir(), currentDateAndTime + ".png");
        try {
            FileOutputStream out = new FileOutputStream(path);
            inImage.compress(Bitmap.CompressFormat.PNG, 100, out); // 100 means no compression, hence HD quality
            out.flush();
            out.close();
            // Use FileProvider to get the URI
            return Uri.fromFile(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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

        countDownTimer = new CountDownTimer(60000, 1000) { // 1 minutes countdown
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


                sendAndStopRecording(finalName);
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


        // store file offline here


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

    public static void expandReply(final View v, final View editLyt) {
        expandReply(v, editLyt, null, false, null, null);
    }

    public static void expandReply(final View v, final View editLyt, final RecyclerView messageRecView, final boolean isAtLastMessage, final View bottomTiming, final View progressBar) {
        Log.d("expandReply", "=== expandReply method called ===");
        Log.d("expandReply", "Reply layout view: " + (v != null ? v.getClass().getSimpleName() : "null"));
        Log.d("expandReply", "EditLyt view: " + (editLyt != null ? editLyt.getClass().getSimpleName() : "null"));
        Log.d("expandReply", "MessageRecView: " + (messageRecView != null ? messageRecView.getClass().getSimpleName() : "null"));
        Log.d("expandReply", "Is at last message: " + isAtLastMessage);
        Log.d("expandReply", "Reply layout visibility before: " + (v != null ? v.getVisibility() : "null"));

        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        Log.d("expandReply", "Target height: " + targetHeight);

        // Set the background to message_box_bg_3 when expanding
        v.setBackgroundResource(R.drawable.message_box_bg_3);
        Log.d("expandReply", "Set reply layout background to message_box_bg_3");

        // Set the editLyt background to message_box_bg_2 when reply is visible
        if (editLyt != null) {
            editLyt.setBackgroundResource(R.drawable.message_box_bg_2);
            Log.d("expandReply", "Set editLyt background to message_box_bg_2");
        } else {
            Log.d("expandReply", "EditLyt is null, skipping background change");
        }

        // Hide bottom timing and progress bar when reply is expanded
        if (bottomTiming != null) {
            bottomTiming.setVisibility(View.GONE);
            Log.d("expandReply", "Hidden bottom timing");
        }

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            Log.d("expandReply", "Hidden progress bar");
        }

        // Start auto-scroll immediately at the same time as reply expansion
        if (isAtLastMessage && messageRecView != null) {
            Log.d("expandReply", "Starting simultaneous auto-scroll with reply expansion");
            messageRecView.post(new Runnable() {
                @Override
                public void run() {
                    if (messageRecView.getAdapter() != null) {
                        int lastPosition = messageRecView.getAdapter().getItemCount() - 1;
                        if (lastPosition >= 0) {
                            // First scroll to position
                            messageRecView.smoothScrollToPosition(lastPosition);
                            Log.d("expandReply", "Simultaneous scroll to position: " + lastPosition);

                            // Then ensure it's at the very top with additional scroll
                            messageRecView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    messageRecView.scrollToPosition(lastPosition);
                                    Log.d("expandReply", "Ensured scroll to very top: " + lastPosition);
                                }
                            }, 50);
                        }
                    }
                }
            });
        } else {
            Log.d("expandReply", "Not at last message or no RecyclerView, skipping auto-scroll");
        }

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Log.d("expandReply", "Set reply layout visibility to VISIBLE");
        ab = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
                if (interpolatedTime == 1) {
                    Log.d("expandReply", "Animation completed - reply layout fully expanded");
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed - fast like WhatsApp/Telegram
        ab.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 2);
        Log.d("expandReply", "Starting animation with duration: " + ab.getDuration() + "ms");
        v.startAnimation(ab);
        Log.d("expandReply", "Animation started");
    }


    public static void collapse(final View v, final View editLyt, final View bottomTiming, final View progressBar) {
        final int initialHeight = v.getMeasuredHeight();

        a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    // Set editLyt background to message_box_bg when reply is collapsed
                    if (editLyt != null) {
                        editLyt.setBackgroundResource(R.drawable.message_box_bg);
                    }


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

    // Overloaded method for backward compatibility
    public static void collapse(final View v) {
        collapse(v, null, null, null);
    }

    // Overloaded method for backward compatibility
    public static void collapse(final View v, final View editLyt) {
        collapse(v, editLyt, null, null);
    }

    public boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }


    private void handleContact(Uri contactUri) {  // Retrieve the contact information
        Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Get the contact ID
            String contactId = null;
            int contactIdColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);

            if (contactIdColumnIndex != -1) {
                contactId = cursor.getString(contactIdColumnIndex);
            }


            // Get the contact type (e.g., "Person" or "Organization")
            String contactType = getContactType(contactId);

            // Get the display nam

            String displayName = null;
            int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            if (displayNameIndex != -1) {
                displayName = cursor.getString(displayNameIndex);
            }

            // Retrieve the phone number
            String phoneNumber = getPhoneNumber(contactId);

            // Display the retrieved information (you can replace this with your own logic)
            Toast.makeText(this, "Contact ID: " + contactId + "\nContact Type: " + contactType + "\nDisplay Name: " + displayName + "\nPhone: " + phoneNumber, Toast.LENGTH_LONG).show();


            bottomSheetDialogContact.show();
            try {
                Constant.getSfFuncion(mContext);
                themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                tintList = ColorStateList.valueOf(Color.parseColor(themColor));
                cancelContact.setTextColor(Color.parseColor(themColor));
                saveContact.setTextColor(Color.parseColor(themColor));
                mobileContact.setTextColor(Color.parseColor(themColor));
                mobile2Contact.setTextColor(Color.parseColor(themColor));

            } catch (Exception ignored) {

            }


            String[] nameParts = displayName.split(" ", 2);

            // Assuming the first part is the first name and the second part is the last name
            String firstNameString = nameParts[0];
            firstnameContact.setText(firstNameString);
            try {
                String lastNameString = nameParts[1];
                lastNameContact.setText(lastNameString);
            } catch (Exception ignored) {
            }

            phoneContact.setText(phoneNumber);
            mobileContact.setText(contactType);
            cancelContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialogContact.dismiss();
                }
            });

            String finalContactId = contactId;
            String finalDisplayName = displayName;
            saveContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_CONTACTS}, 2);

                    } else {

                        updateContact(Long.parseLong(finalContactId), finalDisplayName, phoneNumber);
                    }
                }

            });

        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private String getPhoneNumber(String contactId) {
        String phoneNumber = null;
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);

        if (cursor != null && cursor.moveToFirst()) {

            int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            if (phoneNumberIndex != -1) {
                phoneNumber = cursor.getString(phoneNumberIndex);
            }

        }

        if (cursor != null) {
            cursor.close();
        }

        return phoneNumber;
    }

    private String getContactType(String contactId) {
        String contactType = null;

        Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?", new String[]{contactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE}, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Check if the contact has a structured name, indicating it's a person
            contactType = "Person";
        } else {
            // Check if the contact has an organization, indicating it's an organization
            cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?", new String[]{contactId, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE}, null);

            if (cursor != null && cursor.moveToFirst()) {
                contactType = "Organization";
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return contactType;
    }


    private void updateContact(long contactId, String newName, String newPhoneNumber) {

        ContentResolver contentResolver = getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        // Update name
        operations.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI).withSelection(ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(contactId), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE}).withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newName).build());
        // Add second mobile number
        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValue(ContactsContract.Data.RAW_CONTACT_ID, contactId).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhoneNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (OperationApplicationException | RemoteException e) {
            e.printStackTrace();
            // Handle errors
        }
    }


    public void setupRecyclerViewAndAdapter() {
        String receiverUid = getIntent().getStringExtra("friendUidKey");
        String captionKey = getIntent().getStringExtra("captionKey");
        String original_name = getIntent().getStringExtra("original_name");
        binding.originalName.setText(original_name);
        chatAdapter = new chatAdapter(mContext, binding.messageRecView, messageList, chattingScreen.this, mActivity, phone2Contact, new Handler(), binding.valuable, receiverUid, userFTokenKey, binding.name.getText().toString(), captionKey, binding.originalName, binding.name, binding.blockUser, binding.blockContainer, binding.messageboxContainer);

        // Set up multi-selection listener
        chatAdapter.setMultiSelectListener(new chatAdapter.OnMultiSelectListener() {
            @Override
            public void onMultiSelectModeChanged(boolean isMultiSelectMode) {
                Log.d("MultiSelect", "onMultiSelectModeChanged called with: " + isMultiSelectMode);
                Log.d("MultiSelect", "Bottom layout visibility before: " + binding.bottom.getVisibility());

                if (isMultiSelectMode) {
                    // Hide header1 and show header2 when in multi-select mode
                    binding.header1Cardview.setVisibility(View.GONE);
                    binding.header2Cardview.setVisibility(View.VISIBLE);
                    // Hide bottom layout during multi-selection
                    binding.bottom.setVisibility(View.GONE);
                    Log.d("MultiSelect", "Entering multi-select mode - bottom set to GONE");

                    // Add delayed check to ensure bottom stays hidden
                    binding.bottom.postDelayed(() -> {
                        if (chatAdapter.isMultiSelectMode()) {
                            binding.bottom.setVisibility(View.GONE);
                            Log.d("MultiSelect", "Delayed check - ensuring bottom stays GONE");
                        }
                    }, 100);
                } else {
                    // Show header1 and hide header2 when exiting multi-select mode
                    binding.header1Cardview.setVisibility(View.VISIBLE);
                    binding.header2Cardview.setVisibility(View.GONE);
                    // Show bottom layout when exiting multi-selection
                    binding.bottom.setVisibility(View.VISIBLE);
                    Log.d("MultiSelect", "Exiting multi-select mode - bottom set to VISIBLE");
                }

                Log.d("MultiSelect", "Bottom layout visibility after: " + binding.bottom.getVisibility());
            }

            @Override
            public void onSelectionCountChanged(int count) {
                // Update selected count text
                binding.selectedCounterTxt.setText(count + " selected");

                // Handle bottom layout visibility based on multi-selection state
                if (chatAdapter.isMultiSelectMode()) {
                    // Keep bottom layout hidden during multi-selection
                    binding.bottom.setVisibility(View.GONE);
                    Log.d("MultiSelect", "Selection count changed to: " + count + " - bottom kept GONE (multi-select active)");
                } else {
                    // Multi-select mode is inactive
                    binding.bottom.setVisibility(View.VISIBLE);
                    Log.d("MultiSelect", "Selection count changed to: " + count + " - bottom set to VISIBLE (multi-select inactive)");
                }
            }

            @Override
            public void onForwardSelected() {
                // Handle forward action - this will be called when forwardAll is clicked
                ArrayList<messageModel> selectedMessages = chatAdapter.getSelectedMessages();
                if (!selectedMessages.isEmpty()) {
                    // Open contact selection page for forwarding
                    openContactSelectionForForward(selectedMessages);
                }
            }
        });

        // Set up cross click listener to clear all selections
        binding.cross.setOnClickListener(v -> {
            Log.d("MultiSelect", "Cross button clicked - exiting multi-select mode");
            Log.d("MultiSelect", "Bottom layout visibility before cross click: " + binding.bottom.getVisibility());
            chatAdapter.exitMultiSelectMode();
            // Show bottom layout when exiting multi-selection
            binding.bottom.setVisibility(View.VISIBLE);
            Log.d("MultiSelect", "Bottom layout visibility after cross click: " + binding.bottom.getVisibility());
        });

        // Set up forwardAll click listener
        binding.forwardAll.setOnClickListener(v -> {
            chatAdapter.onForwardSelected();
        });

        // Always enable stable IDs for proper RecyclerView behavior in both debug and release modes
        chatAdapter.enableStableIds();
        binding.messageRecView.setAdapter(chatAdapter);
        binding.messageRecView.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(mContext);
        // Enable layout prefetching for smoother scroll
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setInitialPrefetchItemCount(6);
        binding.messageRecView.setLayoutManager(layoutManager);
        // ✅ Performance tuning and smooth scroll enhancements
        // Note: setHasFixedSize(true) removed because RecyclerView uses wrap_content height
        binding.messageRecView.setItemViewCacheSize(50); // Increased cache size for smoother scrolling
        binding.messageRecView.setDrawingCacheEnabled(true);
        binding.messageRecView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Additional smooth scrolling optimizations
        binding.messageRecView.setNestedScrollingEnabled(true); // Enable nested scrolling for smooth behavior
        binding.messageRecView.setHasFixedSize(false); // Allow dynamic sizing for better performance
        
        // 🚀 ANTI-FLICKER SETTINGS FOR SCROLL END
        binding.messageRecView.setHasTransientState(false); // Disable transient state to prevent flicker
        binding.messageRecView.setItemAnimator(null); // Ensure no animations cause flicker
        binding.messageRecView.setOverScrollMode(View.OVER_SCROLL_NEVER); // Disable overscroll effects

    }

    // Method to open contact selection for forwarding multiple messages
    private void openContactSelectionForForward(ArrayList<messageModel> selectedMessages) {
        // Store selected messages for forwarding
        this.selectedMessagesForForward = selectedMessages;

        for (messageModel msg : selectedMessagesForForward) {
            Log.d("ForwardMessages", "Sender: " + msg.getModelId() + " | Message: " + msg.getMessage());
        }


        // Use the existing forward dialog mechanism
        showForwardDialog(selectedMessages);
    }

    // Method to show the existing forward dialog with multi-selection support
    private void showForwardDialog(ArrayList<messageModel> selectedMessages) {
        // Dismiss any existing dialogs
        BlurHelper.dialogLayoutColor.dismiss();
        Constant.bottomsheetforward(mContext);
        Constant.bottomSheetDialog.show();

        Constant.getSfFuncion(mContext);
        ImageView cancel = Constant.viewShape.findViewById(R.id.cancel);
        View viewnewnn = Constant.viewShape.findViewById(R.id.viewnewnn);
        AutoCompleteTextView searchview = Constant.viewShape.findViewById(R.id.searchview);
        View dx = Constant.viewShape.findViewById(R.id.dx);
        View richBoxForward = Constant.viewShape.findViewById(R.id.richBox);

        ProgressBar progressBarMainNew = Constant.viewShape.findViewById(R.id.progressbar);

        RecyclerView recyclerview = Constant.viewShape.findViewById(R.id.recyclerview);
        RecyclerView namerecyclerview = Constant.viewShape.findViewById(R.id.namerecyclerview);
        TextView forwardText = Constant.viewShape.findViewById(R.id.forward);


        // Set the static fields in chatAdapter
        chatAdapter.recyclerview = recyclerview;
        chatAdapter.namerecyclerview = namerecyclerview;
        chatAdapter.forwardText = forwardText;
        chatAdapter.dx = (LinearLayout) dx;

        // Set the instance field for richBoxForward
        chatAdapter.richBoxForward = (LinearLayout) richBoxForward;
        LinearProgressIndicator networkLoader = Constant.viewShape.findViewById(R.id.networkLoader);
        //TODO : for only network loader Themes

        try {
            Constant.getSfFuncion(mContext);
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#FF6D00"));
                } else if (themColor.equals("#00A3E9")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                } else if (themColor.equals("#7adf2a")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#00C853"));
                } else if (themColor.equals("#ec0001")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#ec7500"));
                } else if (themColor.equals("#16f3ff")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#00F365"));
                } else if (themColor.equals("#FF8A00")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#FFAB00"));
                } else if (themColor.equals("#7F7F7F")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#314E6D"));
                } else if (themColor.equals("#D9B845")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#b0d945"));
                } else if (themColor.equals("#346667")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#729412"));
                } else if (themColor.equals("#9846D9")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#d946d1"));
                } else if (themColor.equals("#A81010")) {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#D85D01"));
                } else {
                    networkLoader.setTrackColor(Color.parseColor(themColor));
                    viewnewnn.setBackgroundTintList(tintList);
                    networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                }
            } catch (Exception ignored) {
            }
        } catch (Exception ignored) {
        }

        if (isInternetConnected()) {
            //TODO ONLINE
            networkLoader.setVisibility(View.GONE);
            try {
                // TODO : - OFFLINE DATA LOAD HERE WHEN ,UNTIL WEBSERVICE 200 STATUS OR ERROR
                get_user_active_contact_forward_list.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_user_active_contact_forward_list = dbHelper.getAllData();

                if (get_user_active_contact_forward_list.size() > 0) {
                    // Toast.makeText(mContext, "not empty", Toast.LENGTH_SHORT).show();
                    setAdapter(get_user_active_contact_forward_list);
                    recyclerview.setVisibility(View.VISIBLE);
                } else {
                }
            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Webservice.get_user_active_chat_list_forward(mContext, Constant.getSF.getString(Constant.UID_KEY, ""), chatAdapter, progressBarMainNew, recyclerview);
        } else {
            //TODO OFFLINE
            networkLoader.setVisibility(View.VISIBLE);
            try {
                Log.d("Network", "disconnected: " + "chattingRoom");
                get_user_active_contact_forward_list.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_user_active_contact_forward_list = dbHelper.getAllData();
                setAdapter(get_user_active_contact_forward_list);
                recyclerview.setVisibility(View.VISIBLE);
            } catch (Exception ignored) {
            }
        }

        forwardText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.getSfFuncion(mContext);

                Date d = new Date();
                android.icu.text.SimpleDateFormat sdf = new android.icu.text.SimpleDateFormat("hh:mm a");
                String currentDateTimeString = sdf.format(d);

                int listcount = chatAdapter.forwardNameList.size();
                String myUID = Constant.getSF.getString(Constant.UID_KEY, "");

                // 🔥 Loop through each forward contact
                for (int i = 0; i < chatAdapter.forwardNameList.size(); i++) {
                    forwardnameModel forwardModel = chatAdapter.forwardNameList.get(i);
                    if (forwardModel == null) continue;

                    String f_id = forwardModel.getFriend_id();
                    String f_token = forwardModel.getF_token();
                    if (f_id == null || f_token == null) continue;

                    // 🔥 Now loop through all selected messages
                    for (messageModel selectedMsg : selectedMessages) {
                        try {
                            String modelId = database.getReference().push().getKey();

                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
                            emojiModels.add(new emojiModel("", ""));

                            // Create new forwarded message
                            messageModel modelnew = new messageModel(
                                    myUID,
                                    selectedMsg.getMessage(),
                                    currentDateTimeString,
                                    selectedMsg.getDocument(),
                                    selectedMsg.getDataType(),
                                    selectedMsg.getExtension(),
                                    selectedMsg.getName(),
                                    selectedMsg.getPhone(),
                                    selectedMsg.getMicPhoto(),
                                    selectedMsg.getMiceTiming(),
                                    selectedMsg.getUserName(),
                                    selectedMsg.getReplytextData(),
                                    selectedMsg.getReplyKey(),
                                    selectedMsg.getReplyType(),
                                    selectedMsg.getReplyOldData(),
                                    selectedMsg.getReplyCrtPostion(),
                                    modelId,
                                    f_id,
                                    Constant.forwordKey,
                                    selectedMsg.getGroupName(),
                                    selectedMsg.getDocSize(),
                                    selectedMsg.getFileName(),
                                    selectedMsg.getThumbnail(),
                                    selectedMsg.getFileNameThumbnail(),
                                    selectedMsg.getCaption(),
                                    selectedMsg.getNotification(),
                                    selectedMsg.getCurrentDate(),
                                    emojiModels,
                                    "",
                                    selectedMsg.getTimestamp(),
                                    selectedMsg.getImageWidth(),
                                    selectedMsg.getImageHeight(),
                                    selectedMsg.getAspectRatio(),
                                    selectedMsg.getSelectionCount(),
                                    selectedMsg.getSelectionBunch()
                            );

                            messagemodel2 model2 = new messagemodel2(
                                    modelnew.getUid(),
                                    modelnew.getMessage(),
                                    modelnew.getTime(),
                                    modelnew.getDocument(),
                                    modelnew.getDataType(),
                                    modelnew.getExtension(),
                                    modelnew.getName(),
                                    modelnew.getPhone(),
                                    modelnew.getMicPhoto(),
                                    modelnew.getMiceTiming(),
                                    modelnew.getUserName(),
                                    modelnew.getReplytextData(),
                                    modelnew.getReplyKey(),
                                    modelnew.getReplyType(),
                                    modelnew.getReplyOldData(),
                                    modelnew.getReplyCrtPostion(),
                                    modelnew.getModelId(),
                                    modelnew.getReceiverUid(),
                                    modelnew.getForwaredKey(),
                                    modelnew.getGroupName(),
                                    modelnew.getDocSize(),
                                    modelnew.getFileName(),
                                    modelnew.getThumbnail(),
                                    modelnew.getFileNameThumbnail(),
                                    modelnew.getCaption(),
                                    modelnew.getNotification(),
                                    modelnew.getCurrentDate(),
                                    modelnew.getEmojiModel(),
                                    modelnew.getEmojiCount(),
                                    modelnew.getTimestamp(),
                                    0,
                                    modelnew.getImageWidth(),
                                    modelnew.getImageHeight(),
                                    modelnew.getAspectRatio(),
                                    modelnew.getSelectionCount()

                            );

                            try {
                                new DatabaseHelper(mContext).insertMessage(model2);
                                Log.d("DATABASE_HELPER", "Message inserted: " + model2.getMessage());
                            } catch (Exception e) {
                                Log.e("DATABASE_HELPER", "SQLite insert failed: " + e.getMessage(), e);
                            }

                            // Upload message
                            UploadChatHelperForward uploadHelper = new UploadChatHelperForward(mContext, myUID, f_token);
                            uploadHelper.uploadContent(modelnew);

                        } catch (Exception e) {
                            Log.e("ForwardError", "Error forwarding to " + f_id + ": " + e.getMessage(), e);
                        }
                    }

                    // 🔥 After finishing all messages for this friend
                    if (i == chatAdapter.forwardNameList.size() - 1) {
                        if (listcount == 1) {
                            Intent intent = new Intent(mContext, chattingScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("nameKey", forwardModel.getName());
                            intent.putExtra("captionKey", "");
                            intent.putExtra("photoKey", "");
                            intent.putExtra("friendUidKey", forwardModel.getFriend_id());
                            intent.putExtra("msgLmtKey", "");
                            intent.putExtra("ecKey", "ecKey");
                            intent.putExtra("userFTokenKey", forwardModel.getF_token());
                            intent.putExtra("deviceType", "");
                            intent.putExtra("fromInviteKey", "fromInviteKey");
                            intent.putExtra("forwardShort", "forwardShort");
                            SwipeNavigationHelper.startActivityWithSwipe(chattingScreen.this, intent);
                            Constant.bottomSheetDialog.dismiss();
                            assert binding.cross != null;
                            binding.cross.performClick();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.messageRecView.post(() -> {
                                        binding.messageRecView.smoothScrollToPosition(messageList.size() - 1);
                                    });
                                }
                            });

                        } else {
                            Intent intent = new Intent(mContext, MainActivityOld.class);
                            SwipeNavigationHelper.startActivityWithSwipe(chattingScreen.this, intent);
                            Constant.bottomSheetDialog.dismiss();
                        }
                    }
                }
            }
        });

    }

    // Method to check internet connectivity
    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                android.net.Network network = connectivityManager.getActiveNetwork();
                android.net.NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && (capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                android.net.NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }

    // Method to set adapter for forward list
    private void setAdapter(ArrayList<get_user_active_contact_list_Model> list) {
        // TODO: Implement adapter setting
        // This method should set the adapter for the forward list
    }

    // Method to forward messages to selected contact using UploadChatHelper
    private void forwardMessagesToContact(ArrayList<messageModel> messages, String contactId, String contactName) {
        if (messages == null || messages.isEmpty()) {
            Toast.makeText(mContext, "No messages to forward", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Forwarding messages...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Forward messages one by one using UploadChatHelper
        new Thread(() -> {
            try {
                for (int i = 0; i < messages.size(); i++) {
                    messageModel message = messages.get(i);
                    final int currentIndex = i; // Make it final for lambda

                    // Update progress on UI thread
                    runOnUiThread(() -> {
                        progressDialog.setMessage("Forwarding message " + (currentIndex + 1) + " of " + messages.size());
                    });

                    // Forward the message using UploadChatHelper
                    forwardSingleMessage(message, contactId, contactName);

                    // Add small delay to prevent overwhelming the server
                    Thread.sleep(100);
                }

                // Hide progress dialog and show success message
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Successfully forwarded " + messages.size() + " messages to " + contactName, Toast.LENGTH_SHORT).show();

                    // Exit multi-select mode
                    chatAdapter.exitMultiSelectMode();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Error forwarding messages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    // Method to forward a single message using UploadChatHelper
    private void forwardSingleMessage(messageModel message, String contactId, String contactName) {
        try {
            // TODO: Implement actual forwarding using UploadChatHelper
            // For now, just log the message details
            Log.d("ForwardMessage", "Forwarding message: " + message.getModelId() + " to contact: " + contactName);

            // Simulate forwarding delay
            Thread.sleep(50);

        } catch (Exception e) {
            Log.e("ForwardMessage", "Exception forwarding message: " + e.getMessage());
        }
    }

    public void setupRecyclerViewAndAdapter2() {
        String receiverUid = getIntent().getStringExtra("friendUidKey");
        String captionKey = getIntent().getStringExtra("captionKey");
        String original_name = getIntent().getStringExtra("original_name");
        binding.originalName.setText("");
        chatAdapter = new chatAdapter(mContext, binding.messageRecView, messageList, chattingScreen.this, mActivity, phone2Contact, new Handler(), binding.valuable, receiverUid, userFTokenKey, binding.name.getText().toString(), captionKey, binding.originalName, binding.name, binding.blockUser, binding.blockContainer, binding.messageboxContainer);
        // Always enable stable IDs for proper RecyclerView behavior in both debug and release modes
        chatAdapter.enableStableIds();
        binding.messageRecView.setAdapter(chatAdapter);
        binding.messageRecView.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(mContext);

        // 🚀 ULTRA-FAST SCROLLING OPTIMIZATIONS - WATER-LIKE PERFORMANCE
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setInitialPrefetchItemCount(12); // Increased prefetch for faster loading
        layoutManager.setRecycleChildrenOnDetach(true); // Recycle views immediately
        layoutManager.setSmoothScrollbarEnabled(true); // Enable smooth scrollbar

        binding.messageRecView.setLayoutManager(layoutManager);

        // 🚀 MAXIMUM PERFORMANCE SETTINGS
        binding.messageRecView.setItemViewCacheSize(100); // Massive cache for ultra-smooth scrolling
        binding.messageRecView.setDrawingCacheEnabled(true);
        binding.messageRecView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // 🚀 SCROLL ACCELERATION AND MOMENTUM
        binding.messageRecView.setNestedScrollingEnabled(false); // Disable nested scrolling for speed
        binding.messageRecView.setHasFixedSize(false); // Dynamic sizing for performance
        binding.messageRecView.setItemAnimator(null); // No animations = maximum speed

        // 🚀 HARDWARE ACCELERATION AND RENDERING
        binding.messageRecView.setLayerType(View.LAYER_TYPE_HARDWARE, null); // Hardware acceleration
        binding.messageRecView.setDrawingCacheBackgroundColor(android.R.color.transparent);

        // 🚀 ADDITIONAL SPEED OPTIMIZATIONS
        binding.messageRecView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING); // Faster touch response
        binding.messageRecView.setOverScrollMode(View.OVER_SCROLL_NEVER); // Disable overscroll for speed

        // 🚀 MEMORY AND PERFORMANCE TUNING
        binding.messageRecView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        binding.messageRecView.getRecycledViewPool().setMaxRecycledViews(0, 20); // More recycled views
        binding.messageRecView.getRecycledViewPool().setMaxRecycledViews(1, 20);

        // 🚀 SCROLL VELOCITY AND ACCELERATION
        binding.messageRecView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY); // Faster scrollbar
        binding.messageRecView.setScrollBarFadeDuration(0); // Instant scrollbar
        binding.messageRecView.setScrollBarDefaultDelayBeforeFade(0); // No delay

    }

    //  TODO: 28/08/24  VIMP
//    private void fetchMessages(String receiverRoom) {
//
//        database.getReference().child(Constant.CHAT).child(receiverRoom).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                try {
//                    messageModel model = snapshot.getValue(messageModel.class);
//                    if (model != null) {
//                        boolean exists = false;
//                        for (messageModel existingModel : messageList) {
//                            if (existingModel.getModelId().equals( model.getModelId()))) {
//                                exists = true;
//                                break;
//                            }
//                        }
//                        if (!exists) {
//                            String uniqDate = model.getCurrentDate();
//                            if (uniqueDates.add(uniqDate)) {
//                                messageList.add(new messageModel( model.getUid(),  model.getMessage()),  model.getTime()),  model.getDocument()),  model.getDataType()),  model.getExtension()),  model.getName()),  model.getPhone()),  model.getMicPhoto()),  model.getMiceTiming()),  model.getUserName()),  model.getReplytextData()),  model.getReplyKey()),  model.getReplyType()),  model.getReplyOldData()),  model.getReplyCrtPostion()),  model.getModelId()),  model.getReceiverUid()),  model.getForwaredKey()),  model.getGroupName()),  model.getDocSize()),  model.getFileName()),  model.getThumbnail()),  model.getFileNameThumbnail()),  model.getCaption()), model.getNotification(), uniqDate));
//                            } else {
//                                messageList.add(new messageModel( model.getUid(),  model.getMessage()),  model.getTime()),  model.getDocument()),  model.getDataType()),  model.getExtension()),  model.getName()),  model.getPhone()),  model.getMicPhoto()),  model.getMiceTiming()),  model.getUserName()),  model.getReplytextData()),  model.getReplyKey()),  model.getReplyType()),  model.getReplyOldData()),  model.getReplyCrtPostion()),  model.getModelId()),  model.getReceiverUid()),  model.getForwaredKey()),  model.getGroupName()),  model.getDocSize()),  model.getFileName()),  model.getThumbnail()),  model.getFileNameThumbnail()),  model.getCaption()), model.getNotification(), ":" + uniqDate));
//                            }
//                            chatAdapter.notifyDataSetChanged();
//                            binding.progressBar.setVisibility(View.GONE);
//                            binding.messageRecView.scrollToPosition(chatAdapter.getItemCount() - 1);
//                            notificationId = getIntent().getIntExtra("notificationId", 200);
//                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                            notificationManager.cancel(notificationId);
//                        }
//                    }
//                } catch (Exception ignored) {
//                    Log.d("TAG", "onChildAdded: " + ignored.getMessage());
//                    if (binding.downCardview.getVisibility() == View.VISIBLE) {
//                        binding.downCardview.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                messageModel model = snapshot.getValue(messageModel.class);
//                if (model != null) {
//                    Constant.getSfFuncion(mContext);
//                    String uid = Constant.getSF.getString(Constant.UID_KEY, "");
//                    try {
//                        if (!uid.equals( model.getModelId()))) {
//                            int indexOfItem = -1;
//                            for (int i = 0; i < messageList.size(); i++) {
//                                messageModel model2 = messageList.get(i);
//                                if (model2.getModelId().equals( model.getModelId()))) {
//                                    indexOfItem = i;
//                                    break;
//                                }
//                            }
//                            if (indexOfItem != -1) {
//                                chatAdapter.removeItem(indexOfItem);
//                            }
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
    private void askCameraPermissions() {
        if (hasPermissions()) {
            Log.d(TAG, "Permissions already granted, launching fragment");
            dispatchTakePictureIntent();
        } else {
            Log.d(TAG, "Requesting permissions");
            permissionLauncher.launch(REQUIRED_PERMISSIONS);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    /// -------------------------------------------------------------------------------------------------- todo START
    private boolean initialLoadDone = false;
    private boolean fullListenerAttached = false;
    private boolean isFirstTimeOnCreate = true; // Flag to track first time onCreate

    // Removed scrollToBottomSafely method - using simple scrollToPosition in fetchMessages and handleKeyboardScroll only

    /**
     * Initialize chat data with proper caching and network fallback
     */
    private void initializeChatData(String receiverUid, String receiverRoom) {
        Log.d("ChatInit", "Initializing chat data for receiver: " + receiverUid);
        Log.d("ScrollText", "=== initializeChatData START ===");

        // Ensure progress bar is hidden before any operations
        if (binding.progressBar != null) {
            binding.progressBar.setVisibility(View.GONE);
        }

        // Step 1: Always load messages from database first
        try {
            Log.d("ChatInit", "Loading messages from database for receiverUid: " + receiverUid);
            getAllMessages(receiverUid, mContext);

            if (!messageList.isEmpty()) {
                Log.d("ChatInit", "Found " + messageList.size() + " messages from database");
                Log.d("ScrollText", "initializeChatData: Found " + messageList.size() + " messages from database");

                // Show messages immediately - no UI thread delay
                if (binding.progressBar != null) {
                    binding.progressBar.setVisibility(View.GONE);
                }
                if (chatAdapter != null) {
                    Log.d("ScrollText", "initializeChatData: Updating adapter with " + messageList.size() + " messages");
                    otherFunctions.updateMessageList(new ArrayList<>(messageList), chatAdapter);
                    
                    // Check scroll position before scrolling
                    LinearLayoutManager layoutManager = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
                    if (layoutManager != null) {
                        int firstVisible = layoutManager.findFirstVisibleItemPosition();
                        int lastVisible = layoutManager.findLastVisibleItemPosition();
                        int itemCount = chatAdapter.getItemCount();
                        Log.d("ScrollText", "initializeChatData: Before scroll - Item count: " + itemCount + ", First visible: " + firstVisible + ", Last visible: " + lastVisible);
                    }
                    
                    // Scroll to bottom to show latest messages - removed scrollToBottomSafely
                    // if (binding.messageRecView != null && messageList.size() > 0 && chatAdapter != null) {
                    //     Log.d("ScrollText", "initializeChatData: Calling scrollToBottomSafely()");
                    //     scrollToBottomSafely();
                    // }
                }
                // Ensure loading state is properly reset
                isLoading = false;
                initialLoadDone = true;

                // Attach listener for new messages after a delay to allow scroll to complete
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    attachFullListener(receiverRoom);
                }, 500); // 500ms delay to allow scroll to complete
                Log.d("ChatInit", "Chat initialized with database data");
                Log.d("ScrollText", "=== initializeChatData END ===");
            }
        } catch (Exception e) {
            Log.e("ChatInit", "Error loading messages from database: " + e.getMessage());
        }

        // Step 2: Load pending messages regardless of cached data
        loadPendingMessages(receiverUid, true); // Scroll to last position when called from onCreate

        // Step 3: Only fetch from network if no cached data
        if (messageList.isEmpty()) {
            Log.d("ChatInit", "No cached data found, fetching from network");
            // Only scroll on first time (onCreate), not on subsequent calls
            fetchMessagesWithRetry(receiverRoom, 3, isFirstTimeOnCreate); // Pass scroll flag only on first time
        } else {
            Log.d("ChatInit", "Cached data found, not fetching from network");
        }
    }

    /**
     * Fetch messages with retry mechanism for better reliability
     */
    private void fetchMessagesWithRetry(String receiverRoom, int retryCount) {
        fetchMessagesWithRetry(receiverRoom, retryCount, false); // Default: don't scroll
    }
    
    private void fetchMessagesWithRetry(String receiverRoom, int retryCount, boolean shouldScrollToLast) {
        // If we already have messages, don't retry
        if (!messageList.isEmpty()) {
            Log.d("ChatInit", "Messages already available, skipping retry");
            return;
        }

        if (retryCount <= 0) {
            Log.w("ChatInit", "Max retries reached, showing cached data or empty state");
            // Show cached data even if network fails
            if (binding.progressBar != null) {
                binding.progressBar.setVisibility(View.GONE);
            }
            if (messageList.isEmpty() && binding.valuable != null) {
                binding.valuable.setVisibility(View.VISIBLE);
            }
            return;
        }

        Log.d("ChatInit", "Fetching messages from network, retry count: " + retryCount);
        fetchMessages(receiverRoom, () -> {
            Log.d("ChatInit", "Network fetch completed successfully");
        }, shouldScrollToLast);
    }

    /**
     * Load pending messages from SQLite and show them with progress
     */
    private void loadPendingMessages(String receiverUid) {
        loadPendingMessages(receiverUid, false); // Default: don't scroll
    }

    /**
     * Load pending messages from SQLite and show them with progress
     *
     * @param receiverUid        The receiver UID
     * @param shouldScrollToLast Whether to scroll to the last position after loading
     */
    private void loadPendingMessages(String receiverUid, boolean shouldScrollToLast) {
        try {
            Log.d("PendingMessages", "loadPendingMessages called for receiverUid: " + receiverUid);
            List<messageModel> pendingMessages = new DatabaseHelper(mContext).getPendingMessages(receiverUid);
            Log.d("PendingMessages", "Retrieved " + pendingMessages.size() + " pending messages from SQLite");

            if (!pendingMessages.isEmpty()) {
                Log.d("PendingMessages", "Loading " + pendingMessages.size() + " pending messages");

                // Add pending messages to the message list
                HashSet<String> existingIds = new HashSet<>();
                for (messageModel m : messageList) existingIds.add(m.getModelId());
                Log.d("PendingMessages", "Existing message IDs: " + existingIds.size());

                int addedCount = 0;
                for (messageModel pendingMessage : pendingMessages) {
                    try {
                        Log.d("PendingMessages", "Pending modelId=" + pendingMessage.getModelId() +
                                ", dataType=" + pendingMessage.getDataType() +
                                ", selectionCount=" + pendingMessage.getSelectionCount() +
                                ", bunchSize=" + (pendingMessage.getSelectionBunch() != null ? pendingMessage.getSelectionBunch().size() : 0));
                    } catch (Exception ignore) {
                    }
                    if (!existingIds.contains(pendingMessage.getModelId())) {
                        messageList.add(pendingMessage);
                        existingIds.add(pendingMessage.getModelId());
                        addedCount++;
                        Log.d("PendingMessages", "Added pending message: " + pendingMessage.getModelId());
                    } else {
                        Log.d("PendingMessages", "Skipped duplicate pending message: " + pendingMessage.getModelId());
                    }
                }

                Log.d("PendingMessages", "Added " + addedCount + " new pending messages to messageList");
                Log.d("PendingMessages", "Total messageList size after adding pending: " + messageList.size());

                // Update UI to show pending messages with progress
                runOnUiThread(() -> {
                    if (chatAdapter != null) {
                        // Ensure adapter is attached so items render
                        try {
                            if (binding != null && binding.messageRecView != null && binding.messageRecView.getAdapter() == null) {
                                binding.messageRecView.setAdapter(chatAdapter);
                            }
                        } catch (Exception ignored) {
                        }

                        // Debug: Check UID matching for view type
                        try {
                            Constant.getSfFuncion(mContext);
                            String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");
                            Log.d("PendingMessages", "Current user UID: " + currentUid);
                            for (messageModel msg : messageList) {
                                Log.d("PendingMessages", "Message UID: " + msg.getUid() + ", ModelId: " + msg.getModelId());
                            }
                        } catch (Exception e) {
                            Log.e("PendingMessages", "Error checking UIDs: " + e.getMessage());
                        }

                        Log.d("PendingMessages", "Updating UI with pending messages");
                        otherFunctions.updateMessageList(new ArrayList<>(messageList), chatAdapter);
                        chatAdapter.setLastItemVisible(true); // Show progress for last message
                        // Removed scrollToBottomSafely from loadPendingMessages
                        // if (shouldScrollToLast && binding.messageRecView != null && messageList.size() > 0) {
                        //     Log.d("ScrollText", "loadPendingMessages: shouldScrollToLast=true, calling scrollToBottomSafely()");
                        //     scrollToBottomSafely();
                        //     Log.d("PendingMessages", "Scrolled to last position as requested");
                        // } else {
                        //     Log.d("ScrollText", "loadPendingMessages: shouldScrollToLast=false, not scrolling");
                        // }
                        Log.d("PendingMessages", "UI updated successfully");
                    } else {
                        Log.e("PendingMessages", "chatAdapter is null, cannot update UI");
                    }
                });
            } else {
                Log.d("PendingMessages", "No pending messages found for receiverUid: " + receiverUid);
            }
        } catch (Exception e) {
            Log.e("PendingMessages", "Error loading pending messages: " + e.getMessage(), e);
        }
    }

    /**
     * Check if Firebase offline capability is working properly
     */
//    private void checkFirebaseOfflineCapability() {
//        try {
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//            // Test Firebase connection and persistence
//            database.getReference().child("test").setValue("test")
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Log.d("FirebasePersistence", "Firebase persistence is working correctly");
//                        } else {
//                            Log.e("FirebasePersistence", "Firebase persistence issue: " + task.getException());
//                        }
//                    });
//
//            // Check if persistence is enabled
//            Log.d("FirebasePersistence", "Firebase database URL: " + database.getApp().getOptions().getDatabaseUrl());
//
//        } catch (Exception e) {
//            Log.e("FirebasePersistence", "Error checking Firebase persistence: " + e.getMessage());
//        }
//    }
    private void fetchMessages(String receiverRoom, OnMessagesFetchedListener listener) {
        fetchMessages(receiverRoom, listener, false); // Default: don't scroll
    }
    
    private void fetchMessages(String receiverRoom, OnMessagesFetchedListener listener, boolean shouldScrollToLast) {
        if (isLoading) {
            Log.d("fetchMessages", "Already loading, skipping fetch.");
            if (listener != null) {
                listener.onMessagesFetched();
            }
            return;
        }

        // If we already have messages (cached data), don't show loader
        if (!messageList.isEmpty()) {
            Log.d("fetchMessages", "Messages already available, skipping network fetch");
            if (listener != null) {
                listener.onMessagesFetched();
            }
            return;
        }

        isLoading = true;
        // Only show progress bar if we have no messages and it's the initial load
        if (messageList.isEmpty() && !initialLoadDone && binding.progressBar != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        Log.d("fetchMessages", "Fetching messages for room: " + receiverRoom);

        if (!initialLoadDone) {
            // 🔹 Phase 1: Load last 10 messages ordered by timestamp
            Log.d("fetchMessages", "Phase 1: Initial load (last 10 messages by timestamp).");
            Query limitedQuery = database.getReference().child(Constant.CHAT)
                    .child(receiverRoom)
                    .orderByChild("timestamp")
                    .limitToLast(10);

            limitedQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("fetchMessages", "Fetched initial data: " + snapshot.getChildrenCount() + " messages.");

                    List<messageModel> tempList = new ArrayList<>();

                    for (DataSnapshot child : snapshot.getChildren()) {
                        messageModel model = child.getValue(messageModel.class);
                        if (model != null) {
                            // Parse selectionBunch from Firebase
                            messageModel.parseSelectionBunchFromSnapshot(child, model);
                            tempList.add(model);
                        }
                    }

                    // 🔹 Directly update adapter once (bulk update)
                    runOnUiThread(() -> {
                        Log.d("ScrollText", "fetchMessages: Updating adapter with " + tempList.size() + " messages");
                        chatAdapter.setMessages(tempList);  // adapter मध्ये नवीन method ठेव
                        
                        // Check scroll position before scrolling
                        LinearLayoutManager layoutManager = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
                        if (layoutManager != null) {
                            int firstVisible = layoutManager.findFirstVisibleItemPosition();
                            int lastVisible = layoutManager.findLastVisibleItemPosition();
                            int itemCount = chatAdapter.getItemCount();
                            Log.d("ScrollText", "fetchMessages: Before scroll - Item count: " + itemCount + ", First visible: " + firstVisible + ", Last visible: " + lastVisible);
                        }
                        
                        // Only scroll to last position if shouldScrollToLast is true
                        if (shouldScrollToLast && tempList.size() > 0) {
                            int lastPosition = tempList.size() - 1;
                            Log.d("ScrollText", "fetchMessages: shouldScrollToLast=true, calling scrollToPosition");
                            Log.d("ScrollText", "fetchMessages: tempList.size()=" + tempList.size() + ", lastPosition=" + lastPosition);
                            Log.d("ScrollText", "fetchMessages: messageList.size()=" + messageList.size() + ", chatAdapter.getItemCount()=" + chatAdapter.getItemCount());
                            
                            // Check what type of message is at the last position
                            if (lastPosition < tempList.size()) {
                                messageModel lastMessage = tempList.get(lastPosition);
                                if (lastMessage != null) {
                                    Log.d("ScrollText", "fetchMessages: Last message UID=" + lastMessage.getUid() + ", Message=" + lastMessage.getMessage());
                                }
                            }
                            
                            // Check if there's a mismatch between tempList and adapter
                            if (tempList.size() != chatAdapter.getItemCount()) {
                                Log.d("ScrollText", "WARNING: tempList.size()=" + tempList.size() + " != chatAdapter.getItemCount()=" + chatAdapter.getItemCount());
                                Log.d("ScrollText", "This suggests data inconsistency - adapter has different data than what we're scrolling to");
                            }
                            
                            binding.messageRecView.scrollToPosition(lastPosition);
                            
                            // Verify scroll position after a short delay
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LinearLayoutManager layoutManager3 = (LinearLayoutManager) binding.messageRecView.getLayoutManager();
                                if (layoutManager3 != null) {
                                    int firstVisible = layoutManager3.findFirstVisibleItemPosition();
                                    int lastVisible = layoutManager3.findLastVisibleItemPosition();
                                    int currentAdapterCount = chatAdapter.getItemCount();
                                    Log.d("ScrollText", "fetchMessages: After scroll - First visible: " + firstVisible + ", Last visible: " + lastVisible + ", Expected: " + lastPosition + ", Adapter count: " + currentAdapterCount);
                                    
                                    if (lastVisible != lastPosition) {
                                        Log.d("ScrollText", "ERROR: Scroll failed! Expected position " + lastPosition + " but got " + lastVisible);
                                        Log.d("ScrollText", "This means the adapter data changed after we set it");
                                    }
                                }
                            }, 100);
                        } else {
                            Log.d("ScrollText", "fetchMessages: shouldScrollToLast=false, not scrolling");
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    });

                    // Update local reference
                    messageList.clear();
                    messageList.addAll(tempList);

                    initialLoadDone = true;
                    isLoading = false;

                    // 🔁 Attach continuous listener after a delay to allow scroll to complete
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        attachFullListener(receiverRoom);
                    }, 500); // 500ms delay to allow scroll to complete

                    if (listener != null) {
                        listener.onMessagesFetched();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    isLoading = false;
                    runOnUiThread(() -> {
                        if (binding.progressBar != null) {
                            binding.progressBar.setVisibility(View.GONE);
                        }
                        if (messageList.isEmpty() && binding.valuable != null) {
                            binding.valuable.setVisibility(View.VISIBLE);
                        }
                    });
                    Log.e("fetchMessages", "Error fetching initial messages: " + error.getMessage());

                    // Don't show toast for network errors to avoid spam
                    if (error.getCode() != DatabaseError.NETWORK_ERROR) {
                        runOnUiThread(() -> {
                            Toast.makeText(mContext, "Error loading messages: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });

        } else {
            // Already loaded once, just make sure full listener is attached
            Log.d("fetchMessages", "Phase 2: Full listener already attached.");
            attachFullListener(receiverRoom);

            if (listener != null) {
                listener.onMessagesFetched();
            }
        }
    }

    private void attachFullListener(String receiverRoom) {
        if (fullListenerAttached) {
            Log.d("attachFullListener", "Full listener already attached, skipping.");
            return; // Prevent duplicate listeners
        }
        fullListenerAttached = true;
        Log.d("attachFullListener", "Attaching full listener to room: " + receiverRoom);

        Query fullQuery = database.getReference().child(Constant.CHAT)
                .child(receiverRoom)
                .orderByChild("timestamp"); // ✅ order by timestamp

        fullQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                Log.d("onChildAdded", "Child added with key: " + dataSnapshot.getKey());
                handleChildAdded(dataSnapshot); // ✅ only for new messages


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                if (snapshot == null || !snapshot.exists()) return;

                String changedKey = snapshot.getKey();
                messageModel updatedModel = snapshot.getValue(messageModel.class);
                if (updatedModel == null || changedKey == null) return;

                // Parse selectionBunch from Firebase
                messageModel.parseSelectionBunchFromSnapshot(snapshot, updatedModel);

                updatedModel.setModelId(changedKey);

                // 🔹 पहिला update skip करायचा का?
                if (changeFlags.containsKey(changedKey) && changeFlags.get(changedKey)) {
                    // पहिल्या वेळेला onChildChanged कॉल झाला → skip
                    changeFlags.put(changedKey, false); // पुढच्या वेळी actual बदल चेक करू
                    Log.d("onChildChanged", "Skipping first update for key: " + changedKey);
                    return;
                }

                // ✅ आता खरंच बदल झालाय का ते तपास
                int index = -1;
                for (int i = 0; i < messageList.size(); i++) {
                    if (messageList.get(i).getModelId().equals(changedKey)) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    messageModel oldModel = messageList.get(index);

                    boolean isChanged = false;

                    if (!Objects.equals(oldModel.getMessage(), updatedModel.getMessage())) {
                        isChanged = true;
                    } else if (!Objects.equals(oldModel.getEmojiCount(), updatedModel.getEmojiCount())) {
                        isChanged = true;
                    } else if (oldModel.getTimestamp() != updatedModel.getTimestamp()) {
                        isChanged = true;
                    }
                    Log.d("*************", "Updated null");
                    if (isChanged) {
                        messageList.set(index, updatedModel);
                        final int finalIndex = index;
                        runOnUiThread(() -> chatAdapter.notifyItemChanged(finalIndex));
                        //    Toast.makeText(mContext, "Updated", Toast.LENGTH_SHORT).show();
                        Log.d("*************", "Updated ");

                        Log.d("onChildChanged", "Message updated for key: " + changedKey);
                    } else {
                        Log.d("onChildChanged", "No meaningful change → update skipped: " + changedKey);
                    }
                }
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d("onChildRemoved", "Child removed with key: " + snapshot.getKey());
                handleChildRemoved(snapshot, receiverRoom);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("onChildMoved", "Child moved with key: " + snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "fullListener:onCancelled", error.toException());
            }
        });
    }

    // Assuming this method is within your Activity or Fragment
    private void handleChildAdded(DataSnapshot dataSnapshot) {
        if (dataSnapshot == null || !dataSnapshot.exists()) {
            Log.w("handleChildAdded", "DataSnapshot is null or does not exist");
            return;
        }

        String key = dataSnapshot.getKey();
        Log.d("handleChildAdded", "Handling child added for key: " + key);

        messageModel model = dataSnapshot.getValue(messageModel.class);
        if (model == null) {
            Log.w("handleChildAdded", "Failed to parse messageModel for key: " + key);
            return;
        }

        // Parse selectionBunch from Firebase
        messageModel.parseSelectionBunchFromSnapshot(dataSnapshot, model);

        // Ensure modelId is always set using the Firebase key to uniquely identify messages
        try {
            if ((model.getModelId() == null || model.getModelId().isEmpty()) && key != null) {
                model.setModelId(key);
            }
        } catch (Exception ignored) {
        }

        Log.d("loadImageIntoViewXXX", "getImageWidth: " + model.getImageWidth());
        Log.d("loadImageIntoViewXXX", "getImageHeight: " + model.getImageHeight());
        Log.d("loadImageIntoViewXXX", "getAspectRatio: " + model.getAspectRatio());
        // Cancel existing notification if any
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancel(notificationId);
            } else {
                Log.w("handleChildAdded", "NotificationManager is null");
            }
        } catch (Exception e) {
            Log.e("handleChildAdded", "Error canceling notification: " + e.getMessage());
        }

        // Add to pending messages (if pendingMessages is still used for other async tasks)
        synchronized (pendingMessages) {
            pendingMessages.add(model);
        }

        try {
            // Safely access model fields with null checks
            String decryptedModelId = model.getModelId() != null ? model.getModelId() : "";
            String decryptedUid = model.getUid() != null ? model.getUid() : "";
            String decryptedMessage = model.getMessage() != null ? model.getMessage() : "";
            String decryptedTime = model.getTime() != null ? model.getTime() : "";
            String decryptedDocument = model.getDocument() != null ? model.getDocument() : "";
            String decryptedDataType = model.getDataType() != null ? model.getDataType() : "";
            String decryptedExtension = model.getExtension() != null ? model.getExtension() : "";
            String decryptedName = model.getName() != null ? model.getName() : "";
            String decryptedPhone = model.getPhone() != null ? model.getPhone() : "";
            String decryptedMicPhoto = model.getMicPhoto() != null ? model.getMicPhoto() : "";
            String decryptedMiceTiming = model.getMiceTiming() != null ? model.getMiceTiming() : "";
            String decryptedUserName = model.getUserName() != null ? model.getUserName() : "";
            String decryptedReplytextData = model.getReplytextData() != null ? model.getReplytextData() : "";
            String decryptedReplyKey = model.getReplyKey() != null ? model.getReplyKey() : "";
            String decryptedReplyType = model.getReplyType() != null ? model.getReplyType() : "";
            String decryptedReplyOldData = model.getReplyOldData() != null ? model.getReplyOldData() : "";
            String decryptedReplyCrtPosition = model.getReplyCrtPostion() != null ? model.getReplyCrtPostion() : "";
            String decryptedReceiverUid = model.getReceiverUid() != null ? model.getReceiverUid() : "";
            String decryptedForwaredKey = model.getForwaredKey() != null ? model.getForwaredKey() : "";
            String decryptedGroupName = model.getGroupName() != null ? model.getGroupName() : "";
            String decryptedDocSize = model.getDocSize() != null ? model.getDocSize() : "";
            String decryptedFileName = model.getFileName() != null ? model.getFileName() : "";
            String decryptedThumbnail = model.getThumbnail() != null ? model.getThumbnail() : "";
            String decryptedFileNameThumbnail = model.getFileNameThumbnail() != null ? model.getFileNameThumbnail() : "";
            String decryptedCaption = model.getCaption() != null ? model.getCaption() : "";
            long decryptedTimestamp = model.getTimestamp();
            ArrayList<emojiModel> decryptedEmoji = model.getEmojiModel() != null ? model.getEmojiModel() : new ArrayList<>();
            String decryptedEmojiCount = model.getEmojiCount() != null ? model.getEmojiCount() : "0";
            String uniqDate = model.getCurrentDate() != null ? model.getCurrentDate() : "";
            String imageWidthDp = model.getImageWidth() != null ? model.getImageWidth() : "";
            String imageHeightDp = model.getImageHeight() != null ? model.getImageHeight() : "";
            String aspectRatio = model.getAspectRatio() != null ? model.getAspectRatio() : "";

            Log.d("loadImageIntoViewGetImage", "getImageWidth: " + model.getImageWidth());
            Log.d("loadImageIntoViewGetImage", "getImageHeight: " + model.getImageHeight());
            Log.d("loadImageIntoViewGetImage", "getAspectRatio: " + aspectRatio);

            // Create a new list for the update
            List<messageModel> updatedMessageList = new ArrayList<>(messageList); // Start with a copy of the current list

            // Remove existing message with the same model ID if it's an update/replacement
            synchronized (messageList) { // Keep synchronized block for consistency if messageList is shared
                int existingIndex = -1;
                for (int i = 0; i < updatedMessageList.size(); i++) { // Iterate over the new list copy
                    messageModel existingModel = updatedMessageList.get(i);
                    if (existingModel != null && decryptedModelId.equals(existingModel.getModelId())) {
                        existingIndex = i;
                        break;
                    }
                }

                if (existingIndex != -1) {
                    updatedMessageList.remove(existingIndex); // Remove from the new list copy
                    Log.d("handleChildAdded", "Duplicate found, removed message with ID: " + decryptedModelId);

                    try {
                        new DatabaseHelper(mContext).deleteMessageByModelId(decryptedModelId);
                    } catch (Exception e) {
                        Log.e("handleChildAdded", "Error deleting message from DB: " + e.getMessage());
                    }
                }

                // Add the new message to the new list copy
                boolean isNewDate = uniqueDates != null && uniqueDates.add(uniqDate);
                String finalDate = isNewDate ? uniqDate : ":" + uniqDate;

                messageModel newModel = new messageModel(
                        decryptedUid, decryptedMessage, decryptedTime,
                        decryptedDocument, decryptedDataType, decryptedExtension, decryptedName,
                        decryptedPhone, decryptedMicPhoto, decryptedMiceTiming, decryptedUserName,
                        decryptedReplytextData, decryptedReplyKey, decryptedReplyType,
                        decryptedReplyOldData, decryptedReplyCrtPosition, decryptedModelId,
                        decryptedReceiverUid, decryptedForwaredKey, decryptedGroupName,
                        decryptedDocSize, decryptedFileName, decryptedThumbnail,
                        decryptedFileNameThumbnail, decryptedCaption, model.getNotification(),
                        finalDate, decryptedEmoji, decryptedEmojiCount, decryptedTimestamp, imageWidthDp, imageHeightDp, aspectRatio, model.getSelectionCount(), model.getSelectionBunch()
                );
                updatedMessageList.add(newModel); // Add to the new list copy

                // Remove message from SQLite pending table since it's now in Firebase
                try {
                    boolean removed = new DatabaseHelper(mContext).removePendingMessage(decryptedModelId, decryptedReceiverUid);
                    if (removed) {
                        Log.d("PendingMessages", "Removed pending message from SQLite: " + decryptedModelId);
                    }
                } catch (Exception e) {
                    Log.e("PendingMessages", "Error removing pending message: " + e.getMessage(), e);
                }

                // Sort messages by timestamp to maintain chronological order
                Collections.sort(updatedMessageList, new Comparator<messageModel>() {
                    @Override
                    public int compare(messageModel m1, messageModel m2) {
                        return Long.compare(m1.getTimestamp(), m2.getTimestamp());
                    }
                });
            }


            // Safely update UI on main thread
            if (chatAdapter != null && binding != null && binding.messageRecView != null) {
                runOnUiThread(() -> {
                    // Update the main messageList reference to maintain consistency
                    messageList.clear();
                    messageList.addAll(updatedMessageList);

                    // Update the adapter with the new list using DiffUtil
                    otherFunctions.updateMessageList(updatedMessageList, chatAdapter);

                    // Check if the new message is from receiver (not current user)
                    String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");
                    boolean isReceiverMessage = model.getUid() != null && !model.getUid().equals(currentUid);

                    // No animation for receiver messages
                    chatAdapter.setLastItemVisible(false);

                    binding.messageRecView.scrollToPosition(updatedMessageList.size() - 1); // Scroll to the end of the updated list
                    if (binding.progressBar != null) {
                        binding.progressBar.setVisibility(View.GONE);
                    }


                    Log.d("handleChildAdded", "Handling child added for key: " + key);

                    if (key != null) {
                        changeFlags.put(key, true); // mark first-time flag
                        //  Toast.makeText(mContext, "added", Toast.LENGTH_SHORT).show();
                        Log.d("*************", "added ");


                    }

                });
                Log.d("handleChildAddeddaca", "Message added with date: " + model.getMessage());
            } else {
                Log.w("handleChildAdded", "UI components are null, cannot update UI");
            }
        } catch (Exception e) {
            Log.e("handleChildAdded", "Error processing message: " + e.getMessage(), e);
        }
    }

    public void getAllMessages(String receiverUid, Context mContext) {
        Log.d("getAllMessages", "Loading messages from database for receiverUid: " + receiverUid);
        List<messagemodel2> messages2 = new DatabaseHelper(mContext).getMessagesFromDatabase(receiverUid);
        List<messageModel> messages = convertToMessageModel(messages2);
        Log.d("getAllMessages", "Retrieved " + messages.size() + " messages from database");

        for (messageModel model : messages) {
            updateMessageList(model);
            // 🔍 Print messageModel data to Logcat
            Log.d("getAllMessages", "Message: " +
                    "\nText: " + model.getMessage() +
                    "\nTime: " + model.getTime() +
                    "\nUID: " + model.getUid() +
                    "\nDate: " + model.getCurrentDate() +
                    "\nType: " + model.getDataType() +
                    "\nIs Forwarded: " + model.getForwaredKey());


//            if (model.getDataType().equals(Constant.Text)) {
//                if (binding.limitStatus.getText().toString().equals("0")) {
//                    UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, model.getUid(), userFTokenKey);
//                    uploadHelper.uploadContent(Constant.Text, null, model.getMessage(), model.getModelId(), null, model.getFileNameThumbnail(), null, model.getName(), model.getPhone(), model.getMiceTiming(), null, model.getExtension(), receiverUid, model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(), model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(), "", "", "");
//                } else {
//                    Constant.showCustomToast(
//                            "Msg limit set for privacy in a day - " + binding.totalMsgLimit.getText().toString(),
//                            customToastCard, customToastText
//                    );
//                }
//            } else
//                if (model.getDataType().equals(Constant.img)) {
//                File file = new File(mContext.getCacheDir(), model.getFileName());
//
//                Uri uri = FileProvider.getUriForFile(
//                        mContext,
//                        "com.Appzia.enclosure",
//                        file
//                );
//
//
//                if (binding.limitStatus.getText().toString().equals("0")) {
//                    UploadChatHelper uploadHelper = new UploadChatHelper(mContext, file, file, model.getUid(), userFTokenKey);
//                    uploadHelper.uploadContent(
//                            Constant.img,
//                            uri, // uri
//                            model.getCaption(), // captionText
//                            model.getModelId(), // modelId
//                            null, // savedThumbnail
//                            null, // fileThumbName
//                            model.getFileName(), // fileName
//                            null, // contactName
//                            null, // contactPhone
//                            null, // audioTime
//                            null, // audioName
//                            model.getExtension(), // extension
//                            receiverUid, model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(),// receiverUid
//                            model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(),
//                            model.getImageWidth(), model.getImageHeight(), model.getAspectRatio());
//                } else {
//                    Constant.showCustomToast(
//                            "Msg limit set for privacy in a day - " + binding.totalMsgLimit.getText().toString(),
//                            customToastCard, customToastText
//                    );
//                }
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
//                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, file, file, model.getUid(), userFTokenKey);
//
//                uploadHelper.uploadContent(
//                        Constant.doc,
//                        uri,
//                        model.getCaption(),
//                        model.getModelId(),
//                        null,
//                        null,
//                        model.getFileName(),
//                        null,
//                        null,
//                        null,
//                        null,
//                        model.getExtension(),
//                        receiverUid, "", "", "", "",
//                        "", "", "", "", "", "", "");
//            } else
//                if (model.getDataType().equals(Constant.contact)) {
//                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, model.getUid(), userFTokenKey);
//                uploadHelper.uploadContent(
//                        Constant.contact, // uploadType
//                        null, // uri
//                        model.getCaption(), // captionText
//                        model.getModelId(), // modelId
//                        null, // savedThumbnail
//                        null, // fileThumbName
//                        null, // fileName
//                        model.getName(), // contactName
//                        model.getPhone(), // contactPhone
//                        null, // audioTime
//                        null, // audioName
//                        null, // extension
//                        receiverUid // receiverUid
//                        , "", "", "", "",
//                        "", "", "", "", "", "", "");
//            } else
//                if (model.getDataType().equals(Constant.video)) {
//
//                File file = new File(mContext.getCacheDir(), model.getFileName());
//
//                if (file.exists()) {
//                    Log.d("FileStatus", "File exists: " + file.getAbsolutePath());
//                } else {
//                    Log.e("FileStatus", "File does NOT exist: " + file.getAbsolutePath());
//                }
//
//                Uri uri = FileProvider.getUriForFile(
//                        mContext,
//                        "com.Appzia.enclosure",
//                        file
//                );
//
//                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, file, file, model.getUid(), userFTokenKey);
//                uploadHelper.uploadContent(
//                        Constant.video, // uploadType
//                        uri, // uri
//                        model.getCaption(), // captionText
//                        model.getModelId(), // modelId
//                        file, // savedThumbnail
//                        model.getFileNameThumbnail(), // fileThumbName
//                        model.getFileName(), // fileName
//                        null, // contactName
//                        null, // contactPhone
//                        null, // audioTime
//                        null, // audioName
//                        model.getExtension(), // extension
//                        receiverUid // receiverUid
//                        , "", "", "", "",
//                        model.getReplytextData(), model.getDataType(), model.getFileName(), "", model.getImageWidth(), model.getImageHeight(), model.getAspectRatio());
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
//                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, file, null, model.getUid(), userFTokenKey);
//                uploadHelper.uploadContent(
//                        Constant.voiceAudio,
//                        uri,
//                        model.getCaption(),
//                        model.getModelId(),
//                        null,
//                        null,
//                        model.getFileName(),
//                        null,
//                        null,
//                        model.getMiceTiming(),
//                        model.getName(),
//                        model.getExtension(),
//                        receiverUid
//                        , "", "", "", "",
//                        model.getReplytextData(), model.getDataType(), model.getFileName(), "", "", "", "");
//            }

        }
        runOnUiThread(() -> {
            chatAdapter.setLastItemVisible(true); // Ensure the last item is visible
            chatAdapter.itemAdd(binding.messageRecView);
            binding.messageBox.setEnabled(true);
        });
    }

    private List<messageModel> convertToMessageModel(List<messagemodel2> messages2) {
        List<messageModel> messages = new ArrayList<>();
        for (messagemodel2 model2 : messages2) {
            messageModel model = new messageModel(
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
                    model2.getUserName(),
                    model2.getReplytextData(),
                    model2.getReplyKey(),
                    model2.getReplyType(),
                    model2.getReplyOldData(),
                    model2.getReplyCrtPostion(),
                    model2.getModelId(),
                    model2.getReceiverUid(),
                    model2.getForwaredKey(),
                    model2.getGroupName(),
                    model2.getDocSize(),
                    model2.getFileName(),
                    model2.getThumbnail(),
                    model2.getFileNameThumbnail(),
                    model2.getCaption(),
                    model2.getNotification(),
                    model2.getCurrentDate(),
                    model2.getEmojiModel(),
                    model2.getEmojiCount(),
                    model2.getTimestamp(),
                    model2.getImageWidth(),
                    model2.getImageHeight(),
                    model2.getAspectRatio(),
                    model2.getSelectionCount(),
                    null // selectionBunch - not available in local database
            );
            messages.add(model);
        }
        return messages;
    }


    // Updated to handle messagemodel
    private void removeDuplicatesAndAddToMessageList(List<messageModel> messages) {
        synchronized (messageList) {
            for (messageModel newModel : messages) {
                String modelId = newModel.getModelId();
                int existingIndex = -1;
                for (int i = 0; i < messageList.size(); i++) {
                    messageModel existingModel = messageList.get(i);
                    if (existingModel != null && modelId != null && modelId.equals(existingModel.getModelId())) {
                        existingIndex = i;
                        break;
                    }
                }
                if (existingIndex != -1) {
                    messageList.remove(existingIndex);
                    Log.d("removeDuplicatesAndAdd", "Duplicate found, removed message with ID: " + modelId);
                }
                messageList.add(newModel);
            }
            Log.d("removeDuplicatesAndAdd", "Messages added to messageList");
        }
    }


    private void updateUI() {
        if (chatAdapter != null && binding != null && binding.messageRecView != null) {
            runOnUiThread(() -> {
                chatAdapter.notifyDataSetChanged();
                // Removed scrollToBottomSafely from updateUI
                // if (isFirstTimeOnCreate && messageList.size() > 0) {
                //     scrollToBottomSafely();
                // }
                if (binding.progressBar != null) {
                    binding.progressBar.setVisibility(View.GONE);
                }
            });
            Log.d("updateUI", "UI updated successfully");
        } else {
            Log.w("updateUI", "UI components are null, cannot update UI");
        }
    }


    // ✅ Same onChildRemoved logic extracted
    private void handleChildRemoved(DataSnapshot snapshot, String receiverRoom) {
        Log.d("handleChildRemoved", "Handling child removed for key: " + snapshot.getKey());
        messageModel model = snapshot.getValue(messageModel.class);
        if (model == null) return;

        // Parse selectionBunch from Firebase
        messageModel.parseSelectionBunchFromSnapshot(snapshot, model);

        String removedModelId = model.getModelId();
        Log.d("handleChildRemoved", "Checking if message with ID " + removedModelId + " exists in DB.");

        database.getReference().child(Constant.CHAT)
                .child(receiverRoom)
                .child(removedModelId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Only proceed if the message does NOT exist (confirmed deletion)
                        if (!snapshot.exists()) {
                            Log.d("handleChildRemoved", "Message confirmed deleted from DB: " + removedModelId);

                            Constant.getSfFuncion(mContext);
                            String deletedModelId = Constant.getSF.getString(Constant.last_deleted_model_id, "");

                            new Handler(Looper.getMainLooper()).post(() -> {
                                for (int i = 0; i < messageList.size(); i++) {
                                    if (messageList.get(i).getModelId().equals(removedModelId)) {
                                        messageList.remove(i);
                                        break;
                                    }
                                }

                                messageList.sort(Comparator.comparingLong(m -> m.getTimestamp()));
                                chatAdapter.notifyDataSetChanged(); // Avoid mixing with notifyItemRemoved()
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("handleChildRemoved", "Error while checking removed message from DB: " + error.getMessage());
                    }
                });
    }


    /// -------------------------------------------------------------------------------------------------- todo END
// Assuming this method is within your Activity or Fragment
    public void loadMore(String receiverRoom, String receiverUid) {
        Log.d("*************", "loadmore");
        if (isLoading) return;
        isLoading = true;

        DatabaseReference chatRef = database.getReference().child(Constant.CHAT).child(receiverRoom);
        Query query;

        if (lastTimestamp != null) {
            // ✅ Load older messages than the currently oldest
            query = chatRef.orderByChild("timestamp")
                    .endBefore(lastTimestamp)
                    .limitToLast(PAGE_SIZE);
        } else {
            // ✅ First load: fetch the latest PAGE_SIZE messages
            query = chatRef.orderByChild("timestamp")
                    .limitToLast(PAGE_SIZE);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<messageModel> fetchedNewMessages = new ArrayList<>();
                Long newLastTimestamp = null;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotKey = snapshot.getKey();
                    messageModel model = snapshot.getValue(messageModel.class);

                    if (model != null && snapshotKey != null) {
                        // Parse selectionBunch from Firebase
                        messageModel.parseSelectionBunchFromSnapshot(snapshot, model);
                        try {
                            // ✅ Decrypt fields - preserved from your original code
                            String decryptedModelId = model.getModelId();
                            String decryptedUid = model.getUid();
                            String decryptedMessage = model.getMessage();
                            String decryptedTime = model.getTime();
                            String decryptedDocument = model.getDocument();
                            String decryptedDataType = model.getDataType();
                            String decryptedExtension = model.getExtension();
                            String decryptedName = model.getName();
                            String decryptedPhone = model.getPhone();
                            String decryptedMicPhoto = model.getMicPhoto();
                            String decryptedMiceTiming = model.getMiceTiming();
                            String decryptedUserName = model.getUserName();
                            String decryptedReplytextData = model.getReplytextData();
                            String decryptedReplyKey = model.getReplyKey();
                            String decryptedReplyType = model.getReplyType();
                            String decryptedReplyOldData = model.getReplyOldData();
                            String decryptedReplyCrtPosition = model.getReplyCrtPostion();
                            String decryptedReceiverUid = model.getReceiverUid();
                            String decryptedForwaredKey = model.getForwaredKey();
                            String decryptedGroupName = model.getGroupName();
                            String decryptedDocSize = model.getDocSize();
                            String decryptedFileName = model.getFileName();
                            String decryptedThumbnail = model.getThumbnail();
                            String decryptedFileNameThumbnail = model.getFileNameThumbnail();
                            String decryptedCaption = model.getCaption();
                            ArrayList<emojiModel> decryptedemoji = model.getEmojiModel();
                            String decryptedemojiCount = model.getEmojiCount();

                            // ❌ BUG in your original code fixed here:
                            // long decryptedTimestamp = model.getTimestamp() == 0 ? model.getTimestamp() : 0;
                            // That always gave 0
                            long decryptedTimestamp = model.getTimestamp(); // ✅ correct

                            String decryptedImageWidth = model.getImageWidth();
                            String decryptedImageHeight = model.getImageHeight();
                            String decryptedAspectRatio = model.getAspectRatio();

                            // ✅ Avoid duplicate messages
                            boolean exists = false;
                            for (messageModel existingModel : messageList) {
                                if (existingModel.getModelId().equals(decryptedModelId)) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                String uniqDate = model.getCurrentDate();
                                messageModel newModel = new messageModel(
                                        decryptedUid, decryptedMessage, decryptedTime,
                                        decryptedDocument, decryptedDataType, decryptedExtension,
                                        decryptedName, decryptedPhone, decryptedMicPhoto,
                                        decryptedMiceTiming, decryptedUserName, decryptedReplytextData,
                                        decryptedReplyKey, decryptedReplyType, decryptedReplyOldData,
                                        decryptedReplyCrtPosition, decryptedModelId, decryptedReceiverUid,
                                        decryptedForwaredKey, decryptedGroupName, decryptedDocSize,
                                        decryptedFileName, decryptedThumbnail, decryptedFileNameThumbnail,
                                        decryptedCaption, model.getNotification(), ":" + uniqDate,
                                        decryptedemoji, decryptedemojiCount, decryptedTimestamp,
                                        decryptedImageWidth, decryptedImageHeight, decryptedAspectRatio, model.getSelectionCount(), model.getSelectionBunch()
                                );

                                fetchedNewMessages.add(newModel);

                                // ✅ Track the oldest timestamp
                                long ts = decryptedTimestamp;
                                if (newLastTimestamp == null || ts < newLastTimestamp) {
                                    newLastTimestamp = ts;
                                }
                            }

                        } catch (Exception e) {
                            Log.e("DecryptionError", "Failed to decrypt data: " + e.getMessage());
                        }
                    }

                    if (snapshotKey != null) {

                        changeFlags.put(snapshotKey, true); // mark first-time flag
                    }
                }

                // ✅ Merge results
                if (!fetchedNewMessages.isEmpty()) {
                    lastTimestamp = newLastTimestamp; // update for next pagination

                    // Merge: prepend new messages
                    List<messageModel> combinedList = new ArrayList<>();
                    combinedList.addAll(fetchedNewMessages);
                    combinedList.addAll(messageList);

                    // ✅ Ensure chronological order
                    Collections.sort(combinedList, (m1, m2) ->
                            Long.compare(m1.getTimestamp(),
                                    m2.getTimestamp())
                    );

                    // ✅ Update adapter
                    otherFunctions.updateMessageList(combinedList, chatAdapter);

                    // ✅ Keep scroll position after prepending
                    layoutManager.scrollToPositionWithOffset(fetchedNewMessages.size(), 0);

                    // ✅ Update reference
                    messageList = (ArrayList<messageModel>) combinedList;
                }

                isLoading = false; // ✅ always reset
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "loadMore:onCancelled", error.toException());
                isLoading = false; // ✅ reset here too
            }
        });
    }


    // Assuming this method is within your Activity or Fragment
    public void loadMoreRedirection(String receiverRoom, String receiverUid) {
        Log.d("*************", "loadMoreRedirection");

        if (isLoading) return;
        isLoading = true;

        Query query = database.getReference().child(Constant.CHAT)
                .child(receiverRoom)
                .orderByKey()
                .limitToLast(PAGE_SIZE);

        if (lastKey != null) {
            query = query.endBefore(lastKey); // Fetch older messages
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<messageModel> fetchedNewMessages = new ArrayList<>(); // Renamed for clarity
                String newLastKey = null;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotKey = snapshot.getKey();
                    messageModel model = snapshot.getValue(messageModel.class);

                    if (model != null && snapshotKey != null) {
                        // Parse selectionBunch from Firebase
                        messageModel.parseSelectionBunchFromSnapshot(snapshot, model);
                        try {
                            // Decrypt fields - keeping your existing decryption logic
                            String decryptedModelId = model.getModelId();
                            String decryptedUid = model.getUid();
                            String decryptedMessage = model.getMessage();
                            String decryptedTime = model.getTime();
                            String decryptedDocument = model.getDocument();
                            String decryptedDataType = model.getDataType();
                            String decryptedExtension = model.getExtension();
                            String decryptedName = model.getName();
                            String decryptedPhone = model.getPhone();
                            String decryptedMicPhoto = model.getMicPhoto();
                            String decryptedMiceTiming = model.getMiceTiming();
                            String decryptedUserName = model.getUserName();
                            String decryptedReplytextData = model.getReplytextData();
                            String decryptedReplyKey = model.getReplyKey();
                            String decryptedReplyType = model.getReplyType();
                            String decryptedReplyOldData = model.getReplyOldData();
                            String decryptedReplyCrtPosition = model.getReplyCrtPostion();
                            String decryptedReceiverUid = model.getReceiverUid();
                            String decryptedForwaredKey = model.getForwaredKey();
                            String decryptedGroupName = model.getGroupName();
                            String decryptedDocSize = model.getDocSize();
                            String decryptedFileName = model.getFileName();
                            String decryptedThumbnail = model.getThumbnail();
                            String decryptedFileNameThumbnail = model.getFileNameThumbnail();
                            String decryptedCaption = model.getCaption();
                            ArrayList<emojiModel> decryptedemoji = model.getEmojiModel();
                            String decryptedemojiCount = model.getEmojiCount();
                            long decryptedtimestamp = model.getTimestamp();
                            String decryptedImageWidth = model.getImageWidth();
                            String decryptedImageHeight = model.getImageHeight();
                            String decryptedAspectRatio = model.getAspectRatio();

                            // Check for duplicates (DiffUtil will also handle this, but explicit check is fine)
                            boolean exists = false;
                            for (messageModel existing : messageList) { // Check against the current messageList
                                if (existing.getModelId().equals(decryptedModelId)) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                String uniqDate = model.getCurrentDate();
                                String formattedDate = ":" + uniqDate;

                                messageModel decryptedModel = new messageModel(
                                        decryptedUid, decryptedMessage, decryptedTime, decryptedDocument,
                                        decryptedDataType, decryptedExtension, decryptedName, decryptedPhone,
                                        decryptedMicPhoto, decryptedMiceTiming, decryptedUserName,
                                        decryptedReplytextData, decryptedReplyKey, decryptedReplyType,
                                        decryptedReplyOldData, decryptedReplyCrtPosition, decryptedModelId,
                                        decryptedReceiverUid, decryptedForwaredKey, decryptedGroupName,
                                        decryptedDocSize, decryptedFileName, decryptedThumbnail,
                                        decryptedFileNameThumbnail, decryptedCaption, model.getNotification(),
                                        formattedDate, decryptedemoji, decryptedemojiCount, decryptedtimestamp, decryptedImageWidth, decryptedImageHeight, decryptedAspectRatio, model.getSelectionCount(), model.getSelectionBunch()
                                );

                                fetchedNewMessages.add(decryptedModel); // Add to the list of newly fetched messages

                                // Track the smallest (oldest) key for pagination
                                if (newLastKey == null || snapshotKey.compareTo(newLastKey) < 0) {
                                    newLastKey = snapshotKey;
                                }
                            }

                        } catch (Exception e) {
                            Log.e("DecryptionError", "Failed to decrypt: " + e.getMessage());
                        }
                    }

                    if (snapshotKey != null) {
                        changeFlags.put(snapshotKey, true); // mark first-time flag
                    }
                }

                // --- Integration with updateMessageList ---
                if (!fetchedNewMessages.isEmpty()) {
                    lastKey = newLastKey; // Set for next pagination

                    // Create a new combined list: new messages at the top, then existing messages
                    List<messageModel> combinedList = new ArrayList<>();
                    combinedList.addAll(fetchedNewMessages); // Add the newly fetched messages first
                    combinedList.addAll(messageList);         // Then add the messages already in the list

                    // Update the adapter with the new combined list
                    // This will trigger DiffUtil to calculate changes and update the RecyclerView
                    otherFunctions.updateMessageList(combinedList, chatAdapter);

                    // IMPORTANT: Update your Activity/Fragment's 'messageList' reference
                    // to point to this new combined list. This is crucial for consistency
                    // in subsequent calls to loadMore or handleChildAdded.
                    messageList = (ArrayList<messageModel>) combinedList; // Assuming messageList is a class member in your Activity/Fragment

                    // You might want to scroll to the first newly loaded item, or maintain scroll position
                    // binding.messageRecView.scrollToPosition(fetchedNewMessages.size() - 1); // Scrolls to the last of the newly added messages
                    // Or if you want to maintain current view:
                    // layoutManager.scrollToPositionWithOffset(fetchedNewMessages.size(), 0);
                }

                isLoading = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", "loadMoreRedirection:onCancelled", databaseError.toException());
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

    public void setAdapteEMojir(List<Emoji> emojis) {

        adapter = new emojiAdapter(mContext, emojis, binding.messageBox);
        emojiRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 9));
        emojiRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    public static void expandXYZ(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(a);
    }

    public static void collapseXYZ(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
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

    private void searchMessages(String query, String receiverRoom) {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference()
                .child(Constant.CHAT)
                .child(receiverRoom);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    messageModel model = child.getValue(messageModel.class);
                    if (model != null && model.getMessage() != null) {
                        // Parse selectionBunch from Firebase
                        messageModel.parseSelectionBunchFromSnapshot(child, model);
                        if (model.getMessage().toLowerCase().contains(query.toLowerCase())) {
                            messageList.add(model);
                        }
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SearchError", error.getMessage());
            }
        });
    }


//    private void updateBlurEffect(boolean blur) {
//        int childCount = binding.messageRecView.getChildCount();
//        float alpha = blur ? 0.7f : 1.0f;
//
//        for (int i = 0; i < childCount; i++) {
//            View child = binding.messageRecView.getChildAt(i);
//            child.setAlpha(alpha);
//        }
//    }

    private void updateBlurEffect(boolean blur) {
        float startAlpha = blur ? 1.0f : 0.1f;
        float endAlpha = blur ? 0.1f : 1.0f;

        ValueAnimator animator = ValueAnimator.ofFloat(startAlpha, endAlpha);
        animator.setDuration(150);
        animator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            int childCount = binding.messageRecView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = binding.messageRecView.getChildAt(i);
                child.setAlpha(alpha);
            }
        });
        animator.start();
    }

    private int dpToPx(int dp) {
        return (int) (dp * mContext.getResources().getDisplayMetrics().density);
    }

    private int getKeyboardHeight() {
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int screenHeight = getWindow().getDecorView().getRootView().getHeight();
        int keyboardHeight = screenHeight - rect.bottom;
        return keyboardHeight < 0 ? 0 : keyboardHeight;
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

    private messageModel createMessageModel(String message, String modelId, String currentDateTimeString, String senderId, String receiverUid) {
        ArrayList<emojiModel> emojiModels = new ArrayList<>();
        emojiModels.add(new emojiModel("", ""));

        messageModel model;

        // Reply logic
        if (binding.replylyout.getVisibility() == View.VISIBLE) {
            float elevationDp = 10.0f; // 10 dp
            float elevationPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    elevationDp,
                    mContext.getResources().getDisplayMetrics()
            );
            binding.bottom.setElevation(elevationPx);
            collapse(binding.replylyout);

            String replyText = binding.msgreply.getText().toString();
            Log.e("SendGroupClickListener", "replyText: " + replyText);
            Log.e("SendGroupClickListener", "daadaa: " + binding.imageWidthDp.getText().toString());
            if (replyText.equals("Photo")) {
                model = new messageModel(
                        senderId, message, currentDateTimeString, "", Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), binding.messageBox.getText().toString().trim(),
                        Constant.ReplyKey, Constant.Text, HalfSwipeCallback.images, binding.listcrntpostion.getText().toString(),
                        modelId, receiverUid, "", "", "", binding.fileNameTextview.getText().toString(), "",
                        "", binding.captionTextView.getText().toString(), 1, Constant.getCurrentDate(),
                        emojiModels, "", Constant.getCurrentTimestamp(), binding.imageWidthDp.getText().toString(), binding.imageHeightDp.getText().toString(), binding.aspectRatio.getText().toString(), "1"
                );
                Log.e("SendGroupClickListener", "replyText: " + model.getDataType().toString() + model.getFileName());
            } else if (replyText.equals("Video")) {
                assert binding.thumbnailimagedata != null;
                model = new messageModel(
                        senderId, message, currentDateTimeString, binding.documentTextview.getText().toString(),
                        Constant.video, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""),
                        binding.messageBox.getText().toString().trim(), Constant.ReplyKey, Constant.Text,
                        HalfSwipeCallback.ThumbnailImage, binding.listcrntpostion.getText().toString(),
                        modelId, receiverUid, "", "", "", binding.fileNameTextview.getText().toString(),
                        binding.thumbnailimagedata.getText().toString(), binding.thumbnailTextview.getText().toString(), binding.captionTextView.getText().toString(),
                        1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(), binding.imageWidthDp.getText().toString(), binding.imageHeightDp.getText().toString(), binding.aspectRatio.getText().toString(), "1"
                );
            } else if (replyText.equals("Contact")) {
                model = new messageModel(
                        senderId, message, currentDateTimeString, "", Constant.contact, "", HalfSwipeCallback.Cname,
                        HalfSwipeCallback.Cphone, "", "", Constant.getSF.getString(Constant.full_name, ""),
                        binding.messageBox.getText().toString().trim(), Constant.ReplyKey, Constant.Text,
                        binding.firstText.getText().toString(), binding.listcrntpostion.getText().toString(),
                        modelId, receiverUid, "", "", "", binding.fileNameTextview.getText().toString(),
                        "", "", binding.captionTextView.getText().toString(), 1, Constant.getCurrentDate(),
                        emojiModels, "", Constant.getCurrentTimestamp(), binding.imageWidthDp.getText().toString(), binding.imageHeightDp.getText().toString(), binding.aspectRatio.getText().toString(), "1"
                );
            } else if (replyText.equals("Audio")) {
                model = new messageModel(
                        senderId, message, currentDateTimeString, "", Constant.voiceAudio, "", "", "", "",
                        HalfSwipeCallback.miceTiming, Constant.getSF.getString(Constant.full_name, ""),
                        binding.messageBox.getText().toString().trim(), Constant.ReplyKey, Constant.Text,
                        HalfSwipeCallback.micePhoto, binding.listcrntpostion.getText().toString(),
                        modelId, receiverUid, "", "", "", binding.fileNameTextview.getText().toString(),
                        "", "", binding.captionTextView.getText().toString(), 1, Constant.getCurrentDate(),
                        emojiModels, "", Constant.getCurrentTimestamp(), binding.imageWidthDp.getText().toString(), binding.imageHeightDp.getText().toString(), binding.aspectRatio.getText().toString(), "1"
                );
            } else if (replyText.equals("Document")) {
                model = new messageModel(
                        senderId, message, currentDateTimeString, "", binding.pageText.getText().toString(), HalfSwipeCallback.extension, "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), binding.messageBox.getText().toString().trim(),
                        Constant.ReplyKey, Constant.Text, binding.replyDataType.getText().toString(),
                        binding.listcrntpostion.getText().toString(), modelId, receiverUid, "", "", "",
                        binding.fileNameTextview.getText().toString(), "", "", binding.captionTextView.getText().toString(),
                        1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(), binding.imageWidthDp.getText().toString(), binding.imageHeightDp.getText().toString(), binding.aspectRatio.getText().toString(), "1"
                );
            } else {

                model = new messageModel(
                        senderId, message, currentDateTimeString, "", Constant.Text, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), binding.messageBox.getText().toString().trim(),
                        Constant.ReplyKey, Constant.Text, binding.msgreply.getText().toString(),
                        binding.listcrntpostion.getText().toString(), modelId, receiverUid, "", "", "", "", "", "", "",
                        1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(), binding.imageWidthDp.getText().toString(), binding.imageHeightDp.getText().toString(), binding.aspectRatio.getText().toString(), "1"

                );

                Log.e("SendGroupClickListener", "SUccess1: " + model.getReplytextData());
            }
        } else {
            // Non-reply message
            Log.d("TAG", "createMessageModel: " + "No reply message");
            model = new messageModel(
                    senderId, message, currentDateTimeString, "", Constant.Text, "", "", "", "", "",
                    Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                    modelId, receiverUid, "", "", "", "", "", "", "", 1, Constant.getCurrentDate(),
                    emojiModels, "", Constant.getCurrentTimestamp(), binding.imageWidthDp.getText().toString(), binding.imageHeightDp.getText().toString(), binding.aspectRatio.getText().toString(), "1", null
            );
        }

        return model;
    }


    private void updateMessageList(messageModel model) {
        String uniqDate = model.getCurrentDate();
        if (uniqueDates.add(uniqDate)) {
            messageList.add(new messageModel(
                    model.getUid(), model.getMessage(), model.getTime(), model.getDocument(),
                    model.getDataType(), model.getExtension(), model.getName(), model.getPhone(),
                    model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(),
                    model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(),
                    model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(),
                    model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(),
                    model.getCaption(), model.getNotification(), uniqDate, model.getEmojiModel(),
                    model.getEmojiCount(), model.getTimestamp(), "", "", "", model.getSelectionCount(), model.getSelectionBunch()
            ));
            Log.d("updateMessageList", "Added message to messageList. Total size: " + messageList.size());
        } else {
            messageList.add(new messageModel(
                    model.getUid(), model.getMessage(), model.getTime(), model.getDocument(),
                    model.getDataType(), model.getExtension(), model.getName(), model.getPhone(),
                    model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(),
                    model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(),
                    model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(),
                    model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(),
                    model.getCaption(), model.getNotification(), ":" + uniqDate, model.getEmojiModel(),
                    model.getEmojiCount(), model.getTimestamp(), "", "", "", model.getSelectionCount(), model.getSelectionBunch()
            ));
            Log.d("updateMessageList", "Added message to messageList (repeated date). Total size: " + messageList.size());
        }

        Log.d("TAG", "updateMessageList: " + messageList.get(0).getDataType());
    }


    private void keepKeyboardVisible(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    public interface OnMessagesFetchedListener {
        void onMessagesFetched();
    }


    private boolean hasPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            boolean granted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
            Log.d(TAG, "Checking permission " + permission + ": " + (granted ? "Granted" : "Denied"));
            if (!granted) {
                return false;
            }
        }
        return true;
    }

    @OptIn(markerClass = UnstableApi.class)
    private void dispatchTakePictureIntent() {
        if (messageList == null) {
            messageList = new ArrayList<>();
            Log.w(TAG, "messageList was null in activity, initialized to empty ArrayList");
        }
        if (uniqueDates == null) {
            uniqueDates = new HashSet<>();
            Log.w(TAG, "uniqueDates was null in activity, initialized to empty HashSet");
        }

        CameraGalleryFragment fragment = CameraGalleryFragment.newInstance(
                modelId,
                userFTokenKey,
                isLastItemVisible,
                messageList,
                true,
                uniqueDates,
                chatAdapter,
                binding.messageRecView
                // permissionsGranted
        );

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Method to stop progress indicator for a specific message
    public void stopMessageProgress(String modelId) {
        try {
            if (chatAdapter != null) {
                chatAdapter.stopProgressIndicator(modelId);
            }
        } catch (Exception e) {
            Log.e("ProgressIndicator", "Error stopping message progress: " + e.getMessage());
        }
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
     * Setup Gallery Recent Images View
     */
    private void setupGalleryRecentView() {
        Log.d("GalleryDebug", "Setting up gallery recent images view...");

        // Initialize the gallery recent view
        // The view is already defined in the layout, just need to set up any initial state
        binding.galleryRecentLyt.setVisibility(View.GONE);
        binding.bottomview.setVisibility(View.GONE);

        // Setup permission text visibility and click handling
        setupPermissionText();

        Log.d("GalleryDebug", "Gallery recent view setup completed");
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
                    SwipeNavigationHelper.startActivityWithSwipe(chattingScreen.this, intent);
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
            //
            Log.d("GalleryDebug", "Images loaded for immediate display, count: " + imagePaths.size());
        }

        binding.messageBox.requestFocus();
    }

    /**
     * Show the gallery recent images view
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
     * Hide the gallery recent images view
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