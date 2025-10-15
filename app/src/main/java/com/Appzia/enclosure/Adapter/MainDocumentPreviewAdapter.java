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

import java.util.List;

public class MainDocumentPreviewAdapter extends RecyclerView.Adapter<MainDocumentPreviewAdapter.DocumentPreviewViewHolder> {
    
    private Context context;
    private List<Uri> documentUris;
    private int currentPosition = 0;
    
    public MainDocumentPreviewAdapter(Context context, List<Uri> documentUris) {
        this.context = context;
        this.documentUris = documentUris;
    }
    
    @NonNull
    @Override
    public DocumentPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_document_preview, parent, false);
        return new DocumentPreviewViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DocumentPreviewViewHolder holder, int position) {
        Uri documentUri = documentUris.get(position);
        
        // Get file name
        String fileName = getFileName(documentUri);
        holder.documentName.setText(fileName);
        
        // Get file size
        String fileSize = getFileSize(documentUri);
        holder.documentSize.setText(fileSize);
        
        // Get file type and set icon
        String fileType = getFileType(documentUri);
        setDocumentIcon(holder.documentIcon, fileType);
        
        // Set selection number
        holder.selectionNumber.setText(String.valueOf(position + 1));
    }
    
    @Override
    public int getItemCount() {
        return documentUris.size();
    }
    
    public void setCurrentPosition(int position) {
        this.currentPosition = position;
        notifyDataSetChanged();
    }
    
    private String getFileName(Uri uri) {
        String fileName = null;
        android.database.Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
            if (index != -1) {
                fileName = cursor.getString(index);
            }
            cursor.close();
        }
        
        if (fileName == null) {
            fileName = "document_" + System.currentTimeMillis();
        }
        
        return fileName;
    }
    
    private String getFileSize(Uri uri) {
        try {
            android.database.Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE);
                if (sizeIndex != -1) {
                    long size = cursor.getLong(sizeIndex);
                    cursor.close();
                    return formatFileSize(size);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown Size";
    }
    
    private String formatFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.1f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }
    
    private String getFileType(Uri uri) {
        try {
            String mimeType = context.getContentResolver().getType(uri);
            if (mimeType != null) {
                // Images
                if (mimeType.startsWith("image/jpeg") || mimeType.startsWith("image/jpg")) return "jpg";
                if (mimeType.startsWith("image/png")) return "png";
                if (mimeType.startsWith("image/gif")) return "gif";
                if (mimeType.startsWith("image/webp")) return "webp";
                if (mimeType.startsWith("image/bmp")) return "bmp";
                if (mimeType.startsWith("image/")) return "img";
                
                // Videos
                if (mimeType.startsWith("video/mp4")) return "mp4";
                if (mimeType.startsWith("video/avi")) return "avi";
                if (mimeType.startsWith("video/mov")) return "mov";
                if (mimeType.startsWith("video/wmv")) return "wmv";
                if (mimeType.startsWith("video/mkv")) return "mkv";
                if (mimeType.startsWith("video/")) return "vid";
                
                // Audio
                if (mimeType.startsWith("audio/mp3")) return "mp3";
                if (mimeType.startsWith("audio/wav")) return "wav";
                if (mimeType.startsWith("audio/aac")) return "aac";
                if (mimeType.startsWith("audio/ogg")) return "ogg";
                if (mimeType.startsWith("audio/")) return "aud";
                
                // Documents
                if (mimeType.startsWith("application/pdf")) return "pdf";
                if (mimeType.startsWith("application/msword") || mimeType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml")) return "doc";
                if (mimeType.startsWith("application/vnd.ms-excel") || mimeType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml")) return "xls";
                if (mimeType.startsWith("application/vnd.ms-powerpoint") || mimeType.startsWith("application/vnd.openxmlformats-officedocument.presentationml")) return "ppt";
                if (mimeType.startsWith("text/plain")) return "txt";
                if (mimeType.startsWith("text/")) return "txt";
                if (mimeType.startsWith("application/rtf")) return "rtf";
                if (mimeType.startsWith("application/vnd.oasis.opendocument")) return "odt";
                
                // Archives
                if (mimeType.startsWith("application/zip")) return "zip";
                if (mimeType.startsWith("application/x-rar")) return "rar";
                if (mimeType.startsWith("application/x-7z")) return "7z";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "file";
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
    
    public static class DocumentPreviewViewHolder extends RecyclerView.ViewHolder {
        ImageView documentIcon;
        TextView documentName;
        TextView documentSize;
        TextView selectionNumber;
        
        public DocumentPreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            documentIcon = itemView.findViewById(R.id.documentIcon);
            documentName = itemView.findViewById(R.id.documentName);
            documentSize = itemView.findViewById(R.id.documentSize);
            selectionNumber = itemView.findViewById(R.id.selectionNumber);
        }
    }
}