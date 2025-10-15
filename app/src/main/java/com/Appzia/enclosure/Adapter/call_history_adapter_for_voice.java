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

public class call_history_adapter_for_voice extends RecyclerView.Adapter<call_history_adapter_for_voice.myViewHolder> {

    Context mContext;
    ColorStateList tintList;
    ArrayList<call_log_history_model> call_log_history_list = new ArrayList<>();
    public call_history_adapter_for_voice(Context mContext, ArrayList<call_log_history_model> call_log_history_list) {
        this.mContext = mContext;
        this.call_log_history_list = call_log_history_list;
    }

    @NonNull
    @Override
    public call_history_adapter_for_voice.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.voice_call_history_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull call_history_adapter_for_voice.myViewHolder holder, int position) {

        //    Collections.reverse(call_log_history_list);

        final call_log_history_model model = call_log_history_list.get(position);


        try {

            Constant.getSfFuncion(mContext);
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));




        } catch (Exception ignored) {
        }


        if (model.getCalling_flag().equals("0")) {
            holder.calling_flag_icon.setImageResource(R.drawable.outgoingcall);
            holder.calling_flag_icon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.green_call), PorterDuff.Mode.SRC_IN);
            holder.calling_flag.setText("Outgoing");
            holder.calling_flag.setTextColor(mContext.getColor(R.color.green_call));


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


                holder.incomingCallTotal.setVisibility(View.VISIBLE);
                holder.endTime.setText(formattedEndTime);
            }
            if (model.getEnd_time().equals("")) {

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


                holder.incomingCallTotal.setVisibility(View.VISIBLE);
                holder.endTime.setText(formattedEndTime);
            }
            if (model.getEnd_time().equals("")) {

                holder.incomingCallTotal.setVisibility(View.INVISIBLE);
                holder.endTime.setText(model.getStart_time());
            }

        } else if (model.getCalling_flag().equals("2")) {

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
            holder.calling_flag_icon.setImageResource(R.drawable.miss_call_svg);
            holder.calling_flag_icon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.red_call), PorterDuff.Mode.SRC_IN);
            holder.calling_flag.setTextColor(mContext.getColor(R.color.red_call));
            holder.calling_flag.setText("Missed");


            holder.incomingCallTotal.setVisibility(View.INVISIBLE);


            if (model.getEnd_time() != null && model.getStart_time() != null) {

                holder.endTime.setText(formattedEndTime);
            }
            if (model.getEnd_time().equals("")) {


                holder.endTime.setText(model.getStart_time());
            }


        }


    }

    @Override
    public int getItemCount() {
        return call_log_history_list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        ImageView calling_flag_icon, incomingcallIcon;
        TextView calling_flag, endTime, incomingCallTotal;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            calling_flag_icon = itemView.findViewById(R.id.calling_flag_icon);
            incomingcallIcon = itemView.findViewById(R.id.incomingcallIcon);
            calling_flag = itemView.findViewById(R.id.calling_flag);
            endTime = itemView.findViewById(R.id.endTime);
            incomingCallTotal = itemView.findViewById(R.id.incomingCallTotal);
        }
    }
}
