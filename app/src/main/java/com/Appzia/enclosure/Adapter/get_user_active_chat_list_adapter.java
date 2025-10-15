package com.Appzia.enclosure.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.chattingScreen;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Utils.BlurHelper;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.crypto.SecretKey;


public class get_user_active_chat_list_adapter extends RecyclerView.Adapter<com.Appzia.enclosure.Adapter.get_user_active_chat_list_adapter.myViewHolder> {

    Context mContext;
    FirebaseDatabase database;
    ColorStateList tintList;
    String themColor;
    BottomSheetDialog bottomSheetDialog;
    ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list = new ArrayList<>();
    LinearLayout enclosure;

    public get_user_active_chat_list_adapter(Context mContext, ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list, LinearLayout enclosure) {
        this.mContext = mContext;
        this.get_user_active_contact_list = get_user_active_contact_list;
        this.enclosure = enclosure;
    }

    public void searchFilteredData(ArrayList<get_user_active_contact_list_Model> filteredList) {
        this.get_user_active_contact_list = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public com.Appzia.enclosure.Adapter.get_user_active_chat_list_adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.get_user_active_contact_list_row, parent, false);
        return new com.Appzia.enclosure.Adapter.get_user_active_chat_list_adapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.Appzia.enclosure.Adapter.get_user_active_chat_list_adapter.myViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();

        final get_user_active_contact_list_Model model = get_user_active_contact_list.get(position);

        Constant.getSfFuncion(mContext);
        String myUid = Constant.getSF.getString(Constant.UID_KEY,"");

        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");
        if (uid.equals(model.getUid())) {

            String nameText = model.getFull_name();
            String youText = " (You)";
            SpannableString spannable = new SpannableString(nameText + youText);

            // Set color for name (black)
            spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                    0, nameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Set color for (You) (gray)
            spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.TextColor)),
                    nameText.length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.contact1text.setText(spannable);
        } else {
            holder.contact1text.setText(model.getFull_name());

        }


        if (holder.contact1text.getText().toString().length() > 20) {
            String truncatedText = holder.contact1text.getText().toString().substring(0, 20) + "..."; // Add dots at the end
            holder.contact1text.setText(truncatedText);
        } else {
            holder.contact1text.setText(holder.contact1text.getText().toString());
        }




        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
                    holder.notiBack.setBackgroundTintList(tintList);


                } else if (themColor.equals("#00A3E9")) {

                    holder.notiBack.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7adf2a")) {

                    holder.notiBack.setBackgroundTintList(tintList);


                } else if (themColor.equals("#ec0001")) {

                    holder.notiBack.setBackgroundTintList(tintList);


                } else if (themColor.equals("#16f3ff")) {

                    holder.notiBack.setBackgroundTintList(tintList);


                } else if (themColor.equals("#FF8A00")) {

                    holder.notiBack.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {


                    holder.notiBack.setBackgroundTintList(tintList);


                } else if (themColor.equals("#D9B845")) {

                    holder.notiBack.setBackgroundTintList(tintList);


                } else if (themColor.equals("#346667")) {

                    holder.notiBack.setBackgroundTintList(tintList);


                } else if (themColor.equals("#9846D9")) {

                    holder.notiBack.setBackgroundTintList(tintList);


                } else if (themColor.equals("#A81010")) {

                    holder.notiBack.setBackgroundTintList(tintList);


                } else {

                    holder.notiBack.setBackgroundTintList(tintList);


                }
            } catch (Exception ignored) {


            }


        } catch (Exception ignored) {
        }


        if (model.getNotification() == 0) {
            holder.notiBack.setVisibility(View.GONE);


            holder.captiontext.setTextColor(Color.parseColor("#9EA6B9"));
            holder.time.setTextColor(Color.parseColor("#9EA6B9"));
        }
        else {

            holder.notiBack.setVisibility(View.VISIBLE);
            holder.notiCount.setText(String.valueOf(model.getNotification()));

            try {
                if (themColor.equals("#ff0080")) {
                    holder.time.setTextColor(Color.parseColor(themColor));
                    holder.captiontext.setTextColor(Color.parseColor(themColor));

                } else if (themColor.equals("#00A3E9")) {

                    holder.time.setTextColor(Color.parseColor(themColor));
                    holder.captiontext.setTextColor(Color.parseColor(themColor));


                } else if (themColor.equals("#7adf2a")) {

                    holder.time.setTextColor(Color.parseColor(themColor));
                    holder.captiontext.setTextColor(Color.parseColor(themColor));


                } else if (themColor.equals("#ec0001")) {

                    holder.time.setTextColor(Color.parseColor(themColor));
                    holder.captiontext.setTextColor(Color.parseColor(themColor));


                } else if (themColor.equals("#16f3ff")) {

                    holder.time.setTextColor(Color.parseColor(themColor));
                    holder.captiontext.setTextColor(Color.parseColor(themColor));


                } else if (themColor.equals("#FF8A00")) {

                    holder.time.setTextColor(Color.parseColor(themColor));

                    holder.captiontext.setTextColor(Color.parseColor(themColor));

                } else if (themColor.equals("#7F7F7F")) {


                    holder.time.setTextColor(Color.parseColor(themColor));
                    holder.captiontext.setTextColor(Color.parseColor(themColor));


                } else if (themColor.equals("#D9B845")) {

                    holder.time.setTextColor(Color.parseColor(themColor));

                    holder.captiontext.setTextColor(Color.parseColor(themColor));

                } else if (themColor.equals("#346667")) {

                    holder.time.setTextColor(Color.parseColor(themColor));
                    holder.captiontext.setTextColor(Color.parseColor(themColor));


                } else if (themColor.equals("#9846D9")) {

                    holder.time.setTextColor(Color.parseColor(themColor));
                    holder.captiontext.setTextColor(Color.parseColor(themColor));


                } else if (themColor.equals("#A81010")) {

                    holder.time.setTextColor(Color.parseColor(themColor));
                    holder.captiontext.setTextColor(Color.parseColor(themColor));


                } else {

                    holder.time.setTextColor(Color.parseColor(themColor));
                    holder.captiontext.setTextColor(Color.parseColor(themColor));


                }
            } catch (Exception ignored) {


            }
        }

        try {

            String dataType = model.getDataType();
            Log.d("dataTypecxsacwecwa", "dataType: " + dataType + " ::::: " + model.getFull_name());
            if (model.getMessages().equals("")) {
                if (dataType.equals(Constant.img)) {


                    holder.captiontext.setText("Photo");
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.gallery);

                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());

// Set the bounds for the drawable
                    drawable.setBounds(0, 0, size, size);
                    drawable.setTint(Color.parseColor("#78787A"));
// Apply the drawable (use setCompoundDrawables, not setCompoundDrawablesWithIntrinsicBounds)
                    holder.captiontext.setCompoundDrawables(drawable, null, null, null);

// Add padding between text and drawable
                    holder.captiontext.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, // 5dp padding
                            mContext.getResources().getDisplayMetrics()));

                } else if (dataType.equals(Constant.video)) {

                    holder.captiontext.setText("Video");
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.videopng);

                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());

