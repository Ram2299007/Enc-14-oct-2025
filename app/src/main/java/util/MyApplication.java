package util;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;
import com.Appzia.enclosure.Utils.ImageLoadingOptimizer;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "AudioPlaybackChannel";
    @Override
    public void onCreate() {

        super.onCreate();

        FirebaseApp.initializeApp(this);

        // Enable Firebase persistence for offline data storage
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Log.d("MyApplication", "Firebase persistence enabled successfully");
        } catch (Exception e) {
            Log.e("MyApplication", "Failed to enable Firebase persistence: " + e.getMessage());
        }

        createNotificationChannel();

        // ✅ Initialize image loading optimizations for better chat performance
        ImageLoadingOptimizer.configureGlide(this);

        // Configure WebView globally to prevent native camera access on Android 15+
        configureWebViewGlobally();

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Audio Playback",
                    NotificationManager.IMPORTANCE_LOW // Use LOW to avoid sound/vibration
            );
            channel.setDescription("Channel for audio playback notifications");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            } else {
                // Log error if NotificationManager is null
                android.util.Log.e("MyApplication", "Failed to get NotificationManager");
            }
        }
    }

    /**
     * Configure WebView globally to prevent native camera access on Android 15+
     */
    private void configureWebViewGlobally() {
        try {
            Log.d("MyApplication", "Configuring WebView globally to prevent native camera access");

            // Android 15+ specific WebView configurations
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                Log.d("MyApplication", "Applying Android 15+ specific WebView configurations");

                // Force WebView to use its own camera implementation
                try {
                    // Use reflection to access Android 15+ specific WebView methods
                    java.lang.reflect.Method setDataDirectorySuffix =
                            WebView.class.getMethod("setDataDirectorySuffix", String.class);
                    if (setDataDirectorySuffix != null) {
                        setDataDirectorySuffix.invoke(null, "webview_secure");
                        Log.d("MyApplication", "Set WebView data directory suffix to prevent native camera access");
                    }
                } catch (Exception e) {
                    Log.d("MyApplication", "Could not set WebView data directory suffix: " + e.getMessage());
                }

                // Disable WebView debugging that might trigger native camera access
                try {
                    WebView.setWebContentsDebuggingEnabled(false);
                    Log.d("MyApplication", "Disabled WebView debugging to prevent native camera access");
                } catch (Exception e) {
                    Log.d("MyApplication", "Could not disable WebView debugging: " + e.getMessage());
                }
            }

            Log.d("MyApplication", "WebView global configuration completed");

        } catch (Exception e) {
            Log.e("MyApplication", "Error configuring WebView globally: " + e.getMessage(), e);
        }
    }
}
