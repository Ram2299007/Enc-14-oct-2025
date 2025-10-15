package com.Appzia.enclosure.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.get_user_active_contact_list_MessageLmt_Model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.Webservice;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class get_user_active_contact_list_msgLmt_Adapter extends RecyclerView.Adapter<get_user_active_contact_list_msgLmt_Adapter.myViewHolder> {

    Context mContext;
    ColorStateList tintList;
    String themColor;
    int positionDummy = RecyclerView.NO_POSITION;
    ArrayList<get_user_active_contact_list_MessageLmt_Model> get_user_active_contact_listmsgLmt = new ArrayList<>();
    CardView customToastCard;
    TextView customToastText;

    public get_user_active_contact_list_msgLmt_Adapter(Context mContext, ArrayList<get_user_active_contact_list_MessageLmt_Model> get_user_active_contact_listmsgLmt, CardView customToastCard, TextView customToastText) {
        this.mContext = mContext;
        this.get_user_active_contact_listmsgLmt = get_user_active_contact_listmsgLmt;
        this.customToastCard = customToastCard;
        this.customToastText = customToastText;
    }

    public void searchFilteredData(ArrayList<get_user_active_contact_list_MessageLmt_Model> filteredList) {
        this.get_user_active_contact_listmsgLmt = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public get_user_active_contact_list_msgLmt_Adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.msg_limit_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull get_user_active_contact_list_msgLmt_Adapter.myViewHolder holder, int position) {

        final get_user_active_contact_list_MessageLmt_Model model = get_user_active_contact_listmsgLmt.get(position);


        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");

        if (uid.equals(model.getUid())) {

        }


        holder.name.setText(model.getFull_name());

        if (holder.name.getText().toString().length() > 22) {
            String truncatedText = holder.name.getText().toString().substring(0, 22) + "..."; // Add dots at the end
            holder.name.setText(truncatedText);
        } else {
            holder.name.setText(holder.name.getText().toString());
        }

        Picasso.get().load(model.getPhoto()).into(holder.imageview);


        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {
                    holder.l1.setBackgroundTintList(tintList);

                } else if (themColor.equals("#00A3E9")) {

                    holder.l1.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7adf2a")) {

                    holder.l1.setBackgroundTintList(tintList);


                } else if (themColor.equals("#ec0001")) {

                    holder.l1.setBackgroundTintList(tintList);


                } else if (themColor.equals("#16f3ff")) {

                    holder.l1.setBackgroundTintList(tintList);


                } else if (themColor.equals("#FF8A00")) {

                    holder.l1.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {


                    holder.l1.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {

                    holder.l1.setBackgroundTintList(tintList);


                } else if (themColor.equals("#346667")) {

                    holder.l1.setBackgroundTintList(tintList);


                } else if (themColor.equals("#9846D9")) {

                    holder.l1.setBackgroundTintList(tintList);


                } else if (themColor.equals("#A81010")) {

                    holder.l1.setBackgroundTintList(tintList);


                } else {

                    holder.l1.setBackgroundTintList(tintList);

                }
            } catch (Exception ignored) {

                holder.l1.setBackgroundTintList(tintList);
            }


        } catch (Exception ignored) {
        }

        holder.txt1.setText(model.getMsgLmt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.l1.performClick();
            }
        });

        holder.l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (holder.cardview.getVisibility() == View.VISIBLE) {
                    holder.cardview.setVisibility(View.GONE);

                    try {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view = ((Activity) mContext).getCurrentFocus();
                        if (view == null) {
                            view = new View(((Activity) mContext));
                        }
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                } else {
                    MainActivityOld.slideUpContainer();
                    holder.cardview.setVisibility(View.VISIBLE);
                    holder.et1.requestFocus();
                    holder.et1.setSelection(holder.et1.getText().length());
                    if (holder.et1.requestFocus()) {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.showSoftInput(holder.et1, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }


                }
            }
        });


        holder.et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (holder.et1.getText().toString().isEmpty()) {
                    holder.txt1.setText("0");
                } else if (holder.et1.getText().toString().equals("0") || holder.et1.getText().toString().equals("00") || holder.et1.getText().toString().equals("000")) {
                    // holder.et1.setText("\u221E");
                    holder.txt1.setText("0");
                } else {
                    holder.txt1.setText(holder.et1.getText().toString());
                }


                if (holder.et1.getText().toString().length() == 3) {
                    holder.cardview.setVisibility(View.GONE);
                    //  holder.et1.setText("");
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.itemView.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                Constant.getSfFuncion(mContext);
                String uid = Constant.getSF.getString(Constant.UID_KEY, "");
                String friend_id = model.getUid();

                try {


                    Webservice.set_message_limit_for_user_chat(mContext, uid, friend_id, holder.txt1.getText().toString(), holder.cardview, holder.et1,customToastCard,customToastText);


                } catch (Exception ignored) {
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return get_user_active_contact_listmsgLmt.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView name;

        LinearLayout l1;
        EditText et1;
        TextView txt1;
        CardView cardview;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageview);
            name = itemView.findViewById(R.id.name);
            l1 = itemView.findViewById(R.id.l1);
            et1 = itemView.findViewById(R.id.et1);
            txt1 = itemView.findViewById(R.id.txt1);
            cardview = itemView.findViewById(R.id.cardview);

        }
    }
}
