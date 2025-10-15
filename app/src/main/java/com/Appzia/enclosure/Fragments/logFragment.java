package com.Appzia.enclosure.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.databinding.FragmentLogBinding;


public class logFragment extends Fragment {

    FragmentLogBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}