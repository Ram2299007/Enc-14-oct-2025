package com.Appzia.enclosure.Utils;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Appzia.enclosure.Adapter.chatAdapter;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.chattingScreen;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HalfSwipeCallback extends ItemTouchHelper.Callback {


    private Context context;
    LinearLayout replylyout;
    LinearLayout bottom;
    int pixels = 0;
    EditText messageBox;
    ColorStateList tintList;
    TextView namereply;
    TextView msgreply;
    ImageView imgreply;
    View view, view2, view3;
    ImageView cancel;
    private static final int SWIPE_DISTANCE_DP = 60;
    private boolean toastShown = false;
    private float swipeDistanceInPixels;
    private boolean isExpanding = false; // Flag to prevent multiple expansions

    String themColor;
    ImageView replysvg;
    RecyclerView messageRecView;
    chatAdapter chatAdapter;
    LinearLayout contactContainer;
    TextView firstText;

    CardView imgcardview;
    LinearLayout pageLyt;
    TextView pageText;
    TextView fileNameTextview;
    TextView thumbnailFileNameTextview;
    TextView documentTextview;
    TextView replyDataTypeTv, listcrntpostion;

    public static String images;
    public static String ImageWidth;
    public static String ImageHeight;
    public static String AspectRatio;
    public static String ThumbnailImage;
    public static String micePhoto;
    public static String miceTiming;
    public static TextView captionTextTxtview;
    public static String Cname;
    public static String Cphone;
    public static String extension;
    public static ArrayList<messageModel> messageListd = new ArrayList<>();
    public static TextView name;
    public static TextView thumbnailimagedata;
    TextView imageWidthDp;
    TextView imageHeightDp;
    TextView aspectRatio;
    boolean iamblocked;
    LinearLayout editLyt;
    View bottomTiming;
    View progressBar;
    CardView imgcardviewVoiceAudio;
    CardView imgcardviewVoiceMusic;

    public HalfSwipeCallback(Context context, LinearLayout replylyout, EditText messageBox, TextView namereply, TextView msgreply, ImageView imgreply, View view, ImageView cancel, ImageView replysvg, View view2, View view3, LinearLayout bottom, RecyclerView messageRecView, chatAdapter chatAdapter, LinearLayout contactContainer, TextView firstText, CardView imgcardview, LinearLayout pageLyt, TextView pageText, TextView replyDataTypeTv, TextView listcrntpostion, ArrayList<messageModel> messageListd, TextView captionTextTxtview, TextView fileNameTextview, TextView thumbnailFileNameTextview, TextView documentTextview, TextView name, TextView thumbnailimagedata, TextView imageWidthDp, TextView imageHeightDp, TextView aspectRatio, boolean iamblocked, LinearLayout editLyt, CardView imgcardviewVoiceAudio, CardView imgcardviewVoiceMusic) {
        this.context = context;
        this.replylyout = replylyout;
        this.editLyt = editLyt;
        this.messageBox = messageBox;
        this.namereply = namereply;
        this.msgreply = msgreply;
        this.imgreply = imgreply;
        this.view = view;
        this.cancel = cancel;
        this.replysvg = replysvg;
        this.view2 = view2;
        this.view3 = view3;
        this.bottom = bottom;
        this.messageRecView = messageRecView;
        this.chatAdapter = chatAdapter;
        this.contactContainer = contactContainer;
        this.firstText = firstText;
        this.imgcardview = imgcardview;
        this.pageLyt = pageLyt;
        this.pageText = pageText;
        this.replyDataTypeTv = replyDataTypeTv;
        this.listcrntpostion = listcrntpostion;
        this.messageListd = messageListd;
        this.captionTextTxtview = captionTextTxtview;
        this.fileNameTextview = fileNameTextview;
        this.thumbnailFileNameTextview = thumbnailFileNameTextview;
        this.documentTextview = documentTextview;
        this.name = name;
        this.swipeDistanceInPixels = convertDpToPx(context, SWIPE_DISTANCE_DP);
        this.thumbnailimagedata = thumbnailimagedata;
        this.imageWidthDp = imageWidthDp;
        this.imageHeightDp = imageHeightDp;
        this.aspectRatio = aspectRatio;
        this.iamblocked = iamblocked;
        this.imgcardviewVoiceAudio = imgcardviewVoiceAudio;
        this.imgcardviewVoiceMusic = imgcardviewVoiceMusic;

    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (iamblocked) {
            return 0; // No swipe or drag allowed
        }
        int swipeFlags = ItemTouchHelper.END; // Allow swipe for all items
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }


    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        float swipeDistancePx = SWIPE_DISTANCE_DP * recyclerView.getResources().getDisplayMetrics().density;
        float normalizedDx = Math.abs(dX) / swipeDistancePx;
        if (Math.abs(dX) > swipeDistancePx) {
            dX = Math.signum(dX) * swipeDistancePx;
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


        int fromPosition = viewHolder.getAdapterPosition();
        String msgs = messageListd.get(fromPosition).getMessage();
        images = messageListd.get(fromPosition).getDocument();
        ImageWidth = messageListd.get(fromPosition).getImageWidth();
        ImageHeight = messageListd.get(fromPosition).getImageHeight();
        AspectRatio = messageListd.get(fromPosition).getAspectRatio();

        Log.d("loadImageIntoView", "getImageWidth: " + messageListd.get(fromPosition).getImageWidth());
        Log.d("loadImageIntoView", "getImageHeight: " + messageListd.get(fromPosition).getImageHeight());
        Log.d("loadImageIntoView", "getAspectRatio: " + messageListd.get(fromPosition).getAspectRatio());

        aspectRatio.setText(messageListd.get(fromPosition).getAspectRatio());
        imageHeightDp.setText(messageListd.get(fromPosition).getImageHeight());
        imageWidthDp.setText(messageListd.get(fromPosition).getImageWidth());

        ThumbnailImage = messageListd.get(fromPosition).getThumbnail();
        String sendrUid = messageListd.get(fromPosition).getUid();
        micePhoto = messageListd.get(fromPosition).getMicPhoto();
        miceTiming = messageListd.get(fromPosition).getMiceTiming();
        String documentName = messageListd.get(fromPosition).getDataType();
        //  String documentName = messageListd.get(fromPosition).get();
        Cname = messageListd.get(fromPosition).getName();
        Cphone = messageListd.get(fromPosition).getPhone();
        extension = messageListd.get(fromPosition).getExtension();

        // for get data from replied element
        String replyDataType = messageListd.get(fromPosition).getDataType();
        String replyTextData = messageListd.get(fromPosition).getReplytextData();


        String captionText = messageListd.get(fromPosition).getCaption();
        String fileNameText = messageListd.get(fromPosition).getFileName();
        String thumbnailFileNameText = messageListd.get(fromPosition).getFileNameThumbnail();
        String documentText = messageListd.get(fromPosition).getDocument();
        String modelIdForRedirection = messageListd.get(fromPosition).getModelId();


        drawReplyIconWithoutBackground(c, viewHolder, dX, sendrUid);


        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX > 0) {


            if (Math.abs(dX) >= swipeDistanceInPixels && !toastShown) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Constant.Vibrator(context);
                }
                toastShown = true;
            } else if (Math.abs(dX) < swipeDistanceInPixels) {
                toastShown = false;
            }

            if (!isCurrentlyActive && Math.abs(dX) >= swipeDistanceInPixels && !isExpanding) {

                Log.d("HalfSwipeCallback", "=== Swipe condition met ===");
                Log.d("HalfSwipeCallback", "dX: " + dX + ", swipeDistanceInPixels: " + swipeDistanceInPixels);
                Log.d("HalfSwipeCallback", "isCurrentlyActive: " + isCurrentlyActive);
                Log.d("HalfSwipeCallback", "isExpanding: " + isExpanding);

                // Set flag to prevent multiple expansions
                isExpanding = true;

                //   Toast.makeText(context, "Swiped 20 dp", Toast.LENGTH_SHORT).show();

                viewHolder.itemView.setEnabled(false);


                //main coding from here


                listcrntpostion.setText(modelIdForRedirection);


                replyDataTypeTv.setText(replyDataType);
                //     Toast.makeText(context, replyDataType, Toast.LENGTH_SHORT).show();


                Constant.getSfFuncion(context);
                if (sendrUid.equals(Constant.getSF.getString(Constant.UID_KEY, ""))) {
                    // for data
                    namereply.setText("You");
                    Log.d("HalfSwipeCallback", "Setting namereply for current user (You)");
                    //  namereply.setTextColor(Color.parseColor("#00A3E9"));


                    try {

                        Constant.getSfFuncion(context);
                        themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                        tintList = ColorStateList.valueOf(Color.parseColor(themColor));

                        Log.d("HalfSwipeCallback", "Retrieved theme color: " + themColor);
                        Log.d("HalfSwipeCallback", "View null check - view: " + (view != null) + ", view2: " + (view2 != null) + ", view3: " + (view3 != null));

                        try {
                            if (themColor.equals("#ff0080")) {
                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));
                                Log.d("HalfSwipeCallback", "Applied pink theme color to namereply");

                            } else if (themColor.equals("#00A3E9")) {

                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));
                                Log.d("HalfSwipeCallback", "Applied blue theme color to namereply");
                            } else if (themColor.equals("#7adf2a")) {
                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));

                            } else if (themColor.equals("#ec0001")) {


                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));

                            } else if (themColor.equals("#16f3ff")) {

                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));

                            } else if (themColor.equals("#FF8A00")) {

                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));

                            } else if (themColor.equals("#7F7F7F")) {

                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));

                            } else if (themColor.equals("#D9B845")) {

                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));

                            } else if (themColor.equals("#346667")) {

                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));

                            } else if (themColor.equals("#9846D9")) {


                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));
                            } else if (themColor.equals("#A81010")) {

                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));

                            } else {

                                if (view != null) view.setBackgroundTintList(tintList);
                                if (view2 != null) view2.setBackgroundTintList(tintList);
                                if (view3 != null) view3.setBackgroundTintList(tintList);
                                namereply.setTextColor(Color.parseColor(themColor));
                                Log.d("HalfSwipeCallback", "Applied default theme color to namereply: " + themColor);

                            }
                        } catch (Exception e) {
                            Log.e("HalfSwipeCallback", "Error applying theme color to namereply: " + e.getMessage());
                        }


                    } catch (Exception ignored) {
                    }


                    //    tintList = ColorStateList.valueOf(Color.parseColor("#00A3E9"));
                    firstText.setTextColor(Color.BLACK);

                    contactContainer.setBackgroundResource(R.drawable.contact_gradient_cirlce);


                    cancel.setImageTintList(tintList);
                    replysvg.setImageTintList(tintList);


                } else {
                    // for data
                    namereply.setText(name.getText().toString());
                    Log.d("HalfSwipeCallback", "Setting namereply for other user: " + name.getText().toString());
                    tintList = ColorStateList.valueOf(context.getColor(R.color.black_white_cross));
                    namereply.setTextColor(context.getColor(R.color.black_white_cross));
                    Log.d("HalfSwipeCallback", "Applied black_white_cross color to namereply for other user");
                    if (view != null) view.setBackgroundTintList(tintList);
                    if (view2 != null) view2.setBackgroundTintList(tintList);
                    if (view3 != null) view3.setBackgroundTintList(tintList);
                    tintList = ColorStateList.valueOf(context.getColor(R.color.black_white_cross));
                    contactContainer.setBackgroundResource(R.drawable.contact_gradient_cirlce_receiver);
                    firstText.setTextColor(Color.WHITE);
                    cancel.setImageTintList(tintList);
                    replysvg.setImageTintList(tintList);
                }


                // Request focus and open keyboard first
                messageBox.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(messageBox, InputMethodManager.SHOW_IMPLICIT);

                // Delay the layout expansion to ensure keyboard is opened first
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("HalfSwipeCallback", "=== Handler delayed runnable executed ===");
                        Log.d("HalfSwipeCallback", "Reply layout visibility: " + replylyout.getVisibility());
                        Log.d("HalfSwipeCallback", "Reply layout visibility == GONE: " + (replylyout.getVisibility() == View.GONE));

                        if (replylyout.getVisibility() == View.GONE) {
                            Log.d("HalfSwipeCallback", "Calling expandReply with replylyout and editLyt");

                            // Check if user is at the last message
                            boolean isAtLastMessage = false;
                            if (messageRecView != null && messageRecView.getAdapter() != null) {
                                RecyclerView.LayoutManager layoutManager = messageRecView.getLayoutManager();
                                if (layoutManager instanceof LinearLayoutManager) {
                                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                                    int totalItemCount = messageRecView.getAdapter().getItemCount();

                                    // Check if last visible item is the last item in the list
                                    isAtLastMessage = (lastVisiblePosition == totalItemCount - 1);

                                    Log.d("HalfSwipeCallback", "Last visible position: " + lastVisiblePosition + ", Total items: " + totalItemCount + ", Is at last: " + isAtLastMessage);

                                    // Additional check: if we're very close to the end (within 1-2 items), consider it as "at last"
                                    if (!isAtLastMessage && totalItemCount > 0) {
                                        isAtLastMessage = (lastVisiblePosition >= totalItemCount - 2);
                                        Log.d("HalfSwipeCallback", "Close to last message check: " + isAtLastMessage);
                                    }
                                }
                            }

                            chattingScreen.expandReply(replylyout, editLyt, messageRecView, isAtLastMessage, null, null);
                        } else {
                            Log.d("HalfSwipeCallback", "Reply layout is not GONE, skipping expandReply call");
                            // Reset flag if reply layout is already visible
                            isExpanding = false;
                        }

                        // Set animation listener
                        chattingScreen.ab.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                bottom.setElevation(0);
                                Log.d("HalfSwipeCallback", "Animation started");
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                // Scroll to a particular position when animation ends
                                // messageRecView.smoothScrollToPosition(fromPosition);
                                Log.d("HalfSwipeCallback", "Animation ended - resetting isExpanding flag");
                                isExpanding = false;
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // No action needed here
                            }
                        });
                    }
                }, 100);


                // todo thumbanial
                try {
                    if (ThumbnailImage.equals("")) {

                    } else {
                        //   Toast.makeText(context, documentName, Toast.LENGTH_SHORT).show();
                        pageLyt.setVisibility(View.GONE);
                        contactContainer.setVisibility(View.GONE);
                        imgreply.setVisibility(View.VISIBLE);
                        imgcardview.setVisibility(View.VISIBLE);

                        try {

                            File customFolder;
                            String exactPath = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                customFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Enclosure/Media/Thumbnail");
                                exactPath = customFolder.getAbsolutePath();
                                Log.d("TAG", "exactPath: " + exactPath + "/" + thumbnailFileNameText);
                            } else {
                                customFolder = new File(context.getExternalFilesDir(null), "Enclosure/Media/Thumbnail");
                                exactPath = customFolder.getAbsolutePath();

                            }
                            if (doesFileExist(exactPath + "/" + thumbnailFileNameText)) {
                                File file = new File(exactPath, thumbnailFileNameText);
                                Picasso.get().load(file).into(imgreply);
                                imgreply.setTag(images);
                            } else {
                                //  Toast.makeText(context, "video not available ", Toast.LENGTH_SHORT).show();
                                Picasso.get().load(images).into(imgreply);
                                imgreply.setTag(images);
                            }


                        } catch (Exception e) {
                        }


                        thumbnailimagedata.setText(ThumbnailImage);
                        msgreply.setText("Video");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.gallery);

