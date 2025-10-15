package com.Appzia.enclosure.activities;

import android.content.Context;
import android.util.Log;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

public class DummyCameraHelper {
    private ProcessCameraProvider cameraProvider;

    public DummyCameraHelper(Context context, PreviewView previewView) {
        ProcessCameraProvider.getInstance(context).addListener(() -> {
            try {
                cameraProvider = ProcessCameraProvider.getInstance(context).get();
                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle((androidx.lifecycle.LifecycleOwner) context, cameraSelector, preview);
            } catch (Exception e) {
                Log.e("DummyCameraHelper", "Error starting dummy camera: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(context));
    }

    public void stopCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }
}
