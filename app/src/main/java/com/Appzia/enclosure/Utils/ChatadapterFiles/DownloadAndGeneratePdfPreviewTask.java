package com.Appzia.enclosure.Utils.ChatadapterFiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadAndGeneratePdfPreviewTask extends AsyncTask<String, Void, Bitmap> {
    private final Context context;
    private final String localPreviewImagePath;
    private final ImageView imageView;
    private boolean downloadSuccess = false; // Flag to track if PDF download was successful


    public DownloadAndGeneratePdfPreviewTask(Context mContext, String localPreviewImagePath, ImageView imageView, ViewGroup parentLayout, int position, boolean loadHighQuality) {
        this.context = mContext;
        this.localPreviewImagePath = localPreviewImagePath;
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Show a temporary placeholder image while the PDF is being downloaded and processed.
        // Replace R.drawable.inviteimg with your actual placeholder drawable.
        imageView.setImageResource(R.drawable.invite_dark);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String pdfUrl = urls[0]; // The remote URL of the PDF
        File tempPdfFile = null; // Temporary file to store the downloaded PDF
        Bitmap previewBitmap = null; // The bitmap generated from the PDF

        try {
            // --- Step 1: Download the PDF file to a temporary location ---
            URL url = new URL(pdfUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000); // 10 seconds timeout for connection
            connection.setReadTimeout(15000);    // 15 seconds timeout for reading
            connection.connect();

            // Check if the HTTP connection was successful (HTTP 200 OK)
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream input = connection.getInputStream();
                // Create a temporary file in the app's cache directory to store the downloaded PDF
                tempPdfFile = File.createTempFile("temp_pdf", ".pdf", context.getCacheDir());
                FileOutputStream output = new FileOutputStream(tempPdfFile);

                byte[] buffer = new byte[4096]; // Buffer for reading data
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead); // Write data to the temporary file
                }
                output.flush(); // Ensure all buffered data is written
                output.close();
                input.close();
                downloadSuccess = true; // Mark download as successful
                Log.d("TAG", "PDF downloaded to temporary file: " + tempPdfFile.getAbsolutePath());
            } else {
                Log.e("TAG", "Failed to download PDF. HTTP error code: " + connection.getResponseCode() + " for URL: " + pdfUrl);
                return null; // Return null if download fails
            }

            // --- Step 2: Generate PDF preview from the downloaded temporary file ---
            if (downloadSuccess && tempPdfFile != null) {
                ParcelFileDescriptor fileDescriptor = null;
                PdfRenderer renderer = null;
                try {
                    // Open the temporary PDF file for reading
                    fileDescriptor = ParcelFileDescriptor.open(tempPdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
                    renderer = new PdfRenderer(fileDescriptor);
                    PdfRenderer.Page page = renderer.openPage(0); // Open the first page of the PDF

                    // Create a mutable bitmap with the same dimensions as the PDF page
                    previewBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    // Render the PDF page onto the bitmap
                    page.render(previewBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                    page.close(); // Close the PDF page
                } finally {
                    // Ensure PdfRenderer and ParcelFileDescriptor are closed to prevent resource leaks
                    if (renderer != null) renderer.close();
                    if (fileDescriptor != null) fileDescriptor.close();
                }

                // --- Step 3: Save the generated bitmap preview to the designated local path ---
                if (previewBitmap != null) {
                    File previewFile = new File(localPreviewImagePath);
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(previewFile);
                        // Compress the bitmap to PNG format (100% quality)
                        previewBitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
                        Log.d("TAG", "PDF preview saved to: " + localPreviewImagePath);
                    } catch (Exception e) {
                        Log.e("TAG", "Error saving PDF preview bitmap to " + localPreviewImagePath + ": " + e.getMessage());
                    } finally {
                        if (out != null) {
                            out.close(); // Close the FileOutputStream
                        }
                    }
                }
            }

        } catch (IOException e) {
            Log.e("TAG", "Network or file I/O error during PDF download/preview generation: " + e.getMessage());
        } catch (Exception e) {
            Log.e("TAG", "General error during PDF preview generation: " + e.getMessage());
        } finally {
            // --- Cleanup: Ensure the temporary PDF file is deleted ---
            if (tempPdfFile != null && tempPdfFile.exists()) {
                if (tempPdfFile.delete()) {
                    Log.d("TAG", "Temporary PDF file deleted: " + tempPdfFile.getAbsolutePath());
                } else {
                    Log.w("TAG", "Failed to delete temporary PDF file: " + tempPdfFile.getAbsolutePath());
                }
            }
        }
        return previewBitmap; // Return the generated bitmap (or null if an error occurred)
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        // This method runs on the UI thread after doInBackground completes.
        if (bitmap != null) {
            // If a bitmap was successfully generated, display it
            if (bitmap != null && localPreviewImagePath != null) {
                messageModel model = new messageModel();
                model.setImageWidth(String.valueOf(bitmap.getWidth()));
                model.setImageHeight(String.valueOf(bitmap.getHeight()));
                model.setAspectRatio(String.valueOf((float) bitmap.getWidth() / bitmap.getHeight()));
                model.setFileName(new File(localPreviewImagePath).getName());

                String imageSource = localPreviewImagePath.startsWith("/") ? "file://" + localPreviewImagePath : localPreviewImagePath;

                ViewGroup parentLayout = (ViewGroup) imageView.getParent();

                RequestOptions requestOptions = new RequestOptions();


                try {
                    Constant.loadImageIntoViewPdf(context, imageSource, requestOptions, imageView, parentLayout, 0, // Use real adapter position if available
                            true, // true
                            model);
                } catch (Exception e) {

                }
            } else {
                imageView.setImageResource(R.drawable.invite_dark);
                Log.e("TAG", "Failed to load PDF preview. Displaying placeholder.");
            }

        } else {
            // If bitmap is null, it means there was an error downloading or generating the preview.
            // Display the error placeholder image.
            imageView.setImageResource(R.drawable.invite_dark);
            Log.e("TAG", "Failed to load PDF preview. Displaying placeholder.");
        }
    }
}