// Convert 18dp to pixels
                        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());

// Set custom bounds (left, top, right, bottom)
                        drawable.setBounds(0, 0, size, size);
                        drawable.setTint(Color.parseColor("#78787A"));
// Apply the drawable with custom size
                        msgreply.setCompoundDrawables(drawable, null, null, null);

                        // Add padding between text and drawable
                        msgreply.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, // 5dp padding
                                context.getResources().getDisplayMetrics()));


                        if (captionText != null) {
                            if (captionText.equals("")) {

                                captionTextTxtview.setText("");
                            } else {

                                captionTextTxtview.setText(captionText);

                            }

                        }


                        if (fileNameText != null) {
                            if (fileNameText.equals("")) {
                                fileNameTextview.setText("");
                            } else {
                                fileNameTextview.setText(fileNameText);
                            }
                        }


                        if (thumbnailFileNameText != null) {
                            if (thumbnailFileNameText.equals("")) {
                                thumbnailFileNameTextview.setText("");
                            } else {
                                thumbnailFileNameTextview.setText(thumbnailFileNameText);
                            }
                        }


                        if (documentText != null) {
                            if (documentText.equals("")) {
                                documentTextview.setText("");
                            } else {
                                documentTextview.setText(documentText);
                            }
                        }

                    }

                } catch (Exception ignored) {
                }


                try {
                    if (documentName.equals("img")) {
                        imgcardviewVoiceMusic.setVisibility(View.GONE);
                        imgcardviewVoiceAudio.setVisibility(View.GONE);
                        try {
                            if (images.equals("")) {

                            } else {
//                                aspectRatio.setText(AspectRatio);
//                                imageHeightDp.setText(ImageHeight);
//                                imageWidthDp.setText(ImageWidth);
                                pageLyt.setVisibility(View.GONE);
                                contactContainer.setVisibility(View.GONE);
                                imgreply.setVisibility(View.VISIBLE);
                                imgcardview.setVisibility(View.VISIBLE);
                                try {

                                    File customFolder;
                                    String exactPath = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        customFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                                        exactPath = customFolder.getAbsolutePath();
                                        Log.d("TAG", "exactPath: " + exactPath + "/" + messageListd.get(fromPosition).getFileName());
                                    } else {
                                        customFolder = new File(context.getExternalFilesDir(null), "Enclosure/Media/Images");
                                        exactPath = customFolder.getAbsolutePath();

                                    }
                                    if (doesFileExist(exactPath + "/" + messageListd.get(fromPosition).getFileName())) {
                                        File file = new File(exactPath, messageListd.get(fromPosition).getFileName());
                                        Picasso.get().load(file).into(imgreply);
                                        imgreply.setTag(images);
                                    } else {
                                        Picasso.get().load(images).into(imgreply);
                                        imgreply.setTag(images);
                                    }


                                } catch (Exception e) {
                                }
                                //Toast.makeText(context, images, Toast.LENGTH_SHORT).show();
                                msgreply.setText("Photo");
                                Drawable drawable = context.getResources().getDrawable(R.drawable.gallery);

// Convert 18dp → px
                                int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());

// Set the bounds for the drawable
                                drawable.setBounds(0, 0, size, size);
                                drawable.setTint(Color.parseColor("#78787A"));
// Apply the drawable (use setCompoundDrawables, not setCompoundDrawablesWithIntrinsicBounds)
                                msgreply.setCompoundDrawables(drawable, null, null, null);

// Add padding between text and drawable
                                msgreply.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, // 5dp padding
                                        context.getResources().getDisplayMetrics()));


                                if (captionText != null) {
                                    if (captionText.equals("")) {

                                        captionTextTxtview.setText("");
                                    } else {

                                        captionTextTxtview.setText(captionText);

                                    }

                                }


                                if (fileNameText != null) {
                                    if (fileNameText.equals("")) {
                                        fileNameTextview.setText("");
                                    } else {
                                        fileNameTextview.setText(fileNameText);
                                    }
                                }


                                if (thumbnailFileNameText != null) {
                                    if (thumbnailFileNameText.equals("")) {
                                        thumbnailFileNameTextview.setText("");
                                    } else {
                                        thumbnailFileNameTextview.setText(thumbnailFileNameText);
                                    }
                                }


                                if (documentText != null) {
                                    if (documentText.equals("")) {
                                        documentTextview.setText("");
                                    } else {
                                        documentTextview.setText(documentText);
                                    }
                                }

                            }

                        } catch (Exception ignored) {
                        }


                    } else if (documentName.equals("Text")) {

                        imgcardviewVoiceMusic.setVisibility(View.GONE);
                        imgcardviewVoiceAudio.setVisibility(View.GONE);
                    } else if (documentName.equals("voiceAudio")) {
                        imgcardviewVoiceMusic.setVisibility(View.GONE);


                        try {

                            if (documentName.equals("voiceAudio")) {

                                if (micePhoto.equals("")) {

                                    pageLyt.setVisibility(View.GONE);
                                    contactContainer.setVisibility(View.GONE);
                                    imgreply.setVisibility(View.GONE);
                                    imgcardview.setVisibility(View.GONE);

                                   // Toast.makeText(context, msgreply.getText().toString(), Toast.LENGTH_SHORT).show();

                                    if(msgreply.getText().toString().equals("Audio")){
                                        imgcardviewVoiceAudio.setVisibility(View.VISIBLE);
                                    }else{
                                        imgcardviewVoiceAudio.setVisibility(View.GONE);
                                    }

                                    msgreply.setText("Audio");
                                    Drawable drawable = context.getResources().getDrawable(R.drawable.mike);

// Convert 18dp to px
                                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());

// Set bounds (left, top, right, bottom)
                                    drawable.setBounds(0, 0, size, size);
                                    drawable.setTint(Color.parseColor("#78787A"));
// Apply the drawable with custom size
                                    msgreply.setCompoundDrawables(drawable, null, null, null);

// Set drawable padding (5dp instead of raw px)
                                    msgreply.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));


                                    if (captionText != null) {
                                        if (captionText.equals("")) {

                                            captionTextTxtview.setText("");
                                        } else {

                                            captionTextTxtview.setText(captionText);

                                        }

                                    }


                                    if (fileNameText != null) {
                                        if (fileNameText.equals("")) {
                                            fileNameTextview.setText("");
                                        } else {
                                            fileNameTextview.setText(fileNameText);
                                        }
                                    }


                                    if (thumbnailFileNameText != null) {
                                        if (thumbnailFileNameText.equals("")) {
                                            thumbnailFileNameTextview.setText("");
                                        } else {
                                            thumbnailFileNameTextview.setText(thumbnailFileNameText);
                                        }
                                    }

                                    if (documentText != null) {
                                        if (documentText.equals("")) {
                                            documentTextview.setText("");
                                        } else {
                                            documentTextview.setText(documentText);
                                        }
                                    }

                                } else {
                                    pageLyt.setVisibility(View.GONE);
                                    contactContainer.setVisibility(View.GONE);
                                    imgreply.setVisibility(View.GONE);
                                    imgcardview.setVisibility(View.GONE);
                                    //   Picasso.get().load(R.drawable.mike).into(imgreply);

                                    imgreply.setTag(R.drawable.mike);

                                //    Toast.makeText(context, msgreply.getText().toString(), Toast.LENGTH_SHORT).show();

                                    if(msgreply.getText().toString().equals("Audio")){
                                        imgcardviewVoiceAudio.setVisibility(View.VISIBLE);
                                    }else{
                                        imgcardviewVoiceAudio.setVisibility(View.GONE);
                                    }
                                    msgreply.setText("Audio");
                                    Drawable drawable = context.getResources().getDrawable(R.drawable.mike);

// Convert 18dp → px
                                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());

