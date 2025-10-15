package com.Appzia.enclosure.Utils; // Ensure this package path is correct for your project

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

public class AudioWaveformView extends View {

    private Paint waveformPaint;
    private byte[] amplitudes; // Holds the audio amplitude data

    // Dimensions for drawing bars
    private float barWidth;
    private float gap;

    // Constructors
    public AudioWaveformView(Context context) {
        super(context);
        init();
    }

    public AudioWaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioWaveformView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        waveformPaint = new Paint();
        waveformPaint.setColor(Color.parseColor("#00FFFF")); // Futuristic Cyan color for the waveform bars
        waveformPaint.setStrokeWidth(3f); // Thickness of each bar
        waveformPaint.setAntiAlias(true); // Smooths the edges of the drawn lines
        waveformPaint.setStrokeCap(Paint.Cap.ROUND); // Makes the ends of the bars rounded

        // Initialize with an empty or zeroed array.
        // It will be updated by the Visualizer from the MusicPlayerBottomSheet.
        amplitudes = new byte[64]; // Default size. The Visualizer's capture size will dictate the actual data length.
        Arrays.fill(amplitudes, (byte) 0); // Fill with zeros so the waveform is flat initially
        // (no bars drawn until audio data is received)

        barWidth = waveformPaint.getStrokeWidth(); // Bar width is the same as stroke width
        gap = 5f; // Gap between individual bars
    }

    /**
     * Updates the waveform data and requests a redraw.
     * This method is designed to be called by the Android Visualizer's
     * OnDataCaptureListener when new audio waveform data is available.
     *
     * @param newAmplitudes The new byte array of audio amplitude data (typically from Visualizer).
     */
    public void updateWaveform(byte[] newAmplitudes) {
        if (newAmplitudes == null || newAmplitudes.length == 0) {
            // If no data, or data is empty, clear the waveform to make it flat.
            clearWaveform();
            return;
        }

        // Ensure the internal amplitudes array has the correct capacity.
        // If the size changes (e.g., Visualizer capture size is dynamic), reallocate.
        if (this.amplitudes.length != newAmplitudes.length) {
            this.amplitudes = new byte[newAmplitudes.length];
        }
        // Copy the new amplitude data into the internal array.
        // Using System.arraycopy is efficient for byte arrays.
        System.arraycopy(newAmplitudes, 0, this.amplitudes, 0, newAmplitudes.length);

        // Request a redraw of the view on the UI thread.
        // This is crucial for making the waveform animation visible.
        postInvalidate();
    }

    /**
     * Clears the waveform, making it flat.
     * This should be called when audio playback pauses, stops, or completes,
     * to visually indicate that no audio is currently playing.
     */
    public void clearWaveform() {
        if (amplitudes != null) {
            Arrays.fill(amplitudes, (byte) 0); // Fill the array with zeros
            postInvalidate(); // Request a redraw to show the flat waveform
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Do not draw anything if amplitudes data is null or empty.
        if (amplitudes == null || amplitudes.length == 0) {
            return;
        }

        float centerY = getHeight() / 2f; // The vertical center of the view
        // Maximum height a single waveform bar can reach.
        // Multiplied by 0.45f to leave some padding at the top/bottom.
        float maxBarHeight = getHeight() * 0.45f;

        // Calculate how many bars can be drawn across the width of the view,
        // considering the bar width and the gap between them.
        int totalBarsToDraw = (int) (getWidth() / (barWidth + gap));

        // Iterate to draw each individual bar of the waveform.
        for (int i = 0; i < totalBarsToDraw; i++) {
            // Map the current bar's position (i) to an index in the 'amplitudes' array.
            // This distributes the available amplitude data evenly across the view's width.
            int amplitudeIndex = (int) ((float) i / totalBarsToDraw * amplitudes.length);

            // Safety check to prevent Array Index Out Of Bounds exceptions,
            // though the mapping should generally prevent this if 'amplitudes' is populated correctly.
            if (amplitudeIndex >= amplitudes.length) {
                break;
            }

            // Get the amplitude value for the current bar.
            // Visualizer data is typically signed byte values (-128 to 127).
            // Math.abs() converts it to a positive value.
            // Dividing by 128f normalizes it to a float between 0.0 and 1.0.
            float amplitude = Math.abs((float) amplitudes[amplitudeIndex] / 128f);

            // Calculate the actual height of the bar based on the normalized amplitude
            // and the maximum allowed bar height.
            float barHeight = maxBarHeight * amplitude;

            // Calculate the X-position for the center of the current bar.
            float x = i * (barWidth + gap) + (barWidth / 2);

            // Draw the line for the current bar.
            // We draw from centerY - half_bar_height to centerY + half_bar_height
            // to make the waveform symmetric around the center line.
            canvas.drawLine(x, centerY - barHeight / 2, x, centerY + barHeight / 2, waveformPaint);
        }
    }

    // IMPORTANT: The simulation logic (onAttachedToWindow and onDetachedFromWindow with updateRunnable)
    // has been REMOVED from this class. The waveform will now only update when
    // updateWaveform() is explicitly called from an external source (like the Visualizer).
}