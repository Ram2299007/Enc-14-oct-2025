package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.util.Log;
import com.Appzia.enclosure.Utils.BroadcastReiciver.UploadChatHelper;
import com.Appzia.enclosure.Utils.BroadcastReiciver.MessageUploadService;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.group_messageModel;
import com.Appzia.enclosure.Utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import android.content.Intent;
import android.os.Build;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Utility class to handle automatic upload of pending messages when internet connectivity is restored
 */
public class PendingMessageUploader {
    private static final String TAG = "PendingMessageUploader";
    private final Context mContext;
    private final DatabaseHelper databaseHelper;
    private final ExecutorService executorService;
    private final FirebaseDatabase firebaseDatabase;
    private BroadcastReceiver uploadSuccessReceiver;

    public PendingMessageUploader(Context context) {
        this.mContext = context;
        this.databaseHelper = new DatabaseHelper(context);
        this.executorService = Executors.newFixedThreadPool(2);
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        setupUploadSuccessReceiver();
    }

    /**
     * Check and upload all pending messages for individual chats
     */
    public void uploadPendingIndividualMessages(String receiverUid, String userFTokenKey) {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Checking pending individual messages for receiver: " + receiverUid);
                List<messageModel> pendingMessages = databaseHelper.getPendingMessages(receiverUid);
                
                if (pendingMessages != null && !pendingMessages.isEmpty()) {
                    Log.d(TAG, "Found " + pendingMessages.size() + " pending individual messages");
                    
                    for (messageModel model : pendingMessages) {
                        // Check if message exists in Firebase before uploading
                        checkAndUploadIndividualMessage(model, receiverUid, userFTokenKey);
                    }
                } else {
                    Log.d(TAG, "No pending individual messages found");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error uploading pending individual messages: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Check and upload all pending messages for group chats
     */
    public void uploadPendingGroupMessages(String grpIdKey, String userFTokenKey) {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Checking pending group messages for group: " + grpIdKey);
                List<group_messageModel> pendingGroupMessages = databaseHelper.getPendingGroupMessages(grpIdKey);
                
                if (pendingGroupMessages != null && !pendingGroupMessages.isEmpty()) {
                    Log.d(TAG, "Found " + pendingGroupMessages.size() + " pending group messages");
                    
                    for (group_messageModel model : pendingGroupMessages) {
                        // Check if message exists in Firebase before uploading
                        checkAndUploadGroupMessage(model, grpIdKey, userFTokenKey);
                    }
                } else {
                    Log.d(TAG, "No pending group messages found");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error uploading pending group messages: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Check if individual message exists in Firebase and upload if not
     */
    private void checkAndUploadIndividualMessage(messageModel model, String receiverUid, String userFTokenKey) {
        try {
            String senderRoom = receiverUid + "_" + model.getUid();
            DatabaseReference messageRef = firebaseDatabase.getReference("chats")
                    .child(senderRoom)
                    .child(model.getModelId());

            messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Log.d(TAG, "Message " + model.getModelId() + " not found in Firebase, uploading...");
                        uploadIndividualMessage(model, userFTokenKey);
                    } else {
                        Log.d(TAG, "Message " + model.getModelId() + " already exists in Firebase, skipping");
                        // Update status to completed
                        databaseHelper.updatePendingMessageStatus(model.getModelId(), receiverUid, 2);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e(TAG, "Error checking message existence: " + error.getMessage());
                    // Try to upload anyway if check fails
                    uploadIndividualMessage(model, userFTokenKey);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error checking individual message existence: " + e.getMessage(), e);
        }
    }

    /**
     * Check if group message exists in Firebase and upload if not
     */
    private void checkAndUploadGroupMessage(group_messageModel model, String grpIdKey, String userFTokenKey) {
        try {
            DatabaseReference messageRef = firebaseDatabase.getReference("group_chats")
                    .child(grpIdKey)
                    .child(model.getModelId());

            messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Log.d(TAG, "Group message " + model.getModelId() + " not found in Firebase, uploading...");
                        uploadGroupMessage(model, userFTokenKey);
                    } else {
                        Log.d(TAG, "Group message " + model.getModelId() + " already exists in Firebase, skipping");
                        // Update status to completed
                        databaseHelper.updatePendingGroupMessageStatus(model.getModelId(), grpIdKey, 2);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e(TAG, "Error checking group message existence: " + error.getMessage());
                    // Try to upload anyway if check fails
                    uploadGroupMessage(model, userFTokenKey);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error checking group message existence: " + e.getMessage(), e);
        }
    }

    /**
     * Upload individual message using MessageUploadService
     */
    private void uploadIndividualMessage(messageModel model, String userFTokenKey) {
        try {
            Log.d(TAG, "Uploading individual message: " + model.getModelId());
            // 1) Pre-intent: reconstruct selection-bunch file paths and ensure model.document points to a file
            if (model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty()) {
                Log.d(TAG, "(pre-intent) Selection bunch detected: " + model.getSelectionBunch().size());
                for (int i = 0; i < model.getSelectionBunch().size(); i++) {
                    com.Appzia.enclosure.Model.selectionBunchModel item = model.getSelectionBunch().get(i);
                    if (item.getImgUrl() == null || item.getImgUrl().isEmpty()) {
                        String reconstructedPath = reconstructFilePath(item.getFileName());
                        if (reconstructedPath != null && new File(reconstructedPath).exists()) {
                            item.setImgUrl(reconstructedPath);
                            Log.d(TAG, "(pre-intent) Reconstructed path for item " + i + ": " + reconstructedPath);
                        } else {
                            Log.w(TAG, "(pre-intent) Could not reconstruct path for item " + i + ": " + item.getFileName());
                        }
                    }
                }
                if (model.getDocument() == null || model.getDocument().trim().isEmpty() || new File(model.getDocument()).isDirectory()) {
                    for (int i = 0; i < model.getSelectionBunch().size(); i++) {
                        String candidate = model.getSelectionBunch().get(i).getImgUrl();
                        if (candidate != null && !candidate.trim().isEmpty() && new File(candidate).isFile()) {
                            model.setDocument(candidate);
                            if (model.getFileName() == null || model.getFileName().trim().isEmpty()) {
                                String fn = model.getSelectionBunch().get(i).getFileName();
                                if (fn == null || fn.trim().isEmpty()) {
                                    int slash = candidate.lastIndexOf('/');
                                    fn = slash >= 0 ? candidate.substring(slash + 1) : candidate;
                                }
                                model.setFileName(fn);
                            }
                            Log.d(TAG, "(pre-intent) Set model.document=" + candidate);
                            break;
                        }
                    }
                }
            }

            // 2) Pre-intent: Calculate and set image dimensions if they are empty
            if ((model.getImageWidth() == null || model.getImageWidth().trim().isEmpty() || 
                 model.getImageHeight() == null || model.getImageHeight().trim().isEmpty()) &&
                model.getDocument() != null && !model.getDocument().trim().isEmpty()) {
                
                File imageFile = new File(model.getDocument());
                if (imageFile.exists()) {
                    try {
                        String[] dimensions = com.Appzia.enclosure.Utils.Constant.calculateImageDimensions(mContext, imageFile, android.net.Uri.fromFile(imageFile));
                        if (dimensions.length >= 3) {
                            model.setImageWidth(dimensions[0]);
                            model.setImageHeight(dimensions[1]);
                            model.setAspectRatio(dimensions[2]);
                            Log.d(TAG, "(pre-intent) Calculated dimensions - width: " + dimensions[0] + ", height: " + dimensions[1] + ", aspectRatio: " + dimensions[2]);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "(pre-intent) Error calculating image dimensions: " + e.getMessage(), e);
                    }
                }
            }

            // Create service intent for MessageUploadService
            Intent serviceIntent = new Intent(mContext, MessageUploadService.class);
            
            // Set all required parameters
            serviceIntent.putExtra("uploadType", model.getDataType());
            serviceIntent.putExtra("uid", model.getUid());
            serviceIntent.putExtra("receiverUid", model.getReceiverUid());
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
            serviceIntent.putExtra("userFTokenKey", userFTokenKey);
            serviceIntent.putExtra("deviceType", "1");
            
            // File paths (after reconstruction)
            String putFilePath = model.getDocument() != null ? model.getDocument() : "";
            String putFullImageFilePath = model.getMicPhoto() != null ? model.getMicPhoto() : "";
            String putThumbnailPath = model.getThumbnail() != null ? model.getThumbnail() : "";
            serviceIntent.putExtra("filePath", putFilePath);
            serviceIntent.putExtra("fullImageFilePath", putFullImageFilePath);
            serviceIntent.putExtra("thumbnailPath", putThumbnailPath);
            
            // Additional fields
            serviceIntent.putExtra("caption", model.getCaption());
            serviceIntent.putExtra("fileName", model.getFileName());
            serviceIntent.putExtra("docSize", model.getDocSize());
            serviceIntent.putExtra("imageWidthDp", model.getImageWidth());
            serviceIntent.putExtra("imageHeightDp", model.getImageHeight());
            serviceIntent.putExtra("aspectRatio", model.getAspectRatio());
            
            // Selection bunch parameters (critical for multi-selection messages)
            serviceIntent.putExtra("selectionCount", model.getSelectionCount());
            if (model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty()) {
                Gson gson = new Gson();
                String selectionBunchJson = gson.toJson(model.getSelectionBunch());
                serviceIntent.putExtra("selectionBunch", selectionBunchJson);
                Log.d(TAG, "SelectionBunch JSON: " + selectionBunchJson);
            }

            // Log the exact extras being sent
            Log.d(TAG, "put filePath=" + putFilePath + ", fullImageFilePath=" + putFullImageFilePath + ", thumbnailPath=" + putThumbnailPath);
            
            // Start the service
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(serviceIntent);
            } else {
                mContext.startService(serviceIntent);
            }
            
            Log.d(TAG, "Individual message upload service started: " + model.getModelId());
            
            // Debug resolved file paths (from model)
            String dbgFilePath = model.getDocument() != null ? model.getDocument() : "";
            String dbgFullImage = model.getMicPhoto() != null ? model.getMicPhoto() : "";
            String dbgThumb = model.getThumbnail() != null ? model.getThumbnail() : "";
            Log.d(TAG, "Individual File paths - filePath: " + dbgFilePath + ", exists: " + (dbgFilePath.isEmpty() ? "empty" : new File(dbgFilePath).exists()));
            Log.d(TAG, "Individual File paths - fullImageFilePath: " + dbgFullImage + ", exists: " + (dbgFullImage.isEmpty() ? "empty" : new File(dbgFullImage).exists()));
            Log.d(TAG, "Individual File paths - thumbnailPath: " + dbgThumb + ", exists: " + (dbgThumb.isEmpty() ? "empty" : new File(dbgThumb).exists()));
            
        } catch (Exception e) {
            Log.e(TAG, "Error uploading individual message: " + e.getMessage(), e);
        }
    }

    /**
     * Upload group message using MessageUploadService
     */
    private void uploadGroupMessage(group_messageModel model, String userFTokenKey) {
        try {
            Log.d(TAG, "Uploading group message: " + model.getModelId());
            
            // Pre-intent: Calculate and set image dimensions if they are empty
            if ((model.getImageWidth() == null || model.getImageWidth().trim().isEmpty() || 
                 model.getImageHeight() == null || model.getImageHeight().trim().isEmpty()) &&
                model.getDocument() != null && !model.getDocument().trim().isEmpty()) {
                
                File imageFile = new File(model.getDocument());
                if (imageFile.exists()) {
                    try {
                        String[] dimensions = com.Appzia.enclosure.Utils.Constant.calculateImageDimensions(mContext, imageFile, android.net.Uri.fromFile(imageFile));
                        if (dimensions.length >= 3) {
                            model.setImageWidth(dimensions[0]);
                            model.setImageHeight(dimensions[1]);
                            model.setAspectRatio(dimensions[2]);
                            Log.d(TAG, "(pre-intent) Calculated group dimensions - width: " + dimensions[0] + ", height: " + dimensions[1] + ", aspectRatio: " + dimensions[2]);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "(pre-intent) Error calculating group image dimensions: " + e.getMessage(), e);
                    }
                }
            }
            
            // Create service intent for MessageUploadService
            Intent serviceIntent = new Intent(mContext, MessageUploadService.class);
            
            // Set all required parameters
            serviceIntent.putExtra("uploadType", model.getDataType());
            serviceIntent.putExtra("uid", model.getUid());
            serviceIntent.putExtra("receiverUid", model.getReceiverUid());
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
            serviceIntent.putExtra("userFTokenKey", userFTokenKey);
            serviceIntent.putExtra("deviceType", "1");
            
            // File paths
            serviceIntent.putExtra("filePath", model.getDocument() != null ? model.getDocument() : "");
            serviceIntent.putExtra("fullImageFilePath", model.getMicPhoto() != null ? model.getMicPhoto() : "");
            serviceIntent.putExtra("thumbnailPath", model.getThumbnail() != null ? model.getThumbnail() : "");
            
            // Additional fields
            serviceIntent.putExtra("caption", model.getCaption());
            serviceIntent.putExtra("fileName", model.getFileName());
            serviceIntent.putExtra("docSize", model.getDocSize());
            serviceIntent.putExtra("imageWidthDp", model.getImageWidth());
            serviceIntent.putExtra("imageHeightDp", model.getImageHeight());
            serviceIntent.putExtra("aspectRatio", model.getAspectRatio());
            
            // Selection bunch parameters (critical for multi-selection messages)
            serviceIntent.putExtra("selectionCount", model.getSelectionCount());
            if (model.getSelectionBunch() != null && !model.getSelectionBunch().isEmpty()) {
                Gson gson = new Gson();
                String selectionBunchJson = gson.toJson(model.getSelectionBunch());
                serviceIntent.putExtra("selectionBunch", selectionBunchJson);
                Log.d(TAG, "Group SelectionBunch JSON: " + selectionBunchJson);
            }
            
            // Start the service
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(serviceIntent);
            } else {
                mContext.startService(serviceIntent);
            }
            
            Log.d(TAG, "Group message upload service started: " + model.getModelId());
            
        } catch (Exception e) {
            Log.e(TAG, "Error uploading group message: " + e.getMessage(), e);
        }
    }
    

    /**
     * Reconstruct file path from filename
     */
    private String reconstructFilePath(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        
        // Try common storage locations
        String[] possiblePaths = {
            // External storage
            android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Enclosure/Media/Images/" + fileName,
            android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Enclosure/" + fileName,
            android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Enclosure/" + fileName,
            // App-specific external storage
            mContext.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES) + "/" + fileName,
            mContext.getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS) + "/Enclosure/Media/Images/" + fileName,
            // Internal storage
            mContext.getFilesDir().getAbsolutePath() + "/" + fileName,
            mContext.getFilesDir().getAbsolutePath() + "/Enclosure/Media/Images/" + fileName
        };
        
        for (String path : possiblePaths) {
            if (new File(path).exists()) {
                Log.d(TAG, "Found file at: " + path);
                return path;
            }
        }
        
        Log.w(TAG, "Could not find file: " + fileName);
        return null;
    }

