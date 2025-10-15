package com.Appzia.enclosure.Adapter;

import android.content.Context;
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
import com.Appzia.enclosure.Screens.shareExternalDataCONTACTScreen;
import com.Appzia.enclosure.Utils.Constant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import android.os.Build;
import android.os.Environment;

public class shareContactAdapter extends RecyclerView.Adapter<shareContactAdapter.myViewHolder> {

    Context mContext;
    int countSizeOfList = 0;
    JSONArray array = new JSONArray();

    ArrayList<forwardnameModel> forwardNameList = new ArrayList<>();
    String data;
    ArrayList<get_user_active_contact_list_Model> get_user_active_contact_forward_list = new ArrayList<>();
    LinearLayout dx;
    String themColor;
    shareExternalDataCONTACTScreen chatAdapter;

    public shareContactAdapter(Context mContext, ArrayList<get_user_active_contact_list_Model> get_user_active_contact_forward_list, LinearLayout dx, shareExternalDataCONTACTScreen chatAdapter) {
        this.mContext = mContext;
        this.get_user_active_contact_forward_list = get_user_active_contact_forward_list;
        this.dx = dx;
        this.chatAdapter = chatAdapter;
        
        // Initialize storage directories to match shareExternalDataScreen structure
        initializeAllStorageDirectories();
        
        // Log storage paths for debugging
        Log.d("shareContactAdapter", "Storage paths initialized: " + getAllStoragePathsInfo());
    }

    // LocalStorage path constants to match shareExternalDataScreen
    private static final String ENCLOSURE_MEDIA_BASE = "Enclosure/Media";
    private static final String ENCLOSURE_MEDIA_IMAGES = ENCLOSURE_MEDIA_BASE + "/Images";
    private static final String ENCLOSURE_MEDIA_VIDEOS = ENCLOSURE_MEDIA_BASE + "/Videos";
    private static final String ENCLOSURE_MEDIA_DOCUMENTS = ENCLOSURE_MEDIA_BASE + "/Documents";
    private static final String ENCLOSURE_MEDIA_THUMBNAILS = ENCLOSURE_MEDIA_BASE + "/Thumbnail";
    private static final String ENCLOSURE_MEDIA_CONTACTS = ENCLOSURE_MEDIA_BASE + "/Contacts";
    private static final String ENCLOSURE_MEDIA_TEXT = ENCLOSURE_MEDIA_BASE + "/Text";

