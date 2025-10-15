package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Fragments.callFragment;
import com.Appzia.enclosure.Model.callloglistModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.SubScreens.CallUtil;
import com.Appzia.enclosure.models.get_call_log_1Child;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class calllogParentAdapterVoice extends RecyclerView.Adapter<calllogParentAdapterVoice.myViewHolder> {

    Context mContext;
    ArrayList<get_call_log_1Child> callloglist = new ArrayList<>();

    childCallingLogAdapterVoice adapter;
    LinearLayout bottomcaller,call;
    TextView mainname;
    TextView phone, photo, ftoken, callerId,deviceType;

    CallUtil callFragment;

    public calllogParentAdapterVoice(Context mContext, ArrayList<get_call_log_1Child> model, LinearLayout bottomcaller, TextView mainname, TextView phone, TextView photo, TextView ftoken, TextView callerId, CallUtil callFragment, TextView deviceType, LinearLayout call) {
        this.mContext = mContext;
        this.callloglist = model;
        this.bottomcaller = bottomcaller;
        this.mainname = mainname;
        this.phone = phone;
        this.photo = photo;
        this.ftoken = ftoken;
        this.callerId = callerId;
        this.callFragment  =  callFragment;
        this.deviceType  =  deviceType;
        this.call  =  call;

    }

    @NonNull
    @Override
    public calllogParentAdapterVoice.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.call_log_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull calllogParentAdapterVoice.myViewHolder holder, int position) {

        final get_call_log_1Child model = callloglist.get(position);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = new Date();
        String date = dateFormat.format(date2);

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1); // Subtract 1 day
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Display yesterday's date
        String yesterdayDate = String.format("%04d-%02d-%02d", year, month, day);
        Log.d("yesterdayDate", yesterdayDate);


        if (date.equals(model.getDate())) {
            holder.today.setText("Today");
        } else if (yesterdayDate.equals(model.getDate())) {
            holder.today.setText("Yesterday");
        } else {
            holder.today.setText(formatDateToMonthDay(model.getDate()));
        }

        adapter = new childCallingLogAdapterVoice(mContext, model.getUser_info(),this.bottomcaller, this.mainname, this.phone, this.photo, this.ftoken, this.callerId,callFragment,deviceType,call);
        holder.childRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        holder.childRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return callloglist.size();
    }

    private String formatDateToMonthDay(String dateString) {
        try {
            // Parse the date string (assuming format is yyyy-MM-dd)
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(dateString);
            
            // Format to abbreviated month and day (e.g., "Sept 6", "Jan 4")
            DateFormat outputFormat = new SimpleDateFormat("MMM d");
            return outputFormat.format(date);
        } catch (Exception e) {
            // If parsing fails, return the original string
            return dateString;
        }
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView today;
        RecyclerView childRecyclerview;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            today = itemView.findViewById(R.id.today);
            childRecyclerview = itemView.findViewById(R.id.childRecyclerview);
        }
    }
}
