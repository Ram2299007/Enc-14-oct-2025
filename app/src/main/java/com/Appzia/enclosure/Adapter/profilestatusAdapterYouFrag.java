package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.profilestatusModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class profilestatusAdapterYouFrag extends RecyclerView.Adapter<profilestatusAdapterYouFrag.myViewHolder> {

    Context mContext;
    ArrayList<profilestatusModel> profilestatusList = new ArrayList<>();

    public profilestatusAdapterYouFrag(Context mContext, ArrayList<profilestatusModel> profilestatusList) {
        this.mContext = mContext;
        this.profilestatusList = profilestatusList;
    }

    @NonNull
    @Override
    public profilestatusAdapterYouFrag.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_status_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull profilestatusAdapterYouFrag.myViewHolder holder, int position) {

        final profilestatusModel model = profilestatusList.get(position);

        Picasso.get().load(model.getPhoto()).into(holder.photo);
        holder.photo.setTag(model.getPhoto());
        holder.id.setText(model.getId());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext, show_image_Screen.class);
                intent.putExtra("imageKey", holder.photo.getTag().toString());
                intent.putExtra("youKey", "youKey");
                mContext.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return profilestatusList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView id;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            id = itemView.findViewById(R.id.id);
        }
    }

    public void removeItem(int position) {
        profilestatusList.remove(position);
        notifyDataSetChanged();

    }
}
