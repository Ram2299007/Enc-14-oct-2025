package com.Appzia.enclosure.Adapter.diff;

import androidx.recyclerview.widget.DiffUtil;

import com.Appzia.enclosure.Model.messageModel;

import java.util.List;

public class MessageDiffCallback extends DiffUtil.Callback {
    private final List<messageModel> mOldList;
    private final List<messageModel> mNewList;

    public MessageDiffCallback(List<messageModel> oldList, List<messageModel> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        String oldKey = keyFor(mOldList.get(oldItemPosition));
        String newKey = keyFor(mNewList.get(newItemPosition));
        return oldKey.equals(newKey);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Avoid heavy equals; compare key content fields that affect rendering
        com.Appzia.enclosure.Model.messageModel oldM = mOldList.get(oldItemPosition);
        com.Appzia.enclosure.Model.messageModel newM = mNewList.get(newItemPosition);

        if (oldM.getTimestamp() != newM.getTimestamp()) return false;
        if (!safeEquals(oldM.getMessage(), newM.getMessage())) return false;
        if (!safeEquals(oldM.getDocument(), newM.getDocument())) return false;
        if (!safeEquals(oldM.getDataType(), newM.getDataType())) return false;
        if (!safeEquals(oldM.getEmojiCount(), newM.getEmojiCount())) return false;
        if (!safeEquals(oldM.getThumbnail(), newM.getThumbnail())) return false;
        if (!safeEquals(oldM.getReplyKey(), newM.getReplyKey())) return false;
        return true;
    }

    private String keyFor(messageModel m) {
        String modelId = m.getModelId();
        if (modelId != null && !modelId.isEmpty()) return modelId;
        String uid = m.getUid() != null ? m.getUid() : "";
        long ts = m.getTimestamp();
        String msg = m.getMessage() != null ? m.getMessage() : (m.getDocument() != null ? m.getDocument() : "");
        return uid + "_" + ts + "_" + Integer.toHexString(msg.hashCode());
    }

    private static boolean safeEquals(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}


