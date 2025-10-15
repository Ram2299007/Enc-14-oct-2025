package com.Appzia.enclosure.Screens;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.Appzia.enclosure.Adapter.BlockedUserAdapter;
import com.Appzia.enclosure.Model.BlockedUserModel;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityBlockContactBinding;

import java.util.ArrayList;

public class block_contact_activity extends AppCompatActivity {

    ActivityBlockContactBinding binding;
    Context mContext;


    @Override
    protected void onResume() {
        super.onResume();

        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        Webservice.getBlockedUsers(mContext, uid, block_contact_activity.this,binding.progressBar);
    }


    public void Initializes() {

        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        Webservice.getBlockedUsers(mContext, uid, block_contact_activity.this,binding.progressBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBlockContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = block_contact_activity.this;

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void setAdapter(ArrayList<BlockedUserModel> blockedUserList) {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        BlockedUserAdapter adapter = new BlockedUserAdapter(this, blockedUserList,block_contact_activity.this);
        binding.recyclerview.setAdapter(adapter);

    }
}