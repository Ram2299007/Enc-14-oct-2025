package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;
import com.Appzia.enclosure.Utils.SmoothNavigationHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.ActivityShowImageScreenBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

public class show_image_Screen extends AppCompatActivity {
    ActivityShowImageScreenBinding binding;
    Context mContext;
    String imageKey, youKey;
    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;


    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }


        String viewHolderTypeKey = getIntent().getStringExtra("viewHolderTypeKey");

        if (viewHolderTypeKey != null) {

            if (viewHolderTypeKey.equals(Constant.senderViewHolder)) {


                File customFolder;
                String exactPath = null;
                //android 10
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                    exactPath = customFolder.getAbsolutePath();

                } else {
                    customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
                    exactPath = customFolder.getAbsolutePath();

                }

                String imageKey = getIntent().getStringExtra("imageKey");


                if (doesFileExist(exactPath + "/" + imageKey)) {
                //    Toast.makeText(mContext, "exist", Toast.LENGTH_SHORT).show();
                    binding.imageview.setImageURI(Uri.parse(exactPath + "/" + imageKey));
                } else {
              //      Toast.makeText(mContext, "not exist", Toast.LENGTH_SHORT).show();
                    String imagePath = getIntent().getStringExtra("imagePath");
                    Picasso.get().load(imagePath).into(binding.imageview);
                }

            } else if (viewHolderTypeKey.equals(Constant.receiverViewHolder)) {


                File customFolder;
                String exactPath = null;
                //android 10
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                    exactPath = customFolder.getAbsolutePath();

                } else {
                    customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                    exactPath = customFolder.getAbsolutePath();

                }

                String imageKey = getIntent().getStringExtra("imageKey");
                String imagePath = getIntent().getStringExtra("imagePath");


                if (doesFileExist(exactPath + "/" + imageKey)) {
                    binding.imageview.setImageURI(Uri.parse(exactPath + "/" + imageKey));
                } else {

                    Picasso.get().load(imagePath).into(binding.imageview);
                }

            } else {
                String imageKey = getIntent().getStringExtra("imageKey");
                Picasso.get().load(imageKey).into(binding.imageview);
            }
        } else {
            String imageKey = getIntent().getStringExtra("imageKey");
            try {
                Picasso.get().load(imageKey).into(binding.imageview);
            } catch (Exception e) {

            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowImageScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Prevent screenshots and screen recording
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        mContext = binding.getRoot().getContext();

        // Apply theme color to menuPoint
        Constant.getSfFuncion(mContext);
        String themeColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        binding.menuPoint.setColorFilter(Color.parseColor(themeColor));

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // Show menu only if coming from chat adapters (senderViewHolder or receiverViewHolder)
        String viewHolderTypeKey = getIntent().getStringExtra("viewHolderTypeKey");
        if (viewHolderTypeKey != null && 
            (viewHolderTypeKey.equals(Constant.senderViewHolder) || viewHolderTypeKey.equals(Constant.receiverViewHolder))) {
            binding.menu.setVisibility(View.VISIBLE);
            
            binding.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.dialogueLayoutShowImageScreen(mContext, R.layout.refresh_ly);
                    Dialog dialogueLayoutCLearLogUp = Constant.dialogLayoutColor;
                    dialogueLayoutCLearLogUp.show();
                    LinearLayout save = dialogueLayoutCLearLogUp.findViewById(R.id.clearcalllog);
                    // 1B1C1C
                    save.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B1C1C")));

                    TextView textView = dialogueLayoutCLearLogUp.findViewById(R.id.textView);

                    textView.setText("Save");
                    textView.setTextColor(Color.parseColor("#ffffff"));
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveImageToGallery();
                            Constant.dialogLayoutColor.dismiss();
                        }
                    });

                }
            });
        } else {
            // Hide menu if not coming from chat adapters
            binding.menu.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        youKey = getIntent().getStringExtra("youKey");
        String grpKey = getIntent().getStringExtra("grpKey");


        if (youKey != null) {
            if (youKey.equals("youKey")) {
                SmoothNavigationHelper.finishWithSlideToRight(show_image_Screen.this);
               
            } else if (youKey.equals("chatKey")) {
                SmoothNavigationHelper.finishWithSlideToRight(show_image_Screen.this);
               
            }
        } else {

            if(grpKey!=null){
                if(grpKey.equals("grpKey")){

                    SmoothNavigationHelper.finishWithSlideToRight(show_image_Screen.this);
                }else {
                    SmoothNavigationHelper.finishWithSlideToRight(show_image_Screen.this);
                }
            }else {
                SmoothNavigationHelper.finishWithSlideToRight(show_image_Screen.this);
            }




           
        }

    }

    public boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }


    private void viewTransformation(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                //       d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
//                        if (lastEvent != null) {
//                            newRot = rotation(event);
//                            view.setRotation((float) (view.getRotation() + (newRot - d)));
//                        }
                    }
                }
                break;
        }
    }

//    private float rotation(MotionEvent event) {
//        double delta_x = (event.getX(0) - event.getX(1));
//        double delta_y = (event.getY(0) - event.getY(1));
//        double radians = Math.atan2(delta_y, delta_x);
//        return (float) Math.toDegrees(radians);
//    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void saveImageToGallery() {
        try {
            String viewHolderTypeKey = getIntent().getStringExtra("viewHolderTypeKey");
            String imageKey = getIntent().getStringExtra("imageKey");
            
            if (imageKey == null) {
                showToast("Image not found");
                return;
            }

            Bitmap bitmap = null;
            String fileName = "Enclosure_" + System.currentTimeMillis() + ".jpg";

            if (viewHolderTypeKey != null) {
                if (viewHolderTypeKey.equals(Constant.senderViewHolder) || viewHolderTypeKey.equals(Constant.receiverViewHolder)) {
                    // Handle local files
                    File customFolder;
                    String exactPath = null;
                    
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                        exactPath = customFolder.getAbsolutePath();
                    } else {
                        customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
                        exactPath = customFolder.getAbsolutePath();
                    }

                    String localImagePath = exactPath + "/" + imageKey;
                    if (doesFileExist(localImagePath)) {
                        bitmap = BitmapFactory.decodeFile(localImagePath);
                    }
                }
            }

            // If bitmap is null, try to load from URL
            if (bitmap == null) {
                try {
                    // For URL images, we need to download them first
                    // This is a simplified approach - in production, you might want to use a proper image loading library
                    bitmap = Picasso.get().load(imageKey).get();
                } catch (Exception e) {
                    showToast("Failed to load image");
                    return;
                }
            }

            if (bitmap != null) {
                saveBitmapToGallery(bitmap, fileName);
            } else {
                showToast("Failed to load image");
            }

        } catch (Exception e) {
            showToast("Failed to save image");
        }
    }

    private void saveBitmapToGallery(Bitmap bitmap, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10+ (API 29+)
                ContentResolver resolver = mContext.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Enclosure");

                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                if (imageUri != null) {
                    OutputStream outputStream = resolver.openOutputStream(imageUri);
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                        showToast("Image saved");
                    } else {
                        showToast("Failed to save image");
                    }
                } else {
                    showToast("Failed to save image");
                }
            } else {
                // For older Android versions
                String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File imageFile = new File(imagesDir, fileName);
                
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();

                // Notify media scanner
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(imageFile));
                mContext.sendBroadcast(mediaScanIntent);
                
                showToast("Image saved");
            }
        } catch (Exception e) {
            showToast("Failed to save image");
        }
    }

    private void showToast(String message) {
        Constant.showCustomToast(message, findViewById(R.id.includedToast), findViewById(R.id.toastText));
    }
}