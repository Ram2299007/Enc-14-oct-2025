package com.Appzia.enclosure.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.get_user_active_contact_list_Adapter_forGrp;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityNewGroupBinding;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class newGroupActivity extends AppCompatActivity implements get_user_active_contact_list_Adapter_forGrp.OnDataClickListener, ConnectivityReceiver.ConnectivityListener {

    ActivityNewGroupBinding binding;

    public static TextView selectednumbers;


    public static RecyclerView grpRecyclerview;

    public static File imageFile, FullImageFile;

    String invited_friend_list = "NODATA";
    String themColor;
    ColorStateList tintList;
    int count = 0;
    Context mContext;
    public static String fontSizePref;
    String uid;
    get_user_active_contact_list_Adapter_forGrp adapter;
    private ConnectivityReceiver connectivityReceiver;
    ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list;

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected) {
        } else {
            Log.d("Network", "disConnected: " + "newGroupActivity");
            binding.networkLoader.setVisibility(View.VISIBLE);
            try {

                get_user_active_contact_list.clear();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                get_user_active_contact_list = dbHelper.getAllDataInsideGroup();
                if (get_user_active_contact_list.size() > 0) {
                    setAdapter(get_user_active_contact_list);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                }


            } catch (Exception ignored) {
            }
        }
    }




    @Override
    public void onStart() {
        super.onStart();

        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (fontSizePref.equals(Constant.small)) {
                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.cont.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.slelctedlist.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.edittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.total.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.selectednumbers.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.cont.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.slelctedlist.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.edittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.total.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                binding.selectednumbers.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.cont.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.slelctedlist.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.edittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                binding.total.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                binding.selectednumbers.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);

            }
        } catch (Exception ignored) {

        }


        assert getFragmentManager() != null;

        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));

            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else if (themColor.equals("#00A3E9")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else if (themColor.equals("#ec0001")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else if (themColor.equals("#16f3ff")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else if (themColor.equals("#FF8A00")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7F7F7F")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else if (themColor.equals("#346667")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else if (themColor.equals("#9846D9")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else if (themColor.equals("#A81010")) {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                } else {
                    binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor(themColor), PorterDuff.Mode.SRC_ATOP);
                    binding.group.setBackgroundTintList(tintList);

                }
            } catch (Exception ignored) {
                binding.edittext.getBackground().mutate().setColorFilter(Color.parseColor("#00A3E9"), PorterDuff.Mode.SRC_ATOP);
                binding.group.setBackgroundTintList(tintList);

            }


        } catch (Exception ignored) {
        }


        //TODO : for only network loader Themes

        try {

            Constant.getSfFuncion(getApplicationContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            try {
                if (themColor.equals("#ff0080")) {

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FF6D00"));
                } else if (themColor.equals("#00A3E9")) {

                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                } else if (themColor.equals("#7adf2a")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00C853"));

                } else if (themColor.equals("#ec0001")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#ec7500"));

                } else if (themColor.equals("#16f3ff")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00F365"));

                } else if (themColor.equals("#FF8A00")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#FFAB00"));

                } else if (themColor.equals("#7F7F7F")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#314E6D"));

                } else if (themColor.equals("#D9B845")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#b0d945"));
                } else if (themColor.equals("#346667")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#729412"));

                } else if (themColor.equals("#9846D9")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#d946d1"));

                } else if (themColor.equals("#A81010")) {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#D85D01"));

                } else {


                    binding.networkLoader.setTrackColor(Color.parseColor(themColor));
                    binding.networkLoader.setIndicatorColor(Color.parseColor("#00BFA5"));
                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

                // Set light status bar (white text/icons) for dark mode
                getWindow().getDecorView().setSystemUiVisibility(0); // Clears LIGHT_STATUS_BAR
            }
        }else{
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

                // Set dark status bar (black text/icons) for light mode
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }


        selectednumbers = findViewById(R.id.selectednumbers);
        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        mContext = binding.getRoot().getContext();

        grpRecyclerview = findViewById(R.id.grpRecyclerview);
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        get_user_active_contact_list = new ArrayList<>();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter, mContext.RECEIVER_EXPORTED);

        //todo --------------------------online


        binding.networkLoader.setVisibility(View.GONE);
        try {
            //TODO : - FOR OFFLINE FIRST


            get_user_active_contact_list.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            get_user_active_contact_list = dbHelper.getAllDataInsideGroup();
            if (get_user_active_contact_list.size() > 0) {
                setAdapter(get_user_active_contact_list);

                binding.grpRecyclerview.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                binding.noData.setVisibility(View.GONE);

            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.noData.setVisibility(View.VISIBLE);
                binding.grpRecyclerview.setVisibility(View.GONE);
            }


        } catch (Exception ignored) {
        }


        Log.d("Network", "connected: " + "newGroupActivity");
        selectednumbers = findViewById(R.id.selectednumbers);
        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");

        try {

            Webservice.get_user_active_chat_list_For_GRP(mContext, uid, newGroupActivity.this, binding.progressBar, binding.noData);

        } catch (Exception ignored) {
        }
        //todo --------------------------online
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edittext.getText().toString().isEmpty()) {
                    Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                    binding.edittext.setError("Missing group name", customErrorDrawable);
                    binding.edittext.requestFocus();
                } else if (invited_friend_list.equals("NODATA")) {
                    Toast.makeText(mContext, "Please add contacts to create group", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if (FullImageFile != null || imageFile != null) {
                            long fileSize = getFileSize(FullImageFile.getPath());
                            long fileSize2 = getFileSize(imageFile.getPath());

                            if (fileSize > 0) {
                                System.out.println("File size: " + getFormattedFileSize(fileSize));
                                System.out.println("File size: " + getFormattedFileSize(fileSize2));


                                if (fileSize > 200 * 1024) {
                                    //  Toast.makeText(mContext, "enbtry1", Toast.LENGTH_SHORT).show();
                                    Webservice.create_group_for_chatting(mContext, uid, binding.edittext.getText().toString(), invited_friend_list, imageFile);

                                } else {
                                    //jar data jast asel 200 kb peksha tr FullImageFile
                                    //    Toast.makeText(mContext, "enbtry2", Toast.LENGTH_SHORT).show();
                                    Webservice.create_group_for_chatting(mContext, uid, binding.edittext.getText().toString(), invited_friend_list, FullImageFile);

                                }
                            } else {
                                //  Toast.makeText(mContext, "enbtry3", Toast.LENGTH_SHORT).show();
                                System.out.println("File not found.");
                            }

                        } else {
                            Webservice.create_group_for_chatting(mContext, uid, binding.edittext.getText().toString(), invited_friend_list, imageFile);

                        }


                    } catch (Exception ignored) {
                    }
                }

            }
        });

        binding.group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFile = null;
                binding.selectednumbers.setText("0");
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a Image to Upload"),
                            1);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    // Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }

    public static String getFormattedFileSize(long fileSize) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = fileSize;

        while (size > 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", size, units[unitIndex]);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            //very important for uplopading multiple data

            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    binding.imgIcon.setImageURI(uri);
                    imageFile = null;
                    Log.d("ImageFile000", uri.getAuthority());
                    Log.d("ImageFile000", uri.getScheme());

                    String extension;
                    File f, f2;
                    if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                        final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                        extension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(uri));

                    } else {
                        extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(uri.getPath()))));

                    }

                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(
                                uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bmpCompresssSize = BitmapFactory.decodeStream(imageStream);
                    Log.d("extension", extension);
                    f = new File(getCacheDir() + "/temp." + extension);

                    try {
                        //part1
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bmpCompresssSize.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                        byte[] bitmapdataCompressed = bos.toByteArray();
                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(bitmapdataCompressed);
                        fos.flush();
                        fos.close();

                        Log.d("imageFile111", f.getPath());
                        imageFile = f;

                        long fileSize = getFileSize(imageFile.getPath());
                        Log.d("File size compressed", getFormattedFileSize(fileSize));


                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onActivityResult: " + e.getMessage());

                    }


                    InputStream imageStream2 = null;
                    try {
                        imageStream2 = getContentResolver().openInputStream(
                                uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bmpFullSize = BitmapFactory.decodeStream(imageStream2);
                    Log.d("extension", extension);
                    f2 = new File(getCacheDir() + "/temp2." + extension);

                    try {
                        //part2
                        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
                        bmpFullSize.compress(Bitmap.CompressFormat.JPEG, 80, bos2);
                        byte[] bitmapdataFull = bos2.toByteArray();
                        FileOutputStream fos2 = new FileOutputStream(f2);
                        fos2.write(bitmapdataFull);
                        fos2.flush();
                        fos2.close();

                        Log.d("imageFile111", f.getPath());
                        FullImageFile = f2;
                        long fileSize = getFileSize(FullImageFile.getPath());
                        Log.d("File size Full", getFormattedFileSize(fileSize));


                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onActivityResult: " + e.getMessage());

                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static Drawable uriToDrawable(@NonNull Context context, @NonNull Uri uri) {
        try {
            Drawable drawable;

            // If the URI is a resource URI (e.g., "android.resource://your.package.name/drawable/image_name")
            if (uri.getScheme().equals("android.resource")) {
                drawable = ContextCompat.getDrawable(context, Integer.parseInt(uri.getLastPathSegment()));
            } else {
                // Otherwise, load the image from the URI
                drawable = Drawable.createFromStream(context.getContentResolver().openInputStream(uri), null);
            }

            return drawable;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBackPressed() {

        SwipeNavigationHelper.finishWithSwipe(newGroupActivity.this);

    }

    public void setAdapter(ArrayList<get_user_active_contact_list_Model> get_user_active_contact_lists) {
        // Get current user UID from shared preferences
        Constant.getSfFuncion(mContext);
        String currentUid = Constant.getSF.getString(Constant.UID_KEY, "");

        // Filter out current user and blocked users
        ArrayList<get_user_active_contact_list_Model> filteredList = new ArrayList<>();
        for (get_user_active_contact_list_Model model : get_user_active_contact_lists) {
            if (!model.getUid().equals(currentUid) && !model.isBlock()) {
                filteredList.add(model);
            }
        }

        // Set the filtered list
        this.get_user_active_contact_list = filteredList;

        // Set up RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.grpRecyclerview.setLayoutManager(linearLayoutManager);

        adapter = new get_user_active_contact_list_Adapter_forGrp(mContext, get_user_active_contact_list);
        adapter.setOnDataClickListener(this);
        binding.grpRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onDataClick(String data) {

        this.invited_friend_list = data;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(connectivityReceiver);
    }
}