package com.Appzia.enclosure.Screens;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.DefaultTimeBar;
import androidx.media3.ui.TimeBar;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.HorizontalImageAdapter;
import com.Appzia.enclosure.Adapter.MainImagePreviewAdapter;
import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.forwardnameModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.messagemodel2;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelper;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.EdgeToEdgeHelper;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Utils.FileUtils;

import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.databinding.ActivityShareExternalDataScreenBinding;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.crypto.SecretKey;

public class shareExternalDataScreen extends AppCompatActivity {
    ActivityShareExternalDataScreenBinding binding;
    Handler handler = new Handler();

    // LocalStorage path constants to match chattingScreen.java structure
    private static final String ENCLOSURE_MEDIA_BASE = "Enclosure/Media";
    private static final String ENCLOSURE_MEDIA_IMAGES = ENCLOSURE_MEDIA_BASE + "/Images";
    private static final String ENCLOSURE_MEDIA_VIDEOS = ENCLOSURE_MEDIA_BASE + "/Videos";
    private static final String ENCLOSURE_MEDIA_DOCUMENTS = ENCLOSURE_MEDIA_BASE + "/Documents";
    private static final String ENCLOSURE_MEDIA_THUMBNAILS = ENCLOSURE_MEDIA_BASE + "/Thumbnail";
    private static final String ENCLOSURE_MEDIA_CONTACTS = ENCLOSURE_MEDIA_BASE + "/Contacts";
    private static final String ENCLOSURE_MEDIA_TEXT = ENCLOSURE_MEDIA_BASE + "/Text";
    private static final String ENCLOSURE_MEDIA_AUDIOS = ENCLOSURE_MEDIA_BASE + "/Audios";

    // मल्टी-इमेज सपोर्ट साठी नवीन व्हेरिएबल्स (Multi-image support variables)
    private ArrayList<Uri> selectedImageUris = new ArrayList<>();
    private String currentCaption = ""; // Single caption for all media items
    private java.util.HashMap<Integer, String> imageCaptions = new java.util.HashMap<>();
    private boolean isUpdatingText = false;
    
    // Video selection variables
    private ArrayList<Uri> selectedVideoUris = new ArrayList<>();
    private java.util.HashMap<Integer, String> videoCaptions = new java.util.HashMap<>();
    private ArrayList<File> selectedVideoFiles = new ArrayList<>();
    private boolean isUpdatingVideoText = false;

