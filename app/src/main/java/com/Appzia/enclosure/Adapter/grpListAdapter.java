package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.get_user_active_contact_list_MessageLmt_Model;
import com.Appzia.enclosure.Model.grp_list_child_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.grpChattingScreen;
import com.Appzia.enclosure.Screens.show_image_Screen;

import com.Appzia.enclosure.Utils.BlurHelper;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.crypto.SecretKey;

public class grpListAdapter extends RecyclerView.Adapter<grpListAdapter.myViewHolder> {

    Context mContext;
    ArrayList<grp_list_child_model> data = new ArrayList<>();

    private SecretKey key;

    public grpListAdapter(Context mContext, ArrayList<grp_list_child_model> data) {
        this.mContext = mContext;
        this.data = data;

        try {


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeItem(int adapterPosition) {
        try {
            data.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        } catch (Exception e) {

        }

    }
    @NonNull
    @Override
    public grpListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.grp_chat_group_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull grpListAdapter.myViewHolder holder, int position) {
        //   Collections.reverse(data);
        final grp_list_child_model model = data.get(position);
        try {
            Picasso.get().load(model.getGroup_icon()).into(holder.grp_icon);
        } catch (Exception ignored) {
            Picasso.get().load(R.drawable.inviteimg).into(holder.grp_icon);
        }
        holder.grpName.setText(model.getGroup_name());
        try {

            if (model.getDec_flg() == 1) {
                holder.time.setText(model.getSent_time());
            } else {
                holder.time.setText(model.getSent_time());
            }


        } catch (Exception e) {
        }

        holder.caption.setSingleLine(true);


        try {

            String dataType = model.getData_type();
            //   Log.d("dataTypecxsacwecwa", "dataType: " + dataType + " ::::: " + model.getFull_name());
            if (model.getL_msg().equals("")) {
                if (dataType.equals(Constant.img)) {

//                    // Get the drawable you want to set as left drawable
                    Drawable leftDrawable = ContextCompat.getDrawable(mContext, R.drawable.baseline_insert_photo_24);
                    // Set bounds for the drawable (adjust dimensions as needed)
                    leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());

                    // Set the left drawable on the TextView
                    holder.caption.setCompoundDrawables(leftDrawable, null, null, null);

                    holder.caption.setText("Photo");
                } else if (dataType.equals(Constant.video)) {

                    // Get the drawable you want to set as left drawable
                    Drawable leftDrawable = ContextCompat.getDrawable(mContext, R.drawable.video_icon);
                    // Set bounds for the drawable (adjust dimensions as needed)
                    leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());

                    // Set the left drawable on the TextView
                    holder.caption.setCompoundDrawables(leftDrawable, null, null, null);

                    holder.caption.setText("Video");
                } else if (dataType.equals(Constant.voiceAudio)) {
                    // Get the drawable you want to set as left drawable
                    Drawable leftDrawable = ContextCompat.getDrawable(mContext, R.drawable.micegray);
                    // Set bounds for the drawable (adjust dimensions as needed)
                    leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());

                    // Set the left drawable on the TextView
                    holder.caption.setCompoundDrawables(leftDrawable, null, null, null);

                    holder.caption.setText("Audio");

                } else if (dataType.equals(Constant.contact)) {
                    // Get the drawable you want to set as left drawable
                    Drawable leftDrawable = ContextCompat.getDrawable(mContext, R.drawable.contact_24);
                    // Set bounds for the drawable (adjust dimensions as needed)
                    leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());

                    // Set the left drawable on the TextView
                    holder.caption.setCompoundDrawables(leftDrawable, null, null, null);
                    holder.caption.setText("Contact");
                } else if (dataType.equals(Constant.Text)) {

                    holder.caption.setCompoundDrawables(null, null, null, null);

                } else {


//                    // Get the drawable you want to set as left drawable
                    Drawable leftDrawable = ContextCompat.getDrawable(mContext, R.drawable.document_24);
                    // Set bounds for the drawable (adjust dimensions as needed)
                    leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());

