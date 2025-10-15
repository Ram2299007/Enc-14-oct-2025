// Sample implementation for showing the bottom sheet dialog
// Add this code to your Activity or Fragment

import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;

public class BottomSheetDialogImplementation {
    
    private BottomSheetDialog bottomSheetDialog;
    private Activity activity;
    
    public BottomSheetDialogImplementation(Activity activity) {
        this.activity = activity;
    }
    
    public void showDataBottomSheet() {
        // Create the bottom sheet dialog
        bottomSheetDialog = new BottomSheetDialog(activity, R.style.TransparentBottomSheetDialog);
        
        // Inflate the layout
        View bottomSheetView = LayoutInflater.from(activity)
                .inflate(R.layout.bottom_sheet_data_cardview, null);
        
        // Set the view to the dialog
        bottomSheetDialog.setContentView(bottomSheetView);
        
        // Set up click listeners for the buttons in the bottom sheet
        setupBottomSheetListeners(bottomSheetView);
        
        // Show the dialog
        bottomSheetDialog.show();
    }
    
    private void setupBottomSheetListeners(View bottomSheetView) {
        // Camera button
        bottomSheetView.findViewById(R.id.cameraLyt).setOnClickListener(v -> {
            // Handle camera action
            bottomSheetDialog.dismiss();
        });
        
        // Gallery button
        bottomSheetView.findViewById(R.id.galleryLyt).setOnClickListener(v -> {
            // Handle gallery action
            bottomSheetDialog.dismiss();
        });
        
        // Video button
        bottomSheetView.findViewById(R.id.videoLyt).setOnClickListener(v -> {
            // Handle video action
            bottomSheetDialog.dismiss();
        });
        
        // Document button
        bottomSheetView.findViewById(R.id.documentLyt).setOnClickListener(v -> {
            // Handle document action
            bottomSheetDialog.dismiss();
        });
        
        // Contact button
        bottomSheetView.findViewById(R.id.contactLyt).setOnClickListener(v -> {
            // Handle contact action
            bottomSheetDialog.dismiss();
        });
    }
    
    public void dismissBottomSheet() {
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
    }
}

// In your Activity's onCreate or where you set up the button click listener:
// Add this to your activity where you have the showDataBottomSheetBtn button

/*
// Find the button in your activity
CardView showDataBottomSheetBtn = findViewById(R.id.showDataBottomSheetBtn);

// Create the bottom sheet implementation
BottomSheetDialogImplementation bottomSheetImpl = new BottomSheetDialogImplementation(this);

// Set click listener
showDataBottomSheetBtn.setOnClickListener(v -> {
    bottomSheetImpl.showDataBottomSheet();
});
*/
