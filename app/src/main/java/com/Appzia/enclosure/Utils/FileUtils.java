package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static File saveBitmapToFile(Context context, Bitmap bitmap, String fileName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getCacheDir(), fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
