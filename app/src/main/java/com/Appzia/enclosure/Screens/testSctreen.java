package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.os.PowerManager;

import android.widget.TextView;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.databinding.ActivityTestSctreenBinding;


public class testSctreen extends AppCompatActivity implements SensorEventListener {
    ActivityTestSctreenBinding binding;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private TextView proximityText;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    @Override
    protected void onResume() {
        super.onResume();
        if (mProximity != null) {
            mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestSctreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        proximityText = findViewById(R.id.proximity_text);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        if (mProximity == null) {
            proximityText.setText("Proximity sensor not available");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < mProximity.getMaximumRange()) {
                // Object is near, turn off the screen
                proximityText.setText("Object is near - Screen Off");
                turnScreenOff();
            } else {
                // Object is far, turn on the screen
                proximityText.setText("Object is far - Screen On");
                turnScreenOn();
            }
        }

    }

    private void turnScreenOff() {
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "MyApp::ProximityScreenOff");
        }
        wakeLock.acquire(10*60*1000L /*10 minutes*/); // Acquire the wake lock for 10 minutes
    }


    private void turnScreenOn() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}