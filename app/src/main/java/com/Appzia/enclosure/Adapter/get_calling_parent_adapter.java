package com.Appzia.enclosure.Adapter;

import static com.Appzia.enclosure.Adapter.get_calling_parent_adapter.myviewholder.nonactiveRecyclerview;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.contact_parent_model;
import com.Appzia.enclosure.Model.get_contact_invite_model;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.R;


import java.util.ArrayList;

public class get_calling_parent_adapter extends RecyclerView.Adapter<get_calling_parent_adapter.myviewholder> {

    Context mContext;
    ArrayList<contact_parent_model> contact_parent_model_List = new ArrayList<>();
    calling_child_adapter calling_child_adapter;
    calling_invite_child_adapter calling_invite_child_adapter;

    contact_parent_model model;
    boolean isLoading = false;
    private int currentPage = 1; // Track the current page of data
    public get_calling_parent_adapter(Context mContext, ArrayList<contact_parent_model> contact_parent_model_List) {
        this.mContext = mContext;
        this.contact_parent_model_List = contact_parent_model_List;


    }

    @NonNull
    @Override
    public get_calling_parent_adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_layout, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull get_calling_parent_adapter.myviewholder holder, int position) {


        model = contact_parent_model_List.get(position);
        //  Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();

        calling_child_adapter = new calling_child_adapter(mContext, model.getGet_contact_modelList());
        holder.activeRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        holder.activeRecyclerview.setAdapter(calling_child_adapter);
        calling_child_adapter.notifyDataSetChanged();

        calling_invite_child_adapter = new calling_invite_child_adapter(mContext, model.getGet_contact_invite_modelList());
        nonactiveRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        nonactiveRecyclerview.setAdapter(calling_invite_child_adapter);
        calling_invite_child_adapter.notifyDataSetChanged();


    }


    @Override
    public int getItemCount() {
        return contact_parent_model_List.size();
    }


    // add in webservice


    public void filteredListActive(String newText) {
        ArrayList<get_contact_model> filteredList = new ArrayList<>();

        for (get_contact_model list : model.getGet_contact_modelList()) {
            if (list.getFull_name().toLowerCase().contains(newText.toLowerCase())) {

                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {
            // Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            calling_child_adapter.searchFilteredData(filteredList);
        }
    }

    public void filteredListInvite(String newText) {
        ArrayList<get_contact_invite_model> filteredList = new ArrayList<>();

        for (get_contact_invite_model list : model.getGet_contact_invite_modelList()) {
            if (list.getContact_name().toLowerCase().contains(newText.toLowerCase())) {

                filteredList.add(list);
            }
        }

        if (filteredList.isEmpty()) {
            // Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            calling_invite_child_adapter.searchFilteredData(filteredList);
        }
    }


    public static class myviewholder extends RecyclerView.ViewHolder {
        RecyclerView activeRecyclerview;
        public static RecyclerView nonactiveRecyclerview;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            activeRecyclerview = itemView.findViewById(R.id.activeRecyclerview);
            nonactiveRecyclerview = itemView.findViewById(R.id.nonactiveRecyclerview);
        }
    }

}






