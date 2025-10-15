package com.Appzia.enclosure.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.newGroupActivity;
import com.Appzia.enclosure.Utils.Constant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class get_user_active_contact_list_Adapter_forGrp extends RecyclerView.Adapter<get_user_active_contact_list_Adapter_forGrp.myViewHolder> {

    Context mContext;
    int countSizeOfList = 0;
    private OnDataClickListener onDataClickListener;
    String themColor;


    JSONArray array = new JSONArray();
    String data;
    ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list = new ArrayList<>();

    public get_user_active_contact_list_Adapter_forGrp(Context mContext, ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list) {
        this.mContext = mContext;
        this.get_user_active_contact_list = get_user_active_contact_list;
    }

    @NonNull
    @Override
    public get_user_active_contact_list_Adapter_forGrp.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.grp_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull get_user_active_contact_list_Adapter_forGrp.myViewHolder holder, int position) {

        final get_user_active_contact_list_Model model = get_user_active_contact_list.get(position);

        holder.name.setText(model.getFull_name());
        Picasso.get().load(model.getPhoto()).into(holder.img);


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



                        countSizeOfList++;
                        newGroupActivity.selectednumbers.setText(String.valueOf(countSizeOfList));


                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("friend_id", model.getUid());

                            array.put(obj);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        data = array.toString();
                        Log.d("data00", data);
                        if (onDataClickListener != null) {
                            onDataClickListener.onDataClick(data);
                        }

                    } else {
                        countSizeOfList--;
                        newGroupActivity.selectednumbers.setText(String.valueOf(countSizeOfList));

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


                        Log.d("data00", data);

                    }

                }
            });


    }

    @Override
    public int getItemCount() {
        return get_user_active_contact_list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        CheckBox checkbox;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            checkbox = itemView.findViewById(R.id.checkbox);

        }
    }

    public interface OnDataClickListener {
        void onDataClick(String data);
    }

    public void setOnDataClickListener(OnDataClickListener listener) {
        this.onDataClickListener = listener;
    }

}
