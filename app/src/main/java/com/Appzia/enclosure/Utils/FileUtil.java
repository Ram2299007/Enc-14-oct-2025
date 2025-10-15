package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;

public class FileUtil {

    // Method to get a File object from the cache directory
    public static File getFileFromCache(Context context, String fileName) {
        if (context == null || fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Context or file name must not be null or empty.");
        }

        File cacheDir = context.getCacheDir();
        return new File(cacheDir, fileName);
    }

    // Method to get a Uri for the file
    public static Uri getFileUri(Context context, String fileName) {
        File file = getFileFromCache(context, fileName);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist.");
        }
        return FileProvider.getUriForFile(context, context.getPackageName(), file);
    }
}
