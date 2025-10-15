package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.group_messageModel;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UploadHelper {
    private static final String TAG = "UploadHelper";
    private Context mContext;
    private File globalFile;
    private File fullImageFile;
    private String createdBy;
    private group_messageModel existingModel; // Store existing model to preserve selectionCount
    private ArrayList<String> selectionBunchFilePaths = new ArrayList<>();

    public UploadHelper(Context context, File globalFile, File fullImageFile, String createdBy) {
        this.mContext = context;
        this.globalFile = globalFile;
        this.fullImageFile = fullImageFile;
        this.createdBy = createdBy;
    }

    // New constructor that accepts an existing group_messageModel
    public UploadHelper(Context context, File globalFile, File fullImageFile, String createdBy, group_messageModel existingModel) {
        this.mContext = context;
        this.globalFile = globalFile;
        this.fullImageFile = fullImageFile;
        this.createdBy = createdBy;
        this.existingModel = existingModel;
    }

    public void setSelectionBunchFilePaths(ArrayList<String> selectionBunchFilePaths) {
        if (selectionBunchFilePaths != null) {
            this.selectionBunchFilePaths = new ArrayList<>(selectionBunchFilePaths);
            Log.d(TAG, "selectionBunchFilePaths set with " + this.selectionBunchFilePaths.size() + " entries");
        } else {
            Log.d(TAG, "selectionBunchFilePaths left unchanged (null provided)");
        }
    }

    public ArrayList<String> getSelectionBunchFilePaths() {
        return selectionBunchFilePaths;
    }

    public void uploadContent(String uploadType, Uri uri, String captionText, String modelId, File savedThumbnail,
                              String fileThumbName, String fileName, String contactName, String contactPhone,
                              String audioTime, String audioName, String extension, String imageWidthDp, String imageHeightDp, String aspectRatio) {
        uploadContent(uploadType, uri, captionText, modelId, savedThumbnail, fileThumbName, fileName, contactName, contactPhone, audioTime, audioName, extension, imageWidthDp, imageHeightDp, aspectRatio, null);
    }

    public void uploadContent(String uploadType, Uri uri, String captionText, String modelId, File savedThumbnail,
                              String fileThumbName, String fileName, String contactName, String contactPhone,
                              String audioTime, String audioName, String extension, String imageWidthDp, String imageHeightDp, String aspectRatio, String grpIdKey) {
        Log.d("CAPTION_TRACE", "========================================");
        Log.d("CAPTION_TRACE", "UploadHelper.uploadContent() ENTRY");
        Log.d("CAPTION_TRACE", "captionText param: '" + captionText + "'");
        Log.d("CAPTION_TRACE", "existingModel: " + (existingModel != null ? "YES" : "NO"));
        if (existingModel != null) {
            Log.d("CAPTION_TRACE", "existingModel caption BEFORE: '" + existingModel.getCaption() + "'");
        }
        Log.d("CAPTION_TRACE", "========================================");
        
        // If we have an existing model, use it directly to preserve selectionCount
        if (existingModel != null) {
            Log.d(TAG, "Using existing group_messageModel with selectionCount=" + existingModel.getSelectionCount());
            
            // Ensure caption is preserved when provided via parameter (e.g., from full-screen dialog)
            try {
                if (captionText != null && !captionText.trim().isEmpty()) {
                    Log.d(TAG, "Applying captionText to existingModel before service start: '" + captionText + "'");
                    Log.d("CAPTION_TRACE", "SETTING caption on existingModel: '" + captionText + "'");
                    existingModel.setCaption(captionText.trim());
                    Log.d("CAPTION_TRACE", "existingModel caption AFTER setCaption: '" + existingModel.getCaption() + "'");
                } else {
                    Log.d("CAPTION_TRACE", "captionText is null/empty, NOT updating existingModel caption");
                }
            } catch (Exception ignore) {
                Log.e("CAPTION_TRACE", "Exception setting caption: " + ignore.getMessage());
            }

            // Store single image in local storage for group chat
            if (uploadType.equals(Constant.img) && globalFile != null && fileName != null) {
                Log.d(TAG, "Storing single image in local storage for group chat");
                storeImageInLocalStorage(globalFile, fileName);
            }

            // Store multiple images (selectionBunch) in local storage for group chat
            if (uploadType.equals(Constant.img) && !selectionBunchFilePaths.isEmpty()) {
                Log.d(TAG, "Storing multiple images in local storage for group chat");
                storeMultipleImagesInLocalStorage(selectionBunchFilePaths, existingModel);
            }
            
            // Pass the existing model data to GroupMessageUploadService
            startGroupMessageUploadService(uploadType, existingModel, grpIdKey);
            return;
        }

        // Log additional parameters for voiceAudio
        Log.d(TAG, "uploadType: " + uploadType);
        Log.d(TAG, "fileName1: " + fileName);
        Log.d(TAG, "uri: " + (uri != null ? uri.toString() : "null"));
        Log.d(TAG, "audioTime: " + audioTime + ", audioName: " + audioName);
        Log.d(TAG, "filePath: " + (globalFile != null ? globalFile.getAbsolutePath() : "null"));
        Log.d(TAG, "fullImageFilePath: " + (fullImageFile != null ? fullImageFile.getAbsolutePath() : "null"));
        Log.d(TAG, "fullImageFilePath: " + (fullImageFile != null ? fullImageFile.getPath() : "null"));
        Log.d(TAG, "fullImageFilePath: " + (fullImageFile != null ? fullImageFile.getAbsolutePath() : "null"));
        Log.d(TAG, "thumbnailPath: " + (savedThumbnail != null ? savedThumbnail.getAbsolutePath() : "null"));
        
        // Enhanced debugging for voiceAudio
        if (uploadType.equals(Constant.voiceAudio)) {
            Log.d(TAG, "=== VOICE AUDIO UPLOAD DEBUG ===");
            Log.d(TAG, "UploadHelper instance: " + this.hashCode());
            Log.d(TAG, "fileName parameter: '" + fileName + "'");
            Log.d(TAG, "fileName length: " + (fileName != null ? fileName.length() : "null"));
            Log.d(TAG, "fileName isEmpty: " + (fileName != null ? fileName.isEmpty() : "null"));
            Log.d(TAG, "fileName isBlank: " + (fileName != null ? fileName.trim().isEmpty() : "null"));
            Log.d(TAG, "audioName parameter: '" + audioName + "'");
            Log.d(TAG, "audioTime parameter: '" + audioTime + "'");
            Log.d(TAG, "================================");
        }

        // For voiceAudio, use uri if globalFile is null
        if (uploadType.equals(Constant.voiceAudio) && globalFile == null && uri != null) {
            try {
                globalFile = createFileFromUri(uri, audioName != null ? audioName : "audio_" + System.currentTimeMillis());
                Log.d(TAG, "Created globalFile from uri: " + globalFile.getAbsolutePath());
            } catch (Exception e) {
                Log.e(TAG, "Failed to create file from uri: " + e.getMessage(), e);
                showSnackbar("Failed to process audio file.");
                return;
            }
        }

        long fileSize = getFileSize(fullImageFile != null ? fullImageFile.getPath() : "");
        long fileSize2 = getFileSize(globalFile != null ? globalFile.getPath() : "");

        Log.d(TAG, "fileSize: " + fileSize + ", fileSize2: " + fileSize2);

        // Skip file validation for text and contact uploads
        if (!uploadType.equals(Constant.contact) && !uploadType.equals(Constant.Text)) {
            if (fileSize2 == 0) {
                Log.e(TAG, "File not found or empty: " + (globalFile != null ? globalFile.getAbsolutePath() : "null"));
                showSnackbar("File not found.");
                return;
            }
            if (fileSize2 > 200 * 1024 * 1024) {
                Log.e(TAG, uploadType + " size exceeds 200 MB: " + fileSize2);
                showSnackbar("Keep the file size under 200 MB.");
                return;
            }
        }

        // Proceed with upload
        Log.d(TAG, "File size2: " + getFormattedFileSize(fileSize2));
        if (fullImageFile != null) {
            Log.d(TAG, "File size: " + getFormattedFileSize(fileSize));
        }

        // Store single image in local storage for group chat
        if (uploadType.equals(Constant.img) && globalFile != null && fileName != null) {
            Log.d(TAG, "Storing single image in local storage for group chat");
            storeImageInLocalStorage(globalFile, fileName);
        }

        // Store multiple images (selectionBunch) in local storage for group chat
        if (uploadType.equals(Constant.img) && !selectionBunchFilePaths.isEmpty()) {
            Log.d(TAG, "Storing multiple images in local storage for group chat");
            storeMultipleImagesInLocalStorage(selectionBunchFilePaths, existingModel);
        }

        // Prepare Intent for GroupMessageUploadService
        Intent serviceIntent = new Intent(mContext, GroupMessageUploadService.class);
        serviceIntent.putExtra("uploadType", uploadType);
        serviceIntent.putExtra("uid", Constant.getSF.getString(Constant.UID_KEY, ""));
        serviceIntent.putExtra("group_id", grpIdKey != null ? grpIdKey : getIntentExtra("grpIdKey"));
        serviceIntent.putExtra("message", "");
        serviceIntent.putExtra("dataType", getDataType(uploadType));
        serviceIntent.putExtra("modelId", modelId);
        serviceIntent.putExtra("userName", Constant.getSF.getString(Constant.full_name, ""));
        serviceIntent.putExtra("time", new SimpleDateFormat("hh:mm a").format(new Date()));
        serviceIntent.putExtra("extension", uri != null ? getFileExtension(uri) : (extension != null ? extension : ""));
        serviceIntent.putExtra("name", contactName != null ? contactName : "");
        serviceIntent.putExtra("phone", contactPhone != null ? contactPhone : "");
        serviceIntent.putExtra("micPhoto", "");
        serviceIntent.putExtra("miceTiming", audioTime != null ? audioTime : "");
        serviceIntent.putExtra("filePath", globalFile != null ? globalFile.getAbsolutePath() : "");
        serviceIntent.putExtra("fullImageFilePath", fullImageFile != null ? fullImageFile.getAbsolutePath() : "");
        serviceIntent.putExtra("thumbnailPath", savedThumbnail != null ? savedThumbnail.getAbsolutePath() : "");
        serviceIntent.putExtra("fileNameThumbnail", fileThumbName != null ? fileThumbName : "");
        serviceIntent.putExtra("fileName", fileName != null ? fileName : "");
        serviceIntent.putExtra("created_by", createdBy);
        serviceIntent.putExtra("userFTokenKey", Constant.getSF.getString(Constant.FCM_TOKEN, ""));
        serviceIntent.putExtra("deviceType", "1");
        serviceIntent.putExtra("replytextData", "");
        serviceIntent.putExtra("replyKey", "");
        serviceIntent.putExtra("replyType", "");
        serviceIntent.putExtra("replyOldData", "");
        serviceIntent.putExtra("replyCrtPostion", "");
        serviceIntent.putExtra("forwaredKey", "");
        serviceIntent.putExtra("groupName", getIntentExtra("nameKey"));
        serviceIntent.putExtra("caption", captionText != null ? captionText : "");
        serviceIntent.putExtra("notification", 1);
        serviceIntent.putExtra("currentDate", Constant.getCurrentDate());
        serviceIntent.putExtra("docSize", getFormattedFileSize(fileSize2));
        Gson gson = new Gson();
        ArrayList<emojiModel> emojiModels = new ArrayList<>();
        emojiModels.add(new emojiModel("", ""));
        serviceIntent.putExtra("emojiModelJson", gson.toJson(emojiModels));
        serviceIntent.putExtra("emojiCount", "");
        serviceIntent.putExtra("timestamp", Constant.getCurrentTimestamp());
        serviceIntent.putExtra("fileSize", fileSize2);
        serviceIntent.putExtra("imageWidthDp", imageWidthDp);
        serviceIntent.putExtra("imageHeightDp", imageHeightDp);
        serviceIntent.putExtra("aspectRatio", aspectRatio);

        // Battery optimization check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            String packageName = mContext.getPackageName();
          // Inside UploadHelper.uploadContent()
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required for non-Activity contexts
                try {
                    SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                } catch (Exception e) {
                    Log.e(TAG, "Battery optimization intent failed: " + e.getMessage(), e);
                }
            }
        }

        // Start service
        Log.d(TAG, "Starting GroupMessageUploadService for uploadType: " + uploadType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(serviceIntent);
        } else {
            mContext.startService(serviceIntent);
        }
    }

    private void startGroupMessageUploadService(String uploadType, group_messageModel model, String grpIdKey) {
        // Debug logging for groupId
        Log.d(TAG, "startGroupMessageUploadService: grpIdKey=" + grpIdKey + ", model.getReceiverUid()=" + model.getReceiverUid());
        
        Log.d("CAPTION_TRACE", "========================================");
        Log.d("CAPTION_TRACE", "startGroupMessageUploadService() ENTRY");
        Log.d("CAPTION_TRACE", "Model caption: '" + model.getCaption() + "'");
        Log.d("CAPTION_TRACE", "Model selectionCount: " + model.getSelectionCount());
        Log.d("CAPTION_TRACE", "========================================");
        
        // Prepare Intent for GroupMessageUploadService
        Intent serviceIntent = new Intent(mContext, GroupMessageUploadService.class);
        serviceIntent.putExtra("uploadType", uploadType);
        serviceIntent.putExtra("uid", model.getUid());
        String finalGroupId = grpIdKey != null ? grpIdKey : model.getReceiverUid();
        serviceIntent.putExtra("group_id", finalGroupId);
        Log.d(TAG, "Setting group_id to: " + finalGroupId);
        serviceIntent.putExtra("message", model.getMessage());
        serviceIntent.putExtra("dataType", model.getDataType());
        serviceIntent.putExtra("modelId", model.getModelId());
        serviceIntent.putExtra("userName", model.getUserName());
        serviceIntent.putExtra("time", model.getTime());
        serviceIntent.putExtra("extension", model.getExtension());
        serviceIntent.putExtra("name", model.getName());
        serviceIntent.putExtra("phone", model.getPhone());
        serviceIntent.putExtra("micPhoto", model.getMicPhoto());
        serviceIntent.putExtra("miceTiming", model.getMiceTiming());
        serviceIntent.putExtra("filePath", globalFile != null && globalFile.exists() ? globalFile.getAbsolutePath() : "");
        serviceIntent.putExtra("fullImageFilePath", fullImageFile != null && fullImageFile.exists() ? fullImageFile.getAbsolutePath() : "");
        serviceIntent.putExtra("thumbnailPath", "");
        serviceIntent.putExtra("fileName", model.getFileName());
        serviceIntent.putExtra("fileNameThumbnail", model.getFileNameThumbnail());
        serviceIntent.putExtra("deviceType", "1");
        serviceIntent.putExtra("userFTokenKey", ""); // Add userFTokenKey
        serviceIntent.putExtra("replytextData", "");
        serviceIntent.putExtra("replyKey", "");
        serviceIntent.putExtra("replyType", "");
        serviceIntent.putExtra("replyOldData", "");
        serviceIntent.putExtra("replyCrtPostion", "");
        serviceIntent.putExtra("forwaredKey", "");
        serviceIntent.putExtra("groupName", "");
        serviceIntent.putExtra("caption", model.getCaption());
        
        Log.d("CAPTION_TRACE", "========================================");
        Log.d("CAPTION_TRACE", "Intent extra 'caption' set to: '" + model.getCaption() + "'");
        Log.d("CAPTION_TRACE", "========================================");
        serviceIntent.putExtra("notification", model.getActive());
        serviceIntent.putExtra("currentDate", model.getCurrentDate());
        serviceIntent.putExtra("docSize", model.getDocSize());
        serviceIntent.putExtra("thumbnail", model.getThumbnail());
        serviceIntent.putExtra("emojiModelJson", "");
        serviceIntent.putExtra("emojiCount", "");
        serviceIntent.putExtra("timestamp", Constant.getCurrentTimestamp());
        serviceIntent.putExtra("imageWidthDp", model.getImageWidth());
        serviceIntent.putExtra("imageHeightDp", model.getImageHeight());
        serviceIntent.putExtra("aspectRatio", model.getAspectRatio());
        serviceIntent.putExtra("selectionCount", model.getSelectionCount());
        // Pass selectionBunch as JSON if present so the service can persist it to Firebase
        try {
            if (model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty()) {
                String selectionBunchJson = new Gson().toJson(model.getSelectionBunch());
                serviceIntent.putExtra("selectionBunchJson", selectionBunchJson);
                Log.d(TAG, "startGroupMessageUploadService: attached selectionBunchJson size=" + model.getSelectionBunch().size());
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to serialize selectionBunch: " + e.getMessage(), e);
        }
        if (!selectionBunchFilePaths.isEmpty()) {
            serviceIntent.putStringArrayListExtra("selectionBunchFilePaths", selectionBunchFilePaths);
        }
        serviceIntent.putExtra("fileSize", 0);
        serviceIntent.putExtra("needsForeground", false);

        // Start service
        Log.d(TAG, "Starting GroupMessageUploadService with existing model: uploadType=" + uploadType + ", selectionCount=" + model.getSelectionCount());
        mContext.startService(serviceIntent);
    }

    private File createFileFromUri(Uri uri, String fileName) throws Exception {
        File file = new File(mContext.getCacheDir(), fileName);
        try (InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            if (inputStream == null) {
                throw new Exception("Unable to open input stream for uri: " + uri);
            }
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return file;
    }

    private String getDataType(String uploadType) {
        if (uploadType.equals(Constant.video)) {
            return Constant.video;
        } else if (uploadType.equals(Constant.contact)) {
            return Constant.contact;
        } else if (uploadType.equals(Constant.voiceAudio)) {
            return Constant.voiceAudio;
        } else if (uploadType.equals(Constant.doc)) {
            return Constant.doc;
        } else if (uploadType.equals(Constant.camera) || uploadType.equals(Constant.img)) {
            return Constant.img;
        } else if (uploadType.equals(Constant.Text)) {
            return Constant.Text;
        } else {
            return Constant.img; // Default fallback
        }
    }

    private void showSnackbar(String message) {
        Log.d(TAG, "Snackbar: " + message);
    }

    private String getIntentExtra(String key) {
        return mContext instanceof android.app.Activity ? ((android.app.Activity) mContext).getIntent().getStringExtra(key) : "";
    }

    private long getFileSize(String path) {
        if (path == null || path.isEmpty()) return 0;
        File file = new File(path);
        return file.exists() ? file.length() : 0;
    }

    private String getFormattedFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.1f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    /**
     * Store single image in local storage for group chat
     * Path: File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images")
     */
    private void storeImageInLocalStorage(File sourceFile, String fileName) {
        try {
            // Create the target directory
            File targetDirectory;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                targetDirectory = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
            } else {
                targetDirectory = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
            }
            
            // Ensure directory exists
            if (!targetDirectory.exists()) {
                boolean created = targetDirectory.mkdirs();
                Log.d(TAG, "Created directory: " + targetDirectory.getAbsolutePath() + ", success: " + created);
            }
            
            // Create target file
            File targetFile = new File(targetDirectory, fileName);
            
            // Copy file to local storage
            if (sourceFile != null && sourceFile.exists()) {
                try (FileInputStream inputStream = new FileInputStream(sourceFile);
                     FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                    
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    
                    Log.d(TAG, "Successfully stored image in local storage: " + targetFile.getAbsolutePath());
                    Log.d(TAG, "File size: " + targetFile.length() + " bytes");
                    
                    // Update the existing model with the local file path if it exists
                    if (existingModel != null) {
                        // Update the document path to point to local storage
                        existingModel.setDocument(targetFile.getAbsolutePath());
                        Log.d(TAG, "Updated model document path to: " + targetFile.getAbsolutePath());
                    }
                    
                } catch (Exception e) {
                    Log.e(TAG, "Error copying file to local storage: " + e.getMessage(), e);
                }
            } else {
                Log.w(TAG, "Source file does not exist: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "null"));
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error storing image in local storage: " + e.getMessage(), e);
        }
    }

    /**
     * Store multiple images (selectionBunch) in local storage for group chat
     * Path: File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images")
     */
    private void storeMultipleImagesInLocalStorage(ArrayList<String> sourceFilePaths, group_messageModel existingModel) {
        try {
            if (sourceFilePaths == null || sourceFilePaths.isEmpty()) {
                Log.w(TAG, "No source file paths provided for multiple images storage");
                return;
            }

            // Create the target directory
            File targetDirectory;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                targetDirectory = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
            } else {
                targetDirectory = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
            }
            
            // Ensure directory exists
            if (!targetDirectory.exists()) {
                boolean created = targetDirectory.mkdirs();
                Log.d(TAG, "Created directory: " + targetDirectory.getAbsolutePath() + ", success: " + created);
            }

            ArrayList<String> storedFilePaths = new ArrayList<>();
            
            // Store each image file
            for (int i = 0; i < sourceFilePaths.size(); i++) {
                String sourcePath = sourceFilePaths.get(i);
                File sourceFile = new File(sourcePath);
                
                if (sourceFile.exists()) {
                    // Generate filename with index to avoid conflicts - remove any subdirectory prefixes
                    String originalFileName = sourceFile.getName();
                    // Remove any subdirectory prefixes like "chats/" from the filename
                    if (originalFileName.contains("/")) {
                        originalFileName = originalFileName.substring(originalFileName.lastIndexOf("/") + 1);
                    }
                    String fileName = "img_" + System.currentTimeMillis() + "_" + i + "_" + originalFileName;
                    File targetFile = new File(targetDirectory, fileName);
                    
                    try (FileInputStream inputStream = new FileInputStream(sourceFile);
                         FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                        
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        
                        storedFilePaths.add(targetFile.getAbsolutePath());
                        Log.d(TAG, "Successfully stored image " + (i + 1) + " in local storage: " + targetFile.getAbsolutePath());
                        Log.d(TAG, "File size: " + targetFile.length() + " bytes");
                        
                        // Update the selectionBunchModel filename if existingModel is available
                        if (existingModel != null && existingModel.getSelectionBunch() != null && 
                            i < existingModel.getSelectionBunch().size()) {
                            existingModel.getSelectionBunch().get(i).setFileName(fileName);
                            Log.d(TAG, "Updated selectionBunchModel[" + i + "] filename to: " + fileName);
                        }
                        
                    } catch (Exception e) {
                        Log.e(TAG, "Error copying file " + (i + 1) + " to local storage: " + e.getMessage(), e);
                    }
                } else {
                    Log.w(TAG, "Source file does not exist: " + sourcePath);
                }
            }
            
            // Update selectionBunchFilePaths with stored file paths
            if (!storedFilePaths.isEmpty()) {
                this.selectionBunchFilePaths = storedFilePaths;
                Log.d(TAG, "Updated selectionBunchFilePaths with " + storedFilePaths.size() + " stored file paths");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error storing multiple images in local storage: " + e.getMessage(), e);
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
}