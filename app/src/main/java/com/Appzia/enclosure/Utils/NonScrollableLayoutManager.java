package com.Appzia.enclosure.Utils;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NonScrollableLayoutManager extends LinearLayoutManager {
    public NonScrollableLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false; // Disable vertical scrolling
    }
}
