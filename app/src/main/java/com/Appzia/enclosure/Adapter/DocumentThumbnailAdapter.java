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

public class DocumentThumbnailAdapter extends RecyclerView.Adapter<DocumentThumbnailAdapter.DocumentThumbnailViewHolder> {
    
    private Context context;
    private List<Uri> documentUris;
    
    public DocumentThumbnailAdapter(Context context, List<Uri> documentUris) {
        this.context = context;
        this.documentUris = documentUris;
    }
    
    @NonNull
    @Override
    public DocumentThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_document_thumbnail, parent, false);
        return new DocumentThumbnailViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DocumentThumbnailViewHolder holder, int position) {
        Uri documentUri = documentUris.get(position);
        
        // Get file name
        String fileName = getFileName(documentUri);
        holder.documentName.setText(fileName);
        
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
                iconView.setColorFilter(context.getResources().getColor(R.color.white));
                break;
            // Videos - use videopng icon from chattingScreen
            case "mp4":
            case "avi":
            case "mov":
            case "wmv":
            case "mkv":
            case "vid":
                iconView.setImageResource(R.drawable.videopng);
                iconView.setColorFilter(context.getResources().getColor(R.color.white));
                break;
            // Audio - use contact icon for audio files
            case "mp3":
            case "wav":
            case "aac":
            case "ogg":
            case "aud":
                iconView.setImageResource(R.drawable.contact);
                iconView.setColorFilter(context.getResources().getColor(R.color.white));
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
                iconView.setColorFilter(context.getResources().getColor(R.color.white));
                break;
            default:
                iconView.setImageResource(R.drawable.documentsvg);
                iconView.setColorFilter(context.getResources().getColor(R.color.white));
                break;
        }
    }
    
    public static class DocumentThumbnailViewHolder extends RecyclerView.ViewHolder {
        ImageView documentIcon;
        TextView documentName;
        TextView selectionNumber;
        
        public DocumentThumbnailViewHolder(@NonNull View itemView) {
            super(itemView);
            documentIcon = itemView.findViewById(R.id.documentIcon);
            documentName = itemView.findViewById(R.id.documentName);
            selectionNumber = itemView.findViewById(R.id.selectionNumber);
        }
    }
}
