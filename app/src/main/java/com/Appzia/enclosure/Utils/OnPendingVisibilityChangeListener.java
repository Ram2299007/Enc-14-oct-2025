package com.Appzia.enclosure.Utils;

import androidx.recyclerview.widget.RecyclerView;

public interface OnPendingVisibilityChangeListener {
    void onPendingVisibilityChange(RecyclerView.ViewHolder holder, boolean isVisible);
}
