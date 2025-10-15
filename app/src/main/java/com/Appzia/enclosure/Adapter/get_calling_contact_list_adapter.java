package com.Appzia.enclosure.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class get_calling_contact_list_adapter extends RecyclerView.Adapter<get_calling_contact_list_adapter.myViewHolder> {
    Context mContext;
    LinearLayout bottomcaller,call;
    ColorStateList tintList;
    String themColor;
    TextView mainname;
    TextView phone, photo, ftoken, callerId, deviceType;
    ArrayList<get_contact_model> get_calling_contact_list = new ArrayList<>();
    int dummyPosition = -1;


    public get_calling_contact_list_adapter(Context mContext, LinearLayout bottomcaller, TextView mainname, TextView phone, TextView photo, TextView ftoken, TextView callerId, ArrayList<get_contact_model> get_calling_contact_list, TextView deviceType, LinearLayout call) {
        this.mContext = mContext;
        this.bottomcaller = bottomcaller;
        this.mainname = mainname;
        this.phone = phone;
        this.photo = photo;
        this.ftoken = ftoken;
        this.callerId = callerId;
        this.get_calling_contact_list = get_calling_contact_list;
        this.deviceType = deviceType;
        this.call = call;
    }

    public void searchFilteredData(ArrayList<get_contact_model> filteredList) {
        this.get_calling_contact_list = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public get_calling_contact_list_adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.get_calling_contact_list_row, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull get_calling_contact_list_adapter.myViewHolder holder, int position) {

        final get_contact_model model = get_calling_contact_list.get(position);

        Picasso.get().load(model.getPhoto()).into(holder.img);
        holder.name.setText(model.getFull_name());
        if (holder.name.getText().toString().length() > 22) {
            String truncatedText = holder.name.getText().toString().substring(0, 22) + "..."; // Add dots at the end
            holder.name.setText(truncatedText);
        } else {
            holder.name.setText(holder.name.getText().toString());
        }
        final float scale = mContext.getResources().getDisplayMetrics().density;
        int pixels = (int) (68 * scale + 0.5f);


        if (dummyPosition != position) {

            if (holder.clickView.getVisibility() == View.VISIBLE) {
                collapseViewToLeft(holder.clickView, 400);
            }

        }



        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
                    holder.pollyy.setBackgroundTintList(tintList);
                   holder.clickView.setBackgroundTintList(tintList);

                } else if (themColor.equals("#00A3E9")) {

                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7adf2a")) {

                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);


                } else if (themColor.equals("#ec0001")) {

                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);


                } else if (themColor.equals("#16f3ff")) {

                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);


                } else if (themColor.equals("#FF8A00")) {

                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {


                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {

                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);


                } else if (themColor.equals("#346667")) {

                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);


                } else if (themColor.equals("#9846D9")) {

                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);


                } else if (themColor.equals("#A81010")) {

                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);


                } else {

                    holder.pollyy.setBackgroundTintList(tintList);
                    holder.clickView.setBackgroundTintList(tintList);

                }
            } catch (Exception ignored) {

                holder.pollyy.setBackgroundTintList(tintList);
                holder.clickView.setBackgroundTintList(tintList);
            }


        } catch (Exception ignored) {
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mainname.setText(model.getFull_name());
                phone.setText(model.getMobile_no());
                photo.setText(model.getPhoto());
                Log.d("zxcvbnm", "onClick: " + model.getPhoto());
                ftoken.setText(model.getF_token());
                callerId.setText(model.getUid());
                deviceType.setText(model.getDevice_type());


                dummyPosition = position;

                if (holder.clickView.getVisibility() == View.GONE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Constant.Vibrator50(mContext);
                    }
                    expandViewFromLeft(holder.clickView, 400);
                    notifyDataSetChanged();
                }

            }
        });

        holder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add any action you need for the clickView
                call.performClick();
            }
        });


    }



    @Override
    public int getItemCount() {
        return get_calling_contact_list.size();
    }

    public void expandViewFromLeft(final View view, int duration) {
        // Step 1: Temporarily make the view VISIBLE to measure it
        int originalVisibility = view.getVisibility();
        view.setVisibility(View.VISIBLE);

        // Step 2: Measure the view with WRAP_CONTENT
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int targetWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 400, view.getResources().getDisplayMetrics());


        // Fallback minimum width (optional, remove if not needed)
        int minWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
                view.getResources().getDisplayMetrics());
        targetWidth = Math.max(targetWidth, minWidth);

        // Step 3: Set width to 0 before starting animation
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = 0;
        view.setLayoutParams(layoutParams);

        // Step 4: Animate from 0 to target width
        ValueAnimator animator = ValueAnimator.ofInt(0, targetWidth);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            layoutParams.width = value;
            view.setLayoutParams(layoutParams);
        });
        animator.start();

        // Restore visibility if needed
        if (originalVisibility == View.GONE || originalVisibility == View.INVISIBLE) {
            view.setVisibility(View.VISIBLE);
        }

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

    public static class myViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, TextColor;
        LinearLayout clickView;
        LinearLayout pollyy;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            pollyy = itemView.findViewById(R.id.pollyy);
            clickView = itemView.findViewById(R.id.clickView);
            TextColor = itemView.findViewById(R.id.text);
        }
    }
}