                    // Set the left drawable on the TextView
                    holder.caption.setCompoundDrawables(leftDrawable, null, null, null);
                    holder.caption.setText("File");
                }
            } else {
                holder.caption.setCompoundDrawables(null, null, null, null);

                Log.d("TAG", "UnknownType2 " + dataType);
                holder.caption.setText(model.getL_msg());

                if (holder.caption.getText().toString().length() > 25) {
                    String truncatedText = holder.caption.getText().toString().substring(0, 25) + "..."; // Add dots at the end
                    holder.caption.setText(truncatedText);
                } else {
                    holder.caption.setText(holder.caption.getText().toString());
                }
            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }


        if (holder.grpName.getText().toString().length() > 20) {
            String truncatedText = holder.grpName.getText().toString().substring(0, 20) + "..."; // Add dots at the end
            holder.grpName.setText(truncatedText);
        } else {
            holder.grpName.setText(holder.grpName.getText().toString());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, grpChattingScreen.class);
                intent.putExtra("nameKey", model.getGroup_name());
                intent.putExtra("grpIdKey", model.getGroup_id());
                intent.putExtra("createdBy", model.getGroup_created_by());
                mContext.startActivity(intent);
                //

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // todo this is why preview hold on a exact location " v "

                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float touchX = location[0];
                float touchY = location[1];

// Show dialog
                BlurHelper.showDialogWithBlurBackground(mContext, R.layout.grp_chat_group_row_blur_dialogue);
                BlurHelper.dialogLayoutColor.show();

// Get the RelativeLayout inside the dialog
                RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);

// Use boundary-aware positioning
                BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);

                androidx.appcompat.widget.AppCompatImageView contact1img;
                TextView contact1text, time, notiCount;
                TextView captiontext;
                LinearLayout contact1, notiBack;
                CardView delete;
                ImageView grp_icon=BlurHelper.dialogLayoutColor.findViewById(R.id.grp_icon);
                TextView grpName=BlurHelper.dialogLayoutColor.findViewById(R.id.grpName);
                TextView caption=BlurHelper.dialogLayoutColor.findViewById(R.id.caption);
                TextView time2=BlurHelper.dialogLayoutColor.findViewById(R.id.time);


                try {
                    Picasso.get().load(model.getGroup_icon()).into(grp_icon);
                } catch (Exception ignored) {
                    Picasso.get().load(R.drawable.inviteimg).into(grp_icon);
                }
                grpName.setText(model.getGroup_name());
                try {

                    if (model.getDec_flg() == 1) {
                        time2.setText(model.getSent_time());
                    } else {
                        time2.setText(model.getSent_time());
                    }


                } catch (Exception e) {
                }

                try {
                    caption.setText(model.getL_msg());
                } catch (Exception e) {
                    caption.setText(model.getL_msg());
                }
                caption.setSingleLine(true);


                if (grpName.getText().toString().length() > 20) {
                    String truncatedText = grpName.getText().toString().substring(0, 20) + "..."; // Add dots at the end
                    grpName.setText(truncatedText);
                } else {
                    grpName.setText(grpName.getText().toString());
                }

                delete = BlurHelper.dialogLayoutColor.findViewById(R.id.deletecardview);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Constant.getSfFuncion(mContext);

                        Webservice.delete_groupp(mContext,model.getGroup_id(),grpListAdapter.this,holder.getAdapterPosition(),BlurHelper.dialogLayoutColor);


                    }
                });
                return true;
            }
        });

        holder.grp_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext, show_image_Screen.class);
                intent.putExtra("imageKey", model.getGroup_icon());
                intent.putExtra("grpKey", "grpKey");
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void searchFilteredData(ArrayList<grp_list_child_model> filteredList) {
        this.data = filteredList;
        notifyDataSetChanged();
    }
    public static class myViewHolder extends RecyclerView.ViewHolder {

        ImageView grp_icon;
        TextView grpName;
        TextView caption;
        TextView time;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            grp_icon = itemView.findViewById(R.id.grp_icon);
            grpName = itemView.findViewById(R.id.grpName);
            time = itemView.findViewById(R.id.time);
            caption = itemView.findViewById(R.id.caption);

        }
    }
}
