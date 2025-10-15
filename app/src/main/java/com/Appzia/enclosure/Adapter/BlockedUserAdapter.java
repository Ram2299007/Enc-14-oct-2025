package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.BlockedUserModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.block_contact_activity;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.bumptech.glide.Glide;

import java.util.List;

public class BlockedUserAdapter extends RecyclerView.Adapter<BlockedUserAdapter.ViewHolder> {

    private Context context;
    private List<BlockedUserModel> blockedUserList;
    block_contact_activity blockContactActivity;

    public BlockedUserAdapter(Context context, List<BlockedUserModel> blockedUserList, block_contact_activity blockContactActivity) {
        this.context = context;
        this.blockedUserList = blockedUserList;
        this.blockContactActivity = blockContactActivity;
    }

    @Override
    public BlockedUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blocked_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BlockedUserAdapter.ViewHolder holder, int position) {
        BlockedUserModel user = blockedUserList.get(position);
        holder.textFullName.setText(user.getFullName());

        Glide.with(context)
            .load(user.getPhoto())
            
            .into(holder.imageProfile);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.dialogueLayout(context,R.layout.unblock_layout);
                TextView TextView = Constant.dialogLayoutColor.findViewById(R.id.TextView);
                TextView.setText("Unblock "+user.getFullName());
                Constant.dialogLayoutColor.show();

                TextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            Constant.Vibrator(context);
                        }
                        Constant.getSfFuncion(context);
                        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                        Webservice.unblockUserSetting(context, uid, user.getUid());
                        Constant.dialogLayoutColor.dismiss();
                        blockContactActivity.Initializes();
                    }
                });
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Constant.dialogueLayout(context,R.layout.unblock_layout);
                TextView TextView = Constant.dialogLayoutColor.findViewById(R.id.TextView);
                TextView.setText("Unblock "+user.getFullName());
                Constant.dialogLayoutColor.show();

                TextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            Constant.Vibrator(context);
                        }

                        Constant.getSfFuncion(context);
                        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                        Webservice.unblockUserSetting(context, uid, user.getUid());
                        Constant.dialogLayoutColor.dismiss();
                        blockContactActivity.Initializes();
                    }
                });
                return false;

            }
        });
    }

    @Override
    public int getItemCount() {
        return blockedUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textFullName;

        public ViewHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            textFullName = itemView.findViewById(R.id.text_full_name);
        }
    }
}
