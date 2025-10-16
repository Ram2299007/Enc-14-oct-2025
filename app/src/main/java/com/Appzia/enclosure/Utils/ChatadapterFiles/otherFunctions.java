package com.Appzia.enclosure.Utils.ChatadapterFiles;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.Color;
import java.util.List;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.chattingScreen;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.selectionBunchModel;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Adapter.chatAdapter.DownloadAndGeneratePdfPreviewTask;
import com.Appzia.enclosure.Adapter.chatAdapter.senderViewHolder;
import com.Appzia.enclosure.Adapter.chatAdapter.receiverViewHolder;
import com.Appzia.enclosure.Adapter.chatAdapter;
import com.Appzia.enclosure.Adapter.chatAdapter.MediaPlayerCallback;
import com.Appzia.enclosure.Adapter.forwardAdapter;
import com.Appzia.enclosure.Adapter.forwardnameAdapter;
import com.Appzia.enclosure.Model.forwardnameModel;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.imageview.ShapeableImageView;

import java.net.URLDecoder;
import java.net.URLEncoder;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class otherFunctions {

    public static boolean checkIfSelectionBunchFromLocalStorage(List<selectionBunchModel> selectionBunch) {
        if (selectionBunch == null || selectionBunch.isEmpty()) {
            return false; // If no images, not from local storage
        }

        for (selectionBunchModel bunch : selectionBunch) {
            if (bunch == null || TextUtils.isEmpty(bunch.getFileName())) {
                continue; // Skip if no filename
            }

            // Check if the image URL or filename indicates it's from local storage
            String fileName = bunch.getFileName();
            String imgUrl = bunch.getImgUrl();

            // If imgUrl has a Firebase URL, it's NOT from local storage anymore
            if (!TextUtils.isEmpty(imgUrl) && (imgUrl.contains("firebase") || imgUrl.contains("googleapis") || imgUrl.startsWith("https://"))) {
                Log.d("bunch###", "Image uploaded to Firebase (not local storage): " + imgUrl);
                return false; // This image is uploaded, not local
            }

            // If imgUrl is null or empty, it's likely from local storage
            if (TextUtils.isEmpty(imgUrl)) {
                Log.d("bunch###", "Image from local storage (no URL): " + fileName);
                return true;
            }

            // Check if the URL is a local file path
            if (imgUrl.startsWith("file://") || imgUrl.startsWith("/")) {
                Log.d("bunch###", "Image from local storage (file path): " + imgUrl);
                return true;
            }

            // Check if the filename suggests it's from local storage directory AND no Firebase URL
            if ((fileName.contains("Enclosure/Media/Images") || fileName.contains("Enclosure%2FMedia%2FImages")) && TextUtils.isEmpty(imgUrl)) {
                Log.d("bunch###", "Image from local storage (filename contains path, no URL): " + fileName);
                return true;
            }
        }

        return false; // Not from local storage
    }

    public static boolean checkAnySelectionBunchImagesMissingForRegularChat(List<selectionBunchModel> selectionBunch, Context context) {
        Log.d("SelectionBunch", "=== CHECKING MISSING IMAGES FOR REGULAR CHAT ===");

        if (selectionBunch == null || selectionBunch.isEmpty()) {
            Log.d("SelectionBunch", "SelectionBunch is null or empty - considering all images missing");
            return true; // If no images, consider them missing
        }

        Log.d("SelectionBunch", "Checking " + selectionBunch.size() + " images in selectionBunch");
        int missingCount = 0;
        int totalCount = selectionBunch.size();

        for (int i = 0; i < selectionBunch.size(); i++) {
            selectionBunchModel bunch = selectionBunch.get(i);
            if (bunch == null) {
                Log.d("SelectionBunch", "Image " + i + " - bunch is null, skipping");
                missingCount++;
                continue; // Skip if no bunch
            }

            Log.d("SelectionBunch", "Image " + i + " - FileName: " + bunch.getFileName() +
                    ", ImgUrl: " + (TextUtils.isEmpty(bunch.getImgUrl()) ? "EMPTY" : "HAS_URL"));

            // For regular chat, check if Firebase URL is available
            if (TextUtils.isEmpty(bunch.getImgUrl())) {
                Log.d("SelectionBunch", "Image " + i + " - Missing Firebase URL: " + bunch.getFileName());
                missingCount++;
                return true; // Found at least one missing image URL
            }

            // Also check if the image exists locally
            if (!TextUtils.isEmpty(bunch.getImgUrl())) {
                String fileName = bunch.getFileName();
                if (!TextUtils.isEmpty(fileName)) {
                    // Try both original filename and cleaned filename to match the stored format
                    String originalFileName = fileName;
                    String cleanFileName = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");

                    // Check in the same folder where images are actually downloaded
                    File customFolder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        customFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                    } else {
                        customFolder = new File(context.getExternalFilesDir(null), "Enclosure/Media/Images");
                    }

                    // Try original filename first
                    String localPath = customFolder.getAbsolutePath() + "/" + originalFileName;
                    boolean fileExists = doesFileExist(localPath);

                    // If not found with original name, try cleaned name
                    if (!fileExists && !originalFileName.equals(cleanFileName)) {
                        localPath = customFolder.getAbsolutePath() + "/" + cleanFileName;
                        fileExists = doesFileExist(localPath);
                        Log.d("SelectionBunch", "Image " + i + " - Trying cleaned filename: " + cleanFileName);
                    }

                    Log.d("SelectionBunch", "Image " + i + " - Checking local path: " + localPath);
                    Log.d("SelectionBunch", "Image " + i + " - File exists: " + fileExists);

                    if (!fileExists) {
                        Log.d("SelectionBunch", "Image " + i + " - NOT FOUND locally: " + localPath);
                        missingCount++;
                        return true; // Found at least one missing local image
                    } else {
                        Log.d("SelectionBunch", "Image " + i + " - Found locally: " + localPath);
                    }
                } else {
                    Log.d("SelectionBunch", "Image " + i + " - Empty filename, considering missing");
                    missingCount++;
                    return true;
                }
            }
        }

        Log.d("SelectionBunch", "=== MISSING IMAGES CHECK COMPLETE ===");
        Log.d("SelectionBunch", "Total images: " + totalCount + ", Missing: " + missingCount);
        Log.d("SelectionBunch", "All images have Firebase URLs and exist locally for regular chat");
        return false; // All images have URLs and exist locally
    }

    public static boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean checkIfSelectionBunchHasValidUrls(List<selectionBunchModel> selectionBunch) {
        if (selectionBunch == null || selectionBunch.isEmpty()) {
            return false; // No images, so no valid URLs
        }

        for (selectionBunchModel bunch : selectionBunch) {
            if (bunch == null) {
                continue; // Skip if no bunch
            }

            // Check if Firebase URL is available and not empty
            if (!TextUtils.isEmpty(bunch.getImgUrl())) {
                return true; // Found at least one valid URL
            }
        }
        return false; // No valid URLs found
    }

    public static void setImageViewDimensions(ImageView imageView, String widthStr, String heightStr, Context context) {
        try {
            // Set consistent scale type for all images
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            if (widthStr != null && heightStr != null && !widthStr.isEmpty() && !heightStr.isEmpty()) {
                // Get device density and orientation
                float density = context.getResources().getDisplayMetrics().density;
                int orientation = context.getResources().getConfiguration().orientation;

                Log.d("setImageViewDimensions", "getImageWidth: " + widthStr);
                Log.d("setImageViewDimensions", "getImageHeight: " + heightStr);
                Log.d("setImageViewDimensions", "Orientation: " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "Portrait" : "Landscape"));

                float imageWidthPx, imageHeightPx, aspectRatio;
                try {
                    imageWidthPx = Float.parseFloat(widthStr);
                    imageHeightPx = Float.parseFloat(heightStr);
                    aspectRatio = imageWidthPx / imageHeightPx;

                    if (aspectRatio <= 0) {
                        aspectRatio = 1.0f;
                    }
                } catch (NumberFormatException e) {
                    Log.e("setImageViewDimensions", "Invalid dimensions, using defaults", e);
                    imageWidthPx = 210f;
                    imageHeightPx = 250f;
                    aspectRatio = 1.0f;
                }

                // Use the same max dimensions as loadImageIntoViewGroup
                final float MAX_WIDTH_DP = 210f;
                final float MAX_HEIGHT_DP = 250f;

                int maxWidthPx = (int) (MAX_WIDTH_DP * density);
                int maxHeightPx = (int) (MAX_HEIGHT_DP * density);

                int finalWidthPx, finalHeightPx;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    finalWidthPx = maxWidthPx;
                    finalHeightPx = (int) (maxWidthPx / aspectRatio);
                    if (finalHeightPx > maxHeightPx) {
                        finalHeightPx = maxHeightPx;
                        finalWidthPx = (int) (maxHeightPx * aspectRatio);
                    }
                } else {
                    finalHeightPx = maxHeightPx;
                    finalWidthPx = (int) (finalHeightPx * aspectRatio);
                    if (finalWidthPx > maxWidthPx) {
                        finalWidthPx = maxWidthPx;
                        finalHeightPx = (int) (maxWidthPx / aspectRatio);
                    }
                }

                finalWidthPx = Math.min(finalWidthPx, maxWidthPx);
                finalHeightPx = Math.min(finalHeightPx, maxHeightPx);

                // Set layout parameters
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.LayoutParams(finalWidthPx, finalHeightPx);
                } else {
                    params.width = finalWidthPx;
                    params.height = finalHeightPx;
                }
                imageView.setLayoutParams(params);
                // Also set parent layout to wrap content if available
                ViewGroup parentLayout = (ViewGroup) imageView.getParent();
                if (parentLayout != null) {
                    ViewGroup.LayoutParams parentParams = parentLayout.getLayoutParams();
                    if (parentParams != null) {
                        parentParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        parentParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        parentLayout.setLayoutParams(parentParams);
                    }
                }

                Log.d("setImageViewDimensions", "Set dimensions using density-based logic - Width: " + finalWidthPx + "px, Height: " + finalHeightPx + "px (Original: " + imageWidthPx + "x" + imageHeightPx + ", Aspect Ratio: " + aspectRatio + ", Density: " + density + ")");
            }
        } catch (Exception e) {
            Log.e("setImageViewDimensions", "Error setting image dimensions: " + e.getMessage());
        }
    }

    public static void ensureConsistentImageScaling(ImageView imageView, messageModel model, Context context) {
        if (imageView != null && model != null) {
            // Set consistent scale type for all images
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            // Set dimensions if available
            setImageViewDimensions(imageView, model.getImageWidth(), model.getImageHeight(), context);
        }
    }

    public static void copyDocToPublicDoc(File privateFile, String fileName, Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.Files.FileColumns.MIME_TYPE, "application/octet-stream");
                values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Enclosure");
                values.put(MediaStore.Files.FileColumns.IS_PENDING, 1);

                Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                if (uri != null) {
                    try (InputStream in = new FileInputStream(privateFile);
                         OutputStream out = context.getContentResolver().openOutputStream(uri)) {
                        byte[] buffer = new byte[8192];
                        int read;
                        while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
                        out.flush();
                    }
                    values.clear();
                    values.put(MediaStore.Files.FileColumns.IS_PENDING, 0);
                    context.getContentResolver().update(uri, values, null, null);
                    Log.d("DOWNLOAD_DEBUG", "Document saved publicly via MediaStore: " + fileName);
                }
            } else {
                File publicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Enclosure");
                if (!publicDir.exists()) publicDir.mkdirs();
                File publicFile = new File(publicDir, fileName);

                copyFile2(privateFile, publicFile);

                MediaScannerConnection.scanFile(
                        context,
                        new String[]{publicFile.getAbsolutePath()},
                        null,
                        (path, uri) -> Log.d("DOWNLOAD_DEBUG", "Document scanned: " + uri)
                );
                Log.d("DOWNLOAD_DEBUG", "Document copied publicly: " + publicFile.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.e("DOWNLOAD_DEBUG", "Error copying document to public folder", e);
        }
    }

    private static void copyFile2(File source, File dest) throws IOException {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
    }

    public static void copyPrivateImageToPublic(File privateFile, String fileName, Context context) {
        new Thread(() -> {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(privateFile.getAbsolutePath());
                if (bitmap == null) {
                    showToastSafe("Failed to read image for public copy", context);
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Enclosure");
                    values.put(MediaStore.MediaColumns.IS_PENDING, 1);

                    Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    if (uri != null) {
                        try (OutputStream out = resolver.openOutputStream(uri)) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        }
                        values.clear();
                        values.put(MediaStore.MediaColumns.IS_PENDING, 0);
                        resolver.update(uri, values, null, null);
                        showToastSafe("Image copied to public gallery", context);
                    } else {
                        showToastSafe("Failed to save image to gallery", context);
                    }

                } else {
                    File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File enclosureDir = new File(picturesDir, "Enclosure");
                    if (!enclosureDir.exists()) enclosureDir.mkdirs();

                    File publicFile = new File(enclosureDir, fileName);
                    try (FileOutputStream out = new FileOutputStream(publicFile)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    }

                    Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    scanIntent.setData(Uri.fromFile(publicFile));
                    context.sendBroadcast(scanIntent);

                    showToastSafe("Image copied to public gallery", context);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToastSafe("Error copying image to gallery", context);
            }
        }).start();
    }

    private static void showToastSafe(String message, Context context) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        );
    }

    public static boolean isMobileNumber(String text) {
        // Remove spaces or dashes
        text = text.replaceAll("[\\s\\-()]", "");

        // Basic mobile number pattern: starts with + or digit, 10 to 15 digits total
        return text.matches("^(\\+\\d{1,3})?\\d{10,14}$");
    }

    public static String getRemotePdfUrl(messageModel model) {
        // Assuming model.getDocument() returns the remote URL of the PDF
        return model.getDocument();
    }

    public static void updateMessageList(List<messageModel> newMessageList, List<messageModel> currentMessageList, RecyclerView.Adapter adapter) {
        // Debug logging for selectionBunch
        Log.d("SelectionBunch", "updateMessageList: Updating adapter with " + newMessageList.size() + " messages");
        for (int i = 0; i < Math.min(newMessageList.size(), 3); i++) {
            messageModel model = newMessageList.get(i);
            Log.d("SelectionBunch", "updateMessageList: Message " + i + " - ID: " + model.getModelId() +
                    ", selectionCount: " + model.getSelectionCount() +
                    ", selectionBunch size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));
        }

        // Calculate the diff between the old and new list
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MessageDiffCallback(currentMessageList, newMessageList));

        // Clear the old list and add all new items
        currentMessageList.clear();
        currentMessageList.addAll(newMessageList);

        // Dispatch the updates to the adapter
        diffResult.dispatchUpdatesTo(adapter);
    }

    public static class MessageDiffCallback extends DiffUtil.Callback {

        private final List<messageModel> mOldList;
        private final List<messageModel> mNewList;

        public MessageDiffCallback(List<messageModel> oldList, List<messageModel> newList) {
            this.mOldList = oldList;
            this.mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList.size();
        }

        @Override
        public int getNewListSize() {
            return mNewList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldList.get(oldItemPosition).getModelId().equals(mNewList.get(newItemPosition).getModelId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            messageModel oldItem = mOldList.get(oldItemPosition);
            messageModel newItem = mNewList.get(newItemPosition);
            return oldItem.equals(newItem);
        }
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getFilePath(messageModel model, Context context) {
        File customFolder;
        String exactPath;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            customFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
            exactPath = customFolder.getAbsolutePath();
        } else {
            customFolder = new File(context.getExternalFilesDir(null), "Enclosure/Media/Documents");
            exactPath = customFolder.getAbsolutePath();
        }
        String filePath = exactPath + "/" + model.getFileName();
        return doesFileExist(filePath) ? filePath : model.getDocument(); // Fallback to remote URL if local file doesn't exist
    }

    public static String getLocalPdfPreviewImagePath(messageModel model, Context context) {
        File customFolder;
        String exactPath;

        // Use getExternalFilesDir() for app-private storage.
        // Environment.DIRECTORY_DOCUMENTS is a standard directory within app-private storage.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            customFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents/Previews");
        } else {
            // For older Android versions, use the root of getExternalFilesDir()
            customFolder = new File(context.getExternalFilesDir(null), "Enclosure/Media/Documents/Previews");
        }

        // Ensure the directory exists. If it doesn't, create it.
        if (!customFolder.exists()) {
            boolean created = customFolder.mkdirs(); // Creates the directory and any necessary parent directories
            if (!created) {
                Log.e("TAG", "Failed to create directory: " + customFolder.getAbsolutePath());
                // Handle this error appropriately, perhaps by falling back to no preview
            }
        }

        exactPath = customFolder.getAbsolutePath();
        // Construct the full path for the preview image, using the original file name + ".png"
        return exactPath + "/" + model.getFileName() + ".png";
    }

    public static void loadPdfPreview(String localPreviewImagePath, String remotePdfUrl, ImageView imageView, ViewGroup parentLayout, int position, messageModel model, Context context) {
        File localPreviewFile = new File(localPreviewImagePath);

        if (localPreviewFile.exists()) {
            Log.d("TAG", "Loading PDF preview from local cache: " + localPreviewImagePath);

            // Use file:// prefix for local files
            String imageSource = "file://" + localPreviewImagePath;

            RequestOptions requestOptions = new RequestOptions().centerCrop();

            // Load low-quality preview from cache
            Constant.loadImageIntoViewPdf(context, imageSource, requestOptions, imageView, parentLayout, position, true, // 👈 Load low-quality
                    model);
        } else {
            Log.d("TAG", "Local PDF preview not found. Downloading PDF from: " + remotePdfUrl);

            // Fallback to generate and download preview
            new DownloadAndGeneratePdfPreviewTask(context, localPreviewImagePath, imageView, parentLayout, position, true).execute(remotePdfUrl);
        }
    }

    public static void bindSelectionBunchImagesReceiver(receiverViewHolder holder,
                                                      messageModel model,
                                                      RequestOptions requestOptions,
                                                      int position,
                                                      boolean loadHighQuality,
                                                      Context context) {
        // Delegate to the original method in chatAdapter
        // This is a placeholder - the actual implementation would need to be moved here
        Log.d("SelectionBunch", "bindSelectionBunchImagesReceiver called for messageId=" + model.getModelId());
        
        if (model.getSelectionBunch() == null) {
            Log.w("SelectionBunch", "selectionBunch is null for messageId=" + model.getModelId());
            return;
        }

        if (model.getSelectionBunch().size() < 2) {
            Log.w("SelectionBunch", "selectionBunch size=" + model.getSelectionBunch().size() + " (need >=2) for messageId=" + model.getModelId());
            return;
        }

        // Check if any images in selectionBunch are missing locally
        boolean anyImagesMissing = checkAnySelectionBunchImagesMissingForRegularChat(model.getSelectionBunch(), context);
        boolean hasValidImageUrls = checkIfSelectionBunchHasValidUrls(model.getSelectionBunch());
        boolean isFromLocalStorage = checkIfSelectionBunchFromLocalStorage(model.getSelectionBunch());

        // Enhanced logging for better debugging
        Log.d("SelectionBunch", "=== DOWNLOAD ICON DECISION (RECEIVER) ===");
        Log.d("SelectionBunch", "MessageId: " + model.getModelId());
        Log.d("SelectionBunch", "SelectionBunch size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));
        Log.d("SelectionBunch", "anyImagesMissing: " + anyImagesMissing);
        Log.d("SelectionBunch", "hasValidImageUrls: " + hasValidImageUrls);
        Log.d("SelectionBunch", "isFromLocalStorage: " + isFromLocalStorage);

        if (anyImagesMissing && hasValidImageUrls) {
            // Show download button when images are missing but have valid URLs
            holder.downlaodImgBunch.setVisibility(View.VISIBLE);
            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);

            // Set click listener for download button
            holder.downlaodImgBunch.setOnClickListener(v -> {
                Log.d("SelectionBunch", "=== DOWNLOAD BUTTON CLICKED (RECEIVER) ===");
                Log.d("SelectionBunch", "MessageId: " + model.getModelId());
                senderReceiverDownload.downloadAllSelectionBunchImagesReceiver(holder, model, position, context);
            });

            Log.d("SelectionBunch", "✅ SHOWING download icon (receiver) - some images missing locally");
        } else {
            // Hide download views when all images exist locally or when images don't have valid URLs
            holder.downlaodImgBunch.setVisibility(View.GONE);
            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);

            String reason = "";
            if (!anyImagesMissing) reason += "all images exist locally; ";
            if (!hasValidImageUrls) reason += "no valid URLs; ";
            if (isFromLocalStorage) reason += "from local storage; ";

            Log.d("SelectionBunch", "❌ HIDING download icon (receiver) - " + reason + " for messageId=" + model.getModelId());
        }

        // Set width to 125dp for all selectionBunch images
        float widthInDp = 125f;
        int widthInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                widthInDp,
                holder.img1.getResources().getDisplayMetrics()
        );

        // Set standard width for all images
        ViewGroup.LayoutParams params1 = holder.img1.getLayoutParams();
        if (params1 != null) {
            params1.width = widthInPx;
            holder.img1.setLayoutParams(params1);
        }

        ViewGroup.LayoutParams params2 = holder.img2.getLayoutParams();
        if (params2 != null) {
            params2.width = widthInPx;
            holder.img2.setLayoutParams(params2);
        }

        ViewGroup.LayoutParams params3 = holder.img3.getLayoutParams();
        if (params3 != null) {
            params3.width = widthInPx;
            holder.img3.setLayoutParams(params3);
        }

        ViewGroup.LayoutParams params4 = holder.img4.getLayoutParams();
        if (params4 != null) {
            params4.width = widthInPx;
            holder.img4.setLayoutParams(params4);
        }

        // Load images based on selectionBunch
        for (int i = 0; i < Math.min(model.getSelectionBunch().size(), 4); i++) {
            selectionBunchModel bunch = model.getSelectionBunch().get(i);
            if (bunch != null && !TextUtils.isEmpty(bunch.getImgUrl())) {
                ImageView targetImageView = null;
                switch (i) {
                    case 0:
                        targetImageView = holder.img1;
                        break;
                    case 1:
                        targetImageView = holder.img2;
                        break;
                    case 2:
                        targetImageView = holder.img3;
                        break;
                    case 3:
                        targetImageView = holder.img4;
                        break;
                }

                if (targetImageView != null) {
                    ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                    Constant.loadImageIntoView(context, bunch.getImgUrl(), requestOptions, targetImageView, parentLayout, position, loadHighQuality, model, null);
                }
            }
        }
    }

    public static void bindSelectionBunchImages(senderViewHolder holder,
                                              messageModel model,
                                              RequestOptions requestOptions,
                                              int position,
                                              boolean loadHighQuality,
                                              Context context) {
        Log.d("SelectionBunch", "bindSelectionBunchImages called for messageId=" + model.getModelId());

        if (model.getSelectionBunch() == null) {
            Log.w("SelectionBunch", "selectionBunch is null for messageId=" + model.getModelId());
            return;
        }

        if (model.getSelectionBunch().size() < 2) {
            Log.w("SelectionBunch", "selectionBunch size=" + model.getSelectionBunch().size() + " (need >=2) for messageId=" + model.getModelId());
            return;
        }

        // Check if any images in selectionBunch are missing locally
        boolean anyImagesMissing = checkAnySelectionBunchImagesMissingForRegularChat(model.getSelectionBunch(), context);
        boolean hasValidImageUrls = checkIfSelectionBunchHasValidUrls(model.getSelectionBunch());
        boolean isFromLocalStorage = checkIfSelectionBunchFromLocalStorage(model.getSelectionBunch());

        if (anyImagesMissing && hasValidImageUrls) {
            // Show download button when images are missing but have valid URLs
            holder.downlaodImgBunch.setVisibility(View.VISIBLE);
            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);

            // Set click listener for download button
            holder.downlaodImgBunch.setOnClickListener(v -> {
                Log.d("SelectionBunch", "=== DOWNLOAD BUTTON CLICKED (SENDER) ===");
                Log.d("SelectionBunch", "MessageId: " + model.getModelId());
                senderReceiverDownload.downloadAllSelectionBunchImages(holder, model, position, context);
            });

            Log.d("SelectionBunch", "✅ SHOWING download icon (sender) - some images missing locally");
        } else {
            // Hide download views when all images exist locally or when images don't have valid URLs
            holder.downlaodImgBunch.setVisibility(View.GONE);
            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);

            String reason = "";
            if (!anyImagesMissing) reason += "all images exist locally; ";
            if (!hasValidImageUrls) reason += "no valid URLs; ";
            if (isFromLocalStorage) reason += "from local storage; ";

            Log.d("SelectionBunch", "❌ HIDING download icon (sender) - " + reason + " for messageId=" + model.getModelId());
        }

        // Set width to 125dp for all selectionBunch images
        float widthInDp = 125f;
        int widthInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                widthInDp,
                holder.img1.getResources().getDisplayMetrics()
        );

        // Set standard width for all images
        ViewGroup.LayoutParams params1 = holder.img1.getLayoutParams();
        if (params1 != null) {
            params1.width = widthInPx;
            holder.img1.setLayoutParams(params1);
        }

        ViewGroup.LayoutParams params2 = holder.img2.getLayoutParams();
        if (params2 != null) {
            params2.width = widthInPx;
            holder.img2.setLayoutParams(params2);
        }

        ViewGroup.LayoutParams params3 = holder.img3.getLayoutParams();
        if (params3 != null) {
            params3.width = widthInPx;
            holder.img3.setLayoutParams(params3);
        }

        ViewGroup.LayoutParams params4 = holder.img4.getLayoutParams();
        if (params4 != null) {
            params4.width = widthInPx;
            holder.img4.setLayoutParams(params4);
        }

        // Load images based on selectionBunch
        for (int i = 0; i < Math.min(model.getSelectionBunch().size(), 4); i++) {
            selectionBunchModel bunch = model.getSelectionBunch().get(i);
            if (bunch != null && !TextUtils.isEmpty(bunch.getImgUrl())) {
                ImageView targetImageView = null;
                switch (i) {
                    case 0:
                        targetImageView = holder.img1;
                        break;
                    case 1:
                        targetImageView = holder.img2;
                        break;
                    case 2:
                        targetImageView = holder.img3;
                        break;
                    case 3:
                        targetImageView = holder.img4;
                        break;
                }

                if (targetImageView != null) {
                    ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                    Constant.loadImageIntoView(context, bunch.getImgUrl(), requestOptions, targetImageView, parentLayout, position, loadHighQuality, model, null);
                }
            }
        }
    }

    public static void bindSelectionBunchImagesSenderLong(
            messageModel model,
            RequestOptions requestOptions,
            int position,
            boolean loadHighQuality,
            ShapeableImageView img1,
            ShapeableImageView img2,
            ShapeableImageView img3,
            ShapeableImageView img4,
            FrameLayout img4Lyt,
            ImageView videoicon,
            TextView overlayTextImg,
            Context context) {
        Log.d("SelectionBunch", "bindSelectionBunchImagesSenderLong called for messageId=" + model.getModelId());

        if (model.getSelectionBunch() == null) {
            Log.w("SelectionBunch", "selectionBunch is null for messageId=" + model.getModelId());
            return;
        }

        if (model.getSelectionBunch().size() < 2) {
            Log.w("SelectionBunch", "selectionBunch size=" + model.getSelectionBunch().size() + " (need >=2) for messageId=" + model.getModelId());
            return;
        }

        // Set width to 125dp for all selectionBunch images
        float widthInDp = 125f;
        int widthInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                widthInDp,
                img1.getResources().getDisplayMetrics()
        );

        // Set standard width for all images
        ViewGroup.LayoutParams params1 = img1.getLayoutParams();
        if (params1 != null) {
            params1.width = widthInPx;
            img1.setLayoutParams(params1);
        }

        ViewGroup.LayoutParams params2 = img2.getLayoutParams();
        if (params2 != null) {
            params2.width = widthInPx;
            img2.setLayoutParams(params2);
        }

        ViewGroup.LayoutParams params3 = img3.getLayoutParams();
        if (params3 != null) {
            params3.width = widthInPx;
            img3.setLayoutParams(params3);
        }

        ViewGroup.LayoutParams params4 = img4.getLayoutParams();
        if (params4 != null) {
            params4.width = widthInPx;
            img4.setLayoutParams(params4);
        }

        // Load images based on selectionBunch
        for (int i = 0; i < Math.min(model.getSelectionBunch().size(), 4); i++) {
            selectionBunchModel bunch = model.getSelectionBunch().get(i);
            if (bunch != null && !TextUtils.isEmpty(bunch.getImgUrl())) {
                ImageView targetImageView = null;
                switch (i) {
                    case 0:
                        targetImageView = img1;
                        break;
                    case 1:
                        targetImageView = img2;
                        break;
                    case 2:
                        targetImageView = img3;
                        break;
                    case 3:
                        targetImageView = img4;
                        break;
                }

                if (targetImageView != null) {
                    ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                    Constant.loadImageIntoView(context, bunch.getImgUrl(), requestOptions, targetImageView, parentLayout, position, loadHighQuality, model, null);
                }
            }
        }
    }

    public static void loadSelectionImageIntoViewForBunch(Context context,
                                                        selectionBunchModel bunch,
                                                        messageModel model,
                                                        RequestOptions requestOptions,
                                                        ImageView targetImageView,
                                                        ViewGroup parentLayout,
                                                        int position,
                                                        boolean loadHighQuality,
                                                        View videoIcon) {
        if (bunch == null || targetImageView == null) {
            Log.w("SelectionBunch", "loadSelectionImageIntoViewForBunch skipped (bunch or targetImageView null)");
            return;
        }

        String imageSource = null;
        String fileName = bunch.getFileName();

        if (!TextUtils.isEmpty(fileName)) {
            File customFolder2;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                customFolder2 = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
            } else {
                customFolder2 = new File(context.getExternalFilesDir(null), "Enclosure/Media/Images");
            }

            // Decode URL-encoded characters in filename
            String decodedFileName = fileName;
            try {
                decodedFileName = URLDecoder.decode(fileName, "UTF-8");
                Log.d("bunch###", "Decoded filename: " + fileName + " -> " + decodedFileName);
            } catch (Exception e) {
                Log.w("bunch###", "Failed to decode filename: " + fileName, e);
            }

            // Remove any subdirectory prefixes like "chats/" from the filename
            String cleanFileName = decodedFileName;
            if (cleanFileName.contains("/")) {
                cleanFileName = cleanFileName.substring(cleanFileName.lastIndexOf("/") + 1);
                Log.d("bunch###", "Removed subdirectory prefix: " + decodedFileName + " -> " + cleanFileName);
            }

            String localPath = customFolder2.getAbsolutePath() + "/" + cleanFileName;
            Log.d("SelectionBunch", "Checking local path for bunch image: " + localPath);
            Log.d("bunch###", "Checking local path for bunch image: " + localPath);
            if (doesFileExist(localPath)) {
                // Check file size to verify it's a different file
                File localFile = new File(localPath);
                if (localFile.exists()) {
                    long fileSize = localFile.length();
                    Log.d("SelectionBunch", "Local file size: " + fileSize + " bytes for " + cleanFileName);

                    // Check if this is a duplicate file (same size as other images in the bunch)
                    boolean isDuplicateFile = false;
                    if (model.getSelectionBunch() != null && model.getSelectionBunch().size() > 1) {
                        for (selectionBunchModel otherBunch : model.getSelectionBunch()) {
                            if (otherBunch != null && !otherBunch.equals(bunch) && !TextUtils.isEmpty(otherBunch.getFileName())) {
                                String otherFileName = otherBunch.getFileName();
                                try {
                                    otherFileName = URLDecoder.decode(otherFileName, "UTF-8");
                                } catch (Exception e) {
                                    // Ignore decode errors
                                }
                                if (otherFileName.contains("/")) {
                                    otherFileName = otherFileName.substring(otherFileName.lastIndexOf("/") + 1);
                                }

                                String otherLocalPath = customFolder2.getAbsolutePath() + "/" + otherFileName;
                                File otherFile = new File(otherLocalPath);
                                if (otherFile.exists() && otherFile.length() == fileSize) {
                                    isDuplicateFile = true;
                                    Log.d("SelectionBunch", "Detected duplicate file size: " + fileSize + " bytes for " + cleanFileName);
                                    break;
                                }
                            }
                        }
                    }

                    if (isDuplicateFile) {
                        // For duplicate files, still use local file but we'll handle uniqueness in Glide signature
                        imageSource = localPath;
                        Log.d("SelectionBunch", "Using local file for duplicate case: " + imageSource);
                    } else {
                        imageSource = localPath;
                        Log.d("SelectionBunch", "Found local bunch image: " + imageSource);
                        Log.d("bunch###", "Found local bunch image: " + imageSource);
                    }
                }
            } else {
                Log.w("bunch###", "Local bunch image not found at: " + localPath);
            }
        }

        if (TextUtils.isEmpty(imageSource)) {
            if (!TextUtils.isEmpty(bunch.getImgUrl())) {
                String baseUrl = bunch.getImgUrl();
                String bunchFileName = bunch.getFileName();

                // Check if this is a duplicate URL issue by comparing with other images in the bunch
                boolean isDuplicateUrl = false;
                if (model.getSelectionBunch() != null && model.getSelectionBunch().size() > 1) {
                    for (selectionBunchModel otherBunch : model.getSelectionBunch()) {
                        if (otherBunch != null && !otherBunch.equals(bunch) &&
                                !TextUtils.isEmpty(otherBunch.getImgUrl()) &&
                                otherBunch.getImgUrl().equals(baseUrl)) {
                            isDuplicateUrl = true;
                            Log.d("SelectionBunch", "Detected duplicate URL for different files");
                            break;
                        }
                    }
                }

                if (isDuplicateUrl && !TextUtils.isEmpty(bunchFileName)) {
                    // For duplicate URLs, use the original URL but we'll handle uniqueness in Glide signature
                    // This ensures we don't try to load non-existent URLs
                    imageSource = baseUrl;
                    Log.d("SelectionBunch", "Using original URL for duplicate case: " + imageSource);
                } else {
                    imageSource = baseUrl;
                }
                Log.d("SelectionBunch", "Using Firebase URL for bunch image: " + imageSource);
            } else if (!TextUtils.isEmpty(model.getDocument())) {
                imageSource = model.getDocument();
                Log.d("SelectionBunch", "Falling back to message document for bunch image: " + imageSource);
            }
        }

        if (TextUtils.isEmpty(imageSource)) {
            Log.w("SelectionBunch", "No image source resolved for bunch; clearing image view");
            targetImageView.setImageDrawable(null);
            return;
        }

        Log.d("SelectionBunch", "Loading bunch image from: " + imageSource);
        Log.d("ImageFlicker", "📸 Loading selectionBunch: " + imageSource + " | Quality: " + (true ? "HIGH" : "LOW") + " | Position: " + position);

        // Only use the custom network loader for remote URLs.
        // If the file exists locally, load it directly via Glide and avoid the network pipeline.
        if (imageSource.startsWith("http")) {
            // Remote - use custom loader to respect dimensions
            loadSelectionBunchImageWithCustomDimensions(context, imageSource, requestOptions, targetImageView, position, true, videoIcon, bunch.getFileName());
        } else {
            // Local - load directly without custom remote pipeline
            Log.d("SelectionBunch", "Loading LOCAL bunch image from: " + imageSource);
            if (videoIcon != null) {
                videoIcon.setVisibility(true ? View.VISIBLE : View.VISIBLE);
            }
            // Clear any existing image to prevent flashes
            targetImageView.setImageDrawable(null);
            Glide.with(context)
                    .load(new File(imageSource))
                    .apply(requestOptions
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .skipMemoryCache(false)
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .dontAnimate()
                            .timeout(10000))
                    .into(targetImageView);
        }
    }

    public static void loadSelectionBunchImageWithCustomDimensions(Context context, String imageSource, RequestOptions requestOptions,
                                                                   ImageView targetImageView, int position, boolean loadHighQuality, View videoIcon, String filename) {
        Log.d("SelectionBunch", "loadSelectionBunchImageWithCustomDimensions called for: " + imageSource);
        Log.d("SelectionBunch", "Image source hash: " + imageSource.hashCode());
        Log.d("SelectionBunch", "Position: " + position);
        Log.d("SelectionBunch", "Filename: " + filename);

        // Set video icon visibility based on quality
        if (videoIcon != null) {
            videoIcon.setVisibility(true ? View.VISIBLE : View.VISIBLE);
        }

        // Use simple Glide loading that respects our custom dimensions
        // Create unique signature to prevent caching issues between different images
        // Use the actual filename to ensure uniqueness even with same local file
        String uniqueSignature = position + "_selectionBunch_" + (filename != null ? filename.hashCode() : imageSource.hashCode()) + "_" + System.currentTimeMillis();
        Log.d("SelectionBunch", "Using unique signature: " + uniqueSignature + " for filename: " + filename);

        // Clear any existing image in the target view to prevent showing wrong image
        targetImageView.setImageDrawable(null);

        // Apply quality settings based on true flag
        RequestOptions qualityOptions = requestOptions;
        // Single quality setting for all images - 75% quality
        qualityOptions = qualityOptions
                .format(DecodeFormat.PREFER_ARGB_8888) // High quality format
                .encodeQuality(75); // Single 75% quality for all images

        Glide.with(context)
                .load(imageSource)
                .apply(qualityOptions
                        .signature(new ObjectKey(uniqueSignature))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // Enable disk cache for better performance
                        .skipMemoryCache(false) // Enable memory cache for smoother scrolling
                        .dontAnimate()
                        .timeout(10000))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("SelectionBunch", "Failed to load selectionBunch image: " + imageSource, e);
                        Log.e("bunch###", "Failed to load bunch image: " + imageSource);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("SelectionBunch", "Successfully loaded selectionBunch image: " + imageSource);
                        Log.d("bunch###", "Successfully loaded bunch image: " + imageSource);
                        return false;
                    }
                })
                .into(targetImageView);
    }

    public static void trackSenderAudioDownloadProgress(long downloadId, ProgressBar progressBar, TextView percentageView, View downloadFab, Context context) {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Query q = new DownloadManager.Query().setFilterById(downloadId);
                    Cursor c = dm.query(q);
                    if (c != null && c.moveToFirst()) {
                        int statusIdx = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int bytesIdx = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int totalIdx = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

                        int status = c.getInt(statusIdx);
                        long bytesDownloaded = c.getLong(bytesIdx);
                        long totalBytes = c.getLong(totalIdx);

                        if (totalBytes > 0) {
                            int pct = (int) ((bytesDownloaded * 100L) / totalBytes);
                            percentageView.setText(pct + "%");
                        } else {
                            percentageView.setText("0%");
                        }

                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            progressBar.setVisibility(View.GONE);
                            percentageView.setVisibility(View.GONE);
                            downloadFab.setVisibility(View.GONE); // file exists now, so keep hidden
                            c.close();
                            return;
                        }
                        c.close();
                        handler.postDelayed(this, 300);
                    } else {
                        if (c != null) c.close();
                        handler.postDelayed(this, 300);
                    }
                } catch (Exception ignored) {
                    progressBar.setVisibility(View.GONE);
                    percentageView.setVisibility(View.GONE);
                    downloadFab.setVisibility(View.VISIBLE);
                }
            }
        };
        handler.post(r);
    }

    public static void trackReceiverAudioDownloadProgress(long downloadId, ProgressBar progressBar, TextView percentageView, View downloadFab, messageModel model, Context context) {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Query q = new DownloadManager.Query().setFilterById(downloadId);
                    Cursor c = dm.query(q);
                    if (c != null && c.moveToFirst()) {
                        int statusIdx = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int bytesIdx = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int totalIdx = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

                        int status = c.getInt(statusIdx);
                        long bytesDownloaded = c.getLong(bytesIdx);
                        long totalBytes = c.getLong(totalIdx);

                        if (totalBytes > 0) {
                            int pct = (int) ((bytesDownloaded * 100L) / totalBytes);
                            percentageView.setText(pct + "%");
                        } else {
                            percentageView.setText("0%");
                        }

                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            progressBar.setVisibility(View.GONE);
                            percentageView.setVisibility(View.GONE);
                            // On success, keep the FAB hidden; on failure, show it again
                            if (status == DownloadManager.STATUS_FAILED) {
                                downloadFab.setVisibility(View.VISIBLE);
                            } else {
                                downloadFab.setVisibility(View.GONE);
                            }
                            c.close();
                            return;
                        }
                        c.close();
                        handler.postDelayed(this, 300);
                    } else {
                        if (c != null) c.close();
                        handler.postDelayed(this, 300);
                    }
                } catch (Exception ignored) {
                    progressBar.setVisibility(View.GONE);
                    percentageView.setVisibility(View.GONE);
                    downloadFab.setVisibility(View.VISIBLE);
                }
            }
        };
        handler.post(r);
    }

    public static void trackDocDownloadProgress(long downloadId, ProgressBar progressBar, TextView percentageView, View downloadFab, Context context) {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Query q = new DownloadManager.Query().setFilterById(downloadId);
                    Cursor c = dm.query(q);
                    if (c != null && c.moveToFirst()) {
                        int statusIdx = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int bytesIdx = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int totalIdx = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

                        int status = statusIdx >= 0 ? c.getInt(statusIdx) : -1;
                        long soFar = (bytesIdx >= 0) ? c.getLong(bytesIdx) : 0L;
                        long total = (totalIdx >= 0) ? c.getLong(totalIdx) : 0L;

                        if (total > 0) {
                            progressBar.setIndeterminate(false);
                            progressBar.setMax(100);
                            int prog = (int) ((soFar * 100L) / total);
                            progressBar.setProgress(prog);
                            percentageView.setText(prog + "%");
                        }

                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            progressBar.setVisibility(View.GONE);
                            percentageView.setVisibility(View.GONE);
                            downloadFab.setVisibility(View.GONE);
                            c.close();
                            return;
                        }
                    }
                    if (c != null) c.close();
                } catch (Exception e) {
                    Log.e("DOWNLOAD_DEBUG", "Error tracking download progress", e);
                }
                handler.postDelayed(this, 100);
            }
        };
        handler.post(r);
    }

    public static String truncateTextToWords(String text, int maxWords) {
        String[] words = text.split("\\s+");
        if (words.length <= maxWords) {
            return text;
        } else {
            return TextUtils.join(" ", Arrays.copyOfRange(words, 0, maxWords)) + " ...";
        }
    }

    public static void clear(RecyclerView.Adapter adapter) {
        adapter.notifyDataSetChanged();
    }

    public static void setforwardNameAdapter(ArrayList<forwardnameModel> forwardNameList, RecyclerView namerecyclerview, Context context) {
        forwardnameAdapter forwardnameAdapter = new forwardnameAdapter(context, forwardNameList, namerecyclerview);
        namerecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        namerecyclerview.setAdapter(forwardnameAdapter);
    }

    public static void setAdapter(ArrayList<get_user_active_contact_list_Model> get_user_active_contact_forward_list, RecyclerView recyclerview, Context context, forwardAdapter forwardAdapter, LinearLayout dx, chatAdapter chatAdapter, View richBoxForward) {
        // Filter out blocked users
        ArrayList<get_user_active_contact_list_Model> filteredList = new ArrayList<>();
        for (get_user_active_contact_list_Model model : get_user_active_contact_forward_list) {
            if (!model.isBlock()) { // ✅ Only include if not blocked
                filteredList.add(model);
            }
        }

        // Set the filtered list to your adapter
        forwardAdapter = new forwardAdapter(context, filteredList, dx, chatAdapter, (LinearLayout) richBoxForward);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(forwardAdapter);
        forwardAdapter.notifyDataSetChanged();
    }

    public static void setMediaPlayerCallback(MediaPlayerCallback callback, chatAdapter adapter) {
        adapter.mediaPlayerCallback = callback;
    }

    public static void setScrolling(boolean scrolling, chatAdapter adapter) {
        adapter.isScrolling = scrolling;
    }

    public static void smoothScrollToLast(chatAdapter adapter) {
        if (adapter.messageRecView != null && adapter.getItemCount() > 0) {
            adapter.messageRecView.post(() -> {
                // First instant scroll to prevent flickering
                adapter.messageRecView.scrollToPosition(adapter.getItemCount() - 1);
            });
        }
    }

    public static void forceScrollToLast(chatAdapter adapter) {
        if (adapter.messageRecView != null && adapter.getItemCount() > 0) {
            adapter.messageRecView.post(() -> {
                int lastPosition = adapter.getItemCount() - 1;

                // First attempt: instant scroll
                adapter.messageRecView.scrollToPosition(lastPosition);

                // Second attempt: ensure with layout manager
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) adapter.messageRecView.getLayoutManager();
                    if (layoutManager != null) {
                        layoutManager.scrollToPositionWithOffset(lastPosition, 0);
                    }
                }, 100);
            });
        }
    }

    public static void searchFilteredData(ArrayList<messageModel> filteredList, chatAdapter adapter) {
        adapter.messageList = filteredList;
        adapter.notifyDataSetChanged();
    }

    public static void scrollToTargetModelId(String receiverRoom, String targetModelId, chattingScreen screenInstance, chatAdapter adapter) {
        loadPagesUntilModelFound(receiverRoom, targetModelId, screenInstance, adapter, () -> {
            List<messageModel> messageList = screenInstance.getMessageList();
            RecyclerView messageRecView = screenInstance.getMessageRecView();
            for (int i = 0; i < messageList.size(); i++) {
                if (messageList.get(i).getModelId().equals(targetModelId)) {
                    final int foundIndex = i;

                    messageRecView.post(() -> {
                        // Use instant scroll to prevent flickering, then smooth scroll for better UX
                        messageRecView.scrollToPosition(foundIndex);

                        // Add a small delay then smooth scroll for better visual effect
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            // Add another delay to ensure the view is bound and laid out
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                RecyclerView.ViewHolder viewHolder = messageRecView.findViewHolderForAdapterPosition(foundIndex);
                                if (viewHolder != null && viewHolder.itemView != null) {
                                    // 🔥 Highlight with animation or visual effect
                                    viewHolder.itemView.setBackgroundColor(adapter.mContext.getColor(R.color.highlightcolor)); // Example highlight
                                    viewHolder.itemView.animate().setDuration(300).withEndAction(() -> {
                                        new Handler().postDelayed(() -> {
                                            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT); // Remove highlight
                                        }, 1000); // Duration of highlight
                                    }).start();
                                }
                            }, 300); // Delay for smooth scroll to complete
                        }, 100); // Delay for instant scroll to complete
                    });
                    break;
                }
            }
        });
    }

    public static void loadPagesUntilModelFound(String receiverRoom, String targetModelId, chattingScreen screenInstance, chatAdapter adapter, Runnable onComplete) {
        List<messageModel> messageList = screenInstance.getMessageList();
        boolean modelFound = false;
        for (messageModel model : messageList) {
            if (model.getModelId().equals(targetModelId)) {
                modelFound = true;
                break;
            }
        }
        if (modelFound) {
            onComplete.run();
            return;
        }
        screenInstance.loadMoreRedirection(receiverRoom, adapter.receiverUid);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            loadPagesUntilModelFound(receiverRoom, targetModelId, screenInstance, adapter, onComplete);
        }, 500);
    }
}
