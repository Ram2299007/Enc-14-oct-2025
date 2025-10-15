package com.Appzia.enclosure.Screens;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.view.WindowCompat;
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

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivityVideoPlayerScreenBinding;

import java.io.File;
import java.util.Locale;

@UnstableApi
public class show_video_playerScreen extends AppCompatActivity {
    ActivityVideoPlayerScreenBinding binding;
    Context mContext;
    ExoPlayer player;

    Handler handler = new Handler();
    Runnable updateProgressAction;
    private int resizeModeIndex = 0; // 0: FIT, 1: FILL, 2: ZOOM
    private boolean wasPlaying = false;

    // References to the custom control views
    private LinearLayout topControls;
    private LinearLayout bottomControls;
    private LinearLayout centerControls; // Added for central play/pause
    private ImageView playButton;
    private ImageView pauseButton;
    private LinearLayout backArrowButton;
    private LinearLayout resizeLayout;
    private TextView resizeText;
    private TextView startTime;
    private TextView totalTime;
    private DefaultTimeBar exoProgress;
    private ImageView forwardButton;
    private ImageView replyButton;
    private TextView nameTitle; // Added for the video title

    private static final long CONTROLS_HIDE_DELAY = 2000; // 2 seconds as requested

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onResume() {
        super.onResume();
        initializePlayerAndControls();
    }