// Set drawable bounds (required for custom sizing)
                                    drawable.setBounds(0, 0, size, size);
                                    drawable.setTint(Color.parseColor("#78787A"));
// Apply with custom bounds
                                    msgreply.setCompoundDrawables(drawable, null, null, null);

// Set padding (5dp instead of raw px)
                                    msgreply.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));


                                    if (captionText != null) {
                                        if (captionText.equals("")) {

                                            captionTextTxtview.setText("");
                                        } else {

                                            captionTextTxtview.setText(captionText);

                                        }

                                    }


                                    if (fileNameText != null) {
                                        if (fileNameText.equals("")) {
                                            fileNameTextview.setText("");
                                        } else {
                                            fileNameTextview.setText(fileNameText);
                                        }
                                    }


                                    if (thumbnailFileNameText != null) {
                                        if (thumbnailFileNameText.equals("")) {
                                            thumbnailFileNameTextview.setText("");
                                        } else {
                                            thumbnailFileNameTextview.setText(thumbnailFileNameText);
                                        }
                                    }

                                    if (documentText != null) {
                                        if (documentText.equals("")) {
                                            documentTextview.setText("");
                                        } else {
                                            documentTextview.setText(documentText);
                                        }
                                    }
                                }
                            }


                        } catch (Exception ignored) {
                            //  Toast.makeText(context, ignored.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    } else if (documentName.equals("video")) {
                        imgcardviewVoiceAudio.setVisibility(View.GONE);
                        imgcardviewVoiceMusic.setVisibility(View.GONE);
                        try {
                            if (documentName.equals("video")) {
                                pageLyt.setVisibility(View.GONE);
                                contactContainer.setVisibility(View.GONE);
                                imgreply.setVisibility(View.VISIBLE);
                                imgcardview.setVisibility(View.VISIBLE);
                                //  Picasso.get().load(images).into(imgreply);
                                msgreply.setText("Video");
                                Drawable drawable = context.getResources().getDrawable(R.drawable.videopng);

// Convert 18dp → px
                                int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());

// Set custom bounds
                                drawable.setBounds(0, 0, size, size);
                                drawable.setTint(Color.parseColor("#78787A"));
// Apply drawable with custom size
                                msgreply.setCompoundDrawables(drawable, null, null, null);

// Set padding between icon & text (5dp)
                                msgreply.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));


                                if (captionText != null) {
                                    if (captionText.equals("")) {

                                        captionTextTxtview.setText("");
                                    } else {

                                        captionTextTxtview.setText(captionText);

                                    }

                                }


                                if (fileNameText != null) {
                                    if (fileNameText.equals("")) {
                                        fileNameTextview.setText("");
                                    } else {
                                        fileNameTextview.setText(fileNameText);
                                    }
                                }


                                if (thumbnailFileNameText != null) {
                                    if (thumbnailFileNameText.equals("")) {
                                        thumbnailFileNameTextview.setText("");
                                    } else {
                                        thumbnailFileNameTextview.setText(thumbnailFileNameText);
                                    }
                                }

                                if (documentText != null) {
                                    if (documentText.equals("")) {
                                        documentTextview.setText("");
                                    } else {
                                        documentTextview.setText(documentText);
                                    }
                                }

                            }

                        } catch (Exception ignored) {
                        }


                    } else if (documentName.equals("contact")) {
                        imgcardviewVoiceAudio.setVisibility(View.GONE);
                        imgcardviewVoiceMusic.setVisibility(View.GONE);
                        try {
                            if (documentName.equals("contact")) {
                                pageLyt.setVisibility(View.GONE);
                                contactContainer.setVisibility(View.VISIBLE);
                                imgreply.setVisibility(View.GONE);
                                imgcardview.setVisibility(View.GONE);
                                msgreply.setText("Contact");
                                Drawable drawable = context.getResources().getDrawable(R.drawable.contact);

// Convert 18dp → pixels
                                int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());

// Set the drawable bounds (left, top, right, bottom)
                                drawable.setBounds(0, 0, size, size);
                                drawable.setTint(Color.parseColor("#78787A"));
// Apply the drawable with custom size
                                msgreply.setCompoundDrawables(drawable, null, null, null);

// Set padding (5dp instead of raw pixels)
                                msgreply.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));


                                String cnametext = Cname;
                                String[] words = cnametext.split(" ");
                                String firstWord = words[0];
                                firstText.setText(firstWord);


                                if (captionText != null) {
                                    if (captionText.equals("")) {

                                        captionTextTxtview.setText("");
                                    } else {

                                        captionTextTxtview.setText(captionText);

                                    }

                                }


                                if (fileNameText != null) {
                                    if (fileNameText.equals("")) {
                                        fileNameTextview.setText("");
                                    } else {
                                        fileNameTextview.setText(fileNameText);
                                    }
                                }


                                if (thumbnailFileNameText != null) {
                                    if (thumbnailFileNameText.equals("")) {
                                        thumbnailFileNameTextview.setText("");
                                    } else {
                                        thumbnailFileNameTextview.setText(thumbnailFileNameText);
                                    }
                                }


                                if (documentText != null) {
                                    if (documentText.equals("")) {
                                        documentTextview.setText("");
                                    } else {
                                        documentTextview.setText(documentText);
                                    }
                                }
                            }

                        } catch (Exception ignored) {
                        }


                    }
                    else {


                        //imgcardviewVoiceMusic

                        List<String> musicExtensions = Arrays.asList("mp3", "wav", "flac", "aac", "ogg", "oga", "m4a", "wma", "alac", "aiff");

                        String ext = messageListd.get(fromPosition).getExtension();
                        if (ext != null && musicExtensions.contains(ext.toLowerCase())) {
                            imgcardviewVoiceAudio.setVisibility(View.GONE);

                         //   Toast.makeText(context, msgreply.getText().toString(), Toast.LENGTH_SHORT).show();

                            if(msgreply.getText().toString().equals("Music")){
                                imgcardviewVoiceMusic.setVisibility(View.VISIBLE);
                            }else {
                                imgcardviewVoiceMusic.setVisibility(View.GONE);
                            }

                            // TODO: 22/08/24  DOCUMENT
                            pageLyt.setVisibility(View.GONE);
                            pageText.setText(extension);
                            fileNameTextview.setText(extension);
                            Log.d("TAG", "onChildDrawpageText: " + pageText.getText().toString());
                            Log.d("TAG", "onChildDrawpageText: " + fileNameTextview.getText().toString());

                            contactContainer.setVisibility(View.GONE);
                            imgcardview.setVisibility(View.GONE);
                            imgreply.setVisibility(View.GONE);
                            msgreply.setText("Music");




                            Drawable drawable = context.getResources().getDrawable(R.drawable.musical_note);

// Convert 18dp → px
                            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());

// Apply custom bounds
                            drawable.setBounds(0, 0, size, size);
                            drawable.setTint(Color.parseColor("#78787A"));

// Attach drawable with custom size
                            msgreply.setCompoundDrawables(drawable, null, null, null);

// Add 5dp padding between drawable and text
                            msgreply.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));


                            if (captionText != null) {
                                if (captionText.equals("")) {

                                    captionTextTxtview.setText("");
                                } else {

                                    captionTextTxtview.setText(captionText);

                                }

                            }


                            if (fileNameText != null) {
                                if (fileNameText.equals("")) {
                                    fileNameTextview.setText("");
                                } else {
                                    fileNameTextview.setText(fileNameText);
                                }
                            }


                            if (thumbnailFileNameText != null) {
                                if (thumbnailFileNameText.equals("")) {
                                    thumbnailFileNameTextview.setText("");
                                } else {
                                    thumbnailFileNameTextview.setText(thumbnailFileNameText);
                                }
                            }

                            if (documentText != null) {
                                if (documentText.equals("")) {
                                    documentTextview.setText("");
                                } else {
                                    documentTextview.setText(documentText);
                                }
                            }
                        } else {
                            imgcardviewVoiceAudio.setVisibility(View.GONE);
                            imgcardviewVoiceMusic.setVisibility(View.GONE);
                            // TODO: 22/08/24  DOCUMENT
                            pageLyt.setVisibility(View.VISIBLE);
                            pageText.setText(extension);
                            fileNameTextview.setText(extension);
                            Log.d("TAG", "onChildDrawpageText: " + pageText.getText().toString());
                            Log.d("TAG", "onChildDrawpageText: " + fileNameTextview.getText().toString());

                            contactContainer.setVisibility(View.GONE);
                            imgcardview.setVisibility(View.GONE);
                            imgreply.setVisibility(View.GONE);
                            msgreply.setText("Document");
                            Drawable drawable = context.getResources().getDrawable(R.drawable.documentsvg);

// Convert 18dp → px
                            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());

