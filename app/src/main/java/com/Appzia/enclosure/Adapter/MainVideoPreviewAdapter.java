package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainVideoPreviewAdapter extends RecyclerView.Adapter<MainVideoPreviewAdapter.ViewHolder> {
    
    private Context context;
    private List<Uri> videoUris;
    private OnVideoClickListener onVideoClickListener;
    
    public interface OnVideoClickListener {
        void onVideoClick(Uri videoUri);
    }
    
    public MainVideoPreviewAdapter(Context context, List<Uri> videoUris) {
        this.context = context;
        this.videoUris = videoUris;
    }
    
    public void setOnVideoClickListener(OnVideoClickListener listener) {
        this.onVideoClickListener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.util.Log.d("MainVideoPreview", "MainVideoPreviewAdapter onCreateViewHolder called");
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_video_preview, parent, false);
        android.util.Log.d("MainVideoPreview", "View inflated: " + (view != null ? "yes" : "no"));
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri videoUri = videoUris.get(position);
        android.util.Log.d("MainVideoPreview", "MainVideoPreviewAdapter onBindViewHolder - Position: " + position + ", URI: " + videoUri);
        android.util.Log.d("MainVideoPreview", "VideoThumbnail found: " + (holder.videoThumbnail != null ? "yes" : "no"));
        android.util.Log.d("MainVideoPreview", "PlayPauseButton found: " + (holder.playPauseButton != null ? "yes" : "no"));
        
        // Generate thumbnail for the video
        Glide.with(context)
                .load(videoUri)
                .into(holder.videoThumbnail);
        
        // Set click listener for video playback
        holder.itemView.setOnClickListener(v -> {
            if (onVideoClickListener != null) {
                android.util.Log.d("MainVideoPreview", "Video clicked at position: " + position);
                onVideoClickListener.onVideoClick(videoUri);
            }
        });
        
        android.util.Log.d("MainVideoPreview", "Video thumbnail setup completed for position: " + position);
    }
    
    @Override
    public int getItemCount() {
        return videoUris.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;
        ImageView playPauseButton;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            playPauseButton = itemView.findViewById(R.id.playPauseButton);
            
            android.util.Log.d("MainVideoPreview", "ViewHolder created - VideoThumbnail: " + (videoThumbnail != null ? "yes" : "no") + 
                    ", PlayPauseButton: " + (playPauseButton != null ? "yes" : "no"));
        }
    }
}
