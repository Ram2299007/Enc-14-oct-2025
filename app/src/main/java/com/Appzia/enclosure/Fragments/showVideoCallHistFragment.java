package com.Appzia.enclosure.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.FragmentShowVideoCallHistBinding;


public class showVideoCallHistFragment extends Fragment {

    FragmentShowVideoCallHistBinding binding;
    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY,Constant.otherBackValue);
            Constant.setSF.apply();

        }catch (Exception ignored){}
    }
    @Override
    public void onStart() {
        super.onStart();
        try{
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY,Constant.otherBackValue);
            Constant.setSF.apply();
        }catch (Exception ignored){}
    }
    @Override
    public void onResume() {
        super.onResume();
        try{
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY,Constant.otherBackValue);
            Constant.setSF.apply();
        }catch (Exception ignored){}
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentShowVideoCallHistBinding.inflate(inflater,container,false);

        mContext = binding.getRoot().getContext();

        binding.backarroe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.contactLayout,new videocallLogFragment()).commit();
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}