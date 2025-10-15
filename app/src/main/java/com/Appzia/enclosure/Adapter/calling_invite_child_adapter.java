package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.get_contact_invite_model;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.R;


import java.util.ArrayList;

public class calling_invite_child_adapter extends RecyclerView.Adapter<calling_invite_child_adapter.myviewholder> {

    Context mContext;
    ArrayList<get_contact_invite_model> get_contact_invite_model_List = new ArrayList<>();



    public calling_invite_child_adapter(Context mContext, ArrayList<get_contact_invite_model> get_contact_invite_model_List) {
        this.mContext = mContext;
        this.get_contact_invite_model_List = get_contact_invite_model_List;

    }

    @NonNull
    @Override
    public calling_invite_child_adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_row, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull calling_invite_child_adapter.myviewholder holder, int position) {

        final get_contact_invite_model model = get_contact_invite_model_List.get(position);



        holder.userName.setText(model.getContact_name());

        if (holder.userName.getText().toString().length() > 35) {
            String truncatedText = holder.userName.getText().toString().substring(0, 35) + "..."; // Add dots at the end
            holder.userName.setText(truncatedText);
        } else {
            holder.userName.setText(holder.userName.getText().toString());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = model.getContact_number();
                String message = "Inviting you to Download Enclosure ! \n" +
                        "Your message will become more valuable here \n" +
                        "New messaging app -\n" +
                        "for billion people https://www.enclosureapp.com";
                Uri uri = Uri.parse("smsto:" + phoneNumber);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                sendIntent.putExtra("sms_body", message);
                // sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(sendIntent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return get_contact_invite_model_List.size();
    }

    public void searchFilteredData(ArrayList<get_contact_invite_model> filteredList) {
        this.get_contact_invite_model_List = filteredList;
        notifyDataSetChanged();
    }
    public static class myviewholder extends RecyclerView.ViewHolder {
        TextView userName, userInvite;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userInvite = itemView.findViewById(R.id.userInvite);
        }
    }
}
