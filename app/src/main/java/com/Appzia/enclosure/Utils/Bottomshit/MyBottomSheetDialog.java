package com.Appzia.enclosure.Utils.Bottomshit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.Appzia.enclosure.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyBottomSheetDialog extends BottomSheetDialogFragment {

    private int buttonYPosition;
    private int buttonHeight;

    // Create an instance with button position and height
    public static MyBottomSheetDialog newInstance(int buttonYPosition, int buttonHeight) {
        MyBottomSheetDialog fragment = new MyBottomSheetDialog();
        Bundle args = new Bundle();
        args.putInt("buttonYPosition", buttonYPosition);
        args.putInt("buttonHeight", buttonHeight);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            buttonYPosition = getArguments().getInt("buttonYPosition", 0);
            buttonHeight = getArguments().getInt("buttonHeight", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        // Set up close button
        Button closeButton = view.findViewById(R.id.buttonClose);
        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                // Set peek height to start exactly below the button
                behavior.setPeekHeight(buttonYPosition + buttonHeight);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                // Optional: Adjust max height to fit remaining screen space
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                behavior.setMaxHeight(screenHeight - buttonYPosition - buttonHeight);
            }
        }
    }
}