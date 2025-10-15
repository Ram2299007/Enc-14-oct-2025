package com.Appzia.enclosure.Utils.BroadcastReiciver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;

import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UploadChatHelper {
    private static final String TAG = "UploadChatHelper";
    private final Context mContext;
    private File globalFile;
    private File fullImageFile;
    public String userFTokenKey;
    private final String createdBy;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final Gson gson = new Gson();
    private final Map<String, Long> fileSizeCache = new ConcurrentHashMap<>();
    private final List<String> pendingSnackbars = Collections.synchronizedList(new ArrayList<>());
    private boolean isShowingSnackbar = false;
    private static final String STORAGE_SUBDIR = "upload_files";
    private String xFilename;
    private messageModel existingModel; // Store existing model to preserve selectionCount
    private ArrayList<String> selectionBunchFilePaths = new ArrayList<>();

    public UploadChatHelper(Context context, File globalFile, File fullImageFile, String createdBy, String userFTokenKey) {
        this.mContext = context;
        this.globalFile = globalFile;
        this.fullImageFile = fullImageFile;
        this.createdBy = createdBy;
        this.userFTokenKey = userFTokenKey;
    }

    // New constructor that accepts an existing messageModel
    public UploadChatHelper(Context context, File globalFile, File fullImageFile, String createdBy, String userFTokenKey, messageModel existingModel) {
        this.mContext = context;
        this.globalFile = globalFile;
        this.fullImageFile = fullImageFile;
        this.createdBy = createdBy;
        this.userFTokenKey = userFTokenKey;
        this.existingModel = existingModel;

        // Debug: Log the existingModel state
        Log.d("SelectionBunch", "UploadChatHelper constructor: existingModel=" + (existingModel != null ? "not null" : "null"));
        if (existingModel != null) {
            Log.d("SelectionBunch", "existingModel.getSelectionBunch()=" + (existingModel.getSelectionBunch() != null ? "not null" : "null"));
            if (existingModel.getSelectionBunch() != null) {
                Log.d("SelectionBunch", "existingModel.getSelectionBunch().size()=" + existingModel.getSelectionBunch().size());
                for (int i = 0; i < existingModel.getSelectionBunch().size(); i++) {
                    com.Appzia.enclosure.Model.selectionBunchModel bunch = existingModel.getSelectionBunch().get(i);
                    Log.d("SelectionBunch", "existingModel selectionBunch[" + i + "]: fileName=" + bunch.getFileName() + ", imgUrl=" + bunch.getImgUrl());
                }
            }
        }
    }

    public void setSelectionBunchFilePaths(ArrayList<String> selectionBunchFilePaths) {
        if (selectionBunchFilePaths != null) {
            this.selectionBunchFilePaths = new ArrayList<>(selectionBunchFilePaths);
            Log.d(TAG, "selectionBunchFilePaths set with " + this.selectionBunchFilePaths.size() + " entries");
        } else {
            Log.d(TAG, "selectionBunchFilePaths left unchanged (null provided)");
        }
    }

    // New method that accepts an existing messageModel and uses it directly
    public void uploadContentWithModel(messageModel model) {
        Log.d(TAG, "uploadContentWithModel: Using existing messageModel with selectionCount=" + model.getSelectionCount());

        // Use the existing model directly instead of creating a new one
        // This preserves the selectionCount from ImageAdapter
        // TODO: Implement direct upload to Firebase using the existing model
    }

    public void uploadContent(String uploadType, Uri uri, String captionText, String modelId, File savedThumbnail,
                              String fileThumbName, String fileName, String contactName, String contactPhone,
                              String audioTime, String audioName, String extension, String receiverUid,
                              String replyCrtPostion, String replyKey, String replyOldData, String replyType,
                              String replytextData, String replydataType, String replyfilename, String forwaredKey, String imageWidthDp, String imageHeightDp, String aspectRatio) {

        Log.d("bunch###", "UploadChatHelper.uploadContent called with uploadType=" + uploadType + ", modelId=" + modelId);
        Log.d(TAG, "uploadType: " + uploadType + ", uri: " + (uri != null ? uri.toString() : "null"));
        Log.d(TAG, "captionText parameter: '" + captionText + "'");
        Log.d(TAG, "Global file: " + (globalFile != null ? globalFile.getAbsolutePath() + ", exists: " + globalFile.exists() + ", size: " + globalFile.length() : "null"));
        Log.d(TAG, "Full image file: " + (fullImageFile != null ? fullImageFile.getAbsolutePath() + ", exists: " + fullImageFile.exists() + ", size: " + fullImageFile.length() : "null"));
        Log.d(TAG, "Thumbnail file: " + (savedThumbnail != null ? savedThumbnail.getAbsolutePath() + ", exists: " + savedThumbnail.exists() + ", size: " + savedThumbnail.length() : "null"));
        Log.d(TAG, "fileName: " + fileName + ", xFilename: " + xFilename);
        Log.d(TAG, "contactName: " + contactName + ", contactPhone: " + contactPhone);
        Log.d(TAG, "extension: " + extension);

        xFilename = fileName;

        try {
            Log.d("bunch###", "UploadChatHelper: Inside try block, existingModel=" + (existingModel != null));
            // If we have an existing model, use it directly to preserve selectionCount
            if (existingModel != null) {
                Log.d("bunch###", "UploadChatHelper: Inside existingModel != null block");
                Log.d(TAG, "Using existing messageModel with selectionCount=" + existingModel.getSelectionCount());
                Log.d(TAG, "Existing model caption: '" + existingModel.getCaption() + "'");
                Log.d(TAG, "CaptionText parameter: '" + captionText + "'");

                // Persist incoming dimension metadata on the retained model so downstream services receive it
                if (imageWidthDp != null && !imageWidthDp.isEmpty()) {
                    existingModel.setImageWidth(imageWidthDp);
                }
                if (imageHeightDp != null && !imageHeightDp.isEmpty()) {
                    existingModel.setImageHeight(imageHeightDp);
                }
                if (aspectRatio != null && !aspectRatio.isEmpty()) {
                    existingModel.setAspectRatio(aspectRatio);
                }

                Log.d(TAG, "Using existing messageModel with imageWidth=" + existingModel.getImageWidth() + ", imageHeight=" + existingModel.getImageHeight() + ", aspectRatio=" + existingModel.getAspectRatio());
                if (existingModel.getSelectionBunch() != null && !existingModel.getSelectionBunch().isEmpty()) {
                    ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> bunch = existingModel.getSelectionBunch();
                    boolean hasMissingUrls = false;
                    for (com.Appzia.enclosure.Model.selectionBunchModel entry : bunch) {
                        if (entry.getImgUrl() == null || entry.getImgUrl().isEmpty()) {
                            hasMissingUrls = true;
                            break;
                        }
                    }
                    // Only clear selectionBunchFilePaths if all URLs are present AND we have no local files to upload
                    if (!hasMissingUrls && selectionBunchFilePaths.isEmpty()) {
                        Log.d(TAG, "All selectionBunch URLs present and no local files to upload, proceeding with existing URLs");
                    } else if (!hasMissingUrls && !selectionBunchFilePaths.isEmpty()) {
                        Log.d(TAG, "All selectionBunch URLs present but local files exist, will re-upload to ensure consistency");
                    }
                }

                messageModel finalModel = existingModel;
                String finalWidth = finalModel.getImageWidth();
                String finalHeight = finalModel.getImageHeight();
                String finalAspect = finalModel.getAspectRatio();

                // Debug: Log the model state before starting selectionBunch upload
                Log.d(TAG, "About to start selectionBunch upload with model:");
                Log.d(TAG, "  - selectionBunch size: " + (finalModel.getSelectionBunch() != null ? finalModel.getSelectionBunch().size() : "null"));
                Log.d(TAG, "  - selectionCount: " + finalModel.getSelectionCount());
                Log.d(TAG, "  - selectionBunchFilePaths size: " + selectionBunchFilePaths.size());

                // Debug: Check if finalModel is the same as existingModel
                Log.d("SelectionBunch", "finalModel == existingModel: " + (finalModel == existingModel));
                Log.d("SelectionBunch", "finalModel.getSelectionBunch() size: " + (finalModel.getSelectionBunch() != null ? finalModel.getSelectionBunch().size() : "null"));
                Log.d("SelectionBunch", "existingModel.getSelectionBunch() size: " + (existingModel != null && existingModel.getSelectionBunch() != null ? existingModel.getSelectionBunch().size() : "null"));

                Log.d("bunch###", "UploadChatHelper: Calling startSelectionBunchUpload directly from existingModel block");
                Log.d("bunch###", "BEFORE mainHandler.post - finalModel.getSelectionBunch() size: " + (finalModel.getSelectionBunch() != null ? finalModel.getSelectionBunch().size() : "null"));
                Log.d("bunch###", "BEFORE mainHandler.post - existingModel.getSelectionBunch() size: " + (existingModel != null && existingModel.getSelectionBunch() != null ? existingModel.getSelectionBunch().size() : "null"));

                // Calculate file sizes for selectionBunch upload
                final long calculatedFileSize = getFileSize(fullImageFile != null ? fullImageFile.getPath() : "");
                final long calculatedFileSize2 = getFileSize(globalFile != null ? globalFile.getPath() : "");
                // Create emoji model JSON
                final ArrayList<emojiModel> emojiModels = new ArrayList<>();
                emojiModels.add(new emojiModel("", ""));
                final String calculatedEmojiModelJson = gson.toJson(emojiModels);
                // Create final copies of other variables
                final File finalSavedThumbnail = savedThumbnail;
                final String finalFileThumbName = fileThumbName;
                final String finalXFilename = xFilename;
                final String finalCaptionText = captionText;
                
                // Create a deep copy of selectionBunch to prevent it from being cleared
                final ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> finalSelectionBunch = new ArrayList<>();
                if (finalModel.getSelectionBunch() != null) {
                    for (com.Appzia.enclosure.Model.selectionBunchModel item : finalModel.getSelectionBunch()) {
                        com.Appzia.enclosure.Model.selectionBunchModel copy = new com.Appzia.enclosure.Model.selectionBunchModel();
                        copy.setFileName(item.getFileName());
                        copy.setImgUrl(item.getImgUrl());
                        finalSelectionBunch.add(copy);
                    }
                }

                mainHandler.post(() -> {
                    Log.d("bunch###", "INSIDE mainHandler.post - finalModel.getSelectionBunch() size: " + (finalModel.getSelectionBunch() != null ? finalModel.getSelectionBunch().size() : "null"));
                    Log.d("bunch###", "INSIDE mainHandler.post - existingModel.getSelectionBunch() size: " + (existingModel != null && existingModel.getSelectionBunch() != null ? existingModel.getSelectionBunch().size() : "null"));

                    // Use the deep copy of selectionBunch to prevent it from being cleared
                    if (finalSelectionBunch != null && !finalSelectionBunch.isEmpty()) {
                        Log.d("bunch###", "Using deep copy of selectionBunch with " + finalSelectionBunch.size() + " items");
                        finalModel.setSelectionBunch(finalSelectionBunch);
                        existingModel.setSelectionBunch(finalSelectionBunch);
                    } else if (finalModel.getSelectionBunch() == null || finalModel.getSelectionBunch().isEmpty()) {
                        Log.d("bunch###", "❌ selectionBunch was cleared! Attempting to restore from selectionBunchFilePaths...");
                        if (selectionBunchFilePaths != null && !selectionBunchFilePaths.isEmpty()) {
                            Log.d("bunch###", "Restoring selectionBunch from " + selectionBunchFilePaths.size() + " file paths");
                            ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> restoredBunch = new ArrayList<>();
                            for (int i = 0; i < selectionBunchFilePaths.size(); i++) {
                                String filePath = selectionBunchFilePaths.get(i);
                                String bunchFileName = new File(filePath).getName();
                                // Remove "full_" prefix if present
                                if (bunchFileName.startsWith("full_")) {
                                    bunchFileName = bunchFileName.substring(5);
                                }
                                com.Appzia.enclosure.Model.selectionBunchModel bunchItem = new com.Appzia.enclosure.Model.selectionBunchModel();
                                bunchItem.setFileName(bunchFileName);
                                bunchItem.setImgUrl(""); // Will be updated after upload
                                restoredBunch.add(bunchItem);
                                Log.d("bunch###", "Restored bunch item " + (i+1) + ": " + bunchFileName);
                            }
                            finalModel.setSelectionBunch(restoredBunch);
                            existingModel.setSelectionBunch(restoredBunch);
                            Log.d("bunch###", "✅ selectionBunch restored with " + restoredBunch.size() + " items");

                            // Now add the message to UI after restoration
                            Log.d("bunch###", "Adding restored message to UI with selectionBunch size: " + finalModel.getSelectionBunch().size());
                            // This will be handled by the chattingScreen after the upload completes
                        } else {
                            Log.d("bunch###", "❌ Cannot restore selectionBunch - no file paths available");
                        }
                    }

                    Log.d("bunch###", "About to call startSelectionBunchUpload with finalModel.getSelectionBunch() size: " + (finalModel.getSelectionBunch() != null ? finalModel.getSelectionBunch().size() : "null"));
                    startSelectionBunchUpload(uploadType, finalModel, calculatedFileSize, calculatedFileSize2, finalSavedThumbnail, finalFileThumbName, finalXFilename, calculatedEmojiModelJson, finalCaptionText, finalWidth, finalHeight, finalAspect);
                });
                return;
            }

            // Clear files for text or contact messages
            if (uploadType.equals(Constant.Text) || uploadType.equals(Constant.contact)) {
                globalFile = null;
                fullImageFile = null;
                savedThumbnail = null;
                Log.d(TAG, "Text or contact message: Cleared globalFile, fullImageFile, and savedThumbnail");
            } else {
                // Handle voice audio
                if (uploadType.equals(Constant.voiceAudio)) {
                    if (globalFile == null || !globalFile.exists() || globalFile.length() == 0) {
                        if (uri != null) {
                            String audioFileName = fileName != null ? fileName : (audioName != null ? audioName : "audio_" + System.currentTimeMillis() + ".mp3");
                            globalFile = createFileFromUri(uri, audioFileName);
                            xFilename = globalFile.getName();
                            if (!globalFile.exists() || globalFile.length() == 0) {
                                Log.w(TAG, "Created audio file is empty or does not exist: " + globalFile.getAbsolutePath() + ", proceeding without file");
                                globalFile = null;
                            }
                        } else {
                            Log.w(TAG, "No valid file or URI for voiceAudio, proceeding without file");
                            globalFile = null;
                        }
                    }
                }

                // Handle camera image
                if (uploadType.equals(Constant.camera)) {
                    if ((fullImageFile == null || !fullImageFile.exists() || fullImageFile.length() == 0) && uri != null) {
                        String imageFileName = fileName != null ? fileName : "camera_" + System.currentTimeMillis() + ".jpg";
                        fullImageFile = createFileFromUri(uri, imageFileName);
                        xFilename = fullImageFile.getName();
                        if (!fullImageFile.exists() || fullImageFile.length() == 0) {
                            Log.w(TAG, "Created image file is empty or does not exist: " + fullImageFile.getAbsolutePath() + ", proceeding without file");
                            fullImageFile = null;
                        }
                    } else if (uri == null) {
                        Log.w(TAG, "No URI provided for camera image, proceeding without file");
                        fullImageFile = null;
                    }
                }

                // Handle video
                if (uploadType.equals(Constant.video)) {
                    if (uri != null && (globalFile == null || !globalFile.exists() || globalFile.length() == 0)) {
                        String videoFileName = fileName != null ? fileName : "video_" + System.currentTimeMillis() + ".mp4";
                        globalFile = createFileFromUri(uri, videoFileName);
                        xFilename = globalFile.getName();
                        if (!globalFile.exists() || globalFile.length() == 0) {
                            Log.w(TAG, "Created video file is empty or does not exist: " + globalFile.getAbsolutePath() + ", proceeding without file");
                            globalFile = null;
                        }
                    } else if (uri == null) {
                        Log.w(TAG, "No URI provided for video, proceeding with existing file or none");
                    }
                    // Handle thumbnail for video
                    if (savedThumbnail == null || !savedThumbnail.exists() || savedThumbnail.length() == 0) {
                        Log.w(TAG, "Thumbnail not found or empty for video: " + (savedThumbnail != null ? savedThumbnail.getAbsolutePath() : "null") + ", proceeding without thumbnail");
                        savedThumbnail = null;
                    }
                }

                // Handle document
                if (uploadType.equals(Constant.doc)) {
                    if (uri != null && (globalFile == null || !globalFile.exists() || globalFile.length() == 0)) {
                        String docFileName = fileName != null ? fileName : "doc_" + System.currentTimeMillis() + "." + (extension != null ? extension : "tmp");
                        globalFile = createFileFromUri(uri, docFileName);
                        xFilename = globalFile.getName();
                        if (!globalFile.exists() || globalFile.length() == 0) {
                            Log.w(TAG, "Created document file is empty or does not exist: " + globalFile.getAbsolutePath() + ", proceeding without file");
                            globalFile = null;
                        }
                    } else if (uri == null) {
                        Log.w(TAG, "No URI provided for document, proceeding with existing file or none");
                    }
                }
            }

            // Validate file existence, readability, and size
            File fileToValidate = fullImageFile != null && fullImageFile.exists() ? fullImageFile : globalFile;
            if (!uploadType.equals(Constant.Text) && !uploadType.equals(Constant.contact)) {
                if (fileToValidate == null || !fileToValidate.exists() || fileToValidate.length() == 0) {
                    Log.w(TAG, "File not found or empty: " + (fileToValidate != null ? fileToValidate.getAbsolutePath() : "null") + ", proceeding without file");
                    fileToValidate = null;
                } else if (!fileToValidate.canRead()) {
                    Log.w(TAG, "File is not readable: " + fileToValidate.getAbsolutePath() + ", proceeding without file");
                    fileToValidate = null;
                } else if (fileToValidate.length() > 200 * 1024 * 1024) {
                    Log.e(TAG, "File exceeds 200 MB: " + getFormattedFileSize(fileToValidate.length()));
                    // mainHandler.post(() -> showSnackbar("File exceeds 200MB limit"));
                    return; // Stop if file is too large
                }
            }

            // Calculate file sizes
            long fileSize = getFileSize(fullImageFile != null ? fullImageFile.getPath() : "");
            long fileSize2 = getFileSize(globalFile != null ? globalFile.getPath() : "");
            String docSize = getFormattedFileSize(fileSize2 > 0 ? fileSize2 : fileSize);
            Log.d(TAG, "fileSize (fullImageFile): " + fileSize + ", fileSize2 (globalFile): " + fileSize2 + ", docSize: " + docSize);

            // Compute dataType
            String dataType = uploadType;
            if (uploadType.equals(Constant.camera)) {
                dataType = Constant.img;
            } else if (uploadType.equals(Constant.doc)) {
                dataType = Constant.doc;
            }

            // Prepare messageModel
            String currentDateTimeString = new SimpleDateFormat("hh:mm a").format(new Date());
            ArrayList<emojiModel> emojiModels = new ArrayList<>();
            emojiModels.add(new emojiModel("", ""));
            String emojiModelJson = gson.toJson(emojiModels);

            messageModel model;
            if (replyType != null) {

                if(!uploadType.equals(Constant.Text)){
                    model = new messageModel(
                            createdBy,
                            captionText != null ? captionText : "",
                            currentDateTimeString,
                            "",
                            replydataType,
                            extension != null ? extension : getFileExtension(uri),
                            contactName != null ? contactName : "",
                            contactPhone != null ? contactPhone : "",
                            "",
                            audioTime != null ? audioTime : "",
                            Constant.getSF.getString(Constant.full_name, ""),
                            replytextData,
                            replyKey,
                            replyType,
                            replyOldData,
                            replyCrtPostion,
                            modelId,
                            receiverUid,
                            forwaredKey,
                            "",
                            docSize,
                            replyfilename,
                            "",
                            fileThumbName != null ? fileThumbName : "",
                            "",
                            1,
                            Constant.getCurrentDate(),
                            emojiModels,
                            "",
                            Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1"
                    );
                }else {
                    model = new messageModel(
                            createdBy,
                            captionText != null ? captionText : "",
                            currentDateTimeString,
                            "",
                            replydataType,
                            extension != null ? extension : getFileExtension(uri),
                            contactName != null ? contactName : "",
                            contactPhone != null ? contactPhone : "",
                            "",
                            audioTime != null ? audioTime : "",
                            Constant.getSF.getString(Constant.full_name, ""),
                            replytextData,
                            replyKey,
                            replyType,
                            replyOldData,
                            replyCrtPostion,
                            modelId,
                            receiverUid,
                            forwaredKey,
                            "",
                            docSize,
                            replyfilename,
                            "",
                            fileThumbName != null ? fileThumbName : "",
                            replydataType.equals(Constant.Text) ? "" : (captionText != null ? captionText : ""),
                            1,
                            Constant.getCurrentDate(),
                            emojiModels,
                            "",
                            Constant.getCurrentTimestamp(), imageWidthDp, imageHeightDp, aspectRatio, "1"
                    );
                }
            } else {


                model = new messageModel(
                        createdBy,
                        captionText != null ? captionText : "",
                        currentDateTimeString,
                        xFilename,
                        dataType,
                        extension != null ? extension : getFileExtension(uri),
                        contactName != null ? contactName : "",
                        contactPhone != null ? contactPhone : "",
                        "",
                        audioTime != null ? audioTime : "",
                        Constant.getSF.getString(Constant.full_name, ""),
                        replytextData,
                        replyKey,
                        replyType,
                        replyOldData,
                        replyCrtPostion,
                        modelId,
                        receiverUid,
                        forwaredKey,
                        "",
                        docSize,
                        xFilename,
                        "",
                        fileThumbName != null ? fileThumbName : "",
                        dataType.equals(Constant.Text) ? "" : (captionText != null ? captionText : ""),
                        1,
                        Constant.getCurrentDate(),
                        emojiModels,
                        "",
                        Constant.getCurrentTimestamp(),imageWidthDp,imageHeightDp,aspectRatio, "1"
                );
            }

            // Start upload
            Log.d("bunch###", "UploadChatHelper: Reached upload section, about to create finalModel");
            messageModel finalModel = model;
            File finalSavedThumbnail = savedThumbnail;
            Log.d("bunch###", "UploadChatHelper: About to post to mainHandler - existingModel=" + (existingModel != null) +
                    ", selectionBunch=" + (existingModel != null && existingModel.getSelectionBunch() != null ? existingModel.getSelectionBunch().size() : "null") +
                    ", filePaths=" + selectionBunchFilePaths.size());
            mainHandler.post(() -> {
                Log.d("bunch###", "UploadChatHelper: Inside mainHandler.post - checking conditions");
                if (existingModel != null && existingModel.getSelectionBunch() != null && !existingModel.getSelectionBunch().isEmpty() && !selectionBunchFilePaths.isEmpty()) {
                    Log.d("bunch###", "UploadChatHelper: Starting selectionBunch upload");
                    startSelectionBunchUpload(uploadType, existingModel, fileSize, fileSize2, finalSavedThumbnail, fileThumbName, xFilename, emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio);
                } else {
                    Log.d("bunch###", "UploadChatHelper: Falling back to regular upload - existingModel=" + (existingModel != null) +
                            ", selectionBunch=" + (existingModel != null && existingModel.getSelectionBunch() != null ? existingModel.getSelectionBunch().size() : "null") +
                            ", filePaths=" + selectionBunchFilePaths.size());
                    startBackgroundService(uploadType, finalModel, fileSize, fileSize2, finalSavedThumbnail, fileThumbName, xFilename, emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio);
                }
            });
        } catch (Exception e) {
            Log.e("bunch###", "UploadChatHelper: Exception in uploadContent: " + e.getMessage(), e);
            Log.e(TAG, "Failed to process upload: " + e.getMessage(), e);
            //mainHandler.post(() -> showSnackbar("Failed to process: " + e.getMessage()));
            // Clean up any created files
//            if (globalFile != null && globalFile.exists()) globalFile.delete();
//            if (fullImageFile != null && fullImageFile.exists()) fullImageFile.delete();
//            if (savedThumbnail != null && savedThumbnail.exists()) savedThumbnail.delete();
        }
    }

    private void startSelectionBunchUpload(String uploadType, messageModel model, long fileSize, long fileSize2,
                                           File savedThumbnail, String fileThumbName, String fileName, String emojiModelJson,
                                           String captionText, String imageWidthDp, String imageHeightDp, String aspectRatio) {
        try {
            Log.d("bunch###", "=== startSelectionBunchUpload START ===");
            Log.d("bunch###", "selectionBunchFilePaths size: " + selectionBunchFilePaths.size());
            Log.d("bunch###", "model.getSelectionBunch() size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : "null"));
            Log.d("bunch###", "existingModel.getSelectionBunch() size: " + (existingModel != null && existingModel.getSelectionBunch() != null ? existingModel.getSelectionBunch().size() : "null"));
            Log.d("bunch###", "model == existingModel: " + (model == existingModel));
            Log.d(TAG, "=== startSelectionBunchUpload DEBUG ===");
            Log.d(TAG, "selectionBunchFilePaths size before upload: " + selectionBunchFilePaths.size());
            Log.d(TAG, "model.getSelectionBunch() size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : "null"));
            Log.d(TAG, "model.getSelectionCount(): " + model.getSelectionCount());
            Log.d(TAG, "model.getDataType(): " + model.getDataType());

            // Debug: Check if this is the existingModel
            Log.d("SelectionBunch", "startSelectionBunchUpload: model == existingModel: " + (model == existingModel));
            Log.d("SelectionBunch", "startSelectionBunchUpload: existingModel.getSelectionBunch() size: " + (existingModel != null && existingModel.getSelectionBunch() != null ? existingModel.getSelectionBunch().size() : "null"));

            // Debug: Check the actual content of selectionBunch
            if (model.getSelectionBunch() != null) {
                Log.d("SelectionBunch", "startSelectionBunchUpload: model.getSelectionBunch() content:");
                for (int i = 0; i < model.getSelectionBunch().size(); i++) {
                    com.Appzia.enclosure.Model.selectionBunchModel bunch = model.getSelectionBunch().get(i);
                    Log.d("SelectionBunch", "  Item " + i + ": fileName=" + bunch.getFileName() + ", imgUrl=" + bunch.getImgUrl());
                }
            } else {
                Log.d("SelectionBunch", "startSelectionBunchUpload: model.getSelectionBunch() is null");
            }

            // If the passed model doesn't have selectionBunch but existingModel does, use existingModel
            if ((model.getSelectionBunch() == null || model.getSelectionBunch().isEmpty()) &&
                    existingModel != null && existingModel.getSelectionBunch() != null && !existingModel.getSelectionBunch().isEmpty()) {
                Log.d("SelectionBunch", "Using existingModel instead of passed model for selectionBunch");
                model = existingModel;
            }

            if (selectionBunchFilePaths.isEmpty()) {
                Log.w("bunch###", "selectionBunchFilePaths is empty, checking fallback paths");
                ArrayList<String> fallbackPaths = new ArrayList<>();
                if (globalFile != null && globalFile.exists()) {
                    fallbackPaths.add(globalFile.getAbsolutePath());
                }
                if (fullImageFile != null && fullImageFile.exists()) {
                    fallbackPaths.add(fullImageFile.getAbsolutePath());
                }
                if (!fallbackPaths.isEmpty()) {
                    Log.w("bunch###", "selectionBunchFilePaths empty; using fallback paths: " + fallbackPaths);
                    Log.w(TAG, "selectionBunchFilePaths empty; using fallback paths: " + fallbackPaths);
                    selectionBunchFilePaths = fallbackPaths;
                } else {
                    Log.w("bunch###", "SelectionBunch upload invoked without file paths; falling back to standard upload");
                    Log.w(TAG, "SelectionBunch upload invoked without file paths; falling back to standard upload");
                    startBackgroundService(uploadType, model, fileSize, fileSize2, savedThumbnail, fileThumbName, fileName, emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio);
                    return;
                }
            }

            if (model.getSelectionBunch() == null || model.getSelectionBunch().isEmpty()) {
                Log.w(TAG, "SelectionBunch upload invoked without selectionBunch entries; falling back to standard upload");
                Log.w(TAG, "model.getSelectionBunch() is null: " + (model.getSelectionBunch() == null));
                Log.w(TAG, "model.getSelectionBunch() is empty: " + (model.getSelectionBunch() != null && model.getSelectionBunch().isEmpty()));
                Log.w(TAG, "This is why selectionBunch is not being sent!");
                startBackgroundService(uploadType, model, fileSize, fileSize2, savedThumbnail, fileThumbName, fileName, emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio);
                return;
            }

            Log.d(TAG, "startSelectionBunchUpload: uploading " + selectionBunchFilePaths.size() + " images before database write");
            Log.d("bunch###", "About to call uploadSelectionBunchFileAtIndex with model.getSelectionBunch() size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : "null"));

            uploadSelectionBunchFileAtIndex(0, uploadType, model, fileSize, fileSize2, savedThumbnail, fileThumbName, fileName,
                    emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio, new ArrayList<>(), new ArrayList<>());
        } catch (Exception e) {
            Log.e("bunch###", "Exception in startSelectionBunchUpload: " + e.getMessage(), e);
            Log.e(TAG, "Exception in startSelectionBunchUpload: " + e.getMessage(), e);
        }
    }

    private void uploadSelectionBunchFileAtIndex(int index, String uploadType, messageModel baseModel,
                                                 long fileSize, long fileSize2, File savedThumbnail, String fileThumbName, String fileName,
                                                 String emojiModelJson, String captionText, String imageWidthDp, String imageHeightDp,
                                                 String aspectRatio, ArrayList<com.Appzia.enclosure.Model.selectionBunchModel> updatedBunch,
                                                 ArrayList<String> uploadedUrls) {
        Log.d("bunch###", "=== uploadSelectionBunchFileAtIndex START === index=" + index + ", selectionBunchFilePaths.size()=" + selectionBunchFilePaths.size() + ", baseModel.getSelectionBunch().size()=" + (baseModel.getSelectionBunch() != null ? baseModel.getSelectionBunch().size() : "null"));
        if (index >= selectionBunchFilePaths.size() || index >= baseModel.getSelectionBunch().size()) {
            Log.d(TAG, "All selectionBunch files uploaded, proceeding to start service");
            baseModel.setSelectionBunch(updatedBunch);

            // Debug final selectionBunch state
            Log.d("SelectionBunch", "Final selectionBunch state before sending to service:");
            for (int i = 0; i < updatedBunch.size(); i++) {
                com.Appzia.enclosure.Model.selectionBunchModel bunch = updatedBunch.get(i);
                Log.d("SelectionBunch", "Final item " + i + ": fileName=" + bunch.getFileName() + ", imgUrl=" + bunch.getImgUrl());
            }

            Intent serviceIntent = createServiceIntent(uploadType, baseModel, fileSize, fileSize2, savedThumbnail, fileThumbName, fileName, emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio);
            if (!uploadedUrls.isEmpty()) {
                String primaryUrl = uploadedUrls.get(0);
                baseModel.setDocument(primaryUrl);
                if (existingModel != null) {
                    existingModel.setDocument(primaryUrl);
                }
                serviceIntent.putExtra("selectionBunchPreUploaded", true);
                serviceIntent.putStringArrayListExtra("selectionBunchFirebaseUrls", uploadedUrls);
            }
            // Don't overwrite existingModel - it contains the original selectionBunch data
            startServiceIntent(serviceIntent, uploadType);
            return;
        }

        String localPath = selectionBunchFilePaths.get(index);
        com.Appzia.enclosure.Model.selectionBunchModel bunchEntry = baseModel.getSelectionBunch().get(index);

        File file = new File(localPath);
        if (!file.exists() || file.length() == 0) {
            Log.w(TAG, "SelectionBunch file missing or empty: " + localPath + ". Skipping this file.");
            updatedBunch.add(bunchEntry);
            uploadSelectionBunchFileAtIndex(index + 1, uploadType, baseModel, fileSize, fileSize2, savedThumbnail, fileThumbName,
                    fileName, emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio, updatedBunch, uploadedUrls);
            return;
        }

        String extension = "";
        int dotIndex = file.getName().lastIndexOf('.');
        if (dotIndex != -1 && dotIndex < file.getName().length() - 1) {
            extension = file.getName().substring(dotIndex + 1);
        }

        String uniqueFileName = baseModel.getUid() + "_" + System.currentTimeMillis() + "_" + index + (extension.isEmpty() ? "" : "." + extension);
        StorageReference fileRef = FirebaseStorage.getInstance().getReference(Constant.CHAT).child(uniqueFileName);

        Log.d(TAG, "Uploading selectionBunch file " + (index + 1) + " / " + selectionBunchFilePaths.size() + ": " + file.getAbsolutePath());
        Log.d(TAG, "File exists: " + file.exists() + ", Size: " + file.length() + " bytes");
        Log.d(TAG, "Uploading to Firebase path: " + uniqueFileName);

        fileRef.putFile(Uri.fromFile(file))
                .addOnSuccessListener(taskSnapshot -> getDownloadUrlWithRetry(fileRef, 1, 5, 300,
                        uri -> {
                            Log.d(TAG, "SelectionBunch file uploaded: " + file.getAbsolutePath() + " -> " + uri.toString());
                            Log.d(TAG, "Uploaded file size: " + taskSnapshot.getBytesTransferred() + " bytes");
                            String downloadUrl = uri.toString();
                            bunchEntry.setImgUrl(downloadUrl);
                            Log.d("SelectionBunch", "Set imgUrl for " + bunchEntry.getFileName() + " to: " + downloadUrl);
                            updatedBunch.add(bunchEntry);
                            uploadedUrls.add(downloadUrl);

                            // Update the backing model with the primary file URL & dimensions if this is the first image
                            if (index == 0) {
                                baseModel.setDocument(downloadUrl);
                                if (existingModel != null) {
                                    existingModel.setDocument(downloadUrl);
                                    if (imageWidthDp != null && !imageWidthDp.isEmpty()) {
                                        existingModel.setImageWidth(imageWidthDp);
                                    }
                                    if (imageHeightDp != null && !imageHeightDp.isEmpty()) {
                                        existingModel.setImageHeight(imageHeightDp);
                                    }
                                    if (aspectRatio != null && !aspectRatio.isEmpty()) {
                                        existingModel.setAspectRatio(aspectRatio);
                                    }
                                }
                            }

                            uploadSelectionBunchFileAtIndex(index + 1, uploadType, baseModel, fileSize, fileSize2, savedThumbnail,
                                    fileThumbName, fileName, emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio, updatedBunch, uploadedUrls);
                        },
                        error -> {
                            Log.e(TAG, "Failed to get download URL for selectionBunch file: " + error.getMessage());
                            bunchEntry.setImgUrl("");
                            updatedBunch.add(bunchEntry);
                            uploadSelectionBunchFileAtIndex(index + 1, uploadType, baseModel, fileSize, fileSize2, savedThumbnail,
                                    fileThumbName, fileName, emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio, updatedBunch, uploadedUrls);
                        }))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "SelectionBunch file upload failed: " + e.getMessage());
                    bunchEntry.setImgUrl("");
                    updatedBunch.add(bunchEntry);
                    uploadSelectionBunchFileAtIndex(index + 1, uploadType, baseModel, fileSize, fileSize2, savedThumbnail,
                            fileThumbName, fileName, emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio, updatedBunch, uploadedUrls);
                });
    }

    private void startBackgroundService(String uploadType, messageModel model, long fileSize, long fileSize2,
                                        File savedThumbnail, String fileThumbName, String fileName, String emojiModelJson, String captionText, String imageWidthDp, String imageHeightDp, String aspectRatio) {
        // Battery optimization check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            String packageName = mContext.getPackageName();
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                // Only prompt if context is an Activity; otherwise skip to avoid UI jump from notification reply
                if (mContext instanceof Activity) {
                    Intent batteryIntent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    batteryIntent.setData(Uri.parse("package:" + packageName));
                    try {
                        SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, batteryIntent);
                    } catch (Exception e) {
                        Log.e(TAG, "Battery optimization intent failed: " + e.getMessage(), e);
                    }
                } else {
                    Log.d(TAG, "Skipping battery optimization prompt (non-Activity context)");
                }
            }
        }

        // Validate files before starting service (log warnings but proceed)
        File fileToUpload = fullImageFile != null && fullImageFile.exists() ? fullImageFile : globalFile;
        if (!uploadType.equals(Constant.Text) && !uploadType.equals(Constant.contact)) {
            if (fileToUpload == null || !fileToUpload.exists() || fileToUpload.length() == 0) {
                Log.w(TAG, "File not found or empty before starting service: " + (fileToUpload != null ? fileToUpload.getAbsolutePath() : "null") + ", proceeding without file");
                fileToUpload = null;
            } else if (!fileToUpload.canRead()) {
                Log.w(TAG, "File is not readable: " + fileToUpload.getAbsolutePath() + ", proceeding without file");
                fileToUpload = null;
            } else if (fileToUpload.length() > 200 * 1024 * 1024) {
                Log.e(TAG, "File exceeds 200MB: " + fileToUpload.getAbsolutePath());
                //  mainHandler.post(() -> showSnackbar("File exceeds 200MB limit"));
                return;
            }
            if (uploadType.equals(Constant.video) && (savedThumbnail == null || !savedThumbnail.exists() || savedThumbnail.length() == 0)) {
                Log.w(TAG, "Thumbnail not found or empty: " + (savedThumbnail != null ? savedThumbnail.getAbsolutePath() : "null") + ", proceeding without thumbnail");
                savedThumbnail = null;
            }
        }

        startServiceIntent(createServiceIntent(uploadType, model, fileSize, fileSize2, savedThumbnail, fileThumbName, fileName, emojiModelJson, captionText, imageWidthDp, imageHeightDp, aspectRatio), uploadType);
    }

    private Intent createServiceIntent(String uploadType, messageModel model, long fileSize, long fileSize2,
                                       File savedThumbnail, String fileThumbName, String fileName, String emojiModelJson, String captionText, String imageWidthDp, String imageHeightDp, String aspectRatio) {
        Constant.setSfFunction(mContext);
        Intent serviceIntent = new Intent(mContext, MessageUploadService.class);
        boolean needsForeground = !(uploadType.equals(Constant.Text) || uploadType.equals(Constant.contact));
        serviceIntent.putExtra("uploadType", uploadType);
        serviceIntent.putExtra("uid", createdBy);
        serviceIntent.putExtra("receiverUid", model.getReceiverUid());
        serviceIntent.putExtra("message", model.getMessage());

        if (uploadType.equals(Constant.doc)) {
            Log.d(TAG, "startBackgroundService: dataType set to " + Constant.doc);
            serviceIntent.putExtra("dataType", Constant.doc);
        } else if (uploadType.equals(Constant.contact)) {
            serviceIntent.putExtra("dataType", Constant.contact);
        } else {
            serviceIntent.putExtra("dataType", model.getDataType());
        }

        serviceIntent.putExtra("modelId", model.getModelId());
        serviceIntent.putExtra("userName", model.getUserName());
        serviceIntent.putExtra("time", model.getTime());
        serviceIntent.putExtra("extension", model.getExtension());
        serviceIntent.putExtra("name", model.getName());
        serviceIntent.putExtra("phone", model.getPhone());
        serviceIntent.putExtra("micPhoto", Constant.getSF.getString(Constant.profilePic, ""));
        serviceIntent.putExtra("miceTiming", model.getMiceTiming());
        // Enhanced file path validation and logging
        String filePath = "";
        String fullImageFilePath = "";
        String thumbnailPath = "";

        if (globalFile != null && globalFile.exists()) {
            filePath = globalFile.getAbsolutePath();
            Log.d("ImageUpload", "Global file path: " + filePath + ", size: " + globalFile.length());
        } else {
            Log.w("ImageUpload", "Global file is null or doesn't exist: " + (globalFile != null ? globalFile.getAbsolutePath() : "null"));
        }

        if (fullImageFile != null && fullImageFile.exists()) {
            fullImageFilePath = fullImageFile.getAbsolutePath();
            Log.d("ImageUpload", "Full image file path: " + fullImageFilePath + ", size: " + fullImageFile.length());
        } else {
            Log.w("ImageUpload", "Full image file is null or doesn't exist: " + (fullImageFile != null ? fullImageFile.getAbsolutePath() : "null"));
        }

        if (savedThumbnail != null && savedThumbnail.exists()) {
            thumbnailPath = savedThumbnail.getAbsolutePath();
            Log.d("ImageUpload", "Thumbnail path: " + thumbnailPath + ", size: " + savedThumbnail.length());
        } else {
            Log.w("ImageUpload", "Thumbnail is null or doesn't exist: " + (savedThumbnail != null ? savedThumbnail.getAbsolutePath() : "null"));
        }

        serviceIntent.putExtra("filePath", filePath);
        serviceIntent.putExtra("fullImageFilePath", fullImageFilePath);
        serviceIntent.putExtra("thumbnailPath", thumbnailPath);
        serviceIntent.putExtra("fileName", uploadType.equals(Constant.doc) ? xFilename : model.getFileName());
        serviceIntent.putExtra("fileNameThumbnail", model.getFileNameThumbnail());
        serviceIntent.putExtra("userFTokenKey", userFTokenKey);
        serviceIntent.putExtra("deviceType", "1");
        serviceIntent.putExtra("replytextData", model.getReplytextData());
        serviceIntent.putExtra("replyKey", model.getReplyKey());
        serviceIntent.putExtra("replyType", model.getReplyType());
        serviceIntent.putExtra("replyOldData", model.getReplyOldData());
        serviceIntent.putExtra("replyCrtPostion", model.getReplyCrtPostion());
        serviceIntent.putExtra("forwaredKey", model.getForwaredKey());
        serviceIntent.putExtra("groupName", model.getGroupName());

        Log.d(TAG, "startBackgroundService: caption " + captionText);
        Log.d(TAG, "Putting caption in service intent: '" + captionText + "'");
        serviceIntent.putExtra("caption", captionText);
        serviceIntent.putExtra("notification", model.getNotification());
        serviceIntent.putExtra("currentDate", model.getCurrentDate());
        serviceIntent.putExtra("docSize", model.getDocSize());
        serviceIntent.putExtra("thumbnail", model.getThumbnail());
        serviceIntent.putExtra("emojiModelJson", emojiModelJson);
        serviceIntent.putExtra("emojiCount", model.getEmojiCount());
        serviceIntent.putExtra("timestamp", model.getTimestamp());
        serviceIntent.putExtra("imageWidthDp", imageWidthDp);
        serviceIntent.putExtra("imageHeightDp", imageHeightDp);
        serviceIntent.putExtra("aspectRatio", aspectRatio);
        serviceIntent.putExtra("selectionCount", model.getSelectionCount());
        if (model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty()) {
            String selectionBunchJson = gson.toJson(model.getSelectionBunch());
            serviceIntent.putExtra("selectionBunch", selectionBunchJson);
            Log.d(TAG, "Added selectionBunch to service intent: " + selectionBunchJson);
        } else {
            serviceIntent.putExtra("selectionBunch", "");
            Log.d(TAG, "selectionBunch is null or empty, sending empty string");
        }
        serviceIntent.putExtra("fileSize", fileSize > 0 ? fileSize : fileSize2);
        serviceIntent.putExtra("needsForeground", needsForeground);
        return serviceIntent;
    }

    private void startServiceIntent(Intent serviceIntent, String uploadType) {
        boolean needsForeground = serviceIntent.getBooleanExtra("needsForeground", true);
        Log.d(TAG, "Starting MessageUploadService: uploadType=" + uploadType + ", fileName=" + serviceIntent.getStringExtra("fileName") + ", filePath=" +
                (globalFile != null ? globalFile.getAbsolutePath() : "null") + ", fullImageFilePath=" +
                (fullImageFile != null ? fullImageFile.getAbsolutePath() : "null") + ", thumbnailPath=" +
                (serviceIntent.getStringExtra("thumbnailPath") != null ? serviceIntent.getStringExtra("thumbnailPath") : "null"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (needsForeground) {
                mContext.startForegroundService(serviceIntent);
            } else {
                mContext.startService(serviceIntent);
            }
        } else {
            mContext.startService(serviceIntent);
        }
    }

    private File createFileFromUri(Uri uri, String fileName) throws IOException {
        if (uri == null) {
            throw new IOException("URI is null");
        }

        File storageDir = new File(mContext.getFilesDir(), STORAGE_SUBDIR);
        if (!storageDir.exists() && !storageDir.mkdirs()) {
            throw new IOException("Failed to create storage subdirectory: " + storageDir.getAbsolutePath());
        }

        String extension = getFileExtension(uri);
        if (extension == null || extension.isEmpty()) {
            extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "tmp";
            Log.w(TAG, "No valid extension found for URI: " + uri + ", using: " + extension);
        }
        if (!fileName.contains(".")) {
            fileName = fileName + "." + extension;
        }

        File tempFile = new File(storageDir, "temp_" + System.currentTimeMillis() + "_" + fileName);
        File finalFile = new File(storageDir, fileName);

        try (InputStream inputStream = mContext.getContentResolver().openInputStream(uri)) {
            if (inputStream == null) {
                throw new IOException("Unable to open input stream for URI: " + uri);
            }
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                long totalBytes = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                }
                outputStream.flush();
                if (totalBytes == 0) {
                    Log.w(TAG, "No data written to file: " + tempFile.getAbsolutePath() + ", proceeding without file");
                    tempFile.delete();
                    return null; // Return null instead of throwing exception
                }
            }
        } catch (IOException e) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            Log.e(TAG, "Error creating file from URI: " + uri + ", error: " + e.getMessage(), e);
            throw e;
        }

        if (!tempFile.renameTo(finalFile)) {
            tempFile.delete();
            throw new IOException("Failed to rename temporary file to: " + finalFile.getAbsolutePath());
        }

        if (!finalFile.exists() || finalFile.length() == 0) {
            Log.w(TAG, "Created file is empty or does not exist: " + finalFile.getAbsolutePath() + ", proceeding without file");
            finalFile.delete();
            return null; // Return null instead of throwing exception
        }

        if (finalFile.length() > 200 * 1024 * 1024) {
            finalFile.delete();
            throw new IOException("Created file exceeds 200MB: " + finalFile.getAbsolutePath());
        }

        Log.d(TAG, "File created successfully: " + finalFile.getAbsolutePath() + ", size: " + finalFile.length());
        return finalFile;
    }

    private long getFileSize(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return 0;
        }
        Long cachedSize = fileSizeCache.get(filePath);
        if (cachedSize != null) {
            return cachedSize;
        }
        File file = new File(filePath);
        long size = file.exists() && file.canRead() ? file.length() : 0;
        fileSizeCache.put(filePath, size);
        return size;
    }

    private String getFormattedFileSize(long size) {
        if (size <= 0) return "";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.2f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    private String getFileExtension(Uri uri) {
        if (uri == null) return "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (extension != null && !extension.isEmpty()) {
            return extension;
        }
        String mimeType = mContext.getContentResolver().getType(uri);
        extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        return extension != null ? extension : "";
    }

    private String safeString(String input) {
        return input != null ? input : "";
    }

    private void getDownloadUrlWithRetry(StorageReference ref,
                                         int attempt,
                                         int maxAttempts,
                                         long delayMs,
                                         OnSuccessListener<Uri> onSuccess,
                                         OnFailureListener onFailure) {
        ref.getDownloadUrl()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(error -> {
                    if (attempt >= maxAttempts) {
                        onFailure.onFailure(error);
                    } else {
                        long nextDelay = delayMs * 2;
                        mainHandler.postDelayed(() ->
                                getDownloadUrlWithRetry(ref, attempt + 1, maxAttempts, nextDelay, onSuccess, onFailure), delayMs);
                    }
                });
    }


    private void displayNextSnackbar() {
        if (pendingSnackbars.isEmpty()) {
            isShowingSnackbar = false;
            return;
        }
        isShowingSnackbar = true;
        String message = pendingSnackbars.remove(0);
        int duration = Snackbar.LENGTH_LONG;
        Snackbar snackbar = Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), message, duration);
        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                displayNextSnackbar();
            }
        });
        snackbar.show();
    }
}