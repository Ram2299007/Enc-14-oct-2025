package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.viewholders.ReceiverViewHolder;
import com.Appzia.enclosure.Adapter.viewholders.SenderViewHolder;
import com.Appzia.enclosure.Model.messageModel;

/**
 * Thin delegate to reduce chatAdapter size. Real logic remains in chatAdapter for now;
 * gradually move code blocks into here.
 */
class ChatMessageBinder {
    static void bind(Context context, RecyclerView.ViewHolder holder, messageModel model, int viewType, chatAdapter adapter) {
        if (viewType == adapter.SENDER_VIEW_TYPE && holder instanceof SenderViewHolder) {
            // Placeholder for future extraction; keep behavior unchanged for now
            // adapter.bindSender((SenderViewHolder) holder, model);
        } else if (viewType == adapter.RECEIVER_VIEW_TYPE && holder instanceof ReceiverViewHolder) {
            // Placeholder for future extraction; keep behavior unchanged for now
            // adapter.bindReceiver((ReceiverViewHolder) holder, model);
        }
    }
}


