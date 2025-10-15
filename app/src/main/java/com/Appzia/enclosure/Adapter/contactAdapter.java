package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.contactGetModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;

import java.util.ArrayList;

public class contactAdapter extends RecyclerView.Adapter<contactAdapter.myViewholder> {

    Context mContext;
    public static String fontSizePref;
    ArrayList<contactGetModel> contactGetList = new ArrayList<>();

    public contactAdapter(Context mContext, ArrayList<contactGetModel> contactGetList) {
        this.mContext = mContext;
        this.contactGetList = contactGetList;
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_row, parent, false);
        return new myViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull contactAdapter.myViewholder holder, int position) {

        final contactGetModel model = contactGetList.get(position);
        holder.userName.setText(model.getName());


//        Log.d("TotalNamesss", model.getName());


        try {

            Constant.getSfFuncion(mContext);
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (fontSizePref.equals(Constant.small)) {

                holder.userName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);


            } else if (fontSizePref.equals(Constant.medium)) {
                holder.userName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            } else if (fontSizePref.equals(Constant.large)) {
                holder.userName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            }
        } catch (Exception ignored) {

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = model.getPhoneNumber();
                String message = "Let’s chat on Enclosure! It’s a \n" +
                        "fast, simple, and secure app \n" +
                        "we can use to message and\n" +
                        "call each other for free. Get it\n" +
                        "at https://Enclosure.com/dl/ ";
                Uri uri = Uri.parse("smsto:" + phoneNumber);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                sendIntent.putExtra("sms_body", message);
                holder.itemView.getContext().startActivity(sendIntent);


            }
        });

    }


    @Override
    public int getItemCount() {
        return contactGetList.size();
    }

    public void searchFilteredData(ArrayList<contactGetModel> filteredList) {
        this.contactGetList = filteredList;
        notifyDataSetChanged();
    }


    public static class myViewholder extends RecyclerView.ViewHolder {
        TextView userName, userInvite;

        public myViewholder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userInvite = itemView.findViewById(R.id.userInvite);

        }
    }
}
