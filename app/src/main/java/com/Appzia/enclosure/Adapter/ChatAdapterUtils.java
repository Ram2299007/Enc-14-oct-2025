package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.Appzia.enclosure.Model.messageModel;

import java.io.File;

class ChatAdapterUtils {
    static String getFilePath(Context context, messageModel model) {
        File customFolder;
        String exactPath;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            customFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
            exactPath = customFolder.getAbsolutePath();
        } else {
            customFolder = new File(context.getExternalFilesDir(null), "Enclosure/Media/Documents");
            exactPath = customFolder.getAbsolutePath();
        }
        return exactPath + "/" + model.getFileName();
    }

    static String getLocalPdfPreviewImagePath(Context context, messageModel model) {
        File customFolder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            customFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents/Previews");
        } else {
            customFolder = new File(context.getExternalFilesDir(null), "Enclosure/Media/Documents/Previews");
        }
        if (!customFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            customFolder.mkdirs();
        }
        String exactPath = customFolder.getAbsolutePath();
        return exactPath + "/" + model.getFileName() + ".png";
    }

    static String getRemotePdfUrl(messageModel model) {
        return model.getDocument();
    }
}


