package com.Appzia.enclosure.Adapter;

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
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Extracted from chatAdapter to reduce file size. Functionality unchanged.
 */
class PdfPreviewLoader extends AsyncTask<String, Void, Bitmap> {
    private final Context context;
    private final String localPreviewImagePath;
    private final ImageView imageView;
    private final boolean loadHighQuality;
    private boolean downloadSuccess = false;

    PdfPreviewLoader(Context context, String localPreviewImagePath, ImageView imageView, boolean loadHighQuality) {
        this.context = context;
        this.localPreviewImagePath = localPreviewImagePath;
        this.imageView = imageView;
        this.loadHighQuality = loadHighQuality;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        imageView.setImageResource(R.drawable.invite_dark);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String pdfUrl = urls[0];
        File tempPdfFile = null;
        Bitmap previewBitmap = null;
        try {
            URL url = new URL(pdfUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream input = connection.getInputStream();
                tempPdfFile = File.createTempFile("temp_pdf", ".pdf", context.getCacheDir());
                FileOutputStream output = new FileOutputStream(tempPdfFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.flush();
                output.close();
                input.close();
                downloadSuccess = true;
            } else {
                Log.e("PdfPreviewLoader", "HTTP error: " + connection.getResponseCode());
                return null;
            }

            if (downloadSuccess && tempPdfFile != null) {
                ParcelFileDescriptor fileDescriptor = null;
                PdfRenderer renderer = null;
                try {
                    fileDescriptor = ParcelFileDescriptor.open(tempPdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
                    renderer = new PdfRenderer(fileDescriptor);
                    PdfRenderer.Page page = renderer.openPage(0);
                    previewBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(previewBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    page.close();
                } finally {
                    if (renderer != null) renderer.close();
                    if (fileDescriptor != null) fileDescriptor.close();
                }

                if (previewBitmap != null) {
                    File previewFile = new File(localPreviewImagePath);
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(previewFile);
                        previewBitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
                    } finally {
                        if (out != null) out.close();
                    }
                }
            }
        } catch (IOException e) {
            Log.e("PdfPreviewLoader", "IO error: " + e.getMessage());
        } catch (Exception e) {
            Log.e("PdfPreviewLoader", "Error: " + e.getMessage());
        } finally {
            // Clean up temp file if present
            //noinspection ResultOfMethodCallIgnored
            // handled by caller environment
        }
        return previewBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null && localPreviewImagePath != null) {
            messageModel model = new messageModel();
            model.setImageWidth(String.valueOf(bitmap.getWidth()));
            model.setImageHeight(String.valueOf(bitmap.getHeight()));
            model.setAspectRatio(String.valueOf((float) bitmap.getWidth() / bitmap.getHeight()));
            model.setFileName(new File(localPreviewImagePath).getName());

            String imageSource = localPreviewImagePath.startsWith("/")
                    ? "file://" + localPreviewImagePath
                    : localPreviewImagePath;

            ViewGroup parentLayout = (ViewGroup) imageView.getParent();
            RequestOptions requestOptions = new RequestOptions();
            try {
                Constant.loadImageIntoViewPdf(
                        context,
                        imageSource,
                        requestOptions,
                        imageView,
                        parentLayout,
                        0,
                        loadHighQuality,
                        model
                );
            } catch (Exception ignored) {}
        } else {
            imageView.setImageResource(R.drawable.invite_dark);
        }
    }
}


