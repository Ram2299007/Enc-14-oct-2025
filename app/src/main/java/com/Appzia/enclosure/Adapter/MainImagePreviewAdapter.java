package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainImagePreviewAdapter extends RecyclerView.Adapter<MainImagePreviewAdapter.ViewHolder> {
    
    private Context context;
    private List<Uri> imageUris;
    
    public MainImagePreviewAdapter(Context context, List<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }
    
    // Dummy methods to maintain compatibility
    public void setOnCaptionEditListener(Object listener) {
        // No-op
    }
    
    public void updateCaption(int position, String caption) {
        // No-op
    }
    
    public List<String> getAllCaptions() {
        return new ArrayList<>();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.util.Log.d("MainImagePreview", "MainImagePreviewAdapter onCreateViewHolder called");
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_image_preview, parent, false);
        android.util.Log.d("MainImagePreview", "View inflated: " + (view != null ? "yes" : "no"));
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        
        android.util.Log.d("MainImagePreview", "MainImagePreviewAdapter onBindViewHolder - Position: " + position + ", URI: " + imageUri);
        android.util.Log.d("MainImagePreview", "ImageView found: " + (holder.imageView != null ? "yes" : "no"));
        
        // Set image view properties
        holder.imageView.setScaleType(android.widget.ImageView.ScaleType.CENTER_INSIDE);
        holder.imageView.setAdjustViewBounds(true);
        
        // Load image using Glide with match parent width
        Glide.with(context)
                .load(imageUri)
                .fitCenter()
                .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(com.bumptech.glide.load.engine.GlideException e, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                        android.util.Log.e("MainImagePreview", "Glide load failed for position " + position + ": " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        android.util.Log.d("MainImagePreview", "Glide load successful for position " + position);
                        return false;
                    }
                })
                .into(holder.imageView);
        
        android.util.Log.d("MainImagePreview", "Glide load completed for position: " + position);
        
        // Force layout update
        holder.imageView.invalidate();
        holder.imageView.requestLayout();
    }
    
    @Override
    public int getItemCount() {
        return imageUris.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mainImageView);
            android.util.Log.d("MainImagePreview", "MainImagePreviewAdapter ViewHolder created - ImageView found: " + (imageView != null ? "yes" : "no"));
        }
    }
}