// Apply custom bounds
                            drawable.setBounds(0, 0, size, size);
                            drawable.setTint(Color.parseColor("#78787A"));

// Attach drawable with custom size
                            msgreply.setCompoundDrawables(drawable, null, null, null);

// Add 5dp padding between drawable and text
                            msgreply.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));


                            if (captionText != null) {
                                if (captionText.equals("")) {

                                    captionTextTxtview.setText("");
                                } else {

                                    captionTextTxtview.setText(captionText);

                                }

                            }


                            if (fileNameText != null) {
                                if (fileNameText.equals("")) {
                                    fileNameTextview.setText("");
                                } else {
                                    fileNameTextview.setText(fileNameText);
                                }
                            }


                            if (thumbnailFileNameText != null) {
                                if (thumbnailFileNameText.equals("")) {
                                    thumbnailFileNameTextview.setText("");
                                } else {
                                    thumbnailFileNameTextview.setText(thumbnailFileNameText);
                                }
                            }

                            if (documentText != null) {
                                if (documentText.equals("")) {
                                    documentTextview.setText("");
                                } else {
                                    documentTextview.setText(documentText);
                                }
                            }
                        }


                    }

                } catch (Exception ignored) {
                }


// TODO: 22/08/24 TEXT
                try {
                    if (replyTextData.equals("")) {
                    } else {
                        try {
                            if (msgs.equals("")) {
                            } else {
                                // Toast.makeText(context, "replied messgge within reply", Toast.LENGTH_SHORT).show();
                                pageLyt.setVisibility(View.GONE);
                                contactContainer.setVisibility(View.GONE);
                                msgreply.setText(replyTextData);
                                msgreply.setVisibility(View.VISIBLE);
                                msgreply.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                if (imgreply.getVisibility() == View.VISIBLE) {
                                    imgreply.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    try {
                        if (msgs.equals("")) {
                        } else {
                            //     Toast.makeText(context, "messagenot empty", Toast.LENGTH_SHORT).show();

                            pageLyt.setVisibility(View.GONE);
                            contactContainer.setVisibility(View.GONE);
                            msgreply.setText(msgs);
                            msgreply.setVisibility(View.VISIBLE);
                            msgreply.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            if (imgreply.getVisibility() == View.VISIBLE) {
                                imgreply.setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception ignored) {

                    }
                } catch (Exception ex) {
                }


            }
        }

    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {


        return Float.MAX_VALUE;

    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return Float.MAX_VALUE;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    private float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }


    private void drawReplyIconWithoutBackground(Canvas c, RecyclerView.ViewHolder viewHolder, float dX, String sendrUid) {
        // Convert the swipe distance to pixels based on the device density
        int swipeDistancePx = (int) (SWIPE_DISTANCE_DP * context.getResources().getDisplayMetrics().density);

        Drawable icon = ContextCompat.getDrawable(context, R.drawable.reply_svg_black);
        View itemView = viewHolder.itemView;

        // Define the size of the reply icon
        int iconWidth = 60;  // Smaller icon size
        int iconHeight = 60; // Smaller icon size

        // Define the larger size of the circular progress indicator
        int progressCircleDiameter = 90;

        // Define a starting margin for the icon and progress bar
        int leftMargin = 0; // Start from the exact left side

        // Calculate the progress bar and icon's position based on swipe distance
        int iconMoveDistance = (int) dX / 2; // Control how far the icon moves based on swipe distance
        int progressLeft = leftMargin + iconMoveDistance;

        // Calculate the icon's position
        int iconLeft = progressLeft + (progressCircleDiameter - iconWidth) / 2;
        int iconRight = iconLeft + iconWidth;
        int iconTop = itemView.getTop() + (itemView.getHeight() - iconHeight) / 2;
        int iconBottom = iconTop + iconHeight;

        if (dX > 0) {
            // Draw the circular progress indicator
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4f);
            paint.setColor(ContextCompat.getColor(context, R.color.halfReplyColor));
            paint.setAntiAlias(true);

            // Calculate the center and radius of the circular progress indicator
            float cx = progressLeft + (float) progressCircleDiameter / 2;
            float cy = iconTop + (float) iconHeight / 2; // Center of the progress circle aligned with the icon
            float radius = ((float) progressCircleDiameter / 2) - paint.getStrokeWidth(); // Radius for the circular progress bar

            // Calculate the progress as a percentage of the swipe distance
            float progress = Math.min(dX / swipeDistancePx, 1); // Ensure progress does not exceed 100%

            // Draw the circular progress
            RectF oval = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
            c.drawArc(oval, -90, 360 * progress, false, paint);

            // Save the current layer
            int saveLayer = c.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);

            // Draw the smaller reply icon inside the progress circle without any color change
            assert icon != null;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            if (progress >= 1) {
                // Apply a red tint to the icon
                Paint iconPaint = new Paint();
                iconPaint.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP));
                icon.draw(c); // Draw the icon with red tint

                // Draw the destruction effect on a separate layer
                Paint destroyPaint = new Paint();
                Constant.getSfFuncion(context);
                if (sendrUid.equals(Constant.getSF.getString(Constant.UID_KEY, ""))) {
                    try {
                        Constant.getSfFuncion(context);
                        themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                        tintList = ColorStateList.valueOf(Color.parseColor(themColor));
                        destroyPaint.setColor(Color.parseColor(themColor)); // Color for the destruction effect
                    } catch (Exception ignored) {
                    }
                } else {
                    destroyPaint.setColor(ContextCompat.getColor(context, R.color.halfReplyColor));
                    //  paint.setColor(ContextCompat.getColor(context,R.color.white));
                }


                //    destroyPaint.setAlpha(128); // Semi-transparent
                destroyPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                c.drawCircle(cx, cy, radius, destroyPaint); // Draw the destruction effect
            } else {
                // Draw the icon without any color change
                icon.draw(c);
            }

            // Restore the original layer
            c.restoreToCount(saveLayer);
        }
    }

    public boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }


}
