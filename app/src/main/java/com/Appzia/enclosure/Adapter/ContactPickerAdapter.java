package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.WhatsAppLikeContactPicker;

import java.util.ArrayList;
import java.util.List;

public class ContactPickerAdapter extends RecyclerView.Adapter<ContactPickerAdapter.ContactViewHolder> {
    
    private Context context;
    private List<WhatsAppLikeContactPicker.ContactInfo> allContacts;
    private List<WhatsAppLikeContactPicker.ContactInfo> selectedContacts;
    private int maxSelection;
    private int themeColor;
    private OnContactSelectedListener listener;
    ColorStateList tintList;
    public interface OnContactSelectedListener {
        void onContactSelected(WhatsAppLikeContactPicker.ContactInfo contactInfo, boolean isSelected);
    }
    
    public ContactPickerAdapter(Context context, List<WhatsAppLikeContactPicker.ContactInfo> allContacts, 
                               List<WhatsAppLikeContactPicker.ContactInfo> selectedContacts, int maxSelection) {
        this.context = context;
        this.allContacts = allContacts != null ? allContacts : new ArrayList<>();
        this.selectedContacts = selectedContacts != null ? selectedContacts : new ArrayList<>();
        this.maxSelection = maxSelection;
        this.themeColor = Color.parseColor("#00A3E9"); // Default theme color
    }
    
    public void setOnContactSelectedListener(OnContactSelectedListener listener) {
        this.listener = listener;
    }
    
    public void setThemeColor(int color, ColorStateList tintList) {
        this.themeColor = color;
        this.tintList = tintList;
        notifyDataSetChanged();
    }
    
    public void updateContacts(List<WhatsAppLikeContactPicker.ContactInfo> contacts) {
        this.allContacts = contacts != null ? contacts : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_picker_row, parent, false);
        return new ContactViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        WhatsAppLikeContactPicker.ContactInfo contact = allContacts.get(position);
        
        // Set contact name
        holder.nameText.setText(contact.name != null ? contact.name : "Unknown");
        
        // Set contact initial for avatar
        String initial = "";
        if (contact.name != null && !contact.name.isEmpty()) {
            initial = contact.name.substring(0, 1).toUpperCase();
        }
        holder.contactInitial.setText(initial);
        
        // Set phone number
        if (contact.phone != null && !contact.phone.isEmpty()) {
            holder.phoneText.setText(contact.phone);
            holder.phoneText.setVisibility(View.VISIBLE);
        } else {
            holder.phoneText.setVisibility(View.GONE);
        }
        
        // Set email
        if (contact.email != null && !contact.email.isEmpty()) {
            holder.emailText.setText(contact.email);
            holder.emailText.setVisibility(View.VISIBLE);
        } else {
            holder.emailText.setVisibility(View.GONE);
        }
        
        // Check if contact is selected
        boolean isSelected = selectedContacts.contains(contact);
        
        // Enable/disable based on selection limit
        boolean canSelect = isSelected || selectedContacts.size() < maxSelection;
        
        // Update UI based on selection state
        holder.itemContainer.setSelected(isSelected);
        holder.selectionIndicator.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        holder.selectionCheckIcon.setVisibility(isSelected ? View.VISIBLE : View.GONE);

        if(isSelected){
            holder.selectionCheckIcon.setImageTintList(tintList);
        }
        
        // Set alpha based on selection state
        float alpha = canSelect ? 1.0f : 0.5f;
        holder.itemContainer.setAlpha(alpha);
        holder.itemContainer.setEnabled(canSelect);
        
        // Set click listener for whole item
        holder.itemContainer.setOnClickListener(v -> {
            if (canSelect) {
                boolean newSelected = !isSelected;
                if (listener != null) {
                    listener.onContactSelected(contact, newSelected);
                }
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return allContacts.size();
    }
    
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemContainer;
        View selectionIndicator;
        LinearLayout contactAvatar;
        TextView contactInitial;
        TextView nameText;
        TextView phoneText;
        TextView emailText;
        ImageView selectionCheckIcon;
        
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.contactItemContainer);
            selectionIndicator = itemView.findViewById(R.id.selectionIndicator);
            contactAvatar = itemView.findViewById(R.id.contactAvatar);
            contactInitial = itemView.findViewById(R.id.contactInitial);
            nameText = itemView.findViewById(R.id.contactName);
            phoneText = itemView.findViewById(R.id.contactPhone);
            emailText = itemView.findViewById(R.id.contactEmail);
            selectionCheckIcon = itemView.findViewById(R.id.selectionCheckIcon);
        }
    }
    
    public void clearSelection() {
        selectedContacts.clear();
        notifyDataSetChanged();
    }
}
