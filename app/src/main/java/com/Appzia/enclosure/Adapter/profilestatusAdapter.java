package com.Appzia.enclosure.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.profilestatusModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Utils.BlurHelper;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class profilestatusAdapter extends RecyclerView.Adapter<profilestatusAdapter.myViewHolder> {

    Context mContext;
    ArrayList<profilestatusModel> profilestatusList = new ArrayList<>();

    public profilestatusAdapter(Context mContext, ArrayList<profilestatusModel> profilestatusList) {
        this.mContext = mContext;
        this.profilestatusList = profilestatusList;
    }

    public void removeItem(int adapterPosition) {
        try {
            profilestatusList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        } catch (Exception e) {

        }

    }

    @NonNull
    @Override
    public profilestatusAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_status_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull profilestatusAdapter.myViewHolder holder, int position) {

        final profilestatusModel model = profilestatusList.get(position);

        Picasso.get().load(model.getPhoto()).into(holder.photo);
        holder.id.setText(model.getId());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, show_image_Screen.class);
                intent.putExtra("imageKey", model.getPhoto());
                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Get touch coordinates
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float touchX = location[0];
                float touchY = location[1];

                BlurHelper.showDialogWithBlurBackground(mContext, R.layout.status_blur);
                BlurHelper.dialogLayoutColor.show();
                RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);
                
                // Use boundary-aware positioning
                BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);

                ImageView photo;
                TextView id;
                photo = BlurHelper.dialogLayoutColor.findViewById(R.id.photo);
                id = BlurHelper.dialogLayoutColor.findViewById(R.id.id);
                Picasso.get().load(model.getPhoto()).into(photo);
                id.setText(model.getId());

                CardView delete;
                delete = BlurHelper.dialogLayoutColor.findViewById(R.id.deletecardview);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Webservice.delete_user_single_status_image(mContext, Constant.getSF.getString(Constant.UID_KEY, ""), holder.id.getText().toString(), holder.getAdapterPosition(), BlurHelper.dialogLayoutColor, profilestatusAdapter.this);

                    }
                });


                return true;
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
        CardView cardview;
        AppCompatButton ok, cancel;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            id = itemView.findViewById(R.id.id);
            cardview = itemView.findViewById(R.id.cardview);
            ok = itemView.findViewById(R.id.ok);
            cancel = itemView.findViewById(R.id.cancel);
        }
    }

}
