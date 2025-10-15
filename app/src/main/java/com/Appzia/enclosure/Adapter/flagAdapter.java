package com.Appzia.enclosure.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.flagModel;
import com.Appzia.enclosure.Model.flagNewModelChild;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.whatsYourNumber;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class flagAdapter extends RecyclerView.Adapter<flagAdapter.myViewHolder> {

    Context mContext;
    ArrayList<flagNewModelChild> flagModelList = new ArrayList<>();


    public flagAdapter(Context mContext, ArrayList<flagNewModelChild> flagModelList) {
        this.mContext = mContext;
        this.flagModelList = flagModelList;
    }

    @NonNull
    @Override
    public flagAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.show_flag_row, parent, false);
        return new myViewHolder(listItem);
    }

    public void searchFilteredData(ArrayList<flagNewModelChild> flagModelList) {
        this.flagModelList = flagModelList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull flagAdapter.myViewHolder holder, int position) {

        final flagNewModelChild model = flagModelList.get(position);


//

        holder.flagTxt.setText("" + "+" + model.getCountry_c_code() + "");
        holder.cname.setText(model.getCountry_name());
        holder.flagCode.setText(model.getCountry_code());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, whatsYourNumber.class);
                intent.putExtra("countrycode", "+"+model.getCountry_c_code());
                intent.putExtra("flagFinal", model.getCountry_code());
                intent.putExtra("c_id", model.getC_id());
                holder.itemView.getContext().startActivity(intent);
                ((Activity) mContext).finish();


            }
        });

    }

    @Override
    public int getItemCount() {
        return flagModelList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView flagCode;
        TextView flagTxt, cname;
        LinearLayout linear;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            flagCode = itemView.findViewById(R.id.flagCode);
            flagTxt = itemView.findViewById(R.id.flagTxt);
            cname = itemView.findViewById(R.id.cname);
            linear = itemView.findViewById(R.id.linear);
        }
    }



}


