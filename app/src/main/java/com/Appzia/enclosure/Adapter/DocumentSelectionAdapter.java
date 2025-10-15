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

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DocumentSelectionAdapter extends RecyclerView.Adapter<DocumentSelectionAdapter.DocumentViewHolder> {
    
    private Context context;
    private List<DocumentItem> documents;
    private List<Uri> selectedDocuments;
    private OnDocumentSelectionListener listener;
    private int maxSelection;
    
    public interface OnDocumentSelectionListener {
        void onDocumentSelected(Uri documentUri, boolean isSelected);
        void onMaxSelectionReached();
    }
    
    public static class DocumentItem {
        public Uri uri;
        public String name;
        public String size;
        public String type;
        public File file;
        
        public DocumentItem(Uri uri, String name, String size, String type, File file) {
            this.uri = uri;
            this.name = name;
            this.size = size;
            this.type = type;
            this.file = file;
        }
    }
    
    public DocumentSelectionAdapter(Context context, List<DocumentItem> documents, List<Uri> selectedDocuments, int maxSelection) {
        this.context = context;
        this.documents = documents;
        this.selectedDocuments = selectedDocuments;
        this.maxSelection = maxSelection;
    }
    
    public void setOnDocumentSelectionListener(OnDocumentSelectionListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_document_selection, parent, false);
        return new DocumentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        DocumentItem document = documents.get(position);
        boolean isSelected = selectedDocuments.contains(document.uri);
        
        holder.documentName.setText(document.name);
        holder.documentSize.setText(document.size);
        holder.documentType.setText(document.type.toUpperCase());
        
        // Set document icon based on type
        setDocumentIcon(holder.documentIcon, document.type);
        
        // Check if limit is reached and this item is not selected
        boolean isLimitReached = selectedDocuments.size() >= maxSelection;
        boolean shouldDisable = isLimitReached && !isSelected;
        
        // Update selection state
        updateSelectionState(holder, isSelected, selectedDocuments.indexOf(document.uri) + 1);
        
        // Apply disabled state if limit reached
        if (shouldDisable) {
            holder.itemView.setAlpha(0.5f);
            holder.itemView.setEnabled(false);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.itemView.setEnabled(true);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (isSelected) {
                // Deselect document
                selectedDocuments.remove(document.uri);
                if (listener != null) {
                    listener.onDocumentSelected(document.uri, false);
                }
                notifyDataSetChanged(); // Refresh all items to update disabled state
            } else {
                // Select document if under limit
                if (selectedDocuments.size() < maxSelection) {
                    selectedDocuments.add(document.uri);
                    if (listener != null) {
                        listener.onDocumentSelected(document.uri, true);
                    }
                    notifyDataSetChanged(); // Refresh all items to update disabled state
                } else {
                    if (listener != null) {
                        listener.onMaxSelectionReached();
                    }
                }
            }
        });
    }
    
    private void setDocumentIcon(ImageView iconView, String type) {
        switch (type.toLowerCase()) {
            // Images - use gallery icon from chattingScreen
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "webp":
            case "bmp":
            case "img":
                iconView.setImageResource(R.drawable.gallery);
                break;
            // Videos - use videopng icon from chattingScreen
            case "mp4":
            case "avi":
            case "mov":
            case "wmv":
            case "mkv":
            case "vid":
                iconView.setImageResource(R.drawable.videopng);
                break;
            // Audio - use contact icon for audio files
            case "mp3":
            case "wav":
            case "aac":
            case "ogg":
            case "aud":
                iconView.setImageResource(R.drawable.contact);
                break;
            // Documents - use documentsvg icon from chattingScreen
            case "pdf":
            case "doc":
            case "docx":
            case "xls":
            case "xlsx":
            case "ppt":
            case "pptx":
            case "txt":
            case "zip":
            case "rar":
            case "7z":
                iconView.setImageResource(R.drawable.documentsvg);
                break;
            default:
                iconView.setImageResource(R.drawable.documentsvg);
                break;
        }
    }
    
    private void updateSelectionState(DocumentViewHolder holder, boolean isSelected, int selectionNumber) {
        if (isSelected) {
            holder.selectionIndicator.setVisibility(View.GONE);
            holder.selectionNumber.setVisibility(View.VISIBLE);
            holder.selectionNumber.setText(String.valueOf(selectionNumber));
        } else {
            holder.selectionIndicator.setVisibility(View.GONE);
            holder.selectionNumber.setVisibility(View.GONE);
        }
    }
    
    @Override
    public int getItemCount() {
        return documents.size();
    }
    
    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        ImageView documentIcon;
        TextView documentName;
        TextView documentSize;
        TextView documentType;
        ImageView selectionIndicator;
        TextView selectionNumber;
        
        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            documentIcon = itemView.findViewById(R.id.documentIcon);
            documentName = itemView.findViewById(R.id.documentName);
            documentSize = itemView.findViewById(R.id.documentSize);
            documentType = itemView.findViewById(R.id.documentType);
            selectionIndicator = itemView.findViewById(R.id.selectionIndicator);
            selectionNumber = itemView.findViewById(R.id.selectionNumber);
        }
    }
    
    public void clearSelection() {
        selectedDocuments.clear();
        notifyDataSetChanged();
    }
    
    public static String formatFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
