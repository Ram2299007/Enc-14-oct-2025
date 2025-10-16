package com.Appzia.enclosure.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.provider.Settings;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FileOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.media3.common.Player;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.DefaultTimeBar;
import androidx.media3.ui.PlayerView;
import androidx.media3.ui.TimeBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.chatAdapter;
import com.Appzia.enclosure.Utils.ChatadapterFiles.senderReceiverDownload;
import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.messagemodel2;
import com.Appzia.enclosure.Utils.ChatadapterFiles.otherFunctions;
import com.Appzia.enclosure.Model.selectionBunchModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelper;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.FileUtils;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.databinding.FragmentCameraGalleryBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@UnstableApi
public class CameraGalleryFragment extends Fragment {
    private static final String TAG = "CameraGalleryFragment";
    private FragmentCameraGalleryBinding binding;
    private ImageCapture imageCapture;
    private VideoCapture<Recorder> videoCapture;
    private CardView customToastCard;
    private TextView customToastText;
    private Recording activeRecording;
    private boolean isRecording = false;
    private ExecutorService cameraExecutor;
    private Uri globalUri;
    private boolean isFlashOn = false;
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private long recordingStartTime = 0;
    private Camera camera;
    private GalleryAdapter galleryAdapter;
    private final List<Uri> galleryImages = new ArrayList<>();
    private String currentCaption = ""; // Single caption for all media items
    private ActivityResultLauncher<String[]> permissionLauncher;
    private ProcessCameraProvider cameraProvider;
    private boolean wasPlaying; // Initialize somewhere in your class
    private Handler handler = new Handler(Looper.getMainLooper()); // Initialize
    private Runnable updateProgressAction; // Initialize
    private CameraSelector cameraSelector;
    private boolean isBackCamera = true;
    private int currentPage = 0;
    private static final int INITIAL_PAGE_SIZE = 12;
    private static final int PAGE_SIZE = 40;
    private boolean isLoading = false;
    private String modelId;
    private String userFTokenKey;
    private boolean isLastItemVisible;
    private ArrayList<messageModel> messageList;
    private boolean isPhotoMode = true;

    private TextView tabPhoto;
    private TextView tabVideo;

    private static final String[] REQUIRED_PERMISSIONS = getRequiredPermissions();
    private FirebaseDatabase database;
    private File globalFile;
    private File fullImageFile;
    private RecyclerView messageRecView;
    private chatAdapter chatAdapter;
    private static final Set<String> uniqueDates = new HashSet<>();
    private ExoPlayer playerPreview; // Added for video preview in dialog
    private int resizeModeIndex = 0; // Added for video preview in dialog

