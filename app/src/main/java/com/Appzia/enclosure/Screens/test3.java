package com.Appzia.enclosure.Screens;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.EdgeToEdgeHelper;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.LinearLayout;

import com.Appzia.enclosure.Adapter.emojiAdapter;
import com.Appzia.enclosure.Model.Emoji;
import com.Appzia.enclosure.R;

import com.Appzia.enclosure.databinding.ActivityTest3Binding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

public class test3 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);

        // Setup edge-to-edge display
        EdgeToEdgeHelper.setupEdgeToEdge(this, findViewById(R.id.main));
    }
}