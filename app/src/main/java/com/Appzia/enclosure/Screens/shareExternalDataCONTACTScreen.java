package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.HorizontalImageAdapter;
import com.Appzia.enclosure.Adapter.MainImagePreviewAdapter;
import com.Appzia.enclosure.Adapter.forwardnameAdapter;
import com.Appzia.enclosure.Adapter.shareContactAdapter;
import com.Appzia.enclosure.Model.forwardnameModel;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.messagemodel2;
import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelper;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.Utils.StoragePathHelper;
import com.Appzia.enclosure.databinding.ActivityShareExternalDataContactScreenBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class shareExternalDataCONTACTScreen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityListener {

    ActivityShareExternalDataContactScreenBinding binding;
    shareContactAdapter forwardAdapter;
    ArrayList<forwardnameModel> forwardNameList = new ArrayList<>();
    Context mContext;
    ArrayList<get_user_active_contact_list_Model> get_user_active_contact_forward_list = new ArrayList<>();
    forwardnameAdapter forwardnameAdapter;
    String type;
    Uri globalUri;
    String sharedText;
    String themColor;
    ColorStateList tintList;
    private ConnectivityReceiver connectivityReceiver;

    // मल्टी-सेलेक्शन साठी नवीन व्हेरिएबल्स (Multi-selection variables)
    private String currentCaption = ""; // Single caption for all media items
    private ArrayList<Uri> selectedImageUris = new ArrayList<>();
    private java.util.HashMap<Integer, String> imageCaptions = new java.util.HashMap<>();
    private boolean isUpdatingText = false;

    // Video selection variables
    private ArrayList<Uri> selectedVideoUris = new ArrayList<>();
    private java.util.HashMap<Integer, String> videoCaptions = new java.util.HashMap<>();
    private ArrayList<java.io.File> selectedVideoFiles = new ArrayList<>();
    private boolean isUpdatingVideoText = false;

    // Multi-contact variables
    private ArrayList<ContactInfo> selectedContactInfos = new ArrayList<>();
    private java.util.HashMap<Integer, String> contactCaptions = new java.util.HashMap<>();
    private boolean isUpdatingContactText = false;

    // Mixed media/document variables
    private ArrayList<Uri> selectedDocumentUris = new ArrayList<>();
    private ArrayList<File> selectedDocumentFiles = new ArrayList<>();
    private java.util.HashMap<Integer, String> documentCaptions = new java.util.HashMap<>();
    private boolean isUpdatingDocumentText = false;

    // ContactInfo class - matches chattingScreen.ContactInfo
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

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected) {


            binding.networkLoader.setVisibility(View.GONE);

            try {
                // TODO : - OFFLINE DATA LOAD HERE WHEN ,UNTIL WEBSERVICE 200 STATUS OR ERROR

                get_user_active_contact_forward_list.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_user_active_contact_forward_list = dbHelper.getAllData();

                if (get_user_active_contact_forward_list.size() > 0) {
                    // Toast.makeText(mContext, "not empty", Toast.LENGTH_SHORT).show();
                    setAdapter(get_user_active_contact_forward_list);

                    binding.recyclerview.setVisibility(View.VISIBLE);
                } else {

                }
            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


            Log.d("Network", "connected: " + "chattingRoom");
            try {

                Webservice.get_user_active_chat_list_ShareContact(mContext, Constant.getSF.getString(Constant.UID_KEY, ""), shareExternalDataCONTACTScreen.this, binding.recyclerview);


            } catch (Exception ignored) {
            }


        } else {
            binding.networkLoader.setVisibility(View.VISIBLE);
            try {

                Log.d("Network", "dissconnetced: " + "chattingRoom");
                get_user_active_contact_forward_list.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_user_active_contact_forward_list = dbHelper.getAllData();
                setAdapter(get_user_active_contact_forward_list);

                binding.recyclerview.setVisibility(View.VISIBLE);

            } catch (Exception ignored) {
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO : for only network loader Themes

        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FF6D00"));


                } else if (themColor.equals("#00A3E9")) {

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                } else if (themColor.equals("#7adf2a")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00C853"));

                } else if (themColor.equals("#ec0001")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#ec7500"));

                } else if (themColor.equals("#16f3ff")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00F365"));

                } else if (themColor.equals("#FF8A00")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FFAB00"));

                } else if (themColor.equals("#7F7F7F")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#314E6D"));

                } else if (themColor.equals("#D9B845")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#b0d945"));
                } else if (themColor.equals("#346667")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#729412"));

                } else if (themColor.equals("#9846D9")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#d946d1"));

                } else if (themColor.equals("#A81010")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#D85D01"));

                } else {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));

                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Don't clear selections in onResume to avoid clearing data before forwarding
        Log.d("OnResume", "Activity resumed, keeping current selections");

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
        }else{
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

                // Set dark status bar (black text/icons) for light mode
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareExternalDataContactScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





        mContext = binding.getRoot().getContext();
        Constant.getSfFuncion(mContext);
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        get_user_active_contact_forward_list = new ArrayList<>();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter,mContext.RECEIVER_EXPORTED);



        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        // Single item sharing
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleSingleItemSharing(intent, type);
        }
        // Multi-item sharing (Photos app मधून multiple images select करताना)
        else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            handleMultipleItemSharing(intent, type);
        } else {
            // Fallback: if no valid intent, set default type
            this.type = "UNKNOWN";
        }

        // Setup UI components
        setupUI();
    }


    /**
     * Single item sharing handle करण्यासाठी पद्धत (Method to handle single item sharing)
     */
    private void handleSingleItemSharing(Intent intent, String type) {
        // Clear only image-related data, keep contact selections
        clearImageSelections();

        if (type.startsWith("image/")) {
            this.type = "IMAGE";
            globalUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            // मल्टी-इमेज सपोर्ट साठी (For multi-image support)
            if (globalUri != null) {
                selectedImageUris.add(globalUri);
                imageCaptions.put(0, ""); // पहिल्या इमेजसाठी रिकामे caption
            }
        } else if (type.startsWith("video/")) {
            this.type = "VIDEO";
            globalUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (globalUri != null) {
                ArrayList<Uri> videoUris = new ArrayList<>();
                videoUris.add(globalUri);
                handleVideoUris(videoUris);
            }
        } else if (type.startsWith("audio/")) {
            this.type = "AUDIO";
            globalUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        } else if (type.startsWith("text/x-vcard")) {
            this.type = "CONTACT";
            globalUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

            // Debug: Log all intent extras
            Log.d("ContactSelection", "Intent action: " + intent.getAction());
            Log.d("ContactSelection", "Intent type: " + intent.getType());
            Log.d("ContactSelection", "EXTRA_STREAM (single): " + globalUri);

            // Check if multiple contacts are passed through EXTRA_STREAM as ArrayList
            ArrayList<Uri> contactUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            Log.d("ContactSelection", "EXTRA_STREAM (ArrayList): " + (contactUris != null ? contactUris.size() : "null"));

            // Also check for other possible extras
            String[] extraNames = intent.getExtras().keySet().toArray(new String[0]);
            Log.d("ContactSelection", "Available extras: " + java.util.Arrays.toString(extraNames));

            if (contactUris != null && !contactUris.isEmpty()) {
                // Multiple contacts through ACTION_SEND
                Log.d("ContactSelection", "Multiple contacts detected via ACTION_SEND: " + contactUris.size());
                selectedContactInfos.clear();
                contactCaptions.clear();

                for (int i = 0; i < contactUris.size(); i++) {
                    Uri contactUri = contactUris.get(i);
                    ContactInfo contactInfo = parseContactFromUri(contactUri);
                    if (contactInfo != null && !contactInfo.name.trim().isEmpty()) {
                        selectedContactInfos.add(contactInfo);
                        contactCaptions.put(i, "");
                        Log.d("ContactSelection", "Added contact " + (i+1) + ": " + contactInfo.name);
                    }
                }
                Log.d("ContactSelection", "Total contacts added: " + selectedContactInfos.size());
            } else if (globalUri != null) {
                // Single contact or multiple contacts in vCard
                Log.d("ContactSelection", "Contact(s) detected, parsing...");

                // Parse contact(s) from URI - this method now handles both single and multiple contacts
                ContactInfo contactInfo = parseContactFromUri(globalUri);
                if (contactInfo != null) {
                    Log.d("ContactSelection", "Contact parsing completed. Total contacts: " + selectedContactInfos.size());
                    for (int i = 0; i < selectedContactInfos.size(); i++) {
                        ContactInfo contact = selectedContactInfos.get(i);
                        Log.d("ContactSelection", "Contact " + (i+1) + ": " + contact.name);
                    }
                }
            }
        } else if (type.startsWith("text/plain")) {
            this.type = "TEXT";
            sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        } else {
            this.type = "DOCUMENT";
            globalUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        }
    }

    /**
     * Multiple items sharing handle करण्यासाठी पद्धत (Method to handle multiple items sharing)
     * Photos app मधून multiple images select करताना ही पद्धत call होते
     */
    private void handleMultipleItemSharing(Intent intent, String type) {
        Log.d("MultiShare", "Handling multiple items sharing, type: " + type);
        Log.d("MultiShare", "Intent action: " + intent.getAction());
        Log.d("MultiShare", "Intent type: " + intent.getType());

        // Clear only image-related data, keep contact selections
        clearImageSelections();

        // Multiple URIs मिळवा (Get multiple URIs)
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);

        if (imageUris != null && !imageUris.isEmpty()) {
            Log.d("MultiShare", "Received " + imageUris.size() + " images");

            if (type.startsWith("image/")) {
                this.type = "IMAGE"; // Class variable update करा (Update class variable)
                // सर्व images selectedImageUris मध्ये add करा (Add all images to selectedImageUris)
                selectedImageUris.clear(); // पूर्वीचे clear करा (Clear previous)
                selectedImageUris.addAll(imageUris);

                // प्रत्येक image साठी caption initialize करा (Initialize caption for each image)
                imageCaptions.clear();
                for (int i = 0; i < selectedImageUris.size(); i++) {
                    imageCaptions.put(i, "");
                }

                Log.d("MultiShare", "Added " + selectedImageUris.size() + " images to selectedImageUris");
            } else if (type.startsWith("video/")) {
                this.type = "VIDEO"; // Class variable update करा (Update class variable)
                // Video handling logic
                handleVideoUris(imageUris);
            } else if (type.startsWith("text/x-vcard")) {
                this.type = "CONTACT"; // Class variable update करा (Update class variable)
                Log.d("MultiContact", "Multiple contacts detected via ACTION_SEND_MULTIPLE: " + imageUris.size());

                // Multiple contacts handling
                selectedContactInfos.clear();
                contactCaptions.clear();

                for (int i = 0; i < imageUris.size(); i++) {
                    Uri contactUri = imageUris.get(i);
                    Log.d("MultiContact", "Parsing contact URI " + (i+1) + ": " + contactUri);
                    ContactInfo contactInfo = parseContactFromUri(contactUri);
                    if (contactInfo != null && !contactInfo.name.trim().isEmpty()) {
                        selectedContactInfos.add(contactInfo);
                        contactCaptions.put(i, "");
                        Log.d("MultiContact", "Added contact " + (i+1) + ": " + contactInfo.name);
                    } else {
                        Log.d("MultiContact", "Failed to parse contact " + (i+1) + " or empty name");
                    }
                }

                Log.d("MultiContact", "Total contacts parsed: " + selectedContactInfos.size());
            } else {
                // Check for mixed media types (jpg + mp4, jpg + mp4 + apk, etc.)
                if (hasMixedMediaTypes(imageUris)) {
                    this.type = "MIXED_MEDIA"; // Mixed media types detected
                    Log.d("MixedMedia", "Mixed media types detected, setting up document preview");
                    setupMixedMediaDocumentPreview(imageUris);
                } else {
                    this.type = "MULTIPLE_MEDIA"; // Class variable update करा (Update class variable)
                    selectedImageUris.clear();
                    selectedImageUris.addAll(imageUris);
                }
            }
        } else {
            Log.d("MultiShare", "No URIs received in multiple sharing");
        }
    }

    /**
     * Check if the URIs contain mixed media types (jpg + mp4, jpg + mp4 + apk, etc.)
     */
    private boolean hasMixedMediaTypes(ArrayList<Uri> uris) {
        if (uris == null || uris.size() < 2) {
            return false;
        }

        java.util.Set<String> mediaTypes = new java.util.HashSet<>();

        for (Uri uri : uris) {
            String mimeType = getContentResolver().getType(uri);
            if (mimeType != null) {
                if (mimeType.startsWith("image/")) {
                    mediaTypes.add("image");
                } else if (mimeType.startsWith("video/")) {
                    mediaTypes.add("video");
                } else if (mimeType.startsWith("audio/")) {
                    mediaTypes.add("audio");
                } else if (mimeType.equals("application/vnd.android.package-archive")) {
                    mediaTypes.add("apk");
                } else if (mimeType.startsWith("application/")) {
                    mediaTypes.add("document");
                } else {
                    mediaTypes.add("other");
                }
            }
        }

        Log.d("MixedMedia", "Detected media types: " + mediaTypes);
        return mediaTypes.size() > 1; // Mixed if more than one type
    }

    /**
     * Setup mixed media document preview
     */
    private void setupMixedMediaDocumentPreview(ArrayList<Uri> uris) {
        Log.d("MixedMedia", "Setting up mixed media document preview with " + uris.size() + " items");

        // Clear existing document data
        selectedDocumentUris.clear();
        selectedDocumentFiles.clear();
        documentCaptions.clear();

        // Add all URIs as documents
        selectedDocumentUris.addAll(uris);

        // Initialize captions for all documents
        for (int i = 0; i < selectedDocumentUris.size(); i++) {
            documentCaptions.put(i, "");
        }

        // Process files for document preview
        processDocumentFiles();

        Log.d("MixedMedia", "Mixed media document preview setup complete with " + selectedDocumentUris.size() + " documents");
    }

    /**
     * Process document files for preview
     */
    private void processDocumentFiles() {
        selectedDocumentFiles.clear();

        // Create documents directory in app's external files directory
        File documentsDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
        Log.d("DocumentStorage", "Documents directory path: " + documentsDir.getAbsolutePath());
        Log.d("DocumentStorage", "Documents directory exists: " + documentsDir.exists());

        if (!documentsDir.exists()) {
            boolean created = documentsDir.mkdirs();
            Log.d("DocumentStorage", "Directory creation result: " + created);
            Log.d("DocumentStorage", "Directory exists after creation: " + documentsDir.exists());
        }

        for (int i = 0; i < selectedDocumentUris.size(); i++) {
            Uri uri = selectedDocumentUris.get(i);
            try {
                // Create a file for the document in Documents directory
                String fileName = "document_" + System.currentTimeMillis() + "_" + i;
                String mimeType = getContentResolver().getType(uri);

                if (mimeType != null) {
                    String extension = getFileExtensionFromMimeType(mimeType);
                    fileName += extension;
                }

                File documentFile = new File(documentsDir, fileName);

                // Copy URI content to file
                java.io.InputStream inputStream = getContentResolver().openInputStream(uri);
                java.io.FileOutputStream outputStream = new java.io.FileOutputStream(documentFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                inputStream.close();
                outputStream.close();

                selectedDocumentFiles.add(documentFile);
                Log.d("DocumentStorage", "Document saved to: " + documentFile.getAbsolutePath());
                Log.d("DocumentStorage", "Document exists: " + documentFile.exists());
                Log.d("DocumentStorage", "Document size: " + documentFile.length() + " bytes");
                Log.d("MixedMedia", "Processed document " + (i+1) + ": " + fileName);

            } catch (Exception e) {
                Log.e("MixedMedia", "Error processing document " + (i+1) + ": " + e.getMessage());
            }
        }
    }

    /**
     * Get file extension from MIME type
     */
    private String getFileExtensionFromMimeType(String mimeType) {
        switch (mimeType) {
            case "image/jpeg":
            case "image/jpg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "video/mp4":
                return ".mp4";
            case "video/3gpp":
                return ".3gp";
            case "audio/mpeg":
                return ".mp3";
            case "audio/wav":
                return ".wav";
            case "application/vnd.android.package-archive":
                return ".apk";
            case "application/pdf":
                return ".pdf";
            case "application/zip":
                return ".zip";
            default:
                return ".bin";
        }
    }

    /**
     * Handle video URIs for both single and multiple video sharing
     */
    private void handleVideoUris(ArrayList<Uri> videoUris) {
        Log.d("VideoHandler", "Handling " + videoUris.size() + " videos");

        // Clear previous video data
        selectedVideoUris.clear();
        selectedVideoFiles.clear();
        videoCaptions.clear();

        // Add video URIs
        selectedVideoUris.addAll(videoUris);
        Log.d("VideoHandler", "selectedVideoUris populated with " + selectedVideoUris.size() + " videos");

        // Process each video file
        for (int i = 0; i < videoUris.size(); i++) {
            Uri videoUri = videoUris.get(i);
            try {
                // Create video file
                String fileName = "video_" + System.currentTimeMillis() + "_" + i + ".mp4";
                java.io.File videoFile = processVideoFile(videoUri, fileName);

                if (videoFile != null && videoFile.exists()) {
                    selectedVideoFiles.add(videoFile);
                    Log.d("VideoHandler", "Processed video " + (i + 1) + ": " + videoFile.getName());
                } else {
                    Log.e("VideoHandler", "Failed to process video " + (i + 1));
                    // Add null file to maintain index alignment
                    selectedVideoFiles.add(null);
                }
            } catch (Exception e) {
                Log.e("VideoHandler", "Error processing video " + (i + 1) + ": " + e.getMessage());
                selectedVideoFiles.add(null);
            }
        }

        // Initialize captions for all videos
        for (int i = 0; i < selectedVideoUris.size(); i++) {
            videoCaptions.put(i, "");
        }

        Log.d("VideoHandler", "Video handling completed. URIs: " + selectedVideoUris.size() + ", Files: " + selectedVideoFiles.size());
    }

    /**
     * Process video file from URI
     */
    private java.io.File processVideoFile(Uri videoUri, String fileName) {
        try {
            java.io.File videoDir = new java.io.File(getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }

            java.io.File videoFile = new java.io.File(videoDir, fileName);

            // Copy video from URI to file
            java.io.InputStream videoStream = getContentResolver().openInputStream(videoUri);
            if (videoStream != null) {
                java.io.FileOutputStream fos = new java.io.FileOutputStream(videoFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = videoStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                videoStream.close();
                fos.close();

                Log.d("VideoProcessor", "Video processed: " + videoFile.getAbsolutePath());
                return videoFile;
            }
        } catch (Exception e) {
            Log.e("VideoProcessor", "Error processing video: " + e.getMessage(), e);
        }

        return null;
    }

    private void setupUI() {
        // Preview will be shown in shareExternalDataScreen, not here
        // This ensures the flow: select image → contact page → preview → MainActivityOld/ChattingScreen

        binding.searchview.addTextChangedListener(new TextWatcher() {
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

        // Preview will be shown in shareExternalDataScreen, not here
        // This ensures the flow: select image/video → contact page → preview → MainActivityOld/ChattingScreen

        binding.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ForwardClick", "=== FORWARD BUTTON CLICKED ===");
                Log.d("ForwardClick", "Current state before forwarding:");
                Log.d("ForwardClick", "type: " + type);
                Log.d("ForwardClick", "selectedImageUris.size(): " + selectedImageUris.size());
                Log.d("ForwardClick", "globalUri: " + globalUri);
                Log.d("ForwardClick", "sharedText: " + sharedText);
                Log.d("ForwardClick", "forwardNameList.size(): " + forwardNameList.size());

                Intent intent = new Intent(mContext, shareExternalDataScreen.class);
                intent.putParcelableArrayListExtra("forwardNameList", forwardNameList);

                // Debug logging
                Log.d("ForwardDebug", "=== FORWARD BUTTON DEBUG ===");
                Log.d("ForwardDebug", "type: " + type);
                Log.d("ForwardDebug", "selectedImageUris.size(): " + selectedImageUris.size());
                Log.d("ForwardDebug", "globalUri: " + globalUri);
                Log.d("ForwardDebug", "sharedText: " + sharedText);
                Log.d("ForwardDebug", "forwardNameList.size(): " + forwardNameList.size());

                // Ensure type is not null
                if (type == null) {
                    type = "UNKNOWN";
                }
                intent.putExtra("typeKey", type);

                if (type.equals("TEXT")) {
                    intent.putExtra("sharedTextKey", sharedText);
                    Log.d("ForwardDebug", "Forwarding TEXT data");
                } else if ((type.equals("IMAGE") || (type != null && type.startsWith("image/"))) && !selectedImageUris.isEmpty()) {
                    // Multi-image sharing साठी (For multi-image sharing)
                    Log.d("ForwardMultiImage", "Multi-image detected, showing preview instead of forwarding");
                    setupMultiImagePreview();
                    return; // Show preview instead of navigating
                } else if ((type.equals("VIDEO") || (type != null && type.startsWith("video/"))) && !selectedVideoUris.isEmpty()) {
                    // Multi-video sharing साठी (For multi-video sharing)
                    Log.d("ForwardMultiVideo", "Multi-video detected, showing preview instead of forwarding");
                    setupMultiVideoPreview();
                    return; // Show preview instead of navigating
                } else if ((type.equals("CONTACT") || (type != null && type.startsWith("text/x-vcard"))) && !selectedContactInfos.isEmpty()) {
                    // Multi-contact sharing साठी (For multi-contact sharing)
                    Log.d("ForwardMultiContact", "Multi-contact detected, showing preview instead of forwarding");
                    setupContactPreview();
                    return; // Show preview instead of navigating
                } else if (type.equals("MIXED_MEDIA") && !selectedDocumentUris.isEmpty()) {
                    // Mixed media sharing साठी (For mixed media sharing)
                    Log.d("ForwardMixedMedia", "Mixed media detected, showing document preview instead of forwarding");
                    setupDocumentPreview();
                    return; // Show preview instead of navigating
                } else if (globalUri != null) {
                    // Single item sharing साठी (For single item sharing)
                    intent.putExtra("URI_EXTRA", globalUri.toString());
                    Log.d("ForwardDebug", "Forwarding single item data");
                } else {
                    Log.e("ForwardDebug", "No valid data found - type: " + type + ", selectedImageUris: " + selectedImageUris.size() + ", globalUri: " + globalUri);
                    Toast.makeText(mContext, "No data to share", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(intent);

                // Clear selections after forwarding
                // This ensures clean state for next time
                Log.d("ForwardClick", "Clearing selections after forwarding");
                clearAllSelections();

            }
        });
    }

    public void setAdapter(ArrayList<get_user_active_contact_list_Model> get_user_active_contact_forward_list) {
        // Filter out blocked users
        ArrayList<get_user_active_contact_list_Model> filteredList = new ArrayList<>();
        for (get_user_active_contact_list_Model model : get_user_active_contact_forward_list) {
            if (!model.isBlock()) { // ✅ Exclude blocked users
                filteredList.add(model);
            }
        }

        this.get_user_active_contact_forward_list = filteredList;

        // Set up the adapter with the filtered list
        forwardAdapter = new shareContactAdapter(mContext, filteredList, binding.dx, shareExternalDataCONTACTScreen.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        binding.recyclerview.setLayoutManager(layoutManager);
        binding.recyclerview.setAdapter(forwardAdapter);
        forwardAdapter.notifyDataSetChanged();
    }


    public void setforwardNameAdapter(ArrayList<forwardnameModel> forwardNameList) {
        this.forwardNameList = forwardNameList;
        forwardnameAdapter = new forwardnameAdapter(mContext, forwardNameList, binding.namerecyclerview);
        binding.namerecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        binding.namerecyclerview.setAdapter(forwardnameAdapter);

    }

    /**
     * Clear only image-related selections, keep contact selections
     */
    public void clearImageSelections() {
        Log.d("ClearImageSelections", "=== CLEARING IMAGE SELECTIONS ONLY ===");
        Log.d("ClearImageSelections", "Before clear - selectedImageUris.size(): " + selectedImageUris.size());
        Log.d("ClearImageSelections", "Before clear - type: " + type);
        Log.d("ClearImageSelections", "Before clear - globalUri: " + globalUri);
        Log.d("ClearImageSelections", "Keeping contact selections - forwardNameList.size(): " + forwardNameList.size());

        // Clear only image and video selections
        selectedImageUris.clear();
        imageCaptions.clear();
        selectedVideoUris.clear();
        videoCaptions.clear();
        selectedVideoFiles.clear();
        globalUri = null;

        // Reset type
        type = null;

        Log.d("ClearImageSelections", "After clear - selectedImageUris.size(): " + selectedImageUris.size());
        Log.d("ClearImageSelections", "After clear - type: " + type);
        Log.d("ClearImageSelections", "Image selections cleared, contact selections preserved");
    }

    /**
     * Clear all selections (images, users, and checkboxes)
     */
    public void clearAllSelections() {
        Log.d("ClearSelections", "=== CLEARING ALL SELECTIONS ===");
        Log.d("ClearSelections", "Before clear - selectedImageUris.size(): " + selectedImageUris.size());
        Log.d("ClearSelections", "Before clear - forwardNameList.size(): " + forwardNameList.size());
        Log.d("ClearSelections", "Before clear - type: " + type);
        Log.d("ClearSelections", "Before clear - globalUri: " + globalUri);

        // Clear image selections
        selectedImageUris.clear();
        imageCaptions.clear();

        // Clear video selections
        selectedVideoUris.clear();
        videoCaptions.clear();
        selectedVideoFiles.clear();

        // Clear contact selections
        selectedContactInfos.clear();
        contactCaptions.clear();

        globalUri = null;

        // Clear user selections
        forwardNameList.clear();

        // Clear adapters and refresh UI
        if (forwardnameAdapter != null) {
            forwardnameAdapter.notifyDataSetChanged();
        }

        if (forwardAdapter != null) {
            forwardAdapter.resetAllCheckboxes();
            forwardAdapter.notifyDataSetChanged();
        }

        // Reset type
        type = null;

        Log.d("ClearSelections", "After clear - selectedImageUris.size(): " + selectedImageUris.size());
        Log.d("ClearSelections", "After clear - forwardNameList.size(): " + forwardNameList.size());
        Log.d("ClearSelections", "After clear - type: " + type);
        Log.d("ClearSelections", "All selections cleared successfully");
    }

    public void filteredList(String newText) {
        ArrayList<get_user_active_contact_list_Model> filteredList = new ArrayList<>();

        for (get_user_active_contact_list_Model list : get_user_active_contact_forward_list) {
            if (list.getFull_name().toLowerCase().contains(newText.toLowerCase())) {

                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {
            //   Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            forwardAdapter.searchFilteredData(filteredList);
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

        // डायलॉग लेआउट सेटअप करा (Setup dialog layout)
        Constant.dialogLayoutFullScreen = new android.app.Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Constant.dialogLayoutFullScreen.setContentView(R.layout.dialogue_full_screen_dialogue);
        Constant.dialogLayoutFullScreen.setCanceledOnTouchOutside(true);

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
        ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        if (mainImagePreview != null) {
            mainImagePreview.setVisibility(View.VISIBLE);

            // मुख्य प्रिव्यूसाठी adapter सेटअप करा (Setup adapter for main preview)
            com.Appzia.enclosure.Adapter.MainImagePreviewAdapter mainAdapter = new com.Appzia.enclosure.Adapter.MainImagePreviewAdapter(mContext, selectedImageUris);
            mainImagePreview.setAdapter(mainAdapter);

            // पेज चेंज callback सेटअप करा (Setup page change callback)
            mainImagePreview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
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
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);

        // Caption change listener सेटअप करा (Setup caption change listener)
        if (messageBoxMy != null) {
            messageBoxMy.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d("TextWatcher", "beforeTextChanged: '" + s + "'");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("TextWatcher", "onTextChanged: '" + s + "'");
                }

                @Override
                public void afterTextChanged(Editable s) {
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
                sendMultiImages();
            });
        }
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

    /**
     * Show multi-image preview dialog
     */
    private void showMultiImagePreviewDialog() {
        Constant.dialogLayoutFullScreen = new android.app.Dialog(shareExternalDataCONTACTScreen.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Constant.dialogLayoutFullScreen.setContentView(R.layout.dialogue_full_screen_dialogue);
        android.view.Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            window.setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setVisibility(View.VISIBLE);
            imageCounter.setText("1 / " + selectedImageUris.size());
        }
        setupHorizontalImagePreview();
        Constant.dialogLayoutFullScreen.show();
    }

    /**
     * Setup horizontal image preview gallery
     */
    private void setupHorizontalImagePreview() {
        Log.d("HorizontalImageGallery", "Setting up horizontal image gallery with " + selectedImageUris.size() + " images");

        // Main image preview
        androidx.viewpager2.widget.ViewPager2 mainImagePreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainImagePreview);
        if (mainImagePreview != null) {
            mainImagePreview.setVisibility(View.VISIBLE);
            com.Appzia.enclosure.Adapter.MainImagePreviewAdapter mainAdapter = new com.Appzia.enclosure.Adapter.MainImagePreviewAdapter(mContext, selectedImageUris);
            mainImagePreview.setAdapter(mainAdapter);
            mainImagePreview.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
                    if (imageCounter != null) {
                        imageCounter.setText((position + 1) + " / " + selectedImageUris.size());
                    }
                    android.widget.EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        Log.d("ImagePageChange", "Switched to image position " + position + ", caption: '" + currentCaption + "'");
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

        // Horizontal RecyclerView for thumbnails


        // Caption editing
        android.widget.EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null) {
            messageBoxMy.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(android.text.Editable s) {
                    // Save the caption as user types
                    currentCaption = s.toString();
                    Log.d("CaptionWatcher", "Caption updated: " + currentCaption);
                }
            });
        }

        // Send button
        android.widget.LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        if (sendGrp != null) {
            sendGrp.setOnClickListener(v -> {
                Log.d("SendMultiImage", "Sending " + selectedImageUris.size() + " images");
                // TODO: Implement image send logic
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }
    }

    /**
     * Show multi-video preview dialog
     */
    private void showMultiVideoPreviewDialog() {
        // Create and show video preview dialog
        Constant.dialogLayoutFullScreen = new android.app.Dialog(shareExternalDataCONTACTScreen.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Constant.dialogLayoutFullScreen.setContentView(R.layout.dialogue_video_preview);

        // Make dialog full screen
        android.view.Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            window.setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // Setup video counter visibility
        TextView videoCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.videoCounter);
        if (videoCounter != null) {
            videoCounter.setVisibility(View.VISIBLE);
            videoCounter.setText("1 / " + selectedVideoUris.size());
        }

        // Set initial caption in dialogue messageBoxMy
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        Log.d("VideoPreviewDialog", "Initial currentCaption: '" + currentCaption + "'");
        Log.d("VideoPreviewDialog", "messageBoxMy found: " + (messageBoxMy != null));
        if (messageBoxMy != null && currentCaption != null && !currentCaption.isEmpty()) {
            messageBoxMy.setText(currentCaption);
            Log.d("VideoPreviewDialog", "Caption set in dialogue: '" + messageBoxMy.getText().toString() + "'");
        } else {
            Log.d("VideoPreviewDialog", "Caption not set - messageBoxMy null: " + (messageBoxMy == null) + ", currentCaption empty: " + (currentCaption == null || currentCaption.isEmpty()));
        }

        // Setup video preview
        setupHorizontalVideoPreview();

        // डायलॉग दाखवा (Show dialog)
        Constant.dialogLayoutFullScreen.show();
    }

    /**
     * Setup horizontal video preview gallery
     */
    private void setupHorizontalVideoPreview() {
        Log.d("HorizontalVideoGallery", "Setting up horizontal video gallery with " + selectedVideoUris.size() + " videos");

        // Setup main video preview ViewPager2
        ViewPager2 mainVideoPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.mainVideoPreview);
        if (mainVideoPreview != null) {
            mainVideoPreview.setVisibility(View.VISIBLE);

            // Setup adapter for main preview
            com.Appzia.enclosure.Adapters.MainVideoPreviewAdapter mainAdapter = new com.Appzia.enclosure.Adapters.MainVideoPreviewAdapter(mContext, selectedVideoUris);
            mainVideoPreview.setAdapter(mainAdapter);

            // Setup page change callback to sync with horizontal RecyclerView
            mainVideoPreview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    // Update counter
                    TextView videoCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.videoCounter);
                    if (videoCounter != null) {
                        videoCounter.setText((position + 1) + " / " + selectedVideoUris.size());
                    }

                    // Update caption EditText
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
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
                EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
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
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null) {
            messageBoxMy.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Save the caption as user types
                    currentCaption = s.toString();
                    Log.d("CaptionWatcher", "Caption updated: " + currentCaption);
                }
            });
        }

        // Setup send button
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        if (sendGrp != null) {
            Log.d("SendButtonSetup", "Setting up send button for video preview");
            sendGrp.setOnClickListener(v -> {
                Log.d("SendMultiVideo", "=== SEND BUTTON CLICKED ===");
                Log.d("SendMultiVideo", "Sending " + selectedVideoUris.size() + " videos");
                Log.d("SendMultiVideo", "SelectedVideoUris: " + selectedVideoUris.toString());
                Log.d("SendMultiVideo", "ForwardNameList size: " + (forwardNameList != null ? forwardNameList.size() : "null"));
                
                // Get final caption from messageBoxMy before sending
                EditText captionEditText = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                if (captionEditText != null) {
                    String finalCaption = captionEditText.getText().toString().trim();
                    currentCaption = finalCaption;
                    Log.d("SendMultiVideo", "Final caption from messageBoxMy: '" + finalCaption + "'");
                } else {
                    Log.e("SendMultiVideo", "❌ messageBoxMy not found in send button click");
                }
                
                //    Toast.makeText(mContext, "Sending " + selectedVideoUris.size() + " videos...", Toast.LENGTH_SHORT).show();
                sendMultiVideos();
            });
        } else {
            Log.e("SendButtonSetup", "❌ Send button not found in video preview dialog");
        }
    }

    /**
     * Send multiple videos using UploadChatHelper
     */
    private void sendMultiVideos() {
        Log.d("SendMultiVideos", "=== STARTING MULTI-VIDEO SEND PROCESS ===");
        Log.d("SendMultiVideos", "Total videos to send: " + selectedVideoUris.size());
        Log.d("SendMultiVideos", "SelectedVideoUris: " + selectedVideoUris.toString());
        Log.d("SendMultiVideos", "VideoCaptions: " + videoCaptions.toString());
        Log.d("SendMultiVideos", "Context: " + (mContext != null ? "Available" : "NULL"));
        Log.d("SendMultiVideos", "Activity: " + (this != null ? "Available" : "NULL"));

        try {
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String uniqDate = Constant.getCurrentDate();
            Log.d("SendMultiVideos", "senderId: " + senderId);
            Log.d("SendMultiVideos", "uniqDate: " + uniqDate);
            Log.d("SendMultiVideos", "Total selected users: " + (forwardNameList != null ? forwardNameList.size() : 0));

            if (forwardNameList == null || forwardNameList.isEmpty()) {
                Log.e("SendMultiVideos", "❌ No recipients selected");
                Toast.makeText(this, "Error: No recipients selected", Toast.LENGTH_SHORT).show();
                return;
            }

            com.google.firebase.database.DatabaseReference database = com.google.firebase.database.FirebaseDatabase.getInstance().getReference();

            for (int userIndex = 0; userIndex < forwardNameList.size(); userIndex++) {
                forwardnameModel currentUser = forwardNameList.get(userIndex);
                String receiverUid = currentUser.getFriend_id();
                String userFTokenKey = currentUser.getF_token();
                Log.d("SendMultiVideos", "User: " + currentUser.getName());
                Log.d("SendMultiVideos", "receiverUid: " + receiverUid);
                Log.d("SendMultiVideos", "userFTokenKey: " + userFTokenKey);

                for (int i = 0; i < selectedVideoUris.size(); i++) {
                    Uri videoUri = selectedVideoUris.get(i);
                    // Use currentCaption instead of videoCaptions map for all videos
                    String caption = currentCaption;
                    if (caption == null) {
                        caption = "";
                    }
                    Log.d("SendMultiVideos", "Using currentCaption for all videos: '" + caption + "'");
                    String videoModelId = database.push().getKey();
                    assert videoModelId != null;
                    Log.d("SendMultiVideos", "Video URI: " + videoUri);
                    Log.d("SendMultiVideos", "Caption: '" + caption + "'");
                    Log.d("SendMultiVideos", "Generated modelId: " + videoModelId);

                    String fileName = "video_" + System.currentTimeMillis() + "_" + userIndex + "_" + i + "_" + videoModelId + ".mp4";
                    Log.d("SendMultiVideos", "Generated fileName: " + fileName);
                    java.io.File videoFile = processVideoFile(videoUri, fileName);
                    Log.d("SendMultiVideos", "Video file processing result: " + (videoFile != null ? "SUCCESS" : "FAILED"));

                    if (videoFile != null && videoFile.exists()) {
                        Log.d("SendMultiVideos", "Video file created successfully: " + videoFile.getAbsolutePath());
                        Log.d("SendMultiVideos", "File size: " + videoFile.length() + " bytes");

                        // Create video thumbnail
                        File savedThumbnail = null;
                        String fileThumbName = null;
                        Log.d("SendMultiVideos", "=== CREATING THUMBNAIL FOR VIDEO " + (i + 1) + " ===");
                        Log.d("SendMultiVideos", "Video file path: " + videoFile.getAbsolutePath());
                        Log.d("SendMultiVideos", "Video file exists: " + videoFile.exists());
                        Log.d("SendMultiVideos", "Video file size: " + videoFile.length());
                        
                        try {
                            android.graphics.Bitmap thumbnail = android.media.ThumbnailUtils.createVideoThumbnail(
                                    videoFile.getAbsolutePath(),
                                    android.provider.MediaStore.Video.Thumbnails.MINI_KIND
                            );
                            Log.d("SendMultiVideos", "Thumbnail creation result: " + (thumbnail != null ? "SUCCESS" : "FAILED"));
                            
                            if (thumbnail != null) {
                                String thumbnailName = "thumb_" + videoModelId + ".png";
                                Log.d("SendMultiVideos", "Generated thumbnail name: " + thumbnailName);

                                // Save thumbnail to local storage
                                try {
                                    File thumbnailDir = new File(getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), "Enclosure/Media/Thumbnail");
                                    if (!thumbnailDir.exists()) {
                                        thumbnailDir.mkdirs();
                                        Log.d("SendMultiVideos", "Created thumbnail directory: " + thumbnailDir.getAbsolutePath());
                                    }
                                    File localThumbnailFile = new File(thumbnailDir, thumbnailName);

                                    // Save bitmap to local storage
                                    java.io.FileOutputStream fos = new java.io.FileOutputStream(localThumbnailFile);
                                    thumbnail.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fos);
                                    fos.close();

                                    savedThumbnail = localThumbnailFile;
                                    fileThumbName = thumbnailName;
                                    Log.d("SendMultiVideos", "✅ Created thumbnail for video " + (i + 1) + ": " + savedThumbnail.getAbsolutePath());
                                    Log.d("SendMultiVideos", "✅ Thumbnail file exists: " + savedThumbnail.exists());
                                    Log.d("SendMultiVideos", "✅ Thumbnail file size: " + savedThumbnail.length());
                                } catch (Exception e) {
                                    Log.e("SendMultiVideos", "❌ Error saving thumbnail to local storage: " + e.getMessage(), e);
                                    // Fallback to cache directory
                                    savedThumbnail = com.Appzia.enclosure.Utils.FileUtils.saveBitmapToFile(mContext, thumbnail, thumbnailName);
                                    fileThumbName = thumbnailName;
                                    Log.d("SendMultiVideos", "Fallback thumbnail created: " + (savedThumbnail != null ? savedThumbnail.getAbsolutePath() : "null"));
                                }
                            } else {
                                Log.e("SendMultiVideos", "❌ Failed to create thumbnail for video " + (i + 1));
                            }
                        } catch (Exception e) {
                            Log.e("SendMultiVideos", "❌ Error creating thumbnail for video " + (i + 1) + ": " + e.getMessage(), e);
                        }
                        
                        Log.d("SendMultiVideos", "Final fileThumbName: '" + fileThumbName + "'");

                        // Create messageModel
                        Log.d("SendMultiVideos", "Creating messageModel with fileThumbName: '" + fileThumbName + "'");
                        messageModel model = createVideoMessageModel(videoFile, caption, i, senderId, videoModelId, receiverUid, uniqDate, fileThumbName);
                        Log.d("SendMultiVideos", "MessageModel creation result: " + (model != null ? "SUCCESS" : "FAILED"));

                        if (model != null) {
                            Log.d("SendMultiVideos", "Model dataType: " + model.getDataType());
                            Log.d("SendMultiVideos", "Model fileName: " + model.getFileName());
                            Log.d("SendMultiVideos", "Model caption: " + model.getCaption());
                            Log.d("SendMultiVideos", "Model modelId: " + model.getModelId());

                            // Insert message into pending table before upload
                            try {
                                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                                dbHelper.insertPendingMessage(model);
                                Log.d("SendMultiVideos", "✅ Message inserted into pending table: " + model.getModelId());
                            } catch (Exception e) {
                                Log.e("SendMultiVideos", "❌ Error inserting pending message: " + e.getMessage(), e);
                            }

                            // Use UploadChatHelper
                            Log.d("SendMultiVideos", "Creating UploadChatHelper...");
                            try {
                                UploadChatHelper uploadHelper = new UploadChatHelper(mContext, videoFile, null, senderId, userFTokenKey);
                                Log.d("SendMultiVideos", "UploadChatHelper created with videoFile: " + (videoFile != null ? videoFile.getAbsolutePath() : "null"));
                                Log.d("SendMultiVideos", "UploadChatHelper created successfully");

                                Log.d("SendMultiVideos", "Calling uploadContent...");
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
                            } catch (Exception uploadException) {
                                Log.e("SendMultiVideos", "❌ UploadChatHelper error for video " + (i + 1) + ": " + uploadException.getMessage(), uploadException);
                            }
                        } else {
                            Log.e("SendMultiVideos", "❌ Failed to create messageModel for video " + (i + 1) + " for user " + (userIndex + 1));
                        }
                    } else {
                        Log.e("SendMultiVideos", "❌ Failed to process video file " + (i + 1) + " for user " + (userIndex + 1) + " - file is null or doesn't exist");
                        if (videoFile == null) {
                            Log.e("SendMultiVideos", "❌ Video file is NULL");
                        } else {
                            Log.e("SendMultiVideos", "❌ Video file doesn't exist: " + videoFile.getAbsolutePath());
                        }
                    }
                }
                Log.d("SendMultiVideos", "✅ Completed sending " + selectedVideoUris.size() + " videos to user " + (userIndex + 1) + ": " + currentUser.getName());
            }

            Log.d("SendMultiVideos", "=== UPLOAD PROCESS COMPLETED ===");
            Log.d("SendMultiVideos", "Total users: " + forwardNameList.size());
            Log.d("SendMultiVideos", "Total videos per user: " + selectedVideoUris.size());
            Log.d("SendMultiVideos", "Total videos sent: " + (forwardNameList.size() * selectedVideoUris.size()));

            // Dismiss dialog
            if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                Log.d("DialogDismiss", "✅ Dismissing dialog after sending videos");
                Constant.dialogLayoutFullScreen.dismiss();
                Log.d("DialogDismiss", "✅ Dialog dismissed successfully");
            } else {
                Log.d("DialogDismiss", "❌ Dialog is null or not showing");
            }

            // Add a small delay to allow upload process to start
            Log.d("SendMultiVideos", "Waiting 2 seconds before navigation to allow upload to start...");
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                Log.e("SendMultiVideos", "Sleep interrupted: " + e.getMessage());
            }

            // Navigate based on user count
            Log.d("SendMultiVideos", "=== NAVIGATION PROCESS ===");
            int listcount = forwardNameList.size();
            Log.d("SendMultiVideos", "Number of users: " + listcount);

            if (listcount == 1) {
                forwardnameModel model1 = forwardNameList.get(0);
                Log.d("SendMultiVideos", "Navigating to chattingScreen for single user: " + model1.getName());
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
                intent.putExtra("messageType", "VIDEO");

                Log.d("SendMultiVideos", "Launching chattingScreen with extras:");
                Log.d("SendMultiVideos", "fromShareExternalData: " + intent.getBooleanExtra("fromShareExternalData", false));
                Log.d("SendMultiVideos", "messageType: " + intent.getStringExtra("messageType"));

                mContext.startActivity(intent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            } else {
                Log.d("SendMultiVideos", "Navigating to MainActivityOld for multiple users: " + listcount);
                Intent intent = new Intent(mContext, MainActivityOld.class);
                startActivity(intent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }

            Log.d("SendMultiVideos", "=== MULTI-VIDEO SEND PROCESS COMPLETED ===");
        } catch (Exception e) {
            Log.e("SendMultiVideos", "Error sending multi-videos: " + e.getMessage());
            Toast.makeText(this, "Error sending videos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create video message model
     */
    private messageModel createVideoMessageModel(java.io.File videoFile, String caption, int index, String senderId, String modelId, String receiverUid, String uniqDate, String fileNameThumbnail) {
        try {
            Log.d("CreateVideoMessageModel", "=== CREATING MESSAGE MODEL FOR VIDEO " + (index + 1) + " ===");
            Log.d("CreateVideoMessageModel", "videoFile: " + videoFile.getAbsolutePath());
            Log.d("CreateVideoMessageModel", "caption: '" + caption + "' (length: " + (caption != null ? caption.length() : "null") + ")");
            Log.d("CreateVideoMessageModel", "fileNameThumbnail: '" + fileNameThumbnail + "' (length: " + (fileNameThumbnail != null ? fileNameThumbnail.length() : "null") + ")");
            Log.d("CreateVideoMessageModel", "senderId: " + senderId);
            Log.d("CreateVideoMessageModel", "modelId: " + modelId);
            Log.d("CreateVideoMessageModel", "receiverUid: " + receiverUid);
            Log.d("CreateVideoMessageModel", "uniqDate: " + uniqDate);

            String currentDateTimeString = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(new java.util.Date());
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

            Log.d("CreateVideoMessageModel", "✅ Video messageModel created successfully");
            Log.d("CreateVideoMessageModel", "Model details:");
            Log.d("CreateVideoMessageModel", "  - modelId: " + model.getModelId());
            Log.d("CreateVideoMessageModel", "  - senderId: " + model.getUid());
            Log.d("CreateVideoMessageModel", "  - receiverUid: " + model.getReceiverUid());
            Log.d("CreateVideoMessageModel", "  - fileName: " + model.getFileName());
            Log.d("CreateVideoMessageModel", "  - fileNameThumbnail: " + model.getFileNameThumbnail());
            Log.d("CreateVideoMessageModel", "  - imageWidth: " + model.getImageWidth());
            Log.d("CreateVideoMessageModel", "  - imageHeight: " + model.getImageHeight());
            Log.d("CreateVideoMessageModel", "  - aspectRatio: " + model.getAspectRatio());
            Log.d("CreateVideoMessageModel", "  - caption: " + model.getCaption());
            Log.d("CreateVideoMessageModel", "  - dataType: " + model.getDataType());
            Log.d("CreateVideoMessageModel", "  - uniqDate: " + model.getCurrentDate());
            Log.d("CreateVideoMessageModel", "  - time: " + model.getTime());

            return model;
        } catch (Exception e) {
            Log.e("CreateVideoMessageModel", "❌ Error creating video messageModel: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get file extension from URI
     */
    private String getFileExtension(Uri uri) {
        try {
            android.content.ContentResolver cR = getContentResolver();
            android.webkit.MimeTypeMap mime = android.webkit.MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
        } catch (Exception e) {
            Log.e("GetFileExtension", "Error getting file extension: " + e.getMessage());
            return "mp4"; // Default to mp4 for videos
        }
    }

    /**
     * Setup contact preview similar to chattingScreen.java
     */
    private void setupContactPreview() {
        Log.d("ContactPreview", "Setting up contact preview with " + selectedContactInfos.size() + " contacts");

        if (selectedContactInfos.isEmpty()) {
            Log.d("ContactPreview", "No contacts selected, returning");
            return;
        }

        // Create and show contact preview dialog
        Constant.dialogLayoutFullScreen = new android.app.Dialog(shareExternalDataCONTACTScreen.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Constant.dialogLayoutFullScreen.setContentView(R.layout.dialogue_full_screen_dialogue);

        // Make dialog full screen
        android.view.Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            window.setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // Setup contact counter visibility
        TextView contactCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (contactCounter != null) {
            contactCounter.setVisibility(View.VISIBLE);
            contactCounter.setText("1 / " + selectedContactInfos.size());
        }

        // Setup contact preview
        setupHorizontalContactPreview();

        // Show dialog
        Constant.dialogLayoutFullScreen.show();
    }

    /**
     * Setup horizontal contact preview gallery
     */
    private void setupHorizontalContactPreview() {
        Log.d("HorizontalContactGallery", "Setting up horizontal contact gallery with " + selectedContactInfos.size() + " contacts");

        // Hide image, video, and document views
        ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        if (singleImageView != null) {
            singleImageView.setVisibility(View.GONE);
        }

        ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
        if (viewPager2 != null) {
            viewPager2.setVisibility(View.GONE);
        }

        // Show contact container for proper contact preview
        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
        if (contactContainer != null) {
            contactContainer.setVisibility(View.VISIBLE);
            Log.d("HorizontalContactGallery", "contactContainer set to VISIBLE");
        }

        // Setup main contact preview ViewPager2 (use contactViewPager for contacts)
        ViewPager2 mainContactPreview = Constant.dialogLayoutFullScreen.findViewById(R.id.contactViewPager);
        Log.d("HorizontalContactGallery", "contactViewPager found: " + (mainContactPreview != null ? "YES" : "NO"));
        if (mainContactPreview != null) {
            mainContactPreview.setVisibility(View.VISIBLE);
            Log.d("HorizontalContactGallery", "contactViewPager set to VISIBLE");



            // Show the contact counter
            TextView contactCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.contactCounter);
            if (contactCounter != null) {
                contactCounter.setVisibility(View.VISIBLE);
                contactCounter.setText("1 / " + selectedContactInfos.size());
                Log.d("HorizontalContactGallery", "contactCounter set to VISIBLE");
            }

            // Show the existing left arrow button (arrowback2)
            LinearLayout arrowback2 = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
            if (arrowback2 != null) {
                arrowback2.setVisibility(View.VISIBLE);
                arrowback2.setOnClickListener(v -> {
                    Log.d("ContactPreview", "Left arrow clicked, dismissing dialog");
                    Constant.dialogLayoutFullScreen.dismiss();
                });
                Log.d("HorizontalContactGallery", "arrowback2 set to VISIBLE");
            }

            // Setup adapter for main preview - using ContactPreviewAdapter
            // Convert our ContactInfo to chattingScreen.ContactInfo
            ArrayList<com.Appzia.enclosure.Screens.chattingScreen.ContactInfo> convertedContacts = new ArrayList<>();
            for (ContactInfo contact : selectedContactInfos) {
                Log.d("ContactConversion", "Converting contact: " + contact.name + ", Phone: " + contact.phoneNumber + ", Email: " + contact.email);
                convertedContacts.add(new com.Appzia.enclosure.Screens.chattingScreen.ContactInfo(contact.name, contact.phoneNumber, contact.email));
            }
            Log.d("ContactConversion", "Converted " + convertedContacts.size() + " contacts for adapter");

            com.Appzia.enclosure.Adapter.ContactPreviewAdapter mainAdapter = new com.Appzia.enclosure.Adapter.ContactPreviewAdapter(mContext, convertedContacts);
            mainContactPreview.setAdapter(mainAdapter);
            Log.d("HorizontalContactGallery", "ContactPreviewAdapter set with " + selectedContactInfos.size() + " contacts");

            // Force adapter to notify data set changed
            mainAdapter.notifyDataSetChanged();

            // Force a layout pass to ensure ViewPager2 is properly displayed
            mainContactPreview.requestLayout();
            mainContactPreview.invalidate();

            // Ensure ViewPager2 is properly configured for sliding
            mainContactPreview.setOffscreenPageLimit(1);
            mainContactPreview.setUserInputEnabled(true);

            // Setup page change callback to sync with horizontal RecyclerView
            mainContactPreview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    // Update horizontal RecyclerView selection


                    // Update counter
                    TextView contactCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.contactCounter);
                    if (contactCounter != null) {
                        contactCounter.setText((position + 1) + " / " + selectedContactInfos.size());
                        contactCounter.setVisibility(View.VISIBLE); // Ensure it's always visible
                    }

                    // Update caption EditText with current contact's caption
                    EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
                    if (messageBoxMy != null) {
                        Log.d("ContactPageChange", "Switched to position " + position + ", caption: '" + currentCaption + "'");

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
        }



        // Setup caption functionality
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null) {
            messageBoxMy.setVisibility(View.VISIBLE);
            messageBoxMy.setHint("Add a caption...");

            // Setup TextWatcher for caption
            messageBoxMy.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!isUpdatingContactText) {
                        // Get current position
                        if (mainContactPreview != null) {
                            int currentPosition = mainContactPreview.getCurrentItem();
                            contactCaptions.put(currentPosition, s.toString());
                            Log.d("ContactCaption", "Saved caption for position " + currentPosition + ": '" + s.toString() + "'");
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        // Setup send button
        LinearLayout sendGrpLyt = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrpLyt);
        if (sendGrpLyt != null) {
            sendGrpLyt.setVisibility(View.VISIBLE);
            sendGrpLyt.setOnClickListener(v -> {
                Log.d("SendMultiContact", "=== SEND BUTTON CLICKED ===");
                Log.d("SendMultiContact", "Sending " + selectedContactInfos.size() + " contacts");
                Log.d("SendMultiContact", "ForwardNameList size: " + (forwardNameList != null ? forwardNameList.size() : "null"));
                sendMultiContacts();
            });
        } else {
            // Fallback to sendGrp if sendGrpLyt not found
            LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
            if (sendGrp != null) {
                Log.d("SendButtonSetup", "Setting up send button for contact preview (fallback)");
                sendGrp.setOnClickListener(v -> {
                    Log.d("SendMultiContact", "=== SEND BUTTON CLICKED ===");
                    Log.d("SendMultiContact", "Sending " + selectedContactInfos.size() + " contacts");
                    Log.d("SendMultiContact", "ForwardNameList size: " + (forwardNameList != null ? forwardNameList.size() : "null"));
                    sendMultiContacts();
                });
            } else {
                Log.e("SendButtonSetup", "❌ Send button not found in contact preview dialog");
            }
        }

        // Setup back arrow
        LinearLayout backarrow = Constant.dialogLayoutFullScreen.findViewById(R.id.backarrow);
        if (backarrow != null) {
            backarrow.setVisibility(View.VISIBLE);
            backarrow.setOnClickListener(v -> {
                // Clear selections when going back
                selectedContactInfos.clear();
                contactCaptions.clear();
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }
    }

    /**
     * Setup document preview for mixed media
     */
    private void setupDocumentPreview() {
        Log.d("DocumentPreview", "Setting up document preview with " + selectedDocumentUris.size() + " documents");

        if (selectedDocumentUris.isEmpty()) {
            Log.d("DocumentPreview", "No documents selected, returning");
            return;
        }

        Constant.dialogLayoutFullScreen = new android.app.Dialog(shareExternalDataCONTACTScreen.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Constant.dialogLayoutFullScreen.setContentView(R.layout.dialogue_full_screen_dialogue);

        android.view.Window window = Constant.dialogLayoutFullScreen.getWindow();
        if (window != null) {
            window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            window.setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // Show document counter
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setVisibility(View.VISIBLE);
            imageCounter.setText("1 / " + selectedDocumentUris.size());
            imageCounter.bringToFront();
            imageCounter.invalidate();
            Log.d("DocumentCounter", "Document counter set to: 1 / " + selectedDocumentUris.size());
        } else {
            Log.e("DocumentCounter", "imageCounter is NULL in setupDocumentPreview");
        }

        setupDocumentPreviewContent();
        Constant.dialogLayoutFullScreen.show();
    }

    /**
     * Setup document preview content
     */
    private void setupDocumentPreviewContent() {
        Log.d("DocumentPreview", "Setting up document preview content");

        // Hide image, video, and contact views
        ImageView singleImageView = Constant.dialogLayoutFullScreen.findViewById(R.id.image);
        if (singleImageView != null) {
            singleImageView.setVisibility(View.GONE);
        }

        android.view.View videoPlayerView = Constant.dialogLayoutFullScreen.findViewById(R.id.video);
        if (videoPlayerView != null) {
            videoPlayerView.setVisibility(View.GONE);
        }

        ViewPager2 viewPager2 = Constant.dialogLayoutFullScreen.findViewById(R.id.viewPager2);
        if (viewPager2 != null) {
            viewPager2.setVisibility(View.GONE);
        }

        LinearLayout contactContainer = Constant.dialogLayoutFullScreen.findViewById(R.id.contactContainer);
        if (contactContainer != null) {
            contactContainer.setVisibility(View.GONE);
        }

        // Show document container
        LinearLayout downloadCtrl = Constant.dialogLayoutFullScreen.findViewById(R.id.downloadCtrl);
        if (downloadCtrl != null) {
            downloadCtrl.setVisibility(View.VISIBLE);
            Log.d("DocumentPreview", "downloadCtrl set to VISIBLE");
        }

        // Setup document ViewPager2 for sliding
        setupDocumentViewPager();

        // Hide navigation arrows - use only ViewPager2 sliding
        View prevButton = Constant.dialogLayoutFullScreen.findViewById(R.id.prevButton);
        View nextButton = Constant.dialogLayoutFullScreen.findViewById(R.id.nextButton);
        if (prevButton != null) {
            prevButton.setVisibility(View.GONE);
        }
        if (nextButton != null) {
            nextButton.setVisibility(View.GONE);
        }

        // Setup back button
        LinearLayout arrowback2 = Constant.dialogLayoutFullScreen.findViewById(R.id.arrowback2);
        if (arrowback2 != null) {
            arrowback2.setVisibility(View.VISIBLE);
            arrowback2.setOnClickListener(v -> {
                Log.d("DocumentPreview", "Back arrow clicked, dismissing dialog");
                Constant.dialogLayoutFullScreen.dismiss();
            });
        }

        // Setup send button
        LinearLayout sendGrp = Constant.dialogLayoutFullScreen.findViewById(R.id.sendGrp);
        if (sendGrp != null) {
            sendGrp.setVisibility(View.VISIBLE);
            sendGrp.setOnClickListener(v -> {
                Log.d("SendMixedMedia", "=== SEND BUTTON CLICKED ===");
                sendMixedMediaDocuments();
            });
        }

        // Setup caption functionality
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null) {
            messageBoxMy.setVisibility(View.VISIBLE);
            messageBoxMy.setHint("Add Caption");

            // Set initial caption without triggering text change listener
            isUpdatingDocumentText = true;
            messageBoxMy.setText(documentCaptions.get(0) != null ? documentCaptions.get(0) : "");
            isUpdatingDocumentText = false;
        }
    }





    /**
     * Setup document ViewPager2 for sliding
     */
    private void setupDocumentViewPager() {
        Log.d("DocumentViewPager", "Setting up document ViewPager2 for sliding");

        // Find ViewPager2 in downloadCtrl
        ViewPager2 documentViewPager = Constant.dialogLayoutFullScreen.findViewById(R.id.documentViewPager);
        if (documentViewPager != null) {
            documentViewPager.setVisibility(View.VISIBLE);

            // Create adapter for documents
            DocumentViewPagerAdapter adapter = new DocumentViewPagerAdapter(selectedDocumentFiles);
            documentViewPager.setAdapter(adapter);

            // Setup page change listener to update counter and caption
            documentViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    updateDocumentCounter(position);
                    updateDocumentCaption(position);
                }
            });

            // Setup caption text change listener
            setupDocumentCaptionListener();

            Log.d("DocumentViewPager", "Document ViewPager2 setup complete with " + selectedDocumentFiles.size() + " documents");
        } else {
            Log.e("DocumentViewPager", "Document ViewPager2 not found in downloadCtrl");
        }
    }

    /**
     * Update document counter
     */
    private void updateDocumentCounter(int position) {
        TextView imageCounter = Constant.dialogLayoutFullScreen.findViewById(R.id.imageCounter);
        if (imageCounter != null) {
            imageCounter.setText((position + 1) + " / " + selectedDocumentUris.size());
            imageCounter.setVisibility(View.VISIBLE);
            imageCounter.bringToFront();
            imageCounter.invalidate();
            Log.d("DocumentCounter", "Updated document counter to: " + (position + 1) + " / " + selectedDocumentUris.size());
        } else {
            Log.e("DocumentCounter", "imageCounter is NULL in updateDocumentCounter");
        }
    }

    /**
     * Update document caption for current position
     */
    private void updateDocumentCaption(int position) {
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null && position < documentCaptions.size()) {
            String caption = documentCaptions.get(position) != null ? documentCaptions.get(position) : "";

            // Set caption without triggering text change listener
            isUpdatingDocumentText = true;
            messageBoxMy.setText(caption);
            isUpdatingDocumentText = false;

            Log.d("DocumentCaption", "Updated caption for position " + position + ": " + caption);
        } else {
            Log.e("DocumentCaption", "messageBoxMy is NULL or position out of bounds in updateDocumentCaption");
        }
    }

    /**
     * Setup document caption text change listener
     */
    private void setupDocumentCaptionListener() {
        EditText messageBoxMy = Constant.dialogLayoutFullScreen.findViewById(R.id.messageBoxMy);
        if (messageBoxMy != null) {
            // Get current ViewPager2 position
            ViewPager2 documentViewPager = Constant.dialogLayoutFullScreen.findViewById(R.id.documentViewPager);
            if (documentViewPager != null) {
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
                        // Save the caption as user types
                        currentCaption = s.toString();
                        Log.d("CaptionWatcher", "Caption updated: " + currentCaption);
                    }
                });
                Log.d("DocumentCaption", "Caption text change listener setup complete");
            }
        } else {
            Log.e("DocumentCaption", "messageBoxMy is NULL in setupDocumentCaptionListener");
        }
    }

    /**
     * Document ViewPager2 Adapter
     */
    private class DocumentViewPagerAdapter extends RecyclerView.Adapter<DocumentViewPagerAdapter.DocumentViewHolder> {
        private ArrayList<File> documentFiles;

        public DocumentViewPagerAdapter(ArrayList<File> documentFiles) {
            this.documentFiles = documentFiles;
        }

        @Override
        public DocumentViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_document_preview, parent, false);
            return new DocumentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DocumentViewHolder holder, int position) {
            if (position < documentFiles.size()) {
                File file = documentFiles.get(position);
                holder.bind(file);
            }
        }

        @Override
        public int getItemCount() {
            return documentFiles.size();
        }

        class DocumentViewHolder extends RecyclerView.ViewHolder {
            private ImageView documentIcon;
            private TextView documentName;
            private TextView documentSize;

            public DocumentViewHolder(android.view.View itemView) {
                super(itemView);
                documentIcon = itemView.findViewById(R.id.documentIcon);
                documentName = itemView.findViewById(R.id.documentName);
                documentSize = itemView.findViewById(R.id.documentSize);
            }

            public void bind(File file) {
                if (documentName != null) {
                    documentName.setText(file.getName());
                }
                if (documentSize != null) {
                    documentSize.setText(formatFileSize(file.length()));
                }
                if (documentIcon != null) {
                    // Set appropriate icon based on file type
                    setDocumentIcon(file.getName());
                }
            }

            private void setDocumentIcon(String fileName) {
                // Always use document_24 icon for all file types
                documentIcon.setImageResource(R.drawable.document_24);
            }

            private String getFileExtensionFromFileName(String fileName) {
                int lastDotIndex = fileName.lastIndexOf('.');
                if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
                    return fileName.substring(lastDotIndex);
                }
                return "";
            }
        }
    }

    /**
     * Setup document information display
     */
    private void setupDocumentInfoDisplay() {
        Log.d("DocumentInfo", "Setting up document info display");

        TextView docName = Constant.dialogLayoutFullScreen.findViewById(R.id.docName);
        TextView size = Constant.dialogLayoutFullScreen.findViewById(R.id.size);

        if (docName != null && size != null && !selectedDocumentFiles.isEmpty()) {
            File firstFile = selectedDocumentFiles.get(0);
            String fileName = firstFile.getName();
            long fileSize = firstFile.length();

            docName.setText(fileName);
            size.setText(formatFileSize(fileSize));

            Log.d("DocumentInfo", "Document name: " + fileName + ", Size: " + formatFileSize(fileSize));
        }
    }

    /**
     * Format file size
     */
    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", size / Math.pow(1024, exp), pre);
    }

    /**
     * Send mixed media documents
     */
    private void sendMixedMediaDocuments() {
        Log.d("SendMixedMedia", "=== SENDING MIXED MEDIA DOCUMENTS ===");
        Log.d("SendMixedMedia", "Total documents to send: " + selectedDocumentUris.size());
        Log.d("SendMixedMedia", "Selected users: " + forwardNameList.size());

        if (selectedDocumentUris.isEmpty()) {
            Toast.makeText(this, "No documents to share", Toast.LENGTH_SHORT).show();
            return;
        }

        if (forwardNameList.isEmpty()) {
            Toast.makeText(this, "No users selected", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            for (int userIndex = 0; userIndex < forwardNameList.size(); userIndex++) {
                forwardnameModel user = forwardNameList.get(userIndex);
                Log.d("SendMixedMedia", "Processing user " + (userIndex + 1) + ": " + user.getName());

                for (int i = 0; i < selectedDocumentUris.size(); i++) {
                    Uri documentUri = selectedDocumentUris.get(i);
                    File documentFile = i < selectedDocumentFiles.size() ? selectedDocumentFiles.get(i) : null;

                    if (documentFile != null && documentFile.exists()) {
                        String caption = documentCaptions.get(i) != null ? documentCaptions.get(i) : "";

                        // Create message model for document
                        messageModel messageModel = createDocumentMessageModel(documentFile, caption, user.getFriend_id(), i);

                        // Insert message into pending table before upload
                        try {
                            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                            dbHelper.insertPendingMessage(messageModel);
                            Log.d("SendMultiDocuments", "✅ Document message inserted into pending table: " + messageModel.getModelId());
                        } catch (Exception e) {
                            Log.e("SendMultiDocuments", "❌ Error inserting pending document message: " + e.getMessage(), e);
                        }

                        // Upload document using UploadChatHelper
                        UploadChatHelper uploadHelper = new UploadChatHelper(
                                mContext,
                                documentFile,
                                documentFile,
                                messageModel.getUid(),  // Use actual UID, not modelId
                                messageModel.getModelId()
                        );

                        uploadHelper.uploadContent(
                                Constant.doc, // uploadType
                                documentUri, // uri
                                caption, // captionText
                                messageModel.getModelId(), // modelId
                                null, // savedThumbnail
                                null, // fileThumbName
                                documentFile.getName(), // fileName
                                null, // contactName
                                null, // contactPhone
                                null, // audioTime
                                null, // audioName
                                getFileExtension(documentUri), // extension
                                user.getFriend_id(), // receiverUid
                                "", // replyCrtPostion
                                "", // replyKey
                                "", // replyOldData
                                "", // replyType
                                "", // replytextData
                                Constant.doc, // dataType
                                documentFile.getName(), // fileName
                                "", // forwaredKey
                                "", // imageWidthDp
                                "", // imageHeightDp
                                ""  // aspectRatio
                        );

                        Log.d("SendMixedMedia", "Document " + (i + 1) + " sent to user " + user.getName());
                    }
                }
            }

            // Show success message
            // Toast.makeText(this, "Documents sent successfully!", Toast.LENGTH_SHORT).show();

            // Dismiss dialog first
            if (Constant.dialogLayoutFullScreen != null) {
                Constant.dialogLayoutFullScreen.dismiss();
            }

            // Add delay before navigation to ensure upload completes
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                // Navigate based on number of users
                if (forwardNameList.size() == 1) {
                    // Single user - go to chatting screen
                    String receiverId = forwardNameList.get(0).getFriend_id();
                    String receiverName = forwardNameList.get(0).getName();
                    Log.d("Navigation", "Navigating to chattingScreen - ReceiverID: " + receiverId + ", ReceiverName: " + receiverName);

                    Intent intent = new Intent(mContext, chattingScreen.class);
                    intent.putExtra("friendUidKey", receiverId);
                    intent.putExtra("name", receiverName);
                    intent.putExtra("profilepic", "");
                    startActivity(intent);
                } else {
                    // Multiple users - go to main activity
                    Intent intent = new Intent(mContext, MainActivityOld.class);
                    startActivity(intent);
                }

                // Finish current activity
                runOnUiThread(() -> finish());
            }, 0); // 2 second delay

        } catch (Exception e) {
            Log.e("SendMixedMedia", "Error sending mixed media documents: " + e.getMessage());
            Toast.makeText(this, "Error sending documents: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create message model for document
     */
    private messageModel createDocumentMessageModel(File documentFile, String caption, String receiverId, int documentIndex) {
        String senderUid = Constant.getSF.getString(Constant.UID_KEY, "");
        Log.d("DocumentModel", "Creating document model - SenderUID: " + senderUid + ", ReceiverUID: " + receiverId);

        messageModel model = new messageModel();
        model.setMessage(caption);
        model.setUid(senderUid);
        model.setReceiverUid(receiverId);
        model.setDataType(Constant.doc);
        model.setTime(String.valueOf(System.currentTimeMillis()));
        // Ensure unique modelId by adding document index
        model.setModelId(senderUid + System.currentTimeMillis() + "_" + documentIndex);

        Log.d("DocumentModel", "Created model - UID: " + model.getUid() + ", ReceiverUID: " + model.getReceiverUid() + ", ModelID: " + model.getModelId());
        return model;
    }


    /**
     * Send multiple contacts using UploadChatHelper
     */
    private void sendMultiContacts() {
        Log.d("SendMultiContact", "=== STARTING MULTI-CONTACT SEND PROCESS ===");
        Log.d("SendMultiContact", "Total contacts to send: " + selectedContactInfos.size());
        Log.d("SendMultiContact", "SelectedContactInfos: " + selectedContactInfos.toString());
        Log.d("SendMultiContact", "ContactCaptions: " + contactCaptions.toString());
        Log.d("SendMultiContact", "Context: " + (mContext != null ? "Available" : "NULL"));
        Log.d("SendMultiContact", "Activity: " + (this != null ? "Available" : "NULL"));

        try {
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String uniqDate = Constant.getCurrentDate();
            Log.d("SendMultiContact", "senderId: " + senderId);
            Log.d("SendMultiContact", "uniqDate: " + uniqDate);
            Log.d("SendMultiContact", "Total selected users: " + (forwardNameList != null ? forwardNameList.size() : 0));

            if (forwardNameList == null || forwardNameList.isEmpty()) {
                Log.e("SendMultiContact", "❌ No recipients selected");
                Toast.makeText(this, "Error: No recipients selected", Toast.LENGTH_SHORT).show();
                return;
            }

            com.google.firebase.database.DatabaseReference database = com.google.firebase.database.FirebaseDatabase.getInstance().getReference();

            for (int userIndex = 0; userIndex < forwardNameList.size(); userIndex++) {
                forwardnameModel currentUser = forwardNameList.get(userIndex);
                String receiverUid = currentUser.getFriend_id();
                String userFTokenKey = currentUser.getF_token();
                Log.d("SendMultiContact", "User: " + currentUser.getName());
                Log.d("SendMultiContact", "receiverUid: " + receiverUid);
                Log.d("SendMultiContact", "userFTokenKey: " + userFTokenKey);

                for (int i = 0; i < selectedContactInfos.size(); i++) {
                    ContactInfo contactInfo = selectedContactInfos.get(i);
                    String caption = contactCaptions.get(i);
                    if (caption == null) {
                        caption = "";
                    }
                    String contactModelId = database.push().getKey();
                    assert contactModelId != null;
                    Log.d("SendMultiContact", "Contact: " + contactInfo.name);
                    Log.d("SendMultiContact", "Caption: '" + caption + "'");
                    Log.d("SendMultiContact", "Generated modelId: " + contactModelId);

                    // Create messageModel
                    messageModel model = createContactMessageModel(contactInfo, caption, i, senderId, contactModelId, receiverUid, uniqDate);
                    Log.d("SendMultiContact", "MessageModel creation result: " + (model != null ? "SUCCESS" : "FAILED"));

                    if (model != null) {
                        Log.d("SendMultiContact", "Model dataType: " + model.getDataType());
                        Log.d("SendMultiContact", "Model fileName: " + model.getFileName());
                        Log.d("SendMultiContact", "Model caption: " + model.getCaption());
                        Log.d("SendMultiContact", "Model modelId: " + model.getModelId());

                        // Insert message into pending table before upload
                        try {
                            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                            dbHelper.insertPendingMessage(model);
                            Log.d("SendMultiContact", "✅ Contact message inserted into pending table: " + model.getModelId());
                        } catch (Exception e) {
                            Log.e("SendMultiContact", "❌ Error inserting pending contact message: " + e.getMessage(), e);
                        }

                        // Use UploadChatHelper
                        Log.d("SendMultiContact", "Creating UploadChatHelper...");
                        try {
                            UploadChatHelper uploadHelper = new UploadChatHelper(mContext, null, null, senderId, userFTokenKey);
                            Log.d("SendMultiContact", "UploadChatHelper created successfully");

                            Log.d("SendMultiContact", "Calling uploadContent...");
                            uploadHelper.uploadContent(
                                    Constant.contact, // uploadType
                                    null, // uri
                                    caption, // captionText
                                    contactModelId, // modelId - unique for each contact
                                    null, // savedThumbnail
                                    null, // fileThumbName
                                    contactInfo.name + ".vcf", // fileName
                                    contactInfo.name, // contactName
                                    contactInfo.phoneNumber, // contactPhone
                                    null, // audioTime
                                    null, // audioName
                                    "vcf", // extension
                                    receiverUid, // receiverUid
                                    "", // replyCrtPostion
                                    "", // replyKey
                                    "", // replyOldData
                                    "", // replyType
                                    "", // replytextData
                                    Constant.contact, // dataType
                                    contactInfo.name + ".vcf", // fileName
                                    "", // forwaredKey
                                    "", // imageWidthDp
                                    "", // imageHeightDp
                                    ""  // aspectRatio
                            );
                            Log.d("SendMultiContact", "✅ Successfully called uploadContent for contact " + (i + 1) + " for user " + (userIndex + 1) + " with modelId: " + contactModelId);
                            Log.d("SendMultiContact", "UploadChatHelper.uploadContent() completed for contact " + (i + 1));
                        } catch (Exception uploadException) {
                            Log.e("SendMultiContact", "❌ UploadChatHelper error for contact " + (i + 1) + ": " + uploadException.getMessage(), uploadException);
                        }
                    } else {
                        Log.e("SendMultiContact", "❌ Failed to create messageModel for contact " + (i + 1) + " for user " + (userIndex + 1));
                    }
                }
                Log.d("SendMultiContact", "✅ Completed sending " + selectedContactInfos.size() + " contacts to user " + (userIndex + 1) + ": " + currentUser.getName());
            }

            Log.d("SendMultiContact", "=== UPLOAD PROCESS COMPLETED ===");
            Log.d("SendMultiContact", "Total users: " + forwardNameList.size());
            Log.d("SendMultiContact", "Total contacts per user: " + selectedContactInfos.size());
            Log.d("SendMultiContact", "Total contacts sent: " + (forwardNameList.size() * selectedContactInfos.size()));

            // Dismiss dialog
            if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                Log.d("DialogDismiss", "✅ Dismissing dialog after sending contacts");
                Constant.dialogLayoutFullScreen.dismiss();
                Log.d("DialogDismiss", "✅ Dialog dismissed successfully");
            } else {
                Log.d("DialogDismiss", "❌ Dialog is null or not showing");
            }

            // Navigate based on user count
            Log.d("SendMultiContact", "=== NAVIGATION PROCESS ===");
            int listcount = forwardNameList.size();
            Log.d("SendMultiContact", "Number of users: " + listcount);

            if (listcount == 1) {
                forwardnameModel model1 = forwardNameList.get(0);
                Log.d("SendMultiContact", "Navigating to chattingScreen for single user: " + model1.getName());
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
                intent.putExtra("messageType", "CONTACT");

                Log.d("SendMultiContact", "Launching chattingScreen with extras:");
                Log.d("SendMultiContact", "fromShareExternalData: " + intent.getBooleanExtra("fromShareExternalData", false));
                Log.d("SendMultiContact", "messageType: " + intent.getStringExtra("messageType"));

                mContext.startActivity(intent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            } else {
                Log.d("SendMultiContact", "Navigating to MainActivityOld for multiple users: " + listcount);
                Intent intent = new Intent(mContext, MainActivityOld.class);
                startActivity(intent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }

            Log.d("SendMultiContact", "=== MULTI-CONTACT SEND PROCESS COMPLETED ===");
        } catch (Exception e) {
            Log.e("SendMultiContact", "Error sending multi-contacts: " + e.getMessage());
            Toast.makeText(this, "Error sending contacts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Parse contact from URI - now handles multiple contacts
     */
    private ContactInfo parseContactFromUri(Uri contactUri) {
        try {
            Log.d("ParseContact", "Parsing contact from URI: " + contactUri);

            // Read contact data from URI
            java.io.InputStream inputStream = getContentResolver().openInputStream(contactUri);
            if (inputStream != null) {
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));
                StringBuilder vCardData = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    vCardData.append(line).append("\n");
                }
                reader.close();
                inputStream.close();

                // Parse vCard data
                String vCardString = vCardData.toString();
                Log.d("ParseContact", "vCard data: " + vCardString);

                // Check if vCard contains multiple contacts
                if (vCardString.contains("BEGIN:VCARD") && vCardString.split("BEGIN:VCARD").length > 2) {
                    // Multiple contacts in single vCard file
                    Log.d("ParseContact", "Multiple contacts detected in vCard file");
                    ArrayList<ContactInfo> allContacts = parseMultipleContactsFromVCard(vCardString);

                    if (!allContacts.isEmpty()) {
                        // Add all contacts to selectedContactInfos
                        selectedContactInfos.clear();
                        selectedContactInfos.addAll(allContacts);

                        // Initialize captions for all contacts
                        contactCaptions.clear();
                        for (int i = 0; i < allContacts.size(); i++) {
                            contactCaptions.put(i, "");
                        }

                        Log.d("ParseContact", "Parsed " + allContacts.size() + " contacts from vCard");

                        // Return the first contact (for backward compatibility)
                        return allContacts.get(0);
                    }
                } else {
                    // Single contact in vCard file
                    ContactInfo contact = parseSingleContactFromVCard(vCardString);

                    if (contact != null && !contact.name.trim().isEmpty()) {
                        Log.d("ParseContact", "Parsed single contact: " + contact.name + " (" + contact.phoneNumber + ")");
                        return contact;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("ParseContact", "Error parsing contact from URI: " + e.getMessage(), e);
        }

        return null;
    }

    /**
     * Parse multiple contacts from vCard string
     */
    private ArrayList<ContactInfo> parseMultipleContactsFromVCard(String vCardString) {
        ArrayList<ContactInfo> contacts = new ArrayList<>();

        try {
            // Split vCard string by BEGIN:VCARD to get individual contacts
            String[] vCardBlocks = vCardString.split("BEGIN:VCARD");

            for (String vCardBlock : vCardBlocks) {
                if (vCardBlock.trim().isEmpty()) continue;

                // Add BEGIN:VCARD back to the block
                String fullVCard = "BEGIN:VCARD" + vCardBlock;

                // Parse individual contact
                ContactInfo contact = parseSingleContactFromVCard(fullVCard);
                if (contact != null && !contact.name.trim().isEmpty()) {
                    contacts.add(contact);
                    Log.d("ParseContact", "Added contact: " + contact.name + " (" + contact.phoneNumber + ")");
                }
            }

            Log.d("ParseContact", "Total contacts parsed: " + contacts.size());

        } catch (Exception e) {
            Log.e("ParseContact", "Error parsing multiple contacts: " + e.getMessage());
        }

        return contacts;
    }

    /**
     * Parse single contact from vCard string
     */
    private ContactInfo parseSingleContactFromVCard(String vCardString) {
        String name = "";
        String phoneNumber = "";
        String email = "";

        try {
            String[] lines = vCardString.split("\n");
            for (String vCardLine : lines) {
                if (vCardLine.startsWith("FN:")) {
                    name = vCardLine.substring(3).trim();
                } else if (vCardLine.startsWith("N:") && name.isEmpty()) {
                    // Handle N: field for name if FN: is not present or empty
                    String[] nameParts = vCardLine.substring(2).split(";");
                    if (nameParts.length > 0) {
                        name = nameParts[0].trim(); // Last name
                        if (nameParts.length > 1) {
                            name = nameParts[1].trim() + " " + name; // First name + Last name
                        }
                    }
                } else if (vCardLine.startsWith("TEL;")) {
                    // Handle various TEL formats like TEL;CELL;PREF:, TEL;X-Phone;PREF:
                    if (vCardLine.contains("TEL;")) {
                        int colonIndex = vCardLine.indexOf(":");
                        if (colonIndex != -1) {
                            phoneNumber = vCardLine.substring(colonIndex + 1).trim();
                            // Remove any non-digit characters except '+'
                            phoneNumber = phoneNumber.replaceAll("[^\\d+]", "");
                        }
                    }
                } else if (vCardLine.startsWith("EMAIL:")) {
                    email = vCardLine.substring(6).trim();
                }
            }

            Log.d("ParseContact", "Parsed contact - Name: " + name + ", Phone: " + phoneNumber + ", Email: " + email);

        } catch (Exception e) {
            Log.e("ParseContact", "Error parsing single contact: " + e.getMessage());
        }

        return new ContactInfo(name, phoneNumber, email);
    }

    /**
     * Create contact message model
     */
    private messageModel createContactMessageModel(ContactInfo contactInfo, String caption, int index, String senderId, String modelId, String receiverUid, String uniqDate) {
        try {
            Log.d("CreateContactMessageModel", "=== CREATING MESSAGE MODEL FOR CONTACT " + (index + 1) + " ===");
            Log.d("CreateContactMessageModel", "contactInfo: " + contactInfo.name);
            Log.d("CreateContactMessageModel", "caption: '" + caption + "'");
            Log.d("CreateContactMessageModel", "senderId: " + senderId);
            Log.d("CreateContactMessageModel", "modelId: " + modelId);
            Log.d("CreateContactMessageModel", "receiverUid: " + receiverUid);
            Log.d("CreateContactMessageModel", "uniqDate: " + uniqDate);

            String currentDateTimeString = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(new java.util.Date());
            Log.d("CreateContactMessageModel", "currentDateTimeString: " + currentDateTimeString);

            Log.d("CreateContactMessageModel", "Creating messageModel with dataType: " + Constant.contact);
            messageModel model = new messageModel(
                    senderId, // senderId
                    caption, // message
                    currentDateTimeString, // time
                    "", // document
                    Constant.contact, // dataType
                    "vcf", // extension
                    contactInfo.name, // name
                    contactInfo.phoneNumber, // phone
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
                    contactInfo.name + ".vcf", // fileName
                    "", // thumbnail
                    "", // fileNameThumbnail
                    caption, // caption
                    1, // notification
                    uniqDate, // uniqDate
                    new ArrayList<>(), // emojiModels
                    "", // replyMessage
                    Constant.getCurrentTimestamp(), // timestamp
                    "", // imageWidthDp
                    "", // imageHeightDp
                    "", // aspectRatio
                    "1" // selectionCount
            );

            Log.d("CreateContactMessageModel", "✅ Contact messageModel created successfully");
            Log.d("CreateContactMessageModel", "Model details:");
            Log.d("CreateContactMessageModel", "  - modelId: " + model.getModelId());
            Log.d("CreateContactMessageModel", "  - senderId: " + model.getUid());
            Log.d("CreateContactMessageModel", "  - receiverUid: " + model.getReceiverUid());
            Log.d("CreateContactMessageModel", "  - fileName: " + model.getFileName());
            Log.d("CreateContactMessageModel", "  - caption: " + model.getCaption());
            Log.d("CreateContactMessageModel", "  - dataType: " + model.getDataType());
            Log.d("CreateContactMessageModel", "  - uniqDate: " + model.getCurrentDate());
            Log.d("CreateContactMessageModel", "  - time: " + model.getTime());

            return model;
        } catch (Exception e) {
            Log.e("CreateContactMessageModel", "❌ Error creating contact messageModel: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(connectivityReceiver);
    }

    private static class SelectionBunchData {
        ArrayList<Uri> uris = new ArrayList<>();
        ArrayList<HashMap<String, String>> selectionBunch = new ArrayList<>();
        ArrayList<File> compressedFiles = new ArrayList<>();
        ArrayList<File> fullSizeFiles = new ArrayList<>();
        ArrayList<Integer> originalIndexes = new ArrayList<>();
    }

    private void sendMultiImages() {
        Log.d("SendMultiImages", "=== STARTING MULTI-IMAGE SEND PROCESS ===");
        Log.d("SendMultiImages", "Total images to send: " + selectedImageUris.size());
        Log.d("SendMultiImages", "SelectedImageUris: " + selectedImageUris);
        Log.d("SendMultiImages", "ImageCaptions: " + imageCaptions);

        try {
            final String senderId = Constant.getSF.getString(Constant.UID_KEY, "");
            final String uniqDate = Constant.getCurrentDate();
            Log.d("SendMultiImages", "senderId=" + senderId + ", uniqDate=" + uniqDate);

            if (forwardNameList == null || forwardNameList.isEmpty()) {
                Log.e("SendMultiImages", "❌ No recipients selected");
                Toast.makeText(this, "Error: No recipients selected", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<messagemodel2> previewModels = new ArrayList<>();

            for (int userIndex = 0; userIndex < forwardNameList.size(); userIndex++) {
                forwardnameModel currentUser = forwardNameList.get(userIndex);
                String receiverUid = currentUser.getFriend_id();
                String userFTokenKey = currentUser.getF_token();

                Log.d("SendMultiImages", "=== PROCESSING USER " + (userIndex + 1) + "/" + forwardNameList.size() + " ===");
                Log.d("SendMultiImages", "User: " + currentUser.getName());
                Log.d("SendMultiImages", "receiverUid: " + receiverUid);
                Log.d("SendMultiImages", "userFTokenKey: " + userFTokenKey);

                SelectionBunchData selectionData = prepareSelectionBunchData(selectedImageUris);
                if (selectionData.selectionBunch.isEmpty()) {
                    Log.e("SendMultiImages", "Selection bunch empty for user " + currentUser.getName());
                    continue;
                }

                ArrayList<String> captions = new ArrayList<>();
                for (int i = 0; i < selectionData.selectionBunch.size(); i++) {
                    int originalIndex = selectionData.originalIndexes.get(i);
                    String caption = "";
                    if (originalIndex < selectedImageUris.size()) {
                        String baseCaption = imageCaptions.get(originalIndex);
                        caption = baseCaption != null ? baseCaption : "";
                    }
                    captions.add(caption);
                }

                messageModel uploadModel = createAndSendSelectionBunchMessage(selectionData, captions, senderId, receiverUid, userFTokenKey);
                Log.d("SendMultiImages", "✅ Completed sending selection bunch to " + currentUser.getName());

                if (forwardNameList.size() == 1 && uploadModel != null) {
                    previewModels.add(convertToMessagemodel2(uploadModel));
                }
            }

            if (Constant.dialogLayoutFullScreen != null && Constant.dialogLayoutFullScreen.isShowing()) {
                Constant.dialogLayoutFullScreen.dismiss();
            }

            if (!forwardNameList.isEmpty()) {
                if (forwardNameList.size() == 1) {
                    forwardnameModel model1 = forwardNameList.get(0);
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
                    fillSelectionBunchPreviewExtras(intent);
                    if (!previewModels.isEmpty()) {
                        intent.putParcelableArrayListExtra("sharedMessageModels", previewModels);
                    }

                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, MainActivityOld.class);
                    startActivity(intent);
                    finish();
                }
            }

        } catch (Exception e) {
            Log.e("SendMultiImages", "Error sending multi-images: " + e.getMessage(), e);
            Toast.makeText(this, "Error sending images: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private SelectionBunchData prepareSelectionBunchData(ArrayList<Uri> selectedUris) {
        SelectionBunchData data = new SelectionBunchData();

        for (int i = 0; i < selectedUris.size(); i++) {
            Uri originalUri = selectedUris.get(i);
            String mimeType = getContentResolver().getType(originalUri);
            if (mimeType == null || !mimeType.startsWith("image/")) {
                Log.d("SelectionBunch", "Skipping non-image URI: " + originalUri);
                continue;
            }

            File compressedFile = processImageFile(originalUri, "compressed", 20);
            File fullSizeFile = processImageFile(originalUri, "full", 80);

            if (compressedFile == null || fullSizeFile == null) {
                Log.e("SelectionBunch", "Failed to prepare files for uri: " + originalUri);
                continue;
            }

            data.compressedFiles.add(compressedFile);
            data.fullSizeFiles.add(fullSizeFile);
            data.uris.add(originalUri);
            data.originalIndexes.add(i);

            String fileName = resolveFileName(originalUri);
            if (fileName == null || fileName.isEmpty()) {
                fileName = compressedFile.getName();
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("imgUrl", "");
            map.put("fileName", fileName);
            data.selectionBunch.add(map);
        }

        return data;
    }

    private messageModel createAndSendSelectionBunchMessage(SelectionBunchData data, ArrayList<String> captions,
                                                            String senderId, String receiverUid, String userFTokenKey) {
        if (data == null || data.selectionBunch.isEmpty()) {
            Log.e("SelectionBunch", "No selection bunch data available to send");
            return null;
        }
        if (data.compressedFiles.isEmpty() || data.fullSizeFiles.isEmpty() || data.uris.isEmpty()) {
            Log.e("SelectionBunch", "Missing file data for selection bunch upload");
            return null;
        }

        try {
            String currentDateTimeString = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(new java.util.Date());
            Constant.getSfFuncion(getApplicationContext());
            String senderName = Constant.getSF.getString(Constant.full_name, "");

            String modelId = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().push().getKey();
            if (modelId == null) {
                Log.e("SelectionBunch", "Failed to generate modelId");
                return null;
            }

            ArrayList<HashMap<String, String>> selectionBunchMaps = new ArrayList<>(data.selectionBunch);

            messageModel model = new messageModel(
                    senderId, "", currentDateTimeString, "", Constant.img,
                    "", "", "", "", "", senderName,
                    "", "", "", "", "", modelId, receiverUid, "", "", "", "", "",
                    "", "", 1, Constant.getCurrentDate(),
                    new ArrayList<>(), "", Constant.getCurrentTimestamp(),
                    "", "", "", String.valueOf(selectionBunchMaps.size())
            );
            model.setSelectionCount(String.valueOf(selectionBunchMaps.size()));
            model.setSelectionBunch(convertToSelectionBunchModels(selectionBunchMaps));

            // Insert message into pending table before upload
            try {
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                dbHelper.insertPendingMessage(model);
                Log.d("SendMultiImages", "✅ Image message inserted into pending table: " + model.getModelId());
            } catch (Exception e) {
                Log.e("SendMultiImages", "❌ Error inserting pending image message: " + e.getMessage(), e);
            }

            ArrayList<String> selectionBunchFilePaths = new ArrayList<>();
            for (File fullFile : data.fullSizeFiles) {
                selectionBunchFilePaths.add(fullFile.getAbsolutePath());
            }
            if (!selectionBunchFilePaths.isEmpty()) {
                String firstFileName = data.selectionBunch.get(0).get("fileName");
                if (firstFileName != null) {
                    model.setFileName(firstFileName);
                }
            }

            UploadChatHelper uploadHelper = new UploadChatHelper(
                    mContext,
                    data.compressedFiles.get(0),
                    data.fullSizeFiles.get(0),
                    senderId,
                    userFTokenKey,
                    model
            );
            uploadHelper.setSelectionBunchFilePaths(selectionBunchFilePaths);

            String[] primaryDimensions = Constant.calculateImageDimensions(mContext,
                    data.compressedFiles.get(0), data.uris.get(0));
            String primaryWidth = primaryDimensions.length > 0 ? primaryDimensions[0] : "";
            String primaryHeight = primaryDimensions.length > 1 ? primaryDimensions[1] : "";
            String primaryAspect = primaryDimensions.length > 2 ? primaryDimensions[2] : "";
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
                    "",
                    "",
                    "",
                    "",
                    "",
                    Constant.img,
                    data.compressedFiles.get(0).getName(),
                    "",
                    primaryWidth,
                    primaryHeight,
                    primaryAspect
            );

            return model;
        } catch (Exception e) {
            Log.e("SelectionBunch", "Error creating selectionBunch message: " + e.getMessage(), e);
            return null;
        }
    }

    private File processImageFile(Uri imageUri, String prefix, int quality) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

            String fileName = resolveFileName(imageUri);
            if (fileName == null) {
                fileName = "image_" + System.currentTimeMillis() + ".jpg";
            }

            File mediaDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }

            File outputFile = new File(mediaDir, prefix + "_" + fileName);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            byte[] data = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(data);
            fos.flush();
            fos.close();

            return outputFile;
        } catch (Exception e) {
            Log.e("CreateImageFile", "Error: " + e.getMessage(), e);
            return null;
        }
    }

    private ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> convertToSelectionBunchModels(ArrayList<HashMap<String, String>> maps) {
        ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> list = new ArrayList<>();
        for (HashMap<String, String> map : maps) {
            String imgUrl = map.get("imgUrl");
            String fileName = map.get("fileName");
            list.add(new com.Appzia.enclosure.Model.selectionBunchModel(imgUrl != null ? imgUrl : "", fileName != null ? fileName : ""));
        }
        return list;
    }

    private String resolveFileName(Uri uri) {
        String fileName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (index != -1) {
                fileName = cursor.getString(index);
            }
            cursor.close();
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }

    private void fillSelectionBunchPreviewExtras(Intent intent) {
        intent.putExtra("preloadedSelectionCount", String.valueOf(selectedImageUris.size()));
        ArrayList<String> fileNames = new ArrayList<>();
        for (Uri uri : selectedImageUris) {
            fileNames.add(resolveFileName(uri));
        }
        intent.putStringArrayListExtra("preloadedSelectionFileNames", new ArrayList<>(fileNames));
    }

    private messagemodel2 convertToMessagemodel2(messageModel model) {
        return new messagemodel2(
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
    }

}