package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.Emoji;
import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.BlurHelper;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class emojiAdapterChatAdapter extends RecyclerView.Adapter<emojiAdapterChatAdapter.myViewHolder> {
    Context mContext;
    List<Emoji> emojis;

    boolean triggered = true;
    String modelId;
    String receiverUid;
    String userFTokenKey;
    String emojiCount;
    ArrayList<emojiModel> emoji;
    messageModel messageModel;
    FirebaseDatabase database;


    public emojiAdapterChatAdapter(Context mContext, List<Emoji> emojis, String modelId, String receiverUid, String emojiCount, ArrayList<emojiModel> emoji, messageModel messageModel, String userFTokenKey) {
        this.mContext = mContext;
        this.emojis = emojis;
        this.modelId = modelId;
        this.receiverUid = receiverUid;
        this.emojiCount = emojiCount;
        this.emoji = emoji;
        this.messageModel = messageModel;
        this.userFTokenKey = userFTokenKey;

    }

    @NonNull
    @Override
    public emojiAdapterChatAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.emoji_adapter_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull emojiAdapterChatAdapter.myViewHolder holder, int position) {
        Constant.getSfFuncion(mContext);
        String senderUid = Constant.getSF.getString(Constant.UID_KEY, "");
        String receiverRoom = receiverUid + senderUid;
        database = FirebaseDatabase.getInstance();

        final Emoji model = emojis.get(position);

        holder.textview.setText(model.getCharacter());

        // Adjust emoji size
        int customSize = model.getUnicodeName().equals("") ? 25 : 40;
        int sizeInPx = (int) (customSize * mContext.getResources().getDisplayMetrics().density);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(sizeInPx, sizeInPx);
        holder.emojiSvg.setLayoutParams(params);

        // Always reset background (default state)
        holder.emojiSvg.setBackgroundResource(R.drawable.custome_ripple_circle);

        // Make sure modelId exists
        if (modelId != null && !emojiCount.isEmpty()) {

            // Use stable ID to track holder
            String currentCharacter = model.getCharacter();
            holder.itemView.setTag(currentCharacter);

            DatabaseReference emojiRef = database.getReference()
                    .child(Constant.CHAT)
                    .child(receiverRoom)
                    .child(modelId)
                    .child("emojiModel");

            // ⚡ Better: use cached data (e.g. Map<String, emojiModel>)
            emojiRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Make sure holder is still bound to same emoji
                    if (!currentCharacter.equals(holder.itemView.getTag())) {
                        return; // holder was recycled, ignore
                    }

                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            emojiModel existingEmoji = ds.getValue(emojiModel.class);

                            if (existingEmoji != null &&
                                    existingEmoji.getEmoji().equals(currentCharacter)) {

                                String nameFromDatabase = existingEmoji.getName();
                                String currentUserId = Constant.getSF.getString(Constant.UID_KEY, "");

                                if (nameFromDatabase != null && nameFromDatabase.equals(currentUserId)) {
                                    holder.emojiSvg.setBackgroundResource(R.drawable.color_circle);
                                } else {
                                    holder.emojiSvg.setBackgroundResource(R.drawable.custome_ripple_circle);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(mContext);
                }
                Constant.getSfFuncion(mContext);
                String senderUid = Constant.getSF.getString(Constant.UID_KEY, "");
                String receiverRoom = receiverUid + senderUid;
                String senderRoom = senderUid + receiverUid;


                if (holder.emojiSvg.getBackground() != null &&
                        holder.emojiSvg.getBackground().getConstantState() ==
                                ContextCompat.getDrawable(mContext, R.drawable.color_circle).getConstantState()) {


                    // TODO: 04/02/25 delete emoji
                    if (BlurHelper.dialogLayoutColor != null) {
                        BlurHelper.dialogLayoutColor.dismiss();
                    }

                    // Only sender will remove their own emoji or delete
                    DatabaseReference emojiRef = database.getReference()
                            .child(Constant.CHAT)
                            .child(receiverRoom)
                            .child(modelId)
                            .child("emojiModel");

                    emojiRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<emojiModel> emojiList = new ArrayList<>();
                            String currentUserId = Constant.getSF.getString(Constant.UID_KEY, "");
                            String emojiKey = currentUserId + "_" + model.getCharacter(); // Create unique key

                            boolean isEmojiRemoved = false;

                            if (snapshot.exists()) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    emojiModel existingEmoji = ds.getValue(emojiModel.class);
                                    if (existingEmoji != null) {
                                        String existingKey = existingEmoji.getName() + "_" + existingEmoji.getEmoji();
                                        if (existingKey.equals(emojiKey)) {
                                            // Mark for removal
                                            isEmojiRemoved = true;
                                            continue; // Skip adding this emoji to the list
                                        } else {
                                            emojiList.add(existingEmoji);  // Keep the others

                                        }
                                    }
                                }
                            }

                            // If the current user's emoji was removed, update the database
                            if (isEmojiRemoved) {
                                // Update the emoji count after removal
                                String emojiCountStr = String.valueOf(emojiList.size());

                                DatabaseReference receiverRef = database.getReference()
                                        .child(Constant.CHAT)
                                        .child(receiverRoom)
                                        .child(modelId)
                                        .child("emojiModel");

                                DatabaseReference senderRef = database.getReference()
                                        .child(Constant.CHAT)
                                        .child(senderRoom)
                                        .child(modelId)
                                        .child("emojiModel");

                                if (emojiList.isEmpty()) {
                                    // Instead of deleting, insert an empty emojiModel
                                    ArrayList<emojiModel> emptyEmojiList = new ArrayList<>();
                                    emptyEmojiList.add(new emojiModel("", "")); // Insert empty emojiModel

                                    receiverRef.setValue(emptyEmojiList);
                                    senderRef.setValue(emptyEmojiList);
                                } else {
                                    receiverRef.setValue(emojiList);
                                    senderRef.setValue(emojiList);
                                }

                                // Update emoji count
                                if (emojiCountStr.equals("0")) {
                                    database.getReference().child(Constant.CHAT).child(receiverRoom).child(modelId).child("emojiCount").setValue("");
                                    database.getReference().child(Constant.CHAT).child(senderRoom).child(modelId).child("emojiCount").setValue("");
                                } else {
                                    database.getReference().child(Constant.CHAT).child(receiverRoom).child(modelId).child("emojiCount").setValue(emojiCountStr);
                                    database.getReference().child(Constant.CHAT).child(senderRoom).child(modelId).child("emojiCount").setValue(emojiCountStr);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle possible error
                        }
                    });

                } else {
                    /// add here emoji
                    // Data uploading here perfect now
                    if (BlurHelper.dialogLayoutColor != null) {
                        BlurHelper.dialogLayoutColor.dismiss();
                    }

                    DatabaseReference emojiRef = database.getReference()
                            .child(Constant.CHAT)
                            .child(receiverRoom)
                            .child(modelId)
                            .child("emojiModel");

                    emojiRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, emojiModel> emojiMap = new HashMap<>();
                            boolean isUpdated = false;
                            String currentUserId = Constant.getSF.getString(Constant.UID_KEY, "");
                            String newEmoji = model.getCharacter();

                            // Ensure that the name is always unique and non-empty
                            if (currentUserId.isEmpty()) return;

                            // Load old data
                            if (snapshot.exists()) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    emojiModel existingEmoji = ds.getValue(emojiModel.class);
                                    if (existingEmoji != null && !existingEmoji.getName().isEmpty()) {
                                        if (existingEmoji.getName().equals(currentUserId)) {
                                            // Update existing emoji for the same name
                                            existingEmoji.setEmoji(newEmoji);
                                            isUpdated = true;
                                        }
                                        emojiMap.put(existingEmoji.getName(), existingEmoji);
                                    }
                                }
                            }

                            // If not updated, add new entry
                            if (!isUpdated) {
                                emojiMap.put(currentUserId, new emojiModel(currentUserId, newEmoji));
                            }

                            // Update database only if changes are made
                            if (!emojiMap.isEmpty()) {
                                emojiRef.setValue(new ArrayList<>(emojiMap.values())).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        String emojiCountStr = String.valueOf(emojiMap.size());

                                        //  Toast.makeText(mContext, "triggered", Toast.LENGTH_SHORT).show();
                                        database.getReference().child(Constant.CHAT).child(receiverRoom).child(modelId).child("emojiCount").setValue(emojiCountStr);
                                        database.getReference().child(Constant.CHAT).child(senderRoom).child(modelId).child("emojiModel").setValue(new ArrayList<>(emojiMap.values()));
                                        database.getReference().child(Constant.CHAT).child(senderRoom).child(modelId).child("emojiCount").setValue(emojiCountStr);



                                        // here need to send notification
                                        File dummy =null;
                                        Date d = new Date();
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                        String currentDateTimeString = sdf.format(d);
                                        Webservice.create_individual_chattingForEmojiReact(mContext, senderUid, receiverUid, model.getCharacter(), dummy, messageModel.getDataType(), messageModel.getExtension(), messageModel.getName(), messageModel.getPhone(), "", messageModel.getMiceTiming(), currentDateTimeString, senderRoom, receiverRoom, messageModel, modelId, Constant.getSF.getString(Constant.full_name, ""), database, userFTokenKey, "1");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return emojis.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView textview;
        LinearLayout emojiSvg;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textview = itemView.findViewById(R.id.textview);
            emojiSvg = itemView.findViewById(R.id.emojiSvg);
        }
    }
}
