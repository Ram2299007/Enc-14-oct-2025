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

public class userinfoofflineadapter extends RecyclerView.Adapter<userinfoofflineadapter.myViewHolder> {

    Context mContext;
    ArrayList<profilestatusModel> profilestatusList = new ArrayList<>();
    public userinfoofflineadapter(Context mContext, ArrayList<profilestatusModel> profilestatusList) {
        this.mContext = mContext;
        this.profilestatusList = profilestatusList;
    }

    @NonNull
    @Override
    public userinfoofflineadapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_status_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userinfoofflineadapter.myViewHolder holder, int position) {

        final profilestatusModel model = profilestatusList.get(position);

        try {
            Picasso.get().load(model.getPhoto()).into(holder.photo);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.inviteimg).into(holder.photo);
        }
        holder.id.setText(model.getId());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final Dialog dialogLayout = new Dialog(holder.itemView.getContext());
//                dialogLayout.setContentView(R.layout.status_view_row);
//                dialogLayout.setCanceledOnTouchOutside(true);
//                dialogLayout.setCancelable(true);
//                dialogLayout.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//                dialogLayout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                dialogLayout.getWindow().setGravity(Gravity.CENTER);
//                ImageView imageView = dialogLayout.findViewById(R.id.showImg);
//                String image = model.getPhoto();
//
//                Glide.with(holder.itemView.getContext()).load(image).into(imageView);
//
//                dialogLayout.show();
//
//                Window window = dialogLayout.getWindow();
//                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Intent intent = new Intent(mContext, show_image_Screen.class);
                intent.putExtra("imageKey", model.getPhoto());
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
