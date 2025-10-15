package com.Appzia.enclosure.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.chattingScreen;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class calling_child_adapter extends RecyclerView.Adapter<calling_child_adapter.myviewholder> {

    Context mContext;
    ArrayList<get_contact_model> get_contact_model_List = new ArrayList<>();



    public calling_child_adapter(Context mContext, ArrayList<get_contact_model> get_contact_model_List) {
        this.mContext = mContext;
        this.get_contact_model_List = get_contact_model_List;

    }

    @NonNull
    @Override
    public calling_child_adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_active_row, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull calling_child_adapter.myviewholder holder, int position) {

        final get_contact_model model = get_contact_model_List.get(position);

        holder.contact1text.setText(model.getFull_name());

        if (holder.contact1text.getText().toString().length() > 20) {
            String truncatedText = holder.contact1text.getText().toString().substring(0, 20) + "..."; // Add dots at the end
            holder.contact1text.setText(truncatedText);
        } else {
            holder.contact1text.setText(holder.contact1text.getText().toString());
        }



        holder.captiontext.setText(model.getCaption());

        if (holder.captiontext.getText().toString().length() > 35) {
            String truncatedText = holder.captiontext.getText().toString().substring(0, 35) + "..."; // Add dots at the end
            holder.captiontext.setText(truncatedText);
        } else {
            holder.captiontext.setText(holder.captiontext.getText().toString());
        }

        Picasso.get().load(model.getPhoto()).into(holder.contact1img);


        //   Toast.makeText(mContext, "success2", Toast.LENGTH_SHORT).show();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, chattingScreen.class);
                intent.putExtra("nameKey", model.getFull_name());
                intent.putExtra("captionKey", model.getCaption());
                intent.putExtra("photoKey", model.getPhoto());
                intent.putExtra("friendUidKey", model.getUid());
                intent.putExtra("msgLmtKey", "0");
                intent.putExtra("ecKey", "ecKey");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return get_contact_model_List.size();
    }

    public void searchFilteredData(ArrayList<get_contact_model> filteredList) {
        this.get_contact_model_List = filteredList;
        notifyDataSetChanged();
    }

    public static class myviewholder extends RecyclerView.ViewHolder {

        androidx.appcompat.widget.AppCompatImageView contact1img;
        TextView contact1text;
        TextView captiontext;
        LinearLayout contact1;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            contact1img = itemView.findViewById(R.id.contact1img);
            contact1text = itemView.findViewById(R.id.contact1text);
            captiontext = itemView.findViewById(R.id.captiontext);
            contact1 = itemView.findViewById(R.id.contact1);
        }
    }
}
