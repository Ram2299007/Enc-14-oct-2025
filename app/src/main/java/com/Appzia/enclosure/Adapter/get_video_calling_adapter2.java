package com.Appzia.enclosure.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.FcmNotificationsSender;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class get_video_calling_adapter2 extends RecyclerView.Adapter<get_video_calling_adapter2.myViewHolder> {
    Context mContext;
    ColorStateList tintList;
    ArrayList<get_contact_model> get_calling_contact_list;
    int dummyPosition = -1;
    CardView customToastCard;
    TextView customToastText;
    String username, roomId;
    Dialog dialogLayoutFullScreen;


    public get_video_calling_adapter2(Context mContext, ArrayList<get_contact_model> get_calling_contact_list,
                                      CardView customToastCard, TextView customToastText,
                                      String username, String roomId, Dialog dialogLayoutFullScreen) {
        this.mContext = mContext;
        this.get_calling_contact_list = get_calling_contact_list;
        this.customToastCard = customToastCard;
        this.customToastText = customToastText;
        this.username = username;
        this.roomId = roomId;
        this.dialogLayoutFullScreen = dialogLayoutFullScreen;

    }

    public void searchFilteredData(ArrayList<get_contact_model> filteredList) {
        this.get_calling_contact_list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.get_calling_contact_list_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final get_contact_model model = get_calling_contact_list.get(position);
        Picasso.get().load(model.getPhoto()).into(holder.img);
        holder.name.setText(model.getFull_name());
        // Toast.makeText(mContext,username, Toast.LENGTH_SHORT).show();

        if (username.equals(model.getUid())) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
        }

        if (holder.name.getText().toString().length() > 22) {
            String truncatedText = holder.name.getText().toString().substring(0, 22) + "...";
            holder.name.setText(truncatedText);
        }

        final float scale = mContext.getResources().getDisplayMetrics().density;
        int pixels = (int) (68 * scale + 0.5f);
        try {
            Constant.getSfFuncion(mContext);
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));
            holder.callIcon.setImageTintList(tintList);
            holder.clickView.setBackgroundTintList(tintList);
        } catch (Exception ignored) {
        }

        if (dummyPosition != position) {
            if (holder.clickView.getVisibility() == View.VISIBLE) {
                collapseViewToLeft(holder.clickView, 400);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            dummyPosition = position;
            if (holder.clickView.getVisibility() == View.GONE) {
                Constant.Vibrator50(mContext);
                expandViewFromLeft(holder.clickView, 400);
                notifyDataSetChanged();
            }
        });

        holder.clickView.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Constant.Vibrator(mContext);
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String currentDate = dateFormat.format(date);
            Constant.getSfFuncion(mContext);
            String uid = Constant.getSF.getString(Constant.UID_KEY, "");
            String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");

            if (!sleepKey.equals(Constant.sleepKey)) {
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                        model.getF_token(), "Enclosure", Constant.videocall, mContext,
                        ((Activity) mContext), Constant.getSF.getString(Constant.full_name, ""),
                        "meetingId", model.getMobile_no(), Constant.getSF.getString(Constant.profilePic, ""),
                        model.getF_token(), uid, model.getUid(), model.getDevice_type(), uid, uid, uid, roomId);
                notificationsSender.SendNotifications();
                Constant.showCustomToast("Sending...", customToastCard, customToastText);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    dialogLayoutFullScreen.dismiss();
                }, 500);
            }
        });
    }

    public void expandViewFromLeft(final View view, int duration) {
        view.setVisibility(View.VISIBLE);
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int targetWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 400, view.getResources().getDisplayMetrics());
        int minWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
                view.getResources().getDisplayMetrics());
        targetWidth = Math.max(targetWidth, minWidth);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = 0;
        view.setLayoutParams(layoutParams);

        ValueAnimator animator = ValueAnimator.ofInt(0, targetWidth);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            layoutParams.width = value;
            view.setLayoutParams(layoutParams);
        });
        animator.start();
    }

    public void collapseViewToLeft(final View view, int duration) {
        final int initialWidth = view.getWidth();
        ValueAnimator animator = ValueAnimator.ofInt(initialWidth, 0);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = value;
            view.setLayoutParams(layoutParams);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    @Override
    public int getItemCount() {
        return get_calling_contact_list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img, callIcon;
        TextView name, TextColor;
        LinearLayout clickView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            callIcon = itemView.findViewById(R.id.callIcon);
            clickView = itemView.findViewById(R.id.clickView);
            TextColor = itemView.findViewById(R.id.text);
        }
    }


}