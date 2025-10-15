package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;

public class FirebaseStorageHelper {

    public static void downloadFileToCustomFolder(Context context, String filePath, String folderName, String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(filePath);

        // Get a reference to the external storage directory
        File externalStorage = Environment.getExternalStorageDirectory();

        // Create the custom folder if it doesn't exist
        File customFolder = new File(externalStorage, folderName);
        if (!customFolder.exists()) {
            customFolder.mkdirs();
        }

        File destinationFile = new File(customFolder, fileName);

        storageRef.getFile(destinationFile).addOnSuccessListener(taskSnapshot -> {
            // File downloaded successfully
            Log.d("FirebaseStorage", "File downloaded to custom folder");
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Log.e("FirebaseStorage", "Error downloading file: " + exception.getMessage());
        });
    }
}
