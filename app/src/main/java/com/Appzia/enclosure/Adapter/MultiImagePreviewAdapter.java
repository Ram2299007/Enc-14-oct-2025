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

public class MultiImagePreviewAdapter extends RecyclerView.Adapter<MultiImagePreviewAdapter.ViewHolder> {
    
    private Context context;
    private ArrayList<Uri> imageUris;
    
    public MultiImagePreviewAdapter(Context context, ArrayList<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.util.Log.d("MultiImageAdapter", "Creating view holder");
        View view = LayoutInflater.from(context).inflate(R.layout.item_multi_image_preview, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        android.util.Log.d("MultiImageAdapter", "Binding image " + position + ": " + imageUri);
        Glide.with(context)
                .load(imageUri)
                .into(holder.imageView);
    }
    
    @Override
    public int getItemCount() {
        return imageUris.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