    /**
     * Get localStorage path for Images - matches shareExternalDataScreen structure
     */
    public File getImagesStoragePath() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), ENCLOSURE_MEDIA_IMAGES);
        } else {
            return new File(mContext.getExternalFilesDir(null), ENCLOSURE_MEDIA_IMAGES);
        }
    }

    /**
     * Get localStorage path for Videos - matches shareExternalDataScreen structure
     */
    public File getVideosStoragePath() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), ENCLOSURE_MEDIA_VIDEOS);
        } else {
            return new File(mContext.getExternalFilesDir(null), ENCLOSURE_MEDIA_VIDEOS);
        }
    }

    /**
     * Get localStorage path for Documents - matches shareExternalDataScreen structure
     */
    public File getDocumentsStoragePath() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), ENCLOSURE_MEDIA_DOCUMENTS);
        } else {
            return new File(mContext.getExternalFilesDir(null), ENCLOSURE_MEDIA_DOCUMENTS);
        }
    }

    /**
     * Get localStorage path for Thumbnails - matches shareExternalDataScreen structure
     */
    public File getThumbnailsStoragePath() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ENCLOSURE_MEDIA_THUMBNAILS);
        } else {
            return new File(mContext.getExternalFilesDir(null), ENCLOSURE_MEDIA_THUMBNAILS);
        }
    }

    /**
     * Get localStorage path for Contacts - matches shareExternalDataScreen structure
     */
    public File getContactsStoragePath() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), ENCLOSURE_MEDIA_CONTACTS);
        } else {
            return new File(mContext.getExternalFilesDir(null), ENCLOSURE_MEDIA_CONTACTS);
        }
    }

    /**
     * Get localStorage path for Text files - matches shareExternalDataScreen structure
     */
    public File getTextStoragePath() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), ENCLOSURE_MEDIA_TEXT);
        } else {
            return new File(mContext.getExternalFilesDir(null), ENCLOSURE_MEDIA_TEXT);
        }
    }

    /**
     * Ensure storage directory exists and create if necessary
     */
    public void ensureStorageDirectoryExists(File storageDir) {
        if (!storageDir.exists()) {
            if (storageDir.mkdirs()) {
                Log.d("shareContactAdapter", "Created storage directory: " + storageDir.getAbsolutePath());
            } else {
                Log.e("shareContactAdapter", "Failed to create storage directory: " + storageDir.getAbsolutePath());
            }
        }
    }

    /**
     * Get absolute path string for a storage directory
     */
    public String getStoragePathString(File storageDir) {
        ensureStorageDirectoryExists(storageDir);
        return storageDir.getAbsolutePath();
    }

    /**
     * Get base media directory path
     */
    public File getBaseMediaDirectory() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), ENCLOSURE_MEDIA_BASE);
        } else {
            return new File(mContext.getExternalFilesDir(null), ENCLOSURE_MEDIA_BASE);
        }
    }

    /**
     * Initialize all storage directories to ensure they exist
     */
    public void initializeAllStorageDirectories() {
        Log.d("shareContactAdapter", "Initializing all storage directories");
        
        // Ensure base media directory exists
        File baseDir = getBaseMediaDirectory();
        ensureStorageDirectoryExists(baseDir);
        
        // Ensure all subdirectories exist
        ensureStorageDirectoryExists(getImagesStoragePath());
        ensureStorageDirectoryExists(getVideosStoragePath());
        ensureStorageDirectoryExists(getDocumentsStoragePath());
        ensureStorageDirectoryExists(getThumbnailsStoragePath());
        ensureStorageDirectoryExists(getContactsStoragePath());
        ensureStorageDirectoryExists(getTextStoragePath());
        
        Log.d("shareContactAdapter", "All storage directories initialized");
    }

    /**
     * Get all storage paths as a formatted string for debugging
     */
    public String getAllStoragePathsInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Storage Paths:\n");
        info.append("Images: ").append(getStoragePathString(getImagesStoragePath())).append("\n");
        info.append("Videos: ").append(getStoragePathString(getVideosStoragePath())).append("\n");
        info.append("Documents: ").append(getStoragePathString(getDocumentsStoragePath())).append("\n");
        info.append("Thumbnails: ").append(getStoragePathString(getThumbnailsStoragePath())).append("\n");
        info.append("Contacts: ").append(getStoragePathString(getContactsStoragePath())).append("\n");
        info.append("Text: ").append(getStoragePathString(getTextStoragePath()));
        return info.toString();
    }

    @NonNull
    @Override
    public shareContactAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.share_contact_layout, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull shareContactAdapter.myViewHolder holder, int position) {

        final get_user_active_contact_list_Model model = get_user_active_contact_forward_list.get(position);

        holder.contact1text.setText(model.getFull_name());

        if (holder.contact1text.getText().toString().length() > 20) {
            String truncatedText = holder.contact1text.getText().toString().substring(0, 20) + "..."; // Add dots at the end
            holder.contact1text.setText(truncatedText);
        } else {
            holder.contact1text.setText(holder.contact1text.getText().toString());
        }
        holder.captiontext.setText(model.getCaption());

        // Always reset checkbox to unchecked state when binding
        holder.checkbox.setChecked(false);


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

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
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

    // Method to reset all checkboxes and clear selection data
    public void resetAllCheckboxes() {
        Log.d("shareContactAdapter", "Resetting all checkboxes and clearing selection data");
        
        // Clear all selection data
        forwardNameList.clear();
        array = new JSONArray();
        data = "";
        
        // Notify the adapter to refresh all views
        notifyDataSetChanged();
        
        // Collapse the selection view if it's visible
        if (dx != null && dx.getVisibility() == View.VISIBLE) {
            collapse(dx);
        }
        
        // Update the forward name adapter with empty list
        if (chatAdapter != null) {
            chatAdapter.setforwardNameAdapter(forwardNameList);
        }
        
        Log.d("shareContactAdapter", "All checkboxes reset and data cleared");
    }

    // Method to get current selection count
    public int getSelectedCount() {
        return array.length();
    }

    // Method to get current selection data
    public String getSelectionData() {
        return data;
    }

    // Method to check if any item is selected
    public boolean hasSelection() {
        return array.length() > 0;
    }

    // Method to refresh adapter with new data (useful when coming back to contact selection)
    public void refreshWithNewData(ArrayList<get_user_active_contact_list_Model> newContactList) {
        Log.d("shareContactAdapter", "Refreshing adapter with new data, clearing previous selections");
        
        // Clear all previous selections
        resetAllCheckboxes();
        
        // Update the contact list
        this.get_user_active_contact_forward_list = newContactList;
        
        // Notify adapter of data change
        notifyDataSetChanged();
        
        Log.d("shareContactAdapter", "Adapter refreshed with " + newContactList.size() + " contacts");
    }

    // Method to clear specific checkbox by position
    public void clearCheckboxAtPosition(int position) {
        if (position >= 0 && position < get_user_active_contact_forward_list.size()) {
            // Find and remove the item from array if it exists
            int itemToRemoveId = Integer.parseInt(get_user_active_contact_forward_list.get(position).getUid());
            for (int i = 0; i < array.length(); i++) {
                try {
                    if (array.getJSONObject(i).optInt("friend_id") == itemToRemoveId) {
                        array.remove(i);
                        break;
                    }
                } catch (JSONException e) {
                    Log.e("shareContactAdapter", "Error removing item: " + e.getMessage());
                }
            }
            
            // Update data and forward list
            data = array.toString();
            updateForwardList();
            
            // Notify item changed to refresh the checkbox
            notifyItemChanged(position);
        }
    }

    // Helper method to update forward list
    private void updateForwardList() {
        forwardNameList.clear();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");
                String friend_id = obj.getString("friend_id");
                String f_token = obj.getString("f_token");
                String device_type = obj.getString("device_type");
                forwardNameList.add(new forwardnameModel(name, friend_id, f_token, device_type));
            } catch (JSONException e) {
                Log.e("shareContactAdapter", "Error updating forward list: " + e.getMessage());
            }
        }
        
        if (chatAdapter != null) {
            chatAdapter.setforwardNameAdapter(forwardNameList);
        }
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
