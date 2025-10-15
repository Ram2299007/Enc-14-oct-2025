package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.unableDeliveredUserMdoel;
import com.Appzia.enclosure.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class unableDeliveredUserAdaptyer extends RecyclerView.Adapter<unableDeliveredUserAdaptyer.myViewHolder> {

    Context mContext;
    ArrayList<unableDeliveredUserMdoel> list = new ArrayList<>();

    public unableDeliveredUserAdaptyer(Context mContext, ArrayList<unableDeliveredUserMdoel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public unableDeliveredUserAdaptyer.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.message_lmt_user_row, null);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull unableDeliveredUserAdaptyer.myViewHolder holder, int position) {

        unableDeliveredUserMdoel model = list.get(position);
        try {
            Picasso.get().load(model.getP_img()).into(holder.contact1img);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.inviteimg).into(holder.contact1img);
        }

        holder.contact1text.setText(model.getFull_name());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView contact1img;
        TextView contact1text;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            contact1img = itemView.findViewById(R.id.contact1img);
            contact1text = itemView.findViewById(R.id.contact1text);
        }
    }
}
