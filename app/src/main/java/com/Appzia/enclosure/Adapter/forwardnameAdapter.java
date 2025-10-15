package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.forwardnameModel;
import com.Appzia.enclosure.R;

import java.util.ArrayList;

public class forwardnameAdapter extends RecyclerView.Adapter<forwardnameAdapter.myViewHolder> {
    Context mContext;
    ArrayList<forwardnameModel> forwardNameList = new ArrayList<>();
    RecyclerView namerecyclerview;

    public forwardnameAdapter(Context mContext, ArrayList<forwardnameModel> forwardNameList, RecyclerView namerecyclerview) {
        this.mContext = mContext;
        this.forwardNameList = forwardNameList;
        this.namerecyclerview = namerecyclerview;
    }


    @NonNull
    @Override
    public forwardnameAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.forwardname_row, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull forwardnameAdapter.myViewHolder holder, int position) {

        final forwardnameModel model = forwardNameList.get(position);

        if(forwardNameList.size() ==1){
            holder.textView.setText(model.getName());
            namerecyclerview.smoothScrollToPosition(forwardNameList.size()-1);
        }else if(forwardNameList.size()>1){
            if(position == forwardNameList.size()-1){
                holder.textView.setText(model.getName());
                namerecyclerview.smoothScrollToPosition(forwardNameList.size()-1);
            }else{
                holder.textView.setText(model.getName()+" "+","+" ");
                namerecyclerview.smoothScrollToPosition(forwardNameList.size()-1);
            }

        }

    }

    @Override
    public int getItemCount() {
        return forwardNameList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