    /**
     * Get external storage directory with proper Android version handling
     * Matches the structure used in chattingScreen.java
     */
    private File getExternalStorageDir(String type, String subPath) {
        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dir = new File(getExternalFilesDir(type), subPath);
        } else {
            dir = new File(getExternalFilesDir(null), subPath);
        }
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    /**
     * Get localStorage path for Images - matches chattingScreen.java structure
     */
    private File getImagesStoragePath() {
        return getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_IMAGES);
    }

    /**
     * Get localStorage path for Videos - matches chattingScreen.java structure
     */
    private File getVideosStoragePath() {
        return getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_VIDEOS);
    }

    /**
     * Get localStorage path for Documents - matches chattingScreen.java structure
     */
    private File getDocumentsStoragePath() {
        return getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_DOCUMENTS);
    }

    /**
     * Get localStorage path for Thumbnails - matches chattingScreen.java structure
     */
    private File getThumbnailsStoragePath() {
        return getExternalStorageDir(Environment.DIRECTORY_PICTURES, ENCLOSURE_MEDIA_THUMBNAILS);
    }

    /**
     * Get localStorage path for Contacts - matches chattingScreen.java structure
     */
    private File getContactsStoragePath() {
        return getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_CONTACTS);
    }

    /**
     * Get localStorage path for Text files - matches chattingScreen.java structure
     */
    private File getTextStoragePath() {
        return getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_TEXT);
    }

    /**
     * Get localStorage path for Audio files - matches chattingScreen.java structure
     */
    private File getAudiosStoragePath() {
        return getExternalStorageDir(Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_AUDIOS);
    }

    /**
     * Initialize all storage directories to ensure they exist
     */
    private void initializeAllStorageDirectories() {
        Log.d("shareExternalDataScreen", "Initializing all storage directories");

        // Ensure all storage directories exist
        getImagesStoragePath();
        getVideosStoragePath();
        getDocumentsStoragePath();
        getThumbnailsStoragePath();
        getContactsStoragePath();
        getTextStoragePath();
        getAudiosStoragePath();

        Log.d("shareExternalDataScreen", "All storage directories initialized");
    }

    /**
     * Reset file references and clean up old files to prevent duplicate image sending
     */
    private void resetFileReferences() {
        Log.d("shareExternalDataScreen", "Resetting file references");

        // Clean up old files if they exist
        if (globalFile != null && globalFile.exists()) {
            try {
                if (globalFile.delete()) {
                    Log.d("shareExternalDataScreen", "Deleted old globalFile: " + globalFile.getAbsolutePath());
                }
            } catch (Exception e) {
                Log.e("shareExternalDataScreen", "Error deleting old globalFile: " + e.getMessage());
            }
        }

        if (FullImageFile != null && FullImageFile.exists()) {
            try {
                if (FullImageFile.delete()) {
                    Log.d("shareExternalDataScreen", "Deleted old FullImageFile: " + FullImageFile.getAbsolutePath());
                }
            } catch (Exception e) {
                Log.e("shareExternalDataScreen", "Error deleting old FullImageFile: " + e.getMessage());
            }
        }

        // Reset file references
        globalFile = null;
        FullImageFile = null;

        Log.d("shareExternalDataScreen", "File references reset successfully");
    }

    /**
     * Clear current image display and prepare for new image selection
     */
    private void clearCurrentImageDisplay() {
        Log.d("shareExternalDataScreen", "Clearing current image display");

        // Clear the image container
        if (binding.imageContainer != null) {
            binding.imageContainer.setImageDrawable(null);
        }

        // Reset file references
        resetFileReferences();

        Log.d("shareExternalDataScreen", "Current image display cleared");
    }

    private CardView customToastCard;
    private TextView customToastText;
    Runnable updateProgressAction;
    private int resizeModeIndex = 0; // 0: FIT, 1: FILL, 2: ZOOM
    private boolean wasPlaying = false;
    Context mContext;
    public static Uri GlobalUri;
    ExoPlayer player;
    private File globalFile = null;
    private int lastProgress = 0;
    private SecretKey key;
    private File FullImageFile = null;
    private Uri currentImageUri = null; // Track current image URI to detect new selections
    private StorageReference reference = FirebaseStorage.getInstance().getReference("chats_storage");
    String path;
    AppCompatActivity mActivity;
    public static String docName;
    String themColor;
    Handler fakeProgressHandler = new Handler(Looper.getMainLooper());
    Runnable fakeProgressRunnable;
    boolean isFakeProgressRunning = false;
    ColorStateList tintList;
    public static FirebaseDatabase database;
    public static int notification = 0;
    public static ArrayList<forwardnameModel> receivedNameList = new ArrayList<>();
    StorageTask mStoragetask;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    String sharedTextKey;

    Dialog globalDialoue;



    @Override
    public void onStop() {
        super.onStop();
        // Revert the changes when the fragment is not visible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT >= 35) {
                // Show system bars when the fragment stops
                getWindow().getInsetsController().show(WindowInsets.Type.systemBars());
            }
        }

        if (player != null) {
            player.stop();
            player.release();
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        globalDialoue = Constant.dialogLayoutFullScreen;
        //TODO : for only network loader Themes

        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {

                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));

                } else if (themColor.equals("#00A3E9")) {

                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));

                } else if (themColor.equals("#7adf2a")) {


                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));


                } else if (themColor.equals("#ec0001")) {


                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));

                } else if (themColor.equals("#16f3ff")) {


                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));

                } else if (themColor.equals("#FF8A00")) {


                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));

                } else if (themColor.equals("#7F7F7F")) {


                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));

                } else if (themColor.equals("#D9B845")) {


                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));
                } else if (themColor.equals("#346667")) {


                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));

                } else if (themColor.equals("#9846D9")) {


                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));

                } else if (themColor.equals("#A81010")) {


                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));

                } else {


                    binding.sendGrp.setBackgroundTintList(tintList);
                    CircularProgressIndicator CircularProgressIndicator = globalDialoue.findViewById(R.id.progressbar);
                    CircularProgressIndicator.setIndicatorColor(Color.parseColor(themColor));
                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Status bar configuration is now handled by EdgeToEdgeHelper
        // No need for manual status bar handling as it's deprecated
        globalDialoue = Constant.dialogLayoutFullScreen;
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareExternalDataScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup edge-to-edge display
        EdgeToEdgeHelper.setupEdgeToEdge(this, binding.getRoot());

        mContext = binding.getRoot().getContext();

        // Initialize storage directories to match chattingScreen.java structure
        initializeAllStorageDirectories();

        // Clear UI elements to ensure clean state
        clearAllUIElements();

        customToastCard = findViewById(R.id.includedToast);
        customToastText = customToastCard.findViewById(R.id.toastText);


//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                if (Build.VERSION.SDK_INT >= 35) {
//                    // Hide system bars after 1 second
//                    getWindow().getInsetsController().hide(WindowInsets.Type.systemBars());
//
//                    // Set system bars behavior after 1 second
//                    getWindow().getInsetsController().setSystemBarsBehavior(
//                            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//                    );
//                }
//            }
//        }, 1000);


        Constant.dialogueFullScreen(mContext, R.layout.progress_dialogue);
        globalDialoue = Constant.dialogLayoutFullScreen;
        Intent receivedIntent = getIntent();
        receivedNameList = receivedIntent.getParcelableArrayListExtra("forwardNameList");
        String type = receivedIntent.getStringExtra("typeKey");
        String sharedTextKey = receivedIntent.getStringExtra("sharedTextKey");

        if (sharedTextKey != null) {

        } else {
            // Multi-image sharing check करा (Check for multi-image sharing)
            ArrayList<Uri> receivedImageUris = receivedIntent.getParcelableArrayListExtra("selectedImageUris");
            if (receivedImageUris != null && !receivedImageUris.isEmpty()) {
                // Multi-image sharing - सर्व images store करा (Store all images)
                selectedImageUris.clear();
                selectedImageUris.addAll(receivedImageUris);
                
                // Captions receive करा (Receive captions)
                imageCaptions = (java.util.HashMap<Integer, String>) receivedIntent.getSerializableExtra("imageCaptions");
                if (imageCaptions == null) {
                    imageCaptions = new java.util.HashMap<>();
                    for (int i = 0; i < selectedImageUris.size(); i++) {
                        imageCaptions.put(i, "");
                    }
                }
                
                // पहिल्या image चा URI वापरा (Use first image URI)
                GlobalUri = receivedImageUris.get(0);
                Log.d("MultiImageReceive", "Received " + receivedImageUris.size() + " images, using first one: " + GlobalUri);
            } else {
                // Multi-video sharing check करा (Check for multi-video sharing)
                ArrayList<Uri> receivedVideoUris = receivedIntent.getParcelableArrayListExtra("selectedVideoUris");
                Log.d("MultiVideoReceive", "receivedVideoUris: " + (receivedVideoUris != null ? receivedVideoUris.size() : "null"));
                if (receivedVideoUris != null && !receivedVideoUris.isEmpty()) {
                    // Multi-video sharing - सर्व videos store करा (Store all videos)
                    selectedVideoUris.clear();
                    selectedVideoUris.addAll(receivedVideoUris);
                    Log.d("MultiVideoReceive", "selectedVideoUris populated with " + selectedVideoUris.size() + " videos");
                    
                    // Video captions receive करा (Receive video captions)
                    videoCaptions = (java.util.HashMap<Integer, String>) receivedIntent.getSerializableExtra("videoCaptions");
                    if (videoCaptions == null) {
                        videoCaptions = new java.util.HashMap<>();
                        for (int i = 0; i < selectedVideoUris.size(); i++) {
                            videoCaptions.put(i, "");
                        }
                    }
                    
                    // Process video files
                    processVideoFiles();
                    
                    // पहिल्या video चा URI वापरा (Use first video URI)
                    GlobalUri = receivedVideoUris.get(0);
                    Log.d("MultiVideoReceive", "Received " + receivedVideoUris.size() + " videos, using first one: " + GlobalUri);
                } else {
                // Single item sharing - URI_EXTRA check करा (Check URI_EXTRA for single item)
                String uriString = receivedIntent.getStringExtra("URI_EXTRA");
                if (uriString != null && !uriString.isEmpty()) {
                    GlobalUri = Uri.parse(uriString);
                } else {
                    Log.e("UriError", "No valid URI found - uriString: " + uriString + ", receivedImageUris: " + (receivedImageUris != null ? receivedImageUris.size() : "null"));
                    Toast.makeText(this, "No valid data to share", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
        }

        database = FirebaseDatabase.getInstance();
        mActivity = shareExternalDataScreen.this;

        for (forwardnameModel model : receivedNameList) {
            Log.d("model", "onCreate: " + model.getFriend_id());
        }


        // Debug logs जोडा (Add debug logs)
        Log.d("TypeDebug", "type: " + type + ", selectedImageUris.size(): " + selectedImageUris.size() + ", GlobalUri: " + GlobalUri);
        
        if (type.equals("IMAGE") || (type != null && type.startsWith("image/"))) {
            binding.editLyt.setVisibility(View.VISIBLE);
            binding.imageContainer.setVisibility(View.VISIBLE);
            binding.videoContaner.setVisibility(View.GONE);
            binding.contactContainer.setVisibility(View.GONE);
            binding.documentContainer.setVisibility(View.GONE);
            binding.textContainer.setVisibility(View.GONE);

            // Image preview will be shown when user clicks send button
            // This ensures the flow: select image → contact page → preview → send
            if (GlobalUri != null) {
                Log.d("SingleImage", "Setting up single image preview");
                binding.imageContainer.setImageURI(GlobalUri);
            }


        } else if (type.equals("VIDEO") || (type != null && type.startsWith("video/"))) {
            binding.editLyt.setVisibility(View.VISIBLE);
            binding.imageContainer.setVisibility(View.GONE);
            binding.videoContaner.setVisibility(View.VISIBLE);
            binding.contactContainer.setVisibility(View.GONE);
            binding.documentContainer.setVisibility(View.GONE);
            binding.textContainer.setVisibility(View.GONE);

            // Video preview will be shown when user clicks send button
            // This ensures the flow: select video → contact page → preview → send
            binding.arrowback.setVisibility(View.GONE);

            if (GlobalUri != null) {


                DefaultTrackSelector trackSelector = new DefaultTrackSelector(mContext);
                TrackSelectionParameters trackSelectionParameters = trackSelector.getParameters().
                        buildUpon().setForceLowestBitrate(true).build();
                trackSelector.setParameters((DefaultTrackSelector.Parameters) trackSelectionParameters);

                player = new ExoPlayer.Builder(mContext).setTrackSelector(trackSelector).build();
                MediaItem mediaItem = MediaItem.fromUri(GlobalUri);
                player.setMediaItem(mediaItem);
                player.prepare();
                player.setPlayWhenReady(false);

                binding.videoContaner.setPlayer(player);

                ImageView play = binding.videoContaner.findViewById(R.id.play);
                ImageView pause = binding.videoContaner.findViewById(R.id.pause);
                ImageView reply = binding.videoContaner.findViewById(R.id.reply);
                ImageView forward = binding.videoContaner.findViewById(R.id.forward);
                LinearLayout rsizeLyt = binding.videoContaner.findViewById(R.id.rsizeLyt);
                LinearLayout lyt = binding.videoContaner.findViewById(R.id.lyt);
                TextView resizeText = binding.videoContaner.findViewById(R.id.resizeText);
                ImageView resizeImg = binding.videoContaner.findViewById(R.id.resizeImg);
                SeekBar brightness = binding.videoContaner.findViewById(R.id.brightness);
                SeekBar volume = binding.videoContaner.findViewById(R.id.volume);
                TextView nameTitle = binding.videoContaner.findViewById(R.id.nameTitle);
                TextView totalTime = binding.videoContaner.findViewById(R.id.totalTime);
                TextView startTime = binding.videoContaner.findViewById(R.id.startTime);
                LinearLayout backarrow = binding.videoContaner.findViewById(R.id.backarrow);
                DefaultTimeBar exoProgress = binding.videoContaner.findViewById(R.id.exoProgress);

                String fileName = getFileName(mContext, GlobalUri);
                nameTitle.setText(fileName);

                backarrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

//
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lyt.getLayoutParams();
//                params.topMargin = 52; // pixels
//                lyt.setLayoutParams(params);


                // Define this as a field or inside your activity/fragment


                rsizeLyt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (resizeModeIndex) {
                            case 0:
                                binding.videoContaner.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                                resizeText.setText("Fit Mode");
                                break;
                            case 1:
                                binding.videoContaner.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                                resizeText.setText("Fill Mode");
                                break;
                            case 2:
                                binding.videoContaner.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                                resizeText.setText("Zoom Mode");
                                break;
                        }

                        resizeModeIndex = (resizeModeIndex + 1) % 3; // Cycle through 0 → 1 → 2 → 0 ...
                    }
                });




                forward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player.seekTo(player.getCurrentPosition() + 10000);
                    }
                });


                reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long num = player.getCurrentPosition() - 10000;
                        if (num < 0) {
                            player.seekTo(0);
                        } else {
                            player.seekTo(player.getCurrentPosition() - 10000);
                        }
                    }
                });

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player.play();
                        pause.setVisibility(View.VISIBLE);
                        play.setVisibility(View.GONE);
                    }
                });

                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player.pause();
                        pause.setVisibility(View.GONE);
                        play.setVisibility(View.VISIBLE);
                    }
                });

                exoProgress.addListener(new TimeBar.OnScrubListener() {
                    @Override
                    public void onScrubStart(TimeBar timeBar, long position) {
                        // Track current playback state
                        wasPlaying = player.getPlayWhenReady();

                        // Pause the video while dragging
                        player.setPlayWhenReady(false);
                    }

                    @Override
                    public void onScrubMove(TimeBar timeBar, long position) {
                        // Optional: update UI while scrubbing
                        startTime.setText(stringForTime(position));
                    }

                    @Override
                    public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                        // Seek to the selected position
                        player.seekTo(position);

                        // Only resume playback if it was playing before
                        if (wasPlaying) {
                            player.setPlayWhenReady(false);
                        }
                    }
                });


                player.addListener(new Player.Listener() {
                    @Override
                    public void onPlaybackStateChanged(int state) {
                        if (state == Player.STATE_READY) {
                            long duration = player.getDuration();
                            totalTime.setText(stringForTime(duration));
                        } else if (state == Player.STATE_ENDED) {
                            // Reset video to beginning
                            player.seekTo(0);
                            player.setPlayWhenReady(false); // or true to auto-play again

                            // Reset UI to 0
                            startTime.setText(stringForTime(0));
                            exoProgress.setPosition(0);
                            exoProgress.setDuration(player.getDuration()); // optional, just to be accurate

                            // Update play/pause button state
                            play.setVisibility(View.VISIBLE);
                            pause.setVisibility(View.GONE);
                        }
                    }


                    @Override
                    public void onIsPlayingChanged(boolean isPlaying) {
                        if (isPlaying) {
                            updateProgressAction = new Runnable() {
                                @Override
                                public void run() {
                                    if (player != null && player.isPlaying()) {
                                        long currentPos = player.getCurrentPosition();
                                        long duration = player.getDuration();

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
                            handler.removeCallbacks(updateProgressAction);
                        }
                    }
                });
            }


        } else if (type.equals("CONTACT")) {
            binding.editLyt.setVisibility(View.VISIBLE);

            binding.imageContainer.setVisibility(View.GONE);
            binding.videoContaner.setVisibility(View.GONE);
            binding.contactContainer.setVisibility(View.VISIBLE);
            binding.documentContainer.setVisibility(View.GONE);
            binding.textContainer.setVisibility(View.GONE);

            if (GlobalUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(GlobalUri);
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String line;
                    String name = "";
                    String phoneNumber = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        // Assuming FN (Formatted Name) and TEL (Telephone) properties
                        if (line.startsWith("FN:")) {
                            // Extract name
                            name = line.substring(3).trim(); // Removing "FN:" prefix
                        } else if (line.startsWith("TEL;")) {
                            // Extract phone number
                            phoneNumber = line.substring(line.indexOf(":") + 1).trim(); // Removing "TEL:" prefix

                            // Remove unwanted symbols from phone number
                            phoneNumber = phoneNumber.replaceAll("[^0-9]", ""); // Keep only digits
                        }

                        // Assuming vCard ends with "END:VCARD"
                        if (line.equals("END:VCARD")) {
                            // Process extracted data
                            Log.d("ContactData", "Name: " + name);
                            binding.cName.setText(name);
                            String firstLetter = "";
                            if (!name.isEmpty()) {
                                firstLetter = String.valueOf(name.charAt(0));
                                binding.firstText.setText(firstLetter);
                            }

                            Log.d("ContactData", "Phone Number: " + phoneNumber);
                            binding.cPhone.setText(phoneNumber);

                            // Reset variables for the next vCard (if any)
                            name = "";
                            phoneNumber = "";
                        }
                    }

                    bufferedReader.close();
                    inputStreamReader.close();
                    inputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } else if (type.equals("DOCUMENT")) {
            binding.editLyt.setVisibility(View.VISIBLE);
            binding.imageContainer.setVisibility(View.GONE);
            binding.videoContaner.setVisibility(View.GONE);
            binding.contactContainer.setVisibility(View.GONE);
            binding.documentContainer.setVisibility(View.VISIBLE);
            binding.textContainer.setVisibility(View.GONE);

            if (GlobalUri != null) {

                String fileName = null;
                long fileSize = 0;

                // Retrieve file name and size
                Cursor cursor = getContentResolver().query(GlobalUri, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                        fileName = cursor.getString(displayNameIndex);
                        fileSize = cursor.getLong(sizeIndex);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                // Convert file size to KB or MB
                String fileSizeStr;
                if (fileSize > 1024 * 1024) {
                    // Convert to MB
                    fileSizeStr = String.format("%.2f MB", (float) fileSize / (1024 * 1024));
                } else {
                    // Convert to KB
                    fileSizeStr = String.format("%.2f KB", (float) fileSize / 1024);
                }

                // Now you have the file name and size
                Log.d("File Info", "File Name: " + fileName);
                binding.docName.setText(fileName);
                binding.size.setText(fileSizeStr);
            }


        } else if (type.equals("TEXT")) {
            binding.editLyt.setVisibility(View.INVISIBLE);
            binding.imageContainer.setVisibility(View.GONE);
            binding.videoContaner.setVisibility(View.GONE);
            binding.contactContainer.setVisibility(View.GONE);
            binding.documentContainer.setVisibility(View.GONE);
            binding.textContainer.setVisibility(View.VISIBLE);

            if (URLUtil.isValidUrl(sharedTextKey)) {
                binding.textLinkData.setText("Link");
                binding.textIcon.setImageResource(R.drawable.link_fav);
            } else {
                binding.textLinkData.setText("Text");
                binding.textIcon.setImageResource(R.drawable.textformat);
            }

            binding.textName.setText(" " + sharedTextKey + " ");


        } else {
            binding.editLyt.setVisibility(View.VISIBLE);
            binding.imageContainer.setVisibility(View.GONE);
            binding.videoContaner.setVisibility(View.GONE);
            binding.contactContainer.setVisibility(View.GONE);
            binding.documentContainer.setVisibility(View.VISIBLE);
            binding.textContainer.setVisibility(View.GONE);

            if (GlobalUri != null) {

                String fileName = null;
                long fileSize = 0;

                // Retrieve file name and size
                Cursor cursor = getContentResolver().query(GlobalUri, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                        fileName = cursor.getString(displayNameIndex);
                        fileSize = cursor.getLong(sizeIndex);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                // Convert file size to KB or MB
                String fileSizeStr;
                if (fileSize > 1024 * 1024) {
                    // Convert to MB
                    fileSizeStr = String.format("%.2f MB", (float) fileSize / (1024 * 1024));
                } else {
                    // Convert to KB
                    fileSizeStr = String.format("%.2f KB", (float) fileSize / 1024);
                }

                // Now you have the file name and size
                Log.d("File Info", "File Name: " + fileName);
                binding.docName.setText(fileName);
                binding.size.setText(fileSizeStr);
            }
        }
        // Handle other types if needed


        binding.sendGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SendButtonClick", "=== SEND BUTTON CLICKED ===");
                // globalDialoue.show();

                // Clear all UI elements to prepare for new data selection
                clearAllUIElements();

                String type = receivedIntent.getStringExtra("typeKey");
                Log.d("SendButtonClick", "Type from intent: " + type);
                assert type != null;

                // Refresh UI for the new data type
                refreshUIForDataType(type);
                Log.d("SendButtonClick", "After refreshUIForDataType, selectedVideoUris.size(): " + selectedVideoUris.size());
                if (type.equals("IMAGE")) {
                    // Check if this is multi-image sharing
                    Log.d("SendButton", "IMAGE type detected, selectedImageUris.size(): " + selectedImageUris.size());
                    if (!selectedImageUris.isEmpty()) {
                        Log.d("SendButton", "Multi-image sharing detected, showing preview");
                        setupMultiImagePreview();
                        return; // Exit early to show preview
                    }
                    
                    // Check if this is a new image selection
                    if (currentImageUri == null || !currentImageUri.equals(GlobalUri)) {
                        // New image selected - reset file references
                        resetFileReferences();
                        currentImageUri = GlobalUri;
                        Log.d("shareExternalDataScreen", "New image selected, resetting file references");
                    }

                    // Show image container and set the new image
                    binding.imageContainer.setVisibility(View.VISIBLE);
                    binding.imageContainer.setImageURI(GlobalUri);

                    File f2External = getImagesStoragePath();
                    String exactPath2 = f2External.getAbsolutePath();
                    binding.editLyt.setVisibility(View.VISIBLE);
                    try {
                        //for uploading document to mysql
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
                            Cursor cursor = mContext.getContentResolver().query(GlobalUri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                if (index != -1) {
                                    fileName = cursor.getString(index);
                                }
                                cursor.close();
                            }
                        }
                        Log.d("TAG", "actualName1: " + fileName);
                        // Use proper storage path instead of cache directory
                        File imagesDir = getImagesStoragePath();
                        if (!imagesDir.exists()) {
                            imagesDir.mkdirs();
                        }
                        f = new File(imagesDir, "compressed_" + fileName);

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
                            Cursor cursor = mContext.getContentResolver().query(GlobalUri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                if (index != -1) {
                                    fileName2 = cursor.getString(index);
                                }
                                cursor.close();
                            }
                        }
                        Log.d("TAG", "actualName2: " + fileName2);

                        // Use proper storage path directly instead of relying on exactPath2
                        imagesDir = getImagesStoragePath();
                        if (!imagesDir.exists()) {
                            imagesDir.mkdirs();
                        }
                        f2 = new File(imagesDir, "full_" + fileName2);

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




                        int listcount = receivedNameList.size();
                        Constant.getSfFuncion(getApplicationContext());
                        final String receiverUid = getIntent().getStringExtra("friendUidKey");
                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                        final String senderRoom = senderId + receiverUid;
                        final String receiverRoom = receiverUid + senderId;
                        Log.d("senderRoom", senderRoom + receiverRoom);
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        String currentDateTimeString = sdf.format(d);


                        String modelId = database.getReference().push().getKey();
                        assert modelId != null;
                        String[] dimensions = Constant.calculateImageDimensions(mContext, globalFile, GlobalUri);
                        String imageWidthDp = dimensions[0];
                        String imageHeightDp = dimensions[1];
                        String aspectRatio = dimensions[2];

                        // todo main logic here
                        ArrayList<messagemodel2> messageModels = new ArrayList<>();
                        Log.d("shareExternalDataScreen", "Created messageModels list. receivedNameList size: " + receivedNameList.size());

                        if (binding.messageBoxMy.getText().toString().trim().equals("")) {
                            messageModel model;
                            for (int i = 0; i < receivedNameList.size(); i++) {
                                forwardnameModel model1 = receivedNameList.get(i);
                                String senderRoom2 = senderId + model1.getFriend_id();
                                String device_type = model1.getDevice_type();
                                String receiverRoom2 = model1.getFriend_id() + senderId;

                                try {
                                    ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                    emojiModels.add(new emojiModel("", ""));
                                    model = new messageModel(senderId, "", currentDateTimeString, "", Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", globalFile.getName(), "", "", "", 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),imageWidthDp,imageHeightDp,aspectRatio, "1");
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
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
                                        0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                        model.getSelectionCount()
                                );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

                              /*  try {
                                    new DatabaseHelper(getApplicationContext()).insertMessage(model2);
                                    Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
                                } catch (Exception e) {
                                    Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
                                }*/


                                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());
                                uploadHelper.uploadContent(
                                        Constant.img,
                                        GlobalUri, // uri
                                        model.getCaption(), // captionText
                                        modelId, // modelId
                                        null, // savedThumbnail
                                        null, // fileThumbName
                                        globalFile.getName(), // fileName
                                        null, // contactName
                                        null, // contactPhone
                                        null, // audioTime
                                        null, // audioName
                                        getFileExtension(GlobalUri), // extension
                                        model1.getFriend_id(), model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(),// receiverUid
                                        model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(),
                                        imageWidthDp,imageHeightDp,aspectRatio);

                                // Add the model to the list for sharing
                                messageModels.add(model2);
                                Log.d("shareExternalDataScreen", "Added model to list. Current size: " + messageModels.size());

                                if (i == receivedNameList.size() - 1) {

                                    if (listcount == 1) {
                                        Intent intent = new Intent(mContext, chattingScreen.class);
                                        intent.putExtra("nameKey", model1.getName());
                                        intent.putExtra("captionKey", "");
                                        intent.putExtra("photoKey", "");
                                        intent.putExtra("friendUidKey", model1.getFriend_id());
                                        intent.putExtra("msgLmtKey", "");
                                        intent.putExtra("ecKey", "ecKey");
                                        intent.putExtra("userFTokenKey", model1.getF_token());
                                        intent.putExtra("deviceType", "");
                                        intent.putExtra("fromInviteKey", "fromInviteKey");
                                        intent.putExtra("fromShareExternalData", true);
                                        intent.putExtra("sharedMessageModels", messageModels);
                                        intent.putExtra("messageType", "IMAGE");

                                        Log.d("shareExternalDataScreen", "Launching chattingScreen with extras:");
                                        Log.d("shareExternalDataScreen", "fromShareExternalData: " + intent.getBooleanExtra("fromShareExternalData", false));
                                        Log.d("shareExternalDataScreen", "sharedMessageModels size: " + (messageModels != null ? messageModels.size() : "null"));
                                        Log.d("shareExternalDataScreen", "messageType: " + intent.getStringExtra("messageType"));

                                        // Test toast to see if we reach this point
                                        //      Toast.makeText(mContext, "About to launch chattingScreen with " + messageModels.size() + " models", Toast.LENGTH_LONG).show();

                                        mContext.startActivity(intent);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });


                                    } else {
                                        Intent intent = new Intent(mContext, MainActivityOld.class);
                                        startActivity(intent);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });

                                    }

                                }


                            }

                        } else {
                            messageModel model;
                            for (int i = 0; i < receivedNameList.size(); i++) {
                                forwardnameModel model1 = receivedNameList.get(i);
                                String senderRoom2 = senderId + model1.getFriend_id();
                                String device_type = model1.getDevice_type();
                                String receiverRoom2 = model1.getFriend_id() + senderId;

                                try {
                                    ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                    emojiModels.add(new emojiModel("", ""));
                                    model = new messageModel(senderId, "", currentDateTimeString, "", Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", globalFile.getName(), "", "", binding.messageBoxMy.getText().toString().trim(), 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),imageWidthDp,imageHeightDp,aspectRatio, "1");
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
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
                                        0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                        model.getSelectionCount()
                                );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//                                try {
//                                    new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                    Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                } catch (Exception e) {
//                                    Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                }


                                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());
                                uploadHelper.uploadContent(
                                        Constant.img,
                                        GlobalUri, // uri
                                        binding.messageBoxMy.getText().toString().trim(), // captionText
                                        modelId, // modelId
                                        null, // savedThumbnail
                                        null, // fileThumbName
                                        globalFile.getName(), // fileName
                                        null, // contactName
                                        null, // contactPhone
                                        null, // audioTime
                                        null, // audioName
                                        getFileExtension(GlobalUri), // extension
                                        model1.getFriend_id(), model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(),// receiverUid
                                        model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey()
                                        ,                                        imageWidthDp,imageHeightDp,aspectRatio);

                                // Add the model to the list for sharing
                                messageModels.add(model2);
                                Log.d("shareExternalDataScreen", "Added model to list (else block). Current size: " + messageModels.size());

                                if (i == receivedNameList.size() - 1) {

                                    if (listcount == 1) {
                                        Intent intent = new Intent(mContext, chattingScreen.class);
                                        intent.putExtra("nameKey", model1.getName());
                                        intent.putExtra("captionKey", "");
                                        intent.putExtra("photoKey", "");
                                        intent.putExtra("friendUidKey", model1.getFriend_id());
                                        intent.putExtra("msgLmtKey", "");
                                        intent.putExtra("ecKey", "ecKey");
                                        intent.putExtra("userFTokenKey", model1.getF_token());
                                        intent.putExtra("deviceType", "");
                                        intent.putExtra("fromInviteKey", "fromInviteKey");
                                        intent.putExtra("fromShareExternalData", true);
                                        intent.putExtra("sharedMessageModels", messageModels);
                                        intent.putExtra("messageType", "IMAGE");

                                        Log.d("shareExternalDataScreen", "Launching chattingScreen with extras (else block):");
                                        Log.d("shareExternalDataScreen", "fromShareExternalData: " + intent.getBooleanExtra("fromShareExternalData", false));
                                        Log.d("shareExternalDataScreen", "sharedMessageModels size: " + (messageModels != null ? messageModels.size() : "null"));
                                        Log.d("shareExternalDataScreen", "messageType: " + intent.getStringExtra("messageType"));

                                        // Test toast to see if we reach this point
                                        Toast.makeText(mContext, "About to launch chattingScreen with " + messageModels.size() + " models (else block)", Toast.LENGTH_LONG).show();

                                        mContext.startActivity(intent);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });


                                    } else {
                                        Intent intent = new Intent(mContext, MainActivityOld.class);
                                        startActivity(intent);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });

                                    }

                                }


                            }

                        }


                    } catch (Exception ignored) {
                    }


                } else if (type.equals("VIDEO") || (type != null && type.startsWith("video/"))) {
                    // Check if this is multi-video sharing
                    Log.d("SendButton", "VIDEO type detected: " + type + ", selectedVideoUris.size(): " + selectedVideoUris.size());
                    if (!selectedVideoUris.isEmpty()) {
                        Log.d("SendButton", "Multi-video sharing detected, showing preview");
                        setupMultiVideoPreview();
                        return; // Exit early to show preview
                    }
                    
                    // Show video container and prepare for video processing
                    binding.videoContaner.setVisibility(View.VISIBLE);
                    binding.editLyt.setVisibility(View.VISIBLE);

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
                            Cursor cursor = mContext.getContentResolver().query(GlobalUri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                if (index != -1) {
                                    fileName = cursor.getString(index);
                                }
                                cursor.close();
                            }
                        }
                        Log.d("TAG", "actualName1: " + fileName);

                        // Use proper storage path instead of cache directory
                        File videosDir = getVideosStoragePath();
                        if (!videosDir.exists()) {
                            videosDir.mkdirs();
                        }
                        f = new File(videosDir, fileName);
                        File savedThumbnail = null;
                        String fileThumbName = null;
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
                            //     Toast.makeText(shareExternalDataScreen.this, "imageFile111", Toast.LENGTH_SHORT).show();
                            globalFile = f;

                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(f.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

                            assert thumbnail != null;
                            savedThumbnail = FileUtils.saveBitmapToFile(getApplicationContext(), thumbnail, "thumbnail.png");
                            fileThumbName = fileName + "." + "png";


                            File f2External = getThumbnailsStoragePath();
                            String exactPath2 = f2External.getAbsolutePath();


                            if (!f2External.exists()) {
                                f2External.mkdirs();
                            }


                        } catch (Exception e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }


                        File f2External = getVideosStoragePath();
                        String exactPath2 = f2External.getAbsolutePath();

                        if (!f2External.exists()) {
                            f2External.mkdirs();
                        }

                        Date currentDate = new Date();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = dateFormat.format(currentDate);
                        System.out.println("Current Date and Time: " + formattedDate);

                        File finalSavedThumbnail = savedThumbnail;
                        String finalFileThumbName = fileThumbName;
                        String finalFileName = fileName;

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

                        String modelId = database.getReference().push().getKey();


                        int listcount = receivedNameList.size();
                        String[] dimensions = Constant.calculateImageDimensions(mContext, finalSavedThumbnail, Uri.fromFile(finalSavedThumbnail));
                        String imageWidthDp = dimensions[0];
                        String imageHeightDp = dimensions[1];
                        String aspectRatio = dimensions[2];

                        if (binding.messageBoxMy.getText().toString().trim().equals("")) {
                            for (int i = 0; i < receivedNameList.size(); i++) {
                                forwardnameModel model1 = receivedNameList.get(i);

                                String senderRoom2 = senderId + model1.getFriend_id();
                                String device_type = model1.getDevice_type();
                                String receiverRoom2 = model1.getFriend_id() + senderId;
                                try {
                                    ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                    emojiModels.add(new emojiModel("", ""));
                                    model = new messageModel(senderId, "", currentDateTimeString, "", Constant.video, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", globalFile.getName(), "", fileThumbName, "", 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),imageWidthDp,imageHeightDp,aspectRatio, "1");
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
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
                                        0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                        model.getSelectionCount()
                                );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//                                try {
//                                    new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                    Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                } catch (Exception e) {
//                                    Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                }

                                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());
                                uploadHelper.uploadContent(
                                        Constant.video, // uploadType
                                        GlobalUri, // uri
                                        "", // captionText
                                        modelId, // modelId
                                        finalSavedThumbnail, // savedThumbnail
                                        finalFileThumbName, // fileThumbName
                                        finalFileName, // fileName
                                        null, // contactName
                                        null, // contactPhone
                                        null, // audioTime
                                        null, // audioName
                                        getFileExtension(GlobalUri), // extension
                                        model1.getFriend_id() // receiverUid
                                        ,"", "", "", "",
                                        model.getReplytextData(), model.getDataType(), model.getFileName(), "", imageWidthDp, imageHeightDp, aspectRatio);

                                if (i == receivedNameList.size() - 1) {

                                    if (listcount == 1) {
                                        Intent intent = new Intent(mContext, chattingScreen.class);
                                        intent.putExtra("nameKey", model1.getName());
                                        intent.putExtra("captionKey", "");
                                        intent.putExtra("photoKey", "");
                                        intent.putExtra("friendUidKey", model1.getFriend_id());
                                        intent.putExtra("msgLmtKey", "");
                                        intent.putExtra("ecKey", "ecKey");
                                        intent.putExtra("userFTokenKey", model1.getF_token());
                                        intent.putExtra("deviceType", "");
                                        intent.putExtra("fromInviteKey", "fromInviteKey");
                                        mContext.startActivity(intent);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });


                                    } else {
                                        Intent intent = new Intent(mContext, MainActivityOld.class);
                                        startActivity(intent);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });

                                    }

                                }
                            }



                        } else {
//                            uploadtoFirebaseVideo(GlobalUri, finalSavedThumbnail, finalFileThumbName, finalFileName, binding.messageBoxMy.getText().toString().trim());
                            for (int i = 0; i < receivedNameList.size(); i++) {
                                forwardnameModel model1 = receivedNameList.get(i);

                                String senderRoom2 = senderId + model1.getFriend_id();
                                String device_type = model1.getDevice_type();
                                String receiverRoom2 = model1.getFriend_id() + senderId;
                                try {
                                    ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                    emojiModels.add(new emojiModel("", ""));
                                    model = new messageModel(senderId, "", currentDateTimeString, "", Constant.video, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", globalFile.getName(), "", fileThumbName, binding.messageBoxMy.getText().toString(), 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),imageWidthDp,imageHeightDp,aspectRatio, "1");
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
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
                                        0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                        model.getSelectionCount()
                                );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//                                try {
//                                    new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                    Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                } catch (Exception e) {
//                                    Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                }

                                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());
                                uploadHelper.uploadContent(
                                        Constant.video, // uploadType
                                        GlobalUri, // uri
                                        model.getCaption(), // captionText
                                        modelId, // modelId
                                        finalSavedThumbnail, // savedThumbnail
                                        finalFileThumbName, // fileThumbName
                                        finalFileName, // fileName
                                        null, // contactName
                                        null, // contactPhone
                                        null, // audioTime
                                        null, // audioName
                                        getFileExtension(GlobalUri), // extension
                                        model1.getFriend_id() // receiverUid
                                        ,"", "", "", "",
                                        model.getReplytextData(), model.getDataType(), model.getFileName(), "", imageWidthDp, imageHeightDp, aspectRatio);

                                if (i == receivedNameList.size() - 1) {

                                    if (listcount == 1) {
                                        Intent intent = new Intent(mContext, chattingScreen.class);
                                        intent.putExtra("nameKey", model1.getName());
                                        intent.putExtra("captionKey", "");
                                        intent.putExtra("photoKey", "");
                                        intent.putExtra("friendUidKey", model1.getFriend_id());
                                        intent.putExtra("msgLmtKey", "");
                                        intent.putExtra("ecKey", "ecKey");
                                        intent.putExtra("userFTokenKey", model1.getF_token());
                                        intent.putExtra("deviceType", "");
                                        intent.putExtra("fromInviteKey", "fromInviteKey");
                                        mContext.startActivity(intent);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });


                                    } else {
                                        Intent intent = new Intent(mContext, MainActivityOld.class);
                                        startActivity(intent);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });

                                    }

                                }
                            }

                        }


                    } catch (Exception x) {
                        Toast.makeText(mActivity, x.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else if (type.equals("CONTACT")) {
                    // Show contact container and prepare for contact processing
                    binding.contactContainer.setVisibility(View.VISIBLE);
                    binding.editLyt.setVisibility(View.VISIBLE);
                    String finalName1 = binding.cName.getText().toString();
                    String finalNumber = binding.cPhone.getText().toString();

                    Constant.getSfFuncion(getApplicationContext());
                    final String receiverUid = getIntent().getStringExtra("friendUidKey");
                    final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                    final String senderRoom = senderId + receiverUid;
                    final String receiverRoom = receiverUid + senderId;
                    Log.d("senderRoom", senderRoom + receiverRoom);
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    String currentDateTimeString = sdf.format(d);

                    String modelId = root.push().getKey();

                    assert modelId != null;

                    int listcount = receivedNameList.size();

                    if (binding.messageBoxMy.getText().toString().trim().equals("")) {

                        for (int i = 0; i < receivedNameList.size(); i++) {
                            forwardnameModel model1 = receivedNameList.get(i);

                            String senderRoom2 = senderId + model1.getFriend_id();
                            String receiverRoom2 = model1.getFriend_id() + senderId;
                            String device_type = model1.getDevice_type();
                            messageModel model = null;
                            try {
                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                emojiModels.add(new emojiModel("", ""));
                                model = new messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", finalName1, finalNumber, "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", "", "", "", "", 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),"","","", "1");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
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
                                    0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                    model.getSelectionCount()
                            );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//                            try {
//                                new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                            } catch (Exception e) {
//                                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                            }
                            //this is a final
                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());
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
                                    model1.getFriend_id() // receiverUid
                                    ,"", "", "", "",
                                    "", "","", "", "","","");

                            if (i == receivedNameList.size() - 1) {

                                if (listcount == 1) {
                                    Intent intent = new Intent(mContext, chattingScreen.class);
                                    intent.putExtra("nameKey", model1.getName());
                                    intent.putExtra("captionKey", "");
                                    intent.putExtra("photoKey", "");
                                    intent.putExtra("friendUidKey", model1.getFriend_id());
                                    intent.putExtra("msgLmtKey", "");
                                    intent.putExtra("ecKey", "ecKey");
                                    intent.putExtra("userFTokenKey", model1.getF_token());
                                    intent.putExtra("deviceType", "");
                                    intent.putExtra("fromInviteKey", "fromInviteKey");
                                    mContext.startActivity(intent);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    });


                                } else {
                                    Intent intent = new Intent(mContext, MainActivityOld.class);
                                    startActivity(intent);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    });

                                }

                            }
                        }


                    } else {
                        for (int i = 0; i < receivedNameList.size(); i++) {
                            forwardnameModel model1 = receivedNameList.get(i);

                            String senderRoom2 = senderId + model1.getFriend_id();
                            String receiverRoom2 = model1.getFriend_id() + senderId;
                            String device_type = model1.getDevice_type();
                            messageModel model = null;
                            try {
                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                emojiModels.add(new emojiModel("", ""));
                                model = new messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", finalName1, finalNumber, "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", "", "", "", binding.messageBoxMy.getText().toString(), 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),"","","", "1");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
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
                                    0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                    model.getSelectionCount()
                            );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//                            try {
//                                new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                            } catch (Exception e) {
//                                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                            }

                            //this is a final
                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());
                            uploadHelper.uploadContent(
                                    Constant.contact, // uploadType
                                    null, // uri
                                    model.getCaption(), // captionText
                                    modelId, // modelId
                                    null, // savedThumbnail
                                    null, // fileThumbName
                                    null, // fileName
                                    finalName1, // contactName
                                    finalNumber, // contactPhone
                                    null, // audioTime
                                    null, // audioName
                                    null, // extension
                                    model1.getFriend_id() // receiverUid
                                    ,"", "", "", "",
                                    "", "","", "","","","");

                            if (i == receivedNameList.size() - 1) {

                                if (listcount == 1) {
                                    Intent intent = new Intent(mContext, chattingScreen.class);
                                    intent.putExtra("nameKey", model1.getName());
                                    intent.putExtra("captionKey", "");
                                    intent.putExtra("photoKey", "");
                                    intent.putExtra("friendUidKey", model1.getFriend_id());
                                    intent.putExtra("msgLmtKey", "");
                                    intent.putExtra("ecKey", "ecKey");
                                    intent.putExtra("userFTokenKey", model1.getF_token());
                                    intent.putExtra("deviceType", "");
                                    intent.putExtra("fromInviteKey", "fromInviteKey");
                                    mContext.startActivity(intent);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    });


                                } else {
                                    Intent intent = new Intent(mContext, MainActivityOld.class);
                                    startActivity(intent);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    });

                                }

                            }
                        }
                    }

                }
                else if (type.equals("DOCUMENT")) {
                    // Show document container and prepare for document processing
                    binding.documentContainer.setVisibility(View.VISIBLE);
                    binding.editLyt.setVisibility(View.VISIBLE);


                    if (GlobalUri != null) {
                        Cursor cursor = mContext.getContentResolver().query(GlobalUri, null, null, null, null);
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

                        //  Toast.makeText(shareExternalDataScreen.this, "Upload in process", Toast.LENGTH_SHORT).show();
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


                                // Log.d("extension", extension)
                                // Use proper storage path instead of cache directory
                                File documentsDir = getDocumentsStoragePath();
                                if (!documentsDir.exists()) {
                                    documentsDir.mkdirs();
                                }
                                f = new File(documentsDir, docName);
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


                                    File f2External = getDocumentsStoragePath();
                                    String exactPath2 = f2External.getAbsolutePath();


                                    if (!f2External.exists()) {
                                        f2External.mkdirs();
                                    }

                                    Constant.getSfFuncion(getApplicationContext());
                                    final String receiverUid = getIntent().getStringExtra("friendUidKey");
                                    final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                    final String senderRoom = senderId + receiverUid;
                                    final String receiverRoom = receiverUid + senderId;
                                    Log.d("senderRoom", senderRoom + receiverRoom);
                                    Date d = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                    String currentDateTimeString = sdf.format(d);

                                    String modelId = root.push().getKey();
                                    long finalFileSize = getFileSize(globalFile.getPath());

                                    assert modelId != null;

                                    int listcount = receivedNameList.size();
                                    if (binding.messageBoxMy.getText().toString().trim().equals("")) {
                                        for (int i = 0; i < receivedNameList.size(); i++) {
                                            forwardnameModel model1 = receivedNameList.get(i);

                                            String senderRoom2 = senderId + model1.getFriend_id();
                                            String receiverRoom2 = model1.getFriend_id() + senderId;
                                            String device_type = model1.getDevice_type();
                                            messageModel model = null;
                                            try {
                                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                                emojiModels.add(new emojiModel("", ""));
                                                model = new messageModel(senderId, "", currentDateTimeString, "", docName, extension, "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", String.valueOf(getFormattedFileSize(finalFileSize)), globalFile.getName(), "", "", "", 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),"","","", "1");
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
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
                                                    0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                                    model.getSelectionCount()
                                            );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//                                            try {
//                                                new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                            } catch (Exception e) {
//                                                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                            }


                                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());

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
                                                    model1.getFriend_id(),"", "", "", "",
                                                    "", "","", "","","","");

                                            if (i == receivedNameList.size() - 1) {

                                                if (listcount == 1) {
                                                    Intent intent = new Intent(mContext, chattingScreen.class);
                                                    intent.putExtra("nameKey", model1.getName());
                                                    intent.putExtra("captionKey", "");
                                                    intent.putExtra("photoKey", "");
                                                    intent.putExtra("friendUidKey", model1.getFriend_id());
                                                    intent.putExtra("msgLmtKey", "");
                                                    intent.putExtra("ecKey", "ecKey");
                                                    intent.putExtra("userFTokenKey", model1.getF_token());
                                                    intent.putExtra("deviceType", "");
                                                    intent.putExtra("fromInviteKey", "fromInviteKey");
                                                    mContext.startActivity(intent);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    });


                                                } else {
                                                    Intent intent = new Intent(mContext, MainActivityOld.class);
                                                    startActivity(intent);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    });

                                                }

                                            }

                                        }

                                    }
                                    else {
                                        for (int i = 0; i < receivedNameList.size(); i++) {
                                            forwardnameModel model1 = receivedNameList.get(i);

                                            String senderRoom2 = senderId + model1.getFriend_id();
                                            String receiverRoom2 = model1.getFriend_id() + senderId;
                                            String device_type = model1.getDevice_type();
                                            messageModel model = null;
                                            try {
                                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                                emojiModels.add(new emojiModel("", ""));
                                                model = new messageModel(senderId, "", currentDateTimeString, "", docName, extension, "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", String.valueOf(getFormattedFileSize(finalFileSize)), globalFile.getName(), "", "", binding.messageBoxMy.getText().toString(), 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),"","","", "1");
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
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
                                                    0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                                    model.getSelectionCount()
                                            );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//                                            try {
//                                                new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                            } catch (Exception e) {
//                                                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                            }


                                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());

                                            uploadHelper.uploadContent(
                                                    Constant.doc,
                                                    GlobalUri,
                                                    model.getCaption(),
                                                    modelId,
                                                    null,
                                                    null,
                                                    globalName,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    extension,
                                                    model1.getFriend_id(),"", "", "", "",
                                                    "", "","", "","","","");

                                            if (i == receivedNameList.size() - 1) {

                                                if (listcount == 1) {
                                                    Intent intent = new Intent(mContext, chattingScreen.class);
                                                    intent.putExtra("nameKey", model1.getName());
                                                    intent.putExtra("captionKey", "");
                                                    intent.putExtra("photoKey", "");
                                                    intent.putExtra("friendUidKey", model1.getFriend_id());
                                                    intent.putExtra("msgLmtKey", "");
                                                    intent.putExtra("ecKey", "ecKey");
                                                    intent.putExtra("userFTokenKey", model1.getF_token());
                                                    intent.putExtra("deviceType", "");
                                                    intent.putExtra("fromInviteKey", "fromInviteKey");
                                                    mContext.startActivity(intent);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    });


                                                } else {
                                                    Intent intent = new Intent(mContext, MainActivityOld.class);
                                                    startActivity(intent);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    });

                                                }

                                            }

                                        }


                                    }

                                } catch (Exception e) {
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            } catch (Exception x) {
                                Toast.makeText(mActivity, x.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(shareExternalDataScreen.this, "Please select image", Toast.LENGTH_SHORT).show();
                        }

                    }


                } else if (type.equals("TEXT")) {
                    // Show text container and prepare for text processing
                    binding.textContainer.setVisibility(View.VISIBLE);
                    Constant.getSfFuncion(mContext);
                    final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    String currentDateTimeString = sdf.format(d);
                    String modelId = database.getReference().push().getKey();
                    File savedThumbnail = null;
                    int listcount = receivedNameList.size();
                    for (int i = 0; i < receivedNameList.size(); i++) {
                        forwardnameModel model1 = receivedNameList.get(i);

                        String senderRoom2 = senderId + model1.getFriend_id();
                        String receiverRoom2 = model1.getFriend_id() + senderId;
                        String sharedTextKey = receivedIntent.getStringExtra("sharedTextKey");
                        String device_type = model1.getDevice_type();

                        if (binding.messageBoxMy.getText().toString().trim().equals("")) {
                            messageModel model = null;
                            try {
                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                emojiModels.add(new emojiModel("", ""));
                                model = new messageModel(senderId, sharedTextKey, currentDateTimeString, "", Constant.Text, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", "", "", "", "", 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),"","","", "1");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
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
                                    0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                    model.getSelectionCount()
                            );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//                            try {
//                                new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                            } catch (Exception e) {
//                                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                            }

                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());
                            uploadHelper.uploadContent(Constant.Text, null, model.getMessage(), modelId, null, model.getFileNameThumbnail(), null, model.getName(), model.getPhone(), model.getMiceTiming(), null, model.getExtension(), model1.getFriend_id(), model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(), model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey(),"","","");

                        }

                        if (i == receivedNameList.size() - 1) {

                            if (listcount == 1) {
                                Intent intent = new Intent(mContext, chattingScreen.class);
                                intent.putExtra("nameKey", model1.getName());
                                intent.putExtra("captionKey", "");
                                intent.putExtra("photoKey", "");
                                intent.putExtra("friendUidKey", model1.getFriend_id());
                                intent.putExtra("msgLmtKey", "");
                                intent.putExtra("ecKey", "ecKey");
                                intent.putExtra("userFTokenKey", model1.getF_token());
                                intent.putExtra("deviceType", "");
                                intent.putExtra("fromInviteKey", "fromInviteKey");
                                mContext.startActivity(intent);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                });


                            } else {
                                Intent intent = new Intent(mContext, MainActivityOld.class);
                                startActivity(intent);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                });

                            }

                        }


                    }

                }
                else {
                    binding.editLyt.setVisibility(View.VISIBLE);
                    // TODO here also document view
                    if (GlobalUri != null) {
                        Cursor cursor = mContext.getContentResolver().query(GlobalUri, null, null, null, null);
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


                                // Log.d("extension", extension)
                                // Use proper storage path instead of cache directory
                                File documentsDir = getDocumentsStoragePath();
                                if (!documentsDir.exists()) {
                                    documentsDir.mkdirs();
                                }
                                f = new File(documentsDir, docName);
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


                                    File f2External = getDocumentsStoragePath();
                                    String exactPath2 = f2External.getAbsolutePath();


                                    if (!f2External.exists()) {
                                        f2External.mkdirs();
                                    }


                                    Constant.getSfFuncion(getApplicationContext());
                                    final String receiverUid = getIntent().getStringExtra("friendUidKey");
                                    final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
                                    final String senderRoom = senderId + receiverUid;
                                    final String receiverRoom = receiverUid + senderId;
                                    Log.d("senderRoom", senderRoom + receiverRoom);
                                    Date d = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                    String currentDateTimeString = sdf.format(d);

                                    String modelId = root.push().getKey();
                                    long finalFileSize = getFileSize(globalFile.getPath());

                                    assert modelId != null;

                                    int listcount = receivedNameList.size();
                                    if (binding.messageBoxMy.getText().toString().trim().equals("")) {
                                        for (int i = 0; i < receivedNameList.size(); i++) {
                                            forwardnameModel model1 = receivedNameList.get(i);

                                            String senderRoom2 = senderId + model1.getFriend_id();
                                            String receiverRoom2 = model1.getFriend_id() + senderId;
                                            String device_type = model1.getDevice_type();
                                            messageModel model = null;
                                            try {
                                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                                emojiModels.add(new emojiModel("", ""));
                                                model = new messageModel(senderId, "", currentDateTimeString, "", docName, extension, "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", String.valueOf(getFormattedFileSize(finalFileSize)), globalFile.getName(), "", "", "", 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),"","","", "1");
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
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
                                                    0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                                    model.getSelectionCount()
                                            );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//                                            try {
//                                                new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                            } catch (Exception e) {
//                                                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                            }


                                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());

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
                                                    model1.getFriend_id(),"", "", "", "",
                                                    "", "","", "","","","");

                                            if (i == receivedNameList.size() - 1) {

                                                if (listcount == 1) {
                                                    Intent intent = new Intent(mContext, chattingScreen.class);
                                                    intent.putExtra("nameKey", model1.getName());
                                                    intent.putExtra("captionKey", "");
                                                    intent.putExtra("photoKey", "");
                                                    intent.putExtra("friendUidKey", model1.getFriend_id());
                                                    intent.putExtra("msgLmtKey", "");
                                                    intent.putExtra("ecKey", "ecKey");
                                                    intent.putExtra("userFTokenKey", model1.getF_token());
                                                    intent.putExtra("deviceType", "");
                                                    intent.putExtra("fromInviteKey", "fromInviteKey");
                                                    mContext.startActivity(intent);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    });


                                                } else {
                                                    Intent intent = new Intent(mContext, MainActivityOld.class);
                                                    startActivity(intent);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    });

                                                }

                                            }

                                        }

                                    }
                                    else {
                                        for (int i = 0; i < receivedNameList.size(); i++) {
                                            forwardnameModel model1 = receivedNameList.get(i);

                                            String senderRoom2 = senderId + model1.getFriend_id();
                                            String receiverRoom2 = model1.getFriend_id() + senderId;
                                            String device_type = model1.getDevice_type();
                                            messageModel model = null;
                                            try {
                                                ArrayList<emojiModel> emojiModels = new ArrayList<>();
                                                emojiModels.add(new emojiModel("", ""));
                                                model = new messageModel(senderId, "", currentDateTimeString, "", docName, extension, "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", String.valueOf(getFormattedFileSize(finalFileSize)), globalFile.getName(), "", "", binding.messageBoxMy.getText().toString(), 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp(),"","","", "1");
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
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
                                                    0,model.getImageWidth(),model.getImageHeight(),model.getAspectRatio(),
                                                    model.getSelectionCount()
                                            );

//TODO : active : 0 = still loading
//TODO : active : 1 = completed

//                                            try {
//                                                new DatabaseHelper(getApplicationContext()).insertMessage(model2);
//                                                Log.d("DATABASE_HELPER", "Message successfully inserted into SQLite database");
//                                            } catch (Exception e) {
//                                                Log.e("DATABASE_HELPER", "Failed to insert message into SQLite: " + e.getMessage(), e);
//                                            }

                                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());

                                            uploadHelper.uploadContent(
                                                    Constant.doc,
                                                    GlobalUri,
                                                    model.getCaption(),
                                                    modelId,
                                                    null,
                                                    null,
                                                    globalName,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    extension,
                                                    model1.getFriend_id(),"", "", "", "",
                                                    "", "","", "","","","");

                                            if (i == receivedNameList.size() - 1) {

                                                if (listcount == 1) {
                                                    Intent intent = new Intent(mContext, chattingScreen.class);
                                                    intent.putExtra("nameKey", model1.getName());
                                                    intent.putExtra("captionKey", "");
                                                    intent.putExtra("photoKey", "");
                                                    intent.putExtra("friendUidKey", model1.getFriend_id());
                                                    intent.putExtra("msgLmtKey", "");
                                                    intent.putExtra("ecKey", "ecKey");
                                                    intent.putExtra("userFTokenKey", model1.getF_token());
                                                    intent.putExtra("deviceType", "");
                                                    intent.putExtra("fromInviteKey", "fromInviteKey");
                                                    mContext.startActivity(intent);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    });


                                                } else {
                                                    Intent intent = new Intent(mContext, MainActivityOld.class);
                                                    startActivity(intent);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    });

                                                }

                                            }

                                        }


                                    }

                                } catch (Exception e) {
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            } catch (Exception x) {
                                Toast.makeText(mActivity, x.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(shareExternalDataScreen.this, "Please select image", Toast.LENGTH_SHORT).show();
                        }

                    }


                }


            }
        });


        binding.arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


