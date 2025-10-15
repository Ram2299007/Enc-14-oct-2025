package com.Appzia.enclosure.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.HashMap;
import java.util.Map;

public class DownloadProgressManager {
    private static DownloadProgressManager instance;
    private Handler downloadHandler;
    private Map<Long, Runnable> downloadRunnables;
    private Map<Long, Boolean> pausedDownloads;
    private Context context;

    private DownloadProgressManager(Context context) {
        this.context = context;
        this.downloadHandler = new Handler();
        this.downloadRunnables = new HashMap<>();
        this.pausedDownloads = new HashMap<>();
    }

    public static DownloadProgressManager getInstance(Context context) {
        if (instance == null) {
            instance = new DownloadProgressManager(context);
        }
        return instance;
    }

    public void startDownloadProgressTracking(long downloadId, TextView percentageView, 
                                            LinearLayout progressContainer, 
                                            FloatingActionButton downloadButton,
                                            ImageButton pauseButton) {
        progressContainer.setVisibility(View.VISIBLE);
        downloadButton.setVisibility(View.GONE);
        
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (pausedDownloads.get(downloadId) != null && pausedDownloads.get(downloadId)) {
                    return; // Skip if paused
                }
                
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
                                progressContainer.setVisibility(View.GONE);
                                downloadRunnables.remove(downloadId);
                                pausedDownloads.remove(downloadId);
                                cursor.close();
                                return;
                            }
                        }
                        
                        int status = cursor.getInt(statusIndex);
                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            progressContainer.setVisibility(View.GONE);
                            downloadRunnables.remove(downloadId);
                            pausedDownloads.remove(downloadId);
                            cursor.close();
                            return;
                        }
                    }
                }
                cursor.close();
                
                // Continue tracking
                downloadHandler.postDelayed(this, 1000); // Update every second
            }
        };
        
        downloadRunnables.put(downloadId, progressRunnable);
        downloadHandler.post(progressRunnable);
        
        // Set up pause button click listener
        pauseButton.setOnClickListener(v -> {
            pauseDownload(downloadId, progressContainer, downloadButton, pauseButton);
        });
    }

    public void pauseDownload(long downloadId, LinearLayout progressContainer, 
                             FloatingActionButton downloadButton, ImageButton pauseButton) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        
        // Remove the download
        downloadManager.remove(downloadId);
        
        // Stop progress tracking
        Runnable runnable = downloadRunnables.get(downloadId);
        if (runnable != null) {
            downloadHandler.removeCallbacks(runnable);
            downloadRunnables.remove(downloadId);
        }
        
        // Hide progress and show download button again
        progressContainer.setVisibility(View.GONE);
        downloadButton.setVisibility(View.VISIBLE);
        
        // Remove from paused downloads
        pausedDownloads.remove(downloadId);
    }

    public void cleanup() {
        if (downloadHandler != null) {
            for (Runnable runnable : downloadRunnables.values()) {
                downloadHandler.removeCallbacks(runnable);
            }
            downloadRunnables.clear();
            pausedDownloads.clear();
        }
    }
}
