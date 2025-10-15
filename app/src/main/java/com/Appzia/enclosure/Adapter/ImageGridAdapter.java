package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.Appzia.enclosure.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImageGridAdapter extends BaseAdapter {
    
    private Context context;
    private List<Uri> imageUris;
    private List<Uri> selectedImages;
    private int maxSelection;
    private OnImageSelectedListener listener;
    private int themeColor = Color.parseColor("#00A3E9");
    ColorStateList tintList;
    
    public interface OnImageSelectedListener {
        void onImageSelected(Uri imageUri, boolean isSelected);
    }
    
    public ImageGridAdapter(Context context, List<Uri> imageUris, List<Uri> selectedImages, int maxSelection) {
        this.context = context;
        this.imageUris = imageUris;
        this.selectedImages = selectedImages;
        this.maxSelection = maxSelection;
    }
    
    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        this.listener = listener;
    }
    
    public void setThemeColor(int color, ColorStateList tintList) {
        this.themeColor = color;
        this.tintList = tintList;
        notifyDataSetChanged();
    }
    
    public void updateImages(List<Uri> newImageUris) {
        this.imageUris = newImageUris;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return imageUris.size();
    }
    
    @Override
    public Object getItem(int position) {
        return imageUris.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_image_grid, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.selectionOverlay = convertView.findViewById(R.id.selectionOverlay);
            holder.checkmark = convertView.findViewById(R.id.checkmark);
            holder.disabledOverlay = convertView.findViewById(R.id.disabledOverlay);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Uri imageUri = imageUris.get(position);
        boolean isSelected = selectedImages.contains(imageUri);
        boolean isMaxReached = selectedImages.size() >= maxSelection && !isSelected;
        
        // Load image
        Glide.with(context)
            .load(imageUri)
            .centerCrop()
            .into(holder.imageView);
        
        // Update selection state
        holder.selectionOverlay.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        holder.checkmark.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        holder.disabledOverlay.setVisibility(isMaxReached ? View.VISIBLE : View.GONE);
        
        // Apply theme color to checkmark background (circular)
        if (isSelected) {
            holder.checkmark.setImageTintList(tintList);
        }
        
        // Set click listener
        convertView.setOnClickListener(v -> {
            if (isMaxReached) {
                // Show message that max selection reached
                return;
            }
            if (listener != null) {
                listener.onImageSelected(imageUri, !isSelected);
            }
        });
        
        return convertView;
    }
    
    public void clearSelection() {
        selectedImages.clear();
        notifyDataSetChanged();
    }
    
    private static class ViewHolder {
        ImageView imageView;
        View selectionOverlay;
        ImageView checkmark;
        View disabledOverlay;
    }
}
