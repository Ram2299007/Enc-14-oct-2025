package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.Emoji;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class emojiAdapterGrp extends RecyclerView.Adapter<emojiAdapterGrp.myViewHolder> {
    Context mContext;
    List<Emoji> emojis;
    List<Emoji> filteredEmojis;
    EditText messageBox;
    private boolean isHorizontalLayout = false;

    public emojiAdapterGrp(Context mContext, List<Emoji> emojis, EditText messageBox) {
        this.mContext = mContext;
        this.emojis = emojis;
        this.filteredEmojis = new ArrayList<>(emojis);
        this.messageBox = messageBox;
    }

    @NonNull
    @Override
    public emojiAdapterGrp.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.emoji_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull emojiAdapterGrp.myViewHolder holder, int position) {

        final Emoji model = filteredEmojis.get(position);
        holder.textview.setText(model.getCharacter());
        
        // Set width based on layout type
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (isHorizontalLayout) {
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        holder.itemView.setLayoutParams(params);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }

                String currentText = messageBox.getText().toString();
                int cursorPosition = messageBox.getSelectionStart();
                String newText = model.getCharacter();
                String updatedText = currentText.substring(0, cursorPosition)
                        + newText
                        + currentText.substring(cursorPosition);
                messageBox.setText(updatedText);
                messageBox.setSelection(cursorPosition + newText.length());
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredEmojis.size();
    }
    
    public void filterEmojis(String searchText) {
        filteredEmojis.clear();
        if (searchText.isEmpty()) {
            filteredEmojis.addAll(emojis);
        } else {
            String searchLower = searchText.toLowerCase();
            for (Emoji emoji : emojis) {
                if (emoji.getUnicodeName().toLowerCase().contains(searchLower) ||
                    emoji.getSlug().toLowerCase().contains(searchLower)) {
                    filteredEmojis.add(emoji);
                }
            }
        }
        notifyDataSetChanged();
    }
    
    public void showAllEmojis() {
        filteredEmojis.clear();
        filteredEmojis.addAll(emojis);
        notifyDataSetChanged();
    }
    
    public void setHorizontalLayout(boolean isHorizontal) {
        this.isHorizontalLayout = isHorizontal;
        notifyDataSetChanged();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView textview;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textview = itemView.findViewById(R.id.textview);
        }
    }
}