    private static String[] getRequiredPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.RECORD_AUDIO);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES);
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO);
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        return permissions.toArray(new String[0]);
    }

    public static CameraGalleryFragment newInstance(String modelId, String userFTokenKey,
                                                    boolean isLastItemVisible,
                                                    ArrayList<messageModel> messageList,
                                                    boolean permissionsGranted,
                                                    Set<String> uniqueDates,
                                                    com.Appzia.enclosure.Adapter.chatAdapter chatAdaptera,
                                                    RecyclerView messageRecViews) {
        CameraGalleryFragment fragment = new CameraGalleryFragment();
        Bundle args = new Bundle();
        args.putString("modelId", modelId);
        args.putString("userFTokenKey", userFTokenKey);
        args.putBoolean("isLastItemVisible", isLastItemVisible);
        args.putSerializable("messageList", messageList);
        args.putBoolean("permissionsGranted", permissionsGranted);
        args.putStringArrayList("uniqueDates", new ArrayList<>(uniqueDates));

        // Set instance variables for this specific fragment
        fragment.chatAdapter = chatAdaptera;
        fragment.messageRecView = messageRecViews;

        Log.d(TAG, "newInstance - Setting chatAdapter: " + (chatAdaptera != null ? "not null" : "null"));
        Log.d(TAG, "newInstance - Setting messageRecView: " + (messageRecViews != null ? "not null" : "null"));
        Log.d(TAG, "newInstance - After setting, fragment.chatAdapter: " + (fragment.chatAdapter != null ? "not null" : "null"));
        Log.d(TAG, "newInstance - After setting, fragment.messageRecView: " + (fragment.messageRecView != null ? "not null" : "null"));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate - messageRecView: " + (messageRecView != null ? "not null" : "null"));
        Log.d(TAG, "onCreate - chatAdapter: " + (chatAdapter != null ? "not null" : "null"));

        // Initialize variables from savedInstanceState or arguments
        if (savedInstanceState != null) {
            initializeFromSavedInstanceState(savedInstanceState);
        } else if (getArguments() != null) {
            initializeFromArguments();
        } else {
            initializeDefaults();
        }

        Log.d(TAG, "onCreate after init - messageRecView: " + (messageRecView != null ? "not null" : "null"));
        Log.d(TAG, "onCreate after init - chatAdapter: " + (chatAdapter != null ? "not null" : "null"));

        cameraExecutor = Executors.newSingleThreadExecutor();
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> handlePermissionResult(permissions));
    }

    private void initializeFromSavedInstanceState(Bundle savedInstanceState) {
        messageList = (ArrayList<messageModel>) savedInstanceState.getSerializable("messageList");
        ArrayList<String> uniqueDatesList = savedInstanceState.getStringArrayList("uniqueDates");
        uniqueDates.clear();
        if (uniqueDatesList != null) {
            uniqueDates.addAll(uniqueDatesList);
        }
        isPhotoMode = savedInstanceState.getBoolean("isPhotoMode", true);
        Log.d(TAG, "Restored from savedInstanceState: messageList=" +
                (messageList != null ? messageList.size() : "null") +
                ", uniqueDates=" + uniqueDates.size() +
                ", isPhotoMode=" + isPhotoMode);
    }

    private void initializeFromArguments() {
        modelId = getArguments().getString("modelId");
        userFTokenKey = getArguments().getString("userFTokenKey");
        isLastItemVisible = getArguments().getBoolean("isLastItemVisible");
        messageList = (ArrayList<messageModel>) getArguments().getSerializable("messageList");
        ArrayList<String> uniqueDatesList = getArguments().getStringArrayList("uniqueDates");
        uniqueDates.clear();
        if (uniqueDatesList != null) {
            uniqueDates.addAll(uniqueDatesList);
        }
        if (messageList == null) {
            messageList = new ArrayList<>();
            Log.w(TAG, "messageList was null, initialized to empty ArrayList");
        }

        Log.d(TAG, "initializeFromArguments - chatAdapter: " + (chatAdapter != null ? "not null" : "null"));
        Log.d(TAG, "initializeFromArguments - messageRecView: " + (messageRecView != null ? "not null" : "null"));
        Log.d(TAG, "initializeFromArguments - messageList size: " + (messageList != null ? messageList.size() : "null"));
    }

    private void initializeDefaults() {
        messageList = new ArrayList<>();
        uniqueDates.clear();
        Log.w(TAG, "Arguments were null, initialized messageList and uniqueDates to empty");
    }

    private void handlePermissionResult(Map<String, Boolean> permissions) {
        boolean allGranted = true;
        for (String permission : REQUIRED_PERMISSIONS) {
            boolean granted = permissions.getOrDefault(permission, false);
            Log.d(TAG, "Permission " + permission + ": " + (granted ? "Granted" : "Denied"));
            if (!granted) {
                allGranted = false;
            }
        }
        if (allGranted) {
            Log.d(TAG, "All permissions granted, starting camera and gallery");
            startCamera();
            new LoadGalleryImagesTask(0, INITIAL_PAGE_SIZE, isPhotoMode, false).execute();
        } else {
            Toast.makeText(requireContext(), "Required permissions denied. Please grant all permissions.",
                    Toast.LENGTH_LONG).show();
            permissionLauncher.launch(REQUIRED_PERMISSIONS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCameraGalleryBinding.inflate(inflater, container, false);
        setLightStatusBar();

        customToastCard = binding.getRoot().findViewById(R.id.includedToast);
        customToastText = customToastCard.findViewById(R.id.toastText);

        tabPhoto = binding.getRoot().findViewById(R.id.tab_photo);
        tabVideo = binding.getRoot().findViewById(R.id.tab_video);

        binding.cameraPreview.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                focusOnPoint(event.getX(), event.getY());
            }
            return true;
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("PENDING_MESSAGES", "=== onViewCreated CALLED ===");
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated - messageRecView: " + (messageRecView != null ? "not null" : "null"));
        Log.d(TAG, "onViewCreated - chatAdapter: " + (chatAdapter != null ? "not null" : "null"));

        setupBottomSheet();
        initializeFirebase();
        setupGalleryRecyclerView();
        setupClickListeners();
        handleInitialPermissions();
        updateButtonStates(); // Call this here to set initial state of tabs and capture button
        
        // Setup permission text visibility
        setupPermissionText();
    }

    private void setupBottomSheet() {
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        int peekHeightPx = (int) (200 * getResources().getDisplayMetrics().density);
        bottomSheetBehavior.setPeekHeight(peekHeightPx);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setSkipCollapsed(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void initializeFirebase() {
        database = FirebaseDatabase.getInstance();
    }

    /**
     * Update multi-selection UI based on selection count
     */
    private void updateMultiSelectionUI(int selectionCount) {
        try {
            LinearLayout sendGrpLyt = getView().findViewById(R.id.multiSelectSendGrpLyt);
            TextView smallCounterText = getView().findViewById(R.id.multiSelectSmallCounterText);
            LinearLayout doneButton = getView().findViewById(R.id.multiSelectDoneButton);

            if (sendGrpLyt != null && smallCounterText != null && doneButton != null) {
                if (selectionCount > 0) {
                    sendGrpLyt.setVisibility(View.VISIBLE);
                    smallCounterText.setVisibility(View.VISIBLE);
                    smallCounterText.setText(String.valueOf(selectionCount));
                    doneButton.setEnabled(true);
                    doneButton.setAlpha(1.0f);

                    // Apply theme color to the send button
                    applyThemeColorToSendButton(doneButton);
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
     * Apply theme color to the multi-selection send button
     */
    private void applyThemeColorToSendButton(LinearLayout doneButton) {
        try {
            Constant.getSfFuncion(requireContext());
            String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themeColor));
            doneButton.setBackgroundTintList(tintList);
            Log.d("ThemeColor", "Applied theme color " + themeColor + " to multi-selection send button");
        } catch (Exception e) {
            Log.e("ThemeColor", "Error applying theme color to send button: " + e.getMessage());
        }
    }

    /**
     * Show/hide multi-selection UI
     */
    private void showMultiSelectionUI(boolean show) {
        try {
            LinearLayout sendGrpLyt = getView().findViewById(R.id.multiSelectSendGrpLyt);
            if (sendGrpLyt != null) {
                sendGrpLyt.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("MultiSelectionUI", "Error showing multi-selection UI: " + e.getMessage());
        }
    }

    /**
     * Setup multi-image preview with selected images
     */
    private void setupMultiImagePreview(ArrayList<Uri> selectedUris) {
        Log.d("DEBUG_VIDEO", "=== FRAGMENT setupMultiImagePreview() CALLED ===");
        Log.d("DEBUG_VIDEO", "Selected URIs: " + selectedUris.size());
        
        // Log all selected URIs and their types
        for (int i = 0; i < selectedUris.size(); i++) {
            Uri uri = selectedUris.get(i);
            String mimeType = requireContext().getContentResolver().getType(uri);
            Log.d("DEBUG_VIDEO", "Fragment - Selected URI " + i + ": " + uri + " (MIME: " + mimeType + ")");
        }

        if (selectedUris.isEmpty()) {
            Log.d("DEBUG_VIDEO", "No images selected, returning");
            return;
        }

        // Call the GalleryAdapter's setupMultiImagePreview method instead
        Log.d("DEBUG_VIDEO", "Calling GalleryAdapter.setupMultiImagePreview()");
        galleryAdapter.setupMultiImagePreview();
        // After dialog is up, set caption into dialogue's messageBoxMy
        try {
            EditText dlgCaption = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
            if (dlgCaption != null) {
                dlgCaption.setText(currentCaption != null ? currentCaption : "");
                dlgCaption.setSelection(dlgCaption.getText().length());
                Log.d("DEBUG_VIDEO", "Prefilled dialog caption: '" + currentCaption + "'");
            } else {
                Log.d("DEBUG_VIDEO", "Dialog caption EditText not found");
            }
        } catch (Exception e) {
            Log.e("DEBUG_VIDEO", "Failed to set dialog caption: " + e.getMessage());
        }
        Log.d("DEBUG_VIDEO", "GalleryAdapter.setupMultiImagePreview() completed");


    }

    /**
     * Setup horizontal gallery preview
     */
    private void setupHorizontalGalleryPreview() {
        ArrayList<Uri> selectedUris = galleryAdapter.getSelectedImageUris();
        Log.d("HorizontalGallery", "Setting up horizontal gallery with " + selectedUris.size() + " images");

        // मुख्य इमेज प्रिव्यू ViewPager2 सेटअप करा (Setup main image preview ViewPager2)
        Log.d("HorizontalGallery", "Finding mainImagePreview...");
        androidx.viewpager2.widget.ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        Log.d("HorizontalGallery", "mainImagePreview found: " + (mainImagePreview != null ? "yes" : "no"));
        if (mainImagePreview != null) {
            Log.d("HorizontalGallery", "Setting mainImagePreview visibility to VISIBLE");
            mainImagePreview.setVisibility(View.VISIBLE);

            // मुख्य प्रिव्यूसाठी adapter सेटअप करा (Setup adapter for main preview)
            Log.d("HorizontalGallery", "Creating MainImagePreviewAdapter...");
            com.Appzia.enclosure.Adapter.MainImagePreviewAdapter mainAdapter = new com.Appzia.enclosure.Adapter.MainImagePreviewAdapter(requireContext(), selectedUris);
            mainImagePreview.setAdapter(mainAdapter);
            Log.d("HorizontalGallery", "MainImagePreviewAdapter set successfully");

            // पेज चेंज callback सेटअप करा (Setup page change callback)
            mainImagePreview.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    updateImageCounter(position + 1, selectedUris.size());
                    
                    // Update caption EditText with current caption
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        messageBoxMy.setText(currentCaption);
                        messageBoxMy.setSelection(messageBoxMy.getText().length());
                    }
                }
            });
            Log.d("HorizontalGallery", "Page change callback registered");
            
            // Setup TextWatcher for caption input
            EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
            if (messageBoxMy != null) {
                messageBoxMy.addTextChangedListener(new android.text.TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Save the caption as user types
                        currentCaption = s.toString();
                        Log.d("CaptionWatcher", "Caption updated: " + currentCaption);
                    }

                    @Override
                    public void afterTextChanged(android.text.Editable s) {}
                });
            }
        }

        // हॉरिझॉन्टल RecyclerView सेटअप करा (Setup horizontal RecyclerView)
        Log.d("HorizontalGallery", "Finding horizontalRecyclerView...");


        // Image counter सेटअप करा (Setup image counter)
        Log.d("HorizontalGallery", "Setting up image counter...");
        updateImageCounter(1, selectedUris.size());
        Log.d("HorizontalGallery", "Image counter setup completed");
    }

    /**
     * Update image counter
     */
    private void updateImageCounter(int current, int total) {
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setVisibility(View.VISIBLE);
            imageCounter.setText(current + " / " + total);
        }
    }

    @OptIn(markerClass = UnstableApi.class)
    private void setupGalleryRecyclerView() {
        Log.d("PENDING_MESSAGES", "=== setupGalleryRecyclerView CALLED ===");
        galleryAdapter = new GalleryAdapter(requireContext(), galleryImages, uri -> {
            Log.d("DEBUG_IMAGE_STORAGE", "=== GALLERY IMAGE CLICKED ===");
            Log.d("PENDING_MESSAGES", "=== GALLERY IMAGE CLICKED ===");
            Log.d("DEBUG_IMAGE_STORAGE", "Clicked URI: " + uri);
            Log.d("DEBUG_IMAGE_STORAGE", "Fragment context: " + (getContext() != null ? "not null" : "null"));
            
            // This is the click listener from the adapter.
            // When an image in the gallery is clicked, we want to show its preview.
            // The logic for handling the preview and sending is now in CameraGalleryFragment.
            globalUri = uri; // Set globalUri for consistency
            String clickMimeType = requireContext().getContentResolver().getType(globalUri);
            boolean isVideoClick = clickMimeType != null && clickMimeType.startsWith("video/");
            
            Log.d("DEBUG_IMAGE_STORAGE", "MIME type: " + clickMimeType);
            Log.d("DEBUG_IMAGE_STORAGE", "Is video click: " + isVideoClick);

            // Removed: binding.backarrow.performClick(); // <--- THIS WAS CAUSING THE ISSUE

            if (isVideoClick) {
                Log.d("DEBUG_IMAGE_STORAGE", "Calling handleVideoSelection");
                handleVideoSelection(uri);
            } else {
                Log.d("DEBUG_IMAGE_STORAGE", "Calling handleImageSelection");
                handleImageSelection(uri);
            }
        }, new GalleryAdapter.OnMultipleImageSendListener() {
            @Override
            public void onMultipleImageSend(ArrayList<Uri> imageUris, String captionText) {
                handleMultipleImageSend(imageUris, captionText);
            }
            
            @Override
            public void onMultipleImageSend(ArrayList<Uri> imageUris, List<String> individualCaptions) {
                handleMultipleImageSend(imageUris, individualCaptions);
            }
        }, new GalleryAdapter.OnVideoSelectionListener() {
            @Override
            public void onVideoSelected(Uri videoUri) {
                Log.d("DEBUG_VIDEO", "=== onVideoSelected CALLBACK CALLED ===");
                Log.d("DEBUG_VIDEO", "Video URI: " + videoUri);
                Log.d("DEBUG_VIDEO", "Calling handleVideoSelection");
                handleVideoSelection(videoUri);
            }
        }, new GalleryAdapter.OnMultipleVideoSendListener() {
            @Override
            public void onMultipleVideoSend(ArrayList<Uri> videoUris, List<String> individualCaptions) {
                Log.d("DEBUG_VIDEO", "=== onMultipleVideoSend CALLBACK CALLED ===");
                Log.d("DEBUG_VIDEO", "Video URIs: " + videoUris.size());
                Log.d("DEBUG_VIDEO", "Captions: " + individualCaptions);
                handleMultipleVideoSend(videoUris, individualCaptions);
            }
        });
        binding.galleryRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        binding.galleryRecyclerView.setAdapter(galleryAdapter);

        // Enable multi-selection mode for gallery
        Log.d("PENDING_MESSAGES", "=== ENABLING MULTI-SELECTION MODE ===");
        galleryAdapter.setMultiSelectionMode(true);
        galleryAdapter.setOnImageSelectionListener(new GalleryAdapter.OnImageSelectionListener() {
            @Override
            public void onImageSelectionChanged(ArrayList<Uri> selectedUris) {
                Log.d("GallerySelection", "Selected images: " + selectedUris.size());
                updateMultiSelectionUI(selectedUris.size());
            }

            @Override
            public void onMultiSelectionModeChanged(boolean isMultiMode) {
                Log.d("GallerySelection", "Multi-selection mode: " + isMultiMode);
                showMultiSelectionUI(isMultiMode);
            }
        });

        binding.galleryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && lastVisibleItem >= totalItemCount - 5) {
                    int pageSize = currentPage == 0 ? INITIAL_PAGE_SIZE : PAGE_SIZE;
                    new LoadGalleryImagesTask(currentPage, pageSize, isPhotoMode, false).execute();
                }
            }
        });
    }

    private void setupClickListeners() {
        binding.captureButton.setOnClickListener(v -> {
            // Animation for button press feedback
            v.animate()
                    .scaleX(0.9f) // Scale down to 90%
                    .scaleY(0.9f) // Scale down to 90%
                    .setDuration(100) // Duration for scale down
                    .withEndAction(() -> {
                        // After scaling down, scale back to original size
                        v.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100) // Duration for scale up
                                .start();
                    })
                    .start();

            // Original button click logic
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(requireContext());
            }
            if (isPhotoMode) {
                takePhoto();
            } else {
                if (isRecording) {
                    stopRecording();
                } else {
                    startRecording();
                }
            }
        });

        // Handle tab clicks
        tabPhoto.setOnClickListener(v -> {
            if (!isPhotoMode) {
                isPhotoMode = true;
                updateButtonStates();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(requireContext());
                }
                // Bounce animation for capture button
                bounceCaptureButton();
                // Only switch camera mode - do not refresh RecyclerView
            }
        });

        tabVideo.setOnClickListener(v -> {
            if (isPhotoMode) {
                isPhotoMode = false;
                updateButtonStates();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(requireContext());
                }
                // Bounce animation for capture button
                bounceCaptureButton();
                // Keep showing image gallery; do not alter RecyclerView visibility
                // Do not clear or reload images on video tab; only switch capture UI
            }
        });

        binding.switchCameraButton.setOnClickListener(v -> {
            isBackCamera = !isBackCamera;
            cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(isBackCamera ? CameraSelector.LENS_FACING_BACK : CameraSelector.LENS_FACING_FRONT)
                    .build();
            startCamera();
        });

        binding.flashButton.setOnClickListener(v -> {
            isFlashOn = !isFlashOn;
            binding.flashIcon.setImageResource(isFlashOn ? R.drawable.flash_off : R.drawable.flash_onn);
            restartCameraWithFlash();
        });

        // Multi-selection caption and done controls
        LinearLayout captionBar = binding.getRoot().findViewById(R.id.captionlyt);
        LinearLayout counterLyt = binding.getRoot().findViewById(R.id.multiSelectSendGrpLyt);
        TextView smallCounter = binding.getRoot().findViewById(R.id.multiSelectSmallCounterText);
        EditText captionEdit = binding.getRoot().findViewById(R.id.messageBoxMy);
        LinearLayout multiSelectDoneButton = binding.getRoot().findViewById(R.id.multiSelectDoneButton);
        if (multiSelectDoneButton != null) {
            multiSelectDoneButton.setOnClickListener(v -> {
                Log.d("MultiSET", "=== MULTI-SELECTION SEND BUTTON CLICKED (INDIVIDUAL) ===");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(requireContext());
                }

                ArrayList<Uri> selectedUris = galleryAdapter.getSelectedImageUris();
                Log.d("MultiSET", "Selected URIs count: " + (selectedUris != null ? selectedUris.size() : "null"));

                if (selectedUris != null && selectedUris.size() > 0) {
                    Log.d("MultiSET", "Calling fragment setupMultiImagePreview() with " + selectedUris.size() + " images (INDIVIDUAL)");

                    // Get caption from edit text and pass it into preview
                    String caption = captionEdit != null ? captionEdit.getText().toString().trim() : "";
                    currentCaption = caption;
                    setupMultiImagePreview(selectedUris);

                    Log.d("MultiSET", "Fragment setupMultiImagePreview() call completed (INDIVIDUAL)");
                } else {
                    Log.d("MultiSET", "No images selected or selectedUris is null (INDIVIDUAL)");
                }
            });
        } else {
            Log.w("MultiSET", "multiSelectDoneButton not found in layout (INDIVIDUAL)");
        }

        // Update caption/done UI based on adapter selection count
        try {
            LinearLayout counterLyt2 = binding.getRoot().findViewById(R.id.multiSelectSendGrpLyt);
            TextView smallCounter2 = binding.getRoot().findViewById(R.id.multiSelectSmallCounterText);
            LinearLayout doneBtn2 = binding.getRoot().findViewById(R.id.multiSelectDoneButton);
            int count = galleryAdapter != null ? galleryAdapter.getSelectedImageUris().size() : 0;
            if (counterLyt2 != null) counterLyt2.setVisibility(View.VISIBLE);
            if (smallCounter2 != null) {
                smallCounter2.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                smallCounter2.setText(String.valueOf(count));
            }
            if (doneBtn2 != null) {
                doneBtn2.setEnabled(count > 0);
                doneBtn2.setAlpha(count > 0 ? 1.0f : 0.5f);
            }
        } catch (Exception ignore) {}

        binding.backarrow.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(requireContext());
            }
            closeWithAnimation();
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (isRecording) {
                            stopRecording();
                        }
                        closeWithAnimation();
                    }
                });
    }

    private void handleInitialPermissions() {
        if (hasPermissions()) {
            Log.d(TAG, "Permissions granted, starting camera and gallery");
            binding.cameraPreview.setVisibility(View.VISIBLE);
            startCamera();
            new LoadGalleryImagesTask(0, INITIAL_PAGE_SIZE, isPhotoMode, false).execute();
        } else {
            Log.d(TAG, "Requesting permissions in fragment");
            permissionLauncher.launch(REQUIRED_PERMISSIONS);
        }
    }

    private void updateButtonStates() {
        // Update capture button background based on photo/video mode and recording state
        if (isPhotoMode) {
            binding.captureButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.TextColor)));
            binding.captureIcon.setImageResource(R.drawable.camera); // Or a simple circle for photo
            binding.captureIcon.setImageTintList(ColorStateList.valueOf(Color.BLACK)); // Inner circle black
            binding.captureIcon.setVisibility(View.GONE); // Ensure it's visible
            tabPhoto.setBackgroundResource(R.drawable.tab_selected_background);
            tabPhoto.setTextColor(ContextCompat.getColor(requireContext(), R.color.edittextBg));
            tabVideo.setBackgroundResource(R.drawable.tab_unselected_background);
            tabVideo.setTextColor(ContextCompat.getColor(requireContext(), R.color.TextColor));
        } else { // Video mode
            if (isRecording) {
                binding.captureButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                binding.captureIcon.setImageResource(R.drawable.videopng); // Or a square for recording
                binding.captureIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE)); // Inner square white
            } else {
                binding.captureButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.TextColor)));
                binding.captureIcon.setImageResource(R.drawable.videopng); // Or a simple circle for video
                binding.captureIcon.setImageTintList(ColorStateList.valueOf(Color.BLACK)); // Inner circle black
            }
            binding.captureIcon.setVisibility(View.GONE); // Ensure it's visible
            tabPhoto.setBackgroundResource(R.drawable.tab_unselected_background);
            tabPhoto.setTextColor(ContextCompat.getColor(requireContext(), R.color.TextColor));
            tabVideo.setBackgroundResource(R.drawable.tab_selected_background);
            tabVideo.setTextColor(ContextCompat.getColor(requireContext(), R.color.edittextBg));
        }
    }

    private void bounceCaptureButton() {
        binding.captureButton.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(100)
                .withEndAction(() -> {
                    binding.captureButton.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }

    private void closeWithAnimation() {
        if (getActivity() != null) {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(0, R.anim.slide_out_bottom)
                    .remove(this)
                    .commit();
        }
    }

    private void stopRecording() {
        if (!isRecording || activeRecording == null) return;

        activeRecording.stop();
        activeRecording = null;
        isRecording = false;
        stopRecordingUI();
        
        // Turn off torch when recording stops
        if (isFlashOn) {
            setTorchOn(false);
        }
        
        // The preview logic will be handled in the Finalize event of startRecording
        updateButtonStates(); // Update button state after stopping recording
    }

    private void stopRecordingUI() {
        binding.timerText.setVisibility(View.GONE);
        timerHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Control torch/flashlight for video recording
     */
    private void setTorchOn(boolean enabled) {
        try {
            if (camera != null) {
                camera.getCameraControl().enableTorch(enabled);
                Log.d(TAG, "Torch " + (enabled ? "enabled" : "disabled"));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error controlling torch: " + e.getMessage());
        }
    }

    private boolean hasPermissions() {
        if (getContext() == null) {
            Log.e(TAG, "Context is null");
            return false;
        }
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission " + permission + " denied");
                return false;
            }
        }
        return true;
    }

    private void startCamera() {
        long startTime = System.currentTimeMillis();
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.cameraPreview.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setFlashMode(isFlashOn ? ImageCapture.FLASH_MODE_ON : ImageCapture.FLASH_MODE_OFF)
                        .build();

                Recorder recorder = new Recorder.Builder()
                        .setExecutor(cameraExecutor)
                        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                        .build();

                videoCapture = VideoCapture.withOutput(recorder);

                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture,
                        videoCapture
                );

                Log.d(TAG, "Camera started successfully in " + (System.currentTimeMillis() - startTime) + "ms");
            } catch (Exception e) {
                Log.e(TAG, "Error starting camera", e);
                showToast("Failed to start camera");
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void startRecording() {
        if (videoCapture == null) {
            Log.e(TAG, "VideoCapture is null");
            showToast("Video capture not initialized");
            return;
        }

        if (!hasPermissions()) {
            permissionLauncher.launch(REQUIRED_PERMISSIONS);
            return;
        }

        // Turn on torch if flash is enabled
        if (isFlashOn) {
            setTorchOn(true);
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = "VID_" + timeStamp + ".mp4";
        
        // Create both private and public directories
        File privateDir = getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Videos", requireContext());
        File publicDir = getPublicVideosDirectory();
        
        if (!privateDir.exists() && !privateDir.mkdirs()) {
            showToast("Failed to create private storage directory");
            return;
        }
        if (!publicDir.exists() && !publicDir.mkdirs()) {
            showToast("Failed to create public storage directory");
            return;
        }
        File videoFile = new File(privateDir, videoFileName);

        FileOutputOptions outputOptions = new FileOutputOptions.Builder(videoFile).build();

        isRecording = true;
        updateButtonStates(); // Update button state to red
        binding.timerText.setVisibility(View.VISIBLE);
        recordingStartTime = System.currentTimeMillis();
        updateTimer();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        activeRecording = videoCapture.getOutput()
                .prepareRecording(requireContext(), outputOptions)
                .withAudioEnabled()
                .start(ContextCompat.getMainExecutor(requireContext()), videoRecordEvent -> {
                    if (videoRecordEvent instanceof VideoRecordEvent.Finalize) {
                        VideoRecordEvent.Finalize finalizeEvent = (VideoRecordEvent.Finalize) videoRecordEvent;
                        if (finalizeEvent.hasError()) {
                            Log.e(TAG, "Video recording failed: " + finalizeEvent.getError());
                            requireActivity().runOnUiThread(() -> {
                                showToast("Failed to record video");
                                stopRecordingUI();
                                updateButtonStates(); // Revert button state if error
                            });
                        } else {
                            Uri savedUri = Uri.fromFile(videoFile);
                            
                            // Copy to public directory as well
                            copyVideoToPublicDirectory(videoFile, publicDir, videoFileName);
                            
                            requireContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, savedUri));
                            // Show preview after recording
                            requireActivity().runOnUiThread(() -> {
                                handleVideoSelection(savedUri); // Show the preview of the recorded video
                                refreshGallery(); // Refresh gallery in the background
                            });
                        }
                    }
                });
    }

    private void updateTimer() {
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    long elapsedMillis = System.currentTimeMillis() - recordingStartTime;
                    binding.timerText.setText(stringForTime(elapsedMillis));
                    timerHandler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void takePhoto() {
        if (imageCapture == null) {
            Log.e(TAG, "ImageCapture is null");
            showToast("Camera not initialized");
            return;
        }

        // Turn on torch if flash is enabled
        if (isFlashOn) {
            setTorchOn(true);
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        
        // Create both private and public directories
        File privateDir = getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Images", requireContext());
        File publicDir = getPublicPicturesDirectory();
        
        if (!privateDir.exists() && !privateDir.mkdirs()) {
            showToast("Failed to create private storage directory");
            return;
        }
        if (!publicDir.exists() && !publicDir.mkdirs()) {
            showToast("Failed to create public storage directory");
            return;
        }
        
        // Use private directory for initial capture
        File photoFile = new File(privateDir, imageFileName);

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(
                outputOptions,
                cameraExecutor,
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri savedUri = Uri.fromFile(photoFile);
                        Log.d(TAG, "Camera capture - photoFile: " + photoFile.getAbsolutePath());
                        Log.d(TAG, "Camera capture - savedUri: " + savedUri.toString());
                        
                        // Copy to public directory as well
                        copyToPublicDirectory(photoFile, publicDir, imageFileName);
                        
                        // Turn off torch after photo is captured
                        if (isFlashOn) {
                            setTorchOn(false);
                        }
                        
                        requireContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, savedUri));
                        requireActivity().runOnUiThread(() -> {
                            handleImageSelection(savedUri); // Show the preview of the captured image
                            refreshGallery(); // Refresh gallery in the background
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e(TAG, "Image capture failed", exception);
                        showToast("Failed to capture image");
                        
                        // Turn off torch if photo capture failed
                        if (isFlashOn) {
                            setTorchOn(false);
                        }
                    }
                });
    }

    private void refreshGallery() {
        requireActivity().runOnUiThread(() -> {
            currentPage = 0;
            galleryImages.clear();
            galleryAdapter.notifyDataSetChanged();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                new LoadGalleryImagesTask(0, INITIAL_PAGE_SIZE, isPhotoMode, true).execute(); // Pass isReloading as true
            }, 1500);
        });
    }

    private void showToast(String message) {
        if (getContext() != null) {
            // Using Toast.makeText directly as per previous instruction to avoid custom toast issues
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            Log.w(TAG, "Attempted to show toast but context is null: " + message);
        }
    }

    private class LoadGalleryImagesTask extends AsyncTask<Void, Void, List<Uri>> {
        private final int page;
        private final int pageSize;
        private final boolean isPhotoMode;
        private final boolean isReloading; // Added to distinguish initial load from refresh

        LoadGalleryImagesTask(int page, int pageSize, boolean isPhotoMode, boolean isReloading) {
            this.page = page;
            this.pageSize = pageSize;
            this.isPhotoMode = isPhotoMode;
            this.isReloading = isReloading;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
            if (!isReloading) {
                // You can show a loading indicator here if it's not a refresh
            }
            scanMediaDirectories();
        }

        private void scanMediaDirectories() {
            Context context = CameraGalleryFragment.this.getContext();
            if (context == null) {
                Log.w(TAG, "Fragment not attached, skipping media scan in AsyncTask.");
                return;
            }

            String[] directories = {
                    getContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getPath(),
                    getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath(),
                    getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath(),
                    "/storage/emulated/0/ScreenRecordings",
                    "/storage/emulated/0/Videos"
            };

            for (String dir : directories) {
                File dirFile = new File(dir);
                if (dirFile.exists() && dirFile.isDirectory()) {
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(dirFile)));
                }
            }
        }

        @Override
        protected List<Uri> doInBackground(Void... voids) {
            long startTime = System.currentTimeMillis();
            List<MediaItem> mediaItems = new ArrayList<>();
            Set<Uri> uniqueUris = new HashSet<>();

            Context fragmentContext = CameraGalleryFragment.this.getContext();
            if (fragmentContext == null) {
                Log.w(TAG, "Fragment detached during doInBackground, cannot load media.");
                return new ArrayList<>(); // Return empty list as fragment is detached
            }

            if (!CameraGalleryFragment.this.hasPermissions()) {
                publishProgress();
                return new ArrayList<>();
            }

            try {
                Thread.sleep(500); // Small delay to allow media scanner to work
            } catch (InterruptedException e) {
                Log.w(TAG, "Media scan delay interrupted", e);
            }

            // Load media based on current mode
            if (isPhotoMode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    CameraGalleryFragment.this.loadImagesQ(fragmentContext, page, pageSize, uniqueUris, mediaItems);
                } else {
                    CameraGalleryFragment.this.loadImagesLegacy(fragmentContext, page, pageSize, uniqueUris, mediaItems);
                }
            } else {
                // Video mode: load videos from gallery
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    CameraGalleryFragment.this.loadVideosQ(fragmentContext, page, pageSize, uniqueUris, mediaItems);
                } else {
                    CameraGalleryFragment.this.loadVideosLegacy(fragmentContext, page, pageSize, uniqueUris, mediaItems);
                }
            }

            mediaItems.sort((a, b) -> {
                int compare = Long.compare(b.dateAdded, a.dateAdded);
                if (compare == 0) {
                    compare = Long.compare(b.dateModified, a.dateModified);
                }
                return compare;
            });

            List<Uri> media = new ArrayList<>();
            int start = page * pageSize;
            int end = Math.min(start + pageSize, mediaItems.size());
            for (int i = start; i < end; i++) {
                media.add(mediaItems.get(i).uri);
            }

            Log.d(TAG, "Total media loaded: " + media.size() + " in " + (System.currentTimeMillis() - startTime) + "ms");
            return media;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Context context = CameraGalleryFragment.this.getContext();
            if (context != null) {
                CameraGalleryFragment.this.showToast("Media access permission required. Requesting now...");
                permissionLauncher.launch(REQUIRED_PERMISSIONS);
            } else {
                Log.w(TAG, "Context is null, cannot launch permission request or show toast.");
            }
        }

        @Override
        protected void onPostExecute(List<Uri> media) {
            Log.d("PENDING_MESSAGES", "=== LoadGalleryImagesTask.onPostExecute CALLED ===");
            Log.d("DEBUG_IMAGE_STORAGE", "=== onPostExecute CALLED ===");
            Log.d("DEBUG_IMAGE_STORAGE", "Media count: " + media.size());
            Log.d("DEBUG_IMAGE_STORAGE", "Is photo mode: " + isPhotoMode);
            Log.d("DEBUG_IMAGE_STORAGE", "Current gallery size before update: " + galleryImages.size());
            Log.d("DEBUG_IMAGE_STORAGE", "Fragment is added: " + CameraGalleryFragment.this.isAdded());
            Log.d("DEBUG_IMAGE_STORAGE", "Fragment view is not null: " + (CameraGalleryFragment.this.getView() != null));
            
            isLoading = false;
            if (CameraGalleryFragment.this.isAdded() && CameraGalleryFragment.this.getView() != null) {
                int startPosition = galleryImages.size();
                Log.d("DEBUG_IMAGE_STORAGE", "Adding media starting at position: " + startPosition);
                galleryImages.addAll(media);
                galleryAdapter.notifyItemRangeInserted(startPosition, media.size());
                currentPage++;
                Log.d("DEBUG_IMAGE_STORAGE", "Gallery updated with " + media.size() + " items, total: " + galleryImages.size());
                
                // Log first few URIs for debugging
                for (int i = 0; i < Math.min(5, media.size()); i++) {
                    Log.d("DEBUG_IMAGE_STORAGE", "Media URI " + i + ": " + media.get(i));
                }
            } else {
                Log.w("DEBUG_IMAGE_STORAGE", "Fragment detached, skipping gallery update.");
            }
        }
    }

    private static class MediaItem {
        final Uri uri;
        final String mimeType;
        final String filePath;
        final long dateAdded;
        final long dateModified;

        MediaItem(Uri uri, String mimeType, String filePath, long dateAdded, long dateModified) {
            this.uri = uri;
            this.mimeType = mimeType;
            this.filePath = filePath;
            this.dateAdded = dateAdded;
            this.dateModified = dateModified;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("messageList", messageList);
        outState.putStringArrayList("uniqueDates", new ArrayList<>(uniqueDates));
        outState.putBoolean("isPhotoMode", isPhotoMode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cleanupResources();
        binding = null;
    }

    private void cleanupResources() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            cameraProvider = null;
        }
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
            try {
                if (!cameraExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    cameraExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                cameraExecutor.shutdownNow();
            }
        }
        timerHandler.removeCallbacksAndMessages(null);
        if (playerPreview != null) {
            playerPreview.stop();
            playerPreview.release();
            playerPreview = null;
        }
    }

    //region Preview Dialog Logic (Moved from GalleryAdapter)
    private void handleVideoSelection(Uri uri) {
        Log.d("PENDING_MESSAGES", "=== handleVideoSelection CALLED ===");
        Log.d("DEBUG_VIDEO", "=== handleVideoSelection CALLED ===");
        Log.d("DEBUG_VIDEO", "URI: " + uri);
        Log.d("DEBUG_VIDEO", "Fragment context: " + (getContext() != null ? "not null" : "null"));
        
        // Generate new modelId for the current media being sent
        modelId = database.getReference().push().getKey();
        globalUri = uri; // Set globalUri for the currently selected/captured media

        try {
            String fileName = getFileNameFromUri(uri, requireContext());
            if (fileName == null) {
                showToast("Unable to get file name");
                return;
            }

            // Copy to cache dir for processing
            globalFile = new File(requireContext().getCacheDir(), fileName);
            copyUriToFile(uri, globalFile, requireContext());

            // Create thumbnail
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(globalFile.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
            File savedThumbnail = FileUtils.saveBitmapToFile(requireContext().getApplicationContext(), thumbnail, "thumbnail.png");
            String fileThumbName = fileName + ".png";

            // Save thumbnail to external storage
            File thumbDir = getExternalStorageDir(Environment.DIRECTORY_PICTURES, "Enclosure/Media/Thumbnail", requireContext());

            File thumbnailFile = new File(thumbDir, fileThumbName);
            if (!thumbnailFile.exists() && savedThumbnail != null) {
                copyUriToFile(Uri.fromFile(savedThumbnail), thumbnailFile, requireContext());
            }

            // Save video to external storage
            File videoDir = getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Videos", requireContext());
            File videoFile = new File(videoDir, fileName);
            if (!videoFile.exists()) {
                copyUriToFile(uri, videoFile, requireContext());
            }

            showVideoPreviewDialog(videoFile, thumbnailFile, fileThumbName, fileName);
        } catch (Exception e) {
            //   showToast("Error: " + e.getMessage());
            Log.e(TAG, "Error processing video", e);
        }
    }

    private void handleImageSelection(Uri uri) {
        Log.d("PENDING_MESSAGES", "=== handleImageSelection CALLED ===");
        Log.d("DEBUG_IMAGE_STORAGE", "=== handleImageSelection CALLED ===");
        Log.d("DEBUG_IMAGE_STORAGE", "URI: " + uri);
        Log.d("DEBUG_IMAGE_STORAGE", "Fragment context: " + (getContext() != null ? "not null" : "null"));
        
        // Generate new modelId for the current media being sent
        modelId = database.getReference().push().getKey();
        globalUri = uri; // Set globalUri for the currently selected/captured media

        if (uri == null) {
            Log.e("DEBUG_IMAGE_STORAGE", "URI is null, showing toast");
            showToast("Please select image");
            return;
        }

        try {
            String extension = getFileExtension(uri, requireContext());
            Log.d("DEBUG_IMAGE_STORAGE", "handleImageSelection - extension: " + extension);
            String fileName = getFileNameFromUri(uri, requireContext());
            Log.d("DEBUG_IMAGE_STORAGE", "handleImageSelection - fileName from URI: " + fileName);
            if (fileName == null) {
                fileName = "image_" + System.currentTimeMillis() + "." + extension;
                Log.d("DEBUG_IMAGE_STORAGE", "handleImageSelection - Generated fileName: " + fileName);
            }

            // Handle compressed image for sending (globalFile)
            Log.d("DEBUG_IMAGE_STORAGE", "Processing compressed image...");
            globalFile = processImage(uri, fileName, 20); // 20% quality for upload
            Log.d("DEBUG_IMAGE_STORAGE", "Compressed image saved to: " + (globalFile != null ? globalFile.getAbsolutePath() : "null"));

            // Handle full image for potential later use (fullImageFile)
            Log.d("DEBUG_IMAGE_STORAGE", "Processing full size image...");
            fullImageFile = processImage(uri, "full_" + fileName, 80); // 80% quality for full resolution
            Log.d("DEBUG_IMAGE_STORAGE", "Full size image saved to: " + (fullImageFile != null ? fullImageFile.getAbsolutePath() : "null"));

            // Save to external storage (optional, as media scanner already adds it)
            File externalDir = getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Images", requireContext());
            Log.d("DEBUG_IMAGE_STORAGE", "External storage directory: " + externalDir.getAbsolutePath());
            Log.d("DEBUG_IMAGE_STORAGE", "Directory exists: " + externalDir.exists());
            
            File imageFile = new File(externalDir, fileName);
            Log.d("DEBUG_IMAGE_STORAGE", "Target image file: " + imageFile.getAbsolutePath());
            Log.d("DEBUG_IMAGE_STORAGE", "Image file exists before copy: " + imageFile.exists());
            
            if (!imageFile.exists()) {
                Log.d("DEBUG_IMAGE_STORAGE", "Copying URI to external storage...");
                copyUriToFile(uri, imageFile, requireContext());
                Log.d("DEBUG_IMAGE_STORAGE", "Image file exists after copy: " + imageFile.exists());
                Log.d("DEBUG_IMAGE_STORAGE", "Image file size after copy: " + (imageFile.exists() ? imageFile.length() + " bytes" : "file not found"));
            } else {
                Log.d("DEBUG_IMAGE_STORAGE", "Image file already exists, skipping copy");
            }

            Log.d("DEBUG_IMAGE_STORAGE", "Showing image preview dialog...");
            showImagePreviewDialog(imageFile, fileName);
        } catch (Exception e) {
            Log.e("DEBUG_IMAGE_STORAGE", "Error processing image: " + e.getMessage(), e);
            showToast("Error processing image: " + e.getMessage());
        }
    }

    private File processImage(Uri uri, String fileName, int quality) throws IOException {
        Log.d("DEBUG_IMAGE_STORAGE", "=== processImage CALLED ===");
        Log.d("DEBUG_IMAGE_STORAGE", "URI: " + uri);
        Log.d("DEBUG_IMAGE_STORAGE", "fileName: " + fileName);
        Log.d("DEBUG_IMAGE_STORAGE", "quality: " + quality);
        
        File file = new File(requireContext().getCacheDir(), fileName);
        Log.d("DEBUG_IMAGE_STORAGE", "Cache file path: " + file.getAbsolutePath());
        Log.d("DEBUG_IMAGE_STORAGE", "Cache directory exists: " + file.getParentFile().exists());
        
        try (InputStream imageStream = requireContext().getContentResolver().openInputStream(uri)) {
            Log.d("DEBUG_IMAGE_STORAGE", "InputStream opened successfully");
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            if (bitmap == null) {
                Log.e("DEBUG_IMAGE_STORAGE", "Failed to decode bitmap from URI: " + uri);
                throw new IOException("Failed to decode bitmap from URI: " + uri);
            }
            Log.d("DEBUG_IMAGE_STORAGE", "Bitmap decoded successfully - width: " + bitmap.getWidth() + ", height: " + bitmap.getHeight());
            
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 FileOutputStream fos = new FileOutputStream(file)) {
                Log.d("DEBUG_IMAGE_STORAGE", "Compressing bitmap with quality: " + quality);
                boolean compressed = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                Log.d("DEBUG_IMAGE_STORAGE", "Bitmap compression result: " + compressed);
                
                byte[] bitmapData = bos.toByteArray();
                Log.d("DEBUG_IMAGE_STORAGE", "Compressed data size: " + bitmapData.length + " bytes");
                
                fos.write(bitmapData);
                fos.flush();
                Log.d("DEBUG_IMAGE_STORAGE", "Image processed: " + file.getPath() + ", quality: " + quality);
                Log.d("DEBUG_IMAGE_STORAGE", "File created successfully: " + file.exists());
                Log.d("DEBUG_IMAGE_STORAGE", "Final file size: " + (file.exists() ? file.length() + " bytes" : "file not found"));
            }
            return file;
        } catch (Exception e) {
            Log.e("DEBUG_IMAGE_STORAGE", "Error in processImage: " + e.getMessage(), e);
            throw e;
        }
    }

    private void showImagePreviewDialog(File imageFile, String fileName) {
        Constant.dialogueFullScreen(requireContext(), R.layout.dialogue_full_screen_dialogue);
        Constant.dialogLayoutFullScreen.show();

        Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
            Window activityWindow = requireActivity().getWindow();
            WindowCompat.setDecorFitsSystemWindows(activityWindow, true);
            // Added: Close the CameraGalleryFragment when the dialog is dismissed


            //   closeWithAnimation();


        });

        setupImagePreviewDialog(imageFile, fileName);
    }

    private void setupImagePreviewDialog(File imageFile, String fileName) {
        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
        ImageView image = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        PlayerView video = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);

        image.setVisibility(View.VISIBLE);
        video.setVisibility(View.GONE);
        downloadCtrl.setVisibility(View.GONE);
        contactContainer.setVisibility(View.GONE);

        image.setImageURI(Uri.fromFile(imageFile));
        applyThemeColor(sendGrp);

        sendGrp.setOnClickListener(v -> {
           // Toast.makeText(messageBoxMy.getContext(), "clicked", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(requireContext());
            }
            sendImageMessage(messageBoxMy, fileName);
        });
        backarrow.setOnClickListener(v -> Constant.dialogLayoutFullScreen.dismiss());
    }

    private void applyThemeColor(LinearLayout sendGrp) {
        try {
            Constant.getSfFuncion(requireContext());
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themColor));
            sendGrp.setBackgroundTintList(tintList);
        } catch (Exception e) {
            Log.e(TAG, "Error setting theme color", e);
        }
    }

    private void sendImageMessage(EditText messageBoxMy, String fileName) {
        Log.d("PENDING_MESSAGES", "=== sendImageMessage CALLED ===");
        // This line was causing the IllegalStateException if fragment detached
        String[] dimensions = calculateCameraImageDimensions(requireContext(), globalFile, globalUri);
        String imageWidthDp = dimensions[0];
        String imageHeightDp = dimensions[1];
        String aspectRatio = dimensions[2];

        String receiverUid = requireActivity().getIntent().getStringExtra("friendUidKey");
        String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
        String currentDateTimeString = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        String uniqDate = Constant.getCurrentDate();

        messageModel model = createMessageModel(senderId, receiverUid, currentDateTimeString,
                uniqDate, messageBoxMy.getText().toString().trim(),
                imageWidthDp, imageHeightDp, aspectRatio);

        Log.d(TAG, "sendImageMessage - Before adding model");
        Log.d(TAG, "sendImageMessage - messageList size before: " + (messageList != null ? messageList.size() : "null"));
        Log.d(TAG, "sendImageMessage - chatAdapter: " + (chatAdapter != null ? "not null" : "null"));
        Log.d(TAG, "sendImageMessage - messageRecView: " + (messageRecView != null ? "not null" : "null"));

        messageList.add(model);
        Log.d(TAG, "sendImageMessage - messageList size after: " + (messageList != null ? messageList.size() : "null"));

        Log.d("PENDING_MESSAGES", "=== ABOUT TO CALL insertMessageToDatabase (IMAGE) ===");
        Log.d("PENDING_MESSAGES", "Model: " + (model != null ? "not null" : "null"));
        insertMessageToDatabase(model);
        updateChatAdapter();

        UploadChatHelper uploadHelper = new UploadChatHelper(requireContext(), globalFile, fullImageFile, senderId, userFTokenKey);
        uploadHelper.uploadContent(
                Constant.img,
                globalUri,
                messageBoxMy.getText().toString().trim(),
                modelId,
                null, null, fileName, null, null, null, null,
                getFileExtension(globalUri, requireContext()),
                receiverUid,
                model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(),
                model.getReplyType(), model.getReplytextData(), model.getDataType(),
                model.getFileName(), model.getForwaredKey(),
                imageWidthDp, imageHeightDp, aspectRatio);

        Constant.dialogLayoutFullScreen.dismiss();
        closeWithAnimation();
        // The closeWithAnimation() call is now handled by the dialog's onDismissListener
    }

    private messageModel createMessageModel(String senderId, String receiverUid, String time,
                                            String uniqDate, String message,
                                            String imageWidthDp, String imageHeightDp, String aspectRatio) {
        ArrayList<emojiModel> emojiModels = new ArrayList<>();
        emojiModels.add(new emojiModel("", ""));
        boolean isNewDate = uniqueDates.add(uniqDate);

        return new messageModel(
                senderId, message, time, globalFile.toString(), Constant.img,
                "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""),
                "", "", "", "", "", modelId, receiverUid,
                "", "", "", globalFile.getName(), "", "", message, 1,
                isNewDate ? uniqDate : ":" + uniqDate, emojiModels, "",
                Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1"
        );
    }

    private void insertMessageToDatabase(messageModel model) {
        Log.d("PENDING_MESSAGES", "=== insertMessageToDatabase CALLED ===");
        Log.d("PENDING_MESSAGES", "Model ID: " + model.getModelId());
        Log.d("PENDING_MESSAGES", "Receiver UID: " + model.getReceiverUid());
        Log.d("PENDING_MESSAGES", "Data Type: " + model.getDataType());
        Log.d("PENDING_MESSAGES", "Document: " + model.getDocument());
        Log.d("PENDING_MESSAGES", "Context: " + (requireContext() != null ? "not null" : "null"));
        
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
            Log.d("PENDING_MESSAGES", "DatabaseHelper created successfully");
            
            // Insert into pending_messages table instead of regular messages table
            dbHelper.insertPendingMessage(model);
            Log.d("PENDING_MESSAGES", "✅ Message inserted into pending_messages table: " + model.getModelId());
            
            // Verify insertion by checking if message exists
            List<messageModel> pendingMessages = dbHelper.getPendingMessages(model.getReceiverUid());
            Log.d("PENDING_MESSAGES", "Total pending messages for receiver: " + pendingMessages.size());
            
            boolean found = false;
            for (messageModel pendingMsg : pendingMessages) {
                if (pendingMsg.getModelId().equals(model.getModelId())) {
                    found = true;
                    Log.d("PENDING_MESSAGES", "✅ Verification: Message found in pending table");
                    break;
                }
            }
            if (!found) {
                Log.e("PENDING_MESSAGES", "❌ Verification: Message NOT found in pending table");
            }
            
        } catch (Exception e) {
            Log.e("PENDING_MESSAGES", "❌ Failed to insert message into pending_messages table: " + e.getMessage(), e);
            Log.e("PENDING_MESSAGES", "Exception type: " + e.getClass().getSimpleName());
            e.printStackTrace();
        }
    }


    private void updateChatAdapter() {
        Log.d("updateChatAdapter", "Starting updateChatAdapter");
        Log.d("updateChatAdapter", "chatAdapter: " + (chatAdapter != null ? "not null" : "null"));
        Log.d("updateChatAdapter", "messageRecView: " + (messageRecView != null ? "not null" : "null"));
        Log.d("updateChatAdapter", "messageList: " + (messageList != null ? "not null" : "null"));
        Log.d("updateChatAdapter", "messageList size: " + (messageList != null ? messageList.size() : "null"));

        ((Activity) getContext()).runOnUiThread(() -> {
            if (chatAdapter != null) {
                senderReceiverDownload.itemAdd(messageRecView, chatAdapter);
                senderReceiverDownload.setLastItemVisible(isLastItemVisible, messageList, chatAdapter);
                Log.d("updateChatAdapter", "Updated chatAdapter with itemAdd");
            } else {
                Log.e("updateChatAdapter", "chatAdapter is null!");
            }

            // Add null check for messageRecView to prevent NullPointerException
            if (messageRecView != null && messageList != null && !messageList.isEmpty()) {
                //     Toast.makeText(getContext(), "not empty - scrolling to position " + (messageList.size() - 1), Toast.LENGTH_SHORT).show();
                messageRecView.scrollToPosition(messageList.size() - 1);
                Log.d("updateChatAdapter", "Scrolled to position: " + (messageList.size() - 1));
            } else {
                String reason = "";
                if (messageRecView == null) reason += "messageRecView is null; ";
                if (messageList == null) reason += "messageList is null; ";
                if (messageList != null && messageList.isEmpty()) reason += "messageList is empty; ";
                //   Toast.makeText(getContext(), "empty - " + reason, Toast.LENGTH_LONG).show();
                Log.e("updateChatAdapter", "Cannot scroll: " + reason);
            }
        });

    }

    @OptIn(markerClass = UnstableApi.class)
    private void showVideoPreviewDialog(File videoFile, File thumbnailFile, String fileThumbName, String fileName) {
        Log.d("VIDEO_DIALOG_SHOW", "CameraGalleryFragment.showVideoPreviewDialog called for: " + fileName);
        Constant.dialogueFullScreen(requireContext(), R.layout.dialogue_full_screen_dialogue);
        Log.d("VIDEO_DIALOG_SHOW", "About to show dialog");
        Constant.dialogLayoutFullScreen.show();

        Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
            Window activityWindow = requireActivity().getWindow();
            WindowCompat.setDecorFitsSystemWindows(activityWindow, true);
            if (playerPreview != null) {
                playerPreview.stop();
                playerPreview.release();
                playerPreview = null;
            }
            // Added: Close the CameraGalleryFragment when the dialog is dismissed
            //  closeWithAnimation();
        });

        setupVideoPreviewDialog(videoFile, thumbnailFile, fileThumbName, fileName);
    }

    @OptIn(markerClass = UnstableApi.class)
    private void setupVideoPreviewDialog(File videoFile, File thumbnailFile, String fileThumbName, String fileName) {
        // You'd typically call Constant.dialogueFullScreen here, but the previous code had it outside
        // this function. Let's assume the dialog is already set up and shown for these methods.
        // Constant.dialogueFullScreen(mContext, R.layout.dialogue_full_screen_dialogue);
        // Constant.dialogLayoutFullScreen.show(); // This would be called from the parent method

        // Setup dialog window flags and dismiss listener if this method is called after dialog is created
        Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false);
            View rootView = window.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(false);

            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
            // Restore decor fitsSystemWindows true for the activity window
            Window activityWindow = ((Activity) requireContext()).getWindow();
            WindowCompat.setDecorFitsSystemWindows(activityWindow, true); // Restore to true
            // No need to find rootView and setFitsSystemWindows(true) explicitly if using WindowCompat.setDecorFitsSystemWindows
            // as it handles system insets for you.
            if (playerPreview != null) {
                playerPreview.stop();
                playerPreview.release();
                playerPreview = null; // Important to nullify to prevent leaks
            }
        });


        ImageView image = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        PlayerView video = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2); // Main dialog back arrow
        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);

        // Ensure all other previews are hidden
        image.setVisibility(View.GONE);
        backarrow.setVisibility(View.GONE);
        downloadCtrl.setVisibility(View.GONE);
        contactContainer.setVisibility(View.GONE);

        // Show the video player
        video.setVisibility(View.VISIBLE);
        video.setUseController(true); // Ensure controller is enabled


        // Setup the ExoPlayer instance and load video
        setupVideoPlayer(video, videoFile);

        // Apply theme color to send button
        applyThemeColor(sendGrp);

        // Set listeners
        sendGrp.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(requireContext());
            }
            sendVideoMessage(videoFile, thumbnailFile, fileThumbName, fileName, messageBoxMy);
        });
        // Back arrow for the main dialog layout (not the one inside the ExoPlayer controller)
        if (backarrow != null) {
            backarrow.setOnClickListener(v -> Constant.dialogLayoutFullScreen.dismiss());
        }

        // Reference and set up the back arrow and name title from the custom ExoPlayer layout
        LinearLayout exoPlayerBackArrow = video.findViewById(R.id.backarrow); // Back arrow inside custom_exoplayer_layout
        TextView exoPlayerNameTitle = video.findViewById(R.id.nameTitle);

        if (exoPlayerBackArrow != null) {
            exoPlayerBackArrow.setVisibility(View.VISIBLE); // Ensure it's visible
            exoPlayerBackArrow.setOnClickListener(v -> Constant.dialogLayoutFullScreen.dismiss());
        }
        if (exoPlayerNameTitle != null) {
            exoPlayerNameTitle.setText(fileName); // Set the video file name
        }
    }

    @OptIn(markerClass = UnstableApi.class)
    private void setupVideoPlayer(PlayerView videoView, File videoFile) {
        // Re-initialize playerPreview
        if (playerPreview != null) {
            playerPreview.release();
        }
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(requireContext()); // Assuming mContext is available
        TrackSelectionParameters trackSelectionParameters = trackSelector.getParameters().
                buildUpon().setForceLowestBitrate(true).build();
        trackSelector.setParameters((DefaultTrackSelector.Parameters) trackSelectionParameters);

        playerPreview = new ExoPlayer.Builder(requireContext()) // Use mContext here
                .setTrackSelector(trackSelector)
                .build();
        playerPreview.setMediaItem(androidx.media3.common.MediaItem.fromUri(Uri.fromFile(videoFile)));
        playerPreview.prepare();
        playerPreview.setPlayWhenReady(true); // Auto-play the video
        videoView.setPlayer(playerPreview);
        android.util.Log.d("VIDEO_PLAYER_INIT", "Fragment setup: PlayerView bound. file=" + videoFile.getName() + ", uri=" + Uri.fromFile(videoFile));

        setupVideoControls(videoView);
    }

    @OptIn(markerClass = UnstableApi.class)
    private void setupVideoControls(PlayerView videoView) {
        ImageView play = videoView.findViewById(R.id.play);
        ImageView pause = videoView.findViewById(R.id.pause);
        ImageView reply = videoView.findViewById(R.id.reply);
        ImageView forward = videoView.findViewById(R.id.forward);
        LinearLayout rsizeLyt = videoView.findViewById(R.id.rsizeLyt);
        TextView resizeText = videoView.findViewById(R.id.resizeText);
        TextView startTime = videoView.findViewById(R.id.startTime);
        TextView totalTime = videoView.findViewById(R.id.totalTime);
        DefaultTimeBar exoProgress = videoView.findViewById(R.id.exoProgress);

        // Note: The previous code used arrays for resizeModeIndex and wasPlaying to make them effectively final
        // for inner classes. If these are class members of your Activity/Fragment, you can use them directly.
        // For this refactor, I'll assume they are class members.

        rsizeLyt.setOnClickListener(v -> {
            switch (resizeModeIndex) { // Use class member
                case 0:
                    videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    resizeText.setText("Fit Mode");
                    break;
                case 1:
                    videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    resizeText.setText("Fill Mode");
                    break;
                case 2:
                    videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                    resizeText.setText("Zoom Mode");
                    break;
            }
            resizeModeIndex = (resizeModeIndex + 1) % 3; // Update class member
        });

        forward.setOnClickListener(v -> playerPreview.seekTo(playerPreview.getCurrentPosition() + 10000));

        reply.setOnClickListener(v -> {
            long newPosition = playerPreview.getCurrentPosition() - 10000;
            playerPreview.seekTo(Math.max(0, newPosition));
        });

        play.setOnClickListener(v -> {
            playerPreview.play();
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        });

        pause.setOnClickListener(v -> {
            playerPreview.pause();
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        });

        exoProgress.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {
                wasPlaying = playerPreview.getPlayWhenReady(); // Use class member
                playerPreview.setPlayWhenReady(false);
            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                startTime.setText(stringForTime(position));
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                playerPreview.seekTo(position);
                if (wasPlaying) { // Use class member
                    playerPreview.setPlayWhenReady(true);
                }
            }
        });

        // Handler is now a class member. updateProgressAction is also a class member.
        // No need for final arrays.

        playerPreview.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    totalTime.setText(stringForTime(playerPreview.getDuration()));
                } else if (state == Player.STATE_ENDED) {
                    playerPreview.seekTo(0);
                    playerPreview.setPlayWhenReady(false);
                    startTime.setText(stringForTime(0));
                    exoProgress.setPosition(0);
                    exoProgress.setDuration(playerPreview.getDuration());
                    play.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.GONE);
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    updateProgressAction = () -> {
                        if (playerPreview != null && playerPreview.isPlaying()) {
                            long currentPos = playerPreview.getCurrentPosition();
                            long duration = playerPreview.getDuration();
                            startTime.setText(stringForTime(currentPos));
                            totalTime.setText(stringForTime(duration));
                            exoProgress.setDuration(duration);
                            exoProgress.setPosition(currentPos);
                            handler.postDelayed(updateProgressAction, 500); // Corrected to use class member
                        }
                    };
                    handler.post(updateProgressAction);
                } else {
                    if (updateProgressAction != null) {
                        handler.removeCallbacks(updateProgressAction);
                    }
                }
            }
        });
    }

    private void sendVideoMessage(File videoFile, File thumbnailFile, String fileThumbName, String fileName, EditText messageBoxMy) {
        Log.d("PENDING_MESSAGES", "=== sendVideoMessage CALLED ===");
        // Check if fragment is still attached
        if (!isAdded() || getContext() == null || getActivity() == null) {
            Log.e(TAG, "Fragment not attached, cannot send video message");
            return;
        }

        String[] dimensions = calculateVideoDimensions(getContext(), videoFile, Uri.fromFile(videoFile));
        String imageWidthDp = dimensions[0];
        String imageHeightDp = dimensions[1];
        String aspectRatio = dimensions[2];

        String receiverUid = getActivity().getIntent().getStringExtra("friendUidKey");
        String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        String uniqDate = Constant.getCurrentDate();
        boolean isNewDate = uniqueDates.add(uniqDate);

        ArrayList<emojiModel> emojiModels = new ArrayList<>();
        emojiModels.add(new emojiModel("", ""));

        messageModel model = new messageModel(
                senderId, messageBoxMy.getText().toString().trim(), currentTime,
                videoFile.toString(), Constant.video,
                "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""),
                "", "", "", "", "", modelId, receiverUid,
                "", "", "", videoFile.getName(), fileThumbName, fileThumbName,
                "", 1, isNewDate ? uniqDate : ":" + uniqDate, emojiModels, "",
                Constant.getCurrentTimestamp(), imageHeightDp, imageWidthDp, aspectRatio, "1"
        );

        messageList.add(model);
        Log.d("PENDING_MESSAGES", "=== ABOUT TO CALL insertMessageToDatabase (VIDEO) ===");
        Log.d("PENDING_MESSAGES", "Model: " + (model != null ? "not null" : "null"));
        insertMessageToDatabase(model);
        updateChatAdapter();

        String captionText = messageBoxMy.getText().toString().trim();
        UploadChatHelper uploadHelper = new UploadChatHelper(requireContext(), videoFile, thumbnailFile, senderId, userFTokenKey);
        uploadHelper.uploadContent(
                Constant.video, Uri.fromFile(videoFile), captionText, modelId, thumbnailFile, fileThumbName, fileName,
                null, null, null, null, getFileExtension(Uri.fromFile(videoFile), requireContext()), receiverUid,
                "", "", "", "", model.getReplytextData(), model.getDataType(),
                model.getFileName(), "", imageHeightDp, imageWidthDp, aspectRatio);

        Constant.dialogLayoutFullScreen.dismiss();
        closeWithAnimation();
        if (playerPreview != null) {
            playerPreview.stop();
            playerPreview.release();
            playerPreview = null;
        }
        // The closeWithAnimation() call is now handled by the dialog's onDismissListener
    }

    /**
     * Calculate image dimensions specifically for camera captures
     * This method swaps width and height to correct camera orientation issues
     */
    private String[] calculateCameraImageDimensions(Context context, File imageFile, Uri imageUri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            // Get dimensions from image source
            if (imageFile != null && imageFile.exists()) {
                BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            } else if (imageUri != null) {
                InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                BitmapFactory.decodeStream(inputStream, null, options);
                if (inputStream != null) {
                    inputStream.close();
                }
            }

            int width = options.outWidth;
            int height = options.outHeight;

            // Swap width and height for camera captures (camera reports dimensions incorrectly)
            int swappedWidth = height;
            int swappedHeight = width;

            // Device density
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float density = displayMetrics.density;

            // Convert to dp
            float imageWidthDp = swappedWidth / density;
            float imageHeightDp = swappedHeight / density;

            // Aspect ratio
            float aspectRatio = (float) swappedWidth / swappedHeight;

            // Convert to strings
            String widthStr = String.format("%.2f", imageWidthDp);
            String heightStr = String.format("%.2f", imageHeightDp);
            String aspectRatioStr = String.format("%.2f", aspectRatio);

            // Logging for debugging
            Log.d("CameraImageDimensions", "Original: " + width + "x" + height + ", Swapped: " + swappedWidth + "x" + swappedHeight);
            Log.d("CameraImageDimensions", "Width: " + widthStr + "dp, Height: " + heightStr + "dp, AspectRatio: " + aspectRatioStr);

            return new String[]{widthStr, heightStr, aspectRatioStr};
        } catch (Exception e) {
            Log.e("CameraImageDimensions", "Error calculating camera dimensions: " + e.getMessage());
            // Default values
            return new String[]{"300.00", "300.00", "1.00"};
        }
    }
    //endregion

    @UnstableApi
    private static class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
        private final Context context;
        private final List<Uri> images;
        private final OnImageClickListener listener;
        private final OnMultipleImageSendListener multipleImageSendListener;
        private final OnVideoSelectionListener videoSelectionListener;
        private final OnMultipleVideoSendListener multipleVideoSendListener;

        // Multi-selection support
        private ArrayList<Uri> selectedImageUris = new ArrayList<>();
        private boolean isMultiSelectionMode = false;
        private OnImageSelectionListener selectionListener;
        private static final int MAX_SELECTION_LIMIT = 30;
        
        // Individual caption support
        private Map<Integer, String> imageCaptions = new HashMap<>();
        private boolean isUpdatingText = false;

        interface OnImageClickListener {
            void onImageClick(Uri uri);
        }

        public interface OnImageSelectionListener {
            void onImageSelectionChanged(ArrayList<Uri> selectedUris);
            void onMultiSelectionModeChanged(boolean isMultiMode);
        }

        public interface OnMultipleImageSendListener {
            void onMultipleImageSend(ArrayList<Uri> imageUris, String captionText);
            void onMultipleImageSend(ArrayList<Uri> imageUris, List<String> individualCaptions);
        }

        public interface OnVideoSelectionListener {
            void onVideoSelected(Uri videoUri);
        }

        public interface OnMultipleVideoSendListener {
            void onMultipleVideoSend(ArrayList<Uri> videoUris, List<String> individualCaptions);
        }

        /**
         * Simple ViewPager2 Adapter for video preview
         */
        private static class VideoPreviewAdapter extends RecyclerView.Adapter<VideoPreviewAdapter.VideoViewHolder> {
            private final List<Uri> videoUris;
            private final Context context;
            private final OnVideoSelectionListener videoSelectionListener;

            public VideoPreviewAdapter(List<Uri> videoUris, Context context, OnVideoSelectionListener videoSelectionListener) {
                this.videoUris = videoUris;
                this.context = context;
                this.videoSelectionListener = videoSelectionListener;
            }

            @NonNull
            @Override
            public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d("DEBUG_VIDEO", "VideoPreviewAdapter onCreateViewHolder");
                View view = LayoutInflater.from(context).inflate(R.layout.item_main_video_preview, parent, false);
                return new VideoViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
                Log.d("DEBUG_VIDEO", "VideoPreviewAdapter onBindViewHolder position: " + position);
                Uri videoUri = videoUris.get(position);
                
                if (holder.videoThumbnail != null && videoUri != null) {
                    // Load video thumbnail
                    com.bumptech.glide.Glide.with(context)
                            .load(videoUri)
                            .into(holder.videoThumbnail);
                }

                // Set up play button click listener
                if (holder.playPauseButton != null) {
                    holder.playPauseButton.setOnClickListener(v -> {
                        Log.d("DEBUG_VIDEO", "Play button clicked for video: " + videoUri);
                        // Call handleVideoSelection to play the video
                        if (context instanceof Activity) {
                            ((Activity) context).runOnUiThread(() -> {
                                if (videoSelectionListener != null) {
                                    videoSelectionListener.onVideoSelected(videoUri);
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public int getItemCount() {
                return videoUris.size();
            }

            public static class VideoViewHolder extends RecyclerView.ViewHolder {
                ImageView videoThumbnail;
                ImageView playPauseButton;

                public VideoViewHolder(@NonNull View itemView) {
                    super(itemView);
                    videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
                    playPauseButton = itemView.findViewById(R.id.playPauseButton);
                }
            }
        }

        public void setOnImageSelectionListener(OnImageSelectionListener listener) {
            this.selectionListener = listener;
        }

        /**
         * Show multi-video preview with swipe functionality
         */
        private void showMultiVideoPreview(ArrayList<Uri> videoUris) {
            Log.d("VIDEO_DIALOG_SHOW", "CameraGalleryFragment.showMultiVideoPreview called");
            Log.d("DEBUG_VIDEO", "=== showMultiVideoPreview CALLED ===");
            Log.d("DEBUG_VIDEO", "Video URIs count: " + videoUris.size());

            // Duplicate dialog avoid करा (Avoid duplicate dialog)
            if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                Log.d("DEBUG_VIDEO", "Dialog already showing, dismissing previous one");
                Constant.dialogLayoutFullScreen.dismiss();
            }

            // डायलॉग लेआउट सेटअप करा (Setup dialog layout)
            Log.d("DEBUG_VIDEO", "Creating new dialog with layout: dialogue_full_screen_dialogue");
            Constant.dialogueFullScreen(context, R.layout.dialogue_full_screen_dialogue);
            Log.d("VIDEO_DIALOG_SHOW", "About to show multi-video dialog");
            Constant.dialogLayoutFullScreen.show();

            // Set up the video preview with ViewPager2
            setupMultiVideoPreviewDialog(videoUris);
        }

        /**
         * Setup multi-video preview dialog with ViewPager2
         */
        private void setupMultiVideoPreviewDialog(ArrayList<Uri> videoUris) {
            Log.d("DEBUG_VIDEO", "=== setupMultiVideoPreviewDialog CALLED ===");
            Log.d("DEBUG_VIDEO", "Video URIs count: " + videoUris.size());

            // Find the ViewPager2 in the dialog
            androidx.viewpager2.widget.ViewPager2 videoPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
            if (videoPreview == null) {
                Log.e("DEBUG_VIDEO", "ViewPager2 not found in dialog");
                return;
            }

            // Create adapter for videos (using ViewPager2)
            VideoPreviewAdapter videoAdapter = new VideoPreviewAdapter(videoUris, context, videoSelectionListener);
            videoPreview.setAdapter(videoAdapter);

            // Setup page change callback for counter
            videoPreview.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    Log.d("DEBUG_VIDEO", "Video page changed to position: " + position);
                    updateVideoCounter(position + 1, videoUris.size());
                }
            });

            // Setup send functionality and caption support
            setupVideoSendFunctionality(videoUris);
            setupVideoCaptionFunctionality(videoUris);

            // Show counter
            updateVideoCounter(1, videoUris.size());
        }

        /**
         * Update video counter display
         */
        private void updateVideoCounter(int current, int total) {
            Log.d("DEBUG_VIDEO", "Updating video counter: " + current + "/" + total);
            TextView videoCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
            if (videoCounter != null) {
                videoCounter.setVisibility(View.VISIBLE);
                videoCounter.setText(current + " / " + total);
            }
        }

        /**
         * Setup video send functionality
         */
        private void setupVideoSendFunctionality(ArrayList<Uri> videoUris) {
            Log.d("DEBUG_VIDEO", "=== setupVideoSendFunctionality CALLED ===");
            
            // Find send button
            ImageView sendButton = Constant.dialogLayoutFullScreen.findViewById(R.id.send);
            if (sendButton == null) {
                Log.e("DEBUG_VIDEO", "Send button not found");
                return;
            }

            sendButton.setOnClickListener(v -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(context);
                }
                Log.d("DEBUG_VIDEO", "Send button clicked for " + videoUris.size() + " videos");
                
                // MANUAL CAPTION SAVE - Force save all captions before sending
                Log.d("DEBUG_VIDEO", "=== MANUAL VIDEO CAPTION SAVE START ===");
                androidx.viewpager2.widget.ViewPager2 currentPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
                EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                
                if (currentPreview != null && messageBoxMy != null) {
                    int currentPosition = currentPreview.getCurrentItem();
                    String currentCaption = messageBoxMy.getText().toString().trim();
                    imageCaptions.put(currentPosition, currentCaption);
                    Log.d("DEBUG_VIDEO", "Saving final caption for video position " + currentPosition + ": '" + currentCaption + "'");
                }

                // Force save captions for ALL videos
                for (int i = 0; i < videoUris.size(); i++) {
                    if (!imageCaptions.containsKey(i)) {
                        imageCaptions.put(i, "");
                        Log.d("DEBUG_VIDEO", "Added empty caption for missing video position " + i);
                    }
                }

                Log.d("DEBUG_VIDEO", "Final video imageCaptions map before sending: " + imageCaptions.toString());
                Log.d("DEBUG_VIDEO", "=== MANUAL VIDEO CAPTION SAVE END ===");
                
                // Send videos with individual captions
                sendMultipleVideosWithIndividualCaptions(videoUris);
                
                // Dismiss dialog after sending
                if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                    Constant.dialogLayoutFullScreen.dismiss();
                }
            });
        }

        /**
         * Setup video caption functionality
         */
        private void setupVideoCaptionFunctionality(ArrayList<Uri> videoUris) {
            Log.d("DEBUG_VIDEO", "=== setupVideoCaptionFunctionality CALLED ===");
            
            // Find message box
            EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
            if (messageBoxMy == null) {
                Log.e("DEBUG_VIDEO", "Message box not found");
                return;
            }

            // Set up TextWatcher to save captions as user types
            messageBoxMy.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Not needed
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Not needed
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    if (!isUpdatingText) {
                        androidx.viewpager2.widget.ViewPager2 currentPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
                        if (currentPreview != null) {
                            int currentPosition = currentPreview.getCurrentItem();
                            String currentCaption = s.toString().trim();
                            imageCaptions.put(currentPosition, currentCaption);
                            Log.d("DEBUG_VIDEO", "Saved video caption for position " + currentPosition + ": '" + currentCaption + "'");
                        }
                    }
                }
            });

            // Setup page change callback to update caption when swiping
            androidx.viewpager2.widget.ViewPager2 videoPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
            if (videoPreview != null) {
                videoPreview.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        Log.d("DEBUG_VIDEO", "Video page changed to position: " + position);
                        
                        // Update caption text when swiping
                        if (messageBoxMy != null) {
                            String currentCaption = imageCaptions.get(position);
                            Log.d("DEBUG_VIDEO", "Current video caption for position " + position + ": '" + currentCaption + "'");
                            isUpdatingText = true;
                            if (currentCaption != null) {
                                messageBoxMy.setText(currentCaption);
                            } else {
                                messageBoxMy.setText("");
                            }
                            isUpdatingText = false;
                        }
                    }
                });
            }
        }

        public void setMultiSelectionMode(boolean isMultiMode) {
            Log.d("PENDING_MESSAGES", "=== setMultiSelectionMode CALLED: " + isMultiMode + " ===");
            this.isMultiSelectionMode = isMultiMode;
            if (!isMultiMode) {
                selectedImageUris.clear();
                Log.d("PENDING_MESSAGES", "=== CLEARED SELECTED IMAGES ===");
            }
            notifyDataSetChanged();
            if (selectionListener != null) {
                selectionListener.onMultiSelectionModeChanged(isMultiMode);
            }
        }

        public ArrayList<Uri> getSelectedImageUris() {
            return selectedImageUris;
        }

        public boolean isMultiSelectionMode() {
            return isMultiSelectionMode;
        }

        public void clearSelection() {
            selectedImageUris.clear();
            notifyDataSetChanged();
            if (selectionListener != null) {
                selectionListener.onImageSelectionChanged(selectedImageUris);
            }
        }

        /**
         * Setup multi-image preview with selected images (images only)
         */
        public void setupMultiImagePreview() {
            Log.d("DEBUG_VIDEO", "=== GALLERY ADAPTER setupMultiImagePreview() START ===");
            Log.d("DEBUG_VIDEO", "selectedImageUris size: " + selectedImageUris.size());
            Log.d("DEBUG_VIDEO", "Context: " + (context != null ? "not null" : "null"));
            Log.d("DEBUG_VIDEO", "imageCaptions map before clear: " + imageCaptions.toString());
            
            // Log all selected URIs and their types
            for (int i = 0; i < selectedImageUris.size(); i++) {
                Uri uri = selectedImageUris.get(i);
                String mimeType = context.getContentResolver().getType(uri);
                Log.d("DEBUG_VIDEO", "Selected URI " + i + ": " + uri + " (MIME: " + mimeType + ")");
            }

            if (selectedImageUris.isEmpty()) {
                Log.d("DEBUG_CAPTION", "No images selected, returning");
                return;
            }

            // Clear previous captions for fresh start
            imageCaptions.clear();
            Log.d("DEBUG_CAPTION", "Cleared previous imageCaptions map");

            // Separate images and videos
            ArrayList<Uri> imageUris = new ArrayList<>();
            ArrayList<Uri> videoUris = new ArrayList<>();
            for (Uri uri : selectedImageUris) {
                String mimeType = context.getContentResolver().getType(uri);
                Log.d("DEBUG_CAPTION", "URI: " + uri + ", MIME: " + mimeType);
                if (mimeType != null && mimeType.startsWith("image/")) {
                    imageUris.add(uri);
                } else if (mimeType != null && mimeType.startsWith("video/")) {
                    videoUris.add(uri);
                }
            }

            Log.d("DEBUG_CAPTION", "Filtered imageUris size: " + imageUris.size());
            Log.d("DEBUG_CAPTION", "Filtered videoUris size: " + videoUris.size());

            // If only videos are selected, show multi-video preview
            if (imageUris.isEmpty() && !videoUris.isEmpty()) {
                Log.d("DEBUG_VIDEO", "=== ONLY VIDEOS SELECTED ===");
                Log.d("DEBUG_VIDEO", "Video count: " + videoUris.size());
                showMultiVideoPreview(videoUris);
                return;
            }

            // If no images or videos found
            if (imageUris.isEmpty()) {
                Log.d("DEBUG_CAPTION", "No images or videos found in selection");
                return;
            }

            // Duplicate dialog avoid करा (Avoid duplicate dialog)
            if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                Log.d("DEBUG_CAPTION", "Dialog already showing, dismissing previous one");
                Constant.dialogLayoutFullScreen.dismiss();
            }

            // डायलॉग लेआउट सेटअप करा (Setup dialog layout)
            Log.d("DEBUG_CAPTION", "Creating new dialog with layout: dialogue_full_screen_dialogue");
            Constant.dialogueFullScreen(context, R.layout.dialogue_full_screen_dialogue);
            Log.d("DEBUG_CAPTION", "Dialog created, showing dialog");
            Constant.dialogLayoutFullScreen.show();
            Log.d("DEBUG_CAPTION", "Dialog shown successfully");

            // Window setup करा (Setup window)
            android.view.Window window = Constant.dialogLayoutFullScreen.getWindow();
            if (window != null) {
                androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false);
                View rootView = window.getDecorView().findViewById(android.R.id.content);
                rootView.setFitsSystemWindows(false);
            }

            window.setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);

            Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
                android.app.Activity activity = (android.app.Activity) context;
                android.view.Window activityWindow = activity.getWindow();
                androidx.core.view.WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
                View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
                rootView.setFitsSystemWindows(true);
            });

            // Hide the single image view and show the horizontal gallery preview
            ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
            singleImageView.setVisibility(View.GONE);

            // Hide ViewPager2 and show horizontal gallery
            androidx.viewpager2.widget.ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
            if (viewPager2 != null) {
                viewPager2.setVisibility(View.GONE);
            }

            // Setup horizontal gallery preview
            setupHorizontalGalleryPreview(imageUris);
        }

        /**
         * Setup horizontal gallery preview
         */
        private void setupHorizontalGalleryPreview(ArrayList<Uri> imageUris) {
            Log.d("HorizontalGallery", "Setting up horizontal gallery with " + imageUris.size() + " images");

            // मुख्य इमेज प्रिव्यू ViewPager2 सेटअप करा (Setup main image preview ViewPager2)
            androidx.viewpager2.widget.ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
            if (mainImagePreview != null) {
                mainImagePreview.setVisibility(View.VISIBLE);

                // मुख्य प्रिव्यूसाठी adapter सेटअप करा (Setup adapter for main preview)
                com.Appzia.enclosure.Adapter.MainImagePreviewAdapter mainAdapter = new com.Appzia.enclosure.Adapter.MainImagePreviewAdapter(context, imageUris);
                
                // Removed individual caption logic
                
                mainImagePreview.setAdapter(mainAdapter);

                // पेज चेंज callback सेटअप करा (Setup page change callback)
                mainImagePreview.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        Log.d("DEBUG_CAPTION", "=== PAGE CHANGED TO POSITION " + position + " ===");
                        updateImageCounter(position + 1, imageUris.size());
                        
                        // Update caption EditText with current image's caption
                        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                        Log.d("DEBUG_CAPTION", "messageBoxMy found: " + (messageBoxMy != null ? "YES" : "NO"));
                        
                        if (messageBoxMy != null) {
                            String currentCaption = imageCaptions.get(position);
                            Log.d("DEBUG_CAPTION", "Current caption for position " + position + ": '" + currentCaption + "'");
                            Log.d("DEBUG_CAPTION", "Full imageCaptions map: " + imageCaptions.toString());
                            Log.d("DEBUG_CAPTION", "isUpdatingText flag: " + isUpdatingText);

                            // Set flag to prevent TextWatcher from saving during programmatic update
                            isUpdatingText = true;
                            Log.d("DEBUG_CAPTION", "Set isUpdatingText to TRUE");

                            if (currentCaption != null) {
                                messageBoxMy.setText(currentCaption);
                                // Position cursor at the end of the text
                                messageBoxMy.setSelection(messageBoxMy.getText().length());
                                Log.d("DEBUG_CAPTION", "Set EditText text to: '" + currentCaption + "'");
                            } else {
                                messageBoxMy.setText("");
                                Log.d("DEBUG_CAPTION", "Set EditText text to empty (caption was null)");
                            }

                            // Reset flag to allow TextWatcher to save again
                            isUpdatingText = false;
                            Log.d("DEBUG_CAPTION", "Set isUpdatingText to FALSE");
                        } else {
                            Log.e("DEBUG_CAPTION", "messageBoxMy is NULL! Cannot update caption");
                        }
                    }
                });
            }

            // Setup caption and send functionality
            setupCaptionAndSendFunctionality(imageUris);

            // Image counter सेटअप करा (Setup image counter)
            updateImageCounter(1, imageUris.size());
        }

        /**
         * Setup caption input and send functionality for multi-image preview
         */
        private void setupCaptionAndSendFunctionality(ArrayList<Uri> imageUris) {
            Log.d("CaptionSetup", "Setting up caption and send functionality for " + imageUris.size() + " images");

            // Get UI elements
            LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
            EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
            LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);

            // Setup back button
            if (backarrow != null) {
                backarrow.setOnClickListener(v -> {
                    Constant.dialogLayoutFullScreen.dismiss();
                });
            }

            // Setup TextWatcher to save captions as user types
            if (messageBoxMy != null) {
                Log.d("DEBUG_CAPTION", "Setting up TextWatcher for messageBoxMy");
                messageBoxMy.addTextChangedListener(new android.text.TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        Log.d("DEBUG_CAPTION", "beforeTextChanged: '" + s + "'");
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.d("DEBUG_CAPTION", "onTextChanged: '" + s + "'");
                    }

                    @Override
                    public void afterTextChanged(android.text.Editable s) {
                        Log.d("DEBUG_CAPTION", "=== TEXT CHANGED ===");
                        Log.d("DEBUG_CAPTION", "isUpdatingText: " + isUpdatingText);
                        Log.d("DEBUG_CAPTION", "New text: '" + s.toString() + "'");
                        
                        if (!isUpdatingText) {
                            androidx.viewpager2.widget.ViewPager2 currentPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
                            Log.d("DEBUG_CAPTION", "currentPreview found: " + (currentPreview != null ? "YES" : "NO"));
                            
                            if (currentPreview != null) {
                                int currentPosition = currentPreview.getCurrentItem();
                                String currentCaption = s.toString().trim();
                                imageCaptions.put(currentPosition, currentCaption);
                                Log.d("DEBUG_CAPTION", "SAVED caption for position " + currentPosition + ": '" + currentCaption + "'");
                                Log.d("DEBUG_CAPTION", "Updated imageCaptions map: " + imageCaptions.toString());
                            } else {
                                Log.e("DEBUG_CAPTION", "currentPreview is NULL! Cannot save caption");
                            }
                        } else {
                            Log.d("DEBUG_CAPTION", "Skipping save because isUpdatingText is TRUE");
                        }
                    }
                });
                Log.d("DEBUG_CAPTION", "TextWatcher setup completed");
            } else {
                Log.e("DEBUG_CAPTION", "messageBoxMy is NULL! Cannot setup TextWatcher");
            }

            // Setup send button functionality
            if (sendGrp != null) {
                Log.d("DEBUG_CAPTION", "Setting up send button click listener");
                sendGrp.setOnClickListener(v -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Constant.Vibrator(context);
                    }
                    Log.d("DEBUG_CAPTION", "=== SEND BUTTON CLICKED ===");
                    Log.d("DEBUG_CAPTION", "Number of images: " + imageUris.size());
                    Log.d("DEBUG_CAPTION", "Current imageCaptions map: " + imageCaptions.toString());
                    
                    // MANUAL CAPTION SAVE - Force save all captions before sending
                    Log.d("DEBUG_CAPTION", "=== MANUAL CAPTION SAVE START ===");
                    androidx.viewpager2.widget.ViewPager2 currentPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
                    Log.d("DEBUG_CAPTION", "currentPreview found: " + (currentPreview != null ? "YES" : "NO"));
                    
                    if (currentPreview != null) {
                        int currentPosition = currentPreview.getCurrentItem();
                        String currentCaption = messageBoxMy != null ? messageBoxMy.getText().toString().trim() : "";
                        imageCaptions.put(currentPosition, currentCaption);
                        Log.d("DEBUG_CAPTION", "Saving final caption for position " + currentPosition + ": '" + currentCaption + "'");
                    }

                    // Force save captions for ALL images
                    for (int i = 0; i < imageUris.size(); i++) {
                        if (!imageCaptions.containsKey(i)) {
                            imageCaptions.put(i, "");
                            Log.d("DEBUG_CAPTION", "Added empty caption for missing position " + i);
                        }
                    }

                    Log.d("DEBUG_CAPTION", "Final imageCaptions map before sending: " + imageCaptions.toString());
                    Log.d("DEBUG_CAPTION", "=== MANUAL CAPTION SAVE END ===");
                    
                    // Send images with individual captions
                    sendMultipleImagesWithIndividualCaptions(imageUris);
                    
                    // Dismiss dialog
                    Constant.dialogLayoutFullScreen.dismiss();
                });
                Log.d("DEBUG_CAPTION", "Send button click listener setup completed");
            } else {
                Log.e("DEBUG_CAPTION", "sendGrp is NULL! Cannot setup send button");
            }

            // Setup theme color for send button
            try {
                Constant.getSfFuncion(context);
                String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                android.content.res.ColorStateList tintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(themColor));
                if (sendGrp != null) {
                    sendGrp.setBackgroundTintList(tintList);
                }
            } catch (Exception e) {
                Log.e("CaptionSetup", "Error setting theme color: " + e.getMessage());
            }
        }

        /**
         * Send multiple images with individual captions
         */
        private void sendMultipleImagesWithCaption(ArrayList<Uri> imageUris, String captionText) {
            Log.d("SendMultipleImages", "Sending " + imageUris.size() + " images with individual captions");
            
            // Get the adapter from the ViewPager2 to access individual captions
            androidx.viewpager2.widget.ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
            if (mainImagePreview != null && mainImagePreview.getAdapter() instanceof com.Appzia.enclosure.Adapter.MainImagePreviewAdapter) {
                com.Appzia.enclosure.Adapter.MainImagePreviewAdapter adapter = (com.Appzia.enclosure.Adapter.MainImagePreviewAdapter) mainImagePreview.getAdapter();
                List<String> individualCaptions = adapter.getAllCaptions();
                
                Log.d("SendMultipleImages", "Individual captions: " + individualCaptions);
                
                if (multipleImageSendListener != null) {
                    multipleImageSendListener.onMultipleImageSend(imageUris, individualCaptions);
                } else {
                    Log.e("SendMultipleImages", "Multiple image send listener is null");
                }
            } else {
                // Fallback to single caption for all images
                if (multipleImageSendListener != null) {
                    multipleImageSendListener.onMultipleImageSend(imageUris, captionText);
                } else {
                    Log.e("SendMultipleImages", "Multiple image send listener is null");
                }
            }
        }

        /**
         * Send multiple images with individual captions from imageCaptions map
         */
        private void sendMultipleImagesWithIndividualCaptions(ArrayList<Uri> imageUris) {
            Log.d("DEBUG_CAPTION", "=== SEND MULTIPLE IMAGES WITH INDIVIDUAL CAPTIONS ===");
            Log.d("DEBUG_CAPTION", "Number of images: " + imageUris.size());
            Log.d("DEBUG_CAPTION", "imageCaptions map size: " + imageCaptions.size());
            Log.d("DEBUG_CAPTION", "imageCaptions map contents: " + imageCaptions.toString());
            
            // Convert map to list for the callback
            List<String> individualCaptions = new ArrayList<>();
            for (int i = 0; i < imageUris.size(); i++) {
                String caption = imageCaptions.get(i);
                if (caption == null) {
                    caption = "";
                    Log.d("DEBUG_CAPTION", "Image " + i + " caption was null, using empty string");
                } else {
                    Log.d("DEBUG_CAPTION", "Image " + i + " caption: '" + caption + "'");
                }
                individualCaptions.add(caption);
            }
            
            Log.d("DEBUG_CAPTION", "Individual captions list: " + individualCaptions);
            Log.d("DEBUG_CAPTION", "multipleImageSendListener: " + (multipleImageSendListener != null ? "NOT NULL" : "NULL"));
            
            if (multipleImageSendListener != null) {
                Log.d("DEBUG_CAPTION", "Calling onMultipleImageSend with " + imageUris.size() + " images and " + individualCaptions.size() + " captions");
                multipleImageSendListener.onMultipleImageSend(imageUris, individualCaptions);
                Log.d("DEBUG_CAPTION", "onMultipleImageSend call completed");
            } else {
                Log.e("DEBUG_CAPTION", "Multiple image send listener is NULL! Cannot send images");
            }
        }

        /**
         * Send multiple videos with individual captions from imageCaptions map
         */
        private void sendMultipleVideosWithIndividualCaptions(ArrayList<Uri> videoUris) {
            Log.d("DEBUG_VIDEO", "Sending " + videoUris.size() + " videos with individual captions from map");
            Log.d("DEBUG_VIDEO", "imageCaptions map size: " + imageCaptions.size());
            Log.d("DEBUG_VIDEO", "imageCaptions map contents: " + imageCaptions.toString());
            
            // Convert map to list for the callback
            List<String> individualCaptions = new ArrayList<>();
            for (int i = 0; i < videoUris.size(); i++) {
                String caption = imageCaptions.get(i);
                if (caption == null) {
                    caption = "";
                    Log.d("DEBUG_VIDEO", "Video " + i + " caption was null, using empty string");
                } else {
                    Log.d("DEBUG_VIDEO", "Video " + i + " caption: '" + caption + "'");
                }
                individualCaptions.add(caption);
            }
            
            Log.d("DEBUG_VIDEO", "Individual video captions list: " + individualCaptions);
            
            // Call the fragment's method to handle multiple video send
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(() -> {
                    // Use the correct video send listener
                    if (multipleVideoSendListener != null) {
                        Log.d("DEBUG_VIDEO", "Calling multipleVideoSendListener.onMultipleVideoSend");
                        multipleVideoSendListener.onMultipleVideoSend(videoUris, individualCaptions);
                    } else {
                        Log.e("DEBUG_VIDEO", "multipleVideoSendListener is null!");
                    }
                });
            }
        }


        /**
         * Show caption edit dialog for individual image
         */
        private void showCaptionEditDialog(int position, String currentCaption, com.Appzia.enclosure.Adapter.MainImagePreviewAdapter adapter) {
            Log.d("DEBUG_CAPTION", "=== SHOW CAPTION EDIT DIALOG ===");
            Log.d("DEBUG_CAPTION", "Position: " + position);
            Log.d("DEBUG_CAPTION", "Current caption: '" + currentCaption + "'");
            Log.d("DEBUG_CAPTION", "Adapter: " + (adapter != null ? "NOT NULL" : "NULL"));
            
            // Create a simple dialog for caption editing
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Edit Caption for Image " + (position + 1));
            
            // Create EditText for caption input
            final EditText input = new EditText(context);
            input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            input.setText(currentCaption);
            input.setHint("Enter caption for this image...");
            input.setMinLines(2);
            input.setMaxLines(4);
            
            // Set margins
            android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(50, 20, 50, 20);
            input.setLayoutParams(params);
            
            builder.setView(input);
            
            builder.setPositiveButton("Save", (dialog, which) -> {
                String newCaption = input.getText().toString().trim();
                Log.d("DEBUG_CAPTION", "=== SAVING CAPTION FROM DIALOG ===");
                Log.d("DEBUG_CAPTION", "Position: " + position);
                Log.d("DEBUG_CAPTION", "New caption: '" + newCaption + "'");
                Log.d("DEBUG_CAPTION", "Updating adapter...");
                adapter.updateCaption(position, newCaption);
                Log.d("DEBUG_CAPTION", "Adapter updated successfully");
            });
            
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                Log.d("DEBUG_CAPTION", "Caption edit dialog cancelled");
                dialog.cancel();
            });
            
            Log.d("DEBUG_CAPTION", "Showing caption edit dialog");
            builder.show();
        }

        /**
         * Update image counter
         */
        private void updateImageCounter(int current, int total) {
            TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
            if (imageCounter != null) {
                imageCounter.setVisibility(View.VISIBLE);
                imageCounter.setText(current + " / " + total);
            }
        }

        GalleryAdapter(Context context, List<Uri> images, OnImageClickListener listener, OnMultipleImageSendListener multipleImageSendListener, OnVideoSelectionListener videoSelectionListener, OnMultipleVideoSendListener multipleVideoSendListener) {
            this.context = context;
            this.images = images;
            this.listener = listener;
            this.multipleImageSendListener = multipleImageSendListener;
            this.videoSelectionListener = videoSelectionListener;
            this.multipleVideoSendListener = multipleVideoSendListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gallery_image, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Log.d("PENDING_MESSAGES", "=== onBindViewHolder CALLED for position: " + position + " ===");
            Uri uri = images.get(position);
            String mimeType = context.getContentResolver().getType(uri);
            boolean isVideo = mimeType != null && mimeType.startsWith("video/");
            boolean isSelected = selectedImageUris.contains(uri);

            if (isVideo) {
                loadVideoThumbnail(holder, uri);
            } else {
                loadImageThumbnail(holder, uri);
            }
            Constant.getSfFuncion(holder.checkmark.getContext());
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            ColorStateList tintList = ColorStateList.valueOf(Color.parseColor(themColor));
            holder.checkmark.setImageTintList(tintList);
            // Update checkmark visibility
            holder.checkmark.setVisibility(isMultiSelectionMode && isSelected ? View.VISIBLE : View.GONE);

            // Apply selection overlay with opposite dimming logic
            if (isMultiSelectionMode) {
                boolean isLimitReached = selectedImageUris.size() >= MAX_SELECTION_LIMIT;

                if (isSelected) {
                    // Selected images are dimmed (darker)
                    holder.imageView.setAlpha(0.5f);
                } else if (isLimitReached) {
                    // When limit reached, unselected images are very dim
                    holder.imageView.setAlpha(0.2f);
                } else {
                    // Unselected images are normal (bright)
                    holder.imageView.setAlpha(1.0f);
                }
            } else {
                holder.imageView.setAlpha(1.0f);
            }

            // Set click listener on the entire item view for better UX
            holder.itemView.setOnClickListener(v -> {
                Log.d("DEBUG_IMAGE_STORAGE", "=== GALLERY ITEM CLICKED ===");
                Log.d("PENDING_MESSAGES", "=== GALLERY ITEM CLICKED ===");
                Log.d("DEBUG_IMAGE_STORAGE", "Position: " + position);
                Log.d("DEBUG_IMAGE_STORAGE", "URI: " + uri);
                Log.d("DEBUG_IMAGE_STORAGE", "Is multi-selection mode: " + isMultiSelectionMode);
                Log.d("DEBUG_IMAGE_STORAGE", "Is selected: " + isSelected);
                
                if (isMultiSelectionMode) {
                    Log.d("DEBUG_IMAGE_STORAGE", "Handling multi-selection");
                    Log.d("PENDING_MESSAGES", "=== MULTI-SELECTION MODE ACTIVE ===");
                    // Handle multi-selection with limit
                    if (isSelected) {
                        // Always allow deselection
                        selectedImageUris.remove(uri);
                        Log.d("DEBUG_IMAGE_STORAGE", "Deselected image, remaining: " + selectedImageUris.size());
                    } else {
                        // Check selection limit before adding
                        if (selectedImageUris.size() < MAX_SELECTION_LIMIT) {
                            selectedImageUris.add(uri);
                            Log.d("DEBUG_IMAGE_STORAGE", "Selected image, total: " + selectedImageUris.size());
                        } else {
                            // Show toast when limit reached
                            Log.d("DEBUG_IMAGE_STORAGE", "Selection limit reached");
                            Toast.makeText(context, "Maximum " + MAX_SELECTION_LIMIT + " images can be selected", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    notifyItemChanged(position);

                    if (selectionListener != null) {
                        selectionListener.onImageSelectionChanged(selectedImageUris);
                    }
                } else {
                    Log.d("DEBUG_IMAGE_STORAGE", "Handling single selection - calling listener.onImageClick");
                    Log.d("PENDING_MESSAGES", "=== SINGLE SELECTION MODE - Calling listener.onImageClick ===");
                    // Original single selection behavior
                    listener.onImageClick(uri);
                }
            });
        }

        private void loadVideoThumbnail(ViewHolder holder, Uri uri) {
            Bitmap thumbnail = null;
            try {
                // Ensure the URI is for a video and can be parsed for ID
                if (ContentUris.parseId(uri) != -1) {
                    thumbnail = MediaStore.Video.Thumbnails.getThumbnail(
                            context.getContentResolver(),
                            ContentUris.parseId(uri),
                            MediaStore.Video.Thumbnails.MINI_KIND,
                            null
                    );
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading video thumbnail for URI: " + uri, e);
            }

            Glide.with(context)
                    .load(thumbnail != null ? thumbnail : R.drawable.invite_dark)
                    .thumbnail(0.25f)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Smaller disk cache
                    .centerCrop()
                    .override(300, 300) // Resize to reduce memory/disk usage
                    .format(DecodeFormat.PREFER_RGB_565) // Use lower quality format
                    .into(holder.imageView);

            holder.playButton.setVisibility(View.VISIBLE);

        }

        private void loadImageThumbnail(ViewHolder holder, Uri uri) {
            Glide.with(context)
                    .load(uri)
                    .thumbnail(0.25f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .override(300, 300)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .into(holder.imageView);

        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            Glide.with(context).clear(holder.imageView);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView imageView;
            final ImageView playButton;
            final ImageView checkmark;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.gallery_image);
                playButton = itemView.findViewById(R.id.play_button);
                checkmark = itemView.findViewById(R.id.checkmark);
            }
        }
    }

    private void restartCameraWithFlash() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            startCamera();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (camera != null && camera.getCameraInfo().hasFlashUnit()) {
                    camera.getCameraControl().enableTorch(isFlashOn);
                } else {
                    Log.w(TAG, "Flashlight not supported on this camera");
                    Constant.showCustomToast("Flashlight not available", customToastCard, customToastText);
                }
            }, 500);
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void focusOnPoint(float x, float y) {
        if (camera == null || binding.cameraPreview == null) return;

        MeteringPointFactory factory = binding.cameraPreview.getMeteringPointFactory();
        MeteringPoint point = factory.createPoint(x, y);

        FocusMeteringAction action = new FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                .addPoint(point, FocusMeteringAction.FLAG_AE)
                .setAutoCancelDuration(3, TimeUnit.SECONDS)
                .build();

        camera.getCameraControl().startFocusAndMetering(action);
    }

    private void setLightStatusBar() {
        if (getActivity() == null) return;

        boolean isDarkTheme = (requireContext().getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES;

        Window window = getActivity().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false);
            WindowInsetsController insetsController = window.getInsetsController();
            if (insetsController != null) {
                if (isDarkTheme) {
                    insetsController.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
                    window.setStatusBarColor(Color.BLACK);
                } else {
                    insetsController.setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    );
                    window.setStatusBarColor(Color.parseColor("#F0F0F0"));
                }
            }
        } else {
            View decorView = window.getDecorView();
            if (isDarkTheme) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.setStatusBarColor(Color.BLACK);
            } else {
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                );
                window.setStatusBarColor(Color.parseColor("#F0F0F0"));
            }
        }
    }

    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        return file.exists() ? file.length() : 0;
    }

    public static String getFormattedFileSize(long fileSize) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = fileSize;

        while (size > 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format(Locale.getDefault(), "%.2f %s", size, units[unitIndex]);
    }

    public static boolean doesFileExist(String filePath) {
        return new File(filePath).exists();
    }

    public static String getFileExtension(Uri uri, Context context) {
        if (uri == null) return "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (extension != null && !extension.isEmpty()) {
            return extension;
        }
        String mimeType = context.getContentResolver().getType(uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

    public static String getFileNameFromUri(Uri uri, Context context) {
        String fileName = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
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
        return fileName != null ? fileName : "document_" + System.currentTimeMillis();
    }

    public static void copyUriToFile(Uri uri, File destination, Context context) throws IOException {
        Log.d("DEBUG_IMAGE_STORAGE", "=== copyUriToFile CALLED ===");
        Log.d("DEBUG_IMAGE_STORAGE", "Source URI: " + uri);
        Log.d("DEBUG_IMAGE_STORAGE", "Destination file: " + destination.getAbsolutePath());
        Log.d("DEBUG_IMAGE_STORAGE", "Destination directory exists: " + destination.getParentFile().exists());
        Log.d("DEBUG_IMAGE_STORAGE", "Destination file exists before copy: " + destination.exists());
        
        try (InputStream is = context.getContentResolver().openInputStream(uri);
             FileOutputStream fos = new FileOutputStream(destination)) {
            if (is == null) {
                Log.e("DEBUG_IMAGE_STORAGE", "InputStream is null for URI: " + uri);
                throw new IOException("InputStream is null for URI: " + uri);
            }
            Log.d("DEBUG_IMAGE_STORAGE", "InputStream opened successfully");
            
            byte[] buffer = new byte[1024];
            int len;
            long totalBytes = 0;
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                totalBytes += len;
            }
            Log.d("DEBUG_IMAGE_STORAGE", "Copy completed - total bytes copied: " + totalBytes);
            Log.d("DEBUG_IMAGE_STORAGE", "Destination file exists after copy: " + destination.exists());
            Log.d("DEBUG_IMAGE_STORAGE", "Destination file size after copy: " + (destination.exists() ? destination.length() + " bytes" : "file not found"));
        } catch (Exception e) {
            Log.e("DEBUG_IMAGE_STORAGE", "Error in copyUriToFile: " + e.getMessage(), e);
            throw e;
        }
    }

    public static File getExternalStorageDir(String type, String subPath, Context context) {
        Log.d("DEBUG_IMAGE_STORAGE", "=== getExternalStorageDir CALLED ===");
        Log.d("DEBUG_IMAGE_STORAGE", "type: " + type);
        Log.d("DEBUG_IMAGE_STORAGE", "subPath: " + subPath);
        Log.d("DEBUG_IMAGE_STORAGE", "Android version: " + Build.VERSION.SDK_INT);
        
        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File baseDir = context.getExternalFilesDir(type);
            Log.d("DEBUG_IMAGE_STORAGE", "Base directory (Q+): " + (baseDir != null ? baseDir.getAbsolutePath() : "null"));
            dir = new File(baseDir, subPath);
        } else {
            File baseDir = context.getExternalFilesDir(null);
            Log.d("DEBUG_IMAGE_STORAGE", "Base directory (Legacy): " + (baseDir != null ? baseDir.getAbsolutePath() : "null"));
            dir = new File(baseDir, subPath);
        }
        
        Log.d("DEBUG_IMAGE_STORAGE", "Final directory path: " + dir.getAbsolutePath());
        Log.d("DEBUG_IMAGE_STORAGE", "Directory exists before mkdirs: " + dir.exists());
        
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            Log.d("DEBUG_IMAGE_STORAGE", "Directory creation result: " + created);
            Log.d("DEBUG_IMAGE_STORAGE", "Directory exists after mkdirs: " + dir.exists());
        } else {
            Log.d("DEBUG_IMAGE_STORAGE", "Directory already exists, skipping creation");
        }
        
        return dir;
    }

    public static String stringForTime(long timeMs) {
        int totalSeconds = (int) (timeMs / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    /**
     * Get public pictures directory for storing captured images
     */
    private File getPublicPicturesDirectory() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10+ (API 29+), use MediaStore
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        } else {
            // For older versions, use traditional external storage
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        }
    }

    /**
     * Get public videos directory for storing recorded videos
     */
    private File getPublicVideosDirectory() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10+ (API 29+), use MediaStore
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        } else {
            // For older versions, use traditional external storage
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        }
    }

    /**
     * Copy captured image to public directory
     */
    private void copyToPublicDirectory(File sourceFile, File publicDir, String fileName) {
        try {
            File publicFile = new File(publicDir, fileName);
            if (!publicFile.exists()) {
                copyFile(sourceFile, publicFile);
                Log.d(TAG, "Image copied to public directory: " + publicFile.getAbsolutePath());
                
                // Notify media scanner about the new file in public directory
                Uri publicUri = Uri.fromFile(publicFile);
                requireContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, publicUri));
            } else {
                Log.d(TAG, "Image already exists in public directory: " + publicFile.getAbsolutePath());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error copying image to public directory: " + e.getMessage(), e);
        }
    }

    /**
     * Copy recorded video to public directory
     */
    private void copyVideoToPublicDirectory(File sourceFile, File publicDir, String fileName) {
        try {
            File publicFile = new File(publicDir, fileName);
            if (!publicFile.exists()) {
                copyFile(sourceFile, publicFile);
                Log.d(TAG, "Video copied to public directory: " + publicFile.getAbsolutePath());
                
                // Notify media scanner about the new file in public directory
                Uri publicUri = Uri.fromFile(publicFile);
                requireContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, publicUri));
            } else {
                Log.d(TAG, "Video already exists in public directory: " + publicFile.getAbsolutePath());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error copying video to public directory: " + e.getMessage(), e);
        }
    }

    /**
     * Copy file from source to destination
     */
    private void copyFile(File source, File destination) throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }

    //region Media Loading Helper Methods
    private void loadImagesQ(Context context, int page, int pageSize, Set<Uri> uniqueUris, List<MediaItem> mediaItems) {
        Log.d("DEBUG_IMAGE_STORAGE", "=== loadImagesQ CALLED ===");
        Log.d("DEBUG_IMAGE_STORAGE", "Page: " + page + ", PageSize: " + pageSize);
        
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE
        };

        String selection = MediaStore.Images.Media.SIZE + " > 0"; // Filter out 0-byte files
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC, " + MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        Log.d("DEBUG_IMAGE_STORAGE", "Querying MediaStore.Images.Media.EXTERNAL_CONTENT_URI");

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
        )) {
            if (cursor != null) {
                Log.d("DEBUG_IMAGE_STORAGE", "Cursor returned with " + cursor.getCount() + " rows");
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
                int dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
                int mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA); // May not be available for Q+

                int processedCount = 0;
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    long dateAdded = cursor.getLong(dateAddedColumn);
                    long dateModified = cursor.getLong(dateModifiedColumn);
                    String mimeType = cursor.getString(mimeTypeColumn);
                    String filePath = (dataColumn != -1) ? cursor.getString(dataColumn) : null; // Get file path if available

                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                    );

                    if (uniqueUris.add(contentUri)) {
                        mediaItems.add(new MediaItem(contentUri, mimeType, filePath, dateAdded, dateModified));
                        processedCount++;
                        if (processedCount <= 5) { // Log first 5 items
                            Log.d("DEBUG_IMAGE_STORAGE", "Added image " + processedCount + ": " + contentUri + " (MIME: " + mimeType + ")");
                        }
                    }
                }
                Log.d("DEBUG_IMAGE_STORAGE", "Processed " + processedCount + " unique images from MediaStore");
            } else {
                Log.w("DEBUG_IMAGE_STORAGE", "Cursor is null - no images found in MediaStore");
            }
        } catch (Exception e) {
            Log.e("DEBUG_IMAGE_STORAGE", "Error loading images (Q+): " + e.getMessage(), e);
        }
    }

    private void loadVideosQ(Context context, int page, int pageSize, Set<Uri> uniqueUris, List<MediaItem> mediaItems) {
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.SIZE
        };

        String selection = MediaStore.Video.Media.SIZE + " > 0"; // Filter out 0-byte files
        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC, " + MediaStore.Video.Media.DATE_MODIFIED + " DESC";

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
        )) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
                int dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED);
                int mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE);
                int dataColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA); // May not be available for Q+

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    long dateAdded = cursor.getLong(dateAddedColumn);
                    long dateModified = cursor.getLong(dateModifiedColumn);
                    String mimeType = cursor.getString(mimeTypeColumn);
                    String filePath = (dataColumn != -1) ? cursor.getString(dataColumn) : null; // Get file path if available

                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            id
                    );

                    if (uniqueUris.add(contentUri)) {
                        mediaItems.add(new MediaItem(contentUri, mimeType, filePath, dateAdded, dateModified));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading videos (Q+): " + e.getMessage(), e);
        }
    }

    private void loadImagesLegacy(Context context, int page, int pageSize, Set<Uri> uniqueUris, List<MediaItem> mediaItems) {
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE
        };

        String selection = MediaStore.Images.Media.SIZE + " > 0"; // Filter out 0-byte files
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC, " + MediaStore.Images.Media.DATE_MODIFIED + " DESC";

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
        )) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
                int dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
                int mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    String filePath = cursor.getString(dataColumn);
                    long dateAdded = cursor.getLong(dateAddedColumn);
                    long dateModified = cursor.getLong(dateModifiedColumn);
                    String mimeType = cursor.getString(mimeTypeColumn);

                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                    );

                    if (uniqueUris.add(contentUri)) {
                        mediaItems.add(new MediaItem(contentUri, mimeType, filePath, dateAdded, dateModified));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading images (Legacy): " + e.getMessage(), e);
        }
    }

    private void loadVideosLegacy(Context context, int page, int pageSize, Set<Uri> uniqueUris, List<MediaItem> mediaItems) {
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.SIZE
        };

        String selection = MediaStore.Video.Media.SIZE + " > 0"; // Filter out 0-byte files
        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC, " + MediaStore.Video.Media.DATE_MODIFIED + " DESC";

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
        )) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
                int dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED);
                int mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE);

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    String filePath = cursor.getString(dataColumn);
                    long dateAdded = cursor.getLong(dateAddedColumn);
                    long dateModified = cursor.getLong(dateModifiedColumn);
                    String mimeType = cursor.getString(mimeTypeColumn);

                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            id
                    );

                    if (uniqueUris.add(contentUri)) {
                        mediaItems.add(new MediaItem(contentUri, mimeType, filePath, dateAdded, dateModified));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading videos (Legacy): " + e.getMessage(), e);
        }
    }
    //endregion

    /**
     * Handle sending multiple images with caption
     */
    public void handleMultipleImageSend(ArrayList<Uri> imageUris, String captionText) {
        Log.d("HandleMultipleImageSend", "Handling send for " + imageUris.size() + " images with caption: '" + captionText + "'");
        Log.d("PENDING_MESSAGES", "=== handleMultipleImageSend CALLED (SINGLE CAPTION) ===");
        
        if (imageUris == null || imageUris.isEmpty()) {
            Log.e("HandleMultipleImageSend", "No images to send");
            return;
        }

        SelectionBunchData data = prepareSelectionBunchData(imageUris);
        if (data.selectionBunch.isEmpty()) {
            Log.e("HandleMultipleImageSend", "Selection bunch is empty, aborting");
            return;
        }

        List<String> uniformCaptions = new ArrayList<>();
        for (int i = 0; i < data.selectionBunch.size(); i++) {
            uniformCaptions.add(captionText != null ? captionText : "");
        }

        createAndSendSelectionBunchMessage(data, uniformCaptions);
    }

    /**
     * Handle sending multiple images with individual captions
     */
    public void handleMultipleImageSend(ArrayList<Uri> imageUris, List<String> individualCaptions) {
        Log.d("HandleMultipleImageSend", "Handling send for " + imageUris.size() + " images with individual captions");
        Log.d("PENDING_MESSAGES", "=== handleMultipleImageSend CALLED (INDIVIDUAL CAPTIONS) ===");
        
        if (imageUris == null || imageUris.isEmpty()) {
            Log.e("HandleMultipleImageSend", "No images to send");
            return;
        }

        SelectionBunchData data = prepareSelectionBunchData(imageUris);
        if (data.selectionBunch.isEmpty()) {
            Log.e("HandleMultipleImageSend", "Selection bunch is empty, aborting");
            return;
        }

        List<String> normalizedCaptions = new ArrayList<>();
        for (int i = 0; i < data.selectionBunch.size(); i++) {
            int originalIndex = data.originalIndexes.get(i);
            String caption = "";
            if (individualCaptions != null && originalIndex < individualCaptions.size()) {
                String originalCaption = individualCaptions.get(originalIndex);
                caption = originalCaption != null ? originalCaption : "";
            }
            normalizedCaptions.add(caption);
        }

        createAndSendSelectionBunchMessage(data, normalizedCaptions);
    }

    /**
     * Create compressed image file
     */
    private File createCompressedImageFile(Uri imageUri, String extension) {
        Log.d("DEBUG_IMAGE_STORAGE", "=== createCompressedImageFile CALLED ===");
        Log.d("DEBUG_IMAGE_STORAGE", "Image URI: " + imageUri);
        Log.d("DEBUG_IMAGE_STORAGE", "Extension: " + extension);
        
        try {
            InputStream imageStream = requireContext().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            
            // Get original filename
            String fileName = getOriginalFileName(imageUri);
            if (fileName == null || fileName.isEmpty()) {
                fileName = "image_" + System.currentTimeMillis() + "." + extension;
            }
            Log.d("DEBUG_IMAGE_STORAGE", "Generated filename: " + fileName);
            
            // Create compressed file in cache directory
            File compressedFile = new File(requireContext().getCacheDir() + "/" + fileName);
            Log.d("DEBUG_IMAGE_STORAGE", "Cache file path: " + compressedFile.getAbsolutePath());
            
            // Compress image
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos);
            byte[] compressedData = bos.toByteArray();
            Log.d("DEBUG_IMAGE_STORAGE", "Compressed data size: " + compressedData.length + " bytes");
            
            FileOutputStream fos = new FileOutputStream(compressedFile);
            fos.write(compressedData);
            fos.flush();
            fos.close();
            
            // ALSO save a copy to external storage directory
            File externalDir = getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Images", requireContext());
            File externalFile = new File(externalDir, fileName);
            Log.d("DEBUG_IMAGE_STORAGE", "External file path: " + externalFile.getAbsolutePath());
            
            if (!externalFile.exists()) {
                Log.d("DEBUG_IMAGE_STORAGE", "Copying compressed image to external storage...");
                copyUriToFile(imageUri, externalFile, requireContext());
                Log.d("DEBUG_IMAGE_STORAGE", "External file created: " + externalFile.exists());
                Log.d("DEBUG_IMAGE_STORAGE", "External file size: " + (externalFile.exists() ? externalFile.length() + " bytes" : "file not found"));
            } else {
                Log.d("DEBUG_IMAGE_STORAGE", "External file already exists, skipping copy");
            }
            
            return compressedFile;
        } catch (Exception e) {
            Log.e("DEBUG_IMAGE_STORAGE", "Error creating compressed image file: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Create full-size image file
     */
    private File createFullSizeImageFile(Uri imageUri, String extension) {
        Log.d("DEBUG_IMAGE_STORAGE", "=== createFullSizeImageFile CALLED ===");
        Log.d("DEBUG_IMAGE_STORAGE", "Image URI: " + imageUri);
        Log.d("DEBUG_IMAGE_STORAGE", "Extension: " + extension);
        
        try {
            InputStream imageStream = requireContext().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            
            // Get original filename
            String fileName = getOriginalFileName(imageUri);
            if (fileName == null || fileName.isEmpty()) {
                fileName = "full_image_" + System.currentTimeMillis() + "." + extension;
            } else {
                fileName = "full_" + fileName;
            }
            Log.d("DEBUG_IMAGE_STORAGE", "Generated full size filename: " + fileName);
            
            // Create full size file in cache directory
            File fullSizeFile = new File(requireContext().getCacheDir() + "/" + fileName);
            Log.d("DEBUG_IMAGE_STORAGE", "Cache full size file path: " + fullSizeFile.getAbsolutePath());
            
            // Compress image with higher quality
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            byte[] fullSizeData = bos.toByteArray();
            Log.d("DEBUG_IMAGE_STORAGE", "Full size data size: " + fullSizeData.length + " bytes");
            
            FileOutputStream fos = new FileOutputStream(fullSizeFile);
            fos.write(fullSizeData);
            fos.flush();
            fos.close();
            
            // ALSO save a copy to external storage directory
            File externalDir = getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Images", requireContext());
            File externalFile = new File(externalDir, fileName);
            Log.d("DEBUG_IMAGE_STORAGE", "External full size file path: " + externalFile.getAbsolutePath());
            
            if (!externalFile.exists()) {
                Log.d("DEBUG_IMAGE_STORAGE", "Copying full size image to external storage...");
                copyUriToFile(imageUri, externalFile, requireContext());
                Log.d("DEBUG_IMAGE_STORAGE", "External full size file created: " + externalFile.exists());
                Log.d("DEBUG_IMAGE_STORAGE", "External full size file size: " + (externalFile.exists() ? externalFile.length() + " bytes" : "file not found"));
            } else {
                Log.d("DEBUG_IMAGE_STORAGE", "External full size file already exists, skipping copy");
            }
            
            return fullSizeFile;
        } catch (Exception e) {
            Log.e("DEBUG_IMAGE_STORAGE", "Error creating full size image file: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get original filename from URI
     */
    private String getOriginalFileName(Uri uri) {
        String fileName = null;
        if (uri != null) {
            Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    fileName = cursor.getString(index);
                }
                cursor.close();
            }
        }
        return fileName != null ? fileName : "image_" + System.currentTimeMillis() + ".jpg";
    }

    /**
     * Create and send image message
     */
    private void createAndSendImageMessage(File compressedFile, File fullSizeFile, Uri imageUri, 
                                         String modelId, String captionText, String imageWidthDp, 
                                         String imageHeightDp, String aspectRatio, boolean isLastImage) {
        try {
            // Get current time
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);

            // Get sender info
            Constant.getSfFuncion(requireContext());
            final String receiverUid = requireActivity().getIntent().getStringExtra("friendUidKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String senderRoom = senderId + receiverUid;
            final String receiverRoom = receiverUid + senderId;

            // Create message model
            String uniqDate = Constant.getCurrentDate();
            messageModel model;
            
            if (uniqueDates.add(uniqDate)) {
                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                emojiModels.add(new emojiModel("", ""));
                model = new messageModel(senderId, "", currentDateTimeString, compressedFile.toString(), 
                                      Constant.img, "", "", "", "", "", 
                                      Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", 
                                      modelId, receiverUid, "", "", "", compressedFile.getName(), "", "", 
                                      captionText, 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), 
                                      imageWidthDp, imageHeightDp, aspectRatio, "1");
            } else {
                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                emojiModels.add(new emojiModel("", ""));
                model = new messageModel(senderId, "", currentDateTimeString, compressedFile.toString(), 
                                      Constant.img, "", "", "", "", "", 
                                      Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", 
                                      modelId, receiverUid, "", "", "", compressedFile.getName(), "", "", 
                                      captionText, 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(), 
                                      imageWidthDp, imageHeightDp, aspectRatio, "1");
            }

            // Add to message list
            messageList.add(model);

            // Update UI
            requireActivity().runOnUiThread(() -> {
                otherFunctions.updateMessageList(new ArrayList<>(messageList), chatAdapter);
                senderReceiverDownload.setLastItemVisible(isLastItemVisible, messageList, chatAdapter);
                messageRecView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            });

            // Upload image
            UploadChatHelper uploadHelper = new UploadChatHelper(requireContext(), compressedFile, fullSizeFile, senderId, userFTokenKey);
            uploadHelper.uploadContent(
                    Constant.img, imageUri, captionText, modelId, null, null, 
                    compressedFile.getName(), null, null, null, null, 
                    getFileExtension(imageUri), receiverUid, model.getReplyCrtPostion(), 
                    model.getReplyKey(), model.getReplyOldData(), model.getReplyType(),
                    model.getReplytextData(), model.getDataType(), model.getFileName(), 
                    model.getForwaredKey(), imageWidthDp, imageHeightDp, aspectRatio);

            Log.d("CreateAndSendImageMessage", "Image message created and uploaded successfully");
            
            // Hide the fragment after sending the last image
            if (isLastImage) {
                Log.d("DEBUG_CAPTION", "Last image sent, hiding CameraGalleryFragment");
                requireActivity().runOnUiThread(() -> {
                    // Dismiss the preview dialog first
                    if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                        Constant.dialogLayoutFullScreen.dismiss();
                    }
                    // Hide the fragment
                    closeWithAnimation();
                });
            }

        } catch (Exception e) {
            Log.e("CreateAndSendImageMessage", "Error: " + e.getMessage(), e);
        }
    }

    /**
     * Handle sending multiple videos with individual captions
     */
    public void handleMultipleVideoSend(ArrayList<Uri> videoUris, List<String> individualCaptions) {
        // Check if fragment is still attached
        if (!isAdded() || getContext() == null || getActivity() == null) {
            Log.e(TAG, "Fragment not attached, cannot handle multiple video send");
            return;
        }

        Log.d("HandleMultipleVideoSend", "=== STARTING MULTIPLE VIDEO SEND ===");
        Log.d("HandleMultipleVideoSend", "Handling send for " + videoUris.size() + " videos with individual captions");
        Log.d("HandleMultipleVideoSend", "Captions: " + individualCaptions);
        
        if (videoUris == null || videoUris.isEmpty()) {
            Log.e("HandleMultipleVideoSend", "No videos to send");
            return;
        }
        
        // Send each video individually with its caption
        for (int i = 0; i < videoUris.size(); i++) {
            Uri videoUri = videoUris.get(i);
            String caption = (i < individualCaptions.size()) ? individualCaptions.get(i) : "";
            
            Log.d("HandleMultipleVideoSend", "=== PROCESSING VIDEO " + (i + 1) + "/" + videoUris.size() + " ===");
            Log.d("HandleMultipleVideoSend", "Video URI: " + videoUri);
            Log.d("HandleMultipleVideoSend", "Caption: '" + caption + "'");
            
            // Process each video individually
            handleVideoSelectionWithCaption(videoUri, caption);
        }
        
        // Auto-hide fragment after sending all videos
        Log.d("HandleMultipleVideoSend", "All videos sent, hiding CameraGalleryFragment");
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                // Dismiss the preview dialog first
                if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                    Constant.dialogLayoutFullScreen.dismiss();
                }
                // Hide the fragment
                closeWithAnimation();
            });
        }
    }

    /**
     * Handle video selection with specific caption
     */
    private void handleVideoSelectionWithCaption(Uri uri, String caption) {
        // Check if fragment is still attached
        if (!isAdded() || getContext() == null || getActivity() == null) {
            Log.e(TAG, "Fragment not attached, cannot handle video selection with caption");
            return;
        }

        Log.d("HandleVideoSelectionWithCaption", "=== STARTING VIDEO PROCESSING ===");
        Log.d("HandleVideoSelectionWithCaption", "URI: " + uri);
        Log.d("HandleVideoSelectionWithCaption", "Caption: '" + caption + "'");

        // Generate new modelId for the current media being sent
        modelId = database.getReference().push().getKey();
        globalUri = uri; // Set globalUri for the currently selected/captured media

        Log.d("HandleVideoSelectionWithCaption", "Generated modelId: " + modelId);

        try {
            String fileName = getFileNameFromUri(uri, getContext());
            Log.d("HandleVideoSelectionWithCaption", "Original fileName from URI: " + fileName);
            if (fileName == null) {
                Log.e("HandleVideoSelectionWithCaption", "Unable to get file name");
                showToast("Unable to get file name");
                return;
            }

            // Copy to cache dir for processing
            globalFile = new File(getContext().getCacheDir(), fileName);
            copyUriToFile(uri, globalFile, getContext());

            // Create thumbnail
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(globalFile.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
            File savedThumbnail = FileUtils.saveBitmapToFile(getContext().getApplicationContext(), thumbnail, "thumbnail.png");
            String fileThumbName = fileName + ".png";
            Log.d(TAG, "processVideo - fileThumbName: " + fileThumbName);

            // Save thumbnail to external storage
            File thumbDir = getExternalStorageDir(Environment.DIRECTORY_PICTURES, "Enclosure/Media/Thumbnail", getContext());
            File thumbnailFile = new File(thumbDir, fileThumbName);
            if (!thumbnailFile.exists() && savedThumbnail != null) {
                copyUriToFile(Uri.fromFile(savedThumbnail), thumbnailFile, getContext());
            }

            // Save video to external storage
            File videoDir = getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Videos", getContext());
            File videoFile = new File(videoDir, fileName);
            if (!videoFile.exists()) {
                copyUriToFile(uri, videoFile, getContext());
            }

            // Send video with caption
            sendVideoMessageWithCaption(videoFile, thumbnailFile, fileThumbName, fileName, caption);

        } catch (Exception e) {
            Log.e(TAG, "Error processing video: " + e.getMessage(), e);
            showToast("Error processing video: " + e.getMessage());
        }
    }

    /**
     * Send video message with specific caption
     */
    private void sendVideoMessageWithCaption(File videoFile, File thumbnailFile, String fileThumbName, String fileName, String captionText) {
        Log.d("PENDING_MESSAGES", "=== sendVideoMessageWithCaption CALLED ===");
        // Check if fragment is still attached
        if (!isAdded() || getContext() == null || getActivity() == null) {
            Log.e(TAG, "Fragment not attached, cannot send video message with caption");
            return;
        }

        Log.d("SendVideoMessageWithCaption", "=== STARTING VIDEO SEND ===");
        Log.d("SendVideoMessageWithCaption", "fileName: " + fileName);
        Log.d("SendVideoMessageWithCaption", "caption: " + captionText);
        Log.d("SendVideoMessageWithCaption", "videoFile exists: " + (videoFile != null && videoFile.exists()));
        Log.d("SendVideoMessageWithCaption", "thumbnailFile exists: " + (thumbnailFile != null && thumbnailFile.exists()));
        
        String[] dimensions = calculateVideoDimensions(getContext(), videoFile, Uri.fromFile(videoFile));
        String imageWidthDp = dimensions[0];
        String imageHeightDp = dimensions[1];
        String aspectRatio = dimensions[2];

        String receiverUid = getActivity().getIntent().getStringExtra("friendUidKey");
        String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        String uniqDate = Constant.getCurrentDate();
        boolean isNewDate = uniqueDates.add(uniqDate);

        ArrayList<emojiModel> emojiModels = new ArrayList<>();
        emojiModels.add(new emojiModel("", ""));

        messageModel model = new messageModel(
                senderId, captionText, currentTime,
                videoFile.toString(), Constant.video,
                "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""),
                "", "", "", "", "", modelId, receiverUid,
                "", "", "", videoFile.getName(), fileThumbName, fileThumbName,
                "", 1, isNewDate ? uniqDate : ":" + uniqDate, emojiModels, "",
                Constant.getCurrentTimestamp(), imageHeightDp, imageWidthDp, aspectRatio, "1"
        );

        messageList.add(model);
        insertMessageToDatabase(model);
        updateChatAdapter();

        UploadChatHelper uploadHelper = new UploadChatHelper(getContext(), videoFile, thumbnailFile, senderId, userFTokenKey);
        uploadHelper.uploadContent(
                Constant.video, Uri.fromFile(videoFile), captionText, modelId, thumbnailFile, fileThumbName, fileName,
                null, null, null, null, getFileExtension(Uri.fromFile(videoFile), getContext()), receiverUid,
                "", "", "", "", model.getReplytextData(), model.getDataType(),
                model.getFileName(), "", imageHeightDp, imageWidthDp, aspectRatio);
        
        Log.d("SendVideoMessageWithCaption", "=== VIDEO SEND COMPLETED ===");
        Log.d("SendVideoMessageWithCaption", "Message added to list and database");
        Log.d("SendVideoMessageWithCaption", "Upload initiated");
        
        // Dismiss dialog first, then close fragment
        dismissVideoPreviewDialog();
    }
    
    /**
     * Dismiss the video preview dialog safely
     */
    private void dismissVideoPreviewDialog() {
        try {
            // Always dismiss the dialog first
            if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                Constant.dialogLayoutFullScreen.dismiss();
            }
            
            // Then close the fragment if it's still attached
            if (isAdded() && getContext() != null) {
                closeWithAnimation();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error closing dialog: " + e.getMessage());
            // Fallback: just dismiss the dialog
            if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                Constant.dialogLayoutFullScreen.dismiss();
            }
        }
    }

    /**
     * Get file extension from URI
     */
    private String getFileExtension(Uri uri) {
        if (uri == null) return "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (extension != null && !extension.isEmpty()) {
            return extension;
        }
        String mimeType = requireContext().getContentResolver().getType(uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

    private SelectionBunchData prepareSelectionBunchData(ArrayList<Uri> selectedUris) {
        SelectionBunchData data = new SelectionBunchData();

        for (int i = 0; i < selectedUris.size(); i++) {
            Uri originalUri = selectedUris.get(i);
            String mimeType = requireContext().getContentResolver().getType(originalUri);
            if (mimeType == null || !mimeType.startsWith("image/")) {
                Log.d("SelectionBunch", "Skipping non-image URI: " + originalUri);
                continue;
            }

            String extension;
            if (originalUri.getScheme() != null && originalUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                extension = mimeTypeMap.getExtensionFromMimeType(mimeType);
            } else {
                extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(originalUri.getPath()))));
            }

            File compressedFile = createCompressedImageFile(originalUri, extension);
            File fullSizeFile = createFullSizeImageFile(originalUri, extension);

            if (compressedFile == null || fullSizeFile == null) {
                Log.e("SelectionBunch", "Failed to prepare files for uri: " + originalUri);
                continue;
            }

            data.compressedFiles.add(compressedFile);
            data.fullSizeFiles.add(fullSizeFile);
            data.uris.add(originalUri);
            data.originalIndexes.add(i);

            String fileName = getOriginalFileName(originalUri);
            if (fileName == null || fileName.isEmpty()) {
                fileName = compressedFile.getName();
            }

            data.selectionBunch.add(new selectionBunchModel("", fileName));
        }

        return data;
    }

    private void createAndSendSelectionBunchMessage(SelectionBunchData data,
                                                    List<String> captions) {
        Log.d("PENDING_MESSAGES", "=== createAndSendSelectionBunchMessage CALLED ===");
        if (data == null || data.selectionBunch.isEmpty()) {
            Log.e("SelectionBunch", "No selection bunch data available to send");
            Log.e("PENDING_MESSAGES", "❌ No selection bunch data available to send");
            return;
        }

        try {
            Log.d("SelectionBunch", "Creating message with " + data.selectionBunch.size() + " images");

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);

            Constant.getSfFuncion(requireContext());
            final String receiverUid = requireActivity().getIntent().getStringExtra("friendUidKey");
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            String modelId = database.getReference().push().getKey();
            String uniqDate = Constant.getCurrentDate();

            ArrayList<emojiModel> emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));

            messageModel model;
            if (uniqueDates.add(uniqDate)) {
                model = new messageModel(senderId, "", currentDateTimeString, "",
                        Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", "", "", "",
                        "", 1, uniqDate, emojiModels, "", Constant.getCurrentTimestamp(),
                        "", "", "", String.valueOf(data.selectionBunch.size()), data.selectionBunch);
            } else {
                model = new messageModel(senderId, "", currentDateTimeString, "",
                        Constant.img, "", "", "", "", "",
                        Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "",
                        modelId, receiverUid, "", "", "", "", "", "",
                        "", 1, ":" + uniqDate, emojiModels, "", Constant.getCurrentTimestamp(),
                        "", "", "", String.valueOf(data.selectionBunch.size()), data.selectionBunch);
            }

            messageList.add(model);
            Log.d("PENDING_MESSAGES", "=== ABOUT TO CALL insertMessageToDatabase (SELECTION BUNCH) ===");
            Log.d("PENDING_MESSAGES", "Model: " + (model != null ? "not null" : "null"));
            insertMessageToDatabase(model);
            requireActivity().runOnUiThread(() -> {
                otherFunctions.updateMessageList(new ArrayList<>(messageList), chatAdapter);
                senderReceiverDownload.setLastItemVisible(isLastItemVisible, messageList, chatAdapter);
                messageRecView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            });

            ArrayList<String> selectionBunchFilePaths = new ArrayList<>();
            for (File fullFile : data.fullSizeFiles) {
                selectionBunchFilePaths.add(fullFile.getAbsolutePath());
            }

            if (!selectionBunchFilePaths.isEmpty()) {
                model.setFileName(data.selectionBunch.get(0).getFileName());
            }

            UploadChatHelper uploadHelper = new UploadChatHelper(requireContext(),
                    data.compressedFiles.get(0), data.fullSizeFiles.get(0), senderId, userFTokenKey, model);
            uploadHelper.setSelectionBunchFilePaths(selectionBunchFilePaths);

            String[] primaryDimensions = Constant.calculateImageDimensions(requireContext(), data.compressedFiles.get(0), data.uris.get(0));
            String primaryCaption = captions != null && !captions.isEmpty() ? captions.get(0) : "";

            uploadHelper.uploadContent(
                    Constant.img,
                    data.uris.get(0),
                    primaryCaption,
                    modelId,
                    null,
                    null,
                    data.compressedFiles.get(0).getName(),
                    null,
                    null,
                    null,
                    null,
                    getFileExtension(data.uris.get(0)),
                    receiverUid,
                    model.getReplyCrtPostion(),
                    model.getReplyKey(),
                    model.getReplyOldData(),
                    model.getReplyType(),
                    model.getReplytextData(),
                    model.getDataType(),
                    model.getFileName(),
                    model.getForwaredKey(),
                    primaryDimensions[0],
                    primaryDimensions[1],
                    primaryDimensions[2]
            );



            closeWithAnimation();

        } catch (Exception e) {
            Log.e("SelectionBunch", "Error creating selectionBunch message: " + e.getMessage(), e);
        }
    }

    private static class SelectionBunchData {
        ArrayList<Uri> uris = new ArrayList<>();
        ArrayList<selectionBunchModel> selectionBunch = new ArrayList<>();
        ArrayList<File> compressedFiles = new ArrayList<>();
        ArrayList<File> fullSizeFiles = new ArrayList<>();
        ArrayList<Integer> originalIndexes = new ArrayList<>();
    }

    /**
     * Calculate video dimensions properly
     */
    private String[] calculateVideoDimensions(Context context, File videoFile, Uri videoUri) {
        Log.d(TAG, "calculateVideoDimensions - Starting video dimension calculation");
        Log.d(TAG, "videoFile: " + (videoFile != null ? videoFile.getAbsolutePath() : "null"));
        Log.d(TAG, "videoUri: " + videoUri);
        
        try {
            // Use MediaMetadataRetriever to get video dimensions
            android.media.MediaMetadataRetriever retriever = new android.media.MediaMetadataRetriever();
            
            if (videoFile != null && videoFile.exists()) {
                Log.d(TAG, "Using videoFile for dimensions");
                retriever.setDataSource(videoFile.getAbsolutePath());
            } else if (videoUri != null) {
                Log.d(TAG, "Using videoUri for dimensions");
                retriever.setDataSource(context, videoUri);
            } else {
                Log.e(TAG, "Both videoFile and videoUri are null!");
                return new String[]{"300.00", "300.00", "1.00"};
            }
            
            // Get video dimensions
            String widthStr = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String heightStr = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            
            retriever.release();
            
            if (widthStr == null || heightStr == null) {
                Log.e(TAG, "Could not extract video dimensions");
                return new String[]{"300.00", "300.00", "1.00"};
            }
            
            int width = Integer.parseInt(widthStr);
            int height = Integer.parseInt(heightStr);
            
            Log.d(TAG, "Raw video dimensions - width: " + width + ", height: " + height);
            
            if (width <= 0 || height <= 0) {
                Log.e(TAG, "Invalid video dimensions detected! width=" + width + ", height=" + height);
                return new String[]{"300.00", "300.00", "1.00"};
            }
            
            // Convert to dp
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float density = displayMetrics.density;
            Log.d(TAG, "Device density: " + density);
            
            float imageWidthDp = width / density;
            float imageHeightDp = height / density;
            float aspectRatio = (float) width / height;
            
            String widthStrDp = String.format("%.2f", imageWidthDp);
            String heightStrDp = String.format("%.2f", imageHeightDp);
            String aspectRatioStr = String.format("%.2f", aspectRatio);
            
            Log.d(TAG, "Final calculated video dimensions:");
            Log.d(TAG, "  Width: " + widthStrDp + "dp");
            Log.d(TAG, "  Height: " + heightStrDp + "dp");
            Log.d(TAG, "  AspectRatio: " + aspectRatioStr);
            
            return new String[]{widthStrDp, heightStrDp, aspectRatioStr};
            
        } catch (Exception e) {
            Log.e(TAG, "Error calculating video dimensions: " + e.getMessage(), e);
            return new String[]{"300.00", "300.00", "1.00"};
        }
    }
    
    /**
     * Setup permission text visibility and click handling
     */
    private void setupPermissionText() {
        TextView managePermissionText = getView().findViewById(R.id.managePermissionText);
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
        // Use the GlobalPermissionPopup method to detect limited access
        return com.Appzia.enclosure.Utils.GlobalPermissionPopup.hasLimitedPhotoAccess(getActivity());
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
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    // Set color for the clickable text using ThemeColorKey
                    String themeColor = com.Appzia.enclosure.Utils.Constant.getSF.getString(com.Appzia.enclosure.Utils.Constant.ThemeColorKey, "#00A3E9");
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
}
