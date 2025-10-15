package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.call_log_history_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.TimeCalculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class call_history_adapter extends RecyclerView.Adapter<call_history_adapter.myViewHolder> {

    Context mContext;
    ColorStateList tintList;
    String themColor;
    ArrayList<call_log_history_model> call_log_history_list;

    public call_history_adapter(Context mContext, ArrayList<call_log_history_model> call_log_history_list) {
        this.mContext = mContext;
        if (call_log_history_list.size() > 14) {
            this.call_log_history_list = new ArrayList<>(call_log_history_list.subList(0, 14));
        } else {
            this.call_log_history_list = new ArrayList<>(call_log_history_list);
        }
        Log.d("AdapterInitialization", "Data size: " + call_log_history_list.size());
    }

    @NonNull
    @Override
    public call_history_adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.call_log_history_layout, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull call_history_adapter.myViewHolder holder, int position) {
        Log.d("onBindViewHoldercecew", "Position: " + position + " - Data: " + call_log_history_list.get(position).toString());

        final call_log_history_model model = call_log_history_list.get(position);


        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
                    holder.pollyy.setBackgroundTintList(tintList);

                } else if (themColor.equals("#00A3E9")) {

                    holder.pollyy.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7adf2a")) {

                    holder.pollyy.setBackgroundTintList(tintList);


                } else if (themColor.equals("#ec0001")) {

                    holder.pollyy.setBackgroundTintList(tintList);


                } else if (themColor.equals("#16f3ff")) {

                    holder.pollyy.setBackgroundTintList(tintList);


                } else if (themColor.equals("#FF8A00")) {

                    holder.pollyy.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {


                    holder.pollyy.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {

                    holder.pollyy.setBackgroundTintList(tintList);


                } else if (themColor.equals("#346667")) {

                    holder.pollyy.setBackgroundTintList(tintList);


                } else if (themColor.equals("#9846D9")) {

                    holder.pollyy.setBackgroundTintList(tintList);


                } else if (themColor.equals("#A81010")) {

                    holder.pollyy.setBackgroundTintList(tintList);


                } else {

                    holder.pollyy.setBackgroundTintList(tintList);

                }
            } catch (Exception ignored) {

                holder.pollyy.setBackgroundTintList(tintList);
            }


        } catch (Exception ignored) {
        }


        if (model.getCalling_flag().equals("0")) {
            holder.calling_flag_icon.setImageResource(R.drawable.outgoingcall);
            holder.calling_flag_icon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.green_call), PorterDuff.Mode.SRC_IN);
            holder.calling_flag.setTextColor(mContext.getColor(R.color.green_call));

            holder.calling_flag.setText("Outgoing");

            if (model.getEnd_time() != null && model.getStart_time() != null) {
                String timeStr1 = model.getStart_time(); // e.g., "12:00:54 PM"
                String timeStr2 = model.getEnd_time();   // e.g., "12:15:30 PM"

                SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss a"); // format of input from model
                SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");   // format expected by TimeCalculator

                String formattedStartTime = "";
                String formattedEndTime = "";

                try {
                    formattedStartTime = outputFormat.format(inputFormat.parse(timeStr1));
                    formattedEndTime = outputFormat.format(inputFormat.parse(timeStr2));
                } catch (ParseException e) {
                    e.printStackTrace();
                    // fallback to raw input if parsing fails
                    formattedStartTime = timeStr1;
                    formattedEndTime = timeStr2;
                }

// calculate time difference
                String timeDifference = TimeCalculator.getTimeDifference(formattedStartTime, formattedEndTime);

// debug logs
                System.out.println("Time Difference: " + timeDifference);
                Log.d("STARTTIME1", " startTime " + formattedStartTime + " endTime " + formattedEndTime);

// display in view
                holder.incomingCallTotal.setText(timeDifference);
                holder.incomingCallTotal.setVisibility(View.VISIBLE);
                holder.endTime.setText(formattedEndTime);


                holder.incomingCallTotal.setText(timeDifference);

                //  holder.incomingcallIcon.setVisibility(View.VISIBLE);
                holder.incomingCallTotal.setVisibility(View.VISIBLE);
                holder.endTime.setText(formattedEndTime); // ✅ already formatted as hh:mm a

            }
            if (model.getEnd_time().equals("")) {
                //      holder.incomingcallIcon.setVisibility(View.INVISIBLE);
                holder.incomingCallTotal.setVisibility(View.INVISIBLE);
                holder.endTime.setText(model.getStart_time());
            }
        } else if (model.getCalling_flag().equals("1")) {
            holder.calling_flag_icon.setImageResource(R.drawable.incomingcall);
            holder.calling_flag_icon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.green_call), PorterDuff.Mode.SRC_IN);
            holder.calling_flag.setTextColor(mContext.getColor(R.color.green_call));
            holder.calling_flag.setText("Incoming");


            if (model.getEnd_time() != null && model.getStart_time() != null) {
                // here add deifference
                // Log.d("STARTTIME", " startTime "+model.getStart_time()+" endTime "+model.getEnd_time());

                String timeStr1 = model.getStart_time(); // e.g., "12:00:54 PM"
                String timeStr2 = model.getEnd_time();   // e.g., "12:15:30 PM"

                SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss a"); // model's format
                SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");   // desired format

                String formattedStartTime = "";
                String formattedEndTime = "";

                try {
                    formattedStartTime = outputFormat.format(inputFormat.parse(timeStr1));
                    formattedEndTime = outputFormat.format(inputFormat.parse(timeStr2));
                } catch (ParseException e) {
                    e.printStackTrace();
                    formattedStartTime = timeStr1;
                    formattedEndTime = timeStr2;
                }

                String timeDifference = TimeCalculator.getTimeDifference(formattedStartTime, formattedEndTime);

// Show in log and UI
                System.out.println("Time Difference: " + timeDifference);
                Log.d("STARTTIME2", " startTime " + formattedStartTime + " endTime " + formattedEndTime);

                holder.incomingCallTotal.setText(timeDifference);
                holder.incomingCallTotal.setVisibility(View.VISIBLE);
                holder.endTime.setText(formattedEndTime);


                holder.incomingCallTotal.setText(timeDifference);

                //  holder.incomingcallIcon.setVisibility(View.VISIBLE);
                holder.incomingCallTotal.setVisibility(View.VISIBLE);
                holder.endTime.setText(formattedEndTime); // ✅ already formatted as hh:mm a

            }
            if (model.getEnd_time().equals("")) {
                //   holder.incomingcallIcon.setVisibility(View.INVISIBLE);
                holder.incomingCallTotal.setVisibility(View.INVISIBLE);
                holder.endTime.setText(model.getStart_time());
            }

        } else if (model.getCalling_flag().equals("2")) {

            holder.calling_flag_icon.setImageResource(R.drawable.miss_call_svg);
            holder.calling_flag_icon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.red_call), PorterDuff.Mode.SRC_IN);
            holder.calling_flag.setTextColor(mContext.getColor(R.color.red_call));
            holder.calling_flag.setText("Missed");


            if (model.getEnd_time() != null && model.getStart_time() != null) {

                String timeStr1 = model.getStart_time(); // e.g., "12:00:54 PM"
                String timeStr2 = model.getEnd_time();   // e.g., "12:15:30 PM"

                SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss a"); // model's format
                SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");   // desired format

                String formattedStartTime = "";
                String formattedEndTime = "";

                try {
                    formattedStartTime = outputFormat.format(inputFormat.parse(timeStr1));
                    formattedEndTime = outputFormat.format(inputFormat.parse(timeStr2));
                } catch (ParseException e) {
                    e.printStackTrace();
                    formattedStartTime = timeStr1;
                    formattedEndTime = timeStr2;
                }

                String timeDifference = TimeCalculator.getTimeDifference(formattedStartTime, formattedEndTime);

// Show in log and UI
                System.out.println("Time Difference: " + timeDifference);
                Log.d("STARTTIME2", " startTime " + formattedStartTime + " endTime " + formattedEndTime);

                holder.incomingCallTotal.setText(timeDifference);
                holder.incomingCallTotal.setVisibility(View.VISIBLE);
                holder.endTime.setText(formattedEndTime);


                holder.incomingCallTotal.setText(timeDifference);

                //  holder.incomingcallIcon.setVisibility(View.VISIBLE);
                holder.incomingCallTotal.setVisibility(View.VISIBLE);
                holder.endTime.setText(formattedEndTime); // ✅ already formatted as hh:mm a

            }
            if (model.getEnd_time().equals("")) {
                //   holder.incomingcallIcon.setVisibility(View.INVISIBLE);
                holder.incomingCallTotal.setVisibility(View.INVISIBLE);
                holder.endTime.setText(model.getStart_time());
            }


        }


    }

    @Override
    public int getItemCount() {
        Log.d("AdapterInitialization", "Size: " + call_log_history_list.size());
        return call_log_history_list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        ImageView calling_flag_icon, incomingcallIcon;
        TextView calling_flag, endTime, incomingCallTotal;
        LinearLayout pollyy;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            calling_flag_icon = itemView.findViewById(R.id.calling_flag_icon);
            incomingcallIcon = itemView.findViewById(R.id.incomingcallIcon);
            calling_flag = itemView.findViewById(R.id.calling_flag);
            endTime = itemView.findViewById(R.id.endTime);
            incomingCallTotal = itemView.findViewById(R.id.incomingCallTotal);
            pollyy = itemView.findViewById(R.id.pollyy);

        }
    }
}
