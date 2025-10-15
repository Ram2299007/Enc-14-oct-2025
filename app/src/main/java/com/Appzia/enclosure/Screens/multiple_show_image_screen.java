package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.MultipleImageAdapter;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.group_messageModel;
import com.Appzia.enclosure.Model.selectionBunchModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class multiple_show_image_screen extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private MultipleImageAdapter adapter;
    private List<String> imageList;
    private int currentPosition;
    private String viewHolderTypeKey;
    private TextView titleText;
    private LinearLayout backButton;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Apply smooth navigation
        SmoothNavigationHelper.setupSmoothBackPress(this);
        
        // Set status bar color
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.appThemeColor));
        
        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false);
        
        setContentView(R.layout.activity_multiple_show_image_screen);
        
        // Initialize views
        initViews();
        
        // Get data from intent
        getIntentData();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup click listeners
        setupClickListeners();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);
    }
    
    private void getIntentData() {
        Intent intent = getIntent();
        
        // Check if messageModel is passed (new format)
        Object modelObject = intent.getSerializableExtra("messageModel");
        if (modelObject != null) {
            android.util.Log.d("MultipleImageScreen", "=== RECEIVED MESSAGE MODEL ===");
            android.util.Log.d("MultipleImageScreen", "Model type: " + modelObject.getClass().getSimpleName());
            
            List<selectionBunchModel> selectionBunch = null;
            String modelId = "";
            String selectionCount = "";
            
            if (modelObject instanceof messageModel) {
                messageModel messageModel = (messageModel) modelObject;
                selectionBunch = messageModel.getSelectionBunch();
                modelId = messageModel.getModelId();
                selectionCount = messageModel.getSelectionCount();
                android.util.Log.d("MultipleImageScreen", "Individual message model - ID: " + modelId + ", Count: " + selectionCount);
            } else if (modelObject instanceof group_messageModel) {
                group_messageModel groupMessageModel = (group_messageModel) modelObject;
                selectionBunch = groupMessageModel.getSelectionBunch();
                modelId = groupMessageModel.getModelId();
                selectionCount = groupMessageModel.getSelectionCount();
                android.util.Log.d("MultipleImageScreen", "Group message model - ID: " + modelId + ", Count: " + selectionCount);
            }
            
            android.util.Log.d("MultipleImageScreen", "Selection Bunch: " + (selectionBunch != null ? "not null" : "null"));
            android.util.Log.d("MultipleImageScreen", "Selection Bunch size: " + (selectionBunch != null ? selectionBunch.size() : 0));
            
            if (selectionBunch != null && !selectionBunch.isEmpty()) {
                // Convert selectionBunch to imageList format
                imageList = new ArrayList<>();
                for (selectionBunchModel bunch : selectionBunch) {
                    if (bunch != null && bunch.getImgUrl() != null) {
                        String imageData = bunch.getFileName() + "|" + bunch.getImgUrl();
                        imageList.add(imageData);
                        android.util.Log.d("MultipleImageScreen", "Added image: " + imageData);
                    }
                }
                currentPosition = 0; // Start from first image
                viewHolderTypeKey = "sender"; // Default to sender
            } else {
                android.util.Log.w("MultipleImageScreen", "No selection bunch found in message model");
                Toast.makeText(this, "No images found in message", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } else {
            // Fallback to old format
            imageList = intent.getStringArrayListExtra("imageList");
            currentPosition = intent.getIntExtra("currentPosition", 0);
            viewHolderTypeKey = intent.getStringExtra("viewHolderTypeKey");
        }
        
        // Log received data
        android.util.Log.d("MultipleImageScreen", "=== FINAL DATA ===");
        android.util.Log.d("MultipleImageScreen", "Image list size: " + (imageList != null ? imageList.size() : 0));
        android.util.Log.d("MultipleImageScreen", "Current position: " + currentPosition);
        android.util.Log.d("MultipleImageScreen", "View holder type key: " + viewHolderTypeKey);
        
        if (imageList == null || imageList.isEmpty()) {
            android.util.Log.w("MultipleImageScreen", "No images found in intent");
            Toast.makeText(this, "No images found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Log each image with file name and URL
        for (int i = 0; i < imageList.size(); i++) {
            String imageData = imageList.get(i);
            android.util.Log.d("MultipleImageScreen", "Image " + i + ": " + imageData);
            
            // Parse bundle data if it contains fileName|URL format
            if (imageData.contains("|")) {
                String[] parts = imageData.split("\\|");
                if (parts.length >= 2) {
                    String fileName = parts[0];
                    String networkUrl = parts[1];
                    android.util.Log.d("MultipleImageScreen", "  → FileName: " + fileName);
                    android.util.Log.d("MultipleImageScreen", "  → NetworkURL: " + networkUrl);
                }
            } else {
                // Single URL or path
                android.util.Log.d("MultipleImageScreen", "  → Single data: " + imageData);
            }
        }
        
        setupRecyclerView();
    }
    
    private void setupRecyclerView() {
        adapter = new MultipleImageAdapter(this, imageList, viewHolderTypeKey);
        
        // Optimize RecyclerView for smooth scrolling
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setInitialPrefetchItemCount(3);
        
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        
        // Scroll to current position
        if (currentPosition >= 0 && currentPosition < imageList.size()) {
            recyclerView.post(() -> {
                recyclerView.scrollToPosition(currentPosition);
            });
        }
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        

    }
    
    private void showMenuOptions() {
        // TODO: Implement menu options like save all, share, etc.
        Toast.makeText(this, "Menu options coming soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        SwipeNavigationHelper.finishWithSwipe(multiple_show_image_screen.this);
        // Apply smooth exit animation
        overridePendingTransition(R.anim.slide_in_from_left_smooth, R.anim.slide_out_to_right_smooth);
    }
    
    // Method to check if file exists locally
    private boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
}
