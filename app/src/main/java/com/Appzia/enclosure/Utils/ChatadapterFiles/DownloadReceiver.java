package com.Appzia.enclosure.Utils.ChatadapterFiles;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import com.Appzia.enclosure.Model.messageModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DownloadReceiver extends BroadcastReceiver {

    //this receiver for all type of files
    ProgressBar progressBarImageview;
    View blur;
    FloatingActionButton downlaod;
    messageModel model;
    private static long downloadId;
    private static Context mContext;

    public DownloadReceiver(ProgressBar progressBarImageview, View blur, FloatingActionButton downlaod) {
        this.progressBarImageview = progressBarImageview;
        this.blur = blur;
        this.downlaod = downlaod;
    }

    public DownloadReceiver(ProgressBar progressBarImageview, View blur, FloatingActionButton downlaod, messageModel model) {
        this.progressBarImageview = progressBarImageview;
        this.blur = blur;
        this.downlaod = downlaod;
        this.model = model;
    }

    public static void setDownloadId(long id) {
        downloadId = id;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (id == downloadId) {
                progressBarImageview.setVisibility(View.GONE);
                blur.setVisibility(View.GONE);

                // Save to public directory if model is available
                if (model != null) {
                    senderReceiverDownload.saveDownloadedFileToPublicDirectory(model, mContext);
                }
            }
        }
    }
}
