package com.Appzia.enclosure.Adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoPreviewAdapter extends RecyclerView.Adapter<VideoPreviewAdapter.VideoPreviewViewHolder> {
    
    private Context context;
    private List<Uri> videoUris;
    private List<File> videoFiles;
    private OnVideoClickListener onVideoClickListener;
    
    public interface OnVideoClickListener {
        void onVideoClick(int position);
    }
    
    public VideoPreviewAdapter(Context context, List<Uri> videoUris, List<File> videoFiles) {
        this.context = context;
        this.videoUris = videoUris;
        this.videoFiles = videoFiles;
    }
    
    public void setOnVideoClickListener(OnVideoClickListener listener) {
        this.onVideoClickListener = listener;
    }
    
    @NonNull
    @Override
    public VideoPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_video, parent, false);
        return new VideoPreviewViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull VideoPreviewViewHolder holder, int position) {
        Uri videoUri = videoUris.get(position);
        File videoFile = videoFiles.get(position);
        
        // Load video thumbnail
        loadVideoThumbnail(holder.videoThumbnail, videoUri);
        
        // Set video duration
        String duration = getVideoDuration(videoFile);
        holder.videoDuration.setText(duration);
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (onVideoClickListener != null) {
                onVideoClickListener.onVideoClick(position);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return videoUris.size();
    }
    
    private void loadVideoThumbnail(ImageView imageView, Uri videoUri) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, videoUri);
            
            // Get thumbnail at 1 second
            android.graphics.Bitmap thumbnail = retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            
            if (thumbnail != null) {
                Glide.with(context)
                    .load(thumbnail)
                    .into(imageView);
            } else {
                // Fallback to video URI
                Glide.with(context)
                    .load(videoUri)
                    .into(imageView);
            }
            
            retriever.release();
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to video URI
            Glide.with(context)
                .load(videoUri)
                .into(imageView);
        }
    }
    
    private String getVideoDuration(File videoFile) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoFile.getAbsolutePath());
            
            String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            retriever.release();
            
            if (durationStr != null) {
                long duration = Long.parseLong(durationStr);
                return formatDuration(duration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "0:00";
    }
    
    private String formatDuration(long durationMs) {
        long seconds = durationMs / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        return String.format("%d:%02d", minutes, seconds);
    }
    
    public static class VideoPreviewViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;
        TextView videoDuration;
        ImageView playIcon;
        View selectionIndicator;
        ImageView selectedCheck;
        
        public VideoPreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            videoDuration = itemView.findViewById(R.id.videoDuration);
            playIcon = itemView.findViewById(R.id.playIcon);
            selectionIndicator = itemView.findViewById(R.id.selectionIndicator);
            selectedCheck = itemView.findViewById(R.id.selectedCheck);
        }
    }
}
