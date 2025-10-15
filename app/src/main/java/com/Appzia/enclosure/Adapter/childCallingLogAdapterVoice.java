package com.Appzia.enclosure.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.SubScreens.CallUtil;
import com.Appzia.enclosure.Model.call_log_history_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.BlurHelper;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.models.user_infoModel;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class childCallingLogAdapterVoice extends RecyclerView.Adapter<childCallingLogAdapterVoice.myViewHolder> {
    Context mContext;
    ArrayList<user_infoModel> list1 = new ArrayList<>();
    LinearLayout bottomcaller, call;
    ColorStateList tintList;
    TextView mainname;
    TextView phone, photo, ftoken, callerId, deviceType;
    CallUtil callFragment;
    int dummyPosition = -1;

    public childCallingLogAdapterVoice(Context mContext, ArrayList<user_infoModel> list1, LinearLayout bottomcaller, TextView mainname, TextView phone, TextView photo, TextView ftoken, TextView callerId, CallUtil callFragment, TextView deviceType, LinearLayout call) {
        this.mContext = mContext;
        this.list1 = list1;
        this.bottomcaller = bottomcaller;
        this.mainname = mainname;
        this.phone = phone;
        this.photo = photo;
        this.ftoken = ftoken;
        this.callerId = callerId;
        this.callFragment = callFragment;
        this.deviceType = deviceType;
        this.call = call;
    }

    public void removeItem(int adapterPosition) {
        try {
            list1.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        } catch (Exception e) {

        }
    }

    @NonNull
    @Override
    public childCallingLogAdapterVoice.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.voice_call_child_log_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull childCallingLogAdapterVoice.myViewHolder holder, int position) {

        //Collections.reverse(list1);
        final user_infoModel model = list1.get(position);

        Log.d("callingUserInfoChildModel", "onBindViewHolder: " + model.getFull_name());


        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");

        if (model.getCall_type().equals("1")) {

            Picasso.get().load(model.getPhoto()).into(holder.img);
            holder.name.setText(model.getFull_name());

            if (holder.name.getText().toString().length() > 22) {
                String truncatedText = holder.name.getText().toString().substring(0, 22) + "..."; // Add dots at the end
                holder.name.setText(truncatedText);
            } else {
                holder.name.setText(holder.name.getText().toString());
            }


            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                Date endTimeDate = inputFormat.parse(model.getEnd_time());
                if (endTimeDate != null) {
                    String formattedEndTime = outputFormat.format(endTimeDate);
                    holder.endTime.setText(formattedEndTime);
                } else {
                    holder.endTime.setText("--");
                }
            } catch (ParseException e) {
                e.printStackTrace();
                holder.endTime.setText("--");
            }

            if (model.getEnd_time().equals("")) {
                holder.endTime.setText(model.getStart_time());
            } else {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                    Date endTimeDate = inputFormat.parse(model.getEnd_time());
                    if (endTimeDate != null) {
                        String formattedEndTime = outputFormat.format(endTimeDate);
                        holder.endTime.setText(formattedEndTime);
                    } else {
                        holder.endTime.setText("--");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    holder.endTime.setText("--");
                }
            }
            if (model.getCalling_flag().equals("0")) {
                holder.calling_flag.setImageResource(R.drawable.outgoingcall);
                holder.calling_flag.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.green_call), PorterDuff.Mode.SRC_IN);
            } else if (model.getCalling_flag().equals("1")) {
                holder.calling_flag.setImageResource(R.drawable.incomingcall);

            } else if (model.getCalling_flag().equals("2")) {
                holder.calling_flag.setImageResource(R.drawable.miss_call_svg);
            }


            try {

                Constant.getSfFuncion(mContext);
                String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                try {
                    if (themColor.equals("#ff0080")) {
                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);

                    } else if (themColor.equals("#00A3E9")) {
                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);
                    } else if (themColor.equals("#7adf2a")) {

                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);
                    } else if (themColor.equals("#ec0001")) {

                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);
                    } else if (themColor.equals("#16f3ff")) {
                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);

                    } else if (themColor.equals("#FF8A00")) {
                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#7F7F7F")) {
                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#D9B845")) {
                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#346667")) {
                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#9846D9")) {
                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);


                    } else if (themColor.equals("#A81010")) {
                        holder.callIcon.setImageTintList(tintList);
                        holder.clickView.setBackgroundTintList(tintList);

                    } else {

                    }
                } catch (Exception ignored) {

                }


            } catch (Exception ignored) {
            }

        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // todo this is why preview hold on a exact location " v "

                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float touchX = location[0];
                float touchY = location[1];


                BlurHelper.showDialogWithBlurBackground(mContext, R.layout.voice_call_child_log_row_dialogue);
                BlurHelper.dialogLayoutColor.show();
                RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);
                
                // Use boundary-aware positioning
                BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);

                ImageView img, calling_flag, callIcon;
                LinearLayout call1;
                TextView name, endTime;

                TextView TextColor;
                LinearLayout clickView;
                CardView delete;

                img = BlurHelper.dialogLayoutColor.findViewById(R.id.img);
                name = BlurHelper.dialogLayoutColor.findViewById(R.id.name);
                endTime = BlurHelper.dialogLayoutColor.findViewById(R.id.endTime);
                calling_flag = BlurHelper.dialogLayoutColor.findViewById(R.id.calling_flag);
                call1 = BlurHelper.dialogLayoutColor.findViewById(R.id.call1);
                callIcon = BlurHelper.dialogLayoutColor.findViewById(R.id.callIcon);
                clickView = BlurHelper.dialogLayoutColor.findViewById(R.id.clickView);
                TextColor = BlurHelper.dialogLayoutColor.findViewById(R.id.text);


                if (model.getCall_type().equals("1")) {


                    Picasso.get().load(model.getPhoto()).into(img);
                    name.setText(model.getFull_name());

                    if (name.getText().toString().length() > 22) {
                        String truncatedText = name.getText().toString().substring(0, 22) + "..."; // Add dots at the end
                        name.setText(truncatedText);
                    } else {
                        name.setText(name.getText().toString());
                    }

                    endTime.setText(model.getEnd_time());

                    if (model.getEnd_time().equals("")) {
                        endTime.setText(model.getStart_time());
                    } else {
                        endTime.setText(model.getEnd_time());
                    }
                    if (model.getCalling_flag().equals("0")) {
                        calling_flag.setImageResource(R.drawable.outgoingcall);
                        calling_flag.setColorFilter(ContextCompat.getColor(((Activity) mContext).getApplicationContext(), R.color.green_call), PorterDuff.Mode.SRC_IN);
                    } else if (model.getCalling_flag().equals("1")) {
                        calling_flag.setImageResource(R.drawable.incomingcall);

                    } else if (model.getCalling_flag().equals("2")) {
                        calling_flag.setImageResource(R.drawable.miss_call_svg);
                    }


                    try {

                        Constant.getSfFuncion(mContext);
                        String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                        tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                        try {
                            if (themColor.equals("#ff0080")) {
                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);

                            } else if (themColor.equals("#00A3E9")) {
                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);
                            } else if (themColor.equals("#7adf2a")) {

                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);
                            } else if (themColor.equals("#ec0001")) {

                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);
                            } else if (themColor.equals("#16f3ff")) {
                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);

                            } else if (themColor.equals("#FF8A00")) {
                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);


                            } else if (themColor.equals("#7F7F7F")) {
                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);


                            } else if (themColor.equals("#D9B845")) {
                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);


                            } else if (themColor.equals("#346667")) {
                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);


                            } else if (themColor.equals("#9846D9")) {
                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);


                            } else if (themColor.equals("#A81010")) {
                                callIcon.setImageTintList(tintList);
                                clickView.setBackgroundTintList(tintList);

                            } else {

                            }
                        } catch (Exception ignored) {

                        }


                    } catch (Exception ignored) {
                    }

                }

                delete = BlurHelper.dialogLayoutColor.findViewById(R.id.deletecardview);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Constant.getSfFuncion(mContext);

                        Webservice.delete_voice_call_log(mContext, model.getId(), childCallingLogAdapterVoice.this, holder.getAdapterPosition(), BlurHelper.dialogLayoutColor, model.getFriend_id(), "1");


                    }
                });
                return true;
            }
        });


        if (dummyPosition != position) {

            if (holder.clickView.getVisibility() == View.VISIBLE) {
                collapseViewToLeft(holder.clickView, 400);
            }

        }

        holder.call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainname.setText(model.getFull_name());
                phone.setText(model.getMobile_no());
                photo.setText(model.getPhoto());
                ftoken.setText(model.getF_token());
                callerId.setText(model.getFriend_id());
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CallUtil.get_call_log_1ChildList.clear();

                notifyDataSetChanged();


                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date2 = new Date();
                String date = dateFormat.format(date2);


                // Get the current date
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -1); // Subtract 1 day

                // Retrieve the year, month, and day of yesterday's date
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Note: Months are zero-based
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Display yesterday's date
                String yesterdayDate = String.format("%04d-%02d-%02d", year, month, day);
                Log.d("yesterdayDate", yesterdayDate);

                CallUtil.label.setVisibility(View.GONE);
                CallUtil.layoutName.setVisibility(View.VISIBLE);
                CallUtil.searchData.setVisibility(View.INVISIBLE);
                Picasso.get().load(model.getPhoto()).into(CallUtil.img);
                CallUtil.name1.setText(model.getFull_name());
                CallUtil.mobile.setText(model.getMobile_no());
                CallUtil.mobile.setText(model.getMobile_no());


                if (date.equals(model.getDate())) {
                    CallUtil.dateLive.setText("Today");
                    CallUtil.dateLive.setVisibility(View.INVISIBLE);

                } else if (yesterdayDate.equals(model.getDate())) {
                    CallUtil.dateLive.setText("Yesterday");
                    CallUtil.dateLive.setVisibility(View.INVISIBLE);
                } else {
                    CallUtil.dateLive.setText(formatDateToMonthDay(model.getDate()));
                    CallUtil.dateLive.setVisibility(View.INVISIBLE);
                }


                Constant.getSfFuncion(mContext);
                bottomcaller.setVisibility(View.GONE);
                ArrayList<call_log_history_model> call_log_history_list = new ArrayList<call_log_history_model>();


                if (isInternetConnected()) {


                    try {
                        call_log_history_list.clear();
                        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                        call_log_history_list = dbHelper.get_voice_call_logChild2Table(model.getFriend_id(), model.getDate());

                        if (call_log_history_list.size() > 0) {
                            callFragment.setCallHistoryAdapter(call_log_history_list);

                        } else {

                        }


                    } catch (Exception ignored) {
                    }


                    Webservice.get_voice_call_log_history_for_voice(mContext, Constant.getSF.getString(Constant.UID_KEY, ""), model.getFriend_id(), model.getDate(), callFragment, call_log_history_list);

                } else {
                    try {
                        call_log_history_list.clear();
                        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                        call_log_history_list = dbHelper.get_voice_call_logChild2Table(model.getFriend_id(), model.getDate());
                        callFragment.setCallHistoryAdapter(call_log_history_list);
                    } catch (Exception ignored) {
                    }
                }

            }
        });


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

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img, calling_flag, callIcon;
        LinearLayout call1;
        TextView name, endTime;

        TextView TextColor;
        LinearLayout clickView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            endTime = itemView.findViewById(R.id.endTime);
            calling_flag = itemView.findViewById(R.id.calling_flag);
            call1 = itemView.findViewById(R.id.call1);
            callIcon = itemView.findViewById(R.id.callIcon);
            clickView = itemView.findViewById(R.id.clickView);
            TextColor = itemView.findViewById(R.id.text);
        }
    }


    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private String formatDateToMonthDay(String dateString) {
        try {
            // Parse the date string (assuming format is yyyy-MM-dd)
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(dateString);

            // Format to "Month, Day" (e.g., "July, 2", "January, 4")
            DateFormat outputFormat = new SimpleDateFormat("MMMM, d");
            return outputFormat.format(date);
        } catch (Exception e) {
            // If parsing fails, return the original string
            return dateString;
        }
    }

}