    /**
     * Setup broadcast receiver to listen for upload success
     */
    private void setupUploadSuccessReceiver() {
        uploadSuccessReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("MESSAGE_UPLOADED".equals(intent.getAction())) {
                    String modelId = intent.getStringExtra("modelId");
                    if (modelId != null) {
                        Log.d(TAG, "Upload success received for modelId: " + modelId);
                        // Update pending message status to completed
                        updatePendingMessageStatusToCompleted(modelId);
                    }
                }
            }
        };
        
        IntentFilter filter = new IntentFilter("MESSAGE_UPLOADED");
        LocalBroadcastManager.getInstance(mContext).registerReceiver(uploadSuccessReceiver, filter);
    }

    /**
     * Update pending message status to completed
     */
    private void updatePendingMessageStatusToCompleted(String modelId) {
        executorService.execute(() -> {
            try {
                // Find and update individual message status
                boolean updated = updatePendingMessageStatusByModelId(modelId);
                if (updated) {
                    Log.d(TAG, "Updated pending individual message status to completed: " + modelId);
                } else {
                    Log.d(TAG, "No pending individual message found for modelId: " + modelId);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error updating pending message status: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Update pending message status by modelId only (finds receiverUid from database)
     */
    private boolean updatePendingMessageStatusByModelId(String modelId) {
        try {
            // Try to find the message in the database by searching through all possible receiverUids
            // This is a simplified approach - we'll try common receiverUids
            String[] commonReceiverUids = {"1", "2", "3", "4", "5"}; // Add more as needed
            
            for (String receiverUid : commonReceiverUids) {
                List<messageModel> messages = databaseHelper.getPendingMessages(receiverUid);
                for (messageModel msg : messages) {
                    if (modelId.equals(msg.getModelId())) {
                        databaseHelper.updatePendingMessageStatus(modelId, receiverUid, 2);
                        Log.d(TAG, "Found and updated pending message for modelId: " + modelId + ", receiverUid: " + receiverUid);
                        return true;
                    }
                }
            }
            
            Log.d(TAG, "Message not found in individual pending messages for modelId: " + modelId);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error finding pending message by modelId: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Cleanup resources
     */
    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        if (uploadSuccessReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(uploadSuccessReceiver);
        }
    }
}
