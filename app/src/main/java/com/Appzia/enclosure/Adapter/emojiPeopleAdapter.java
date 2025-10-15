package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class emojiPeopleAdapter extends RecyclerView.Adapter<emojiPeopleAdapter.EmojiViewHolder> {
    private Context context;
    private ArrayList<emojiModel> emojiList;
    String receiverUid;

    public emojiPeopleAdapter(Context context, ArrayList<emojiModel> emojiList, String receiverUid) {
        this.context = context;
        this.emojiList = emojiList;
        this.receiverUid = receiverUid;
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.emoji_people_row, parent, false);
        return new EmojiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        emojiModel emoji = emojiList.get(position);

        if (emoji.getName().equals(Constant.getSF.getString(Constant.UID_KEY, ""))) {
            holder.contact1text.setText(Constant.getSF.getString(Constant.full_name, ""));
            try {
                Picasso.get().load(Constant.getSF.getString(Constant.profilePic, "")).into(holder.contact1img);
            } catch (Exception e) {

            }
        }else{
            Webservice.get_profile_UserInfoEmoji(context, receiverUid,holder.contact1text,holder.contact1img);
        }
        holder.emojiTxt.setText(emoji.getEmoji());  // Assuming emojiModel has `getEmoji()`
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    public static class EmojiViewHolder extends RecyclerView.ViewHolder {
        TextView contact1text;
        TextView emojiTxt;
        ImageView contact1img;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            contact1text = itemView.findViewById(R.id.contact1text);
            emojiTxt = itemView.findViewById(R.id.emojiTxt);
            contact1img = itemView.findViewById(R.id.contact1img);
        }
    }
}
