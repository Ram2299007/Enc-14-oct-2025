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
import com.Appzia.enclosure.Screens.chattingScreen;

import java.util.ArrayList;
import java.util.List;

public class ContactPreviewAdapter extends RecyclerView.Adapter<ContactPreviewAdapter.ViewHolder> {
    
    private Context context;
    private List<chattingScreen.ContactInfo> contactInfos;
    
    public ContactPreviewAdapter(Context context, List<chattingScreen.ContactInfo> contactInfos) {
        this.context = context;
        this.contactInfos = contactInfos;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact_preview, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        chattingScreen.ContactInfo contactInfo = contactInfos.get(position);
        
        android.util.Log.d("ContactPreviewAdapter", "Binding contact " + position + ": " + contactInfo.name);
        
        // Set contact name
        holder.contactName.setText(contactInfo.name);
        
        // Set contact phone
        if (contactInfo.phoneNumber != null && !contactInfo.phoneNumber.isEmpty()) {
            holder.contactPhone.setText(contactInfo.phoneNumber);
            holder.contactPhone.setVisibility(View.VISIBLE);
        } else {
            holder.contactPhone.setVisibility(View.GONE);
        }
        
        // Set contact email
        if (contactInfo.email != null && !contactInfo.email.isEmpty()) {
            holder.contactEmail.setText(contactInfo.email);
            holder.contactEmail.setVisibility(View.VISIBLE);
        } else {
            holder.contactEmail.setVisibility(View.GONE);
        }
        
        // Set contact initial
        if (contactInfo.name != null && !contactInfo.name.isEmpty()) {
            String initial = contactInfo.name.substring(0, 1).toUpperCase();
            holder.contactInitial.setText(initial);
        }
    }
    
    @Override
    public int getItemCount() {
        return contactInfos.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout contactAvatar;
        TextView contactInitial;
        TextView contactName;
        TextView contactPhone;
        TextView contactEmail;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactAvatar = itemView.findViewById(R.id.contactAvatar);
            contactInitial = itemView.findViewById(R.id.contactInitial);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
            contactEmail = itemView.findViewById(R.id.contactEmail);
        }
    }
}
