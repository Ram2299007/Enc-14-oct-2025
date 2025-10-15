package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Centralized storage path helper to ensure consistent file storage paths
 * across all screens in the Enclosure app
 */
public class StoragePathHelper {
    private static final String TAG = "StoragePathHelper";
    
    // Base path constants
    private static final String ENCLOSURE_MEDIA_BASE = "Enclosure/Media";
    private static final String ENCLOSURE_MEDIA_IMAGES = ENCLOSURE_MEDIA_BASE + "/Images";
    private static final String ENCLOSURE_MEDIA_VIDEOS = ENCLOSURE_MEDIA_BASE + "/Videos";
    private static final String ENCLOSURE_MEDIA_DOCUMENTS = ENCLOSURE_MEDIA_BASE + "/Documents";
    private static final String ENCLOSURE_MEDIA_THUMBNAILS = ENCLOSURE_MEDIA_BASE + "/Thumbnail";
    private static final String ENCLOSURE_MEDIA_CONTACTS = ENCLOSURE_MEDIA_BASE + "/Contacts";
    private static final String ENCLOSURE_MEDIA_TEXT = ENCLOSURE_MEDIA_BASE + "/Text";
    private static final String ENCLOSURE_MEDIA_AUDIOS = ENCLOSURE_MEDIA_BASE + "/Audios";

    /**
     * Get external storage directory with proper Android version handling
     * Uses Environment.DIRECTORY_DOCUMENTS for consistent storage location
     */
    private static File getExternalStorageDir(Context context, String type, String subPath) {
        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dir = new File(context.getExternalFilesDir(type), subPath);
        } else {
            dir = new File(context.getExternalFilesDir(null), subPath);
        }
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            Log.d(TAG, "Created directory " + dir.getAbsolutePath() + ": " + created);
        }
        return dir;
    }

    /**
     * Get localStorage path for Images - consistent across all screens
     * Path: Environment.DIRECTORY_DOCUMENTS/Enclosure/Media/Images
     */
    public static File getImagesStoragePath(Context context) {
        return getExternalStorageDir(context, Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_IMAGES);
    }

    /**
     * Get localStorage path for Videos - consistent across all screens
     * Path: Environment.DIRECTORY_DOCUMENTS/Enclosure/Media/Videos
     */
    public static File getVideosStoragePath(Context context) {
        return getExternalStorageDir(context, Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_VIDEOS);
    }

    /**
     * Get localStorage path for Documents - consistent across all screens
     * Path: Environment.DIRECTORY_DOCUMENTS/Enclosure/Media/Documents
     */
    public static File getDocumentsStoragePath(Context context) {
        return getExternalStorageDir(context, Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_DOCUMENTS);
    }

    /**
     * Get localStorage path for Thumbnails - consistent across all screens
     * Path: Environment.DIRECTORY_PICTURES/Enclosure/Media/Thumbnail
     */
    public static File getThumbnailsStoragePath(Context context) {
        return getExternalStorageDir(context, Environment.DIRECTORY_PICTURES, ENCLOSURE_MEDIA_THUMBNAILS);
    }

    /**
     * Get localStorage path for Contacts - consistent across all screens
     * Path: Environment.DIRECTORY_DOCUMENTS/Enclosure/Media/Contacts
     */
    public static File getContactsStoragePath(Context context) {
        return getExternalStorageDir(context, Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_CONTACTS);
    }

    /**
     * Get localStorage path for Text files - consistent across all screens
     * Path: Environment.DIRECTORY_DOCUMENTS/Enclosure/Media/Text
     */
    public static File getTextStoragePath(Context context) {
        return getExternalStorageDir(context, Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_TEXT);
    }

    /**
     * Get localStorage path for Audio files - consistent across all screens
     * Path: Environment.DIRECTORY_DOCUMENTS/Enclosure/Media/Audios
     */
    public static File getAudiosStoragePath(Context context) {
        return getExternalStorageDir(context, Environment.DIRECTORY_DOCUMENTS, ENCLOSURE_MEDIA_AUDIOS);
    }

    /**
     * Initialize all storage directories to ensure they exist
     * Call this method during app initialization
     */
    public static void initializeAllStorageDirectories(Context context) {
        Log.d(TAG, "Initializing all storage directories");
        
        // Ensure all storage directories exist
        getImagesStoragePath(context);
        getVideosStoragePath(context);
        getDocumentsStoragePath(context);
        getThumbnailsStoragePath(context);
        getContactsStoragePath(context);
        getTextStoragePath(context);
        getAudiosStoragePath(context);
        
        Log.d(TAG, "All storage directories initialized");
    }

    /**
     * Get the absolute path string for images storage
     * @param context Application context
     * @return Absolute path string for images storage
     */
    public static String getImagesStoragePathString(Context context) {
        return getImagesStoragePath(context).getAbsolutePath();
    }

    /**
     * Get the absolute path string for videos storage
     * @param context Application context
     * @return Absolute path string for videos storage
     */
    public static String getVideosStoragePathString(Context context) {
        return getVideosStoragePath(context).getAbsolutePath();
    }

    /**
     * Get the absolute path string for documents storage
     * @param context Application context
     * @return Absolute path string for documents storage
     */
    public static String getDocumentsStoragePathString(Context context) {
        return getDocumentsStoragePath(context).getAbsolutePath();
    }
}
