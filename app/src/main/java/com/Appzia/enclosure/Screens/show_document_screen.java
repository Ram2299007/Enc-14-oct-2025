package com.Appzia.enclosure.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView; // Added import for PlayerView

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout; // Added import for LinearLayout
import android.widget.ImageView;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.provider.MediaStore;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivityShowDocumentScreenBinding;

import java.io.File;
import java.util.Objects;

public class show_document_screen extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    ActivityShowDocumentScreenBinding binding;
    Context mContext;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 112; // This variable is declared but not used.
    private long downloadId;
    int iState = 0;
    String themColor;
    ColorStateList tintList;
    ExoPlayer player;
    AppCompatActivity mActivity;
    String viewHolderTypeKey;

    @Override
    protected void onResume() {
        super.onResume();

        // Set status bar to transparent and layout to full screen for a modern look
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        // Apply theme color to UI elements (progress bar and download button)
        try {
            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            // Set tint based on the theme color. This can be simplified by directly applying tintList.
            binding.progressBar.setIndeterminateTintList(tintList);
            binding.downlaod.setBackgroundTintList(tintList);

        } catch (Exception ignored) {
            // Fallback to default theme color if an exception occurs
            tintList = ColorStateList.valueOf(Color.parseColor("#00A3E9"));
            binding.progressBar.setIndeterminateTintList(tintList);
            binding.downlaod.setBackgroundTintList(tintList);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowDocumentScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = binding.getRoot().getContext();
        mActivity = show_document_screen.this;

        // Prevent screenshots and screen recording
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // Apply theme color to menuPoint if it exists
        Constant.getSfFuncion(mContext);
        String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        if (binding.menuPoint != null) {
            binding.menuPoint.setColorFilter(Color.parseColor(themeColor));
        }

        // Conditional menu visibility based on viewHolderTypeKey
        viewHolderTypeKey = getIntent().getStringExtra("viewHolderTypeKey");
        if (viewHolderTypeKey != null && 
            (viewHolderTypeKey.equals(Constant.senderViewHolder) || viewHolderTypeKey.equals(Constant.receiverViewHolder))) {
            if (binding.menu != null) {
                binding.menu.setVisibility(View.VISIBLE);
                binding.menu.setOnClickListener(new View.OnClickListener() {
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
                                saveFileToGallery();
                                Constant.dialogLayoutColor.dismiss();
                            }
                        });
                    }
                });
            }
        } else {
            if (binding.menu != null) {
                binding.menu.setVisibility(View.GONE);
            }
        }

        binding.backarrow34.setOnClickListener(v -> {
         //   Toast.makeText(mContext, "Back clicked", Toast.LENGTH_SHORT).show();
           onBackPressed();
        });



        // Retrieve data passed from the previous activity
        String fileUrl = getIntent().getStringExtra("documentKey");
        String nameKey = getIntent().getStringExtra("nameKey");
        String sizeKey = getIntent().getStringExtra("sizeKey");
        String extensionKey = getIntent().getStringExtra("extensionKey");
        Log.d("TAG007", "nameKey: " + nameKey);
        Log.d("TAG007", "fileUrl: " + fileUrl);
        Log.d("TAG007", "sizeKey: " + sizeKey);
        Log.d("TAG007", "extensionKey: " + extensionKey);

        // Handle file display based on ViewHolder type (sender or receiver)
        if (viewHolderTypeKey != null) {
            if (viewHolderTypeKey.equals(Constant.senderViewHolder)) {
                handleSenderFileDisplay(nameKey, extensionKey);
            } else if (viewHolderTypeKey.equals(Constant.receiverViewHolder)) {
                handleSenderFileDisplay(nameKey, extensionKey);
            }
        }

        // Set document name and size in the UI
        binding.docName.setText(nameKey);
        binding.size.setText(sizeKey);

        // Set click listener for the download button
        binding.downlaod.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onClick(View v) {
                if (iState == 1) {
                    // If file is already downloaded, attempt to open it
                    String currentExtensionKey = getIntent().getStringExtra("extensionKey");
                    String currentNameKey = getIntent().getStringExtra("nameKey");
                    String currentViewHolderTypeKey = getIntent().getStringExtra("viewHolderTypeKey");

                    if (currentViewHolderTypeKey != null) {
                        if (currentViewHolderTypeKey.equals(Constant.senderViewHolder)) {
                            openDownloadedFile(currentNameKey, currentExtensionKey, true);
                        } else if (currentViewHolderTypeKey.equals(Constant.receiverViewHolder)) {
                            openDownloadedFile(currentNameKey, currentExtensionKey, false);
                        }
                    }
                } else if (iState == 0) {
                    // If file is not downloaded, start the download process
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.downlaod.setVisibility(View.GONE);
                    binding.downlaod.setImageResource(R.drawable.done); // This might be redundant as it's hidden.

                    // Register a BroadcastReceiver to receive download completion events
                    // Note: RECEIVER_EXPORTED is required for Android 13+ for receivers not registered in manifest
                    registerReceiver(new DownloadReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED);

                    // Start the download
                    downloadFile(fileUrl, nameKey);
                }
            }
        });
    }

    /**
     * Handles the display logic for files sent by the user (senderViewHolder).
     * Checks if the file exists locally and displays it, or shows the download button.
     *
     * @param nameKey      The name of the file.
     * @param extensionKey The extension of the file.
     */
    @OptIn(markerClass = UnstableApi.class)
    private void handleSenderFileDisplay(String nameKey, String extensionKey) {
        if (extensionKey == null) return;

        String format = extensionKey.toLowerCase();
        File customFolder = getCustomFolder(true); // true for sender

        String exactPath = customFolder.getAbsolutePath();

        if (nameKey.equals(Constant.img) || isImageFormat(format)) {
            if (doesFileExist(exactPath + "/" + nameKey)) {
                displayImage(exactPath + "/" + nameKey);
            } else {
                showDownloadControls();
            }
        } else if (nameKey.equals(Constant.voiceAudio) || isAudioFormat(format)) {
            if (doesFileExist(exactPath + "/" + nameKey)) {
                playMedia(exactPath + "/" + nameKey);
            } else {
                showDownloadControls();
            }
        } else if (nameKey.equals(Constant.video) || isVideoFormat(format)) {

            if (doesFileExist(exactPath + "/" + nameKey)) {
               // playMedia(exactPath + "/" + nameKey);
                Intent intent = new Intent(mContext, show_video_playerScreen.class);
                intent.putExtra("videoUri", exactPath + "/" + nameKey);
                intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                SmoothNavigationHelper.startActivityWithSlideFromRight(show_document_screen.this, intent, true);

            } else {
                showDownloadControls();
            }
        } else {
            // For other document types, check if it exists and allow opening
            if (doesFileExist(exactPath + "/" + nameKey)) {
                iState = 1; // Mark as downloaded
                binding.progressBar.setVisibility(View.GONE);
                binding.downlaod.setVisibility(View.VISIBLE);
                binding.downlaod.setImageResource(R.drawable.done);
                // Hide preview controls as it's a document to be opened externally
                binding.downloadCtrl.setVisibility(View.VISIBLE);
                binding.previewCtrl.setVisibility(View.GONE);
                binding.imageview.setVisibility(View.GONE);
                binding.playerView.setVisibility(View.GONE);
            } else {
                showDownloadControls();
            }
        }
    }

    /**
     * Handles the display logic for files received by the user (receiverViewHolder).
     * Checks if the file exists locally and displays it, or shows the download button.
     *
     * @param nameKey      The name of the file.
     * @param extensionKey The extension of the file.
     */
    private void handleReceiverFileDisplay(String nameKey, String extensionKey) {
        if (extensionKey == null) return;

        String format = extensionKey.toLowerCase();
        File customFolder = getCustomFolder(false); // false for receiver

        String exactPath = customFolder.getAbsolutePath();

        if (nameKey.equals(Constant.img) || isImageFormat(format)) {
            if (doesFileExist(exactPath + "/" + nameKey)) {
                displayImage(exactPath + "/" + nameKey);
            } else {
                showDownloadControls();
            }
        } else if (nameKey.equals(Constant.voiceAudio) || isAudioFormat(format)) {
            if (doesFileExist(exactPath + "/" + nameKey)) {
                playMedia(exactPath + "/" + nameKey);
            } else {
                showDownloadControls();
            }
        } else if (nameKey.equals(Constant.video) || isVideoFormat(format)) {
            if (doesFileExist(exactPath + "/" + nameKey)) {
                playMedia(exactPath + "/" + nameKey);
            } else {
                showDownloadControls();
            }
        } else {
            // For other document types, check if it exists and allow opening
            if (doesFileExist(exactPath + "/" + nameKey)) {
                iState = 1; // Mark as downloaded
                binding.progressBar.setVisibility(View.GONE);
                binding.downlaod.setVisibility(View.VISIBLE);
                binding.downlaod.setImageResource(R.drawable.done);
                // Hide preview controls as it's a document to be opened externally
                binding.downloadCtrl.setVisibility(View.VISIBLE);
                binding.previewCtrl.setVisibility(View.GONE);
                binding.imageview.setVisibility(View.GONE);
                binding.playerView.setVisibility(View.GONE);
            } else {
                showDownloadControls();
            }
        }
    }

    /**
     * Determines the custom folder path based on whether the file is sent or received.
     *
     * @param isSender True if the file is from the sender, false otherwise.
     * @return The File object representing the custom folder.
     */
    private File getCustomFolder(boolean isSender) {
        File customFolder;
        String nameKey = getIntent().getStringExtra("nameKey");
        String extensionKey = getIntent().getStringExtra("extensionKey");
        String format = extensionKey != null ? extensionKey.toLowerCase() : "";

        if (isSender) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
            } else {
                customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents");
            }
        } else { // Receiver
            if (nameKey != null) {
                if (nameKey.equals(Constant.img) || isImageFormat(format)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                    } else {
                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                    }
                } else if (nameKey.equals(Constant.voiceAudio) || isAudioFormat(format)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
                    } else {
                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
                    }
                } else if (nameKey.equals(Constant.video) || isVideoFormat(format)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                    } else {
                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                    } else {
                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                    }
                }
            } else {
                // Default for receiver if nameKey is null or unrecognized
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                } else {
                    customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                }
            }
        }
        if (!customFolder.exists()) {
            customFolder.mkdirs();
        }
        return customFolder;
    }

    /**
     * Checks if the given format string corresponds to a known image file extension.
     *
     * @param format The file extension string (e.g., "jpg", "png").
     * @return True if it's an image format, false otherwise.
     */
    private boolean isImageFormat(String format) {
        return format.equals("jpg") || format.equals("jpeg") || format.equals("png") ||
                format.equals("webp") || format.equals("gif") || format.equals("tiff") ||
                format.equals("psd") || format.equals("heif") || format.equals("svg");
    }

    /**
     * Checks if the given format string corresponds to a known video file extension.
     *
     * @param format The file extension string (e.g., "mp4", "mov").
     * @return True if it's a video format, false otherwise.
     */
    private boolean isVideoFormat(String format) {
        return format.equals("mp4") || format.equals("mov") || format.equals("wmv") ||
                format.equals("flv") || format.equals("mkv") || format.equals("avi") ||
                format.equals("avchd") || format.equals("webm") || format.equals("hevc");
    }

    /**
     * Checks if the given format string corresponds to a known audio file extension.
     *
     * @param format The file extension string (e.g., "mp3", "wav").
     * @return True if it's an audio format, false otherwise.
     */
    private boolean isAudioFormat(String format) {
        return format.equals("flac") || format.equals("ape") || format.equals("wv") ||
                format.equals("tta") || format.equals("alac") || format.equals("m4a") ||
                format.equals("awb") || format.equals("wma") || format.equals("shn") ||
                format.equals("mp3") || format.equals("ogg") || format.equals("opus") ||
                format.equals("mp2") || format.equals("m4b") || format.equals("aac") ||
                format.equals("amr") || format.equals("atrac3") || format.equals("wavpack") ||
                format.equals("wav") || format.equals("aiff") || format.equals("au") ||
                format.equals("raw");
    }

    /**
     * Displays an image using PhotoView.
     *
     * @param filePath The path to the image file.
     */
    private void displayImage(String filePath) {
        iState = 1;
        binding.progressBar.setVisibility(View.GONE);
        binding.downlaod.setVisibility(View.VISIBLE);
        binding.downlaod.setImageResource(R.drawable.done);

        binding.downloadCtrl.setVisibility(View.GONE);
        binding.playerView.setVisibility(View.GONE);
        binding.previewCtrl.setVisibility(View.VISIBLE);
        binding.imageview.setVisibility(View.VISIBLE);
        binding.imageview.setImageURI(Uri.parse(filePath));
        
        // Ensure back arrow stays on top
        binding.backarrow34.bringToFront();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.backarrow34.setElevation(16f);
        }
    }

    /**
     * Plays audio or video using ExoPlayer.
     *
     * @param filePath The path to the media file.
     */
    @OptIn(markerClass = UnstableApi.class)
    private void playMedia(String filePath) {
        File mediaFile = new File(filePath);
        if (!mediaFile.exists()) {
            Log.e("show_document_screen", "Media file does not exist: " + filePath);
            showToast("Media file not found");
            showDownloadControls();
            return;
        }

        iState = 1;
        binding.progressBar.setVisibility(View.GONE);
        binding.downlaod.setVisibility(View.VISIBLE);
        binding.downlaod.setImageResource(R.drawable.done);

        binding.downloadCtrl.setVisibility(View.GONE);
        binding.previewCtrl.setVisibility(View.VISIBLE);
        binding.imageview.setVisibility(View.GONE);
        binding.playerView.setVisibility(View.VISIBLE);

        // Configure PlayerView
        binding.playerView.setControllerHideOnTouch(false); // Disable hide on touch for testing
        binding.playerView.setControllerShowTimeoutMs(3000);
        binding.playerView.setUseController(true);
        binding.playerView.showController();

        if (player == null) {
            player = new ExoPlayer.Builder(mContext).build();
            binding.playerView.setPlayer(player);
        }

        // Add player listener for error handling and state changes
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e("show_document_screen", "Player error: " + error.getMessage());
               // Toast.makeText(mContext, "Playback error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                Log.d("show_document_screen", "Playback state: " + state);
                if (state == Player.STATE_READY) {
                    binding.playerView.showController();
                }
            }
        });

        MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(new File(filePath)));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

        // Custom controller setup
        LinearLayout backArrowLayoutInController = binding.playerView.findViewById(R.id.backarrow34);
        if (backArrowLayoutInController != null) {
            Log.d("show_document_screen", "Back arrow layout found");
            backArrowLayoutInController.setOnClickListener(v -> onBackPressed());
        } else {
            Log.e("show_document_screen", "Back arrow layout not found");
        }

        TextView nameTitleTextViewInController = binding.playerView.findViewById(R.id.nameTitle);
        if (nameTitleTextViewInController != null) {
            Log.d("show_document_screen", "Name title TextView found");
            String nameKey = getIntent().getStringExtra("nameKey");
            nameTitleTextViewInController.setText(nameKey != null ? nameKey : "Video Playback");
        } else {
            Log.e("show_document_screen", "Name title TextView not found");
        }
        
        // Ensure back arrow stays on top
        binding.backarrow34.bringToFront();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.backarrow34.setElevation(16f);
        }
    }

    /**
     * Shows the download controls and hides the preview controls.
     */
    private void showDownloadControls() {
        binding.downloadCtrl.setVisibility(View.VISIBLE);
        binding.previewCtrl.setVisibility(View.GONE);
        
        // Ensure back arrow stays on top
        binding.backarrow34.bringToFront();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.backarrow34.setElevation(16f);
        }
    }

    /**
     * Attempts to open a downloaded file with an appropriate external application.
     *
     * @param nameKey   The name of the file.
     * @param extensionKey The extension of the file.
     * @param isSender True if the file is from the sender, false otherwise.
     */
    private void openDownloadedFile(String nameKey, String extensionKey, boolean isSender) {
        if (extensionKey == null) return;

        File customFolder = getCustomFolder(isSender);
        String exactPath = customFolder.getAbsolutePath();
        String filePath = exactPath + "/" + nameKey;

        try {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri fileUri = FileProvider.getUriForFile(mContext, mContext.getPackageName(), new File(filePath));
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(fileUri.toString()));

            intent.setDataAndType(fileUri, mimeType);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            SmoothNavigationHelper.startActivityWithSlideFromRight(show_document_screen.this, intent);

        } catch (Exception e) {
            Constant.dialogueLayoutForAll(mContext, R.layout.nodocumentfounddialogue);
            TextView view = Constant.dialogLayoutColor.findViewById(R.id.name);
            AppCompatButton btn = Constant.dialogLayoutColor.findViewById(R.id.ok);
            view.setText(nameKey + ". You do not have an app installed on this device to open this type of file.");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.dialogLayoutColor.dismiss();
                }
            });
            Constant.dialogLayoutColor.show();
        }
        
        // Ensure back arrow stays on top
        binding.backarrow34.bringToFront();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.backarrow34.setElevation(16f);
        }
    }


    /**
     * Initiates a file download using DownloadManager.
     *
     * @param url      The URL of the file to download.
     * @param fileName The desired name for the downloaded file.
     */
    private void downloadFile(String url, String fileName) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return; // Request permission and return, download will proceed after permission is granted
        }

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading");

        // Determine destination folder based on sender/receiver and file type
        boolean isSender = Objects.equals(getIntent().getStringExtra("viewHolderTypeKey"), Constant.senderViewHolder);
        File customFolder = getCustomFolder(isSender);

        File destinationFile = new File(customFolder, fileName);
        request.setDestinationUri(Uri.fromFile(destinationFile));

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // Enqueue the download
        downloadId = downloadManager.enqueue(request);
    }

    /**
     * BroadcastReceiver to handle download completion events.
     */
    private class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == downloadId) {
                    showToast("Download Successful");
                    iState = 1; // Mark as downloaded
                    binding.progressBar.setVisibility(View.GONE);
                    binding.downlaod.setVisibility(View.VISIBLE);
                    binding.downlaod.setImageResource(R.drawable.done);

                    // Automatically display/open the downloaded file
                    String nameKey = getIntent().getStringExtra("nameKey");
                    String extensionKey = getIntent().getStringExtra("extensionKey");
                    boolean isSender = Objects.equals(getIntent().getStringExtra("viewHolderTypeKey"), Constant.senderViewHolder);

                    if (nameKey != null && extensionKey != null) {
                        String format = extensionKey.toLowerCase();
                        File customFolder = getCustomFolder(isSender);
                        String exactPath = customFolder.getAbsolutePath();

                        if (nameKey.equals(Constant.img) || isImageFormat(format)) {
                            displayImage(exactPath + "/" + nameKey);
                        } else if (nameKey.equals(Constant.voiceAudio) || isAudioFormat(format) || nameKey.equals(Constant.video) || isVideoFormat(format)) {
                            playMedia(exactPath + "/" + nameKey);
                        } else {

                            openDownloadedFile(nameKey, extensionKey, isSender);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now write to external storage.
                // If a download was pending, you might want to retry it here.
            } else {
                // Permission denied, handle the situation accordingly.
                showToast("Storage permission denied. Cannot download file.");
            }
        }
    }

    /**
     * Checks if a file exists at the given file path.
     *
     * @param filePath The absolute path to the file.
     * @return True if the file exists, false otherwise.
     */
    public boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    @Override
    public void onBackPressed() {
        // Release ExoPlayer resources when navigating back
        if (player != null) {
            player.stop();
            player.release();
            player = null; // Set to null to avoid using a released player
        }
        SmoothNavigationHelper.finishWithSlideToRight(show_document_screen.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Release ExoPlayer resources when the activity is stopped
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release ExoPlayer resources when the activity is destroyed
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        // Unregister the BroadcastReceiver to prevent memory leaks
        try {
            unregisterReceiver(new DownloadReceiver());
        } catch (IllegalArgumentException e) {
            // Receiver was not registered or already unregistered
            Log.e("show_document_screen", "Receiver not registered or already unregistered: " + e.getMessage());
        }
    }

    /**
     * Saves the current file to gallery or documents folder based on file type
     */
    private void saveFileToGallery() {
        String nameKey = getIntent().getStringExtra("nameKey");
        String extensionKey = getIntent().getStringExtra("extensionKey");
        String viewHolderTypeKey = getIntent().getStringExtra("viewHolderTypeKey");
        
        if (nameKey == null || extensionKey == null) {
            showToast("Cannot save file: Missing file information");
            return;
        }
        
        String format = extensionKey.toLowerCase();
        boolean isSender = viewHolderTypeKey != null && viewHolderTypeKey.equals(Constant.senderViewHolder);
        File customFolder = getCustomFolder(isSender);
        String filePath = customFolder.getAbsolutePath() + "/" + nameKey;
        
        File sourceFile = new File(filePath);
        if (!sourceFile.exists()) {
            showToast("File not found locally");
            return;
        }
        
        // Check file type and save accordingly
        if (nameKey.equals(Constant.img) || isImageFormat(format)) {
            saveImageToGallery(sourceFile, nameKey);
        } else if (nameKey.equals(Constant.video) || isVideoFormat(format)) {
            saveVideoToGallery(sourceFile, nameKey);
        } else {
            // Save other documents to Documents folder
            saveDocumentToFolder(sourceFile, nameKey);
        }
    }
    
    /**
     * Saves an image file to the gallery
     */
    private void saveImageToGallery(File sourceFile, String fileName) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            if (bitmap == null) {
                showToast("Failed to load image");
                return;
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = mContext.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Enclosure");
                
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                if (imageUri != null) {
                    OutputStream outputStream = resolver.openOutputStream(imageUri);
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                        showToast("Image saved");
                    } else {
                        showToast("Failed to save image");
                    }
                } else {
                    showToast("Failed to save image");
                }
            } else {
                File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File enclosureDir = new File(picturesDir, "Enclosure");
                if (!enclosureDir.exists()) {
                    enclosureDir.mkdirs();
                }
                
                File destFile = new File(enclosureDir, fileName);
                FileOutputStream outputStream = new FileOutputStream(destFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
                
                // Notify media scanner
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(destFile));
                mContext.sendBroadcast(mediaScanIntent);
                
                showToast("Image saved");
            }
        } catch (IOException e) {
            Log.e("show_document_screen", "Error saving image: " + e.getMessage());
            showToast("Failed to save image");
        }
    }
    
    /**
     * Saves a video file to the gallery
     */
    private void saveVideoToGallery(File sourceFile, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = mContext.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/Enclosure");
                
                Uri videoUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                if (videoUri != null) {
                    OutputStream outputStream = resolver.openOutputStream(videoUri);
                    if (outputStream != null) {
                        copyFile(new FileInputStream(sourceFile), outputStream);
                        outputStream.close();
                        showToast("Video saved");
                    } else {
                        showToast("Failed to save video");
                    }
                } else {
                    showToast("Failed to save video");
                }
            } else {
                File moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                File enclosureDir = new File(moviesDir, "Enclosure");
                if (!enclosureDir.exists()) {
                    enclosureDir.mkdirs();
                }
                
                File destFile = new File(enclosureDir, fileName);
                copyFile(new FileInputStream(sourceFile), new FileOutputStream(destFile));
                
                // Notify media scanner
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(destFile));
                mContext.sendBroadcast(mediaScanIntent);
                
                showToast("Video saved");
            }
        } catch (IOException e) {
            Log.e("show_document_screen", "Error saving video: " + e.getMessage());
            showToast("Failed to save video");
        }
    }
    
    /**
     * Saves a document file to the Documents folder
     */
    private void saveDocumentToFolder(File sourceFile, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = mContext.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Enclosure");
                
                Uri documentUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
                if (documentUri != null) {
                    OutputStream outputStream = resolver.openOutputStream(documentUri);
                    if (outputStream != null) {
                        copyFile(new FileInputStream(sourceFile), outputStream);
                        outputStream.close();
                        showToast("Document saved");
                    } else {
                        showToast("Failed to save document");
                    }
                } else {
                    showToast("Failed to save document");
                }
            } else {
                File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File enclosureDir = new File(documentsDir, "Enclosure");
                if (!enclosureDir.exists()) {
                    enclosureDir.mkdirs();
                }
                
                File destFile = new File(enclosureDir, fileName);
                copyFile(new FileInputStream(sourceFile), new FileOutputStream(destFile));
                
                showToast("Document saved");
            }
        } catch (IOException e) {
            Log.e("show_document_screen", "Error saving document: " + e.getMessage());
            showToast("Failed to save document");
        }
    }
    
    /**
     * Copies data from input stream to output stream
     */
    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }
    
    /**
     * Shows custom toast message
     */
    private void showToast(String message) {
        Constant.showCustomToast(message, findViewById(R.id.includedToast), findViewById(R.id.toastText));
    }
}
