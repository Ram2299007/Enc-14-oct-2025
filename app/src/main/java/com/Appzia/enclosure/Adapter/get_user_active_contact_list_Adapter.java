package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.chattingScreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class get_user_active_contact_list_Adapter extends RecyclerView.Adapter<get_user_active_contact_list_Adapter.myViewHolder> {

    Context mContext;
    ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list = new ArrayList<>();

    public get_user_active_contact_list_Adapter(Context mContext, ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list) {
        this.mContext = mContext;
        this.get_user_active_contact_list = get_user_active_contact_list;
    }

    public void searchFilteredData(ArrayList<get_user_active_contact_list_Model> filteredList) {
        this.get_user_active_contact_list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public get_user_active_contact_list_Adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_active_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull get_user_active_contact_list_Adapter.myViewHolder holder, int position) {

        final get_user_active_contact_list_Model model = get_user_active_contact_list.get(position);

        holder.contact1text.setText(model.getFull_name());


        holder.captiontext.setText(model.getCaption());
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
                intent.putExtra("msgLmtKey", model.getMsg_limit());
                intent.putExtra("ecKey", "ecKey");
                intent.putExtra("userFTokenKey", model.getF_token());
                intent.putExtra("deviceType", model.getDevice_type());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return get_user_active_contact_list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        androidx.appcompat.widget.AppCompatImageView contact1img;
        TextView contact1text;
        TextView captiontext;
        LinearLayout contact1;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            contact1img = itemView.findViewById(R.id.contact1img);
            contact1text = itemView.findViewById(R.id.contact1text);
            captiontext = itemView.findViewById(R.id.captiontext);
            contact1 = itemView.findViewById(R.id.contact1);

        }
    }
}
