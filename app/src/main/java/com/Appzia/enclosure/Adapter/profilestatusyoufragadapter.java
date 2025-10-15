package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.profilestatusModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Utils.Constant;
import com.squareup.picasso.Picasso;

public class profilestatusyoufragadapter extends RecyclerView.Adapter<profilestatusyoufragadapter.myViewHolder> {

    Context mContext;

    public profilestatusyoufragadapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public profilestatusyoufragadapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_status_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull profilestatusyoufragadapter.myViewHolder holder, int position) {

        final profilestatusModel model = Constant.profilestatusList.get(position);

        Picasso.get().load(model.getPhoto()).into(holder.photo);
        holder.id.setText(model.getId());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final Dialog dialogLayout = new Dialog(holder.itemView.getContext());
//                dialogLayout.setContentView(R.layout.status_view_row);
//                dialogLayout.setCanceledOnTouchOutside(true);
//                dialogLayout.setCancelable(true);
//                dialogLayout.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//                dialogLayout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                dialogLayout.getWindow().setGravity(Gravity.CENTER);
//                ImageView imageView = dialogLayout.findViewById(R.id.showImg);
//                String image = model.getPhoto();
//
//                Glide.with(holder.itemView.getContext()).load(image).into(imageView);
//
//                dialogLayout.show();
//
//                Window window = dialogLayout.getWindow();
//                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Intent intent = new Intent(mContext, show_image_Screen.class);
                intent.putExtra("imageKey", model.getPhoto());
                mContext.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return Constant.profilestatusList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView id;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            id = itemView.findViewById(R.id.id);
        }
    }

    public void removeItem(int position) {
        Constant.profilestatusList.remove(position);
        notifyDataSetChanged();

    }
}
