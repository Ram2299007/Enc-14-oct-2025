package com.Appzia.enclosure.Utils.Bottomshit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList; // Import ColorStateList
import android.graphics.Color; // Import Color for parsing hex
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.BroadcastReiciver.AudioPlaybackService;
import com.Appzia.enclosure.Utils.Constant;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import rm.com.audiowave.AudioWaveView;
import android.util.Log;

public class MusicPlayerBottomSheet extends BottomSheetDialogFragment {
    
    public static final String TAG = "MusicPlayerBottomSheet";

    private ImageView playPauseButton;
    private AudioWaveView audioWaveProgressBar;
    private TextView currentTimeTextView;
    private TextView totalTimeTextView;
    private TextView songTitleTextView;

    private CircleImageView profileImageView;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateSeekBarRunnable;
    private String audioUrl;
    private String receiverUid;
    private String profileImageUrl;
    private String songTitle;
    private Animation rotationAnimation;
    private Animation scaleAnimation;
    private String captionKey,nameKey;

    private boolean isPlaying = false;

    private BroadcastReceiver playbackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case AudioPlaybackService.ACTION_PLAY:
                        isPlaying = true;
                        updatePlayPauseButton(true);
                        if (profileImageView != null && rotationAnimation != null) {
                            profileImageView.startAnimation(rotationAnimation);
                        }
                        startSeekBarUpdate();
                        break;
                    case AudioPlaybackService.ACTION_PAUSE:
                        isPlaying = false;
                        updatePlayPauseButton(false);
                        if (profileImageView != null) {
                            profileImageView.clearAnimation();
                        }
                        stopSeekBarUpdate();
                        break;
                    case AudioPlaybackService.ACTION_COMPLETED:
                        isPlaying = false;
                        updatePlayPauseButton(false);
                        if (profileImageView != null) {
                            profileImageView.clearAnimation();
                        }
                        stopSeekBarUpdate();
                        // 💥 Dismiss the bottom sheet
                        if (isAdded() && getDialog() != null && getDialog().isShowing()) {
                            dismissAllowingStateLoss();
                        }
                        break;
                    case AudioPlaybackService.ACTION_UPDATE_PROGRESS:
                        int currentPosition = intent.getIntExtra("currentPosition", 0);
                        int duration = intent.getIntExtra("duration", 0);
                        updateSeekBar(currentPosition, duration);
                        break;
                }
            }
        }
    };

    public static MusicPlayerBottomSheet newInstance(String audioUrl, String profileImageUrl, String songTitle,String friendUidKey,String nameKey,String captionKey) {
        MusicPlayerBottomSheet fragment = new MusicPlayerBottomSheet();
        Bundle args = new Bundle();
        args.putString("audioUrl", audioUrl);
        args.putString("profileImageUrl", profileImageUrl);
        args.putString("songTitle", songTitle);
        args.putString("friendUidKey", friendUidKey);
        args.putString("nameKey", nameKey);
        args.putString("captionKey", captionKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialog2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog2);

        if (getArguments() != null) {
            audioUrl = getArguments().getString("audioUrl", "");
            profileImageUrl = getArguments().getString("profileImageUrl", "");
            songTitle = getArguments().getString("songTitle", "");
            receiverUid = getArguments().getString("friendUidKey", "");
            nameKey = getArguments().getString("nameKey", "");
            captionKey = getArguments().getString("captionKey", "");
        }

        if (getContext() != null) {
            rotationAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_album);
            scaleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_button);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioPlaybackService.ACTION_PLAY);
        filter.addAction(AudioPlaybackService.ACTION_PAUSE);
        filter.addAction(AudioPlaybackService.ACTION_COMPLETED);
        filter.addAction(AudioPlaybackService.ACTION_UPDATE_PROGRESS);
        if (getContext() != null) {
            getContext().registerReceiver(playbackReceiver, filter, Context.RECEIVER_EXPORTED);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheetmusicplayer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playPauseButton = view.findViewById(R.id.btn_play_pause);
        audioWaveProgressBar = view.findViewById(R.id.audio_wave_progress);
        currentTimeTextView = view.findViewById(R.id.tv_current_time);
        totalTimeTextView = view.findViewById(R.id.tv_total_time);
        songTitleTextView = view.findViewById(R.id.tv_song_title);
        profileImageView = view.findViewById(R.id.iv_profile_image);

        songTitleTextView.setText(songTitle.isEmpty() ? "" : songTitle);

        Glide.with(this)
                .load(profileImageUrl)
                
                .error(R.drawable.inviteimg)
                .into(profileImageView);

        // Programmatically set SeekBar tint colors
        // Define your desired hex color
        Constant.getSfFuncion(getContext());
        String hexColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        int color = Color.parseColor(hexColor);
        ColorStateList colorStateList = ColorStateList.valueOf(color);

        // Configure AudioWave View
        // Generate wave data based on duration (will be updated when actual duration is known)
        byte[] waveData = generateWaveDataForDuration(180000); // Default 3 minutes
        audioWaveProgressBar.setRawData(waveData);
        
        // Set max progress to 100 (AudioWaveProgressBar expects 0-100 range)
        try {
            audioWaveProgressBar.getClass().getMethod("setMaxProgress", int.class).invoke(audioWaveProgressBar, 100);
            Log.d(TAG, "Set max progress to 100");
        } catch (Exception e) {
            Log.d(TAG, "setMaxProgress method not available during initialization");
        }
        
        // Set progress color from SharedPreferences
        setProgressColor();
        
        // Force initial refresh
        audioWaveProgressBar.invalidate();
        audioWaveProgressBar.requestLayout();
        
        // Add touch listener for seeking functionality
        audioWaveProgressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    // Calculate the position based on touch X coordinate
                    float touchX = event.getX();
                    float viewWidth = v.getWidth();
                    
                    if (viewWidth > 0 && currentDuration > 0) {
                        // Calculate the percentage of touch position
                        float touchPercentage = touchX / viewWidth;
                        touchPercentage = Math.max(0f, Math.min(1f, touchPercentage)); // Clamp between 0 and 1
                        
                        // Calculate the new position in milliseconds
                        int newPosition = (int) (touchPercentage * currentDuration);
                        
                        Log.d(TAG, "Touch seeking - X: " + touchX + ", Width: " + viewWidth + 
                              ", Percentage: " + (touchPercentage * 100) + "%, New Position: " + newPosition + "ms");
                        
                        // Seek to the new position
                        seekToPosition(newPosition);
                        
                        return true;
                    }
                }
                return false;
            }
        });
        
        Log.d(TAG, "AudioWave initialized with default duration: 180000ms and touch seeking enabled");

        playPauseButton.setOnClickListener(v -> {
            if (scaleAnimation != null) {
                v.startAnimation(scaleAnimation);
            }

            Intent intent = new Intent();
            intent.setAction(isPlaying ? AudioPlaybackService.ACTION_PAUSE : AudioPlaybackService.ACTION_PLAY);
            if (getContext() != null) {
                getContext().sendBroadcast(intent);
            } else {
                Toast.makeText(getContext(), "Context is null", Toast.LENGTH_SHORT).show();
            }
        });

        // SeekBar listener removed - using static wave design

        if (getContext() != null) {
            Intent serviceIntent = new Intent(getContext(), AudioPlaybackService.class);
            serviceIntent.putExtra("audioUrl", audioUrl);
            serviceIntent.putExtra("profileImageUrl", profileImageUrl);
            serviceIntent.putExtra("songTitle", songTitle);
            serviceIntent.putExtra("friendUidKey", receiverUid); // <-- Add this!
            serviceIntent.putExtra("captionKey", captionKey); // <-- Add this!
            serviceIntent.putExtra("nameKey", nameKey); // <-- Add this!
            getContext().startService(serviceIntent);
            isPlaying = true;
            updatePlayPauseButton(true);
            if (profileImageView != null && rotationAnimation != null) {
                profileImageView.startAnimation(rotationAnimation);
            }
            startSeekBarUpdate();
        }
    }

    private void updatePlayPauseButton(boolean playing) {
        playPauseButton.animate()
                .alpha(0f)
                .setDuration(100)
                .withEndAction(() -> {
                    playPauseButton.setImageResource(playing ? R.drawable.pause : R.drawable.play);
                    playPauseButton.animate().alpha(1f).setDuration(100).start();
                })
                .start();
    }

    private void updateSeekBar(int currentPosition, int duration) {
        Log.d(TAG, "updateSeekBar called - currentPosition: " + currentPosition + "ms, duration: " + duration + "ms");
        
        // Update current duration
        if (duration > 0) {
            currentDuration = duration;
            Log.d(TAG, "Updated currentDuration to: " + currentDuration + "ms");
        }
        
        // Update AudioWave View progress
        if (duration > 0) {
            float progress = (float) currentPosition / duration;
            Log.d(TAG, "Calculated progress: " + progress + " (" + (progress * 100) + "%)");
            
            // Set progress with proper bounds checking
            progress = Math.max(0f, Math.min(1f, progress)); // Clamp between 0 and 1
            
            // Use the new comprehensive wave progress update method
            updateWaveProgress(progress);
            
            // Update wave data if duration has changed significantly
            if (Math.abs(duration - lastKnownDuration) > 5000) { // 5 second threshold
                Log.d(TAG, "Duration changed significantly, regenerating wave data");
                byte[] newWaveData = generateWaveDataForDuration(duration);
                audioWaveProgressBar.setRawData(newWaveData);
                lastKnownDuration = duration;
                Log.d(TAG, "Wave data regenerated for duration: " + duration + "ms");
            }
        }
        currentTimeTextView.setText(formatTime(currentPosition));
        totalTimeTextView.setText(formatTime(duration));
    }
    
    private int lastKnownDuration = 0;
    private int currentDuration = 180000; // Default 3 minutes
    
    private void setProgressColor() {
        try {
            Constant.getSfFuncion(getContext());
            String hexColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            int color = Color.parseColor(hexColor);
            
            Log.d(TAG, "Setting AudioWave progress color to: " + hexColor);
            
            // Set the wave color using the theme color
            audioWaveProgressBar.setWaveColor(color);
            
            // Try multiple methods to set progress color
            try {
                // Method 1: setProgressColor
                audioWaveProgressBar.getClass().getMethod("setProgressColor", int.class).invoke(audioWaveProgressBar, color);
                Log.d(TAG, "Used setProgressColor method");
            } catch (Exception e) {
                Log.d(TAG, "setProgressColor method not available");
            }
            
            try {
                // Method 2: setProgressFillColor
                audioWaveProgressBar.getClass().getMethod("setProgressFillColor", int.class).invoke(audioWaveProgressBar, color);
                Log.d(TAG, "Used setProgressFillColor method");
            } catch (Exception e) {
                Log.d(TAG, "setProgressFillColor method not available");
            }
            
            try {
                // Method 3: setFillColor
                audioWaveProgressBar.getClass().getMethod("setFillColor", int.class).invoke(audioWaveProgressBar, color);
                Log.d(TAG, "Used setFillColor method");
            } catch (Exception e) {
                Log.d(TAG, "setFillColor method not available");
            }
            
            try {
                // Method 4: setActiveColor
                audioWaveProgressBar.getClass().getMethod("setActiveColor", int.class).invoke(audioWaveProgressBar, color);
                Log.d(TAG, "Used setActiveColor method");
            } catch (Exception e) {
                Log.d(TAG, "setActiveColor method not available");
            }
            
            Log.d(TAG, "AudioWave progress color set successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting progress color: " + e.getMessage());
        }
    }
    
    private void seekToPosition(int positionMs) {
        try {
            Log.d(TAG, "Seeking to position: " + positionMs + "ms");
            
            // Send broadcast to seek the audio using the correct action and parameter name
            Intent seekIntent = new Intent("com.Appzia.enclosure.ACTION_SEEK");
            seekIntent.putExtra("seekPosition", positionMs);
            getContext().sendBroadcast(seekIntent);
            
            Log.d(TAG, "Seek broadcast sent for position: " + positionMs + "ms with action: com.Appzia.enclosure.ACTION_SEEK");
            
        } catch (Exception e) {
            Log.e(TAG, "Error seeking to position: " + e.getMessage());
        }
    }
    
    private void updateWaveProgress(float progress) {
        try {
            Log.d(TAG, "Attempting to update wave progress to: " + progress);
            
            // Convert progress from 0-1 range to 0-100 range (AudioWaveProgressBar expects 0-100)
            int progressPercent = (int) (progress * 100);
            Log.d(TAG, "Converted progress to percentage: " + progressPercent + "%");
            
            // Method 1: Set max progress first (if not already set)
            try {
                audioWaveProgressBar.getClass().getMethod("setMaxProgress", int.class).invoke(audioWaveProgressBar, 100);
                Log.d(TAG, "Set max progress to 100");
            } catch (Exception e) {
                Log.d(TAG, "setMaxProgress method not available");
            }
            
            // Method 2: Set progress with percentage value
            audioWaveProgressBar.setProgress(progressPercent);
            Log.d(TAG, "Set progress to: " + progressPercent + "%");
            
            // Method 3: Try alternative progress setting methods
            try {
                // Try setCurrentProgress with percentage
                audioWaveProgressBar.getClass().getMethod("setCurrentProgress", int.class).invoke(audioWaveProgressBar, progressPercent);
                Log.d(TAG, "Used setCurrentProgress with percentage: " + progressPercent);
            } catch (Exception e) {
                Log.d(TAG, "setCurrentProgress method not available");
            }
            
            // Method 4: Force refresh
            audioWaveProgressBar.invalidate();
            audioWaveProgressBar.requestLayout();
            
            // Method 5: Post delayed refresh
            audioWaveProgressBar.post(new Runnable() {
                @Override
                public void run() {
                    audioWaveProgressBar.invalidate();
                    audioWaveProgressBar.requestLayout();
                }
            });
            
            Log.d(TAG, "Wave progress update completed - progress: " + progressPercent + "%");
            
        } catch (Exception e) {
            Log.e(TAG, "Error updating wave progress: " + e.getMessage());
        }
    }
    
    private byte[] generateWaveDataForDuration(int durationMs) {
        Log.d(TAG, "Generating wave data for duration: " + durationMs + "ms");
        
        // Generate wave data proportional to duration
        int dataLength = Math.max(1000, durationMs / 10); // 1 data point per 10ms
        byte[] data = new byte[dataLength];
        
        Log.d(TAG, "Wave data length: " + dataLength + " bytes");
        
        for (int i = 0; i < dataLength; i++) {
            // Create more realistic wave patterns
            double frequency = 0.02 + (i % 100) * 0.001; // Varying frequency
            double amplitude = 30 + Math.sin(i * 0.01) * 20; // Varying amplitude
            data[i] = (byte) (Math.sin(i * frequency) * amplitude + Math.random() * 10);
        }
        
        Log.d(TAG, "Wave data generation completed");
        return data;
    }

    private void startSeekBarUpdate() {
        stopSeekBarUpdate();
        updateSeekBarRunnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100); // Update every 100ms for smoother progress
            }
        };
        handler.post(updateSeekBarRunnable);
    }

    private void stopSeekBarUpdate() {
        if (updateSeekBarRunnable != null) {
            handler.removeCallbacks(updateSeekBarRunnable);
        }
    }

    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopSeekBarUpdate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopSeekBarUpdate();
        if (profileImageView != null) {
            profileImageView.clearAnimation();
        }
        if (getContext() != null) {
            try {
                getContext().unregisterReceiver(playbackReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        handler.removeCallbacksAndMessages(null);
    }
}