    @OptIn(markerClass = UnstableApi.class)
    private void initializePlayerAndControls() {
        String viewHolderTypeKey = getIntent().getStringExtra("viewHolderTypeKey");

        PlayerView playerView = binding.senderVideo;
        topControls = playerView.findViewById(R.id.top_controls);
        bottomControls = playerView.findViewById(R.id.bottom_controls);
        centerControls = playerView.findViewById(R.id.center_controls);
        playButton = playerView.findViewById(R.id.play);
        pauseButton = playerView.findViewById(R.id.pause);
        backArrowButton = playerView.findViewById(R.id.backarrow);
        resizeLayout = playerView.findViewById(R.id.rsizeLyt);
        resizeText = playerView.findViewById(R.id.resizeText);
        startTime = playerView.findViewById(R.id.startTime);
        totalTime = playerView.findViewById(R.id.totalTime);
        exoProgress = playerView.findViewById(R.id.exoProgress);
        forwardButton = playerView.findViewById(R.id.forward);
        replyButton = playerView.findViewById(R.id.reply);
        nameTitle = playerView.findViewById(R.id.nameTitle);


        // Always show controls and system bars initially when the activity is created/resumed.
        showControls();
        showSystemBars();


        // Setup touch listener for the player view to toggle controls
        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleControlsVisibility();
            }
        });

        // Set up the custom control listeners
        setupControlListeners();


        if (viewHolderTypeKey != null) {
            String data = getIntent().getStringExtra("videoUri");
            Log.d("TAG", "My data: " + data);

            File customFolder;
            String exactPath = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (viewHolderTypeKey.equals(Constant.senderViewHolder)) {
                    customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                } else { // receiverViewHolder
                    customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                }
            } else {
                if (viewHolderTypeKey.equals(Constant.senderViewHolder)) {
                    customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Videos");
                } else { // receiverViewHolder
                    customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                }
            }
            exactPath = customFolder.getAbsolutePath();

            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mContext);
            TrackSelectionParameters trackSelectionParameters = trackSelector.getParameters().
                    buildUpon().setForceLowestBitrate(true).build();
            trackSelector.setParameters((DefaultTrackSelector.Parameters) trackSelectionParameters);

            String mediaUri;
            if (doesFileExist(exactPath + "/" + data)) {
                mediaUri = exactPath + "/" + data;
            } else {
              //  Toast.makeText(mContext, "File not exist, playing from raw URI", Toast.LENGTH_SHORT).show();
                mediaUri = data; // Fallback to raw URI if file doesn't exist at expected path
            }

            player = new ExoPlayer.Builder(mContext).setTrackSelector(trackSelector).build();
            MediaItem mediaItem = MediaItem.fromUri(mediaUri);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true); // Auto-play the video initially
            binding.senderVideo.setPlayer(player);

            // Set video title if available
            if (data != null) {
                nameTitle.setText(new File(data).getName());
            }

            player.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int state) {
                    if (state == Player.STATE_READY) {
                        long duration = player.getDuration();
                        totalTime.setText(stringForTime(duration));
                        exoProgress.setDuration(duration); // Set total duration on progress bar

                        // If player is ready, update play/pause button state
                        // The auto-hide logic is now primarily in onIsPlayingChanged
                        if (player.getPlayWhenReady()) {
                            playButton.setVisibility(View.GONE);
                            pauseButton.setVisibility(View.VISIBLE);
                        } else {
                            playButton.setVisibility(View.VISIBLE);
                            pauseButton.setVisibility(View.GONE);
                        }

                    } else if (state == Player.STATE_ENDED) {
                        player.seekTo(0);
                        player.setPlayWhenReady(false);
                        startTime.setText(stringForTime(0));
                        exoProgress.setPosition(0);
                        playButton.setVisibility(View.VISIBLE);
                        pauseButton.setVisibility(View.GONE);
                        showControls(); // Show custom controls when video ends
                        showSystemBars(); // Show system bars when video ends
                        stopProgressUpdate(); // Ensure progress updates stop when ended
                        stopHideControlsTimer(); // Ensure controls stay visible at end
                    }
                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if (isPlaying) {
                        playButton.setVisibility(View.GONE);
                        pauseButton.setVisibility(View.VISIBLE);
                        startProgressUpdate(); // Start updates when playing
                        // When playing, schedule controls to hide after delay and hide system bars
                        hideControlsAfterDelay();
                        hideSystemBars();
                    } else {
                        playButton.setVisibility(View.VISIBLE);
                        pauseButton.setVisibility(View.GONE);
                        stopProgressUpdate(); // Stop updates when paused
                        // When paused, show controls and system bars
                        showControls();
                        showSystemBars();
                        // Stop the auto-hide timer when paused
                        stopHideControlsTimer(); // Important to stop auto-hide when paused
                    }
                }
            });

            // No initial hideControlsAfterDelay() call here.
            // It will be triggered by onIsPlayingChanged when playback actually starts.
        }
    }

    private void setupControlListeners() {
        resizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (resizeModeIndex) {
                    case 0:
                        binding.senderVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                        resizeText.setText("Fit Mode");
                        break;
                    case 1:
                        binding.senderVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                        resizeText.setText("Fill Mode");
                        break;
                    case 2:
                        binding.senderVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                        resizeText.setText("Zoom Mode");
                        break;
                }
                resizeModeIndex = (resizeModeIndex + 1) % 3;
                resetHideControlsTimer(); // Keep controls visible for a bit after resize
            }
        });

        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null) {
                    player.seekTo(player.getCurrentPosition() + 10000); // Forward 10 seconds
                    resetHideControlsTimer(); // Keep controls visible for a bit after seeking
                }
            }
        });

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null) {
                    long num = player.getCurrentPosition() - 10000; // Rewind 10 seconds
                    if (num < 0) {
                        player.seekTo(0);
                    } else {
                        player.seekTo(num);
                    }
                    resetHideControlsTimer(); // Keep controls visible for a bit after seeking
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null) {
                    player.play();
                    resetHideControlsTimer(); // Reset timer to keep controls visible after interaction
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null) {
                    player.pause();
                    resetHideControlsTimer(); // Reset timer to keep controls visible after interaction
                }
            }
        });

        exoProgress.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void  onScrubStart(TimeBar timeBar, long position) {
                wasPlaying = player.getPlayWhenReady();
                player.setPlayWhenReady(false); // Pause playback during scrub
                stopHideControlsTimer(); // Keep custom controls visible while scrubbing
                showSystemBars(); // Show system bars while scrubbing
            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                startTime.setText(stringForTime(position)); // Update current time display during scrub
                exoProgress.setPosition(position); // Update the visual position of the scrub bar itself
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                player.seekTo(position); // Seek to the new position
                if (wasPlaying) {
                    player.setPlayWhenReady(true); // Resume playback if it was playing before scrub
                } else {
                    // If video was not playing before scrub, keep system bars visible
                    showSystemBars();
                }
                // When scrubbing stops, immediately show controls and then schedule to hide them
                showControls();
                resetHideControlsTimer(); // Resume auto-hide for custom controls after scrubbing
            }
        });
    }


    private void toggleControlsVisibility() {
        if (topControls.getVisibility() == View.VISIBLE) {
            hideControls();
            hideSystemBars();
        } else {
            // Crucial: When showing controls, stop any pending hide actions first
            stopHideControlsTimer();
            showControls();
            showSystemBars();
            // Then, if the player is currently playing, schedule them to hide after a delay
            if (player != null && player.isPlaying()) {
                hideControlsAfterDelay();
            }
        }
    }

    private void showControls() {
        topControls.setVisibility(View.VISIBLE);
        bottomControls.setVisibility(View.VISIBLE);
        centerControls.setVisibility(View.VISIBLE);

        // Ensure play/pause are correctly set based on player state
        if (player != null) {
            if (player.isPlaying()) {
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
            } else {
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
            }
        } else {
            // Default state if player is null (e.g., before initialization)
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }
    }

    private void hideControls() {
        topControls.setVisibility(View.GONE);
        bottomControls.setVisibility(View.GONE);
        centerControls.setVisibility(View.GONE);
    }

    //region System Bar Management
    private void hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.systemBars());
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            // For older APIs, use deprecated FLAG_FULLSCREEN and SYSTEM_UI_FLAGs
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    private void showSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.show(WindowInsets.Type.systemBars());
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_DEFAULT); // Reset to default behavior
            }
        } else {
            // For older APIs, clear fullscreen flags and restore default visibility
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    //endregion

    private void startProgressUpdate() {
        stopProgressUpdate(); // Ensure no duplicate callbacks are running
        updateProgressAction = new Runnable() {
            @Override
            public void run() {
                if (player != null && player.isPlaying()) {
                    long currentPos = player.getCurrentPosition();
                    long duration = player.getDuration();
                    long bufferedPosition = player.getBufferedPosition(); // Get buffered position

                    startTime.setText(stringForTime(currentPos));
                    totalTime.setText(stringForTime(duration));
                    exoProgress.setDuration(duration);
                    exoProgress.setPosition(currentPos);
                    exoProgress.setBufferedPosition(bufferedPosition); // Update buffered position

                    handler.postDelayed(this, 500); // Update every 500ms
                }
            }
        };
        handler.post(updateProgressAction);
    }

    private void stopProgressUpdate() {
        handler.removeCallbacks(updateProgressAction);
    }

    private final Runnable hideControlsRunnable = new Runnable() {
        @Override
        public void run() {
            // Only auto-hide custom controls if currently playing AND controls are currently visible
            if (player != null && player.isPlaying() && topControls.getVisibility() == View.VISIBLE) {
                hideControls();
                hideSystemBars();
            }
        }
    };

    private void hideControlsAfterDelay() {
        handler.removeCallbacks(hideControlsRunnable);
        handler.postDelayed(hideControlsRunnable, CONTROLS_HIDE_DELAY);
    }

    private void resetHideControlsTimer() {
        handler.removeCallbacks(hideControlsRunnable);
        // Only schedule hide if the player is currently playing
        if (player != null && player.isPlaying()) {
            handler.postDelayed(hideControlsRunnable, CONTROLS_HIDE_DELAY);
        } else {
            // If player is paused or ended, ensure system bars are visible
            showSystemBars();
            stopHideControlsTimer(); // No need to schedule hide if not playing
        }
    }

    private void stopHideControlsTimer() {
        handler.removeCallbacks(hideControlsRunnable);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = binding.getRoot().getContext();

        // Prevent screenshots and screen recording
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // Show menu only if coming from chat adapters (senderViewHolder or receiverViewHolder)
        String viewHolderTypeKey = getIntent().getStringExtra("viewHolderTypeKey");
        Log.d("VideoPlayer", "viewHolderTypeKey: " + viewHolderTypeKey);
        Log.d("VideoPlayer", "senderViewHolder: " + Constant.senderViewHolder);
        Log.d("VideoPlayer", "receiverViewHolder: " + Constant.receiverViewHolder);
        
        // Get menu button from PlayerView's custom layout
        PlayerView playerView = binding.senderVideo;
        LinearLayout menuButton = playerView.findViewById(R.id.videoMenu);
        
        // Apply theme color to menuPoint
        Constant.getSfFuncion(mContext);
        String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        ImageView menuPoint = playerView.findViewById(R.id.menuPoint);
        if (menuPoint != null) {
            menuPoint.setColorFilter(Color.parseColor(themeColor));
        }
        
        if (viewHolderTypeKey != null && 
            (viewHolderTypeKey.equals(Constant.senderViewHolder) || viewHolderTypeKey.equals(Constant.receiverViewHolder))) {
            Log.d("VideoPlayer", "Showing menu button");
            menuButton.setVisibility(View.VISIBLE);
            
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.dialogueLayoutShowImageScreen(mContext, R.layout.refresh_ly);
                    Dialog dialogueLayoutCLearLogUp = Constant.dialogLayoutColor;
                    dialogueLayoutCLearLogUp.show();
                    LinearLayout save = dialogueLayoutCLearLogUp.findViewById(R.id.clearcalllog);
                    // 1B1C1C
                    save.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B1C1C")));

                    TextView textView = dialogueLayoutCLearLogUp.findViewById(R.id.textView);

                    textView.setText("Save");
                    textView.setTextColor(Color.parseColor("#ffffff"));
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveVideoToGallery();
                            Constant.dialogLayoutColor.dismiss();
                        }
                    });

                }
            });
        } else {
            // Hide menu if not coming from chat adapters
            Log.d("VideoPlayer", "Hiding menu button");
            menuButton.setVisibility(View.GONE);
        }

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }

    @Override
    public void onBackPressed() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        SmoothNavigationHelper.finishWithSlideToRight(show_video_playerScreen.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        stopProgressUpdate(); // Stop updates
        stopHideControlsTimer(); // Stop auto-hide

        // Always show system bars when the activity is stopped
        showSystemBars();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
        stopProgressUpdate();
        stopHideControlsTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false); // Pause the player when activity goes to background
        }
        stopHideControlsTimer(); // Stop auto-hide when paused
        showSystemBars(); // Ensure system bars are shown when activity pauses or goes to background
    }

    public boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
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

    private void saveVideoToGallery() {
        try {
            String viewHolderTypeKey = getIntent().getStringExtra("viewHolderTypeKey");
            String videoUri = getIntent().getStringExtra("videoUri");
            
            if (videoUri == null) {
                showToast("Video not found");
                return;
            }

            String fileName = "Enclosure_" + System.currentTimeMillis() + ".mp4";
            File sourceFile = null;

            if (viewHolderTypeKey != null) {
                if (viewHolderTypeKey.equals(Constant.senderViewHolder) || viewHolderTypeKey.equals(Constant.receiverViewHolder)) {
                    // Handle local files
                    File customFolder;
                    String exactPath = null;
                    
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                        exactPath = customFolder.getAbsolutePath();
                    } else {
                        customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Videos");
                        exactPath = customFolder.getAbsolutePath();
                    }

                    String localVideoPath = exactPath + "/" + videoUri;
                    if (doesFileExist(localVideoPath)) {
                        sourceFile = new File(localVideoPath);
                    }
                }
            }

            // If sourceFile is null, try to use the videoUri directly
            if (sourceFile == null) {
                if (videoUri.startsWith("http")) {
                    showToast("Cannot save online videos");
                    return;
                } else {
                    sourceFile = new File(videoUri);
                    if (!sourceFile.exists()) {
                        showToast("Video file not found");
                        return;
                    }
                }
            }

            if (sourceFile != null && sourceFile.exists()) {
                saveVideoFileToGallery(sourceFile, fileName);
            } else {
                showToast("Failed to access video file");
            }

        } catch (Exception e) {
            showToast("Failed to save video");
        }
    }

    private void saveVideoFileToGallery(File sourceFile, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10+ (API 29+)
                ContentResolver resolver = mContext.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/Enclosure");

                Uri videoUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                if (videoUri != null) {
                    OutputStream outputStream = resolver.openOutputStream(videoUri);
                    if (outputStream != null) {
                        FileInputStream inputStream = new FileInputStream(sourceFile);
                        copyFile(inputStream, outputStream);
                        inputStream.close();
                        outputStream.close();
                        showToast("Video saved");
                    } else {
                        showToast("Failed to save video");
                    }
                } else {
                    showToast("Failed to save video");
                }
            } else {
                // For older Android versions
                String moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).toString();
                File destinationFile = new File(moviesDir, fileName);
                
                FileInputStream inputStream = new FileInputStream(sourceFile);
                FileOutputStream outputStream = new FileOutputStream(destinationFile);
                copyFile(inputStream, outputStream);
                inputStream.close();
                outputStream.close();

                // Notify media scanner
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(destinationFile));
                mContext.sendBroadcast(mediaScanIntent);
                
                showToast("Video saved");
            }
        } catch (Exception e) {
            showToast("Failed to save video");
        }
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
    }

    private void showToast(String message) {
        Constant.showCustomToast(message, findViewById(R.id.includedToast), findViewById(R.id.toastText));
    }
}