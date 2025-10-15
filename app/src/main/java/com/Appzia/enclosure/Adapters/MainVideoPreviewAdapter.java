package com.Appzia.enclosure.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.ui.PlayerView;
import android.widget.FrameLayout;
import android.view.TextureView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.Appzia.enclosure.R;

import java.util.List;

public class MainVideoPreviewAdapter extends RecyclerView.Adapter<MainVideoPreviewAdapter.VideoViewHolder> {
    
    private Context context;
    private List<Uri> videoUris;
    private OnVideoClickListener onVideoClickListener;
    private ExoPlayer currentPlayingPlayer;
    
    public interface OnVideoClickListener {
        void onVideoClick(int position);
    }
    
    public MainVideoPreviewAdapter(Context context, List<Uri> videoUris) {
        this.context = context;
        this.videoUris = videoUris;
    }
    
    public void setOnVideoClickListener(OnVideoClickListener listener) {
        this.onVideoClickListener = listener;
    }
    
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_video_preview, parent, false);
        return new VideoViewHolder(view);
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Uri videoUri = videoUris.get(position);
        android.util.Log.d("VIDEO_BIND", "Binding video at position " + position + ", URI: " + videoUri);
        
        // Release any existing player in this holder to prevent memory leaks
        if (holder.player != null) {
            android.util.Log.d("VIDEO_BIND", "Releasing existing player for position " + position);
            if (currentPlayingPlayer == holder.player) {
                currentPlayingPlayer = null;
            }
            holder.player.release();
            holder.player = null;
        }
        
        // Create ExoPlayer instance with memory-optimized settings
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(context);
        
        // Configure track selector for better performance and memory usage
        trackSelector.setParameters(
            trackSelector.getParameters()
                .buildUpon()
                .setForceLowestBitrate(true)
                .setMaxVideoSizeSd()
                .setMaxVideoBitrate(500_000) // Limit video bitrate to 500kbps
                .setMaxAudioBitrate(128_000) // Limit audio bitrate to 128kbps
                .build()
        );
        
        // Create ExoPlayer with memory-optimized configuration
        ExoPlayer player = new ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setLoadControl(new androidx.media3.exoplayer.DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    1000,  // Min buffer duration
                    2000,  // Max buffer duration  
                    500,   // Buffer for playback
                    1000   // Buffer for playback after rebuffer
                )
                .setBackBuffer(1000, true) // Keep only 1 second of back buffer
                .build())
            .build();
        
        // Set player to TextureView (with null check)
        if (holder.videoSurface != null) {
            android.util.Log.d("VIDEO_SURFACE", "TextureView found for position " + position + ", URI: " + videoUri);
            
            // Set up TextureView surface listener
            holder.videoSurface.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(android.graphics.SurfaceTexture surface, int width, int height) {
                    android.util.Log.d("VIDEO_SURFACE", "Surface available for position " + position + ": " + width + "x" + height);
                    player.setVideoSurface(new android.view.Surface(surface));
                }
                
                @Override
                public void onSurfaceTextureSizeChanged(android.graphics.SurfaceTexture surface, int width, int height) {
                    android.util.Log.d("VIDEO_SURFACE", "Surface size changed for position " + position + ": " + width + "x" + height);
                    // Apply simple scaling to fit video properly
                    adjustVideoScale(holder, width, height);
                }
                
                @Override
                public boolean onSurfaceTextureDestroyed(android.graphics.SurfaceTexture surface) {
                    android.util.Log.d("VIDEO_SURFACE", "Surface destroyed for position " + position);
                    return false;
                }
                
                @Override
                public void onSurfaceTextureUpdated(android.graphics.SurfaceTexture surface) {
                    // Handle surface updates if needed
                }
            });
            
            // Check if surface is already available
            if (holder.videoSurface.isAvailable()) {
                android.util.Log.d("VIDEO_SURFACE", "Surface already available for position " + position);
                player.setVideoSurface(new android.view.Surface(holder.videoSurface.getSurfaceTexture()));
            }
            
        } else {
            android.util.Log.e("VIDEO_SURFACE", "TextureView is null for position " + position);
            return;
        }
        
        // Create MediaItem and set it to player
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player.setMediaItem(mediaItem);
        // Don't prepare immediately to save memory - prepare only when play is clicked
        player.setPlayWhenReady(false); // Don't auto-play
        
        // Show video thumbnail using Glide - always visible
        if (holder.videoThumbnail != null) {
            android.util.Log.d("VIDEO_THUMBNAIL", "Loading thumbnail for position " + position);
            holder.videoThumbnail.setVisibility(View.VISIBLE);
            holder.videoThumbnail.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
            Glide.with(context)
                .load(videoUri)
                
                .error(R.drawable.inviteimg)
                .centerCrop()
                .into(holder.videoThumbnail);
        } else {
            android.util.Log.e("VIDEO_THUMBNAIL", "Thumbnail view is null for position " + position);
        }
        
        // Set click listener for play/pause button (with null check)
        if (holder.playPauseButton != null) {
            android.util.Log.d("VIDEO_BUTTON", "Setting up play/pause button for position " + position);
            android.util.Log.d("VIDEO_BUTTON", "Button visibility: " + holder.playPauseButton.getVisibility() + 
                ", enabled: " + holder.playPauseButton.isEnabled() + ", clickable: " + holder.playPauseButton.isClickable());
            
            // Make sure button is visible and clickable
            holder.playPauseButton.setVisibility(View.VISIBLE);
            holder.playPauseButton.setEnabled(true);
            holder.playPauseButton.setClickable(true);
            
            // Clear any existing click listeners first
            holder.playPauseButton.setOnClickListener(null);
            holder.playPauseButton.setOnClickListener(v -> {
                android.util.Log.d("VIDEO_PLAY_CLICK", "Play button clicked for position " + position + " - Opening full screen player");
                
                // Open the show_video_playerScreen instead of playing in dialog
                android.content.Intent intent = new android.content.Intent(context, com.Appzia.enclosure.Screens.show_video_playerScreen.class);
                intent.putExtra("videoUri", videoUris.get(position).toString());
                intent.putExtra("viewHolderTypeKey", "multiVideoPreview"); // Identify this as multi-video preview
                context.startActivity(intent);
                
                if (onVideoClickListener != null) {
                    onVideoClickListener.onVideoClick(position);
                }
            });
        } else {
            android.util.Log.e("VIDEO_BUTTON", "Play/pause button is null for position " + position);
        }
        
        // No need for complex player listeners since we're opening full screen player
        // Just keep the player for thumbnail generation
        /*
        player.addListener(new Player.Listener() {
            @Override
            public void onVideoSizeChanged(androidx.media3.common.VideoSize videoSize) {
                android.util.Log.d("VIDEO_SIZE", "Video size changed for position " + position + ": " + videoSize.width + "x" + videoSize.height);
                
                // Only process video size changes for the currently playing video
                if (currentPlayingPlayer != null && currentPlayingPlayer != player) {
                    android.util.Log.d("VIDEO_SIZE", "Skipping video size change for non-playing video at position " + position);
                    return;
                }
                
                // Set the TextureView surface size to match video dimensions to prevent stretching
                if (holder.videoSurface != null && videoSize.width > 0 && videoSize.height > 0) {
                    try {
                        // Set the surface size to match video dimensions
                        holder.videoSurface.getSurfaceTexture().setDefaultBufferSize(videoSize.width, videoSize.height);
                        android.util.Log.d("VIDEO_SIZE", "Set TextureView buffer size to: " + videoSize.width + "x" + videoSize.height);
                        
                        // Force the TextureView to use the correct aspect ratio
                        android.view.ViewGroup.LayoutParams params = holder.videoSurface.getLayoutParams();
                        if (params != null) {
                            // Calculate the correct dimensions to maintain aspect ratio
                            float aspectRatio = (float) videoSize.width / videoSize.height;
                            int surfaceWidth = holder.videoSurface.getWidth();
                            int surfaceHeight = holder.videoSurface.getHeight();
                            
                            if (surfaceWidth > 0 && surfaceHeight > 0) {
                                float surfaceAspectRatio = (float) surfaceWidth / surfaceHeight;
                                
                                if (aspectRatio > surfaceAspectRatio) {
                                    // Video is wider - fit to width
                                    params.height = (int) (surfaceWidth / aspectRatio);
                                } else {
                                    // Video is taller - fit to height  
                                    params.width = (int) (surfaceHeight * aspectRatio);
                                }
                                holder.videoSurface.setLayoutParams(params);
                                android.util.Log.d("VIDEO_SIZE", "Adjusted TextureView size to: " + params.width + "x" + params.height);
                            }
                        }
                    } catch (Exception e) {
                        android.util.Log.e("VIDEO_SIZE", "Error setting buffer size: " + e.getMessage());
                    }
                }
                // Apply scaling when video dimensions are known
                if (holder.videoSurface != null) {
                    adjustVideoScale(holder, holder.videoSurface.getWidth(), holder.videoSurface.getHeight());
                } else {
                    android.util.Log.e("VIDEO_SIZE", "VideoSurface is null when trying to adjust scale for position " + position);
                }
            }
            
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (holder.playPauseButton != null) {
                    if (playbackState == Player.STATE_ENDED) {
                        holder.playPauseButton.setVisibility(View.VISIBLE);
                        holder.playPauseButton.setImageResource(R.drawable.play);
                        // Show thumbnail when video ends
                        if (holder.videoThumbnail != null) {
                            holder.videoThumbnail.setVisibility(View.VISIBLE);
                        }
                        if (currentPlayingPlayer == player) {
                            currentPlayingPlayer = null;
                        }
                    } else if (playbackState == Player.STATE_READY) {
                        // Video is ready to play
                        holder.playPauseButton.setVisibility(View.VISIBLE);
                    }
                }
            }
            
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                android.util.Log.d("VIDEO_PLAYER", "Playback state changed for position " + position + ": isPlaying=" + isPlaying);
                if (holder.playPauseButton != null) {
                    if (isPlaying) {
                        android.util.Log.d("VIDEO_PLAYER", "Video started playing at position " + position);
                        holder.playPauseButton.setImageResource(R.drawable.pause);
                        // Hide thumbnail when playing
                        if (holder.videoThumbnail != null) {
                            holder.videoThumbnail.setVisibility(View.GONE);
                        }
                        currentPlayingPlayer = player;
                        android.util.Log.d("VIDEO_PLAYER", "Set currentPlayingPlayer for position " + position);
                    } else {
                        android.util.Log.d("VIDEO_PLAYER", "Video stopped playing at position " + position);
                        holder.playPauseButton.setImageResource(R.drawable.play);
                        // Show thumbnail when not playing
                        if (holder.videoThumbnail != null) {
                            holder.videoThumbnail.setVisibility(View.VISIBLE);
                        }
                        if (currentPlayingPlayer == player) {
                            android.util.Log.d("VIDEO_PLAYER", "Clearing currentPlayingPlayer for position " + position);
                            currentPlayingPlayer = null;
                        }
                    }
                }
            }
        });
        */
        
        // Store player reference for cleanup
        holder.setPlayer(player);
        
        // Configure TextureView to prevent stretching
        if (holder.videoSurface != null) {
            // Set the TextureView to maintain aspect ratio
            holder.videoSurface.setOpaque(false);
            holder.videoSurface.setKeepScreenOn(false);
            android.util.Log.d("VIDEO_SURFACE", "Configured TextureView for position " + position);
        }
        
        // Add test click listener to entire item view
        holder.itemView.setOnClickListener(v -> {
            android.util.Log.d("VIDEO_ITEM_CLICK", "Item view clicked for position " + position);
        });
    }
    
    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder) {
        super.onViewRecycled(holder);
        android.util.Log.d("VIDEO_RECYCLE", "View recycled for position " + holder.getAdapterPosition());
        // Stop and release player when view is recycled
        if (holder.player != null) {
            android.util.Log.d("VIDEO_RECYCLE", "Releasing player for recycled view");
            if (currentPlayingPlayer == holder.player) {
                android.util.Log.d("VIDEO_RECYCLE", "Clearing currentPlayingPlayer for recycled view");
                currentPlayingPlayer = null;
            }
            // Stop playback before releasing
            try {
                holder.player.stop();
                holder.player.release();
            } catch (Exception e) {
                android.util.Log.e("VIDEO_RECYCLE", "Error releasing player: " + e.getMessage());
            }
            holder.player = null;
        }
    }
    
    // Method to stop all videos
    public void stopAllVideos() {
        android.util.Log.d("VIDEO_CLEANUP", "Stopping all videos");
        if (currentPlayingPlayer != null) {
            android.util.Log.d("VIDEO_CLEANUP", "Pausing current playing player");
            try {
                currentPlayingPlayer.pause();
            } catch (Exception e) {
                android.util.Log.e("VIDEO_CLEANUP", "Error pausing player: " + e.getMessage());
            }
            currentPlayingPlayer = null;
        } else {
            android.util.Log.d("VIDEO_CLEANUP", "No current playing player to stop");
        }
    }
    
    // Method to pause current video
    public void pauseCurrentVideo() {
        android.util.Log.d("VIDEO_CLEANUP", "Pausing current video");
        if (currentPlayingPlayer != null) {
            android.util.Log.d("VIDEO_CLEANUP", "Pausing current playing player");
            try {
                currentPlayingPlayer.pause();
            } catch (Exception e) {
                android.util.Log.e("VIDEO_CLEANUP", "Error pausing player: " + e.getMessage());
            }
        } else {
            android.util.Log.d("VIDEO_CLEANUP", "No current playing player to pause");
        }
    }
    
    // Method to destroy all video players
    public void destroyAllVideos() {
        android.util.Log.d("VIDEO_CLEANUP", "Destroying all videos");
        if (currentPlayingPlayer != null) {
            android.util.Log.d("VIDEO_CLEANUP", "Stopping and releasing current playing player");
            try {
                if (currentPlayingPlayer.getPlaybackState() != Player.STATE_IDLE) {
                    currentPlayingPlayer.stop();
                }
                currentPlayingPlayer.release();
            } catch (Exception e) {
                android.util.Log.e("VIDEO_CLEANUP", "Error destroying player: " + e.getMessage());
            }
            currentPlayingPlayer = null;
        } else {
            android.util.Log.d("VIDEO_CLEANUP", "No current playing player to destroy");
        }
    }
    
    // Method to get current playing player for cleanup
    public ExoPlayer getCurrentPlayingPlayer() {
        return currentPlayingPlayer;
    }
    
    @Override
    public int getItemCount() {
        return videoUris.size();
    }
    
    @Override
    public long getItemId(int position) {
        // Return stable IDs to prevent unnecessary recycling
        return videoUris.get(position).hashCode();
    }
    
    @Override
    public int getItemViewType(int position) {
        // Return different view types to prevent recycling between different videos
        return position;
    }
    
    // Method to clear all videos and release players
    public void clearVideos() {
        // Release all current players
        for (int i = 0; i < getItemCount(); i++) {
            // This will be handled by onViewRecycled when views are recycled
        }
        videoUris.clear();
        notifyDataSetChanged();
    }
    
    // Method to update videos list
    public void updateVideos(List<Uri> newVideoUris) {
        videoUris.clear();
        videoUris.addAll(newVideoUris);
        notifyDataSetChanged();
    }
    
    
    // Video scaling method similar to PlayerView resize_mode="fit"
    private void adjustVideoScale(VideoViewHolder holder, int surfaceWidth, int surfaceHeight) {
        android.util.Log.d("VIDEO_SCALE", "Starting video scale adjustment - Surface: " + surfaceWidth + "x" + surfaceHeight);
        
        if (holder.videoSurface == null || holder.player == null) {
            android.util.Log.e("VIDEO_SCALE", "VideoSurface or player is null - cannot adjust scale");
            return;
        }
        
        // Check if this is the currently playing video to prevent duplicate scaling
        if (currentPlayingPlayer != null && currentPlayingPlayer != holder.player) {
            android.util.Log.d("VIDEO_SCALE", "Skipping scale adjustment for non-playing video");
            return;
        }
        
        try {
            // Get video dimensions from player
            androidx.media3.common.VideoSize videoSize = holder.player.getVideoSize();
            int videoWidth = videoSize.width;
            int videoHeight = videoSize.height;
            
            android.util.Log.d("VIDEO_SCALE", "Video dimensions: " + videoWidth + "x" + videoHeight);
            
            if (videoWidth > 0 && videoHeight > 0 && surfaceWidth > 0 && surfaceHeight > 0) {
                // Calculate scale to fit video within surface (similar to PlayerView resize_mode="fit")
                float scaleX = (float) surfaceWidth / videoWidth;
                float scaleY = (float) surfaceHeight / videoHeight;
                float scale = Math.min(scaleX, scaleY); // Use the smaller scale to fit within bounds
                
                // Calculate the scaled dimensions
                float scaledWidth = videoWidth * scale;
                float scaledHeight = videoHeight * scale;
                
                // Calculate translation to center the video
                float translateX = (surfaceWidth - scaledWidth) / 2f;
                float translateY = (surfaceHeight - scaledHeight) / 2f;
                
                android.util.Log.d("VIDEO_SCALE", "Scale calculation - scaleX: " + scaleX + ", scaleY: " + scaleY + 
                    ", final scale: " + scale + ", scaled: " + scaledWidth + "x" + scaledHeight + 
                    ", translate: (" + translateX + ", " + translateY + ")");
                
                // Apply transformation matrix
                android.graphics.Matrix matrix = new android.graphics.Matrix();
                matrix.setScale(scale, scale);
                matrix.postTranslate(translateX, translateY);
                holder.videoSurface.setTransform(matrix);

                android.util.Log.d("VIDEO_SCALE", "Video scaling applied - Video: " + videoWidth + "x" + videoHeight +
                    ", Surface: " + surfaceWidth + "x" + surfaceHeight + ", Scale: " + scale +
                    ", Translate: (" + translateX + ", " + translateY + ")");
                
            } else {
                android.util.Log.w("VIDEO_SCALE", "Invalid dimensions - Video: " + videoWidth + "x" + videoHeight + 
                    ", Surface: " + surfaceWidth + "x" + surfaceHeight);
            }
        } catch (Exception e) {
            android.util.Log.e("VIDEO_SCALE", "Error scaling video: " + e.getMessage(), e);
        }
    }
    
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        FrameLayout playerView;
        TextureView videoSurface;
        ImageView videoThumbnail;
        ImageView playPauseButton;
        ExoPlayer player;
        
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            videoSurface = itemView.findViewById(R.id.videoSurface);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            playPauseButton = itemView.findViewById(R.id.playPauseButton);
        }
        
        public void setPlayer(ExoPlayer player) {
            this.player = player;
        }
    }
}
