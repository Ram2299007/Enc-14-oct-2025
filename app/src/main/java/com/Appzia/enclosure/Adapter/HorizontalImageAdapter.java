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

public class HorizontalImageAdapter extends RecyclerView.Adapter<HorizontalImageAdapter.ViewHolder> {
    
    private Context context;
    private List<Uri> imageUris;
    private OnImageClickListener listener;
    private int selectedPosition = 0;
    
    // Multi-selection support
    private ArrayList<Uri> selectedImageUris = new ArrayList<>();
    private boolean isMultiSelectionMode = false;
    private OnImageSelectionListener selectionListener;
    
    public interface OnImageClickListener {
        void onImageClick(int position, Uri imageUri);
    }
    
    public interface OnImageSelectionListener {
        void onImageSelectionChanged(ArrayList<Uri> selectedUris);
        void onMultiSelectionModeChanged(boolean isMultiMode);
    }
    
    public HorizontalImageAdapter(Context context, List<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }
    
    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }
    
    public void setOnImageSelectionListener(OnImageSelectionListener listener) {
        this.selectionListener = listener;
    }
    
    public void setMultiSelectionMode(boolean isMultiMode) {
        this.isMultiSelectionMode = isMultiMode;
        if (!isMultiMode) {
            selectedImageUris.clear();
        }
        notifyDataSetChanged();
        if (selectionListener != null) {
            selectionListener.onMultiSelectionModeChanged(isMultiMode);
        }
    }
    
    public ArrayList<Uri> getSelectedImageUris() {
        return selectedImageUris;
    }
    
    public boolean isMultiSelectionMode() {
        return isMultiSelectionMode;
    }
    
    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(oldPosition);
        notifyItemChanged(selectedPosition);
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.util.Log.d("HorizontalImage", "HorizontalImageAdapter onCreateViewHolder called");
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_image, parent, false);
        android.util.Log.d("HorizontalImage", "Horizontal view inflated: " + (view != null ? "yes" : "no"));
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        boolean isSelected = selectedImageUris.contains(imageUri);
        
        android.util.Log.d("HorizontalImage", "HorizontalImageAdapter onBindViewHolder - Position: " + position + ", URI: " + imageUri);
        android.util.Log.d("HorizontalImage", "ImageView found: " + (holder.imageView != null ? "yes" : "no"));
        
        // Load image using Glide
        Glide.with(context)
                .load(imageUri)
                .centerCrop()
                .into(holder.imageView);
        
        android.util.Log.d("HorizontalImage", "HorizontalImageAdapter Glide load completed for position: " + position);
        
        // Update checkmark visibility
        holder.checkmark.setVisibility(isMultiSelectionMode && isSelected ? View.VISIBLE : View.GONE);
        
        // Set selection state
        if (isMultiSelectionMode) {
            // Multi-selection mode
            holder.imageView.setAlpha(isSelected ? 0.7f : 1.0f);
            holder.selectionBorder.setVisibility(View.GONE);
        } else {
            // Normal mode - show selected position
            if (position == selectedPosition) {
                holder.imageView.setAlpha(1.0f);
                holder.selectionBorder.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setAlpha(0.7f);
                holder.selectionBorder.setVisibility(View.GONE);
            }
        }
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (isMultiSelectionMode) {
                // Handle multi-selection
                if (isSelected) {
                    selectedImageUris.remove(imageUri);
                } else {
                    selectedImageUris.add(imageUri);
                }
                notifyItemChanged(position);
                
                if (selectionListener != null) {
                    selectionListener.onImageSelectionChanged(selectedImageUris);
                }
            } else {
                // Normal mode
                if (listener != null) {
                    listener.onImageClick(position, imageUri);
                }
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return imageUris.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View selectionBorder;
        ImageView checkmark;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnailImage);
            selectionBorder = itemView.findViewById(R.id.selectionBorder);
            checkmark = itemView.findViewById(R.id.checkmark);
        }
    }
}
