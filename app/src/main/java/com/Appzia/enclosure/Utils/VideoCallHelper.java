package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.Model.incomingVideoCallModel;
import com.Appzia.enclosure.Utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Helper class for video call functionality
 * Helps break down large methods to comply with Google Play's 16 KB page size requirement
 */
public class VideoCallHelper {
    private static final String TAG = "VideoCallHelper";

    /**
     * Filters contact list by name
     */
    public static void filterContactsByName(String newText, ArrayList<get_contact_model> contactList, Object adapter) {
        ArrayList<get_contact_model> filteredList = new ArrayList<>();

        for (get_contact_model list : contactList) {
            if (list.getFull_name().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {
            Log.d(TAG, "No contacts found matching name: " + newText);
        } else {
            try {
                // Use reflection to call searchFilteredData method
                java.lang.reflect.Method searchMethod = adapter.getClass().getMethod("searchFilteredData", ArrayList.class);
                searchMethod.invoke(adapter, filteredList);
            } catch (Exception e) {
                Log.e(TAG, "Error calling searchFilteredData: " + e.getMessage());
            }
        }
    }

    /**
     * Filters contact list by phone number
     */
    public static void filterContactsByNumber(String newText, ArrayList<get_contact_model> contactList, Object adapter) {
        ArrayList<get_contact_model> filteredList = new ArrayList<>();

        for (get_contact_model list : contactList) {
            if (list.getMobile_no().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {
            Log.d(TAG, "No contacts found matching number: " + newText);
        } else {
            try {
                // Use reflection to call searchFilteredData method
                java.lang.reflect.Method searchMethod = adapter.getClass().getMethod("searchFilteredData", ArrayList.class);
                searchMethod.invoke(adapter, filteredList);
            } catch (Exception e) {
                Log.e(TAG, "Error calling searchFilteredData: " + e.getMessage());
            }
        }
    }

    /**
     * Creates a meeting with the given token
     */
    public static void createMeeting(Context context, String token, String ftoken, String phone, 
                                   String callerId, String uid, String username, String createdBy, 
                                   boolean incoming, Object adapter) {
        
        // Create meeting API call
        createMeetingApiCall(context, token, ftoken, phone, callerId, uid, username, createdBy, incoming);
    }

    /**
     * Makes the API call to create a meeting
     */
    private static void createMeetingApiCall(Context context, String token, String ftoken, String phone,
                                           String callerId, String uid, String username, String createdBy,
                                           boolean incoming) {
        
        // This would typically use your networking library
        // For now, we'll create a placeholder implementation
        Log.d(TAG, "Creating meeting with token: " + token);
        
        // Create meeting model and save to database
        createMeetingModel(context, ftoken, phone, callerId, uid, username, createdBy, incoming);
    }

    /**
     * Creates and saves the meeting model to database
     */
    private static void createMeetingModel(Context context, String ftoken, String phone, String callerId,
                                         String uid, String username, String createdBy, boolean incoming) {
        
        try {
            // Generate meeting ID (in real implementation, this comes from API response)
            String meetingId = generateMeetingId();
            
            // Create the model
            incomingVideoCallModel model = new incomingVideoCallModel(
                ftoken, 
                Constant.getSF.getString(Constant.full_name, ""), 
                meetingId, 
                phone, 
                Constant.getSF.getString(Constant.profilePic, ""), 
                "", // token placeholder
                uid, 
                callerId, 
                ""
            );

            // Save to Firebase
            saveMeetingToDatabase(model, callerId, context);
            
            // Log meeting creation
            logMeetingCreation(meetingId);
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating meeting model: " + e.getMessage());
            Toast.makeText(context, "Error creating meeting: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Generates a unique meeting ID
     */
    private static String generateMeetingId() {
        return "meeting_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    /**
     * Saves the meeting model to Firebase database
     */
    private static void saveMeetingToDatabase(incomingVideoCallModel model, String callerId, Context context) {
        try {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child(Constant.INCOMING_VIDEO_CALL)
                   .child(callerId.trim())
                   .setValue(model)
                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                           Log.d(TAG, "Meeting saved to database successfully");
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.e(TAG, "Failed to save meeting to database: " + e.getMessage());
                           Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });
        } catch (Exception e) {
            Log.e(TAG, "Error saving meeting to database: " + e.getMessage());
        }
    }

    /**
     * Logs meeting creation details
     */
    private static void logMeetingCreation(String meetingId) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String currentDate = dateFormat.format(date);
            
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            String amPm = (calendar.get(Calendar.AM_PM) == Calendar.AM) ? "am" : "pm";
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
            String currentTime = timeFormat.format(calendar.getTime()) + " " + amPm;

            Log.d(TAG, "Meeting created - Date: " + currentDate + ", Time: " + currentTime + ", ID: " + meetingId);
            
        } catch (Exception e) {
            Log.e(TAG, "Error logging meeting creation: " + e.getMessage());
        }
    }

    /**
     * Handles meeting creation response from API
     */
    public static void handleMeetingResponse(Context context, JSONObject response, String ftoken, 
                                           String phone, String callerId, String uid, String username, 
                                           String createdBy, boolean incoming) {
        
        try {
            String meetingId = response.getString("roomId");
            Log.d(TAG, "Meeting created with ID: " + meetingId);
            
            // Create and save meeting model
            createMeetingModel(context, ftoken, phone, callerId, uid, username, createdBy, incoming);
            
        } catch (Exception e) {
            Log.e(TAG, "Error handling meeting response: " + e.getMessage());
            Toast.makeText(context, "Error creating meeting: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