//        binding.nbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    // todo image

//    private void uploadtoFirebase(Uri uri, String captionText) {
//
//
//        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
//
//        long fileSize = getFileSize(FullImageFile.getPath());
//        long fileSize2 = getFileSize(globalFile.getPath());
//
//
//        if (fileSize > 0) {
//            System.out.println("File size: " + getFormattedFileSize(fileSize));
//            System.out.println("File size: " + getFormattedFileSize(fileSize2));
//
//
//            if (fileSize > 200 * 1024) {
//
//                mStoragetask = fileRef.putFile(Uri.fromFile(globalFile)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//
//                                Constant.getSfFuncion(getApplicationContext());
//                                final String receiverUid = getIntent().getStringExtra("friendUidKey");
//                                final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
//                                final String senderRoom = senderId + receiverUid;
//                                final String receiverRoom = receiverUid + senderId;
//                                Log.d("senderRoom", senderRoom + receiverRoom);
//                                Date d = new Date();
//                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//                                String currentDateTimeString = sdf.format(d);
//                                messageModel model = null;
//
//                                String modelId = database.getReference().push().getKey();
//                                assert modelId != null;
//
//                                // here need to send photo name
//                                // Toast.makeText(mActivity, captionText, Toast.LENGTH_SHORT).show();
//                                Log.d("TAG", "actualName: " + globalFile.getName());
//
//
//                                try {
//                                    long fileSize = getFileSize(FullImageFile.getPath());
//                                    long fileSize2 = getFileSize(globalFile.getPath());
//
//
//                                    if (fileSize > 0) {
//                                        System.out.println("File size: " + getFormattedFileSize(fileSize));
//                                        System.out.println("File size: " + getFormattedFileSize(fileSize2));
//                                        File savedThumbnail = null;
//                                        int listcount = receivedNameList.size();
//                                        if (fileSize > 200 * 1024) {
//
//
//                                        } else {
//                                            for (int i = 0; i < receivedNameList.size(); i++) {
//                                                forwardnameModel model1 = receivedNameList.get(i);
//                                                String senderRoom2 = senderId + model1.getFriend_id();
//                                                String receiverRoom2 = model1.getFriend_id() + senderId;
//                                                String device_type = model1.getDevice_type();
//                                                try {
//                                                    ArrayList<emojiModel> emojiModels = new ArrayList<>();
//                                                    emojiModels.add(new emojiModel("", ""));
//                                                    model = new messageModel(senderId, "", currentDateTimeString, uri.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", globalFile.getName(), "", "", captionText, 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp());
//                                                } catch (Exception e) {
//                                                    throw new RuntimeException(e);
//                                                }
//
//                                                //   Webservice.create_individual_chattingSharedContact(mContext, senderId, model1.getFriend_id(), "", FullImageFile, Constant.img, "", "", "", "", "", currentDateTimeString, senderRoom2, receiverRoom2, model, modelId, Constant.getSF.getString(Constant.full_name, ""), database, model1.getF_token(), mActivity, notification, savedThumbnail, globalDialoue, device_type, customToastCard, customToastText, i, receivedNameList, binding.progressbar);
//
//                                                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, globalFile, FullImageFile, senderId, model1.getF_token());
//                                                uploadHelper.uploadContent(
//                                                        Constant.img,
//                                                        GlobalUri, // uri
//                                                        model.getCaption(), // captionText
//                                                        modelId, // modelId
//                                                        null, // savedThumbnail
//                                                        null, // fileThumbName
//                                                        globalFile.getName(), // fileName
//                                                        null, // contactName
//                                                        null, // contactPhone
//                                                        null, // audioTime
//                                                        null, // audioName
//                                                        getFileExtension(GlobalUri), // extension
//                                                        model1.getFriend_id(), model.getReplyCrtPostion(), model.getReplyKey(), model.getReplyOldData(), model.getReplyType(),// receiverUid
//                                                        model.getReplytextData(), model.getDataType(), model.getFileName(), model.getForwaredKey()
//                                                );
//
//                                                if (i == receivedNameList.size() - 1) {
//                                                    if (listcount == 1) {
//                                                        Intent intent = new Intent(mContext, chattingScreen.class);
//                                                        intent.putExtra("nameKey", model1.getName());
//                                                        intent.putExtra("captionKey", "");
//                                                        intent.putExtra("photoKey", "");
//                                                        intent.putExtra("friendUidKey", model1.getFriend_id());
//                                                        intent.putExtra("msgLmtKey", "");
//                                                        intent.putExtra("ecKey", "ecKey");
//                                                        intent.putExtra("userFTokenKey", model1.getF_token());
//                                                        intent.putExtra("deviceType", "");
//                                                        intent.putExtra("fromInviteKey", "fromInviteKey");
//                                                        mContext.startActivity(intent);
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                finish();
//                                                            }
//                                                        });
//
//
//                                                    } else {
//                                                        Intent intent = new Intent(mContext, MainActivityOld.class);
//                                                        startActivity(intent);
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                finish();
//                                                            }
//                                                        });
//
//                                                    }
//
//                                                }
//                                            }
//                                        }
//                                    } else {
//                                        System.out.println("File not found.");
//                                    }
//
//
//                                } catch (Exception ignored) {
//                                }
//
//                            }
//                        });
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(shareExternalDataScreen.this, "new :" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                        Log.d("Permission", e.getMessage());
//                        //   progressBar.setVisibility(View.INVISIBLE);
//                    }
//                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//
//                        int progress = (int) ((100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount());
//                        int displayProgress = Math.min(progress, 90);
//
//                        if (displayProgress > lastProgress) {
//                            lastProgress = displayProgress;
//
//                            updateProgressUI(displayProgress);
//
//                            // Start fake progress once we hit 90%
//                            if (displayProgress >= 90 && !isFakeProgressRunning) {
//                                startFakeProgressTo99();
//                            }
//                        }
//                    }
//                });
//
//
//            } else {
//                mStoragetask = fileRef.putFile(Uri.fromFile(FullImageFile)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//
//                                Constant.getSfFuncion(getApplicationContext());
//                                final String receiverUid = getIntent().getStringExtra("friendUidKey");
//                                final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
//                                final String senderRoom = senderId + receiverUid;
//                                final String receiverRoom = receiverUid + senderId;
//                                Log.d("senderRoom", senderRoom + receiverRoom);
//                                Date d = new Date();
//                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//                                String currentDateTimeString = sdf.format(d);
//                                messageModel model = null;
//
//                                String modelId = database.getReference().push().getKey();
//                                assert modelId != null;
//
//                                //here need to add file name
//
//
//                                try {
//
//                                    long fileSize = getFileSize(FullImageFile.getPath());
//                                    long fileSize2 = getFileSize(globalFile.getPath());
//
//
//                                    if (fileSize > 0) {
//                                        System.out.println("File size: " + getFormattedFileSize(fileSize));
//                                        System.out.println("File size: " + getFormattedFileSize(fileSize2));
//
//                                        File savedThumbnail = null;
//                                        if (fileSize > 200 * 1024) {
//
//                                            for (int i = 0; i < receivedNameList.size(); i++) {
//                                                forwardnameModel model1 = receivedNameList.get(i);
//
//                                                String senderRoom2 = senderId + model1.getFriend_id();
//                                                String receiverRoom2 = model1.getFriend_id() + senderId;
//                                                String device_type = model1.getDevice_type();
//                                                try {
//                                                    ArrayList<emojiModel> emojiModels = new ArrayList<>();
//                                                    emojiModels.add(new emojiModel("", ""));
//                                                    model = new messageModel(senderId, "", currentDateTimeString, uri.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", globalFile.getName(), "", "", captionText, 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp());
//                                                } catch (Exception e) {
//                                                    throw new RuntimeException(e);
//                                                }
//                                                Webservice.create_individual_chattingSharedContact(mContext, senderId, model1.getFriend_id(), "", globalFile, Constant.img, "", "", "", "", "", currentDateTimeString, senderRoom2, receiverRoom2, model, modelId, Constant.getSF.getString(Constant.full_name, ""), database, model1.getF_token(), mActivity, notification, savedThumbnail, globalDialoue, device_type, customToastCard, customToastText, i, receivedNameList, binding.progressbar);
//
//                                            }
//
//
//                                        } else {
//                                            for (int i = 0; i < receivedNameList.size(); i++) {
//                                                forwardnameModel model1 = receivedNameList.get(i);
//                                                String senderRoom2 = senderId + model1.getFriend_id();
//                                                String receiverRoom2 = model1.getFriend_id() + senderId;
//                                                String device_type = model1.getDevice_type();
//                                                try {
//                                                    ArrayList<emojiModel> emojiModels = new ArrayList<>();
//                                                    emojiModels.add(new emojiModel("", ""));
//                                                    model = new messageModel(senderId, "", currentDateTimeString, uri.toString(), Constant.img, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", globalFile.getName(), "", "", captionText, 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp());
//                                                } catch (Exception e) {
//                                                    throw new RuntimeException(e);
//                                                }
//                                                Webservice.create_individual_chattingSharedContact(mContext, senderId, model1.getFriend_id(), "", FullImageFile, Constant.img, "", "", "", "", "", currentDateTimeString, senderRoom2, receiverRoom2, model, modelId, Constant.getSF.getString(Constant.full_name, ""), database, model1.getF_token(), mActivity, notification, savedThumbnail, globalDialoue, device_type, customToastCard, customToastText, i, receivedNameList, binding.progressbar);
//
//                                            }
//                                        }
//                                    } else {
//                                        System.out.println("File not found.");
//                                    }
//
//
//                                } catch (Exception ignored) {
//                                }
//                            }
//                        });
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(shareExternalDataScreen.this, "new :" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                        Log.d("Permission", e.getMessage());
//                        //   progressBar.setVisibility(View.INVISIBLE);
//                    }
//                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//
//                        int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
//                        int displayProgress = Math.min(progress, 90);
//
//                        if (displayProgress > lastProgress) {
//                            lastProgress = displayProgress;
//
//                            updateProgressUI(displayProgress);
//
//                            // Start fake progress once we hit 90%
//                            if (displayProgress >= 90 && !isFakeProgressRunning) {
//                                startFakeProgressTo99();
//                            }
//                        }
//                    }
//                });
//
//            }
//        } else {
//            System.out.println("File not found.");
//        }
//
//    }
//
//
//    // todo videos
//
//    private void uploadtoFirebaseVideo(Uri uri, File savedThumbnail, String fileThumbName, String fileName, String captionText) {
//
//        StorageReference fileRef = reference.child(fileName);
//        StorageReference fileRefThumbnail = reference.child(fileThumbName);
//        mStoragetask = fileRef.putFile(Uri.fromFile(globalFile)).
//                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//
//                                mStoragetask = fileRefThumbnail.putFile(Uri.fromFile(savedThumbnail)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                        fileRefThumbnail.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uriThumbnail) {
//
//                                                Constant.getSfFuncion(getApplicationContext());
//                                                final String receiverUid = getIntent().getStringExtra("friendUidKey");
//                                                final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
//                                                final String senderRoom = senderId + receiverUid;
//                                                final String receiverRoom = receiverUid + senderId;
//                                                Log.d("senderRoom", senderRoom + receiverRoom);
//                                                Date d = new Date();
//                                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//                                                String currentDateTimeString = sdf.format(d);
//                                                messageModel model = null;
//
//                                                String modelId = database.getReference().push().getKey();
//                                                assert modelId != null;
//
//
//                                                try {
//
//
//                                                    long fileSize = getFileSize(globalFile.getPath());
//
//
//                                                    if (fileSize > 0) {
//                                                        System.out.println("Filesizexx: " + getFormattedFileSize(fileSize));
//
//
//                                                        if (fileSize > 200000 * 1024) {
//
//
//                                                            String message = "Keep the video file size under 200 MB.";
//
//                                                            int duration = Snackbar.LENGTH_SHORT;
//                                                            Snackbar snackbar = Snackbar.make(binding.getRoot(), message, duration);
//                                                            View snackbarView = snackbar.getView();
//                                                            int topMarginInDp = 30;
//                                                            float scale = snackbarView.getResources().getDisplayMetrics().density;
//                                                            int topMarginInPx = (int) (topMarginInDp * scale + 0.5f);
//
//// Change gravity to top and add margin
//                                                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
//                                                            params.gravity = Gravity.TOP;
//                                                            params.topMargin = topMarginInPx;
//                                                            snackbarView.setLayoutParams(params);
//                                                            //  binding.downArrow.setVisibility(View.GONE);
//
//                                                        } else {
//
//                                                            for (int i = 0; i < receivedNameList.size(); i++) {
//                                                                forwardnameModel model1 = receivedNameList.get(i);
//
//                                                                String senderRoom2 = senderId + model1.getFriend_id();
//                                                                String device_type = model1.getDevice_type();
//                                                                String receiverRoom2 = model1.getFriend_id() + senderId;
//                                                                try {
//                                                                    ArrayList<emojiModel> emojiModels = new ArrayList<>();
//                                                                    emojiModels.add(new emojiModel("", ""));
//                                                                    model = new messageModel(senderId, "", currentDateTimeString, uri.toString(), Constant.video, "", "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", globalFile.getName(), uriThumbnail.toString(), fileThumbName, captionText, 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp());
//                                                                } catch (Exception e) {
//                                                                    throw new RuntimeException(e);
//                                                                }
//
//                                                                Webservice.create_individual_chattingSharedContact(mContext, senderId, model1.getFriend_id(), "", globalFile, Constant.video, "", "", "", "", "", currentDateTimeString, senderRoom2, receiverRoom2, model, modelId, Constant.getSF.getString(Constant.full_name, ""), database, model1.getF_token(), mActivity, notification, savedThumbnail, globalDialoue, device_type, customToastCard, customToastText, i, receivedNameList, binding.progressbar);
//
//                                                            }
//
//
//                                                        }
//                                                    }
//
//
//                                                } catch (Exception ignored) {
//                                                }
//
//                                            }
//                                        });
//
////
//
//
//                                    }
//                                });
//
//
//                            }
//                        });
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(shareExternalDataScreen.this, "new :" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                        Log.d("Permission", e.getMessage());
//                        //   progressBar.setVisibility(View.INVISIBLE);
//                    }
//                }).
//                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//
//                        int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
//                        int displayProgress = Math.min(progress, 90);
//
//                        if (displayProgress > lastProgress) {
//                            lastProgress = displayProgress;
//
//                            updateProgressUI(displayProgress);
//
//                            // Start fake progress once we hit 90%
//                            if (displayProgress >= 90 && !isFakeProgressRunning) {
//                                startFakeProgressTo99();
//                            }
//                        }
//
//                    }
//                });
//
//
//    }
//
//
//    // todo document
//
//    private void uploadtoFirebaseDoc(Uri uri, String extension, String name, String captionText) {
//
//        StorageReference fileRef = reference.child(globalFile.getName() + "." + getFileExtension(uri));
//
//
//        mStoragetask = fileRef.putFile(Uri.fromFile(globalFile)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//                        Constant.getSfFuncion(getApplicationContext());
//                        final String receiverUid = getIntent().getStringExtra("friendUidKey");
//                        final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
//                        final String senderRoom = senderId + receiverUid;
//                        final String receiverRoom = receiverUid + senderId;
//                        Log.d("senderRoom", senderRoom + receiverRoom);
//                        Date d = new Date();
//                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//                        String currentDateTimeString = sdf.format(d);
//
//                        String modelId = root.push().getKey();
//                        long finalFileSize = getFileSize(globalFile.getPath());
//
//                        assert modelId != null;
//
//
//                        try {
//
//
//                            long fileSize = getFileSize(globalFile.getPath());
//
//
//                            if (fileSize > 0) {
//                                System.out.println("Filesizexx: " + getFormattedFileSize(fileSize));
//
//
//                                if (fileSize > 200000 * 1024) {
//
//                                    String message = "Keep the document file size under 200 MB.";
//
//                                    int duration = Snackbar.LENGTH_SHORT;
//                                    Snackbar snackbar = Snackbar.make(binding.getRoot(), message, duration);
//                                    View snackbarView = snackbar.getView();
//                                    int topMarginInDp = 30;
//                                    float scale = snackbarView.getResources().getDisplayMetrics().density;
//                                    int topMarginInPx = (int) (topMarginInDp * scale + 0.5f);
//
    //// Change gravity to top and add margin
//                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
//                                    params.gravity = Gravity.TOP;
//                                    params.topMargin = topMarginInPx;
//                                    snackbarView.setLayoutParams(params);
//                                    //   binding.downArrow.setVisibility(View.GONE);
//
//                                } else {
//                                    File savedThumbnail = null;
//
//                                    for (int i = 0; i < receivedNameList.size(); i++) {
//                                        forwardnameModel model1 = receivedNameList.get(i);
//
//                                        String senderRoom2 = senderId + model1.getFriend_id();
//                                        String receiverRoom2 = model1.getFriend_id() + senderId;
//                                        String device_type = model1.getDevice_type();
//                                        messageModel model = null;
//                                        try {
//                                            ArrayList<emojiModel> emojiModels = new ArrayList<>();
//                                            emojiModels.add(new emojiModel("", ""));
//                                            model = new messageModel(senderId, "", currentDateTimeString, uri.toString(), docName, extension, "", "", "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", String.valueOf(getFormattedFileSize(finalFileSize)), globalFile.getName(), "", "", captionText, 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp());
//                                        } catch (Exception e) {
//                                            throw new RuntimeException(e);
//                                        }
//
//                                        Webservice.create_individual_chattingSharedContact(mContext, senderId, model1.getFriend_id(), "", globalFile, docName, extension, "", "", "", "", currentDateTimeString, senderRoom2, receiverRoom2, model, modelId, Constant.getSF.getString(Constant.full_name, ""), database, model1.getF_token(), mActivity, notification, savedThumbnail, globalDialoue, device_type, customToastCard, customToastText, i, receivedNameList, binding.progressbar);
//
//                                    }
//
//                                }
//                            } else {
//                                System.out.println("File not found.");
//                            }
//
//
//                        } catch (Exception ignored) {
//                        }
//                    }
//                });
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Constant.dialogueLayoutForAll(mContext, R.layout.nodocumentfounddialogue);
//                TextView view = Constant.dialogLayoutColor.findViewById(R.id.name);
//                AppCompatButton btn = Constant.dialogLayoutColor.findViewById(R.id.ok);
//                TextView open = Constant.dialogLayoutColor.findViewById(R.id.open);
//                TextView unable = Constant.dialogLayoutColor.findViewById(R.id.unable);
//                unable.setVisibility(View.GONE);
//                open.setText("Send file");
//                view.setText("The selected file format is not compatible with the sending mechanism.Please create a .zip file and proceed with resending it.");
//                btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Constant.dialogLayoutColor.dismiss();
//                    }
//                });
//                Constant.dialogLayoutColor.show();
//
//                //  binding.downArrow.setVisibility(View.GONE);
//
//                Log.d("errorDilogueReal", e.getMessage());
//                //   progressBar.setVisibility(View.INVISIBLE);
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//
//                int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
//                int displayProgress = Math.min(progress, 90);
//
//                if (displayProgress > lastProgress) {
//                    lastProgress = displayProgress;
//
//                    updateProgressUI(displayProgress);
//
//                    // Start fake progress once we hit 90%
//                    if (displayProgress >= 90 && !isFakeProgressRunning) {
//                        startFakeProgressTo99();
//                    }
//                }
//            }
//        });
//
//
//    }
//
//    // todo contact
//
//    private void uploadtoFirebaseContact(String name, String phone, String captionText) {
//
//        try {
//            Constant.getSfFuncion(getApplicationContext());
//            final String receiverUid = getIntent().getStringExtra("friendUidKey");
//            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
//            final String senderRoom = senderId + receiverUid;
//            final String receiverRoom = receiverUid + senderId;
//            Log.d("senderRoom", senderRoom + receiverRoom);
//            Date d = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//            String currentDateTimeString = sdf.format(d);
//
//            String modelId = root.push().getKey();
//
//            assert modelId != null;
//
//
//            try {
//                File savedThumbnail = null;
//
//
//                for (int i = 0; i < receivedNameList.size(); i++) {
//                    forwardnameModel model1 = receivedNameList.get(i);
//
//                    String senderRoom2 = senderId + model1.getFriend_id();
//                    String receiverRoom2 = model1.getFriend_id() + senderId;
//                    String device_type = model1.getDevice_type();
//                    messageModel model = null;
//                    try {
//                        ArrayList<emojiModel> emojiModels = new ArrayList<>();
//                        emojiModels.add(new emojiModel("", ""));
//                        model = new messageModel(senderId, "", currentDateTimeString, "", Constant.contact, "", name, phone, "", "", Constant.getSF.getString(Constant.full_name, ""), "", "", "", "", "", modelId, model1.getFriend_id(), "", "", "", "", "", "", "", 1, Constant.getCurrentDate(), emojiModels, "", Constant.getCurrentTimestamp());
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    //this is a final
//                    Webservice.create_individual_chattingSharedContact(mContext, senderId, model1.getFriend_id(), "", globalFile, Constant.contact, "", name, phone, "", "", currentDateTimeString, senderRoom2, receiverRoom2, model, modelId, Constant.getSF.getString(Constant.full_name, ""), database, model1.getF_token(), mActivity, notification, savedThumbnail, globalDialoue, device_type, customToastCard, customToastText, i, receivedNameList, binding.progressbar);
//
//                }
//
//
//            } catch (Exception ignored) {
//            }
//        } catch (Exception ignored) {
//        }
//    }
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

    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }

    public String getFileExtension(Uri mUri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(mUri));

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
        String currentDateAndTime = dateFormat.format(new Date());
        path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, currentDateAndTime, null);
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {
        cleanupFilesOnExit();
        SwipeNavigationHelper.finishWithSwipe(shareExternalDataScreen.this);
    }

    /**
     * Clean up files and UI when exiting the activity
     */
    private void cleanupFilesOnExit() {
        Log.d("shareExternalDataScreen", "Cleaning up files and UI on exit");

        // Clear all UI elements
        clearAllUIElements();

        // Clean up old files if they exist
        if (globalFile != null && globalFile.exists()) {
            try {
                if (globalFile.delete()) {
                    Log.d("shareExternalDataScreen", "Deleted globalFile on exit: " + globalFile.getAbsolutePath());
                }
            } catch (Exception e) {
                Log.e("shareExternalDataScreen", "Error deleting globalFile on exit: " + e.getMessage());
            }
        }

        if (FullImageFile != null && FullImageFile.exists()) {
            try {
                if (FullImageFile.delete()) {
                    Log.d("shareExternalDataScreen", "Deleted FullImageFile on exit: " + FullImageFile.getAbsolutePath());
                }
            } catch (Exception e) {
                Log.e("shareExternalDataScreen", "Error deleting FullImageFile on exit: " + e.getMessage());
            }
        }

        // Reset file references
        globalFile = null;
        FullImageFile = null;
        currentImageUri = null;

        Log.d("shareExternalDataScreen", "Files and UI cleaned up on exit");
    }

    private void updateProgressUI(int progress) {
        Log.d("UPLOAD_PROGRESS", "Upload is " + progress + "% done");

        TextView percentage = globalDialoue.findViewById(R.id.percentage);
        percentage.setText(progress + " %");

        CircularProgressIndicator progressBar = globalDialoue.findViewById(R.id.progressbar);
        if (progressBar != null) {
            progressBar.setProgress(progress);
        } else {
            Log.e("ProgressIndicator", "CircularProgressIndicator is null");
        }
    }

    private void startFakeProgressTo99() {
        isFakeProgressRunning = true;

        fakeProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (lastProgress < 99) {
                    lastProgress++;
                    updateProgressUI(lastProgress);

                    // Increase delay slightly as progress increases
                    int delay = 300 + ((lastProgress - 90) * 50); // delay: 300ms → 750ms
                    fakeProgressHandler.postDelayed(this, delay);
                }
            }
        };

        fakeProgressHandler.postDelayed(fakeProgressRunnable, 300);
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




    public static String getFileName(Context context, Uri uri) {
        String result = null;

        // For content:// Uris
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    result = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        }

        // For file:// Uris
        if (result == null) {
            result = uri.getLastPathSegment();
            if (result != null && result.contains("/")) {
                result = result.substring(result.lastIndexOf("/") + 1);
            }
        }

        return result;
    }

    /**
     * Clear all UI elements to prepare for new data selection
     */
    private void clearAllUIElements() {
        Log.d("shareExternalDataScreen", "Clearing all UI elements for new data selection");

        // Clear image container
        if (binding.imageContainer != null) {
            binding.imageContainer.setImageDrawable(null);
        }

        // Clear video container
        if (binding.videoContaner != null) {
            binding.videoContaner.setPlayer(null);
        }

        // Clear document container
        if (binding.documentContainer != null) {
            binding.docName.setText("");
            binding.size.setText("");
        }

        // Clear contact container
        if (binding.contactContainer != null) {
            // Contact container will be cleared by hiding it
        }

        // Clear text container
        if (binding.textContainer != null) {
            binding.textLinkData.setText("");
            binding.textName.setText("");
        }

        // Clear message box
        if (binding.messageBoxMy != null) {
            binding.messageBoxMy.setText("");
        }

        // Clear contact input fields
        if (binding.cName != null) {
            binding.cName.setText("");
        }
        if (binding.cPhone != null) {
            binding.cPhone.setText("");
        }

        // Hide all containers initially
        binding.imageContainer.setVisibility(View.GONE);
        binding.videoContaner.setVisibility(View.GONE);
        binding.documentContainer.setVisibility(View.GONE);
        binding.contactContainer.setVisibility(View.GONE);
        binding.textContainer.setVisibility(View.GONE);

        Log.d("shareExternalDataScreen", "All UI elements cleared successfully");
    }

    /**
     * Refresh UI based on the selected data type
     */
    private void refreshUIForDataType(String type) {
        Log.d("shareExternalDataScreen", "Refreshing UI for data type: " + type);

        // Hide all containers first
        binding.imageContainer.setVisibility(View.GONE);
        binding.videoContaner.setVisibility(View.GONE);
        binding.documentContainer.setVisibility(View.GONE);
        binding.contactContainer.setVisibility(View.GONE);
        binding.textContainer.setVisibility(View.GONE);

        // Show appropriate container based on type
        switch (type) {
            case "IMAGE":
                binding.imageContainer.setVisibility(View.VISIBLE);
                if (GlobalUri != null) {
                    binding.imageContainer.setImageURI(GlobalUri);
                }
                break;
            case "VIDEO":
                binding.videoContaner.setVisibility(View.VISIBLE);
                // Video player will be set up in the processing logic
                break;
            case "DOCUMENT":
                binding.documentContainer.setVisibility(View.VISIBLE);
                // Document info will be populated in the processing logic
                break;
            case "CONTACT":
                binding.contactContainer.setVisibility(View.VISIBLE);
                // Contact info will be populated in the processing logic
                break;
            case "TEXT":
                binding.textContainer.setVisibility(View.VISIBLE);
                // Text info will be populated in the processing logic
                break;
        }

        Log.d("shareExternalDataScreen", "UI refreshed for data type: " + type);
    }

    /**
     * Clear all data variables to prepare for new data
     */
    private void clearAllDataVariables() {
        Log.d("shareExternalDataScreen", "Clearing all data variables for new data");

        // Clear file references
        globalFile = null;
        FullImageFile = null;
        currentImageUri = null;

        // Clear document name
        docName = null;

        // Clear shared text
        sharedTextKey = null;

        // Clear GlobalUri completely for new data
        GlobalUri = null;

        // Clear the current intent data
        Intent currentIntent = getIntent();
        if (currentIntent != null) {
            currentIntent.removeExtra("URI_EXTRA");
            currentIntent.removeExtra("sharedTextKey");
            currentIntent.removeExtra("typeKey");
        }

        // Clear cache directory
        clearCacheDirectory();

        Log.d("shareExternalDataScreen", "All data variables cleared successfully");
    }

    /**
     * Clear cache directory to remove old temporary files
     */
    private void clearCacheDirectory() {
        Log.d("shareExternalDataScreen", "Clearing cache directory");

        try {
            File cacheDir = getCacheDir();
            if (cacheDir != null && cacheDir.exists()) {
                File[] files = cacheDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            if (file.delete()) {
                                Log.d("shareExternalDataScreen", "Deleted cache file: " + file.getName());
                            } else {
                                Log.w("shareExternalDataScreen", "Failed to delete cache file: " + file.getName());
                            }
                        }
                    }
                }
            }
            Log.d("shareExternalDataScreen", "Cache directory cleared successfully");
        } catch (Exception e) {
            Log.e("shareExternalDataScreen", "Error clearing cache directory: " + e.getMessage());
        }
    }

    /**
     * मल्टी-इमेज प्रिव्यू सेटअप करण्यासाठी पद्धत (Method to setup multi-image preview)
     * chattingScreen.java मधील setupMultiImagePreview() पद्धतीचा वापर करून
     */
    private void setupMultiImagePreview() {
        Log.d("MultiImagePreview", "Setting up preview with " + selectedImageUris.size() + " images");

        if (selectedImageUris.isEmpty()) {
            Log.d("MultiImagePreview", "No images selected, returning");
            return;
        }

        // Duplicate dialog avoid करा (Avoid duplicate dialog)
        if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
            Log.d("MultiImagePreview", "Dialog already showing, dismissing previous one");
            Constant.dialogLayoutFullScreen.dismiss();
        }

        // डायलॉग लेआउट सेटअप करा (Setup dialog layout) - chattingScreen सारखे same design
        Constant.dialogueFullScreen(this, R.layout.dialogue_full_screen_dialogue);
        Constant.dialogLayoutFullScreen.show();

        // Window setup करा (Setup window) - chattingScreen सारखे same margins/padding
        android.view.Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false);
            View rootView = window.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(false);
        }

        window.setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
            android.view.Window activityWindow = getWindow();
            androidx.core.view.WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
            View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(true);
        });

        // Image counter सेटअप करा (Setup image counter)
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setVisibility(View.VISIBLE);
            imageCounter.setText("1 / " + selectedImageUris.size());
        }

        // मल्टी-इमेज प्रिव्यू सेटअप करा (Setup multi-image preview)
        setupHorizontalGalleryPreview();
        
        // डायलॉग दाखवा (Show dialog)
        Constant.dialogLayoutFullScreen.show();
    }

    /**
     * हॉरिझॉन्टल गॅलरी प्रिव्यू सेटअप करण्यासाठी पद्धत (Method to setup horizontal gallery preview)
     */
    private void setupHorizontalGalleryPreview() {
        Log.d("HorizontalGallery", "Setting up horizontal gallery with " + selectedImageUris.size() + " images");

        // मुख्य इमेज प्रिव्यू ViewPager2 सेटअप करा (Setup main image preview ViewPager2)
        androidx.viewpager2.widget.ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        if (mainImagePreview != null) {
            mainImagePreview.setVisibility(View.VISIBLE);

            // मुख्य प्रिव्यूसाठी adapter सेटअप करा (Setup adapter for main preview)
            MainImagePreviewAdapter mainAdapter = new MainImagePreviewAdapter(this, selectedImageUris);
            mainImagePreview.setAdapter(mainAdapter);

            // पेज चेंज callback सेटअप करा (Setup page change callback)
            mainImagePreview.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);


                    // काउंटर अपडेट करा (Update counter)
                    TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
                    if (imageCounter != null) {
                        imageCounter.setText((position + 1) + " / " + selectedImageUris.size());
                        imageCounter.setVisibility(View.VISIBLE);
                    }

                    // Caption EditText अपडेट करा (Update caption EditText)
                    android.widget.EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        Log.d("PageChange", "Switched to position " + position + ", caption: '" + currentCaption + "'");

                        if (currentCaption != null) {
                            messageBoxMy.setText(currentCaption);
                            messageBoxMy.setSelection(messageBoxMy.getText().length());
                        } else {
                            messageBoxMy.setText("");
                        }
                    }
                }
            });
        }

        // हॉरिझॉन्टल RecyclerView सेटअप करा (Setup horizontal RecyclerView)


        // इतर elements लपवा (Hide other elements)
        View videoView = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
        if (videoView != null) {
            videoView.setVisibility(View.GONE);
        }

        View downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            downloadCtrl.setVisibility(View.GONE);
        }

        // Image counter सेटअप करा (Setup image counter)
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setText("1 / " + selectedImageUris.size());
            imageCounter.setVisibility(View.VISIBLE);
        }

        // Send button सेटअप करा (Setup send button)
        android.widget.LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        android.widget.EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);

        // Caption change listener सेटअप करा (Setup caption change listener)
        if (messageBoxMy != null) {
            messageBoxMy.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d("TextWatcher", "beforeTextChanged: '" + s + "'");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("TextWatcher", "onTextChanged: '" + s + "'");
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    // Save the caption as user types
                    currentCaption = s.toString();
                    Log.d("CaptionWatcher", "Caption updated: " + currentCaption);
                }
            });
        }

        // Send button click listener सेटअप करा (Setup send button click listener)
        if (sendGrp != null) {
            sendGrp.setOnClickListener(v -> {
                Log.d("SendMultiImage", "Sending " + selectedImageUris.size() + " images");
                
                // Multi-image send logic implement करा (Implement multi-image send logic)
                sendMultiImages();
                
                // Dialog dismiss करा (Dismiss dialog) - sendMultiImages() मध्ये already dismiss आहे
                // Constant.dialogLayoutFullScreen.dismiss(); // Remove duplicate dismiss
            });
        }
    }

    /**
     * मल्टी-व्हिडिओ प्रिव्यू सेटअप करण्यासाठी पद्धत (Method to setup multi-video preview)
     * chattingScreen.java मधील setupMultiVideoPreview() पद्धतीचा वापर करून
     */
    private void setupMultiVideoPreview() {
        Log.d("MultiVideoPreview", "Setting up preview with " + selectedVideoUris.size() + " videos");

        if (selectedVideoUris.isEmpty()) {
            Log.d("MultiVideoPreview", "No videos selected, returning");
            return;
        }

        // Duplicate dialog avoid करा (Avoid duplicate dialog)
        if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
            Log.d("MultiVideoPreview", "Dialog already showing, dismissing previous one");
            Constant.dialogLayoutFullScreen.dismiss();
        }

        // डायलॉग लेआउट सेटअप करा (Setup dialog layout) - video preview layout
        Constant.dialogueFullScreen(this, R.layout.dialogue_video_preview);
        Constant.dialogLayoutFullScreen.show();

        // Window setup करा (Setup window) - chattingScreen सारखे same margins/padding
        android.view.Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false);
            View rootView = window.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(false);
        }

        window.setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Constant.dialogLayoutFullScreen.setOnDismissListener(d -> {
            android.view.Window activityWindow = getWindow();
            androidx.core.view.WindowCompat.setDecorFitsSystemWindows(activityWindow, false);
            View rootView = activityWindow.getDecorView().findViewById(android.R.id.content);
            rootView.setFitsSystemWindows(true);
        });

        // Video counter सेटअप करा (Setup video counter)
        TextView videoCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.videoCounter);
        if (videoCounter != null) {
            videoCounter.setVisibility(View.VISIBLE);
            videoCounter.setText("1 / " + selectedVideoUris.size());
        }

        // Video preview setup करा (Setup video preview)
        setupHorizontalVideoPreview();
    }

    /**
     * Setup horizontal video preview gallery
     */
    private void setupHorizontalVideoPreview() {
        Log.d("HorizontalVideoGallery", "Setting up horizontal video gallery with " + selectedVideoUris.size() + " videos");

        // Setup main video preview ViewPager2
        androidx.viewpager2.widget.ViewPager2 mainVideoPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainVideoPreview);
        if (mainVideoPreview != null) {
            mainVideoPreview.setVisibility(View.VISIBLE);

            // Setup adapter for main preview
            com.Appzia.enclosure.Adapters.MainVideoPreviewAdapter mainAdapter = new com.Appzia.enclosure.Adapters.MainVideoPreviewAdapter(mContext, selectedVideoUris);
            mainVideoPreview.setAdapter(mainAdapter);

            // Setup page change callback to sync with horizontal RecyclerView
            mainVideoPreview.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    // Update counter
                    TextView videoCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.videoCounter);
                    if (videoCounter != null) {
                        videoCounter.setText((position + 1) + " / " + selectedVideoUris.size());
                    }

                    // Update caption EditText
                    android.widget.EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        Log.d("VideoPageChange", "Switched to video position " + position + ", caption: '" + currentCaption + "'");

                        if (currentCaption != null) {
                            messageBoxMy.setText(currentCaption);
                            messageBoxMy.setSelection(messageBoxMy.getText().length());
                        } else {
                            messageBoxMy.setText("");
                        }
                    }
                }
            });
        }

        // Setup horizontal RecyclerView for video thumbnails
        androidx.recyclerview.widget.RecyclerView horizontalRecyclerView = Constant.dialogLayoutFullScreen.findViewById(R.id.horizontalVideoRecyclerView);
        if (horizontalRecyclerView != null) {
            horizontalRecyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(mContext, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));

            // Setup adapter
            com.Appzia.enclosure.Adapters.VideoPreviewAdapter adapter = new com.Appzia.enclosure.Adapters.VideoPreviewAdapter(mContext, selectedVideoUris, selectedVideoFiles);
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
                android.widget.EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                if (messageBoxMy != null) {
                    Log.d("VideoThumbnailClick", "Clicked thumbnail " + position + ", caption: '" + currentCaption + "'");

                    if (currentCaption != null) {
                        messageBoxMy.setText(currentCaption);
                        messageBoxMy.setSelection(messageBoxMy.getText().length());
                    } else {
                        messageBoxMy.setText("");
                    }
                }
            });
        }

        // Setup caption EditText with TextWatcher
        android.widget.EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null) {
            messageBoxMy.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    // Save the caption as user types
                    currentCaption = s.toString();
                    Log.d("CaptionWatcher", "Caption updated: " + currentCaption);
                }
            });
        }

        // Setup send button
        android.widget.LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        if (sendGrp != null) {
            sendGrp.setOnClickListener(v -> {
                Log.d("SendMultiVideo", "Sending " + selectedVideoUris.size() + " videos");
                // Multi-video send logic implement करा (Implement multi-video send logic)
                sendMultiVideos();
            });
        }
    }

    /**
     * Multi-videos send करण्यासाठी पद्धत (Method to send multi-videos)
     * UploadChatHelper format मध्ये send करा
     */
    private void sendMultiVideos() {
        Log.d("SendMultiVideos", "=== STARTING MULTI-VIDEO SEND PROCESS ===");
        Log.d("SendMultiVideos", "Total videos to send: " + selectedVideoUris.size());
        Log.d("SendMultiVideos", "SelectedVideoUris: " + selectedVideoUris.toString());
        Log.d("SendMultiVideos", "VideoCaptions: " + videoCaptions.toString());
        
        try {
            // Required variables define करा (Define required variables)
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String uniqDate = Constant.getCurrentDate();
            
            Log.d("SendMultiVideos", "=== REQUIRED VARIABLES ===");
            Log.d("SendMultiVideos", "senderId: " + senderId);
            Log.d("SendMultiVideos", "uniqDate: " + uniqDate);
            Log.d("SendMultiVideos", "Total selected users: " + (receivedNameList != null ? receivedNameList.size() : 0));
            
            // Validate required variables
            if (receivedNameList == null || receivedNameList.isEmpty()) {
                Log.e("SendMultiVideos", "❌ No recipients selected");
                Toast.makeText(this, "Error: No recipients selected", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Database reference for modelId
            com.google.firebase.database.DatabaseReference database = com.google.firebase.database.FirebaseDatabase.getInstance().getReference();
            
            // प्रत्येक user साठी videos send करा (Send videos to each user)
            Log.d("SendMultiVideos", "=== STARTING USER PROCESSING LOOP ===");
            for (int userIndex = 0; userIndex < receivedNameList.size(); userIndex++) {
                forwardnameModel currentUser = receivedNameList.get(userIndex);
                String receiverUid = currentUser.getFriend_id();
                String userFTokenKey = currentUser.getF_token();
                
                Log.d("SendMultiVideos", "=== PROCESSING USER " + (userIndex + 1) + "/" + receivedNameList.size() + " ===");
                Log.d("SendMultiVideos", "User: " + currentUser.getName());
                Log.d("SendMultiVideos", "receiverUid: " + receiverUid);
                Log.d("SendMultiVideos", "userFTokenKey: " + userFTokenKey);
                
                // प्रत्येक video साठी messageModel create करा (Create messageModel for each video)
                Log.d("SendMultiVideos", "=== STARTING VIDEO PROCESSING FOR USER " + (userIndex + 1) + " ===");
                Log.d("SendMultiVideos", "Total videos to process for user " + (userIndex + 1) + ": " + selectedVideoUris.size());
                int videosProcessedForUser = 0;
                for (int i = 0; i < selectedVideoUris.size(); i++) {
                    Uri videoUri = selectedVideoUris.get(i);
                    String caption = videoCaptions.get(i);
                    if (caption == null) {
                        caption = "";
                    }
                    
                    // प्रत्येक video साठी unique modelId create करा (Create unique modelId for each video)
                    String videoModelId = database.push().getKey();
                    assert videoModelId != null;
                    
                    Log.d("SendMultiVideos", "=== PROCESSING VIDEO " + (i + 1) + "/" + selectedVideoUris.size() + " FOR USER " + (userIndex + 1) + " ===");
                    Log.d("SendMultiVideos", "Video URI: " + videoUri);
                    Log.d("SendMultiVideos", "Caption: '" + caption + "'");
                    Log.d("SendMultiVideos", "Generated modelId: " + videoModelId);
                
                    // Video file process करा (Process video file) - unique filename for each user
                    String fileName = "video_" + System.currentTimeMillis() + "_" + userIndex + "_" + i + "_" + videoModelId + ".mp4";
                    Log.d("SendMultiVideos", "Generated fileName: " + fileName);
                    File videoFile = processVideoFile(videoUri, fileName);
                
                    if (videoFile != null && videoFile.exists()) {
                        Log.d("SendMultiVideos", "Video file created successfully: " + videoFile.getAbsolutePath());
                        Log.d("SendMultiVideos", "File size: " + videoFile.length() + " bytes");
                        
                        // Create video thumbnail
                        File savedThumbnail = null;
                        String fileThumbName = null;
                        try {
                            android.graphics.Bitmap thumbnail = android.media.ThumbnailUtils.createVideoThumbnail(
                                videoFile.getAbsolutePath(), 
                                android.provider.MediaStore.Video.Thumbnails.MINI_KIND
                            );
                            if (thumbnail != null) {
                                String thumbnailName = "thumb_" + videoModelId + ".png";
                                
                                // Save thumbnail to local storage
                                try {
                                    File thumbnailDir = new File(getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), "Enclosure/Media/Thumbnail");
                                    if (!thumbnailDir.exists()) {
                                        thumbnailDir.mkdirs();
                                    }
                                    File localThumbnailFile = new File(thumbnailDir, thumbnailName);
                                    
                                    // Save bitmap to local storage
                                    java.io.FileOutputStream fos = new java.io.FileOutputStream(localThumbnailFile);
                                    thumbnail.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fos);
                                    fos.close();
                                    
                                    savedThumbnail = localThumbnailFile;
                                    fileThumbName = thumbnailName;
                                    Log.d("SendMultiVideos", "Created thumbnail for video " + (i + 1) + ": " + savedThumbnail.getAbsolutePath());
                                } catch (Exception e) {
                                    Log.e("SendMultiVideos", "Error saving thumbnail to local storage: " + e.getMessage(), e);
                                    // Fallback to cache directory
                                    savedThumbnail = com.Appzia.enclosure.Utils.FileUtils.saveBitmapToFile(mContext, thumbnail, thumbnailName);
                                    fileThumbName = thumbnailName;
                                }
                            } else {
                                Log.e("SendMultiVideos", "Failed to create thumbnail for video " + (i + 1));
                            }
                        } catch (Exception e) {
                            Log.e("SendMultiVideos", "Error creating thumbnail for video " + (i + 1) + ": " + e.getMessage(), e);
                        }
                        
                        // messageModel create करा (Create messageModel)
                        messageModel model = createVideoMessageModel(videoFile, caption, i, senderId, videoModelId, receiverUid, uniqDate, fileThumbName);
                    
                        if (model != null) {
                            Log.d("SendMultiVideos", "=== MESSAGE MODEL CREATED ===");
                            Log.d("SendMultiVideos", "Model dataType: " + model.getDataType());
                            Log.d("SendMultiVideos", "Model fileName: " + model.getFileName());
                            Log.d("SendMultiVideos", "Model caption: " + model.getCaption());
                            Log.d("SendMultiVideos", "Model modelId: " + model.getModelId());
                            
                            // UploadChatHelper मध्ये upload करा (Upload using UploadChatHelper)
                            Log.d("SendMultiVideos", "=== CREATING UPLOADCHATHELPER ===");
                                 UploadChatHelper uploadHelper = new UploadChatHelper(mContext, videoFile, null, senderId, userFTokenKey);
                                 Log.d("SendMultiVideos", "UploadChatHelper created with videoFile: " + (videoFile != null ? videoFile.getAbsolutePath() : "null"));
                            
                            Log.d("SendMultiVideos", "=== CALLING UPLOADCONTENT ===");
                            Log.d("SendMultiVideos", "uploadType: " + Constant.video);
                            Log.d("SendMultiVideos", "videoUri: " + videoUri);
                            Log.d("SendMultiVideos", "caption: " + caption);
                            Log.d("SendMultiVideos", "videoModelId: " + videoModelId);
                            Log.d("SendMultiVideos", "videoFile.getName(): " + videoFile.getName());
                            Log.d("SendMultiVideos", "getFileExtension(videoUri): " + getFileExtension(videoUri));
                            Log.d("SendMultiVideos", "receiverUid: " + receiverUid);
                            Log.d("SendMultiVideos", "videoFile exists: " + videoFile.exists());
                            Log.d("SendMultiVideos", "videoFile size: " + videoFile.length());
                            Log.d("SendMultiVideos", "videoFile canRead: " + videoFile.canRead());
                            Log.d("SendMultiVideos", "savedThumbnail: " + (savedThumbnail != null ? savedThumbnail.getAbsolutePath() : "null"));
                            Log.d("SendMultiVideos", "fileThumbName: " + fileThumbName);
                            
                            uploadHelper.uploadContent(
                                    Constant.video, // uploadType
                                    videoUri, // uri
                                    caption, // captionText
                                    videoModelId, // modelId - unique for each video
                                    savedThumbnail, // savedThumbnail
                                    fileThumbName, // fileThumbName
                                    videoFile.getName(), // fileName
                                    null, // contactName
                                    null, // contactPhone
                                    null, // audioTime
                                    null, // audioName
                                    getFileExtension(videoUri), // extension
                                    receiverUid, // receiverUid
                                    "", // replyCrtPostion
                                    "", // replyKey
                                    "", // replyOldData
                                    "", // replyType
                                    "", // replytextData
                                    Constant.video, // dataType
                                    videoFile.getName(), // fileName
                                    "", // forwaredKey
                                    model.getImageWidth(), // imageWidthDp
                                    model.getImageHeight(), // imageHeightDp
                                    model.getAspectRatio()  // aspectRatio
            );
                            Log.d("SendMultiVideos", "✅ Successfully called uploadContent for video " + (i + 1) + " for user " + (userIndex + 1) + " with modelId: " + videoModelId);
                            Log.d("SendMultiVideos", "UploadChatHelper.uploadContent() completed for video " + (i + 1));
                            videosProcessedForUser++;
                        } else {
                            Log.e("SendMultiVideos", "❌ Failed to create messageModel for video " + (i + 1) + " for user " + (userIndex + 1));
                        }
                    } else {
                        Log.e("SendMultiVideos", "❌ Failed to process video file " + (i + 1) + " for user " + (userIndex + 1) + " - file is null or doesn't exist");
                    }
                }
                
                Log.d("SendMultiVideos", "✅ Completed sending " + videosProcessedForUser + "/" + selectedVideoUris.size() + " videos to user " + (userIndex + 1) + ": " + currentUser.getName());
            }
            
            // UploadChatHelper start करा (Start UploadChatHelper)
            // UploadChatHelper automatically starts when created
            Log.d("SendMultiVideos", "=== UPLOAD PROCESS COMPLETED ===");
            Log.d("SendMultiVideos", "Total users: " + receivedNameList.size());
            Log.d("SendMultiVideos", "Total videos per user: " + selectedVideoUris.size());
            Log.d("SendMultiVideos", "Total videos sent: " + (receivedNameList.size() * selectedVideoUris.size()));
            
            // Dialog dismiss करा (Dismiss dialog)
            Log.d("SendMultiVideos", "=== DIALOG DISMISS PROCESS ===");
            if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                Log.d("DialogDismiss", "✅ Dismissing dialog after sending videos");
                Constant.dialogLayoutFullScreen.dismiss();
                Log.d("DialogDismiss", "✅ Dialog dismissed successfully");
            } else {
                Log.d("DialogDismiss", "❌ Dialog is null or not showing");
            }
            
            // Navigation logic based on number of users
            Log.d("SendMultiVideos", "=== NAVIGATION PROCESS ===");
            int listcount = receivedNameList.size();
            Log.d("SendMultiVideos", "Number of users: " + listcount);
            
            if (listcount == 1) {
                // Single user - navigate to chattingScreen
                forwardnameModel model1 = receivedNameList.get(0);
                Log.d("SendMultiVideos", "Navigating to chattingScreen for single user: " + model1.getName());
                
                Intent intent = new Intent(this, chattingScreen.class);
                intent.putExtra("nameKey", model1.getName());
                intent.putExtra("captionKey", "");
                intent.putExtra("photoKey", "");
                intent.putExtra("friendUidKey", model1.getFriend_id());
                intent.putExtra("msgLmtKey", "");
                intent.putExtra("ecKey", "ecKey");
                intent.putExtra("userFTokenKey", model1.getF_token());
                intent.putExtra("deviceType", "");
                intent.putExtra("fromInviteKey", "fromInviteKey");
                intent.putExtra("fromShareExternalData", true);
                intent.putExtra("messageType", "VIDEO");
                
                Log.d("SendMultiVideos", "Launching chattingScreen with extras:");
                Log.d("SendMultiVideos", "fromShareExternalData: " + intent.getBooleanExtra("fromShareExternalData", false));
                Log.d("SendMultiVideos", "messageType: " + intent.getStringExtra("messageType"));
                
                startActivity(intent);
                finish();
                
            } else {
                // Multiple users - navigate to MainActivityOld
                Log.d("SendMultiVideos", "Navigating to MainActivityOld for multiple users: " + listcount);
                
                Intent intent = new Intent(this, MainActivityOld.class);
                startActivity(intent);
                finish();
            }
            
            Log.d("SendMultiVideos", "=== MULTI-VIDEO SEND PROCESS COMPLETED ===");
            
        } catch (Exception e) {
            Log.e("SendMultiVideos", "Error sending multi-videos: " + e.getMessage());
            Toast.makeText(this, "Error sending videos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Video messageModel create करण्यासाठी पद्धत (Method to create video messageModel)
     */
    private messageModel createVideoMessageModel(File videoFile, String caption, int index, String senderId, String modelId, String receiverUid, String uniqDate, String fileNameThumbnail) {
        try {
            Log.d("CreateVideoMessageModel", "=== CREATING MESSAGE MODEL FOR VIDEO " + (index + 1) + " ===");
            Log.d("CreateVideoMessageModel", "videoFile: " + videoFile.getAbsolutePath());
            Log.d("CreateVideoMessageModel", "caption: '" + caption + "'");
            Log.d("CreateVideoMessageModel", "senderId: " + senderId);
            Log.d("CreateVideoMessageModel", "modelId: " + modelId);
            Log.d("CreateVideoMessageModel", "receiverUid: " + receiverUid);
            Log.d("CreateVideoMessageModel", "uniqDate: " + uniqDate);
            
            // Current timestamp मिळवा (Get current timestamp)
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
            Log.d("CreateVideoMessageModel", "currentDateTimeString: " + currentDateTimeString);
            
            // Extract video dimensions
            String imageWidth = "";
            String imageHeight = "";
            String aspectRatio = "";
            try {
                android.media.MediaMetadataRetriever retriever = new android.media.MediaMetadataRetriever();
                retriever.setDataSource(videoFile.getAbsolutePath());
                
                String width = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                String height = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                
                if (width != null && height != null) {
                    int videoWidth = Integer.parseInt(width);
                    int videoHeight = Integer.parseInt(height);
                    
                    // Calculate aspect ratio
                    float ratio = (float) videoWidth / videoHeight;
                    aspectRatio = String.format("%.2f", ratio);
                    
                    // Convert to DP (assuming 160 DPI)
                    float density = getResources().getDisplayMetrics().density;
                    imageWidth = String.format("%.2f", videoWidth / density);
                    imageHeight = String.format("%.2f", videoHeight / density);
                    
                    Log.d("CreateVideoMessageModel", "Video dimensions: " + videoWidth + "x" + videoHeight);
                    Log.d("CreateVideoMessageModel", "Aspect ratio: " + aspectRatio);
                    Log.d("CreateVideoMessageModel", "DP dimensions: " + imageWidth + "x" + imageHeight);
                }
                
                retriever.release();
            } catch (Exception e) {
                Log.e("CreateVideoMessageModel", "Error extracting video dimensions: " + e.getMessage());
            }
            
            // messageModel create करा (Create messageModel)
            Log.d("CreateVideoMessageModel", "Creating messageModel with dataType: " + Constant.video);
            messageModel model = new messageModel(
                senderId, // senderId
                caption, // message
                currentDateTimeString, // time
                videoFile.toString(), // document
                Constant.video, // dataType
                "", // extension
                "", // name
                "", // phone
                "", // micPhoto
                "", // miceTiming
                Constant.getSF.getString(Constant.full_name, ""), // userName
                "", // replytextData
                "", // replyKey
                "", // replyType
                "", // replyOldData
                "", // replyCrtPostion
                modelId, // modelId
                receiverUid, // receiverUid
                "", // forwaredKey
                "", // groupName
                "", // docSize
                videoFile.getName(), // fileName
                "", // thumbnail
                fileNameThumbnail != null ? fileNameThumbnail : "", // fileNameThumbnail
                caption, // caption
                1, // notification
                uniqDate, // uniqDate
                new ArrayList<>(), // emojiModels
                "", // replyMessage
                Constant.getCurrentTimestamp(), // timestamp
                imageWidth, // imageWidthDp
                imageHeight, // imageHeightDp
                aspectRatio, // aspectRatio
                "1" // selectionCount
            );
            
            Log.d("CreateVideoMessageModel", "=== MESSAGE MODEL CREATED SUCCESSFULLY ===");
            Log.d("CreateVideoMessageModel", "Model dataType: " + model.getDataType());
            Log.d("CreateVideoMessageModel", "Model fileName: " + model.getFileName());
            Log.d("CreateVideoMessageModel", "Model fileNameThumbnail: " + model.getFileNameThumbnail());
            Log.d("CreateVideoMessageModel", "Model imageWidth: " + model.getImageWidth());
            Log.d("CreateVideoMessageModel", "Model imageHeight: " + model.getImageHeight());
            Log.d("CreateVideoMessageModel", "Model aspectRatio: " + model.getAspectRatio());
            Log.d("CreateVideoMessageModel", "Model caption: " + model.getCaption());
            Log.d("CreateVideoMessageModel", "Model modelId: " + model.getModelId());
            Log.d("CreateVideoMessageModel", "Model document: " + model.getDocument());
            Log.d("CreateVideoMessageModel", "Created messageModel for video " + (index + 1) + ": " + videoFile.getName());
            
            return model;
            
        } catch (Exception e) {
            Log.e("CreateVideoMessageModel", "❌ Error creating video messageModel: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Multi-images send करण्यासाठी पद्धत (Method to send multi-images)
     * UploadChatHelper format मध्ये send करा
     */
    private void sendMultiImages() {
        Log.d("SendMultiImages", "=== STARTING MULTI-IMAGE SEND PROCESS ===");
        Log.d("SendMultiImages", "Total images to send: " + selectedImageUris.size());
        Log.d("SendMultiImages", "SelectedImageUris: " + selectedImageUris.toString());
        Log.d("SendMultiImages", "ImageCaptions: " + imageCaptions.toString());
        
        try {
            // Required variables define करा (Define required variables)
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String uniqDate = Constant.getCurrentDate();
            
            Log.d("SendMultiImages", "=== REQUIRED VARIABLES ===");
            Log.d("SendMultiImages", "senderId: " + senderId);
            Log.d("SendMultiImages", "uniqDate: " + uniqDate);
            Log.d("SendMultiImages", "Total selected users: " + (receivedNameList != null ? receivedNameList.size() : 0));
            
            // Validate required variables
            if (receivedNameList == null || receivedNameList.isEmpty()) {
                Log.e("SendMultiImages", "❌ No recipients selected");
                Toast.makeText(this, "Error: No recipients selected", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Database reference for modelId
            com.google.firebase.database.DatabaseReference database = com.google.firebase.database.FirebaseDatabase.getInstance().getReference();
            
            // प्रत्येक user साठी images send करा (Send images to each user)
            Log.d("SendMultiImages", "=== STARTING USER PROCESSING LOOP ===");
            for (int userIndex = 0; userIndex < receivedNameList.size(); userIndex++) {
                forwardnameModel currentUser = receivedNameList.get(userIndex);
                String receiverUid = currentUser.getFriend_id();
                String userFTokenKey = currentUser.getF_token();
                
                Log.d("SendMultiImages", "=== PROCESSING USER " + (userIndex + 1) + "/" + receivedNameList.size() + " ===");
                Log.d("SendMultiImages", "User: " + currentUser.getName());
                Log.d("SendMultiImages", "receiverUid: " + receiverUid);
                Log.d("SendMultiImages", "userFTokenKey: " + userFTokenKey);
                
                // प्रत्येक image साठी messageModel create करा (Create messageModel for each image)
                Log.d("SendMultiImages", "=== STARTING IMAGE PROCESSING FOR USER " + (userIndex + 1) + " ===");
                for (int i = 0; i < selectedImageUris.size(); i++) {
                    Uri imageUri = selectedImageUris.get(i);
                    String caption = imageCaptions.get(i);
                    if (caption == null) {
                        caption = "";
                    }
                    
                    // प्रत्येक image साठी unique modelId create करा (Create unique modelId for each image)
                    String imageModelId = database.push().getKey();
                    assert imageModelId != null;
                    
                    Log.d("SendMultiImages", "=== PROCESSING IMAGE " + (i + 1) + "/" + selectedImageUris.size() + " FOR USER " + (userIndex + 1) + " ===");
                    Log.d("SendMultiImages", "Image URI: " + imageUri);
                    Log.d("SendMultiImages", "Caption: '" + caption + "'");
                    Log.d("SendMultiImages", "Generated modelId: " + imageModelId);
                
                    // Image file process करा (Process image file) - unique filename for each user
                    String fileName = "image_" + System.currentTimeMillis() + "_" + userIndex + "_" + i + ".jpg";
                    Log.d("SendMultiImages", "Generated fileName: " + fileName);
                    File imageFile = processImageFile(imageUri, fileName);
                
                    if (imageFile != null && imageFile.exists()) {
                        Log.d("SendMultiImages", "Image file created successfully: " + imageFile.getAbsolutePath());
                        Log.d("SendMultiImages", "File size: " + imageFile.length() + " bytes");

                        String[] dimensions = Constant.calculateImageDimensions(this, imageFile, Uri.fromFile(imageFile));
                        String imageWidthDp = dimensions.length > 0 ? dimensions[0] : "";
                        String imageHeightDp = dimensions.length > 1 ? dimensions[1] : "";
                        String aspectRatio = dimensions.length > 2 ? dimensions[2] : "";
                        
                        // messageModel create करा (Create messageModel)
                        messageModel model = createImageMessageModel(imageFile, caption, i, senderId, imageModelId, receiverUid, uniqDate);
                    
                        if (model != null) {
                            Log.d("SendMultiImages", "=== MESSAGE MODEL CREATED ===");
                            Log.d("SendMultiImages", "Model dataType: " + model.getDataType());
                            Log.d("SendMultiImages", "Model fileName: " + model.getFileName());
                            Log.d("SendMultiImages", "Model caption: " + model.getCaption());
                            Log.d("SendMultiImages", "Model modelId: " + model.getModelId());
                            
                            // UploadChatHelper मध्ये upload करा (Upload using UploadChatHelper)
                            Log.d("SendMultiImages", "=== CREATING UPLOADCHATHELPER ===");
                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, imageFile, null, senderId, userFTokenKey);
                            
                            Log.d("SendMultiImages", "=== CALLING UPLOADCONTENT ===");
                            Log.d("SendMultiImages", "uploadType: " + Constant.img);
                            Log.d("SendMultiImages", "imageUri: " + imageUri);
                            Log.d("SendMultiImages", "caption: " + caption);
                            Log.d("SendMultiImages", "imageModelId: " + imageModelId);
                            Log.d("SendMultiImages", "imageFile.getName(): " + imageFile.getName());
                            Log.d("SendMultiImages", "getFileExtension(imageUri): " + getFileExtension(imageUri));
                            Log.d("SendMultiImages", "receiverUid: " + receiverUid);
                            
                            uploadHelper.uploadContent(
                                    Constant.img, // uploadType
                                    imageUri, // uri
                                    caption, // captionText
                                    imageModelId, // modelId - unique for each image
                                    null, // savedThumbnail
                                    null, // fileThumbName
                                    imageFile.getName(), // fileName
                                    null, // contactName
                                    null, // contactPhone
                                    null, // audioTime
                                    null, // audioName
                                    getFileExtension(imageUri), // extension
                                    receiverUid, // receiverUid
                                    "", // replyCrtPostion
                                    "", // replyKey
                                    "", // replyOldData
                                    "", // replyType
                                    "", // replytextData
                                    Constant.img, // dataType
                                    imageFile.getName(), // fileName
                                    "", // forwaredKey
                                    imageWidthDp, // imageWidthDp
                                    imageHeightDp, // imageHeightDp
                                    aspectRatio  // aspectRatio
            );
                            Log.d("SendMultiImages", "✅ Successfully added image " + (i + 1) + " to UploadChatHelper for user " + (userIndex + 1) + " with modelId: " + imageModelId);
                        } else {
                            Log.e("SendMultiImages", "❌ Failed to create messageModel for image " + (i + 1) + " for user " + (userIndex + 1));
                        }
                    } else {
                        Log.e("SendMultiImages", "❌ Failed to process image file " + (i + 1) + " for user " + (userIndex + 1) + " - file is null or doesn't exist");
                    }
                }
                
                Log.d("SendMultiImages", "✅ Completed sending " + selectedImageUris.size() + " images to user " + (userIndex + 1) + ": " + currentUser.getName());
            }
            
            // UploadChatHelper start करा (Start UploadChatHelper)
            // UploadChatHelper automatically starts when created
            Log.d("SendMultiImages", "=== UPLOAD PROCESS COMPLETED ===");
            Log.d("SendMultiImages", "Total users: " + receivedNameList.size());
            Log.d("SendMultiImages", "Total images per user: " + selectedImageUris.size());
            Log.d("SendMultiImages", "Total images sent: " + (receivedNameList.size() * selectedImageUris.size()));
            
            // Dialog dismiss करा (Dismiss dialog)
            Log.d("SendMultiImages", "=== DIALOG DISMISS PROCESS ===");
            if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                Log.d("DialogDismiss", "✅ Dismissing dialog after sending images");
                Constant.dialogLayoutFullScreen.dismiss();
                Log.d("DialogDismiss", "✅ Dialog dismissed successfully");
            } else {
                Log.d("DialogDismiss", "❌ Dialog is null or not showing");
            }
            
            // Navigation logic based on number of users
            Log.d("SendMultiImages", "=== NAVIGATION PROCESS ===");
            int listcount = receivedNameList.size();
            Log.d("SendMultiImages", "Number of users: " + listcount);
            
            if (listcount == 1) {
                // Single user - navigate to chattingScreen with UI update
                forwardnameModel model1 = receivedNameList.get(0);
                Log.d("SendMultiImages", "Navigating to chattingScreen for single user: " + model1.getName());
                
                Intent intent = new Intent(this, chattingScreen.class);
                intent.putExtra("nameKey", model1.getName());
                intent.putExtra("captionKey", "");
                intent.putExtra("photoKey", "");
                intent.putExtra("friendUidKey", model1.getFriend_id());
                intent.putExtra("msgLmtKey", "");
                intent.putExtra("ecKey", "ecKey");
                intent.putExtra("userFTokenKey", model1.getF_token());
                intent.putExtra("deviceType", "");
                intent.putExtra("fromInviteKey", "fromInviteKey");
                intent.putExtra("fromShareExternalData", true);
                intent.putExtra("messageType", "IMAGE");
                
                Log.d("SendMultiImages", "Launching chattingScreen with extras:");
                Log.d("SendMultiImages", "fromShareExternalData: " + intent.getBooleanExtra("fromShareExternalData", false));
                Log.d("SendMultiImages", "messageType: " + intent.getStringExtra("messageType"));
                
                startActivity(intent);
                finish();
                
            } else {
                // Multiple users - navigate to MainActivityOld
                Log.d("SendMultiImages", "Navigating to MainActivityOld for multiple users: " + listcount);
                
                Intent intent = new Intent(this, MainActivityOld.class);
                startActivity(intent);
                finish();
            }
            
            Log.d("SendMultiImages", "=== MULTI-IMAGE SEND PROCESS COMPLETED ===");
            
        } catch (Exception e) {
            Log.e("SendMultiImages", "Error sending multi-images: " + e.getMessage());
            Toast.makeText(this, "Error sending images: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Image file process करण्यासाठी पद्धत (Method to process image file)
     */
    private File processImageFile(Uri imageUri, String fileName) {
        try {
            // Use the specific local storage path you mentioned
            File mediaDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }
            
            File imageFile = new File(mediaDir, fileName);
            
            // URI से file copy करा (Copy from URI to file)
            java.io.InputStream inputStream = getContentResolver().openInputStream(imageUri);
            java.io.FileOutputStream outputStream = new java.io.FileOutputStream(imageFile);
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            inputStream.close();
            outputStream.close();
            
            Log.d("ProcessImageFile", "Image file created: " + imageFile.getAbsolutePath());
            return imageFile;
            
        } catch (Exception e) {
            Log.e("ProcessImageFile", "Error processing image file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Process video files from URIs
     */
    private void processVideoFiles() {
        Log.d("ProcessVideoFiles", "Processing " + selectedVideoUris.size() + " videos");
        
        selectedVideoFiles.clear();
        
        for (int i = 0; i < selectedVideoUris.size(); i++) {
            Uri videoUri = selectedVideoUris.get(i);
            try {
                // Create video file
                String fileName = "video_" + System.currentTimeMillis() + "_" + i + ".mp4";
                File videoFile = processVideoFile(videoUri, fileName);
                
                if (videoFile != null && videoFile.exists()) {
                    selectedVideoFiles.add(videoFile);
                    Log.d("ProcessVideoFiles", "Processed video " + (i + 1) + ": " + videoFile.getName());
                } else {
                    Log.e("ProcessVideoFiles", "Failed to process video " + (i + 1));
                    selectedVideoFiles.add(null);
                }
            } catch (Exception e) {
                Log.e("ProcessVideoFiles", "Error processing video " + (i + 1) + ": " + e.getMessage());
                selectedVideoFiles.add(null);
            }
        }
        
        Log.d("ProcessVideoFiles", "Video processing completed. Files: " + selectedVideoFiles.size());
    }

    /**
     * Process individual video file from URI
     */
    private File processVideoFile(Uri videoUri, String fileName) {
        try {
            // Use the specific local storage path for videos
            File mediaDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }
            
            File videoFile = new File(mediaDir, fileName);
            
            // URI से file copy करा (Copy from URI to file)
            java.io.InputStream inputStream = getContentResolver().openInputStream(videoUri);
            java.io.FileOutputStream outputStream = new java.io.FileOutputStream(videoFile);
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            inputStream.close();
            outputStream.close();
            
            Log.d("ProcessVideoFile", "Video file created: " + videoFile.getAbsolutePath());
            return videoFile;
            
        } catch (Exception e) {
            Log.e("ProcessVideoFile", "Error processing video file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Image messageModel create करण्यासाठी पद्धत (Method to create image messageModel)
     */
    private messageModel createImageMessageModel(File imageFile, String caption, int index, String senderId, String modelId, String receiverUid, String uniqDate) {
        try {
            Log.d("CreateImageMessageModel", "=== CREATING MESSAGE MODEL FOR IMAGE " + (index + 1) + " ===");
            Log.d("CreateImageMessageModel", "imageFile: " + imageFile.getAbsolutePath());
            Log.d("CreateImageMessageModel", "caption: '" + caption + "'");
            Log.d("CreateImageMessageModel", "senderId: " + senderId);
            Log.d("CreateImageMessageModel", "modelId: " + modelId);
            Log.d("CreateImageMessageModel", "receiverUid: " + receiverUid);
            Log.d("CreateImageMessageModel", "uniqDate: " + uniqDate);
            
            // Current timestamp मिळवा (Get current timestamp)
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
            Log.d("CreateImageMessageModel", "currentDateTimeString: " + currentDateTimeString);
            
            // Calculate image dimensions for metadata
            String[] dimensions = Constant.calculateImageDimensions(this, imageFile, Uri.fromFile(imageFile));
            String imageWidthDp = dimensions.length > 0 ? dimensions[0] : "";
            String imageHeightDp = dimensions.length > 1 ? dimensions[1] : "";
            String aspectRatio = dimensions.length > 2 ? dimensions[2] : "";

            // messageModel create करा (Create messageModel)
            Log.d("CreateImageMessageModel", "Creating messageModel with dataType: " + Constant.img);
            messageModel model = new messageModel(
                senderId, // senderId
                caption, // message
                currentDateTimeString, // time
                imageFile.toString(), // document
                Constant.img, // dataType
                "", // extension
                "", // name
                "", // phone
                "", // micPhoto
                "", // miceTiming
                Constant.getSF.getString(Constant.full_name, ""), // userName
                "", // replytextData
                "", // replyKey
                "", // replyType
                "", // replyOldData
                "", // replyCrtPostion
                modelId, // modelId
                receiverUid, // receiverUid
                "", // forwaredKey
                "", // groupName
                "", // docSize
                imageFile.getName(), // fileName
                "", // thumbnail
                "", // fileNameThumbnail
                caption, // caption
                1, // notification
                uniqDate, // uniqDate
                new ArrayList<>(), // emojiModels
                "", // replyMessage
                Constant.getCurrentTimestamp(), // timestamp
                imageWidthDp, // imageWidthDp
                imageHeightDp, // imageHeightDp
                aspectRatio, // aspectRatio
                "1" // selectionCount
            );
            
            Log.d("CreateImageMessageModel", "=== MESSAGE MODEL CREATED SUCCESSFULLY ===");
            Log.d("CreateImageMessageModel", "Model dataType: " + model.getDataType());
            Log.d("CreateImageMessageModel", "Model fileName: " + model.getFileName());
            Log.d("CreateImageMessageModel", "Model caption: " + model.getCaption());
            Log.d("CreateImageMessageModel", "Model modelId: " + model.getModelId());
            Log.d("CreateImageMessageModel", "Model document: " + model.getDocument());
            Log.d("CreateImageMessageModel", "Created messageModel for image " + (index + 1) + ": " + imageFile.getName());
            return model;
            
        } catch (Exception e) {
            Log.e("CreateMessageModel", "Error creating messageModel: " + e.getMessage());
            return null;
        }
    }

}