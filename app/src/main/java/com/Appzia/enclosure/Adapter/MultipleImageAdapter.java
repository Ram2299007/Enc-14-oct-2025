package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class MultipleImageAdapter extends RecyclerView.Adapter<MultipleImageAdapter.ImageViewHolder> {

    private Context context;
    private List<String> imageList;
    private String viewHolderTypeKey;

    public MultipleImageAdapter(Context context, List<String> imageList, String viewHolderTypeKey) {
        this.context = context;
        this.imageList = imageList;
        this.viewHolderTypeKey = viewHolderTypeKey;

        // Log received data in constructor
        android.util.Log.d("MultipleImageAdapter", "=== CONSTRUCTOR CALLED ===");
        android.util.Log.d("MultipleImageAdapter", "Image list size: " + (imageList != null ? imageList.size() : 0));
        android.util.Log.d("MultipleImageAdapter", "View holder type key: " + viewHolderTypeKey);

        if (imageList != null) {
            for (int i = 0; i < imageList.size(); i++) {
                String imageData = imageList.get(i);
                android.util.Log.d("MultipleImageAdapter", "Image " + i + ": " + imageData);

                // Parse bundle data if it contains fileName|URL format
                if (imageData.contains("|")) {
                    String[] parts = imageData.split("\\|");
                    if (parts.length >= 2) {
                        String fileName = parts[0];
                        String networkUrl = parts[1];
                        android.util.Log.d("MultipleImageAdapter", "  → FileName: " + fileName);
                        android.util.Log.d("MultipleImageAdapter", "  → NetworkURL: " + networkUrl);
                    }
                } else {
                    android.util.Log.d("MultipleImageAdapter", "  → Single data format");
                }
            }
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_multiple_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imagePath = imageList.get(position);

        // Log received data for this position
        android.util.Log.d("MultipleImageAdapter", "=== BINDING VIEW HOLDER " + position + " ===");
        android.util.Log.d("MultipleImageAdapter", "Raw image data: " + imagePath);

        // Parse bundle data if it contains fileName|URL format
        String fileName = null;
        String networkUrl = null;
        if (imagePath.contains("|")) {
            String[] parts = imagePath.split("\\|");
            if (parts.length >= 2) {
                fileName = parts[0];
                networkUrl = parts[1];
                android.util.Log.d("MultipleImageAdapter", "  → Parsed FileName: " + fileName);
                android.util.Log.d("MultipleImageAdapter", "  → Parsed NetworkURL: " + networkUrl);
            }
        } else {
            android.util.Log.d("MultipleImageAdapter", "  → Single data format (no bundle)");
        }

        // Set image number
        holder.imageNumberText.setText(String.valueOf(position + 1));

        // Load image with performance optimizations
        loadImageOptimized(holder.imageView, imagePath, fileName, networkUrl);

        // Set click listener to open individual image
        holder.itemView.setOnClickListener(v -> {
            openIndividualImage(imagePath, position);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }



    private void loadImageOptimized(ImageView imageView, String imagePath, String parsedFileName, String parsedNetworkUrl) {
        // Use parsed data if available, otherwise extract from imagePath
        String fileName = parsedFileName != null ? parsedFileName : extractFileNameFromUrl(imagePath);
        String networkUrl = parsedNetworkUrl != null ? parsedNetworkUrl : imagePath;

        android.util.Log.d("MultipleImageAdapter", "Loading optimized - original: " + imagePath);
        android.util.Log.d("MultipleImageAdapter", "  → Using fileName: " + fileName);
        android.util.Log.d("MultipleImageAdapter", "  → Using networkUrl: " + networkUrl);

        // Check if file exists in local storage
        String localPath = getLocalImagePath(fileName);
        android.util.Log.d("MultipleImageAdapter", "Local path: " + localPath);
        android.util.Log.d("MultipleImageAdapter", "Checking file: " + localPath + " - exists: " + doesFileExist(localPath));

        if (localPath != null && doesFileExist(localPath)) {
            // Load from local file with optimizations
            android.util.Log.d("MultipleImageAdapter", "✅ Loading from local storage (optimized): " + localPath);
            imageView.setImageURI(Uri.parse(localPath));
        } else {
            // Load from URL with performance optimizations
            android.util.Log.d("MultipleImageAdapter", "❌ File not found locally, loading from URL (optimized): " + networkUrl);
            Picasso.get()
                    .load(networkUrl)
                    .into(imageView);
        }
    }

    private String extractFileNameFromUrl(String url) {
        try {
            // If it's already a local file path, return as is
            if (url.startsWith("/") || url.startsWith("file://")) {
                return new File(url).getName();
            }

            // Extract filename from URL
            Uri uri = Uri.parse(url);
            String path = uri.getPath();
            if (path != null) {
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                // Remove the "1_" prefix if it exists (Firebase adds this prefix)
                if (fileName.startsWith("1_")) {
                    fileName = fileName.substring(2); // Remove "1_" prefix
                }
                return fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url; // Fallback to original URL
    }

    private String getLocalImagePath(String fileName) {
        try {
            File customFolder;
            String exactPath = null;

            // Android 10+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                customFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                exactPath = customFolder.getAbsolutePath();
            } else {
                customFolder = new File(context.getExternalFilesDir(null), "Enclosure/Media/Images");
                exactPath = customFolder.getAbsolutePath();
            }

            return exactPath + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void openIndividualImage(String imagePath, int position) {
        // Parse bundle data to get fileName and networkUrl
        String fileName = null;
        String networkUrl = null;

        if (imagePath.contains("|")) {
            String[] parts = imagePath.split("\\|");
            if (parts.length >= 2) {
                fileName = parts[0];
                networkUrl = parts[1];
                android.util.Log.d("MultipleImageAdapter", "Opening individual image - Parsed fileName: " + fileName);
                android.util.Log.d("MultipleImageAdapter", "Opening individual image - Parsed networkUrl: " + networkUrl);
            }
        } else {
            // Fallback to extraction if no bundle format
            fileName = extractFileNameFromUrl(imagePath);
            networkUrl = imagePath;
            android.util.Log.d("MultipleImageAdapter", "Opening individual image - Extracted fileName: " + fileName);
            android.util.Log.d("MultipleImageAdapter", "Opening individual image - Using imagePath: " + networkUrl);
        }

        Intent intent = new Intent(context, show_image_Screen.class);
        intent.putExtra("imageKey", fileName); // Pass filename for local storage lookup
        intent.putExtra("viewHolderTypeKey", viewHolderTypeKey);
        intent.putExtra("imagePath", networkUrl); // Pass network URL for fallback
        intent.putExtra("originalImagePath", imagePath); // Pass original bundle data
        android.util.Log.d("MultipleImageAdapter", "Intent data - imageKey: " + fileName + ", imagePath: " + networkUrl);
        context.startActivity(intent);
    }

    public boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView imageNumberText;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageNumberText = itemView.findViewById(R.id.imageNumberText);

            // Performance optimizations
            imageView.setDrawingCacheEnabled(true);
            imageView.setLayerType(ImageView.LAYER_TYPE_HARDWARE, null);
        }
    }
}