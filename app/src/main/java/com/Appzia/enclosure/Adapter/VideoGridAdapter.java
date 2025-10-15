package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.Appzia.enclosure.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class VideoGridAdapter extends BaseAdapter {
    
    private Context context;
    private List<Uri> videoUris;
    private List<Uri> selectedVideos;
    private int maxSelection;
    private OnVideoSelectionListener listener;
    private int themeColor = Color.parseColor("#00A3E9");
    ColorStateList tintList;
    
    public interface OnVideoSelectionListener {
        void onVideoSelected(Uri videoUri);
    }
    
    public VideoGridAdapter(Context context, List<Uri> videoUris, List<Uri> selectedVideos, int maxSelection) {
        this.context = context;
        this.videoUris = videoUris;
        this.selectedVideos = selectedVideos;
        this.maxSelection = maxSelection;
    }
    
    public void setOnVideoSelectionListener(OnVideoSelectionListener listener) {
        this.listener = listener;
    }
    
    public void setThemeColor(int color, ColorStateList tintList) {
        this.themeColor = color;
        this.tintList = tintList;
        notifyDataSetChanged();
    }
    
    public void updateVideos(List<Uri> newVideoUris) {
        this.videoUris = newVideoUris;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return videoUris.size();
    }
    
    @Override
    public Object getItem(int position) {
        return videoUris.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_video_grid, parent, false);
            holder = new ViewHolder();
            holder.videoThumbnail = convertView.findViewById(R.id.videoThumbnail);
            holder.playIcon = convertView.findViewById(R.id.playIcon);
            holder.selectionOverlay = convertView.findViewById(R.id.selectionOverlay);
            holder.checkmark = convertView.findViewById(R.id.checkmark);
            holder.disabledOverlay = convertView.findViewById(R.id.disabledOverlay);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Uri videoUri = videoUris.get(position);
        boolean isSelected = selectedVideos.contains(videoUri);
        boolean isMaxReached = selectedVideos.size() >= maxSelection && !isSelected;
        
        // Load video thumbnail using ThumbnailUtils for faster loading
        try {
            // First try to get thumbnail from MediaStore
            long videoId = Long.parseLong(videoUri.getLastPathSegment());
            Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(
                context.getContentResolver(),
                videoId,
                MediaStore.Video.Thumbnails.MINI_KIND,
                null
            );
            
            if (thumbnail != null) {
                holder.videoThumbnail.setImageBitmap(thumbnail);
            } else {
                // Fallback to Glide if thumbnail not available
                Glide.with(context)
                    .load(videoUri)
                    .centerCrop()
                    .override(200, 200)
                    
                    .error(R.drawable.inviteimg)
                    .into(holder.videoThumbnail);
            }
        } catch (Exception e) {
            // Fallback to Glide if there's any error
            Glide.with(context)
                .load(videoUri)
                .centerCrop()
                .override(200, 200)
                .placeholder(R.drawable.baseline_play_arrow_24)
                .error(R.drawable.baseline_play_arrow_24)
                .into(holder.videoThumbnail);
        }
        
        // Update selection state
        holder.selectionOverlay.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        holder.checkmark.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        holder.disabledOverlay.setVisibility(isMaxReached ? View.VISIBLE : View.GONE);
        holder.playIcon.setVisibility(isMaxReached ? View.GONE : View.VISIBLE);
        
        // Apply theme color to checkmark background (circular)
        if (isSelected) {
//            GradientDrawable checkmarkBg = new GradientDrawable();
//            checkmarkBg.setShape(GradientDrawable.OVAL);
//            checkmarkBg.setColor(themeColor);
//            holder.checkmark.setBackground(checkmarkBg);

            holder.checkmark.setImageTintList(tintList);
        }
        
        // Set click listener
        convertView.setOnClickListener(v -> {
            if (isMaxReached) {
                // Show message that max selection reached
                return;
            }
            
            if (listener != null) {
                listener.onVideoSelected(videoUri);
            }
        });
        
        return convertView;
    }
    
    public void clearSelection() {
        selectedVideos.clear();
        notifyDataSetChanged();
    }
    
    private static class ViewHolder {
        ImageView videoThumbnail;
        ImageView playIcon;
        View selectionOverlay;
        ImageView checkmark;
        View disabledOverlay;
    }
}
