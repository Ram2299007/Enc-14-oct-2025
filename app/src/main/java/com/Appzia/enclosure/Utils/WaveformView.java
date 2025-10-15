package com.Appzia.enclosure.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF; // For drawing rounded rectangles
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.Appzia.enclosure.R;

public class WaveformView extends View {

    private byte[] waveformBytes;
    private Paint playedPaint;   // Paint for the played portion
    private Paint unplayedPaint; // Paint for the unplayed portion
    private float barWidth;
    private float barSpacing;
    private float waveformHeight;
    private int currentPlaybackProgress = 0; // 0 to 100, representing percentage played

    // Customizable attributes (defined in attrs.xml)
    private int playedColor;
    private int unplayedColor;
    private float barCornerRadius;
    private float defaultBarWidth = 4f; // Default width of a single bar
    private float defaultBarSpacing = 2f; // Default spacing between bars

    public WaveformView(Context context) {
        super(context);
        init(null);
    }

    public WaveformView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WaveformView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        // Default colors
        playedColor = Color.GREEN;
        unplayedColor = Color.parseColor("#80FFFFFF"); // Lighter transparent white for unplayed
        barCornerRadius = 2f; // Default rounded corners for bars

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.WaveformView,
                    0, 0);
            try {
                playedColor = a.getColor(R.styleable.WaveformView_playedWaveColor, Color.GREEN);
                unplayedColor = a.getColor(R.styleable.WaveformView_unplayedWaveColor, Color.parseColor("#80FFFFFF"));
                barCornerRadius = a.getDimension(R.styleable.WaveformView_barCornerRadius, 2f);
                defaultBarWidth = a.getDimension(R.styleable.WaveformView_barWidth, 4f);
                defaultBarSpacing = a.getDimension(R.styleable.WaveformView_barSpacing, 2f);
            } finally {
                a.recycle();
            }
        }

        playedPaint = new Paint();
        playedPaint.setColor(playedColor);
        playedPaint.setStyle(Paint.Style.FILL); // Fill the bars
        playedPaint.setAntiAlias(true);

        unplayedPaint = new Paint();
        unplayedPaint.setColor(unplayedColor);
        unplayedPaint.setStyle(Paint.Style.FILL); // Fill the bars
        unplayedPaint.setAntiAlias(true);

        // Pre-calculate bar dimensions (can be dynamic based on view width)
        // For a fixed number of bars like WhatsApp, we need to calculate this once
        // Let's assume a target number of bars, say 60-80 for a typical chat message width.
        // We will make the number of bars dynamic based on the view width.
        // Or, you can pre-process your audio into 64 or 128 "amplitude" values
        // like WhatsApp's API suggests (if you want exactly like them).
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        waveformHeight = h;
        // Dynamically calculate bar dimensions to fill the width
        // Adjust these values to get the desired number of bars and spacing
        // A common approach is to fix bar width and calculate spacing, or vice versa.
        // Let's aim for a certain number of visible bars across the screen.
        int totalBarsToShow = (int) (w / (defaultBarWidth + defaultBarSpacing));
        barWidth = defaultBarWidth;
        barSpacing = (w - (totalBarsToShow * barWidth)) / (totalBarsToShow - 1);
        if (barSpacing < 0) barSpacing = 0; // Avoid negative spacing

        // If you want a fixed number of bars (e.g., 64 like WhatsApp's API),
        // you would pre-process your audio into 64 amplitude values and then
        // calculate barWidth and barSpacing based on 64 bars filling the width.
        // For now, we're using raw Visualizer data which is variable length.
    }

    public void updateWaveform(byte[] waveformBytes) {
        this.waveformBytes = waveformBytes;
        // Invalidate the view to trigger a redraw
        invalidate();
    }

    /**
     * Updates the playback progress for the waveform fill.
     * @param progress Percentage played (0-100).
     */
    public void setPlaybackProgress(int progress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        this.currentPlaybackProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (waveformBytes == null || waveformBytes.length == 0 || getWidth() == 0 || getHeight() == 0) {
            // Draw a flat line or default bars if no data
            drawPlaceholderWaveform(canvas);
            return;
        }

        // The Visualizer provides a variable number of samples (e.g., 128, 256, 512)
        // We need to map these to our fixed number of bars to draw.
        // Simple averaging for downsampling
        int numBarsToDraw = (int) (getWidth() / (barWidth + barSpacing));
        if (numBarsToDraw <= 0) return;

        // Calculate the percentage of the waveform that has been played
        float playedRatio = currentPlaybackProgress / 100f;

        for (int i = 0; i < numBarsToDraw; i++) {
            // Map the current bar index to a range in the waveformBytes array
            int startIndex = (int) (i * (float) waveformBytes.length / numBarsToDraw);
            int endIndex = (int) ((i + 1) * (float) waveformBytes.length / numBarsToDraw);
            if (endIndex >= waveformBytes.length) endIndex = waveformBytes.length - 1;

            // Calculate average amplitude for this segment
            float sumAmplitude = 0;
            int count = 0;
            for (int j = startIndex; j < endIndex; j++) {
                // Waveform data is signed 8-bit (-128 to 127). We need absolute amplitude.
                sumAmplitude += Math.abs(waveformBytes[j]);
                count++;
            }
            float averageAmplitude = (count > 0) ? sumAmplitude / count : 0;

            // Scale amplitude to a bar height
            // Max amplitude is 127. Scale it to waveformHeight.
            float barHeight = (averageAmplitude / 128f) * waveformHeight * 0.8f; // 0.8 to give some padding at top/bottom
            if (barHeight < 2f) barHeight = 2f; // Minimum bar height for very quiet or silent parts

            float x = i * (barWidth + barSpacing);
            float top = (waveformHeight - barHeight) / 2f; // Center vertically
            float bottom = (waveformHeight + barHeight) / 2f;

            // Determine which paint to use based on playback progress
            float playbackX = playedRatio * getWidth();
            Paint currentBarPaint = (x + barWidth) <= playbackX ? playedPaint : unplayedPaint;

            // Draw the rounded rectangle bar
            canvas.drawRoundRect(new RectF(x, top, x + barWidth, bottom), barCornerRadius, barCornerRadius, currentBarPaint);
        }
        // Optional: Draw a small circular "playback head" at the current position
        // float playbackCircleX = playedRatio * getWidth();
        // float playbackCircleRadius = barWidth / 2f + 2f; // Slightly larger than bar width
        // canvas.drawCircle(playbackCircleX, waveformHeight / 2f, playbackCircleRadius, playedPaint);
    }

    private void drawPlaceholderWaveform(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();

        // Draw a few default bars to represent an empty/unplayed waveform
        // This is a simplified version, you can make it more complex for better aesthetics
        int numBarsToDraw = (int) (width / (defaultBarWidth + defaultBarSpacing));
        if (numBarsToDraw <= 0) numBarsToDraw = 20; // Fallback for very small views

        barWidth = defaultBarWidth;
        barSpacing = (width - (numBarsToDraw * barWidth)) / (numBarsToDraw - 1);
        if (barSpacing < 0) barSpacing = 0;


        for (int i = 0; i < numBarsToDraw; i++) {
            // Draw a small, consistent height for placeholder
            float barHeight = height * 0.1f; // 10% of total height, or fixed like 5dp
            float x = i * (barWidth + barSpacing);
            float top = (height - barHeight) / 2f;
            float bottom = (height + barHeight) / 2f;
            canvas.drawRoundRect(new RectF(x, top, x + barWidth, bottom), barCornerRadius, barCornerRadius, unplayedPaint);
        }
    }
}