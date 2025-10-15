package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

// Assume R.drawable.inviteimg is a placeholder drawable, or you can draw a simple rectangle
// For this example, we'll draw a grey rectangle as a placeholder.

public class CustomPdfPreviewView extends View {

    private static final String TAG = "CustomPdfPreviewView";

    private Bitmap currentPdfBitmap;
    private String currentPdfUrl; // To track the URL associated with the current bitmap
    private LoadPdfPreviewTask currentTask;
    private Paint backgroundPaint;
    private Paint textPaint; // For "Loading..." text

    public CustomPdfPreviewView(Context context) {
        super(context);
        init();
    }

    public CustomPdfPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPdfPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize paint for drawing placeholder background
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY); // Light grey placeholder

        // Initialize paint for drawing "Loading..." text
        textPaint = new Paint();
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextSize(getResources().getDisplayMetrics().density * 16); // 16sp equivalent
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * Loads and displays a PDF preview.
     * This method should be called from the RecyclerView's onBindViewHolder.
     *
     * @param localPreviewImagePath The path where the generated preview image should be saved.
     * @param remotePdfUrl          The remote URL of the PDF.
     */
    public void loadPdfPreview(String localPreviewImagePath, String remotePdfUrl) {
        // If the URL is the same and we already have a bitmap, do nothing
        if (currentPdfUrl != null && currentPdfUrl.equals(remotePdfUrl) && currentPdfBitmap != null) {
            // Already loaded this PDF
            Log.d(TAG, "PDF already loaded for URL: " + remotePdfUrl);
            invalidate(); // Ensure it's drawn if visible
            return;
        }

        // Cancel any ongoing task for this view
        if (currentTask != null) {
            currentTask.cancel(true);
            Log.d(TAG, "Cancelling previous task for URL: " + currentTask.url);
        }

        // Clear previous bitmap and set placeholder state
        currentPdfBitmap = null;
        currentPdfUrl = remotePdfUrl; // Set the new URL
        invalidate(); // Redraw with placeholder

        File localPreviewFile = new File(localPreviewImagePath);

        if (localPreviewFile.exists()) {
            // If the local preview image file exists, load it directly
            Log.d(TAG, "Loading PDF preview from local cache: " + localPreviewImagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(localPreviewImagePath);
            if (bitmap != null) {
                currentPdfBitmap = bitmap;
                invalidate(); // Redraw with the loaded bitmap
                Log.d(TAG, "Loaded PDF preview from local cache: " + localPreviewImagePath);
            } else {
                // If decoding the local bitmap fails (e.g., corrupted file), try downloading
                Log.e(TAG, "Failed to decode local bitmap: " + localPreviewImagePath + ". Attempting to re-download.");
                currentTask = new LoadPdfPreviewTask(getContext(), this, localPreviewImagePath);
                currentTask.execute(remotePdfUrl);
            }
        } else {
            // Local preview does not exist, initiate download, generate preview, save, then display
            Log.d(TAG, "Local PDF preview not found. Downloading PDF from: " + remotePdfUrl);
            currentTask = new LoadPdfPreviewTask(getContext(), this, localPreviewImagePath);
            currentTask.execute(remotePdfUrl);
        }
    }

    // Called when the view is detached, important for RecyclerView to clean up
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
        if (currentPdfBitmap != null) {
            currentPdfBitmap.recycle(); // Free up memory
            currentPdfBitmap = null;
        }
        currentPdfUrl = null;
        Log.d(TAG, "View detached from window. Task cancelled, bitmap recycled.");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Draw a placeholder background
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        if (currentPdfBitmap != null) {
            // Calculate destination rectangle to fit the bitmap within the view's bounds
            Rect srcRect = new Rect(0, 0, currentPdfBitmap.getWidth(), currentPdfBitmap.getHeight());
            RectF destRect = new RectF(0, 0, width, height); // Draw to fill the view

            // You might want to adjust how the bitmap is scaled/fitted
            // For example, to maintain aspect ratio and center it:
            float scaleX = (float) width / currentPdfBitmap.getWidth();
            float scaleY = (float) height / currentPdfBitmap.getHeight();
            float scale = Math.min(scaleX, scaleY); // Fit inside
            // float scale = Math.max(scaleX, scaleY); // Fill and crop

            float scaledWidth = currentPdfBitmap.getWidth() * scale;
            float scaledHeight = currentPdfBitmap.getHeight() * scale;

            float posX = (width - scaledWidth) / 2;
            float posY = (height - scaledHeight) / 2;

            destRect.set(posX, posY, posX + scaledWidth, posY + scaledHeight);

            canvas.drawBitmap(currentPdfBitmap, srcRect, destRect, null);
        } else {
            // Draw "Loading..." text when no bitmap is present
            String text = "Loading...";
            float x = width / 2f;
            float y = (height / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f);
            canvas.drawText(text, x, y, textPaint);
        }
    }

    // --- AsyncTask for background loading ---
    private static class LoadPdfPreviewTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<Context> contextWeakReference;
        private final WeakReference<CustomPdfPreviewView> viewWeakReference;
        private final String localPreviewImagePath;
        private String url; // Store the URL to validate in onPostExecute
        private boolean downloadSuccess = false;

        public LoadPdfPreviewTask(Context context, CustomPdfPreviewView view, String localPreviewImagePath) {
            this.contextWeakReference = new WeakReference<>(context);
            this.viewWeakReference = new WeakReference<>(view);
            this.localPreviewImagePath = localPreviewImagePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomPdfPreviewView view = viewWeakReference.get();
            if (view != null) {
                view.currentPdfBitmap = null; // Clear previous bitmap
                view.invalidate(); // Show placeholder
            }
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            url = urls[0];
            if (isCancelled()) {
                return null;
            }

            Context context = contextWeakReference.get();
            CustomPdfPreviewView view = viewWeakReference.get();
            if (context == null || view == null || url == null) {
                // Context or view no longer exists, or URL is null
                Log.w(TAG, "Context, View, or URL is null during doInBackground. Cancelling.");
                return null;
            }

            File tempPdfFile = null;
            Bitmap previewBitmap = null;

            try {
                // --- Step 1: Download the PDF file to a temporary location ---
                URL downloadUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.connect();

                if (isCancelled()) {
                    return null;
                }

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    tempPdfFile = File.createTempFile("temp_pdf", ".pdf", context.getCacheDir());
                    FileOutputStream output = new FileOutputStream(tempPdfFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        if (isCancelled()) {
                            output.close();
                            input.close();
                            return null;
                        }
                        output.write(buffer, 0, bytesRead);
                    }
                    output.flush();
                    output.close();
                    input.close();
                    downloadSuccess = true;
                    Log.d(TAG, "PDF downloaded to temporary file: " + tempPdfFile.getAbsolutePath());
                } else {
                    Log.e(TAG, "Failed to download PDF. HTTP error code: " + connection.getResponseCode() + " for URL: " + url);
                    return null;
                }

                // --- Step 2: Generate PDF preview from the downloaded temporary file ---
                if (downloadSuccess && tempPdfFile != null) {
                    if (isCancelled()) {
                        return null;
                    }

                    ParcelFileDescriptor fileDescriptor = null;
                    PdfRenderer renderer = null;
                    try {
                        fileDescriptor = ParcelFileDescriptor.open(tempPdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
                        renderer = new PdfRenderer(fileDescriptor);

                        if (renderer.getPageCount() == 0) {
                            Log.e(TAG, "PDF has no pages: " + tempPdfFile.getAbsolutePath());
                            return null;
                        }

                        PdfRenderer.Page page = renderer.openPage(0);

                        // Get the actual width and height of the view to generate a bitmap of appropriate size
                        // This might return 0 if the layout pass hasn't completed.
                        // For a robust solution, you might need to observe layout changes or
                        // generate a fixed size/max size bitmap.
                        // Here, we'll try to get the current dimensions, falling back to PDF's native size.
                        int desiredWidth = view.getWidth();
                        int desiredHeight = view.getHeight();

                        if (desiredWidth <= 0 || desiredHeight <= 0) {
                            // Fallback if view dimensions are not yet available
                            desiredWidth = page.getWidth();
                            desiredHeight = page.getHeight();
                        } else {
                            // Scale down if PDF is much larger than view, maintain aspect ratio
                            float scaleFactor = Math.min((float) desiredWidth / page.getWidth(), (float) desiredHeight / page.getHeight());
                            desiredWidth = (int) (page.getWidth() * scaleFactor);
                            desiredHeight = (int) (page.getHeight() * scaleFactor);
                        }
                        
                        // Ensure minimum size for visibility if desiredWidth/Height became too small
                        if (desiredWidth <= 0) desiredWidth = 100; // Example fallback
                        if (desiredHeight <= 0) desiredHeight = 150; // Example fallback


                        previewBitmap = Bitmap.createBitmap(desiredWidth, desiredHeight, Bitmap.Config.ARGB_8888);
                        page.render(previewBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                        page.close();
                    } finally {
                        if (renderer != null) renderer.close();
                        if (fileDescriptor != null) fileDescriptor.close();
                    }

                    // --- Step 3: Save the generated bitmap preview to the designated local path ---
                    if (previewBitmap != null) {
                        if (isCancelled()) {
                            return null;
                        }
                        File previewFile = new File(localPreviewImagePath);
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(previewFile);
                            previewBitmap.compress(Bitmap.CompressFormat.PNG, 80, out); // 80% quality for PNG (ignored)
                            Log.d(TAG, "PDF preview saved to: " + localPreviewImagePath);
                        } catch (Exception e) {
                            Log.e(TAG, "Error saving PDF preview bitmap to " + localPreviewImagePath + ": " + e.getMessage());
                        } finally {
                            if (out != null) {
                                out.close();
                            }
                        }
                    }
                }

            } catch (IOException e) {
                Log.e(TAG, "Network or file I/O error during PDF download/preview generation: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "General error during PDF preview generation: " + e.getMessage());
            } finally {
                if (tempPdfFile != null && tempPdfFile.exists()) {
                    if (tempPdfFile.delete()) {
                        Log.d(TAG, "Temporary PDF file deleted: " + tempPdfFile.getAbsolutePath());
                    } else {
                        Log.w(TAG, "Failed to delete temporary PDF file: " + tempPdfFile.getAbsolutePath());
                    }
                }
            }
            return previewBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            CustomPdfPreviewView view = viewWeakReference.get();
            if (view == null || isCancelled()) {
                if (bitmap != null) {
                    bitmap.recycle(); // Recycle bitmap if view is gone or task cancelled
                }
                Log.d(TAG, "Task cancelled or view gone for URL: " + url);
                return;
            }

            // Crucial check: Is this task still relevant for the current view's URL?
            // This handles RecyclerView recycling
            if (view.currentPdfUrl != null && view.currentPdfUrl.equals(url)) {
                if (view.currentPdfBitmap != null) {
                    view.currentPdfBitmap.recycle(); // Recycle old bitmap if a new one is arriving
                }
                view.currentPdfBitmap = bitmap; // Set the new bitmap
                view.invalidate(); // Trigger redraw
                Log.d(TAG, "PDF preview loaded successfully for URL: " + url);
            } else {
                if (bitmap != null) {
                    bitmap.recycle(); // Recycle bitmap if it's for an outdated URL
                }
                Log.d(TAG, "Ignoring result for URL " + url + " as view is now expecting " + view.currentPdfUrl);
            }
        }
    }
}