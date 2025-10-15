package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Screens.userInfoScreen;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.models.members;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class forGroupVisibleMembersAdapter extends RecyclerView.Adapter<forGroupVisibleMembersAdapter.myViewHolder> {
    Context mContext;
    ArrayList<members> membersList;

    public forGroupVisibleMembersAdapter(Context mContext, ArrayList<members> members) {
        this.mContext = mContext;
        this.membersList = members;
    }

    @NonNull
    @Override
    public forGroupVisibleMembersAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grp_details_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull forGroupVisibleMembersAdapter.myViewHolder holder, int position) {
        members model = membersList.get(position);


        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        if (uid.equals(model.getUid())) {
            String nameText = model.getFull_name();
            String youText = " (You)";
            SpannableString spannable = new SpannableString(nameText + youText);

            // Set color for name (black)
            spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                    0, nameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Set color for (You) (gray)
            spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                    nameText.length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.contact1text.setText(spannable);
        } else {
            holder.contact1text.setText(model.getFull_name());

        }





        if (holder.contact1text.getText().toString().length() > 20) {
            String truncatedText = holder.contact1text.getText().toString().substring(0, 20) + "..."; // Add dots at the end
            holder.contact1text.setText(truncatedText);
        } else {
            holder.contact1text.setText(holder.contact1text.getText().toString());
        }


        holder.captiontext.setText(model.getCaption());

        if (holder.captiontext.getText().toString().length() > 35) {
            String truncatedText = holder.captiontext.getText().toString().substring(0, 35) + "..."; // Add dots at the end
            holder.captiontext.setText(truncatedText);
        } else {
            holder.captiontext.setText(holder.captiontext.getText().toString());
        }

        try {
            Picasso.get().load(model.getPhoto()).into(holder.contact1img);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.inviteimg).into(holder.contact1img);
        }


        holder.contact1img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext, show_image_Screen.class);
                intent.putExtra("imageKey", model.getPhoto());

                mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, userInfoScreen.class);
                intent.putExtra("recUserId", model.getUid());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView contact1img;
        TextView contact1text;
        TextView captiontext;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            contact1img = itemView.findViewById(R.id.contact1img);
            contact1text = itemView.findViewById(R.id.contact1text);
            captiontext = itemView.findViewById(R.id.captiontext);

        }
    }
}
