package com.Appzia.enclosure.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.allContactListModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.chattingScreen;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class allContactListAdapter extends RecyclerView.Adapter<allContactListAdapter.myViewHolder> {
    Context mContext;
    ArrayList<allContactListModel> allContactListModelList = new ArrayList<>();
    private int firstInviteUserPosition = -1;
    CardView valuable;
    TextView textCard;

    public allContactListAdapter(Context mContext, ArrayList<allContactListModel> allContactListModelList, CardView valuable, TextView textCard) {
        this.mContext = mContext;
        this.allContactListModelList = allContactListModelList;
        this.valuable = valuable;
        this.textCard = textCard;
        findFirstInviteUserPosition();
    }

    public void updateList(ArrayList<allContactListModel> newList) {
        this.allContactListModelList = newList != null ? newList : new ArrayList<>();
        findFirstInviteUserPosition();
        notifyDataSetChanged();
    }

    public void searchFilteredData(ArrayList<allContactListModel> filteredList) {
        this.allContactListModelList = filteredList != null ? filteredList : new ArrayList<>();
        findFirstInviteUserPosition();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_active_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        allContactListModel model = allContactListModelList.get(position);
        String c_flag = model.getC_flag();

        if ("1".equals(c_flag)) {
            // Active user section

            holder.inviteText.setVisibility(View.GONE);
            holder.activeContactRel.setVisibility(View.VISIBLE);
            holder.inviteRel.setVisibility(View.GONE);

            // Display name with (You) if uid matches
            Constant.getSfFuncion(mContext);
            String uid = Constant.getSF.getString(Constant.UID_KEY, "");
            if (uid.equals(model.getUid())) {
                String nameText = model.getFull_name();
                String youText = " (You)";
                SpannableString spannable = new SpannableString(nameText + youText);

                spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                        0, nameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                        nameText.length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.contact1text.setText(spannable);
            } else {
                holder.contact1text.setText(model.getFull_name());
            }

            // Truncate name if too long
            if (holder.contact1text.getText().toString().length() > 20) {
                String truncatedText = holder.contact1text.getText().toString().substring(0, 20) + "...";
                holder.contact1text.setText(truncatedText);
            }

            // Set caption and truncate if needed
            holder.captiontext.setText(model.getCaption());
            if (holder.captiontext.getText().toString().length() > 35) {
                String truncatedText = holder.captiontext.getText().toString().substring(0, 35) + "...";
                holder.captiontext.setText(truncatedText);
            }

            // Load photo with Picasso
            try {
                Picasso.get().load(model.getPhoto()).into(holder.contact1img);
            } catch (Exception e) {
                Picasso.get().load(R.drawable.inviteimg).into(holder.contact1img);
            }

            // Click listener to open chattingScreen
            holder.itemView.setOnClickListener(v -> {
                Intent intent0 = ((Activity)mContext).getIntent();
                if (intent0.hasExtra("grpKey")) {
                    intent0.removeExtra("grpKey");
                }

                Intent intent00 = ((Activity)mContext).getIntent();
                if (intent00.hasExtra("voiceCallKey")) {
                    intent00.removeExtra("voiceCallKey");
                }
                Intent intent000 = ((Activity)mContext).getIntent();
                if (intent000.hasExtra("youKey")) {
                    intent000.removeExtra("youKey");
                }
                Intent intent0000 = ((Activity)mContext).getIntent();
                if (intent0000.hasExtra("videoCallKey")) {
                    intent0000.removeExtra("videoCallKey");
                }

                Intent intent = new Intent(mContext, chattingScreen.class);
                intent.putExtra("nameKey", model.getFull_name());
                intent.putExtra("captionKey", model.getCaption());
                intent.putExtra("photoKey", model.getPhoto());
                intent.putExtra("friendUidKey", model.getUid());
                intent.putExtra("msgLmtKey", "0");
                intent.putExtra("ecKey", "ecKey");
                intent.putExtra("fromInviteKey", "fromInviteKey");
                intent.putExtra("block", model.isBlock());
                intent.putExtra("iamblocked", model.isIamblocked());
                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
            });

        } else {
            // Invite user section

            holder.activeContactRel.setVisibility(View.GONE);
            holder.inviteRel.setVisibility(View.VISIBLE);

            // Show inviteText ONLY if this is the first invite user
            if (position == firstInviteUserPosition) {
                holder.inviteText.setVisibility(View.VISIBLE);
                Log.d("AdapterDebug", "Showing inviteText at position: " + position);
            } else {
                holder.inviteText.setVisibility(View.GONE);
                Log.d("AdapterDebug", "Hiding inviteText at position: " + position);
            }

            // Set invite user name and truncate if needed
            holder.userName.setText(model.getContact_name());
            if (holder.userName.getText().toString().length() > 35) {
                String truncatedText = holder.userName.getText().toString().substring(0, 35) + "...";
                holder.userName.setText(truncatedText);
            }

            // Click listener to send SMS invite
            holder.itemView.setOnClickListener(v -> {
                String phoneNumber = model.getContact_number();
                String message = "Inviting you to Download Enclosure ! \n" +
                        "Your message will become more valuable here \n" +
                        "New messaging app -\n" +
                        "for billion people https://www.enclosureapp.com";
                Uri uri = Uri.parse("smsto:" + phoneNumber);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                sendIntent.putExtra("sms_body", message);
                holder.itemView.getContext().startActivity(sendIntent);
            });
        }
    }

    private void findFirstInviteUserPosition() {
        firstInviteUserPosition = -1;
        for (int i = 0; i < allContactListModelList.size(); i++) {
            if ("0".equals(allContactListModelList.get(i).getC_flag())) {
                firstInviteUserPosition = i;
                Log.d("AdapterDebug", "First invite user found at position: " + i);
                break;
            }
        }
        if (firstInviteUserPosition == -1) {
            Log.d("AdapterDebug", "No invite user found");
        }
    }

    @Override
    public int getItemCount() {
        return allContactListModelList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout activeContactRel;
        RelativeLayout inviteRel;
        AppCompatImageView contact1img;
        TextView contact1text;
        TextView captiontext;
        TextView inviteText;
        TextView userName;
        TextView userInvite;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            activeContactRel = itemView.findViewById(R.id.activeContactRel);
            inviteRel = itemView.findViewById(R.id.inviteRel);
            contact1img = itemView.findViewById(R.id.contact1img);
            contact1text = itemView.findViewById(R.id.contact1text);
            captiontext = itemView.findViewById(R.id.captiontext);
            inviteText = itemView.findViewById(R.id.inviteText);
            userName = itemView.findViewById(R.id.userName);
            userInvite = itemView.findViewById(R.id.userInvite);
        }
    }
}