// Set the bounds for the drawable
                    drawable.setBounds(0, 0, size, size);
                    drawable.setTint(Color.parseColor("#78787A"));
// Apply the drawable (use setCompoundDrawables, not setCompoundDrawablesWithIntrinsicBounds)
                    holder.captiontext.setCompoundDrawables(drawable, null, null, null);

// Add padding between text and drawable
                    holder.captiontext.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, // 5dp padding
                            mContext.getResources().getDisplayMetrics()));
                } else if (dataType.equals(Constant.voiceAudio)) {

                    holder.captiontext.setText("Mic");
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.mike);

                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());

                    drawable.setBounds(0, 0, size, size);
                    drawable.setTint(Color.parseColor("#78787A"));
                    holder.captiontext.setCompoundDrawables(drawable, null, null, null);

                    holder.captiontext.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, // 5dp padding
                            mContext.getResources().getDisplayMetrics()));

                } else if (dataType.equals(Constant.contact)) {

                    holder.captiontext.setText("Contact");
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.contact);

                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());

                    drawable.setBounds(0, 0, size, size);
                    drawable.setTint(Color.parseColor("#78787A"));
                    holder.captiontext.setCompoundDrawables(drawable, null, null, null);

                    holder.captiontext.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, // 5dp padding
                            mContext.getResources().getDisplayMetrics()));
                } else if (dataType.equals(Constant.Text)) {

                    holder.captiontext.setCompoundDrawables(null, null, null, null);

                } else if (dataType.equals(Constant.doc)) {

                    // Show actual filename for documents instead of just "File"
                    // For document types, the filename might be in the messages field
                    String fileName = model.getMessages();
                    if (fileName != null && !fileName.isEmpty() && !fileName.equals("doc")) {
                        holder.captiontext.setText(fileName);
                    } else {
                        holder.captiontext.setText("Document");
                    }
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.documentsvg);

                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());

                    drawable.setBounds(0, 0, size, size);
                    drawable.setTint(Color.parseColor("#78787A"));
                    holder.captiontext.setCompoundDrawables(drawable, null, null, null);

                    holder.captiontext.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, // 5dp padding
                            mContext.getResources().getDisplayMetrics()));

                } else {

                    holder.captiontext.setText("File");
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.documentsvg);

                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());

                    drawable.setBounds(0, 0, size, size);
                    drawable.setTint(Color.parseColor("#78787A"));
                    holder.captiontext.setCompoundDrawables(drawable, null, null, null);

                    holder.captiontext.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, // 5dp padding
                            mContext.getResources().getDisplayMetrics()));
                }
            } else {
                holder.captiontext.setCompoundDrawables(null, null, null, null);

                Log.d("TAG", "UnknownType2 " + dataType);
                holder.captiontext.setText(model.getMessages());

                if (holder.captiontext.getText().toString().length() > 25) {
                    String truncatedText = holder.captiontext.getText().toString().substring(0, 25) + "..."; // Add dots at the end
                    holder.captiontext.setText(truncatedText);
                } else {
                    holder.captiontext.setText(holder.captiontext.getText().toString());
                }
            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }


        if (model.getSent_time().length() > 10) {
            try {
                holder.time.setText(model.getSent_time());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            holder.time.setText(model.getSent_time());
        }

        if(model.isBlock()){
            try {
                Picasso.get().load(R.drawable.inviteimg).into(holder.contact1img);
                Log.d("photo", model.getPhoto());
            } catch (Exception ignored) {
            }
        }else{
            try {
                Picasso.get().load(model.getPhoto()).into(holder.contact1img);
                Log.d("photo", model.getPhoto());
            } catch (Exception ignored) {
            }
        }





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent0 = ((Activity)mContext).getIntent();
                if (intent0.hasExtra("grpKey")) {
                    intent0.removeExtra("grpKey");
                }

                Intent intent00 = ((Activity)mContext).getIntent();
                if (intent00.hasExtra("voiceCallKey")) {
                    intent00.removeExtra("voiceCallKey");
                }
                Intent intent000 = ((Activity)mContext).getIntent();
                if (intent000.hasExtra("youKey")) {
                    intent000.removeExtra("youKey");
                }
                Intent intent0000 = ((Activity)mContext).getIntent();
                if (intent0000.hasExtra("videoCallKey")) {
                    intent0000.removeExtra("videoCallKey");
                }

                Intent intent = new Intent(mContext, chattingScreen.class);
                intent.putExtra("nameKey", model.getFull_name());
                intent.putExtra("captionKey", model.getCaption());
                intent.putExtra("photoKey", model.getPhoto());
                intent.putExtra("friendUidKey", model.getUid());
                intent.putExtra("msgLmtKey", model.getMsg_limit());
                if (holder.notiBack.getVisibility() == View.VISIBLE) {
                    intent.putExtra("ecKey", "");
                    intent.putExtra("NotiCountKey", String.valueOf(model.getNotification()));
                } else if (holder.notiBack.getVisibility() == View.GONE) {
                    intent.putExtra("ecKey", "ecKey");
                }

                intent.putExtra("userFTokenKey", model.getF_token());
                intent.putExtra("deviceType", model.getDevice_type());
                intent.putExtra("original_name", model.getOriginal_name());
                intent.putExtra("block", model.isBlock());
                intent.putExtra("iamblocked", model.isIamblocked());
                Log.d("StartChatIntent", "Sending block = " + model.isBlock());

                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                //

            }
        });

        holder.contact1img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext, show_image_Screen.class);
                intent.putExtra("imageKey", model.getPhoto());
                intent.putExtra("youKey", "chatKey");
                intent.putExtra("ecKey", "ecKey");
                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);

            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // 1. Get touch coordinates
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                final int touchX = location[0];
                final int touchY = location[1];

