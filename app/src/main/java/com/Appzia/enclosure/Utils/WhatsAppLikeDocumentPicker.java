package com.Appzia.enclosure.Utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.DocumentSelectionAdapter;
import com.Appzia.enclosure.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WhatsAppLikeDocumentPicker extends AppCompatActivity {
    
    public static final String EXTRA_SELECTED_DOCUMENTS = "selected_documents";
    public static final String EXTRA_MAX_SELECTION = "max_selection";
    public static final int DEFAULT_MAX_SELECTION = 5;
    
    private RecyclerView documentRecyclerView;
    private TextView counterText;
    private TextView smallCounterText;
    private View doneButton;
    private View cancelButton;
    private ProgressBar loadingIndicator;
    private View emptyState;
    private DocumentSelectionAdapter adapter;
    private List<Uri> selectedDocuments = new ArrayList<>();
    private List<DocumentSelectionAdapter.DocumentItem> allDocuments = new ArrayList<>();
    private int maxSelection = DEFAULT_MAX_SELECTION;
    
    // Pagination variables
    private static final int PAGE_SIZE = 20;
    private boolean isLoading = false;
    private boolean hasMoreData = true;
    private int currentPage = 0;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_document_picker);
        
        // Get extras
        if (getIntent() != null) {
            maxSelection = getIntent().getIntExtra(EXTRA_MAX_SELECTION, DEFAULT_MAX_SELECTION);
            ArrayList<Uri> existingSelection = getIntent().getParcelableArrayListExtra(EXTRA_SELECTED_DOCUMENTS);
            if (existingSelection != null) {
                selectedDocuments.addAll(existingSelection);
            }
        }
        
        initViews();
        setupListeners();
        loadDocuments();
    }
    
    private void initViews() {
        documentRecyclerView = findViewById(R.id.documentRecyclerView);
        counterText = findViewById(R.id.counterText);
        smallCounterText = findViewById(R.id.smallCounterText);
        doneButton = findViewById(R.id.doneButton);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        View backButton = findViewById(R.id.backButton);
        
        // Setup RecyclerView
        documentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Setup back button listener
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                // Clear all selections when canceling
                selectedDocuments.clear();
                if (adapter != null) {
                    adapter.clearSelection();
                }
                setResult(Activity.RESULT_CANCELED);
                finish();
            });
        }
        
        // Update counter
        updateCounter();
    }
    
    @Override
    public void onBackPressed() {
        // Clear all selections when going back
        selectedDocuments.clear();
        if (adapter != null) {
            adapter.clearSelection();
        }
        setResult(Activity.RESULT_CANCELED);
        SwipeNavigationHelper.finishWithSwipe(WhatsAppLikeDocumentPicker.this);
    }
    
    private void setupListeners() {
        doneButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra(EXTRA_SELECTED_DOCUMENTS, new ArrayList<>(selectedDocuments));
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
    
    private void loadDocuments() {
        if (isLoading) return;
        
        isLoading = true;
        if (currentPage == 0) {
            loadingIndicator.setVisibility(View.VISIBLE);
            documentRecyclerView.setVisibility(View.GONE);
        }
        
        new Thread(() -> {
            List<DocumentSelectionAdapter.DocumentItem> documents = getAllDocuments();
            
            // Add some debug logging
            android.util.Log.d("DocumentPicker", "Found " + documents.size() + " documents");
            
            // If no documents found, try to create some sample documents for testing
            if (documents.isEmpty()) {
                documents = createSampleDocuments();
                android.util.Log.d("DocumentPicker", "Created " + documents.size() + " sample documents");
            }
            
            // Sort by date modified (most recent first)
            documents.sort((a, b) -> {
                if (a.file != null && b.file != null) {
                    return Long.compare(b.file.lastModified(), a.file.lastModified());
                }
                return 0;
            });
            
            final List<DocumentSelectionAdapter.DocumentItem> finalDocuments = documents;
            
            runOnUiThread(() -> {
                if (currentPage == 0) {
                    allDocuments.clear();
                }
                
                // Add paginated documents
                int startIndex = currentPage * PAGE_SIZE;
                int endIndex = Math.min(startIndex + PAGE_SIZE, finalDocuments.size());
                
                for (int i = startIndex; i < endIndex; i++) {
                    allDocuments.add(finalDocuments.get(i));
                }
                
                hasMoreData = endIndex < finalDocuments.size();
                currentPage++;
                
                if (adapter == null) {
                    adapter = new DocumentSelectionAdapter(this, allDocuments, selectedDocuments, maxSelection);
                    adapter.setOnDocumentSelectionListener(new DocumentSelectionAdapter.OnDocumentSelectionListener() {
                        @Override
                        public void onDocumentSelected(Uri documentUri, boolean isSelected) {
                            updateCounter();
                        }
                        
                        @Override
                        public void onMaxSelectionReached() {
                            Toast.makeText(WhatsAppLikeDocumentPicker.this, 
                                "Maximum " + maxSelection + " documents can be selected", 
                                Toast.LENGTH_SHORT).show();
                        }
                    });
                    documentRecyclerView.setAdapter(adapter);
                    
                    // Add scroll listener for pagination
                    documentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            
                            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            if (layoutManager != null && hasMoreData && !isLoading) {
                                int visibleItemCount = layoutManager.getChildCount();
                                int totalItemCount = layoutManager.getItemCount();
                                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                                
                                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5) {
                                    loadDocuments(); // Load more documents
                                }
                            }
                        }
                    });
                } else {
                    adapter.notifyDataSetChanged();
                }
                
                loadingIndicator.setVisibility(View.GONE);
                documentRecyclerView.setVisibility(View.VISIBLE);
                isLoading = false;
            });
        }).start();
    }
    
    private List<DocumentSelectionAdapter.DocumentItem> getAllDocuments() {
        List<DocumentSelectionAdapter.DocumentItem> documents = new ArrayList<>();
        
        // Try multiple approaches to get documents
        documents.addAll(getDocumentsFromMediaStore());
        
        // If no documents found, try alternative approach
        if (documents.isEmpty()) {
            documents.addAll(getDocumentsFromDownloads());
        }
        
        return documents;
    }
    
    private List<DocumentSelectionAdapter.DocumentItem> getDocumentsFromMediaStore() {
        List<DocumentSelectionAdapter.DocumentItem> documents = new ArrayList<>();
        
        String[] projection = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE
        };
        
        // Try different selection criteria to get all file types
        String[] selections = {
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'image/%' OR " +
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'video/%' OR " +
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'audio/%' OR " +
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'application/pdf%' OR " +
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'application/msword%' OR " +
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'application/vnd.openxmlformats-officedocument%' OR " +
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'text/%' OR " +
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'application/rtf%' OR " +
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'application/zip%' OR " +
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'application/x-rar%' OR " +
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'application/x-7z%'",
            MediaStore.Files.FileColumns.SIZE + " > 0"
        };
        
        for (String selection : selections) {
            try (Cursor cursor = getContentResolver().query(
                    MediaStore.Files.getContentUri("external"),
                    projection,
                    selection,
                    null,
                    MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC")) {
                
                if (cursor != null) {
                    android.util.Log.d("DocumentPicker", "MediaStore query returned " + cursor.getCount() + " results");
                    
                    if (cursor.moveToFirst()) {
                        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
                        int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                        int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
                        int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);
                        int mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE);
                        
                        do {
                            long id = cursor.getLong(idColumn);
                            String data = cursor.getString(dataColumn);
                            String name = cursor.getString(nameColumn);
                            long size = cursor.getLong(sizeColumn);
                            String mimeType = cursor.getString(mimeTypeColumn);
                            
                            if (data != null && name != null) {
                                File file = new File(data);
                                if (file.exists() && isDocumentFile(mimeType)) {
                                    Uri uri = Uri.withAppendedPath(MediaStore.Files.getContentUri("external"), String.valueOf(id));
                                    String fileType = getFileTypeFromMimeType(mimeType);
                                    String formattedSize = DocumentSelectionAdapter.formatFileSize(size);
                                    
                                    documents.add(new DocumentSelectionAdapter.DocumentItem(uri, name, formattedSize, fileType, file));
                                    android.util.Log.d("DocumentPicker", "Added document: " + name);
                                }
                            }
                        } while (cursor.moveToNext());
                    }
                }
            } catch (Exception e) {
                android.util.Log.e("DocumentPicker", "Error querying MediaStore: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return documents;
    }
    
    private List<DocumentSelectionAdapter.DocumentItem> getDocumentsFromDownloads() {
        List<DocumentSelectionAdapter.DocumentItem> documents = new ArrayList<>();
        
        // Try to get documents from Downloads folder
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (downloadsDir != null && downloadsDir.exists()) {
            File[] files = downloadsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isDocumentFile(getMimeTypeFromFile(file))) {
                        String fileName = file.getName();
                        String fileType = getFileTypeFromFileName(fileName);
                        String formattedSize = DocumentSelectionAdapter.formatFileSize(file.length());
                        Uri uri = Uri.fromFile(file);
                        
                        documents.add(new DocumentSelectionAdapter.DocumentItem(uri, fileName, formattedSize, fileType, file));
                    }
                }
            }
        }
        
        return documents;
    }
    
    private String getMimeTypeFromFile(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        
        switch (extension) {
            case "pdf": return "application/pdf";
            case "doc": return "application/msword";
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls": return "application/vnd.ms-excel";
            case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt": return "application/vnd.ms-powerpoint";
            case "pptx": return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "txt": return "text/plain";
            default: return "application/octet-stream";
        }
    }
    
    private String getFileTypeFromFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return extension;
    }
    
    private boolean isDocumentFile(String mimeType) {
        if (mimeType == null) return false;
        
        // Include all file types: images, videos, documents, audio, etc.
        return mimeType.startsWith("image/") ||
               mimeType.startsWith("video/") ||
               mimeType.startsWith("audio/") ||
               mimeType.startsWith("application/pdf") ||
               mimeType.startsWith("application/msword") ||
               mimeType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml") ||
               mimeType.startsWith("application/vnd.ms-excel") ||
               mimeType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml") ||
               mimeType.startsWith("application/vnd.ms-powerpoint") ||
               mimeType.startsWith("application/vnd.openxmlformats-officedocument.presentationml") ||
               mimeType.startsWith("text/") ||
               mimeType.startsWith("application/rtf") ||
               mimeType.startsWith("application/vnd.oasis.opendocument") ||
               mimeType.startsWith("application/zip") ||
               mimeType.startsWith("application/x-rar") ||
               mimeType.startsWith("application/x-7z") ||
               mimeType.startsWith("application/octet-stream");
    }
    
    private String getFileTypeFromMimeType(String mimeType) {
        if (mimeType == null) return "unknown";
        
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
        
        return "file";
    }
    
    private void updateCounter() {
        int count = selectedDocuments.size();
        counterText.setText(count + "/" + maxSelection);
        smallCounterText.setText(String.valueOf(count));
        
        // Show/hide small counter
        smallCounterText.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        
        // Enable/disable done button
        doneButton.setEnabled(count > 0);
        doneButton.setAlpha(count > 0 ? 1.0f : 0.5f);
    }
    
    private List<DocumentSelectionAdapter.DocumentItem> createSampleDocuments() {
        List<DocumentSelectionAdapter.DocumentItem> sampleDocs = new ArrayList<>();
        
        // Create sample files of all types for testing
        String[] sampleFiles = {
            "Vacation Photo.jpg",
            "Screenshot.png", 
            "Funny GIF.gif",
            "Meeting Video.mp4",
            "Music Track.mp3",
            "Sample Document.pdf",
            "Report.docx", 
            "Spreadsheet.xlsx",
            "Presentation.pptx",
            "Notes.txt",
            "Data Archive.zip",
            "Manual.pdf"
        };
        
        String[] fileTypes = {"jpg", "png", "gif", "mp4", "mp3", "pdf", "docx", "xlsx", "pptx", "txt", "zip", "pdf"};
        String[] fileSizes = {"3.2 MB", "1.8 MB", "2.1 MB", "15.7 MB", "4.3 MB", "2.5 MB", "1.8 MB", "3.2 MB", "4.1 MB", "156 KB", "8.9 MB", "5.7 MB"};
        
        for (int i = 0; i < sampleFiles.length; i++) {
            // Create a dummy file for testing
            File sampleFile = new File(getCacheDir(), "sample_" + sampleFiles[i]);
            try {
                sampleFile.createNewFile();
                // Write some dummy content
                java.io.FileWriter writer = new java.io.FileWriter(sampleFile);
                writer.write("This is a sample " + fileTypes[i] + " file for testing purposes.");
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Uri uri = Uri.fromFile(sampleFile);
            sampleDocs.add(new DocumentSelectionAdapter.DocumentItem(
                uri, 
                sampleFiles[i], 
                fileSizes[i], 
                fileTypes[i], 
                sampleFile
            ));
        }
        
        return sampleDocs;
    }
}
