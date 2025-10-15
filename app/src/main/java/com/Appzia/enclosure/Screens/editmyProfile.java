package com.Appzia.enclosure.Screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
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
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.profilestatusAdapter;
import com.Appzia.enclosure.Model.profileDBModel;
import com.Appzia.enclosure.Model.profilestatusModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.ConnectivityReceiver;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.databinding.ActivityEditmyProfileBinding;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class editmyProfile extends AppCompatActivity implements ConnectivityReceiver.ConnectivityListener {
    ActivityEditmyProfileBinding binding;
    public static LinearLayout mulitidemo;
    public static LinearLayout one;
    public static LinearLayout two;
    public static Uri selectedImageUri;
    public static LinearLayout three;
    public static LinearLayout four;
    public static LinearLayout topper;
    public static Context mContext;
    public static int PICK_IMAGE = 5;
    public static int PICK_IMAGE_TWO = 10;
    public static ImageView profile;
    public static EditText pName;
    public static TextInputEditText pCaption;
    File imageFile, imageFile2, FullImageFile,FullImageFile2;
    public static ArrayList<profilestatusModel> profilestatusList;
    String uid;
    String checkOnActivityResukt = "";
    public static profilestatusAdapter adapter;
    public static RecyclerView multiimageRecyclerview;
    private ConnectivityReceiver connectivityReceiver;
    String themColor;
    ColorStateList tintList;
    Animation animation;

    @Override
    public void onConnectivityChanged(boolean isConnected) {

        if (isConnected) {

        } else {

            try {

                //TODO : GET PROFILE DETAILS
                Log.d("Network", "disconnected: " + "youFragment");
                binding.networkLoader.setVisibility(View.VISIBLE);
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                profileDBModel model = dbHelper.getAllDataProfile(uid);

                binding.name.setText(model.getFull_name());
                binding.caption.setText(model.getCaption());


                try {
                    binding.profile.setTag(model.getPhoto());
                    Picasso.get().load(model.getPhoto()).placeholder(R.drawable.inviteimg).into(binding.profile);
                } catch (Exception ignored) {
                }


                binding.profile.setVisibility(View.VISIBLE);

            } catch (Exception ignored) {
            }


            try {

                //TODO : GET PROFILE IMAGES STATUS
                Log.d("Network", "disconnected: " + "youFragment");
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                profilestatusList = dbHelper.getAllDataget_get_user_profile_imagesTablel(uid);
                Collections.reverse(profilestatusList);

                if(profilestatusList.size()>0){
                    setAdapter(profilestatusList);
                    binding.progressBar.setVisibility(View.GONE);
                }else{
                    binding.progressBar.setVisibility(View.GONE);
                }



                binding.four.setVisibility(View.GONE);
                binding.three.setVisibility(View.GONE);
                binding.two.setVisibility(View.GONE);
                binding.one.setVisibility(View.GONE);

                binding.topper.setVisibility(View.VISIBLE);
                binding.profile.setVisibility(View.VISIBLE);



            } catch (Exception ignored) {
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

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


        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");


        try {

            Constant.profilestatusList.clear();
            Webservice.get_user_profile_images(mContext, uid, editmyProfile.this,binding.progressBar);


        } catch (Exception ignored) {
        }

        try {


            if (checkOnActivityResukt.equals("")) {
                binding.profile.setImageURI(selectedImageUri);


                Webservice.get_profile_EditProfile(mContext, uid);


            } else if (checkOnActivityResukt.equals("ok")) {
                binding.profile.setImageURI(selectedImageUri);
            }

        } catch (Exception ignored) {
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditmyProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        mContext = editmyProfile.this;

        topper = (LinearLayout) findViewById(R.id.topper);
        mulitidemo = (LinearLayout) findViewById(R.id.mulitidemo);
        one = (LinearLayout) findViewById(R.id.one);
        two = (LinearLayout) findViewById(R.id.two);
        three = (LinearLayout) findViewById(R.id.three);
        four = (LinearLayout) findViewById(R.id.four);

        pName = findViewById(R.id.name);
        pCaption = findViewById(R.id.caption);
        profile = findViewById(R.id.profile);
        multiimageRecyclerview = findViewById(R.id.multiimageRecyclerview);
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityListener(this);
        profilestatusList = new ArrayList<>();


        animation = AnimationUtils.loadAnimation(this, R.anim.custom_animation);



        //todo --------------------------------online-----------------------------d


        Log.d("Network", "connected: " + "youFragment");
        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");
        binding.networkLoader.setVisibility(View.GONE);
        try {

            //TODO : - offline call first for only profiles image

            Log.d("Network", "disconnected: " + "youFragment");
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            profileDBModel model = dbHelper.getAllDataProfile(uid);

            if (model != null) {
                binding.name.setText(model.getFull_name());
                binding.caption.setText(model.getCaption());


                try {
                    binding.profile.setTag(model.getPhoto());
                    Picasso.get().load(model.getPhoto()).placeholder(R.drawable.inviteimg).into(binding.profile);
                } catch (Exception ignored) {
                }

                binding.profile.setVisibility(View.VISIBLE);


            } else {
            }


        } catch (Exception ignored) {
        }


        try {
            //TODO : - offline call first for only status images
            Log.d("Network", "disconnected: " + "youFragment");
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            profilestatusList = dbHelper.getAllDataget_get_user_profile_imagesTablel(uid);


            Collections.reverse(profilestatusList);
            if (profilestatusList.size() > 0) {
                setAdapter(profilestatusList);
                binding.progressBar.setVisibility(View.GONE);

                binding.four.setVisibility(View.GONE);
                binding.three.setVisibility(View.GONE);
                binding.two.setVisibility(View.GONE);
                binding.one.setVisibility(View.GONE);

                binding.topper.setVisibility(View.VISIBLE);
                binding.profile.setVisibility(View.VISIBLE);

            } else {
                binding.progressBar.setVisibility(View.GONE);
            }


        } catch (Exception ignored) {
        }


        Constant.getSfFuncion(mContext);
        uid = Constant.getSF.getString(Constant.UID_KEY, "");


        try {

            Constant.profilestatusList.clear();
            Webservice.get_user_profile_images(mContext, uid, editmyProfile.this, binding.progressBar);


        } catch (Exception ignored) {
        }

        try {


            if (checkOnActivityResukt.equals("")) {
                binding.profile.setImageURI(selectedImageUri);


                Webservice.get_profile_EditProfile(mContext, uid);


            } else if (checkOnActivityResukt.equals("ok")) {
                binding.profile.setImageURI(selectedImageUri);
            }

        } catch (Exception ignored) {
        }

        //todo --------------------------------online-----------------------------d

        // Set animation listener
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Finish activity when animation ends
                SwipeNavigationHelper.finishWithSwipe(editmyProfile.this);
                overridePendingTransition(0, 0); // Disable default activity transition
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(connectivityReceiver, filter,mContext.RECEIVER_EXPORTED);

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();


            }
        });

        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, show_image_Screen.class);
                intent.putExtra("imageKey", binding.profile.getTag().toString());
                SwipeNavigationHelper.startActivityWithSwipe(editmyProfile.this, intent);
            }
        });


        binding.setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.profile.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                SwipeNavigationHelper.startActivityForResultWithSwipe(editmyProfile.this, Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

              //  CropImage.activity().start(((Activity) mContext));


            }
        });
        binding.pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                SwipeNavigationHelper.startActivityForResultWithSwipe(editmyProfile.this, Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_TWO);
            }
        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Webservice.delete_user_profile_image(mContext, uid);


            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.name.getText().toString().trim().isEmpty()) {
                    Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                    binding.name.setError("Missing name ?", customErrorDrawable);
                    binding.name.setText("");

                    binding.name.requestFocus();
                } else if (binding.caption.getText().toString().trim().length() > 300) {
                    Drawable customErrorDrawable = mContext.getResources().getDrawable(R.drawable.circlewarn);
                    customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                    binding.caption.setError("Caption must be less than 300 !", customErrorDrawable);
                    binding.caption.requestFocus();
                } else {


                    long fileSize = 1;
                    long fileSize2 = 1;
                    try {
                        fileSize = getFileSize(FullImageFile.getPath());
                        fileSize2 = getFileSize(imageFile.getPath());
                    } catch (Exception ignored) {
                        fileSize = 1;
                        fileSize2 = 1;
                    }


                    if (fileSize > 0) {
                        System.out.println("File size: " + getFormattedFileSize(fileSize));
                        System.out.println("File size: " + getFormattedFileSize(fileSize2));


                        if (fileSize > 200 * 1024) {

                            //jar data kami asel 200 kb peksha tr imageFile
//                                Toast.makeText(editmyProfile.this, "above 200", Toast.LENGTH_SHORT).show();
                            Webservice.profile_update(mContext, uid, binding.name.getText().toString().trim(), binding.caption.getText().toString().trim(), imageFile,findViewById(R.id.includedToast),findViewById(R.id.toastText));


                        } else {
                            //jar data jast asel 200 kb peksha tr FullImageFile
                            //  Toast.makeText(editmyProfile.this, "below 200", Toast.LENGTH_SHORT).show();
                            Webservice.profile_update(mContext, uid, binding.name.getText().toString().trim(), binding.caption.getText().toString().trim(), FullImageFile,findViewById(R.id.includedToast),findViewById(R.id.toastText));

                        }
                    } else {
                        System.out.println("File not found.");
                    }


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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {


            checkOnActivityResukt = "ok";
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                selectedImageUri = resultUri;
                // Toast.makeText(mContext, resultUri.toString(), Toast.LENGTH_SHORT).show();
                Glide.with(this).load(resultUri).placeholder(R.drawable.inviteimg).into(binding.profile);

                String extension;
                File f, f2;
                if (selectedImageUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    extension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(selectedImageUri));

                } else {
                    extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(selectedImageUri.getPath()))));

                }


                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(
                            selectedImageUri);
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
                            selectedImageUri);
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
                    FullImageFile = f2;
                    long fileSize = getFileSize(FullImageFile.getPath());
                    Log.d("File size Full", getFormattedFileSize(fileSize));
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onActivityResult: " + e.getMessage());

                }
            }



        } else if (requestCode == PICK_IMAGE_TWO && resultCode == RESULT_OK) {
            assert data != null;
            Uri selectedImageUri = data.getData();
            String extension;
            File f, f2;
            if (selectedImageUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                extension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(selectedImageUri));

            } else {
                extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(selectedImageUri.getPath()))));

            }


            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(
                        selectedImageUri);
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
                imageFile2 = f;

                long fileSize = getFileSize(imageFile2.getPath());
                Log.d("File size compressed", getFormattedFileSize(fileSize));


            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onActivityResult: " + e.getMessage());

            }


            InputStream imageStream2 = null;
            try {
                imageStream2 = getContentResolver().openInputStream(
                        selectedImageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bmpFullSize = BitmapFactory.decodeStream(imageStream2);
            Log.d("extension", extension);
            f2 = new File(getCacheDir() + "/temp2." + extension);

            try {
                //part1
                ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
                bmpFullSize.compress(Bitmap.CompressFormat.JPEG, 80, bos2);
                byte[] bitmapdataFull = bos2.toByteArray();
                FileOutputStream fos2 = new FileOutputStream(f2);
                fos2.write(bitmapdataFull);
                fos2.flush();
                fos2.close();

                Log.d("imageFile111", f.getPath());
                FullImageFile2 = f2;
                long fileSize = getFileSize(FullImageFile2.getPath());
                Log.d("File size Full", getFormattedFileSize(fileSize));


            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onActivityResult: " + e.getMessage());

            }

            long fileSize = getFileSize(FullImageFile2.getPath());
            long fileSize2 = getFileSize(imageFile2.getPath());


            if (fileSize > 0) {
                System.out.println("File size: " + getFormattedFileSize(fileSize));
                System.out.println("File size: " + getFormattedFileSize(fileSize2));


                if (fileSize > 200 * 1024) {

                    Webservice.upload_user_profile_images(mContext, uid, imageFile2, editmyProfile.this,binding.progressBar);
                } else {

                    Webservice.upload_user_profile_images(mContext, uid, FullImageFile2, editmyProfile.this, binding.progressBar);

                }
            } else {
                System.out.println("File not found.");
            }
        }
    }


    @Override
    public void onBackPressed() {
        SwipeNavigationHelper.finishWithSwipe(editmyProfile.this);
      //  binding.animImg.startAnimation(animation);

       // TransitionHelper.performTransition(((Activity)mContext));
    }


    public static void setAdapter(ArrayList<profilestatusModel> profilestatusLists) {
        editmyProfile.profilestatusList = profilestatusLists;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        multiimageRecyclerview.setLayoutManager(linearLayoutManager);
        adapter = new profilestatusAdapter(mContext, profilestatusList);
        multiimageRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(connectivityReceiver);
    }

}