// 2. Show the dialog
                BlurHelper.showDialogWithBlurBackground(mContext, R.layout.get_user_active_chat_blur_dialogue);
                BlurHelper.dialogLayoutColor.show();

// 3. Use boundary-aware positioning
                final RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);
                BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);


                androidx.appcompat.widget.AppCompatImageView contact1img;
                TextView contact1text, time, notiCount;
                TextView captiontext;
                LinearLayout contact1, notiBack;
                CardView delete;

                contact1img = BlurHelper.dialogLayoutColor.findViewById(R.id.contact1img);
                contact1text = BlurHelper.dialogLayoutColor.findViewById(R.id.contact1text);
                captiontext = BlurHelper.dialogLayoutColor.findViewById(R.id.captiontext);
                contact1 = BlurHelper.dialogLayoutColor.findViewById(R.id.contact1);
                notiBack = BlurHelper.dialogLayoutColor.findViewById(R.id.notiBack);
                time = BlurHelper.dialogLayoutColor.findViewById(R.id.time);
                notiCount = BlurHelper.dialogLayoutColor.findViewById(R.id.notiCount);
                delete = BlurHelper.dialogLayoutColor.findViewById(R.id.deletecardview);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Constant.getSfFuncion(mContext);
                        Webservice.delete_individual_user_chatting(mContext, Constant.getSF.getString(Constant.UID_KEY, ""), model.getUid(), database, get_user_active_chat_list_adapter.this, holder.getAdapterPosition(), get_user_active_contact_list, enclosure, holder.itemView, BlurHelper.dialogLayoutColor);

                    }
                });


                contact1text.setText(model.getFull_name());

                if (contact1text.getText().toString().length() > 20) {
                    String truncatedText = contact1text.getText().toString().substring(0, 20) + "..."; // Add dots at the end
                    contact1text.setText(truncatedText);
                } else {
                    contact1text.setText(contact1text.getText().toString());
                }


                try {

                    Constant.getSfFuncion(mContext);
                    themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                    tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                    //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

                    try {
                        if (themColor.equals("#ff0080")) {
                            notiBack.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#00A3E9")) {

                            notiBack.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#7adf2a")) {

                            notiBack.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#ec0001")) {

                            notiBack.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#16f3ff")) {

                            notiBack.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#FF8A00")) {

                            notiBack.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#7F7F7F")) {


                            notiBack.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#D9B845")) {

                            notiBack.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#346667")) {

                            notiBack.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#9846D9")) {

                            notiBack.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#A81010")) {

                            notiBack.setBackgroundTintList(tintList);


                        } else {

                            notiBack.setBackgroundTintList(tintList);


                        }
                    } catch (Exception ignored) {


                    }


                } catch (Exception ignored) {
                }


                if (model.getNotification() == 0) {
                    notiBack.setVisibility(View.GONE);

                    captiontext.setTextColor(Color.parseColor("#9EA6B9"));
                    time.setTextColor(Color.parseColor("#9EA6B9"));
                } else {

                    captiontext.setTextColor(Color.parseColor("#000000"));
                    notiBack.setVisibility(View.VISIBLE);
                    notiCount.setText(String.valueOf(model.getNotification()));

                    try {
                        if (themColor.equals("#ff0080")) {
                            time.setTextColor(Color.parseColor(themColor));

                        } else if (themColor.equals("#00A3E9")) {

                            time.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#7adf2a")) {

                            time.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#ec0001")) {

                            time.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#16f3ff")) {

                            time.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#FF8A00")) {

                            time.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#7F7F7F")) {


                            time.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#D9B845")) {

                            time.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#346667")) {

                            time.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#9846D9")) {

                            time.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#A81010")) {

                            time.setTextColor(Color.parseColor(themColor));


                        } else {

                            time.setTextColor(Color.parseColor(themColor));


                        }
                    } catch (Exception ignored) {


                    }
                }

                try {
                    String dataType = model.getDataType();
                    if (model.getMessages().equals("")) {
                        if (dataType.equals(Constant.img)) {

                            // Get the drawable you want to set as left drawable
                            Drawable leftDrawable = ContextCompat.getDrawable(mContext, R.drawable.baseline_insert_photo_24);
                            // Set bounds for the drawable (adjust dimensions as needed)
                            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());

                            // Set the left drawable on the TextView
                            captiontext.setCompoundDrawables(leftDrawable, null, null, null);

                            captiontext.setText("Photo");
                        } else if (dataType.equals(Constant.video)) {

                            // Get the drawable you want to set as left drawable
                            Drawable leftDrawable = ContextCompat.getDrawable(mContext, R.drawable.video_icon);
                            // Set bounds for the drawable (adjust dimensions as needed)
                            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());

                            // Set the left drawable on the TextView
                            captiontext.setCompoundDrawables(leftDrawable, null, null, null);

                            captiontext.setText("Video");
                        } else if (dataType.equals(Constant.voiceAudio)) {
                            // Get the drawable you want to set as left drawable
                            Drawable leftDrawable = ContextCompat.getDrawable(mContext, R.drawable.micegray);
                            // Set bounds for the drawable (adjust dimensions as needed)
                            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());

                            // Set the left drawable on the TextView
                            captiontext.setCompoundDrawables(leftDrawable, null, null, null);

                            captiontext.setText("Audio");

                        } else if (dataType.equals(Constant.contact)) {
                            // Get the drawable you want to set as left drawable
                            Drawable leftDrawable = ContextCompat.getDrawable(mContext, R.drawable.contact_24);
                            // Set bounds for the drawable (adjust dimensions as needed)
                            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());

                            // Set the left drawable on the TextView
                            captiontext.setCompoundDrawables(leftDrawable, null, null, null);
                            captiontext.setText("Contact");
                        } else if (dataType.equals(Constant.Text)) {

                            holder.captiontext.setCompoundDrawables(null, null, null, null);

                        } else {


                            // Get the drawable you want to set as left drawable
                            Drawable leftDrawable = ContextCompat.getDrawable(mContext, R.drawable.document_24);
                            // Set bounds for the drawable (adjust dimensions as needed)
                            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());

                            // Set the left drawable on the TextView
                            captiontext.setCompoundDrawables(leftDrawable, null, null, null);
                            captiontext.setText(dataType);
                        }
                    } else {
                        holder.captiontext.setCompoundDrawables(null, null, null, null);

                        Log.d("TAG", "UnknownType2 " + dataType);
                        captiontext.setText(model.getMessages());

                        if (captiontext.getText().toString().length() > 25) {
                            String truncatedText = captiontext.getText().toString().substring(0, 25) + "..."; // Add dots at the end
                            captiontext.setText(truncatedText);
                        } else {
                            captiontext.setText(captiontext.getText().toString());
                        }
                    }
                } catch (Exception e) {
//            throw new RuntimeException(e);
                }

                Log.d("MAR", "sent time: " + model.getSent_time());


                if (model.getSent_time().length() > 10) {
                    try {
                        time.setText(model.getSent_time());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    time.setText(model.getSent_time());
                }

                try {
                    Picasso.get().load(model.getPhoto()).into(contact1img);
                    Log.d("photo", model.getPhoto());
                } catch (Exception ignored) {
                }


//                bottomSheetDialog = new BottomSheetDialog(mContext, R.style.CustomBottomSheetDialogTheme);
//                View viewShape = LayoutInflater.from(mContext).inflate(R.layout.delete_dialogue_box, null, false);
//                bottomSheetDialog.setContentView(viewShape);
//
//                TextView username = viewShape.findViewById(R.id.usernamess);
//                CardView cancelss = viewShape.findViewById(R.id.cancelss);
//                LinearLayout delete = viewShape.findViewById(R.id.delete);
//                username.setText(model.getFull_name());
//                //  Toast.makeText(mContext, model.getFull_name(), Toast.LENGTH_SHORT).show();
//                bottomSheetDialog.show();
//
//                cancelss.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bottomSheetDialog.dismiss();
//                    }
//                });
//
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bottomSheetDialog.dismiss();
//
//                        // call here delete api
//                        //  Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
//                        Constant.getSfFuncion(mContext);
//
//                        Webservice.delete_individual_user_chatting(mContext, Constant.getSF.getString(Constant.UID_KEY, ""), model.getUid(), database, get_user_active_chat_list_adapter.this, getAdapterPosition(), get_user_active_contact_list, enclosure, holder.itemView);
//
//                    }
//                });


                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return get_user_active_contact_list.size();
    }

    public void removeItem(int adapterPosition, View itemView) {
        try {
            get_user_active_contact_list.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        } catch (Exception e) {

        }

    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        androidx.appcompat.widget.AppCompatImageView contact1img;
        TextView contact1text, time, notiCount;
        TextView captiontext;
        LinearLayout contact1, notiBack;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            contact1img = itemView.findViewById(R.id.contact1img);
            contact1text = itemView.findViewById(R.id.contact1text);
            captiontext = itemView.findViewById(R.id.captiontext);
            contact1 = itemView.findViewById(R.id.contact1);
            notiBack = itemView.findViewById(R.id.notiBack);
            time = itemView.findViewById(R.id.time);
            notiCount = itemView.findViewById(R.id.notiCount);

        }
    }
}


