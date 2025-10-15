package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.grpChattingScreen;

import java.util.List;

public class GroupHorizontalContactThumbnailAdapter extends RecyclerView.Adapter<GroupHorizontalContactThumbnailAdapter.ViewHolder> {
    
    private Context context;
    private List<grpChattingScreen.ContactInfo> contactInfos;
    private OnContactClickListener listener;
    private int selectedPosition = 0;
    
    public interface OnContactClickListener {
        void onContactClick(int position, grpChattingScreen.ContactInfo contactInfo);
    }
    
    public GroupHorizontalContactThumbnailAdapter(Context context, List<grpChattingScreen.ContactInfo> contactInfos) {
        this.context = context;
        this.contactInfos = contactInfos;
    }
    
    public void setOnContactClickListener(OnContactClickListener listener) {
        this.listener = listener;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_contact, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        grpChattingScreen.ContactInfo contactInfo = contactInfos.get(position);
        
        // Set contact initial
        if (contactInfo.name != null && !contactInfo.name.isEmpty()) {
            String initial = contactInfo.name.substring(0, 1).toUpperCase();
            holder.contactInitial.setText(initial);
        }
        
        // Set selection state
        if (position == selectedPosition) {
            holder.itemView.setAlpha(1.0f);
            holder.selectionIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setAlpha(0.6f);
            holder.selectionIndicator.setVisibility(View.GONE);
        }
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContactClick(position, contactInfo);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return contactInfos.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout contactAvatar;
        TextView contactInitial;
        View selectionIndicator;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactAvatar = itemView.findViewById(R.id.contactAvatar);
            contactInitial = itemView.findViewById(R.id.contactInitial);
            selectionIndicator = itemView.findViewById(R.id.selectionIndicator);
        }
    }
}
