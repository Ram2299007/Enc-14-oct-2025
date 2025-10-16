package com.Appzia.enclosure.Utils.ChatadapterFiles;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.chatAdapter;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.selectionBunchModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class senderReceiverDownload {

    /**
     * Starts sender image download with progress tracking
     */
    public static void startSenderImageDownloadWithProgress(RecyclerView.ViewHolder holder, messageModel model, Context context) {
        Log.d("DOWNLOAD_DEBUG", "startSenderImageDownloadWithProgress called");
        Log.d("DOWNLOAD_DEBUG", "Document URL: " + model.getDocument());
        Log.d("DOWNLOAD_DEBUG", "File Name: " + model.getFileName());

        File downloadsDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
        if (!downloadsDir.exists()) downloadsDir.mkdirs();

        File destinationFile = new File(downloadsDir, model.getFileName());

        if (destinationFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "File already exists privately");
            showToastSafe(context, "Image already downloaded");

            ((chatAdapter.senderViewHolder) holder).downlaod.setVisibility(View.GONE);
            ((chatAdapter.senderViewHolder) holder).progressBar.setVisibility(View.GONE);
            ((chatAdapter.senderViewHolder) holder).downloadPercentageImageSender.setText("");
            ((chatAdapter.senderViewHolder) holder).downloadPercentageImageSender.setVisibility(View.GONE);

            // 🔁 Copy existing private file to public folder (if not already there)
            copyPrivateImageToPublic(destinationFile, model.getFileName(), context);
            return;
        }

        // Update UI before download
        ((chatAdapter.senderViewHolder) holder).progressBar.setVisibility(View.GONE);
        ((chatAdapter.senderViewHolder) holder).downlaod.setVisibility(View.GONE);
        ((chatAdapter.senderViewHolder) holder).downloadPercentageImageSender.setVisibility(View.VISIBLE);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request;

        if ("ReplyKey".equals(model.getReplyKey())) {
            request = new DownloadManager.Request(Uri.parse(model.getReplyOldData()));
            Log.d("DOWNLOAD_DEBUG", "Using reply data URL for sender image");
        } else {
            request = new DownloadManager.Request(Uri.parse(model.getDocument()));
            Log.d("DOWNLOAD_DEBUG", "Using document URL for sender image");
        }

        request.setTitle(model.getFileName());
        request.setDescription("Downloading Image");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationUri(Uri.fromFile(destinationFile));

        long downloadId = downloadManager.enqueue(request);
        Log.d("DOWNLOAD_DEBUG", "Download started with ID: " + downloadId);

        // ✅ Track and handle completion
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == downloadId) {
                    ((chatAdapter.senderViewHolder) holder).progressBar.setVisibility(View.GONE);
                    ((chatAdapter.senderViewHolder) holder).downloadPercentageImageSender.setVisibility(View.GONE);
                    ((chatAdapter.senderViewHolder) holder).downloadPercentageImageSender.setText("");

                    if (destinationFile.exists()) {
                        showToastSafe(context, "Image downloaded");
                        // ✅ Copy to public gallery
                        copyPrivateImageToPublic(destinationFile, model.getFileName(), context);
                    } else {
                        showToastSafe(context, "Download failed");
                    }
                }
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), context.RECEIVER_EXPORTED);
    }

    /**
     * Starts sender document download with progress tracking
     */
    public static void startSenderDocDownloadWithProgress(RecyclerView.ViewHolder holder, messageModel model, Context context) {
        Log.d("DOWNLOAD_DEBUG", "startSenderDocDownloadWithProgress called");
        Log.d("DOWNLOAD_DEBUG", "Document URL: " + model.getDocument());
        Log.d("DOWNLOAD_DEBUG", "File Name: " + model.getFileName());

        // Prepare UI
        ((chatAdapter.senderViewHolder) holder).downlaodDoc.setVisibility(View.GONE);
        ((chatAdapter.senderViewHolder) holder).progressBarDoc.setVisibility(View.GONE);
        ((chatAdapter.senderViewHolder) holder).downloadPercentageDocSender.setVisibility(View.VISIBLE);
        ((chatAdapter.senderViewHolder) holder).downloadPercentageDocSender.setText("0%");

        // --- PRIVATE DOCUMENT DIRECTORY ---
        File privateDocsDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
        if (!privateDocsDir.exists()) privateDocsDir.mkdirs();
        File privateDocFile = new File(privateDocsDir, model.getFileName());

        // --- If already exists ---
        if (privateDocFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "Document already exists, skipping download");
            ((chatAdapter.senderViewHolder) holder).progressBarDoc.setVisibility(View.GONE);
            ((chatAdapter.senderViewHolder) holder).downloadPercentageDocSender.setVisibility(View.GONE);
            ((chatAdapter.senderViewHolder) holder).downloadPercentageDocSender.setText("");

            copyDocToPublicDoc(privateDocFile, model.getFileName(), context); // Copy to public if missing
            return;
        }

        // --- Setup DownloadManager ---
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(model.getDocument()));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        req.setAllowedOverRoaming(false);
        req.setTitle(model.getFileName());
        req.setDescription("Downloading document");
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        req.setDestinationUri(Uri.fromFile(privateDocFile));

        long docDownloadId = dm.enqueue(req);
        Log.d("DOWNLOAD_DEBUG", "Document Download ID: " + docDownloadId);

        // --- Track progress ---
        trackDocDownloadProgress(
                docDownloadId,
                ((chatAdapter.senderViewHolder) holder).progressBarDoc,
                ((chatAdapter.senderViewHolder) holder).downloadPercentageDocSender,
                ((chatAdapter.senderViewHolder) holder).downlaodDoc,
                context
        );

        // --- Copy to public after download completes ---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != docDownloadId) return;

                ((chatAdapter.senderViewHolder) holder).progressBarDoc.setVisibility(View.GONE);
                ((chatAdapter.senderViewHolder) holder).downloadPercentageDocSender.setText("");

                // Copy document to public folder
                copyDocToPublicDoc(privateDocFile, model.getFileName(), context);
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), context.RECEIVER_EXPORTED);
    }

    /**
     * Copies a file from private app storage to public Pictures/Enclosure folder
     */
    private static void copyPrivateImageToPublic(File privateFile, String fileName, Context context) {
        new Thread(() -> {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(privateFile.getAbsolutePath());
                if (bitmap == null) {
                    showToastSafe(context, "Failed to read image for public copy");
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
                        showToastSafe(context, "Image copied to public gallery");
                    } else {
                        showToastSafe(context, "Failed to save image to gallery");
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

                    showToastSafe(context, "Image copied to public gallery");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToastSafe(context, "Error copying image to gallery");
            }
        }).start();
    }

    /**
     * Starts sender video download with progress tracking
     */
    public static void startSenderVideoDownloadWithProgress(RecyclerView.ViewHolder holder, messageModel model, Context context) {
        Log.d("DOWNLOAD_DEBUG", "startSenderVideoDownloadWithProgress called");

        // --- PRIVATE VIDEO PATH ---
        File privateVideoDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
        if (!privateVideoDir.exists()) privateVideoDir.mkdirs();
        File privateVideoFile = new File(privateVideoDir, model.getFileName());

        // --- Skip if already downloaded privately ---
        if (privateVideoFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "Video file already exists privately: " + privateVideoFile.getAbsolutePath());
            ((chatAdapter.senderViewHolder) holder).downlaodVideo.setVisibility(View.GONE);
            ((chatAdapter.senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
            ((chatAdapter.senderViewHolder) holder).downloadPercentageVideoSender.setText("");
            ((chatAdapter.senderViewHolder) holder).downloadPercentageVideoSender.setVisibility(View.VISIBLE);
            copyVideoToPublic(privateVideoFile, model.getFileName(), context); // try public copy if missing
            return;
        }

        // --- Show progress UI ---
        ((chatAdapter.senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
        ((chatAdapter.senderViewHolder) holder).downlaodVideo.setVisibility(View.GONE);
        ((chatAdapter.senderViewHolder) holder).downloadPercentageVideoSender.setVisibility(View.VISIBLE);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(model.getDocument()));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(model.getFileName());
        request.setDescription("Downloading video...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationUri(Uri.fromFile(privateVideoFile));

        long downloadId = downloadManager.enqueue(request);
        Log.d("DOWNLOAD_DEBUG", "Sender Video Download ID: " + downloadId);

        trackSenderVideoDownloadProgress(downloadId, ((chatAdapter.senderViewHolder) holder).downloadPercentageVideoSender, holder, context);

        // --- Use BroadcastReceiver to copy after DownloadManager completes ---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != downloadId) return;

                // Hide progress UI
                ((chatAdapter.senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
                ((chatAdapter.senderViewHolder) holder).downloadPercentageVideoSender.setText("");

                // --- Copy video to public folder ---
                copyVideoToPublic(privateVideoFile, model.getFileName(), context);
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), context.RECEIVER_EXPORTED);

        // --- Download thumbnail privately only ---
        if (model.getThumbnail() != null && !model.getThumbnail().isEmpty()) {
            File thumbnailDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Enclosure/Media/Thumbnail");
            if (!thumbnailDir.exists()) thumbnailDir.mkdirs();

            String thumbnailFileName = model.getFileNameThumbnail();
            File thumbnailFile = new File(thumbnailDir, thumbnailFileName);

            DownloadManager.Request thumbRequest = new DownloadManager.Request(Uri.parse(model.getThumbnail()));
            thumbRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            thumbRequest.setAllowedOverRoaming(false);
            thumbRequest.setTitle("Downloading thumbnail");
            thumbRequest.setDescription("Thumbnail download in progress...");
            thumbRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            thumbRequest.setDestinationUri(Uri.fromFile(thumbnailFile));

            downloadManager.enqueue(thumbRequest);
        }
    }

    /**
     * Copies a video file from private app storage to public Movies/Enclosure folder
     */
    private static void copyVideoToPublic(File privateFile, String fileName, Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);
                values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                values.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/Enclosure");
                values.put(MediaStore.Video.Media.IS_PENDING, 1);

                Uri uri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    try (InputStream in = new FileInputStream(privateFile);
                         OutputStream out = context.getContentResolver().openOutputStream(uri)) {
                        byte[] buffer = new byte[8192];
                        int read;
                        while ((read = in.read(buffer)) != -1) {
                            out.write(buffer, 0, read);
                        }
                        out.flush();
                    }
                    values.clear();
                    values.put(MediaStore.Video.Media.IS_PENDING, 0);
                    context.getContentResolver().update(uri, values, null, null);
                    Log.d("DOWNLOAD_DEBUG", "Video saved publicly via MediaStore: " + fileName);
                }
            } else {
                File publicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Enclosure");
                if (!publicDir.exists()) publicDir.mkdirs();
                File publicFile = new File(publicDir, fileName);

                copyFile(privateFile, publicFile);

                MediaScannerConnection.scanFile(
                        context,
                        new String[]{publicFile.getAbsolutePath()},
                        null,
                        (path, uri) -> Log.d("DOWNLOAD_DEBUG", "Video scanned: " + uri)
                );
                Log.d("DOWNLOAD_DEBUG", "Video copied publicly: " + publicFile.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.e("DOWNLOAD_DEBUG", "Error copying video to public folder", e);
        }
    }

    /**
     * Tracks sender video download progress
     */
    private static void trackSenderVideoDownloadProgress(long downloadId, TextView percentageView, RecyclerView.ViewHolder holder, Context context) {
        Handler handler = new Handler();
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);

                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    int bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                    int bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                    int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);

                    if (bytesDownloadedIndex != -1 && bytesTotalIndex != -1 && statusIndex != -1) {
                        long bytesDownloaded = cursor.getLong(bytesDownloadedIndex);
                        long bytesTotal = cursor.getLong(bytesTotalIndex);

                        if (bytesTotal > 0) {
                            int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                            percentageView.setText(progress + "%");
                        }

                        int status = cursor.getInt(statusIndex);
                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            percentageView.setVisibility(View.GONE);
                            ((chatAdapter.senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
                            ((chatAdapter.senderViewHolder) holder).blurVideo.setVisibility(View.GONE);
                            cursor.close();
                            return;
                        }
                    }
                }
                cursor.close();
                handler.postDelayed(this, 100);
            }
        };
        handler.post(progressRunnable);
    }

    /**
     * Helper method to copy files
     */
    private static void copyFile(File source, File dest) throws IOException {
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

    /**
     * Copies a document file from private app storage to public Documents/Enclosure folder
     */
    private static void copyDocToPublicDoc(File privateFile, String fileName, Context context) {
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

                copyFile(privateFile, publicFile);

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

    /**
     * Tracks document download progress
     */
    private static void trackDocDownloadProgress(long downloadId, ProgressBar progressBar, TextView percentageView, View downloadFab, Context context) {
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

    /**
     * Starts sender audio download with progress tracking
     */
    public static void startSenderAudioDownloadWithProgress(RecyclerView.ViewHolder holder, messageModel model, Context context) {
        Log.d("DOWNLOAD_DEBUG", "startSenderAudioDownloadWithProgress called");
        Log.d("DOWNLOAD_DEBUG", "Document URL: " + model.getDocument());
        Log.d("DOWNLOAD_DEBUG", "File Name: " + model.getFileName());

        // Prepare UI
        ((chatAdapter.senderViewHolder) holder).downlaodAudio.setVisibility(View.GONE);
        ((chatAdapter.senderViewHolder) holder).progressBarAudio.setIndeterminate(true);
        ((chatAdapter.senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
        ((chatAdapter.senderViewHolder) holder).downloadPercentageAudioSender.setText("0%");
        ((chatAdapter.senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.VISIBLE);

        // --- PRIVATE AUDIO DIRECTORY ---
        File privateAudiosDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
        if (!privateAudiosDir.exists()) privateAudiosDir.mkdirs();
        File privateAudioFile = new File(privateAudiosDir, model.getFileName());

        // --- If already exists ---
        if (privateAudioFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "Audio already exists, skipping download");
            ((chatAdapter.senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
            ((chatAdapter.senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.GONE);
            ((chatAdapter.senderViewHolder) holder).downloadPercentageAudioSender.setText("");

            copyDocToPublicDoc(privateAudioFile, model.getFileName(), context); // Copy to public if missing
            return;
        }

        // --- Setup DownloadManager ---
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(model.getDocument()));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        req.setAllowedOverRoaming(false);
        req.setTitle(model.getFileName());
        req.setDescription("Downloading audio");
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        req.setDestinationUri(Uri.fromFile(privateAudioFile));

        long audioDownloadId = dm.enqueue(req);
        Log.d("DOWNLOAD_DEBUG", "Audio Download ID: " + audioDownloadId);

        // --- Track progress ---
        trackSenderAudioDownloadProgress(
                audioDownloadId,
                ((chatAdapter.senderViewHolder) holder).progressBarAudio,
                ((chatAdapter.senderViewHolder) holder).downloadPercentageAudioSender,
                ((chatAdapter.senderViewHolder) holder).downlaodAudio,
                context
        );

        // --- Copy to public after download completes ---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != audioDownloadId) return;

                ((chatAdapter.senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
                ((chatAdapter.senderViewHolder) holder).downloadPercentageAudioSender.setText("");

                // Copy audio to public folder
                copyDocToPublicDoc(privateAudioFile, model.getFileName(), context);
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), context.RECEIVER_EXPORTED);
    }

    /**
     * Tracks sender audio download progress
     */
    private static void trackSenderAudioDownloadProgress(long downloadId, ProgressBar progressBar, TextView percentageView, View downloadFab, Context context) {
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

    /**
     * Starts receiver video download with progress tracking
     */
    public static void startVideoDownloadWithProgress(RecyclerView.ViewHolder holder, messageModel model, Context context) {
        Log.d("DOWNLOAD_DEBUG", "startVideoDownloadWithProgress called");

        // --- PRIVATE VIDEO PATH ---
        File privateVideoDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
        if (!privateVideoDir.exists()) privateVideoDir.mkdirs();
        File privateVideoFile = new File(privateVideoDir, model.getFileName());

        // --- PRIVATE THUMBNAIL PATH ---
        File privateThumbnailDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Enclosure/Media/Thumbnail");
        if (!privateThumbnailDir.exists()) privateThumbnailDir.mkdirs();
        File privateThumbnailFile = new File(privateThumbnailDir, model.getFileNameThumbnail());

        // --- Skip if already downloaded ---
        if (privateVideoFile.exists() && privateThumbnailFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "Video and thumbnail already exist, skipping download");
            ((chatAdapter.receiverViewHolder) holder).downlaodVideo.setVisibility(View.GONE);
            ((chatAdapter.receiverViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
            ((chatAdapter.receiverViewHolder) holder).downloadPercentageVideo.setText("Downloaded");
            ((chatAdapter.receiverViewHolder) holder).downloadPercentageVideo.setVisibility(View.VISIBLE);
            copyVideoToPublic2(privateVideoFile, model.getFileName(), context); // copy to public if missing
            return;
        }

        // --- UI setup ---
        ((chatAdapter.receiverViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
        ((chatAdapter.receiverViewHolder) holder).downlaodVideo.setVisibility(View.GONE);
        ((chatAdapter.receiverViewHolder) holder).downloadPercentageVideo.setVisibility(View.VISIBLE);
        ((chatAdapter.receiverViewHolder) holder).blurVideo.setVisibility(View.GONE);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        // --- Download Thumbnail (private only) ---
        if (!privateThumbnailFile.exists() && model.getThumbnail() != null && !model.getThumbnail().isEmpty()) {
            try {
                DownloadManager.Request thumbRequest = new DownloadManager.Request(Uri.parse(model.getThumbnail()));
                thumbRequest.setTitle(model.getFileNameThumbnail());
                thumbRequest.setDescription("Downloading Video Thumbnail");
                thumbRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                thumbRequest.setDestinationUri(Uri.fromFile(privateThumbnailFile));
                downloadManager.enqueue(thumbRequest);
                Log.d("DOWNLOAD_DEBUG", "Thumbnail download started: " + model.getFileNameThumbnail());
            } catch (Exception e) {
                Log.e("DOWNLOAD_DEBUG", "Error downloading thumbnail: " + e.getMessage());
            }
        }

        // --- Download Video ---
        if (!privateVideoFile.exists()) {
            DownloadManager.Request videoRequest;
            if ("ReplyKey".equals(model.getReplyKey())) {
                videoRequest = new DownloadManager.Request(Uri.parse(model.getReplyOldData()));
                Log.d("DOWNLOAD_DEBUG", "Using reply data URL for video");
            } else {
                videoRequest = new DownloadManager.Request(Uri.parse(model.getDocument()));
                Log.d("DOWNLOAD_DEBUG", "Using document URL for video");
            }

            videoRequest.setTitle(model.getFileName());
            videoRequest.setDescription("Downloading Video");
            videoRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

            if (!privateVideoDir.exists()) privateVideoDir.mkdirs();
            videoRequest.setDestinationUri(Uri.fromFile(privateVideoFile));

            long videoDownloadId = downloadManager.enqueue(videoRequest);
            Log.d("DOWNLOAD_DEBUG", "Video Download ID: " + videoDownloadId);

            trackVideoDownloadProgress(videoDownloadId, ((chatAdapter.receiverViewHolder) holder).downloadPercentageVideo, holder, context);

            // --- Copy video to public folder after download completes ---
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (id != videoDownloadId) return;

                    // Hide progress UI
                    ((chatAdapter.receiverViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
                    ((chatAdapter.receiverViewHolder) holder).downloadPercentageVideo.setText("");

                    // Copy to public folder
                    copyVideoToPublic2(privateVideoFile, model.getFileName(), context);
                }
            }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), context.RECEIVER_EXPORTED);
        } else {
            Log.d("DOWNLOAD_DEBUG", "Video already exists: " + model.getFileName());
            ((chatAdapter.receiverViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
            ((chatAdapter.receiverViewHolder) holder).downloadPercentageVideo.setVisibility(View.GONE);
            ((chatAdapter.receiverViewHolder) holder).downlaodVideo.setVisibility(View.GONE);
        }
    }

    /**
     * Copies a video file from private app storage to public Movies/Enclosure folder (receiver)
     */
    private static void copyVideoToPublic2(File privateFile, String fileName, Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);
                values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                values.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/Enclosure");
                values.put(MediaStore.Video.Media.IS_PENDING, 1);

                Uri uri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    try (InputStream in = new FileInputStream(privateFile);
                         OutputStream out = context.getContentResolver().openOutputStream(uri)) {
                        byte[] buffer = new byte[8192];
                        int read;
                        while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
                        out.flush();
                    }
                    values.clear();
                    values.put(MediaStore.Video.Media.IS_PENDING, 0);
                    context.getContentResolver().update(uri, values, null, null);
                    Log.d("DOWNLOAD_DEBUG", "Video saved publicly via MediaStore: " + fileName);
                }
            } else {
                File publicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Enclosure");
                if (!publicDir.exists()) publicDir.mkdirs();
                File publicFile = new File(publicDir, fileName);

                copyFile(privateFile, publicFile);

                MediaScannerConnection.scanFile(
                        context,
                        new String[]{publicFile.getAbsolutePath()},
                        null,
                        (path, uri) -> Log.d("DOWNLOAD_DEBUG", "Video scanned: " + uri)
                );
                Log.d("DOWNLOAD_DEBUG", "Video copied publicly: " + publicFile.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.e("DOWNLOAD_DEBUG", "Error copying video to public folder", e);
        }
    }

    /**
     * Tracks receiver video download progress
     */
    private static void trackVideoDownloadProgress(long downloadId, TextView percentageView, RecyclerView.ViewHolder holder, Context context) {
        Handler handler = new Handler();
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);

                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    int bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                    int bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                    int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);

                    if (bytesDownloadedIndex != -1 && bytesTotalIndex != -1 && statusIndex != -1) {
                        int bytesDownloaded = cursor.getInt(bytesDownloadedIndex);
                        int bytesTotal = cursor.getInt(bytesTotalIndex);

                        if (bytesTotal > 0) {
                            int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                            percentageView.setText(progress + "%");

                            if (progress >= 100) {
                                percentageView.setVisibility(View.GONE);
                                ((chatAdapter.receiverViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
                                cursor.close();
                                percentageView.setText(0 + "%");
                                return;
                            }
                        }

                        int status = cursor.getInt(statusIndex);
                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            percentageView.setVisibility(View.GONE);
                            ((chatAdapter.receiverViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
                            cursor.close();
                            return;
                        }
                    }
                }
                cursor.close();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(progressRunnable);
    }

    /**
     * Starts receiver image download with progress tracking using Firebase Storage
     */
    public static void startReceiverImageDownloadWithProgressFirebase(RecyclerView.ViewHolder holder, messageModel model, Context context) {
        chatAdapter.receiverViewHolder viewHolder = (chatAdapter.receiverViewHolder) holder;

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(model.getDocument());
        File downloadsDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
        if (!downloadsDir.exists()) downloadsDir.mkdirs();
        File destinationFile = new File(downloadsDir, model.getFileName());

        if (destinationFile.exists()) {
            //   showToastSafe("Image already downloaded");
            viewHolder.downlaod.setVisibility(View.GONE);
            viewHolder.downloadPercentageImage.setVisibility(View.GONE);
            copyPrivateImageToPublic(destinationFile, model.getFileName(), context);
            return;
        }

        viewHolder.downlaod.setVisibility(View.GONE);
        viewHolder.downloadPercentageImage.setVisibility(View.VISIBLE);
        viewHolder.downloadPercentageImage.setText("0%");

        storageRef.getFile(destinationFile)
                .addOnProgressListener(snapshot -> {
                    long totalBytes = snapshot.getTotalByteCount();
                    long downloadedBytes = snapshot.getBytesTransferred();
                    if (totalBytes > 0) {
                        int progress = (int) ((downloadedBytes * 100) / totalBytes);
                        viewHolder.downloadPercentageImage.setText(progress + "%");
                    }
                })
                .addOnSuccessListener(taskSnapshot -> {
                    viewHolder.downloadPercentageImage.setVisibility(View.GONE);
                    viewHolder.downloadPercentageImage.setText("");
                    // showToastSafe("Image downloaded");
                    copyPrivateImageToPublic(destinationFile, model.getFileName(), context);
                })
                .addOnFailureListener(e -> {
                    viewHolder.downloadPercentageImage.setText("❌");
                    showToastSafe(context, "Download failed: " + e.getMessage());
                });
    }

    /**
     * Starts receiver document download with progress tracking
     */
    public static void startReceiverDocDownloadWithProgress(RecyclerView.ViewHolder holder, messageModel model, Context context) {
        Log.d("DOWNLOAD_DEBUG", "startReceiverDocDownloadWithProgress called");
        Log.d("DOWNLOAD_DEBUG", "Document URL: " + model.getDocument());
        Log.d("DOWNLOAD_DEBUG", "File Name: " + model.getFileName());

        // Prepare UI
        ((chatAdapter.receiverViewHolder) holder).downlaodDocReceiver.setVisibility(View.GONE);
        ((chatAdapter.receiverViewHolder) holder).progressBarDocReceiver.setVisibility(View.GONE);
        ((chatAdapter.receiverViewHolder) holder).downloadPercentageDocReceiver.setVisibility(View.VISIBLE);
        ((chatAdapter.receiverViewHolder) holder).downloadPercentageDocReceiver.setText("0%");

        // --- PRIVATE DOCUMENT DIRECTORY ---
        File privateDocsDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
        if (!privateDocsDir.exists()) privateDocsDir.mkdirs();
        File privateDocFile = new File(privateDocsDir, model.getFileName());

        // --- If already exists ---
        if (privateDocFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "Document already exists, skipping download");
            ((chatAdapter.receiverViewHolder) holder).progressBarDocReceiver.setVisibility(View.GONE);
            ((chatAdapter.receiverViewHolder) holder).downloadPercentageDocReceiver.setVisibility(View.GONE);
            ((chatAdapter.receiverViewHolder) holder).downloadPercentageDocReceiver.setText("");

            copyDocToPublicDoc(privateDocFile, model.getFileName(), context); // Copy to public if missing
            return;
        }

        // --- Setup DownloadManager ---
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(model.getDocument()));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        req.setAllowedOverRoaming(false);
        req.setTitle(model.getFileName());
        req.setDescription("Downloading document");
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        req.setDestinationUri(Uri.fromFile(privateDocFile));

        long docDownloadId = dm.enqueue(req);
        Log.d("DOWNLOAD_DEBUG", "Document Download ID: " + docDownloadId);

        // --- Track progress ---
        trackDocDownloadProgress(
                docDownloadId,
                ((chatAdapter.receiverViewHolder) holder).progressBarDocReceiver,
                ((chatAdapter.receiverViewHolder) holder).downloadPercentageDocReceiver,
                ((chatAdapter.receiverViewHolder) holder).downlaodDocReceiver,
                context
        );

        // --- Copy to public after download completes ---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != docDownloadId) return;

                ((chatAdapter.receiverViewHolder) holder).progressBarDocReceiver.setVisibility(View.GONE);
                ((chatAdapter.receiverViewHolder) holder).downloadPercentageDocReceiver.setText("");

                // Copy document to public folder
                copyDocToPublicDoc(privateDocFile, model.getFileName(), context);
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), context.RECEIVER_EXPORTED);
    }

    /**
     * Starts receiver audio download with progress tracking
     */
    public static void startReceiverAudioDownloadWithProgress(RecyclerView.ViewHolder holder, messageModel model, Context context) {
        Log.d("DOWNLOAD_DEBUG", "startReceiverAudioDownloadWithProgress called");
        Log.d("DOWNLOAD_DEBUG", "Document URL: " + model.getDocument());
        Log.d("DOWNLOAD_DEBUG", "File Name: " + model.getFileName());

        // Prepare UI
        ((chatAdapter.receiverViewHolder) holder).downlaodAudioReceiver.setVisibility(View.GONE);
        ((chatAdapter.receiverViewHolder) holder).progressBarAudioReceiver.setIndeterminate(true);
        ((chatAdapter.receiverViewHolder) holder).progressBarAudioReceiver.setVisibility(View.GONE);
        ((chatAdapter.receiverViewHolder) holder).downloadPercentageAudioReceiver.setText("0%");
        ((chatAdapter.receiverViewHolder) holder).downloadPercentageAudioReceiver.setVisibility(View.VISIBLE);

        // --- PRIVATE AUDIO DIRECTORY ---
        File privateAudiosDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
        if (!privateAudiosDir.exists()) privateAudiosDir.mkdirs();
        File privateAudioFile = new File(privateAudiosDir, model.getFileName());

        // --- If already exists ---
        if (privateAudioFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "Audio already exists, skipping download");
            ((chatAdapter.receiverViewHolder) holder).progressBarAudioReceiver.setVisibility(View.GONE);
            ((chatAdapter.receiverViewHolder) holder).downloadPercentageAudioReceiver.setVisibility(View.GONE);
            ((chatAdapter.receiverViewHolder) holder).downloadPercentageAudioReceiver.setText("");

            copyDocToPublicDoc(privateAudioFile, model.getFileName(), context); // Copy to public if missing
            return;
        }

        // --- Setup DownloadManager ---
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(model.getDocument()));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        req.setAllowedOverRoaming(false);
        req.setTitle(model.getFileName());
        req.setDescription("Downloading audio");
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        req.setDestinationUri(Uri.fromFile(privateAudioFile));

        long audioDownloadId = dm.enqueue(req);
        Log.d("DOWNLOAD_DEBUG", "Audio Download ID: " + audioDownloadId);

        // --- Track progress ---
        trackReceiverAudioDownloadProgress(
                audioDownloadId,
                ((chatAdapter.receiverViewHolder) holder).progressBarAudioReceiver,
                ((chatAdapter.receiverViewHolder) holder).downloadPercentageAudioReceiver,
                ((chatAdapter.receiverViewHolder) holder).downlaodAudioReceiver,
                model,
                context
        );

        // --- Copy to public after download completes ---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != audioDownloadId) return;

                ((chatAdapter.receiverViewHolder) holder).progressBarAudioReceiver.setVisibility(View.GONE);
                ((chatAdapter.receiverViewHolder) holder).downloadPercentageAudioReceiver.setText("");

                // Copy audio to public folder
                copyDocToPublicDoc(privateAudioFile, model.getFileName(), context);
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), context.RECEIVER_EXPORTED);
    }

    /**
     * Tracks receiver audio download progress
     */
    private static void trackReceiverAudioDownloadProgress(long downloadId, ProgressBar progressBar, TextView percentageView, View downloadFab, messageModel model, Context context) {
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

    /**
     * Toast helper (adapter-safe)
     */
    private static void showToastSafe(Context context, String message) {
        // Toast functionality can be implemented here if needed
        // For now, keeping it empty as in the original implementation
    }

    public static void startSenderAudioDownloadWithProgressXDocument(RecyclerView.ViewHolder holder, messageModel model, Context context) {
        // Prepare UI
        ((chatAdapter.senderViewHolder) holder).downlaodAudio.setVisibility(View.GONE);
        ((chatAdapter.senderViewHolder) holder).progressBarAudio.setIndeterminate(true);
        ((chatAdapter.senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
        ((chatAdapter.senderViewHolder) holder).downloadPercentageAudioSender.setText("0%");
        ((chatAdapter.senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.VISIBLE);

        // Ensure destination dir exists
        File audiosDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
        if (!audiosDir.exists()) audiosDir.mkdirs();

        try {
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(model.getDocument());
            DownloadManager.Request req = new DownloadManager.Request(uri)
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setTitle(model.getFileName())
                    .setDescription("Downloading audio")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Documents/" + model.getFileName());

            long audioDownloadId = dm.enqueue(req);
            trackSenderAudioDownloadProgress(audioDownloadId, 
                    ((chatAdapter.senderViewHolder) holder).progressBarAudio, 
                    ((chatAdapter.senderViewHolder) holder).downloadPercentageAudioSender, 
                    ((chatAdapter.senderViewHolder) holder).downlaodAudio, 
                    context);
        } catch (Exception e) {
            // Reset UI on error
            ((chatAdapter.senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.GONE);
            ((chatAdapter.senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
            ((chatAdapter.senderViewHolder) holder).downlaodAudio.setVisibility(View.VISIBLE);
        }
    }

    public static void startReceiverAudioDownloadWithProgressXDocuments(RecyclerView.ViewHolder holder, messageModel model, Context context) {
        // Prepare UI
        ((chatAdapter.receiverViewHolder) holder).downlaodAudioReceiver.setVisibility(View.GONE);
        ((chatAdapter.receiverViewHolder) holder).progressBarAudioReceiver.setIndeterminate(true);
        ((chatAdapter.receiverViewHolder) holder).progressBarAudioReceiver.setVisibility(View.GONE);
        ((chatAdapter.receiverViewHolder) holder).downloadPercentageAudioReceiver.setText("0%");
        ((chatAdapter.receiverViewHolder) holder).downloadPercentageAudioReceiver.setVisibility(View.VISIBLE);

        // Ensure destination dir exists
        File audiosDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
        if (!audiosDir.exists()) audiosDir.mkdirs();

        try {
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(model.getDocument());
            DownloadManager.Request req = new DownloadManager.Request(uri)
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setTitle(model.getFileName())
                    .setDescription("Downloading audio")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOCUMENTS, "Enclosure/Media/Documents/" + model.getFileName());

            long audioDownloadId = dm.enqueue(req);
            trackReceiverAudioDownloadProgress(audioDownloadId, 
                    ((chatAdapter.receiverViewHolder) holder).progressBarAudioReceiver, 
                    ((chatAdapter.receiverViewHolder) holder).downloadPercentageAudioReceiver, 
                    ((chatAdapter.receiverViewHolder) holder).downlaodAudioReceiver, 
                    model, 
                    context);
        } catch (Exception e) {
            // Reset UI on error
            ((chatAdapter.receiverViewHolder) holder).downloadPercentageAudioReceiver.setVisibility(View.GONE);
            ((chatAdapter.receiverViewHolder) holder).progressBarAudioReceiver.setVisibility(View.GONE);
            ((chatAdapter.receiverViewHolder) holder).downlaodAudioReceiver.setVisibility(View.VISIBLE);
        }
    }

    public static void downloadAllSelectionBunchImages(chatAdapter.senderViewHolder holder, messageModel model, int position, Context context) {
        Log.d("SelectionBunch", "=== STARTING DOWNLOAD ALL SELECTION BUNCH IMAGES (SENDER) ===");
        Log.d("SelectionBunch", "MessageId: " + model.getModelId());

        if (model.getSelectionBunch() == null || model.getSelectionBunch().isEmpty()) {
            Log.w("SelectionBunch", "No selectionBunch to download");
            return;
        }

        Log.d("SelectionBunch", "Total images in selectionBunch: " + model.getSelectionBunch().size());

        // Filter to only missing images
        List<selectionBunchModel> missingImages = getMissingSelectionBunchImages(model.getSelectionBunch(), context);

        if (missingImages.isEmpty()) {
            Log.d("SelectionBunch", "No missing images to download - all images already exist locally");
            holder.downlaodImgBunch.setVisibility(View.GONE);
            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);
            return;
        }

        Log.d("SelectionBunch", "Found " + missingImages.size() + " missing images out of " + model.getSelectionBunch().size() + " total");
        Log.d("SelectionBunch", "Missing images details:");
        for (int i = 0; i < missingImages.size(); i++) {
            selectionBunchModel bunch = missingImages.get(i);
            Log.d("SelectionBunch", "  Missing " + i + ": " + bunch.getFileName() + " (URL: " +
                    (TextUtils.isEmpty(bunch.getImgUrl()) ? "EMPTY" : "HAS_URL") + ")");
        }

        // Show progress and hide download button immediately
        holder.downlaodImgBunch.setVisibility(View.GONE);  // Hide download button immediately
        holder.downloadPercentageImageSenderBunch.setVisibility(View.VISIBLE);
        holder.downloadPercentageImageSenderBunch.setText("0%");

        // Create download task for each missing image
        int totalMissingImages = missingImages.size();
        final int[] downloadedCount = {0};

        for (selectionBunchModel bunch : missingImages) {
            if (bunch == null || TextUtils.isEmpty(bunch.getImgUrl())) {
                continue;
            }

            // Download each missing image
            downloadSelectionBunchImage(bunch, new DownloadCallback() {
                @Override
                public void onProgress(int progress) {
                    // Update progress for this specific image
                    int overallProgress = (downloadedCount[0] * 100 + progress) / totalMissingImages;
                    // Update UI on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        holder.downloadPercentageImageSenderBunch.setText(overallProgress + "%");
                        Log.d("SelectionBunch", "Updated progress: " + overallProgress + "%");
                    });
                }

                @Override
                public void onSuccess() {
                    downloadedCount[0]++;
                    int overallProgress = (downloadedCount[0] * 100) / totalMissingImages;
                    // Update UI on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        holder.downloadPercentageImageSenderBunch.setText(overallProgress + "%");

                        if (downloadedCount[0] >= totalMissingImages) {
                            // All missing images downloaded
                            holder.downlaodImgBunch.setVisibility(View.GONE);
                            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);
                            Log.d("SelectionBunch", "All missing selectionBunch images downloaded successfully");
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    Log.e("SelectionBunch", "Failed to download image: " + error);
                    // Continue with other images even if one fails
                    downloadedCount[0]++;
                    // Update UI on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Log.d("SelectionBunch", "Image download failed, continuing with others: " + downloadedCount[0] + "/" + totalMissingImages);
                        if (downloadedCount[0] >= totalMissingImages) {
                            Log.d("SelectionBunch", "All download attempts completed, hiding download button");
                            holder.downlaodImgBunch.setVisibility(View.GONE);
                            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);
                        }
                    });
                }
            }, context);
        }
    }

    public static void downloadAllSelectionBunchImagesReceiver(chatAdapter.receiverViewHolder holder, messageModel model, int position, Context context) {
        Log.d("SelectionBunch", "=== STARTING DOWNLOAD ALL SELECTION BUNCH IMAGES (RECEIVER) ===");
        Log.d("SelectionBunch", "MessageId: " + model.getModelId());
        Log.d("SelectionBunch", "SelectionBunch size: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

        if (model.getSelectionBunch() == null || model.getSelectionBunch().isEmpty()) {
            Log.w("SelectionBunch", "No selectionBunch to download");
            return;
        }

        Log.d("SelectionBunch", "Total images in selectionBunch: " + model.getSelectionBunch().size());

        // Filter to only missing images
        List<selectionBunchModel> missingImages = getMissingSelectionBunchImages(model.getSelectionBunch(), context);

        if (missingImages.isEmpty()) {
            Log.d("SelectionBunch", "No missing images to download - all images already exist locally (receiver)");
            holder.downlaodImgBunch.setVisibility(View.GONE);
            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);
            return;
        }

        Log.d("SelectionBunch", "Found " + missingImages.size() + " missing images out of " + model.getSelectionBunch().size() + " total (receiver)");
        Log.d("SelectionBunch", "Missing images details:");
        for (int i = 0; i < missingImages.size(); i++) {
            selectionBunchModel bunch = missingImages.get(i);
            Log.d("SelectionBunch", "  Missing " + i + ": " + bunch.getFileName() + " (URL: " +
                    (TextUtils.isEmpty(bunch.getImgUrl()) ? "EMPTY" : "HAS_URL") + ")");
        }

        // Show progress and hide download button immediately
        holder.downlaodImgBunch.setVisibility(View.GONE);  // Hide download button immediately
        holder.downloadPercentageImageSenderBunch.setVisibility(View.VISIBLE);
        holder.downloadPercentageImageSenderBunch.setText("0%");

        // Create download task for each missing image
        int totalMissingImages = missingImages.size();
        final int[] downloadedCount = {0};

        for (selectionBunchModel bunch : missingImages) {
            if (bunch == null || TextUtils.isEmpty(bunch.getImgUrl())) {
                continue;
            }

            // Download each missing image
            downloadSelectionBunchImage(bunch, new DownloadCallback() {
                @Override
                public void onProgress(int progress) {
                    // Update progress for this specific image
                    int overallProgress = (downloadedCount[0] * 100 + progress) / totalMissingImages;
                    // Update UI on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        holder.downloadPercentageImageSenderBunch.setText(overallProgress + "%");
                        Log.d("SelectionBunch", "Updated progress: " + overallProgress + "%");
                    });
                }

                @Override
                public void onSuccess() {
                    downloadedCount[0]++;
                    int overallProgress = (downloadedCount[0] * 100) / totalMissingImages;
                    // Update UI on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        holder.downloadPercentageImageSenderBunch.setText(overallProgress + "%");
                        Log.d("SelectionBunch", "Image " + downloadedCount[0] + "/" + totalMissingImages + " downloaded successfully");

                        if (downloadedCount[0] >= totalMissingImages) {
                            // All missing images downloaded
                            Log.d("SelectionBunch", "All images downloaded, hiding download button");
                            holder.downlaodImgBunch.setVisibility(View.GONE);
                            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);
                            Log.d("SelectionBunch", "All missing selectionBunch images downloaded successfully (receiver)");
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    Log.e("SelectionBunch", "Failed to download image: " + error);
                    // Continue with other images even if one fails
                    downloadedCount[0]++;
                    // Update UI on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Log.d("SelectionBunch", "Image download failed, continuing with others: " + downloadedCount[0] + "/" + totalMissingImages);
                        if (downloadedCount[0] >= totalMissingImages) {
                            Log.d("SelectionBunch", "All download attempts completed, hiding download button");
                            holder.downlaodImgBunch.setVisibility(View.GONE);
                            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);
                        }
                    });
                }
            }, context);
        }
    }

    private static List<selectionBunchModel> getMissingSelectionBunchImages(List<selectionBunchModel> selectionBunch, Context context) {
        Log.d("SelectionBunch", "=== GETTING MISSING SELECTION BUNCH IMAGES ===");
        List<selectionBunchModel> missingImages = new ArrayList<>();

        if (selectionBunch == null || selectionBunch.isEmpty()) {
            Log.d("SelectionBunch", "SelectionBunch is null or empty, returning empty missing list");
            return missingImages;
        }

        Log.d("SelectionBunch", "Checking " + selectionBunch.size() + " images for missing status");

        for (int i = 0; i < selectionBunch.size(); i++) {
            selectionBunchModel bunch = selectionBunch.get(i);
            if (bunch == null || TextUtils.isEmpty(bunch.getFileName())) {
                Log.d("SelectionBunch", "Image " + i + " - null or empty filename, skipping");
                continue; // Skip if no filename
            }

            File customFolder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                customFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
            } else {
                customFolder = new File(context.getExternalFilesDir(null), "Enclosure/Media/Images");
            }

            // Decode URL-encoded characters in filename
            String decodedFileName = bunch.getFileName();
            try {
                decodedFileName = java.net.URLDecoder.decode(bunch.getFileName(), "UTF-8");
                Log.d("SelectionBunch", "Image " + i + " - Decoded filename: " + bunch.getFileName() + " -> " + decodedFileName);
            } catch (Exception e) {
                Log.w("SelectionBunch", "Image " + i + " - Failed to decode filename: " + bunch.getFileName(), e);
            }

            // Remove any subdirectory prefixes like "chats/" from the filename
            String cleanFileName = decodedFileName;
            if (cleanFileName.contains("/")) {
                cleanFileName = cleanFileName.substring(cleanFileName.lastIndexOf("/") + 1);
                Log.d("SelectionBunch", "Image " + i + " - Removed subdirectory prefix: " + decodedFileName + " -> " + cleanFileName);
            }

            String localPath = customFolder.getAbsolutePath() + "/" + cleanFileName;

            Log.d("SelectionBunch", "Image " + i + " - Checking at path: " + localPath);
            boolean fileExists = doesFileExist(localPath);
            Log.d("SelectionBunch", "Image " + i + " - File exists: " + fileExists);

            if (!fileExists) {
                Log.d("SelectionBunch", "Image " + i + " - MISSING: " + localPath);
                missingImages.add(bunch);
            } else {
                Log.d("SelectionBunch", "Image " + i + " - EXISTS: " + localPath);
            }
        }

        Log.d("SelectionBunch", "Found " + missingImages.size() + " missing images out of " + selectionBunch.size() + " total");
        return missingImages;
    }

    private static void downloadSelectionBunchImage(selectionBunchModel bunch, DownloadCallback callback, Context context) {
        if (bunch == null || TextUtils.isEmpty(bunch.getImgUrl())) {
            callback.onError("Invalid image URL");
            return;
        }

        // --- Prepare filename ---
        String fileName = bunch.getFileName();
        if (TextUtils.isEmpty(fileName)) {
            fileName = extractFileNameFromFirebaseUrl(bunch.getImgUrl());
            if (TextUtils.isEmpty(fileName)) {
                fileName = "selectionBunch_" + System.currentTimeMillis() + ".jpg";
            }
        } else {
            try {
                fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
            } catch (Exception ignored) {
            }
            if (fileName.contains("/")) {
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            }
        }

        final String safeFileName = fileName; // ✅ make final for lambda
        final String mimeType = "image/jpeg";

        new Thread(() -> {
            java.net.HttpURLConnection connection = null;
            java.io.InputStream input = null;
            java.io.OutputStream publicOut = null;
            java.io.OutputStream privateOut = null;

            try {
                java.net.URL url = new java.net.URL(bunch.getImgUrl());
                connection = (java.net.HttpURLConnection) url.openConnection();
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode != java.net.HttpURLConnection.HTTP_OK) {
                    callback.onError("HTTP Error: " + responseCode);
                    return;
                }

                input = connection.getInputStream();
                long contentLength = connection.getContentLength();

                Uri imageUri = null;

                // ✅ PUBLIC GALLERY STORAGE (for Android 10+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, safeFileName);
                    values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                    values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Enclosure");

                    imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    if (imageUri != null) {
                        publicOut = context.getContentResolver().openOutputStream(imageUri);
                    }
                }

                // ✅ PRIVATE STORAGE (always create)
                File privateImagesDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                if (!privateImagesDir.exists()) privateImagesDir.mkdirs();
                File privateImageFile = new File(privateImagesDir, safeFileName);
                privateOut = new java.io.FileOutputStream(privateImageFile);

                // ✅ DOWNLOAD & SAVE
                byte[] buffer = new byte[4096];
                long totalBytesRead = 0;
                int bytesRead;

                while ((bytesRead = input.read(buffer)) != -1) {
                    totalBytesRead += bytesRead;
                    if (publicOut != null) publicOut.write(buffer, 0, bytesRead);
                    privateOut.write(buffer, 0, bytesRead);

                    // Update progress
                    if (contentLength > 0) {
                        int progress = (int) ((totalBytesRead * 100) / contentLength);
                        callback.onProgress(progress);
                    }
                }

                // Close streams
                if (publicOut != null) publicOut.close();
                privateOut.close();
                input.close();

                // Scan for gallery
                if (imageUri != null) {
                    MediaScannerConnection.scanFile(context, new String[]{imageUri.getPath()}, new String[]{mimeType}, null);
                }

                callback.onSuccess();
                Log.d("SelectionBunch", "Image downloaded successfully: " + safeFileName);

            } catch (Exception e) {
                Log.e("SelectionBunch", "Download failed", e);
                callback.onError(e.getMessage());
            } finally {
                try {
                    if (input != null) input.close();
                    if (publicOut != null) publicOut.close();
                    if (privateOut != null) privateOut.close();
                    if (connection != null) connection.disconnect();
                } catch (Exception e) {
                    Log.e("SelectionBunch", "Error closing streams", e);
                }
            }
        }).start();
    }

    private static boolean doesFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) return false;
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.length() > 0;
    }

    private static String extractFileNameFromFirebaseUrl(String url) {
        if (TextUtils.isEmpty(url)) return null;
        try {
            String path = new java.net.URL(url).getPath();
            if (path.contains("/")) {
                return path.substring(path.lastIndexOf("/") + 1);
            }
        } catch (Exception e) {
            Log.e("SelectionBunch", "Error extracting filename from URL", e);
        }
        return null;
    }

    private interface DownloadCallback {
        void onProgress(int progress);
        void onSuccess();
        void onError(String error);
    }

    public static void saveImageToGallery(File sourceFile, String fileName, Context context) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            if (bitmap == null) {
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
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
                    }
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
                context.sendBroadcast(mediaScanIntent);
            }
        } catch (IOException e) {
            Log.e("senderDownload", "Error saving image: " + e.getMessage());
        }
    }

    public static void saveVideoToGallery(File sourceFile, String fileName, Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
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
                    }
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
                context.sendBroadcast(mediaScanIntent);
            }
        } catch (IOException e) {
            Log.e("senderDownload", "Error saving video: " + e.getMessage());
        }
    }

    public static void saveDocumentToFolder(File sourceFile, String fileName, Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Enclosure");

                Uri documentUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
                if (documentUri != null) {
                    OutputStream outputStream = resolver.openOutputStream(documentUri);
                    if (outputStream != null) {
                        copyFile(new FileInputStream(sourceFile), outputStream);
                        outputStream.close();
                    }
                }
            } else {
                File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File enclosureDir = new File(documentsDir, "Enclosure");
                if (!enclosureDir.exists()) {
                    enclosureDir.mkdirs();
                }

                File destFile = new File(enclosureDir, fileName);
                copyFile(new FileInputStream(sourceFile), new FileOutputStream(destFile));
            }
        } catch (IOException e) {
            Log.e("senderDownload", "Error saving document: " + e.getMessage());
        }
    }

    private static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public static void saveFileToPublicDirectory(File sourceFile, String fileName, String extension, Context context) {
        if (sourceFile == null || !sourceFile.exists()) {
            return;
        }

        String format = extension != null ? extension.toLowerCase() : "";

        // Check file type and save accordingly
        if (isImageFormat(format)) {
            saveImageToGallery(sourceFile, fileName, context);
        } else if (isVideoFormat(format)) {
            saveVideoToGallery(sourceFile, fileName, context);
        } else {
            // Save other documents to Documents folder
            saveDocumentToFolder(sourceFile, fileName, context);
        }
    }

    private static boolean isImageFormat(String format) {
        return format.equals("jpg") || format.equals("jpeg") || format.equals("png") ||
                format.equals("webp") || format.equals("gif") || format.equals("tiff") ||
                format.equals("psd") || format.equals("heif") || format.equals("svg");
    }

    private static boolean isVideoFormat(String format) {
        return format.equals("mp4") || format.equals("mov") || format.equals("wmv") ||
                format.equals("flv") || format.equals("mkv") || format.equals("avi") ||
                format.equals("avchd") || format.equals("webm") || format.equals("hevc");
    }

    private static boolean isAudioFormat(String format) {
        return format.equals("flac") || format.equals("ape") || format.equals("wv") ||
                format.equals("tta") || format.equals("alac") || format.equals("m4a") ||
                format.equals("awb") || format.equals("wma") || format.equals("shn") ||
                format.equals("mp3") || format.equals("ogg") || format.equals("opus") ||
                format.equals("mp2") || format.equals("m4b") || format.equals("aac") ||
                format.equals("amr") || format.equals("atrac3") || format.equals("wavpack") ||
                format.equals("wav") || format.equals("aiff") || format.equals("au") ||
                format.equals("raw");
    }

    public static void saveDownloadedFileToPublicDirectory(messageModel model, Context context) {
        if (model == null) return;

        String fileName = model.getFileName();
        String extension = model.getExtension();

        if (fileName == null) return;

        // Determine the source file path based on file type
        File sourceFile = null;
        String format = extension != null ? extension.toLowerCase() : "";

        if (isImageFormat(format)) {
            File imagesDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
            sourceFile = new File(imagesDir, fileName);
        } else if (isVideoFormat(format)) {
            File videosDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
            sourceFile = new File(videosDir, fileName);
        } else if (isAudioFormat(format)) {
            File audiosDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
            sourceFile = new File(audiosDir, fileName);
        } else {
            // For documents
            File docsDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
            sourceFile = new File(docsDir, fileName);
        }

        if (sourceFile != null && sourceFile.exists()) {
            saveFileToPublicDirectory(sourceFile, fileName, extension, context);
        }
    }
}
