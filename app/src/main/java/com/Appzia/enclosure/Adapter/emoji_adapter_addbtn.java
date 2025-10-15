package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.Emoji;
import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.BlurHelper;
import com.Appzia.enclosure.Utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class emoji_adapter_addbtn extends RecyclerView.Adapter<emoji_adapter_addbtn.myViewHolder> {
    Context mContext;
    List<Emoji> emojis;
    String modelId;
    String receiverUid;
    FirebaseDatabase database;

    public emoji_adapter_addbtn(Context mContext, List<Emoji> emojis, String modelId, String receiverUid) {
        this.mContext = mContext;
        this.emojis = emojis;
        this.modelId = modelId;
        this.receiverUid = receiverUid;
    }

    @NonNull
    @Override
    public emoji_adapter_addbtn.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.emoji_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull emoji_adapter_addbtn.myViewHolder holder, int position) {

        final Emoji model = emojis.get(position);
        database = FirebaseDatabase.getInstance();
        holder.textview.setText(model.getCharacter());

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

                if (Constant.bottomSheetDialog != null) {
                    Constant.bottomSheetDialog.dismiss();

                    if (BlurHelper.dialogLayoutColor != null) {
                        BlurHelper.dialogLayoutColor.dismiss();
                    }
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
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle cancellation error
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return emojis.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView textview;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textview = itemView.findViewById(R.id.textview);
        }
    }
}
