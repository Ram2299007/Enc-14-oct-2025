package com.Appzia.enclosure.Utils;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class UnfoldAnimation extends Animation {
    private final int mWidth;
    private final int mHeight;

    public UnfoldAnimation(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float angle = interpolatedTime * 90;
        t.getMatrix().setRotate(angle, 0, mHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
