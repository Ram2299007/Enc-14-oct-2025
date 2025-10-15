package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.CombineModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.chattingScreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class inviteScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_INVITE = 2;

    Context mContext;
    private ArrayList<CombineModel> CombineModelList;

    public inviteScreenAdapter(Context mContext, ArrayList<CombineModel> CombineModelList) {
        this.mContext = mContext;
        this.CombineModelList = CombineModelList;

    }

    @Override
    public int getItemViewType(int position) {
        CombineModel combinedModel = CombineModelList.get(position);
        if (combinedModel.getUserData() != null) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_INVITE;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_USER) {
            View userView = inflater.inflate(R.layout.contact_active_row, parent, false);
            return new ContactActiveHolder(userView);
        } else {
            View inviteView = inflater.inflate(R.layout.contact_row, parent, false);
            return new InviteHolder(inviteView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CombineModel combinedModel = CombineModelList.get(position);

        if (getItemViewType(position) == VIEW_TYPE_USER) {
            ContactActiveHolder userViewHolder = (ContactActiveHolder) holder;


            userViewHolder.contact1text.setText(combinedModel.getUserData().getFull_name());

            if (userViewHolder.contact1text.getText().toString().length() > 20) {
                String truncatedText = userViewHolder.contact1text.getText().toString().substring(0, 20) + "..."; // Add dots at the end
                userViewHolder.contact1text.setText(truncatedText);
            } else {
                userViewHolder.contact1text.setText(userViewHolder.contact1text.getText().toString());
            }



            userViewHolder.captiontext.setText(combinedModel.getUserData().getCaption());

            if (userViewHolder.captiontext.getText().toString().length() > 35) {
                String truncatedText = userViewHolder.captiontext.getText().toString().substring(0, 35) + "..."; // Add dots at the end
                userViewHolder.captiontext.setText(truncatedText);
            } else {
                userViewHolder.captiontext.setText(userViewHolder.captiontext.getText().toString());
            }

            Picasso.get().load(combinedModel.getUserData().getPhoto()).into(userViewHolder.contact1img);


            //   Toast.makeText(mContext, "success2", Toast.LENGTH_SHORT).show();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, chattingScreen.class);
                    intent.putExtra("nameKey", combinedModel.getUserData().getFull_name());
                    intent.putExtra("captionKey", combinedModel.getUserData().getCaption());
                    intent.putExtra("photoKey", combinedModel.getUserData().getPhoto());
                    intent.putExtra("friendUidKey", combinedModel.getUserData().getUid());
                    intent.putExtra("ecKey", "ecKey");
                    mContext.startActivity(intent);
                }
            });




            // Bind other user data
        } else {
            InviteHolder inviteViewHolder = (InviteHolder) holder;

            inviteViewHolder.userName.setText(combinedModel.getInviteData().getContact_name());

            if (inviteViewHolder.userName.getText().toString().length() > 35) {
                String truncatedText = inviteViewHolder.userName.getText().toString().substring(0, 35) + "..."; // Add dots at the end
                inviteViewHolder.userName.setText(truncatedText);
            } else {
                inviteViewHolder.userName.setText(inviteViewHolder.userName.getText().toString());
            }

            inviteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = combinedModel.getInviteData().getContact_number();
                    String message = "Inviting you to Download Enclosure ! \n" +
                            "Your message will become more valuable here \n" +
                            "New messaging app -\n" +
                            "for billion people https://Enclosure.com/dl";
                    Uri uri = Uri.parse("smsto:" + phoneNumber);
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    sendIntent.putExtra("sms_body", message);
                    // sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    inviteViewHolder.itemView.getContext().startActivity(sendIntent);


                }
            });



            // Bind other invite data
        }
    }

    @Override
    public int getItemCount() {
        return CombineModelList.size();
    }

    public static class ContactActiveHolder extends RecyclerView.ViewHolder {

        androidx.appcompat.widget.AppCompatImageView contact1img;
        TextView contact1text;
        TextView captiontext;
        LinearLayout contact1;

        public ContactActiveHolder(View itemView) {
            super(itemView);
            contact1img = itemView.findViewById(R.id.contact1img);
            contact1text = itemView.findViewById(R.id.contact1text);
            captiontext = itemView.findViewById(R.id.captiontext);
            contact1 = itemView.findViewById(R.id.contact1);
        }


    }

    public static class InviteHolder extends RecyclerView.ViewHolder {
        TextView userName, userInvite;

        public InviteHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userInvite = itemView.findViewById(R.id.userInvite);
        }

    }
}

