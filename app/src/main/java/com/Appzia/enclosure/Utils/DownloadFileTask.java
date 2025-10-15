package com.Appzia.enclosure.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileTask {

    private ProgressDialog progressDialog;
    private String downloadUrl;

    public DownloadFileTask(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Downloading file...");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
    }

    public void downloadFile(String fileUrl) {
        downloadUrl = fileUrl;

        new Thread(() -> {
            try {
                // Create a URL object.
                URL url = new URL(downloadUrl);

                // Open a connection to the URL.
                URLConnection connection = url.openConnection();

                // Get the content length of the file.
                int contentLength = connection.getContentLength();

                // Create a byte array to store the downloaded data.
                byte[] data = new byte[contentLength];

                // Open an input stream to read the data from the URL.
                InputStream inputStream = connection.getInputStream();

                // Read the data from the input stream into the byte array.
                int bytesRead = 0;
                while (bytesRead < contentLength) {
                    int bytesReadThisTime = inputStream.read(data, bytesRead, contentLength - bytesRead);
                    if (bytesReadThisTime == -1) {
                        break;
                    }
                    bytesRead += bytesReadThisTime;

                    // Update the progress dialog with the current download progress.
                    progressDialog.setProgress((int) ((bytesRead * 100) / contentLength));
                }

                // Close the input stream.
                inputStream.close();

                // Create an output stream to write the downloaded data to a file.
                FileOutputStream outputStream = new FileOutputStream("/sdcard/Download/" + downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1));

                // Write the downloaded data to the file.
                outputStream.write(data);

                // Close the output stream.
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }).start();
    }
}

