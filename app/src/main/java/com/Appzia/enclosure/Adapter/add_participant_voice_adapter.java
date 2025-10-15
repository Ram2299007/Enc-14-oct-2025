package com.Appzia.enclosure.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.Model.incomingVideoCallModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.FcmNotificationsSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class add_participant_voice_adapter extends RecyclerView.Adapter<add_participant_voice_adapter.myViewHolder> {
    Context mContext;
    LinearLayout bottomcaller;
    TextView mainname;
    String phone, photo, ftoken, callerId, uid;
    Dialog dialogLayoutColor;
    String meetingId, sampleToken;

    Activity activity;

    FirebaseDatabase database;
    ArrayList<get_contact_model> get_calling_contact_list;

    public add_participant_voice_adapter(Context mContext, Dialog dialogLayoutColor, String meetingId, Activity activity, String sampleToken, String phone, String ftoken, String uid, ArrayList<get_contact_model> get_calling_contact_list) {
        this.mContext = mContext;
        this.dialogLayoutColor = dialogLayoutColor;
        this.meetingId = meetingId;
        this.activity = activity;
        this.sampleToken = sampleToken;
        this.phone = phone;
        this.ftoken = ftoken;
        this.uid = uid;
        this.get_calling_contact_list = get_calling_contact_list;
    }

    @NonNull
    @Override
    public add_participant_voice_adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.get_voice_calling_contact_list_row, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull add_participant_voice_adapter.myViewHolder holder, int position) {

        final get_contact_model model = get_calling_contact_list.get(position);

        Picasso.get().load(model.getPhoto()).into(holder.img);
        holder.name.setText(model.getFull_name());

        database = FirebaseDatabase.getInstance();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.getSfFuncion(mContext);
               // final incomingVideoCallModel model = new incomingVideoCallModel(binding.ftoken.getText().toString(), Constant.getSF.getString(Constant.full_name, ""), meetingId, binding.phone.getText().toString(), Constant.getSF.getString(Constant.profilePic, ""), token, uid, binding.callerId.getText().toString(), "");

                final incomingVideoCallModel model2 = new incomingVideoCallModel(ftoken, Constant.getSF.getString(Constant.full_name, ""), meetingId, phone, model.getPhoto(), sampleToken, uid, model.getUid(), model.getUid());

                Log.d("photoLog", model.getPhoto());
                database.getReference().child(Constant.INCOMING_VOICE_CALL).child(model.getUid()).setValue(model2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //  Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                      //  Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Constant.getSfFuncion(mContext);
                String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");


                if (sleepKey.equals(Constant.sleepKey)) {

                }else {
//
//                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(model.getF_token(), "Enclosure", Constant.voicecall, mContext, activity, model.getFull_name(), meetingId, model.getMobile_no(), model.getPhoto(), sampleToken, Constant.getSF.getString(Constant.UID_KEY, ""), model.getUid(),model.getDevice_type(), "username", "createdBy", "incoming");
//                    notificationsSender.SendNotifications();
                    dialogLayoutColor.dismiss();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return get_calling_contact_list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
        }
    }
}
