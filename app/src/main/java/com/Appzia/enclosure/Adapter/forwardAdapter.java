package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.forwardnameModel;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class forwardAdapter extends RecyclerView.Adapter<forwardAdapter.myViewHolder> {

    Context mContext;
    int countSizeOfList = 0;
    JSONArray array = new JSONArray();
    ArrayList<forwardnameModel> forwardNameList = new ArrayList<>();
    String data;
    ArrayList<get_user_active_contact_list_Model> get_user_active_contact_forward_list = new ArrayList<>();
    LinearLayout dx,richBoxForward;
    String themColor;
    chatAdapter chatAdapter;

    public forwardAdapter(Context mContext, ArrayList<get_user_active_contact_list_Model> get_user_active_contact_forward_list, LinearLayout dx, chatAdapter chatAdapter,LinearLayout richBoxForward) {
        this.mContext = mContext;
        this.get_user_active_contact_forward_list = get_user_active_contact_forward_list;
        this.dx = dx;
        this.chatAdapter = chatAdapter;
        this.richBoxForward = richBoxForward;
    }

    @NonNull
    @Override
    public forwardAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.forward_layout_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull forwardAdapter.myViewHolder holder, int position) {

        final get_user_active_contact_list_Model model = get_user_active_contact_forward_list.get(position);



        ColorStateList tintList;
        int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            Constant.getSfFuncion(mContext);
            String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");


            try {
                if (themColor.equals("#ff0080")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                   
                    richBoxForward.setBackgroundTintList(tintList);





                } else if (themColor.equals("#00A3E9")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                   
                    richBoxForward.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {

                    tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                   
                    richBoxForward.setBackgroundTintList(tintList);



                } else if (themColor.equals("#ec0001")) {

                    tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                   
                    richBoxForward.setBackgroundTintList(tintList);



                } else if (themColor.equals("#16f3ff")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                   
                    richBoxForward.setBackgroundTintList(tintList);

                } else if (themColor.equals("#FF8A00")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                   
                    richBoxForward.setBackgroundTintList(tintList);


                } else if (themColor.equals("#7F7F7F")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                   
                    richBoxForward.setBackgroundTintList(tintList);

                } else if (themColor.equals("#D9B845")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                   
                    richBoxForward.setBackgroundTintList(tintList);

                } else if (themColor.equals("#346667")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                   
                    richBoxForward.setBackgroundTintList(tintList);

                } else if (themColor.equals("#9846D9")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                   
                    richBoxForward.setBackgroundTintList(tintList);


                } else if (themColor.equals("#A81010")) {
                    tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                   
                    richBoxForward.setBackgroundTintList(tintList);

                } else {
                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                   
                    richBoxForward.setBackgroundTintList(tintList);


                }
            } catch (Exception ignored) {
                tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
               
                richBoxForward.setBackgroundTintList(tintList);


            }


        } else {

             tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
            // Replace #011224 with your hex color value
            richBoxForward.setBackgroundTintList(tintList);
        }



        holder.contact1text.setText(model.getFull_name());

        if (holder.contact1text.getText().toString().length() > 20) {
            String truncatedText = holder.contact1text.getText().toString().substring(0, 20) + "..."; // Add dots at the end
            holder.contact1text.setText(truncatedText);
        } else {
            holder.contact1text.setText(holder.contact1text.getText().toString());
        }
        holder.captiontext.setText(model.getCaption());


        if (holder.captiontext.getText().toString().length() > 35) {
            String truncatedText = holder.captiontext.getText().toString().substring(0, 35) + "..."; // Add dots at the end
            holder.captiontext.setText(truncatedText);
        } else {
            holder.captiontext.setText(holder.captiontext.getText().toString());
        }

        Picasso.get().load(model.getPhoto()).into(holder.contact1img);
        try {

            Constant.getSfFuncion(mContext);
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");


            try {
                if (themColor.equals("#ff0080")) {
                    holder.checkbox.setButtonDrawable(R.drawable.checkbox_pink);

                } else if (themColor.equals("#00A3E9")) {
                    holder.checkbox.setButtonDrawable(R.drawable.checkboxtwo_bg_default);
                } else if (themColor.equals("#7adf2a")) {

                    holder.checkbox.setButtonDrawable(R.drawable.checkbox_popati);

                } else if (themColor.equals("#ec0001")) {

                    holder.checkbox.setButtonDrawable(R.drawable.checkbox_redone);

                } else if (themColor.equals("#16f3ff")) {
                    holder.checkbox.setButtonDrawable(R.drawable.checkbox_blue);

                } else if (themColor.equals("#FF8A00")) {
                    holder.checkbox.setButtonDrawable(R.drawable.checkbox_orange);


                } else if (themColor.equals("#7F7F7F")) {
                    holder.checkbox.setButtonDrawable(R.drawable.checkbox_gray);


                } else if (themColor.equals("#D9B845")) {
                    holder.checkbox.setButtonDrawable(R.drawable.checkbox_yellow);

                } else if (themColor.equals("#346667")) {
                    holder.checkbox.setButtonDrawable(R.drawable.checkbox_green);


                } else if (themColor.equals("#9846D9")) {
                    holder.checkbox.setButtonDrawable(R.drawable.checkbox_voilet);


                } else if (themColor.equals("#A81010")) {
                    holder.checkbox.setButtonDrawable(R.drawable.checkbox_redtwo);


                } else {

                }
            } catch (Exception ignored) {

            }


        } catch (Exception ignored) {
        }



//
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkbox.performClick();
            }
        });

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    forwardNameList.clear();

                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("friend_id", Integer.parseInt(model.getUid()));
                        obj.put("name", model.getFull_name());
                        obj.put("f_token", model.getF_token());
                        obj.put("device_type", model.getDevice_type());
                        array.put(obj);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    data = array.toString();
                    Log.d("data00666", data + "size :" + array.length());

                    if (array.length() > 0) {

                        if (dx.getVisibility() == View.GONE) {
                            expand(dx);
                        }


                    }


                    for (int i = 0; i < array.length(); i++) {
                        try {
                            JSONObject obj = array.getJSONObject(i);
                            String name = obj.getString("name");
                            String friend_id = obj.getString("friend_id");
                            String f_token = obj.getString("f_token");
                            String device_type = obj.getString("device_type");

                            forwardNameList.add(new forwardnameModel(name, friend_id,f_token,device_type));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    chatAdapter.setforwardNameAdapter(forwardNameList);


                } else {

                    forwardNameList.clear();
                    int itemToRemoveId = Integer.parseInt(model.getUid());
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            if (array.getJSONObject(i).optInt("friend_id") == itemToRemoveId) {
                                array.remove(i);
                                data = array.toString();
                                break; // Assuming "id" is unique, we can break out of the loop once we find the item.
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }


                    Log.d("data00", data + "size :" + array.length());

                    if (array.length() <= 0) {
                        if (dx.getVisibility() == View.VISIBLE) {
                            collapse(dx);
                        }

                    }

                    for (int i = 0; i < array.length(); i++) {
                        try {
                            JSONObject obj = array.getJSONObject(i);
                            String name = obj.getString("name");
                            String friend_id = obj.getString("friend_id");
                            String f_token = obj.getString("f_token");
                            String device_type = obj.getString("device_type");
                            forwardNameList.add(new forwardnameModel(name, friend_id,f_token,device_type));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    chatAdapter.setforwardNameAdapter(forwardNameList);


                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return get_user_active_contact_forward_list.size();
    }

    public void searchFilteredData(ArrayList<get_user_active_contact_list_Model> filteredList) {
        this.get_user_active_contact_forward_list = filteredList;
        notifyDataSetChanged();
    }


    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(a);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        androidx.appcompat.widget.AppCompatImageView contact1img;
        TextView contact1text;
        TextView captiontext;
        AppCompatCheckBox checkbox;



        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            contact1img = itemView.findViewById(R.id.contact1img);
            contact1text = itemView.findViewById(R.id.contact1text);
            captiontext = itemView.findViewById(R.id.captiontext);
            checkbox = itemView.findViewById(R.id.checkbox);

        }
    }
}
