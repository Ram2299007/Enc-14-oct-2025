package com.Appzia.enclosure.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class FileDownloader {

    public static void downloadFile(Context context, String fileUrl, String filename) {
        // Get a reference to the DownloadManager
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        // Create a DownloadManager.Request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));

        // Set the destination folder and file name

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

        // Set other properties if needed
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Enqueue the download and get the download ID
        long downloadId = downloadManager.enqueue(request);




    }
}
